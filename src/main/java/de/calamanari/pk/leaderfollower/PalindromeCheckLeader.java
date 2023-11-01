//@formatter:off
/*
 * Palindrome Check Leader - demonstrates LEADER FOLLOWER
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
package de.calamanari.pk.leaderfollower;

import java.io.File;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.calamanari.pk.util.TimeUtils;
import de.calamanari.pk.util.itfa.IndexedTextFileAccessor;
import de.calamanari.pk.util.itfa.ItfaConfiguration;

/**
 * Palindrome Check Leader - the LEADER in this LEADER FOLLOWER example, divides the task into subtasks waits for the followers to complete the sub-tasks and
 * returns the result.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class PalindromeCheckLeader {

    private static final Logger LOGGER = LoggerFactory.getLogger(PalindromeCheckLeader.class);

    /**
     * simulated load
     */
    private static final long OTHER_TASK_DELAY_MILLIS = 1000;

    /**
     * Size of the partitions a follower will work on
     */
    private final int partitionSize;

    /**
     * Internally used executor to control the followers' work
     */
    private final Executor executor;

    /**
     * Configuration for the underlying indexer.
     */
    private final ItfaConfiguration checkLeaderIndexerConfiguration;

    /**
     * Creates the leader with some configuration information
     * 
     * @param numberOfFollowers number of parallel workers
     * @param partitionSize the size (characters) of a partition (pair) a follower will work in
     * @param maxNumberOfCharIndexEntries positive index size, see {@link ItfaConfiguration#maxNumberOfCharIndexEntries}
     */
    public PalindromeCheckLeader(int numberOfFollowers, int partitionSize, int maxNumberOfCharIndexEntries) {
        this.partitionSize = partitionSize;
        this.executor = Executors.newFixedThreadPool(numberOfFollowers);
        ItfaConfiguration indexerConfig = new ItfaConfiguration();
        indexerConfig.setMaxNumberOfCharIndexEntries(maxNumberOfCharIndexEntries);
        indexerConfig.setMaxNumberOfLineIndexEntries(1);
        this.checkLeaderIndexerConfiguration = indexerConfig;
    }

    /**
     * While waiting the LEADER can do other things
     */
    private void doOtherStuff() {
        LOGGER.debug("LEADER does other stuff while waiting ...");
        TimeUtils.sleepIgnoreException(OTHER_TASK_DELAY_MILLIS);
    }

    /**
     * Perform a palindrome test on the given text file.
     * 
     * @param file source file with text to be checked
     * @param charsetName character set, i.e. "UTF-8"
     * @return check result
     * @throws IOException on data access error
     * @throws InterruptedException on any interruption
     * @throws ExecutionException on any error during execution
     */
    public PalindromeCheckResult performPalindromeFileTest(File file, String charsetName) throws IOException, InterruptedException, ExecutionException {
        LOGGER.debug("{}.performPalindromeFileTest({file={}, charsetName={}}) called", this.getClass().getSimpleName(), file, charsetName);
        PalindromeCheckResult res;

        LOGGER.info("Scanning input file (create index) ... ");
        IndexedTextFileAccessor textFileAccessor = new IndexedTextFileAccessor(file, charsetName, checkLeaderIndexerConfiguration);

        if (textFileAccessor.getNumberOfCharacters() < 2) {
            LOGGER.info("Skipped partitioning (nothing to do). ");
            res = PalindromeCheckResult.CONFIRMED;
        }
        else {

            LOGGER.info("Preparing (create partitions for FOLLOWERs) ... ");
            PalindromeCheckFuture future = partitionAndStartFollowers(textFileAccessor);

            LOGGER.info("Preparation completed, LEADER is waiting for FOLLOWER-results ...");
            waitForFollowersToComplete(future);

            LOGGER.debug("Palindrome check finished!");
            res = future.get();

            if (res == PalindromeCheckResult.ERROR) {
                throw new ExecutionException("Unknown problem during palindrome test, see logs for details.", null);
            }
        }
        return res;
    }

    /**
     * This method frequently polls for the leader result (until we know whether the input was a palindrome or not).
     * 
     * @param future allows polling
     */
    private void waitForFollowersToComplete(PalindromeCheckFuture future) {
        boolean done = false;
        do {
            LOGGER.debug("LEADER polls for result ...");
            done = future.isDone();
            if (!done) {
                if (LOGGER.isInfoEnabled()) {
                    NumberFormat nf = NumberFormat.getInstance(Locale.US);
                    nf.setMaximumFractionDigits(0);
                    nf.setMinimumFractionDigits(0);
                    LOGGER.info("{}% completed, LEADER is still waiting for FOLLOWER-results ...", nf.format(future.getProgressPerc()));
                }
                doOtherStuff();
            }
        } while (!done);
    }

    /**
     * This method divides the task into smaller sub-tasks by creating data partitions.<br>
     * Afterwards it starts the follower executions using the {@link #executor}.
     * 
     * @param textFileAccessor indexed source file accessor
     * @return future to allow the leader to combine the total result
     */
    private PalindromeCheckFuture partitionAndStartFollowers(IndexedTextFileAccessor textFileAccessor) {
        long numberOfCharacters = textFileAccessor.getNumberOfCharacters();
        long halfLen = numberOfCharacters / 2;

        // (1) if the number was even, say 70 characters
        // centerOfFile=halfLen = 35 (incl.)
        // first half == characters [0..35) and second half == characters [35..70)
        //
        // (2) if the number was odd, say 71 characters
        // centerOfFile=halfLen = 35 (excl.)
        // first half == characters [0..35) and second half == characters [36..71) (ignore character 35)

        int numberOfFullPartitions = (int) (halfLen / partitionSize);
        int remainderPartitionSize = (int) (halfLen - ((long) numberOfFullPartitions) * partitionSize);

        // number of partitions is the number of section pairs (left/right) for palindrome test
        // AQAABBTBCCCCXCCCCBTBBAAQA
        // S1 S2 S3 S4 S5 S6
        //
        // 6 sections (S1-S6), 3 partitions (pairs): [S1, S6]; [S2, S5]; [S3, S4]
        // S1 will be compared character-by-character against the reversed version of S6, same for S2 and S5 as well as
        // for S3 and S4

        int numberOfPartitions = numberOfFullPartitions;
        if (remainderPartitionSize > 0) {
            numberOfPartitions++; // one (shorter) remainder partition pair
        }

        PalindromeCheckFuture future = new PalindromeCheckFuture(numberOfPartitions);

        for (int i = 0; i < numberOfFullPartitions; i++) {
            long startOfLeftPartition = ((long) i) * partitionSize;
            long startOfRightPartition = numberOfCharacters - startOfLeftPartition - partitionSize;
            PalindromeCheckFollowerTask task = new PalindromeCheckFollowerTask(textFileAccessor, startOfLeftPartition, startOfRightPartition, partitionSize,
                    future);
            executor.execute(task);
        }

        if (remainderPartitionSize > 0) {
            long startOfLeftPartition = halfLen - remainderPartitionSize;
            long startOfRightPartition = numberOfCharacters - startOfLeftPartition - remainderPartitionSize;
            PalindromeCheckFollowerTask task = new PalindromeCheckFollowerTask(textFileAccessor, startOfLeftPartition, startOfRightPartition,
                    remainderPartitionSize, future);
            executor.execute(task);
        }
        return future;
    }

}
