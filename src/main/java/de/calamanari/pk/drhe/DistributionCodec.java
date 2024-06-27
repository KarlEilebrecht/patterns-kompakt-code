//@formatter:off
/*
 * DistributionCodec
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
package de.calamanari.pk.drhe;

import de.calamanari.pk.drhe.util.BitUtils;
import de.calamanari.pk.drhe.util.PrimePatterns;
import de.calamanari.pk.drhe.util.XoRotUtils;

/**
 * This class provides functions to encode and decode values with the purpose of distributing the values within the full range of the related data type.
 * <p/>
 * To achieve an even distribution while the mapping does not follow any simple pattern we leverage a combination of XOR operations with selected binary
 * {@link PrimePatterns} and rotations.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class DistributionCodec {

    /**
     * Patterns for encoding/decoding byte values, using the full range.
     */
    private static final byte[] XOROT_BYTE_PATTERNS = new byte[] { PrimePatterns.P_BYTE_03_S0_A, PrimePatterns.P_BYTE_05_S1_FX, PrimePatterns.P_BYTE_03_S0_D,
            PrimePatterns.P_BYTE_05_S1_I };

    /**
     * Patterns for encoding/decoding byte values, only treating the trailing 7 bits to keep the sign-bit unchanged.
     */
    private static final byte[] XOROT_PRESERVE_SIGN_BYTE_PATTERNS = new byte[] { PrimePatterns.P_BYTE_03_S0_BX, PrimePatterns.P_BYTE_05_S0_F,
            PrimePatterns.P_BYTE_03_S0_E, PrimePatterns.P_BYTE_05_S0_E };

    /**
     * Patterns for encoding/decoding short values, using the full range.
     */
    private static final short[] XOROT_SHORT_PATTERNS = new short[] { PrimePatterns.P_SHORT_05_S0_A, PrimePatterns.P_SHORT_11_S1_FX,
            PrimePatterns.P_SHORT_07_S0_BX, PrimePatterns.P_SHORT_05_S0_F, PrimePatterns.P_SHORT_11_S1_GX };

    /**
     * Patterns for encoding/decoding short values, only treating the trailing 15 bits to keep the sign-bit unchanged.
     */
    private static final short[] XOROT_PRESERVE_SIGN_SHORT_PATTERNS = new short[] { PrimePatterns.P_SHORT_05_S0_BX, PrimePatterns.P_SHORT_11_S0_A,
            PrimePatterns.P_SHORT_07_S0_CX, PrimePatterns.P_SHORT_05_S0_C, PrimePatterns.P_SHORT_11_S0_I };

    /**
     * Patterns for encoding/decoding int values, using the full range.
     */
    private static final int[] XOROT_INT_PATTERNS = new int[] { PrimePatterns.P_INT_13_S0_A, PrimePatterns.P_INT_19_S1_EX, PrimePatterns.P_INT_17_S0_A,
            PrimePatterns.P_INT_13_S1_FX, PrimePatterns.P_INT_19_S0_G, PrimePatterns.P_INT_17_S1_I, PrimePatterns.P_INT_17_S0_E, PrimePatterns.P_INT_16_S0_B,
            PrimePatterns.P_INT_16_S1_DX };

    /**
     * Patterns for encoding/decoding int values, only treating the trailing 31 bits to keep the sign-bit unchanged.
     */
    private static final int[] XOROT_PRESERVE_SIGN_INT_PATTERNS = new int[] { PrimePatterns.P_INT_13_S0_A, PrimePatterns.P_INT_19_S0_AX,
            PrimePatterns.P_INT_17_S0_C, PrimePatterns.P_INT_13_S0_B, PrimePatterns.P_INT_13_S0_F, PrimePatterns.P_INT_19_S0_BX, PrimePatterns.P_INT_17_S0_BX,
            PrimePatterns.P_INT_17_S0_B, PrimePatterns.P_INT_17_S0_D };

    /**
     * Patterns for encoding/decoding long values, using the full range.
     */
    private static final long[] XOROT_LONG_PATTERNS = new long[] { PrimePatterns.P_LONGM_32_S1_076X, PrimePatterns.P_LONGM_32_S0_049,
            PrimePatterns.P_LONGM_32_S1_200, PrimePatterns.P_LONGM_32_S0_031X, PrimePatterns.P_LONGM_32_S1_138, PrimePatterns.P_LONGM_32_S1_072X,
            PrimePatterns.P_LONGM_32_S0_077, PrimePatterns.P_LONGM_32_S1_050X };

    /**
     * Patterns for encoding/decoding long values, only treating the trailing 63 bits to keep the sign-bit unchanged.
     */
    private static final long[] XOROT_PRESERVE_SIGN_LONG_PATTERNS = new long[] { PrimePatterns.P_LONG_32_S0_CX, PrimePatterns.P_LONG_23_S0_F,
            PrimePatterns.P_LONG_31_S0_E, PrimePatterns.P_LONG_41_S0_AX, PrimePatterns.P_LONG_23_S0_F, PrimePatterns.P_LONG_32_S0_A };

    /**
     * Encodes a given byte into any other byte of the full range.
     * <ul>
     * <li>Positive values may result in negative values and vice-versa.</li>
     * <li>There are no self-mappings. The returned value never equals the input value.</li>
     * <li>Depending on the input value repeated application sooner or later produces the original input value, so the process goes into a cycle. The longest
     * possible cycle is 120, the shortest is 2.</li>
     * </ul>
     * 
     * @param b input
     * @return encoded value
     */
    public static final byte encode(byte b) {
        return XoRotUtils.encode(b, XOROT_BYTE_PATTERNS);
    }

    /**
     * Reverse operation of {@link #encode(byte)}
     * 
     * @param b input
     * @return decoded value
     */
    public static final byte decode(byte b) {
        return XoRotUtils.decode(b, XOROT_BYTE_PATTERNS);
    }

    /**
     * Encodes a given byte into any other byte preserving the sign.
     * <ul>
     * <li>Positive values (incl. 0) lead to positive result values while negative input values cause negative result values.</li>
     * <li>There are no self-mappings. The returned value never equals the input value.</li>
     * <li>Depending on the input value repeated application sooner or later produces the original input value, so the process goes into a cycle. The longest
     * possible cycle is 62, the shortest is 2.</li>
     * </ul>
     * 
     * @param b input
     * @return encoded value
     */
    public static final byte encodePreserveSign(byte b) {
        return XoRotUtils.encodePreserveSign(b, XOROT_PRESERVE_SIGN_BYTE_PATTERNS);
    }

    /**
     * Reverse operation of {@link #encodePreserveSign(byte)}
     * 
     * @param b input
     * @return decoded value
     */
    public static final byte decodePreserveSign(byte b) {
        return XoRotUtils.decodePreserveSign(b, XOROT_PRESERVE_SIGN_BYTE_PATTERNS);
    }

    /**
     * Encodes a given short into any other short of the full range.
     * <ul>
     * <li>Positive values may result in negative values and vice-versa.</li>
     * <li>There are no self-mappings. The returned value never equals the input value.</li>
     * <li>Depending on the input value repeated application sooner or later produces the original input value, so the process goes into a cycle. The longest
     * possible cycle is 25364, the shortest is 4.</li>
     * </ul>
     * 
     * @param s input
     * @return encoded value
     */
    public static final short encode(short s) {
        return XoRotUtils.encode(s, XOROT_SHORT_PATTERNS);
    }

    /**
     * Reverse operation of {@link #encode(short)}
     * 
     * @param s input
     * @return decoded value
     */
    public static final short decode(short s) {
        return XoRotUtils.decode(s, XOROT_SHORT_PATTERNS);
    }

    /**
     * Encodes a given short into any other short preserving the sign.
     * <ul>
     * <li>Positive values (incl. 0) lead to positive result values while negative input values cause negative result values.</li>
     * <li>There are no self-mappings. The returned value never equals the input value.</li>
     * <li>Depending on the input value repeated application sooner or later produces the original input value, so the process goes into a cycle. The longest
     * possible cycle is 26690, the shortest is 2.</li>
     * </ul>
     * 
     * @param s input
     * @return encoded value
     */
    public static final short encodePreserveSign(short s) {
        return XoRotUtils.encodePreserveSign(s, XOROT_PRESERVE_SIGN_SHORT_PATTERNS);
    }

    /**
     * Reverse operation of {@link #encodePreserveSign(short)}
     * 
     * @param s input
     * @return decoded value
     */
    public static final short decodePreserveSign(short s) {
        return XoRotUtils.decodePreserveSign(s, XOROT_PRESERVE_SIGN_SHORT_PATTERNS);
    }

    /**
     * Encodes a given int into any other int of the full range.
     * <ul>
     * <li>Positive values may result in negative values and vice-versa.</li>
     * <li>There are no self-mappings. The returned value never equals the input value.</li>
     * <li>Depending on the input value repeated application sooner or later produces the original input value, so the process goes into a cycle.</li>
     * </ul>
     * 
     * @param i input
     * @return encoded value
     */
    public static final int encode(int i) {
        return XoRotUtils.encode(i, XOROT_INT_PATTERNS);
    }

    /**
     * Reverse operation of {@link #encode(int)}
     * 
     * @param i input
     * @return decoded value
     */
    public static final int decode(int i) {
        return XoRotUtils.decode(i, XOROT_INT_PATTERNS);
    }

    /**
     * Encodes a given int into any other int preserving the sign.
     * <ul>
     * <li>Positive values (incl. 0) lead to positive result values while negative input values cause negative result values.</li>
     * <li>There are no self-mappings. The returned value never equals the input value.</li>
     * <li>Depending on the input value repeated application sooner or later produces the original input value, so the process goes into a cycle.</li>
     * </ul>
     * 
     * @param i input
     * @return encoded value
     */
    public static final int encodePreserveSign(int i) {
        return XoRotUtils.encodePreserveSign(i, XOROT_PRESERVE_SIGN_INT_PATTERNS);
    }

    /**
     * Reverse operation of {@link #encodePreserveSign(int)}
     * 
     * @param i input
     * @return decoded value
     */
    public static final int decodePreserveSign(int i) {
        return XoRotUtils.decodePreserveSign(i, XOROT_PRESERVE_SIGN_INT_PATTERNS);
    }

    /**
     * Encodes a given long into any other long of the full range.
     * <ul>
     * <li>Positive values may result in negative values and vice-versa.</li>
     * <li>It is unknown whether there are any self-mappings. So there might exist input values which will be mapped to themselves.</li>
     * <li>Depending on the input value repeated application sooner or later may produce the original input value, so the process may go into a cycle.</li>
     * </ul>
     * 
     * @param l input
     * @return encoded value
     */
    public static final long encode(long l) {
        return XoRotUtils.encode(l, XOROT_LONG_PATTERNS);
    }

    /**
     * Reverse operation of {@link #encode(long)}
     * 
     * @param l input
     * @return decoded value
     */
    public static final long decode(long l) {
        return XoRotUtils.decode(l, XOROT_LONG_PATTERNS);
    }

    /**
     * Encodes a given long into any other long preserving the sign.
     * <ul>
     * <li>Positive values (incl. 0) lead to positive result values while negative input values cause negative result values.</li>
     * <li>It is unknown whether there are any self-mappings. So there might exist input values which will be mapped to themselves.</li>
     * <li>Depending on the input value repeated application sooner or later may produce the original input value, so the process may go into a cycle.</li>
     * </ul>
     * 
     * @param l input
     * @return encoded value
     */
    public static final long encodePreserveSign(long l) {

        // The original straight-forward implementation (apply series of long patterns) did not lead to acceptable results.
        // Thus, this implementation recursively applies encoding.
        // While this ensures a decent distribution it unfortunately makes the method very slow.

        l = XoRotUtils.encodePreserveSign(l, XOROT_PRESERVE_SIGN_LONG_PATTERNS);

        int i0 = encodePreserveSign(BitUtils.getIntAt(l, 0));
        int i1 = encode(BitUtils.getIntAt(l, 1));
        l = BitUtils.composeLong(i0, i1);
        l = XoRotUtils.encodePreserveSign(l, XOROT_PRESERVE_SIGN_LONG_PATTERNS);

        return l;
    }

    /**
     * Reverse operation of {@link #encodePreserveSign(long)}
     * 
     * @param i input
     * @return decoded value
     */
    public static final long decodePreserveSign(long l) {

        l = XoRotUtils.decodePreserveSign(l, XOROT_PRESERVE_SIGN_LONG_PATTERNS);

        int i0 = decodePreserveSign(BitUtils.getIntAt(l, 0));
        int i1 = decode(BitUtils.getIntAt(l, 1));
        l = BitUtils.composeLong(i0, i1);
        l = XoRotUtils.decodePreserveSign(l, XOROT_PRESERVE_SIGN_LONG_PATTERNS);
        return l;
    }

    private DistributionCodec() {
        // no instances
    }

}