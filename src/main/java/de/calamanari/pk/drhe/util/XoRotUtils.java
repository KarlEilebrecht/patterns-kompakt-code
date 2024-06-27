//@formatter:off
/*
 * XoRotUtils
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

/**
 * This class contains the basic function we use for distributing numbers in a range.
 * <p/>
 * The basic idea combines XOR with binary patterns followed by rotation, where the rotation distance equals the number of <b><code>1</code></b>-bits in a given
 * pattern.<br/>
 * This way the operation can be reverted, in other words: no two values can be mapped to the same destination value.
 * <p>
 * Callers shall provide the same array of patterns (same order) to the <i>decode</i> method they previously provided to the corresponding <i>encode</i> method.
 * <p>
 * Be aware that the sign-preserving versions run significantly slower due to the non-native implementation of the bit-rotation.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class XoRotUtils {

    /**
     * Encodes the value potentially modifying all its bits, so the value may switch from negative to positive or vice-versa.
     * @param source to be encoded
     * @param patterns values for XOR
     * @return xorot(source)
     */
    public static final long encode(long source, long... patterns) {
        long res = source;
        for (int i = 0; i < patterns.length; i++) {
            res = res ^ patterns[i];
            res = BitUtils.rotateBitsLeft(res, BitUtils.bitCount(res));
        }
        return res;
    }

    /**
     * Decodes the value potentially modifying all its bits, so the value may switch from negative to positive or vice-versa.
     * @param source to be encoded
     * @param patterns values for XOR
     * @return xorot(source)
     */
    public static final long decode(long source, long... patterns) {
        long res = source;
        for (int i = patterns.length - 1; i >= 0; i--) {
            res = BitUtils.rotateBitsRight(res, BitUtils.bitCount(res));
            res = res ^ patterns[i];
        }
        return res;
    }

    /**
     * Encodes the value potentially, leaving the sign-bit unchanged (the outer left bit of each pattern will be ignored).<br/>
     * Consequently, values won't switch from negative to positive or vice-versa.
     * @param source to be encoded
     * @param patterns values for XOR
     * @return xorot(source)
     */
    public static final long encodePreserveSign(long source, long... patterns) {
        long res = source;
        for (int i = 0; i < patterns.length; i++) {
            res = res ^ (patterns[i] & CommonBitMasks.UNSET_FIRST_BIT_MASK_INT);
            res = BitUtils.rotateBitsLeftPreserveSign(res, BitUtils.bitCount(res));
        }
        return res;
    }

    /**
     * Decodes the value potentially, leaving the sign-bit unchanged (the outer left bit of each pattern will be ignored).<br/>
     * Consequently, values won't switch from negative to positive or vice-versa.
     * @param source to be encoded
     * @param patterns values for XOR
     * @return xorot(source)
     */
    public static final long decodePreserveSign(long source, long... patterns) {
        long res = source;
        for (int i = patterns.length - 1; i >= 0; i--) {
            res = BitUtils.rotateBitsRightPreserveSign(res, BitUtils.bitCount(res));
            res = res ^ (patterns[i] & CommonBitMasks.UNSET_FIRST_BIT_MASK_INT);
        }
        return res;
    }

    /**
     * Encodes the value potentially modifying all its bits, so the value may switch from negative to positive or vice-versa.
     * @param source to be encoded
     * @param patterns values for XOR
     * @return xorot(source)
     */
    public static final int encode(int source, int... patterns) {
        int res = source;
        for (int i = 0; i < patterns.length; i++) {
            res = res ^ patterns[i];
            res = BitUtils.rotateBitsLeft(res, BitUtils.bitCount(res));
        }
        return res;
    }

    /**
     * Decodes the value potentially modifying all its bits, so the value may switch from negative to positive or vice-versa.
     * @param source to be encoded
     * @param patterns values for XOR
     * @return xorot(source)
     */
    public static final int decode(int source, int... patterns) {
        int res = source;
        for (int i = patterns.length - 1; i >= 0; i--) {
            res = BitUtils.rotateBitsRight(res, BitUtils.bitCount(res));
            res = res ^ patterns[i];
        }
        return res;
    }

    /**
     * Encodes the value potentially, leaving the sign-bit unchanged (the outer left bit of each pattern will be ignored).<br/>
     * Consequently, values won't switch from negative to positive or vice-versa.
     * @param source to be encoded
     * @param patterns values for XOR
     * @return xorot(source)
     */
    public static final int encodePreserveSign(int source, int... patterns) {
        int res = source;
        for (int i = 0; i < patterns.length; i++) {
            res = res ^ (patterns[i] & CommonBitMasks.UNSET_FIRST_BIT_MASK_INT);
            res = BitUtils.rotateBitsLeftPreserveSign(res, BitUtils.bitCount(res));
        }
        return res;
    }

    /**
     * Decodes the value potentially, leaving the sign-bit unchanged (the outer left bit of each pattern will be ignored).<br/>
     * Consequently, values won't switch from negative to positive or vice-versa.
     * @param source to be encoded
     * @param patterns values for XOR
     * @return xorot(source)
     */
    public static final int decodePreserveSign(int source, int... patterns) {
        int res = source;
        for (int i = patterns.length - 1; i >= 0; i--) {
            res = BitUtils.rotateBitsRightPreserveSign(res, BitUtils.bitCount(res));
            res = res ^ (patterns[i] & CommonBitMasks.UNSET_FIRST_BIT_MASK_INT);
        }
        return res;
    }

    /**
     * Encodes the value potentially modifying all its bits, so the value may switch from negative to positive or vice-versa.
     * @param source to be encoded
     * @param patterns values for XOR
     * @return xorot(source)
     */
    public static final short encode(short source, short... patterns) {
        short res = source;
        for (int i = 0; i < patterns.length; i++) {
            res = (short) (res ^ patterns[i]);
            res = BitUtils.rotateBitsLeft(res, BitUtils.bitCount(res));
        }
        return res;
    }

    /**
     * Decodes the value potentially modifying all its bits, so the value may switch from negative to positive or vice-versa.
     * @param source to be encoded
     * @param patterns values for XOR
     * @return xorot(source)
     */
    public static final short decode(short source, short... patterns) {
        short res = source;
        for (int i = patterns.length - 1; i >= 0; i--) {
            res = BitUtils.rotateBitsRight(res, BitUtils.bitCount(res));
            res = (short) (res ^ patterns[i]);
        }
        return res;
    }

    /**
     * Encodes the value potentially, leaving the sign-bit unchanged (the outer left bit of each pattern will be ignored).<br/>
     * Consequently, values won't switch from negative to positive or vice-versa.
     * @param source to be encoded
     * @param patterns values for XOR
     * @return xorot(source)
     */
    public static final short encodePreserveSign(short source, short... patterns) {
        short res = source;
        for (int i = 0; i < patterns.length; i++) {
            res = (short) (res ^ (patterns[i] & CommonBitMasks.UNSET_FIRST_BIT_MASK_SHORT));
            res = BitUtils.rotateBitsLeftPreserveSign(res, BitUtils.bitCount(res));
        }
        return res;
    }

    /**
     * Decodes the value potentially, leaving the sign-bit unchanged (the outer left bit of each pattern will be ignored).<br/>
     * Consequently, values won't switch from negative to positive or vice-versa.
     * @param source to be encoded
     * @param patterns values for XOR
     * @return xorot(source)
     */
    public static final short decodePreserveSign(short source, short... patterns) {
        short res = source;
        for (int i = patterns.length - 1; i >= 0; i--) {
            res = BitUtils.rotateBitsRightPreserveSign(res, BitUtils.bitCount(res));
            res = (short) (res ^ (patterns[i] & CommonBitMasks.UNSET_FIRST_BIT_MASK_SHORT));
        }
        return res;
    }

    /**
     * Encodes the value potentially modifying all its bits, so the value may switch from negative to positive or vice-versa.
     * @param source to be encoded
     * @param patterns values for XOR
     * @return xorot(source)
     */
    public static final byte encode(byte source, byte... patterns) {
        byte res = source;
        for (int i = 0; i < patterns.length; i++) {
            res = (byte) (res ^ patterns[i]);
            res = BitUtils.rotateBitsLeft(res, BitUtils.bitCount(res));
        }
        return res;
    }

    /**
     * Decodes the value potentially modifying all its bits, so the value may switch from negative to positive or vice-versa.
     * @param source to be encoded
     * @param patterns values for XOR
     * @return xorot(source)
     */
    public static final byte decode(byte source, byte... patterns) {
        byte res = source;
        for (int i = patterns.length - 1; i >= 0; i--) {
            res = BitUtils.rotateBitsRight(res, BitUtils.bitCount(res));
            res = (byte) (res ^ patterns[i]);
        }
        return res;
    }

    /**
     * Encodes the value potentially, leaving the sign-bit unchanged (the outer left bit of each pattern will be ignored).<br/>
     * Consequently, values won't switch from negative to positive or vice-versa.
     * @param source to be encoded
     * @param patterns values for XOR
     * @return xorot(source)
     */
    public static final byte encodePreserveSign(byte source, byte... patterns) {
        byte res = source;
        for (int i = 0; i < patterns.length; i++) {
            res = (byte) (res ^ (patterns[i] & CommonBitMasks.UNSET_FIRST_BIT_MASK_BYTE));
            res = BitUtils.rotateBitsLeftPreserveSign(res, BitUtils.bitCount(res));
        }
        return res;
    }

    /**
     * Decodes the value potentially, leaving the sign-bit unchanged (the outer left bit of each pattern will be ignored).<br/>
     * Consequently, values won't switch from negative to positive or vice-versa.
     * @param source to be encoded
     * @param patterns values for XOR
     * @return xorot(source)
     */
    public static final byte decodePreserveSign(byte source, byte... patterns) {
        byte res = source;
        for (int i = patterns.length - 1; i >= 0; i--) {
            res = BitUtils.rotateBitsRightPreserveSign(res, BitUtils.bitCount(res));
            res = (byte) (res ^ (patterns[i] & CommonBitMasks.UNSET_FIRST_BIT_MASK_BYTE));
        }
        return res;
    }

    private XoRotUtils() {
        // no instances
    }

}
