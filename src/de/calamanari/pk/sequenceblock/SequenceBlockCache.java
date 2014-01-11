/*
 * Sequence block cache
 * Code-Beispiel zum Buch Patterns Kompakt, Verlag Springer Vieweg
 * Copyright 2014 Karl Eilebrecht
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package de.calamanari.pk.sequenceblock;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.DataSource;

/**
 * The sequence block cache manages an arbitrary number of sequences using the SEQUENCE BLOCK pattern.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 * 
 */
public class SequenceBlockCache {

    /**
     * logger
     */
    public static final Logger LOGGER = Logger.getLogger(SequenceBlockCache.class.getName());

    /**
     * block size (number of keys to cache before accessing database table again)<br>
     * <i>value:</i> {@value}
     */
    public static final int DEFAULT_SEQUENCE_BLOCK_INCREMENT = 10;

    /**
     * prepared statement to query the current sequence-id of a sequence:<br>
     * <code>{@value}</code>
     */
    private static final String SQL_SELECT_CURRENT_SEQ_ID = "select SEQ_ID from SEQUENCE_TABLE where SEQ_NAME=?";

    /**
     * prepared statement to store a new sequence item:<br>
     * <code>{@value}</code>
     */
    private static final String SQL_INSERT_NEW_SEQUENCE = "insert into SEQUENCE_TABLE (SEQ_NAME, SEQ_ID) values (?, 0)";

    /**
     * prepared statement to update (increase) the sequence-id of a sequence:<br>
     * <code>{@value}</code>
     */
    private static final String SQL_UPDATE_SEQ_ID = "update SEQUENCE_TABLE set SEQ_ID=?"
            + " where SEQ_NAME=? and SEQ_ID=?";

    /**
     * Maps sequence names to sequence blocks
     */
    private final Map<String, SequenceBlock> cachedSequenceBlocksMap = new ConcurrentHashMap<>();

    /**
     * Maps sequence names to the locks for synchronization
     */
    private final ConcurrentHashMap<String, Integer> sequenceLockMap = new ConcurrentHashMap<>();

    /**
     * data source for accessing database via JDBC
     */
    private final DataSource dataSource;

    /**
     * Creates new cache using the underlying database
     * @param dataSource underlying data source
     */
    public SequenceBlockCache(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * We want to do locking (synchronization) for single sequences only (not globally).<br>
     * This method returns the Object we can synchronize on for the particular sequence.
     * @param sequenceName name of sequence
     * @return lock object for sychronization
     */
    private Object getSequenceLockObject(String sequenceName) {
        Integer lockObject = sequenceLockMap.get(sequenceName);
        if (lockObject == null) {
            lockObject = new Integer(0);
            Integer prevLockObject = sequenceLockMap.putIfAbsent(sequenceName, lockObject);
            if (prevLockObject != null) {
                lockObject = prevLockObject;
            }
        }
        return lockObject;
    }

    /**
     * Returns the current item, rather for internal use, do NEVER modify/save !
     * @param sequenceName (PK)
     * @return current value of sequence or null to indicate a missing sequence
     */
    private Long getCurrentSequenceNumberFromDb(String sequenceName) {
        LOGGER.fine("Thread @" + Integer.toHexString(Thread.currentThread().hashCode()) + ": "
                + this.getClass().getSimpleName() + ".getCurrentSequenceNumberFromDb('" + sequenceName + "') called.");
        Long res = null;

        // START NEW TRANSACTION --->
        LOGGER.fine("Thread @" + Integer.toHexString(Thread.currentThread().hashCode()) + ": Transaction.begin()");
        try (Connection con = dataSource.getConnection();
                PreparedStatement stmt = con.prepareStatement(SQL_SELECT_CURRENT_SEQ_ID)) {
            stmt.setString(1, sequenceName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    res = rs.getLong(1);
                }
            }

            // <--- END OF TRANSACTION (implicit, auto-commit)
            LOGGER.fine("Thread @" + Integer.toHexString(Thread.currentThread().hashCode()) + ": Transaction.commit()");
        }
        catch (SQLException ex) {
            throw new RuntimeException(ex);
        }
        return res;
    }

