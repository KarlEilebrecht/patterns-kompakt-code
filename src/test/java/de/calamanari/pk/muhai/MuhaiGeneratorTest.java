package de.calamanari.pk.muhai;

import static de.calamanari.pk.muhai.MuhaiGenerator.IND_SPACER;
import static de.calamanari.pk.muhai.MuhaiGenerator.IND_VALUE;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;

import java.math.BigInteger;
import java.util.Arrays;

import org.junit.Test;

import de.calamanari.pk.util.CloneUtils;

public class MuhaiGeneratorTest {

    @Test
    public void testUniquenessAndSerialization() throws Exception {
        MuhaiGenerator generator = MuhaiGenerator.getInstance("101");

        assertSame(generator, CloneUtils.passByValue(generator));

        assertSame(generator, MuhaiGenerator.getInstance("101"));

    }

    @Test
    public void testGemination() {
        byte[] empty = new byte[] {};
        byte[] noSpacerByte = new byte[] { 127, 45, 97, -125 };
        byte[] justOneSpacerByte = new byte[] { IND_SPACER };
        byte[] twoSpacerBytes = new byte[] { IND_SPACER, IND_SPACER };
        byte[] spacerByteAtStart = new byte[] { IND_SPACER, 127, 45, 97, -126 };
        byte[] spacerByteAtEnd = new byte[] { 127, 45, 97, -126, IND_SPACER };
        byte[] spacerBytesBothSides = new byte[] { IND_SPACER, 127, 45, 97, -126, IND_SPACER };
        byte[] spacerByteInTheMiddle = new byte[] { 127, 45, IND_SPACER, 97, -126 };

        assertSame(empty, MuhaiGenerator.geminateSpacerBytes(empty));
        assertSame(noSpacerByte, MuhaiGenerator.geminateSpacerBytes(noSpacerByte));
        assertArrayEquals(new byte[] { IND_SPACER, IND_SPACER }, MuhaiGenerator.geminateSpacerBytes(justOneSpacerByte));
        assertArrayEquals(new byte[] { IND_SPACER, IND_SPACER, IND_SPACER, IND_SPACER }, MuhaiGenerator.geminateSpacerBytes(twoSpacerBytes));
        assertArrayEquals(new byte[] { IND_SPACER, IND_SPACER, 127, 45, 97, -126 }, MuhaiGenerator.geminateSpacerBytes(spacerByteAtStart));
        assertArrayEquals(new byte[] { 127, 45, 97, -126, IND_SPACER, IND_SPACER }, MuhaiGenerator.geminateSpacerBytes(spacerByteAtEnd));
        assertArrayEquals(new byte[] { IND_SPACER, IND_SPACER, 127, 45, 97, -126, IND_SPACER, IND_SPACER },
                MuhaiGenerator.geminateSpacerBytes(spacerBytesBothSides));
        assertArrayEquals(new byte[] { 127, 45, IND_SPACER, IND_SPACER, 97, -126 }, MuhaiGenerator.geminateSpacerBytes(spacerByteInTheMiddle));

    }

