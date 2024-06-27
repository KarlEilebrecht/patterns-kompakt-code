//@formatter:off
/*
 * PrimePatterns
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
 * This is a collection of <i>irregular</i> bit-patterns for 8-, 16-, 32- and 64-bit numbers based on prime numbers.
 * <p/>
 * In each of the pattern the bit-sequence from the first to the last <b><code>1</code></b> is the binary representation of an unsigned prime number.
 * <p/>
 * <b>Naming convention:</b>
 * <ul>
 * <li>A constant name starts with the type prefix (byte, short, int or long) which stands for the number of bits (8, 16, 32, 64).</li>
 * <li>After the prefix, you see the number of <b><code>1</code></b>-bits in the sequence.</li>
 * <li>The first-bit-indicator tells if the bit-sequence starts with a <b><code>1</code></b> or a <b><code>0</code></b> (S1 vs. S0).</li>
 * <li>The suffix letters A, B, C, ... or a number don't have any special meaning.</li>
 * <li>If the name ends with an 'X', the bit sequence itself is not a prime number but the left-shifted <i>even</i> version of the same sequence without the 'X'
 * in the name. This allows the last bit of the sequence to be <b><code>0</code></b>.</li>
 * </ul>
 * <p/>
 * <b>Reasoning:</b><br/>
 * It is difficult to define (and generate) truly <i>irregular</i> patterns. Thus, we use prime numbers because we know that prime numbers do not follow any
 * particular scheme. Consequently, we can be sure there are no hidden recurring patterns in the bit-sequences representing prime numbers.
 * <p/>
 * However, the distance between prime numbers varies which can lead to unwanted similarities between patterns. Thus, for each pattern there is a documented
 * list of other patterns with a bit-flip-distance of 1/2 x bit-count, means half of all the bits must be flipped to turn one pattern into one of the others.
 * <p>
 * Additionally, there is a special list of constants prefixed with <code>P_LONGM_32</code>. These values are selected groups of 32-bit prime patterns, where
 * all patterns in the same group have a bit-flip distance of 32 <b>among each other</b>. <br/>
 * This may help finding pairs of <i>non-similar</i> patterns.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class PrimePatterns {

    // Byte patterns with 3 bits set

    /**
     * <code>00010011</code>
     * <p/>
     * Other patterns with half bit-flip-distance (4):
     * <ul>
     * <li>{@link #P_BYTE_03_S0_AX} (<code>00100110</code>)</li>
     * <li>{@link #P_BYTE_03_S0_B} (<code>00100101</code>)</li>
     * <li>{@link #P_BYTE_03_S0_BX} (<code>01001010</code>)</li>
     * <li>{@link #P_BYTE_03_S0_C} (<code>00101001</code>)</li>
     * <li>{@link #P_BYTE_03_S0_D} (<code>01001001</code>)</li>
     * <li>{@link #P_BYTE_03_S0_E} (<code>10001001</code>)</li>
     * <li>{@link #P_BYTE_05_S0_E} (<code>01101011</code>)</li>
     * <li>{@link #P_BYTE_05_S1_EX} (<code>11010110</code>)</li>
     * <li>{@link #P_BYTE_05_S1_FX} (<code>11011010</code>)</li>
     * <li>{@link #P_BYTE_05_S1_H} (<code>10110101</code>)</li>
     * </ul>
     */
    public static final byte P_BYTE_03_S0_A = (byte) 19;

    /**
     * <code>00100110</code>
     * <p/>
     * Other patterns with half bit-flip-distance (4):
     * <ul>
     * <li>{@link #P_BYTE_03_S0_A} (<code>00010011</code>)</li>
     * <li>{@link #P_BYTE_03_S0_BX} (<code>01001010</code>)</li>
     * <li>{@link #P_BYTE_03_S0_C} (<code>00101001</code>)</li>
     * <li>{@link #P_BYTE_03_S0_CX} (<code>01010010</code>)</li>
     * <li>{@link #P_BYTE_03_S1_DX} (<code>10010010</code>)</li>
     * <li>{@link #P_BYTE_05_S0_E} (<code>01101011</code>)</li>
     * <li>{@link #P_BYTE_05_S0_F} (<code>01101101</code>)</li>
     * <li>{@link #P_BYTE_05_S1_EX} (<code>11010110</code>)</li>
     * <li>{@link #P_BYTE_05_S1_F} (<code>10101101</code>)</li>
     * <li>{@link #P_BYTE_05_S1_G} (<code>10110011</code>)</li>
     * <li>{@link #P_BYTE_05_S1_H} (<code>10110101</code>)</li>
     * </ul>
     */
    public static final byte P_BYTE_03_S0_AX = (byte) 38;

    /**
     * <code>00100101</code>
     * <p/>
     * Other patterns with half bit-flip-distance (4):
     * <ul>
     * <li>{@link #P_BYTE_03_S0_A} (<code>00010011</code>)</li>
     * <li>{@link #P_BYTE_03_S0_D} (<code>01001001</code>)</li>
     * <li>{@link #P_BYTE_03_S0_E} (<code>10001001</code>)</li>
     * <li>{@link #P_BYTE_05_S0_E} (<code>01101011</code>)</li>
     * <li>{@link #P_BYTE_05_S1_G} (<code>10110011</code>)</li>
     * </ul>
     */
    public static final byte P_BYTE_03_S0_B = (byte) 37;

    /**
     * <code>01001010</code>
     * <p/>
     * Other patterns with half bit-flip-distance (4):
     * <ul>
     * <li>{@link #P_BYTE_03_S0_A} (<code>00010011</code>)</li>
     * <li>{@link #P_BYTE_03_S0_AX} (<code>00100110</code>)</li>
     * <li>{@link #P_BYTE_03_S0_C} (<code>00101001</code>)</li>
     * <li>{@link #P_BYTE_03_S0_E} (<code>10001001</code>)</li>
     * <li>{@link #P_BYTE_03_S1_DX} (<code>10010010</code>)</li>
     * <li>{@link #P_BYTE_05_S0_F} (<code>01101101</code>)</li>
     * <li>{@link #P_BYTE_05_S1_EX} (<code>11010110</code>)</li>
     * <li>{@link #P_BYTE_05_S1_I} (<code>11010011</code>)</li>
     * </ul>
     */
    public static final byte P_BYTE_03_S0_BX = (byte) 74;

    /**
     * <code>00101001</code>
     * <p/>
     * Other patterns with half bit-flip-distance (4):
     * <ul>
     * <li>{@link #P_BYTE_03_S0_A} (<code>00010011</code>)</li>
     * <li>{@link #P_BYTE_03_S0_AX} (<code>00100110</code>)</li>
     * <li>{@link #P_BYTE_03_S0_BX} (<code>01001010</code>)</li>
     * <li>{@link #P_BYTE_05_S1_G} (<code>10110011</code>)</li>
     * <li>{@link #P_BYTE_05_S1_H} (<code>10110101</code>)</li>
     * </ul>
     */
    public static final byte P_BYTE_03_S0_C = (byte) 41;

    /**
     * <code>01010010</code>
     * <p/>
     * Other patterns with half bit-flip-distance (4):
     * <ul>
     * <li>{@link #P_BYTE_03_S0_AX} (<code>00100110</code>)</li>
     * <li>{@link #P_BYTE_03_S0_D} (<code>01001001</code>)</li>
     * <li>{@link #P_BYTE_05_S0_E} (<code>01101011</code>)</li>
     * <li>{@link #P_BYTE_05_S1_G} (<code>10110011</code>)</li>
     * </ul>
     */
    public static final byte P_BYTE_03_S0_CX = (byte) 82;

    /**
     * <code>01001001</code>
     * <p/>
     * Other patterns with half bit-flip-distance (4):
     * <ul>
     * <li>{@link #P_BYTE_03_S0_A} (<code>00010011</code>)</li>
     * <li>{@link #P_BYTE_03_S0_B} (<code>00100101</code>)</li>
     * <li>{@link #P_BYTE_03_S0_CX} (<code>01010010</code>)</li>
     * <li>{@link #P_BYTE_05_S1_F} (<code>10101101</code>)</li>
     * <li>{@link #P_BYTE_05_S1_FX} (<code>11011010</code>)</li>
     * <li>{@link #P_BYTE_05_S1_I} (<code>11010011</code>)</li>
     * </ul>
     */
    public static final byte P_BYTE_03_S0_D = (byte) 73;

    /**
     * <code>10010010</code>
     * <p/>
     * Other patterns with half bit-flip-distance (4):
     * <ul>
     * <li>{@link #P_BYTE_03_S0_AX} (<code>00100110</code>)</li>
     * <li>{@link #P_BYTE_03_S0_BX} (<code>01001010</code>)</li>
     * <li>{@link #P_BYTE_03_S0_E} (<code>10001001</code>)</li>
     * <li>{@link #P_BYTE_05_S1_H} (<code>10110101</code>)</li>
     * </ul>
     */
    public static final byte P_BYTE_03_S1_DX = (byte) -110;

    /**
     * <code>10001001</code>
     * <p/>
     * Other patterns with half bit-flip-distance (4):
     * <ul>
     * <li>{@link #P_BYTE_03_S0_A} (<code>00010011</code>)</li>
     * <li>{@link #P_BYTE_03_S0_B} (<code>00100101</code>)</li>
     * <li>{@link #P_BYTE_03_S0_BX} (<code>01001010</code>)</li>
     * <li>{@link #P_BYTE_03_S1_DX} (<code>10010010</code>)</li>
     * <li>{@link #P_BYTE_05_S0_E} (<code>01101011</code>)</li>
     * <li>{@link #P_BYTE_05_S0_F} (<code>01101101</code>)</li>
     * <li>{@link #P_BYTE_05_S1_FX} (<code>11011010</code>)</li>
     * <li>{@link #P_BYTE_05_S1_G} (<code>10110011</code>)</li>
     * <li>{@link #P_BYTE_05_S1_H} (<code>10110101</code>)</li>
     * <li>{@link #P_BYTE_05_S1_I} (<code>11010011</code>)</li>
     * </ul>
     */
    public static final byte P_BYTE_03_S0_E = (byte) -119;

    // Byte patterns with 4 bits set

    /**
     * <code>00010111</code>
     * <p/>
     * Other patterns with half bit-flip-distance (4):
     * <ul>
     * <li>{@link #P_BYTE_04_S0_AX} (<code>00101110</code>)</li>
     * <li>{@link #P_BYTE_04_S0_B} (<code>00101011</code>)</li>
     * <li>{@link #P_BYTE_04_S0_D} (<code>01100101</code>)</li>
     * <li>{@link #P_BYTE_04_S1_CX} (<code>10001110</code>)</li>
     * <li>{@link #P_BYTE_04_S1_E} (<code>10001011</code>)</li>
     * <li>{@link #P_BYTE_04_S1_F} (<code>11000101</code>)</li>
     * </ul>
     */
    public static final byte P_BYTE_04_S0_A = (byte) 23;

    /**
     * <code>00101110</code>
     * <p/>
     * Other patterns with half bit-flip-distance (4):
     * <ul>
     * <li>{@link #P_BYTE_04_S0_A} (<code>00010111</code>)</li>
     * <li>{@link #P_BYTE_04_S0_BX} (<code>01010110</code>)</li>
     * <li>{@link #P_BYTE_04_S0_C} (<code>01000111</code>)</li>
     * <li>{@link #P_BYTE_04_S0_D} (<code>01100101</code>)</li>
     * <li>{@link #P_BYTE_04_S1_DX} (<code>11001010</code>)</li>
     * <li>{@link #P_BYTE_04_S1_E} (<code>10001011</code>)</li>
     * </ul>
     */
    public static final byte P_BYTE_04_S0_AX = (byte) 46;

    /**
     * <code>00101011</code>
     * <p/>
     * Other patterns with half bit-flip-distance (4):
     * <ul>
     * <li>{@link #P_BYTE_04_S0_A} (<code>00010111</code>)</li>
     * <li>{@link #P_BYTE_04_S0_C} (<code>01000111</code>)</li>
     * <li>{@link #P_BYTE_04_S0_D} (<code>01100101</code>)</li>
     * <li>{@link #P_BYTE_04_S1_CX} (<code>10001110</code>)</li>
     * <li>{@link #P_BYTE_04_S1_DX} (<code>11001010</code>)</li>
     * </ul>
     */
    public static final byte P_BYTE_04_S0_B = (byte) 43;

    /**
     * <code>01010110</code>
     * <p/>
     * Other patterns with half bit-flip-distance (4):
     * <ul>
     * <li>{@link #P_BYTE_04_S0_AX} (<code>00101110</code>)</li>
     * <li>{@link #P_BYTE_04_S0_D} (<code>01100101</code>)</li>
     * <li>{@link #P_BYTE_04_S1_CX} (<code>10001110</code>)</li>
     * <li>{@link #P_BYTE_04_S1_DX} (<code>11001010</code>)</li>
     * <li>{@link #P_BYTE_04_S1_F} (<code>11000101</code>)</li>
     * </ul>
     */
    public static final byte P_BYTE_04_S0_BX = (byte) 86;

    /**
     * <code>01000111</code>
     * <p/>
     * Other patterns with half bit-flip-distance (4):
     * <ul>
     * <li>{@link #P_BYTE_04_S0_AX} (<code>00101110</code>)</li>
     * <li>{@link #P_BYTE_04_S0_B} (<code>00101011</code>)</li>
     * <li>{@link #P_BYTE_04_S1_CX} (<code>10001110</code>)</li>
     * <li>{@link #P_BYTE_04_S1_DX} (<code>11001010</code>)</li>
     * <li>{@link #P_BYTE_04_S1_E} (<code>10001011</code>)</li>
     * </ul>
     */
    public static final byte P_BYTE_04_S0_C = (byte) 71;

    /**
     * <code>10001110</code>
     * <p/>
     * Other patterns with half bit-flip-distance (4):
     * <ul>
     * <li>{@link #P_BYTE_04_S0_A} (<code>00010111</code>)</li>
     * <li>{@link #P_BYTE_04_S0_B} (<code>00101011</code>)</li>
     * <li>{@link #P_BYTE_04_S0_BX} (<code>01010110</code>)</li>
     * <li>{@link #P_BYTE_04_S0_C} (<code>01000111</code>)</li>
     * <li>{@link #P_BYTE_04_S1_F} (<code>11000101</code>)</li>
     * </ul>
     */
    public static final byte P_BYTE_04_S1_CX = (byte) -114;

    /**
     * <code>01100101</code>
     * <p/>
     * Other patterns with half bit-flip-distance (4):
     * <ul>
     * <li>{@link #P_BYTE_04_S0_A} (<code>00010111</code>)</li>
     * <li>{@link #P_BYTE_04_S0_AX} (<code>00101110</code>)</li>
     * <li>{@link #P_BYTE_04_S0_B} (<code>00101011</code>)</li>
     * <li>{@link #P_BYTE_04_S0_BX} (<code>01010110</code>)</li>
     * </ul>
     */
    public static final byte P_BYTE_04_S0_D = (byte) 101;

    /**
     * <code>11001010</code>
     * <p/>
     * Other patterns with half bit-flip-distance (4):
     * <ul>
     * <li>{@link #P_BYTE_04_S0_AX} (<code>00101110</code>)</li>
     * <li>{@link #P_BYTE_04_S0_B} (<code>00101011</code>)</li>
     * <li>{@link #P_BYTE_04_S0_BX} (<code>01010110</code>)</li>
     * <li>{@link #P_BYTE_04_S0_C} (<code>01000111</code>)</li>
     * <li>{@link #P_BYTE_04_S1_F} (<code>11000101</code>)</li>
     * </ul>
     */
    public static final byte P_BYTE_04_S1_DX = (byte) -54;

    /**
     * <code>10001011</code>
     * <p/>
     * Other patterns with half bit-flip-distance (4):
     * <ul>
     * <li>{@link #P_BYTE_04_S0_A} (<code>00010111</code>)</li>
     * <li>{@link #P_BYTE_04_S0_AX} (<code>00101110</code>)</li>
     * <li>{@link #P_BYTE_04_S0_C} (<code>01000111</code>)</li>
     * <li>{@link #P_BYTE_04_S1_F} (<code>11000101</code>)</li>
     * </ul>
     */
    public static final byte P_BYTE_04_S1_E = (byte) -117;

    /**
     * <code>11000101</code>
     * <p/>
     * Other patterns with half bit-flip-distance (4):
     * <ul>
     * <li>{@link #P_BYTE_04_S0_A} (<code>00010111</code>)</li>
     * <li>{@link #P_BYTE_04_S0_BX} (<code>01010110</code>)</li>
     * <li>{@link #P_BYTE_04_S1_CX} (<code>10001110</code>)</li>
     * <li>{@link #P_BYTE_04_S1_DX} (<code>11001010</code>)</li>
     * <li>{@link #P_BYTE_04_S1_E} (<code>10001011</code>)</li>
     * </ul>
     */
    public static final byte P_BYTE_04_S1_F = (byte) -59;

    // Byte patterns with 5 bits set

    /**
     * <code>01101011</code>
     * <p/>
     * Other patterns with half bit-flip-distance (4):
     * <ul>
     * <li>{@link #P_BYTE_03_S0_A} (<code>00010011</code>)</li>
     * <li>{@link #P_BYTE_03_S0_AX} (<code>00100110</code>)</li>
     * <li>{@link #P_BYTE_03_S0_B} (<code>00100101</code>)</li>
     * <li>{@link #P_BYTE_03_S0_CX} (<code>01010010</code>)</li>
     * <li>{@link #P_BYTE_03_S0_E} (<code>10001001</code>)</li>
     * <li>{@link #P_BYTE_05_S1_F} (<code>10101101</code>)</li>
     * <li>{@link #P_BYTE_05_S1_FX} (<code>11011010</code>)</li>
     * <li>{@link #P_BYTE_05_S1_G} (<code>10110011</code>)</li>
     * <li>{@link #P_BYTE_05_S1_I} (<code>11010011</code>)</li>
     * </ul>
     */
    public static final byte P_BYTE_05_S0_E = (byte) 107;

    /**
     * <code>11010110</code>
     * <p/>
     * Other patterns with half bit-flip-distance (4):
     * <ul>
     * <li>{@link #P_BYTE_03_S0_A} (<code>00010011</code>)</li>
     * <li>{@link #P_BYTE_03_S0_AX} (<code>00100110</code>)</li>
     * <li>{@link #P_BYTE_03_S0_BX} (<code>01001010</code>)</li>
     * <li>{@link #P_BYTE_05_S1_G} (<code>10110011</code>)</li>
     * <li>{@link #P_BYTE_05_S1_H} (<code>10110101</code>)</li>
     * </ul>
     */
    public static final byte P_BYTE_05_S1_EX = (byte) -42;

    /**
     * <code>01101101</code>
     * <p/>
     * Other patterns with half bit-flip-distance (4):
     * <ul>
     * <li>{@link #P_BYTE_03_S0_AX} (<code>00100110</code>)</li>
     * <li>{@link #P_BYTE_03_S0_BX} (<code>01001010</code>)</li>
     * <li>{@link #P_BYTE_03_S0_E} (<code>10001001</code>)</li>
     * <li>{@link #P_BYTE_05_S1_H} (<code>10110101</code>)</li>
     * </ul>
     */
    public static final byte P_BYTE_05_S0_F = (byte) 109;

    /**
     * <code>11011010</code>
     * <p/>
     * Other patterns with half bit-flip-distance (4):
     * <ul>
     * <li>{@link #P_BYTE_03_S0_A} (<code>00010011</code>)</li>
     * <li>{@link #P_BYTE_03_S0_D} (<code>01001001</code>)</li>
     * <li>{@link #P_BYTE_03_S0_E} (<code>10001001</code>)</li>
     * <li>{@link #P_BYTE_05_S0_E} (<code>01101011</code>)</li>
     * <li>{@link #P_BYTE_05_S1_G} (<code>10110011</code>)</li>
     * </ul>
     */
    public static final byte P_BYTE_05_S1_FX = (byte) -38;

    /**
     * <code>10101101</code>
     * <p/>
     * Other patterns with half bit-flip-distance (4):
     * <ul>
     * <li>{@link #P_BYTE_03_S0_AX} (<code>00100110</code>)</li>
     * <li>{@link #P_BYTE_03_S0_D} (<code>01001001</code>)</li>
     * <li>{@link #P_BYTE_05_S0_E} (<code>01101011</code>)</li>
     * <li>{@link #P_BYTE_05_S1_G} (<code>10110011</code>)</li>
     * </ul>
     */
    public static final byte P_BYTE_05_S1_F = (byte) -83;

    /**
     * <code>10110011</code>
     * <p/>
     * Other patterns with half bit-flip-distance (4):
     * <ul>
     * <li>{@link #P_BYTE_03_S0_AX} (<code>00100110</code>)</li>
     * <li>{@link #P_BYTE_03_S0_B} (<code>00100101</code>)</li>
     * <li>{@link #P_BYTE_03_S0_C} (<code>00101001</code>)</li>
     * <li>{@link #P_BYTE_03_S0_CX} (<code>01010010</code>)</li>
     * <li>{@link #P_BYTE_03_S0_E} (<code>10001001</code>)</li>
     * <li>{@link #P_BYTE_05_S0_E} (<code>01101011</code>)</li>
     * <li>{@link #P_BYTE_05_S1_EX} (<code>11010110</code>)</li>
     * <li>{@link #P_BYTE_05_S1_F} (<code>10101101</code>)</li>
     * <li>{@link #P_BYTE_05_S1_FX} (<code>11011010</code>)</li>
     * </ul>
     */
    public static final byte P_BYTE_05_S1_G = (byte) -77;

    /**
     * <code>10110101</code>
     * <p/>
     * Other patterns with half bit-flip-distance (4):
     * <ul>
     * <li>{@link #P_BYTE_03_S0_A} (<code>00010011</code>)</li>
     * <li>{@link #P_BYTE_03_S0_AX} (<code>00100110</code>)</li>
     * <li>{@link #P_BYTE_03_S0_C} (<code>00101001</code>)</li>
     * <li>{@link #P_BYTE_03_S0_E} (<code>10001001</code>)</li>
     * <li>{@link #P_BYTE_03_S1_DX} (<code>10010010</code>)</li>
     * <li>{@link #P_BYTE_05_S0_F} (<code>01101101</code>)</li>
     * <li>{@link #P_BYTE_05_S1_EX} (<code>11010110</code>)</li>
     * <li>{@link #P_BYTE_05_S1_I} (<code>11010011</code>)</li>
     * </ul>
     */
    public static final byte P_BYTE_05_S1_H = (byte) -75;

    /**
     * <code>11010011</code>
     * <p/>
     * Other patterns with half bit-flip-distance (4):
     * <ul>
     * <li>{@link #P_BYTE_03_S0_BX} (<code>01001010</code>)</li>
     * <li>{@link #P_BYTE_03_S0_D} (<code>01001001</code>)</li>
     * <li>{@link #P_BYTE_03_S0_E} (<code>10001001</code>)</li>
     * <li>{@link #P_BYTE_05_S0_E} (<code>01101011</code>)</li>
     * <li>{@link #P_BYTE_05_S1_H} (<code>10110101</code>)</li>
     * </ul>
     */
    public static final byte P_BYTE_05_S1_I = (byte) -45;

    // Short patterns with 5 bits set

    /**
     * <code>0001000100010101</code>
     * <p/>
     * Other patterns with half bit-flip-distance (8):
     * <ul>
     * <li>{@link #P_SHORT_05_S0_CX} (<code>0010010001000110</code>)</li>
     * <li>{@link #P_SHORT_05_S0_DX} (<code>0010010001010010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_JX} (<code>0100010100100010</code>)</li>
     * <li>{@link #P_SHORT_07_S0_A} (<code>0010010010011011</code>)</li>
     * <li>{@link #P_SHORT_07_S0_C} (<code>0010011001011001</code>)</li>
     * <li>{@link #P_SHORT_07_S0_E} (<code>0010110010010011</code>)</li>
     * <li>{@link #P_SHORT_07_S0_H} (<code>0101001001001011</code>)</li>
     * <li>{@link #P_SHORT_07_S1_HX} (<code>1010010010010110</code>)</li>
     * <li>{@link #P_SHORT_07_S1_J} (<code>1011001001001001</code>)</li>
     * <li>{@link #P_SHORT_11_S0_E} (<code>0110110110110111</code>)</li>
     * <li>{@link #P_SHORT_11_S0_I} (<code>0111010110111011</code>)</li>
     * <li>{@link #P_SHORT_11_S0_J} (<code>0111011010110111</code>)</li>
     * <li>{@link #P_SHORT_11_S1_FX} (<code>1101101110110110</code>)</li>
     * </ul>
     */
    public static final short P_SHORT_05_S0_A = (short) 4373;

    /**
     * <code>0010001000101010</code>
     * <p/>
     * Other patterns with half bit-flip-distance (8):
     * <ul>
     * <li>{@link #P_SHORT_07_S0_AX} (<code>0100100100110110</code>)</li>
     * <li>{@link #P_SHORT_07_S0_CX} (<code>0100110010110010</code>)</li>
     * <li>{@link #P_SHORT_07_S0_D} (<code>0010100101100101</code>)</li>
     * <li>{@link #P_SHORT_07_S0_E} (<code>0010110010010011</code>)</li>
     * <li>{@link #P_SHORT_07_S0_EX} (<code>0101100100100110</code>)</li>
     * <li>{@link #P_SHORT_07_S1_HX} (<code>1010010010010110</code>)</li>
     * <li>{@link #P_SHORT_11_S0_A} (<code>0011101110110111</code>)</li>
     * <li>{@link #P_SHORT_11_S0_G} (<code>0110111001110111</code>)</li>
     * <li>{@link #P_SHORT_11_S0_I} (<code>0111010110111011</code>)</li>
     * <li>{@link #P_SHORT_11_S0_J} (<code>0111011010110111</code>)</li>
     * <li>{@link #P_SHORT_11_S1_EX} (<code>1101101101101110</code>)</li>
     * <li>{@link #P_SHORT_11_S1_IX} (<code>1110101101110110</code>)</li>
     * <li>{@link #P_SHORT_11_S1_JX} (<code>1110110101101110</code>)</li>
     * </ul>
     */
    public static final short P_SHORT_05_S0_AX = (short) 8746;

    /**
     * <code>0001000101000101</code>
     * <p/>
     * Other patterns with half bit-flip-distance (8):
     * <ul>
     * <li>{@link #P_SHORT_05_S0_DX} (<code>0010010001010010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_EX} (<code>0010010001100010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_HX} (<code>0100010001001010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_I} (<code>0010001000110001</code>)</li>
     * <li>{@link #P_SHORT_05_S0_IX} (<code>0100010001100010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_J} (<code>0010001010010001</code>)</li>
     * <li>{@link #P_SHORT_05_S0_JX} (<code>0100010100100010</code>)</li>
     * <li>{@link #P_SHORT_07_S0_AX} (<code>0100100100110110</code>)</li>
     * <li>{@link #P_SHORT_07_S0_BX} (<code>0100101001001110</code>)</li>
     * <li>{@link #P_SHORT_07_S0_C} (<code>0010011001011001</code>)</li>
     * <li>{@link #P_SHORT_07_S0_DX} (<code>0101001011001010</code>)</li>
     * <li>{@link #P_SHORT_11_S0_A} (<code>0011101110110111</code>)</li>
     * <li>{@link #P_SHORT_11_S0_AX} (<code>0111011101101110</code>)</li>
     * <li>{@link #P_SHORT_11_S1_EX} (<code>1101101101101110</code>)</li>
     * </ul>
     */
    public static final short P_SHORT_05_S0_B = (short) 4421;

    /**
     * <code>0010001010001010</code>
     * <p/>
     * Other patterns with half bit-flip-distance (8):
     * <ul>
     * <li>{@link #P_SHORT_05_S0_E} (<code>0001001000110001</code>)</li>
     * <li>{@link #P_SHORT_05_S0_IX} (<code>0100010001100010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_JX} (<code>0100010100100010</code>)</li>
     * <li>{@link #P_SHORT_07_S0_B} (<code>0010010100100111</code>)</li>
     * <li>{@link #P_SHORT_07_S0_CX} (<code>0100110010110010</code>)</li>
     * <li>{@link #P_SHORT_07_S0_F} (<code>0011001100100101</code>)</li>
     * <li>{@link #P_SHORT_11_S0_A} (<code>0011101110110111</code>)</li>
     * <li>{@link #P_SHORT_11_S0_AX} (<code>0111011101101110</code>)</li>
     * <li>{@link #P_SHORT_11_S0_F} (<code>0110110111011011</code>)</li>
     * <li>{@link #P_SHORT_11_S0_I} (<code>0111010110111011</code>)</li>
     * <li>{@link #P_SHORT_11_S0_J} (<code>0111011010110111</code>)</li>
     * </ul>
     */
    public static final short P_SHORT_05_S0_BX = (short) 8842;

    /**
     * <code>0001001000100011</code>
     * <p/>
     * Other patterns with half bit-flip-distance (8):
     * <ul>
     * <li>{@link #P_SHORT_05_S0_CX} (<code>0010010001000110</code>)</li>
     * <li>{@link #P_SHORT_05_S0_DX} (<code>0010010001010010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_FX} (<code>0010100010001010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_HX} (<code>0100010001001010</code>)</li>
     * <li>{@link #P_SHORT_07_S0_A} (<code>0010010010011011</code>)</li>
     * <li>{@link #P_SHORT_07_S0_AX} (<code>0100100100110110</code>)</li>
     * <li>{@link #P_SHORT_07_S0_BX} (<code>0100101001001110</code>)</li>
     * <li>{@link #P_SHORT_07_S0_C} (<code>0010011001011001</code>)</li>
     * <li>{@link #P_SHORT_07_S0_CX} (<code>0100110010110010</code>)</li>
     * <li>{@link #P_SHORT_07_S0_D} (<code>0010100101100101</code>)</li>
     * <li>{@link #P_SHORT_07_S0_E} (<code>0010110010010011</code>)</li>
     * <li>{@link #P_SHORT_07_S0_FX} (<code>0110011001001010</code>)</li>
     * <li>{@link #P_SHORT_11_S0_AX} (<code>0111011101101110</code>)</li>
     * <li>{@link #P_SHORT_11_S0_G} (<code>0110111001110111</code>)</li>
     * <li>{@link #P_SHORT_11_S0_H} (<code>0111010101110111</code>)</li>
     * <li>{@link #P_SHORT_11_S0_I} (<code>0111010110111011</code>)</li>
     * <li>{@link #P_SHORT_11_S1_CX} (<code>1011101110101110</code>)</li>
     * <li>{@link #P_SHORT_11_S1_DX} (<code>1011101110111010</code>)</li>
     * <li>{@link #P_SHORT_11_S1_EX} (<code>1101101101101110</code>)</li>
     * <li>{@link #P_SHORT_11_S1_FX} (<code>1101101110110110</code>)</li>
     * </ul>
     */
    public static final short P_SHORT_05_S0_C = (short) 4643;

    /**
     * <code>0010010001000110</code>
     * <p/>
     * Other patterns with half bit-flip-distance (8):
     * <ul>
     * <li>{@link #P_SHORT_05_S0_A} (<code>0001000100010101</code>)</li>
     * <li>{@link #P_SHORT_05_S0_C} (<code>0001001000100011</code>)</li>
     * <li>{@link #P_SHORT_05_S0_G} (<code>0001010100010001</code>)</li>
     * <li>{@link #P_SHORT_05_S0_I} (<code>0010001000110001</code>)</li>
     * <li>{@link #P_SHORT_05_S0_J} (<code>0010001010010001</code>)</li>
     * <li>{@link #P_SHORT_07_S0_AX} (<code>0100100100110110</code>)</li>
     * <li>{@link #P_SHORT_07_S0_CX} (<code>0100110010110010</code>)</li>
     * <li>{@link #P_SHORT_07_S0_DX} (<code>0101001011001010</code>)</li>
     * <li>{@link #P_SHORT_07_S0_EX} (<code>0101100100100110</code>)</li>
     * <li>{@link #P_SHORT_07_S0_F} (<code>0011001100100101</code>)</li>
     * <li>{@link #P_SHORT_07_S0_G} (<code>0100100101010101</code>)</li>
     * <li>{@link #P_SHORT_07_S0_H} (<code>0101001001001011</code>)</li>
     * <li>{@link #P_SHORT_07_S1_J} (<code>1011001001001001</code>)</li>
     * <li>{@link #P_SHORT_11_S0_B} (<code>0101011101110111</code>)</li>
     * <li>{@link #P_SHORT_11_S0_C} (<code>0101110111010111</code>)</li>
     * <li>{@link #P_SHORT_11_S0_E} (<code>0110110110110111</code>)</li>
     * <li>{@link #P_SHORT_11_S0_F} (<code>0110110111011011</code>)</li>
     * <li>{@link #P_SHORT_11_S0_J} (<code>0111011010110111</code>)</li>
     * <li>{@link #P_SHORT_11_S1_GX} (<code>1101110011101110</code>)</li>
     * <li>{@link #P_SHORT_11_S1_HX} (<code>1110101011101110</code>)</li>
     * <li>{@link #P_SHORT_11_S1_IX} (<code>1110101101110110</code>)</li>
     * </ul>
     */
    public static final short P_SHORT_05_S0_CX = (short) 9286;

    /**
     * <code>0001001000101001</code>
     * <p/>
     * Other patterns with half bit-flip-distance (8):
     * <ul>
     * <li>{@link #P_SHORT_05_S0_EX} (<code>0010010001100010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_FX} (<code>0010100010001010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_HX} (<code>0100010001001010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_IX} (<code>0100010001100010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_JX} (<code>0100010100100010</code>)</li>
     * <li>{@link #P_SHORT_07_S0_A} (<code>0010010010011011</code>)</li>
     * <li>{@link #P_SHORT_07_S0_B} (<code>0010010100100111</code>)</li>
     * <li>{@link #P_SHORT_07_S0_BX} (<code>0100101001001110</code>)</li>
     * <li>{@link #P_SHORT_07_S0_D} (<code>0010100101100101</code>)</li>
     * <li>{@link #P_SHORT_07_S0_EX} (<code>0101100100100110</code>)</li>
     * <li>{@link #P_SHORT_07_S0_FX} (<code>0110011001001010</code>)</li>
     * <li>{@link #P_SHORT_11_S0_A} (<code>0011101110110111</code>)</li>
     * <li>{@link #P_SHORT_11_S0_AX} (<code>0111011101101110</code>)</li>
     * <li>{@link #P_SHORT_11_S0_B} (<code>0101011101110111</code>)</li>
     * <li>{@link #P_SHORT_11_S0_I} (<code>0111010110111011</code>)</li>
     * <li>{@link #P_SHORT_11_S0_J} (<code>0111011010110111</code>)</li>
     * <li>{@link #P_SHORT_11_S1_CX} (<code>1011101110101110</code>)</li>
     * <li>{@link #P_SHORT_11_S1_DX} (<code>1011101110111010</code>)</li>
     * <li>{@link #P_SHORT_11_S1_EX} (<code>1101101101101110</code>)</li>
     * </ul>
     */
    public static final short P_SHORT_05_S0_D = (short) 4649;

    /**
     * <code>0010010001010010</code>
     * <p/>
     * Other patterns with half bit-flip-distance (8):
     * <ul>
     * <li>{@link #P_SHORT_05_S0_A} (<code>0001000100010101</code>)</li>
     * <li>{@link #P_SHORT_05_S0_B} (<code>0001000101000101</code>)</li>
     * <li>{@link #P_SHORT_05_S0_C} (<code>0001001000100011</code>)</li>
     * <li>{@link #P_SHORT_05_S0_E} (<code>0001001000110001</code>)</li>
     * <li>{@link #P_SHORT_05_S0_H} (<code>0010001000100101</code>)</li>
     * <li>{@link #P_SHORT_07_S0_AX} (<code>0100100100110110</code>)</li>
     * <li>{@link #P_SHORT_07_S0_BX} (<code>0100101001001110</code>)</li>
     * <li>{@link #P_SHORT_07_S0_D} (<code>0010100101100101</code>)</li>
     * <li>{@link #P_SHORT_07_S0_DX} (<code>0101001011001010</code>)</li>
     * <li>{@link #P_SHORT_07_S0_G} (<code>0100100101010101</code>)</li>
     * <li>{@link #P_SHORT_07_S0_H} (<code>0101001001001011</code>)</li>
     * <li>{@link #P_SHORT_07_S1_J} (<code>1011001001001001</code>)</li>
     * <li>{@link #P_SHORT_11_S0_AX} (<code>0111011101101110</code>)</li>
     * <li>{@link #P_SHORT_11_S0_B} (<code>0101011101110111</code>)</li>
     * <li>{@link #P_SHORT_11_S0_C} (<code>0101110111010111</code>)</li>
     * <li>{@link #P_SHORT_11_S0_E} (<code>0110110110110111</code>)</li>
     * <li>{@link #P_SHORT_11_S0_I} (<code>0111010110111011</code>)</li>
     * <li>{@link #P_SHORT_11_S0_J} (<code>0111011010110111</code>)</li>
     * <li>{@link #P_SHORT_11_S1_BX} (<code>1010111011101110</code>)</li>
     * <li>{@link #P_SHORT_11_S1_IX} (<code>1110101101110110</code>)</li>
     * <li>{@link #P_SHORT_11_S1_JX} (<code>1110110101101110</code>)</li>
     * </ul>
     */
    public static final short P_SHORT_05_S0_DX = (short) 9298;

    /**
     * <code>0001001000110001</code>
     * <p/>
     * Other patterns with half bit-flip-distance (8):
     * <ul>
     * <li>{@link #P_SHORT_05_S0_BX} (<code>0010001010001010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_DX} (<code>0010010001010010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_EX} (<code>0010010001100010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_IX} (<code>0100010001100010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_JX} (<code>0100010100100010</code>)</li>
     * <li>{@link #P_SHORT_07_S0_A} (<code>0010010010011011</code>)</li>
     * <li>{@link #P_SHORT_07_S0_AX} (<code>0100100100110110</code>)</li>
     * <li>{@link #P_SHORT_07_S0_B} (<code>0010010100100111</code>)</li>
     * <li>{@link #P_SHORT_07_S0_CX} (<code>0100110010110010</code>)</li>
     * <li>{@link #P_SHORT_07_S0_D} (<code>0010100101100101</code>)</li>
     * <li>{@link #P_SHORT_07_S0_DX} (<code>0101001011001010</code>)</li>
     * <li>{@link #P_SHORT_07_S0_E} (<code>0010110010010011</code>)</li>
     * <li>{@link #P_SHORT_07_S0_EX} (<code>0101100100100110</code>)</li>
     * <li>{@link #P_SHORT_07_S0_G} (<code>0100100101010101</code>)</li>
     * <li>{@link #P_SHORT_11_S0_G} (<code>0110111001110111</code>)</li>
     * <li>{@link #P_SHORT_11_S0_H} (<code>0111010101110111</code>)</li>
     * <li>{@link #P_SHORT_11_S0_I} (<code>0111010110111011</code>)</li>
     * <li>{@link #P_SHORT_11_S1_DX} (<code>1011101110111010</code>)</li>
     * <li>{@link #P_SHORT_11_S1_FX} (<code>1101101110110110</code>)</li>
     * </ul>
     */
    public static final short P_SHORT_05_S0_E = (short) 4657;

    /**
     * <code>0010010001100010</code>
     * <p/>
     * Other patterns with half bit-flip-distance (8):
     * <ul>
     * <li>{@link #P_SHORT_05_S0_B} (<code>0001000101000101</code>)</li>
     * <li>{@link #P_SHORT_05_S0_D} (<code>0001001000101001</code>)</li>
     * <li>{@link #P_SHORT_05_S0_E} (<code>0001001000110001</code>)</li>
     * <li>{@link #P_SHORT_05_S0_G} (<code>0001010100010001</code>)</li>
     * <li>{@link #P_SHORT_05_S0_J} (<code>0010001010010001</code>)</li>
     * <li>{@link #P_SHORT_07_S0_AX} (<code>0100100100110110</code>)</li>
     * <li>{@link #P_SHORT_07_S0_BX} (<code>0100101001001110</code>)</li>
     * <li>{@link #P_SHORT_07_S0_DX} (<code>0101001011001010</code>)</li>
     * <li>{@link #P_SHORT_07_S0_EX} (<code>0101100100100110</code>)</li>
     * <li>{@link #P_SHORT_07_S0_F} (<code>0011001100100101</code>)</li>
     * <li>{@link #P_SHORT_07_S0_H} (<code>0101001001001011</code>)</li>
     * <li>{@link #P_SHORT_07_S1_GX} (<code>1001001010101010</code>)</li>
     * <li>{@link #P_SHORT_07_S1_J} (<code>1011001001001001</code>)</li>
     * <li>{@link #P_SHORT_11_S0_B} (<code>0101011101110111</code>)</li>
     * <li>{@link #P_SHORT_11_S0_E} (<code>0110110110110111</code>)</li>
     * <li>{@link #P_SHORT_11_S0_F} (<code>0110110111011011</code>)</li>
     * <li>{@link #P_SHORT_11_S0_I} (<code>0111010110111011</code>)</li>
     * <li>{@link #P_SHORT_11_S0_J} (<code>0111011010110111</code>)</li>
     * <li>{@link #P_SHORT_11_S1_GX} (<code>1101110011101110</code>)</li>
     * <li>{@link #P_SHORT_11_S1_HX} (<code>1110101011101110</code>)</li>
     * <li>{@link #P_SHORT_11_S1_IX} (<code>1110101101110110</code>)</li>
     * </ul>
     */
    public static final short P_SHORT_05_S0_EX = (short) 9314;

    /**
     * <code>0001010001000101</code>
     * <p/>
     * Other patterns with half bit-flip-distance (8):
     * <ul>
     * <li>{@link #P_SHORT_05_S0_I} (<code>0010001000110001</code>)</li>
     * <li>{@link #P_SHORT_05_S0_J} (<code>0010001010010001</code>)</li>
     * <li>{@link #P_SHORT_05_S0_JX} (<code>0100010100100010</code>)</li>
     * <li>{@link #P_SHORT_07_S0_A} (<code>0010010010011011</code>)</li>
     * <li>{@link #P_SHORT_07_S0_BX} (<code>0100101001001110</code>)</li>
     * <li>{@link #P_SHORT_07_S0_DX} (<code>0101001011001010</code>)</li>
     * <li>{@link #P_SHORT_07_S0_E} (<code>0010110010010011</code>)</li>
     * <li>{@link #P_SHORT_07_S0_EX} (<code>0101100100100110</code>)</li>
     * <li>{@link #P_SHORT_07_S0_FX} (<code>0110011001001010</code>)</li>
     * <li>{@link #P_SHORT_07_S1_HX} (<code>1010010010010110</code>)</li>
     * <li>{@link #P_SHORT_11_S0_AX} (<code>0111011101101110</code>)</li>
     * <li>{@link #P_SHORT_11_S0_G} (<code>0110111001110111</code>)</li>
     * <li>{@link #P_SHORT_11_S0_J} (<code>0111011010110111</code>)</li>
     * <li>{@link #P_SHORT_11_S1_GX} (<code>1101110011101110</code>)</li>
     * </ul>
     */
    public static final short P_SHORT_05_S0_F = (short) 5189;

    /**
     * <code>0010100010001010</code>
     * <p/>
     * Other patterns with half bit-flip-distance (8):
     * <ul>
     * <li>{@link #P_SHORT_05_S0_C} (<code>0001001000100011</code>)</li>
     * <li>{@link #P_SHORT_05_S0_D} (<code>0001001000101001</code>)</li>
     * <li>{@link #P_SHORT_05_S0_H} (<code>0010001000100101</code>)</li>
     * <li>{@link #P_SHORT_05_S0_I} (<code>0010001000110001</code>)</li>
     * <li>{@link #P_SHORT_05_S0_IX} (<code>0100010001100010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_JX} (<code>0100010100100010</code>)</li>
     * <li>{@link #P_SHORT_07_S0_AX} (<code>0100100100110110</code>)</li>
     * <li>{@link #P_SHORT_07_S0_B} (<code>0010010100100111</code>)</li>
     * <li>{@link #P_SHORT_07_S0_C} (<code>0010011001011001</code>)</li>
     * <li>{@link #P_SHORT_07_S0_D} (<code>0010100101100101</code>)</li>
     * <li>{@link #P_SHORT_07_S0_EX} (<code>0101100100100110</code>)</li>
     * <li>{@link #P_SHORT_07_S0_H} (<code>0101001001001011</code>)</li>
     * <li>{@link #P_SHORT_07_S1_J} (<code>1011001001001001</code>)</li>
     * <li>{@link #P_SHORT_11_S0_A} (<code>0011101110110111</code>)</li>
     * <li>{@link #P_SHORT_11_S0_E} (<code>0110110110110111</code>)</li>
     * <li>{@link #P_SHORT_11_S0_I} (<code>0111010110111011</code>)</li>
     * <li>{@link #P_SHORT_11_S1_GX} (<code>1101110011101110</code>)</li>
     * <li>{@link #P_SHORT_11_S1_JX} (<code>1110110101101110</code>)</li>
     * </ul>
     */
    public static final short P_SHORT_05_S0_FX = (short) 10378;

    /**
     * <code>0001010100010001</code>
     * <p/>
     * Other patterns with half bit-flip-distance (8):
     * <ul>
     * <li>{@link #P_SHORT_05_S0_CX} (<code>0010010001000110</code>)</li>
     * <li>{@link #P_SHORT_05_S0_EX} (<code>0010010001100010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_H} (<code>0010001000100101</code>)</li>
     * <li>{@link #P_SHORT_05_S0_HX} (<code>0100010001001010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_IX} (<code>0100010001100010</code>)</li>
     * <li>{@link #P_SHORT_07_S0_AX} (<code>0100100100110110</code>)</li>
     * <li>{@link #P_SHORT_07_S0_CX} (<code>0100110010110010</code>)</li>
     * <li>{@link #P_SHORT_07_S0_D} (<code>0010100101100101</code>)</li>
     * <li>{@link #P_SHORT_07_S0_EX} (<code>0101100100100110</code>)</li>
     * <li>{@link #P_SHORT_07_S0_H} (<code>0101001001001011</code>)</li>
     * <li>{@link #P_SHORT_07_S1_HX} (<code>1010010010010110</code>)</li>
     * <li>{@link #P_SHORT_07_S1_J} (<code>1011001001001001</code>)</li>
     * <li>{@link #P_SHORT_11_S0_A} (<code>0011101110110111</code>)</li>
     * <li>{@link #P_SHORT_11_S0_E} (<code>0110110110110111</code>)</li>
     * <li>{@link #P_SHORT_11_S0_F} (<code>0110110111011011</code>)</li>
     * <li>{@link #P_SHORT_11_S0_J} (<code>0111011010110111</code>)</li>
     * </ul>
     */
    public static final short P_SHORT_05_S0_G = (short) 5393;

    /**
     * <code>0010101000100010</code>
     * <p/>
     * Other patterns with half bit-flip-distance (8):
     * <ul>
     * <li>{@link #P_SHORT_05_S0_HX} (<code>0100010001001010</code>)</li>
     * <li>{@link #P_SHORT_07_S0_A} (<code>0010010010011011</code>)</li>
     * <li>{@link #P_SHORT_07_S0_C} (<code>0010011001011001</code>)</li>
     * <li>{@link #P_SHORT_07_S0_DX} (<code>0101001011001010</code>)</li>
     * <li>{@link #P_SHORT_07_S0_H} (<code>0101001001001011</code>)</li>
     * <li>{@link #P_SHORT_07_S1_HX} (<code>1010010010010110</code>)</li>
     * <li>{@link #P_SHORT_07_S1_J} (<code>1011001001001001</code>)</li>
     * <li>{@link #P_SHORT_11_S0_AX} (<code>0111011101101110</code>)</li>
     * <li>{@link #P_SHORT_11_S0_E} (<code>0110110110110111</code>)</li>
     * <li>{@link #P_SHORT_11_S0_J} (<code>0111011010110111</code>)</li>
     * <li>{@link #P_SHORT_11_S1_EX} (<code>1101101101101110</code>)</li>
     * <li>{@link #P_SHORT_11_S1_FX} (<code>1101101110110110</code>)</li>
     * <li>{@link #P_SHORT_11_S1_JX} (<code>1110110101101110</code>)</li>
     * </ul>
     */
    public static final short P_SHORT_05_S0_GX = (short) 10786;

    /**
     * <code>0010001000100101</code>
     * <p/>
     * Other patterns with half bit-flip-distance (8):
     * <ul>
     * <li>{@link #P_SHORT_05_S0_DX} (<code>0010010001010010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_FX} (<code>0010100010001010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_G} (<code>0001010100010001</code>)</li>
     * <li>{@link #P_SHORT_05_S0_IX} (<code>0100010001100010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_JX} (<code>0100010100100010</code>)</li>
     * <li>{@link #P_SHORT_07_S0_A} (<code>0010010010011011</code>)</li>
     * <li>{@link #P_SHORT_07_S0_AX} (<code>0100100100110110</code>)</li>
     * <li>{@link #P_SHORT_07_S0_BX} (<code>0100101001001110</code>)</li>
     * <li>{@link #P_SHORT_07_S0_E} (<code>0010110010010011</code>)</li>
     * <li>{@link #P_SHORT_07_S0_EX} (<code>0101100100100110</code>)</li>
     * <li>{@link #P_SHORT_07_S0_FX} (<code>0110011001001010</code>)</li>
     * <li>{@link #P_SHORT_07_S0_G} (<code>0100100101010101</code>)</li>
     * <li>{@link #P_SHORT_07_S0_H} (<code>0101001001001011</code>)</li>
     * <li>{@link #P_SHORT_07_S1_GX} (<code>1001001010101010</code>)</li>
     * <li>{@link #P_SHORT_07_S1_HX} (<code>1010010010010110</code>)</li>
     * <li>{@link #P_SHORT_11_S0_AX} (<code>0111011101101110</code>)</li>
     * <li>{@link #P_SHORT_11_S0_B} (<code>0101011101110111</code>)</li>
     * <li>{@link #P_SHORT_11_S0_E} (<code>0110110110110111</code>)</li>
     * <li>{@link #P_SHORT_11_S0_H} (<code>0111010101110111</code>)</li>
     * <li>{@link #P_SHORT_11_S1_BX} (<code>1010111011101110</code>)</li>
     * <li>{@link #P_SHORT_11_S1_CX} (<code>1011101110101110</code>)</li>
     * <li>{@link #P_SHORT_11_S1_HX} (<code>1110101011101110</code>)</li>
     * <li>{@link #P_SHORT_11_S1_IX} (<code>1110101101110110</code>)</li>
     * </ul>
     */
    public static final short P_SHORT_05_S0_H = (short) 8741;

    /**
     * <code>0100010001001010</code>
     * <p/>
     * Other patterns with half bit-flip-distance (8):
     * <ul>
     * <li>{@link #P_SHORT_05_S0_B} (<code>0001000101000101</code>)</li>
     * <li>{@link #P_SHORT_05_S0_C} (<code>0001001000100011</code>)</li>
     * <li>{@link #P_SHORT_05_S0_D} (<code>0001001000101001</code>)</li>
     * <li>{@link #P_SHORT_05_S0_G} (<code>0001010100010001</code>)</li>
     * <li>{@link #P_SHORT_05_S0_GX} (<code>0010101000100010</code>)</li>
     * <li>{@link #P_SHORT_07_S0_AX} (<code>0100100100110110</code>)</li>
     * <li>{@link #P_SHORT_07_S0_B} (<code>0010010100100111</code>)</li>
     * <li>{@link #P_SHORT_07_S0_E} (<code>0010110010010011</code>)</li>
     * <li>{@link #P_SHORT_07_S0_EX} (<code>0101100100100110</code>)</li>
     * <li>{@link #P_SHORT_07_S0_G} (<code>0100100101010101</code>)</li>
     * <li>{@link #P_SHORT_07_S1_GX} (<code>1001001010101010</code>)</li>
     * <li>{@link #P_SHORT_07_S1_HX} (<code>1010010010010110</code>)</li>
     * <li>{@link #P_SHORT_07_S1_J} (<code>1011001001001001</code>)</li>
     * <li>{@link #P_SHORT_11_S0_B} (<code>0101011101110111</code>)</li>
     * <li>{@link #P_SHORT_11_S0_C} (<code>0101110111010111</code>)</li>
     * <li>{@link #P_SHORT_11_S0_D} (<code>0101110111011101</code>)</li>
     * <li>{@link #P_SHORT_11_S0_G} (<code>0110111001110111</code>)</li>
     * <li>{@link #P_SHORT_11_S0_H} (<code>0111010101110111</code>)</li>
     * <li>{@link #P_SHORT_11_S0_I} (<code>0111010110111011</code>)</li>
     * <li>{@link #P_SHORT_11_S1_BX} (<code>1010111011101110</code>)</li>
     * <li>{@link #P_SHORT_11_S1_EX} (<code>1101101101101110</code>)</li>
     * <li>{@link #P_SHORT_11_S1_HX} (<code>1110101011101110</code>)</li>
     * </ul>
     */
    public static final short P_SHORT_05_S0_HX = (short) 17482;

    /**
     * <code>0010001000110001</code>
     * <p/>
     * Other patterns with half bit-flip-distance (8):
     * <ul>
     * <li>{@link #P_SHORT_05_S0_B} (<code>0001000101000101</code>)</li>
     * <li>{@link #P_SHORT_05_S0_CX} (<code>0010010001000110</code>)</li>
     * <li>{@link #P_SHORT_05_S0_F} (<code>0001010001000101</code>)</li>
     * <li>{@link #P_SHORT_05_S0_FX} (<code>0010100010001010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_IX} (<code>0100010001100010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_JX} (<code>0100010100100010</code>)</li>
     * <li>{@link #P_SHORT_07_S0_AX} (<code>0100100100110110</code>)</li>
     * <li>{@link #P_SHORT_07_S0_CX} (<code>0100110010110010</code>)</li>
     * <li>{@link #P_SHORT_07_S0_FX} (<code>0110011001001010</code>)</li>
     * <li>{@link #P_SHORT_07_S0_G} (<code>0100100101010101</code>)</li>
     * <li>{@link #P_SHORT_07_S0_H} (<code>0101001001001011</code>)</li>
     * <li>{@link #P_SHORT_07_S1_GX} (<code>1001001010101010</code>)</li>
     * <li>{@link #P_SHORT_07_S1_HX} (<code>1010010010010110</code>)</li>
     * <li>{@link #P_SHORT_11_S0_B} (<code>0101011101110111</code>)</li>
     * <li>{@link #P_SHORT_11_S0_E} (<code>0110110110110111</code>)</li>
     * <li>{@link #P_SHORT_11_S0_H} (<code>0111010101110111</code>)</li>
     * <li>{@link #P_SHORT_11_S0_I} (<code>0111010110111011</code>)</li>
     * <li>{@link #P_SHORT_11_S1_DX} (<code>1011101110111010</code>)</li>
     * <li>{@link #P_SHORT_11_S1_IX} (<code>1110101101110110</code>)</li>
     * </ul>
     */
    public static final short P_SHORT_05_S0_I = (short) 8753L;

    /**
     * <code>0010001000110001</code>
     * <p/>
     * Other patterns with half bit-flip-distance (8):
     * <ul>
     * <li>{@link #P_SHORT_05_S0_B} (<code>0001000101000101</code>)</li>
     * <li>{@link #P_SHORT_05_S0_BX} (<code>0010001010001010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_D} (<code>0001001000101001</code>)</li>
     * <li>{@link #P_SHORT_05_S0_E} (<code>0001001000110001</code>)</li>
     * <li>{@link #P_SHORT_05_S0_FX} (<code>0010100010001010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_G} (<code>0001010100010001</code>)</li>
     * <li>{@link #P_SHORT_05_S0_H} (<code>0010001000100101</code>)</li>
     * <li>{@link #P_SHORT_05_S0_I} (<code>0010001000110001</code>)</li>
     * <li>{@link #P_SHORT_07_S0_A} (<code>0010010010011011</code>)</li>
     * <li>{@link #P_SHORT_07_S0_C} (<code>0010011001011001</code>)</li>
     * <li>{@link #P_SHORT_07_S0_D} (<code>0010100101100101</code>)</li>
     * <li>{@link #P_SHORT_07_S0_E} (<code>0010110010010011</code>)</li>
     * <li>{@link #P_SHORT_07_S0_G} (<code>0100100101010101</code>)</li>
     * <li>{@link #P_SHORT_07_S1_GX} (<code>1001001010101010</code>)</li>
     * <li>{@link #P_SHORT_07_S1_HX} (<code>1010010010010110</code>)</li>
     * <li>{@link #P_SHORT_11_S0_C} (<code>0101110111010111</code>)</li>
     * <li>{@link #P_SHORT_11_S0_E} (<code>0110110110110111</code>)</li>
     * <li>{@link #P_SHORT_11_S0_F} (<code>0110110111011011</code>)</li>
     * <li>{@link #P_SHORT_11_S0_I} (<code>0111010110111011</code>)</li>
     * <li>{@link #P_SHORT_11_S0_J} (<code>0111011010110111</code>)</li>
     * <li>{@link #P_SHORT_11_S1_BX} (<code>1010111011101110</code>)</li>
     * <li>{@link #P_SHORT_11_S1_EX} (<code>1101101101101110</code>)</li>
     * <li>{@link #P_SHORT_11_S1_HX} (<code>1110101011101110</code>)</li>
     * <li>{@link #P_SHORT_11_S1_IX} (<code>1110101101110110</code>)</li>
     * </ul>
     */
    public static final short P_SHORT_05_S0_IX = (short) 17506;

    /**
     * <code>0010001010010001</code>
     * <p/>
     * Other patterns with half bit-flip-distance (8):
     * <ul>
     * <li>{@link #P_SHORT_05_S0_B} (<code>0001000101000101</code>)</li>
     * <li>{@link #P_SHORT_05_S0_CX} (<code>0010010001000110</code>)</li>
     * <li>{@link #P_SHORT_05_S0_EX} (<code>0010010001100010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_F} (<code>0001010001000101</code>)</li>
     * <li>{@link #P_SHORT_07_S0_B} (<code>0010010100100111</code>)</li>
     * <li>{@link #P_SHORT_07_S0_CX} (<code>0100110010110010</code>)</li>
     * <li>{@link #P_SHORT_07_S0_D} (<code>0010100101100101</code>)</li>
     * <li>{@link #P_SHORT_07_S0_DX} (<code>0101001011001010</code>)</li>
     * <li>{@link #P_SHORT_07_S0_FX} (<code>0110011001001010</code>)</li>
     * <li>{@link #P_SHORT_07_S0_G} (<code>0100100101010101</code>)</li>
     * <li>{@link #P_SHORT_07_S0_H} (<code>0101001001001011</code>)</li>
     * <li>{@link #P_SHORT_07_S1_GX} (<code>1001001010101010</code>)</li>
     * <li>{@link #P_SHORT_11_S0_E} (<code>0110110110110111</code>)</li>
     * <li>{@link #P_SHORT_11_S0_F} (<code>0110110111011011</code>)</li>
     * <li>{@link #P_SHORT_11_S0_G} (<code>0110111001110111</code>)</li>
     * <li>{@link #P_SHORT_11_S0_I} (<code>0111010110111011</code>)</li>
     * <li>{@link #P_SHORT_11_S1_DX} (<code>1011101110111010</code>)</li>
     * </ul>
     */
    public static final short P_SHORT_05_S0_J = (short) 8849;

    /**
     * <code>0100010100100010</code>
     * <p/>
     * Other patterns with half bit-flip-distance (8):
     * <ul>
     * <li>{@link #P_SHORT_05_S0_A} (<code>0001000100010101</code>)</li>
     * <li>{@link #P_SHORT_05_S0_B} (<code>0001000101000101</code>)</li>
     * <li>{@link #P_SHORT_05_S0_BX} (<code>0010001010001010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_D} (<code>0001001000101001</code>)</li>
     * <li>{@link #P_SHORT_05_S0_E} (<code>0001001000110001</code>)</li>
     * <li>{@link #P_SHORT_05_S0_F} (<code>0001010001000101</code>)</li>
     * <li>{@link #P_SHORT_05_S0_FX} (<code>0010100010001010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_H} (<code>0010001000100101</code>)</li>
     * <li>{@link #P_SHORT_05_S0_I} (<code>0010001000110001</code>)</li>
     * <li>{@link #P_SHORT_07_S0_A} (<code>0010010010011011</code>)</li>
     * <li>{@link #P_SHORT_07_S0_BX} (<code>0100101001001110</code>)</li>
     * <li>{@link #P_SHORT_07_S0_D} (<code>0010100101100101</code>)</li>
     * <li>{@link #P_SHORT_07_S0_DX} (<code>0101001011001010</code>)</li>
     * <li>{@link #P_SHORT_07_S0_E} (<code>0010110010010011</code>)</li>
     * <li>{@link #P_SHORT_07_S0_F} (<code>0011001100100101</code>)</li>
     * <li>{@link #P_SHORT_07_S0_G} (<code>0100100101010101</code>)</li>
     * <li>{@link #P_SHORT_07_S0_H} (<code>0101001001001011</code>)</li>
     * <li>{@link #P_SHORT_07_S1_GX} (<code>1001001010101010</code>)</li>
     * <li>{@link #P_SHORT_07_S1_HX} (<code>1010010010010110</code>)</li>
     * <li>{@link #P_SHORT_11_S0_C} (<code>0101110111010111</code>)</li>
     * <li>{@link #P_SHORT_11_S0_F} (<code>0110110111011011</code>)</li>
     * <li>{@link #P_SHORT_11_S0_G} (<code>0110111001110111</code>)</li>
     * <li>{@link #P_SHORT_11_S0_J} (<code>0111011010110111</code>)</li>
     * <li>{@link #P_SHORT_11_S1_EX} (<code>1101101101101110</code>)</li>
     * <li>{@link #P_SHORT_11_S1_FX} (<code>1101101110110110</code>)</li>
     * <li>{@link #P_SHORT_11_S1_GX} (<code>1101110011101110</code>)</li>
     * <li>{@link #P_SHORT_11_S1_IX} (<code>1110101101110110</code>)</li>
     * </ul>
     */
    public static final short P_SHORT_05_S0_JX = (short) 17698;

    // Short patterns with 7 bits set

    /**
     * <code>0010010010011011</code>
     * <p/>
     * Other patterns with half bit-flip-distance (8):
     * <ul>
     * <li>{@link #P_SHORT_05_S0_A} (<code>0001000100010101</code>)</li>
     * <li>{@link #P_SHORT_05_S0_C} (<code>0001001000100011</code>)</li>
     * <li>{@link #P_SHORT_05_S0_D} (<code>0001001000101001</code>)</li>
     * <li>{@link #P_SHORT_05_S0_E} (<code>0001001000110001</code>)</li>
     * <li>{@link #P_SHORT_05_S0_F} (<code>0001010001000101</code>)</li>
     * <li>{@link #P_SHORT_05_S0_GX} (<code>0010101000100010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_H} (<code>0010001000100101</code>)</li>
     * <li>{@link #P_SHORT_05_S0_IX} (<code>0100010001100010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_JX} (<code>0100010100100010</code>)</li>
     * <li>{@link #P_SHORT_07_S0_DX} (<code>0101001011001010</code>)</li>
     * <li>{@link #P_SHORT_07_S0_H} (<code>0101001001001011</code>)</li>
     * <li>{@link #P_SHORT_07_S1_GX} (<code>1001001010101010</code>)</li>
     * <li>{@link #P_SHORT_07_S1_J} (<code>1011001001001001</code>)</li>
     * <li>{@link #P_SHORT_11_S0_A} (<code>0011101110110111</code>)</li>
     * <li>{@link #P_SHORT_11_S0_C} (<code>0101110111010111</code>)</li>
     * <li>{@link #P_SHORT_11_S0_D} (<code>0101110111011101</code>)</li>
     * <li>{@link #P_SHORT_11_S0_G} (<code>0110111001110111</code>)</li>
     * <li>{@link #P_SHORT_11_S0_H} (<code>0111010101110111</code>)</li>
     * <li>{@link #P_SHORT_11_S1_BX} (<code>1010111011101110</code>)</li>
     * <li>{@link #P_SHORT_11_S1_DX} (<code>1011101110111010</code>)</li>
     * </ul>
     */
    public static final short P_SHORT_07_S0_A = (short) 9371;

    /**
     * <code>0100100100110110</code>
     * <p/>
     * Other patterns with half bit-flip-distance (8):
     * <ul>
     * <li>{@link #P_SHORT_05_S0_AX} (<code>0010001000101010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_B} (<code>0001000101000101</code>)</li>
     * <li>{@link #P_SHORT_05_S0_C} (<code>0001001000100011</code>)</li>
     * <li>{@link #P_SHORT_05_S0_CX} (<code>0010010001000110</code>)</li>
     * <li>{@link #P_SHORT_05_S0_DX} (<code>0010010001010010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_E} (<code>0001001000110001</code>)</li>
     * <li>{@link #P_SHORT_05_S0_EX} (<code>0010010001100010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_FX} (<code>0010100010001010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_G} (<code>0001010100010001</code>)</li>
     * <li>{@link #P_SHORT_05_S0_H} (<code>0010001000100101</code>)</li>
     * <li>{@link #P_SHORT_05_S0_HX} (<code>0100010001001010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_I} (<code>0010001000110001</code>)</li>
     * <li>{@link #P_SHORT_07_S0_E} (<code>0010110010010011</code>)</li>
     * <li>{@link #P_SHORT_07_S0_F} (<code>0011001100100101</code>)</li>
     * <li>{@link #P_SHORT_07_S1_HX} (<code>1010010010010110</code>)</li>
     * <li>{@link #P_SHORT_11_S0_AX} (<code>0111011101101110</code>)</li>
     * <li>{@link #P_SHORT_11_S0_D} (<code>0101110111011101</code>)</li>
     * <li>{@link #P_SHORT_11_S0_F} (<code>0110110111011011</code>)</li>
     * <li>{@link #P_SHORT_11_S0_I} (<code>0111010110111011</code>)</li>
     * <li>{@link #P_SHORT_11_S0_J} (<code>0111011010110111</code>)</li>
     * <li>{@link #P_SHORT_11_S1_CX} (<code>1011101110101110</code>)</li>
     * <li>{@link #P_SHORT_11_S1_DX} (<code>1011101110111010</code>)</li>
     * <li>{@link #P_SHORT_11_S1_GX} (<code>1101110011101110</code>)</li>
     * <li>{@link #P_SHORT_11_S1_HX} (<code>1110101011101110</code>)</li>
     * </ul>
     */
    public static final short P_SHORT_07_S0_AX = (short) 18742;

    /**
     * <code>0010010100100111</code>
     * <p/>
     * Other patterns with half bit-flip-distance (8):
     * <ul>
     * <li>{@link #P_SHORT_05_S0_BX} (<code>0010001010001010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_D} (<code>0001001000101001</code>)</li>
     * <li>{@link #P_SHORT_05_S0_E} (<code>0001001000110001</code>)</li>
     * <li>{@link #P_SHORT_05_S0_FX} (<code>0010100010001010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_HX} (<code>0100010001001010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_J} (<code>0010001010010001</code>)</li>
     * <li>{@link #P_SHORT_07_S0_C} (<code>0010011001011001</code>)</li>
     * <li>{@link #P_SHORT_07_S0_CX} (<code>0100110010110010</code>)</li>
     * <li>{@link #P_SHORT_07_S0_FX} (<code>0110011001001010</code>)</li>
     * <li>{@link #P_SHORT_07_S0_G} (<code>0100100101010101</code>)</li>
     * <li>{@link #P_SHORT_11_S0_C} (<code>0101110111010111</code>)</li>
     * <li>{@link #P_SHORT_11_S0_F} (<code>0110110111011011</code>)</li>
     * <li>{@link #P_SHORT_11_S1_BX} (<code>1010111011101110</code>)</li>
     * <li>{@link #P_SHORT_11_S1_CX} (<code>1011101110101110</code>)</li>
     * <li>{@link #P_SHORT_11_S1_IX} (<code>1110101101110110</code>)</li>
     * </ul>
     */
    public static final short P_SHORT_07_S0_B = (short) 9511;

    /**
     * <code>0100101001001110</code>
     * <p/>
     * Other patterns with half bit-flip-distance (8):
     * <ul>
     * <li>{@link #P_SHORT_05_S0_B} (<code>0001000101000101</code>)</li>
     * <li>{@link #P_SHORT_05_S0_C} (<code>0001001000100011</code>)</li>
     * <li>{@link #P_SHORT_05_S0_D} (<code>0001001000101001</code>)</li>
     * <li>{@link #P_SHORT_05_S0_DX} (<code>0010010001010010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_EX} (<code>0010010001100010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_F} (<code>0001010001000101</code>)</li>
     * <li>{@link #P_SHORT_05_S0_H} (<code>0010001000100101</code>)</li>
     * <li>{@link #P_SHORT_05_S0_JX} (<code>0100010100100010</code>)</li>
     * <li>{@link #P_SHORT_07_S0_C} (<code>0010011001011001</code>)</li>
     * <li>{@link #P_SHORT_07_S0_CX} (<code>0100110010110010</code>)</li>
     * <li>{@link #P_SHORT_07_S0_D} (<code>0010100101100101</code>)</li>
     * <li>{@link #P_SHORT_07_S1_GX} (<code>1001001010101010</code>)</li>
     * <li>{@link #P_SHORT_07_S1_J} (<code>1011001001001001</code>)</li>
     * <li>{@link #P_SHORT_11_S0_B} (<code>0101011101110111</code>)</li>
     * <li>{@link #P_SHORT_11_S0_C} (<code>0101110111010111</code>)</li>
     * <li>{@link #P_SHORT_11_S0_D} (<code>0101110111011101</code>)</li>
     * <li>{@link #P_SHORT_11_S0_F} (<code>0110110111011011</code>)</li>
     * <li>{@link #P_SHORT_11_S1_CX} (<code>1011101110101110</code>)</li>
     * <li>{@link #P_SHORT_11_S1_FX} (<code>1101101110110110</code>)</li>
     * </ul>
     */
    public static final short P_SHORT_07_S0_BX = (short) 19022;

    /**
     * <code>0010011001011001</code>
     * <p/>
     * Other patterns with half bit-flip-distance (8):
     * <ul>
     * <li>{@link #P_SHORT_05_S0_A} (<code>0001000100010101</code>)</li>
     * <li>{@link #P_SHORT_05_S0_B} (<code>0001000101000101</code>)</li>
     * <li>{@link #P_SHORT_05_S0_C} (<code>0001001000100011</code>)</li>
     * <li>{@link #P_SHORT_05_S0_FX} (<code>0010100010001010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_GX} (<code>0010101000100010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_IX} (<code>0100010001100010</code>)</li>
     * <li>{@link #P_SHORT_07_S0_B} (<code>0010010100100111</code>)</li>
     * <li>{@link #P_SHORT_07_S0_BX} (<code>0100101001001110</code>)</li>
     * <li>{@link #P_SHORT_07_S0_D} (<code>0010100101100101</code>)</li>
     * <li>{@link #P_SHORT_07_S0_DX} (<code>0101001011001010</code>)</li>
     * <li>{@link #P_SHORT_07_S0_F} (<code>0011001100100101</code>)</li>
     * <li>{@link #P_SHORT_07_S0_G} (<code>0100100101010101</code>)</li>
     * <li>{@link #P_SHORT_07_S1_HX} (<code>1010010010010110</code>)</li>
     * <li>{@link #P_SHORT_11_S0_AX} (<code>0111011101101110</code>)</li>
     * <li>{@link #P_SHORT_11_S0_B} (<code>0101011101110111</code>)</li>
     * <li>{@link #P_SHORT_11_S0_D} (<code>0101110111011101</code>)</li>
     * <li>{@link #P_SHORT_11_S0_H} (<code>0111010101110111</code>)</li>
     * <li>{@link #P_SHORT_11_S0_I} (<code>0111010110111011</code>)</li>
     * <li>{@link #P_SHORT_11_S0_J} (<code>0111011010110111</code>)</li>
     * <li>{@link #P_SHORT_11_S1_BX} (<code>1010111011101110</code>)</li>
     * </ul>
     */
    public static final short P_SHORT_07_S0_C = (short) 9817;

    /**
     * <code>0100110010110010</code>
     * <p/>
     * Other patterns with half bit-flip-distance (8):
     * <ul>
     * <li>{@link #P_SHORT_05_S0_AX} (<code>0010001000101010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_BX} (<code>0010001010001010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_C} (<code>0001001000100011</code>)</li>
     * <li>{@link #P_SHORT_05_S0_CX} (<code>0010010001000110</code>)</li>
     * <li>{@link #P_SHORT_05_S0_E} (<code>0001001000110001</code>)</li>
     * <li>{@link #P_SHORT_05_S0_G} (<code>0001010100010001</code>)</li>
     * <li>{@link #P_SHORT_05_S0_I} (<code>0010001000110001</code>)</li>
     * <li>{@link #P_SHORT_05_S0_J} (<code>0010001010010001</code>)</li>
     * <li>{@link #P_SHORT_07_S0_B} (<code>0010010100100111</code>)</li>
     * <li>{@link #P_SHORT_07_S0_BX} (<code>0100101001001110</code>)</li>
     * <li>{@link #P_SHORT_07_S0_DX} (<code>0101001011001010</code>)</li>
     * <li>{@link #P_SHORT_07_S0_FX} (<code>0110011001001010</code>)</li>
     * <li>{@link #P_SHORT_07_S0_G} (<code>0100100101010101</code>)</li>
     * <li>{@link #P_SHORT_07_S1_GX} (<code>1001001010101010</code>)</li>
     * <li>{@link #P_SHORT_11_S0_A} (<code>0011101110110111</code>)</li>
     * <li>{@link #P_SHORT_11_S0_B} (<code>0101011101110111</code>)</li>
     * <li>{@link #P_SHORT_11_S0_D} (<code>0101110111011101</code>)</li>
     * <li>{@link #P_SHORT_11_S0_H} (<code>0111010101110111</code>)</li>
     * <li>{@link #P_SHORT_11_S1_BX} (<code>1010111011101110</code>)</li>
     * <li>{@link #P_SHORT_11_S1_DX} (<code>1011101110111010</code>)</li>
     * <li>{@link #P_SHORT_11_S1_HX} (<code>1110101011101110</code>)</li>
     * <li>{@link #P_SHORT_11_S1_IX} (<code>1110101101110110</code>)</li>
     * <li>{@link #P_SHORT_11_S1_JX} (<code>1110110101101110</code>)</li>
     * </ul>
     */
    public static final short P_SHORT_07_S0_CX = (short) 19634;

    /**
     * <code>0010100101100101</code>
     * <p/>
     * Other patterns with half bit-flip-distance (8):
     * <ul>
     * <li>{@link #P_SHORT_05_S0_AX} (<code>0010001000101010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_C} (<code>0001001000100011</code>)</li>
     * <li>{@link #P_SHORT_05_S0_D} (<code>0001001000101001</code>)</li>
     * <li>{@link #P_SHORT_05_S0_DX} (<code>0010010001010010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_E} (<code>0001001000110001</code>)</li>
     * <li>{@link #P_SHORT_05_S0_FX} (<code>0010100010001010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_G} (<code>0001010100010001</code>)</li>
     * <li>{@link #P_SHORT_05_S0_IX} (<code>0100010001100010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_J} (<code>0010001010010001</code>)</li>
     * <li>{@link #P_SHORT_05_S0_JX} (<code>0100010100100010</code>)</li>
     * <li>{@link #P_SHORT_07_S0_BX} (<code>0100101001001110</code>)</li>
     * <li>{@link #P_SHORT_07_S0_C} (<code>0010011001011001</code>)</li>
     * <li>{@link #P_SHORT_07_S0_E} (<code>0010110010010011</code>)</li>
     * <li>{@link #P_SHORT_07_S1_J} (<code>1011001001001001</code>)</li>
     * <li>{@link #P_SHORT_11_S0_AX} (<code>0111011101101110</code>)</li>
     * <li>{@link #P_SHORT_11_S0_B} (<code>0101011101110111</code>)</li>
     * <li>{@link #P_SHORT_11_S0_C} (<code>0101110111010111</code>)</li>
     * <li>{@link #P_SHORT_11_S0_D} (<code>0101110111011101</code>)</li>
     * <li>{@link #P_SHORT_11_S0_F} (<code>0110110111011011</code>)</li>
     * <li>{@link #P_SHORT_11_S1_BX} (<code>1010111011101110</code>)</li>
     * <li>{@link #P_SHORT_11_S1_CX} (<code>1011101110101110</code>)</li>
     * <li>{@link #P_SHORT_11_S1_EX} (<code>1101101101101110</code>)</li>
     * <li>{@link #P_SHORT_11_S1_HX} (<code>1110101011101110</code>)</li>
     * </ul>
     */
    public static final short P_SHORT_07_S0_D = (short) 10597;

    /**
     * <code>0101001011001010</code>
     * <p/>
     * Other patterns with half bit-flip-distance (8):
     * <ul>
     * <li>{@link #P_SHORT_05_S0_B} (<code>0001000101000101</code>)</li>
     * <li>{@link #P_SHORT_05_S0_CX} (<code>0010010001000110</code>)</li>
     * <li>{@link #P_SHORT_05_S0_DX} (<code>0010010001010010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_E} (<code>0001001000110001</code>)</li>
     * <li>{@link #P_SHORT_05_S0_EX} (<code>0010010001100010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_F} (<code>0001010001000101</code>)</li>
     * <li>{@link #P_SHORT_05_S0_GX} (<code>0010101000100010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_J} (<code>0010001010010001</code>)</li>
     * <li>{@link #P_SHORT_05_S0_JX} (<code>0100010100100010</code>)</li>
     * <li>{@link #P_SHORT_07_S0_A} (<code>0010010010011011</code>)</li>
     * <li>{@link #P_SHORT_07_S0_C} (<code>0010011001011001</code>)</li>
     * <li>{@link #P_SHORT_07_S0_CX} (<code>0100110010110010</code>)</li>
     * <li>{@link #P_SHORT_07_S0_EX} (<code>0101100100100110</code>)</li>
     * <li>{@link #P_SHORT_11_S0_B} (<code>0101011101110111</code>)</li>
     * <li>{@link #P_SHORT_11_S0_C} (<code>0101110111010111</code>)</li>
     * <li>{@link #P_SHORT_11_S0_D} (<code>0101110111011101</code>)</li>
     * <li>{@link #P_SHORT_11_S0_F} (<code>0110110111011011</code>)</li>
     * <li>{@link #P_SHORT_11_S0_I} (<code>0111010110111011</code>)</li>
     * <li>{@link #P_SHORT_11_S0_J} (<code>0111011010110111</code>)</li>
     * <li>{@link #P_SHORT_11_S1_BX} (<code>1010111011101110</code>)</li>
     * <li>{@link #P_SHORT_11_S1_CX} (<code>1011101110101110</code>)</li>
     * <li>{@link #P_SHORT_11_S1_DX} (<code>1011101110111010</code>)</li>
     * <li>{@link #P_SHORT_11_S1_FX} (<code>1101101110110110</code>)</li>
     * </ul>
     */
    public static final short P_SHORT_07_S0_DX = (short) 21194;

    /**
     * <code>0010110010010011</code>
     * <p/>
     * Other patterns with half bit-flip-distance (8):
     * <ul>
     * <li>{@link #P_SHORT_05_S0_A} (<code>0001000100010101</code>)</li>
     * <li>{@link #P_SHORT_05_S0_AX} (<code>0010001000101010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_C} (<code>0001001000100011</code>)</li>
     * <li>{@link #P_SHORT_05_S0_E} (<code>0001001000110001</code>)</li>
     * <li>{@link #P_SHORT_05_S0_F} (<code>0001010001000101</code>)</li>
     * <li>{@link #P_SHORT_05_S0_H} (<code>0010001000100101</code>)</li>
     * <li>{@link #P_SHORT_05_S0_HX} (<code>0100010001001010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_IX} (<code>0100010001100010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_JX} (<code>0100010100100010</code>)</li>
     * <li>{@link #P_SHORT_07_S0_AX} (<code>0100100100110110</code>)</li>
     * <li>{@link #P_SHORT_07_S0_D} (<code>0010100101100101</code>)</li>
     * <li>{@link #P_SHORT_07_S0_FX} (<code>0110011001001010</code>)</li>
     * <li>{@link #P_SHORT_07_S0_G} (<code>0100100101010101</code>)</li>
     * <li>{@link #P_SHORT_11_S0_D} (<code>0101110111011101</code>)</li>
     * <li>{@link #P_SHORT_11_S0_H} (<code>0111010101110111</code>)</li>
     * <li>{@link #P_SHORT_11_S1_BX} (<code>1010111011101110</code>)</li>
     * <li>{@link #P_SHORT_11_S1_DX} (<code>1011101110111010</code>)</li>
     * </ul>
     */
    public static final short P_SHORT_07_S0_E = (short) 11411;

    /**
     * <code>0101100100100110</code>
     * <p/>
     * Other patterns with half bit-flip-distance (8):
     * <ul>
     * <li>{@link #P_SHORT_05_S0_AX} (<code>0010001000101010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_CX} (<code>0010010001000110</code>)</li>
     * <li>{@link #P_SHORT_05_S0_D} (<code>0001001000101001</code>)</li>
     * <li>{@link #P_SHORT_05_S0_E} (<code>0001001000110001</code>)</li>
     * <li>{@link #P_SHORT_05_S0_EX} (<code>0010010001100010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_F} (<code>0001010001000101</code>)</li>
     * <li>{@link #P_SHORT_05_S0_FX} (<code>0010100010001010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_G} (<code>0001010100010001</code>)</li>
     * <li>{@link #P_SHORT_05_S0_H} (<code>0010001000100101</code>)</li>
     * <li>{@link #P_SHORT_05_S0_HX} (<code>0100010001001010</code>)</li>
     * <li>{@link #P_SHORT_07_S0_DX} (<code>0101001011001010</code>)</li>
     * <li>{@link #P_SHORT_07_S0_H} (<code>0101001001001011</code>)</li>
     * <li>{@link #P_SHORT_07_S1_GX} (<code>1001001010101010</code>)</li>
     * <li>{@link #P_SHORT_11_S0_D} (<code>0101110111011101</code>)</li>
     * <li>{@link #P_SHORT_11_S0_G} (<code>0110111001110111</code>)</li>
     * <li>{@link #P_SHORT_11_S0_I} (<code>0111010110111011</code>)</li>
     * <li>{@link #P_SHORT_11_S0_J} (<code>0111011010110111</code>)</li>
     * <li>{@link #P_SHORT_11_S1_DX} (<code>1011101110111010</code>)</li>
     * <li>{@link #P_SHORT_11_S1_HX} (<code>1110101011101110</code>)</li>
     * </ul>
     */
    public static final short P_SHORT_07_S0_EX = (short) 22822;

    /**
     * <code>0011001100100101</code>
     * <p/>
     * Other patterns with half bit-flip-distance (8):
     * <ul>
     * <li>{@link #P_SHORT_05_S0_BX} (<code>0010001010001010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_CX} (<code>0010010001000110</code>)</li>
     * <li>{@link #P_SHORT_05_S0_EX} (<code>0010010001100010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_JX} (<code>0100010100100010</code>)</li>
     * <li>{@link #P_SHORT_07_S0_AX} (<code>0100100100110110</code>)</li>
     * <li>{@link #P_SHORT_07_S0_C} (<code>0010011001011001</code>)</li>
     * <li>{@link #P_SHORT_07_S0_G} (<code>0100100101010101</code>)</li>
     * <li>{@link #P_SHORT_07_S0_H} (<code>0101001001001011</code>)</li>
     * <li>{@link #P_SHORT_07_S1_GX} (<code>1001001010101010</code>)</li>
     * <li>{@link #P_SHORT_11_S0_E} (<code>0110110110110111</code>)</li>
     * <li>{@link #P_SHORT_11_S0_G} (<code>0110111001110111</code>)</li>
     * <li>{@link #P_SHORT_11_S0_I} (<code>0111010110111011</code>)</li>
     * <li>{@link #P_SHORT_11_S1_DX} (<code>1011101110111010</code>)</li>
     * <li>{@link #P_SHORT_11_S1_EX} (<code>1101101101101110</code>)</li>
     * <li>{@link #P_SHORT_11_S1_FX} (<code>1101101110110110</code>)</li>
     * <li>{@link #P_SHORT_11_S1_IX} (<code>1110101101110110</code>)</li>
     * </ul>
     */
    public static final short P_SHORT_07_S0_F = (short) 13093;

    /**
     * <code>0110011001001010</code>
     * <p/>
     * Other patterns with half bit-flip-distance (8):
     * <ul>
     * <li>{@link #P_SHORT_05_S0_C} (<code>0001001000100011</code>)</li>
     * <li>{@link #P_SHORT_05_S0_D} (<code>0001001000101001</code>)</li>
     * <li>{@link #P_SHORT_05_S0_F} (<code>0001010001000101</code>)</li>
     * <li>{@link #P_SHORT_05_S0_H} (<code>0010001000100101</code>)</li>
     * <li>{@link #P_SHORT_05_S0_I} (<code>0010001000110001</code>)</li>
     * <li>{@link #P_SHORT_05_S0_J} (<code>0010001010010001</code>)</li>
     * <li>{@link #P_SHORT_07_S0_B} (<code>0010010100100111</code>)</li>
     * <li>{@link #P_SHORT_07_S0_CX} (<code>0100110010110010</code>)</li>
     * <li>{@link #P_SHORT_07_S0_E} (<code>0010110010010011</code>)</li>
     * <li>{@link #P_SHORT_07_S1_GX} (<code>1001001010101010</code>)</li>
     * <li>{@link #P_SHORT_07_S1_HX} (<code>1010010010010110</code>)</li>
     * <li>{@link #P_SHORT_11_S0_B} (<code>0101011101110111</code>)</li>
     * <li>{@link #P_SHORT_11_S0_H} (<code>0111010101110111</code>)</li>
     * <li>{@link #P_SHORT_11_S0_I} (<code>0111010110111011</code>)</li>
     * <li>{@link #P_SHORT_11_S0_J} (<code>0111011010110111</code>)</li>
     * <li>{@link #P_SHORT_11_S1_EX} (<code>1101101101101110</code>)</li>
     * <li>{@link #P_SHORT_11_S1_GX} (<code>1101110011101110</code>)</li>
     * <li>{@link #P_SHORT_11_S1_IX} (<code>1110101101110110</code>)</li>
     * </ul>
     */
    public static final short P_SHORT_07_S0_FX = (short) 26186;

    /**
     * <code>0100100101010101</code>
     * <p/>
     * Other patterns with half bit-flip-distance (8):
     * <ul>
     * <li>{@link #P_SHORT_05_S0_CX} (<code>0010010001000110</code>)</li>
     * <li>{@link #P_SHORT_05_S0_DX} (<code>0010010001010010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_E} (<code>0001001000110001</code>)</li>
     * <li>{@link #P_SHORT_05_S0_H} (<code>0010001000100101</code>)</li>
     * <li>{@link #P_SHORT_05_S0_HX} (<code>0100010001001010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_I} (<code>0010001000110001</code>)</li>
     * <li>{@link #P_SHORT_05_S0_IX} (<code>0100010001100010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_J} (<code>0010001010010001</code>)</li>
     * <li>{@link #P_SHORT_05_S0_JX} (<code>0100010100100010</code>)</li>
     * <li>{@link #P_SHORT_07_S0_B} (<code>0010010100100111</code>)</li>
     * <li>{@link #P_SHORT_07_S0_C} (<code>0010011001011001</code>)</li>
     * <li>{@link #P_SHORT_07_S0_CX} (<code>0100110010110010</code>)</li>
     * <li>{@link #P_SHORT_07_S0_E} (<code>0010110010010011</code>)</li>
     * <li>{@link #P_SHORT_07_S0_F} (<code>0011001100100101</code>)</li>
     * <li>{@link #P_SHORT_07_S0_H} (<code>0101001001001011</code>)</li>
     * <li>{@link #P_SHORT_11_S0_A} (<code>0011101110110111</code>)</li>
     * <li>{@link #P_SHORT_11_S1_EX} (<code>1101101101101110</code>)</li>
     * <li>{@link #P_SHORT_11_S1_FX} (<code>1101101110110110</code>)</li>
     * <li>{@link #P_SHORT_11_S1_JX} (<code>1110110101101110</code>)</li>
     * </ul>
     */
    public static final short P_SHORT_07_S0_G = (short) 18773;

    /**
     * <code>1001001010101010</code>
     * <p/>
     * Other patterns with half bit-flip-distance (8):
     * <ul>
     * <li>{@link #P_SHORT_05_S0_EX} (<code>0010010001100010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_H} (<code>0010001000100101</code>)</li>
     * <li>{@link #P_SHORT_05_S0_HX} (<code>0100010001001010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_I} (<code>0010001000110001</code>)</li>
     * <li>{@link #P_SHORT_05_S0_IX} (<code>0100010001100010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_J} (<code>0010001010010001</code>)</li>
     * <li>{@link #P_SHORT_05_S0_JX} (<code>0100010100100010</code>)</li>
     * <li>{@link #P_SHORT_07_S0_A} (<code>0010010010011011</code>)</li>
     * <li>{@link #P_SHORT_07_S0_BX} (<code>0100101001001110</code>)</li>
     * <li>{@link #P_SHORT_07_S0_CX} (<code>0100110010110010</code>)</li>
     * <li>{@link #P_SHORT_07_S0_EX} (<code>0101100100100110</code>)</li>
     * <li>{@link #P_SHORT_07_S0_F} (<code>0011001100100101</code>)</li>
     * <li>{@link #P_SHORT_07_S0_FX} (<code>0110011001001010</code>)</li>
     * <li>{@link #P_SHORT_07_S1_HX} (<code>1010010010010110</code>)</li>
     * <li>{@link #P_SHORT_11_S0_A} (<code>0011101110110111</code>)</li>
     * <li>{@link #P_SHORT_11_S0_AX} (<code>0111011101101110</code>)</li>
     * <li>{@link #P_SHORT_11_S0_I} (<code>0111010110111011</code>)</li>
     * <li>{@link #P_SHORT_11_S0_J} (<code>0111011010110111</code>)</li>
     * </ul>
     */
    public static final short P_SHORT_07_S1_GX = (short) -27990;

    /**
     * <code>0101001001001011</code>
     * <p/>
     * Other patterns with half bit-flip-distance (8):
     * <ul>
     * <li>{@link #P_SHORT_05_S0_A} (<code>0001000100010101</code>)</li>
     * <li>{@link #P_SHORT_05_S0_CX} (<code>0010010001000110</code>)</li>
     * <li>{@link #P_SHORT_05_S0_DX} (<code>0010010001010010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_EX} (<code>0010010001100010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_FX} (<code>0010100010001010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_G} (<code>0001010100010001</code>)</li>
     * <li>{@link #P_SHORT_05_S0_GX} (<code>0010101000100010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_H} (<code>0010001000100101</code>)</li>
     * <li>{@link #P_SHORT_05_S0_I} (<code>0010001000110001</code>)</li>
     * <li>{@link #P_SHORT_05_S0_J} (<code>0010001010010001</code>)</li>
     * <li>{@link #P_SHORT_05_S0_JX} (<code>0100010100100010</code>)</li>
     * <li>{@link #P_SHORT_07_S0_A} (<code>0010010010011011</code>)</li>
     * <li>{@link #P_SHORT_07_S0_EX} (<code>0101100100100110</code>)</li>
     * <li>{@link #P_SHORT_07_S0_F} (<code>0011001100100101</code>)</li>
     * <li>{@link #P_SHORT_07_S0_G} (<code>0100100101010101</code>)</li>
     * <li>{@link #P_SHORT_11_S0_C} (<code>0101110111010111</code>)</li>
     * <li>{@link #P_SHORT_11_S0_D} (<code>0101110111011101</code>)</li>
     * <li>{@link #P_SHORT_11_S0_F} (<code>0110110111011011</code>)</li>
     * <li>{@link #P_SHORT_11_S0_G} (<code>0110111001110111</code>)</li>
     * <li>{@link #P_SHORT_11_S0_H} (<code>0111010101110111</code>)</li>
     * <li>{@link #P_SHORT_11_S0_I} (<code>0111010110111011</code>)</li>
     * <li>{@link #P_SHORT_11_S0_J} (<code>0111011010110111</code>)</li>
     * <li>{@link #P_SHORT_11_S1_GX} (<code>1101110011101110</code>)</li>
     * <li>{@link #P_SHORT_11_S1_HX} (<code>1110101011101110</code>)</li>
     * </ul>
     */
    public static final short P_SHORT_07_S0_H = (short) 21067;

    /**
     * <code>1010010010010110</code>
     * <p/>
     * Other patterns with half bit-flip-distance (8):
     * <ul>
     * <li>{@link #P_SHORT_05_S0_A} (<code>0001000100010101</code>)</li>
     * <li>{@link #P_SHORT_05_S0_AX} (<code>0010001000101010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_F} (<code>0001010001000101</code>)</li>
     * <li>{@link #P_SHORT_05_S0_G} (<code>0001010100010001</code>)</li>
     * <li>{@link #P_SHORT_05_S0_GX} (<code>0010101000100010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_H} (<code>0010001000100101</code>)</li>
     * <li>{@link #P_SHORT_05_S0_HX} (<code>0100010001001010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_I} (<code>0010001000110001</code>)</li>
     * <li>{@link #P_SHORT_05_S0_IX} (<code>0100010001100010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_JX} (<code>0100010100100010</code>)</li>
     * <li>{@link #P_SHORT_07_S0_AX} (<code>0100100100110110</code>)</li>
     * <li>{@link #P_SHORT_07_S0_C} (<code>0010011001011001</code>)</li>
     * <li>{@link #P_SHORT_07_S0_FX} (<code>0110011001001010</code>)</li>
     * <li>{@link #P_SHORT_07_S1_GX} (<code>1001001010101010</code>)</li>
     * <li>{@link #P_SHORT_11_S0_A} (<code>0011101110110111</code>)</li>
     * <li>{@link #P_SHORT_11_S0_C} (<code>0101110111010111</code>)</li>
     * <li>{@link #P_SHORT_11_S0_F} (<code>0110110111011011</code>)</li>
     * <li>{@link #P_SHORT_11_S0_G} (<code>0110111001110111</code>)</li>
     * <li>{@link #P_SHORT_11_S0_H} (<code>0111010101110111</code>)</li>
     * <li>{@link #P_SHORT_11_S0_I} (<code>0111010110111011</code>)</li>
     * <li>{@link #P_SHORT_11_S1_CX} (<code>1011101110101110</code>)</li>
     * <li>{@link #P_SHORT_11_S1_DX} (<code>1011101110111010</code>)</li>
     * <li>{@link #P_SHORT_11_S1_FX} (<code>1101101110110110</code>)</li>
     * <li>{@link #P_SHORT_11_S1_GX} (<code>1101110011101110</code>)</li>
     * <li>{@link #P_SHORT_11_S1_HX} (<code>1110101011101110</code>)</li>
     * <li>{@link #P_SHORT_11_S1_IX} (<code>1110101101110110</code>)</li>
     * <li>{@link #P_SHORT_11_S1_JX} (<code>1110110101101110</code>)</li>
     * </ul>
     */
    public static final short P_SHORT_07_S1_HX = (short) -23402;

    /**
     * <code>1001001010011110</code>
     * <p/>
     * Other patterns with half bit-flip-distance (8):
     * <ul>
     * <li>{@link #P_SHORT_08_S0_BX} (<code>0101101101010010</code>)</li>
     * <li>{@link #P_SHORT_08_S0_C} (<code>0100010011011011</code>)</li>
     * <li>{@link #P_SHORT_08_S1_EX} (<code>1100011011100010</code>)</li>
     * <li>{@link #P_SHORT_08_S1_F} (<code>1000100101010111</code>)</li>
     * <li>{@link #P_SHORT_08_S1_G} (<code>1001010100111001</code>)</li>
     * <li>{@link #P_SHORT_08_S1_I} (<code>1100101001010101</code>)</li>
     * </ul>
     */
    public static final short P_SHORT_07_S1_I = (short) -28002;

    /**
     * <code>1011001001001001</code>
     * <p/>
     * Other patterns with half bit-flip-distance (8):
     * <ul>
     * <li>{@link #P_SHORT_05_S0_A} (<code>0001000100010101</code>)</li>
     * <li>{@link #P_SHORT_05_S0_CX} (<code>0010010001000110</code>)</li>
     * <li>{@link #P_SHORT_05_S0_DX} (<code>0010010001010010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_EX} (<code>0010010001100010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_FX} (<code>0010100010001010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_G} (<code>0001010100010001</code>)</li>
     * <li>{@link #P_SHORT_05_S0_GX} (<code>0010101000100010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_HX} (<code>0100010001001010</code>)</li>
     * <li>{@link #P_SHORT_07_S0_A} (<code>0010010010011011</code>)</li>
     * <li>{@link #P_SHORT_07_S0_BX} (<code>0100101001001110</code>)</li>
     * <li>{@link #P_SHORT_07_S0_D} (<code>0010100101100101</code>)</li>
     * <li>{@link #P_SHORT_11_S0_AX} (<code>0111011101101110</code>)</li>
     * <li>{@link #P_SHORT_11_S1_BX} (<code>1010111011101110</code>)</li>
     * <li>{@link #P_SHORT_11_S1_CX} (<code>1011101110101110</code>)</li>
     * <li>{@link #P_SHORT_11_S1_DX} (<code>1011101110111010</code>)</li>
     * <li>{@link #P_SHORT_11_S1_EX} (<code>1101101101101110</code>)</li>
     * <li>{@link #P_SHORT_11_S1_HX} (<code>1110101011101110</code>)</li>
     * </ul>
     */
    public static final short P_SHORT_07_S1_J = (short) -19895;

    // Short patterns with 8 bits set

    /**
     * <code>0010001010110111</code>
     * <p/>
     * Other patterns with half bit-flip-distance (8):
     * <ul>
     * <li>{@link #P_SHORT_08_S0_B} (<code>0010110110101001</code>)</li>
     * <li>{@link #P_SHORT_08_S0_C} (<code>0100010011011011</code>)</li>
     * <li>{@link #P_SHORT_08_S0_D} (<code>0101001100011101</code>)</li>
     * <li>{@link #P_SHORT_08_S1_EX} (<code>1100011011100010</code>)</li>
     * <li>{@link #P_SHORT_08_S1_F} (<code>1000100101010111</code>)</li>
     * <li>{@link #P_SHORT_08_S1_I} (<code>1100101001010101</code>)</li>
     * </ul>
     */
    public static final short P_SHORT_08_S0_A = (short) 8887;

    /**
     * <code>0100010101101110</code>
     * <p/>
     * Other patterns with half bit-flip-distance (8):
     * <ul>
     * <li>{@link #P_SHORT_08_S0_B} (<code>0010110110101001</code>)</li>
     * <li>{@link #P_SHORT_08_S0_BX} (<code>0101101101010010</code>)</li>
     * <li>{@link #P_SHORT_08_S0_D} (<code>0101001100011101</code>)</li>
     * <li>{@link #P_SHORT_08_S0_E} (<code>0110001101110001</code>)</li>
     * <li>{@link #P_SHORT_08_S1_CX} (<code>1000100110110110</code>)</li>
     * <li>{@link #P_SHORT_08_S1_DX} (<code>1010011000111010</code>)</li>
     * <li>{@link #P_SHORT_08_S1_F} (<code>1000100101010111</code>)</li>
     * <li>{@link #P_SHORT_08_S1_G} (<code>1001010100111001</code>)</li>
     * </ul>
     */
    public static final short P_SHORT_08_S0_AX = (short) 17774;

    /**
     * <code>0010110110101001</code>
     * <p/>
     * Other patterns with half bit-flip-distance (8):
     * <ul>
     * <li>{@link #P_SHORT_08_S0_A} (<code>0010001010110111</code>)</li>
     * <li>{@link #P_SHORT_08_S0_AX} (<code>0100010101101110</code>)</li>
     * <li>{@link #P_SHORT_08_S0_C} (<code>0100010011011011</code>)</li>
     * <li>{@link #P_SHORT_08_S0_E} (<code>0110001101110001</code>)</li>
     * <li>{@link #P_SHORT_08_S1_CX} (<code>1000100110110110</code>)</li>
     * <li>{@link #P_SHORT_08_S1_DX} (<code>1010011000111010</code>)</li>
     * </ul>
     */
    public static final short P_SHORT_08_S0_B = (short) 11689;

    /**
     * <code>0101101101010010</code>
     * <p/>
     * Other patterns with half bit-flip-distance (8):
     * <ul>
     * <li>{@link #P_SHORT_07_S1_I} (<code>1001001010011110</code>)</li>
     * <li>{@link #P_SHORT_08_S0_AX} (<code>0100010101101110</code>)</li>
     * <li>{@link #P_SHORT_08_S0_C} (<code>0100010011011011</code>)</li>
     * <li>{@link #P_SHORT_08_S1_CX} (<code>1000100110110110</code>)</li>
     * <li>{@link #P_SHORT_08_S1_EX} (<code>1100011011100010</code>)</li>
     * <li>{@link #P_SHORT_08_S1_J} (<code>1110110100010001</code>)</li>
     * </ul>
     */
    public static final short P_SHORT_08_S0_BX = (short) 23378;

    /**
     * <code>0100010011011011</code>
     * <p/>
     * Other patterns with half bit-flip-distance (8):
     * <ul>
     * <li>{@link #P_SHORT_07_S1_I} (<code>1001001010011110</code>)</li>
     * <li>{@link #P_SHORT_08_S0_A} (<code>0010001010110111</code>)</li>
     * <li>{@link #P_SHORT_08_S0_B} (<code>0010110110101001</code>)</li>
     * <li>{@link #P_SHORT_08_S0_BX} (<code>0101101101010010</code>)</li>
     * <li>{@link #P_SHORT_08_S0_D} (<code>0101001100011101</code>)</li>
     * <li>{@link #P_SHORT_08_S0_E} (<code>0110001101110001</code>)</li>
     * <li>{@link #P_SHORT_08_S1_DX} (<code>1010011000111010</code>)</li>
     * <li>{@link #P_SHORT_08_S1_F} (<code>1000100101010111</code>)</li>
     * <li>{@link #P_SHORT_08_S1_G} (<code>1001010100111001</code>)</li>
     * <li>{@link #P_SHORT_08_S1_H} (<code>1010111001001001</code>)</li>
     * <li>{@link #P_SHORT_08_S1_I} (<code>1100101001010101</code>)</li>
     * <li>{@link #P_SHORT_08_S1_J} (<code>1110110100010001</code>)</li>
     * </ul>
     */
    public static final short P_SHORT_08_S0_C = (short) 17627;

    /**
     * <code>1000100110110110</code>
     * <p/>
     * Other patterns with half bit-flip-distance (8):
     * <ul>
     * <li>{@link #P_SHORT_08_S0_AX} (<code>0100010101101110</code>)</li>
     * <li>{@link #P_SHORT_08_S0_B} (<code>0010110110101001</code>)</li>
     * <li>{@link #P_SHORT_08_S0_BX} (<code>0101101101010010</code>)</li>
     * <li>{@link #P_SHORT_08_S1_DX} (<code>1010011000111010</code>)</li>
     * <li>{@link #P_SHORT_08_S1_EX} (<code>1100011011100010</code>)</li>
     * <li>{@link #P_SHORT_08_S1_G} (<code>1001010100111001</code>)</li>
     * <li>{@link #P_SHORT_08_S1_I} (<code>1100101001010101</code>)</li>
     * <li>{@link #P_SHORT_08_S1_J} (<code>1110110100010001</code>)</li>
     * </ul>
     */
    public static final short P_SHORT_08_S1_CX = (short) -30282;

    /**
     * <code>0101001100011101</code>
     * <p/>
     * Other patterns with half bit-flip-distance (8):
     * <ul>
     * <li>{@link #P_SHORT_08_S0_A} (<code>0010001010110111</code>)</li>
     * <li>{@link #P_SHORT_08_S0_AX} (<code>0100010101101110</code>)</li>
     * <li>{@link #P_SHORT_08_S0_C} (<code>0100010011011011</code>)</li>
     * <li>{@link #P_SHORT_08_S1_F} (<code>1000100101010111</code>)</li>
     * <li>{@link #P_SHORT_08_S1_J} (<code>1110110100010001</code>)</li>
     * </ul>
     */
    public static final short P_SHORT_08_S0_D = (short) 21277;

    /**
     * <code>1010011000111010</code>
     * <p/>
     * Other patterns with half bit-flip-distance (8):
     * <ul>
     * <li>{@link #P_SHORT_08_S0_AX} (<code>0100010101101110</code>)</li>
     * <li>{@link #P_SHORT_08_S0_B} (<code>0010110110101001</code>)</li>
     * <li>{@link #P_SHORT_08_S0_C} (<code>0100010011011011</code>)</li>
     * <li>{@link #P_SHORT_08_S0_E} (<code>0110001101110001</code>)</li>
     * <li>{@link #P_SHORT_08_S1_CX} (<code>1000100110110110</code>)</li>
     * <li>{@link #P_SHORT_08_S1_J} (<code>1110110100010001</code>)</li>
     * </ul>
     */
    public static final short P_SHORT_08_S1_DX = (short) -22982;

    /**
     * <code>0110001101110001</code>
     * <p/>
     * Other patterns with half bit-flip-distance (8):
     * <ul>
     * <li>{@link #P_SHORT_08_S0_AX} (<code>0100010101101110</code>)</li>
     * <li>{@link #P_SHORT_08_S0_B} (<code>0010110110101001</code>)</li>
     * <li>{@link #P_SHORT_08_S0_C} (<code>0100010011011011</code>)</li>
     * <li>{@link #P_SHORT_08_S1_DX} (<code>1010011000111010</code>)</li>
     * <li>{@link #P_SHORT_08_S1_EX} (<code>1100011011100010</code>)</li>
     * <li>{@link #P_SHORT_08_S1_F} (<code>1000100101010111</code>)</li>
     * <li>{@link #P_SHORT_08_S1_G} (<code>1001010100111001</code>)</li>
     * <li>{@link #P_SHORT_08_S1_H} (<code>1010111001001001</code>)</li>
     * </ul>
     */
    public static final short P_SHORT_08_S0_E = (short) 25457;

    /**
     * <code>1100011011100010</code>
     * <p/>
     * Other patterns with half bit-flip-distance (8):
     * <ul>
     * <li>{@link #P_SHORT_07_S1_I} (<code>1001001010011110</code>)</li>
     * <li>{@link #P_SHORT_08_S0_A} (<code>0010001010110111</code>)</li>
     * <li>{@link #P_SHORT_08_S0_BX} (<code>0101101101010010</code>)</li>
     * <li>{@link #P_SHORT_08_S0_E} (<code>0110001101110001</code>)</li>
     * <li>{@link #P_SHORT_08_S1_CX} (<code>1000100110110110</code>)</li>
     * <li>{@link #P_SHORT_08_S1_H} (<code>1010111001001001</code>)</li>
     * <li>{@link #P_SHORT_08_S1_I} (<code>1100101001010101</code>)</li>
     * </ul>
     */
    public static final short P_SHORT_08_S1_EX = (short) -14622;

    /**
     * <code>1000100101010111</code>
     * <p/>
     * Other patterns with half bit-flip-distance (8):
     * <ul>
     * <li>{@link #P_SHORT_07_S1_I} (<code>1001001010011110</code>)</li>
     * <li>{@link #P_SHORT_08_S0_A} (<code>0010001010110111</code>)</li>
     * <li>{@link #P_SHORT_08_S0_AX} (<code>0100010101101110</code>)</li>
     * <li>{@link #P_SHORT_08_S0_C} (<code>0100010011011011</code>)</li>
     * <li>{@link #P_SHORT_08_S0_D} (<code>0101001100011101</code>)</li>
     * <li>{@link #P_SHORT_08_S0_E} (<code>0110001101110001</code>)</li>
     * <li>{@link #P_SHORT_08_S1_G} (<code>1001010100111001</code>)</li>
     * <li>{@link #P_SHORT_08_S1_H} (<code>1010111001001001</code>)</li>
     * </ul>
     */
    public static final short P_SHORT_08_S1_F = (short) -30377;

    /**
     * <code>1001010100111001</code>
     * <p/>
     * Other patterns with half bit-flip-distance (8):
     * <ul>
     * <li>{@link #P_SHORT_07_S1_I} (<code>1001001010011110</code>)</li>
     * <li>{@link #P_SHORT_08_S0_AX} (<code>0100010101101110</code>)</li>
     * <li>{@link #P_SHORT_08_S0_C} (<code>0100010011011011</code>)</li>
     * <li>{@link #P_SHORT_08_S0_E} (<code>0110001101110001</code>)</li>
     * <li>{@link #P_SHORT_08_S1_CX} (<code>1000100110110110</code>)</li>
     * <li>{@link #P_SHORT_08_S1_F} (<code>1000100101010111</code>)</li>
     * <li>{@link #P_SHORT_08_S1_H} (<code>1010111001001001</code>)</li>
     * </ul>
     */
    public static final short P_SHORT_08_S1_G = (short) -27335;

    /**
     * <code>1010111001001001</code>
     * <p/>
     * Other patterns with half bit-flip-distance (8):
     * <ul>
     * <li>{@link #P_SHORT_08_S0_C} (<code>0100010011011011</code>)</li>
     * <li>{@link #P_SHORT_08_S0_E} (<code>0110001101110001</code>)</li>
     * <li>{@link #P_SHORT_08_S1_EX} (<code>1100011011100010</code>)</li>
     * <li>{@link #P_SHORT_08_S1_F} (<code>1000100101010111</code>)</li>
     * <li>{@link #P_SHORT_08_S1_G} (<code>1001010100111001</code>)</li>
     * </ul>
     */
    public static final short P_SHORT_08_S1_H = (short) -20919;

    /**
     * <code>1100101001010101</code>
     * <p/>
     * Other patterns with half bit-flip-distance (8):
     * <ul>
     * <li>{@link #P_SHORT_07_S1_I} (<code>1001001010011110</code>)</li>
     * <li>{@link #P_SHORT_08_S0_A} (<code>0010001010110111</code>)</li>
     * <li>{@link #P_SHORT_08_S0_C} (<code>0100010011011011</code>)</li>
     * <li>{@link #P_SHORT_08_S1_CX} (<code>1000100110110110</code>)</li>
     * <li>{@link #P_SHORT_08_S1_EX} (<code>1100011011100010</code>)</li>
     * </ul>
     */
    public static final short P_SHORT_08_S1_I = (short) -13739;

    /**
     * <code>1110110100010001</code>
     * <p/>
     * Other patterns with half bit-flip-distance (8):
     * <ul>
     * <li>{@link #P_SHORT_08_S0_BX} (<code>0101101101010010</code>)</li>
     * <li>{@link #P_SHORT_08_S0_C} (<code>0100010011011011</code>)</li>
     * <li>{@link #P_SHORT_08_S0_D} (<code>0101001100011101</code>)</li>
     * <li>{@link #P_SHORT_08_S1_CX} (<code>1000100110110110</code>)</li>
     * <li>{@link #P_SHORT_08_S1_DX} (<code>1010011000111010</code>)</li>
     * </ul>
     */
    public static final short P_SHORT_08_S1_J = (short) -4847;

    // Short patterns with 11 bits set

    /**
     * <code>0011101110110111</code>
     * <p/>
     * Other patterns with half bit-flip-distance (8):
     * <ul>
     * <li>{@link #P_SHORT_05_S0_AX} (<code>0010001000101010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_B} (<code>0001000101000101</code>)</li>
     * <li>{@link #P_SHORT_05_S0_BX} (<code>0010001010001010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_D} (<code>0001001000101001</code>)</li>
     * <li>{@link #P_SHORT_05_S0_FX} (<code>0010100010001010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_G} (<code>0001010100010001</code>)</li>
     * <li>{@link #P_SHORT_07_S0_A} (<code>0010010010011011</code>)</li>
     * <li>{@link #P_SHORT_07_S0_CX} (<code>0100110010110010</code>)</li>
     * <li>{@link #P_SHORT_07_S0_G} (<code>0100100101010101</code>)</li>
     * <li>{@link #P_SHORT_07_S1_GX} (<code>1001001010101010</code>)</li>
     * <li>{@link #P_SHORT_07_S1_HX} (<code>1010010010010110</code>)</li>
     * <li>{@link #P_SHORT_11_S0_AX} (<code>0111011101101110</code>)</li>
     * <li>{@link #P_SHORT_11_S0_D} (<code>0101110111011101</code>)</li>
     * <li>{@link #P_SHORT_11_S0_F} (<code>0110110111011011</code>)</li>
     * <li>{@link #P_SHORT_11_S1_BX} (<code>1010111011101110</code>)</li>
     * <li>{@link #P_SHORT_11_S1_EX} (<code>1101101101101110</code>)</li>
     * <li>{@link #P_SHORT_11_S1_HX} (<code>1110101011101110</code>)</li>
     * </ul>
     */
    public static final short P_SHORT_11_S0_A = (short) 15287;

    /**
     * <code>0111011101101110</code>
     * <p/>
     * Other patterns with half bit-flip-distance (8):
     * <ul>
     * <li>{@link #P_SHORT_05_S0_B} (<code>0001000101000101</code>)</li>
     * <li>{@link #P_SHORT_05_S0_BX} (<code>0010001010001010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_C} (<code>0001001000100011</code>)</li>
     * <li>{@link #P_SHORT_05_S0_D} (<code>0001001000101001</code>)</li>
     * <li>{@link #P_SHORT_05_S0_DX} (<code>0010010001010010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_F} (<code>0001010001000101</code>)</li>
     * <li>{@link #P_SHORT_05_S0_GX} (<code>0010101000100010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_H} (<code>0010001000100101</code>)</li>
     * <li>{@link #P_SHORT_07_S0_AX} (<code>0100100100110110</code>)</li>
     * <li>{@link #P_SHORT_07_S0_C} (<code>0010011001011001</code>)</li>
     * <li>{@link #P_SHORT_07_S0_D} (<code>0010100101100101</code>)</li>
     * <li>{@link #P_SHORT_07_S1_GX} (<code>1001001010101010</code>)</li>
     * <li>{@link #P_SHORT_07_S1_J} (<code>1011001001001001</code>)</li>
     * <li>{@link #P_SHORT_11_S0_A} (<code>0011101110110111</code>)</li>
     * <li>{@link #P_SHORT_11_S0_C} (<code>0101110111010111</code>)</li>
     * <li>{@link #P_SHORT_11_S0_D} (<code>0101110111011101</code>)</li>
     * <li>{@link #P_SHORT_11_S0_E} (<code>0110110110110111</code>)</li>
     * <li>{@link #P_SHORT_11_S0_F} (<code>0110110111011011</code>)</li>
     * <li>{@link #P_SHORT_11_S1_DX} (<code>1011101110111010</code>)</li>
     * <li>{@link #P_SHORT_11_S1_FX} (<code>1101101110110110</code>)</li>
     * </ul>
     */
    public static final short P_SHORT_11_S0_AX = (short) 30574;

    /**
     * <code>0101011101110111</code>
     * <p/>
     * Other patterns with half bit-flip-distance (8):
     * <ul>
     * <li>{@link #P_SHORT_05_S0_CX} (<code>0010010001000110</code>)</li>
     * <li>{@link #P_SHORT_05_S0_D} (<code>0001001000101001</code>)</li>
     * <li>{@link #P_SHORT_05_S0_DX} (<code>0010010001010010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_EX} (<code>0010010001100010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_H} (<code>0010001000100101</code>)</li>
     * <li>{@link #P_SHORT_05_S0_HX} (<code>0100010001001010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_I} (<code>0010001000110001</code>)</li>
     * <li>{@link #P_SHORT_07_S0_BX} (<code>0100101001001110</code>)</li>
     * <li>{@link #P_SHORT_07_S0_C} (<code>0010011001011001</code>)</li>
     * <li>{@link #P_SHORT_07_S0_CX} (<code>0100110010110010</code>)</li>
     * <li>{@link #P_SHORT_07_S0_D} (<code>0010100101100101</code>)</li>
     * <li>{@link #P_SHORT_07_S0_DX} (<code>0101001011001010</code>)</li>
     * <li>{@link #P_SHORT_07_S0_FX} (<code>0110011001001010</code>)</li>
     * <li>{@link #P_SHORT_11_S0_F} (<code>0110110111011011</code>)</li>
     * <li>{@link #P_SHORT_11_S1_GX} (<code>1101110011101110</code>)</li>
     * <li>{@link #P_SHORT_11_S1_JX} (<code>1110110101101110</code>)</li>
     * </ul>
     */
    public static final short P_SHORT_11_S0_B = (short) 22391;

    /**
     * <code>1010111011101110</code>
     * <p/>
     * Other patterns with half bit-flip-distance (8):
     * <ul>
     * <li>{@link #P_SHORT_05_S0_DX} (<code>0010010001010010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_H} (<code>0010001000100101</code>)</li>
     * <li>{@link #P_SHORT_05_S0_HX} (<code>0100010001001010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_IX} (<code>0100010001100010</code>)</li>
     * <li>{@link #P_SHORT_07_S0_A} (<code>0010010010011011</code>)</li>
     * <li>{@link #P_SHORT_07_S0_B} (<code>0010010100100111</code>)</li>
     * <li>{@link #P_SHORT_07_S0_C} (<code>0010011001011001</code>)</li>
     * <li>{@link #P_SHORT_07_S0_CX} (<code>0100110010110010</code>)</li>
     * <li>{@link #P_SHORT_07_S0_D} (<code>0010100101100101</code>)</li>
     * <li>{@link #P_SHORT_07_S0_DX} (<code>0101001011001010</code>)</li>
     * <li>{@link #P_SHORT_07_S0_E} (<code>0010110010010011</code>)</li>
     * <li>{@link #P_SHORT_07_S1_J} (<code>1011001001001001</code>)</li>
     * <li>{@link #P_SHORT_11_S0_A} (<code>0011101110110111</code>)</li>
     * <li>{@link #P_SHORT_11_S0_E} (<code>0110110110110111</code>)</li>
     * <li>{@link #P_SHORT_11_S0_F} (<code>0110110111011011</code>)</li>
     * <li>{@link #P_SHORT_11_S0_J} (<code>0111011010110111</code>)</li>
     * <li>{@link #P_SHORT_11_S1_FX} (<code>1101101110110110</code>)</li>
     * </ul>
     */
    public static final short P_SHORT_11_S1_BX = (short) -20754;

    /**
     * <code>0101110111010111</code>
     * <p/>
     * Other patterns with half bit-flip-distance (8):
     * <ul>
     * <li>{@link #P_SHORT_05_S0_CX} (<code>0010010001000110</code>)</li>
     * <li>{@link #P_SHORT_05_S0_DX} (<code>0010010001010010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_HX} (<code>0100010001001010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_IX} (<code>0100010001100010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_JX} (<code>0100010100100010</code>)</li>
     * <li>{@link #P_SHORT_07_S0_A} (<code>0010010010011011</code>)</li>
     * <li>{@link #P_SHORT_07_S0_B} (<code>0010010100100111</code>)</li>
     * <li>{@link #P_SHORT_07_S0_BX} (<code>0100101001001110</code>)</li>
     * <li>{@link #P_SHORT_07_S0_D} (<code>0010100101100101</code>)</li>
     * <li>{@link #P_SHORT_07_S0_DX} (<code>0101001011001010</code>)</li>
     * <li>{@link #P_SHORT_07_S0_H} (<code>0101001001001011</code>)</li>
     * <li>{@link #P_SHORT_07_S1_HX} (<code>1010010010010110</code>)</li>
     * <li>{@link #P_SHORT_11_S0_AX} (<code>0111011101101110</code>)</li>
     * <li>{@link #P_SHORT_11_S1_EX} (<code>1101101101101110</code>)</li>
     * <li>{@link #P_SHORT_11_S1_IX} (<code>1110101101110110</code>)</li>
     * <li>{@link #P_SHORT_11_S1_JX} (<code>1110110101101110</code>)</li>
     * </ul>
     */
    public static final short P_SHORT_11_S0_C = (short) 24023;

    /**
     * <code>1011101110101110</code>
     * <p/>
     * Other patterns with half bit-flip-distance (8):
     * <ul>
     * <li>{@link #P_SHORT_05_S0_C} (<code>0001001000100011</code>)</li>
     * <li>{@link #P_SHORT_05_S0_D} (<code>0001001000101001</code>)</li>
     * <li>{@link #P_SHORT_05_S0_H} (<code>0010001000100101</code>)</li>
     * <li>{@link #P_SHORT_07_S0_AX} (<code>0100100100110110</code>)</li>
     * <li>{@link #P_SHORT_07_S0_B} (<code>0010010100100111</code>)</li>
     * <li>{@link #P_SHORT_07_S0_BX} (<code>0100101001001110</code>)</li>
     * <li>{@link #P_SHORT_07_S0_D} (<code>0010100101100101</code>)</li>
     * <li>{@link #P_SHORT_07_S0_DX} (<code>0101001011001010</code>)</li>
     * <li>{@link #P_SHORT_07_S1_HX} (<code>1010010010010110</code>)</li>
     * <li>{@link #P_SHORT_07_S1_J} (<code>1011001001001001</code>)</li>
     * <li>{@link #P_SHORT_11_S0_E} (<code>0110110110110111</code>)</li>
     * <li>{@link #P_SHORT_11_S0_I} (<code>0111010110111011</code>)</li>
     * <li>{@link #P_SHORT_11_S0_J} (<code>0111011010110111</code>)</li>
     * </ul>
     */
    public static final short P_SHORT_11_S1_CX = (short) -17490;

    /**
     * <code>0101110111011101</code>
     * <p/>
     * Other patterns with half bit-flip-distance (8):
     * <ul>
     * <li>{@link #P_SHORT_05_S0_HX} (<code>0100010001001010</code>)</li>
     * <li>{@link #P_SHORT_07_S0_A} (<code>0010010010011011</code>)</li>
     * <li>{@link #P_SHORT_07_S0_AX} (<code>0100100100110110</code>)</li>
     * <li>{@link #P_SHORT_07_S0_BX} (<code>0100101001001110</code>)</li>
     * <li>{@link #P_SHORT_07_S0_C} (<code>0010011001011001</code>)</li>
     * <li>{@link #P_SHORT_07_S0_CX} (<code>0100110010110010</code>)</li>
     * <li>{@link #P_SHORT_07_S0_D} (<code>0010100101100101</code>)</li>
     * <li>{@link #P_SHORT_07_S0_DX} (<code>0101001011001010</code>)</li>
     * <li>{@link #P_SHORT_07_S0_E} (<code>0010110010010011</code>)</li>
     * <li>{@link #P_SHORT_07_S0_EX} (<code>0101100100100110</code>)</li>
     * <li>{@link #P_SHORT_07_S0_H} (<code>0101001001001011</code>)</li>
     * <li>{@link #P_SHORT_11_S0_A} (<code>0011101110110111</code>)</li>
     * <li>{@link #P_SHORT_11_S0_AX} (<code>0111011101101110</code>)</li>
     * <li>{@link #P_SHORT_11_S0_G} (<code>0110111001110111</code>)</li>
     * <li>{@link #P_SHORT_11_S0_J} (<code>0111011010110111</code>)</li>
     * <li>{@link #P_SHORT_11_S1_EX} (<code>1101101101101110</code>)</li>
     * <li>{@link #P_SHORT_11_S1_FX} (<code>1101101110110110</code>)</li>
     * <li>{@link #P_SHORT_11_S1_JX} (<code>1110110101101110</code>)</li>
     * </ul>
     */
    public static final short P_SHORT_11_S0_D = (short) 24029;

    /**
     * <code>1011101110111010</code>
     * <p/>
     * Other patterns with half bit-flip-distance (8):
     * <ul>
     * <li>{@link #P_SHORT_05_S0_C} (<code>0001001000100011</code>)</li>
     * <li>{@link #P_SHORT_05_S0_D} (<code>0001001000101001</code>)</li>
     * <li>{@link #P_SHORT_05_S0_E} (<code>0001001000110001</code>)</li>
     * <li>{@link #P_SHORT_05_S0_I} (<code>0010001000110001</code>)</li>
     * <li>{@link #P_SHORT_05_S0_J} (<code>0010001010010001</code>)</li>
     * <li>{@link #P_SHORT_07_S0_A} (<code>0010010010011011</code>)</li>
     * <li>{@link #P_SHORT_07_S0_AX} (<code>0100100100110110</code>)</li>
     * <li>{@link #P_SHORT_07_S0_CX} (<code>0100110010110010</code>)</li>
     * <li>{@link #P_SHORT_07_S0_DX} (<code>0101001011001010</code>)</li>
     * <li>{@link #P_SHORT_07_S0_E} (<code>0010110010010011</code>)</li>
     * <li>{@link #P_SHORT_07_S0_EX} (<code>0101100100100110</code>)</li>
     * <li>{@link #P_SHORT_07_S0_F} (<code>0011001100100101</code>)</li>
     * <li>{@link #P_SHORT_07_S1_HX} (<code>1010010010010110</code>)</li>
     * <li>{@link #P_SHORT_07_S1_J} (<code>1011001001001001</code>)</li>
     * <li>{@link #P_SHORT_11_S0_AX} (<code>0111011101101110</code>)</li>
     * <li>{@link #P_SHORT_11_S0_E} (<code>0110110110110111</code>)</li>
     * <li>{@link #P_SHORT_11_S0_F} (<code>0110110111011011</code>)</li>
     * <li>{@link #P_SHORT_11_S0_J} (<code>0111011010110111</code>)</li>
     * <li>{@link #P_SHORT_11_S1_GX} (<code>1101110011101110</code>)</li>
     * <li>{@link #P_SHORT_11_S1_JX} (<code>1110110101101110</code>)</li>
     * </ul>
     */
    public static final short P_SHORT_11_S1_DX = (short) -17478;

    /**
     * <code>0110110110110111</code>
     * <p/>
     * Other patterns with half bit-flip-distance (8):
     * <ul>
     * <li>{@link #P_SHORT_05_S0_A} (<code>0001000100010101</code>)</li>
     * <li>{@link #P_SHORT_05_S0_CX} (<code>0010010001000110</code>)</li>
     * <li>{@link #P_SHORT_05_S0_DX} (<code>0010010001010010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_EX} (<code>0010010001100010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_FX} (<code>0010100010001010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_G} (<code>0001010100010001</code>)</li>
     * <li>{@link #P_SHORT_05_S0_GX} (<code>0010101000100010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_H} (<code>0010001000100101</code>)</li>
     * <li>{@link #P_SHORT_05_S0_I} (<code>0010001000110001</code>)</li>
     * <li>{@link #P_SHORT_05_S0_IX} (<code>0100010001100010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_J} (<code>0010001010010001</code>)</li>
     * <li>{@link #P_SHORT_07_S0_F} (<code>0011001100100101</code>)</li>
     * <li>{@link #P_SHORT_11_S0_AX} (<code>0111011101101110</code>)</li>
     * <li>{@link #P_SHORT_11_S1_BX} (<code>1010111011101110</code>)</li>
     * <li>{@link #P_SHORT_11_S1_CX} (<code>1011101110101110</code>)</li>
     * <li>{@link #P_SHORT_11_S1_DX} (<code>1011101110111010</code>)</li>
     * <li>{@link #P_SHORT_11_S1_GX} (<code>1101110011101110</code>)</li>
     * <li>{@link #P_SHORT_11_S1_HX} (<code>1110101011101110</code>)</li>
     * </ul>
     */
    public static final short P_SHORT_11_S0_E = (short) 28087;

    /**
     * <code>1101101101101110</code>
     * <p/>
     * Other patterns with half bit-flip-distance (8):
     * <ul>
     * <li>{@link #P_SHORT_05_S0_AX} (<code>0010001000101010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_B} (<code>0001000101000101</code>)</li>
     * <li>{@link #P_SHORT_05_S0_C} (<code>0001001000100011</code>)</li>
     * <li>{@link #P_SHORT_05_S0_D} (<code>0001001000101001</code>)</li>
     * <li>{@link #P_SHORT_05_S0_GX} (<code>0010101000100010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_HX} (<code>0100010001001010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_IX} (<code>0100010001100010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_JX} (<code>0100010100100010</code>)</li>
     * <li>{@link #P_SHORT_07_S0_D} (<code>0010100101100101</code>)</li>
     * <li>{@link #P_SHORT_07_S0_F} (<code>0011001100100101</code>)</li>
     * <li>{@link #P_SHORT_07_S0_FX} (<code>0110011001001010</code>)</li>
     * <li>{@link #P_SHORT_07_S0_G} (<code>0100100101010101</code>)</li>
     * <li>{@link #P_SHORT_07_S1_J} (<code>1011001001001001</code>)</li>
     * <li>{@link #P_SHORT_11_S0_A} (<code>0011101110110111</code>)</li>
     * <li>{@link #P_SHORT_11_S0_C} (<code>0101110111010111</code>)</li>
     * <li>{@link #P_SHORT_11_S0_D} (<code>0101110111011101</code>)</li>
     * <li>{@link #P_SHORT_11_S0_G} (<code>0110111001110111</code>)</li>
     * <li>{@link #P_SHORT_11_S0_H} (<code>0111010101110111</code>)</li>
     * </ul>
     */
    public static final short P_SHORT_11_S1_EX = (short) -9362;

    /**
     * <code>0110110111011011</code>
     * <p/>
     * Other patterns with half bit-flip-distance (8):
     * <ul>
     * <li>{@link #P_SHORT_05_S0_BX} (<code>0010001010001010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_CX} (<code>0010010001000110</code>)</li>
     * <li>{@link #P_SHORT_05_S0_EX} (<code>0010010001100010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_G} (<code>0001010100010001</code>)</li>
     * <li>{@link #P_SHORT_05_S0_IX} (<code>0100010001100010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_J} (<code>0010001010010001</code>)</li>
     * <li>{@link #P_SHORT_05_S0_JX} (<code>0100010100100010</code>)</li>
     * <li>{@link #P_SHORT_07_S0_AX} (<code>0100100100110110</code>)</li>
     * <li>{@link #P_SHORT_07_S0_B} (<code>0010010100100111</code>)</li>
     * <li>{@link #P_SHORT_07_S0_BX} (<code>0100101001001110</code>)</li>
     * <li>{@link #P_SHORT_07_S0_D} (<code>0010100101100101</code>)</li>
     * <li>{@link #P_SHORT_07_S0_DX} (<code>0101001011001010</code>)</li>
     * <li>{@link #P_SHORT_07_S0_H} (<code>0101001001001011</code>)</li>
     * <li>{@link #P_SHORT_07_S1_HX} (<code>1010010010010110</code>)</li>
     * <li>{@link #P_SHORT_11_S0_A} (<code>0011101110110111</code>)</li>
     * <li>{@link #P_SHORT_11_S0_AX} (<code>0111011101101110</code>)</li>
     * <li>{@link #P_SHORT_11_S0_B} (<code>0101011101110111</code>)</li>
     * <li>{@link #P_SHORT_11_S0_J} (<code>0111011010110111</code>)</li>
     * <li>{@link #P_SHORT_11_S1_BX} (<code>1010111011101110</code>)</li>
     * <li>{@link #P_SHORT_11_S1_DX} (<code>1011101110111010</code>)</li>
     * <li>{@link #P_SHORT_11_S1_GX} (<code>1101110011101110</code>)</li>
     * <li>{@link #P_SHORT_11_S1_HX} (<code>1110101011101110</code>)</li>
     * <li>{@link #P_SHORT_11_S1_IX} (<code>1110101101110110</code>)</li>
     * </ul>
     */
    public static final short P_SHORT_11_S0_F = (short) 28123;

    /**
     * <code>1101101110110110</code>
     * <p/>
     * Other patterns with half bit-flip-distance (8):
     * <ul>
     * <li>{@link #P_SHORT_05_S0_A} (<code>0001000100010101</code>)</li>
     * <li>{@link #P_SHORT_05_S0_C} (<code>0001001000100011</code>)</li>
     * <li>{@link #P_SHORT_05_S0_E} (<code>0001001000110001</code>)</li>
     * <li>{@link #P_SHORT_05_S0_GX} (<code>0010101000100010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_JX} (<code>0100010100100010</code>)</li>
     * <li>{@link #P_SHORT_07_S0_BX} (<code>0100101001001110</code>)</li>
     * <li>{@link #P_SHORT_07_S0_DX} (<code>0101001011001010</code>)</li>
     * <li>{@link #P_SHORT_07_S0_F} (<code>0011001100100101</code>)</li>
     * <li>{@link #P_SHORT_07_S0_G} (<code>0100100101010101</code>)</li>
     * <li>{@link #P_SHORT_07_S1_HX} (<code>1010010010010110</code>)</li>
     * <li>{@link #P_SHORT_11_S0_AX} (<code>0111011101101110</code>)</li>
     * <li>{@link #P_SHORT_11_S0_D} (<code>0101110111011101</code>)</li>
     * <li>{@link #P_SHORT_11_S0_G} (<code>0110111001110111</code>)</li>
     * <li>{@link #P_SHORT_11_S0_H} (<code>0111010101110111</code>)</li>
     * <li>{@link #P_SHORT_11_S0_I} (<code>0111010110111011</code>)</li>
     * <li>{@link #P_SHORT_11_S1_BX} (<code>1010111011101110</code>)</li>
     * <li>{@link #P_SHORT_11_S1_JX} (<code>1110110101101110</code>)</li>
     * </ul>
     */
    public static final short P_SHORT_11_S1_FX = (short) -9290;

    /**
     * <code>0110111001110111</code>
     * <p/>
     * Other patterns with half bit-flip-distance (8):
     * <ul>
     * <li>{@link #P_SHORT_05_S0_AX} (<code>0010001000101010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_C} (<code>0001001000100011</code>)</li>
     * <li>{@link #P_SHORT_05_S0_E} (<code>0001001000110001</code>)</li>
     * <li>{@link #P_SHORT_05_S0_F} (<code>0001010001000101</code>)</li>
     * <li>{@link #P_SHORT_05_S0_HX} (<code>0100010001001010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_J} (<code>0010001010010001</code>)</li>
     * <li>{@link #P_SHORT_05_S0_JX} (<code>0100010100100010</code>)</li>
     * <li>{@link #P_SHORT_07_S0_A} (<code>0010010010011011</code>)</li>
     * <li>{@link #P_SHORT_07_S0_EX} (<code>0101100100100110</code>)</li>
     * <li>{@link #P_SHORT_07_S0_F} (<code>0011001100100101</code>)</li>
     * <li>{@link #P_SHORT_07_S0_H} (<code>0101001001001011</code>)</li>
     * <li>{@link #P_SHORT_07_S1_HX} (<code>1010010010010110</code>)</li>
     * <li>{@link #P_SHORT_11_S0_D} (<code>0101110111011101</code>)</li>
     * <li>{@link #P_SHORT_11_S0_I} (<code>0111010110111011</code>)</li>
     * <li>{@link #P_SHORT_11_S1_EX} (<code>1101101101101110</code>)</li>
     * <li>{@link #P_SHORT_11_S1_FX} (<code>1101101110110110</code>)</li>
     * <li>{@link #P_SHORT_11_S1_GX} (<code>1101110011101110</code>)</li>
     * </ul>
     */
    public static final short P_SHORT_11_S0_G = (short) 28279;

    /**
     * <code>1101110011101110</code>
     * <p/>
     * Other patterns with half bit-flip-distance (8):
     * <ul>
     * <li>{@link #P_SHORT_05_S0_CX} (<code>0010010001000110</code>)</li>
     * <li>{@link #P_SHORT_05_S0_EX} (<code>0010010001100010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_F} (<code>0001010001000101</code>)</li>
     * <li>{@link #P_SHORT_05_S0_FX} (<code>0010100010001010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_JX} (<code>0100010100100010</code>)</li>
     * <li>{@link #P_SHORT_07_S0_AX} (<code>0100100100110110</code>)</li>
     * <li>{@link #P_SHORT_07_S0_FX} (<code>0110011001001010</code>)</li>
     * <li>{@link #P_SHORT_07_S0_H} (<code>0101001001001011</code>)</li>
     * <li>{@link #P_SHORT_07_S1_HX} (<code>1010010010010110</code>)</li>
     * <li>{@link #P_SHORT_11_S0_B} (<code>0101011101110111</code>)</li>
     * <li>{@link #P_SHORT_11_S0_E} (<code>0110110110110111</code>)</li>
     * <li>{@link #P_SHORT_11_S0_F} (<code>0110110111011011</code>)</li>
     * <li>{@link #P_SHORT_11_S0_G} (<code>0110111001110111</code>)</li>
     * <li>{@link #P_SHORT_11_S0_H} (<code>0111010101110111</code>)</li>
     * <li>{@link #P_SHORT_11_S0_I} (<code>0111010110111011</code>)</li>
     * <li>{@link #P_SHORT_11_S0_J} (<code>0111011010110111</code>)</li>
     * <li>{@link #P_SHORT_11_S1_DX} (<code>1011101110111010</code>)</li>
     * <li>{@link #P_SHORT_11_S1_IX} (<code>1110101101110110</code>)</li>
     * </ul>
     */
    public static final short P_SHORT_11_S1_GX = (short) -8978;

    /**
     * <code>0111010101110111</code>
     * <p/>
     * Other patterns with half bit-flip-distance (8):
     * <ul>
     * <li>{@link #P_SHORT_05_S0_C} (<code>0001001000100011</code>)</li>
     * <li>{@link #P_SHORT_05_S0_E} (<code>0001001000110001</code>)</li>
     * <li>{@link #P_SHORT_05_S0_H} (<code>0010001000100101</code>)</li>
     * <li>{@link #P_SHORT_05_S0_HX} (<code>0100010001001010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_I} (<code>0010001000110001</code>)</li>
     * <li>{@link #P_SHORT_07_S0_A} (<code>0010010010011011</code>)</li>
     * <li>{@link #P_SHORT_07_S0_C} (<code>0010011001011001</code>)</li>
     * <li>{@link #P_SHORT_07_S0_CX} (<code>0100110010110010</code>)</li>
     * <li>{@link #P_SHORT_07_S0_E} (<code>0010110010010011</code>)</li>
     * <li>{@link #P_SHORT_07_S0_FX} (<code>0110011001001010</code>)</li>
     * <li>{@link #P_SHORT_07_S0_H} (<code>0101001001001011</code>)</li>
     * <li>{@link #P_SHORT_07_S1_HX} (<code>1010010010010110</code>)</li>
     * <li>{@link #P_SHORT_11_S1_EX} (<code>1101101101101110</code>)</li>
     * <li>{@link #P_SHORT_11_S1_FX} (<code>1101101110110110</code>)</li>
     * <li>{@link #P_SHORT_11_S1_GX} (<code>1101110011101110</code>)</li>
     * </ul>
     */
    public static final short P_SHORT_11_S0_H = (short) 30071;

    /**
     * <code>1110101011101110</code>
     * <p/>
     * Other patterns with half bit-flip-distance (8):
     * <ul>
     * <li>{@link #P_SHORT_05_S0_CX} (<code>0010010001000110</code>)</li>
     * <li>{@link #P_SHORT_05_S0_EX} (<code>0010010001100010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_H} (<code>0010001000100101</code>)</li>
     * <li>{@link #P_SHORT_05_S0_HX} (<code>0100010001001010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_IX} (<code>0100010001100010</code>)</li>
     * <li>{@link #P_SHORT_07_S0_AX} (<code>0100100100110110</code>)</li>
     * <li>{@link #P_SHORT_07_S0_CX} (<code>0100110010110010</code>)</li>
     * <li>{@link #P_SHORT_07_S0_D} (<code>0010100101100101</code>)</li>
     * <li>{@link #P_SHORT_07_S0_EX} (<code>0101100100100110</code>)</li>
     * <li>{@link #P_SHORT_07_S0_H} (<code>0101001001001011</code>)</li>
     * <li>{@link #P_SHORT_07_S1_HX} (<code>1010010010010110</code>)</li>
     * <li>{@link #P_SHORT_07_S1_J} (<code>1011001001001001</code>)</li>
     * <li>{@link #P_SHORT_11_S0_A} (<code>0011101110110111</code>)</li>
     * <li>{@link #P_SHORT_11_S0_E} (<code>0110110110110111</code>)</li>
     * <li>{@link #P_SHORT_11_S0_F} (<code>0110110111011011</code>)</li>
     * <li>{@link #P_SHORT_11_S0_J} (<code>0111011010110111</code>)</li>
     * </ul>
     */
    public static final short P_SHORT_11_S1_HX = (short) -5394;

    /**
     * <code>0111010110111011</code>
     * <p/>
     * Other patterns with half bit-flip-distance (8):
     * <ul>
     * <li>{@link #P_SHORT_05_S0_A} (<code>0001000100010101</code>)</li>
     * <li>{@link #P_SHORT_05_S0_AX} (<code>0010001000101010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_BX} (<code>0010001010001010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_C} (<code>0001001000100011</code>)</li>
     * <li>{@link #P_SHORT_05_S0_D} (<code>0001001000101001</code>)</li>
     * <li>{@link #P_SHORT_05_S0_DX} (<code>0010010001010010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_E} (<code>0001001000110001</code>)</li>
     * <li>{@link #P_SHORT_05_S0_EX} (<code>0010010001100010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_FX} (<code>0010100010001010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_HX} (<code>0100010001001010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_I} (<code>0010001000110001</code>)</li>
     * <li>{@link #P_SHORT_05_S0_IX} (<code>0100010001100010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_J} (<code>0010001010010001</code>)</li>
     * <li>{@link #P_SHORT_07_S0_AX} (<code>0100100100110110</code>)</li>
     * <li>{@link #P_SHORT_07_S0_C} (<code>0010011001011001</code>)</li>
     * <li>{@link #P_SHORT_07_S0_DX} (<code>0101001011001010</code>)</li>
     * <li>{@link #P_SHORT_07_S0_EX} (<code>0101100100100110</code>)</li>
     * <li>{@link #P_SHORT_07_S0_F} (<code>0011001100100101</code>)</li>
     * <li>{@link #P_SHORT_07_S0_FX} (<code>0110011001001010</code>)</li>
     * <li>{@link #P_SHORT_07_S0_H} (<code>0101001001001011</code>)</li>
     * <li>{@link #P_SHORT_07_S1_GX} (<code>1001001010101010</code>)</li>
     * <li>{@link #P_SHORT_07_S1_HX} (<code>1010010010010110</code>)</li>
     * <li>{@link #P_SHORT_11_S0_G} (<code>0110111001110111</code>)</li>
     * <li>{@link #P_SHORT_11_S1_CX} (<code>1011101110101110</code>)</li>
     * <li>{@link #P_SHORT_11_S1_FX} (<code>1101101110110110</code>)</li>
     * <li>{@link #P_SHORT_11_S1_GX} (<code>1101110011101110</code>)</li>
     * <li>{@link #P_SHORT_11_S1_JX} (<code>1110110101101110</code>)</li>
     * </ul>
     */
    public static final short P_SHORT_11_S0_I = (short) 30139;

    /**
     * <code>1110101101110110</code>
     * <p/>
     * Other patterns with half bit-flip-distance (8):
     * <ul>
     * <li>{@link #P_SHORT_05_S0_AX} (<code>0010001000101010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_CX} (<code>0010010001000110</code>)</li>
     * <li>{@link #P_SHORT_05_S0_DX} (<code>0010010001010010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_EX} (<code>0010010001100010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_H} (<code>0010001000100101</code>)</li>
     * <li>{@link #P_SHORT_05_S0_I} (<code>0010001000110001</code>)</li>
     * <li>{@link #P_SHORT_05_S0_IX} (<code>0100010001100010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_JX} (<code>0100010100100010</code>)</li>
     * <li>{@link #P_SHORT_07_S0_B} (<code>0010010100100111</code>)</li>
     * <li>{@link #P_SHORT_07_S0_CX} (<code>0100110010110010</code>)</li>
     * <li>{@link #P_SHORT_07_S0_F} (<code>0011001100100101</code>)</li>
     * <li>{@link #P_SHORT_07_S0_FX} (<code>0110011001001010</code>)</li>
     * <li>{@link #P_SHORT_07_S1_HX} (<code>1010010010010110</code>)</li>
     * <li>{@link #P_SHORT_11_S0_C} (<code>0101110111010111</code>)</li>
     * <li>{@link #P_SHORT_11_S0_F} (<code>0110110111011011</code>)</li>
     * <li>{@link #P_SHORT_11_S0_J} (<code>0111011010110111</code>)</li>
     * <li>{@link #P_SHORT_11_S1_GX} (<code>1101110011101110</code>)</li>
     * </ul>
     */
    public static final short P_SHORT_11_S1_IX = (short) -5258;

    /**
     * <code>0111011010110111</code>
     * <p/>
     * Other patterns with half bit-flip-distance (8):
     * <ul>
     * <li>{@link #P_SHORT_05_S0_A} (<code>0001000100010101</code>)</li>
     * <li>{@link #P_SHORT_05_S0_AX} (<code>0010001000101010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_BX} (<code>0010001010001010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_CX} (<code>0010010001000110</code>)</li>
     * <li>{@link #P_SHORT_05_S0_D} (<code>0001001000101001</code>)</li>
     * <li>{@link #P_SHORT_05_S0_DX} (<code>0010010001010010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_EX} (<code>0010010001100010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_F} (<code>0001010001000101</code>)</li>
     * <li>{@link #P_SHORT_05_S0_G} (<code>0001010100010001</code>)</li>
     * <li>{@link #P_SHORT_05_S0_GX} (<code>0010101000100010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_IX} (<code>0100010001100010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_JX} (<code>0100010100100010</code>)</li>
     * <li>{@link #P_SHORT_07_S0_AX} (<code>0100100100110110</code>)</li>
     * <li>{@link #P_SHORT_07_S0_C} (<code>0010011001011001</code>)</li>
     * <li>{@link #P_SHORT_07_S0_DX} (<code>0101001011001010</code>)</li>
     * <li>{@link #P_SHORT_07_S0_EX} (<code>0101100100100110</code>)</li>
     * <li>{@link #P_SHORT_07_S0_FX} (<code>0110011001001010</code>)</li>
     * <li>{@link #P_SHORT_07_S0_H} (<code>0101001001001011</code>)</li>
     * <li>{@link #P_SHORT_07_S1_GX} (<code>1001001010101010</code>)</li>
     * <li>{@link #P_SHORT_11_S0_D} (<code>0101110111011101</code>)</li>
     * <li>{@link #P_SHORT_11_S0_F} (<code>0110110111011011</code>)</li>
     * <li>{@link #P_SHORT_11_S1_BX} (<code>1010111011101110</code>)</li>
     * <li>{@link #P_SHORT_11_S1_CX} (<code>1011101110101110</code>)</li>
     * <li>{@link #P_SHORT_11_S1_DX} (<code>1011101110111010</code>)</li>
     * <li>{@link #P_SHORT_11_S1_GX} (<code>1101110011101110</code>)</li>
     * <li>{@link #P_SHORT_11_S1_HX} (<code>1110101011101110</code>)</li>
     * <li>{@link #P_SHORT_11_S1_IX} (<code>1110101101110110</code>)</li>
     * </ul>
     */
    public static final short P_SHORT_11_S0_J = (short) 30391;

    /**
     * <code>1110110101101110</code>
     * <p/>
     * Other patterns with half bit-flip-distance (8):
     * <ul>
     * <li>{@link #P_SHORT_05_S0_AX} (<code>0010001000101010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_DX} (<code>0010010001010010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_FX} (<code>0010100010001010</code>)</li>
     * <li>{@link #P_SHORT_05_S0_GX} (<code>0010101000100010</code>)</li>
     * <li>{@link #P_SHORT_07_S0_CX} (<code>0100110010110010</code>)</li>
     * <li>{@link #P_SHORT_07_S0_G} (<code>0100100101010101</code>)</li>
     * <li>{@link #P_SHORT_07_S1_HX} (<code>1010010010010110</code>)</li>
     * <li>{@link #P_SHORT_11_S0_B} (<code>0101011101110111</code>)</li>
     * <li>{@link #P_SHORT_11_S0_C} (<code>0101110111010111</code>)</li>
     * <li>{@link #P_SHORT_11_S0_D} (<code>0101110111011101</code>)</li>
     * <li>{@link #P_SHORT_11_S0_I} (<code>0111010110111011</code>)</li>
     * <li>{@link #P_SHORT_11_S1_DX} (<code>1011101110111010</code>)</li>
     * <li>{@link #P_SHORT_11_S1_FX} (<code>1101101110110110</code>)</li>
     * </ul>
     */
    public static final short P_SHORT_11_S1_JX = (short) -4754;

    // Int patterns with 13 bits set

    /**
     * <code>00010001001010110100101001100011</code>
     * <p/>
     * Other patterns with half bit-flip-distance (16):
     * <ul>
     * <li>{@link #P_INT_13_S0_BX} (<code>00101100010001110100010001010110</code>)</li>
     * <li>{@link #P_INT_13_S0_CX} (<code>01001001010100100010110110100010</code>)</li>
     * <li>{@link #P_INT_13_S1_EX} (<code>10001110011001000101011000100010</code>)</li>
     * <li>{@link #P_INT_13_S1_FX} (<code>10100010001010101001100010010110</code>)</li>
     * <li>{@link #P_INT_17_S0_C} (<code>01000111000101011001101101110101</code>)</li>
     * <li>{@link #P_INT_17_S0_E} (<code>01101010010011001101101001010111</code>)</li>
     * <li>{@link #P_INT_17_S1_H} (<code>10101110001001101010101001110101</code>)</li>
     * <li>{@link #P_INT_17_S1_I} (<code>11010100010100111010011001010111</code>)</li>
     * <li>{@link #P_INT_19_S0_AX} (<code>00100111010110101110101110101110</code>)</li>
     * <li>{@link #P_INT_19_S0_BX} (<code>01010001110011011011101110111010</code>)</li>
     * <li>{@link #P_INT_19_S0_DX} (<code>01110111001100110111010111010010</code>)</li>
     * <li>{@link #P_INT_19_S1_EX} (<code>10110011001101010101110110101110</code>)</li>
     * <li>{@link #P_INT_19_S1_H} (<code>10101110101010101101101110100101</code>)</li>
     * </ul>
     */
    public static final int P_INT_13_S0_A = 288049763;

    /**
     * <code>00100010010101101001010011000110</code>
     * <p/>
     * Other patterns with half bit-flip-distance (16):
     * <ul>
     * <li>{@link #P_INT_13_S0_C} (<code>00100100101010010001011011010001</code>)</li>
     * <li>{@link #P_INT_13_S0_F} (<code>01010001000101010100110001001011</code>)</li>
     * <li>{@link #P_INT_13_S1_J} (<code>11101001010010010100010001000101</code>)</li>
     * <li>{@link #P_INT_17_S0_C} (<code>01000111000101011001101101110101</code>)</li>
     * <li>{@link #P_INT_17_S1_CX} (<code>10001110001010110011011011101010</code>)</li>
     * <li>{@link #P_INT_17_S1_EX} (<code>11010100100110011011010010101110</code>)</li>
     * <li>{@link #P_INT_17_S1_H} (<code>10101110001001101010101001110101</code>)</li>
     * <li>{@link #P_INT_19_S0_A} (<code>00010011101011010111010111010111</code>)</li>
     * <li>{@link #P_INT_19_S0_E} (<code>01011001100110101010111011010111</code>)</li>
     * <li>{@link #P_INT_19_S0_F} (<code>01100101011011100111011011101001</code>)</li>
     * <li>{@link #P_INT_19_S0_G} (<code>01110101110011000101101011010111</code>)</li>
     * <li>{@link #P_INT_19_S1_J} (<code>11101110100111011001110100010011</code>)</li>
     * </ul>
     */
    public static final int P_INT_13_S0_AX = 576099526;

    /**
     * <code>00010110001000111010001000101011</code>
     * <p/>
     * Other patterns with half bit-flip-distance (16):
     * <ul>
     * <li>{@link #P_INT_13_S0_C} (<code>00100100101010010001011011010001</code>)</li>
     * <li>{@link #P_INT_13_S0_F} (<code>01010001000101010100110001001011</code>)</li>
     * <li>{@link #P_INT_13_S1_FX} (<code>10100010001010101001100010010110</code>)</li>
     * <li>{@link #P_INT_17_S0_AX} (<code>00100010110111011010101110110010</code>)</li>
     * <li>{@link #P_INT_17_S0_B} (<code>00110010110111011000110001110011</code>)</li>
     * <li>{@link #P_INT_17_S0_C} (<code>01000111000101011001101101110101</code>)</li>
     * <li>{@link #P_INT_17_S1_F} (<code>10001110100101001011010110011011</code>)</li>
     * <li>{@link #P_INT_19_S0_BX} (<code>01010001110011011011101110111010</code>)</li>
     * <li>{@link #P_INT_19_S0_C} (<code>00110100111001011011010111011101</code>)</li>
     * <li>{@link #P_INT_19_S0_DX} (<code>01110111001100110111010111010010</code>)</li>
     * <li>{@link #P_INT_19_S0_F} (<code>01100101011011100111011011101001</code>)</li>
     * <li>{@link #P_INT_19_S1_H} (<code>10101110101010101101101110100101</code>)</li>
     * </ul>
     */
    public static final int P_INT_13_S0_B = 371434027;

    /**
     * <code>00101100010001110100010001010110</code>
     * <p/>
     * Other patterns with half bit-flip-distance (16):
     * <ul>
     * <li>{@link #P_INT_13_S0_A} (<code>00010001001010110100101001100011</code>)</li>
     * <li>{@link #P_INT_13_S0_CX} (<code>01001001010100100010110110100010</code>)</li>
     * <li>{@link #P_INT_13_S1_FX} (<code>10100010001010101001100010010110</code>)</li>
     * <li>{@link #P_INT_17_S0_A} (<code>00010001011011101101010111011001</code>)</li>
     * <li>{@link #P_INT_17_S0_BX} (<code>01100101101110110001100011100110</code>)</li>
     * <li>{@link #P_INT_17_S1_CX} (<code>10001110001010110011011011101010</code>)</li>
     * <li>{@link #P_INT_19_S0_A} (<code>00010011101011010111010111010111</code>)</li>
     * <li>{@link #P_INT_19_S0_CX} (<code>01101001110010110110101110111010</code>)</li>
     * <li>{@link #P_INT_19_S0_F} (<code>01100101011011100111011011101001</code>)</li>
     * <li>{@link #P_INT_19_S1_FX} (<code>11001010110111001110110111010010</code>)</li>
     * <li>{@link #P_INT_19_S1_J} (<code>11101110100111011001110100010011</code>)</li>
     * </ul>
     */
    public static final int P_INT_13_S0_BX = 742868054;

    /**
     * <code>00100100101010010001011011010001</code>
     * <p/>
     * Other patterns with half bit-flip-distance (16):
     * <ul>
     * <li>{@link #P_INT_13_S0_AX} (<code>00100010010101101001010011000110</code>)</li>
     * <li>{@link #P_INT_13_S0_B} (<code>00010110001000111010001000101011</code>)</li>
     * <li>{@link #P_INT_13_S0_E} (<code>01000111001100100010101100010001</code>)</li>
     * <li>{@link #P_INT_13_S1_EX} (<code>10001110011001000101011000100010</code>)</li>
     * <li>{@link #P_INT_17_S0_AX} (<code>00100010110111011010101110110010</code>)</li>
     * <li>{@link #P_INT_17_S0_C} (<code>01000111000101011001101101110101</code>)</li>
     * <li>{@link #P_INT_17_S0_D} (<code>01010101110110010010101110001011</code>)</li>
     * <li>{@link #P_INT_17_S0_E} (<code>01101010010011001101101001010111</code>)</li>
     * <li>{@link #P_INT_17_S1_DX} (<code>10101011101100100101011100010110</code>)</li>
     * <li>{@link #P_INT_17_S1_EX} (<code>11010100100110011011010010101110</code>)</li>
     * <li>{@link #P_INT_17_S1_F} (<code>10001110100101001011010110011011</code>)</li>
     * <li>{@link #P_INT_17_S1_H} (<code>10101110001001101010101001110101</code>)</li>
     * <li>{@link #P_INT_17_S1_I} (<code>11010100010100111010011001010111</code>)</li>
     * <li>{@link #P_INT_19_S0_E} (<code>01011001100110101010111011010111</code>)</li>
     * </ul>
     */
    public static final int P_INT_13_S0_C = 615061201;

    /**
     * <code>01001001010100100010110110100010</code>
     * <p/>
     * Other patterns with half bit-flip-distance (16):
     * <ul>
     * <li>{@link #P_INT_13_S0_A} (<code>00010001001010110100101001100011</code>)</li>
     * <li>{@link #P_INT_13_S0_BX} (<code>00101100010001110100010001010110</code>)</li>
     * <li>{@link #P_INT_13_S1_EX} (<code>10001110011001000101011000100010</code>)</li>
     * <li>{@link #P_INT_13_S1_J} (<code>11101001010010010100010001000101</code>)</li>
     * <li>{@link #P_INT_17_S1_CX} (<code>10001110001010110011011011101010</code>)</li>
     * <li>{@link #P_INT_17_S1_DX} (<code>10101011101100100101011100010110</code>)</li>
     * <li>{@link #P_INT_17_S1_EX} (<code>11010100100110011011010010101110</code>)</li>
     * <li>{@link #P_INT_17_S1_F} (<code>10001110100101001011010110011011</code>)</li>
     * <li>{@link #P_INT_17_S1_G} (<code>10100100110110101100110111010001</code>)</li>
     * <li>{@link #P_INT_17_S1_I} (<code>11010100010100111010011001010111</code>)</li>
     * <li>{@link #P_INT_19_S0_F} (<code>01100101011011100111011011101001</code>)</li>
     * <li>{@link #P_INT_19_S1_EX} (<code>10110011001101010101110110101110</code>)</li>
     * </ul>
     */
    public static final int P_INT_13_S0_CX = 1230122402;

    /**
     * <code>00110010100010011000100101011001</code>
     * <p/>
     * Other patterns with half bit-flip-distance (16):
     * <ul>
     * <li>{@link #P_INT_13_S0_E} (<code>01000111001100100010101100010001</code>)</li>
     * <li>{@link #P_INT_13_S1_H} (<code>10001010001001001001100010101101</code>)</li>
     * <li>{@link #P_INT_13_S1_J} (<code>11101001010010010100010001000101</code>)</li>
     * <li>{@link #P_INT_17_S1_F} (<code>10001110100101001011010110011011</code>)</li>
     * <li>{@link #P_INT_17_S1_H} (<code>10101110001001101010101001110101</code>)</li>
     * <li>{@link #P_INT_17_S1_J} (<code>11101110010101001100101001010011</code>)</li>
     * <li>{@link #P_INT_19_S0_CX} (<code>01101001110010110110101110111010</code>)</li>
     * <li>{@link #P_INT_19_S0_E} (<code>01011001100110101010111011010111</code>)</li>
     * <li>{@link #P_INT_19_S0_G} (<code>01110101110011000101101011010111</code>)</li>
     * <li>{@link #P_INT_19_S1_FX} (<code>11001010110111001110110111010010</code>)</li>
     * <li>{@link #P_INT_19_S1_H} (<code>10101110101010101101101110100101</code>)</li>
     * <li>{@link #P_INT_19_S1_I} (<code>11011010010101110010101110011011</code>)</li>
     * </ul>
     */
    public static final int P_INT_13_S0_D = 847874393;

    /**
     * <code>01100101000100110001001010110010</code>
     * <p/>
     * Other patterns with half bit-flip-distance (16):
     * <ul>
     * <li>{@link #P_INT_13_S0_F} (<code>01010001000101010100110001001011</code>)</li>
     * <li>{@link #P_INT_13_S0_G} (<code>01100100101001001001000101010101</code>)</li>
     * <li>{@link #P_INT_13_S1_EX} (<code>10001110011001000101011000100010</code>)</li>
     * <li>{@link #P_INT_17_S1_J} (<code>11101110010101001100101001010011</code>)</li>
     * <li>{@link #P_INT_19_S0_D} (<code>00111011100110011011101011101001</code>)</li>
     * <li>{@link #P_INT_19_S0_E} (<code>01011001100110101010111011010111</code>)</li>
     * <li>{@link #P_INT_19_S1_EX} (<code>10110011001101010101110110101110</code>)</li>
     * <li>{@link #P_INT_19_S1_GX} (<code>11101011100110001011010110101110</code>)</li>
     * <li>{@link #P_INT_19_S1_I} (<code>11011010010101110010101110011011</code>)</li>
     * <li>{@link #P_INT_19_S1_J} (<code>11101110100111011001110100010011</code>)</li>
     * </ul>
     */
    public static final int P_INT_13_S0_DX = 1695748786;

    /**
     * <code>01000111001100100010101100010001</code>
     * <p/>
     * Other patterns with half bit-flip-distance (16):
     * <ul>
     * <li>{@link #P_INT_13_S0_C} (<code>00100100101010010001011011010001</code>)</li>
     * <li>{@link #P_INT_13_S0_D} (<code>00110010100010011000100101011001</code>)</li>
     * <li>{@link #P_INT_13_S0_F} (<code>01010001000101010100110001001011</code>)</li>
     * <li>{@link #P_INT_13_S1_FX} (<code>10100010001010101001100010010110</code>)</li>
     * <li>{@link #P_INT_17_S0_AX} (<code>00100010110111011010101110110010</code>)</li>
     * <li>{@link #P_INT_17_S0_BX} (<code>01100101101110110001100011100110</code>)</li>
     * <li>{@link #P_INT_17_S1_F} (<code>10001110100101001011010110011011</code>)</li>
     * <li>{@link #P_INT_17_S1_G} (<code>10100100110110101100110111010001</code>)</li>
     * <li>{@link #P_INT_19_S0_CX} (<code>01101001110010110110101110111010</code>)</li>
     * <li>{@link #P_INT_19_S0_F} (<code>01100101011011100111011011101001</code>)</li>
     * <li>{@link #P_INT_19_S1_H} (<code>10101110101010101101101110100101</code>)</li>
     * <li>{@link #P_INT_19_S1_J} (<code>11101110100111011001110100010011</code>)</li>
     * </ul>
     */
    public static final int P_INT_13_S0_E = 1194470161;

    /**
     * <code>10001110011001000101011000100010</code>
     * <p/>
     * Other patterns with half bit-flip-distance (16):
     * <ul>
     * <li>{@link #P_INT_13_S0_A} (<code>00010001001010110100101001100011</code>)</li>
     * <li>{@link #P_INT_13_S0_C} (<code>00100100101010010001011011010001</code>)</li>
     * <li>{@link #P_INT_13_S0_CX} (<code>01001001010100100010110110100010</code>)</li>
     * <li>{@link #P_INT_13_S0_DX} (<code>01100101000100110001001010110010</code>)</li>
     * <li>{@link #P_INT_13_S1_FX} (<code>10100010001010101001100010010110</code>)</li>
     * <li>{@link #P_INT_13_S1_J} (<code>11101001010010010100010001000101</code>)</li>
     * <li>{@link #P_INT_19_S1_FX} (<code>11001010110111001110110111010010</code>)</li>
     * <li>{@link #P_INT_19_S1_J} (<code>11101110100111011001110100010011</code>)</li>
     * </ul>
     */
    public static final int P_INT_13_S1_EX = -1906026974;

    /**
     * <code>01010001000101010100110001001011</code>
     * <p/>
     * Other patterns with half bit-flip-distance (16):
     * <ul>
     * <li>{@link #P_INT_13_S0_AX} (<code>00100010010101101001010011000110</code>)</li>
     * <li>{@link #P_INT_13_S0_B} (<code>00010110001000111010001000101011</code>)</li>
     * <li>{@link #P_INT_13_S0_DX} (<code>01100101000100110001001010110010</code>)</li>
     * <li>{@link #P_INT_13_S0_E} (<code>01000111001100100010101100010001</code>)</li>
     * <li>{@link #P_INT_13_S1_GX} (<code>11001001010010010010001010101010</code>)</li>
     * <li>{@link #P_INT_17_S0_BX} (<code>01100101101110110001100011100110</code>)</li>
     * <li>{@link #P_INT_17_S0_E} (<code>01101010010011001101101001010111</code>)</li>
     * <li>{@link #P_INT_17_S1_EX} (<code>11010100100110011011010010101110</code>)</li>
     * <li>{@link #P_INT_19_S0_BX} (<code>01010001110011011011101110111010</code>)</li>
     * <li>{@link #P_INT_19_S0_D} (<code>00111011100110011011101011101001</code>)</li>
     * <li>{@link #P_INT_19_S0_F} (<code>01100101011011100111011011101001</code>)</li>
     * <li>{@link #P_INT_19_S1_FX} (<code>11001010110111001110110111010010</code>)</li>
     * <li>{@link #P_INT_19_S1_J} (<code>11101110100111011001110100010011</code>)</li>
     * </ul>
     */
    public static final int P_INT_13_S0_F = 1360350283;

    /**
     * <code>10100010001010101001100010010110</code>
     * <p/>
     * Other patterns with half bit-flip-distance (16):
     * <ul>
     * <li>{@link #P_INT_13_S0_A} (<code>00010001001010110100101001100011</code>)</li>
     * <li>{@link #P_INT_13_S0_B} (<code>00010110001000111010001000101011</code>)</li>
     * <li>{@link #P_INT_13_S0_BX} (<code>00101100010001110100010001010110</code>)</li>
     * <li>{@link #P_INT_13_S0_E} (<code>01000111001100100010101100010001</code>)</li>
     * <li>{@link #P_INT_13_S1_EX} (<code>10001110011001000101011000100010</code>)</li>
     * <li>{@link #P_INT_17_S0_A} (<code>00010001011011101101010111011001</code>)</li>
     * <li>{@link #P_INT_17_S0_B} (<code>00110010110111011000110001110011</code>)</li>
     * <li>{@link #P_INT_17_S1_EX} (<code>11010100100110011011010010101110</code>)</li>
     * <li>{@link #P_INT_17_S1_F} (<code>10001110100101001011010110011011</code>)</li>
     * <li>{@link #P_INT_17_S1_J} (<code>11101110010101001100101001010011</code>)</li>
     * <li>{@link #P_INT_19_S0_A} (<code>00010011101011010111010111010111</code>)</li>
     * <li>{@link #P_INT_19_S0_DX} (<code>01110111001100110111010111010010</code>)</li>
     * <li>{@link #P_INT_19_S0_E} (<code>01011001100110101010111011010111</code>)</li>
     * <li>{@link #P_INT_19_S0_G} (<code>01110101110011000101101011010111</code>)</li>
     * <li>{@link #P_INT_19_S1_FX} (<code>11001010110111001110110111010010</code>)</li>
     * </ul>
     */
    public static final int P_INT_13_S1_FX = -1574266730;

    /**
     * <code>01100100101001001001000101010101</code>
     * <p/>
     * Other patterns with half bit-flip-distance (16):
     * <ul>
     * <li>{@link #P_INT_13_S0_DX} (<code>01100101000100110001001010110010</code>)</li>
     * <li>{@link #P_INT_13_S1_J} (<code>11101001010010010100010001000101</code>)</li>
     * <li>{@link #P_INT_17_S0_B} (<code>00110010110111011000110001110011</code>)</li>
     * <li>{@link #P_INT_17_S1_DX} (<code>10101011101100100101011100010110</code>)</li>
     * <li>{@link #P_INT_17_S1_I} (<code>11010100010100111010011001010111</code>)</li>
     * <li>{@link #P_INT_19_S0_DX} (<code>01110111001100110111010111010010</code>)</li>
     * <li>{@link #P_INT_19_S0_F} (<code>01100101011011100111011011101001</code>)</li>
     * </ul>
     */
    public static final int P_INT_13_S0_G = 1688506709;

    /**
     * <code>11001001010010010010001010101010</code>
     * <p/>
     * Other patterns with half bit-flip-distance (16):
     * <ul>
     * <li>{@link #P_INT_13_S0_F} (<code>01010001000101010100110001001011</code>)</li>
     * <li>{@link #P_INT_13_S1_H} (<code>10001010001001001001100010101101</code>)</li>
     * <li>{@link #P_INT_17_S0_BX} (<code>01100101101110110001100011100110</code>)</li>
     * <li>{@link #P_INT_17_S1_I} (<code>11010100010100111010011001010111</code>)</li>
     * <li>{@link #P_INT_19_S0_E} (<code>01011001100110101010111011010111</code>)</li>
     * <li>{@link #P_INT_19_S1_FX} (<code>11001010110111001110110111010010</code>)</li>
     * </ul>
     */
    public static final int P_INT_13_S1_GX = -917953878;

    /**
     * <code>10001010001001001001100010101101</code>
     * <p/>
     * Other patterns with half bit-flip-distance (16):
     * <ul>
     * <li>{@link #P_INT_13_S0_D} (<code>00110010100010011000100101011001</code>)</li>
     * <li>{@link #P_INT_13_S1_GX} (<code>11001001010010010010001010101010</code>)</li>
     * <li>{@link #P_INT_17_S0_A} (<code>00010001011011101101010111011001</code>)</li>
     * <li>{@link #P_INT_17_S1_EX} (<code>11010100100110011011010010101110</code>)</li>
     * <li>{@link #P_INT_17_S1_J} (<code>11101110010101001100101001010011</code>)</li>
     * <li>{@link #P_INT_19_S0_C} (<code>00110100111001011011010111011101</code>)</li>
     * <li>{@link #P_INT_19_S1_I} (<code>11011010010101110010101110011011</code>)</li>
     * <li>{@link #P_INT_19_S1_J} (<code>11101110100111011001110100010011</code>)</li>
     * </ul>
     */
    public static final int P_INT_13_S1_H = -1977313107;

    /**
     * <code>10100010001011010100110001000101</code>
     * <p/>
     * Other patterns with half bit-flip-distance (16):
     * <ul>
     * <li>{@link #P_INT_17_S0_A} (<code>00010001011011101101010111011001</code>)</li>
     * <li>{@link #P_INT_17_S0_BX} (<code>01100101101110110001100011100110</code>)</li>
     * <li>{@link #P_INT_17_S0_C} (<code>01000111000101011001101101110101</code>)</li>
     * <li>{@link #P_INT_17_S1_CX} (<code>10001110001010110011011011101010</code>)</li>
     * <li>{@link #P_INT_17_S1_DX} (<code>10101011101100100101011100010110</code>)</li>
     * <li>{@link #P_INT_19_S0_C} (<code>00110100111001011011010111011101</code>)</li>
     * <li>{@link #P_INT_19_S0_F} (<code>01100101011011100111011011101001</code>)</li>
     * <li>{@link #P_INT_19_S0_G} (<code>01110101110011000101101011010111</code>)</li>
     * <li>{@link #P_INT_19_S1_FX} (<code>11001010110111001110110111010010</code>)</li>
     * </ul>
     */
    public static final int P_INT_13_S1_I = -1574089659;

    /**
     * <code>11101001010010010100010001000101</code>
     * <p/>
     * Other patterns with half bit-flip-distance (16):
     * <ul>
     * <li>{@link #P_INT_13_S0_AX} (<code>00100010010101101001010011000110</code>)</li>
     * <li>{@link #P_INT_13_S0_CX} (<code>01001001010100100010110110100010</code>)</li>
     * <li>{@link #P_INT_13_S0_D} (<code>00110010100010011000100101011001</code>)</li>
     * <li>{@link #P_INT_13_S0_G} (<code>01100100101001001001000101010101</code>)</li>
     * <li>{@link #P_INT_13_S1_EX} (<code>10001110011001000101011000100010</code>)</li>
     * <li>{@link #P_INT_17_S0_A} (<code>00010001011011101101010111011001</code>)</li>
     * <li>{@link #P_INT_17_S0_B} (<code>00110010110111011000110001110011</code>)</li>
     * <li>{@link #P_INT_17_S0_BX} (<code>01100101101110110001100011100110</code>)</li>
     * <li>{@link #P_INT_17_S1_DX} (<code>10101011101100100101011100010110</code>)</li>
     * <li>{@link #P_INT_19_S0_A} (<code>00010011101011010111010111010111</code>)</li>
     * <li>{@link #P_INT_19_S0_B} (<code>00101000111001101101110111011101</code>)</li>
     * <li>{@link #P_INT_19_S0_CX} (<code>01101001110010110110101110111010</code>)</li>
     * <li>{@link #P_INT_19_S0_E} (<code>01011001100110101010111011010111</code>)</li>
     * <li>{@link #P_INT_19_S1_FX} (<code>11001010110111001110110111010010</code>)</li>
     * <li>{@link #P_INT_19_S1_GX} (<code>11101011100110001011010110101110</code>)</li>
     * <li>{@link #P_INT_19_S1_J} (<code>11101110100111011001110100010011</code>)</li>
     * </ul>
     */
    public static final int P_INT_13_S1_J = -381074363;

    // Int patterns with 16 bits set

    /**
     * <code>00010010101101001011011000111011</code>
     * <p/>
     * Other patterns with half bit-flip-distance (16):
     * <ul>
     * <li>{@link #P_INT_16_S1_G} (<code>10011000100100101100111011000111</code>)</li>
     * </ul>
     */
    public static final int P_INT_16_S0_A = 313833019;

    /**
     * <code>00100101011010010110110001110110</code>
     * <p/>
     * Other patterns with half bit-flip-distance (16):
     * <ul>
     * <li>{@link #P_INT_16_S0_C} (<code>00110011010001011101000101101011</code>)</li>
     * <li>{@link #P_INT_16_S1_J} (<code>11101110010011000101100010010101</code>)</li>
     * </ul>
     */
    public static final int P_INT_16_S0_AX = 627666038;

    /**
     * <code>00100101110100101010100101011101</code>
     * <p/>
     * Other patterns with half bit-flip-distance (16):
     * <ul>
     * <li>{@link #P_INT_16_S0_C} (<code>00110011010001011101000101101011</code>)</li>
     * <li>{@link #P_INT_16_S1_DX} (<code>10011001100110101010011100110010</code>)</li>
     * <li>{@link #P_INT_16_S1_G} (<code>10011000100100101100111011000111</code>)</li>
     * <li>{@link #P_INT_16_S1_H} (<code>10101010100110011010011010010101</code>)</li>
     * <li>{@link #P_INT_16_S1_I} (<code>11001110010110100110101010010001</code>)</li>
     * </ul>
     */
    public static final int P_INT_16_S0_B = 634562909;

    /**
     * <code>01001011101001010101001010111010</code>
     * <p/>
     * Other patterns with half bit-flip-distance (16):
     * <ul>
     * <li>{@link #P_INT_16_S0_CX} (<code>01100110100010111010001011010110</code>)</li>
     * <li>{@link #P_INT_16_S0_E} (<code>01100101010001011000101011010111</code>)</li>
     * <li>{@link #P_INT_16_S1_J} (<code>11101110010011000101100010010101</code>)</li>
     * </ul>
     */
    public static final int P_INT_16_S0_BX = 1269125818;

    /**
     * <code>00110011010001011101000101101011</code>
     * <p/>
     * Other patterns with half bit-flip-distance (16):
     * <ul>
     * <li>{@link #P_INT_16_S0_AX} (<code>00100101011010010110110001110110</code>)</li>
     * <li>{@link #P_INT_16_S0_B} (<code>00100101110100101010100101011101</code>)</li>
     * <li>{@link #P_INT_16_S0_D} (<code>01001100110011010101001110011001</code>)</li>
     * <li>{@link #P_INT_16_S0_F} (<code>01110101100100101010101001101001</code>)</li>
     * </ul>
     */
    public static final int P_INT_16_S0_C = 860213611;

    /**
     * <code>01100110100010111010001011010110</code>
     * <p/>
     * Other patterns with half bit-flip-distance (16):
     * <ul>
     * <li>{@link #P_INT_16_S0_BX} (<code>01001011101001010101001010111010</code>)</li>
     * <li>{@link #P_INT_16_S0_D} (<code>01001100110011010101001110011001</code>)</li>
     * <li>{@link #P_INT_16_S1_DX} (<code>10011001100110101010011100110010</code>)</li>
     * <li>{@link #P_INT_16_S1_FX} (<code>11101011001001010101010011010010</code>)</li>
     * <li>{@link #P_INT_16_S1_G} (<code>10011000100100101100111011000111</code>)</li>
     * <li>{@link #P_INT_16_S1_J} (<code>11101110010011000101100010010101</code>)</li>
     * </ul>
     */
    public static final int P_INT_16_S0_CX = 1720427222;

    /**
     * <code>01001100110011010101001110011001</code>
     * <p/>
     * Other patterns with half bit-flip-distance (16):
     * <ul>
     * <li>{@link #P_INT_16_S0_C} (<code>00110011010001011101000101101011</code>)</li>
     * <li>{@link #P_INT_16_S0_CX} (<code>01100110100010111010001011010110</code>)</li>
     * <li>{@link #P_INT_16_S1_FX} (<code>11101011001001010101010011010010</code>)</li>
     * <li>{@link #P_INT_16_S1_H} (<code>10101010100110011010011010010101</code>)</li>
     * </ul>
     */
    public static final int P_INT_16_S0_D = 1288524697;

    /**
     * <code>10011001100110101010011100110010</code>
     * <p/>
     * Other patterns with half bit-flip-distance (16):
     * <ul>
     * <li>{@link #P_INT_16_S0_B} (<code>00100101110100101010100101011101</code>)</li>
     * <li>{@link #P_INT_16_S0_CX} (<code>01100110100010111010001011010110</code>)</li>
     * <li>{@link #P_INT_16_S1_I} (<code>11001110010110100110101010010001</code>)</li>
     * </ul>
     */
    public static final int P_INT_16_S1_DX = -1717917902;

    /**
     * <code>01100101010001011000101011010111</code>
     * <p/>
     * Other patterns with half bit-flip-distance (16):
     * <ul>
     * <li>{@link #P_INT_16_S0_BX} (<code>01001011101001010101001010111010</code>)</li>
     * <li>{@link #P_INT_16_S1_G} (<code>10011000100100101100111011000111</code>)</li>
     * <li>{@link #P_INT_16_S1_H} (<code>10101010100110011010011010010101</code>)</li>
     * <li>{@link #P_INT_16_S1_I} (<code>11001110010110100110101010010001</code>)</li>
     * </ul>
     */
    public static final int P_INT_16_S0_E = 1699056343;

    /**
     * <code>11001010100010110001010110101110</code>
     * <p/>
     * Other patterns with half bit-flip-distance (16):
     * <ul>
     * <li>{@link #P_INT_16_S1_G} (<code>10011000100100101100111011000111</code>)</li>
     * <li>{@link #P_INT_16_S1_J} (<code>11101110010011000101100010010101</code>)</li>
     * </ul>
     */
    public static final int P_INT_16_S1_EX = -896854610;

    /**
     * <code>01110101100100101010101001101001</code>
     * <p/>
     * Other patterns with half bit-flip-distance (16):
     * <ul>
     * <li>{@link #P_INT_16_S0_C} (<code>00110011010001011101000101101011</code>)</li>
     * <li>{@link #P_INT_16_S1_I} (<code>11001110010110100110101010010001</code>)</li>
     * </ul>
     */
    public static final int P_INT_16_S0_F = 1972546153;

    /**
     * <code>11101011001001010101010011010010</code>
     * <p/>
     * Other patterns with half bit-flip-distance (16):
     * <ul>
     * <li>{@link #P_INT_16_S0_CX} (<code>01100110100010111010001011010110</code>)</li>
     * <li>{@link #P_INT_16_S0_D} (<code>01001100110011010101001110011001</code>)</li>
     * <li>{@link #P_INT_16_S1_H} (<code>10101010100110011010011010010101</code>)</li>
     * </ul>
     */
    public static final int P_INT_16_S1_FX = -349874990;

    /**
     * <code>10011000100100101100111011000111</code>
     * <p/>
     * Other patterns with half bit-flip-distance (16):
     * <ul>
     * <li>{@link #P_INT_16_S0_A} (<code>00010010101101001011011000111011</code>)</li>
     * <li>{@link #P_INT_16_S0_B} (<code>00100101110100101010100101011101</code>)</li>
     * <li>{@link #P_INT_16_S0_CX} (<code>01100110100010111010001011010110</code>)</li>
     * <li>{@link #P_INT_16_S0_E} (<code>01100101010001011000101011010111</code>)</li>
     * <li>{@link #P_INT_16_S1_EX} (<code>11001010100010110001010110101110</code>)</li>
     * </ul>
     */
    public static final int P_INT_16_S1_G = -1735209273;

    /**
     * <code>10101010100110011010011010010101</code>
     * <p/>
     * Other patterns with half bit-flip-distance (16):
     * <ul>
     * <li>{@link #P_INT_16_S0_B} (<code>00100101110100101010100101011101</code>)</li>
     * <li>{@link #P_INT_16_S0_D} (<code>01001100110011010101001110011001</code>)</li>
     * <li>{@link #P_INT_16_S0_E} (<code>01100101010001011000101011010111</code>)</li>
     * <li>{@link #P_INT_16_S1_FX} (<code>11101011001001010101010011010010</code>)</li>
     * </ul>
     */
    public static final int P_INT_16_S1_H = -1432770923;

    /**
     * <code>11001110010110100110101010010001</code>
     * <p/>
     * Other patterns with half bit-flip-distance (16):
     * <ul>
     * <li>{@link #P_INT_16_S0_B} (<code>00100101110100101010100101011101</code>)</li>
     * <li>{@link #P_INT_16_S0_E} (<code>01100101010001011000101011010111</code>)</li>
     * <li>{@link #P_INT_16_S0_F} (<code>01110101100100101010101001101001</code>)</li>
     * <li>{@link #P_INT_16_S1_DX} (<code>10011001100110101010011100110010</code>)</li>
     * </ul>
     */
    public static final int P_INT_16_S1_I = -832935279;

    /**
     * <code>11101110010011000101100010010101</code>
     * <p/>
     * Other patterns with half bit-flip-distance (16):
     * <ul>
     * <li>{@link #P_INT_16_S0_AX} (<code>00100101011010010110110001110110</code>)</li>
     * <li>{@link #P_INT_16_S0_BX} (<code>01001011101001010101001010111010</code>)</li>
     * <li>{@link #P_INT_16_S0_CX} (<code>01100110100010111010001011010110</code>)</li>
     * <li>{@link #P_INT_16_S1_EX} (<code>11001010100010110001010110101110</code>)</li>
     * </ul>
     */
    public static final int P_INT_16_S1_J = -296986475;

    // Int patterns with 17 bits set

    /**
     * <code>00010001011011101101010111011001</code>
     * <p/>
     * Other patterns with half bit-flip-distance (16):
     * <ul>
     * <li>{@link #P_INT_13_S0_BX} (<code>00101100010001110100010001010110</code>)</li>
     * <li>{@link #P_INT_13_S1_FX} (<code>10100010001010101001100010010110</code>)</li>
     * <li>{@link #P_INT_13_S1_H} (<code>10001010001001001001100010101101</code>)</li>
     * <li>{@link #P_INT_13_S1_I} (<code>10100010001011010100110001000101</code>)</li>
     * <li>{@link #P_INT_13_S1_J} (<code>11101001010010010100010001000101</code>)</li>
     * <li>{@link #P_INT_17_S0_B} (<code>00110010110111011000110001110011</code>)</li>
     * <li>{@link #P_INT_17_S0_E} (<code>01101010010011001101101001010111</code>)</li>
     * <li>{@link #P_INT_17_S1_F} (<code>10001110100101001011010110011011</code>)</li>
     * <li>{@link #P_INT_19_S0_E} (<code>01011001100110101010111011010111</code>)</li>
     * <li>{@link #P_INT_19_S1_EX} (<code>10110011001101010101110110101110</code>)</li>
     * <li>{@link #P_INT_19_S1_FX} (<code>11001010110111001110110111010010</code>)</li>
     * </ul>
     */
    public static final int P_INT_17_S0_A = 292476377;

    /**
     * <code>00100010110111011010101110110010</code>
     * <p/>
     * Other patterns with half bit-flip-distance (16):
     * <ul>
     * <li>{@link #P_INT_13_S0_B} (<code>00010110001000111010001000101011</code>)</li>
     * <li>{@link #P_INT_13_S0_C} (<code>00100100101010010001011011010001</code>)</li>
     * <li>{@link #P_INT_13_S0_E} (<code>01000111001100100010101100010001</code>)</li>
     * <li>{@link #P_INT_17_S0_BX} (<code>01100101101110110001100011100110</code>)</li>
     * <li>{@link #P_INT_17_S1_EX} (<code>11010100100110011011010010101110</code>)</li>
     * <li>{@link #P_INT_17_S1_H} (<code>10101110001001101010101001110101</code>)</li>
     * <li>{@link #P_INT_19_S0_A} (<code>00010011101011010111010111010111</code>)</li>
     * <li>{@link #P_INT_19_S0_C} (<code>00110100111001011011010111011101</code>)</li>
     * <li>{@link #P_INT_19_S0_E} (<code>01011001100110101010111011010111</code>)</li>
     * <li>{@link #P_INT_19_S0_G} (<code>01110101110011000101101011010111</code>)</li>
     * <li>{@link #P_INT_19_S1_EX} (<code>10110011001101010101110110101110</code>)</li>
     * <li>{@link #P_INT_19_S1_H} (<code>10101110101010101101101110100101</code>)</li>
     * </ul>
     */
    public static final int P_INT_17_S0_AX = 584952754;

    /**
     * <code>00110010110111011000110001110011</code>
     * <p/>
     * Other patterns with half bit-flip-distance (16):
     * <ul>
     * <li>{@link #P_INT_13_S0_B} (<code>00010110001000111010001000101011</code>)</li>
     * <li>{@link #P_INT_13_S0_G} (<code>01100100101001001001000101010101</code>)</li>
     * <li>{@link #P_INT_13_S1_FX} (<code>10100010001010101001100010010110</code>)</li>
     * <li>{@link #P_INT_13_S1_J} (<code>11101001010010010100010001000101</code>)</li>
     * <li>{@link #P_INT_17_S0_A} (<code>00010001011011101101010111011001</code>)</li>
     * <li>{@link #P_INT_17_S0_BX} (<code>01100101101110110001100011100110</code>)</li>
     * <li>{@link #P_INT_17_S0_D} (<code>01010101110110010010101110001011</code>)</li>
     * <li>{@link #P_INT_17_S1_EX} (<code>11010100100110011011010010101110</code>)</li>
     * <li>{@link #P_INT_17_S1_F} (<code>10001110100101001011010110011011</code>)</li>
     * <li>{@link #P_INT_17_S1_H} (<code>10101110001001101010101001110101</code>)</li>
     * <li>{@link #P_INT_19_S0_B} (<code>00101000111001101101110111011101</code>)</li>
     * <li>{@link #P_INT_19_S1_EX} (<code>10110011001101010101110110101110</code>)</li>
     * <li>{@link #P_INT_19_S1_I} (<code>11011010010101110010101110011011</code>)</li>
     * </ul>
     */
    public static final int P_INT_17_S0_B = 853380211;

    /**
     * <code>01100101101110110001100011100110</code>
     * <p/>
     * Other patterns with half bit-flip-distance (16):
     * <ul>
     * <li>{@link #P_INT_13_S0_BX} (<code>00101100010001110100010001010110</code>)</li>
     * <li>{@link #P_INT_13_S0_E} (<code>01000111001100100010101100010001</code>)</li>
     * <li>{@link #P_INT_13_S0_F} (<code>01010001000101010100110001001011</code>)</li>
     * <li>{@link #P_INT_13_S1_GX} (<code>11001001010010010010001010101010</code>)</li>
     * <li>{@link #P_INT_13_S1_I} (<code>10100010001011010100110001000101</code>)</li>
     * <li>{@link #P_INT_13_S1_J} (<code>11101001010010010100010001000101</code>)</li>
     * <li>{@link #P_INT_17_S0_AX} (<code>00100010110111011010101110110010</code>)</li>
     * <li>{@link #P_INT_17_S0_B} (<code>00110010110111011000110001110011</code>)</li>
     * <li>{@link #P_INT_17_S1_DX} (<code>10101011101100100101011100010110</code>)</li>
     * <li>{@link #P_INT_17_S1_G} (<code>10100100110110101100110111010001</code>)</li>
     * <li>{@link #P_INT_19_S0_A} (<code>00010011101011010111010111010111</code>)</li>
     * <li>{@link #P_INT_19_S0_BX} (<code>01010001110011011011101110111010</code>)</li>
     * <li>{@link #P_INT_19_S1_J} (<code>11101110100111011001110100010011</code>)</li>
     * </ul>
     */
    public static final int P_INT_17_S0_BX = 1706760422;

    /**
     * <code>01000111000101011001101101110101</code>
     * <p/>
     * Other patterns with half bit-flip-distance (16):
     * <ul>
     * <li>{@link #P_INT_13_S0_A} (<code>00010001001010110100101001100011</code>)</li>
     * <li>{@link #P_INT_13_S0_AX} (<code>00100010010101101001010011000110</code>)</li>
     * <li>{@link #P_INT_13_S0_B} (<code>00010110001000111010001000101011</code>)</li>
     * <li>{@link #P_INT_13_S0_C} (<code>00100100101010010001011011010001</code>)</li>
     * <li>{@link #P_INT_13_S1_I} (<code>10100010001011010100110001000101</code>)</li>
     * <li>{@link #P_INT_17_S0_D} (<code>01010101110110010010101110001011</code>)</li>
     * <li>{@link #P_INT_17_S1_F} (<code>10001110100101001011010110011011</code>)</li>
     * <li>{@link #P_INT_19_S0_A} (<code>00010011101011010111010111010111</code>)</li>
     * <li>{@link #P_INT_19_S0_AX} (<code>00100111010110101110101110101110</code>)</li>
     * <li>{@link #P_INT_19_S0_C} (<code>00110100111001011011010111011101</code>)</li>
     * <li>{@link #P_INT_19_S0_DX} (<code>01110111001100110111010111010010</code>)</li>
     * <li>{@link #P_INT_19_S0_E} (<code>01011001100110101010111011010111</code>)</li>
     * <li>{@link #P_INT_19_S1_EX} (<code>10110011001101010101110110101110</code>)</li>
     * <li>{@link #P_INT_19_S1_H} (<code>10101110101010101101101110100101</code>)</li>
     * <li>{@link #P_INT_19_S1_I} (<code>11011010010101110010101110011011</code>)</li>
     * </ul>
     */
    public static final int P_INT_17_S0_C = 1192598389;

    /**
     * <code>10001110001010110011011011101010</code>
     * <p/>
     * Other patterns with half bit-flip-distance (16):
     * <ul>
     * <li>{@link #P_INT_13_S0_AX} (<code>00100010010101101001010011000110</code>)</li>
     * <li>{@link #P_INT_13_S0_BX} (<code>00101100010001110100010001010110</code>)</li>
     * <li>{@link #P_INT_13_S0_CX} (<code>01001001010100100010110110100010</code>)</li>
     * <li>{@link #P_INT_13_S1_I} (<code>10100010001011010100110001000101</code>)</li>
     * <li>{@link #P_INT_17_S1_DX} (<code>10101011101100100101011100010110</code>)</li>
     * <li>{@link #P_INT_17_S1_I} (<code>11010100010100111010011001010111</code>)</li>
     * <li>{@link #P_INT_19_S0_A} (<code>00010011101011010111010111010111</code>)</li>
     * <li>{@link #P_INT_19_S0_AX} (<code>00100111010110101110101110101110</code>)</li>
     * <li>{@link #P_INT_19_S0_CX} (<code>01101001110010110110101110111010</code>)</li>
     * <li>{@link #P_INT_19_S1_EX} (<code>10110011001101010101110110101110</code>)</li>
     * <li>{@link #P_INT_19_S1_I} (<code>11011010010101110010101110011011</code>)</li>
     * </ul>
     */
    public static final int P_INT_17_S1_CX = -1909770518;

    /**
     * <code>01010101110110010010101110001011</code>
     * <p/>
     * Other patterns with half bit-flip-distance (16):
     * <ul>
     * <li>{@link #P_INT_13_S0_C} (<code>00100100101010010001011011010001</code>)</li>
     * <li>{@link #P_INT_17_S0_B} (<code>00110010110111011000110001110011</code>)</li>
     * <li>{@link #P_INT_17_S0_C} (<code>01000111000101011001101101110101</code>)</li>
     * <li>{@link #P_INT_17_S1_F} (<code>10001110100101001011010110011011</code>)</li>
     * <li>{@link #P_INT_17_S1_G} (<code>10100100110110101100110111010001</code>)</li>
     * <li>{@link #P_INT_19_S0_A} (<code>00010011101011010111010111010111</code>)</li>
     * <li>{@link #P_INT_19_S0_C} (<code>00110100111001011011010111011101</code>)</li>
     * <li>{@link #P_INT_19_S0_DX} (<code>01110111001100110111010111010010</code>)</li>
     * <li>{@link #P_INT_19_S0_F} (<code>01100101011011100111011011101001</code>)</li>
     * <li>{@link #P_INT_19_S1_FX} (<code>11001010110111001110110111010010</code>)</li>
     * <li>{@link #P_INT_19_S1_GX} (<code>11101011100110001011010110101110</code>)</li>
     * <li>{@link #P_INT_19_S1_J} (<code>11101110100111011001110100010011</code>)</li>
     * </ul>
     */
    public static final int P_INT_17_S0_D = 1440295819;

    /**
     * <code>10101011101100100101011100010110</code> P_INT_17_S1_DX
     * <p/>
     * Other patterns with half bit-flip-distance (16):
     * <ul>
     * <li>{@link #P_INT_13_S0_C} (<code>00100100101010010001011011010001</code>)</li>
     * <li>{@link #P_INT_13_S0_CX} (<code>01001001010100100010110110100010</code>)</li>
     * <li>{@link #P_INT_13_S0_G} (<code>01100100101001001001000101010101</code>)</li>
     * <li>{@link #P_INT_13_S1_I} (<code>10100010001011010100110001000101</code>)</li>
     * <li>{@link #P_INT_13_S1_J} (<code>11101001010010010100010001000101</code>)</li>
     * <li>{@link #P_INT_17_S0_BX} (<code>01100101101110110001100011100110</code>)</li>
     * <li>{@link #P_INT_17_S0_E} (<code>01101010010011001101101001010111</code>)</li>
     * <li>{@link #P_INT_17_S1_CX} (<code>10001110001010110011011011101010</code>)</li>
     * <li>{@link #P_INT_17_S1_G} (<code>10100100110110101100110111010001</code>)</li>
     * <li>{@link #P_INT_17_S1_H} (<code>10101110001001101010101001110101</code>)</li>
     * <li>{@link #P_INT_17_S1_J} (<code>11101110010101001100101001010011</code>)</li>
     * <li>{@link #P_INT_19_S0_AX} (<code>00100111010110101110101110101110</code>)</li>
     * <li>{@link #P_INT_19_S0_CX} (<code>01101001110010110110101110111010</code>)</li>
     * <li>{@link #P_INT_19_S0_E} (<code>01011001100110101010111011010111</code>)</li>
     * <li>{@link #P_INT_19_S1_FX} (<code>11001010110111001110110111010010</code>)</li>
     * </ul>
     */
    public static final int P_INT_17_S1_DX = -1414375658;

    /**
     * <code>01101010010011001101101001010111</code>
     * <p/>
     * Other patterns with half bit-flip-distance (16):
     * <ul>
     * <li>{@link #P_INT_13_S0_A} (<code>00010001001010110100101001100011</code>)</li>
     * <li>{@link #P_INT_13_S0_C} (<code>00100100101010010001011011010001</code>)</li>
     * <li>{@link #P_INT_13_S0_F} (<code>01010001000101010100110001001011</code>)</li>
     * <li>{@link #P_INT_17_S0_A} (<code>00010001011011101101010111011001</code>)</li>
     * <li>{@link #P_INT_17_S1_DX} (<code>10101011101100100101011100010110</code>)</li>
     * <li>{@link #P_INT_17_S1_G} (<code>10100100110110101100110111010001</code>)</li>
     * <li>{@link #P_INT_17_S1_I} (<code>11010100010100111010011001010111</code>)</li>
     * <li>{@link #P_INT_19_S0_A} (<code>00010011101011010111010111010111</code>)</li>
     * <li>{@link #P_INT_19_S0_AX} (<code>00100111010110101110101110101110</code>)</li>
     * <li>{@link #P_INT_19_S0_BX} (<code>01010001110011011011101110111010</code>)</li>
     * <li>{@link #P_INT_19_S0_CX} (<code>01101001110010110110101110111010</code>)</li>
     * <li>{@link #P_INT_19_S0_D} (<code>00111011100110011011101011101001</code>)</li>
     * <li>{@link #P_INT_19_S0_F} (<code>01100101011011100111011011101001</code>)</li>
     * <li>{@link #P_INT_19_S1_I} (<code>11011010010101110010101110011011</code>)</li>
     * </ul>
     */
    public static final int P_INT_17_S0_E = 1783421527;

    /**
     * <code>11010100100110011011010010101110</code>
     * <p/>
     * Other patterns with half bit-flip-distance (16):
     * <ul>
     * <li>{@link #P_INT_13_S0_AX} (<code>00100010010101101001010011000110</code>)</li>
     * <li>{@link #P_INT_13_S0_C} (<code>00100100101010010001011011010001</code>)</li>
     * <li>{@link #P_INT_13_S0_CX} (<code>01001001010100100010110110100010</code>)</li>
     * <li>{@link #P_INT_13_S0_F} (<code>01010001000101010100110001001011</code>)</li>
     * <li>{@link #P_INT_13_S1_FX} (<code>10100010001010101001100010010110</code>)</li>
     * <li>{@link #P_INT_13_S1_H} (<code>10001010001001001001100010101101</code>)</li>
     * <li>{@link #P_INT_17_S0_AX} (<code>00100010110111011010101110110010</code>)</li>
     * <li>{@link #P_INT_17_S0_B} (<code>00110010110111011000110001110011</code>)</li>
     * <li>{@link #P_INT_19_S0_A} (<code>00010011101011010111010111010111</code>)</li>
     * <li>{@link #P_INT_19_S0_AX} (<code>00100111010110101110101110101110</code>)</li>
     * <li>{@link #P_INT_19_S0_DX} (<code>01110111001100110111010111010010</code>)</li>
     * <li>{@link #P_INT_19_S1_FX} (<code>11001010110111001110110111010010</code>)</li>
     * </ul>
     */
    public static final int P_INT_17_S1_EX = -728124242;

    /**
     * <code>10001110100101001011010110011011</code>
     * <p/>
     * Other patterns with half bit-flip-distance (16):
     * <ul>
     * <li>{@link #P_INT_13_S0_B} (<code>00010110001000111010001000101011</code>)</li>
     * <li>{@link #P_INT_13_S0_C} (<code>00100100101010010001011011010001</code>)</li>
     * <li>{@link #P_INT_13_S0_CX} (<code>01001001010100100010110110100010</code>)</li>
     * <li>{@link #P_INT_13_S0_D} (<code>00110010100010011000100101011001</code>)</li>
     * <li>{@link #P_INT_13_S0_E} (<code>01000111001100100010101100010001</code>)</li>
     * <li>{@link #P_INT_13_S1_FX} (<code>10100010001010101001100010010110</code>)</li>
     * <li>{@link #P_INT_17_S0_A} (<code>00010001011011101101010111011001</code>)</li>
     * <li>{@link #P_INT_17_S0_B} (<code>00110010110111011000110001110011</code>)</li>
     * <li>{@link #P_INT_17_S0_C} (<code>01000111000101011001101101110101</code>)</li>
     * <li>{@link #P_INT_17_S0_D} (<code>01010101110110010010101110001011</code>)</li>
     * <li>{@link #P_INT_17_S1_H} (<code>10101110001001101010101001110101</code>)</li>
     * <li>{@link #P_INT_17_S1_I} (<code>11010100010100111010011001010111</code>)</li>
     * <li>{@link #P_INT_19_S0_BX} (<code>01010001110011011011101110111010</code>)</li>
     * <li>{@link #P_INT_19_S0_D} (<code>00111011100110011011101011101001</code>)</li>
     * <li>{@link #P_INT_19_S0_DX} (<code>01110111001100110111010111010010</code>)</li>
     * <li>{@link #P_INT_19_S0_E} (<code>01011001100110101010111011010111</code>)</li>
     * <li>{@link #P_INT_19_S1_EX} (<code>10110011001101010101110110101110</code>)</li>
     * <li>{@link #P_INT_19_S1_H} (<code>10101110101010101101101110100101</code>)</li>
     * </ul>
     */
    public static final int P_INT_17_S1_F = -1902856805;

    /**
     * <code>10100100110110101100110111010001</code>
     * <p/>
     * Other patterns with half bit-flip-distance (16):
     * <ul>
     * <li>{@link #P_INT_13_S0_CX} (<code>01001001010100100010110110100010</code>)</li>
     * <li>{@link #P_INT_13_S0_E} (<code>01000111001100100010101100010001</code>)</li>
     * <li>{@link #P_INT_17_S0_BX} (<code>01100101101110110001100011100110</code>)</li>
     * <li>{@link #P_INT_17_S0_D} (<code>01010101110110010010101110001011</code>)</li>
     * <li>{@link #P_INT_17_S0_E} (<code>01101010010011001101101001010111</code>)</li>
     * <li>{@link #P_INT_17_S1_DX} (<code>10101011101100100101011100010110</code>)</li>
     * <li>{@link #P_INT_17_S1_H} (<code>10101110001001101010101001110101</code>)</li>
     * <li>{@link #P_INT_19_S0_CX} (<code>01101001110010110110101110111010</code>)</li>
     * <li>{@link #P_INT_19_S0_DX} (<code>01110111001100110111010111010010</code>)</li>
     * <li>{@link #P_INT_19_S0_F} (<code>01100101011011100111011011101001</code>)</li>
     * </ul>
     */
    public static final int P_INT_17_S1_G = -1529164335;

    /**
     * <code>10101110001001101010101001110101</code>
     * <p/>
     * Other patterns with half bit-flip-distance (16):
     * <ul>
     * <li>{@link #P_INT_13_S0_A} (<code>00010001001010110100101001100011</code>)</li>
     * <li>{@link #P_INT_13_S0_AX} (<code>00100010010101101001010011000110</code>)</li>
     * <li>{@link #P_INT_13_S0_C} (<code>00100100101010010001011011010001</code>)</li>
     * <li>{@link #P_INT_13_S0_D} (<code>00110010100010011000100101011001</code>)</li>
     * <li>{@link #P_INT_17_S0_AX} (<code>00100010110111011010101110110010</code>)</li>
     * <li>{@link #P_INT_17_S0_B} (<code>00110010110111011000110001110011</code>)</li>
     * <li>{@link #P_INT_17_S1_DX} (<code>10101011101100100101011100010110</code>)</li>
     * <li>{@link #P_INT_17_S1_F} (<code>10001110100101001011010110011011</code>)</li>
     * <li>{@link #P_INT_17_S1_G} (<code>10100100110110101100110111010001</code>)</li>
     * <li>{@link #P_INT_19_S0_AX} (<code>00100111010110101110101110101110</code>)</li>
     * <li>{@link #P_INT_19_S0_C} (<code>00110100111001011011010111011101</code>)</li>
     * <li>{@link #P_INT_19_S0_D} (<code>00111011100110011011101011101001</code>)</li>
     * <li>{@link #P_INT_19_S0_E} (<code>01011001100110101010111011010111</code>)</li>
     * <li>{@link #P_INT_19_S0_F} (<code>01100101011011100111011011101001</code>)</li>
     * <li>{@link #P_INT_19_S1_I} (<code>11011010010101110010101110011011</code>)</li>
     * <li>{@link #P_INT_19_S1_J} (<code>11101110100111011001110100010011</code>)</li>
     * </ul>
     */
    public static final int P_INT_17_S1_H = -1373197707;

    /**
     * <code>11010100010100111010011001010111</code>
     * <p/>
     * Other patterns with half bit-flip-distance (16):
     * <ul>
     * <li>{@link #P_INT_13_S0_A} (<code>00010001001010110100101001100011</code>)</li>
     * <li>{@link #P_INT_13_S0_C} (<code>00100100101010010001011011010001</code>)</li>
     * <li>{@link #P_INT_13_S0_CX} (<code>01001001010100100010110110100010</code>)</li>
     * <li>{@link #P_INT_13_S0_G} (<code>01100100101001001001000101010101</code>)</li>
     * <li>{@link #P_INT_13_S1_GX} (<code>11001001010010010010001010101010</code>)</li>
     * <li>{@link #P_INT_17_S0_E} (<code>01101010010011001101101001010111</code>)</li>
     * <li>{@link #P_INT_17_S1_CX} (<code>10001110001010110011011011101010</code>)</li>
     * <li>{@link #P_INT_17_S1_F} (<code>10001110100101001011010110011011</code>)</li>
     * <li>{@link #P_INT_19_S0_G} (<code>01110101110011000101101011010111</code>)</li>
     * <li>{@link #P_INT_19_S1_FX} (<code>11001010110111001110110111010010</code>)</li>
     * <li>{@link #P_INT_19_S1_J} (<code>11101110100111011001110100010011</code>)</li>
     * </ul>
     */
    public static final int P_INT_17_S1_I = -732715433;

    /**
     * <code>11101110010101001100101001010011</code>
     * <p/>
     * Other patterns with half bit-flip-distance (16):
     * <ul>
     * <li>{@link #P_INT_13_S0_D} (<code>00110010100010011000100101011001</code>)</li>
     * <li>{@link #P_INT_13_S0_DX} (<code>01100101000100110001001010110010</code>)</li>
     * <li>{@link #P_INT_13_S1_FX} (<code>10100010001010101001100010010110</code>)</li>
     * <li>{@link #P_INT_13_S1_H} (<code>10001010001001001001100010101101</code>)</li>
     * <li>{@link #P_INT_17_S1_DX} (<code>10101011101100100101011100010110</code>)</li>
     * <li>{@link #P_INT_19_S0_AX} (<code>00100111010110101110101110101110</code>)</li>
     * <li>{@link #P_INT_19_S0_B} (<code>00101000111001101101110111011101</code>)</li>
     * <li>{@link #P_INT_19_S0_E} (<code>01011001100110101010111011010111</code>)</li>
     * <li>{@link #P_INT_19_S1_H} (<code>10101110101010101101101110100101</code>)</li>
     * </ul>
     */
    public static final int P_INT_17_S1_J = -296433069;

    // Int patterns with 19 bits set

    /**
     * <code>00010011101011010111010111010111</code>
     * <p/>
     * Other patterns with half bit-flip-distance (16):
     * <ul>
     * <li>{@link #P_INT_13_S0_AX} (<code>00100010010101101001010011000110</code>)</li>
     * <li>{@link #P_INT_13_S0_BX} (<code>00101100010001110100010001010110</code>)</li>
     * <li>{@link #P_INT_13_S1_FX} (<code>10100010001010101001100010010110</code>)</li>
     * <li>{@link #P_INT_13_S1_J} (<code>11101001010010010100010001000101</code>)</li>
     * <li>{@link #P_INT_17_S0_AX} (<code>00100010110111011010101110110010</code>)</li>
     * <li>{@link #P_INT_17_S0_BX} (<code>01100101101110110001100011100110</code>)</li>
     * <li>{@link #P_INT_17_S0_C} (<code>01000111000101011001101101110101</code>)</li>
     * <li>{@link #P_INT_17_S0_D} (<code>01010101110110010010101110001011</code>)</li>
     * <li>{@link #P_INT_17_S0_E} (<code>01101010010011001101101001010111</code>)</li>
     * <li>{@link #P_INT_17_S1_CX} (<code>10001110001010110011011011101010</code>)</li>
     * <li>{@link #P_INT_17_S1_EX} (<code>11010100100110011011010010101110</code>)</li>
     * <li>{@link #P_INT_19_S0_D} (<code>00111011100110011011101011101001</code>)</li>
     * <li>{@link #P_INT_19_S0_F} (<code>01100101011011100111011011101001</code>)</li>
     * <li>{@link #P_INT_19_S1_GX} (<code>11101011100110001011010110101110</code>)</li>
     * <li>{@link #P_INT_19_S1_J} (<code>11101110100111011001110100010011</code>)</li>
     * </ul>
     */
    public static final int P_INT_19_S0_A = 330134999;

    /**
     * <code>00100111010110101110101110101110</code>
     * <p/>
     * Other patterns with half bit-flip-distance (16):
     * <ul>
     * <li>{@link #P_INT_13_S0_A} (<code>00010001001010110100101001100011</code>)</li>
     * <li>{@link #P_INT_17_S0_C} (<code>01000111000101011001101101110101</code>)</li>
     * <li>{@link #P_INT_17_S0_E} (<code>01101010010011001101101001010111</code>)</li>
     * <li>{@link #P_INT_17_S1_CX} (<code>10001110001010110011011011101010</code>)</li>
     * <li>{@link #P_INT_17_S1_DX} (<code>10101011101100100101011100010110</code>)</li>
     * <li>{@link #P_INT_17_S1_EX} (<code>11010100100110011011010010101110</code>)</li>
     * <li>{@link #P_INT_17_S1_H} (<code>10101110001001101010101001110101</code>)</li>
     * <li>{@link #P_INT_17_S1_J} (<code>11101110010101001100101001010011</code>)</li>
     * <li>{@link #P_INT_19_S0_DX} (<code>01110111001100110111010111010010</code>)</li>
     * <li>{@link #P_INT_19_S0_E} (<code>01011001100110101010111011010111</code>)</li>
     * <li>{@link #P_INT_19_S0_G} (<code>01110101110011000101101011010111</code>)</li>
     * <li>{@link #P_INT_19_S1_FX} (<code>11001010110111001110110111010010</code>)</li>
     * <li>{@link #P_INT_19_S1_I} (<code>11011010010101110010101110011011</code>)</li>
     * </ul>
     */
    public static final int P_INT_19_S0_AX = 660269998;

    /**
     * <code>00101000111001101101110111011101</code>
     * <p/>
     * Other patterns with half bit-flip-distance (16):
     * <ul>
     * <li>{@link #P_INT_13_S1_J} (<code>11101001010010010100010001000101</code>)</li>
     * <li>{@link #P_INT_17_S0_B} (<code>00110010110111011000110001110011</code>)</li>
     * <li>{@link #P_INT_17_S1_J} (<code>11101110010101001100101001010011</code>)</li>
     * <li>{@link #P_INT_19_S0_CX} (<code>01101001110010110110101110111010</code>)</li>
     * <li>{@link #P_INT_19_S0_E} (<code>01011001100110101010111011010111</code>)</li>
     * <li>{@link #P_INT_19_S1_EX} (<code>10110011001101010101110110101110</code>)</li>
     * <li>{@link #P_INT_19_S1_J} (<code>11101110100111011001110100010011</code>)</li>
     * </ul>
     */
    public static final int P_INT_19_S0_B = 686218717;

    /**
     * <code>01010001110011011011101110111010</code>
     * <p/>
     * Other patterns with half bit-flip-distance (16):
     * <ul>
     * <li>{@link #P_INT_13_S0_A} (<code>00010001001010110100101001100011</code>)</li>
     * <li>{@link #P_INT_13_S0_B} (<code>00010110001000111010001000101011</code>)</li>
     * <li>{@link #P_INT_13_S0_F} (<code>01010001000101010100110001001011</code>)</li>
     * <li>{@link #P_INT_17_S0_BX} (<code>01100101101110110001100011100110</code>)</li>
     * <li>{@link #P_INT_17_S0_E} (<code>01101010010011001101101001010111</code>)</li>
     * <li>{@link #P_INT_17_S1_F} (<code>10001110100101001011010110011011</code>)</li>
     * <li>{@link #P_INT_19_S0_F} (<code>01100101011011100111011011101001</code>)</li>
     * <li>{@link #P_INT_19_S1_EX} (<code>10110011001101010101110110101110</code>)</li>
     * <li>{@link #P_INT_19_S1_J} (<code>11101110100111011001110100010011</code>)</li>
     * </ul>
     */
    public static final int P_INT_19_S0_BX = 1372437434;

    /**
     * <code>00110100111001011011010111011101</code>
     * <p/>
     * Other patterns with half bit-flip-distance (16):
     * <ul>
     * <li>{@link #P_INT_13_S0_B} (<code>00010110001000111010001000101011</code>)</li>
     * <li>{@link #P_INT_13_S1_H} (<code>10001010001001001001100010101101</code>)</li>
     * <li>{@link #P_INT_13_S1_I} (<code>10100010001011010100110001000101</code>)</li>
     * <li>{@link #P_INT_17_S0_AX} (<code>00100010110111011010101110110010</code>)</li>
     * <li>{@link #P_INT_17_S0_C} (<code>01000111000101011001101101110101</code>)</li>
     * <li>{@link #P_INT_17_S0_D} (<code>01010101110110010010101110001011</code>)</li>
     * <li>{@link #P_INT_17_S1_H} (<code>10101110001001101010101001110101</code>)</li>
     * <li>{@link #P_INT_19_S0_D} (<code>00111011100110011011101011101001</code>)</li>
     * <li>{@link #P_INT_19_S1_EX} (<code>10110011001101010101110110101110</code>)</li>
     * <li>{@link #P_INT_19_S1_J} (<code>11101110100111011001110100010011</code>)</li>
     * </ul>
     */
    public static final int P_INT_19_S0_C = 887469533;

    /**
     * <code>01101001110010110110101110111010</code>
     * <p/>
     * Other patterns with half bit-flip-distance (16):
     * <ul>
     * <li>{@link #P_INT_13_S0_BX} (<code>00101100010001110100010001010110</code>)</li>
     * <li>{@link #P_INT_13_S0_D} (<code>00110010100010011000100101011001</code>)</li>
     * <li>{@link #P_INT_13_S0_E} (<code>01000111001100100010101100010001</code>)</li>
     * <li>{@link #P_INT_13_S1_J} (<code>11101001010010010100010001000101</code>)</li>
     * <li>{@link #P_INT_17_S0_E} (<code>01101010010011001101101001010111</code>)</li>
     * <li>{@link #P_INT_17_S1_CX} (<code>10001110001010110011011011101010</code>)</li>
     * <li>{@link #P_INT_17_S1_DX} (<code>10101011101100100101011100010110</code>)</li>
     * <li>{@link #P_INT_17_S1_G} (<code>10100100110110101100110111010001</code>)</li>
     * <li>{@link #P_INT_19_S0_B} (<code>00101000111001101101110111011101</code>)</li>
     * <li>{@link #P_INT_19_S0_DX} (<code>01110111001100110111010111010010</code>)</li>
     * <li>{@link #P_INT_19_S1_H} (<code>10101110101010101101101110100101</code>)</li>
     * </ul>
     */
    public static final int P_INT_19_S0_CX = 1774939066;

    /**
     * <code>00111011100110011011101011101001</code>
     * <p/>
     * Other patterns with half bit-flip-distance (16):
     * <ul>
     * <li>{@link #P_INT_13_S0_DX} (<code>01100101000100110001001010110010</code>)</li>
     * <li>{@link #P_INT_13_S0_F} (<code>01010001000101010100110001001011</code>)</li>
     * <li>{@link #P_INT_17_S0_E} (<code>01101010010011001101101001010111</code>)</li>
     * <li>{@link #P_INT_17_S1_F} (<code>10001110100101001011010110011011</code>)</li>
     * <li>{@link #P_INT_17_S1_H} (<code>10101110001001101010101001110101</code>)</li>
     * <li>{@link #P_INT_19_S0_A} (<code>00010011101011010111010111010111</code>)</li>
     * <li>{@link #P_INT_19_S0_C} (<code>00110100111001011011010111011101</code>)</li>
     * <li>{@link #P_INT_19_S0_F} (<code>01100101011011100111011011101001</code>)</li>
     * <li>{@link #P_INT_19_S0_G} (<code>01110101110011000101101011010111</code>)</li>
     * <li>{@link #P_INT_19_S1_EX} (<code>10110011001101010101110110101110</code>)</li>
     * <li>{@link #P_INT_19_S1_I} (<code>11011010010101110010101110011011</code>)</li>
     * <li>{@link #P_INT_19_S1_J} (<code>11101110100111011001110100010011</code>)</li>
     * </ul>
     */
    public static final int P_INT_19_S0_D = 999930601;

    /**
     * <code>01110111001100110111010111010010</code>
     * <p/>
     * Other patterns with half bit-flip-distance (16):
     * <ul>
     * <li>{@link #P_INT_13_S0_A} (<code>00010001001010110100101001100011</code>)</li>
     * <li>{@link #P_INT_13_S0_B} (<code>00010110001000111010001000101011</code>)</li>
     * <li>{@link #P_INT_13_S0_G} (<code>01100100101001001001000101010101</code>)</li>
     * <li>{@link #P_INT_13_S1_FX} (<code>10100010001010101001100010010110</code>)</li>
     * <li>{@link #P_INT_17_S0_C} (<code>01000111000101011001101101110101</code>)</li>
     * <li>{@link #P_INT_17_S0_D} (<code>01010101110110010010101110001011</code>)</li>
     * <li>{@link #P_INT_17_S1_EX} (<code>11010100100110011011010010101110</code>)</li>
     * <li>{@link #P_INT_17_S1_F} (<code>10001110100101001011010110011011</code>)</li>
     * <li>{@link #P_INT_17_S1_G} (<code>10100100110110101100110111010001</code>)</li>
     * <li>{@link #P_INT_19_S0_AX} (<code>00100111010110101110101110101110</code>)</li>
     * <li>{@link #P_INT_19_S0_CX} (<code>01101001110010110110101110111010</code>)</li>
     * <li>{@link #P_INT_19_S0_E} (<code>01011001100110101010111011010111</code>)</li>
     * <li>{@link #P_INT_19_S0_G} (<code>01110101110011000101101011010111</code>)</li>
     * <li>{@link #P_INT_19_S1_FX} (<code>11001010110111001110110111010010</code>)</li>
     * <li>{@link #P_INT_19_S1_GX} (<code>11101011100110001011010110101110</code>)</li>
     * <li>{@link #P_INT_19_S1_I} (<code>11011010010101110010101110011011</code>)</li>
     * <li>{@link #P_INT_19_S1_J} (<code>11101110100111011001110100010011</code>)</li>
     * </ul>
     */
    public static final int P_INT_19_S0_DX = 1999861202;

    /**
     * <code>01011001100110101010111011010111</code>
     * <p/>
     * Other patterns with half bit-flip-distance (16):
     * <ul>
     * <li>{@link #P_INT_13_S0_AX} (<code>00100010010101101001010011000110</code>)</li>
     * <li>{@link #P_INT_13_S0_C} (<code>00100100101010010001011011010001</code>)</li>
     * <li>{@link #P_INT_13_S0_D} (<code>00110010100010011000100101011001</code>)</li>
     * <li>{@link #P_INT_13_S0_DX} (<code>01100101000100110001001010110010</code>)</li>
     * <li>{@link #P_INT_13_S1_FX} (<code>10100010001010101001100010010110</code>)</li>
     * <li>{@link #P_INT_13_S1_GX} (<code>11001001010010010010001010101010</code>)</li>
     * <li>{@link #P_INT_13_S1_J} (<code>11101001010010010100010001000101</code>)</li>
     * <li>{@link #P_INT_17_S0_A} (<code>00010001011011101101010111011001</code>)</li>
     * <li>{@link #P_INT_17_S0_AX} (<code>00100010110111011010101110110010</code>)</li>
     * <li>{@link #P_INT_17_S0_C} (<code>01000111000101011001101101110101</code>)</li>
     * <li>{@link #P_INT_17_S1_DX} (<code>10101011101100100101011100010110</code>)</li>
     * <li>{@link #P_INT_17_S1_F} (<code>10001110100101001011010110011011</code>)</li>
     * <li>{@link #P_INT_17_S1_H} (<code>10101110001001101010101001110101</code>)</li>
     * <li>{@link #P_INT_17_S1_J} (<code>11101110010101001100101001010011</code>)</li>
     * <li>{@link #P_INT_19_S0_AX} (<code>00100111010110101110101110101110</code>)</li>
     * <li>{@link #P_INT_19_S0_B} (<code>00101000111001101101110111011101</code>)</li>
     * <li>{@link #P_INT_19_S0_DX} (<code>01110111001100110111010111010010</code>)</li>
     * <li>{@link #P_INT_19_S1_J} (<code>11101110100111011001110100010011</code>)</li>
     * </ul>
     */
    public static final int P_INT_19_S0_E = 1503309527;

    /**
     * <code>10110011001101010101110110101110</code>
     * <p/>
     * Other patterns with half bit-flip-distance (16):
     * <ul>
     * <li>{@link #P_INT_13_S0_A} (<code>00010001001010110100101001100011</code>)</li>
     * <li>{@link #P_INT_13_S0_CX} (<code>01001001010100100010110110100010</code>)</li>
     * <li>{@link #P_INT_13_S0_DX} (<code>01100101000100110001001010110010</code>)</li>
     * <li>{@link #P_INT_17_S0_A} (<code>00010001011011101101010111011001</code>)</li>
     * <li>{@link #P_INT_17_S0_AX} (<code>00100010110111011010101110110010</code>)</li>
     * <li>{@link #P_INT_17_S0_B} (<code>00110010110111011000110001110011</code>)</li>
     * <li>{@link #P_INT_17_S0_C} (<code>01000111000101011001101101110101</code>)</li>
     * <li>{@link #P_INT_17_S1_CX} (<code>10001110001010110011011011101010</code>)</li>
     * <li>{@link #P_INT_17_S1_F} (<code>10001110100101001011010110011011</code>)</li>
     * <li>{@link #P_INT_19_S0_B} (<code>00101000111001101101110111011101</code>)</li>
     * <li>{@link #P_INT_19_S0_BX} (<code>01010001110011011011101110111010</code>)</li>
     * <li>{@link #P_INT_19_S0_C} (<code>00110100111001011011010111011101</code>)</li>
     * <li>{@link #P_INT_19_S0_D} (<code>00111011100110011011101011101001</code>)</li>
     * <li>{@link #P_INT_19_S1_H} (<code>10101110101010101101101110100101</code>)</li>
     * <li>{@link #P_INT_19_S1_I} (<code>11011010010101110010101110011011</code>)</li>
     * <li>{@link #P_INT_19_S1_J} (<code>11101110100111011001110100010011</code>)</li>
     * </ul>
     */
    public static final int P_INT_19_S1_EX = -1288348242;

    /**
     * <code>01100101011011100111011011101001</code>
     * <p/>
     * Other patterns with half bit-flip-distance (16):
     * <ul>
     * <li>{@link #P_INT_13_S0_AX} (<code>00100010010101101001010011000110</code>)</li>
     * <li>{@link #P_INT_13_S0_B} (<code>00010110001000111010001000101011</code>)</li>
     * <li>{@link #P_INT_13_S0_BX} (<code>00101100010001110100010001010110</code>)</li>
     * <li>{@link #P_INT_13_S0_CX} (<code>01001001010100100010110110100010</code>)</li>
     * <li>{@link #P_INT_13_S0_E} (<code>01000111001100100010101100010001</code>)</li>
     * <li>{@link #P_INT_13_S0_F} (<code>01010001000101010100110001001011</code>)</li>
     * <li>{@link #P_INT_13_S0_G} (<code>01100100101001001001000101010101</code>)</li>
     * <li>{@link #P_INT_13_S1_I} (<code>10100010001011010100110001000101</code>)</li>
     * <li>{@link #P_INT_17_S0_D} (<code>01010101110110010010101110001011</code>)</li>
     * <li>{@link #P_INT_17_S0_E} (<code>01101010010011001101101001010111</code>)</li>
     * <li>{@link #P_INT_17_S1_G} (<code>10100100110110101100110111010001</code>)</li>
     * <li>{@link #P_INT_17_S1_H} (<code>10101110001001101010101001110101</code>)</li>
     * <li>{@link #P_INT_19_S0_A} (<code>00010011101011010111010111010111</code>)</li>
     * <li>{@link #P_INT_19_S0_BX} (<code>01010001110011011011101110111010</code>)</li>
     * <li>{@link #P_INT_19_S0_D} (<code>00111011100110011011101011101001</code>)</li>
     * <li>{@link #P_INT_19_S1_H} (<code>10101110101010101101101110100101</code>)</li>
     * </ul>
     */
    public static final int P_INT_19_S0_F = 1701738217;

    /**
     * <code>11001010110111001110110111010010</code>
     * <p/>
     * Other patterns with half bit-flip-distance (16):
     * <ul>
     * <li>{@link #P_INT_13_S0_BX} (<code>00101100010001110100010001010110</code>)</li>
     * <li>{@link #P_INT_13_S0_D} (<code>00110010100010011000100101011001</code>)</li>
     * <li>{@link #P_INT_13_S0_F} (<code>01010001000101010100110001001011</code>)</li>
     * <li>{@link #P_INT_13_S1_EX} (<code>10001110011001000101011000100010</code>)</li>
     * <li>{@link #P_INT_13_S1_FX} (<code>10100010001010101001100010010110</code>)</li>
     * <li>{@link #P_INT_13_S1_GX} (<code>11001001010010010010001010101010</code>)</li>
     * <li>{@link #P_INT_13_S1_I} (<code>10100010001011010100110001000101</code>)</li>
     * <li>{@link #P_INT_13_S1_J} (<code>11101001010010010100010001000101</code>)</li>
     * <li>{@link #P_INT_17_S0_A} (<code>00010001011011101101010111011001</code>)</li>
     * <li>{@link #P_INT_17_S0_D} (<code>01010101110110010010101110001011</code>)</li>
     * <li>{@link #P_INT_17_S1_DX} (<code>10101011101100100101011100010110</code>)</li>
     * <li>{@link #P_INT_17_S1_EX} (<code>11010100100110011011010010101110</code>)</li>
     * <li>{@link #P_INT_17_S1_I} (<code>11010100010100111010011001010111</code>)</li>
     * <li>{@link #P_INT_19_S0_AX} (<code>00100111010110101110101110101110</code>)</li>
     * <li>{@link #P_INT_19_S0_DX} (<code>01110111001100110111010111010010</code>)</li>
     * <li>{@link #P_INT_19_S0_G} (<code>01110101110011000101101011010111</code>)</li>
     * </ul>
     */
    public static final int P_INT_19_S1_FX = -891490862;

    /**
     * <code>01110101110011000101101011010111</code>
     * <p/>
     * Other patterns with half bit-flip-distance (16):
     * <ul>
     * <li>{@link #P_INT_13_S0_AX} (<code>00100010010101101001010011000110</code>)</li>
     * <li>{@link #P_INT_13_S0_D} (<code>00110010100010011000100101011001</code>)</li>
     * <li>{@link #P_INT_13_S1_FX} (<code>10100010001010101001100010010110</code>)</li>
     * <li>{@link #P_INT_13_S1_I} (<code>10100010001011010100110001000101</code>)</li>
     * <li>{@link #P_INT_17_S0_AX} (<code>00100010110111011010101110110010</code>)</li>
     * <li>{@link #P_INT_17_S1_I} (<code>11010100010100111010011001010111</code>)</li>
     * <li>{@link #P_INT_19_S0_AX} (<code>00100111010110101110101110101110</code>)</li>
     * <li>{@link #P_INT_19_S0_D} (<code>00111011100110011011101011101001</code>)</li>
     * <li>{@link #P_INT_19_S0_DX} (<code>01110111001100110111010111010010</code>)</li>
     * <li>{@link #P_INT_19_S1_FX} (<code>11001010110111001110110111010010</code>)</li>
     * <li>{@link #P_INT_19_S1_H} (<code>10101110101010101101101110100101</code>)</li>
     * <li>{@link #P_INT_19_S1_J} (<code>11101110100111011001110100010011</code>)</li>
     * </ul>
     */
    public static final int P_INT_19_S0_G = 1976326871;

    /**
     * <code>11101011100110001011010110101110</code>
     * <p/>
     * Other patterns with half bit-flip-distance (16):
     * <ul>
     * <li>{@link #P_INT_13_S0_DX} (<code>01100101000100110001001010110010</code>)</li>
     * <li>{@link #P_INT_13_S1_J} (<code>11101001010010010100010001000101</code>)</li>
     * <li>{@link #P_INT_17_S0_D} (<code>01010101110110010010101110001011</code>)</li>
     * <li>{@link #P_INT_19_S0_A} (<code>00010011101011010111010111010111</code>)</li>
     * <li>{@link #P_INT_19_S0_DX} (<code>01110111001100110111010111010010</code>)</li>
     * </ul>
     */
    public static final int P_INT_19_S1_GX = -342313554;

    /**
     * <code>10101110101010101101101110100101</code>
     * <p/>
     * Other patterns with half bit-flip-distance (16):
     * <ul>
     * <li>{@link #P_INT_13_S0_A} (<code>00010001001010110100101001100011</code>)</li>
     * <li>{@link #P_INT_13_S0_B} (<code>00010110001000111010001000101011</code>)</li>
     * <li>{@link #P_INT_13_S0_D} (<code>00110010100010011000100101011001</code>)</li>
     * <li>{@link #P_INT_13_S0_E} (<code>01000111001100100010101100010001</code>)</li>
     * <li>{@link #P_INT_17_S0_AX} (<code>00100010110111011010101110110010</code>)</li>
     * <li>{@link #P_INT_17_S0_C} (<code>01000111000101011001101101110101</code>)</li>
     * <li>{@link #P_INT_17_S1_F} (<code>10001110100101001011010110011011</code>)</li>
     * <li>{@link #P_INT_17_S1_J} (<code>11101110010101001100101001010011</code>)</li>
     * <li>{@link #P_INT_19_S0_CX} (<code>01101001110010110110101110111010</code>)</li>
     * <li>{@link #P_INT_19_S0_F} (<code>01100101011011100111011011101001</code>)</li>
     * <li>{@link #P_INT_19_S0_G} (<code>01110101110011000101101011010111</code>)</li>
     * <li>{@link #P_INT_19_S1_EX} (<code>10110011001101010101110110101110</code>)</li>
     * </ul>
     */
    public static final int P_INT_19_S1_H = -1364534363;

    /**
     * <code>11011010010101110010101110011011</code>
     * <p/>
     * Other patterns with half bit-flip-distance (16):
     * <ul>
     * <li>{@link #P_INT_13_S0_D} (<code>00110010100010011000100101011001</code>)</li>
     * <li>{@link #P_INT_13_S0_DX} (<code>01100101000100110001001010110010</code>)</li>
     * <li>{@link #P_INT_13_S1_H} (<code>10001010001001001001100010101101</code>)</li>
     * <li>{@link #P_INT_17_S0_B} (<code>00110010110111011000110001110011</code>)</li>
     * <li>{@link #P_INT_17_S0_C} (<code>01000111000101011001101101110101</code>)</li>
     * <li>{@link #P_INT_17_S0_E} (<code>01101010010011001101101001010111</code>)</li>
     * <li>{@link #P_INT_17_S1_CX} (<code>10001110001010110011011011101010</code>)</li>
     * <li>{@link #P_INT_17_S1_H} (<code>10101110001001101010101001110101</code>)</li>
     * <li>{@link #P_INT_19_S0_AX} (<code>00100111010110101110101110101110</code>)</li>
     * <li>{@link #P_INT_19_S0_D} (<code>00111011100110011011101011101001</code>)</li>
     * <li>{@link #P_INT_19_S0_DX} (<code>01110111001100110111010111010010</code>)</li>
     * <li>{@link #P_INT_19_S1_EX} (<code>10110011001101010101110110101110</code>)</li>
     * </ul>
     */
    public static final int P_INT_19_S1_I = -631821413;

    /**
     * <code>11101110100111011001110100010011</code>
     * <p/>
     * Other patterns with half bit-flip-distance (16):
     * <ul>
     * <li>{@link #P_INT_13_S0_AX} (<code>00100010010101101001010011000110</code>)</li>
     * <li>{@link #P_INT_13_S0_BX} (<code>00101100010001110100010001010110</code>)</li>
     * <li>{@link #P_INT_13_S0_DX} (<code>01100101000100110001001010110010</code>)</li>
     * <li>{@link #P_INT_13_S0_E} (<code>01000111001100100010101100010001</code>)</li>
     * <li>{@link #P_INT_13_S0_F} (<code>01010001000101010100110001001011</code>)</li>
     * <li>{@link #P_INT_13_S1_EX} (<code>10001110011001000101011000100010</code>)</li>
     * <li>{@link #P_INT_13_S1_H} (<code>10001010001001001001100010101101</code>)</li>
     * <li>{@link #P_INT_13_S1_J} (<code>11101001010010010100010001000101</code>)</li>
     * <li>{@link #P_INT_17_S0_BX} (<code>01100101101110110001100011100110</code>)</li>
     * <li>{@link #P_INT_17_S0_D} (<code>01010101110110010010101110001011</code>)</li>
     * <li>{@link #P_INT_17_S1_H} (<code>10101110001001101010101001110101</code>)</li>
     * <li>{@link #P_INT_17_S1_I} (<code>11010100010100111010011001010111</code>)</li>
     * <li>{@link #P_INT_19_S0_A} (<code>00010011101011010111010111010111</code>)</li>
     * <li>{@link #P_INT_19_S0_B} (<code>00101000111001101101110111011101</code>)</li>
     * <li>{@link #P_INT_19_S0_BX} (<code>01010001110011011011101110111010</code>)</li>
     * <li>{@link #P_INT_19_S0_C} (<code>00110100111001011011010111011101</code>)</li>
     * <li>{@link #P_INT_19_S0_D} (<code>00111011100110011011101011101001</code>)</li>
     * <li>{@link #P_INT_19_S0_DX} (<code>01110111001100110111010111010010</code>)</li>
     * <li>{@link #P_INT_19_S0_E} (<code>01011001100110101010111011010111</code>)</li>
     * <li>{@link #P_INT_19_S0_G} (<code>01110101110011000101101011010111</code>)</li>
     * <li>{@link #P_INT_19_S1_EX} (<code>10110011001101010101110110101110</code>)</li>
     * </ul>
     */
    public static final int P_INT_19_S1_J = -291660525;

    // Long patterns with 23 bits set

    /**
     * <code>0001000100010001001000110100100010001001100101101000100100111001</code>
     * <p/>
     * Other patterns with half bit-flip-distance (32):
     * <ul>
     * <li>{@link #P_LONG_23_S0_D} (<code>0010010101001001001001001001000101010001001000100011000111000101</code>)</li>
     * <li>{@link #P_LONG_23_S0_E} (<code>0100010001100010100010001000101011010001001001001110001000100011</code>)</li>
     * <li>{@link #P_LONG_31_S0_D} (<code>0100010101100101011010011010101101101110011010100100101100010001</code>)</li>
     * <li>{@link #P_LONG_31_S0_F} (<code>0111000100011100101011101101010110010101100010010110010011010001</code>)</li>
     * <li>{@link #P_LONG_31_S1_I} (<code>1100101101011010010100110101010001010101001010010100101110110001</code>)</li>
     * <li>{@link #P_LONG_41_S0_AX} (<code>0010110110110110011101011100111011101101101001101110111011101110</code>)</li>
     * <li>{@link #P_LONG_41_S0_D} (<code>0110101101010101110101110110111011100011101101011101011011011011</code>)</li>
     * <li>{@link #P_LONG_41_S1_I} (<code>1101110110110111010110010011101010010110110101101011011101110111</code>)</li>
     * </ul>
     */
    public static final long P_LONG_23_S0_A = 1229802967701817657L;

    /**
     * <code>0010001000100010010001101001000100010011001011010001001001110010</code>
     * <p/>
     * Other patterns with half bit-flip-distance (32):
     * <ul>
     * <li>{@link #P_LONG_23_S0_DX} (<code>0100101010010010010010010010001010100010010001000110001110001010</code>)</li>
     * <li>{@link #P_LONG_23_S1_EX} (<code>1000100011000101000100010001010110100010010010011100010001000110</code>)</li>
     * <li>{@link #P_LONG_23_S1_H} (<code>1001000100010001001010001100010101000100101100110001001100010001</code>)</li>
     * <li>{@link #P_LONG_31_S0_A} (<code>0001010010110010100100110011010010011011001011100110110111000101</code>)</li>
     * <li>{@link #P_LONG_31_S1_DX} (<code>1000101011001010110100110101011011011100110101001001011000100010</code>)</li>
     * <li>{@link #P_LONG_31_S1_FX} (<code>1110001000111001010111011010101100101011000100101100100110100010</code>)</li>
     * <li>{@link #P_LONG_41_S0_F} (<code>0111011101110111010010101110101101110100111010111010011011011011</code>)</li>
     * <li>{@link #P_LONG_41_S1_DX} (<code>1101011010101011101011101101110111000111011010111010110110110110</code>)</li>
     * </ul>
     */
    public static final long P_LONG_23_S0_AX = 2459605935403635314L;

    /**
     * <code>0001001000101001010100010010011100011001010001100100100100010001</code>
     * <p/>
     * Other patterns with half bit-flip-distance (32):
     * <ul>
     * <li>{@link #P_LONG_23_S0_CX} (<code>0100010001010010001100011000101000100100010001001001100110001110</code>)</li>
     * <li>{@link #P_LONG_23_S0_E} (<code>0100010001100010100010001000101011010001001001001110001000100011</code>)</li>
     * <li>{@link #P_LONG_23_S1_EX} (<code>1000100011000101000100010001010110100010010010011100010001000110</code>)</li>
     * <li>{@link #P_LONG_31_S0_C} (<code>0011000101110001101010101100100100011101011100010011000101011101</code>)</li>
     * <li>{@link #P_LONG_31_S1_DX} (<code>1000101011001010110100110101011011011100110101001001011000100010</code>)</li>
     * <li>{@link #P_LONG_31_S1_H} (<code>1010010100111001100100101001010101100100101101000110110101000111</code>)</li>
     * <li>{@link #P_LONG_41_S0_A} (<code>0001011011011011001110101110011101110110110100110111011101110111</code>)</li>
     * <li>{@link #P_LONG_41_S0_C} (<code>0101101110111011011101101110111011011100100101101011010101101101</code>)</li>
     * <li>{@link #P_LONG_41_S1_FX} (<code>1110111011101110100101011101011011101001110101110100110110110110</code>)</li>
     * </ul>
     */
    public static final long P_LONG_23_S0_B = 1308666395097450769L;

    /**
     * <code>0010010001010010101000100100111000110010100011001001001000100010</code>
     * <p/>
     * Other patterns with half bit-flip-distance (32):
     * <ul>
     * <li>{@link #P_LONG_23_S1_EX} (<code>1000100011000101000100010001010110100010010010011100010001000110</code>)</li>
     * <li>{@link #P_LONG_23_S1_H} (<code>1001000100010001001010001100010101000100101100110001001100010001</code>)</li>
     * <li>{@link #P_LONG_31_S0_CX} (<code>0110001011100011010101011001001000111010111000100110001010111010</code>)</li>
     * <li>{@link #P_LONG_41_S0_AX} (<code>0010110110110110011101011100111011101101101001101110111011101110</code>)</li>
     * <li>{@link #P_LONG_41_S0_F} (<code>0111011101110111010010101110101101110100111010111010011011011011</code>)</li>
     * <li>{@link #P_LONG_41_S1_CX} (<code>1011011101110110111011011101110110111001001011010110101011011010</code>)</li>
     * <li>{@link #P_LONG_41_S1_G} (<code>1000111011101110111011100111011101100110111010100111011100011011</code>)</li>
     * </ul>
     */
    public static final long P_LONG_23_S0_BX = 2617332790194901538L;

    /**
     * <code>0010001000101001000110001100010100010010001000100100110011000111</code>
     * <p/>
     * Other patterns with half bit-flip-distance (32):
     * <ul>
     * <li>{@link #P_LONG_23_S0_F} (<code>0110100100010001010100010010001010001100100100111000100010001001</code>)</li>
     * <li>{@link #P_LONG_31_S0_AX} (<code>0010100101100101001001100110100100110110010111001101101110001010</code>)</li>
     * <li>{@link #P_LONG_31_S0_BX} (<code>0100100110010011001011010101011101101010010010110101010100100110</code>)</li>
     * <li>{@link #P_LONG_31_S0_D} (<code>0100010101100101011010011010101101101110011010100100101100010001</code>)</li>
     * <li>{@link #P_LONG_31_S1_I} (<code>1100101101011010010100110101010001010101001010010100101110110001</code>)</li>
     * <li>{@link #P_LONG_41_S0_F} (<code>0111011101110111010010101110101101110100111010111010011011011011</code>)</li>
     * <li>{@link #P_LONG_41_S1_EX} (<code>1110011100010111011101101110110110011011011010111011101011101010</code>)</li>
     * </ul>
     */
    public static final long P_LONG_23_S0_C = 2461525906026548423L;

    /**
     * <code>0100010001010010001100011000101000100100010001001001100110001110</code>
     * <p/>
     * Other patterns with half bit-flip-distance (32):
     * <ul>
     * <li>{@link #P_LONG_23_S0_B} (<code>0001001000101001010100010010011100011001010001100100100100010001</code>)</li>
     * <li>{@link #P_LONG_23_S1_FX} (<code>1101001000100010101000100100010100011001001001110001000100010010</code>)</li>
     * <li>{@link #P_LONG_23_S1_J} (<code>1110011000101000100100010001001100010001000100010100101001000101</code>)</li>
     * <li>{@link #P_LONG_31_S0_C} (<code>0011000101110001101010101100100100011101011100010011000101011101</code>)</li>
     * <li>{@link #P_LONG_31_S1_DX} (<code>1000101011001010110100110101011011011100110101001001011000100010</code>)</li>
     * <li>{@link #P_LONG_31_S1_H} (<code>1010010100111001100100101001010101100100101101000110110101000111</code>)</li>
     * <li>{@link #P_LONG_41_S1_FX} (<code>1110111011101110100101011101011011101001110101110100110110110110</code>)</li>
     * <li>{@link #P_LONG_41_S1_I} (<code>1101110110110111010110010011101010010110110101101011011101110111</code>)</li>
     * <li>{@link #P_LONG_41_S1_J} (<code>1110111011101011011101110011100011010110111011101010010110101011</code>)</li>
     * </ul>
     */
    public static final long P_LONG_23_S0_CX = 4923051812053096846L;

    /**
     * <code>0010010101001001001001001001000101010001001000100011000111000101</code>
     * <p/>
     * Other patterns with half bit-flip-distance (32):
     * <ul>
     * <li>{@link #P_LONG_23_S0_A} (<code>0001000100010001001000110100100010001001100101101000100100111001</code>)</li>
     * <li>{@link #P_LONG_31_S0_AX} (<code>0010100101100101001001100110100100110110010111001101101110001010</code>)</li>
     * <li>{@link #P_LONG_31_S0_BX} (<code>0100100110010011001011010101011101101010010010110101010100100110</code>)</li>
     * <li>{@link #P_LONG_31_S0_CX} (<code>0110001011100011010101011001001000111010111000100110001010111010</code>)</li>
     * <li>{@link #P_LONG_31_S0_E} (<code>0101101000111010011101001010100010110001010111000101100011000111</code>)</li>
     * <li>{@link #P_LONG_31_S1_G} (<code>1000100010100010101100101101110110010100011001011001011101001101</code>)</li>
     * <li>{@link #P_LONG_31_S1_I} (<code>1100101101011010010100110101010001010101001010010100101110110001</code>)</li>
     * <li>{@link #P_LONG_41_S0_A} (<code>0001011011011011001110101110011101110110110100110111011101110111</code>)</li>
     * <li>{@link #P_LONG_41_S1_DX} (<code>1101011010101011101011101101110111000111011010111010110110110110</code>)</li>
     * <li>{@link #P_LONG_41_S1_EX} (<code>1110011100010111011101101110110110011011011010111011101011101010</code>)</li>
     * <li>{@link #P_LONG_41_S1_J} (<code>1110111011101011011101110011100011010110111011101010010110101011</code>)</li>
     * </ul>
     */
    public static final long P_LONG_23_S0_D = 2686718859253264837L;

    /**
     * <code>0100101010010010010010010010001010100010010001000110001110001010</code>
     * <p/>
     * Other patterns with half bit-flip-distance (32):
     * <ul>
     * <li>{@link #P_LONG_23_S0_AX} (<code>0010001000100010010001101001000100010011001011010001001001110010</code>)</li>
     * <li>{@link #P_LONG_31_S1_EX} (<code>1011010001110100111010010101000101100010101110001011000110001110</code>)</li>
     * <li>{@link #P_LONG_31_S1_I} (<code>1100101101011010010100110101010001010101001010010100101110110001</code>)</li>
     * <li>{@link #P_LONG_41_S0_AX} (<code>0010110110110110011101011100111011101101101001101110111011101110</code>)</li>
     * <li>{@link #P_LONG_41_S0_B} (<code>0011101100110110110111011011011011101101110111001101110101010011</code>)</li>
     * </ul>
     */
    public static final long P_LONG_23_S0_DX = 5373437718506529674L;

    /**
     * <code>0100010001100010100010001000101011010001001001001110001000100011</code>
     * <p/>
     * Other patterns with half bit-flip-distance (32):
     * <ul>
     * <li>{@link #P_LONG_23_S0_A} (<code>0001000100010001001000110100100010001001100101101000100100111001</code>)</li>
     * <li>{@link #P_LONG_23_S0_B} (<code>0001001000101001010100010010011100011001010001100100100100010001</code>)</li>
     * <li>{@link #P_LONG_31_S0_C} (<code>0011000101110001101010101100100100011101011100010011000101011101</code>)</li>
     * <li>{@link #P_LONG_31_S1_FX} (<code>1110001000111001010111011010101100101011000100101100100110100010</code>)</li>
     * <li>{@link #P_LONG_31_S1_H} (<code>1010010100111001100100101001010101100100101101000110110101000111</code>)</li>
     * <li>{@link #P_LONG_31_S1_I} (<code>1100101101011010010100110101010001010101001010010100101110110001</code>)</li>
     * <li>{@link #P_LONG_41_S1_DX} (<code>1101011010101011101011101101110111000111011010111010110110110110</code>)</li>
     * <li>{@link #P_LONG_41_S1_I} (<code>1101110110110111010110010011101010010110110101101011011101110111</code>)</li>
     * <li>{@link #P_LONG_41_S1_J} (<code>1110111011101011011101110011100011010110111011101010010110101011</code>)</li>
     * </ul>
     */
    public static final long P_LONG_23_S0_E = 4927651072092463651L;

    /**
     * <code>1000100011000101000100010001010110100010010010011100010001000110</code>
     * <p/>
     * Other patterns with half bit-flip-distance (32):
     * <ul>
     * <li>{@link #P_LONG_23_S0_AX} (<code>0010001000100010010001101001000100010011001011010001001001110010</code>)</li>
     * <li>{@link #P_LONG_23_S0_B} (<code>0001001000101001010100010010011100011001010001100100100100010001</code>)</li>
     * <li>{@link #P_LONG_23_S0_BX} (<code>0010010001010010101000100100111000110010100011001001001000100010</code>)</li>
     * <li>{@link #P_LONG_23_S0_F} (<code>0110100100010001010100010010001010001100100100111000100010001001</code>)</li>
     * <li>{@link #P_LONG_31_S0_CX} (<code>0110001011100011010101011001001000111010111000100110001010111010</code>)</li>
     * <li>{@link #P_LONG_41_S0_D} (<code>0110101101010101110101110110111011100011101101011101011011011011</code>)</li>
     * <li>{@link #P_LONG_41_S1_I} (<code>1101110110110111010110010011101010010110110101101011011101110111</code>)</li>
     * </ul>
     */
    public static final long P_LONG_23_S1_EX = -8591441929524624314L;

    /**
     * <code>0110100100010001010100010010001010001100100100111000100010001001</code>
     * <p/>
     * Other patterns with half bit-flip-distance (32):
     * <ul>
     * <li>{@link #P_LONG_23_S0_C} (<code>0010001000101001000110001100010100010010001000100100110011000111</code>)</li>
     * <li>{@link #P_LONG_23_S1_EX} (<code>1000100011000101000100010001010110100010010010011100010001000110</code>)</li>
     * <li>{@link #P_LONG_31_S0_AX} (<code>0010100101100101001001100110100100110110010111001101101110001010</code>)</li>
     * <li>{@link #P_LONG_31_S0_E} (<code>0101101000111010011101001010100010110001010111000101100011000111</code>)</li>
     * <li>{@link #P_LONG_31_S1_DX} (<code>1000101011001010110100110101011011011100110101001001011000100010</code>)</li>
     * <li>{@link #P_LONG_41_S0_F} (<code>0111011101110111010010101110101101110100111010111010011011011011</code>)</li>
     * <li>{@link #P_LONG_41_S1_EX} (<code>1110011100010111011101101110110110011011011010111011101011101010</code>)</li>
     * <li>{@link #P_LONG_41_S1_J} (<code>1110111011101011011101110011100011010110111011101010010110101011</code>)</li>
     * </ul>
     */
    public static final long P_LONG_23_S0_F = 7570921657415731337L;

    /**
     * <code>1101001000100010101000100100010100011001001001110001000100010010</code>
     * <p/>
     * Other patterns with half bit-flip-distance (32):
     * <ul>
     * <li>{@link #P_LONG_23_S0_CX} (<code>0100010001010010001100011000101000100100010001001001100110001110</code>)</li>
     * <li>{@link #P_LONG_31_S0_E} (<code>0101101000111010011101001010100010110001010111000101100011000111</code>)</li>
     * <li>{@link #P_LONG_31_S1_EX} (<code>1011010001110100111010010101000101100010101110001011000110001110</code>)</li>
     * <li>{@link #P_LONG_41_S1_FX} (<code>1110111011101110100101011101011011101001110101110100110110110110</code>)</li>
     * <li>{@link #P_LONG_41_S1_G} (<code>1000111011101110111011100111011101100110111010100111011100011011</code>)</li>
     * </ul>
     */
    public static final long P_LONG_23_S1_FX = -3304900758878088942L;

    /**
     * <code>1000100010001001000100010001010110010110100010100010100101001001</code>
     * <p/>
     * Other patterns with half bit-flip-distance (32):
     * <ul>
     * <li>{@link #P_LONG_31_S0_B} (<code>0010010011001001100101101010101110110101001001011010101010010011</code>)</li>
     * <li>{@link #P_LONG_31_S0_BX} (<code>0100100110010011001011010101011101101010010010110101010100100110</code>)</li>
     * <li>{@link #P_LONG_31_S0_CX} (<code>0110001011100011010101011001001000111010111000100110001010111010</code>)</li>
     * <li>{@link #P_LONG_41_S0_C} (<code>0101101110111011011101101110111011011100100101101011010101101101</code>)</li>
     * <li>{@link #P_LONG_41_S1_G} (<code>1000111011101110111011100111011101100110111010100111011100011011</code>)</li>
     * </ul>
     */
    public static final long P_LONG_23_S1_G = -8608330428324370103L;

    /**
     * <code>1001000100010001001010001100010101000100101100110001001100010001</code>
     * <p/>
     * Other patterns with half bit-flip-distance (32):
     * <ul>
     * <li>{@link #P_LONG_23_S0_AX} (<code>0010001000100010010001101001000100010011001011010001001001110010</code>)</li>
     * <li>{@link #P_LONG_23_S0_BX} (<code>0010010001010010101000100100111000110010100011001001001000100010</code>)</li>
     * <li>{@link #P_LONG_41_S0_C} (<code>0101101110111011011101101110111011011100100101101011010101101101</code>)</li>
     * <li>{@link #P_LONG_41_S1_DX} (<code>1101011010101011101011101101110111000111011010111010110110110110</code>)</li>
     * <li>{@link #P_LONG_41_S1_G} (<code>1000111011101110111011100111011101100110111010100111011100011011</code>)</li>
     * </ul>
     */
    public static final long P_LONG_23_S1_H = -7993563035879664879L;

    /**
     * <code>1010001000110011000110001001000110010001000101000100010001011001</code>
     * <p/>
     * Other patterns with half bit-flip-distance (32):
     * <ul>
     * <li>{@link #P_LONG_31_S0_B} (<code>0010010011001001100101101010101110110101001001011010101010010011</code>)</li>
     * <li>{@link #P_LONG_31_S1_I} (<code>1100101101011010010100110101010001010101001010010100101110110001</code>)</li>
     * <li>{@link #P_LONG_41_S0_E} (<code>0111001110001011101110110111011011001101101101011101110101110101</code>)</li>
     * <li>{@link #P_LONG_41_S1_H} (<code>1011101110111010110011101010011011001101101110111010111011011001</code>)</li>
     * <li>{@link #P_LONG_41_S1_I} (<code>1101110110110111010110010011101010010110110101101011011101110111</code>)</li>
     * </ul>
     */
    public static final long P_LONG_23_S1_I = -6759031602269633447L;

    /**
     * <code>1110011000101000100100010001001100010001000100010100101001000101</code>
     * <p/>
     * Other patterns with half bit-flip-distance (32):
     * <ul>
     * <li>{@link #P_LONG_23_S0_CX} (<code>0100010001010010001100011000101000100100010001001001100110001110</code>)</li>
     * <li>{@link #P_LONG_31_S0_C} (<code>0011000101110001101010101100100100011101011100010011000101011101</code>)</li>
     * <li>{@link #P_LONG_31_S0_CX} (<code>0110001011100011010101011001001000111010111000100110001010111010</code>)</li>
     * <li>{@link #P_LONG_31_S1_DX} (<code>1000101011001010110100110101011011011100110101001001011000100010</code>)</li>
     * <li>{@link #P_LONG_41_S1_CX} (<code>1011011101110110111011011101110110111001001011010110101011011010</code>)</li>
     * </ul>
     */
    public static final long P_LONG_23_S1_J = -1862078934840948155L;

    // Long patterns with 31 bits set

    /**
     * <code>0001010010110010100100110011010010011011001011100110110111000101</code>
     * <p/>
     * Other patterns with half bit-flip-distance (32):
     * <ul>
     * <li>{@link #P_LONG_23_S0_AX} (<code>0010001000100010010001101001000100010011001011010001001001110010</code>)</li>
     * <li>{@link #P_LONG_31_S0_B} (<code>0010010011001001100101101010101110110101001001011010101010010011</code>)</li>
     * <li>{@link #P_LONG_41_S0_BX} (<code>0111011001101101101110110110110111011011101110011011101010100110</code>)</li>
     * <li>{@link #P_LONG_41_S0_C} (<code>0101101110111011011101101110111011011100100101101011010101101101</code>)</li>
     * <li>{@link #P_LONG_41_S1_FX} (<code>1110111011101110100101011101011011101001110101110100110110110110</code>)</li>
     * </ul>
     */
    public static final long P_LONG_31_S0_A = 1491416280764149189L;

    /**
     * <code>0010100101100101001001100110100100110110010111001101101110001010</code>
     * <p/>
     * Other patterns with half bit-flip-distance (32):
     * <ul>
     * <li>{@link #P_LONG_23_S0_C} (<code>0010001000101001000110001100010100010010001000100100110011000111</code>)</li>
     * <li>{@link #P_LONG_23_S0_D} (<code>0010010101001001001001001001000101010001001000100011000111000101</code>)</li>
     * <li>{@link #P_LONG_23_S0_F} (<code>0110100100010001010100010010001010001100100100111000100010001001</code>)</li>
     * <li>{@link #P_LONG_31_S0_BX} (<code>0100100110010011001011010101011101101010010010110101010100100110</code>)</li>
     * <li>{@link #P_LONG_41_S1_CX} (<code>1011011101110110111011011101110110111001001011010110101011011010</code>)</li>
     * </ul>
     */
    public static final long P_LONG_31_S0_AX = 2982832561528298378L;

    /**
     * <code>0010010011001001100101101010101110110101001001011010101010010011</code>
     * <p/>
     * Other patterns with half bit-flip-distance (32):
     * <ul>
     * <li>{@link #P_LONG_23_S1_G} (<code>1000100010001001000100010001010110010110100010100010100101001001</code>)</li>
     * <li>{@link #P_LONG_23_S1_I} (<code>1010001000110011000110001001000110010001000101000100010001011001</code>)</li>
     * <li>{@link #P_LONG_31_S0_A} (<code>0001010010110010100100110011010010011011001011100110110111000101</code>)</li>
     * <li>{@link #P_LONG_31_S0_D} (<code>0100010101100101011010011010101101101110011010100100101100010001</code>)</li>
     * <li>{@link #P_LONG_31_S0_E} (<code>0101101000111010011101001010100010110001010111000101100011000111</code>)</li>
     * <li>{@link #P_LONG_41_S0_A} (<code>0001011011011011001110101110011101110110110100110111011101110111</code>)</li>
     * <li>{@link #P_LONG_41_S0_AX} (<code>0010110110110110011101011100111011101101101001101110111011101110</code>)</li>
     * </ul>
     */
    public static final long P_LONG_31_S0_B = 2650815519906966163L;

    /**
     * <code>0100100110010011001011010101011101101010010010110101010100100110</code>
     * <p/>
     * Other patterns with half bit-flip-distance (32):
     * <ul>
     * <li>{@link #P_LONG_23_S0_C} (<code>0010001000101001000110001100010100010010001000100100110011000111</code>)</li>
     * <li>{@link #P_LONG_23_S0_D} (<code>0010010101001001001001001001000101010001001000100011000111000101</code>)</li>
     * <li>{@link #P_LONG_23_S1_G} (<code>1000100010001001000100010001010110010110100010100010100101001001</code>)</li>
     * <li>{@link #P_LONG_31_S0_AX} (<code>0010100101100101001001100110100100110110010111001101101110001010</code>)</li>
     * <li>{@link #P_LONG_31_S1_DX} (<code>1000101011001010110100110101011011011100110101001001011000100010</code>)</li>
     * <li>{@link #P_LONG_31_S1_EX} (<code>1011010001110100111010010101000101100010101110001011000110001110</code>)</li>
     * <li>{@link #P_LONG_31_S1_I} (<code>1100101101011010010100110101010001010101001010010100101110110001</code>)</li>
     * <li>{@link #P_LONG_41_S0_AX} (<code>0010110110110110011101011100111011101101101001101110111011101110</code>)</li>
     * <li>{@link #P_LONG_41_S0_B} (<code>0011101100110110110111011011011011101101110111001101110101010011</code>)</li>
     * <li>{@link #P_LONG_41_S0_C} (<code>0101101110111011011101101110111011011100100101101011010101101101</code>)</li>
     * <li>{@link #P_LONG_41_S1_I} (<code>1101110110110111010110010011101010010110110101101011011101110111</code>)</li>
     * </ul>
     */
    public static final long P_LONG_31_S0_BX = 5301631039813932326L;

    /**
     * <code>0011000101110001101010101100100100011101011100010011000101011101</code>
     * <p/>
     * Other patterns with half bit-flip-distance (32):
     * <ul>
     * <li>{@link #P_LONG_23_S0_B} (<code>0001001000101001010100010010011100011001010001100100100100010001</code>)</li>
     * <li>{@link #P_LONG_23_S0_CX} (<code>0100010001010010001100011000101000100100010001001001100110001110</code>)</li>
     * <li>{@link #P_LONG_23_S0_E} (<code>0100010001100010100010001000101011010001001001001110001000100011</code>)</li>
     * <li>{@link #P_LONG_23_S1_J} (<code>1110011000101000100100010001001100010001000100010100101001000101</code>)</li>
     * <li>{@link #P_LONG_41_S1_DX} (<code>1101011010101011101011101101110111000111011010111010110110110110</code>)</li>
     * <li>{@link #P_LONG_41_S1_EX} (<code>1110011100010111011101101110110110011011011010111011101011101010</code>)</li>
     * <li>{@link #P_LONG_41_S1_H} (<code>1011101110111010110011101010011011001101101110111010111011011001</code>)</li>
     * </ul>
     */
    public static final long P_LONG_31_S0_C = 3562816560985878877L;

    /**
     * <code>0110001011100011010101011001001000111010111000100110001010111010</code>
     * <p/>
     * Other patterns with half bit-flip-distance (32):
     * <ul>
     * <li>{@link #P_LONG_23_S0_BX} (<code>0010010001010010101000100100111000110010100011001001001000100010</code>)</li>
     * <li>{@link #P_LONG_23_S0_D} (<code>0010010101001001001001001001000101010001001000100011000111000101</code>)</li>
     * <li>{@link #P_LONG_23_S1_EX} (<code>1000100011000101000100010001010110100010010010011100010001000110</code>)</li>
     * <li>{@link #P_LONG_23_S1_G} (<code>1000100010001001000100010001010110010110100010100010100101001001</code>)</li>
     * <li>{@link #P_LONG_23_S1_J} (<code>1110011000101000100100010001001100010001000100010100101001000101</code>)</li>
     * <li>{@link #P_LONG_31_S1_I} (<code>1100101101011010010100110101010001010101001010010100101110110001</code>)</li>
     * <li>{@link #P_LONG_41_S0_A} (<code>0001011011011011001110101110011101110110110100110111011101110111</code>)</li>
     * <li>{@link #P_LONG_41_S0_D} (<code>0110101101010101110101110110111011100011101101011101011011011011</code>)</li>
     * <li>{@link #P_LONG_41_S1_I} (<code>1101110110110111010110010011101010010110110101101011011101110111</code>)</li>
     * </ul>
     */
    public static final long P_LONG_31_S0_CX = 7125633121971757754L;

    /**
     * <code>0100010101100101011010011010101101101110011010100100101100010001</code>
     * <p/>
     * Other patterns with half bit-flip-distance (32):
     * <ul>
     * <li>{@link #P_LONG_23_S0_A} (<code>0001000100010001001000110100100010001001100101101000100100111001</code>)</li>
     * <li>{@link #P_LONG_23_S0_C} (<code>0010001000101001000110001100010100010010001000100100110011000111</code>)</li>
     * <li>{@link #P_LONG_31_S0_B} (<code>0010010011001001100101101010101110110101001001011010101010010011</code>)</li>
     * <li>{@link #P_LONG_31_S1_EX} (<code>1011010001110100111010010101000101100010101110001011000110001110</code>)</li>
     * <li>{@link #P_LONG_31_S1_I} (<code>1100101101011010010100110101010001010101001010010100101110110001</code>)</li>
     * <li>{@link #P_LONG_31_S1_J} (<code>1110110011001100010001010001100010100101010100111000111011010101</code>)</li>
     * <li>{@link #P_LONG_41_S0_A} (<code>0001011011011011001110101110011101110110110100110111011101110111</code>)</li>
     * <li>{@link #P_LONG_41_S0_B} (<code>0011101100110110110111011011011011101101110111001101110101010011</code>)</li>
     * <li>{@link #P_LONG_41_S1_CX} (<code>1011011101110110111011011101110110111001001011010110101011011010</code>)</li>
     * <li>{@link #P_LONG_41_S1_I} (<code>1101110110110111010110010011101010010110110101101011011101110111</code>)</li>
     * </ul>
     */
    public static final long P_LONG_31_S0_D = 5000519146277587729L;

    /**
     * <code>1000101011001010110100110101011011011100110101001001011000100010</code>
     * <p/>
     * Other patterns with half bit-flip-distance (32):
     * <ul>
     * <li>{@link #P_LONG_23_S0_AX} (<code>0010001000100010010001101001000100010011001011010001001001110010</code>)</li>
     * <li>{@link #P_LONG_23_S0_B} (<code>0001001000101001010100010010011100011001010001100100100100010001</code>)</li>
     * <li>{@link #P_LONG_23_S0_CX} (<code>0100010001010010001100011000101000100100010001001001100110001110</code>)</li>
     * <li>{@link #P_LONG_23_S0_F} (<code>0110100100010001010100010010001010001100100100111000100010001001</code>)</li>
     * <li>{@link #P_LONG_23_S1_J} (<code>1110011000101000100100010001001100010001000100010100101001000101</code>)</li>
     * <li>{@link #P_LONG_31_S0_BX} (<code>0100100110010011001011010101011101101010010010110101010100100110</code>)</li>
     * <li>{@link #P_LONG_31_S1_J} (<code>1110110011001100010001010001100010100101010100111000111011010101</code>)</li>
     * <li>{@link #P_LONG_41_S0_AX} (<code>0010110110110110011101011100111011101101101001101110111011101110</code>)</li>
     * <li>{@link #P_LONG_41_S0_BX} (<code>0111011001101101101110110110110111011011101110011011101010100110</code>)</li>
     * <li>{@link #P_LONG_41_S1_H} (<code>1011101110111010110011101010011011001101101110111010111011011001</code>)</li>
     * </ul>
     */
    public static final long P_LONG_31_S1_DX = -8445705781154376158L;

    /**
     * <code>0101101000111010011101001010100010110001010111000101100011000111</code>
     * <p/>
     * Other patterns with half bit-flip-distance (32):
     * <ul>
     * <li>{@link #P_LONG_23_S0_D} (<code>0010010101001001001001001001000101010001001000100011000111000101</code>)</li>
     * <li>{@link #P_LONG_23_S0_F} (<code>0110100100010001010100010010001010001100100100111000100010001001</code>)</li>
     * <li>{@link #P_LONG_23_S1_FX} (<code>1101001000100010101000100100010100011001001001110001000100010010</code>)</li>
     * <li>{@link #P_LONG_31_S0_B} (<code>0010010011001001100101101010101110110101001001011010101010010011</code>)</li>
     * <li>{@link #P_LONG_31_S0_F} (<code>0111000100011100101011101101010110010101100010010110010011010001</code>)</li>
     * <li>{@link #P_LONG_31_S1_G} (<code>1000100010100010101100101101110110010100011001011001011101001101</code>)</li>
     * <li>{@link #P_LONG_31_S1_I} (<code>1100101101011010010100110101010001010101001010010100101110110001</code>)</li>
     * <li>{@link #P_LONG_41_S0_AX} (<code>0010110110110110011101011100111011101101101001101110111011101110</code>)</li>
     * <li>{@link #P_LONG_41_S0_D} (<code>0110101101010101110101110110111011100011101101011101011011011011</code>)</li>
     * <li>{@link #P_LONG_41_S1_FX} (<code>1110111011101110100101011101011011101001110101110100110110110110</code>)</li>
     * <li>{@link #P_LONG_41_S1_I} (<code>1101110110110111010110010011101010010110110101101011011101110111</code>)</li>
     * <li>{@link #P_LONG_41_S1_J} (<code>1110111011101011011101110011100011010110111011101010010110101011</code>)</li>
     * </ul>
     */
    public static final long P_LONG_31_S0_E = 6501637279941679303L;

    /**
     * <code>1011010001110100111010010101000101100010101110001011000110001110</code>
     * <p/>
     * Other patterns with half bit-flip-distance (32):
     * <ul>
     * <li>{@link #P_LONG_23_S0_DX} (<code>0100101010010010010010010010001010100010010001000110001110001010</code>)</li>
     * <li>{@link #P_LONG_23_S1_FX} (<code>1101001000100010101000100100010100011001001001110001000100010010</code>)</li>
     * <li>{@link #P_LONG_31_S0_BX} (<code>0100100110010011001011010101011101101010010010110101010100100110</code>)</li>
     * <li>{@link #P_LONG_31_S0_D} (<code>0100010101100101011010011010101101101110011010100100101100010001</code>)</li>
     * <li>{@link #P_LONG_31_S1_FX} (<code>1110001000111001010111011010101100101011000100101100100110100010</code>)</li>
     * <li>{@link #P_LONG_41_S1_DX} (<code>1101011010101011101011101101110111000111011010111010110110110110</code>)</li>
     * <li>{@link #P_LONG_41_S1_J} (<code>1110111011101011011101110011100011010110111011101010010110101011</code>)</li>
     * </ul>
     */
    public static final long P_LONG_31_S1_EX = -5443469513826193010L;

    /**
     * <code>0111000100011100101011101101010110010101100010010110010011010001</code>
     * <p/>
     * Other patterns with half bit-flip-distance (32):
     * <ul>
     * <li>{@link #P_LONG_23_S0_A} (<code>0001000100010001001000110100100010001001100101101000100100111001</code>)</li>
     * <li>{@link #P_LONG_31_S0_E} (<code>0101101000111010011101001010100010110001010111000101100011000111</code>)</li>
     * <li>{@link #P_LONG_31_S1_G} (<code>1000100010100010101100101101110110010100011001011001011101001101</code>)</li>
     * <li>{@link #P_LONG_31_S1_J} (<code>1110110011001100010001010001100010100101010100111000111011010101</code>)</li>
     * <li>{@link #P_LONG_41_S0_A} (<code>0001011011011011001110101110011101110110110100110111011101110111</code>)</li>
     * <li>{@link #P_LONG_41_S0_BX} (<code>0111011001101101101110110110110111011011101110011011101010100110</code>)</li>
     * <li>{@link #P_LONG_41_S0_D} (<code>0110101101010101110101110110111011100011101101011101011011011011</code>)</li>
     * <li>{@link #P_LONG_41_S1_EX} (<code>1110011100010111011101101110110110011011011010111011101011101010</code>)</li>
     * </ul>
     */
    public static final long P_LONG_31_S0_F = 8150581657993831633L;

    /**
     * <code>1110001000111001010111011010101100101011000100101100100110100010</code>
     * <p/>
     * Other patterns with half bit-flip-distance (32):
     * <ul>
     * <li>{@link #P_LONG_23_S0_AX} (<code>0010001000100010010001101001000100010011001011010001001001110010</code>)</li>
     * <li>{@link #P_LONG_23_S0_E} (<code>0100010001100010100010001000101011010001001001001110001000100011</code>)</li>
     * <li>{@link #P_LONG_31_S1_EX} (<code>1011010001110100111010010101000101100010101110001011000110001110</code>)</li>
     * <li>{@link #P_LONG_31_S1_H} (<code>1010010100111001100100101001010101100100101101000110110101000111</code>)</li>
     * <li>{@link #P_LONG_31_S1_J} (<code>1110110011001100010001010001100010100101010100111000111011010101</code>)</li>
     * <li>{@link #P_LONG_41_S0_AX} (<code>0010110110110110011101011100111011101101101001101110111011101110</code>)</li>
     * <li>{@link #P_LONG_41_S0_D} (<code>0110101101010101110101110110111011100011101101011101011011011011</code>)</li>
     * <li>{@link #P_LONG_41_S1_DX} (<code>1101011010101011101011101101110111000111011010111010110110110110</code>)</li>
     * <li>{@link #P_LONG_41_S1_J} (<code>1110111011101011011101110011100011010110111011101010010110101011</code>)</li>
     * </ul>
     */
    public static final long P_LONG_31_S1_FX = -2145580757721888350L;

    /**
     * <code>1000100010100010101100101101110110010100011001011001011101001101</code>
     * <p/>
     * Other patterns with half bit-flip-distance (32):
     * <ul>
     * <li>{@link #P_LONG_23_S0_D} (<code>0010010101001001001001001001000101010001001000100011000111000101</code>)</li>
     * <li>{@link #P_LONG_31_S0_E} (<code>0101101000111010011101001010100010110001010111000101100011000111</code>)</li>
     * <li>{@link #P_LONG_31_S0_F} (<code>0111000100011100101011101101010110010101100010010110010011010001</code>)</li>
     * <li>{@link #P_LONG_31_S1_I} (<code>1100101101011010010100110101010001010101001010010100101110110001</code>)</li>
     * <li>{@link #P_LONG_31_S1_J} (<code>1110110011001100010001010001100010100101010100111000111011010101</code>)</li>
     * <li>{@link #P_LONG_41_S0_A} (<code>0001011011011011001110101110011101110110110100110111011101110111</code>)</li>
     * <li>{@link #P_LONG_41_S0_AX} (<code>0010110110110110011101011100111011101101101001101110111011101110</code>)</li>
     * <li>{@link #P_LONG_41_S1_EX} (<code>1110011100010111011101101110110110011011011010111011101011101010</code>)</li>
     * </ul>
     */
    public static final long P_LONG_31_S1_G = -8601115673577023667L;

    /**
     * <code>1010010100111001100100101001010101100100101101000110110101000111</code>
     * <p/>
     * Other patterns with half bit-flip-distance (32):
     * <ul>
     * <li>{@link #P_LONG_23_S0_B} (<code>0001001000101001010100010010011100011001010001100100100100010001</code>)</li>
     * <li>{@link #P_LONG_23_S0_CX} (<code>0100010001010010001100011000101000100100010001001001100110001110</code>)</li>
     * <li>{@link #P_LONG_23_S0_E} (<code>0100010001100010100010001000101011010001001001001110001000100011</code>)</li>
     * <li>{@link #P_LONG_31_S1_FX} (<code>1110001000111001010111011010101100101011000100101100100110100010</code>)</li>
     * <li>{@link #P_LONG_31_S1_I} (<code>1100101101011010010100110101010001010101001010010100101110110001</code>)</li>
     * <li>{@link #P_LONG_41_S0_C} (<code>0101101110111011011101101110111011011100100101101011010101101101</code>)</li>
     * <li>{@link #P_LONG_41_S1_DX} (<code>1101011010101011101011101101110111000111011010111010110110110110</code>)</li>
     * <li>{@link #P_LONG_41_S1_G} (<code>1000111011101110111011100111011101100110111010100111011100011011</code>)</li>
     * <li>{@link #P_LONG_41_S1_H} (<code>1011101110111010110011101010011011001101101110111010111011011001</code>)</li>
     * </ul>
     */
    public static final long P_LONG_31_S1_H = -6541035813441606329L;

    /**
     * <code>1100101101011010010100110101010001010101001010010100101110110001</code>
     * <p/>
     * Other patterns with half bit-flip-distance (32):
     * <ul>
     * <li>{@link #P_LONG_23_S0_A} (<code>0001000100010001001000110100100010001001100101101000100100111001</code>)</li>
     * <li>{@link #P_LONG_23_S0_C} (<code>0010001000101001000110001100010100010010001000100100110011000111</code>)</li>
     * <li>{@link #P_LONG_23_S0_D} (<code>0010010101001001001001001001000101010001001000100011000111000101</code>)</li>
     * <li>{@link #P_LONG_23_S0_DX} (<code>0100101010010010010010010010001010100010010001000110001110001010</code>)</li>
     * <li>{@link #P_LONG_23_S0_E} (<code>0100010001100010100010001000101011010001001001001110001000100011</code>)</li>
     * <li>{@link #P_LONG_23_S1_I} (<code>1010001000110011000110001001000110010001000101000100010001011001</code>)</li>
     * <li>{@link #P_LONG_31_S0_BX} (<code>0100100110010011001011010101011101101010010010110101010100100110</code>)</li>
     * <li>{@link #P_LONG_31_S0_CX} (<code>0110001011100011010101011001001000111010111000100110001010111010</code>)</li>
     * <li>{@link #P_LONG_31_S0_D} (<code>0100010101100101011010011010101101101110011010100100101100010001</code>)</li>
     * <li>{@link #P_LONG_31_S0_E} (<code>0101101000111010011101001010100010110001010111000101100011000111</code>)</li>
     * <li>{@link #P_LONG_31_S1_G} (<code>1000100010100010101100101101110110010100011001011001011101001101</code>)</li>
     * <li>{@link #P_LONG_31_S1_H} (<code>1010010100111001100100101001010101100100101101000110110101000111</code>)</li>
     * <li>{@link #P_LONG_41_S1_DX} (<code>1101011010101011101011101101110111000111011010111010110110110110</code>)</li>
     * <li>{@link #P_LONG_41_S1_EX} (<code>1110011100010111011101101110110110011011011010111011101011101010</code>)</li>
     * <li>{@link #P_LONG_41_S1_G} (<code>1000111011101110111011100111011101100110111010100111011100011011</code>)</li>
     * </ul>
     */
    public static final long P_LONG_31_S1_I = -3793628114435093583L;

    /**
     * <code>1110110011001100010001010001100010100101010100111000111011010101</code>
     * <p/>
     * Other patterns with half bit-flip-distance (32):
     * <ul>
     * <li>{@link #P_LONG_31_S0_D} (<code>0100010101100101011010011010101101101110011010100100101100010001</code>)</li>
     * <li>{@link #P_LONG_31_S0_F} (<code>0111000100011100101011101101010110010101100010010110010011010001</code>)</li>
     * <li>{@link #P_LONG_31_S1_DX} (<code>1000101011001010110100110101011011011100110101001001011000100010</code>)</li>
     * <li>{@link #P_LONG_31_S1_FX} (<code>1110001000111001010111011010101100101011000100101100100110100010</code>)</li>
     * <li>{@link #P_LONG_31_S1_G} (<code>1000100010100010101100101101110110010100011001011001011101001101</code>)</li>
     * <li>{@link #P_LONG_41_S1_DX} (<code>1101011010101011101011101101110111000111011010111010110110110110</code>)</li>
     * </ul>
     */
    public static final long P_LONG_31_S1_J = -1383655013354336555L;

    // Long patterns with 32 bits set

    /**
     * <code>0001000101110011011011001001001101001110100010011100110111000111</code>
     * <p/>
     * Other patterns with half bit-flip-distance (32):
     * <ul>
     * <li>{@link #P_LONG_32_S0_B} (<code>0010001100101000101001101101110101011101000101011011011010100101</code>)</li>
     * <li>{@link #P_LONG_32_S1_I} (<code>1100111011101100011011000100100010110001010011011100010011000111</code>)</li>
     * </ul>
     */
    public static final long P_LONG_32_S0_A = 1257468100900146631L;

    /**
     * <code>0010001011100110110110010010011010011101000100111001101110001110</code>
     * <p/>
     * Other patterns with half bit-flip-distance (32):
     * <ul>
     * <li>{@link #P_LONG_32_S0_B} (<code>0010001100101000101001101101110101011101000101011011011010100101</code>)</li>
     * <li>{@link #P_LONG_32_S0_BX} (<code>0100011001010001010011011011101010111010001010110110110101001010</code>)</li>
     * <li>{@link #P_LONG_32_S1_G} (<code>1001010001000111001011000100110110010010101010110101101110010111</code>)</li>
     * <li>{@link #P_LONG_32_S1_H} (<code>1011001011001110100010110101100110110001001101010110011000100011</code>)</li>
     * </ul>
     */
    public static final long P_LONG_32_S0_AX = 2514936201800293262L;

    /**
     * <code>0010001100101000101001101101110101011101000101011011011010100101</code>
     * <p/>
     * Other patterns with half bit-flip-distance (32):
     * <ul>
     * <li>{@link #P_LONG_32_S0_A} (<code>0001000101110011011011001001001101001110100010011100110111000111</code>)</li>
     * <li>{@link #P_LONG_32_S0_AX} (<code>0010001011100110110110010010011010011101000100111001101110001110</code>)</li>
     * <li>{@link #P_LONG_32_S1_I} (<code>1100111011101100011011000100100010110001010011011100010011000111</code>)</li>
     * <li>{@link #P_LONG_32_S1_J} (<code>1110111011101001000100110101101000110001101010001001011011001001</code>)</li>
     * </ul>
     */
    public static final long P_LONG_32_S0_B = 2533458260075591333L;

    /**
     * <code>0100011001010001010011011011101010111010001010110110110101001010</code>
     * <p/>
     * Other patterns with half bit-flip-distance (32):
     * <ul>
     * <li>{@link #P_LONG_32_S0_AX} (<code>0010001011100110110110010010011010011101000100111001101110001110</code>)</li>
     * <li>{@link #P_LONG_32_S1_J} (<code>1110111011101001000100110101101000110001101010001001011011001001</code>)</li>
     * </ul>
     */
    public static final long P_LONG_32_S0_BX = 5066916520151182666L;

    /**
     * <code>0011101011010110111010001010011001100100100110101001010111010001</code>
     * <p/>
     * Other patterns with half bit-flip-distance (32):
     * <ul>
     * <li>{@link #P_LONG_32_S0_D} (<code>0101001001011101001001010001110101001011001100111010110101010011</code>)</li>
     * <li>{@link #P_LONG_32_S0_E} (<code>0110010110101100100101011001001011001100110011001001110101000111</code>)</li>
     * <li>{@link #P_LONG_32_S0_F} (<code>0111011101000101010011011011100010100011100101100100011010101001</code>)</li>
     * <li>{@link #P_LONG_32_S1_FX} (<code>1110111010001010100110110111000101000111001011001000110101010010</code>)</li>
     * </ul>
     */
    public static final long P_LONG_32_S0_C = 4239831900565968337L;

    /**
     * <code>0111010110101101110100010100110011001001001101010010101110100010</code>
     * <p/>
     * Other patterns with half bit-flip-distance (32):
     * <ul>
     * <li>{@link #P_LONG_32_S1_DX} (<code>1010010010111010010010100011101010010110011001110101101010100110</code>)</li>
     * <li>{@link #P_LONG_32_S1_EX} (<code>1100101101011001001010110010010110011001100110010011101010001110</code>)</li>
     * <li>{@link #P_LONG_32_S1_FX} (<code>1110111010001010100110110111000101000111001011001000110101010010</code>)</li>
     * </ul>
     */
    public static final long P_LONG_32_S0_CX = 8479663801131936674L;

    /**
     * <code>0101001001011101001001010001110101001011001100111010110101010011</code>
     * <p/>
     * Other patterns with half bit-flip-distance (32):
     * <ul>
     * <li>{@link #P_LONG_32_S0_C} (<code>0011101011010110111010001010011001100100100110101001010111010001</code>)</li>
     * <li>{@link #P_LONG_32_S0_F} (<code>0111011101000101010011011011100010100011100101100100011010101001</code>)</li>
     * </ul>
     */
    public static final long P_LONG_32_S0_D = 5934940691690138963L;

    /**
     * <code>1010010010111010010010100011101010010110011001110101101010100110</code>
     * <p/>
     * Other patterns with half bit-flip-distance (32):
     * <ul>
     * <li>{@link #P_LONG_32_S0_CX} (<code>0111010110101101110100010100110011001001001101010010101110100010</code>)</li>
     * <li>{@link #P_LONG_32_S1_FX} (<code>1110111010001010100110110111000101000111001011001000110101010010</code>)</li>
     * </ul>
     */
    public static final long P_LONG_32_S1_DX = -6576862690329273690L;

    /**
     * <code>0110010110101100100101011001001011001100110011001001110101000111</code>
     * <p/>
     * Other patterns with half bit-flip-distance (32):
     * <ul>
     * <li>{@link #P_LONG_32_S0_C} (<code>0011101011010110111010001010011001100100100110101001010111010001</code>)</li>
     * </ul>
     */
    public static final long P_LONG_32_S0_E = 7326395151558679879L;

    /**
     * <code>1100101101011001001010110010010110011001100110010011101010001110</code>
     * <p/>
     * Other patterns with half bit-flip-distance (32):
     * <ul>
     * <li>{@link #P_LONG_32_S0_CX} (<code>0111010110101101110100010100110011001001001101010010101110100010</code>)</li>
     * <li>{@link #P_LONG_32_S1_H} (<code>1011001011001110100010110101100110110001001101010110011000100011</code>)</li>
     * <li>{@link #P_LONG_32_S1_I} (<code>1100111011101100011011000100100010110001010011011100010011000111</code>)</li>
     * </ul>
     */
    public static final long P_LONG_32_S1_EX = -3793953770592191858L;

    /**
     * <code>0111011101000101010011011011100010100011100101100100011010101001</code>
     * <p/>
     * Other patterns with half bit-flip-distance (32):
     * <ul>
     * <li>{@link #P_LONG_32_S0_C} (<code>0011101011010110111010001010011001100100100110101001010111010001</code>)</li>
     * <li>{@link #P_LONG_32_S0_D} (<code>0101001001011101001001010001110101001011001100111010110101010011</code>)</li>
     * <li>{@link #P_LONG_32_S1_G} (<code>1001010001000111001011000100110110010010101010110101101110010111</code>)</li>
     * </ul>
     */
    public static final long P_LONG_32_S0_F = 8594360919320315561L;

    /**
     * <code>1110111010001010100110110111000101000111001011001000110101010010</code>
     * <p/>
     * Other patterns with half bit-flip-distance (32):
     * <ul>
     * <li>{@link #P_LONG_32_S0_C} (<code>0011101011010110111010001010011001100100100110101001010111010001</code>)</li>
     * <li>{@link #P_LONG_32_S0_CX} (<code>0111010110101101110100010100110011001001001101010010101110100010</code>)</li>
     * <li>{@link #P_LONG_32_S1_DX} (<code>1010010010111010010010100011101010010110011001110101101010100110</code>)</li>
     * <li>{@link #P_LONG_32_S1_I} (<code>1100111011101100011011000100100010110001010011011100010011000111</code>)</li>
     * </ul>
     */
    public static final long P_LONG_32_S1_FX = -1258022235068920494L;

    /**
     * <code>1001010001000111001011000100110110010010101010110101101110010111</code>
     * <p/>
     * Other patterns with half bit-flip-distance (32):
     * <ul>
     * <li>{@link #P_LONG_32_S0_AX} (<code>0010001011100110110110010010011010011101000100111001101110001110</code>)</li>
     * <li>{@link #P_LONG_32_S0_F} (<code>0111011101000101010011011011100010100011100101100100011010101001</code>)</li>
     * </ul>
     */
    public static final long P_LONG_32_S1_G = -7762186721064952937L;

    /**
     * <code>1011001011001110100010110101100110110001001101010110011000100011</code>
     * <p/>
     * Other patterns with half bit-flip-distance (32):
     * <ul>
     * <li>{@link #P_LONG_32_S0_AX} (<code>0010001011100110110110010010011010011101000100111001101110001110</code>)</li>
     * <li>{@link #P_LONG_32_S1_EX} (<code>1100101101011001001010110010010110011001100110010011101010001110</code>)</li>
     * </ul>
     */
    public static final long P_LONG_32_S1_H = -5562355272414566877L;

    /**
     * <code>1100111011101100011011000100100010110001010011011100010011000111</code>
     * <p/>
     * Other patterns with half bit-flip-distance (32):
     * <ul>
     * <li>{@link #P_LONG_32_S0_A} (<code>0001000101110011011011001001001101001110100010011100110111000111</code>)</li>
     * <li>{@link #P_LONG_32_S0_B} (<code>0010001100101000101001101101110101011101000101011011011010100101</code>)</li>
     * <li>{@link #P_LONG_32_S1_EX} (<code>1100101101011001001010110010010110011001100110010011101010001110</code>)</li>
     * <li>{@link #P_LONG_32_S1_FX} (<code>1110111010001010100110110111000101000111001011001000110101010010</code>)</li>
     * </ul>
     */
    public static final long P_LONG_32_S1_I = -3536332547924572985L;

    /**
     * <code>1110111011101001000100110101101000110001101010001001011011001001</code>
     * <p/>
     * Other patterns with half bit-flip-distance (32):
     * <ul>
     * <li>{@link #P_LONG_32_S0_B} (<code>0010001100101000101001101101110101011101000101011011011010100101</code>)</li>
     * <li>{@link #P_LONG_32_S0_BX} (<code>0100011001010001010011011011101010111010001010110110110101001010</code>)</li>
     * </ul>
     */
    public static final long P_LONG_32_S1_J = -1231431745008003383L;

    // Long patterns with 41 bits set

    /**
     * <code>0001011011011011001110101110011101110110110100110111011101110111</code>
     * <p/>
     * Other patterns with half bit-flip-distance (32):
     * <ul>
     * <li>{@link #P_LONG_23_S0_B} (<code>0001001000101001010100010010011100011001010001100100100100010001</code>)</li>
     * <li>{@link #P_LONG_23_S0_D} (<code>0010010101001001001001001001000101010001001000100011000111000101</code>)</li>
     * <li>{@link #P_LONG_31_S0_B} (<code>0010010011001001100101101010101110110101001001011010101010010011</code>)</li>
     * <li>{@link #P_LONG_31_S0_CX} (<code>0110001011100011010101011001001000111010111000100110001010111010</code>)</li>
     * <li>{@link #P_LONG_31_S0_D} (<code>0100010101100101011010011010101101101110011010100100101100010001</code>)</li>
     * <li>{@link #P_LONG_31_S0_F} (<code>0111000100011100101011101101010110010101100010010110010011010001</code>)</li>
     * <li>{@link #P_LONG_31_S1_G} (<code>1000100010100010101100101101110110010100011001011001011101001101</code>)</li>
     * <li>{@link #P_LONG_41_S1_FX} (<code>1110111011101110100101011101011011101001110101110100110110110110</code>)</li>
     * </ul>
     */
    public static final long P_LONG_41_S0_A = 1646974854539474807L;

    /**
     * <code>0010110110110110011101011100111011101101101001101110111011101110</code>
     * <p/>
     * Other patterns with half bit-flip-distance (32):
     * <ul>
     * <li>{@link #P_LONG_23_S0_A} (<code>0001000100010001001000110100100010001001100101101000100100111001</code>)</li>
     * <li>{@link #P_LONG_23_S0_BX} (<code>0010010001010010101000100100111000110010100011001001001000100010</code>)</li>
     * <li>{@link #P_LONG_23_S0_DX} (<code>0100101010010010010010010010001010100010010001000110001110001010</code>)</li>
     * <li>{@link #P_LONG_31_S0_B} (<code>0010010011001001100101101010101110110101001001011010101010010011</code>)</li>
     * <li>{@link #P_LONG_31_S0_BX} (<code>0100100110010011001011010101011101101010010010110101010100100110</code>)</li>
     * <li>{@link #P_LONG_31_S0_E} (<code>0101101000111010011101001010100010110001010111000101100011000111</code>)</li>
     * <li>{@link #P_LONG_31_S1_DX} (<code>1000101011001010110100110101011011011100110101001001011000100010</code>)</li>
     * <li>{@link #P_LONG_31_S1_FX} (<code>1110001000111001010111011010101100101011000100101100100110100010</code>)</li>
     * <li>{@link #P_LONG_31_S1_G} (<code>1000100010100010101100101101110110010100011001011001011101001101</code>)</li>
     * <li>{@link #P_LONG_41_S0_E} (<code>0111001110001011101110110111011011001101101101011101110101110101</code>)</li>
     * </ul>
     */
    public static final long P_LONG_41_S0_AX = 3293949709078949614L;

    /**
     * <code>0011101100110110110111011011011011101101110111001101110101010011</code>
     * <p/>
     * Other patterns with half bit-flip-distance (32):
     * <ul>
     * <li>{@link #P_LONG_23_S0_DX} (<code>0100101010010010010010010010001010100010010001000110001110001010</code>)</li>
     * <li>{@link #P_LONG_31_S0_BX} (<code>0100100110010011001011010101011101101010010010110101010100100110</code>)</li>
     * <li>{@link #P_LONG_31_S0_D} (<code>0100010101100101011010011010101101101110011010100100101100010001</code>)</li>
     * <li>{@link #P_LONG_41_S0_F} (<code>0111011101110111010010101110101101110100111010111010011011011011</code>)</li>
     * </ul>
     */
    public static final long P_LONG_41_S0_B = 4266841474724584787L;

    /**
     * <code>0111011001101101101110110110110111011011101110011011101010100110</code>
     * <p/>
     * Other patterns with half bit-flip-distance (32):
     * <ul>
     * <li>{@link #P_LONG_31_S0_A} (<code>0001010010110010100100110011010010011011001011100110110111000101</code>)</li>
     * <li>{@link #P_LONG_31_S0_F} (<code>0111000100011100101011101101010110010101100010010110010011010001</code>)</li>
     * <li>{@link #P_LONG_31_S1_DX} (<code>1000101011001010110100110101011011011100110101001001011000100010</code>)</li>
     * <li>{@link #P_LONG_41_S1_FX} (<code>1110111011101110100101011101011011101001110101110100110110110110</code>)</li>
     * </ul>
     */
    public static final long P_LONG_41_S0_BX = 8533682949449169574L;

    /**
     * <code>0101101110111011011101101110111011011100100101101011010101101101</code>
     * <p/>
     * Other patterns with half bit-flip-distance (32):
     * <ul>
     * <li>{@link #P_LONG_23_S0_B} (<code>0001001000101001010100010010011100011001010001100100100100010001</code>)</li>
     * <li>{@link #P_LONG_23_S1_G} (<code>1000100010001001000100010001010110010110100010100010100101001001</code>)</li>
     * <li>{@link #P_LONG_23_S1_H} (<code>1001000100010001001010001100010101000100101100110001001100010001</code>)</li>
     * <li>{@link #P_LONG_31_S0_A} (<code>0001010010110010100100110011010010011011001011100110110111000101</code>)</li>
     * <li>{@link #P_LONG_31_S0_BX} (<code>0100100110010011001011010101011101101010010010110101010100100110</code>)</li>
     * <li>{@link #P_LONG_31_S1_H} (<code>1010010100111001100100101001010101100100101101000110110101000111</code>)</li>
     * <li>{@link #P_LONG_41_S1_DX} (<code>1101011010101011101011101101110111000111011010111010110110110110</code>)</li>
     * </ul>
     */
    public static final long P_LONG_41_S0_C = 6610007646371493229L;

    /**
     * <code>1011011101110110111011011101110110111001001011010110101011011010</code>
     * <p/>
     * Other patterns with half bit-flip-distance (32):
     * <ul>
     * <li>{@link #P_LONG_23_S0_BX} (<code>0010010001010010101000100100111000110010100011001001001000100010</code>)</li>
     * <li>{@link #P_LONG_23_S1_J} (<code>1110011000101000100100010001001100010001000100010100101001000101</code>)</li>
     * <li>{@link #P_LONG_31_S0_AX} (<code>0010100101100101001001100110100100110110010111001101101110001010</code>)</li>
     * <li>{@link #P_LONG_31_S0_D} (<code>0100010101100101011010011010101101101110011010100100101100010001</code>)</li>
     * <li>{@link #P_LONG_41_S1_G} (<code>1000111011101110111011100111011101100110111010100111011100011011</code>)</li>
     * </ul>
     */
    public static final long P_LONG_41_S1_CX = -5226728780966565158L;

    /**
     * <code>0110101101010101110101110110111011100011101101011101011011011011</code>
     * <p/>
     * Other patterns with half bit-flip-distance (32):
     * <ul>
     * <li>{@link #P_LONG_23_S0_A} (<code>0001000100010001001000110100100010001001100101101000100100111001</code>)</li>
     * <li>{@link #P_LONG_23_S1_EX} (<code>1000100011000101000100010001010110100010010010011100010001000110</code>)</li>
     * <li>{@link #P_LONG_31_S0_CX} (<code>0110001011100011010101011001001000111010111000100110001010111010</code>)</li>
     * <li>{@link #P_LONG_31_S0_E} (<code>0101101000111010011101001010100010110001010111000101100011000111</code>)</li>
     * <li>{@link #P_LONG_31_S0_F} (<code>0111000100011100101011101101010110010101100010010110010011010001</code>)</li>
     * <li>{@link #P_LONG_31_S1_FX} (<code>1110001000111001010111011010101100101011000100101100100110100010</code>)</li>
     * <li>{@link #P_LONG_41_S1_G} (<code>1000111011101110111011100111011101100110111010100111011100011011</code>)</li>
     * <li>{@link #P_LONG_41_S1_I} (<code>1101110110110111010110010011101010010110110101101011011101110111</code>)</li>
     * <li>{@link #P_LONG_41_S1_J} (<code>1110111011101011011101110011100011010110111011101010010110101011</code>)</li>
     * </ul>
     */
    public static final long P_LONG_41_S0_D = 7734324806345414363L;

    /**
     * <code>1101011010101011101011101101110111000111011010111010110110110110</code>
     * <p/>
     * Other patterns with half bit-flip-distance (32):
     * <ul>
     * <li>{@link #P_LONG_23_S0_AX} (<code>0010001000100010010001101001000100010011001011010001001001110010</code>)</li>
     * <li>{@link #P_LONG_23_S0_D} (<code>0010010101001001001001001001000101010001001000100011000111000101</code>)</li>
     * <li>{@link #P_LONG_23_S0_E} (<code>0100010001100010100010001000101011010001001001001110001000100011</code>)</li>
     * <li>{@link #P_LONG_23_S1_H} (<code>1001000100010001001010001100010101000100101100110001001100010001</code>)</li>
     * <li>{@link #P_LONG_31_S0_C} (<code>0011000101110001101010101100100100011101011100010011000101011101</code>)</li>
     * <li>{@link #P_LONG_31_S1_EX} (<code>1011010001110100111010010101000101100010101110001011000110001110</code>)</li>
     * <li>{@link #P_LONG_31_S1_FX} (<code>1110001000111001010111011010101100101011000100101100100110100010</code>)</li>
     * <li>{@link #P_LONG_31_S1_H} (<code>1010010100111001100100101001010101100100101101000110110101000111</code>)</li>
     * <li>{@link #P_LONG_31_S1_I} (<code>1100101101011010010100110101010001010101001010010100101110110001</code>)</li>
     * <li>{@link #P_LONG_31_S1_J} (<code>1110110011001100010001010001100010100101010100111000111011010101</code>)</li>
     * <li>{@link #P_LONG_41_S0_C} (<code>0101101110111011011101101110111011011100100101101011010101101101</code>)</li>
     * </ul>
     */
    public static final long P_LONG_41_S1_DX = -2978094461018722890L;

    /**
     * <code>0111001110001011101110110111011011001101101101011101110101110101</code>
     * <p/>
     * Other patterns with half bit-flip-distance (32):
     * <ul>
     * <li>{@link #P_LONG_23_S1_I} (<code>1010001000110011000110001001000110010001000101000100010001011001</code>)</li>
     * <li>{@link #P_LONG_41_S0_AX} (<code>0010110110110110011101011100111011101101101001101110111011101110</code>)</li>
     * </ul>
     */
    public static final long P_LONG_41_S0_E = 8325954455056276853L;

    /**
     * <code>1110011100010111011101101110110110011011011010111011101011101010</code>
     * <p/>
     * Other patterns with half bit-flip-distance (32):
     * <ul>
     * <li>{@link #P_LONG_23_S0_C} (<code>0010001000101001000110001100010100010010001000100100110011000111</code>)</li>
     * <li>{@link #P_LONG_23_S0_D} (<code>0010010101001001001001001001000101010001001000100011000111000101</code>)</li>
     * <li>{@link #P_LONG_23_S0_F} (<code>0110100100010001010100010010001010001100100100111000100010001001</code>)</li>
     * <li>{@link #P_LONG_31_S0_C} (<code>0011000101110001101010101100100100011101011100010011000101011101</code>)</li>
     * <li>{@link #P_LONG_31_S0_F} (<code>0111000100011100101011101101010110010101100010010110010011010001</code>)</li>
     * <li>{@link #P_LONG_31_S1_G} (<code>1000100010100010101100101101110110010100011001011001011101001101</code>)</li>
     * <li>{@link #P_LONG_31_S1_I} (<code>1100101101011010010100110101010001010101001010010100101110110001</code>)</li>
     * </ul>
     */
    public static final long P_LONG_41_S1_EX = -1794835163596997910L;

    /**
     * <code>0111011101110111010010101110101101110100111010111010011011011011</code>
     * <p/>
     * Other patterns with half bit-flip-distance (32):
     * <ul>
     * <li>{@link #P_LONG_23_S0_AX} (<code>0010001000100010010001101001000100010011001011010001001001110010</code>)</li>
     * <li>{@link #P_LONG_23_S0_BX} (<code>0010010001010010101000100100111000110010100011001001001000100010</code>)</li>
     * <li>{@link #P_LONG_23_S0_C} (<code>0010001000101001000110001100010100010010001000100100110011000111</code>)</li>
     * <li>{@link #P_LONG_23_S0_F} (<code>0110100100010001010100010010001010001100100100111000100010001001</code>)</li>
     * <li>{@link #P_LONG_41_S0_B} (<code>0011101100110110110111011011011011101101110111001101110101010011</code>)</li>
     * </ul>
     */
    public static final long P_LONG_41_S0_F = 8608431587881363163L;

    /**
     * <code>1110111011101110100101011101011011101001110101110100110110110110</code>
     * <p/>
     * Other patterns with half bit-flip-distance (32):
     * <ul>
     * <li>{@link #P_LONG_23_S0_B} (<code>0001001000101001010100010010011100011001010001100100100100010001</code>)</li>
     * <li>{@link #P_LONG_23_S0_CX} (<code>0100010001010010001100011000101000100100010001001001100110001110</code>)</li>
     * <li>{@link #P_LONG_23_S1_FX} (<code>1101001000100010101000100100010100011001001001110001000100010010</code>)</li>
     * <li>{@link #P_LONG_31_S0_A} (<code>0001010010110010100100110011010010011011001011100110110111000101</code>)</li>
     * <li>{@link #P_LONG_31_S0_E} (<code>0101101000111010011101001010100010110001010111000101100011000111</code>)</li>
     * <li>{@link #P_LONG_41_S0_A} (<code>0001011011011011001110101110011101110110110100110111011101110111</code>)</li>
     * <li>{@link #P_LONG_41_S0_BX} (<code>0111011001101101101110110110110111011011101110011011101010100110</code>)</li>
     * <li>{@link #P_LONG_41_S1_H} (<code>1011101110111010110011101010011011001101101110111010111011011001</code>)</li>
     * </ul>
     */
    public static final long P_LONG_41_S1_FX = -1229880897946825290L;

    /**
     * <code>1000111011101110111011100111011101100110111010100111011100011011</code>
     * <p/>
     * Other patterns with half bit-flip-distance (32):
     * <ul>
     * <li>{@link #P_LONG_23_S0_BX} (<code>0010010001010010101000100100111000110010100011001001001000100010</code>)</li>
     * <li>{@link #P_LONG_23_S1_FX} (<code>1101001000100010101000100100010100011001001001110001000100010010</code>)</li>
     * <li>{@link #P_LONG_23_S1_G} (<code>1000100010001001000100010001010110010110100010100010100101001001</code>)</li>
     * <li>{@link #P_LONG_23_S1_H} (<code>1001000100010001001010001100010101000100101100110001001100010001</code>)</li>
     * <li>{@link #P_LONG_31_S1_H} (<code>1010010100111001100100101001010101100100101101000110110101000111</code>)</li>
     * <li>{@link #P_LONG_31_S1_I} (<code>1100101101011010010100110101010001010101001010010100101110110001</code>)</li>
     * <li>{@link #P_LONG_41_S0_D} (<code>0110101101010101110101110110111011100011101101011101011011011011</code>)</li>
     * <li>{@link #P_LONG_41_S1_CX} (<code>1011011101110110111011011101110110111001001011010110101011011010</code>)</li>
     * <li>{@link #P_LONG_41_S1_I} (<code>1101110110110111010110010011101010010110110101101011011101110111</code>)</li>
     * </ul>
     */
    public static final long P_LONG_41_S1_G = -8147312479271487717L;

    /**
     * <code>1011101110111010110011101010011011001101101110111010111011011001</code>
     * <p/>
     * Other patterns with half bit-flip-distance (32):
     * <ul>
     * <li>{@link #P_LONG_23_S1_I} (<code>1010001000110011000110001001000110010001000101000100010001011001</code>)</li>
     * <li>{@link #P_LONG_31_S0_C} (<code>0011000101110001101010101100100100011101011100010011000101011101</code>)</li>
     * <li>{@link #P_LONG_31_S1_DX} (<code>1000101011001010110100110101011011011100110101001001011000100010</code>)</li>
     * <li>{@link #P_LONG_31_S1_H} (<code>1010010100111001100100101001010101100100101101000110110101000111</code>)</li>
     * <li>{@link #P_LONG_41_S1_FX} (<code>1110111011101110100101011101011011101001110101110100110110110110</code>)</li>
     * <li>{@link #P_LONG_41_S1_J} (<code>1110111011101011011101110011100011010110111011101010010110101011</code>)</li>
     * </ul>
     */
    public static final long P_LONG_41_S1_H = -4919392427137323303L;

    /**
     * <code>1101110110110111010110010011101010010110110101101011011101110111</code>
     * <p/>
     * Other patterns with half bit-flip-distance (32):
     * <ul>
     * <li>{@link #P_LONG_23_S0_A} (<code>0001000100010001001000110100100010001001100101101000100100111001</code>)</li>
     * <li>{@link #P_LONG_23_S0_CX} (<code>0100010001010010001100011000101000100100010001001001100110001110</code>)</li>
     * <li>{@link #P_LONG_23_S0_E} (<code>0100010001100010100010001000101011010001001001001110001000100011</code>)</li>
     * <li>{@link #P_LONG_23_S1_EX} (<code>1000100011000101000100010001010110100010010010011100010001000110</code>)</li>
     * <li>{@link #P_LONG_23_S1_I} (<code>1010001000110011000110001001000110010001000101000100010001011001</code>)</li>
     * <li>{@link #P_LONG_31_S0_BX} (<code>0100100110010011001011010101011101101010010010110101010100100110</code>)</li>
     * <li>{@link #P_LONG_31_S0_CX} (<code>0110001011100011010101011001001000111010111000100110001010111010</code>)</li>
     * <li>{@link #P_LONG_31_S0_D} (<code>0100010101100101011010011010101101101110011010100100101100010001</code>)</li>
     * <li>{@link #P_LONG_31_S0_E} (<code>0101101000111010011101001010100010110001010111000101100011000111</code>)</li>
     * <li>{@link #P_LONG_41_S0_D} (<code>0110101101010101110101110110111011100011101101011101011011011011</code>)</li>
     * <li>{@link #P_LONG_41_S1_G} (<code>1000111011101110111011100111011101100110111010100111011100011011</code>)</li>
     * </ul>
     */
    public static final long P_LONG_41_S1_I = -2470407762415798409L;

    /**
     * <code>1110111011101011011101110011100011010110111011101010010110101011</code>
     * <p/>
     * Other patterns with half bit-flip-distance (32):
     * <ul>
     * <li>{@link #P_LONG_23_S0_CX} (<code>0100010001010010001100011000101000100100010001001001100110001110</code>)</li>
     * <li>{@link #P_LONG_23_S0_D} (<code>0010010101001001001001001001000101010001001000100011000111000101</code>)</li>
     * <li>{@link #P_LONG_23_S0_E} (<code>0100010001100010100010001000101011010001001001001110001000100011</code>)</li>
     * <li>{@link #P_LONG_23_S0_F} (<code>0110100100010001010100010010001010001100100100111000100010001001</code>)</li>
     * <li>{@link #P_LONG_31_S0_E} (<code>0101101000111010011101001010100010110001010111000101100011000111</code>)</li>
     * <li>{@link #P_LONG_31_S1_EX} (<code>1011010001110100111010010101000101100010101110001011000110001110</code>)</li>
     * <li>{@link #P_LONG_31_S1_FX} (<code>1110001000111001010111011010101100101011000100101100100110100010</code>)</li>
     * <li>{@link #P_LONG_41_S0_D} (<code>0110101101010101110101110110111011100011101101011101011011011011</code>)</li>
     * <li>{@link #P_LONG_41_S1_H} (<code>1011101110111010110011101010011011001101101110111010111011011001</code>)</li>
     * </ul>
     */
    public static final long P_LONG_41_S1_J = -1230758987147860565L;

    /**
     * <code>0010010100101001001001101010111001110010010101110011101110101001</code>
     * <p/>
     * Pattern group with half bit-flip-distance (32) to this pattern and <i>among</i> each other:
     * <ul>
     * <li>{@link #P_LONGM_32_S0_008X} (<code>0100110100101010100101001011001100101011010010101001110111010110</code>)</li>
     * <li>{@link #P_LONGM_32_S0_018} (<code>0010101001110010101001010010011001001101101101001010110110011011</code>)</li>
     * <li>{@link #P_LONGM_32_S0_049} (<code>0100100101011011101010101001110011001001011010010100100101101011</code>)</li>
     * <li>{@link #P_LONGM_32_S1_149} (<code>1010101010110010010101110011100100101011010110010110101001001001</code>)</li>
     * </ul>
     */
    public static final long P_LONGM_32_S0_004 = 2677713984132955049L;

    /**
     * <code>0100110100101010100101001011001100101011010010101001110111010110</code>
     * <p/>
     * Pattern group with half bit-flip-distance (32) to this pattern and <i>among</i> each other:
     * <ul>
     * <li>{@link #P_LONGM_32_S0_004} (<code>0010010100101001001001101010111001110010010101110011101110101001</code>)</li>
     * <li>{@link #P_LONGM_32_S0_018} (<code>0010101001110010101001010010011001001101101101001010110110011011</code>)</li>
     * <li>{@link #P_LONGM_32_S0_049} (<code>0100100101011011101010101001110011001001011010010100100101101011</code>)</li>
     * <li>{@link #P_LONGM_32_S1_149} (<code>1010101010110010010101110011100100101011010110010110101001001001</code>)</li>
     * </ul>
     * <p/>
     * Pattern group 2 with half bit-flip-distance (32) to this pattern and <i>among</i> each other:
     * <ul>
     * <li>{@link #P_LONGM_32_S0_011} (<code>0010011100101101001001110101101001101001110100110011010100100101</code>)</li>
     * <li>{@link #P_LONGM_32_S0_018} (<code>0010101001110010101001010010011001001101101101001010110110011011</code>)</li>
     * <li>{@link #P_LONGM_32_S0_090} (<code>0110100101010011011101001100111011001010010110101001001010101001</code>)</li>
     * <li>{@link #P_LONGM_32_S1_149} (<code>1010101010110010010101110011100100101011010110010110101001001001</code>)</li>
     * </ul>
     */
    public static final long P_LONGM_32_S0_008X = 5560420187188665814L;

    /**
     * <code>0010011100101101001001110101101001101001110100110011010100100101</code>
     * <p/>
     * Pattern group with half bit-flip-distance (32) to this pattern and <i>among</i> each other:
     * <ul>
     * <li>{@link #P_LONGM_32_S0_008X} (<code>0100110100101010100101001011001100101011010010101001110111010110</code>)</li>
     * <li>{@link #P_LONGM_32_S0_018} (<code>0010101001110010101001010010011001001101101101001010110110011011</code>)</li>
     * <li>{@link #P_LONGM_32_S0_090} (<code>0110100101010011011101001100111011001010010110101001001010101001</code>)</li>
     * <li>{@link #P_LONGM_32_S1_149} (<code>1010101010110010010101110011100100101011010110010110101001001001</code>)</li>
     * </ul>
     * <p/>
     * Pattern group 2 with half bit-flip-distance (32) to this pattern and <i>among</i> each other:
     * <ul>
     * <li>{@link #P_LONGM_32_S0_018} (<code>0010101001110010101001010010011001001101101101001010110110011011</code>)</li>
     * <li>{@link #P_LONGM_32_S0_025X} (<code>0101100101100110110011100101001100101101110010100100110010101010</code>)</li>
     * <li>{@link #P_LONGM_32_S0_090} (<code>0110100101010011011101001100111011001010010110101001001010101001</code>)</li>
     * <li>{@link #P_LONGM_32_S1_149} (<code>1010101010110010010101110011100100101011010110010110101001001001</code>)</li>
     * </ul>
     */
    public static final long P_LONGM_32_S0_011 = 2822955810707158309L;

    /**
     * <code>0101001010101100111010111001001101011010011001010010011100101010</code>
     * <p/>
     * Pattern group with half bit-flip-distance (32) to this pattern and <i>among</i> each other:
     * <ul>
     * <li>{@link #P_LONGM_32_S0_020X} (<code>0101010111001110010110010100100110011001001011001101101001110010</code>)</li>
     * <li>{@link #P_LONGM_32_S0_039X} (<code>0110110101011001011010010100101101010110100101101010010010011010</code>)</li>
     * <li>{@link #P_LONGM_32_S1_080X} (<code>1010110010100101110110101010010101110100101001001011001001010110</code>)</li>
     * <li>{@link #P_LONGM_32_S1_141} (<code>1010011100101001011001011100110111010101001010010010100100100111</code>)</li>
     * </ul>
     */
    public static final long P_LONGM_32_S0_013X = 5957395425213622058L;

    /**
     * <code>0010101001011010101010101010100100100111011011100101101001100101</code>
     * <p/>
     * Pattern group with half bit-flip-distance (32) to this pattern and <i>among</i> each other:
     * <ul>
     * <li>{@link #P_LONGM_32_S0_020} (<code>0010101011100111001011001010010011001100100101100110110100111001</code>)</li>
     * <li>{@link #P_LONGM_32_S0_021} (<code>0010101100100101011100100111001001101011001110011100100100100111</code>)</li>
     * <li>{@link #P_LONGM_32_S0_029} (<code>0011001011001011001110010100101010101010010110010010111011001011</code>)</li>
     * <li>{@link #P_LONGM_32_S1_125} (<code>1001101010110110010010010110010111010010010110010101001101010101</code>)</li>
     * </ul>
     */
    public static final long P_LONGM_32_S0_015 = 3051939340984670821L;

    /**
     * <code>0010101001110010101001010010011001001101101101001010110110011011</code>
     * <p/>
     * Pattern group with half bit-flip-distance (32) to this pattern and <i>among</i> each other:
     * <ul>
     * <li>{@link #P_LONGM_32_S0_004} (<code>0010010100101001001001101010111001110010010101110011101110101001</code>)</li>
     * <li>{@link #P_LONGM_32_S0_008X} (<code>0100110100101010100101001011001100101011010010101001110111010110</code>)</li>
     * <li>{@link #P_LONGM_32_S0_049} (<code>0100100101011011101010101001110011001001011010010100100101101011</code>)</li>
     * <li>{@link #P_LONGM_32_S1_149} (<code>1010101010110010010101110011100100101011010110010110101001001001</code>)</li>
     * </ul>
     * <p/>
     * Pattern group 2 with half bit-flip-distance (32) to this pattern and <i>among</i> each other:
     * <ul>
     * <li>{@link #P_LONGM_32_S0_008X} (<code>0100110100101010100101001011001100101011010010101001110111010110</code>)</li>
     * <li>{@link #P_LONGM_32_S0_011} (<code>0010011100101101001001110101101001101001110100110011010100100101</code>)</li>
     * <li>{@link #P_LONGM_32_S0_090} (<code>0110100101010011011101001100111011001010010110101001001010101001</code>)</li>
     * <li>{@link #P_LONGM_32_S1_149} (<code>1010101010110010010101110011100100101011010110010110101001001001</code>)</li>
     * </ul>
     * <p/>
     * Pattern group 3 with half bit-flip-distance (32) to this pattern and <i>among</i> each other:
     * <ul>
     * <li>{@link #P_LONGM_32_S0_011} (<code>0010011100101101001001110101101001101001110100110011010100100101</code>)</li>
     * <li>{@link #P_LONGM_32_S0_025X} (<code>0101100101100110110011100101001100101101110010100100110010101010</code>)</li>
     * <li>{@link #P_LONGM_32_S0_090} (<code>0110100101010011011101001100111011001010010110101001001010101001</code>)</li>
     * <li>{@link #P_LONGM_32_S1_149} (<code>1010101010110010010101110011100100101011010110010110101001001001</code>)</li>
     * </ul>
     */
    public static final long P_LONGM_32_S0_018 = 3058688680869014939L;

    /**
     * <code>0010101011100111001011001010010011001100100101100110110100111001</code>
     * <p/>
     * Pattern group with half bit-flip-distance (32) to this pattern and <i>among</i> each other:
     * <ul>
     * <li>{@link #P_LONGM_32_S0_015} (<code>0010101001011010101010101010100100100111011011100101101001100101</code>)</li>
     * <li>{@link #P_LONGM_32_S0_021} (<code>0010101100100101011100100111001001101011001110011100100100100111</code>)</li>
     * <li>{@link #P_LONGM_32_S0_029} (<code>0011001011001011001110010100101010101010010110010010111011001011</code>)</li>
     * <li>{@link #P_LONGM_32_S1_125} (<code>1001101010110110010010010110010111010010010110010101001101010101</code>)</li>
     * </ul>
     */
    public static final long P_LONGM_32_S0_020 = 3091488755531803961L;

    /**
     * <code>0101010111001110010110010100100110011001001011001101101001110010</code>
     * <p/>
     * Pattern group with half bit-flip-distance (32) to this pattern and <i>among</i> each other:
     * <ul>
     * <li>{@link #P_LONGM_32_S0_013X} (<code>0101001010101100111010111001001101011010011001010010011100101010</code>)</li>
     * <li>{@link #P_LONGM_32_S0_039X} (<code>0110110101011001011010010100101101010110100101101010010010011010</code>)</li>
     * <li>{@link #P_LONGM_32_S1_080X} (<code>1010110010100101110110101010010101110100101001001011001001010110</code>)</li>
     * <li>{@link #P_LONGM_32_S1_141} (<code>1010011100101001011001011100110111010101001010010010100100100111</code>)</li>
     * </ul>
     */
    public static final long P_LONGM_32_S0_020X = 6182977511063607922L;

    /**
     * <code>0010101100100101011100100111001001101011001110011100100100100111</code>
     * <p/>
     * Pattern group with half bit-flip-distance (32) to this pattern and <i>among</i> each other:
     * <ul>
     * <li>{@link #P_LONGM_32_S0_015} (<code>0010101001011010101010101010100100100111011011100101101001100101</code>)</li>
     * <li>{@link #P_LONGM_32_S0_020} (<code>0010101011100111001011001010010011001100100101100110110100111001</code>)</li>
     * <li>{@link #P_LONGM_32_S0_029} (<code>0011001011001011001110010100101010101010010110010010111011001011</code>)</li>
     * <li>{@link #P_LONGM_32_S1_125} (<code>1001101010110110010010010110010111010010010110010101001101010101</code>)</li>
     * </ul>
     */
    public static final long P_LONGM_32_S0_021 = 3109016953519982887L;

    /**
     * <code>0101100101100110110011100101001100101101110010100100110010101010</code>
     * <p/>
     * Pattern group with half bit-flip-distance (32) to this pattern and <i>among</i> each other:
     * <ul>
     * <li>{@link #P_LONGM_32_S0_011} (<code>0010011100101101001001110101101001101001110100110011010100100101</code>)</li>
     * <li>{@link #P_LONGM_32_S0_018} (<code>0010101001110010101001010010011001001101101101001010110110011011</code>)</li>
     * <li>{@link #P_LONGM_32_S0_090} (<code>0110100101010011011101001100111011001010010110101001001010101001</code>)</li>
     * <li>{@link #P_LONGM_32_S1_149} (<code>1010101010110010010101110011100100101011010110010110101001001001</code>)</li>
     * </ul>
     */
    public static final long P_LONGM_32_S0_025X = 6442063173645913258L;

    /**
     * <code>0101101001010111011011010100101011010010010011010010011010110010</code>
     * <p/>
     * Pattern group with half bit-flip-distance (32) to this pattern and <i>among</i> each other:
     * <ul>
     * <li>{@link #P_LONGM_32_S0_031X} (<code>0110010110101011011001100100101101001011101010100101110010010010</code>)</li>
     * <li>{@link #P_LONGM_32_S0_049} (<code>0100100101011011101010101001110011001001011010010100100101101011</code>)</li>
     * <li>{@link #P_LONGM_32_S1_076X} (<code>1010101001001110010010110111001001110011100100110100100101011010</code>)</li>
     * <li>{@link #P_LONGM_32_S1_200} (<code>1110111001001011100100100100110101010100101110010010011001010101</code>)</li>
     * </ul>
     */
    public static final long P_LONGM_32_S0_026X = 6509791954510620338L;

    /**
     * <code>0011001011001011001110010100101010101010010110010010111011001011</code>
     * <p/>
     * Pattern group with half bit-flip-distance (32) to this pattern and <i>among</i> each other:
     * <ul>
     * <li>{@link #P_LONGM_32_S0_015} (<code>0010101001011010101010101010100100100111011011100101101001100101</code>)</li>
     * <li>{@link #P_LONGM_32_S0_020} (<code>0010101011100111001011001010010011001100100101100110110100111001</code>)</li>
     * <li>{@link #P_LONGM_32_S0_021} (<code>0010101100100101011100100111001001101011001110011100100100100111</code>)</li>
     * <li>{@link #P_LONGM_32_S1_125} (<code>1001101010110110010010010110010111010010010110010101001101010101</code>)</li>
     * </ul>
     */
    public static final long P_LONGM_32_S0_029 = 3660082115016994507L;

    /**
     * <code>0110010110101011011001100100101101001011101010100101110010010010</code>
     * <p/>
     * Pattern group with half bit-flip-distance (32) to this pattern and <i>among</i> each other:
     * <ul>
     * <li>{@link #P_LONGM_32_S0_026X} (<code>0101101001010111011011010100101011010010010011010010011010110010</code>)</li>
     * <li>{@link #P_LONGM_32_S0_049} (<code>0100100101011011101010101001110011001001011010010100100101101011</code>)</li>
     * <li>{@link #P_LONGM_32_S1_076X} (<code>1010101001001110010010110111001001110011100100110100100101011010</code>)</li>
     * <li>{@link #P_LONGM_32_S1_200} (<code>1110111001001011100100100100110101010100101110010010011001010101</code>)</li>
     * </ul>
     * <p/>
     * Pattern group 2 with half bit-flip-distance (32) to this pattern and <i>among</i> each other:
     * <ul>
     * <li>{@link #P_LONGM_32_S0_037} (<code>0011010011011101011010110011001010101011001001101001011001001001</code>)</li>
     * <li>{@link #P_LONGM_32_S1_118} (<code>1001100100100111010011010010100101101101101101010010011010011001</code>)</li>
     * <li>{@link #P_LONGM_32_S1_140} (<code>1010011011001011010010101100100110110010010110011010011010010101</code>)</li>
     * <li>{@link #P_LONGM_32_S1_192} (<code>1110011001101001001001010111010100100101011010010110101001001011</code>)</li>
     * </ul>
     */
    public static final long P_LONGM_32_S0_031X = 7326061692426280082L;

    /**
     * <code>0011010011011101011010110011001010101011001001101001011001001001</code>
     * <p/>
     * Pattern group with half bit-flip-distance (32) to this pattern and <i>among</i> each other:
     * <ul>
     * <li>{@link #P_LONGM_32_S0_031X} (<code>0110010110101011011001100100101101001011101010100101110010010010</code>)</li>
     * <li>{@link #P_LONGM_32_S1_118} (<code>1001100100100111010011010010100101101101101101010010011010011001</code>)</li>
     * <li>{@link #P_LONGM_32_S1_140} (<code>1010011011001011010010101100100110110010010110011010011010010101</code>)</li>
     * <li>{@link #P_LONGM_32_S1_192} (<code>1110011001101001001001010111010100100101011010010110101001001011</code>)</li>
     * </ul>
     */
    public static final long P_LONGM_32_S0_037 = 3809318725189277257L;

    /**
     * <code>0110101001010011001010010101101101110100111010010010011001010110</code>
     * <p/>
     * Pattern group with half bit-flip-distance (32) to this pattern and <i>among</i> each other:
     * <ul>
     * <li>{@link #P_LONGM_32_S0_043} (<code>0011101010101010011100110010101101010010101011100110010100101001</code>)</li>
     * <li>{@link #P_LONGM_32_S0_046X} (<code>0111010111001010010010010011010011001010101110110100101001100110</code>)</li>
     * <li>{@link #P_LONGM_32_S0_084} (<code>0101101101010010110110100101010010011010010010101011010100110101</code>)</li>
     * <li>{@link #P_LONGM_32_S1_147} (<code>1010100111010011010101001001100101001110010010101110101011001001</code>)</li>
     * </ul>
     */
    public static final long P_LONGM_32_S0_038X = 7661512863867545174L;

    /**
     * <code>0110110101011001011010010100101101010110100101101010010010011010</code>
     * <p/>
     * Pattern group with half bit-flip-distance (32) to this pattern and <i>among</i> each other:
     * <ul>
     * <li>{@link #P_LONGM_32_S0_013X} (<code>0101001010101100111010111001001101011010011001010010011100101010</code>)</li>
     * <li>{@link #P_LONGM_32_S0_020X} (<code>0101010111001110010110010100100110011001001011001101101001110010</code>)</li>
     * <li>{@link #P_LONGM_32_S1_080X} (<code>1010110010100101110110101010010101110100101001001011001001010110</code>)</li>
     * <li>{@link #P_LONGM_32_S1_141} (<code>1010011100101001011001011100110111010101001010010010100100100111</code>)</li>
     * </ul>
     */
    public static final long P_LONGM_32_S0_039X = 7879444795357570202L;

    /**
     * <code>0011101010101010011100110010101101010010101011100110010100101001</code>
     * <p/>
     * Pattern group with half bit-flip-distance (32) to this pattern and <i>among</i> each other:
     * <ul>
     * <li>{@link #P_LONGM_32_S0_038X} (<code>0110101001010011001010010101101101110100111010010010011001010110</code>)</li>
     * <li>{@link #P_LONGM_32_S0_046X} (<code>0111010111001010010010010011010011001010101110110100101001100110</code>)</li>
     * <li>{@link #P_LONGM_32_S0_084} (<code>0101101101010010110110100101010010011010010010101011010100110101</code>)</li>
     * <li>{@link #P_LONGM_32_S1_147} (<code>1010100111010011010101001001100101001110010010101110101011001001</code>)</li>
     * </ul>
     */
    public static final long P_LONGM_32_S0_043 = 4227317830148580649L;

    /**
     * <code>0111010111001010010010010011010011001010101110110100101001100110</code>
     * <p/>
     * Pattern group with half bit-flip-distance (32) to this pattern and <i>among</i> each other:
     * <ul>
     * <li>{@link #P_LONGM_32_S0_038X} (<code>0110101001010011001010010101101101110100111010010010011001010110</code>)</li>
     * <li>{@link #P_LONGM_32_S0_043} (<code>0011101010101010011100110010101101010010101011100110010100101001</code>)</li>
     * <li>{@link #P_LONGM_32_S0_084} (<code>0101101101010010110110100101010010011010010010101011010100110101</code>)</li>
     * <li>{@link #P_LONGM_32_S1_147} (<code>1010100111010011010101001001100101001110010010101110101011001001</code>)</li>
     * </ul>
     */
    public static final long P_LONGM_32_S0_046X = 8487676938821519974L;

    /**
     * <code>0100100101011011101010101001110011001001011010010100100101101011</code>
     * <p/>
     * Pattern group with half bit-flip-distance (32) to this pattern and <i>among</i> each other:
     * <ul>
     * <li>{@link #P_LONGM_32_S0_004} (<code>0010010100101001001001101010111001110010010101110011101110101001</code>)</li>
     * <li>{@link #P_LONGM_32_S0_008X} (<code>0100110100101010100101001011001100101011010010101001110111010110</code>)</li>
     * <li>{@link #P_LONGM_32_S0_018} (<code>0010101001110010101001010010011001001101101101001010110110011011</code>)</li>
     * <li>{@link #P_LONGM_32_S1_149} (<code>1010101010110010010101110011100100101011010110010110101001001001</code>)</li>
     * </ul>
     * <p/>
     * Pattern group 2 with half bit-flip-distance (32) to this pattern and <i>among</i> each other:
     * <ul>
     * <li>{@link #P_LONGM_32_S0_026X} (<code>0101101001010111011011010100101011010010010011010010011010110010</code>)</li>
     * <li>{@link #P_LONGM_32_S0_031X} (<code>0110010110101011011001100100101101001011101010100101110010010010</code>)</li>
     * <li>{@link #P_LONGM_32_S1_076X} (<code>1010101001001110010010110111001001110011100100110100100101011010</code>)</li>
     * <li>{@link #P_LONGM_32_S1_200} (<code>1110111001001011100100100100110101010100101110010010011001010101</code>)</li>
     * </ul>
     */
    public static final long P_LONGM_32_S0_049 = 5286006178020149611L;

    /**
     * <code>0101010010010010110100101001011001100111001110011001110010010111</code>
     * <p/>
     * Pattern group with half bit-flip-distance (32) to this pattern and <i>among</i> each other:
     * <ul>
     * <li>{@link #P_LONGM_32_S0_078} (<code>0101010110100101001101001011100100101010100110101010111001100101</code>)</li>
     * <li>{@link #P_LONGM_32_S0_086} (<code>0101110101010110010010011001001110100100101010100111001101011001</code>)</li>
     * <li>{@link #P_LONGM_32_S1_105} (<code>1001001011010101011010100111011001001001100100101001001101110011</code>)</li>
     * <li>{@link #P_LONGM_32_S1_116} (<code>1001011100100100111001001010010011010111010010010011101101001011</code>)</li>
     * </ul>
     */
    public static final long P_LONGM_32_S0_073 = 6094164789204458647L;

    /**
     * <code>0101010101001001110100101110100110101010100101110100110010100101</code>
     * <p/>
     * Pattern group with half bit-flip-distance (32) to this pattern and <i>among</i> each other:
     * <ul>
     * <li>{@link #P_LONGM_32_S1_050X} (<code>1001001100100101100101001001101110110011001010011100111011010010</code>)</li>
     * <li>{@link #P_LONGM_32_S1_072X} (<code>1010011001001110011001001101011010101010010010010110110100101110</code>)</li>
     * <li>{@link #P_LONGM_32_S1_138} (<code>1010010101010010101100101010010010101001101011011011001011001011</code>)</li>
     * <li>{@link #P_LONGM_32_S1_139} (<code>1010011010010011010010010100110110011100100110010100111011011001</code>)</li>
     * </ul>
     */
    public static final long P_LONGM_32_S0_077 = 6145675067555007653L;

    /**
     * <code>0101010110100101001101001011100100101010100110101010111001100101</code>
     * <p/>
     * Pattern group with half bit-flip-distance (32) to this pattern and <i>among</i> each other:
     * <ul>
     * <li>{@link #P_LONGM_32_S0_073} (<code>0101010010010010110100101001011001100111001110011001110010010111</code>)</li>
     * <li>{@link #P_LONGM_32_S0_086} (<code>0101110101010110010010011001001110100100101010100111001101011001</code>)</li>
     * <li>{@link #P_LONGM_32_S1_105} (<code>1001001011010101011010100111011001001001100100101001001101110011</code>)</li>
     * <li>{@link #P_LONGM_32_S1_116} (<code>1001011100100100111001001010010011010111010010010011101101001011</code>)</li>
     * </ul>
     */
    public static final long P_LONGM_32_S0_078 = 6171396834269507173L;

    /**
     * <code>0101101101010010110110100101010010011010010010101011010100110101</code>
     * <p/>
     * Pattern group with half bit-flip-distance (32) to this pattern and <i>among</i> each other:
     * <ul>
     * <li>{@link #P_LONGM_32_S0_038X} (<code>0110101001010011001010010101101101110100111010010010011001010110</code>)</li>
     * <li>{@link #P_LONGM_32_S0_043} (<code>0011101010101010011100110010101101010010101011100110010100101001</code>)</li>
     * <li>{@link #P_LONGM_32_S0_046X} (<code>0111010111001010010010010011010011001010101110110100101001100110</code>)</li>
     * <li>{@link #P_LONGM_32_S1_147} (<code>1010100111010011010101001001100101001110010010101110101011001001</code>)</li>
     * </ul>
     */
    public static final long P_LONGM_32_S0_084 = 6580562062442411317L;

    /**
     * <code>0101110101010110010010011001001110100100101010100111001101011001</code>
     * <p/>
     * Pattern group with half bit-flip-distance (32) to this pattern and <i>among</i> each other:
     * <ul>
     * <li>{@link #P_LONGM_32_S0_073} (<code>0101010010010010110100101001011001100111001110011001110010010111</code>)</li>
     * <li>{@link #P_LONGM_32_S0_078} (<code>0101010110100101001101001011100100101010100110101010111001100101</code>)</li>
     * <li>{@link #P_LONGM_32_S1_105} (<code>1001001011010101011010100111011001001001100100101001001101110011</code>)</li>
     * <li>{@link #P_LONGM_32_S1_116} (<code>1001011100100100111001001010010011010111010010010011101101001011</code>)</li>
     * </ul>
     */
    public static final long P_LONGM_32_S0_086 = 6725643991996068697L;

    /**
     * <code>0110100101010011011101001100111011001010010110101001001010101001</code>
     * <p/>
     * Pattern group with half bit-flip-distance (32) to this pattern and <i>among</i> each other:
     * <ul>
     * <li>{@link #P_LONGM_32_S0_008X} (<code>0100110100101010100101001011001100101011010010101001110111010110</code>)</li>
     * <li>{@link #P_LONGM_32_S0_011} (<code>0010011100101101001001110101101001101001110100110011010100100101</code>)</li>
     * <li>{@link #P_LONGM_32_S0_018} (<code>0010101001110010101001010010011001001101101101001010110110011011</code>)</li>
     * <li>{@link #P_LONGM_32_S1_149} (<code>1010101010110010010101110011100100101011010110010110101001001001</code>)</li>
     * </ul>
     * <p/>
     * Pattern group 2 with half bit-flip-distance (32) to this pattern and <i>among</i> each other:
     * <ul>
     * <li>{@link #P_LONGM_32_S0_011} (<code>0010011100101101001001110101101001101001110100110011010100100101</code>)</li>
     * <li>{@link #P_LONGM_32_S0_018} (<code>0010101001110010101001010010011001001101101101001010110110011011</code>)</li>
     * <li>{@link #P_LONGM_32_S0_025X} (<code>0101100101100110110011100101001100101101110010100100110010101010</code>)</li>
     * <li>{@link #P_LONGM_32_S1_149} (<code>1010101010110010010101110011100100101011010110010110101001001001</code>)</li>
     * </ul>
     */
    public static final long P_LONGM_32_S0_090 = 7589538228556436137L;

    /**
     * <code>1001001010010010010101010010101001011100111010111010111001010110</code>
     * <p/>
     * Pattern group with half bit-flip-distance (32) to this pattern and <i>among</i> each other:
     * <ul>
     * <li>{@link #P_LONGM_32_S1_057X} (<code>1001011001100101011011010010101110011001001001100101101010101010</code>)</li>
     * <li>{@link #P_LONGM_32_S1_069X} (<code>1010010010111011001100101001001011010110101001010101110010110010</code>)</li>
     * <li>{@link #P_LONGM_32_S1_076X} (<code>1010101001001110010010110111001001110011100100110100100101011010</code>)</li>
     * <li>{@link #P_LONGM_32_S1_119} (<code>1001100100110010010010100101100110100111011001001110101001110011</code>)</li>
     * </ul>
     */
    public static final long P_LONGM_32_S1_048X = -7885146357136380330L;

    /**
     * <code>1001001100100101100101001001101110110011001010011100111011010010</code>
     * <p/>
     * Pattern group with half bit-flip-distance (32) to this pattern and <i>among</i> each other:
     * <ul>
     * <li>{@link #P_LONGM_32_S0_077} (<code>0101010101001001110100101110100110101010100101110100110010100101</code>)</li>
     * <li>{@link #P_LONGM_32_S1_072X} (<code>1010011001001110011001001101011010101010010010010110110100101110</code>)</li>
     * <li>{@link #P_LONGM_32_S1_138} (<code>1010010101010010101100101010010010101001101011011011001011001011</code>)</li>
     * <li>{@link #P_LONGM_32_S1_139} (<code>1010011010010011010010010100110110011100100110010100111011011001</code>)</li>
     * </ul>
     */
    public static final long P_LONGM_32_S1_050X = -7843699779549147438L;

    /**
     * <code>1001011001100101011011010010101110011001001001100101101010101010</code>
     * <p/>
     * Pattern group with half bit-flip-distance (32) to this pattern and <i>among</i> each other:
     * <ul>
     * <li>{@link #P_LONGM_32_S1_048X} (<code>1001001010010010010101010010101001011100111010111010111001010110</code>)</li>
     * <li>{@link #P_LONGM_32_S1_069X} (<code>1010010010111011001100101001001011010110101001010101110010110010</code>)</li>
     * <li>{@link #P_LONGM_32_S1_076X} (<code>1010101001001110010010110111001001110011100100110100100101011010</code>)</li>
     * <li>{@link #P_LONGM_32_S1_119} (<code>1001100100110010010010100101100110100111011001001110101001110011</code>)</li>
     * </ul>
     */
    public static final long P_LONGM_32_S1_057X = -7609555961352136022L;

    /**
     * <code>1010010010111011001100101001001011010110101001010101110010110010</code>
     * <p/>
     * Pattern group with half bit-flip-distance (32) to this pattern and <i>among</i> each other:
     * <ul>
     * <li>{@link #P_LONGM_32_S1_048X} (<code>1001001010010010010101010010101001011100111010111010111001010110</code>)</li>
     * <li>{@link #P_LONGM_32_S1_057X} (<code>1001011001100101011011010010101110011001001001100101101010101010</code>)</li>
     * <li>{@link #P_LONGM_32_S1_076X} (<code>1010101001001110010010110111001001110011100100110100100101011010</code>)</li>
     * <li>{@link #P_LONGM_32_S1_119} (<code>1001100100110010010010100101100110100111011001001110101001110011</code>)</li>
     * </ul>
     */
    public static final long P_LONGM_32_S1_069X = -6576607224596702030L;

    /**
     * <code>1010011001001110011001001101011010101010010010010110110100101110</code>
     * <p/>
     * Pattern group with half bit-flip-distance (32) to this pattern and <i>among</i> each other:
     * <ul>
     * <li>{@link #P_LONGM_32_S0_077} (<code>0101010101001001110100101110100110101010100101110100110010100101</code>)</li>
     * <li>{@link #P_LONGM_32_S1_050X} (<code>1001001100100101100101001001101110110011001010011100111011010010</code>)</li>
     * <li>{@link #P_LONGM_32_S1_138} (<code>1010010101010010101100101010010010101001101011011011001011001011</code>)</li>
     * <li>{@link #P_LONGM_32_S1_139} (<code>1010011010010011010010010100110110011100100110010100111011011001</code>)</li>
     * </ul>
     */
    public static final long P_LONGM_32_S1_072X = -6463117542087365330L;

    /**
     * <code>1010101001001110010010110111001001110011100100110100100101011010</code>
     * <p/>
     * Pattern group with half bit-flip-distance (32) to this pattern and <i>among</i> each other:
     * <ul>
     * <li>{@link #P_LONGM_32_S0_026X} (<code>0101101001010111011011010100101011010010010011010010011010110010</code>)</li>
     * <li>{@link #P_LONGM_32_S0_031X} (<code>0110010110101011011001100100101101001011101010100101110010010010</code>)</li>
     * <li>{@link #P_LONGM_32_S0_049} (<code>0100100101011011101010101001110011001001011010010100100101101011</code>)</li>
     * <li>{@link #P_LONGM_32_S1_200} (<code>1110111001001011100100100100110101010100101110010010011001010101</code>)</li>
     * </ul>
     * <p/>
     * Pattern group 2 with half bit-flip-distance (32) to this pattern and <i>among</i> each other:
     * <ul>
     * <li>{@link #P_LONGM_32_S1_048X} (<code>1001001010010010010101010010101001011100111010111010111001010110</code>)</li>
     * <li>{@link #P_LONGM_32_S1_057X} (<code>1001011001100101011011010010101110011001001001100101101010101010</code>)</li>
     * <li>{@link #P_LONGM_32_S1_069X} (<code>1010010010111011001100101001001011010110101001010101110010110010</code>)</li>
     * <li>{@link #P_LONGM_32_S1_119} (<code>1001100100110010010010100101100110100111011001001110101001110011</code>)</li>
     * </ul>
     */
    public static final long P_LONGM_32_S1_076X = -6174915084140983974L;

    /**
     * <code>1010110010100101110110101010010101110100101001001011001001010110</code>
     * <p/>
     * Pattern group with half bit-flip-distance (32) to this pattern and <i>among</i> each other:
     * <ul>
     * <li>{@link #P_LONGM_32_S0_013X} (<code>0101001010101100111010111001001101011010011001010010011100101010</code>)</li>
     * <li>{@link #P_LONGM_32_S0_020X} (<code>0101010111001110010110010100100110011001001011001101101001110010</code>)</li>
     * <li>{@link #P_LONGM_32_S0_039X} (<code>0110110101011001011010010100101101010110100101101010010010011010</code>)</li>
     * <li>{@link #P_LONGM_32_S1_141} (<code>1010011100101001011001011100110111010101001010010010100100100111</code>)</li>
     * </ul>
     */
    public static final long P_LONGM_32_S1_080X = -6006154123867278762L;

    /**
     * <code>1001001011010101011010100111011001001001100100101001001101110011</code>
     * <p/>
     * Pattern group with half bit-flip-distance (32) to this pattern and <i>among</i> each other:
     * <ul>
     * <li>{@link #P_LONGM_32_S0_073} (<code>0101010010010010110100101001011001100111001110011001110010010111</code>)</li>
     * <li>{@link #P_LONGM_32_S0_078} (<code>0101010110100101001101001011100100101010100110101010111001100101</code>)</li>
     * <li>{@link #P_LONGM_32_S0_086} (<code>0101110101010110010010011001001110100100101010100111001101011001</code>)</li>
     * <li>{@link #P_LONGM_32_S1_116} (<code>1001011100100100111001001010010011010111010010010011101101001011</code>)</li>
     * </ul>
     */
    public static final long P_LONGM_32_S1_105 = -7866264117859675277L;

    /**
     * <code>1001011100100100111001001010010011010111010010010011101101001011</code>
     * <p/>
     * Pattern group with half bit-flip-distance (32) to this pattern and <i>among</i> each other:
     * <ul>
     * <li>{@link #P_LONGM_32_S0_073} (<code>0101010010010010110100101001011001100111001110011001110010010111</code>)</li>
     * <li>{@link #P_LONGM_32_S0_078} (<code>0101010110100101001101001011100100101010100110101010111001100101</code>)</li>
     * <li>{@link #P_LONGM_32_S0_086} (<code>0101110101010110010010011001001110100100101010100111001101011001</code>)</li>
     * <li>{@link #P_LONGM_32_S1_105} (<code>1001001011010101011010100111011001001001100100101001001101110011</code>)</li>
     * </ul>
     */
    public static final long P_LONGM_32_S1_116 = -7555662878183179445L;

    /**
     * <code>1001100100100111010011010010100101101101101101010010011010011001</code>
     * <p/>
     * Pattern group with half bit-flip-distance (32) to this pattern and <i>among</i> each other:
     * <ul>
     * <li>{@link #P_LONGM_32_S0_031X} (<code>0110010110101011011001100100101101001011101010100101110010010010</code>)</li>
     * <li>{@link #P_LONGM_32_S0_037} (<code>0011010011011101011010110011001010101011001001101001011001001001</code>)</li>
     * <li>{@link #P_LONGM_32_S1_140} (<code>1010011011001011010010101100100110110010010110011010011010010101</code>)</li>
     * <li>{@link #P_LONGM_32_S1_192} (<code>1110011001101001001001010111010100100101011010010110101001001011</code>)</li>
     * </ul>
     */
    public static final long P_LONGM_32_S1_118 = -7410869821485275495L;

    /**
     * <code>1001100100110010010010100101100110100111011001001110101001110011</code>
     * <p/>
     * Pattern group with half bit-flip-distance (32) to this pattern and <i>among</i> each other:
     * <ul>
     * <li>{@link #P_LONGM_32_S1_048X} (<code>1001001010010010010101010010101001011100111010111010111001010110</code>)</li>
     * <li>{@link #P_LONGM_32_S1_057X} (<code>1001011001100101011011010010101110011001001001100101101010101010</code>)</li>
     * <li>{@link #P_LONGM_32_S1_069X} (<code>1010010010111011001100101001001011010110101001010101110010110010</code>)</li>
     * <li>{@link #P_LONGM_32_S1_076X} (<code>1010101001001110010010110111001001110011100100110100100101011010</code>)</li>
     * </ul>
     */
    public static final long P_LONGM_32_S1_119 = -7407776688150091149L;

    /**
     * <code>1001101010110110010010010110010111010010010110010101001101010101</code>
     * <p/>
     * Pattern group with half bit-flip-distance (32) to this pattern and <i>among</i> each other:
     * <ul>
     * <li>{@link #P_LONGM_32_S0_015} (<code>0010101001011010101010101010100100100111011011100101101001100101</code>)</li>
     * <li>{@link #P_LONGM_32_S0_020} (<code>0010101011100111001011001010010011001100100101100110110100111001</code>)</li>
     * <li>{@link #P_LONGM_32_S0_021} (<code>0010101100100101011100100111001001101011001110011100100100100111</code>)</li>
     * <li>{@link #P_LONGM_32_S0_029} (<code>0011001011001011001110010100101010101010010110010010111011001011</code>)</li>
     * </ul>
     */
    public static final long P_LONGM_32_S1_125 = -7298565444437716139L;

    /**
     * <code>1010010101010010101100101010010010101001101011011011001011001011</code>
     * <p/>
     * Pattern group with half bit-flip-distance (32) to this pattern and <i>among</i> each other:
     * <ul>
     * <li>{@link #P_LONGM_32_S0_077} (<code>0101010101001001110100101110100110101010100101110100110010100101</code>)</li>
     * <li>{@link #P_LONGM_32_S1_050X} (<code>1001001100100101100101001001101110110011001010011100111011010010</code>)</li>
     * <li>{@link #P_LONGM_32_S1_072X} (<code>1010011001001110011001001101011010101010010010010110110100101110</code>)</li>
     * <li>{@link #P_LONGM_32_S1_139} (<code>1010011010010011010010010100110110011100100110010100111011011001</code>)</li>
     * </ul>
     */
    public static final long P_LONGM_32_S1_138 = -6533963689070054709L;

    /**
     * <code>1010011010010011010010010100110110011100100110010100111011011001</code>
     * <p/>
     * Pattern group with half bit-flip-distance (32) to this pattern and <i>among</i> each other:
     * <ul>
     * <li>{@link #P_LONGM_32_S0_077} (<code>0101010101001001110100101110100110101010100101110100110010100101</code>)</li>
     * <li>{@link #P_LONGM_32_S1_050X} (<code>1001001100100101100101001001101110110011001010011100111011010010</code>)</li>
     * <li>{@link #P_LONGM_32_S1_072X} (<code>1010011001001110011001001101011010101010010010010110110100101110</code>)</li>
     * <li>{@link #P_LONGM_32_S1_138} (<code>1010010101010010101100101010010010101001101011011011001011001011</code>)</li>
     * </ul>
     */
    public static final long P_LONGM_32_S1_139 = -6443726044148445479L;

    /**
     * <code>1010011011001011010010101100100110110010010110011010011010010101</code>
     * <p/>
     * Pattern group with half bit-flip-distance (32) to this pattern and <i>among</i> each other:
     * <ul>
     * <li>{@link #P_LONGM_32_S0_031X} (<code>0110010110101011011001100100101101001011101010100101110010010010</code>)</li>
     * <li>{@link #P_LONGM_32_S0_037} (<code>0011010011011101011010110011001010101011001001101001011001001001</code>)</li>
     * <li>{@link #P_LONGM_32_S1_118} (<code>1001100100100111010011010010100101101101101101010010011010011001</code>)</li>
     * <li>{@link #P_LONGM_32_S1_192} (<code>1110011001101001001001010111010100100101011010010110101001001011</code>)</li>
     * </ul>
     */
    public static final long P_LONGM_32_S1_140 = -6427961813000149355L;

    /**
     * <code>1010011100101001011001011100110111010101001010010010100100100111</code>
     * <p/>
     * Pattern group with half bit-flip-distance (32) to this pattern and <i>among</i> each other:
     * <ul>
     * <li>{@link #P_LONGM_32_S0_013X} (<code>0101001010101100111010111001001101011010011001010010011100101010</code>)</li>
     * <li>{@link #P_LONGM_32_S0_020X} (<code>0101010111001110010110010100100110011001001011001101101001110010</code>)</li>
     * <li>{@link #P_LONGM_32_S0_039X} (<code>0110110101011001011010010100101101010110100101101010010010011010</code>)</li>
     * <li>{@link #P_LONGM_32_S1_080X} (<code>1010110010100101110110101010010101110100101001001011001001010110</code>)</li>
     * </ul>
     */
    public static final long P_LONGM_32_S1_141 = -6401473460611503833L;

    /**
     * <code>1010100111010011010101001001100101001110010010101110101011001001</code>
     * <p/>
     * Pattern group with half bit-flip-distance (32) to this pattern and <i>among</i> each other:
     * <ul>
     * <li>{@link #P_LONGM_32_S0_038X} (<code>0110101001010011001010010101101101110100111010010010011001010110</code>)</li>
     * <li>{@link #P_LONGM_32_S0_043} (<code>0011101010101010011100110010101101010010101011100110010100101001</code>)</li>
     * <li>{@link #P_LONGM_32_S0_046X} (<code>0111010111001010010010010011010011001010101110110100101001100110</code>)</li>
     * <li>{@link #P_LONGM_32_S0_084} (<code>0101101101010010110110100101010010011010010010101011010100110101</code>)</li>
     * </ul>
     */
    public static final long P_LONGM_32_S1_147 = -6209526443793519927L;

    /**
     * <code>1010101010110010010101110011100100101011010110010110101001001001</code>
     * <p/>
     * Pattern group with half bit-flip-distance (32) to this pattern and <i>among</i> each other:
     * <ul>
     * <li>{@link #P_LONGM_32_S0_004} (<code>0010010100101001001001101010111001110010010101110011101110101001</code>)</li>
     * <li>{@link #P_LONGM_32_S0_008X} (<code>0100110100101010100101001011001100101011010010101001110111010110</code>)</li>
     * <li>{@link #P_LONGM_32_S0_018} (<code>0010101001110010101001010010011001001101101101001010110110011011</code>)</li>
     * <li>{@link #P_LONGM_32_S0_049} (<code>0100100101011011101010101001110011001001011010010100100101101011</code>)</li>
     * </ul>
     * <p/>
     * Pattern group 2 with half bit-flip-distance (32) to this pattern and <i>among</i> each other:
     * <ul>
     * <li>{@link #P_LONGM_32_S0_008X} (<code>0100110100101010100101001011001100101011010010101001110111010110</code>)</li>
     * <li>{@link #P_LONGM_32_S0_011} (<code>0010011100101101001001110101101001101001110100110011010100100101</code>)</li>
     * <li>{@link #P_LONGM_32_S0_018} (<code>0010101001110010101001010010011001001101101101001010110110011011</code>)</li>
     * <li>{@link #P_LONGM_32_S0_090} (<code>0110100101010011011101001100111011001010010110101001001010101001</code>)</li>
     * </ul>
     * <p/>
     * Pattern group 3 with half bit-flip-distance (32) to this pattern and <i>among</i> each other:
     * <ul>
     * <li>{@link #P_LONGM_32_S0_011} (<code>0010011100101101001001110101101001101001110100110011010100100101</code>)</li>
     * <li>{@link #P_LONGM_32_S0_018} (<code>0010101001110010101001010010011001001101101101001010110110011011</code>)</li>
     * <li>{@link #P_LONGM_32_S0_025X} (<code>0101100101100110110011100101001100101101110010100100110010101010</code>)</li>
     * <li>{@link #P_LONGM_32_S0_090} (<code>0110100101010011011101001100111011001010010110101001001010101001</code>)</li>
     * </ul>
     */
    public static final long P_LONGM_32_S1_149 = -6146754638355273143L;

    /**
     * <code>1110011001101001001001010111010100100101011010010110101001001011</code>
     * <p/>
     * Pattern group with half bit-flip-distance (32) to this pattern and <i>among</i> each other:
     * <ul>
     * <li>{@link #P_LONGM_32_S0_031X} (<code>0110010110101011011001100100101101001011101010100101110010010010</code>)</li>
     * <li>{@link #P_LONGM_32_S0_037} (<code>0011010011011101011010110011001010101011001001101001011001001001</code>)</li>
     * <li>{@link #P_LONGM_32_S1_118} (<code>1001100100100111010011010010100101101101101101010010011010011001</code>)</li>
     * <li>{@link #P_LONGM_32_S1_140} (<code>1010011011001011010010101100100110110010010110011010011010010101</code>)</li>
     * </ul>
     */
    public static final long P_LONGM_32_S1_192 = -1843901387362440629L;

    /**
     * <code>1110111001001011100100100100110101010100101110010010011001010101</code>
     * <p/>
     * Pattern group with half bit-flip-distance (32) to this pattern and <i>among</i> each other:
     * <ul>
     * <li>{@link #P_LONGM_32_S0_026X} (<code>0101101001010111011011010100101011010010010011010010011010110010</code>)</li>
     * <li>{@link #P_LONGM_32_S0_031X} (<code>0110010110101011011001100100101101001011101010100101110010010010</code>)</li>
     * <li>{@link #P_LONGM_32_S0_049} (<code>0100100101011011101010101001110011001001011010010100100101101011</code>)</li>
     * <li>{@link #P_LONGM_32_S1_076X} (<code>1010101001001110010010110111001001110011100100110100100101011010</code>)</li>
     * </ul>
     */
    public static final long P_LONGM_32_S1_200 = -1275765208597846443L;

    private PrimePatterns() {
        // constant class
    }
}