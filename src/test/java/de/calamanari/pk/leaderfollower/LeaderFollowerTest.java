//@formatter:off
/*
 * Leader Follower Test - demonstrates LEADER FOLLOWER pattern.
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

import static org.junit.Assert.assertEquals;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.calamanari.pk.util.FileUtils;
import de.calamanari.pk.util.TimeUtils;
import de.calamanari.pk.util.itfa.IndexedTextFileAccessor;
import de.calamanari.pk.util.itfa.ItfaConfiguration;

/**
 * Leader Follower Test - demonstrates LEADER FOLLOWER pattern.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
@SuppressWarnings("resource")
public class LeaderFollowerTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(LeaderFollowerTest.class);

    /**
     * Character set for the test
     */
    private static final String CHARSET_NAME = "UTF-8";

    /**
     * Allowed characters including some German umlauts which will need two bytes encoded using UTF-8
     */
    private static final char[] CHARACTERS = "\u00C4\u00D6\u00DCABCDEFGHIJKLMNOPQRSTUVWXYZ01234567890123456789".toCharArray();

    /**
     * Number of available characters
     */
    private static final int NUMBER_OF_CHARACTERS = CHARACTERS.length;

    /**
     * Number of characters in the palindrome
     */
    @SuppressWarnings("java:S125")
    private static final int PALINDROME_SIZE = 1_111_111; // 1 GB = 1_073_741_824;

    /**
     * number of followers (worker threads)
     */
    private static final int NUMBER_OF_FOLLOWERS = Runtime.getRuntime().availableProcessors();

    /**
     * size of partition each follower works on
     */
    private static final int FOLLOWER_PARTITION_SIZE = 10_000;

    /**
     * Setting for the maxium number of character index entries to be created by the underlying {@link IndexedTextFileAccessor}, see also
     * {@link ItfaConfiguration}.
     */
    private static final int MAX_NUMBER_OF_CHAR_INDEX_ENTRIES = 20_000;

    /**
     * the LEADER
     */
    private PalindromeCheckLeader palindromeCheckLeader = new PalindromeCheckLeader(NUMBER_OF_FOLLOWERS, FOLLOWER_PARTITION_SIZE,
            MAX_NUMBER_OF_CHAR_INDEX_ENTRIES);

    @Test
    public void testLeaderFollowerPalindrome() throws Exception {

        // HINTS:
        // * Adjust the log-level in logback.xml to DEBUG to see the LEADER FOLLOWER working
        // * Play with the settings for PALINDROME_SIZE / NUMBER_OF_FOLLOWERS / FOLLOWER_PARTITION_SIZE and watch execution
        // time and memory consumption
        // * Palindrome creation (below) uses the same approach, try to improve it!
        // * The class IndexedTextFileAccessor used in this example internally uses Leader-Follower to improve indexing
        // performance

        // THE SURROGATES PROBLEM:
        // This implementation does not handle surrogate pairs correctly.
        // The sequence "FOO_\uD800\uDC00_BARRAB_\uD800\uDC00_OOF" is a palindrome that won't be recognized.
        // This cannot be fixed easily because partitioning is character-aligned,
        // introducing the risk of "broken surrogate pairs".
        // Means: The high surrogate character could be located in one partition
        // while the related low surrogate becomes part of the next partition.
        // In this case we have no more chance to adjust the order to detect palindromes.

        // FIXING THE SURROGATES PROBLEM:
        // For the interested reader here comes an idea to address surrogate pairs.
        // Albeit not easy the problem described above could be solved by introducing
        // a more clever (dynamic) partitioning algorithm.
        // (1) Ensure that a partition does not end after a high-surrogate (do not break pairs).
        // (2) Before comparing the characters from left and right partition, detect
        // any high surrogate on the left and exchange it with the subsequent low surrogate.
        // Logically this is a "double-switch" since the right partion gets iterated right to left.
        //
        // Example:
        // Input: FOO_\uD800\uDC00_BARRAB_\uD800\uDC00_OOF
        // Left partition: FOO_\uD800\uDC00_BAR
        // Right partition: RAB_\uD800\uDC00_OOF
        // Compare (0-0): F <-> F
        // Compare (1-1): O <-> O
        // ...
        // Compare (4-4): HIGH SURROGATE! => postpone left
        // Compare (5-4): \uDC00 <-> \uDC00
        // Compare (4-5): \uD800 <-> \uD800
        // ...
        // Compare (9-9): R <-> R
        //
        // Palindrome with surrogate pair successfully detected.
        //
        // This example shows that the partitioning algorithm is extremely important
        // when implementing this kind of LEADER-FOLLOWER scenario.
        // Partitioning can take a considerable amount of time.
        // Depending on the given requirements it is sometimes reasonable to implement
        // a simpler algorithm for performance reasons, accepting certain limitations.
        // However, it is important to document those limitations!

        File testFile = createPalindromeTestFile(PALINDROME_SIZE, null);

        LOGGER.info("Test Leader Follower Palindrome ...");
        long startTimeNanos = System.nanoTime();

        PalindromeCheckResult checkResult = palindromeCheckLeader.performPalindromeFileTest(testFile, CHARSET_NAME);

        assertEquals(PalindromeCheckResult.CONFIRMED, checkResult);

        String elapsedTimeString = TimeUtils.formatNanosAsSeconds(System.nanoTime() - startTimeNanos);
        LOGGER.info("Test Leader Follower Palindrome successful! Elapsed time: {} s", elapsedTimeString);

        testFile.delete();

    }

    @Test
    public void testLeaderFollowerNonPalindrome() throws Exception {

        int errorPositionLeft = (PALINDROME_SIZE / 4);
        int errorPositionRight = (PALINDROME_SIZE - 1) - errorPositionLeft;
        File testFile = createPalindromeTestFile(PALINDROME_SIZE, new int[] { errorPositionLeft });

        LOGGER.info("Test Leader Follower Non Palindrome ...");
        long startTimeNanos = System.nanoTime();

        PalindromeCheckResult checkResult = palindromeCheckLeader.performPalindromeFileTest(testFile, CHARSET_NAME);

        assertEquals(PalindromeCheckResult.createFailedResult(errorPositionLeft, errorPositionRight), checkResult);

        String elapsedTimeString = TimeUtils.formatNanosAsSeconds(System.nanoTime() - startTimeNanos);
        LOGGER.info("Test Leader Follower Non Palindrome successful! Elapsed time: {} s", elapsedTimeString);

        testFile.delete();

    }

    /**
     * Creates a test file for palindrome check with optional error positions. Without error positions a palindrome will be created.
     * 
     * @param size the size (in characters) of the sample to be created
     * @param errorPositions all error positions must be in the first half of the palindrome, may be null
     * @return test file
     * @throws Exception
     */
    private File createPalindromeTestFile(int size, int[] errorPositions) throws Exception {

        if (errorPositions == null) {
            errorPositions = new int[0];
        }

        int numberOfErrorPositions = errorPositions.length;
        File res = null;
        res = new File(FileUtils.getHomeDirectory(), "palindrome_test_" + CHARSET_NAME + "_" + size + "_err=" + numberOfErrorPositions + ".txt");

        LOGGER.info("Creating palindrome test file {} ...", res);

        int halfSize = size / 2;

        int numberOfThreads = Runtime.getRuntime().availableProcessors();

        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);

        int subPartitionSize = 10000;

        int partitionSize = subPartitionSize * numberOfThreads;

        try (Writer writer = new OutputStreamWriter(new BufferedOutputStream(new FileOutputStream(res), 1000000), CHARSET_NAME)) {

            if (partitionSize < halfSize) {
                partitionSize = halfSize;
            }

            int remaining = halfSize;
            int startIdx = 0;

            createPalindromeHalf(writer, executorService, remaining, startIdx, partitionSize, subPartitionSize, numberOfThreads, errorPositions);

            // if the size is an odd number, add a center character
            if (size % 2 > 0) {
                writer.append(CHARACTERS[0]);
            }

            remaining = halfSize;
            startIdx = halfSize - 1;

            createPalindromeHalf(writer, executorService, remaining, startIdx, partitionSize, subPartitionSize, numberOfThreads, null);

        }
        finally {
            try {
                executorService.shutdown();
            }
            catch (Exception ex) {
                // ignore
            }
        }
        LOGGER.info("Test file created.");

        return res;
    }

    /**
     * Creates one half of the test palindrome
     * 
     * @param writer
     * @param executorService
     * @param remaining
     * @param startIdx
     * @param partitionSize
     * @param subPartitionSize
     * @param numberOfThreads
     * @param errorPositions
     * @throws Exception
     */
    private void createPalindromeHalf(Writer writer, ExecutorService executorService, int remaining, int startIdx, int partitionSize, int subPartitionSize,
            int numberOfThreads, int[] errorPositions) throws Exception {
        int direction = startIdx == 0 ? 1 : -1;
        while (remaining > 0) {
            int currentPartitionSize = (remaining >= partitionSize ? partitionSize : remaining);
            int numberOfSubPartitions = (int) Math.ceil((double) currentPartitionSize / (double) subPartitionSize);
            int subRemaining = currentPartitionSize;
            List<PalindromeCreatorFollower> followerList = new ArrayList<>();
            CountDownLatch latch = new CountDownLatch(Math.min(numberOfThreads, numberOfSubPartitions));
            int count = 0;
            while (subRemaining > 0) {
                if (count > 0 && count % numberOfThreads == 0) {
                    latch.await();
                    for (PalindromeCreatorFollower follower : followerList) {
                        writer.write(follower.getResult());
                    }
                    writer.flush();
                    followerList.clear();
                    numberOfSubPartitions = numberOfSubPartitions - numberOfThreads;
                    latch = new CountDownLatch(Math.min(numberOfThreads, numberOfSubPartitions));
                }
                int currentSubPartitionSize = (subRemaining >= subPartitionSize ? subPartitionSize : subRemaining);
                PalindromeCreatorFollower follower = new PalindromeCreatorFollower(startIdx, startIdx + (direction * currentSubPartitionSize), errorPositions,
                        latch);
                followerList.add(follower);
                executorService.execute(follower);
                startIdx = startIdx + (direction * currentSubPartitionSize);
                subRemaining = subRemaining - currentSubPartitionSize;
                count++;
            }
            remaining = remaining - currentPartitionSize;
            latch.await();
            for (PalindromeCreatorFollower follower : followerList) {
                writer.write(follower.getResult());
            }
        }

    }

    /**
     * Creating large palindromes took way to long, so I decided to use Leader-Follower again for creation. :-)
     */
    private class PalindromeCreatorFollower implements Runnable {

        final int fromIdx;

        final int toIdx;

        final int[] errorPositions;

        final CountDownLatch latch;

        char[] buffer;

        public PalindromeCreatorFollower(int fromIdx, int toIdx, int[] errorPositions, CountDownLatch latch) {
            this.fromIdx = fromIdx;
            this.toIdx = toIdx;
            this.errorPositions = errorPositions;
            this.latch = latch;
        }

        @Override
        public void run() {

            int len = Math.abs(toIdx - fromIdx);
            char[] localBuffer = new char[len];

            int numberOfErrorPositions = (errorPositions == null ? 0 : errorPositions.length);
            int delta = 1;
            if (fromIdx > toIdx) {
                delta = -1;
            }

            for (int i = 0; i < len; i++) {
                int charIdx = fromIdx + (i * delta);
                if (numberOfErrorPositions > 0) {
                    for (int j = 0; j < numberOfErrorPositions; j++) {
                        if (charIdx == errorPositions[j]) {
                            charIdx++;
                            break;
                        }
                    }
                }
                charIdx = charIdx % NUMBER_OF_CHARACTERS;
                localBuffer[i] = CHARACTERS[charIdx];
            }
            synchronized (this) {
                this.buffer = localBuffer;
                latch.countDown();
            }
        }

        public char[] getResult() {
            synchronized (this) {
                return this.buffer;
            }
        }

    }

}
