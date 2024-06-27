//@formatter:off
/*
 * BitUtilsTest
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
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.Test;

/**
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class BitUtilsTest {

    @Test
    public void testLongBinStr() {

        assertEquals(64, BitUtils.binStr(0L).length());
        assertEquals(64, BitUtils.binStr(1L).length());
        assertEquals(64, BitUtils.binStr(Long.MIN_VALUE).length());
        assertEquals(64, BitUtils.binStr(Long.MAX_VALUE).length());
        assertEquals("1000000000000000000000000000000000000000000000000000000000000000", BitUtils.binStr(Long.MIN_VALUE));
        assertEquals("0111111111111111111111111111111111111111111111111111111111111111", BitUtils.binStr(Long.MAX_VALUE));
        assertEquals("0000000000000000000000000000000000000000000000000000000000000000", BitUtils.binStr(0L));
        assertEquals("0000000000000000000000000000000000000000000000000000000000000001", BitUtils.binStr(1L));
        assertEquals("1111111111111111111111111111111111111111111111111111111111111111", BitUtils.binStr(-1L));
        assertEquals("1111111111111111111111111111111111111111111111111111111111111110", BitUtils.binStr(-2L));
        assertEquals("1010101010101010101010101010101010101010101010101010101010101010", BitUtils.binStr(CommonBitMasks.TIKTOK_MASK_LONG));
    }

    @Test
    public void testIntBinStr() {
        assertEquals(32, BitUtils.binStr(0).length());
        assertEquals(32, BitUtils.binStr(1).length());
        assertEquals(32, BitUtils.binStr(Integer.MIN_VALUE).length());
        assertEquals(32, BitUtils.binStr(Integer.MAX_VALUE).length());
        assertEquals("10000000000000000000000000000000", BitUtils.binStr(Integer.MIN_VALUE));
        assertEquals("01111111111111111111111111111111", BitUtils.binStr(Integer.MAX_VALUE));
        assertEquals("00000000000000000000000000000000", BitUtils.binStr(0));
        assertEquals("00000000000000000000000000000001", BitUtils.binStr(1));
        assertEquals("11111111111111111111111111111111", BitUtils.binStr(-1));
        assertEquals("11111111111111111111111111111110", BitUtils.binStr(-2));
        assertEquals("10101010101010101010101010101010", BitUtils.binStr(CommonBitMasks.TIKTOK_MASK_INT));
    }

    @Test
    public void testShortBinStr() {
        assertEquals(16, BitUtils.binStr((short) 0).length());
        assertEquals(16, BitUtils.binStr((short) 1).length());
        assertEquals(16, BitUtils.binStr(Short.MIN_VALUE).length());
        assertEquals(16, BitUtils.binStr(Short.MAX_VALUE).length());
        assertEquals("1000000000000000", BitUtils.binStr(Short.MIN_VALUE));
        assertEquals("0111111111111111", BitUtils.binStr(Short.MAX_VALUE));
        assertEquals("0000000000000000", BitUtils.binStr((short) 0));
        assertEquals("0000000000000001", BitUtils.binStr((short) 1));
        assertEquals("1111111111111111", BitUtils.binStr((short) -1));
        assertEquals("1111111111111110", BitUtils.binStr((short) -2));
        assertEquals("1010101010101010", BitUtils.binStr(CommonBitMasks.TIKTOK_MASK_SHORT));
    }

    @Test
    public void testByteBinStr() {
        assertEquals(8, BitUtils.binStr((byte) 0).length());
        assertEquals(8, BitUtils.binStr((byte) 1).length());
        assertEquals(8, BitUtils.binStr(Byte.MIN_VALUE).length());
        assertEquals(8, BitUtils.binStr(Byte.MAX_VALUE).length());
        assertEquals("10000000", BitUtils.binStr(Byte.MIN_VALUE));
        assertEquals("01111111", BitUtils.binStr(Byte.MAX_VALUE));
        assertEquals("00000000", BitUtils.binStr((byte) 0));
        assertEquals("00000001", BitUtils.binStr((byte) 1));
        assertEquals("11111111", BitUtils.binStr((byte) -1));
        assertEquals("11111110", BitUtils.binStr((byte) -2));
        assertEquals("10101010", BitUtils.binStr(CommonBitMasks.TIKTOK_MASK_BYTE));
    }

    @Test
    public void testParseLong() {
        assertEquals(Long.MIN_VALUE, BitUtils.parseLong("1000000000000000000000000000000000000000000000000000000000000000"));
        assertEquals(Long.MAX_VALUE, BitUtils.parseLong("0111111111111111111111111111111111111111111111111111111111111111"));
        assertEquals(0L, BitUtils.parseLong("0000000000000000000000000000000000000000000000000000000000000000"));
        assertEquals(1L, BitUtils.parseLong("0000000000000000000000000000000000000000000000000000000000000001"));
        assertEquals(-1L, BitUtils.parseLong("1111111111111111111111111111111111111111111111111111111111111111"));
        assertEquals(-2L, BitUtils.parseLong("1111111111111111111111111111111111111111111111111111111111111110"));
        assertEquals(CommonBitMasks.TIKTOK_MASK_LONG, BitUtils.parseLong("1010101010101010101010101010101010101010101010101010101010101010"));
        assertEquals(Long.MAX_VALUE, BitUtils.parseLong("111111111111111111111111111111111111111111111111111111111111111"));
        assertEquals(0L, BitUtils.parseLong("0"));
        assertEquals(1L, BitUtils.parseLong("0000000000000000000001"));
        assertThrows(NumberFormatException.class, () -> BitUtils.parseLong("10000000000000000000000000000000000000000000000000000000000000000"));
        assertThrows(NumberFormatException.class, () -> BitUtils.parseLong("0xFF"));
    }

    @Test
    public void testParseInt() {
        assertEquals(Integer.MIN_VALUE, BitUtils.parseInt("10000000000000000000000000000000"));
        assertEquals(Integer.MAX_VALUE, BitUtils.parseInt("01111111111111111111111111111111"));
        assertEquals(0, BitUtils.parseInt("00000000000000000000000000000000"));
        assertEquals(1, BitUtils.parseInt("00000000000000000000000000000001"));
        assertEquals(-1, BitUtils.parseInt("11111111111111111111111111111111"));
        assertEquals(-2, BitUtils.parseInt("11111111111111111111111111111110"));
        assertEquals(CommonBitMasks.TIKTOK_MASK_INT, BitUtils.parseInt("10101010101010101010101010101010"));
        assertEquals(Integer.MAX_VALUE, BitUtils.parseInt("1111111111111111111111111111111"));
        assertEquals(0, BitUtils.parseInt("0"));
        assertEquals(1, BitUtils.parseInt("000001"));
        assertThrows(NumberFormatException.class, () -> BitUtils.parseInt("100000000000000000000000000000000"));
        assertThrows(NumberFormatException.class, () -> BitUtils.parseInt("0xFF"));
    }

    @Test
    public void testParseShort() {
        assertEquals(Short.MIN_VALUE, BitUtils.parseShort("1000000000000000"));
        assertEquals(Short.MAX_VALUE, BitUtils.parseShort("0111111111111111"));
        assertEquals((short) 0, BitUtils.parseShort("0000000000000000"));
        assertEquals((short) 1, BitUtils.parseShort("0000000000000001"));
        assertEquals((short) -1, BitUtils.parseShort("1111111111111111"));
        assertEquals((short) -2, BitUtils.parseShort("1111111111111110"));
        assertEquals(CommonBitMasks.TIKTOK_MASK_SHORT, BitUtils.parseShort("1010101010101010"));
        assertEquals(Short.MAX_VALUE, BitUtils.parseShort("111111111111111"));
        assertEquals((short) 0, BitUtils.parseShort("0"));
        assertEquals((short) 1, BitUtils.parseShort("000001"));
        assertThrows(NumberFormatException.class, () -> BitUtils.parseShort("10000000000000000"));
        assertThrows(NumberFormatException.class, () -> BitUtils.parseShort("0xFF"));
    }

    @Test
    public void testParseByte() {
        assertEquals(Byte.MIN_VALUE, BitUtils.parseByte("10000000"));
        assertEquals(Byte.MAX_VALUE, BitUtils.parseByte("01111111"));
        assertEquals((byte) 0, BitUtils.parseByte("00000000"));
        assertEquals((byte) 1, BitUtils.parseByte("00000001"));
        assertEquals((byte) -1, BitUtils.parseByte("11111111"));
        assertEquals((byte) -2, BitUtils.parseByte("11111110"));
        assertEquals(CommonBitMasks.TIKTOK_MASK_BYTE, BitUtils.parseByte("10101010"));
        assertEquals(Byte.MAX_VALUE, BitUtils.parseByte("1111111"));
        assertEquals((byte) 0, BitUtils.parseByte("0"));
        assertEquals((byte) 1, BitUtils.parseByte("00001"));
        assertThrows(NumberFormatException.class, () -> BitUtils.parseByte("100000000"));
        assertThrows(NumberFormatException.class, () -> BitUtils.parseByte("0xFF"));
    }

    @Test
    public void testLongBitFlipDistance() {
        assertEquals(countBinaryChanges(Long.MIN_VALUE, 0), BitUtils.bitFlipDistance(Long.MIN_VALUE, 0));
        assertEquals(BitUtils.bitFlipDistance(0, Long.MIN_VALUE), BitUtils.bitFlipDistance(Long.MIN_VALUE, 0));
        assertEquals(countBinaryChanges(Long.MAX_VALUE, 0), BitUtils.bitFlipDistance(Long.MAX_VALUE, 0));
        assertEquals(BitUtils.bitFlipDistance(0, Long.MAX_VALUE), BitUtils.bitFlipDistance(Long.MAX_VALUE, 0));
        assertEquals(countBinaryChanges(Long.MAX_VALUE, Long.MIN_VALUE), BitUtils.bitFlipDistance(Long.MAX_VALUE, Long.MIN_VALUE));
        assertEquals(BitUtils.bitFlipDistance(Long.MIN_VALUE, Long.MAX_VALUE), BitUtils.bitFlipDistance(Long.MAX_VALUE, Long.MIN_VALUE));
        assertEquals(countBinaryChanges(CommonBitMasks.TIKTOK_MASK_LONG, CommonBitMasks.TIKTOK_MASK_LONG >>> 1),
                BitUtils.bitFlipDistance(CommonBitMasks.TIKTOK_MASK_LONG, CommonBitMasks.TIKTOK_MASK_LONG >>> 1));
        assertEquals(64, BitUtils.bitFlipDistance(CommonBitMasks.TIKTOK_MASK_LONG, CommonBitMasks.TIKTOK_MASK_LONG >>> 1));
    }

    @Test
    public void testIntBitFlipDistance() {
        assertEquals(countBinaryChanges(Integer.MIN_VALUE, 0), BitUtils.bitFlipDistance(Integer.MIN_VALUE, 0));
        assertEquals(BitUtils.bitFlipDistance(0, Integer.MIN_VALUE), BitUtils.bitFlipDistance(Integer.MIN_VALUE, 0));
        assertEquals(countBinaryChanges(Integer.MAX_VALUE, 0), BitUtils.bitFlipDistance(Integer.MAX_VALUE, 0));
        assertEquals(BitUtils.bitFlipDistance(0, Integer.MAX_VALUE), BitUtils.bitFlipDistance(Integer.MAX_VALUE, 0));
        assertEquals(countBinaryChanges(Integer.MAX_VALUE, Integer.MIN_VALUE), BitUtils.bitFlipDistance(Integer.MAX_VALUE, Integer.MIN_VALUE));
        assertEquals(BitUtils.bitFlipDistance(Integer.MIN_VALUE, Integer.MAX_VALUE), BitUtils.bitFlipDistance(Integer.MAX_VALUE, Integer.MIN_VALUE));
        assertEquals(countBinaryChanges(CommonBitMasks.TIKTOK_MASK_INT, CommonBitMasks.TIKTOK_MASK_INT >>> 1),
                BitUtils.bitFlipDistance(CommonBitMasks.TIKTOK_MASK_INT, CommonBitMasks.TIKTOK_MASK_INT >>> 1));
        assertEquals(32, BitUtils.bitFlipDistance(CommonBitMasks.TIKTOK_MASK_INT, CommonBitMasks.TIKTOK_MASK_INT >>> 1));
    }

    @Test
    public void testShortBitFlipDistance() {
        assertEquals(countBinaryChanges(Short.MIN_VALUE, 0), BitUtils.bitFlipDistance(Short.MIN_VALUE, 0));
        assertEquals(BitUtils.bitFlipDistance(0, Short.MIN_VALUE), BitUtils.bitFlipDistance(Short.MIN_VALUE, 0));
        assertEquals(countBinaryChanges(Short.MAX_VALUE, 0), BitUtils.bitFlipDistance(Short.MAX_VALUE, 0));
        assertEquals(BitUtils.bitFlipDistance(0, Short.MAX_VALUE), BitUtils.bitFlipDistance(Short.MAX_VALUE, 0));
        assertEquals(countBinaryChanges(Short.MAX_VALUE, Short.MIN_VALUE), BitUtils.bitFlipDistance(Short.MAX_VALUE, Short.MIN_VALUE));
        assertEquals(BitUtils.bitFlipDistance(Short.MIN_VALUE, Short.MAX_VALUE), BitUtils.bitFlipDistance(Short.MAX_VALUE, Short.MIN_VALUE));
        assertEquals(countBinaryChanges(CommonBitMasks.TIKTOK_MASK_SHORT, (short) (Short.toUnsignedInt(CommonBitMasks.TIKTOK_MASK_SHORT) >>> 1)),
                BitUtils.bitFlipDistance(CommonBitMasks.TIKTOK_MASK_SHORT, (short) (Short.toUnsignedInt(CommonBitMasks.TIKTOK_MASK_SHORT) >>> 1)));
        assertEquals(16, BitUtils.bitFlipDistance(CommonBitMasks.TIKTOK_MASK_SHORT, (short) (Short.toUnsignedInt(CommonBitMasks.TIKTOK_MASK_SHORT) >>> 1)));
    }

    @Test
    public void testByteBitFlipDistance() {
        assertEquals(countBinaryChanges(Byte.MIN_VALUE, 0), BitUtils.bitFlipDistance(Byte.MIN_VALUE, 0));
        assertEquals(BitUtils.bitFlipDistance(0, Byte.MIN_VALUE), BitUtils.bitFlipDistance(Byte.MIN_VALUE, 0));
        assertEquals(countBinaryChanges(Byte.MAX_VALUE, 0), BitUtils.bitFlipDistance(Byte.MAX_VALUE, 0));
        assertEquals(BitUtils.bitFlipDistance(0, Byte.MAX_VALUE), BitUtils.bitFlipDistance(Byte.MAX_VALUE, 0));
        assertEquals(countBinaryChanges(Byte.MAX_VALUE, Byte.MIN_VALUE), BitUtils.bitFlipDistance(Byte.MAX_VALUE, Byte.MIN_VALUE));
        assertEquals(BitUtils.bitFlipDistance(Byte.MIN_VALUE, Byte.MAX_VALUE), BitUtils.bitFlipDistance(Byte.MAX_VALUE, Byte.MIN_VALUE));
        assertEquals(countBinaryChanges(CommonBitMasks.TIKTOK_MASK_BYTE, (byte) (Byte.toUnsignedInt(CommonBitMasks.TIKTOK_MASK_BYTE) >>> 1)),
                BitUtils.bitFlipDistance(CommonBitMasks.TIKTOK_MASK_BYTE, (byte) (Byte.toUnsignedInt(CommonBitMasks.TIKTOK_MASK_BYTE) >>> 1)));
        assertEquals(8, BitUtils.bitFlipDistance(CommonBitMasks.TIKTOK_MASK_BYTE, (byte) (Byte.toUnsignedInt(CommonBitMasks.TIKTOK_MASK_BYTE) >>> 1)));
    }

    @Test
    public void testLongBitCount() {
        assertEquals(countBinaryChanges(Long.MIN_VALUE, 0), BitUtils.bitCount(Long.MIN_VALUE));
        assertEquals(countBinaryChanges(Long.MAX_VALUE, 0), BitUtils.bitCount(Long.MAX_VALUE));
        assertEquals(0, BitUtils.bitCount(0L));
        assertEquals(countBinaryChanges(1L, 0), BitUtils.bitCount(1L));
        assertEquals(countBinaryChanges(-1L, 0), BitUtils.bitCount(-1L));
        assertEquals(countBinaryChanges(-2L, 0), BitUtils.bitCount(-2L));
        assertEquals(countBinaryChanges(CommonBitMasks.TIKTOK_MASK_LONG, 0), BitUtils.bitCount(CommonBitMasks.TIKTOK_MASK_LONG));
    }

    @Test
    public void testIntBitCount() {
        assertEquals(countBinaryChanges(Integer.MIN_VALUE, 0), BitUtils.bitCount(Integer.MIN_VALUE));
        assertEquals(countBinaryChanges(Integer.MAX_VALUE, 0), BitUtils.bitCount(Integer.MAX_VALUE));
        assertEquals(0, BitUtils.bitCount(0));
        assertEquals(countBinaryChanges(1, 0), BitUtils.bitCount(1));
        assertEquals(countBinaryChanges(-1, 0), BitUtils.bitCount(-1));
        assertEquals(countBinaryChanges(-2, 0), BitUtils.bitCount(-2));
        assertEquals(countBinaryChanges(CommonBitMasks.TIKTOK_MASK_INT, 0), BitUtils.bitCount(CommonBitMasks.TIKTOK_MASK_INT));
    }

    @Test
    public void testShortBitCount() {
        assertEquals(countBinaryChanges(Short.MIN_VALUE, (short) 0), BitUtils.bitCount(Short.MIN_VALUE));
        assertEquals(countBinaryChanges(Short.MAX_VALUE, (short) 0), BitUtils.bitCount(Short.MAX_VALUE));
        assertEquals(0, BitUtils.bitCount((short) 0));
        assertEquals(countBinaryChanges((short) 1, (short) 0), BitUtils.bitCount((short) 1));
        assertEquals(countBinaryChanges((short) -1, (short) 0), BitUtils.bitCount((short) -1));
        assertEquals(countBinaryChanges((short) -2, (short) 0), BitUtils.bitCount((short) -2));
        assertEquals(countBinaryChanges(CommonBitMasks.TIKTOK_MASK_SHORT, (short) 0), BitUtils.bitCount(CommonBitMasks.TIKTOK_MASK_SHORT));
    }

    @Test
    public void testByteBitCount() {
        assertEquals(countBinaryChanges(Byte.MIN_VALUE, (byte) 0), BitUtils.bitCount(Byte.MIN_VALUE));
        assertEquals(countBinaryChanges(Byte.MAX_VALUE, (byte) 0), BitUtils.bitCount(Byte.MAX_VALUE));
        assertEquals(0, BitUtils.bitCount((byte) 0));
        assertEquals(countBinaryChanges((byte) 1, (byte) 0), BitUtils.bitCount((byte) 1));
        assertEquals(countBinaryChanges((byte) -1, (byte) 0), BitUtils.bitCount((byte) -1));
        assertEquals(countBinaryChanges((byte) -2, (byte) 0), BitUtils.bitCount((byte) -2));
        assertEquals(countBinaryChanges(CommonBitMasks.TIKTOK_MASK_BYTE, (byte) 0), BitUtils.bitCount(CommonBitMasks.TIKTOK_MASK_BYTE));
    }

    @Test
    public void testLongRotateBits() {
        for (long src : longTestPatterns()) {

            for (int distance = -64; distance < 65; distance++) {

                long rotLeft = BitUtils.rotateBitsLeft(src, distance);
                long rotLeftRight = BitUtils.rotateBitsRight(rotLeft, distance);

                assertEquals(rotateLongBitStringLeft(src, distance), rotLeft);
                assertEquals(src, rotLeftRight);

                rotLeft = BitUtils.rotateBitsLeftPreserveSign(src, distance);
                rotLeftRight = BitUtils.rotateBitsRightPreserveSign(rotLeft, distance);

                assertEquals(rotateLongBitStringLeftPreserveSign(src, distance), rotLeft);
                assertEquals(src, rotLeftRight);

            }
        }
    }

    @Test
    public void testIntRotateBits() {
        for (int src : intTestPatterns()) {

            for (int distance = -32; distance < 33; distance++) {

                int rotLeft = BitUtils.rotateBitsLeft(src, distance);
                int rotLeftRight = BitUtils.rotateBitsRight(rotLeft, distance);

                assertEquals(rotateIntBitStringLeft(src, distance), rotLeft);
                assertEquals(src, rotLeftRight);

                rotLeft = BitUtils.rotateBitsLeftPreserveSign(src, distance);
                rotLeftRight = BitUtils.rotateBitsRightPreserveSign(rotLeft, distance);

                assertEquals(rotateIntBitStringLeftPreserveSign(src, distance), rotLeft);
                assertEquals(src, rotLeftRight);

            }
        }
    }

    @Test
    public void testShortRotateBits() {
        for (short src : shortTestPatterns()) {

            for (int distance = -16; distance < 17; distance++) {

                short rotLeft = BitUtils.rotateBitsLeft(src, distance);
                short rotLeftRight = BitUtils.rotateBitsRight(rotLeft, distance);

                assertEquals(rotateShortBitStringLeft(src, distance), rotLeft);
                assertEquals(src, rotLeftRight);

                rotLeft = BitUtils.rotateBitsLeftPreserveSign(src, distance);
                rotLeftRight = BitUtils.rotateBitsRightPreserveSign(rotLeft, distance);

                assertEquals(rotateShortBitStringLeftPreserveSign(src, distance), rotLeft);
                assertEquals(src, rotLeftRight);

            }
        }
    }

    @Test
    public void testByteRotateBits() {

        for (byte src : byteTestPatterns()) {

            for (int distance = -8; distance < 9; distance++) {

                byte rotLeft = BitUtils.rotateBitsLeft(src, distance);
                byte rotLeftRight = BitUtils.rotateBitsRight(rotLeft, distance);

                assertEquals(rotateByteBitStringLeft(src, distance), rotLeft);
                assertEquals(src, rotLeftRight);

                rotLeft = BitUtils.rotateBitsLeftPreserveSign(src, distance);
                rotLeftRight = BitUtils.rotateBitsRightPreserveSign(rotLeft, distance);

                assertEquals(rotateByteBitStringLeftPreserveSign(src, distance), rotLeft);
                assertEquals(src, rotLeftRight);

            }
        }
    }

    @Test
    public void testLongGetSetIntAt() {

        int[] patterns = intTestPatterns();

        for (int src0 : patterns) {
            for (int src1 : patterns) {

                long dest = 0;
                dest = BitUtils.setIntAt(dest, src0, 0);
                dest = BitUtils.setIntAt(dest, src1, 1);

                assertEquals(src0, BitUtils.getIntAt(dest, 0));
                assertEquals(src1, BitUtils.getIntAt(dest, 1));

                assertEquals(src0, (int) (dest >>> 32));

            }
        }

    }

    @Test
    public void testLongGetSetShortAt() {

        short[] patterns = shortTestPatterns();

        for (short src0 : patterns) {
            for (short src1 : patterns) {
                for (short src2 : patterns) {
                    for (short src3 : patterns) {

                        long dest = 0;
                        dest = BitUtils.setShortAt(dest, src0, 0);
                        dest = BitUtils.setShortAt(dest, src1, 1);
                        dest = BitUtils.setShortAt(dest, src2, 2);
                        dest = BitUtils.setShortAt(dest, src3, 3);

                        assertEquals(src0, BitUtils.getShortAt(dest, 0));
                        assertEquals(src1, BitUtils.getShortAt(dest, 1));
                        assertEquals(src2, BitUtils.getShortAt(dest, 2));
                        assertEquals(src3, BitUtils.getShortAt(dest, 3));

                        assertEquals(src0, (short) (dest >>> 48));
                        assertEquals(src1, (short) ((dest << 16) >>> 48));
                        assertEquals(src2, (short) ((dest << 32) >>> 48));
                        assertEquals(src3, (short) ((dest << 48) >>> 48));

                    }
                }
            }
        }
    }

    @Test
    public void testLongGetSetByteAt() {

        byte[] patterns = byteTestPatterns();

        for (byte src0 : patterns) {
            for (byte src1 : patterns) {
                for (byte src2 : patterns) {
                    for (byte src3 : patterns) {
                        for (byte src4 : patterns) {
                            for (byte src5 : patterns) {
                                for (byte src6 : patterns) {
                                    for (byte src7 : patterns) {

                                        long dest = 0;
                                        dest = BitUtils.setByteAt(dest, src0, 0);
                                        dest = BitUtils.setByteAt(dest, src1, 1);
                                        dest = BitUtils.setByteAt(dest, src2, 2);
                                        dest = BitUtils.setByteAt(dest, src3, 3);
                                        dest = BitUtils.setByteAt(dest, src4, 4);
                                        dest = BitUtils.setByteAt(dest, src5, 5);
                                        dest = BitUtils.setByteAt(dest, src6, 6);
                                        dest = BitUtils.setByteAt(dest, src7, 7);

                                        assertEquals(src0, BitUtils.getByteAt(dest, 0));
                                        assertEquals(src1, BitUtils.getByteAt(dest, 1));
                                        assertEquals(src2, BitUtils.getByteAt(dest, 2));
                                        assertEquals(src3, BitUtils.getByteAt(dest, 3));
                                        assertEquals(src4, BitUtils.getByteAt(dest, 4));
                                        assertEquals(src5, BitUtils.getByteAt(dest, 5));
                                        assertEquals(src6, BitUtils.getByteAt(dest, 6));
                                        assertEquals(src7, BitUtils.getByteAt(dest, 7));

                                        assertEquals(src0, (byte) (dest >>> 56));
                                        assertEquals(src1, (byte) ((dest << 8) >>> 56));
                                        assertEquals(src2, (byte) ((dest << 16) >>> 56));
                                        assertEquals(src3, (byte) ((dest << 24) >>> 56));
                                        assertEquals(src4, (byte) ((dest << 32) >>> 56));
                                        assertEquals(src5, (byte) ((dest << 40) >>> 56));
                                        assertEquals(src6, (byte) ((dest << 48) >>> 56));
                                        assertEquals(src7, (byte) ((dest << 56) >>> 56));

                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    @Test
    public void testLongGetSetFlipBitAt() {
        for (long src : longTestPatterns()) {

            String srcS = BitUtils.binStr(src);
            long dest1 = 0;
            long dest2 = -1L;

            for (int i = 0; i < 64; i++) {
                dest1 = BitUtils.setBitAt(dest1, srcS.charAt(i) - 48, i);
                dest2 = BitUtils.setBitAt(dest2, srcS.charAt(i) - 48, i);
            }
            assertEquals(src, dest1);
            assertEquals(src, dest2);

            for (int i = 0; i < 64; i++) {
                dest1 = BitUtils.flipBitAt(dest1, i);
            }

            assertEquals(src ^ -1L, dest1);

        }
    }

    @Test
    public void testIntGetSetFlipBitAt() {
        for (int src : intTestPatterns()) {

            String srcS = BitUtils.binStr(src);
            int dest1 = 0;
            int dest2 = -1;

            for (int i = 0; i < 32; i++) {
                dest1 = BitUtils.setBitAt(dest1, srcS.charAt(i) - 48, i);
                dest2 = BitUtils.setBitAt(dest2, srcS.charAt(i) - 48, i);
            }
            assertEquals(src, dest1);
            assertEquals(src, dest2);

            for (int i = 0; i < 32; i++) {
                dest1 = BitUtils.flipBitAt(dest1, i);
            }

            assertEquals(src ^ -1, dest1);

        }
    }

    @Test
    public void testShortGetSetFlipBitAt() {
        for (short src : shortTestPatterns()) {

            String srcS = BitUtils.binStr(src);
            short dest1 = 0;
            short dest2 = -1;

            for (int i = 0; i < 16; i++) {
                dest1 = BitUtils.setBitAt(dest1, srcS.charAt(i) - 48, i);
                dest2 = BitUtils.setBitAt(dest2, srcS.charAt(i) - 48, i);
            }
            assertEquals(src, dest1);
            assertEquals(src, dest2);

            for (int i = 0; i < 16; i++) {
                dest1 = BitUtils.flipBitAt(dest1, i);
            }

            assertEquals((short) (src ^ -1), dest1);

        }
    }

    @Test
    public void testByteGetSetFlipBitAt() {
        for (byte src : byteTestPatterns()) {

            String srcS = BitUtils.binStr(src);
            byte dest1 = 0;
            byte dest2 = -1;

            for (int i = 0; i < 8; i++) {
                dest1 = BitUtils.setBitAt(dest1, srcS.charAt(i) - 48, i);
                dest2 = BitUtils.setBitAt(dest2, srcS.charAt(i) - 48, i);
            }
            assertEquals(src, dest1);
            assertEquals(src, dest2);

            for (int i = 0; i < 8; i++) {
                dest1 = BitUtils.flipBitAt(dest1, i);
            }

            assertEquals((byte) (src ^ -1), dest1);

        }
    }

    @Test
    public void testLongGetBytes() {
        for (long src : longTestPatterns()) {
            byte[] bytes = BitUtils.getBytes(src);

            long dest = 0;
            for (int i = 0; i < bytes.length; i++) {
                dest = BitUtils.setByteAt(dest, bytes[i], i);
            }
            assertEquals(src, dest);
        }
    }

    @Test
    public void testIntGetBytes() {
        for (int src : intTestPatterns()) {
            byte[] bytes = BitUtils.getBytes(src);

            int dest = 0;
            for (int i = 0; i < bytes.length; i++) {
                dest = BitUtils.setByteAt(dest, bytes[i], i);
            }
            assertEquals(src, dest);
        }
    }

    @Test
    public void testShortGetBytes() {
        for (short src : shortTestPatterns()) {
            byte[] bytes = BitUtils.getBytes(src);

            short dest = 0;
            for (int i = 0; i < bytes.length; i++) {
                dest = BitUtils.setByteAt(dest, bytes[i], i);
            }
            assertEquals(src, dest);
        }
    }

    @Test
    public void testLongWriteRead() {

        byte[] buffer1 = new byte[8];
        byte[] buffer2 = new byte[10];
        for (long src : longTestPatterns()) {
            BitUtils.writeLongAt(src, buffer1, 0);
            BitUtils.writeLongAt(src, buffer2, 2);

            long dest1 = BitUtils.readLongAt(buffer1, 0);
            long dest2 = BitUtils.readLongAt(buffer2, 2);

            assertEquals(src, dest1);
            assertEquals(src, dest2);
        }
        assertThrows(IndexOutOfBoundsException.class, () -> BitUtils.writeLongAt(1, buffer1, 1));

    }

    @Test
    public void testIntWriteRead() {

        byte[] buffer1 = new byte[4];
        byte[] buffer2 = new byte[6];
        for (int src : intTestPatterns()) {
            BitUtils.writeIntAt(src, buffer1, 0);
            BitUtils.writeIntAt(src, buffer2, 2);

            int dest1 = BitUtils.readIntAt(buffer1, 0);
            int dest2 = BitUtils.readIntAt(buffer2, 2);

            assertEquals(src, dest1);
            assertEquals(src, dest2);
        }
        assertThrows(IndexOutOfBoundsException.class, () -> BitUtils.writeIntAt(1, buffer1, 1));

    }

    @Test
    public void testShortWriteRead() {

        byte[] buffer1 = new byte[2];
        byte[] buffer2 = new byte[4];
        for (short src : shortTestPatterns()) {
            BitUtils.writeShortAt(src, buffer1, 0);
            BitUtils.writeShortAt(src, buffer2, 2);

            short dest1 = BitUtils.readShortAt(buffer1, 0);
            short dest2 = BitUtils.readShortAt(buffer2, 2);

            assertEquals(src, dest1);
            assertEquals(src, dest2);
        }
        assertThrows(IndexOutOfBoundsException.class, () -> BitUtils.writeShortAt((short) 1, buffer1, 1));

    }

    @Test
    public void testLongCompose() {

        for (long src : longTestPatterns()) {

            int i0 = BitUtils.getIntAt(src, 0);
            int i1 = BitUtils.getIntAt(src, 1);

            assertEquals(src, BitUtils.composeLong(i0, i1));

            short s0 = BitUtils.getShortAt(src, 0);
            short s1 = BitUtils.getShortAt(src, 1);
            short s2 = BitUtils.getShortAt(src, 2);
            short s3 = BitUtils.getShortAt(src, 3);

            assertEquals(src, BitUtils.composeLong(s0, s1, s2, s3));

            byte b0 = BitUtils.getByteAt(src, 0);
            byte b1 = BitUtils.getByteAt(src, 1);
            byte b2 = BitUtils.getByteAt(src, 2);
            byte b3 = BitUtils.getByteAt(src, 3);
            byte b4 = BitUtils.getByteAt(src, 4);
            byte b5 = BitUtils.getByteAt(src, 5);
            byte b6 = BitUtils.getByteAt(src, 6);
            byte b7 = BitUtils.getByteAt(src, 7);

            assertEquals(src, BitUtils.composeLong(b0, b1, b2, b3, b4, b5, b6, b7));

        }

    }

    @Test
    public void testIntCompose() {

        for (int src : intTestPatterns()) {

            short s0 = BitUtils.getShortAt(src, 0);
            short s1 = BitUtils.getShortAt(src, 1);

            assertEquals(src, BitUtils.composeInt(s0, s1));

            byte b0 = BitUtils.getByteAt(src, 0);
            byte b1 = BitUtils.getByteAt(src, 1);
            byte b2 = BitUtils.getByteAt(src, 2);
            byte b3 = BitUtils.getByteAt(src, 3);

            assertEquals(src, BitUtils.composeInt(b0, b1, b2, b3));

        }

    }

    @Test
    public void testShortCompose() {

        for (short src : shortTestPatterns()) {

            byte b0 = BitUtils.getByteAt(src, 0);
            byte b1 = BitUtils.getByteAt(src, 1);

            assertEquals(src, BitUtils.composeShort(b0, b1));

        }

    }

    @Test
    public void testRotationVerficationCode() {

        for (long l : longTestPatterns()) {

            for (int i = -64; i < 65; i++) {

                long rotLeft = rotateBitStringLeft(l, 64, i);
                long rotLeftRight = rotateBitStringRight(rotLeft, 64, i);

                assertEquals(l, rotLeftRight);

                rotLeft = rotateBitStringLeft(l, 63, i);
                rotLeftRight = rotateBitStringRight(rotLeft, 63, i);

                assertEquals(l, rotLeftRight);
                assertTrue((l < 0 && rotLeft < 0) || (l >= 0 && rotLeft >= 0));

                rotLeft = rotateBitStringLeft(l, 32, i);
                rotLeftRight = rotateBitStringRight(rotLeft, 32, i);

                assertEquals(l, rotLeftRight);
                assertEquals(BitUtils.binStr(l).substring(0, 32), BitUtils.binStr(rotLeft).substring(0, 32));

                rotLeft = rotateBitStringLeft(l, 15, i);
                rotLeftRight = rotateBitStringRight(rotLeft, 15, i);

                assertEquals(l, rotLeftRight);
                assertEquals(BitUtils.binStr(l).substring(0, 17), BitUtils.binStr(rotLeft).substring(0, 17));

                rotLeft = rotateBitStringLeft(l, 8, i);
                rotLeftRight = rotateBitStringRight(rotLeft, 8, i);

                assertEquals(l, rotLeftRight);
                assertEquals(BitUtils.binStr(l).substring(0, 8), BitUtils.binStr(rotLeft).substring(0, 8));

            }

        }

    }

    private static long[] longTestPatterns() {
        return new long[] { Long.MIN_VALUE, Long.MIN_VALUE + 1, -2, -1, 0, 1, CommonBitMasks.TIKTOK_MASK_LONG, Long.MAX_VALUE };
    }

    private static int[] intTestPatterns() {
        return new int[] { Integer.MIN_VALUE, Integer.MIN_VALUE + 1, -2, -1, 0, 1, CommonBitMasks.TIKTOK_MASK_INT, Integer.MAX_VALUE };
    }

    private static short[] shortTestPatterns() {
        return new short[] { Short.MIN_VALUE, (short) (Short.MIN_VALUE + 1), -2, -1, 0, 1, CommonBitMasks.TIKTOK_MASK_SHORT, Short.MAX_VALUE };
    }

    private static byte[] byteTestPatterns() {
        return new byte[] { Byte.MIN_VALUE, (byte) (Byte.MIN_VALUE + 1), -2, -1, 0, 1, CommonBitMasks.TIKTOK_MASK_BYTE, Byte.MAX_VALUE };
    }

    private static int countBinaryChanges(long l1, long l2) {
        String s1 = BitUtils.binStr(l1);
        String s2 = BitUtils.binStr(l2);
        int res = 0;
        for (int i = 0; i < 64; i++) {
            if (s1.charAt(i) != s2.charAt(i)) {
                res++;
            }
        }
        return res;
    }

    private static int countBinaryChanges(int i1, int i2) {
        return countBinaryChanges(Integer.toUnsignedLong(i1), Integer.toUnsignedLong(i2));
    }

    private static int countBinaryChanges(short s1, short s2) {
        return countBinaryChanges(Short.toUnsignedLong(s1), Short.toUnsignedLong(s2));
    }

    private static int countBinaryChanges(byte b1, byte b2) {
        return countBinaryChanges(Byte.toUnsignedLong(b1), Byte.toUnsignedLong(b2));
    }

    private static long rotateLongBitStringLeft(long l, int distance) {
        return rotateBitStringLeft(l, 64, distance);
    }

    private static long rotateLongBitStringLeftPreserveSign(long l, int distance) {
        return rotateBitStringLeft(l, 63, distance);
    }

    private static int rotateIntBitStringLeft(int i, int distance) {
        return (int) rotateBitStringLeft(Integer.toUnsignedLong(i), 32, distance);
    }

    private static int rotateIntBitStringLeftPreserveSign(int i, int distance) {
        return (int) rotateBitStringLeft(Integer.toUnsignedLong(i), 31, distance);
    }

    private static short rotateShortBitStringLeft(short s, int distance) {
        return (short) rotateBitStringLeft(Short.toUnsignedLong(s), 16, distance);
    }

    private static short rotateShortBitStringLeftPreserveSign(short s, int distance) {
        return (short) rotateBitStringLeft(Short.toUnsignedLong(s), 15, distance);
    }

    private static byte rotateByteBitStringLeft(byte b, int distance) {
        return (byte) rotateBitStringLeft(Byte.toUnsignedLong(b), 8, distance);
    }

    private static byte rotateByteBitStringLeftPreserveSign(byte b, int distance) {
        return (byte) rotateBitStringLeft(Byte.toUnsignedLong(b), 7, distance);
    }

    private static long rotateBitStringLeft(long l, int bitCount, int distance) {

        distance = distance % bitCount;

        if (distance < 0) {
            distance = bitCount + distance;
        }
        if (distance > 0 && distance < bitCount) {
            String src = BitUtils.binStr(l);
            int startIdx = 64 - bitCount;
            String p1 = src.substring(0, startIdx);
            String p2 = src.substring(startIdx, startIdx + distance);
            String p3 = src.substring(startIdx + distance);
            l = BitUtils.parseLong(p1 + p3 + p2);
        }
        return l;
    }

    private static long rotateBitStringRight(long l, int bitCount, int distance) {
        return rotateBitStringLeft(l, bitCount, (distance % bitCount) * -1);
    }

}