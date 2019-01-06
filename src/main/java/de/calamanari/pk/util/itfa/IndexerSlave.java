//@formatter:off
/*
 * Indexer Slave
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
package de.calamanari.pk.util.itfa;

import static de.calamanari.pk.util.CharsetUtils.CARRIAGE_RETURN_CODE;
import static de.calamanari.pk.util.CharsetUtils.LINE_BREAK_CODE;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.calamanari.pk.util.CharsetUtils;

/**
 * Indexer Slave<br>
 * Indexing is implemented using the MASTER-SLAVE pattern. While the master (see {@link IndexerMaster}) splits the work into parts the slaves perform the
 * indexing. Finally the master collects the partial results and sets up the full index.<br>
 * Slaves can be recycled.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
final class IndexerSlave implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(IndexerSlave.class);

    /**
     * Number of index entries this slave may use for characters
     */
    int maxNumberOfCharEntries;

    /**
     * Number of index entries this slave may use for lines
     */
    int maxNumberOfLineEntries;

    /**
     * Position within the partition where the slave shall start indexing
     */
    int startIdx;

    /**
     * Size of the slaves workspace within the partition
     */
    int subPartitionSize;

    /**
     * Current partition
     */
    char[] partition;

    /**
     * Result of this slave
     */
    final AtomicReference<IndexerSlaveResult> result = new AtomicReference<>();

    /**
     * lookup to re-calculate the length in bytes of a particular character
     */
    private final byte[] charLengthLookup;

    /**
     * Errors will be memorized here to be propagated to/by the master
     */
    final AtomicReference<Throwable> propagateError = new AtomicReference<>();

    /**
     * The countdown latch is a slave-to-master signal, the master waits until all slaves have completed
     */
    CountDownLatch latch;

    /**
     * Creates new slave
     * 
     * @param charLengthLookup character length lookup used while indexing
     */
    public IndexerSlave(byte[] charLengthLookup) {
        this.charLengthLookup = charLengthLookup;
    }

    @Override
    public void run() {

        try {

            // copy instructions from master (config) and prepare state

            // immutable settings
            SlaveConfig conf = createSlaveConfig();

            // processing state
            SlaveState state = createSlaveState(conf);

            for (int i = 0; i < conf.currentSubPartitionSize; i++) {
                processNextPartitionCharacter(conf, state, i);
            }

            if (!state.lastCharWasIndexed && state.charEntryCount < conf.currentMaxNumberOfCharEntries
                    && (state.read < CharsetUtils.MIN_HIGH_SURROGATE_CODE || state.read > CharsetUtils.MAX_SURROGATE_CODE)) {
                state.charEntryCount++;
                state.charIndex[state.charEntryCount - 1] = new long[] { state.numberOfCharactersRead - 1, state.beforeLastBytePos };
            }
            if (state.lastCharWasCR) {
                state.numberOfLinesRead++;
                if (state.lineEntryCount < conf.currentMaxNumberOfLineEntries) {
                    state.lineEntryCount++;
                    state.lineIndex[state.lineEntryCount - 1] = new long[] { state.numberOfLinesRead, state.bytePosAfterCR };
                }
            }

            // slave results for master
            IndexerSlaveResult localResult = new IndexerSlaveResult(state.charIndex, state.lineIndex, state.currentBytePos, state.numberOfCharactersRead,
                    state.numberOfLinesRead, state.charEntryCount, state.lineEntryCount);
            this.result.set(localResult);
        }
        catch (RuntimeException ex) {
            LOGGER.debug("Unexpected error while indexing", ex);
            this.propagateError.set(ex);
        }
        finally {
            this.latch.countDown();
        }

    }

    private void processNextPartitionCharacter(SlaveConfig conf, SlaveState state, int idx) {
        state.read = conf.currentPartition[conf.currentStartIdx + idx];

        // increase the byte position using reverse lookup
        state.currentBytePos = state.currentBytePos + charLengthLookup[state.read];

        state.lastCharWasIndexed = false;

        if (state.read == LINE_BREAK_CODE || state.lastCharWasCR) {
            updateLineIndex(conf, state);
        }
        if (state.read == CARRIAGE_RETURN_CODE) {
            state.lastCharWasCR = true;
            state.bytePosAfterCR = state.currentBytePos;
        }
        else {
            state.lastCharWasCR = false;
        }
        updateCharacterIndex(conf, state);
        state.numberOfCharactersRead++;
        state.beforeLastBytePos = state.lastBytePos;
        state.lastBytePos = state.currentBytePos;
    }

    private void updateLineIndex(SlaveConfig conf, SlaveState state) {
        // new Line
        state.numberOfLinesRead++;
        long newLineStart = state.currentBytePos;
        long newLineStartCharNumber = state.numberOfCharactersRead;

        if (state.read != LINE_BREAK_CODE) {
            // this position WAS already the line start
            newLineStart = state.bytePosAfterCR;
            newLineStartCharNumber--;
        }
        if (conf.currentMaxNumberOfLineEntries > state.lineEntryCount
                && (newLineStartCharNumber == 0 || (newLineStartCharNumber - state.lastIndexedLineStartCharNumber >= conf.lineEntryDistance)
                        || (state.backupLineEntries > 0 && (newLineStartCharNumber - state.lastIndexedLineStartCharNumber >= conf.lineEntryDistance / 2)))) {
            if (newLineStartCharNumber - state.lastIndexedLineStartCharNumber < conf.lineEntryDistance) {
                state.backupLineEntries--;
            }
            state.lineEntryCount++;
            state.lineIndex[state.lineEntryCount - 1] = new long[] { state.numberOfLinesRead, newLineStart };
            // lastLineStartPos = newLineStart;
            state.lastIndexedLineStartCharNumber = newLineStartCharNumber;
        }
    }

    private void updateCharacterIndex(SlaveConfig conf, SlaveState state) {
        // never create entries for low surrogate characters (56320-57343), because it is impossible to
        // directly read (decode) them without reading the mandatory high surrogate before;
        // low surrogates don't have any absolute position
        if ((state.read < CharsetUtils.MIN_HIGH_SURROGATE_CODE || state.read > CharsetUtils.MAX_SURROGATE_CODE)
                && conf.currentMaxNumberOfCharEntries > state.charEntryCount
                && (state.numberOfCharactersRead == 0 || state.numberOfCharactersRead - state.lastIndexedCharNumber >= conf.charEntryDistance
                        || (state.backupCharEntries > 0 && state.numberOfCharactersRead - state.lastIndexedCharNumber >= conf.charEntryDistance / 2))) {
            if (state.numberOfCharactersRead - state.lastIndexedCharNumber < conf.charEntryDistance) {
                state.backupCharEntries--;
            }
            state.charEntryCount++;
            state.charIndex[state.charEntryCount - 1] = new long[] { state.numberOfCharactersRead, state.lastBytePos };
            // lastCharPos = lastBytePos;
            state.lastCharWasIndexed = true;
            state.lastIndexedCharNumber = state.numberOfCharactersRead;
        }
    }

    private SlaveState createSlaveState(SlaveConfig conf) {
        SlaveState state = new SlaveState();

        state.backupCharEntries = conf.currentMaxNumberOfCharEntries - conf.availableCharEntries;
        state.backupLineEntries = conf.currentMaxNumberOfLineEntries - conf.availableLineEntries;

        // Line breaks may consist of a carriage return optionally followed by a line feed.
        // To handle both options, a flag helps.
        state.lastCharWasCR = false;
        state.bytePosAfterCR = 0L;
        state.lastCharWasIndexed = false;

        state.charEntryCount = 0;
        state.lineEntryCount = 0;
        state.numberOfCharactersRead = 0L;
        state.numberOfLinesRead = 0L;

        state.currentBytePos = 0L;
        state.lastBytePos = 0L;
        state.beforeLastBytePos = 0L;

        state.lastIndexedLineStartCharNumber = 0L;

        state.lastIndexedCharNumber = 0L;

        state.read = 0;

        state.charIndex = new long[conf.currentMaxNumberOfCharEntries][];
        state.lineIndex = new long[conf.currentMaxNumberOfLineEntries][];
        return state;
    }

    private SlaveConfig createSlaveConfig() {
        SlaveConfig conf = new SlaveConfig();
        conf.currentMaxNumberOfCharEntries = (maxNumberOfCharEntries <= subPartitionSize ? maxNumberOfCharEntries : subPartitionSize);
        conf.currentMaxNumberOfLineEntries = (maxNumberOfLineEntries <= subPartitionSize ? maxNumberOfLineEntries : subPartitionSize);
        conf.currentStartIdx = startIdx;
        conf.currentSubPartitionSize = subPartitionSize;
        conf.currentPartition = partition;
        conf.charEntryDistance = conf.currentMaxNumberOfCharEntries == 0 ? 0
                : (int) Math.floor((double) conf.currentSubPartitionSize / (double) conf.currentMaxNumberOfCharEntries);
        conf.lineEntryDistance = conf.currentMaxNumberOfLineEntries == 0 ? 0
                : (int) Math.floor((double) conf.currentSubPartitionSize / (double) conf.currentMaxNumberOfLineEntries);
        conf.availableCharEntries = conf.charEntryDistance == 0 ? 0 : conf.currentSubPartitionSize / conf.charEntryDistance;
        conf.availableLineEntries = conf.lineEntryDistance == 0 ? 0 : conf.currentSubPartitionSize / conf.lineEntryDistance;
        return conf;
    }

    private static class SlaveConfig {

        int currentMaxNumberOfCharEntries;
        int currentMaxNumberOfLineEntries;
        int currentStartIdx;
        int currentSubPartitionSize;
        char[] currentPartition;
        int charEntryDistance;
        int lineEntryDistance;
        int availableCharEntries;
        int availableLineEntries;

    }

    private static class SlaveState {
        int backupCharEntries;
        int backupLineEntries;
        boolean lastCharWasCR;
        long bytePosAfterCR;
        boolean lastCharWasIndexed;
        int charEntryCount;
        int lineEntryCount;
        long numberOfCharactersRead;
        long numberOfLinesRead;
        long currentBytePos;
        long lastBytePos;
        long beforeLastBytePos;
        long lastIndexedLineStartCharNumber;
        long lastIndexedCharNumber;
        int read;
        long[][] charIndex;
        long[][] lineIndex;
    }

}