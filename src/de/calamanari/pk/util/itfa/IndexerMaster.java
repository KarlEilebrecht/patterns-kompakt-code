/*
 * Indexer Master
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
package de.calamanari.pk.util.itfa;

import static de.calamanari.pk.util.CharsetUtils.CARRIAGE_RETURN_CODE;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import de.calamanari.pk.util.MiscUtils;
import de.calamanari.pk.util.pfis.ParallelFileInputStream;

/**
 * Indexer Master<br>
 * Indexing is implemented using the MASTER-SLAVE pattern. The master cuts the work into peaces the slaves perform the
 * indexing. Finally the master collects the results and sets up the full index.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
final class IndexerMaster {

    /**
     * A lookup for all character codes to the length (in bytes) of the character represented using the current charset.
     */
    private final byte[] charLengthLookup;

    /**
     * Indexer configuration.
     */
    private final ItfaConfiguration configuration;

    /**
     * The file the indexed file belongs to
     */
    private final File file;

    /**
     * size in bytes of the input file
     */
    private final long fileSize;

    /**
     * Name of the character set to be used
     */
    private final String charsetName;

    /**
     * Handling line breaks is complex, we avoid carriage returns at the end of a partition, because there may be a
     * following line feed.<br>
     * Thus we move a trailing CR to the next partition.<br>
     * In this case the buffer start index will be 1 otherwise 0
     */
    private boolean movedCarriageReturnToNextPartition = false;

    /**
     * The maximum number of character entries is fixed and has to be distributed as good as possible over the complete
     * file.<br>
     * Process-inherent problems (rounding for example) can lead to a loss of entries.<br>
     * To avoid this, we maintain a delta (unused entries).
     */
    private double charEntryDelta = 0;

    /**
     * The maximum number of line entries is fixed and has to be distributed as good as possible over the complete file.<br>
     * Process-inherent problems (rounding for example) can lead to a loss of entries.<br>
     * To avoid this, we maintain a delta (unused entries).
     */
    private double lineEntryDelta = 0;

    /**
     * sums-up lost character entries from sub-partition indexing
     */
    private int lostCharEntries = 0;

    /**
     * sums-up lost line entries from sub-partition indexing
     */
    private int lostLineEntries = 0;

    /**
     * variable to hold the number of bytes processed in total
     */
    private long totalNumberOfBytesProcessed = 0L;

    /**
     * counts the number of characters during processing the file
     */
    private long totalNumberOfCharsRead = 0L;

    /**
     * counts the number of lines during processing the file
     */
    private long totalNumberOfLinesRead = 0L;

    /**
     * the character entry distance: each n bytes there should be a character index entry<br>
     * depends on the file size
     */
    private long averageCharEntryDistance = 0L;

    /**
     * the line entry distance: each n bytes there should be a line index entry<br>
     * depends on the file size
     */
    private long averageLineEntryDistance = 0L;

    /**
     * for a number of characters (number of character in the sequence) map the exact byte position<br>
     * filled during indexer run
     */
    private final Map<Long, Long> characterPositionIndex;

    /**
     * for a number of lines map the exact byte position of the first character<br>
     * filled during indexer run
     */
    private final Map<Long, Long> linePositionIndex;

    /**
     * The number of threads, calculated at start depending on the current settings
     */
    private int numberOfThreads = 1;

    /**
     * The executor service for multi-threading
     */
    private ExecutorService executorService = null;

    /**
     * The array of indexer slaves for fast access
     */
    private IndexerSlave[] indexerSlaves = null;

    /**
     * Number of Bytes processed last time<br>
     * This backup value allows to compute differences after a subsequent read.
     */
    private long lastNumberOfBytesProcessed = 0L;

    /**
     * Creates the master, not started, yet
     * @param configuration indexer configuration
     * @param file The file being indexed
     * @param charsetName name of the charset for the underlying file
     * @param charLengthLookup A lookup for all character codes to the length (in bytes) of the character represented
     *            using the current charset.
     */
    public IndexerMaster(ItfaConfiguration configuration, File file, String charsetName, byte[] charLengthLookup) {
        this.configuration = configuration;
        this.file = file;
        this.fileSize = file.length();
        this.charsetName = charsetName;
        this.charLengthLookup = charLengthLookup;
        this.characterPositionIndex = new HashMap<>(configuration.maxNumberOfCharIndexEntries);
        this.linePositionIndex = new HashMap<>(configuration.maxNumberOfLineIndexEntries);

    }

    /**
     * This method creates the index, an instance of {@link IndexerMasterResult}
     * @return index data
     * @throws IOException on file access problems
     */
    public IndexerMasterResult createIndex() throws IOException {

        prepareRun();

        ParallelFileInputStream masterStream = null;
        try {
            masterStream = prepareMasterStream();
            indexAll(masterStream);
        }
        finally {
            try {
                executorService.shutdown();
            }
            catch (Throwable t) {
                // ignore
            }
            MiscUtils.closeResourceCatch(masterStream);
        }

        return createResult();
    }

    /**
     * This method creates the parallel file input stream for the indexer run and winds it forward to the first data
     * position (after the byte-order-mark)
     * @return prepared master stream
     * @throws IOException on file access problems
     */
    private ParallelFileInputStream prepareMasterStream() throws IOException {
        ParallelFileInputStream masterStream;
        masterStream = ParallelFileInputStream.createInputStream(file, configuration.indexerReadBufferSize,
                configuration.indexerReadBufferType);

        for (int i = 0; i < configuration.bomSize; i++) {
            masterStream.read();
        }
        // number of Bytes processed, yet
        // this allows us to determine the position within the file
        lastNumberOfBytesProcessed = masterStream.getNumberOfBytesDelivered();

        return masterStream;
    }

    /**
     * This method prepares the major settings for the current indexer run
     */
    private void prepareRun() {

        long dataSize = fileSize - configuration.bomSize;

        // the character entry distance: each n bytes there should be a character index entry
        averageCharEntryDistance = (long) Math.ceil(((double) dataSize) / configuration.maxNumberOfCharIndexEntries);
        if (averageCharEntryDistance == 0) {
            averageCharEntryDistance = 1;
        }

        // the line entry distance: each n bytes there may be a line index entry
        averageLineEntryDistance = (long) Math.ceil(((double) dataSize) / configuration.maxNumberOfLineIndexEntries);
        if (averageLineEntryDistance == 0) {
            averageLineEntryDistance = 1;
        }

        numberOfThreads = Runtime.getRuntime().availableProcessors();

        if (dataSize < configuration.multiThreadingThreshold) {
            numberOfThreads = 1;
        }

        // master uses a number of slaves
        executorService = Executors.newFixedThreadPool(numberOfThreads);

        indexerSlaves = new IndexerSlave[numberOfThreads];
        for (int i = 0; i < numberOfThreads; i++) {
            indexerSlaves[i] = new IndexerSlave(this.charLengthLookup);
        }
        characterPositionIndex.clear();
        linePositionIndex.clear();
    }

    /**
     * Processes all the data from the input file
     * @param masterStream input data
     * @throws IOException on file access problems
     */
    private void indexAll(ParallelFileInputStream masterStream) throws IOException {
        BufferedReader masterReader = null;

        try {
            masterReader = new BufferedReader(new InputStreamReader(masterStream, charsetName),
                    configuration.charBufferSize);

            long dataSize = fileSize - configuration.bomSize;

            char[] buffer = new char[configuration.charBufferSize];

            // calibrate start position related to end of byte-order-mark (if any)
            long firstDataBytePos = (configuration.bomSize > 0) ? configuration.bomSize : 0L;
            if (dataSize > 0) {
                characterPositionIndex.put(0L, firstDataBytePos);
                linePositionIndex.put(0L, firstDataBytePos);
            }

            indexPartitions(masterStream, masterReader, buffer);
        }
        finally {
            if (masterReader != null) {
                try {
                    masterReader.close();
                }
                catch (Throwable t) {
                    // irrelevant, we will close the channel anyway
                }
            }
        }
    }

    /**
     * Reads the underlying file's data partition-wise and indexes the obtained characters and lines.
     * @param masterStream stream (for calculating absolute byte-positions)
     * @param masterReader provides the character from the file
     * @param buffer process character buffer (for partitioning)
     * @throws IOException on file access problems
     */
    private void indexPartitions(ParallelFileInputStream masterStream, BufferedReader masterReader, char[] buffer)
            throws IOException {
        int partitionSize = 0;
        int bufferStartIdx = 0;
        while ((partitionSize = masterReader
                .read(buffer, bufferStartIdx, configuration.charBufferSize - bufferStartIdx)) != -1
                || bufferStartIdx > 0) {

            if (partitionSize == -1) {
                partitionSize = bufferStartIdx;
            }

            // memorize position AFTER partition
            long numberOfBytesProcessed = masterStream.getNumberOfBytesDelivered();

            IndexerPartitionMetaData metaData = preparePartitionAndAdjustDeltas(buffer, partitionSize,
                    numberOfBytesProcessed);

            lostCharEntries = 0;
            lostLineEntries = 0;

            lastNumberOfBytesProcessed = numberOfBytesProcessed;

            indexPartition(buffer, metaData);

            // pre-install the moved carriage return
            if (movedCarriageReturnToNextPartition) {
                buffer[0] = (char) CARRIAGE_RETURN_CODE;
                bufferStartIdx = 1;
            }
            else {
                bufferStartIdx = 0;
            }
        }
    }

    /**
     * This method processes (indexes) a single partition of the input file
     * @param buffer characters read from file
     * @param partitionMetaData partition settings
     * @throws IOException on file access problems
     */
    private void indexPartition(char[] buffer, IndexerPartitionMetaData partitionMetaData) throws IOException {

        // a latch to let the slaves communicate with the master
        CountDownLatch slavesFinishedLatch = new CountDownLatch(partitionMetaData.numberOfSlaves);

        indexSubPartitionsAsync(buffer, partitionMetaData, slavesFinishedLatch);

        waitForSlaves(slavesFinishedLatch);

        combineSlaveResults(partitionMetaData.numberOfSlaves);
    }

    /**
     * Splits the partition into sub-partition and starts asynchronous slave-tasks to index these.
     * @param buffer partition character data
     * @param partitionMetaData partition meta data
     * @param slavesFinishedLatch latch, any slave shall count-down after having finished
     */
    private void indexSubPartitionsAsync(char[] buffer, IndexerPartitionMetaData partitionMetaData,
            CountDownLatch slavesFinishedLatch) {

        // at sub-partition boundaries we must again care for carriage return characters
        boolean subPartitionMovedCR = false;

        // now setup and start slaves
        for (int i = 0; i < partitionMetaData.numberOfSlaves; i++) {
            int subPartitionStartIdx = i * partitionMetaData.defaultSubPartitionSize;

            int subPartitionSize = partitionMetaData.defaultSubPartitionSize;

            // Within sub-partitions we also have to deal with trailing
            // carriage returns (move to the next sub-partition)

            if (subPartitionMovedCR) {

                // found "saved" carriage return, prepend to current sub-partition
                subPartitionStartIdx--;

                // adopt partition size
                subPartitionSize++;
            }

            // shall we move a trailing carriage return?
            subPartitionMovedCR = (i < (partitionMetaData.numberOfSlaves - 1) && buffer[subPartitionStartIdx
                    + subPartitionSize - 1] == CARRIAGE_RETURN_CODE);

            if (subPartitionMovedCR) {
                // move carriage return to next sub-partition by (truncate)
                subPartitionSize--;
            }

            if (i == partitionMetaData.numberOfSlaves - 1) {
                // special case: last sub-partition, process ALL remaining characters
                subPartitionSize = partitionMetaData.partitionSize - subPartitionStartIdx;
            }

            indexSubPartitionAsync(buffer, i, subPartitionStartIdx, subPartitionSize,
                    partitionMetaData.maxNumberOfCharEntriesInSubPartition,
                    partitionMetaData.maxNumberOfLineEntriesInSubPartition, slavesFinishedLatch);
        }
    }

    /**
     * Starts a the specified slave on the given sub-partition
     * @param buffer current buffer data
     * @param slaveNumber number of the slave 0-based
     * @param subPartitionStartIdx start of the sub-partition related to main partition
     * @param subPartitionSize size of the sub-partition
     * @param maxNumberOfCharEntriesInSubPartition maximum number of characters to be indexed in sub-partition
     * @param maxNumberOfLineEntriesInSubPartition maximum number of lines to be indexed in sub-partition
     * @param slavesFinishedLatch latch, slave shall count-down after having finished
     */
    private void indexSubPartitionAsync(char[] buffer, int slaveNumber, int subPartitionStartIdx, int subPartitionSize,
            int maxNumberOfCharEntriesInSubPartition, int maxNumberOfLineEntriesInSubPartition,
            CountDownLatch slavesFinishedLatch) {
        IndexerSlave slave = indexerSlaves[slaveNumber];
        slave.latch = slavesFinishedLatch;
        slave.maxNumberOfCharEntries = maxNumberOfCharEntriesInSubPartition;
        slave.maxNumberOfLineEntries = maxNumberOfLineEntriesInSubPartition;
        slave.partition = buffer;
        slave.startIdx = subPartitionStartIdx;
        slave.subPartitionSize = subPartitionSize;
        slave.propagateError.set(null);
        slave.result.set(null);
        executorService.execute(slave);
    }

    /**
     * This method calculates settings for processing the next partition and adjust global settings (memorize moved
     * carriage return and entry deltas)
     * @param buffer character buffer (partition data)
     * @param partitionSize size of the new partition
     * @param numberOfBytesProcessed number of bytes AFTER reading the current partition from the file
     * @return partition meta data
     */
    private IndexerPartitionMetaData preparePartitionAndAdjustDeltas(char[] buffer, int partitionSize,
            long numberOfBytesProcessed) {
        IndexerPartitionMetaData partitionMetaData = new IndexerPartitionMetaData();
        partitionMetaData.partitionSize = partitionSize;
        partitionMetaData.partitionSizeInBytes = numberOfBytesProcessed - lastNumberOfBytesProcessed;

        // re-integrate moved carriage return
        if (movedCarriageReturnToNextPartition) {
            partitionMetaData.partitionSizeInBytes = partitionMetaData.partitionSizeInBytes
                    + charLengthLookup[(int) CARRIAGE_RETURN_CODE];
        }

        // check this partition whether it ends with a CR
        movedCarriageReturnToNextPartition = (partitionMetaData.partitionSize > 1
                && buffer[partitionMetaData.partitionSize - 1] == CARRIAGE_RETURN_CODE);

        // move CR to next partition
        if (movedCarriageReturnToNextPartition) {
            partitionMetaData.partitionSize--;
            partitionMetaData.partitionSizeInBytes = partitionMetaData.partitionSizeInBytes
                    - charLengthLookup[(int) CARRIAGE_RETURN_CODE];
        }

        adjustSettingsAndDeltas(partitionMetaData);
        return partitionMetaData;
    }

    /**
     * Distributing the entries over the file needs corrections to avoid loosing possible entries (division/remainder
     * problem), to reduce loss, the character and line entry delta helps us collecting "entry fractions".<br>
     * This method adjusts these deltas and uses the values to evtl. adjust the number of possible entries
     * (character/line) entries in the next sub-partition.
     * @param partitionMetaData current partition meta data
     */
    private void adjustSettingsAndDeltas(IndexerPartitionMetaData partitionMetaData) {
        // calculate number of entries and loss
        double exactMaxNumberOfCharEntriesInPartition =
                ((double) partitionMetaData.partitionSizeInBytes / (double) averageCharEntryDistance)
                        + lostCharEntries;
        int maxNumberOfCharEntriesInPartition = ((int) (Math.floor(exactMaxNumberOfCharEntriesInPartition)));
        charEntryDelta = charEntryDelta + (exactMaxNumberOfCharEntriesInPartition - maxNumberOfCharEntriesInPartition);

        double exactMaxNumberOfLineEntriesInPartition =
                ((double) partitionMetaData.partitionSizeInBytes / (double) averageLineEntryDistance)
                        + lostLineEntries;
        int maxNumberOfLineEntriesInPartition = ((int) (Math.floor(exactMaxNumberOfLineEntriesInPartition)));
        lineEntryDelta = lineEntryDelta + (exactMaxNumberOfLineEntriesInPartition - maxNumberOfLineEntriesInPartition);

        partitionMetaData.numberOfSlaves = 1;
        partitionMetaData.defaultSubPartitionSize = partitionMetaData.partitionSize;

        if (partitionMetaData.partitionSize > configuration.multiThreadingThreshold) {
            partitionMetaData.defaultSubPartitionSize = partitionMetaData.partitionSize / numberOfThreads;
            partitionMetaData.numberOfSlaves = numberOfThreads;
        }

        // in the code below we calculate how many entries we have to spread over the partition and its sub-partitions

        double exactMaxNumberOfCharEntriesInSubPartition = (double) maxNumberOfCharEntriesInPartition
                / (double) partitionMetaData.numberOfSlaves;

        partitionMetaData.maxNumberOfCharEntriesInSubPartition = (int) Math
                .floor(exactMaxNumberOfCharEntriesInSubPartition);
        charEntryDelta = charEntryDelta
                + (exactMaxNumberOfCharEntriesInSubPartition - partitionMetaData.maxNumberOfCharEntriesInSubPartition)
                * partitionMetaData.numberOfSlaves;

        if (charEntryDelta > partitionMetaData.numberOfSlaves) {
            partitionMetaData.maxNumberOfCharEntriesInSubPartition++;
            charEntryDelta = charEntryDelta - partitionMetaData.numberOfSlaves;
        }

        double exactMaxNumberOfLineEntriesInSubPartition = (double) maxNumberOfLineEntriesInPartition
                / (double) partitionMetaData.numberOfSlaves;
        partitionMetaData.maxNumberOfLineEntriesInSubPartition = (int) Math
                .floor(exactMaxNumberOfLineEntriesInSubPartition);
        lineEntryDelta = lineEntryDelta
                + (exactMaxNumberOfLineEntriesInSubPartition - partitionMetaData.maxNumberOfLineEntriesInSubPartition)
                * partitionMetaData.numberOfSlaves;

        if (lineEntryDelta > partitionMetaData.numberOfSlaves) {
            partitionMetaData.maxNumberOfLineEntriesInSubPartition++;
            lineEntryDelta = lineEntryDelta - partitionMetaData.numberOfSlaves;
        }
    }

    /**
     * Waits for all the started slaves
     * @param slavesFinishedLatch the countdown-latch the slaves use to signal finalization to the master
     * @throws IOException on file access problems
     */
    private void waitForSlaves(CountDownLatch slavesFinishedLatch) throws IOException {
        try {
            slavesFinishedLatch.await();
        }
        catch (InterruptedException ex) {
            throw new IOException("Unexpected interruption during index run", ex);
        }
    }

    /**
     * After all slaves have finished we collect and combine their sub-partition index data.<br>
     * The result is the the index data for the partition
     * @param numberOfSlaves number of indexing slaves
     * @throws IOException on file access problems
     */
    private void combineSlaveResults(int numberOfSlaves) throws IOException {
        for (int i = 0; i < numberOfSlaves; i++) {
            IndexerSlave slave = indexerSlaves[i];
            IndexerSlaveResult slaveResult = slave.result.get();

            Throwable propagateError = slave.propagateError.get();
            if (propagateError != null) {
                throw new IOException("Unexpected error during index run", propagateError);
            }

            long offsetBytes = totalNumberOfBytesProcessed;
            long offsetChars = totalNumberOfCharsRead;
            long offsetLines = totalNumberOfLinesRead;

            totalNumberOfBytesProcessed = totalNumberOfBytesProcessed + slaveResult.numberOfBytesProcessed;
            totalNumberOfCharsRead = totalNumberOfCharsRead + slaveResult.numberOfCharactersRead;
            totalNumberOfLinesRead = totalNumberOfLinesRead + slaveResult.numberOfLinesRead;

            lostCharEntries = lostCharEntries
                    + Math.max(0, slave.maxNumberOfCharEntries - slaveResult.numberOfCharIndexEntries);
            lostLineEntries = lostLineEntries
                    + Math.max(0, slave.maxNumberOfLineEntries - slaveResult.numberOfLineIndexEntries);

            int numberOfCharIndexEntries = slaveResult.numberOfCharIndexEntries;
            long[][] charIndex = slaveResult.charIndex;
            for (int j = 0; j < numberOfCharIndexEntries; j++) {
                long[] entry = charIndex[j];
                characterPositionIndex.put(offsetChars + entry[0], offsetBytes + entry[1]);
            }

            int numberOfLineIndexEntries = slaveResult.numberOfLineIndexEntries;
            long[][] lineIndex = slaveResult.lineIndex;
            for (int j = 0; j < numberOfLineIndexEntries; j++) {
                long[] entry = lineIndex[j];
                linePositionIndex.put(offsetLines + entry[0], offsetBytes + entry[1]);
            }
        }
    }

    /**
     * Creates the indexer result from the collected process data
     * @return result
     */
    private IndexerMasterResult createResult() {
        IndexerMasterResult res = new IndexerMasterResult();
        res.fileSize = fileSize;
        res.characterPositionIndex = new HashMap<>(characterPositionIndex);
        res.linePositionIndex = new HashMap<>(linePositionIndex);
        res.numberOfCharacterIndexEntries = characterPositionIndex.size();
        res.numberOfLineIndexEntries = linePositionIndex.size();
        res.numberOfCharacters = totalNumberOfCharsRead;
        res.numberOfLines = (res.fileSize > 0 ? totalNumberOfLinesRead + 1 : 0);
        int pos = -1;
        res.indexedLineNumbers = new long[linePositionIndex.size()];
        for (Long key : linePositionIndex.keySet()) {
            pos++;
            res.indexedLineNumbers[pos] = key;
        }
        Arrays.sort(res.indexedLineNumbers);
        pos = -1;
        res.indexedCharNumbers = new long[characterPositionIndex.size()];
        for (Long key : characterPositionIndex.keySet()) {
            pos++;
            res.indexedCharNumbers[pos] = key;
        }
        Arrays.sort(res.indexedCharNumbers);
        return res;
    }

}
