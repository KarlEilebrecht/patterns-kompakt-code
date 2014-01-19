/*
 * Palindrome Check Slave Task - demonstrates MASTER SLAVE
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
package de.calamanari.pk.masterslave;

import java.io.IOException;
import java.io.Reader;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.calamanari.pk.util.itfa.IndexedTextFileAccessor;

/**
 * Palindrome Check Slave Task - executes a SLAVE's work in this MASTER-SLAVE example.<br>
 * An instance runs in a separate thread and sends its result to his MASTER.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class PalindromeCheckSlaveTask implements Runnable {

    /**
     * logger
     */
    public static final Logger LOGGER = Logger.getLogger(PalindromeCheckSlaveTask.class.getName());

    /**
     * test result: the source was a palindrome
     */
    private static final int[] PALINDROME_CONFIRMED = new int[] { -1, -1 };

    /**
     * start of the first partition (incl.), character position in source file
     */
    private final long startIdx1;

    /**
     * start of the second partition (incl.), character position in source file
     */
    private final long startIdx2;

    /**
     * length of partitions
     */
    private final int partitionSize;

    /**
     * the source, for loading the partition data
     */
    private final IndexedTextFileAccessor textFileAccessor;

    /**
     * future for communication from slave to master
     */
    private final PalindromeCheckFuture future;

    /**
     * Creates a new slave task comparing two partitions start1-end1 vs. end2-start2
     * @param textFileAccessor source
     * @param startIdx1 start of first partition (incl.), character position in source file
     * @param startIdx2 start of second partition (incl.), character position in source file
     * @param partitionSize size of partitions (number of characters)
     * @param future the slave reports a partial result using this future
     */
    public PalindromeCheckSlaveTask(IndexedTextFileAccessor textFileAccessor, long startIdx1, long startIdx2,
            int partitionSize, PalindromeCheckFuture future) {
        this.textFileAccessor = textFileAccessor;
        this.startIdx1 = startIdx1;
        this.startIdx2 = startIdx2;
        this.partitionSize = partitionSize;
        this.future = future;
        LOGGER.fine(this.getClass().getSimpleName() + "({startIdx1=" + startIdx1 + ", startIdx2=" + startIdx2
                + ", partitionSize=" + partitionSize + "}) created.");
    }

    @Override
    public void run() {
        PalindromeCheckResult result = PalindromeCheckResult.UNKNOWN;
        try {
            if (!future.isAborted()) {
                LOGGER.fine("SLAVE-Thread @" + Integer.toHexString(Thread.currentThread().hashCode()) + " executes "
                        + this.getClass().getSimpleName() + "({startIdx1=" + startIdx1 + ", startIdx2=" + startIdx2
                        + ", partitionSize=" + partitionSize + "}).");
                char[] partition1 = getPartition(startIdx1);
                char[] partition2 = getPartition(startIdx2);
                int[] partialResult = performPalindromeTest(partition1, partition2);
                if (partialResult == PALINDROME_CONFIRMED) {
                    result = PalindromeCheckResult.CONFIRMED;
                }
                else {
                    result = PalindromeCheckResult.createFailedResult(startIdx1 + partialResult[0], startIdx2
                            + partialResult[1]);
                }
            }
            else {
                LOGGER.fine("SLAVE-Thread @" + Integer.toHexString(Thread.currentThread().hashCode()) + " skips "
                        + this.getClass().getSimpleName() + "({startIdx1=" + startIdx1 + ", startIdx2=" + startIdx2
                        + ", partitionSize=" + partitionSize + "}).");
            }
        }
        catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Error while checking partitions [" + startIdx1 + ", "
                    + (startIdx1 + partitionSize) + ") vs. [" + startIdx2 + ", " + (startIdx2 + partitionSize) + ")",
                    ex);
            // tell the MASTER about the problem
            result = PalindromeCheckResult.ERROR;
        }
        finally {
            LOGGER.fine("SLAVE-Thread @" + Integer.toHexString(Thread.currentThread().hashCode())
                    + " reports to MASTER via Future: " + result.toString());
            future.reportSlaveResult(result);
        }
    }

    /**
     * Reads the requested part from the underlying file and returns it.
     * @param startIdx character position where to start reading
     * @return partition from file
     * @throws Exception on any error
     */
    private char[] getPartition(long startIdx) throws Exception {
        char[] partition = new char[partitionSize];
        try (Reader reader = textFileAccessor.createInputStreamReaderAtChar(startIdx, partitionSize)) {

            int lastRead = 0;
            int totalRead = 0;
            do {
                lastRead = reader.read(partition, totalRead, partitionSize - totalRead);
                totalRead = totalRead + lastRead;
            } while (lastRead > 0 && totalRead < partitionSize);

            if (totalRead != partitionSize) {
                throw new IOException("Unable to read partition at " + startIdx);
            }

        }
        return partition;
    }

    /**
     * This method tests whether the first partition equals the reversed second partition.<br>
     * Both partitions must have the same length = this.partitionSize.
     * @param partition1 first partition, not null, not empty
     * @param partition2 second partition, not null, same length as first partition
     * @return 2-element array with the positions that do not match or {@link #PALINDROME_CONFIRMED}
     */
    private int[] performPalindromeTest(char[] partition1, char[] partition2) {
        int[] result = PALINDROME_CONFIRMED;
        for (int i = 0; i < partitionSize; i++) {
            int pos1 = i;
            int pos2 = (partitionSize - 1) - i;
            if (partition1[pos1] != partition2[pos2]) {
                // no palindrome!
                result = new int[] { pos1, pos2 };
                break;
            }
        }
        return result;
    }

}
