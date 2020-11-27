//@formatter:off
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
//@formatter:on
package de.calamanari.pk.sequenceblock;

import static de.calamanari.pk.util.LambdaSupportLoggerProxy.defer;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.calamanari.pk.util.LambdaSupportLoggerProxy;

/**
 * The sequence block cache manages an arbitrary number of sequences using the SEQUENCE BLOCK pattern.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 * 
 */
public class SequenceBlockCache {

    private static final Logger LOGGER = LambdaSupportLoggerProxy.wrap(LoggerFactory.getLogger(SequenceBlockCache.class));

    /**
     * log message used multiple times, 1 argument: thread-id
     */
    private static final String MSG_TX_BEGIN = "Thread @{}: Transaction.begin()";

    /**
     * log message used multiple times, 1 argument: thread-id
     */
    private static final String MSG_TX_COMMIT = "Thread @{}: Transaction.commit()";

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
    private static final String SQL_UPDATE_SEQ_ID = "update SEQUENCE_TABLE set SEQ_ID=?" + " where SEQ_NAME=? and SEQ_ID=?";

    /**
     * Maps sequence names to sequence blocks
     */
    private final Map<String, SequenceBlock> cachedSequenceBlocksMap = new ConcurrentHashMap<>();

    /**
     * Maps sequence names to the locks for synchronization
     */
    private final ConcurrentHashMap<String, Object> sequenceLockMap = new ConcurrentHashMap<>();

    /**
     * data source for accessing database via JDBC
     */
    private final DataSource dataSource;

