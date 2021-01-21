//@formatter:off
/*
 * AtomicFixedLengthBitVectorTest 
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

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Random;

import org.junit.Test;

/**
 * Test coverage for the bit vector
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class AtomicFixedLengthBitVectorTest {

    @Test
    public void testConstruction() {

        assertThrows(IllegalArgumentException.class, () -> new AtomicFixedLengthBitVector(-1));
        assertThrows(IllegalArgumentException.class, () -> new AtomicFixedLengthBitVector(0));
        assertThrows(IllegalArgumentException.class, () -> new AtomicFixedLengthBitVector(64L * Integer.MAX_VALUE));

        assertEquals(64L, new AtomicFixedLengthBitVector(1).getSize());
        assertEquals(64L, new AtomicFixedLengthBitVector(15).getSize());
        assertEquals(64L, new AtomicFixedLengthBitVector(64).getSize());
        assertEquals(128L, new AtomicFixedLengthBitVector(65).getSize());
        assertEquals(128L, new AtomicFixedLengthBitVector(128).getSize());
        assertEquals(2147483648L, new AtomicFixedLengthBitVector(Integer.MAX_VALUE).getSize());

    }

    @Test
    public void testSerialization() throws Exception {
        AtomicFixedLengthBitVector vector = new AtomicFixedLengthBitVector(1273);

        Random rand = new Random(82636);

        for (int i = 0; i < 200; i++) {
            vector.setBit(rand.nextInt(1273));
        }

        AtomicFixedLengthBitVector vector2 = CloneUtils.passByValue(vector);

        assertEquals(vector.toPaddedBinaryString(), vector2.toPaddedBinaryString());

        long[] data = vector.toLongArray();

        AtomicFixedLengthBitVector vector3 = new AtomicFixedLengthBitVector(data);

        assertEquals(vector.toPaddedBinaryString(), vector3.toPaddedBinaryString());

    }

    @Test
    public void testBasics() {
        AtomicFixedLengthBitVector vector = new AtomicFixedLengthBitVector(128);
        assertAllBits(0, vector);

        assertThrows(IndexOutOfBoundsException.class, () -> vector.getBit(128));
        assertThrows(IndexOutOfBoundsException.class, () -> vector.setBit(128));
        assertThrows(IndexOutOfBoundsException.class, () -> vector.unsetBit(128));
        assertThrows(IndexOutOfBoundsException.class, () -> vector.setBitIfNotPresent(128));
        assertThrows(IndexOutOfBoundsException.class, () -> vector.unsetBitIfPresent(128));
        assertThrows(IndexOutOfBoundsException.class, () -> vector.flipBit(128));

        for (int i = 0; i < 128; i++) {
            vector.setBit(i);
        }
        assertAllBits(1, vector);

        vector.clear();
        assertAllBits(0, vector);

        StringBuilder sbZero = new StringBuilder(
                "00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000");
        assertEquals(sbZero.toString(), vector.toPaddedBinaryString());

        for (int i = 0; i < 128; i++) {
            StringBuilder sb = new StringBuilder(sbZero.toString());
            sb.setCharAt(127 - i, '1');
            vector.setBit(i);
            assertEquals(sb.toString(), vector.toPaddedBinaryString());
            vector.unsetBit(i);
        }

        Random rand = new Random(82636);

        for (int i = 0; i < 2000; i++) {
            StringBuilder sb = new StringBuilder(sbZero.toString());

            int pos1 = rand.nextInt(128);
            int pos2 = rand.nextInt(128);
            int pos3 = rand.nextInt(128);

            sb.setCharAt(127 - pos1, '1');
            sb.setCharAt(127 - pos2, '1');
            sb.setCharAt(127 - pos3, '1');
            vector.setBit(pos1);
            vector.setBit(pos2);
            vector.setBit(pos3);
            assertEquals(sb.toString(), vector.toPaddedBinaryString());
            vector.unsetBit(pos1);
            vector.unsetBit(pos2);
            vector.unsetBit(pos3);
            assertAllBits(0, vector);
        }

        StringBuilder sbOnes = new StringBuilder(
                "11111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111111");
        for (int i = 0; i < 128; i++) {
            vector.setBit(i);
        }

        assertEquals(sbOnes.toString(), vector.toPaddedBinaryString());

        for (int i = 0; i < 128; i++) {
            StringBuilder sb = new StringBuilder(sbOnes.toString());
            sb.setCharAt(127 - i, '0');
            vector.unsetBit(i);
            assertEquals(sb.toString(), vector.toPaddedBinaryString());
            vector.setBit(i);
        }

        for (int i = 0; i < 2000; i++) {
            StringBuilder sb = new StringBuilder(sbOnes.toString());

            int pos1 = rand.nextInt(128);
            int pos2 = rand.nextInt(128);
            int pos3 = rand.nextInt(128);

            sb.setCharAt(127 - pos1, '0');
            sb.setCharAt(127 - pos2, '0');
            sb.setCharAt(127 - pos3, '0');
            vector.unsetBit(pos1);
            vector.unsetBit(pos2);
            vector.unsetBit(pos3);
            assertEquals(sb.toString(), vector.toPaddedBinaryString());
            vector.setBit(pos1);
            vector.setBit(pos2);
            vector.setBit(pos3);
            assertAllBits(1, vector);
        }

    }

    @Test
    public void testPresenceCheck() {
        AtomicFixedLengthBitVector vector = new AtomicFixedLengthBitVector(128);

        assertTrue(vector.setBitIfNotPresent(1));
        assertTrue(vector.isBitSet(1));
        assertFalse(vector.setBitIfNotPresent(1));
        assertTrue(vector.isBitSet(1));
        assertTrue(vector.unsetBitIfPresent(1));
        assertFalse(vector.isBitSet(1));
        assertAllBits(0, vector);
        assertFalse(vector.unsetBitIfPresent(1));

    }

    @Test
    public void testFlipBit() {
        AtomicFixedLengthBitVector vector = new AtomicFixedLengthBitVector(128);
        assertAllBits(0, vector);

        StringBuilder sbZero = new StringBuilder(
                "00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000");
        assertEquals(sbZero.toString(), vector.toPaddedBinaryString());

        for (int i = 0; i < 128; i++) {
            StringBuilder sb = new StringBuilder(sbZero.toString());
            sb.setCharAt(127 - i, '1');
            vector.flipBit(i);
            assertEquals(sb.toString(), vector.toPaddedBinaryString());
            vector.flipBit(i);
            assertAllBits(0, vector);
        }

        Random rand = new Random(82636);

        for (int i = 0; i < 2000; i++) {
            StringBuilder sb = new StringBuilder(sbZero.toString());

            int pos1 = rand.nextInt(128);
            int pos2 = rand.nextInt(128);
            int pos3 = rand.nextInt(128);

            if (pos1 != pos2 && pos1 != pos3 && pos2 != pos3) {
                sb.setCharAt(127 - pos1, '1');
                sb.setCharAt(127 - pos2, '1');
                sb.setCharAt(127 - pos3, '1');

                vector.flipBit(pos1);
                vector.flipBit(pos2);
                vector.flipBit(pos3);
                assertEquals(sb.toString(), vector.toPaddedBinaryString());
                vector.flipBit(pos1);
                vector.flipBit(pos2);
                vector.flipBit(pos3);
                assertAllBits(0, vector);
            }
        }

    }

    @Test
    public void testCountNumberOfBitsSet() {
        AtomicFixedLengthBitVector vector = new AtomicFixedLengthBitVector(128);

        for (int i = 0; i < 100; i++) {
            vector.setBit(i);
            assertEquals((i + 1), vector.countNumberOfBitsSet());
        }

    }

    private static void assertAllBits(int expected, AtomicFixedLengthBitVector vector) {
        assertEquals(vector.getSize(), count(expected, vector));
        for (int i = 0; i < vector.getSize(); i++) {
            if (expected == 1) {
                assertTrue(vector.isBitSet(i));
            }
            else {
                assertFalse(vector.isBitSet(i));
            }
        }
    }

    private static long count(int cmp, AtomicFixedLengthBitVector vector) {
        long count = 0;
        for (long i = 0; i < vector.getSize(); i++) {
            if (vector.getBit(i) == cmp) {
                count++;
            }
        }
        return count;
    }
}
