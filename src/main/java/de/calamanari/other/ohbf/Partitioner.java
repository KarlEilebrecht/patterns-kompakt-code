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
 * The {@link Partitioner} tries to find a suitable partion setup, see {@link #computePartitions(long, int)}
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class Partitioner {

    private static final Logger LOGGER = LambdaSupportLoggerProxy.wrap(LoggerFactory.getLogger(Partitioner.class));

    /**
     * we allow partitions from 2 up to 2^{@value #NUMBER_OF_VALID_PARTITION_SIZES} elements
     */
    private static final int NUMBER_OF_VALID_PARTITION_SIZES = 31;

    /**
     * we allow partitions from 2 up to 2^{@value #NUMBER_OF_VALID_PARTITION_SIZES} elements
     */
    private static final long[] VALID_PARTITION_SIZES;
    static {
        long[] sizes = new long[NUMBER_OF_VALID_PARTITION_SIZES + 1];
        for (int i = 1; i <= NUMBER_OF_VALID_PARTITION_SIZES; i++) {
            sizes[i] = (long) Math.pow(2, i);
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
     * Computes set of partitions to fit the desired size, if possible all of the same size (average). Each partition's size is 2^m, m in range [1..29]. <br/>
     * The returned array does not contain the absolute size of the partitions but the powers, means [m1,m2,m3, ...]. <br/>
     * The same m (and thus partition size) can occur multiple times.
     * @param desiredSize total size all partitions together shall reach
     * @param numberOfPartitions requested number of partitions (fixed)
     * @return m-array
     */
    public int[] computePartitions(long desiredSize, int numberOfPartitions) {
        int[] res = computeAvgSizePartitions(desiredSize, numberOfPartitions);
        int candidate = 0;
        while (calculateWaste(res, desiredSize) < 0) {
            res[candidate]++;
            candidate++;
        }
        LOGGER.debug("Partitions: {}", defer(() -> Arrays.toString(res)));
        return res;
    }

    /**
     * Computes set of partitions to fit <b>&lt;= desired size</b>, all of the same size (average), where the partition's size is 2^m, m in range [1..29]. <br/>
     * The returned array does not contain the absolute size of the partitions but the powers, means [m1,m2,m3, ...]. <br/>
     * The same m (and thus partition size) can occur multiple times.
     * @param desiredSize total size all partitions together shall reach
     * @param numberOfPartitions requested number of partitions (fixed)
     * @return m-array
     */
    public int[] computeAvgSizePartitions(long desiredSize, int numberOfPartitions) {
        int[] res = new int[numberOfPartitions];
        int avgSize = (int) Math.ceil(((double) desiredSize) / numberOfPartitions);
        int sizeIdx = findNextSizeIdx(avgSize);
        Arrays.fill(res, sizeIdx - 1);
        LOGGER.debug("Average size partitions: {}", defer(() -> Arrays.toString(res)));
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

}
