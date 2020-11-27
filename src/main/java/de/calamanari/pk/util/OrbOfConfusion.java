//@formatter:off
/*
 * Orb Of Confusion
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
package de.calamanari.pk.util;

import java.util.Arrays;

/**
 * This class implements the "Orb Of Confusion" algorithm which performs a shuffled partition index transformation on an arbitrary range of positive
 * numbers.<br>
 * <ul>
 * <li><i>Objective:</i> Map bijectively any number of a given range to another number of the same range where the mappings are not easy to be guessed. Thus a
 * sequence of numbers (i.e. a database sequence) can be transformed duplicate-free on-the-fly.</li>
 * <li><i>Application:</i> serial numbers, voucher codes and other application of blurring numbers.</li>
 * <li><i>Idea:</i> Use pre-shuffled small partitions to virtually shuffle the whole range.<br>
 * Provide a method to quickly find the mapped value for any number in the range regardless how big this range may be ( {@link Long#MAX_VALUE} is the maximum
 * upper bound).</li>
 * <li><i>Function:</i> Actually for a given number we partition the range in {@link #MAX_PART_COUNT} partitions and choose one.<br>
 * Using the pre-shuffled partitions we map the partition to another partition and add the remainder. This is done {@link #TRANSFORMATION_STEPS} times using
 * different partition counts on each run.</li>
 * <li><i>Advantage:</i> Easy to implement and understand, state/configuration can be hold externally, no repetition before every number of the range occurred.
 * Last not least: a typical user (without super-natural powers) won't suspect an interrelation or even be able to guess the next value.</li>
 * <li><i>Disadvantages:</i> The randomness of the created shuffled range values is probably not very good and of course not scientifically examined. ;-)</li>
 * </ul>
 * <br>
 * By the way: the name "Orb Of Confusion" is a reminiscence to a Sponge Bob TV-episode and reminds us of the fact that we try to spread disorder over a range
 * of numbers.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class OrbOfConfusion {

    /**
     * maximum partition count (we use 11 partitions, starting with the largest one)
     */
    private static final int MAX_PART_COUNT = 31;

    /**
     * number of partition sets (transformation steps)<br>
     * <i>value:</i> {@value}
     */
    public static final int TRANSFORMATION_STEPS = 11;

    /**
     * The first {@link #TRANSFORMATION_STEPS} prime numbers
     */
    private static final int[] PRIMES = new int[] { 2, 3, 5, 7, 11, 13, 17, 19, 23, 29, 31 };

    /**
     * For each count of partitions we hold a pre-shuffled mapping, this is a default one
     */
    private static final int[][] DEFAULT_PARTITIONS = new int[TRANSFORMATION_STEPS][];
    static {
        DEFAULT_PARTITIONS[0] = new int[] { 1, 0 }; // prime=2
        DEFAULT_PARTITIONS[1] = new int[] { 1, 2, 0 }; // prime=3
        DEFAULT_PARTITIONS[2] = new int[] { 0, 3, 2, 4, 1 }; // prime=5
        DEFAULT_PARTITIONS[3] = new int[] { 3, 4, 0, 5, 6, 2, 1 }; // prime=7
        DEFAULT_PARTITIONS[4] = new int[] { 8, 9, 4, 7, 3, 10, 0, 2, 5, 6, 1 }; // prime=11
        DEFAULT_PARTITIONS[5] = new int[] { 12, 6, 10, 11, 3, 1, 2, 7, 4, 5, 0, 8, 9 }; // prime=13
        DEFAULT_PARTITIONS[6] = new int[] { 1, 0, 12, 6, 9, 11, 13, 14, 3, 15, 10, 4, 5, 16, 8, 2, 7 }; // prime=17
        DEFAULT_PARTITIONS[7] = new int[] { 6, 15, 13, 2, 1, 17, 5, 18, 4, 3, 0, 14, 7, 8, 10, 11, 9, 16, 12 }; // prime=19
        DEFAULT_PARTITIONS[8] = new int[] { 12, 7, 3, 18, 5, 16, 22, 13, 15, 11, 1, 6, 4, 2, 0, 17, 21, 20, 9, 19, 8, 14, 10 }; // prime=23
        DEFAULT_PARTITIONS[9] = new int[] { 20, 1, 0, 15, 23, 22, 25, 21, 28, 16, 19, 12, 26, 11, 4, 3, 2, 27, 10, 5, 8, 6, 18, 17, 24, 9, 13, 7, 14 }; // prime=29
        DEFAULT_PARTITIONS[10] = new int[] { 9, 23, 24, 20, 28, 12, 26, 19, 21, 27, 22, 10, 4, 17, 18, 14, 29, 8, 25, 6, 15, 11, 0, 30, 7, 13, 16, 1, 2, 3, 5 }; // prime=31
    }

    /**
     * Returns default shuffled partitions
     * 
     * @param sourcePartitions shuffled partitions set to be copied (must be a valid partition, no checks here)
     * @return array of {@link #TRANSFORMATION_STEPS} shuffled index arrays having the length of the first {@link #TRANSFORMATION_STEPS} primes.
     */
    public static int[][] copyPartitions(int[][] sourcePartitions) {
        int[][] partitions = new int[TRANSFORMATION_STEPS][];
        for (int i = 0; i < TRANSFORMATION_STEPS; i++) {
            int[] sourcePartition = sourcePartitions[i];
            int[] dest = new int[sourcePartition.length];
            System.arraycopy(sourcePartition, 0, dest, 0, sourcePartition.length);
            partitions[i] = dest;
        }
        return partitions;
    }

    /**
     * Returns default shuffled partitions
     * 
     * @return array of {@link #TRANSFORMATION_STEPS} shuffled index arrays having the length of the first {@link #TRANSFORMATION_STEPS} primes.
     */
    public static int[][] createDefaultPartitions() {
        return copyPartitions(DEFAULT_PARTITIONS);
    }

    /**
     * Because partitions structure must follow strict rules, we provide a check method.
     * 
     * @param partitions to be validated
     */
    public static final void checkPartitions(int[][] partitions) {
        if (partitions == null) {
            throw new IllegalArgumentException("Argument partitions must not be null.");
        }
        if (partitions.length < TRANSFORMATION_STEPS) {
            throw new IllegalArgumentException("Argument partitions must at least have " + TRANSFORMATION_STEPS + " elements.");
        }
        for (int i = 0; i < TRANSFORMATION_STEPS; i++) {
            int[] partition = partitions[i];
            if (partition == null) {
                throw new IllegalArgumentException("partitions" + "[" + i + "] was null.");
            }
            if (partition.length != PRIMES[i]) {
                throw new IllegalArgumentException("partitions" + "[" + i + "] contains " + partition.length + " elements (expected " + PRIMES[i] + ").");
            }
            checkPartitionDetails(i, partition);
        }
    }

    private static void checkPartitionDetails(int i, int[] partition) {
        for (int j = 0; j < partition.length; j++) {
            boolean ok = false;
            for (int k = 0; k < partition.length; k++) {
                if (partition[k] == j) {
                    ok = true;
                    break;
                }
            }
            if (!ok) {
                StringBuilder sb = new StringBuilder();
                for (int k = 0; k < partition.length; k++) {
                    if (k > 0) {
                        sb.append(", ");
                    }
                    sb.append(partition[k]);
                }
                throw new IllegalArgumentException(
                        "partitions[" + i + "][]{" + sb.toString() + "} is no valid shuffled index partition (missing index=" + j + ").");
            }
        }
    }

    /**
     * Calculates the remainder for two long operands.
     * 
     * @param numerator operand
     * @param divisor operand
     * @return the remainder when calculating numerator over divisor
     */
    private static final long remainder(long numerator, long divisor) {
        return numerator - ((numerator / divisor) * divisor);
    }

    /**
     * Maps the input value to the range [0..limit] (limit excluded) using the specified pre-shuffled partition mapping.
     * 
     * @param input arbitrary value less than limit (!)
     * @param partitions array of {@link #TRANSFORMATION_STEPS} shuffled index arrays having the length of the first {@link #TRANSFORMATION_STEPS} primes.
     * @param limit upper bound of range (excl.)
     * @param partitionIdx index of pre-shuffled mapping partition
     * @return value in target range
     */
    private static long mapToPartition(long input, int[][] partitions, long limit, int partitionIdx) {

        // the partition count is always a prime
        int partCount = PRIMES[partitionIdx];

        // flip even/odd to lower/higher half of target range (
        if (remainder(input, 2) == 0) {
            input = input / 2;
        }
        else {
            input = (limit - 1) - (input / 2);
        }

        long output = 0;

        long partSize = limit / partCount;
        long upperBound = partSize * partCount;

        if (limit <= partCount || input >= upperBound) {
            // nothing to do
            // if limit is less than partition count, we cannot partition
            // if input is after last partition we cannot transform
            output = input;
        }
        else {
            // choose partition
            // swap partition using pre-shuffled index
            // go to partition and add remainder
            output = partSize * partitions[partitionIdx][(int) (input / partSize)] + remainder(input, partSize);
        }
        return output;
    }

    /**
     * Transforms the input value to a value in the specified range. Guarantees all numbers of the specified range to occur once before the first number will be
     * repeated for when calling this method n times with input = n.<br>
     * <i>Note:</i> The {@link #transform(long)} delegates to this method internally.
     * 
     * @param input arbitrary positive value
     * @param partitions array of {@link #TRANSFORMATION_STEPS} shuffled index arrays having the length of the first {@link #TRANSFORMATION_STEPS} primes.
     * @param salt constant positive value which influences code generation (may be 0)
     * @param start begin of target range (incl.)
     * @param end limit of target range (excl.)
     * @return value in target range
     */
    public static long transform(long input, int[][] partitions, long salt, long start, long end) {
        if (start < 0 || end <= start) {
            throw new IllegalArgumentException("start=" + start + ", end=" + end + "(expected: 0 <= start < end)");
        }
        if (salt < 0) {
            throw new IllegalArgumentException("salt=" + salt + " (expected: salt >= 0)");
        }
        if (input < 0) {
            throw new IllegalArgumentException("input=" + input + " (expected: input >= 0)");
        }
        long limit = end - start;

        if (salt > 0) {
            input = input - salt;
            if (input < 0) {
                input = Long.MAX_VALUE + input;
            }
        }

        input = remainder(input, limit);

        if (limit >= MAX_PART_COUNT) {
            // "remainder swapping"
            // if the range can be partitioned
            // swap all elements after the last partition (remainders) with
            // chosen elements of the partitions
            long partSize = limit / MAX_PART_COUNT;
            long upperBound = partSize * MAX_PART_COUNT;

            if (input >= upperBound) {
                input = (input - upperBound) * partSize;
            }
            else if (remainder(input, partSize) == 0) {
                long swap = (upperBound + (input / partSize));
                if (swap < limit) {
                    input = swap;
                }
            }
        }
        long output = input;
        for (int i = TRANSFORMATION_STEPS - 1; i > -1; i--) {
            output = mapToPartition(output, partitions, limit, i);
        }
        return start + output; // move into range
    }

    /**
     * For each count of partitions we hold a pre-shuffled mapping
     */
    private final int[][] partitions;

    /**
     * influences mapping when transforming numbers
     */
    private final long salt;

    /**
     * range start (min target number)
     */
    private final long rangeStart;

    /**
     * end of target number range (excl.)
     */
    private final long rangeEnd;

    /**
     * Default configuration with default partitions, no salt and default range [0..Long.MAX_VALUE).
     */
    public OrbOfConfusion() {
        this.partitions = createDefaultPartitions();
        checkPartitions(this.partitions);
        this.salt = 0;
        this.rangeStart = 0;
        this.rangeEnd = Long.MAX_VALUE;
    }

    /**
     * Creates new configuration with the given partitions and salt.
     * 
     * @param partitions array of {@link #TRANSFORMATION_STEPS} shuffled index arrays having the length of the first {@link #TRANSFORMATION_STEPS} primes.
     * @param salt 0 or positive value to influence number transformation
     * @param start begin of target range (incl.)
     * @param end limit of target range (excl.)
     */
    public OrbOfConfusion(int[][] partitions, long salt, long start, long end) {
        this.partitions = (partitions == null ? null : Arrays.copyOf(partitions, partitions.length));
        checkPartitions(this.partitions);
        if (salt < 0) {
            throw new IllegalArgumentException("salt=" + salt + " (expected: salt >= 0)");
        }
        if (start < 0 || end <= start) {
            throw new IllegalArgumentException("start=" + start + ", end=" + end + " (expected: 0 <= start < end)");
        }
        this.salt = salt;
        this.rangeStart = start;
        this.rangeEnd = end;
    }

    /**
     * Returns a copy of the currently used partition set.
     * 
     * @return array of {@link #TRANSFORMATION_STEPS} shuffled index arrays having the length of the first {@link #TRANSFORMATION_STEPS} primes.
     */
    public int[][] getPartitions() {
        return copyPartitions(partitions);
    }

    /**
     * Returns end (excl.) of target numbers range
     * 
     * @return range limit
     */
    public long getRangeEnd() {
        return rangeEnd;
    }

    /**
     * Returns lower bound (incl.) of target numbers range
     * 
     * @return range minimum
     */
    public long getRangeStart() {
        return rangeStart;
    }

    /**
     * Returns the salt value for transformation
     * 
     * @return salt value (greater or equal to 0)
     */
    public long getSalt() {
        return salt;
    }

    /**
     * Transforms the input value to a value in the configured range.
     * 
     * @param input arbitrary positive value
     * @return value in target range
     */
    public long transform(long input) {
        return transform(input, partitions, salt, rangeStart, rangeEnd);
    }

}
