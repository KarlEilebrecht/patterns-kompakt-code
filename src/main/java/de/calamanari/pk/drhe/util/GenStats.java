//@formatter:off
/*
 * GenStats64 - provides distance metrics for 64-bit numbers
 * Code-Beispiel zum Buch Patterns Kompakt, Verlag Springer Vieweg
 * Copyright 2024 Karl Eilebrecht
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
package de.calamanari.pk.drhe.util;

import java.math.BigInteger;
import java.util.Comparator;

/**
 * Analytics tool to collect information about distances of subsequently computed values.
 * <p/>
 * We track the distance between the source and the computed value as well as the distances between subsequently computed values.
 * <p>
 * This implementation avoids {@link BigInteger} during the collection phase for performance reasons.<br/>
 * Nevertheless, it is capable of handling the full Long-range.
 * <p>
 * Due to its comprehensive internal state, an instance of {@link GenStats} must not be accessed concurrently by multiple threads.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class GenStats {

    /**
     * {@value Long#MAX_VALUE} as BigInteger
     */
    private static final BigInteger OVERFLOW = BigInteger.valueOf(Long.MAX_VALUE);

    /**
     * {@value Long#MAX_VALUE} + 1 as BigInteger
     */
    private static final BigInteger OVERFLOW_PLUS_ONE = OVERFLOW.add(BigInteger.ONE);

    /**
     * Ignore transition distances if the sign changes
     */
    private final boolean ignoreSignTransitionSuccessorDistance;

    /**
     * number of values seen so far
     */
    private long count = 0;

    /**
     * number of successors (for average computation)
     */
    private long countSuc = 0;

    /**
     * last computed value
     */
    private long prevDestValue = 0;

    /**
     * number of source values mapped to themselves
     */
    private long selfMappedCount = 0;

    /**
     * sum of all distances between source value and computed value
     */
    private long srcDistSum = 0;

    /**
     * number of overflows while summing all distances
     */
    private long srcDistSumOF = 0;

    /**
     * minimum distance between source value and encoded value (start value beyond max)
     */
    private long minSrcDist = -1;

    /**
     * overflow flag for minimum distance between source value and computed value
     */
    private boolean minSrcDistOF = false;

    /**
     * maximum distance between source value and encoded value (start value below min)
     */
    private long maxSrcDist = -1;

    /**
     * overflow flag for maximum distance between source value and computed value
     */
    private boolean maxSrcDistOF = false;

    /**
     * sum of all distances between subsequently computed values
     */
    private long sucDistSum = 0;

    /**
     * number of overflows while summing all distances
     */
    private long sucDistSumOF = 0;

    /**
     * minimum distance between subsequent computed values
     */
    private long minSucDist = -1;

    /**
     * overflow flag for minimum distance between two subsequently computed values
     */
    private boolean minSucDistOF = false;

    /**
     * maximum distance between subsequent encoded values
     */
    private long maxSucDist = -1;

    /**
     * overflow flag for maximum distance between two subsequently computed values
     */
    private boolean maxSucDistOF = false;

    /**
     * When we analyze sign-preserving computation methods across negative and positive values we want to ignore the transition point (last value negative, next
     * 0 or positive) when computing the average successor distance. This behavior can be controlled using this flag.
     * <p>
     * <b>Clarification:</b> This only affects transitions from negative to positive or vice-versa. Let's say your input is
     * <code>..., -3, -2, -1, 0, 1, 2, 3, ...</code> then in sign-preserving mode your corresponding output could be anything like <code>..., -18363527, -6,
     * -78992876, 928362, 6, 928372, ...</b>, so we should ignore the transition event (input -1, 0) to avoid computing a wrong average.
     * 
     * @param ignoreSignTransitionSuccessorDistance behavior control flag
     */
    public GenStats(boolean ignoreSignTransitionSuccessorDistance) {
        this.ignoreSignTransitionSuccessorDistance = ignoreSignTransitionSuccessorDistance;
    }

    /**
     * Creates a new instance with <code>ignoreSignTransitionSuccessorDistance=false;</code>
     */
    public GenStats() {
        this(false);
    }

    /**
     * @return total number of values
     */
    public long getCount() {
        return count;
    }

    /**
     * @return number of computed values which equal the computed ones
     */
    public long getSelfMappedCount() {
        return selfMappedCount;
    }

    /**
     * @return minimum distance between a source value and the computed one
     */
    public BigInteger getMinSrcDist() {
        BigInteger res = BigInteger.valueOf(minSrcDist);
        if (minSrcDistOF) {
            res = res.add(OVERFLOW_PLUS_ONE);
        }
        return res;
    }

    /**
     * @return maximum distance between a source value and the computed one
     */
    public BigInteger getMaxSrcDist() {
        BigInteger res = BigInteger.valueOf(maxSrcDist);
        if (maxSrcDistOF) {
            res = res.add(OVERFLOW_PLUS_ONE);
        }
        return res;
    }

    /**
     * @return average distance between a source value and the computed one
     */
    public BigInteger getAvgSrcDist() {
        if (minSrcDist < 0) {
            return BigInteger.valueOf(minSrcDist);
        }
        BigInteger sumTotal = BigInteger.valueOf(srcDistSum);
        if (srcDistSumOF > 0) {
            sumTotal = sumTotal.add(OVERFLOW.multiply(BigInteger.valueOf(srcDistSumOF)));
        }
        return sumTotal.divide(count == 0 ? BigInteger.ONE : BigInteger.valueOf(count));
    }

    /**
     * @return minimum distance between two subsequently computed values
     */
    public BigInteger getMinSucDist() {
        BigInteger res = BigInteger.valueOf(minSucDist);
        if (minSucDistOF) {
            res = res.add(OVERFLOW_PLUS_ONE);
        }
        return res;
    }

    /**
     * @return maximum distance between two subsequently computed values
     */
    public BigInteger getMaxSucDist() {
        BigInteger res = BigInteger.valueOf(maxSucDist);
        if (maxSucDistOF) {
            res = res.add(OVERFLOW_PLUS_ONE);
        }
        return res;
    }

    /**
     * @return average distance between two subsequently computed values
     */
    public BigInteger getAvgSucDist() {
        if (minSucDist < 0) {
            return BigInteger.valueOf(minSucDist);
        }
        BigInteger sumTotal = BigInteger.valueOf(sucDistSum);
        if (sucDistSumOF > 0) {
            sumTotal = sumTotal.add(OVERFLOW.multiply(BigInteger.valueOf(sucDistSumOF)));
        }
        return sumTotal.divide(countSuc == 0 ? BigInteger.ONE : BigInteger.valueOf(countSuc));
    }

    /**
     * This method computes the distance between two 64-bit signed numbers, so that all 2^64 possible values can be preserved.
     * <p>
     * For any distance <code>d &gt; Long.MAX_VALUE</code> between <code>value1</code> and </code>value2</code>a negative value <code>dist</code>will be
     * returned, so that <br/>
     * <code>BigInteger.valueOf(dist).abs().add(BigInteger.valueOf(Long.MAX_VALUE).add(BigInteger.ONE))</code> reflects the absolute distance.
     * @param value1 any 64 bit long value
     * @param value2 any 64 bit long value
     * @return distance between the values
     */
    static long dist64(long value1, long value2) {
        if (value1 == value2) {
            return 0;
        }
        if ((value1 < 0 && value2 < 0) || (value1 >= 0 && value2 >= 0)) {
            return Math.abs(value1 - value2);
        }
        else if (value1 == Long.MIN_VALUE) {
            return (value2 * -1) - 1;
        }
        else if (value2 == Long.MIN_VALUE) {
            return (value1 * -1) - 1;
        }
        else {
            long res = Math.abs(value1) + Math.abs(value2);
            if (res < 0) {
                res = (Long.MIN_VALUE - res) - 1;
            }
            return res;
        }
    }

    /**
     * Compares the current distance value against the given limit.
     * @param currentDist (negative values encode overflow as specified)
     * @param limitDist current limit
     * @param limitDistOF overflow indicator for the limit
     * @return see {@link Comparator#compare(Object, Object)} contract
     */
    static int compareDist64(long currentDist, long limitDist, boolean limitDistOF) {
        boolean currentDistOF = (currentDist < 0);
        if (currentDistOF && !limitDistOF) {
            return 1;
        }
        else if (!currentDistOF && limitDistOF) {
            return -1;
        }
        int res = 0;
        if (currentDistOF) {
            res = Long.compare(Math.abs(currentDist + 1), limitDist - 1);
        }
        else {
            res = Long.compare(currentDist, limitDist);
        }
        if (res > 0) {
            res = 1;
        }
        else if (res < 0) {
            res = -1;
        }
        return res;
    }

    /**
     * Increments the source distance sum by the given distance
     * @param srcDist current distance
     */
    private void incrementSrcDistSum(long srcDist) {
        if (srcDist < 0) {
            srcDistSumOF++;
            srcDistSum = srcDistSum + Math.abs(srcDist + 1) + 1;
        }
        else {
            srcDistSum = srcDistSum + srcDist;
        }
        if (srcDistSum < 0) {
            srcDistSum = srcDistSum - Long.MAX_VALUE;
            srcDistSumOF++;
        }
    }

    /**
     * Increments the successor distance sum by the given distance
     * @param sucDist current distance
     */
    private void incrementSucDistSum(long sucDist) {
        if (sucDist < 0) {
            sucDistSumOF++;
            sucDistSum = sucDistSum + Math.abs(sucDist + 1) + 1;
        }
        else {
            sucDistSum = sucDistSum + sucDist;
        }
        if (sucDistSum < 0) {
            sucDistSum = sucDistSum - Long.MAX_VALUE;
            sucDistSumOF++;
        }
    }

    /**
     * Consumes a pair of values (input, computed value)
     * @param srcValue the value that was encoded
     * @param destValue encoded value
     */
    public void consume(long srcValue, long destValue) {

        consumeSourceDistance(dist64(destValue, srcValue));
        count++;

        if (count > 1 && (!ignoreSignTransitionSuccessorDistance || (destValue < 0 && prevDestValue < 0) || (destValue >= 0 && prevDestValue >= 0))) {
            consumeSuccessorDistance(dist64(destValue, prevDestValue));
            countSuc++;
        }

        if (srcValue == destValue) {
            selfMappedCount++;
        }

        prevDestValue = destValue;
    }

    /**
     * handles the computed source distance
     * @param srcDist current distance
     */
    private void consumeSourceDistance(long srcDist) {
        if (maxSrcDist < 0 || compareDist64(srcDist, maxSrcDist, maxSrcDistOF) > 0) {
            if (srcDist < 0) {
                maxSrcDistOF = true;
                maxSrcDist = Math.abs(srcDist + 1);
            }
            else {
                maxSrcDistOF = false;
                maxSrcDist = srcDist;
            }
        }
        if (minSrcDist < 0 || compareDist64(srcDist, minSrcDist, minSrcDistOF) < 0) {
            if (srcDist < 0) {
                minSrcDistOF = true;
                minSrcDist = Math.abs(srcDist + 1);
            }
            else {
                minSrcDistOF = false;
                minSrcDist = srcDist;
            }
        }
        incrementSrcDistSum(srcDist);
    }

    /**
     * handles the computed successor distance
     * @param sucDist current distance
     */
    private void consumeSuccessorDistance(long sucDist) {
        if (maxSucDist < 0 || compareDist64(sucDist, maxSucDist, maxSucDistOF) > 0) {
            if (sucDist < 0) {
                maxSucDistOF = true;
                maxSucDist = Math.abs(sucDist + 1);
            }
            else {
                maxSucDistOF = false;
                maxSucDist = sucDist;
            }
        }
        if (minSucDist < 0 || compareDist64(sucDist, minSucDist, minSucDistOF) < 0) {
            if (sucDist < 0) {
                minSucDistOF = true;
                minSucDist = Math.abs(sucDist + 1);
            }
            else {
                minSucDistOF = false;
                minSucDist = sucDist;
            }
        }
        incrementSucDistSum(sucDist);
    }

    /**
     * Deals with the overflows when printing the values
     * @param value current value
     * @param overflow overflow indicator
     * @return actual value as a string
     */
    static String formatHandleOverflow(long value, boolean overflow) {
        if (!overflow) {
            return String.valueOf(value);
        }
        else {
            return BigInteger.valueOf(value).add(OVERFLOW_PLUS_ONE).toString();
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append(this.getClass().getSimpleName()).append(" {\n");
        sb.append("Total count:      ").append(count).append("\n");
        sb.append("\n");
        sb.append("Minimum src diff: ").append(formatHandleOverflow(minSrcDist, minSrcDistOF)).append("\n");
        sb.append("Maximum src diff: ").append(formatHandleOverflow(maxSrcDist, maxSrcDistOF)).append("\n");
        sb.append("Average src diff: ").append(getAvgSrcDist()).append("\n");
        sb.append("\n");
        sb.append("Minimum suc diff: ").append(formatHandleOverflow(minSucDist, minSucDistOF)).append("\n");
        sb.append("Maximum suc diff: ").append(formatHandleOverflow(maxSucDist, maxSucDistOF)).append("\n");
        sb.append("Average suc diff: ").append(getAvgSucDist()).append("\n");
        sb.append("\n");
        sb.append("Self-mapped:      ").append(selfMappedCount).append("\n");
        sb.append("}");

        return sb.toString();

    }

}