    /**
     * Creates the new sequence and returns the current value of the sequence afterwards.
     * @param sequenceName (PK)
     * @return generic Value of type SequenceValueItem
     */
    private Long createAndGetCurrentSequenceValue(String sequenceName) {
        LOGGER.fine("Thread @" + Integer.toHexString(Thread.currentThread().hashCode()) + ": "
                + this.getClass().getSimpleName() + ".createAndGetCurrentSequenceValue('" + sequenceName + "') called.");

        // START NEW TRANSACTION --->
        LOGGER.fine("Thread @" + Integer.toHexString(Thread.currentThread().hashCode()) + ": Transaction.begin()");

        try (Connection con = dataSource.getConnection();
                PreparedStatement stmt = con.prepareStatement(SQL_INSERT_NEW_SEQUENCE)) {
            stmt.setString(1, sequenceName);
            stmt.executeUpdate();

            // <--- END OF TRANSACTION (implicit, auto-commit)
            LOGGER.fine("Thread @" + Integer.toHexString(Thread.currentThread().hashCode()) + ": Transaction.commit()");
        }
        catch (SQLException ex) {
            // probably a race condition, silently ignored
            LOGGER.log(Level.WARNING, "Thread @" + Integer.toHexString(Thread.currentThread().hashCode())
                    + ": Could not create new sequence record ('" + sequenceName + "').", ex);
        }

        // call other service to return the result of our insert or concurrent
        // insert
        return getCurrentSequenceNumberFromDb(sequenceName);
    }

    /**
     * Returns a new sequence block or null if not successful (concurrent increment detected)
     * @param sequenceName name of sequence in sequence table, MUST EXIST in database at this time!
     * @param lastUsedValue the last used key from that sequence
     * @param blockIncrement optionally specify a block increment (number of keys to be cached), equal or greater 1
     * @return new sequence block or null to indicate that caller must try again
     */
    private SequenceBlock reserveNextSequenceBlock(String sequenceName, long lastUsedValue, int blockIncrement) {
        LOGGER.fine("Thread @" + Integer.toHexString(Thread.currentThread().hashCode()) + ": "
                + this.getClass().getSimpleName() + ".reserveNextSequenceBlock('" + sequenceName + "', lastUsedValue="
                + lastUsedValue + ", blockIncrement=" + blockIncrement + ") called.");

        SequenceBlock res = null;
        if (blockIncrement < 1) {
            blockIncrement = DEFAULT_SEQUENCE_BLOCK_INCREMENT;
        }
        long startOfBlock = lastUsedValue + 1;
        long endOfBlock = startOfBlock + blockIncrement;
        long updCount = 0;

        // START NEW TRANSACTION --->
        LOGGER.fine("Thread @" + Integer.toHexString(Thread.currentThread().hashCode()) + ": Transaction.begin()");
        try (Connection con = dataSource.getConnection();
                PreparedStatement stmt = con.prepareStatement(SQL_UPDATE_SEQ_ID)) {

            stmt.setLong(1, endOfBlock - 1);
            stmt.setString(2, sequenceName);
            stmt.setLong(3, lastUsedValue);

            updCount = stmt.executeUpdate();

            // <--- END OF TRANSACTION (implicit, auto-commit)
            LOGGER.fine("Thread @" + Integer.toHexString(Thread.currentThread().hashCode()) + ": Transaction.commit()");
        }
        catch (SQLException ex) {
            throw new RuntimeException(ex);
        }

        if (updCount == 1) {
            res = new SequenceBlock(startOfBlock, endOfBlock);
        }
        return res;
    }

    /**
     * This method reserves a new sequence block (involves DB-access).
     * @param sequenceName name of sequence in sequence table, missing sequences will be auto-created
     * @param blockIncrement optionally specify a block increment (number of keys to be cached), equal or greater 1
     * @return newly reserved block
     */
    private SequenceBlock reserveNextSequenceBlock(String sequenceName, int blockIncrement) {
        SequenceBlock sequenceBlock = null;

        // retry strategy for concurrent access by multiple servers
        // get current state, try reserve, get current state, try
        // reserve ...
        // This retry-strategy could be improved :-)
        while (sequenceBlock == null) {
            Long currentValue = getCurrentSequenceNumberFromDb(sequenceName);
            if (currentValue == null) {
                // As many implementations we automatically create a
                // missing sequence, however this is HIGHLY QUESTIONABLE
                // since typos might lead to heavy trouble ...
                LOGGER.info("Thread @" + Integer.toHexString(Thread.currentThread().hashCode())
                        + ": Creating new sequence record for sequenceName='" + sequenceName + "'.");
                currentValue = createAndGetCurrentSequenceValue(sequenceName);
                if (currentValue == null) {
                    throw new RuntimeException("Thread @" + Integer.toHexString(Thread.currentThread().hashCode())
                            + ": The sequence '" + sequenceName
                            + "' could neither be created nor updated (unknown server error).");
                }
            }
            long lastUsedValue = currentValue;
            if (lastUsedValue < 0) {
                throw new IllegalStateException("Thread @" + Integer.toHexString(Thread.currentThread().hashCode())
                        + ": Found negative value for sequence '" + sequenceName + "' - check database SEQUENCE_TABLE!");
            }
            sequenceBlock = reserveNextSequenceBlock(sequenceName, currentValue, blockIncrement);
            if (sequenceBlock == null) {
                LOGGER.fine("Thread @" + Integer.toHexString(Thread.currentThread().hashCode())
                        + ": chosen as victim during concurrent block reservation - going to try again ...");
            }
        }
        return sequenceBlock;
    }

