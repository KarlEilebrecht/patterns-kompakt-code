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
package de.calamanari.other.ohbf;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.text.NumberFormat;
import java.util.Locale;

import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.calamanari.pk.util.CloneUtils;

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

}
