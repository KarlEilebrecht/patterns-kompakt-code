//@formatter:off
/*
 * BitUtils
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

import static de.calamanari.pk.drhe.util.CommonBitMasks.BYTE0_MASK_INT;
import static de.calamanari.pk.drhe.util.CommonBitMasks.BYTE0_MASK_INV_INT;
import static de.calamanari.pk.drhe.util.CommonBitMasks.BYTE0_MASK_INV_LONG;
import static de.calamanari.pk.drhe.util.CommonBitMasks.BYTE0_MASK_INV_SHORT;
import static de.calamanari.pk.drhe.util.CommonBitMasks.BYTE0_MASK_LONG;
import static de.calamanari.pk.drhe.util.CommonBitMasks.BYTE0_MASK_SHORT;
import static de.calamanari.pk.drhe.util.CommonBitMasks.BYTE1_MASK_INT;
import static de.calamanari.pk.drhe.util.CommonBitMasks.BYTE1_MASK_INV_INT;
import static de.calamanari.pk.drhe.util.CommonBitMasks.BYTE1_MASK_INV_LONG;
import static de.calamanari.pk.drhe.util.CommonBitMasks.BYTE1_MASK_INV_SHORT;
import static de.calamanari.pk.drhe.util.CommonBitMasks.BYTE1_MASK_LONG;
import static de.calamanari.pk.drhe.util.CommonBitMasks.BYTE1_MASK_SHORT;
import static de.calamanari.pk.drhe.util.CommonBitMasks.BYTE2_MASK_INT;
import static de.calamanari.pk.drhe.util.CommonBitMasks.BYTE2_MASK_INV_INT;
import static de.calamanari.pk.drhe.util.CommonBitMasks.BYTE2_MASK_INV_LONG;
import static de.calamanari.pk.drhe.util.CommonBitMasks.BYTE2_MASK_LONG;
import static de.calamanari.pk.drhe.util.CommonBitMasks.BYTE3_MASK_INT;
import static de.calamanari.pk.drhe.util.CommonBitMasks.BYTE3_MASK_INV_INT;
import static de.calamanari.pk.drhe.util.CommonBitMasks.BYTE3_MASK_INV_LONG;
import static de.calamanari.pk.drhe.util.CommonBitMasks.BYTE3_MASK_LONG;
import static de.calamanari.pk.drhe.util.CommonBitMasks.BYTE4_MASK_INV_LONG;
import static de.calamanari.pk.drhe.util.CommonBitMasks.BYTE4_MASK_LONG;
import static de.calamanari.pk.drhe.util.CommonBitMasks.BYTE5_MASK_INV_LONG;
import static de.calamanari.pk.drhe.util.CommonBitMasks.BYTE5_MASK_LONG;
import static de.calamanari.pk.drhe.util.CommonBitMasks.BYTE6_MASK_INV_LONG;
import static de.calamanari.pk.drhe.util.CommonBitMasks.BYTE6_MASK_LONG;
import static de.calamanari.pk.drhe.util.CommonBitMasks.BYTE7_MASK_INV_LONG;
import static de.calamanari.pk.drhe.util.CommonBitMasks.BYTE7_MASK_LONG;
import static de.calamanari.pk.drhe.util.CommonBitMasks.INT0_MASK_INV_LONG;
import static de.calamanari.pk.drhe.util.CommonBitMasks.INT0_MASK_LONG;
import static de.calamanari.pk.drhe.util.CommonBitMasks.INT1_MASK_INV_LONG;
import static de.calamanari.pk.drhe.util.CommonBitMasks.INT1_MASK_LONG;
import static de.calamanari.pk.drhe.util.CommonBitMasks.SET_FIRST_BIT_MASK_BYTE;
import static de.calamanari.pk.drhe.util.CommonBitMasks.SET_FIRST_BIT_MASK_INT;
import static de.calamanari.pk.drhe.util.CommonBitMasks.SET_FIRST_BIT_MASK_LONG;
import static de.calamanari.pk.drhe.util.CommonBitMasks.SET_FIRST_BIT_MASK_SHORT;
import static de.calamanari.pk.drhe.util.CommonBitMasks.SHORT0_MASK_INT;
import static de.calamanari.pk.drhe.util.CommonBitMasks.SHORT0_MASK_INV_INT;
import static de.calamanari.pk.drhe.util.CommonBitMasks.SHORT0_MASK_INV_LONG;
import static de.calamanari.pk.drhe.util.CommonBitMasks.SHORT0_MASK_LONG;
import static de.calamanari.pk.drhe.util.CommonBitMasks.SHORT1_MASK_INT;
import static de.calamanari.pk.drhe.util.CommonBitMasks.SHORT1_MASK_INV_INT;
import static de.calamanari.pk.drhe.util.CommonBitMasks.SHORT1_MASK_INV_LONG;
import static de.calamanari.pk.drhe.util.CommonBitMasks.SHORT1_MASK_LONG;
import static de.calamanari.pk.drhe.util.CommonBitMasks.SHORT2_MASK_INV_LONG;
import static de.calamanari.pk.drhe.util.CommonBitMasks.SHORT2_MASK_LONG;
import static de.calamanari.pk.drhe.util.CommonBitMasks.SHORT3_MASK_INV_LONG;
import static de.calamanari.pk.drhe.util.CommonBitMasks.SHORT3_MASK_LONG;
import static de.calamanari.pk.drhe.util.CommonBitMasks.UNSET_FIRST_BIT_MASK_BYTE;
import static de.calamanari.pk.drhe.util.CommonBitMasks.UNSET_FIRST_BIT_MASK_INT;
import static de.calamanari.pk.drhe.util.CommonBitMasks.UNSET_FIRST_BIT_MASK_LONG;
import static de.calamanari.pk.drhe.util.CommonBitMasks.UNSET_FIRST_BIT_MASK_SHORT;

/**
 * This class provides a couple of methods to work "intuitively" with the binary representations of numeric values.<br/>
 * Features like binary string writing and reading, de-construction and reconstruction and more are available at a central place.
 * <p/>
 * <b>Important:</b> All methods here follow left-to-right semantics. For example {@link #getByteAt(int, int)} for position <code>0</code> will return the most
 * significant byte (outer left), consequently {@link #getBitAt(byte, int)} first returns the <i>most</i> significant bit for position <code>0</code>.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class BitUtils {

    private static final String MSG_EXPECTED_0_IDX_7_GIVEN = "expected 0 <= idx <= 7, given: ";

    private static final String MSG_EXPECTED_0_IDX_15_GIVEN = "expected 0 <= idx <= 15, given: ";

    private static final String MSG_EXPECTED_0_IDX_31_GIVEN = "expected 0 <= idx <= 31, given: ";

    private static final String MSG_EXPECTED_0_BIT_VALUE_1_GIVEN = "expected 0 <= bitValue <= 1, given: ";

    private static final String MSG_EXPECTED_0_IDX_63_GIVEN = "expected 0 <= idx <= 63, given: ";

    private static final String MSG_EXPECTED_0_IDX_4_GIVEN = "expected: 0 <= idx < 4, given: ";

    private static final String MSG_EXPECTED_0_IDX_2_GIVEN = "expected: 0 <= idx < 2, given: ";

    /**
     * Prints the 64 bits of a long value left-padded with zeros.
     * 
     * @param l to be printed
     * @return binary string
     */
    public static String binStr(long l) {
        String res = "0000000000000000000000000000000000000000000000000000000000000000" + Long.toBinaryString(l);
        return res.substring(res.length() - 64);
    }

    /**
     * Prints the 32 bits of an int value left-padded with zeros.
     * 
     * @param i to be printed
     * @return binary string
     */
    public static final String binStr(int i) {
        String res = "00000000000000000000000000000000" + Integer.toBinaryString(i);

        return res.substring(res.length() - 32);
    }

    /**
     * Prints the 16 bits of a short value left-padded with zeros.
     * 
     * @param s to be printed
     * @return binary string
     */
    public static final String binStr(short s) {
        String res = "0000000000000000" + Integer.toBinaryString(Short.toUnsignedInt(s));

        return res.substring(res.length() - 16);
    }

    /**
     * Prints the 8 bits of a byte value left-padded with zeros.
     * 
     * @param b to be printed
     * @return binary string
     */
    public static final String binStr(byte b) {
        String res = "00000000" + Integer.toBinaryString(Byte.toUnsignedInt(b));
        return res.substring(res.length() - 8);
    }

    /**
     * Converts the binary string into a number representing the same bit sequence
     * 
     * @param binaryString
     * @return number
     */
    public static final long parseLong(String binaryString) {
        return Long.parseUnsignedLong(binaryString, 2);
    }

    /**
     * Converts the binary string into a number representing the same bit sequence
     * 
     * @param binaryString
     * @return number
     */
    public static final int parseInt(String binaryString) {
        return Integer.parseUnsignedInt(binaryString, 2);
    }

    /**
     * Converts the binary string into a number representing the same bit sequence
     * 
     * @param binaryString
     * @return number
     */
    public static final short parseShort(String binaryString) {
        if (binaryString.length() > 16) {
            throw new NumberFormatException("expected number of digits <= 16, given: " + binaryString);
        }
        return (short) Integer.parseUnsignedInt(binaryString, 2);
    }

    /**
     * Converts the binary string into a number representing the same bit sequence
     * 
     * @param binaryString
     * @return number
     */
    public static final byte parseByte(String binaryString) {
        if (binaryString.length() > 8) {
            throw new NumberFormatException("expected number of digits <= 8, given: " + binaryString);
        }
        return (byte) Integer.parseUnsignedInt(binaryString, 2);
    }

    /**
     * Returns the number of flipped bits when bitwise comparing the first input with the second
     * 
     * @param l1 input
     * @param l2 input
     * @return number of flipped bits
     */
    public static final int bitFlipDistance(long l1, long l2) {
        return Long.bitCount(l1 | l2) - Long.bitCount(l1 & l2);
    }

    /**
     * Returns the number of flipped bits when bitwise comparing the first input with the second
     * 
     * @param i1 input
     * @param i2 input
     * @return number of flipped bits
     */
    public static final int bitFlipDistance(int i1, int i2) {
        return Integer.bitCount(i1 | i2) - Integer.bitCount(i1 & i2);
    }

    /**
     * Returns the number of flipped bits when bitwise comparing the first input with the second
     * 
     * @param s1 input
     * @param s2 input
     * @return number of flipped bits
     */
    public static final int bitFlipDistance(short s1, short s2) {
        return Integer.bitCount(Short.toUnsignedInt(s1) | Short.toUnsignedInt(s2)) - Integer.bitCount(Short.toUnsignedInt(s1) & Short.toUnsignedInt(s2));
    }

    /**
     * Returns the number of flipped bits when bitwise comparing the first input with the second
     * 
     * @param b1 input
     * @param b2 input
     * @return number of flipped bits
     */
    public static final int bitFlipDistance(byte b1, byte b2) {
        return Integer.bitCount(Byte.toUnsignedInt(b1) | Byte.toUnsignedInt(b2)) - Integer.bitCount(Byte.toUnsignedInt(b1) & Byte.toUnsignedInt(b2));
    }

    /**
     * @param num source
     * @return number of '1's in the bit sequence
     */
    public static final int bitCount(long num) {
        return Long.bitCount(num);
    }

    /**
     * @param num source
     * @return number of '1's in the bit sequence
     */
    public static final int bitCount(int num) {
        return Integer.bitCount(num);
    }

    /**
     * @param num source
     * @return number of '1's in the bit sequence
     */
    public static final int bitCount(short num) {
        return Long.bitCount(Short.toUnsignedLong(num));
    }

    /**
     * @param num source
     * @return number of '1's in the bit sequence
     */
    public static final int bitCount(byte num) {
        return Long.bitCount(Byte.toUnsignedLong(num));
    }

    /**
     * Rotates 63 bits left, leaving the sign-bit unchanged
     * 
     * @param num to be rotated
     * @param distance
     * @return modified number
     */
    public static final long rotateBitsLeftPreserveSign(long num, int distance) {
        if (distance == 64) {
            distance = 1;
        }
        else if (distance == -64) {
            distance = 62;
        }
        if (distance < 0) {
            distance = 63 + (distance % 63);
        }
        else if (distance > 63) {
            distance = distance % 63;
        }
        if (num != Long.MIN_VALUE && num != Long.MAX_VALUE && distance != 0 && distance != 63) {
            long sign = num & SET_FIRST_BIT_MASK_LONG;
            long tail = (num & UNSET_FIRST_BIT_MASK_LONG) >> (63L - distance);
            long front = (num << (distance + 1L)) >>> 1L;
            num = sign | front | tail;
        }

        return num;
    }

    /**
     * Rotates 63 bits right, leaving the sign-bit unchanged
     * 
     * @param num to be rotated
     * @param distance
     * @return modified number
     */
    public static final long rotateBitsRightPreserveSign(long num, int distance) {
        if (distance == Integer.MIN_VALUE) {
            distance = -2;
        }
        return rotateBitsLeftPreserveSign(num, distance * -1);
    }

    /**
     * Rotates bits left
     * 
     * @param num to be rotated
     * @param distance
     * @return modified number
     */
    public static final long rotateBitsLeft(long num, int distance) {
        return Long.rotateLeft(num, distance);
    }

    /**
     * Rotates bits right
     * 
     * @param num to be rotated
     * @param distance
     * @return modified number
     */
    public static final long rotateBitsRight(long num, int distance) {
        return Long.rotateRight(num, distance);
    }

    /**
     * Rotates 31 bits left, leaving the sign-bit unchanged
     * 
     * @param num to be rotated
     * @param distance
     * @return modified number
     */
    public static final int rotateBitsLeftPreserveSign(int num, int distance) {
        if (distance == 32) {
            distance = 1;
        }
        else if (distance == -32) {
            distance = 30;
        }
        if (distance < 0) {
            distance = 31 + (distance % 31);
        }
        else if (distance > 31) {
            distance = distance % 31;
        }
        if (num != Integer.MIN_VALUE && num != Integer.MAX_VALUE && distance != 0 && distance != 31) {
            int sign = num & SET_FIRST_BIT_MASK_INT;
            int tail = (num & UNSET_FIRST_BIT_MASK_INT) >> (31 - distance);
            int front = (num << (distance + 1)) >>> 1;
            num = sign | front | tail;
        }

        return num;
    }

    /**
     * Rotates 31 bits right, leaving the sign-bit unchanged
     * 
     * @param num to be rotated
     * @param distance
     * @return modified number
     */
    public static final int rotateBitsRightPreserveSign(int num, int distance) {
        if (distance == Integer.MIN_VALUE) {
            distance = -2;
        }
        return rotateBitsLeftPreserveSign(num, distance * -1);
    }

    /**
     * Rotates bits left
     * 
     * @param num to be rotated
     * @param distance
     * @return modified number
     */
    public static final int rotateBitsLeft(int num, int distance) {
        return Integer.rotateLeft(num, distance);
    }

    /**
     * Rotates bits right
     * 
     * @param num to be rotated
     * @param distance
     * @return modified number
     */
    public static final int rotateBitsRight(int num, int distance) {
        return Integer.rotateRight(num, distance);
    }

    /**
     * Rotates 15 bits left, leaving the sign-bit unchanged
     * 
     * @param num to be rotated
     * @param distance
     * @return modified number
     */
    public static final short rotateBitsLeftPreserveSign(short num, int distance) {

        if (distance == 16) {
            distance = 1;
        }
        else if (distance == -16) {
            distance = 14;
        }
        if (distance < 0) {
            distance = 15 + (distance % 15);
        }
        else if (distance > 15) {
            distance = distance % 15;
        }
        if (num != Short.MIN_VALUE && num != Short.MAX_VALUE && distance != 0 && distance != 15) {
            short sign = (short) (num & SET_FIRST_BIT_MASK_SHORT);
            short tail = (short) ((num & UNSET_FIRST_BIT_MASK_SHORT) >> (15 - distance));
            short front = (short) (((num << (distance + 1)) >>> 1) & UNSET_FIRST_BIT_MASK_SHORT);
            num = (short) (sign | front | tail);
        }

        return num;
    }

    /**
     * Rotates 15 bits right, leaving the sign-bit unchanged
     * 
     * @param num to be rotated
     * @param distance
     * @return modified number
     */
    public static final short rotateBitsRightPreserveSign(short num, int distance) {
        if (distance == Integer.MIN_VALUE) {
            distance = -8;
        }
        return rotateBitsLeftPreserveSign(num, distance * -1);

    }

    /**
     * Rotates bits left
     * 
     * @param num to be rotated
     * @param distance
     * @return modified number
     */
    public static final short rotateBitsLeft(short num, int distance) {
        distance = distance & 0xf;
        if (distance != 0) {
            int src = Short.toUnsignedInt(num);
            int tail = (src >> (16 - distance));
            int front = (src << distance) & CommonBitMasks.SHORT1_MASK_INT;
            num = (short) (front | tail);
        }

        return num;
    }

    /**
     * Rotates bits right
     * 
     * @param num to be rotated
     * @param distance &gt;=0
     * @return modified number
     */
    public static final short rotateBitsRight(short num, int distance) {
        if (distance == Integer.MIN_VALUE) {
            return num;
        }
        return rotateBitsLeft(num, distance * -1);

    }

    /**
     * Rotates 7 bits left, leaving the sign-bit unchanged
     * 
     * @param num to be rotated
     * @param distance &gt;=0
     * @return modified number
     */
    public static final byte rotateBitsLeftPreserveSign(byte num, int distance) {

        if (distance == 8) {
            distance = 1;
        }
        else if (distance == -8) {
            distance = 6;
        }
        if (distance < 0) {
            distance = 7 + (distance % 7);
        }
        else if (distance > 7) {
            distance = distance % 7;
        }
        if (num != Byte.MIN_VALUE && num != Byte.MAX_VALUE && distance != 0 && distance != 7) {
            byte sign = (byte) (num & SET_FIRST_BIT_MASK_BYTE);
            byte tail = (byte) ((num & UNSET_FIRST_BIT_MASK_BYTE) >> (7 - distance));
            byte front = (byte) (((num << (distance + 1)) >>> 1) & Byte.toUnsignedInt(UNSET_FIRST_BIT_MASK_BYTE));
            num = (byte) (sign | front | tail);
        }

        return num;
    }

    /**
     * Rotates 7 bits right, leaving the sign-bit unchanged
     * 
     * @param num to be rotated
     * @param distance &gt;=0
     * @return modified number
     */
    public static final byte rotateBitsRightPreserveSign(byte num, int distance) {
        if (distance == Integer.MIN_VALUE) {
            distance = -2;
        }
        return rotateBitsLeftPreserveSign(num, distance * -1);
    }

    /**
     * Rotates bits left
     * 
     * @param num to be rotated
     * @param distance &gt;=0
     * @return modified number
     */
    public static final byte rotateBitsLeft(byte num, int distance) {
        distance = distance & 7;
        if (distance != 0) {
            int src = Byte.toUnsignedInt(num);
            int tail = (src >> (8 - distance));
            int front = (src << distance) & CommonBitMasks.BYTE3_MASK_INT;
            num = (byte) (front | tail);
        }

        return num;
    }

    /**
     * Rotates bits right
     * 
     * @param num to be rotated
     * @param distance &gt;=0
     * @return modified number
     */
    public static final byte rotateBitsRight(byte num, int distance) {
        if (distance == Integer.MIN_VALUE) {
            return num;
        }
        return rotateBitsLeft(num, distance * -1);

    }

    /**
     * Returns the int of the bit sequence from left to right, so that concatenating the binary strings <br/>
     * of the returned values equals the binary string representation of the given input.
     * 
     * @param num input
     * @param idx int index (from left to right)
     * @return byte at the given index
     */
    public static final int getIntAt(long num, int idx) {
        switch (idx) {
        case 0:
            return (int) ((num & INT0_MASK_LONG) >> 32);
        case 1:
            return (int) (num & INT1_MASK_LONG);
        default:
            throw new IndexOutOfBoundsException(MSG_EXPECTED_0_IDX_2_GIVEN + idx);
        }
    }

    /**
     * Sets the int at the given position not affecting the other ints
     * 
     * @param num where to set int
     * @param i int value to be set a the given position
     * @param idx int index (from left to right)
     * @return updated number
     */
    public static final long setIntAt(long num, int i, int idx) {
        switch (idx) {
        case 0:
            return (num & INT0_MASK_INV_LONG) | (Integer.toUnsignedLong(i) << 32);
        case 1:
            return (num & INT1_MASK_INV_LONG) | Integer.toUnsignedLong(i);
        default:
            throw new IndexOutOfBoundsException(MSG_EXPECTED_0_IDX_2_GIVEN + idx);
        }

    }

    /**
     * Returns the shorts of the bit sequence from left to right, so that concatenating the binary strings <br/>
     * of the returned values equals the binary string representation of the given input.
     * 
     * @param num input
     * @param idx short index (from left to right)
     * @return byte at the given index
     */
    public static final short getShortAt(long num, int idx) {
        switch (idx) {
        case 0:
            return (short) ((num & SHORT0_MASK_LONG) >> 48);
        case 1:
            return (short) ((num & SHORT1_MASK_LONG) >> 32);
        case 2:
            return (short) ((num & SHORT2_MASK_LONG) >> 16);
        case 3:
            return (short) (num & SHORT3_MASK_LONG);
        default:
            throw new IndexOutOfBoundsException(MSG_EXPECTED_0_IDX_4_GIVEN + idx);
        }
    }

    /**
     * Sets the short at the given position not affecting the other shorts
     * 
     * @param num where to set short
     * @param s short value to be set a the given position
     * @param idx short index (from left to right)
     * @return updated number
     */
    public static final long setShortAt(long num, short s, int idx) {
        switch (idx) {
        case 0:
            return (num & SHORT0_MASK_INV_LONG) | (Short.toUnsignedLong(s) << 48);
        case 1:
            return (num & SHORT1_MASK_INV_LONG) | (Short.toUnsignedLong(s) << 32);
        case 2:
            return (num & SHORT2_MASK_INV_LONG) | (Short.toUnsignedLong(s) << 16);
        case 3:
            return (num & SHORT3_MASK_INV_LONG) | Short.toUnsignedLong(s);
        default:
            throw new IndexOutOfBoundsException(MSG_EXPECTED_0_IDX_4_GIVEN + idx);
        }

    }

    /**
     * Returns the shorts of the bit sequence from left to right, so that concatenating the binary strings <br/>
     * of the returned values equals the binary string representation of the given input.
     * 
     * @param num input
     * @param idx short index (from left to right)
     * @return byte at the given index
     */
    public static final short getShortAt(int num, int idx) {
        switch (idx) {
        case 0:
            return (short) ((num & SHORT0_MASK_INT) >> 16);
        case 1:
            return (short) (num & SHORT1_MASK_INT);
        default:
            throw new IndexOutOfBoundsException(MSG_EXPECTED_0_IDX_2_GIVEN + idx);
        }
    }

    /**
     * Sets the short at the given position not affecting the other short
     * 
     * @param num where to set short
     * @param s short value to be set a the given position
     * @param idx short index (from left to right)
     * @return updated number
     */
    public static final int setShortAt(int num, short s, int idx) {
        switch (idx) {
        case 0:
            return (num & SHORT0_MASK_INV_INT) | (Short.toUnsignedInt(s) << 16);
        case 1:
            return (num & SHORT1_MASK_INV_INT) | Short.toUnsignedInt(s);
        default:
            throw new IndexOutOfBoundsException(MSG_EXPECTED_0_IDX_2_GIVEN + idx);
        }
    }

    /**
     * Returns the byte of the bit sequence from left to right, so that concatenating the binary strings <br/>
     * of the returned values equals the binary string representation of the given input.
     * 
     * @param num input
     * @param idx byte index (from left to right)
     * @return byte at the given index
     */
    public static final byte getByteAt(long num, int idx) {
        switch (idx) {
        case 0:
            return (byte) ((num & BYTE0_MASK_LONG) >> 56);
        case 1:
            return (byte) ((num & BYTE1_MASK_LONG) >> 48);
        case 2:
            return (byte) ((num & BYTE2_MASK_LONG) >> 40);
        case 3:
            return (byte) ((num & BYTE3_MASK_LONG) >> 32);
        case 4:
            return (byte) ((num & BYTE4_MASK_LONG) >> 24);
        case 5:
            return (byte) ((num & BYTE5_MASK_LONG) >> 16);
        case 6:
            return (byte) ((num & BYTE6_MASK_LONG) >> 8);
        case 7:
            return (byte) (num & BYTE7_MASK_LONG);
        default:
            throw new IndexOutOfBoundsException("expected: 0 <= idx < 8, given: " + idx);
        }
    }

    /**
     * Sets the byte at the given position not affecting the other bytes
     * 
     * @param num where to set byte
     * @param b byte to be set a the given position
     * @param idx byte index (from left to right)
     * @return updated number
     */
    public static final long setByteAt(long num, byte b, int idx) {
        switch (idx) {
        case 0:
            return (num & BYTE0_MASK_INV_LONG) | (Byte.toUnsignedLong(b) << 56);
        case 1:
            return (num & BYTE1_MASK_INV_LONG) | (Byte.toUnsignedLong(b) << 48);
        case 2:
            return (num & BYTE2_MASK_INV_LONG) | (Byte.toUnsignedLong(b) << 40);
        case 3:
            return (num & BYTE3_MASK_INV_LONG) | (Byte.toUnsignedLong(b) << 32);
        case 4:
            return (num & BYTE4_MASK_INV_LONG) | (Byte.toUnsignedLong(b) << 24);
        case 5:
            return (num & BYTE5_MASK_INV_LONG) | (Byte.toUnsignedLong(b) << 16);
        case 6:
            return (num & BYTE6_MASK_INV_LONG) | (Byte.toUnsignedLong(b) << 8);
        case 7:
            return (num & BYTE7_MASK_INV_LONG) | Byte.toUnsignedLong(b);
        default:
            throw new IndexOutOfBoundsException("expected: 0 <= idx < 8, given: " + idx);
        }

    }

    /**
     * Returns the bytes of the bit sequence from left to right, so that concatenating the binary strings <br/>
     * of the returned values equals the binary string representation of the given input.
     * 
     * @param num input
     * @param idx byte index (from left to right)
     * @return byte at the given index
     */
    public static final byte getByteAt(int num, int idx) {
        switch (idx) {
        case 0:
            return (byte) ((num & BYTE0_MASK_INT) >> 24);
        case 1:
            return (byte) ((num & BYTE1_MASK_INT) >> 16);
        case 2:
            return (byte) ((num & BYTE2_MASK_INT) >> 8);
        case 3:
            return (byte) (num & BYTE3_MASK_INT);
        default:
            throw new IndexOutOfBoundsException(MSG_EXPECTED_0_IDX_4_GIVEN + idx);
        }
    }

    /**
     * Sets the byte at the given position not affecting the other bytes
     * 
     * @param num where to set byte
     * @param b byte to be set a the given position
     * @param idx byte index (from left to right)
     * @return updated number
     */
    public static final int setByteAt(int num, byte b, int idx) {
        switch (idx) {
        case 0:
            return (num & BYTE0_MASK_INV_INT) | (Byte.toUnsignedInt(b) << 24);
        case 1:
            return (num & BYTE1_MASK_INV_INT) | (Byte.toUnsignedInt(b) << 16);
        case 2:
            return (num & BYTE2_MASK_INV_INT) | (Byte.toUnsignedInt(b) << 8);
        case 3:
            return (num & BYTE3_MASK_INV_INT) | Byte.toUnsignedInt(b);
        default:
            throw new IndexOutOfBoundsException(MSG_EXPECTED_0_IDX_4_GIVEN + idx);
        }

    }

    /**
     * Returns the bytes of the bit sequence from left to right, so that concatenating the binary strings <br/>
     * of the returned values equals the binary string representation of the given input.
     * 
     * @param num input
     * @param idx byte index (from left to right)
     * @return byte at the given index
     */
    public static final byte getByteAt(short num, int idx) {
        switch (idx) {
        case 0:
            return (byte) ((num & BYTE0_MASK_SHORT) >> 8);
        case 1:
            return (byte) (num & BYTE1_MASK_SHORT);
        default:
            throw new IndexOutOfBoundsException(MSG_EXPECTED_0_IDX_2_GIVEN + idx);
        }
    }

    /**
     * Sets the byte at the given position not affecting the other bytes
     *
     * @param num where to set byte
     * @param b byte to be set a the given position
     * @param idx byte index (from left to right)
     * @return updated number
     */
    public static final short setByteAt(short num, byte b, int idx) {
        switch (idx) {
        case 0:
            return (short) ((num & BYTE0_MASK_INV_SHORT) | (Byte.toUnsignedInt(b) << 8));
        case 1:
            return (short) ((num & BYTE1_MASK_INV_SHORT) | Byte.toUnsignedInt(b));
        default:
            throw new IndexOutOfBoundsException(MSG_EXPECTED_0_IDX_2_GIVEN + idx);
        }

    }

    /**
     * Returns the bit value of the bit at the given position
     * 
     * @param num input
     * @param idx (from left to right, 0 is the <i>most</i> significant bit)
     * @return bit state <code>0</code> or <code>1</code>
     */
    public static final int getBitAt(long num, int idx) {
        if (idx < 0 || idx > 63) {
            throw new IndexOutOfBoundsException(MSG_EXPECTED_0_IDX_63_GIVEN + idx);
        }
        return (int) ((num & (CommonBitMasks.SET_FIRST_BIT_MASK_LONG >>> idx)) >>> (63 - idx));
    }

    /**
     * Sets the bit at the given position to the given value.
     * 
     * @param num source value
     * @param bitValue must be <code>0</code> or <code>1</code>
     * @param idx (from left to right, 0 is the <i>most</i> significant bit)
     * @return updated value
     */
    public static final long setBitAt(long num, int bitValue, int idx) {
        if (idx < 0 || idx > 63) {
            throw new IndexOutOfBoundsException(MSG_EXPECTED_0_IDX_63_GIVEN + idx);
        }

        switch (bitValue) {
        case 1:
            return num | (CommonBitMasks.SET_FIRST_BIT_MASK_LONG >>> idx);
        case 0:
            return num & Long.rotateRight(CommonBitMasks.UNSET_FIRST_BIT_MASK_LONG, idx);
        default:
            throw new IllegalArgumentException(MSG_EXPECTED_0_IDX_63_GIVEN + bitValue);
        }

    }

    /**
     * Flips the bit at the given position to the given value.
     * 
     * @param num source value
     * @param idx (from left to right, 0 is the <i>most</i> significant bit)
     * @return updated value
     */
    public static final long flipBitAt(long num, int idx) {
        if (idx < 0 || idx > 63) {
            throw new IndexOutOfBoundsException(MSG_EXPECTED_0_IDX_63_GIVEN + idx);
        }
        return num ^ (CommonBitMasks.SET_FIRST_BIT_MASK_LONG >>> idx);
    }

    /**
     * Returns the bit value of the bit at the given position
     * 
     * @param num input
     * @param idx (from left to right, 0 is the <i>most</i> significant bit)
     * @return bit state <code>0</code> or <code>1</code>
     */
    public static final int getBitAt(int num, int idx) {
        if (idx < 0 || idx > 31) {
            throw new IndexOutOfBoundsException(MSG_EXPECTED_0_IDX_31_GIVEN + idx);
        }
        return (num & (CommonBitMasks.SET_FIRST_BIT_MASK_INT >>> idx)) >>> (31 - idx);
    }

    /**
     * Sets the bit at the given position to the given value.
     * 
     * @param num source value
     * @param bitValue must be <code>0</code> or <code>1</code>
     * @param idx (from left to right, 0 is the <i>most</i> significant bit)
     * @return updated value
     */
    public static final int setBitAt(int num, int bitValue, int idx) {
        if (idx < 0 || idx > 31) {
            throw new IndexOutOfBoundsException(MSG_EXPECTED_0_IDX_31_GIVEN + idx);
        }

        switch (bitValue) {
        case 1:
            return num | (CommonBitMasks.SET_FIRST_BIT_MASK_INT >>> idx);
        case 0:
            return num & Integer.rotateRight(CommonBitMasks.UNSET_FIRST_BIT_MASK_INT, idx);
        default:
            throw new IllegalArgumentException(MSG_EXPECTED_0_BIT_VALUE_1_GIVEN + bitValue);
        }

    }

    /**
     * Flips the bit at the given position to the given value.
     * 
     * @param num source value
     * @param idx (from left to right, 0 is the <i>most</i> significant bit)
     * @return updated value
     */
    public static final int flipBitAt(int num, int idx) {
        if (idx < 0 || idx > 31) {
            throw new IndexOutOfBoundsException(MSG_EXPECTED_0_IDX_31_GIVEN + idx);
        }
        return num ^ (CommonBitMasks.SET_FIRST_BIT_MASK_INT >>> idx);
    }

    /**
     * Returns the bit value of the bit at the given position
     * 
     * @param num input
     * @param idx (from left to right, 0 is the <i>most</i> significant bit)
     * @return bit state <code>0</code> or <code>1</code>
     */
    public static final int getBitAt(short num, int idx) {
        if (idx < 0 || idx > 15) {
            throw new IndexOutOfBoundsException(MSG_EXPECTED_0_IDX_15_GIVEN + idx);
        }
        return (Short.toUnsignedInt(num) & (CommonBitMasks.SET_FIRST_BIT_MASK_INT >>> (16 + idx))) >>> (15 - idx);
    }

    /**
     * Sets the bit at the given position to the given value.
     * 
     * @param num source value
     * @param bitValue must be <code>0</code> or <code>1</code>
     * @param idx (from left to right, 0 is the <i>most</i> significant bit)
     * @return updated value
     */
    public static final short setBitAt(short num, int bitValue, int idx) {
        if (idx < 0 || idx > 15) {
            throw new IndexOutOfBoundsException(MSG_EXPECTED_0_IDX_15_GIVEN + idx);
        }

        switch (bitValue) {
        case 1:
            return (short) (Short.toUnsignedInt(num) | (CommonBitMasks.SET_FIRST_BIT_MASK_INT >>> (16 + idx)));
        case 0:
            return (short) (Short.toUnsignedInt(num) & Integer.rotateRight(CommonBitMasks.UNSET_FIRST_BIT_MASK_INT, (16 + idx)));
        default:
            throw new IllegalArgumentException(MSG_EXPECTED_0_BIT_VALUE_1_GIVEN + bitValue);
        }
    }

    /**
     * Flips the bit at the given position to the given value.
     * 
     * @param num source value
     * @param idx (from left to right, 0 is the <i>most</i> significant bit)
     * @return updated value
     */
    public static final short flipBitAt(short num, int idx) {
        if (idx < 0 || idx > 15) {
            throw new IndexOutOfBoundsException(MSG_EXPECTED_0_IDX_15_GIVEN + idx);
        }
        return (short) (Short.toUnsignedInt(num) ^ (CommonBitMasks.SET_FIRST_BIT_MASK_INT >>> (16 + idx)));
    }

    /**
     * Returns the bit value of the bit at the given position
     * 
     * @param num input
     * @param idx (from left to right, 0 is the <i>most</i> significant bit)
     * @return bit state <code>0</code> or <code>1</code>
     */
    public static final int getBitAt(byte num, int idx) {
        if (idx < 0 || idx > 7) {
            throw new IndexOutOfBoundsException(MSG_EXPECTED_0_IDX_7_GIVEN + idx);
        }
        return (Byte.toUnsignedInt(num) & (CommonBitMasks.SET_FIRST_BIT_MASK_INT >>> (24 + idx))) >>> (7 - idx);
    }

    /**
     * Sets the bit at the given position to the given value.
     * 
     * @param num source value
     * @param bitValue must be <code>0</code> or <code>1</code>
     * @param idx (from left to right, 0 is the <i>most</i> significant bit)
     * @return updated value
     */
    public static final byte setBitAt(byte num, int bitValue, int idx) {
        if (idx < 0 || idx > 7) {
            throw new IndexOutOfBoundsException(MSG_EXPECTED_0_IDX_7_GIVEN + idx);
        }

        switch (bitValue) {
        case 1:
            return (byte) (Byte.toUnsignedInt(num) | (CommonBitMasks.SET_FIRST_BIT_MASK_INT >>> (24 + idx)));
        case 0:
            return (byte) (Short.toUnsignedInt(num) & Integer.rotateRight(CommonBitMasks.UNSET_FIRST_BIT_MASK_INT, (24 + idx)));
        default:
            throw new IllegalArgumentException(MSG_EXPECTED_0_BIT_VALUE_1_GIVEN + bitValue);
        }
    }

    /**
     * Flips the bit at the given position to the given value.
     * 
     * @param num source value
     * @param idx (from left to right, 0 is the <i>most</i> significant bit)
     * @return updated value
     */
    public static final byte flipBitAt(byte num, int idx) {
        if (idx < 0 || idx > 7) {
            throw new IndexOutOfBoundsException(MSG_EXPECTED_0_IDX_7_GIVEN + idx);
        }
        return (byte) (Byte.toUnsignedInt(num) ^ (CommonBitMasks.SET_FIRST_BIT_MASK_INT >>> (24 + idx)));
    }

    /**
     * Writes the 8 bytes of the long into the given array at the given position, most significant first (left to right).
     * 
     * @param num source value
     * @param dest destination
     * @param startIdx where to start writing
     */
    public static final void writeLongAt(long num, byte[] dest, int startIdx) {
        if (startIdx + 8 > dest.length) {
            throw new IndexOutOfBoundsException(String.format("8 bytes required, given: startIdx=%d, dest.length=%d", startIdx, dest.length));
        }
        dest[startIdx] = (byte) ((num & CommonBitMasks.BYTE0_MASK_LONG) >>> 56);
        dest[startIdx + 1] = (byte) ((num & CommonBitMasks.BYTE1_MASK_LONG) >>> 48);
        dest[startIdx + 2] = (byte) ((num & CommonBitMasks.BYTE2_MASK_LONG) >>> 40);
        dest[startIdx + 3] = (byte) ((num & CommonBitMasks.BYTE3_MASK_LONG) >>> 32);
        dest[startIdx + 4] = (byte) ((num & CommonBitMasks.BYTE4_MASK_LONG) >>> 24);
        dest[startIdx + 5] = (byte) ((num & CommonBitMasks.BYTE5_MASK_LONG) >>> 16);
        dest[startIdx + 6] = (byte) ((num & CommonBitMasks.BYTE6_MASK_LONG) >>> 8);
        dest[startIdx + 7] = (byte) (num & CommonBitMasks.BYTE7_MASK_LONG);
    }

    /**
     * Returns the 8 bytes of the long as an array, most significant first (left to right).
     * 
     * @param num source value
     * @param byte array
     */
    public static final byte[] getBytes(long num) {
        byte[] res = new byte[8];
        writeLongAt(num, res, 0);
        return res;
    }

    /**
     * Reads the long value (8 bytes) from the given byte array at the given position, most significant first (left to right).
     * 
     * @param src to obtain bytes from
     * @param startIdx where to start
     * @return parsed value
     */
    public static final long readLongAt(byte[] src, int startIdx) {
        if (startIdx + 8 > src.length) {
            throw new IndexOutOfBoundsException(String.format("8 bytes required, given: startIdx=%d, src.length=%d", startIdx, src.length));
        }

        long res = Byte.toUnsignedLong(src[startIdx]) << 56;
        res = res | (Byte.toUnsignedLong(src[startIdx + 1]) << 48);
        res = res | (Byte.toUnsignedLong(src[startIdx + 2]) << 40);
        res = res | (Byte.toUnsignedLong(src[startIdx + 3]) << 32);
        res = res | (Byte.toUnsignedLong(src[startIdx + 4]) << 24);
        res = res | (Byte.toUnsignedLong(src[startIdx + 5]) << 16);
        res = res | (Byte.toUnsignedLong(src[startIdx + 6]) << 8);
        res = res | Byte.toUnsignedLong(src[startIdx + 7]);
        return res;
    }

    /**
     * Writes the 4 bytes of the int value into the given array at the given position, most significant first (left to right).
     * 
     * @param num source value
     * @param dest destination
     * @param startIdx where to start writing
     */
    public static final void writeIntAt(int num, byte[] dest, int startIdx) {
        if (startIdx + 4 > dest.length) {
            throw new IndexOutOfBoundsException(String.format("4 bytes required, given: startIdx=%d, dest.length=%d", startIdx, dest.length));
        }
        dest[startIdx] = (byte) ((num & CommonBitMasks.BYTE0_MASK_INT) >>> 24);
        dest[startIdx + 1] = (byte) ((num & CommonBitMasks.BYTE1_MASK_INT) >>> 16);
        dest[startIdx + 2] = (byte) ((num & CommonBitMasks.BYTE2_MASK_INT) >>> 8);
        dest[startIdx + 3] = (byte) (num & CommonBitMasks.BYTE3_MASK_INT);
    }

    /**
     * Returns the 4 bytes of the int value as an array, most significant first (left to right).
     * 
     * @param num source value
     * @param byte array
     */
    public static final byte[] getBytes(int num) {
        byte[] res = new byte[4];
        writeIntAt(num, res, 0);
        return res;
    }

    /**
     * Reads the int value (4 bytes) from the given byte array at the given position, most significant first (left to right).
     * 
     * @param src to obtain bytes from
     * @param startIdx where to start
     * @return parsed value
     */
    public static final int readIntAt(byte[] src, int startIdx) {
        if (startIdx + 4 > src.length) {
            throw new IndexOutOfBoundsException(String.format("4 bytes required, given: startIdx=%d, src.length=%d", startIdx, src.length));
        }

        int res = Byte.toUnsignedInt(src[startIdx]) << 24;
        res = res | (Byte.toUnsignedInt(src[startIdx + 1]) << 16);
        res = res | (Byte.toUnsignedInt(src[startIdx + 2]) << 8);
        res = res | Byte.toUnsignedInt(src[startIdx + 3]);
        return res;
    }

    /**
     * Writes the 2 bytes of the short value into the given array at the given position, most significant first (left to right).
     * 
     * @param num source value
     * @param dest destination
     * @param startIdx where to start writing
     */
    public static final void writeShortAt(short num, byte[] dest, int startIdx) {
        if (startIdx + 2 > dest.length) {
            throw new IndexOutOfBoundsException(String.format("2 bytes required, given: startIdx=%d, dest.length=%d", startIdx, dest.length));
        }
        dest[startIdx] = (byte) ((num & CommonBitMasks.BYTE0_MASK_SHORT) >>> 8);
        dest[startIdx + 1] = (byte) (num & CommonBitMasks.BYTE1_MASK_SHORT);
    }

    /**
     * Returns the 2 bytes of the short value as an array, most significant first (left to right).
     * 
     * @param num source value
     * @param byte array
     */
    public static final byte[] getBytes(short num) {
        byte[] res = new byte[2];
        writeShortAt(num, res, 0);
        return res;
    }

    /**
     * Reads the short value (2 bytes) from the given byte array at the given position, most significant first (left to right).
     * 
     * @param src to obtain bytes from
     * @param startIdx where to start
     * @return parsed value
     */
    public static final short readShortAt(byte[] src, int startIdx) {
        if (startIdx + 2 > src.length) {
            throw new IndexOutOfBoundsException(String.format("2 bytes required, given: startIdx=%d, src.length=%d", startIdx, src.length));
        }

        int res = Byte.toUnsignedInt(src[startIdx]) << 8;
        res = res | Byte.toUnsignedInt(src[startIdx + 1]);
        return (short) res;
    }

    /**
     * Re-creates the long value from 2 ints (left to right), so that the bit sequence is the same as when printing all the ints one after another.
     * 
     * @param ints 2 elements required
     * @return parsed value
     */
    public static final long composeLong(int... ints) {

        if (ints.length != 2) {
            throw new IndexOutOfBoundsException("expected array length == 2, given: " + ints.length);
        }
        long res = Integer.toUnsignedLong(ints[0]) << 32;
        res = res | Integer.toUnsignedLong(ints[1]);
        return res;
    }

    /**
     * Re-creates the long value from 4 shorts (left to right), so that the bit sequence is the same as when printing all the shorts one after another.
     * 
     * @param shorts 4 elements required
     * @return parsed value
     */
    public static final long composeLong(short... shorts) {

        if (shorts.length != 4) {
            throw new IndexOutOfBoundsException("expected array length == 4, given: " + shorts.length);
        }
        long res = Short.toUnsignedLong(shorts[0]) << 48;
        res = res | (Short.toUnsignedLong(shorts[1]) << 32);
        res = res | (Short.toUnsignedLong(shorts[2]) << 16);
        res = res | Short.toUnsignedLong(shorts[3]);
        return res;
    }

    /**
     * Re-creates the long value from 8 bytes (left to right), so that the bit sequence is the same as when printing all the bytes one after another.
     * 
     * @param bytes 8 elements required
     * @return parsed value
     */
    public static final long composeLong(byte... bytes) {
        return readLongAt(bytes, 0);
    }

    /**
     * Re-creates the int value from 2 shorts (left to right), so that the bit sequence is the same as when printing all the shorts one after another.
     * 
     * @param shorts 2 elements required
     * @return parsed value
     */
    public static final int composeInt(short... shorts) {

        if (shorts.length != 2) {
            throw new IndexOutOfBoundsException("expected array length == 2, given: " + shorts.length);
        }
        int res = Short.toUnsignedInt(shorts[0]) << 16;
        res = res | Short.toUnsignedInt(shorts[1]);
        return res;
    }

    /**
     * Re-creates the int value from 4 bytes (left to right), so that the bit sequence is the same as when printing all the bytes one after another.
     * 
     * @param bytes 4 elements required
     * @return parsed value
     */
    public static final int composeInt(byte... bytes) {
        return readIntAt(bytes, 0);
    }

    /**
     * Re-creates the short value from 2 bytes (left to right), so that the bit sequence is the same as when printing all the bytes one after another.
     * 
     * @param bytes 2 elements required
     * @return parsed value
     */
    public static final short composeShort(byte... bytes) {
        return readShortAt(bytes, 0);
    }

    private BitUtils() {
        // no instances
    }
}