    @Test
    public void testRawHashing() {
        assertArrayEquals(new byte[] { -38, 57, -93, -18, 94, 107, 75, 13, 50, 85, -65, -17, -107, 96, 24, -112, -81, -40, 7, 9 },
                MuhaiGenerator.computeHashBytes());

        assertArrayEquals(MuhaiGenerator.computeHashBytes(), MuhaiGenerator.computeHashBytes((Object[]) null));

        String someNullString = null;

        assertFalse("hash values should be different but are equal!",
                Arrays.equals(MuhaiGenerator.computeHashBytes((Object[]) null), MuhaiGenerator.computeHashBytes(someNullString)));

        assertFalse("hash values should be different but are equal!",
                Arrays.equals(MuhaiGenerator.computeHashBytes("hanni", "van"), MuhaiGenerator.computeHashBytes("hann", "ivan")));

        assertFalse("hash values should be different but are equal!",
                Arrays.equals(MuhaiGenerator.computeHashBytes(""), MuhaiGenerator.computeHashBytes(someNullString)));

        assertFalse("hash values should be different but are equal!",
                Arrays.equals(MuhaiGenerator.computeHashBytes(""), MuhaiGenerator.computeHashBytes(new byte[] {})));

        assertFalse("hash values should be different but are equal!",
                Arrays.equals(MuhaiGenerator.computeHashBytes(""), MuhaiGenerator.computeHashBytes("", "")));

        assertFalse("hash values should be different but are equal!",
                Arrays.equals(MuhaiGenerator.computeHashBytes(""), MuhaiGenerator.computeHashBytes("", null)));

        assertFalse("hash values should be different but are equal!",
                Arrays.equals(MuhaiGenerator.computeHashBytes(null, ""), MuhaiGenerator.computeHashBytes("", null)));

        assertFalse("hash values should be different but are equal!",
                Arrays.equals(MuhaiGenerator.computeHashBytes(null, null), MuhaiGenerator.computeHashBytes(null, null, null)));

        assertFalse("hash values should be different but are equal!",
                Arrays.equals(MuhaiGenerator.computeHashBytes("null"), MuhaiGenerator.computeHashBytes((String) null)));

        byte[] noSpacerByte = new byte[] { 127, 45, 97, -126 };
        byte[] justOneSpacerByte = new byte[] { IND_SPACER };
        byte[] twoSpacerBytes = new byte[] { IND_SPACER, IND_SPACER };
        byte[] spacerByteAtStart = new byte[] { IND_SPACER, 127, 45, 97, -126 };
        byte[] spacerByteAtEnd = new byte[] { 127, 45, 97, -126, IND_SPACER };
        byte[] provokeSingleWithDoubleValueCollision = new byte[] { IND_SPACER, IND_SPACER, IND_SPACER, IND_VALUE, IND_SPACER, IND_SPACER };

        assertFalse("hash values should be different but are equal!", Arrays.equals(MuhaiGenerator.computeHashBytes(noSpacerByte, spacerByteAtStart),
                MuhaiGenerator.computeHashBytes(spacerByteAtEnd, noSpacerByte)));

        assertFalse("hash values should be different but are equal!", Arrays.equals(MuhaiGenerator.computeHashBytes(justOneSpacerByte, twoSpacerBytes),
                MuhaiGenerator.computeHashBytes(twoSpacerBytes, justOneSpacerByte)));

        // we assume that collisions between keys composed of n parts and keys composed of m parts with m!=n are acceptable
        assertArrayEquals(MuhaiGenerator.computeHashBytes(justOneSpacerByte, justOneSpacerByte),
                MuhaiGenerator.computeHashBytes(provokeSingleWithDoubleValueCollision));

        MuhaiGenerator.cleanup();

    }

    @Test
    public void testLongs() {
        String s = Long.toUnsignedString(Long.MAX_VALUE);
        // 9223372036854775807
        System.out.println(s);

        System.out.println(Long.toUnsignedString(-1));

        // 111111111111111111111111111111111111111111111111111111111111111
        // 1000000000000000000000000000000000000000000000000000000000000000
        System.out.println(Long.toBinaryString(Long.MIN_VALUE));

        System.out.println(Long.toBinaryString(-1));

        String binary = Long.toBinaryString(-1);

        long l = Long.parseUnsignedLong(binary, 2);

        System.out.println(l);

        System.out.println(Long.toHexString(-1));

        System.out.println(Long.toBinaryString(-1L << 0));

        System.out.println(BigInteger.TWO.pow(62));

        System.out.println(MuhaiUtils.toPaddedBinaryString(0L));
        System.out.println(MuhaiUtils.toPaddedBinaryString(0L >> 64));

        System.out.println(BigInteger.TWO.pow(62));
        System.out.println(MuhaiUtils.toPaddedBinaryString(1L << 62));
        System.out.println(MuhaiUtils.toPaddedIntString(1L << 62));
        System.out.println(MuhaiUtils.toPaddedBinaryString(Long.MAX_VALUE));
        System.out.println(MuhaiUtils.toPaddedIntString(Long.MAX_VALUE));

        System.out.println(MuhaiUtils.toPaddedHexString(1L << 62));
        System.out.println(MuhaiUtils.toPaddedHexString(Long.MAX_VALUE));

        System.out.println(1L << 62);
        System.out.println(Long.MAX_VALUE);

    }
}
