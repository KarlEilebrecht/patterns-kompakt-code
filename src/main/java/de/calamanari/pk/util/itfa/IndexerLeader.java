//@formatter:off
/*
 * Indexer Leader
 * Code-Beispiel zum Buch Patterns Kompakt, Verlag Springer Vieweg
 * Copyright 2014 Karl Eilebrecht
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"):
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
import java.util.concurrent.TimeUnit;

import de.calamanari.pk.util.CloseUtils;
import de.calamanari.pk.util.pfis.ParallelFileInputStream;

/**
 * Indexer Leader<br>
 * Indexing is implemented using the LEADER-FOLLOWER pattern. The leader cuts the work into peaces the followers perform the indexing. Finally the leader
 * collects the results and sets up the full index.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
final class IndexerLeader {

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
     * Handling line breaks is complex, we avoid carriage returns at the end of a partition, because there may be a following line feed.<br>
     * Thus we move a trailing CR to the next partition.<br>
     * In this case the buffer start index will be 1 otherwise 0
     */
    private boolean movedCarriageReturnToNextPartition = false;

    /**
     * The maximum number of character entries is fixed and has to be distributed as good as possible over the complete file.<br>
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
     * The array of indexer followers for fast access
     */
    private IndexerFollower[] indexerFollowers = null;

    /**
     * Number of Bytes processed last time<br>
     * This backup value allows to compute differences after a subsequent read.
     */
    private long lastNumberOfBytesProcessed = 0L;

    /**
     * Creates the leader, not started, yet
     * 
     * @param configuration indexer configuration
     * @param file The file being indexed
     * @param charsetName name of the charset for the underlying file
     * @param charLengthLookup A lookup for all character codes to the length (in bytes) of the character represented using the current charset.
     */
    public IndexerLeader(ItfaConfiguration configuration, File file, String charsetName, byte[] charLengthLookup) {
        this.configuration = configuration;
        this.file = file;
        this.fileSize = file.length();
        this.charsetName = charsetName;
        this.charLengthLookup = charLengthLookup;
        this.characterPositionIndex = HashMap.newHashMap(configuration.maxNumberOfCharIndexEntries);
        this.linePositionIndex = HashMap.newHashMap(configuration.maxNumberOfLineIndexEntries);

    }

    /**
     * This method creates the index, an instance of {@link IndexerLeaderResult}
     * 
     * @return index data
     * @throws IOException on file access problems
     */
    public IndexerLeaderResult createIndex() throws IOException {

        prepareRun();

        ParallelFileInputStream leaderStream = null;
        try {
            leaderStream = prepareLeaderStream();
            indexAll(leaderStream);
        }
        finally {
            try {
                executorService.shutdown();
                executorService.awaitTermination(5, TimeUnit.SECONDS);
            }
            catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
            }
            catch (RuntimeException ex) {
                // ignore
            }
            CloseUtils.closeResourceCatch(leaderStream);
        }

        return createResult();
    }

    /**
     * This method creates the parallel file input stream for the indexer run and winds it forward to the first data position (after the byte-order-mark)
     * 
     * @return prepared leader stream
     * @throws IOException on file access problems
     */
    // This method's job is to return an open stream, thus suppressing SonarLint complaint
    @SuppressWarnings("squid:S2095")
    private ParallelFileInputStream prepareLeaderStream() throws IOException {
        ParallelFileInputStream leaderStream;
        leaderStream = ParallelFileInputStream.createInputStream(file, configuration.indexerReadBufferSize, configuration.indexerReadBufferType);

        for (int i = 0; i < configuration.bomSize; i++) {
            leaderStream.read();
        }
        // number of Bytes processed, yet
        // this allows us to determine the position within the file
        lastNumberOfBytesProcessed = leaderStream.getNumberOfBytesDelivered();

        return leaderStream;
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

        // leader uses a number of followers
        executorService = Executors.newFixedThreadPool(numberOfThreads);

        indexerFollowers = new IndexerFollower[numberOfThreads];
        for (int i = 0; i < numberOfThreads; i++) {
            indexerFollowers[i] = new IndexerFollower(this.charLengthLookup);
        }
        characterPositionIndex.clear();
        linePositionIndex.clear();
    }

    /**
     * Processes all the data from the input file
     * 
     * @param leaderStream input data
     * @throws IOException on file access problems
     */
    private void indexAll(ParallelFileInputStream leaderStream) throws IOException {

        try (InputStreamReader isr = new InputStreamReader(leaderStream, charsetName);
                BufferedReader leaderReader = new BufferedReader(isr, configuration.charBufferSize)) {

            long dataSize = fileSize - configuration.bomSize;

            char[] buffer = new char[configuration.charBufferSize];

            // calibrate start position related to end of byte-order-mark (if any)
            long firstDataBytePos = (configuration.bomSize > 0) ? configuration.bomSize : 0L;
            if (dataSize > 0) {
                characterPositionIndex.put(0L, firstDataBytePos);
                linePositionIndex.put(0L, firstDataBytePos);
            }

            indexPartitions(leaderStream, leaderReader, buffer);
        }
    }

    /**
     * Reads the underlying file's data partition-wise and indexes the obtained characters and lines.
     * 
     * @param leaderStream stream (for calculating absolute byte-positions)
     * @param leaderReader provides the character from the file
     * @param buffer process character buffer (for partitioning)
     * @throws IOException on file access problems
     */
    private void indexPartitions(ParallelFileInputStream leaderStream, BufferedReader leaderReader, char[] buffer) throws IOException {
        int partitionSize = 0;
        int bufferStartIdx = 0;
        while ((partitionSize = leaderReader.read(buffer, bufferStartIdx, configuration.charBufferSize - bufferStartIdx)) != -1 || bufferStartIdx > 0) {

            if (partitionSize == -1) {
                partitionSize = bufferStartIdx;
            }

            // memorize position AFTER partition
            long numberOfBytesProcessed = leaderStream.getNumberOfBytesDelivered();

            IndexerPartitionMetaData metaData = preparePartitionAndAdjustDeltas(buffer, partitionSize, numberOfBytesProcessed);

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
     * 
     * @param buffer characters read from file
     * @param partitionMetaData partition settings
     * @throws IOException on file access problems
     */
    private void indexPartition(char[] buffer, IndexerPartitionMetaData partitionMetaData) throws IOException {

        // a latch to let the followers communicate with the leader
        CountDownLatch followersFinishedLatch = new CountDownLatch(partitionMetaData.numberOfFollowers);

        indexSubPartitionsAsync(buffer, partitionMetaData, followersFinishedLatch);

        waitForFollowers(followersFinishedLatch);

        combineFollowerResults(partitionMetaData.numberOfFollowers);
    }

    /**
     * Splits the partition into sub-partition and starts asynchronous follower-tasks to index these.
     * 
     * @param buffer partition character data
     * @param partitionMetaData partition meta data
     * @param followersFinishedLatch latch, any follower shall count-down after having finished
     */
    private void indexSubPartitionsAsync(char[] buffer, IndexerPartitionMetaData partitionMetaData, CountDownLatch followersFinishedLatch) {

        // at sub-partition boundaries we must again care for carriage return characters
        boolean subPartitionMovedCR = false;

        // now setup and start followers
        for (int i = 0; i < partitionMetaData.numberOfFollowers; i++) {
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
            subPartitionMovedCR = (i < (partitionMetaData.numberOfFollowers - 1)
                    && buffer[subPartitionStartIdx + subPartitionSize - 1] == CARRIAGE_RETURN_CODE);

            if (subPartitionMovedCR) {
                // move carriage return to next sub-partition by (truncate)
                subPartitionSize--;
            }

            if (i == partitionMetaData.numberOfFollowers - 1) {
                // special case: last sub-partition, process ALL remaining characters
                subPartitionSize = partitionMetaData.partitionSize - subPartitionStartIdx;
            }

            indexSubPartitionAsync(buffer, i, subPartitionStartIdx, subPartitionSize, partitionMetaData.maxNumberOfCharEntriesInSubPartition,
                    partitionMetaData.maxNumberOfLineEntriesInSubPartition, followersFinishedLatch);
        }
    }

    /**
     * Starts a the specified follower on the given sub-partition
     * 
     * @param buffer current buffer data
     * @param followerNumber number of the follower 0-based
     * @param subPartitionStartIdx start of the sub-partition related to main partition
     * @param subPartitionSize size of the sub-partition
     * @param maxNumberOfCharEntriesInSubPartition maximum number of characters to be indexed in sub-partition
     * @param maxNumberOfLineEntriesInSubPartition maximum number of lines to be indexed in sub-partition
     * @param followersFinishedLatch latch, follower shall count-down after having finished
     */
    private void indexSubPartitionAsync(char[] buffer, int followerNumber, int subPartitionStartIdx, int subPartitionSize,
            int maxNumberOfCharEntriesInSubPartition, int maxNumberOfLineEntriesInSubPartition, CountDownLatch followersFinishedLatch) {
        IndexerFollower follower = indexerFollowers[followerNumber];
        follower.latch = followersFinishedLatch;
        follower.maxNumberOfCharEntries = maxNumberOfCharEntriesInSubPartition;
        follower.maxNumberOfLineEntries = maxNumberOfLineEntriesInSubPartition;
        follower.partition = buffer;
        follower.startIdx = subPartitionStartIdx;
        follower.subPartitionSize = subPartitionSize;
        follower.propagateError.set(null);
        follower.result.set(null);
        executorService.execute(follower);
    }

    /**
     * This method calculates settings for processing the next partition and adjust global settings (memorize moved carriage return and entry deltas)
     * 
     * @param buffer character buffer (partition data)
     * @param partitionSize size of the new partition
     * @param numberOfBytesProcessed number of bytes AFTER reading the current partition from the file
     * @return partition meta data
     */
    private IndexerPartitionMetaData preparePartitionAndAdjustDeltas(char[] buffer, int partitionSize, long numberOfBytesProcessed) {
        IndexerPartitionMetaData partitionMetaData = new IndexerPartitionMetaData();
        partitionMetaData.partitionSize = partitionSize;
        partitionMetaData.partitionSizeInBytes = numberOfBytesProcessed - lastNumberOfBytesProcessed;

        // re-integrate moved carriage return
        if (movedCarriageReturnToNextPartition) {
            partitionMetaData.partitionSizeInBytes = partitionMetaData.partitionSizeInBytes + charLengthLookup[CARRIAGE_RETURN_CODE];
        }

        // check this partition whether it ends with a CR
        movedCarriageReturnToNextPartition = (partitionMetaData.partitionSize > 1 && buffer[partitionMetaData.partitionSize - 1] == CARRIAGE_RETURN_CODE);

        // move CR to next partition
        if (movedCarriageReturnToNextPartition) {
            partitionMetaData.partitionSize--;
            partitionMetaData.partitionSizeInBytes = partitionMetaData.partitionSizeInBytes - charLengthLookup[CARRIAGE_RETURN_CODE];
        }

        adjustSettingsAndDeltas(partitionMetaData);
        return partitionMetaData;
    }

    /**
     * Distributing the entries over the file needs corrections to avoid loosing possible entries (division/remainder problem), to reduce loss, the character
     * and line entry delta helps us collecting "entry fractions".<br>
     * This method adjusts these deltas and uses the values to evtl. adjust the number of possible entries (character/line) entries in the next sub-partition.
     * 
     * @param partitionMetaData current partition meta data
     */
    private void adjustSettingsAndDeltas(IndexerPartitionMetaData partitionMetaData) {
        // calculate number of entries and loss
        double exactMaxNumberOfCharEntriesInPartition = ((double) partitionMetaData.partitionSizeInBytes / (double) averageCharEntryDistance) + lostCharEntries;
        int maxNumberOfCharEntriesInPartition = ((int) (Math.floor(exactMaxNumberOfCharEntriesInPartition)));
        charEntryDelta = charEntryDelta + (exactMaxNumberOfCharEntriesInPartition - maxNumberOfCharEntriesInPartition);

        double exactMaxNumberOfLineEntriesInPartition = ((double) partitionMetaData.partitionSizeInBytes / (double) averageLineEntryDistance) + lostLineEntries;
        int maxNumberOfLineEntriesInPartition = ((int) (Math.floor(exactMaxNumberOfLineEntriesInPartition)));
        lineEntryDelta = lineEntryDelta + (exactMaxNumberOfLineEntriesInPartition - maxNumberOfLineEntriesInPartition);

        partitionMetaData.numberOfFollowers = 1;
        partitionMetaData.defaultSubPartitionSize = partitionMetaData.partitionSize;

        if (partitionMetaData.partitionSize > configuration.multiThreadingThreshold) {
            partitionMetaData.defaultSubPartitionSize = partitionMetaData.partitionSize / numberOfThreads;
            partitionMetaData.numberOfFollowers = numberOfThreads;
        }

        // in the code below we calculate how many entries we have to spread over the partition and its sub-partitions

        double exactMaxNumberOfCharEntriesInSubPartition = (double) maxNumberOfCharEntriesInPartition / (double) partitionMetaData.numberOfFollowers;

        partitionMetaData.maxNumberOfCharEntriesInSubPartition = (int) Math.floor(exactMaxNumberOfCharEntriesInSubPartition);
        charEntryDelta = charEntryDelta
                + (exactMaxNumberOfCharEntriesInSubPartition - partitionMetaData.maxNumberOfCharEntriesInSubPartition) * partitionMetaData.numberOfFollowers;

        if (charEntryDelta > partitionMetaData.numberOfFollowers) {
            partitionMetaData.maxNumberOfCharEntriesInSubPartition++;
            charEntryDelta = charEntryDelta - partitionMetaData.numberOfFollowers;
        }

        double exactMaxNumberOfLineEntriesInSubPartition = (double) maxNumberOfLineEntriesInPartition / (double) partitionMetaData.numberOfFollowers;
        partitionMetaData.maxNumberOfLineEntriesInSubPartition = (int) Math.floor(exactMaxNumberOfLineEntriesInSubPartition);
        lineEntryDelta = lineEntryDelta
                + (exactMaxNumberOfLineEntriesInSubPartition - partitionMetaData.maxNumberOfLineEntriesInSubPartition) * partitionMetaData.numberOfFollowers;

        if (lineEntryDelta > partitionMetaData.numberOfFollowers) {
            partitionMetaData.maxNumberOfLineEntriesInSubPartition++;
            lineEntryDelta = lineEntryDelta - partitionMetaData.numberOfFollowers;
        }
    }

    /**
     * Waits for all the started followers
     * 
     * @param followersFinishedLatch the countdown-latch the followers use to signal finalization to the leader
     * @throws IOException on file access problems
     */
    private void waitForFollowers(CountDownLatch followersFinishedLatch) throws IOException {
        try {
            followersFinishedLatch.await();
        }
        catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new IOException("Unexpected interruption during index run", ex);
        }
    }

    /**
     * After all followers have finished we collect and combine their sub-partition index data.<br>
     * The result is the the index data for the partition
     * 
     * @param numberOfFollowers number of indexing followers
     * @throws IOException on file access problems
     */
    private void combineFollowerResults(int numberOfFollowers) throws IOException {
        for (int i = 0; i < numberOfFollowers; i++) {
            IndexerFollower follower = indexerFollowers[i];
            IndexerFollowerResult followerResult = follower.result.get();

            Throwable propagateError = follower.propagateError.get();
            if (propagateError != null) {
                throw new IOException("Unexpected error during index run", propagateError);
            }

            long offsetBytes = totalNumberOfBytesProcessed;
            long offsetChars = totalNumberOfCharsRead;
            long offsetLines = totalNumberOfLinesRead;

            totalNumberOfBytesProcessed = totalNumberOfBytesProcessed + followerResult.numberOfBytesProcessed;
            totalNumberOfCharsRead = totalNumberOfCharsRead + followerResult.numberOfCharactersRead;
            totalNumberOfLinesRead = totalNumberOfLinesRead + followerResult.numberOfLinesRead;

            lostCharEntries = lostCharEntries + Math.max(0, follower.maxNumberOfCharEntries - followerResult.numberOfCharIndexEntries);
            lostLineEntries = lostLineEntries + Math.max(0, follower.maxNumberOfLineEntries - followerResult.numberOfLineIndexEntries);

            int numberOfCharIndexEntries = followerResult.numberOfCharIndexEntries;
            long[][] charIndex = followerResult.charIndex;
            for (int j = 0; j < numberOfCharIndexEntries; j++) {
                long[] entry = charIndex[j];
                characterPositionIndex.put(offsetChars + entry[0], offsetBytes + entry[1]);
            }

            int numberOfLineIndexEntries = followerResult.numberOfLineIndexEntries;
            long[][] lineIndex = followerResult.lineIndex;
            for (int j = 0; j < numberOfLineIndexEntries; j++) {
                long[] entry = lineIndex[j];
                linePositionIndex.put(offsetLines + entry[0], offsetBytes + entry[1]);
            }
        }
    }

    /**
     * Creates the indexer result from the collected process data
     * 
     * @return result
     */
    private IndexerLeaderResult createResult() {
        IndexerLeaderResult res = new IndexerLeaderResult();
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
