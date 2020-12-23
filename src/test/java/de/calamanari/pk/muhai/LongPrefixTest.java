//@formatter:off
/*
 * LongPrefixTest
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
package de.calamanari.pk.muhai;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Random;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;

import de.calamanari.pk.util.CloneUtils;

public class LongPrefixTest {

    @Test
    public void testPrefixCreation() {

        assertSame(LongPrefix.NONE, LongPrefix.fromBinaryString(""));
        assertSame(LongPrefix.DEFAULT, LongPrefix.fromBinaryString("00"));
        assertSame(LongPrefix.POSITIVE, LongPrefix.fromBinaryString("0"));
        assertSame(LongPrefix.POSITIVE_31, LongPrefix.fromBinaryString("000000000000000000000000000000000"));
        assertSame(LongPrefix.STRAIGHT, LongPrefix.fromBinaryString("01"));
        assertSame(LongPrefix.STRAIGHT_30, LongPrefix.fromBinaryString("0000000000000000000000000000000001"));

        for (int i = 1; i < 64; i++) {
            String prefix0s = Stream.generate(() -> "0").limit(i).collect(Collectors.joining());
            LongPrefix longPrefix0s = LongPrefix.fromBinaryString(prefix0s);
            assertEquals(prefix0s, longPrefix0s.toBinaryString());

            String prefix1s = Stream.generate(() -> "1").limit(i).collect(Collectors.joining());
            LongPrefix longPrefix1s = LongPrefix.fromBinaryString(prefix1s);
            assertEquals(prefix1s, longPrefix1s.toBinaryString());
            Random rand = new Random(99987 + i);
            String prefixRand = Stream.generate(() -> "" + rand.nextInt(2)).limit(i).collect(Collectors.joining());

            LongPrefix longPrefixRand = LongPrefix.fromBinaryString(prefixRand);
            assertEquals(prefixRand, longPrefixRand.toBinaryString());
            assertEquals(prefixRand.length(), longPrefixRand.getLength());
            assertEquals(prefixRand.compareTo(prefix0s), longPrefixRand.compareTo(longPrefix0s));
            assertEquals(prefixRand.compareTo(prefix1s), longPrefixRand.compareTo(longPrefix1s));

        }

        assertGetInvalidPrefixException(null);
        assertGetInvalidPrefixException(" 010");
        assertGetInvalidPrefixException("010 ");
        assertGetInvalidPrefixException("_");
        assertGetInvalidPrefixException(Stream.generate(() -> "0").limit(64).collect(Collectors.joining()));
        assertGetInvalidPrefixException(Stream.generate(() -> "1").limit(64).collect(Collectors.joining()));

    }

    @Test
    public void testPrefixApplicationAndMatching() {
        for (long key : new long[] { Long.MIN_VALUE, -1L, 0L, Long.MAX_VALUE }) {
            assertEquals(key, LongPrefix.NONE.applyTo(key));
        }

        for (int i = 1; i < 64; i++) {
            String prefix0s = Stream.generate(() -> "0").limit(i).collect(Collectors.joining());
            assertPrefixGetsAppliedProperly(LongPrefix.fromBinaryString(prefix0s));

            String prefix1s = Stream.generate(() -> "1").limit(i).collect(Collectors.joining());
            assertPrefixGetsAppliedProperly(LongPrefix.fromBinaryString(prefix1s));
            Random rand = new Random(99987 + i);
            String prefixRand = Stream.generate(() -> "" + rand.nextInt(2)).limit(i).collect(Collectors.joining());
            assertPrefixGetsAppliedProperly(LongPrefix.fromBinaryString(prefixRand));
        }

    }

    @Test
    public void testStraightKeyGeneration() {
        assertKeyRepresentationLength(63, LongPrefix.STRAIGHT, Long::toBinaryString, false);
        assertKeyRepresentationLength(19, LongPrefix.STRAIGHT, Long::toUnsignedString, false);
        assertKeyRepresentationLength(16, LongPrefix.STRAIGHT, Long::toHexString, false);
        assertKeyRepresentationLength(31, LongPrefix.STRAIGHT_30, Long::toBinaryString, true);
        assertKeyRepresentationLength(10, LongPrefix.STRAIGHT_30, Long::toUnsignedString, true);
        assertKeyRepresentationLength(8, LongPrefix.STRAIGHT_30, Long::toHexString, true);
    }

    @Test
    public void testSerialization() throws Exception {
        assertSame(LongPrefix.DEFAULT, CloneUtils.passByValue(LongPrefix.DEFAULT));
        assertSame(LongPrefix.NONE, CloneUtils.passByValue(LongPrefix.NONE));
        assertSame(LongPrefix.POSITIVE, CloneUtils.passByValue(LongPrefix.POSITIVE));
        assertSame(LongPrefix.POSITIVE_31, CloneUtils.passByValue(LongPrefix.POSITIVE_31));
        assertSame(LongPrefix.STRAIGHT, CloneUtils.passByValue(LongPrefix.STRAIGHT));
        assertSame(LongPrefix.STRAIGHT_30, CloneUtils.passByValue(LongPrefix.STRAIGHT_30));

        for (int i = 1; i < 64; i++) {
            String prefix0s = Stream.generate(() -> "0").limit(i).collect(Collectors.joining());
            LongPrefix longPrefix0s = LongPrefix.fromBinaryString(prefix0s);
            assertEquals(longPrefix0s, CloneUtils.passByValue(longPrefix0s));

            String prefix1s = Stream.generate(() -> "1").limit(i).collect(Collectors.joining());
            LongPrefix longPrefix1s = LongPrefix.fromBinaryString(prefix1s);
            assertEquals(longPrefix1s, CloneUtils.passByValue(longPrefix1s));

            Random rand = new Random(7777 + i);
            String prefixRand = Stream.generate(() -> "" + rand.nextInt(2)).limit(i).collect(Collectors.joining());
            LongPrefix longPrefixRand = LongPrefix.fromBinaryString(prefixRand);
            assertEquals(longPrefixRand, CloneUtils.passByValue(longPrefixRand));

        }

    }

    private void assertPrefixGetsAppliedProperly(LongPrefix prefix) {
        for (long key : new long[] { Long.MIN_VALUE, -1L, 0L, Long.MAX_VALUE, (long) Integer.MIN_VALUE, (long) Integer.MAX_VALUE }) {
            assertPrefixGetsAppliedProperly(prefix, key);
        }

        Random rand = new Random(8463);
        for (int i = 0; i < 500; i++) {
            long key = rand.nextLong();
            assertPrefixGetsAppliedProperly(prefix, key);
        }
    }

    private void assertPrefixGetsAppliedProperly(LongPrefix prefix, long key) {
        String keyAsPaddedBinaryString = MuhaiUtils.toPaddedBinaryString(key);
        String expectedModifiedKeyAsBinaryString = prefix.toBinaryString() + keyAsPaddedBinaryString.substring(prefix.getLength());
        long modifiedKey = prefix.applyTo(key);
        assertEquals(expectedModifiedKeyAsBinaryString, MuhaiUtils.toPaddedBinaryString(modifiedKey));
        assertTrue(prefix.match(modifiedKey));
    }

    private void assertKeyRepresentationLength(int expectedLength, LongPrefix prefix, Function<Long, String> keyToStringFunction,
            boolean sameAsIntegerExpected) {
        for (long key : new long[] { Long.MIN_VALUE, -1L, 0L, Long.MAX_VALUE, (long) Integer.MIN_VALUE, (long) Integer.MAX_VALUE }) {
            long modifiedKey = prefix.applyTo(key);
            assertEquals(expectedLength, keyToStringFunction.apply(modifiedKey).length());
            if (sameAsIntegerExpected) {
                assertEffectivelyPositiveInt32(modifiedKey);
            }
        }
        Random rand = new Random(716338);
        for (int i = 0; i < 500; i++) {
            long key = rand.nextLong();
            long modifiedKey = prefix.applyTo(key);
            assertEquals(expectedLength, keyToStringFunction.apply(modifiedKey).length());
            if (sameAsIntegerExpected) {
                assertEffectivelyPositiveInt32(modifiedKey);
            }
        }

    }

    private void assertEffectivelyPositiveInt32(long key) {
        int keyAsInteger = (int) key;
        assertEquals(key, (long) keyAsInteger);
    }

    private void assertGetInvalidPrefixException(String prefix) {
        Throwable expectedError = null;
        try {
            LongPrefix.fromBinaryString(prefix);
        }
        catch (RuntimeException ex) {
            expectedError = ex;
        }
        assertTrue("The prefix '" + prefix + "' should have caused an exceptio but did not!", expectedError instanceof InvalidPrefixException);
    }
}
