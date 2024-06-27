//@formatter:off
/*
 * GenStats64Test
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

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigInteger;
import java.util.Random;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class GenStatsTest {

    static final Logger LOGGER = LoggerFactory.getLogger(GenStatsTest.class);

    @Test
    public void testDist64() {

        testDist64(Long.MIN_VALUE, Long.MAX_VALUE);
        testDist64(Long.MAX_VALUE, Long.MIN_VALUE);
        testDist64(Long.MIN_VALUE, -1);
        testDist64(-1, Long.MIN_VALUE);
        testDist64(-3, -1);
        testDist64(-1, -3);
        testDist64(Long.MIN_VALUE, 0);
        testDist64(0, Long.MIN_VALUE);
        testDist64(Long.MAX_VALUE, 0);
        testDist64(0, Long.MAX_VALUE);
        testDist64(3, 1);
        testDist64(1, 3);
        testDist64(Long.MAX_VALUE, 1);
        testDist64(1, Long.MAX_VALUE);
        testDist64(0, 0);
        testDist64(Long.MIN_VALUE, Long.MIN_VALUE);
        testDist64(Long.MAX_VALUE, Long.MAX_VALUE);

        Random rand = new Random(826426462);

        long lastValue = 0;
        for (int i = 0; i < 10000; i++) {

            long value = rand.nextLong();

            testDist64(value, lastValue);
            testDist64(lastValue, value);

        }

    }

    @Test
    public void testCompare() {
        testCompareDist64(Long.MIN_VALUE + 1, Long.MAX_VALUE, true);
        testCompareDist64(Long.MIN_VALUE + 1, Long.MAX_VALUE, false);
        testCompareDist64(Long.MIN_VALUE + 2, Long.MAX_VALUE, true);
        testCompareDist64(Long.MIN_VALUE + 2, Long.MAX_VALUE, false);

        testCompareDist64(1, 2, false);
        testCompareDist64(1, 2, true);
        testCompareDist64(2, 2, false);
        testCompareDist64(2, 1, true);
        testCompareDist64(0, 0, false);
        testCompareDist64(-177, 177, true);
        testCompareDist64(177, 177, true);
        testCompareDist64(177, 177, false);

        Random rand = new Random(82646283);

        for (int i = 0; i < 10000; i++) {
            long value1 = rand.nextLong();
            long value2 = rand.nextLong(Long.MAX_VALUE);
            testCompareDist64(value1, value2, rand.nextBoolean());
        }

    }

    @Test
    public void testFormatHandleOverflow() {

        assertEquals("0", GenStats.formatHandleOverflow(0, false));
        assertEquals("9223372036854775808", GenStats.formatHandleOverflow(0, true));

        assertEquals("-1", GenStats.formatHandleOverflow(-1, false));
        assertEquals("9223372036854775807", GenStats.formatHandleOverflow(-1, true));

        assertEquals("-9223372036854775807", GenStats.formatHandleOverflow(Long.MAX_VALUE * -1, false));
        assertEquals("1", GenStats.formatHandleOverflow(Long.MAX_VALUE * -1, true));

        assertEquals("9223372036854775807", GenStats.formatHandleOverflow(Long.MAX_VALUE, false));
        assertEquals("18446744073709551615", GenStats.formatHandleOverflow(Long.MAX_VALUE, true));

    }

    @Test
    public void testBasics1() {

        GenStats stats = new GenStats();

        assertEquals(0, stats.getCount());
        assertEquals(0, stats.getSelfMappedCount());
        assertEquals(BigInteger.ONE.negate(), stats.getMinSrcDist());
        assertEquals(BigInteger.ONE.negate(), stats.getMaxSrcDist());
        assertEquals(BigInteger.ONE.negate(), stats.getAvgSrcDist());
        assertEquals(BigInteger.ONE.negate(), stats.getMinSucDist());
        assertEquals(BigInteger.ONE.negate(), stats.getMaxSucDist());
        assertEquals(BigInteger.ONE.negate(), stats.getAvgSucDist());

        stats.consume(0, 0);

        assertEquals(1, stats.getCount());
        assertEquals(1, stats.getSelfMappedCount());
        assertEquals(BigInteger.ZERO, stats.getMinSrcDist());
        assertEquals(BigInteger.ZERO, stats.getMaxSrcDist());
        assertEquals(BigInteger.ZERO, stats.getAvgSrcDist());
        assertEquals(BigInteger.ONE.negate(), stats.getMinSucDist());
        assertEquals(BigInteger.ONE.negate(), stats.getMaxSucDist());
        assertEquals(BigInteger.ONE.negate(), stats.getAvgSucDist());
    }

    @Test
    public void testBasics2() {

        GenStats stats = new GenStats();

        for (int i = -10000; i < 10000; i++) {
            stats.consume(i, i);
        }

        assertEquals(20000, stats.getCount());
        assertEquals(20000, stats.getSelfMappedCount());
        assertEquals(BigInteger.ZERO, stats.getMinSrcDist());
        assertEquals(BigInteger.ZERO, stats.getMaxSrcDist());
        assertEquals(BigInteger.ZERO, stats.getAvgSrcDist());
        assertEquals(BigInteger.ONE, stats.getMinSucDist());
        assertEquals(BigInteger.ONE, stats.getMaxSucDist());
        assertEquals(BigInteger.ONE, stats.getAvgSucDist());

        stats.consume(10001, Long.MAX_VALUE);
        stats.consume(10002, Long.MIN_VALUE);

        assertEquals(20002, stats.getCount());
        assertEquals(20000, stats.getSelfMappedCount());
        assertEquals(BigInteger.ZERO, stats.getMinSrcDist());
        assertEquals(new BigInteger("9223372036854785810"), stats.getMaxSrcDist());
        assertEquals(BigInteger.TWO.pow(64).subtract(BigInteger.ONE), stats.getMaxSucDist());

    }

    @Test
    public void testAverageMinMaxDist() {
        GenStats stats = new GenStats();

        Random rand = new Random(92834726331L);

        BigInteger srcDistanceSumBI = BigInteger.ZERO;
        BigInteger sucDistanceSumBI = BigInteger.ZERO;
        BigInteger srcDistanceMaxBI = BigInteger.ONE.negate();
        BigInteger sucDistanceMaxBI = BigInteger.ONE.negate();
        BigInteger srcDistanceMinBI = BigInteger.ONE.negate();
        BigInteger sucDistanceMinBI = BigInteger.ONE.negate();

        BigInteger prevDestValueBI = null;

        long count = 0;

        for (int i = -10000; i < 10000; i++) {

            count++;

            long distance = rand.nextLong(Long.MAX_VALUE);

            boolean rwd = rand.nextBoolean();
            long destValue = 0;
            if (rwd) {
                destValue = i - distance;
            }
            else {
                destValue = i + distance;
            }
            stats.consume(i, destValue);

            BigInteger srcDistanceBI = BigInteger.valueOf(distance);
            srcDistanceSumBI = srcDistanceSumBI.add(srcDistanceBI);

            BigInteger destValueBI = BigInteger.valueOf(destValue);

            if (srcDistanceMinBI.signum() < 0 || srcDistanceBI.compareTo(srcDistanceMinBI) < 0) {
                srcDistanceMinBI = srcDistanceBI;
            }
            if (srcDistanceMaxBI.signum() < 0 || srcDistanceBI.compareTo(srcDistanceMaxBI) > 0) {
                srcDistanceMaxBI = srcDistanceBI;
            }

            if (count > 1) {

                BigInteger sucDistanceBI = prevDestValueBI.subtract(destValueBI).abs();
                sucDistanceSumBI = sucDistanceSumBI.add(sucDistanceBI);
                if (sucDistanceMinBI.signum() < 0 || sucDistanceBI.compareTo(sucDistanceMinBI) < 0) {
                    sucDistanceMinBI = sucDistanceBI;
                }
                if (sucDistanceMaxBI.signum() < 0 || sucDistanceBI.compareTo(sucDistanceMaxBI) > 0) {
                    sucDistanceMaxBI = sucDistanceBI;
                }

            }
            prevDestValueBI = destValueBI;
        }

        BigInteger expectedAvgSrcDist = srcDistanceSumBI.divide(BigInteger.valueOf(count));
        BigInteger expectedAvgSucDist = sucDistanceSumBI.divide(BigInteger.valueOf(count - 1));

        assertTrue(srcDistanceSumBI.compareTo(BigInteger.valueOf(Long.MAX_VALUE)) > 0);
        assertEquals(expectedAvgSrcDist, stats.getAvgSrcDist());
        assertEquals(expectedAvgSucDist, stats.getAvgSucDist());
        assertEquals(srcDistanceMaxBI, stats.getMaxSrcDist());
        assertEquals(srcDistanceMinBI, stats.getMinSrcDist());
        assertEquals(sucDistanceMaxBI, stats.getMaxSucDist());
        assertEquals(sucDistanceMinBI, stats.getMinSucDist());

    }

    @Test
    public void testNegativePositiveTransition() {
        GenStats stats = new GenStats(false);

        stats.consume(-100, -99);
        stats.consume(-99, -98);
        stats.consume(1000, 10001);
        stats.consume(1001, 10002);

        assertEquals(BigInteger.valueOf(9001), stats.getMaxSrcDist());
        assertEquals(BigInteger.valueOf(10099), stats.getMaxSucDist());
        assertEquals(BigInteger.valueOf(4501), stats.getAvgSrcDist());
        assertEquals(BigInteger.valueOf(3367), stats.getAvgSucDist());

        stats = new GenStats(true);

        stats.consume(-100, -99);
        stats.consume(-99, -98);
        stats.consume(1000, 10001);
        stats.consume(1001, 10002);

        assertEquals(BigInteger.valueOf(9001), stats.getMaxSrcDist());
        assertEquals(BigInteger.valueOf(1), stats.getMaxSucDist());
        assertEquals(BigInteger.valueOf(4501), stats.getAvgSrcDist());
        assertEquals(BigInteger.valueOf(1), stats.getAvgSucDist());

        stats = new GenStats(true);

        stats.consume(-100, -99);
        stats.consume(-99, -98);
        stats.consume(0, 1);

        assertEquals(BigInteger.valueOf(1), stats.getMaxSrcDist());
        assertEquals(BigInteger.valueOf(1), stats.getMaxSucDist());
        assertEquals(BigInteger.valueOf(1), stats.getAvgSrcDist());
        assertEquals(BigInteger.valueOf(1), stats.getAvgSucDist());

    }

    private void testCompareDist64(long value1, long value2, boolean value2OF) {
        assertEquals(compareAlt(value1, value2OF ? value2 * -1 : value2), GenStats.compareDist64(value1, value2, value2OF));
    }

    private BigInteger convertToBigInt(long value) {
        BigInteger res = BigInteger.valueOf(value);
        if (value < 0) {
            res = res.abs().add(BigInteger.valueOf(Long.MAX_VALUE));
        }
        return res;
    }

    private int compareAlt(long value1, long value2) {
        int res = convertToBigInt(value1).compareTo(convertToBigInt(value2));
        if (res > 0) {
            res = 1;
        }
        else if (res < 0) {
            res = -1;
        }
        return res;
    }

    private void testDist64(long value1, long value2) {

        BigInteger expected = computeDistance(value1, value2);
        BigInteger actual = BigInteger.valueOf(GenStats.dist64(value1, value2));
        if (actual.signum() == -1) {
            actual = actual.abs().add(BigInteger.valueOf(Long.MAX_VALUE));
        }
        assertEquals(expected, actual);

    }

    private BigInteger computeDistance(long value1, long value2) {
        if (value1 == value2) {
            return BigInteger.ZERO;
        }
        BigInteger b1 = BigInteger.valueOf(value1);
        BigInteger b2 = BigInteger.valueOf(value2);
        if (b1.signum() == b2.signum()) {
            return b1.subtract(b2).abs();
        }
        else {
            return b1.abs().add(b2.abs());
        }
    }

}
