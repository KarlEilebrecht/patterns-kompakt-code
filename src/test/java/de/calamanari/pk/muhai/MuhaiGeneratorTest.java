package de.calamanari.pk.muhai;

import static de.calamanari.pk.muhai.MuhaiGenerator.IND_SPACER;
import static de.calamanari.pk.muhai.MuhaiGenerator.IND_VALUE;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.Random;

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
    public void testGemination() {
        byte[] empty = new byte[] {};
        byte[] noSpacerByte = new byte[] { 127, 45, 97, -125 };
        byte[] justOneSpacerByte = new byte[] { IND_SPACER };
        byte[] twoSpacerBytes = new byte[] { IND_SPACER, IND_SPACER };
        byte[] spacerByteAtStart = new byte[] { IND_SPACER, 127, 45, 97, -126 };
        byte[] spacerByteAtEnd = new byte[] { 127, 45, 97, -126, IND_SPACER };
        byte[] spacerBytesBothSides = new byte[] { IND_SPACER, 127, 45, 97, -126, IND_SPACER };
        byte[] spacerByteInTheMiddle = new byte[] { 127, 45, IND_SPACER, 97, -126 };

        MuhaiGenerator generator = new MuhaiGenerator(LongPrefix.NONE);

        assertSame(empty, generator.geminateSpacerBytes(empty));
        assertSame(noSpacerByte, generator.geminateSpacerBytes(noSpacerByte));
        assertArrayEquals(new byte[] { IND_SPACER, IND_SPACER }, generator.geminateSpacerBytes(justOneSpacerByte));
        assertArrayEquals(new byte[] { IND_SPACER, IND_SPACER, IND_SPACER, IND_SPACER }, generator.geminateSpacerBytes(twoSpacerBytes));
        assertArrayEquals(new byte[] { IND_SPACER, IND_SPACER, 127, 45, 97, -126 }, generator.geminateSpacerBytes(spacerByteAtStart));
        assertArrayEquals(new byte[] { 127, 45, 97, -126, IND_SPACER, IND_SPACER }, generator.geminateSpacerBytes(spacerByteAtEnd));
        assertArrayEquals(new byte[] { IND_SPACER, IND_SPACER, 127, 45, 97, -126, IND_SPACER, IND_SPACER },
                generator.geminateSpacerBytes(spacerBytesBothSides));
        assertArrayEquals(new byte[] { 127, 45, IND_SPACER, IND_SPACER, 97, -126 }, generator.geminateSpacerBytes(spacerByteInTheMiddle));

    }

    @Test
    public void testRawHashing() {
        MuhaiGenerator generator = new MuhaiGenerator(LongPrefix.NONE);
        assertArrayEquals(new byte[] { -38, 57, -93, -18, 94, 107, 75, 13, 50, 85, -65, -17, -107, 96, 24, -112, -81, -40, 7, 9 },
                generator.computeHashBytes());

        assertArrayEquals(generator.computeHashBytes(), generator.computeHashBytes((Object[]) null));

        assertArrayEquals(generator.computeHashBytes("Blabla"), generator.computeHashBytes("Blabla".getBytes(StandardCharsets.UTF_8)));

        assertArrayEquals(generator.computeHashBytes(new int[] { 1, 2, 3 }), generator.computeHashBytes(Arrays.toString(new int[] { 1, 2, 3 })));

        int[][] complexArray = new int[2][3];
        complexArray[0][1] = 7;
        complexArray[1][2] = 7;
        System.out.println(Arrays.deepToString(complexArray));

        assertArrayEquals(generator.computeHashBytes((Object) complexArray),
                generator.computeHashBytes(Arrays.deepToString(complexArray).getBytes(StandardCharsets.UTF_8)));

        String someNullString = null;

        assertFalse(UXP_ARR_EQUAL, Arrays.equals(generator.computeHashBytes((Object[]) null), generator.computeHashBytes(someNullString)));

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
        byte[] provokeSingleWithDoubleValueCollision = new byte[] { IND_SPACER, IND_SPACER, IND_SPACER, IND_VALUE, IND_SPACER, IND_SPACER };

        assertFalse(UXP_ARR_EQUAL,
                Arrays.equals(generator.computeHashBytes(noSpacerByte, spacerByteAtStart), generator.computeHashBytes(spacerByteAtEnd, noSpacerByte)));

        assertFalse(UXP_ARR_EQUAL,
                Arrays.equals(generator.computeHashBytes(justOneSpacerByte, twoSpacerBytes), generator.computeHashBytes(twoSpacerBytes, justOneSpacerByte)));

        // we assume that collisions between keys composed of n parts and keys composed of m parts with m!=n are acceptable
        assertArrayEquals(generator.computeHashBytes(justOneSpacerByte, justOneSpacerByte), generator.computeHashBytes(provokeSingleWithDoubleValueCollision));

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

}
