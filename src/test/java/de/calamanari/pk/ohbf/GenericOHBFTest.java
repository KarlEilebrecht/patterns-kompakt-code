//@formatter:off
/*
 * GenericOHBF2Test
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
package de.calamanari.pk.ohbf;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.stream.LongStream;

import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.calamanari.pk.util.CloneUtils;
import de.calamanari.pk.util.TimeUtils;

/**
 * Test coverage for the Generic one-hashing bloom filter
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class GenericOHBFTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(GenericOHBFTest.class);

    @Test
    public void testEstimation() {

        assertEquals(0, GenericOHBF.computeEstimatedNumberOfElementsInserted(0, 1, 1));
        assertEquals(0, GenericOHBF.computeEstimatedNumberOfElementsInserted(0, 0, 1));
        assertEquals(0, GenericOHBF.computeEstimatedNumberOfElementsInserted(0, 0, 0));
        assertEquals(0, GenericOHBF.computeEstimatedNumberOfElementsInserted(0, 1, 0));
        assertEquals(1, GenericOHBF.computeEstimatedNumberOfElementsInserted(1, 1, 0));
        assertEquals(2, GenericOHBF.computeEstimatedNumberOfElementsInserted(1, 2, 1));
        assertEquals(2, GenericOHBF.computeEstimatedNumberOfElementsInserted(1, 4, 1));
        assertEquals(3, GenericOHBF.computeEstimatedNumberOfElementsInserted(2, 4, 1));
        assertEquals(197, GenericOHBF.computeEstimatedNumberOfElementsInserted(86, 100, 1));
        assertEquals(66, GenericOHBF.computeEstimatedNumberOfElementsInserted(86, 100, 3));
        assertEquals(Long.MAX_VALUE, GenericOHBF.computeEstimatedNumberOfElementsInserted(1, 2, 0));

    }

    @Test
    public void testVectorPositionComputation() {
        byte[] hashBytes = new byte[8];

        long pos = GenericOHBF.fetchBitPosition(hashBytes, 0, 9);

        assertEquals(0, pos);

        pos = GenericOHBF.fetchBitPosition(hashBytes, 1, 9);

        assertEquals(9, pos);

        hashBytes = new byte[6];

        pos = GenericOHBF.fetchBitPosition(hashBytes, 1, 9);

        assertEquals(9, pos);

        hashBytes[5] = 1;

        pos = GenericOHBF.fetchBitPosition(hashBytes, 1, 9);

        assertEquals(9, pos);

        hashBytes[2] = -1;
        hashBytes[3] = -1;
        hashBytes[4] = -1;
        hashBytes[5] = -1;

        pos = GenericOHBF.fetchBitPosition(hashBytes, 1, 9);

        assertEquals(17, pos);

    }

    @Test
    public void testBasics() {

        BloomFilterConfig config = new BloomFilterConfig(100, 0.0001d);

        GenericOHBF bloom = new GenericOHBF(config);

        assertTrue(bloom.put("Bla"));

        assertTrue(bloom.mightContain("Bla"));

        assertFalse(bloom.mightContain("Bla1"));
        long numberOfElementsInserted = 1;
        for (int i = 0; i < 99; i++) {
            if (bloom.put(i)) {
                numberOfElementsInserted++;
            }
            LOGGER.debug("Puts: " + (i + 1) + ", successful inserts: " + numberOfElementsInserted + ", estimatedNumberOfInserts: "
                    + bloom.getEstimatedNumberOfElementsInserted() + ", bitsInUse: " + bloom.getNumberOfBitsUsed());
            assertTrue(bloom.mightContain(i));
            assertEquals(i, numberOfElementsInserted - 2);
            assertTrue(Math.abs(bloom.getEstimatedNumberOfElementsInserted() - numberOfElementsInserted) <= 3);
            LOGGER.debug("Puts: " + (i + 1) + ", successful inserts: " + numberOfElementsInserted + ", estimatedNumberOfInserts: "
                    + bloom.getEstimatedNumberOfElementsInserted() + ", bitsInUse: " + bloom.getNumberOfBitsUsed());
        }

        int falseClaims = 0;
        int correctClaims = 0;
        for (int i = 100; i < 1_000_000; i++) {
            if (bloom.mightContain(i)) {
                falseClaims++;
            }
            else {
                correctClaims++;
            }
        }

        assertTrue(bloom.getNumberOfBitsUsed() > 0);

        assertEquals(bloom.getNumberOfBitsUsed(), bloom.getBitVectorAsPaddedBinaryString().codePoints().map(c -> c - 48).filter(c -> c == 1).sum());

        NumberFormat nf = NumberFormat.getInstance(Locale.US);
        nf.setMaximumFractionDigits(15);
        double falsePositiveRate = ((double) falseClaims) / 1_000_000;

        LOGGER.debug("Correct claims: {}, false claims: {} ({})", correctClaims, falseClaims, nf.format(falsePositiveRate));
        assertTrue(falsePositiveRate <= config.getFalsePositiveRateEpsilon());
    }

    @Test
    public void testSerialization() throws Exception {

        BloomFilterConfig config = new BloomFilterConfig(100, 0.0001d);

        GenericOHBF bloom = new GenericOHBF(config);

        assertTrue(bloom.put("Bla"));

        assertTrue(bloom.mightContain("Bla"));

        assertFalse(bloom.mightContain("Bla1"));
        int numberOfInserts = 0;
        for (int i = 0; i < 99; i++) {
            if (bloom.put(i)) {
                numberOfInserts++;
            }
        }
        GenericOHBF clone = CloneUtils.passByValue(bloom);

        assertEquals(bloom.getConfig(), clone.getConfig());

        assertEquals(bloom.getBitVectorAsPaddedBinaryString(), clone.getBitVectorAsPaddedBinaryString());

        assertEquals(bloom.getEstimatedNumberOfElementsInserted(), clone.getEstimatedNumberOfElementsInserted());

        assertEquals(bloom.getNumberOfBitsUsed(), clone.getNumberOfBitsUsed());

        assertEquals(bloom.getSize(), clone.getSize());

        assertEquals(bloom.getWaste(), clone.getWaste());

        int i = 100;
        while (clone.getNumberOfBitsUsed() < clone.getSize()) {
            if (clone.put(i)) {
                numberOfInserts++;
                LOGGER.debug("Inserts: {} ({}), bitsInUse: {}", clone.getEstimatedNumberOfElementsInserted(), numberOfInserts, clone.getNumberOfBitsUsed());
            }
            i++;
        }

    }

    @Test
    @Ignore("Long-running test")
    public void testAscendingSetups() {

        // This test configures and fills the filter with n elements and performs contains-checks until n * 1000 elements.

        // Observations
        // * In ca. 50% of the cases the observed false-positive rate is lower (better) than the configured one
        // * The lower the configured n the more likely it is that we see setups with worse (even derailed) false-positive rate
        // * Starting at n=10000 minimum, I did not observe any false-positive rate more than 10% worse than configured (see assertion)
        // * I do not see any indication that specific values for n or epsilon have a special influence on the filter's epsilon deviation
        // * Especially for high n's this implementation very likely fulfills the configured false-positive rate

        long[] ns = new long[] { 10000, 10001, 10511, 11111, 20000, 27674, 30000, 33333, 40000, 50000, 50901, 60000, 60999, 70000, 71911, 80000, 87777, 90000,
                90761, 100000, 200000, 250000, 311111, 500000 };

        double[] epsilons = new double[] { 0.1, 0.09, 0.08, 0.07, 0.06, 0.05, 0.04, 0.03, 0.02, 0.01, 0.009, 0.0075, 0.0053, 0.0021, 0.001, 0.0009, 0.0008,
                0.0007, 0.0006, 0.0005, 0.0004, 0.0003, 0.0002, 0.0001 };

        NumberFormat nf = NumberFormat.getInstance(Locale.US);
        nf.setMaximumFractionDigits(15);

        int runs = 0;
        int worseCount = 0;
        double maxWorseDelta = 0;
        double minBetterDelta = 0;
        double estDelta = 0;
        for (long n : ns) {
            long tries = n * 100;
            for (double epsilon : epsilons) {
                BloomFilterConfig config = new BloomFilterConfig(n, epsilon);

                GenericOHBF bloom = new GenericOHBF(config);

                int correctClaims = 0;
                int falseClaims = 0;

                long numberOfElementsInserted = 0;
                for (int i = 0; i < n; i++) {
                    if (bloom.put(i)) {
                        numberOfElementsInserted++;
                        correctClaims++;
                    }
                    else {
                        falseClaims++;
                    }
                }

                double estimationDelta = ((Math.abs(((double) numberOfElementsInserted) - bloom.getEstimatedNumberOfElementsInserted()))
                        / numberOfElementsInserted) * 100;

                LOGGER.debug("n={}, epsilon={}: Successful inserts: {}, estimatedNumberOfInserts: {} (delta={}%), bitsInUse: {}", n, nf.format(epsilon),
                        numberOfElementsInserted, bloom.getEstimatedNumberOfElementsInserted(), nf.format(estimationDelta), bloom.getNumberOfBitsUsed());

                estDelta = Math.max(estDelta, estimationDelta);

                for (int i = (int) n; i < tries; i++) {
                    if (bloom.mightContain(i)) {
                        falseClaims++;
                    }
                    else {
                        correctClaims++;
                    }
                }

                double falsePositiveRate = ((double) falseClaims) / tries;
                LOGGER.debug("n={}, epsilon={}: Correct claims: {}, false claims: {} ({})", n, nf.format(epsilon), correctClaims, falseClaims,
                        nf.format(falsePositiveRate));

                double delta = falsePositiveRate - config.getFalsePositiveRateEpsilon();
                double deltaPerc = (delta / config.getFalsePositiveRateEpsilon()) * 100d;
                String indicator = "";
                runs++;
                if (deltaPerc < 0) {
                    indicator = "(better)";
                    minBetterDelta = Math.min(minBetterDelta, deltaPerc);
                }
                if (deltaPerc > 0) {
                    indicator = "(worse)";
                    worseCount++;
                    maxWorseDelta = Math.max(maxWorseDelta, deltaPerc);
                }
                LOGGER.debug("n={}, epsilon={}: falsePositive rate delta={}% {}", n, nf.format(epsilon), nf.format(deltaPerc), indicator);
                assertTrue(deltaPerc < 10.0d);

            }
        }
        LOGGER.debug(
                "Runs: {}, worseCount: {}, max false-positive rate deviation (worse than expected): {}, min false-positive rate deviation (better than expected): {}, max insert estimation delta: {}%",
                runs, worseCount, nf.format(maxWorseDelta), nf.format(minBetterDelta), nf.format(estDelta));
    }

    @Test
    @Ignore("Long running test")
    public void testManyElementsAtLowFalsePositiveRate() {
        BloomFilterConfig config = new BloomFilterConfig(10_000_000, 0.000001d);

        GenericOHBF bloom = new GenericOHBF(config);
        int correctClaims = 0;
        int falseClaims = 0;
        long numberOfElementsInserted = 0;

        for (int i = 0; i < 10_000_000; i++) {
            if (bloom.put(i)) {
                numberOfElementsInserted++;
                correctClaims++;
            }
            else {
                falseClaims++;
            }
            assertTrue(bloom.mightContain(i));
        }

        LOGGER.debug("EstimatedNumberOfElementsInserted: {}, numberOfElementsInserted: {}", bloom.getEstimatedNumberOfElementsInserted(),
                numberOfElementsInserted);
        assertTrue(numberOfElementsInserted >= bloom.getEstimatedNumberOfElementsInserted());

        assertEquals(falseClaims, 10_000_000 - numberOfElementsInserted);
        assertTrue(Math.abs(bloom.getEstimatedNumberOfElementsInserted() - numberOfElementsInserted) <= 300_000);

        for (int i = 10_000_000; i < 100_000_000; i++) {
            if (bloom.mightContain(i)) {
                falseClaims++;
            }
            else {
                correctClaims++;
            }
        }

        NumberFormat nf = NumberFormat.getInstance(Locale.US);
        nf.setMaximumFractionDigits(15);
        double falsePositiveRate = ((double) falseClaims) / 100_000_000;
        assertTrue(falsePositiveRate <= config.getFalsePositiveRateEpsilon());
        LOGGER.debug("Correct claims: {}, false claims: {} ({})", correctClaims, falseClaims, nf.format(falsePositiveRate));
    }

    @Test
    @Ignore("Long running test")
    public void testModerateNumberOfElementsAtLowFalsePositiveRate() {

        BloomFilterConfig config = new BloomFilterConfig(100000, 0.0000001d);

        GenericOHBF bloom = new GenericOHBF(config);
        int correctClaims = 0;
        int falseClaims = 0;

        long numberOfElementsInserted = 0;

        for (int i = 0; i < 100_000; i++) {
            if (bloom.put(i)) {
                numberOfElementsInserted++;
                correctClaims++;
            }
            else {
                falseClaims++;
            }
            assertTrue(bloom.mightContain(i));
        }

        assertEquals(0, falseClaims);
        assertEquals(100_000, correctClaims);
        assertEquals(100_000, numberOfElementsInserted);
        LOGGER.debug("EstimatedNumberOfElementsInserted: {}, numberOfElementsInserted: {}", bloom.getEstimatedNumberOfElementsInserted(),
                numberOfElementsInserted);

        assertTrue(numberOfElementsInserted >= bloom.getEstimatedNumberOfElementsInserted());
        assertTrue(((double) bloom.getEstimatedNumberOfElementsInserted()) / numberOfElementsInserted >= (98d / 100d));

        for (int i = 100_000; i < 100_000_000; i++) {
            if (bloom.mightContain(i)) {
                falseClaims++;
            }
            else {
                correctClaims++;
            }
        }

        NumberFormat nf = NumberFormat.getInstance(Locale.US);
        nf.setMaximumFractionDigits(15);
        double falsePositiveRate = ((double) falseClaims) / 100_000_000;
        assertTrue(falsePositiveRate <= config.getFalsePositiveRateEpsilon());
        LOGGER.debug("Correct claims: {}, false claims: {} ({})", correctClaims, falseClaims, nf.format(falsePositiveRate));
    }

    @Test
    @Ignore("Takes time")
    public void testReduction() {

        long startTimeNanos = System.nanoTime();
        long rangeUpperBound = 71;

        long[] counters = new long[(int) rangeUpperBound];
        for (long l = Integer.MIN_VALUE; l <= Integer.MAX_VALUE; l++) {
            long unsigned = l & 0x00000000ffffffffL;
            int reduced = (int) ((unsigned * rangeUpperBound) >>> 32L);
            counters[reduced]++;
        }

        long sum = LongStream.of(counters).sum();

        long numberOfPossibleValues = (long) Math.pow(2, 32);

        double mp = ((double) numberOfPossibleValues) / rangeUpperBound;

        assertEquals(mp, LongStream.of(counters).average().getAsDouble(), 0.01);
        assertEquals(numberOfPossibleValues, sum);

        long[] last = new long[] { Long.MAX_VALUE };
        LongStream.of(counters).forEach(l -> {
            if (last[0] != Long.MAX_VALUE) {
                assertTrue(Math.abs(last[0] - l) <= 1);
            }
            last[0] = l;
        });

        String elapsedTimeString = TimeUtils.formatNanosAsSeconds(System.nanoTime() - startTimeNanos);
        LOGGER.info("testReduction successful! Elapsed time: {} s", elapsedTimeString);

    }

    @Test
    @Ignore("Long running")
    public void testModulo() {

        long startTimeNanos = System.nanoTime();
        long rangeUpperBound = 71;

        long[] counters = new long[(int) rangeUpperBound];
        for (long l = Integer.MIN_VALUE; l <= Integer.MAX_VALUE; l++) {
            long unsigned = l & 0x00000000ffffffffL;
            int reduced = (int) (unsigned % rangeUpperBound);
            counters[reduced]++;
        }

        long sum = LongStream.of(counters).sum();

        long numberOfPossibleValues = (long) Math.pow(2, 32);

        double mp = ((double) numberOfPossibleValues) / rangeUpperBound;

        assertEquals(mp, LongStream.of(counters).average().getAsDouble(), 0.01);
        assertEquals(numberOfPossibleValues, sum);

        long[] last = new long[] { Long.MAX_VALUE };
        LongStream.of(counters).forEach(l -> {
            if (last[0] != Long.MAX_VALUE) {
                assertTrue(Math.abs(last[0] - l) <= 1);
            }
            last[0] = l;
        });

        String elapsedTimeString = TimeUtils.formatNanosAsSeconds(System.nanoTime() - startTimeNanos);
        LOGGER.info("testModulo successful! Elapsed time: {} s", elapsedTimeString);

    }

}
