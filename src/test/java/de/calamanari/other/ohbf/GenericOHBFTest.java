//@formatter:off
/*
 * GenericOHBFTest
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
import java.util.stream.IntStream;

import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.calamanari.other.ohbf.GenericOHBF.Partition;
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
    public void testPartitions() {
        Partitioner partitioner = new Partitioner();

        int[] partitions = partitioner.computePartitions(513, 4);

        assertEquals(4, partitions.length);

        int expectedM = IntStream.of(partitions).map(i -> (int) Math.pow(2, i)).sum();

        int expectedTotalHashBitCount = IntStream.of(partitions).sum();

        Partition[] partitionArray = GenericOHBF.createPartitions(new BloomFilterConfig(0.1d, 513));

        assertEquals(expectedM, GenericOHBF.computeEffectiveM(partitionArray));

        assertEquals(expectedTotalHashBitCount, GenericOHBF.computeRequiredHashBitCount(partitionArray));

    }

    @Test
    public void testVectorPositionComputation() {
        byte[] hashBytes = new byte[2];

        Partition p1 = new Partition(0, 8);
        Partition p2 = new Partition(256, 8);

        int[] hashOffsets = new int[2];

        long pos = GenericOHBF.fetchBitPosition(hashBytes, hashOffsets, p1);

        assertEquals(0, pos);

        pos = GenericOHBF.fetchBitPosition(hashBytes, hashOffsets, p2);

        assertEquals(256, pos);

        hashBytes = new byte[] { 13, -1 };

        hashOffsets = new int[2];

        pos = GenericOHBF.fetchBitPosition(hashBytes, hashOffsets, p1);

        assertEquals(13, pos);

        pos = GenericOHBF.fetchBitPosition(hashBytes, hashOffsets, p2);

        assertEquals(511, pos);

        hashBytes = new byte[2];
        p1 = new Partition(0, 4);
        p2 = new Partition(16, 8);
        hashOffsets = new int[2];

        pos = GenericOHBF.fetchBitPosition(hashBytes, hashOffsets, p1);

        assertEquals(0, pos);

        pos = GenericOHBF.fetchBitPosition(hashBytes, hashOffsets, p2);

        assertEquals(16, pos);

        p1 = new Partition(0, 4);
        p2 = new Partition(16, 12);

        hashBytes = new byte[] { sByte("00010000"), sByte("00000010") };
        hashOffsets = new int[2];

        pos = GenericOHBF.fetchBitPosition(hashBytes, hashOffsets, p1);

        assertEquals(1, pos);

        pos = GenericOHBF.fetchBitPosition(hashBytes, hashOffsets, p2);

        assertEquals(18, pos);

        for (int i = 0; i < 256; i++) {

            for (int k = 0; k < 16; k++) {
                int k2 = k << 4;
                hashBytes = new byte[] { (byte) k2, (byte) i };
                hashOffsets = new int[2];

                pos = GenericOHBF.fetchBitPosition(hashBytes, hashOffsets, p1);

                assertEquals(k, pos);

                pos = GenericOHBF.fetchBitPosition(hashBytes, hashOffsets, p2);

                assertEquals(i + 16, pos);

            }

        }

        p1 = new Partition(0, 3);
        p2 = new Partition(8, 9);
        Partition p3 = new Partition(520, 4);

        hashBytes = new byte[] { sByte("00100000"), sByte("00111111") };
        hashOffsets = new int[2];

        pos = GenericOHBF.fetchBitPosition(hashBytes, hashOffsets, p1);

        assertEquals(1, pos);

        pos = GenericOHBF.fetchBitPosition(hashBytes, hashOffsets, p2);

        assertEquals(11, pos);

        pos = GenericOHBF.fetchBitPosition(hashBytes, hashOffsets, p3);

        assertEquals(535, pos);

        hashBytes = new byte[] { sByte("00100000"), sByte("00111111"), sByte("10000001") };
        hashOffsets = new int[2];

        Partition p4 = new Partition(536, 7);
        Partition p5 = new Partition(664, 1);

        pos = GenericOHBF.fetchBitPosition(hashBytes, hashOffsets, p1);

        assertEquals(1, pos);

        pos = GenericOHBF.fetchBitPosition(hashBytes, hashOffsets, p2);

        assertEquals(11, pos);

        pos = GenericOHBF.fetchBitPosition(hashBytes, hashOffsets, p3);

        assertEquals(535, pos);

        pos = GenericOHBF.fetchBitPosition(hashBytes, hashOffsets, p4);

        assertEquals(600, pos);

        pos = GenericOHBF.fetchBitPosition(hashBytes, hashOffsets, p5);

        assertEquals(665, pos);

    }

    @Test
    public void testBasics() {

        BloomFilterConfig config = new BloomFilterConfig(100, 0.0001d);

        GenericOHBF bloom = new GenericOHBF(config);

        assertTrue(bloom.put("Bla"));

        assertTrue(bloom.contains("Bla"));

        assertFalse(bloom.contains("Bla1"));
        long numberOfElementsInserted = 1;
        for (int i = 0; i < 99; i++) {
            if (bloom.put(i)) {
                numberOfElementsInserted++;
            }
            assertTrue(bloom.contains(i));
            assertEquals(i, numberOfElementsInserted - 2);
            assertTrue(Math.abs(bloom.getEstimatedNumberOfElementsInserted() - numberOfElementsInserted) <= 3);
            LOGGER.debug("Puts: " + (i + 1) + ", successful inserts: " + numberOfElementsInserted + ", estimatedNumberOfInserts: "
                    + bloom.getEstimatedNumberOfElementsInserted() + ", bitsInUse: " + bloom.getNumberOfBitsUsed());
        }

        int falseClaims = 0;
        int correctClaims = 0;
        for (int i = 100; i < 1_000_000; i++) {
            if (bloom.contains(i)) {
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
        assertTrue(falsePositiveRate <= config.getFalsePositiveRateEpsilon());

        LOGGER.debug("Correct claims: {}, false claims: {} ({})", correctClaims, falseClaims, nf.format(falsePositiveRate));
    }

    @Test
    public void testSerialization() throws Exception {

        BloomFilterConfig config = new BloomFilterConfig(100, 0.0001d);

        GenericOHBF bloom = new GenericOHBF(config);

        assertTrue(bloom.put("Bla"));

        assertTrue(bloom.contains("Bla"));

        assertFalse(bloom.contains("Bla1"));
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
            assertTrue(bloom.contains(i));
        }

        assertTrue(numberOfElementsInserted >= bloom.getEstimatedNumberOfElementsInserted());

        assertEquals(falseClaims, 10_000_000 - numberOfElementsInserted);
        assertTrue(Math.abs(bloom.getEstimatedNumberOfElementsInserted() - numberOfElementsInserted) <= 300_000);

        for (int i = 10_000_000; i < 100_000_000; i++) {
            if (bloom.contains(i)) {
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

        long numberOfElementsInserted = 10;

        for (int i = 0; i < 100_000; i++) {
            if (bloom.put(i)) {
                numberOfElementsInserted++;
                correctClaims++;
            }
            else {
                falseClaims++;
            }
            assertTrue(bloom.contains(i));
        }

        assertEquals(0, falseClaims);
        assertEquals(100_000, correctClaims);
        assertEquals(100_000, numberOfElementsInserted);
        assertTrue(numberOfElementsInserted >= bloom.getEstimatedNumberOfElementsInserted());
        assertTrue(((double) bloom.getEstimatedNumberOfElementsInserted()) / numberOfElementsInserted >= (98d / 100d));

        for (int i = 100_000; i < 100_000_000; i++) {
            if (bloom.contains(i)) {
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
    public void testHelpers() {
        for (int i = 0; i < 256; i++) {
            byte b = (byte) i;
            String s = bString(b);
            byte b2 = sByte(s);
            assertEquals(i, b2 & 0xff);
        }
    }

    /**
     * Convenience method to convert a binary string into a byte
     * @param bits8 string of length 8, composed of 0 and 1
     * @return the parsed byte
     */
    private byte sByte(String bits8) {
        return (byte) Integer.parseUnsignedInt(bits8, 2);
    }

    /**
     * Converts the given byte into its binary representation
     * @param b source byte
     * @return binary string of length 8, left-padded with 0s
     */
    private String bString(byte b) {
        String res = "00000000" + Integer.toBinaryString(b & 0xff);
        return res.substring(res.length() - 8);
    }

}