    /**
     * Creates new cache using the underlying database
     * 
     * @param dataSource underlying data source
     */
    public SequenceBlockCache(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * We want to do locking (synchronization) for single sequences only (not globally).<br>
     * This method returns the Object we can synchronize on for the particular sequence.
     * 
     * @param sequenceName name of sequence
     * @return lock object for sychronization
     */
    private Object getSequenceLockObject(String sequenceName) {
        Object lockObject = sequenceLockMap.get(sequenceName);
        if (lockObject == null) {
            lockObject = new Object();
            Object prevLockObject = sequenceLockMap.putIfAbsent(sequenceName, lockObject);
            if (prevLockObject != null) {
                lockObject = prevLockObject;
            }
        }
        return lockObject;
    }

    /**
     * Returns the current item, rather for internal use, do NEVER modify/save !
     * 
     * @param sequenceName (PK)
     * @return current value of sequence or null to indicate a missing sequence
     */
    private Long getCurrentSequenceNumberFromDb(String sequenceName) {
        LOGGER.debug("Thread @{}: {}.getCurrentSequenceNumberFromDb('{}') called.", defer(() -> Integer.toHexString(Thread.currentThread().hashCode())),
                this.getClass().getSimpleName(), sequenceName);
        Long res = null;

        // START NEW TRANSACTION --->
        LOGGER.debug(MSG_TX_BEGIN, defer(() -> Integer.toHexString(Thread.currentThread().hashCode())));
        try (Connection con = dataSource.getConnection(); PreparedStatement stmt = con.prepareStatement(SQL_SELECT_CURRENT_SEQ_ID)) {
            stmt.setString(1, sequenceName);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    res = rs.getLong(1);
                }
            }

            // <--- END OF TRANSACTION (implicit, auto-commit)
            LOGGER.debug(MSG_TX_COMMIT, defer(() -> Integer.toHexString(Thread.currentThread().hashCode())));
        }
        catch (SQLException ex) {
            throw new SequenceBlockManagementException(
                    String.format("Database error while retrieving current sequence number for sequenceName=%s", sequenceName), ex);
        }
        return res;
    }

    /**
     * Creates the new sequence and returns the current value of the sequence afterwards.
     * 
     * @param sequenceName (PK)
     * @return generic Value of type SequenceValueItem
     */
    private Long createAndGetCurrentSequenceValue(String sequenceName) {
        LOGGER.debug("Thread @{}: {}.createAndGetCurrentSequenceValue('{}') called.", defer(() -> Integer.toHexString(Thread.currentThread().hashCode())),
                this.getClass().getSimpleName(), sequenceName);

        // START NEW TRANSACTION --->
        LOGGER.debug(MSG_TX_BEGIN, defer(() -> Integer.toHexString(Thread.currentThread().hashCode())));

        try (Connection con = dataSource.getConnection(); PreparedStatement stmt = con.prepareStatement(SQL_INSERT_NEW_SEQUENCE)) {
            stmt.setString(1, sequenceName);
            stmt.executeUpdate();

            // <--- END OF TRANSACTION (implicit, auto-commit)
            LOGGER.debug(MSG_TX_COMMIT, defer(() -> Integer.toHexString(Thread.currentThread().hashCode())));
        }
        catch (SQLException ex) {
            // probably a race condition, silently ignored
            LOGGER.warn("Thread @{}: Could not create new sequence record ('{}').", defer(() -> Integer.toHexString(Thread.currentThread().hashCode())),
                    sequenceName, ex);
        }

        // call other service to return the result of our insert or concurrent
        // insert
        return getCurrentSequenceNumberFromDb(sequenceName);
    }

    /**
     * Returns a new sequence block or null if not successful (concurrent increment detected)
     * 
     * @param sequenceName name of sequence in sequence table, MUST EXIST in database at this time!
     * @param lastUsedValue the last used key from that sequence
     * @param blockIncrement optionally specify a block increment (number of keys to be cached), equal or greater 1
     * @return new sequence block or null to indicate that caller must try again
     */
    private SequenceBlock reserveNextSequenceBlock(String sequenceName, long lastUsedValue, int blockIncrement) {
        LOGGER.debug("Thread @{}: {}.reserveNextSequenceBlock('{}', lastUsedValue={}, blockIncrement={}) called.",
                defer(() -> Integer.toHexString(Thread.currentThread().hashCode())), this.getClass().getSimpleName(), sequenceName, lastUsedValue,
                blockIncrement);

        SequenceBlock res = null;
        if (blockIncrement < 1) {
            blockIncrement = DEFAULT_SEQUENCE_BLOCK_INCREMENT;
        }
        long startOfBlock = lastUsedValue + 1;
        long endOfBlock = startOfBlock + blockIncrement;
        long updCount = 0;

        // START NEW TRANSACTION --->
        LOGGER.debug(MSG_TX_BEGIN, defer(() -> Integer.toHexString(Thread.currentThread().hashCode())));
        try (Connection con = dataSource.getConnection(); PreparedStatement stmt = con.prepareStatement(SQL_UPDATE_SEQ_ID)) {

            stmt.setLong(1, endOfBlock - 1);
            stmt.setString(2, sequenceName);
            stmt.setLong(3, lastUsedValue);

            updCount = stmt.executeUpdate();

            // <--- END OF TRANSACTION (implicit, auto-commit)
            LOGGER.debug(MSG_TX_COMMIT, defer(() -> Integer.toHexString(Thread.currentThread().hashCode())));
        }
        catch (SQLException ex) {
            throw new SequenceBlockManagementException(
                    String.format("Thread @%s: Database error while reserving next sequence block: sequenceName=%s, lastUsedValue=%d, blockIncrement=%d",
                            Integer.toHexString(Thread.currentThread().hashCode()), sequenceName, lastUsedValue, blockIncrement),
                    ex);
        }

        if (updCount == 1) {
            res = new SequenceBlock(startOfBlock, endOfBlock);
        }
        return res;
    }

    /**
     * This method reserves a new sequence block (involves DB-access).
     * 
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
                LOGGER.info("Thread @{}: Creating new sequence record for sequenceName='{}'.",
                        defer(() -> Integer.toHexString(Thread.currentThread().hashCode())), sequenceName);
                currentValue = createAndGetCurrentSequenceValue(sequenceName);
                if (currentValue == null) {
                    throw new SequenceBlockManagementException(String.format(
                            "Thread @%s: The sequence could neither be created nor updated (unknown server error): sequenceName=%s, blockIncrement=%d",
                            Integer.toHexString(Thread.currentThread().hashCode()), sequenceName, blockIncrement));
                }
            }
            long lastUsedValue = currentValue;
            if (lastUsedValue < 0) {
                throw new SequenceBlockManagementException(
                        String.format("Thread @%s: Found negative value for sequence - check database SEQUENCE_TABLE: sequenceName=%s, blockIncrement=%d",
                                Integer.toHexString(Thread.currentThread().hashCode()), sequenceName, blockIncrement));
            }
            sequenceBlock = reserveNextSequenceBlock(sequenceName, currentValue, blockIncrement);
            if (sequenceBlock == null) {
                LOGGER.debug("Thread @{}: chosen as victim during concurrent block reservation - going to try again ...",
                        defer(() -> Integer.toHexString(Thread.currentThread().hashCode())));
            }
        }
        return sequenceBlock;
    }

    /**
     * Returns the corresponding sequence block<br>
     * 
     * @param sequenceName name of sequence in sequence table, missing sequences will be auto-created
     * @param blockIncrement optionally specify a block increment (number of keys to be cached), equal or greater 1
     * @return sequence block, never null (instead throws RuntimeException)
     */
    private SequenceBlock getSequenceBlock(String sequenceName, int blockIncrement) {
        LOGGER.debug("Thread @{}: {}.getSequenceBlock('{}', blockIncrement={}) called.", defer(() -> Integer.toHexString(Thread.currentThread().hashCode())),
                this.getClass().getSimpleName(), sequenceName, blockIncrement);
        SequenceBlock sequenceBlock = cachedSequenceBlocksMap.get(sequenceName);
        if (sequenceBlock == null || sequenceBlock.isExhausted()) {
            // do not synchronize globally but per sequence!
            synchronized (getSequenceLockObject(sequenceName)) {
                LOGGER.debug("Thread @{}: Performing lookup in sequence block cache", defer(() -> Integer.toHexString(Thread.currentThread().hashCode())));
                sequenceBlock = cachedSequenceBlocksMap.get(sequenceName);
                if (sequenceBlock == null || sequenceBlock.isExhausted()) {

                    LOGGER.debug("Thread @{}: New sequence block required!", defer(() -> Integer.toHexString(Thread.currentThread().hashCode())));
                    sequenceBlock = reserveNextSequenceBlock(sequenceName, blockIncrement);

                    cachedSequenceBlocksMap.put(sequenceName, sequenceBlock);
                }
                else {
                    LOGGER.debug("Thread @{}: Found existing sequence block, no need for db-access.",
                            defer(() -> Integer.toHexString(Thread.currentThread().hashCode())));
                }
            }
        }
        return sequenceBlock;
    }

    /**
     * Returns the next available sequence id. Sequences start at 1. Non-existing sequences will be auto-created.
     * 
     * @param sequenceName name of sequence in sequence table
     * @param blockIncrement specify a block increment (number of keys to be cached), equal or greater 1, less than 1 means default which is
     *            {@link #DEFAULT_SEQUENCE_BLOCK_INCREMENT}
     * @return sequence value as Long
     */
    public long getNextId(String sequenceName, int blockIncrement) {
        LOGGER.debug("Thread @{}: {}.getNextId('{}', blockIncrement={}) called.", defer(() -> Integer.toHexString(Thread.currentThread().hashCode())),
                this.getClass().getSimpleName(), sequenceName, blockIncrement);
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
     * 
     * @param sequenceName name of sequence in sequence table
     * @return sequence value as Long
     */
    public long getNextId(String sequenceName) {
        LOGGER.debug("Thread @{}: {}.getNextId('{}') called.", defer(() -> Integer.toHexString(Thread.currentThread().hashCode())),
                this.getClass().getSimpleName(), sequenceName);
        return getNextId(sequenceName, DEFAULT_SEQUENCE_BLOCK_INCREMENT);
    }

}
