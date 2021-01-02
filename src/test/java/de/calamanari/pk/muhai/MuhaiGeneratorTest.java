//@formatter:off
/*
 * MuhaiGeneratorTest
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

import static de.calamanari.pk.muhai.MuhaiGenerator.IND_SPACER;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;

import de.calamanari.pk.util.CloneUtils;

public class MuhaiGeneratorTest {

    private static final String UXP_ARR_EQUAL = "hash values should be different but are equal!";

    @Test
    public void testSerialization() throws Exception {
        MuhaiGenerator generator = new MuhaiGenerator(LongPrefix.fromBinaryString("101"));

        MuhaiGenerator copy = CloneUtils.passByValue(generator);

        assertNotSame(generator, copy);
        assertEquals(generator.getPrefix(), copy.getPrefix());
        assertSame(MuhaiGenerator.EMPTY_BYTES, generator.getHashPepper());
        assertSame(MuhaiGenerator.EMPTY_BYTES, copy.getHashPepper());

        generator = new MuhaiGenerator(LongPrefix.fromBinaryString("01"), "PEPPER");

        copy = CloneUtils.passByValue(generator);
        assertSame(generator.getPrefix(), LongPrefix.STRAIGHT);
        assertSame(copy.getPrefix(), LongPrefix.STRAIGHT);
        assertArrayEquals("PEPPER".getBytes(StandardCharsets.UTF_8), generator.getHashPepper());
        assertArrayEquals("PEPPER".getBytes(StandardCharsets.UTF_8), copy.getHashPepper());
    }

    @Test
    public void testDescription() {
        MuhaiGenerator generator = new MuhaiGenerator(LongPrefix.NONE);
        assertEquals("MuhaiGenerator(prefix=<NONE>, hashPepper=<NONE>, size of keyspace: 18446744073709551616)", generator.toString());

        generator = new MuhaiGenerator(LongPrefix.DEFAULT);
        assertEquals("MuhaiGenerator(prefix='00', hashPepper=<NONE>, size of keyspace: 4611686018427387904)", generator.toString());

        generator = new MuhaiGenerator(LongPrefix.POSITIVE);
        assertEquals("MuhaiGenerator(prefix='0', hashPepper=<NONE>, size of keyspace: 9223372036854775808)", generator.toString());

        generator = new MuhaiGenerator(LongPrefix.POSITIVE_31, "PEPPER");
        assertEquals("MuhaiGenerator(prefix='000000000000000000000000000000000', hashPepper=[80, 69, 80, 80, 69, 82], size of keyspace: 2147483648)",
                generator.toString());

        ByteArrayBuilder bb = new ByteArrayBuilder();

        generator = new MuhaiGenerator(LongPrefix.STRAIGHT, bb.add(3).add(127).add(-128).get());
        assertEquals("MuhaiGenerator(prefix='01', hashPepper=[3, 127, -128], size of keyspace: 4611686018427387904)", generator.toString());

    }

    @Test
    public void testSpacerByteGetsDoubled() {
        byte[] empty = new byte[] {};
        byte[] noSpacerByte = new byte[] { 127, 45, 97, -125 };
        byte[] justOneSpacerByte = new byte[] { IND_SPACER };
        byte[] twoSpacerBytes = new byte[] { IND_SPACER, IND_SPACER };
        byte[] spacerByteAtStart = new byte[] { IND_SPACER, 127, 45, 97, -126 };
        byte[] spacerByteAtEnd = new byte[] { 127, 45, 97, -126, IND_SPACER };
        byte[] spacerBytesBothSides = new byte[] { IND_SPACER, 127, 45, 97, -126, IND_SPACER };
        byte[] spacerByteInTheMiddle = new byte[] { 127, 45, IND_SPACER, 97, -126 };

        MuhaiGenerator generator = new MuhaiGenerator(LongPrefix.NONE);

        assertSame(empty, generator.escapeSpacerBytes(empty));
        assertSame(noSpacerByte, generator.escapeSpacerBytes(noSpacerByte));
        assertArrayEquals(new byte[] { IND_SPACER, IND_SPACER }, generator.escapeSpacerBytes(justOneSpacerByte));
        assertArrayEquals(new byte[] { IND_SPACER, IND_SPACER, IND_SPACER, IND_SPACER }, generator.escapeSpacerBytes(twoSpacerBytes));
        assertArrayEquals(new byte[] { IND_SPACER, IND_SPACER, 127, 45, 97, -126 }, generator.escapeSpacerBytes(spacerByteAtStart));
        assertArrayEquals(new byte[] { 127, 45, 97, -126, IND_SPACER, IND_SPACER }, generator.escapeSpacerBytes(spacerByteAtEnd));
        assertArrayEquals(new byte[] { IND_SPACER, IND_SPACER, 127, 45, 97, -126, IND_SPACER, IND_SPACER }, generator.escapeSpacerBytes(spacerBytesBothSides));
        assertArrayEquals(new byte[] { 127, 45, IND_SPACER, IND_SPACER, 97, -126 }, generator.escapeSpacerBytes(spacerByteInTheMiddle));

    }

    @Test
    public void testCannotHashNothing() {
        MuhaiGenerator generator = new MuhaiGenerator(LongPrefix.NONE);

        assertThrows(MuhaiException.class, () -> generator.computeHashBytes());
        assertThrows(MuhaiException.class, () -> generator.computeHashBytes((Object[]) null));
        assertThrows(MuhaiException.class, () -> generator.computeHashBytes(new Object[0]));

    }

    @Test
    public void testRawHashing() {
        MuhaiGenerator generator = new MuhaiGenerator(LongPrefix.NONE);

        int[][] complexArray = new int[2][3];
        complexArray[0][1] = 7;
        complexArray[1][2] = 7;

        String someNullString = null;

        assertFalse(UXP_ARR_EQUAL, Arrays.equals(generator.computeHashBytes((Object) complexArray),
                generator.computeHashBytes(Arrays.deepToString(complexArray).getBytes(StandardCharsets.UTF_8))));

        assertFalse(UXP_ARR_EQUAL,
                Arrays.equals(generator.computeHashBytes(new int[] { 1, 2, 3 }), generator.computeHashBytes(Arrays.toString(new int[] { 1, 2, 3 }))));

        assertFalse(UXP_ARR_EQUAL, Arrays.equals(generator.computeHashBytes("Blabla"), generator.computeHashBytes("Blabla".getBytes(StandardCharsets.UTF_8))));

        assertFalse(UXP_ARR_EQUAL, Arrays.equals(generator.computeHashBytes("hanni", "van"), generator.computeHashBytes("hann", "ivan")));

        assertFalse(UXP_ARR_EQUAL, Arrays.equals(generator.computeHashBytes(""), generator.computeHashBytes(someNullString)));

        assertFalse(UXP_ARR_EQUAL, Arrays.equals(generator.computeHashBytes(""), generator.computeHashBytes(new byte[] {})));

        assertFalse(UXP_ARR_EQUAL, Arrays.equals(generator.computeHashBytes(""), generator.computeHashBytes("", "")));

        assertFalse(UXP_ARR_EQUAL, Arrays.equals(generator.computeHashBytes(""), generator.computeHashBytes("", null)));

        assertFalse(UXP_ARR_EQUAL, Arrays.equals(generator.computeHashBytes(null, ""), generator.computeHashBytes("", null)));

        assertFalse(UXP_ARR_EQUAL, Arrays.equals(generator.computeHashBytes(null, null), generator.computeHashBytes(null, null, null)));

        assertFalse(UXP_ARR_EQUAL, Arrays.equals(generator.computeHashBytes("null"), generator.computeHashBytes((String) null)));

        byte[] noSpacerByte = new byte[] { 127, 45, 97, -126 };
        byte[] justOneSpacerByte = new byte[] { IND_SPACER };
        byte[] twoSpacerBytes = new byte[] { IND_SPACER, IND_SPACER };
        byte[] spacerByteAtStart = new byte[] { IND_SPACER, 127, 45, 97, -126 };
        byte[] spacerByteAtEnd = new byte[] { 127, 45, 97, -126, IND_SPACER };

        assertFalse(UXP_ARR_EQUAL,
                Arrays.equals(generator.computeHashBytes(noSpacerByte, spacerByteAtStart), generator.computeHashBytes(spacerByteAtEnd, noSpacerByte)));

        assertFalse(UXP_ARR_EQUAL,
                Arrays.equals(generator.computeHashBytes(justOneSpacerByte, twoSpacerBytes), generator.computeHashBytes(twoSpacerBytes, justOneSpacerByte)));

        MuhaiGenerator.cleanup();

    }

    @Test
    public void testRawHashingWithPepper() {
        MuhaiGenerator generator = new MuhaiGenerator(LongPrefix.NONE, "");
        assertSame(MuhaiGenerator.EMPTY_BYTES, generator.getHashPepper());

        generator = new MuhaiGenerator(LongPrefix.NONE, new byte[0]);
        assertSame(MuhaiGenerator.EMPTY_BYTES, generator.getHashPepper());

        generator = new MuhaiGenerator(LongPrefix.NONE, (String) null);
        assertSame(MuhaiGenerator.EMPTY_BYTES, generator.getHashPepper());

        generator = new MuhaiGenerator(LongPrefix.NONE, (byte[]) null);
        assertSame(MuhaiGenerator.EMPTY_BYTES, generator.getHashPepper());

        MuhaiGenerator generatorWithPepper = new MuhaiGenerator(LongPrefix.NONE, "PEP");

        Random rand = new Random(193774);

        for (int i = 0; i < 10_000; i++) {
            String input = "val" + rand.nextLong();
            assertFalse(UXP_ARR_EQUAL, Arrays.equals(generator.computeHashBytes(input), generatorWithPepper.computeHashBytes(input)));

        }
        MuhaiGenerator generatorWithPepper2 = new MuhaiGenerator(LongPrefix.NONE, "PEP2");
        for (int i = 0; i < 10_000; i++) {
            String input = "val" + rand.nextLong();
            assertFalse(UXP_ARR_EQUAL, Arrays.equals(generator.computeHashBytes(input), generatorWithPepper2.computeHashBytes(input)));
            assertFalse(UXP_ARR_EQUAL, Arrays.equals(generatorWithPepper.computeHashBytes(input), generatorWithPepper2.computeHashBytes(input)));
        }

    }

    @Test
    public void testAttributeConversion() {
        MuhaiGenerator generator = new MuhaiGenerator(LongPrefix.NONE);

        ByteArrayBuilder bb = new ByteArrayBuilder();

        assertArrayEquals(bb.reset().add("[1, 3, 78]").get(), generator.convertAttributeToByteArray(new int[] { 1, 3, 78 }));
        assertArrayEquals(bb.reset().add("[1.0, 3.2, 78.9]").get(), generator.convertAttributeToByteArray(new float[] { 1.0f, 3.2f, 78.9f }));
        assertArrayEquals(bb.reset().add("[-11.0, 13.25, 789.7]").get(), generator.convertAttributeToByteArray(new double[] { -11.0d, 13.25d, 789.7d }));

        assertArrayEquals(bb.reset().add("").get(), generator.convertAttributeToByteArray(""));
        assertArrayEquals(bb.reset().add("Hugo").get(), generator.convertAttributeToByteArray("Hugo"));

        Object[] someArray = new Object[] { "Hugo", 1d, new Object[] { 8, 9 }, new int[] { 5, 1, 2 } };
        assertArrayEquals(bb.reset().add("[Hugo, 1.0, [8, 9], [5, 1, 2]]").get(), generator.convertAttributeToByteArray(someArray));

        assertSame(MuhaiGenerator.EMPTY_BYTES, generator.convertAttributeToByteArray(""));

    }

    @Test
    public void testAddToDigest() {

        ByteArrayBuilder bb = new ByteArrayBuilder();
        MessageDigest digest = createMockDigest(bb, new byte[64]);

        MuhaiGenerator generator = new MuhaiGenerator(LongPrefix.NONE);

        generator.addToDigest(digest, null);

        ByteArrayBuilder bb2 = new ByteArrayBuilder();

        assertArrayEquals(bb2.reset().add(MuhaiGenerator.IND_NULL).get(), bb.get());
        bb.reset();
        generator.addToDigest(digest, new byte[0]);
        assertArrayEquals(bb2.reset().add(MuhaiGenerator.IND_BYTE_ARRAY_VALUE).get(), bb.get());

        bb.reset();
        generator.addToDigest(digest, new byte[] { 1, 2, 3 });
        assertArrayEquals(bb2.reset().add(MuhaiGenerator.IND_BYTE_ARRAY_VALUE).add(1).add(2).add(3).get(), bb.get());

        bb.reset();
        generator.addToDigest(digest, new byte[] { 1, 0, 3 });
        assertArrayEquals(bb2.reset().add(MuhaiGenerator.IND_BYTE_ARRAY_VALUE).add(1).add(0).add(0).add(3).get(), bb.get());

        bb.reset();
        generator.addToDigest(digest, "Hello!");
        assertArrayEquals(bb2.reset().add(MuhaiGenerator.IND_STRING_VALUE).add("Hello!").get(), bb.get());

        bb.reset();
        generator.addToDigest(digest, new double[] { 34.1, 56.2, 80.0, -7 });
        assertArrayEquals(bb2.reset().add(MuhaiGenerator.IND_OTHER_VALUE).add("[34.1, 56.2, 80.0, -7.0]").get(), bb.get());

        bb.reset();
        generator.addToDigest(digest, new Object[] { "Hugo", 1d, new Object[] { 8, 9 }, new int[] { 5, 1, 2 } });
        assertArrayEquals(bb2.reset().add(MuhaiGenerator.IND_OTHER_VALUE).add("[Hugo, 1.0, [8, 9], [5, 1, 2]]").get(), bb.get());

    }

    @Test
    public void testFeedDigest() {
        ByteArrayBuilder bb = new ByteArrayBuilder();
        ByteArrayBuilder bb2 = new ByteArrayBuilder();

        MuhaiGenerator generator = createMuhaiGeneratorWithMockDigest(LongPrefix.NONE, null, bb);

        generator.createKey((Object) null);
        assertArrayEquals(bb2.reset().add(MuhaiGenerator.IND_SPACER, MuhaiGenerator.IND_NULL).get(), bb.get());

        generator.createKey(new byte[0]);
        assertArrayEquals(bb2.reset().add(MuhaiGenerator.IND_SPACER, MuhaiGenerator.IND_BYTE_ARRAY_VALUE).get(), bb.get());

        generator.createKey("Hello!");
        assertArrayEquals(bb2.reset().add(MuhaiGenerator.IND_SPACER, MuhaiGenerator.IND_STRING_VALUE).add("Hello!").get(), bb.get());

        generator.createKey(new double[] { 34.1, 56.2, 80.0, -7 });
        assertArrayEquals(bb2.reset().add(MuhaiGenerator.IND_SPACER, MuhaiGenerator.IND_OTHER_VALUE).add("[34.1, 56.2, 80.0, -7.0]").get(), bb.get());

        generator.createKey((Object) new Object[] { "Hugo", 1d, new Object[] { 8, 9 }, new int[] { 5, 1, 2 } });
        assertArrayEquals(bb2.reset().add(MuhaiGenerator.IND_SPACER, MuhaiGenerator.IND_OTHER_VALUE).add("[Hugo, 1.0, [8, 9], [5, 1, 2]]").get(), bb.get());

        generator.createKey(new Object[] { "Hugo", 1d, new Object[] { 8, 9 }, new int[] { 5, 1, 2 } });
        // @formatter:off
        assertArrayEquals(bb2.reset()
                .add(MuhaiGenerator.IND_SPACER, MuhaiGenerator.IND_STRING_VALUE)
                .add("Hugo")
                .add(MuhaiGenerator.IND_SPACER, MuhaiGenerator.IND_OTHER_VALUE)
                .add("1.0")
                .add(MuhaiGenerator.IND_SPACER, MuhaiGenerator.IND_OTHER_VALUE)
                .add("[8, 9]")
                .add(MuhaiGenerator.IND_SPACER, MuhaiGenerator.IND_OTHER_VALUE)
                .add("[5, 1, 2]").get(), bb.get());
        // @formatter:on

        byte[] pepper = "Joda".getBytes(StandardCharsets.UTF_8);

        generator = createMuhaiGeneratorWithMockDigest(LongPrefix.NONE, pepper, bb);

        generator.createKey("Hello!", "Dolly!");
        assertArrayEquals(bb2.reset().add(pepper).add(MuhaiGenerator.IND_SPACER, MuhaiGenerator.IND_STRING_VALUE).add("Hello!")
                .add(MuhaiGenerator.IND_SPACER, MuhaiGenerator.IND_STRING_VALUE).add("Dolly!").get(), bb.get());

        byte[] pepperWithSpacerToBeEscaped = Arrays.copyOf(pepper, 5);
        generator = createMuhaiGeneratorWithMockDigest(LongPrefix.NONE, pepperWithSpacerToBeEscaped, bb);
        generator.createKey("Hello!", "Dolly!");
        assertArrayEquals(bb2.reset().add(pepperWithSpacerToBeEscaped).add(0).add(MuhaiGenerator.IND_SPACER, MuhaiGenerator.IND_STRING_VALUE).add("Hello!")
                .add(MuhaiGenerator.IND_SPACER, MuhaiGenerator.IND_STRING_VALUE).add("Dolly!").get(), bb.get());

    }

    @Test
    public void testGeneratedHashBytesLookAsExpected() {

        // Sometimes we need to test the test helper methods :)

        for (int i = Byte.MIN_VALUE; i <= Byte.MAX_VALUE; i++) {
            byte b = (byte) i;
            assertEquals(b, createFixedHashResult(leftPadByteToBinaryString(b))[0]);
        }
        for (int i = Byte.MIN_VALUE; i < Byte.MAX_VALUE; i++) {
            byte b = (byte) i;
            byte b2 = (byte) (i + 1);
            assertEquals(b, createFixedHashResult(leftPadByteToBinaryString(b) + leftPadByteToBinaryString(b2))[0]);
            assertEquals(b2, createFixedHashResult(leftPadByteToBinaryString(b) + leftPadByteToBinaryString(b2))[1]);
            assertEquals(b2, createFixedHashResult(leftPadByteToBinaryString(b2) + leftPadByteToBinaryString(b))[0]);
            assertEquals(b, createFixedHashResult(leftPadByteToBinaryString(b2) + leftPadByteToBinaryString(b))[1]);
        }

    }

    @Test
    public void testTakeHashBytesFromTheLeft() {

        // Testing absolute key-results would be possible but these
        // tests are hard to maintain in case of changes or
        // when experimenting with different digests.

        // Instead we omit "testing the digest" and concentrate on
        // the process after computing the hash.

        final Random rand = new Random(826446);
        String allBits0 = Stream.generate(() -> "0").limit(64).collect(Collectors.joining());
        String allBits1 = Stream.generate(() -> "1").limit(64).collect(Collectors.joining());
        String bitsRandom = Stream.generate(() -> "" + rand.nextInt(2)).limit(64).collect(Collectors.joining());

        assertBitsTakenFromTheLeftWithPrefix(LongPrefix.NONE, allBits0);
        assertBitsTakenFromTheLeftWithPrefix(LongPrefix.NONE, allBits1);
        assertBitsTakenFromTheLeftWithPrefix(LongPrefix.NONE, bitsRandom);

        assertBitsTakenFromTheLeftWithPrefix(LongPrefix.DEFAULT, allBits0);
        assertBitsTakenFromTheLeftWithPrefix(LongPrefix.DEFAULT, allBits1);
        assertBitsTakenFromTheLeftWithPrefix(LongPrefix.DEFAULT, bitsRandom);

        assertBitsTakenFromTheLeftWithPrefix(LongPrefix.POSITIVE, allBits0);
        assertBitsTakenFromTheLeftWithPrefix(LongPrefix.POSITIVE, allBits1);
        assertBitsTakenFromTheLeftWithPrefix(LongPrefix.POSITIVE, bitsRandom);

        assertBitsTakenFromTheLeftWithPrefix(LongPrefix.POSITIVE_31, allBits0);
        assertBitsTakenFromTheLeftWithPrefix(LongPrefix.POSITIVE_31, allBits1);
        assertBitsTakenFromTheLeftWithPrefix(LongPrefix.POSITIVE_31, bitsRandom);

        assertBitsTakenFromTheLeftWithPrefix(LongPrefix.STRAIGHT, allBits0);
        assertBitsTakenFromTheLeftWithPrefix(LongPrefix.STRAIGHT, allBits1);
        assertBitsTakenFromTheLeftWithPrefix(LongPrefix.STRAIGHT, bitsRandom);

        assertBitsTakenFromTheLeftWithPrefix(LongPrefix.STRAIGHT_30, allBits0);
        assertBitsTakenFromTheLeftWithPrefix(LongPrefix.STRAIGHT_30, allBits1);
        assertBitsTakenFromTheLeftWithPrefix(LongPrefix.STRAIGHT_30, bitsRandom);

    }

    private void assertBitsTakenFromTheLeftWithPrefix(LongPrefix prefix, String fixedHashBitString) {
        MuhaiGenerator generator = createMuhaiGeneratorWithMockDigestAndFixedHashResult(prefix, createFixedHashResult(fixedHashBitString));
        String keyAsBinaryString = MuhaiUtils.toPaddedBinaryString(generator.createKey("dummy"));

        String prefixString = keyAsBinaryString.substring(0, prefix.getLength());
        String hashPart = keyAsBinaryString.substring(prefix.getLength());

        assertEquals(prefix.toBinaryString(), prefixString);
        assertEquals(fixedHashBitString.substring(0, 64 - prefix.getLength()), hashPart);

    }

    private MessageDigest createMockDigest(ByteArrayBuilder bb, byte[] returnHashResult) {
        MessageDigest digest = mock(MessageDigest.class);
        doAnswer(invocation -> {
            bb.add((byte[]) invocation.getArgument(0));
            return null;
        }).when(digest).update(any(byte[].class));
        doAnswer(invocation -> {
            bb.add((byte) invocation.getArgument(0));
            return null;
        }).when(digest).update(any(byte.class));
        doAnswer(invocation -> {
            return returnHashResult;
        }).when(digest).digest();
        return digest;
    }

    private MuhaiGenerator createMuhaiGeneratorWithMockDigestAndFixedHashResult(LongPrefix prefix, byte[] returnHashResult) {
        ByteArrayBuilder bb = new ByteArrayBuilder();
        MessageDigest digest = createMockDigest(bb, returnHashResult);

        MuhaiGenerator generator = new MuhaiGenerator(prefix) {
            private static final long serialVersionUID = -1682865430642303615L;

            protected MessageDigest initDigest() {
                bb.reset();
                return digest;
            }
        };

        return generator;
    }

    private MuhaiGenerator createMuhaiGeneratorWithMockDigest(LongPrefix prefix, byte[] pepper, ByteArrayBuilder bb) {
        MessageDigest digest = createMockDigest(bb, new byte[64]);

        MuhaiGenerator generator = new MuhaiGenerator(prefix, pepper) {
            private static final long serialVersionUID = -1682865430642303615L;

            protected MessageDigest initDigest() {
                bb.reset();
                return digest;
            }
        };

        return generator;
    }

    /**
     * Prepares an expected bit sequence (0/1) as a byte array.<br>
     * We don't want to test the digest, we want to test the process after the digest. This method is for creating pseudo hash values.
     * @param startOf64bitSequence starting from the left
     * @return pseudoHashResut, 64 bytes, first 8 bytes as specified, other bytes 0
     */
    private byte[] createFixedHashResult(String startOf64bitSequence) {
        String bits64 = Stream.generate(() -> "0").limit(64).collect(Collectors.joining());
        bits64 = (startOf64bitSequence + bits64).substring(0, 64);
        long l = MuhaiUtils.fromBinaryString(bits64);
        byte[] result = new byte[64];
        for (int i = 7; i >= 0; i--) {
            result[i] = (byte) (l & 0xFF);
            l >>= 8;
        }
        return result;
    }

    private String leftPadByteToBinaryString(byte b) {
        String res = "00000000" + Long.toBinaryString(b);
        return res.substring(res.length() - 8);
    }

    private class ByteArrayBuilder {

        private final ByteArrayOutputStream bos = new ByteArrayOutputStream();

        ByteArrayBuilder add(String s) {
            try {
                bos.write(s.getBytes(StandardCharsets.UTF_8));
            }
            catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            return this;
        }

        ByteArrayBuilder add(byte... bytes) {
            try {
                bos.write(bytes);
            }
            catch (IOException ex) {
                throw new RuntimeException(ex);
            }
            return this;
        }

        ByteArrayBuilder add(int b) {
            return add(new byte[] { (byte) b });
        }

        byte[] get() {
            return bos.toByteArray();
        }

        ByteArrayBuilder reset() {
            bos.reset();
            return this;
        }
    }
}
