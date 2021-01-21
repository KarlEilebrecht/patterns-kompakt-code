//@formatter:off
/*
 * Partitioner
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
package de.calamanari.other.ohbf;

import static de.calamanari.pk.util.LambdaSupportLoggerProxy.defer;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.calamanari.pk.util.LambdaSupportLoggerProxy;

/**
 * The {@link Partitioner} tries to find an optimal partion setup, see {@link #computePartitions(long, int)}
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class Partitioner {

    private static final Logger LOGGER = LambdaSupportLoggerProxy.wrap(LoggerFactory.getLogger(Partitioner.class));

    /**
     * we allow partitions from 2 up to 2^{@value #NUMBER_OF_VALID_PARTITION_SIZES} elements
     */
    private static final int NUMBER_OF_VALID_PARTITION_SIZES = 29;

    /**
     * we allow partitions from 2 up to 2^{@value #NUMBER_OF_VALID_PARTITION_SIZES} elements
     */
    private static final int[] VALID_PARTITION_SIZES;
    static {
        int[] sizes = new int[NUMBER_OF_VALID_PARTITION_SIZES + 1];
        for (int i = 1; i <= NUMBER_OF_VALID_PARTITION_SIZES; i++) {
            sizes[i] = (int) Math.pow(2, i);
        }
        VALID_PARTITION_SIZES = sizes;
        LOGGER.debug("Valid partion sizes: {}", defer(() -> Arrays.toString(VALID_PARTITION_SIZES)));
    }

    /**
     * Returns the closest partition size that is &gt;= size
     * @param size the size to be covered
     * @return index
     * @throws IllegalArgumentException if the partition size could not be met
     */
    private int findNextSizeIdx(int size) {
        for (int i = 1; i <= NUMBER_OF_VALID_PARTITION_SIZES; i++) {
            if (VALID_PARTITION_SIZES[i] >= size) {
                return i;
            }
        }
        throw new IllegalArgumentException("Can't find a valid partition size index for " + size);
    }

    /**
     * Performs a recursive optimization of the partitions from left to right.
     * @param desiredSize total size (including all partitions)
     * @param currentSizeIndexes the partitions, each size index represents a partition of a certain size
     * @param currentWaste the best (minimum warte) we could achieve so far, trying to get better
     * @param start where to start optimization (partitions to the left considered final)
     * @return optimization result or currentSizeIndexes if there was no better solution
     */
    private int[] optimize(long desiredSize, int[] currentSizeIndexes, long currentWaste, int start) {

        OptimizationData run = new OptimizationData();

        run.currentWaste = currentWaste;
        run.desiredSize = desiredSize;
        run.numberOfPartitions = currentSizeIndexes.length;
        run.bestResult = currentSizeIndexes;
        run.sizeIndexes = Arrays.copyOf(currentSizeIndexes, currentSizeIndexes.length);
        run.start = start;
        run.leadSizeIdx = run.sizeIndexes[run.start];
        if (start >= run.numberOfPartitions) {
            // ignore
        }
        else if (start == run.numberOfPartitions - 1) {
            // last partition reached (right end), here we can only optimize by diminishing this partition
            minimizeLastPartition(run);
        }
        else {
            // when optimizing subsequent partions we need to compute the size of the ignored partitions on the left
            // to later calculate the correct total size
            run.sumBefore = 0;
            for (int i = 0; i < start; i++) {
                run.sumBefore = run.sumBefore + VALID_PARTITION_SIZES[run.sizeIndexes[i]];
            }
            performBumpedLeadSubsequenceOptimization(run);
        }
        return run.bestResult;
    }

    /**
     * The idea is that the current set of partitions (of equal size) is bigger than the desired size (guaranteed by the steps before) and we want to reduce the
     * waste by finding a setup of partitions that fits better.
     * <p>
     * This method therefore plays with the lead (first partition counting from the left), computes the average size for the remaining partitions and then
     * applies the logic again to the partitions on the right (after the lead).
     * <p>
     * The algorithm starts with the lead as is and then tries to bump it to affect the subsequent partition sizes.
     * @param run data of the current run
     */
    private void performBumpedLeadSubsequenceOptimization(OptimizationData run) {
        run.leadSizeIdxUpperBound = (run.start == 0 ? VALID_PARTITION_SIZES.length : (run.sizeIndexes[run.start - 1] + 1));
        for (run.newLeadSizeIdx = run.leadSizeIdx; run.newLeadSizeIdx < run.leadSizeIdxUpperBound; run.newLeadSizeIdx++) {
            long remainder = run.desiredSize - run.sumBefore - VALID_PARTITION_SIZES[run.newLeadSizeIdx];
            run.avgSize = (int) Math.ceil(((double) remainder) / (run.numberOfPartitions - run.start - 1));
            if (run.avgSize > 0 && optimizeSubsequentPartitionsBasedOnAverage(run)) {
                // stop the search, because the result is optimal
                break;
            }
        }
    }

    /**
     * Now we set all subsequent partitions to the computed average and concentrate on this subset of partitions.
     * @param run data of the current run
     * @return true if we found an optimal result (waste <= 1)
     */
    private boolean optimizeSubsequentPartitionsBasedOnAverage(OptimizationData run) {
        int avgSizeIdx = findNextSizeIdx(run.avgSize);
        run.sizeIndexes[run.start] = run.newLeadSizeIdx;
        Arrays.fill(run.sizeIndexes, run.start + 1, run.sizeIndexes.length, avgSizeIdx);
        long waste = calculateWaste(run.sizeIndexes, run.desiredSize);
        if (waste > 1) {
            int[] optimizedSizeIndexes = optimize(run.desiredSize, run.sizeIndexes, Math.min(waste, run.currentWaste), run.start + 1);
            long optimizedWaste = calculateWaste(optimizedSizeIndexes, run.desiredSize);
            if (optimizedWaste <= 1 || optimizedWaste < calculateWaste(run.bestResult, run.desiredSize)) {
                run.bestResult = optimizedSizeIndexes;
                if (optimizedWaste <= 1) {
                    return true;
                }
            }
        }
        else {
            run.bestResult = run.sizeIndexes;
            return true;
        }
        return false;
    }

    /**
     * If there is only one partition on the right, we can only try to diminish it
     * @param run data of the current run
     */
    private void minimizeLastPartition(OptimizationData run) {
        for (int decreasedIdx = run.leadSizeIdx; decreasedIdx > 0; decreasedIdx--) {
            run.sizeIndexes[run.start]--;
            if (run.sizeIndexes[run.start] < 1 || calculateWaste(run.sizeIndexes, run.desiredSize) < 0) {
                run.sizeIndexes[run.start]++;
                break;
            }
        }
        if (calculateWaste(run.sizeIndexes, run.desiredSize) < run.currentWaste) {
            run.bestResult = run.sizeIndexes;
        }
    }

    /**
     * Tries to find an optimal set of partitions to fit the desired size. Each partition's size is 2^m, m in range [1..29]. <br/>
     * The returned array does not contain the absolute size of the partitions but the powers, means [m1,m2,m3, ...]. <br/>
     * The same m (and thus partition size) can occur multiple times.
     * @param desiredSize total size all partitions together shall reach
     * @param numberOfPartitions requested number of partitions (fixed)
     * @return m-array
     */
    public int[] computePartitions(long desiredSize, int numberOfPartitions) {
        int[] res = new int[numberOfPartitions];
        int avgSize = (int) Math.ceil(((double) desiredSize) / numberOfPartitions);
        int sizeIdx = findNextSizeIdx(avgSize);
        Arrays.fill(res, sizeIdx);
        long waste = calculateWaste(res, desiredSize);
        if (waste > 1) {
            res = optimize(desiredSize, res, waste, 0);
        }
        return res;
    }

    /**
     * The waste-function to compare different partition setups
     * @param sizeIndexes partitions (m-values)
     * @param limit total size to compare
     * @return the difference between desired size and the total size of the partition setup. 0/1 is optimal, a negative value indicates a bad setup
     */
    public static long calculateWaste(int[] sizeIndexes, long limit) {
        long size = 0;
        for (int i = 0; i < sizeIndexes.length; i++) {
            size = size + VALID_PARTITION_SIZES[sizeIndexes[i]];
        }
        long res = size - limit;

        LOGGER.debug("Waste calculation (limit = {}): {} -> {}", limit, defer(() -> Arrays.toString(sizeIndexes)), res);
        return res;
    }

    /**
     * Parameter container to avoid huge parameter lists
     *
     */
    private class OptimizationData {

        /**
         * the sum of partition sizes to the left (outside current optimization area)
         */
        long sumBefore;

        /**
         * Don't bump subsequent indexes higher (does not make sense from left to right)
         */
        int leadSizeIdxUpperBound;

        /**
         * the partition setup
         */
        int[] sizeIndexes;

        /**
         * average size computed as start size for subsequent partions on the right
         */
        int avgSize;

        /**
         * desired total size (goal)
         */
        long desiredSize;

        /**
         * The index (on the left) we have fixed to concentrate on the partitions to the right
         */
        int leadSizeIdx;

        /**
         * Computed (increased) updated lead size index
         */
        int newLeadSizeIdx;

        /**
         * Position in the sizeIndexes array where to start optimization
         */
        int start;

        /**
         * Waste that was computed for the best setup (the smaller the better) before calling this subsequent optimization
         */
        long currentWaste;

        /**
         * currently best result that could befound in this run
         */
        int[] bestResult;

        /**
         * fixed number of partitions
         */
        int numberOfPartitions;

    }
}
