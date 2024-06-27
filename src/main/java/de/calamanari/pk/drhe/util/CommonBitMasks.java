//@formatter:off
/*
 * CommonBitMasks
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
 * This class contains common bit masks when working with bytes, shorts, ints or long values on binary level.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class CommonBitMasks {

    /**
     * <code>0111111111111111111111111111111111111111111111111111111111111111</code>
     */
    public static final long UNSET_FIRST_BIT_MASK_LONG = Long.MAX_VALUE;

    /**
     * <code>1000000000000000000000000000000000000000000000000000000000000000</code>
     */
    public static final long SET_FIRST_BIT_MASK_LONG = Long.MIN_VALUE;

    /**
     * <code>1111111111111111111111111111111111111111111111111111111111111110</code>
     */
    public static final long UNSET_LAST_BIT_MASK_LONG = -2L;

    /**
     * <code>0000000000000000000000000000000000000000000000000000000000000001</code>
     */
    public static final long SET_LAST_BIT_MASK_LONG = 1L;

    /**
     * <code>1111111111111111111111111111111111111111111111111111111111111111</code>
     */
    public static final long ALL_BITS_MASK_LONG = -1L;

    /**
     * <code>0000000000000000000000000000000000000000000000000000000000000000</code>
     */
    public static final long NO_BITS_MASK_LONG = 0L;

    /**
     * <code>1010101010101010101010101010101010101010101010101010101010101010</code>
     */
    public static final long TIKTOK_MASK_LONG = -6148914691236517206L;

    /**
     * <code>01111111111111111111111111111111</code>
     */
    public static final int UNSET_FIRST_BIT_MASK_INT = Integer.MAX_VALUE;

    /**
     * <code>10000000000000000000000000000000</code>
     */
    public static final int SET_FIRST_BIT_MASK_INT = Integer.MIN_VALUE;

    /**
     * <code>11111111111111111111111111111110</code>
     */
    public static final int UNSET_LAST_BIT_MASK_INT = -2;

    /**
     * <code>00000000000000000000000000000001</code>
     */
    public static final int SET_LAST_BIT_MASK_INT = 1;

    /**
     * <code>11111111111111111111111111111111</code>
     */
    public static final int ALL_BITS_MASK_INT = -1;

    /**
     * <code>00000000000000000000000000000000</code>
     */
    public static final int NO_BITS_MASK_INT = 0;

    /**
     * <code>10101010101010101010101010101010</code>
     */
    public static final int TIKTOK_MASK_INT = -1431655766;

    /**
     * <code>0111111111111111</code>
     */
    public static final short UNSET_FIRST_BIT_MASK_SHORT = Short.MAX_VALUE;

    /**
     * <code>1000000000000000</code>
     */
    public static final short SET_FIRST_BIT_MASK_SHORT = Short.MIN_VALUE;

    /**
     * <code>1111111111111110</code>
     */
    public static final short UNSET_LAST_BIT_MASK_SHORT = -2;

    /**
     * <code>0000000000000001</code>
     */
    public static final short SET_LAST_BIT_MASK_SHORT = 1;

    /**
     * <code>1111111111111111</code>
     */
    public static final short ALL_BITS_MASK_SHORT = -1;

    /**
     * <code>0000000000000000</code>
     */
    public static final short NO_BITS_MASK_SHORT = 0;

    /**
     * <code>1010101010101010</code>
     */
    public static final short TIKTOK_MASK_SHORT = -21846;

    /**
     * <code>01111111</code>
     */
    public static final byte UNSET_FIRST_BIT_MASK_BYTE = Byte.MAX_VALUE;

    /**
     * <code>10000000</code>
     */
    public static final byte SET_FIRST_BIT_MASK_BYTE = Byte.MIN_VALUE;

    /**
     * <code>11111110</code>
     */
    public static final byte UNSET_LAST_BIT_MASK_BYTE = -2;

    /**
     * <code>00000001</code>
     */
    public static final byte SET_LAST_BIT_MASK_BYTE = 1;

    /**
     * <code>11111111</code>
     */
    public static final byte ALL_BITS_MASK_BYTE = -1;

    /**
     * <code>00000000</code>
     */
    public static final byte NO_BITS_MASK_BYTE = 0;

    /**
     * <code>10101010</code>
     */
    public static final byte TIKTOK_MASK_BYTE = -86;

    /**
     * <code>1111111111111111111111111111111100000000000000000000000000000000</code>
     */
    public static final long INT0_MASK_LONG = -4294967296L;

    /**
     * <code>0000000000000000000000000000000011111111111111111111111111111111</code>
     */
    public static final long INT1_MASK_LONG = 4294967295L;

    /**
     * <code>1111111111111111000000000000000000000000000000000000000000000000</code>
     */
    public static final long SHORT0_MASK_LONG = -281474976710656L;

    /**
     * <code>0000000000000000111111111111111100000000000000000000000000000000</code>
     */
    public static final long SHORT1_MASK_LONG = 281470681743360L;

    /**
     * <code>0000000000000000000000000000000011111111111111110000000000000000</code>
     */
    public static final long SHORT2_MASK_LONG = 4294901760L;

    /**
     * <code>0000000000000000000000000000000000000000000000001111111111111111</code>
     */
    public static final long SHORT3_MASK_LONG = 65535L;

    /**
     * <code>11111111111111110000000000000000</code>
     */
    public static final int SHORT0_MASK_INT = -65536;

    /**
     * <code>00000000000000001111111111111111</code>
     */
    public static final int SHORT1_MASK_INT = 65535;

    /**
     * <code>1111111100000000000000000000000000000000000000000000000000000000</code>
     */
    public static final long BYTE0_MASK_LONG = -72057594037927936L;

    /**
     * <code>0000000011111111000000000000000000000000000000000000000000000000</code>
     */
    public static final long BYTE1_MASK_LONG = 71776119061217280L;

    /**
     * <code>0000000000000000111111110000000000000000000000000000000000000000</code>
     */
    public static final long BYTE2_MASK_LONG = 280375465082880L;

    /**
     * <code>0000000000000000000000001111111100000000000000000000000000000000</code>
     */
    public static final long BYTE3_MASK_LONG = 1095216660480L;

    /**
     * <code>0000000000000000000000000000000011111111000000000000000000000000</code>
     */
    public static final long BYTE4_MASK_LONG = 4278190080L;

    /**
     * <code>0000000000000000000000000000000000000000111111110000000000000000</code>
     */
    public static final long BYTE5_MASK_LONG = 16711680L;

    /**
     * <code>0000000000000000000000000000000000000000000000001111111100000000</code>
     */
    public static final long BYTE6_MASK_LONG = 65280L;

    /**
     * <code>0000000000000000000000000000000000000000000000000000000011111111</code>
     */
    public static final long BYTE7_MASK_LONG = 255L;

    /**
     * <code>11111111000000000000000000000000</code>
     */
    public static final int BYTE0_MASK_INT = -16777216;

    /**
     * <code>00000000111111110000000000000000</code>
     */
    public static final int BYTE1_MASK_INT = 16711680;

    /**
     * <code>00000000000000001111111100000000</code>
     */
    public static final int BYTE2_MASK_INT = 65280;

    /**
     * <code>00000000000000000000000011111111</code>
     */
    public static final int BYTE3_MASK_INT = 255;

    /**
     * <code>1111111100000000</code>
     */
    public static final short BYTE0_MASK_SHORT = -256;

    /**
     * <code>0000000011111111</code>
     */
    public static final short BYTE1_MASK_SHORT = 255;

    /**
     * <code>0000000000000000000000000000000011111111111111111111111111111111</code>
     */
    public static final long INT0_MASK_INV_LONG = 4294967295L;

    /**
     * <code>1111111111111111111111111111111100000000000000000000000000000000</code>
     */
    public static final long INT1_MASK_INV_LONG = -4294967296L;

    /**
     * <code>0000000000000000111111111111111111111111111111111111111111111111</code>
     */
    public static final long SHORT0_MASK_INV_LONG = 281474976710655L;

    /**
     * <code>1111111111111111000000000000000011111111111111111111111111111111</code>
     */
    public static final long SHORT1_MASK_INV_LONG = -281470681743361L;

    /**
     * <code>1111111111111111111111111111111100000000000000001111111111111111</code>
     */
    public static final long SHORT2_MASK_INV_LONG = -4294901761L;

    /**
     * <code>1111111111111111111111111111111111111111111111110000000000000000</code>
     */
    public static final long SHORT3_MASK_INV_LONG = -65536L;

    /**
     * <code>00000000000000001111111111111111</code>
     */
    public static final int SHORT0_MASK_INV_INT = 65535;

    /**
     * <code>11111111111111110000000000000000</code>
     */
    public static final int SHORT1_MASK_INV_INT = -65536;

    /**
     * <code>0000000011111111111111111111111111111111111111111111111111111111</code>
     */
    public static final long BYTE0_MASK_INV_LONG = 72057594037927935L;

    /**
     * <code>1111111100000000111111111111111111111111111111111111111111111111</code>
     */
    public static final long BYTE1_MASK_INV_LONG = -71776119061217281L;

    /**
     * <code>1111111111111111000000001111111111111111111111111111111111111111</code>
     */
    public static final long BYTE2_MASK_INV_LONG = -280375465082881L;

    /**
     * <code>1111111111111111111111110000000011111111111111111111111111111111</code>
     */
    public static final long BYTE3_MASK_INV_LONG = -1095216660481L;

    /**
     * <code>1111111111111111111111111111111100000000111111111111111111111111</code>
     */
    public static final long BYTE4_MASK_INV_LONG = -4278190081L;

    /**
     * <code>1111111111111111111111111111111111111111000000001111111111111111</code>
     */
    public static final long BYTE5_MASK_INV_LONG = -16711681L;

    /**
     * <code>1111111111111111111111111111111111111111111111110000000011111111</code>
     */
    public static final long BYTE6_MASK_INV_LONG = -65281L;

    /**
     * <code>1111111111111111111111111111111111111111111111111111111100000000</code>
     */
    public static final long BYTE7_MASK_INV_LONG = -256L;

    /**
     * <code>00000000111111111111111111111111</code>
     */
    public static final int BYTE0_MASK_INV_INT = 16777215;

    /**
     * <code>11111111000000001111111111111111</code>
     */
    public static final int BYTE1_MASK_INV_INT = -16711681;

    /**
     * <code>11111111111111110000000011111111</code>
     */
    public static final int BYTE2_MASK_INV_INT = -65281;

    /**
     * <code>11111111111111111111111100000000</code>
     */
    public static final int BYTE3_MASK_INV_INT = -256;

    /**
     * <code>0000000011111111</code>
     */
    public static final short BYTE0_MASK_INV_SHORT = 255;

    /**
     * <code>1111111100000000</code>
     */
    public static final short BYTE1_MASK_INV_SHORT = -256;

    private CommonBitMasks() {
        // only constants
    }

}