    /**
     * Returns the corresponding sequence block<br>
     * @param sequenceName name of sequence in sequence table, missing sequences will be auto-created
     * @param blockIncrement optionally specify a block increment (number of keys to be cached), equal or greater 1
     * @return sequence block, never null (instead throws RuntimeException)
     */
    private SequenceBlock getSequenceBlock(String sequenceName, int blockIncrement) {
        LOGGER.fine("Thread @" + Integer.toHexString(Thread.currentThread().hashCode()) + ": "
                + this.getClass().getSimpleName() + ".getSequenceBlock('" + sequenceName + "', blockIncrement="
                + blockIncrement + ") called.");
        SequenceBlock sequenceBlock = cachedSequenceBlocksMap.get(sequenceName);
        if (sequenceBlock == null || sequenceBlock.isExhausted()) {
            // do not synchronize globally but per sequence!
            synchronized (getSequenceLockObject(sequenceName)) {
                LOGGER.fine("Thread @" + Integer.toHexString(Thread.currentThread().hashCode())
                        + ": Performing lookup in sequence block cache");
                sequenceBlock = cachedSequenceBlocksMap.get(sequenceName);
                if (sequenceBlock == null || sequenceBlock.isExhausted()) {

                    LOGGER.fine("Thread @" + Integer.toHexString(Thread.currentThread().hashCode())
                            + ": New sequence block required!");
                    sequenceBlock = reserveNextSequenceBlock(sequenceName, blockIncrement);

                    cachedSequenceBlocksMap.put(sequenceName, sequenceBlock);
                }
                else {
                    LOGGER.fine("Thread @" + Integer.toHexString(Thread.currentThread().hashCode())
                            + ": Found existing sequence block, no need for db-access.");
                }
            }
        }
        return sequenceBlock;
    }

    /**
     * Returns the next available sequence id. Sequences start at 1. Non-existing sequences will be auto-created.
     * @param sequenceName name of sequence in sequence table
     * @param blockIncrement specify a block increment (number of keys to be cached), equal or greater 1, less than 1
     *            means default which is {@link #DEFAULT_SEQUENCE_BLOCK_INCREMENT}
     * @return sequence value as Long
     */
    public long getNextId(String sequenceName, int blockIncrement) {
        LOGGER.fine("Thread @" + Integer.toHexString(Thread.currentThread().hashCode()) + ": "
                + this.getClass().getSimpleName() + ".getNextId('" + sequenceName + "', blockIncrement="
                + blockIncrement + ") called.");
        if (blockIncrement < 1) {
            blockIncrement = DEFAULT_SEQUENCE_BLOCK_INCREMENT;
        }
        long res = -1;
        while (res < 0) {
            res = getSequenceBlock(sequenceName, blockIncrement).getNextId();
        }
        return res;
    }

    /**
     * Returns the next available sequence id. Sequences start at 1. Non-existing sequences will be auto-created.<br>
     * This convenience method uses the default block increment: {@link #DEFAULT_SEQUENCE_BLOCK_INCREMENT}
     * @param sequenceName name of sequence in sequence table
     * @return sequence value as Long
     */
    public long getNextId(String sequenceName) {
        LOGGER.fine("Thread @" + Integer.toHexString(Thread.currentThread().hashCode()) + ": "
                + this.getClass().getSimpleName() + ".getNextId('" + sequenceName + "') called.");
        return getNextId(sequenceName, DEFAULT_SEQUENCE_BLOCK_INCREMENT);
    }

}
