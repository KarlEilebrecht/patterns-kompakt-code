//@formatter:off
/*
 * XoRotUtilsTest
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

import static de.calamanari.pk.drhe.util.BitUtils.parseByte;
import static de.calamanari.pk.drhe.util.BitUtils.parseInt;
import static de.calamanari.pk.drhe.util.BitUtils.parseLong;
import static de.calamanari.pk.drhe.util.BitUtils.parseShort;
import static de.calamanari.pk.drhe.util.XoRotUtils.decode;
import static de.calamanari.pk.drhe.util.XoRotUtils.decodePreserveSign;
import static de.calamanari.pk.drhe.util.XoRotUtils.encode;
import static de.calamanari.pk.drhe.util.XoRotUtils.encodePreserveSign;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Random;

import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class XoRotUtilsTest {

    static final Logger LOGGER = LoggerFactory.getLogger(XoRotUtilsTest.class);

    private static final byte BYTE_PATTERN1 = parseByte("10101010");
    private static final byte BYTE_PATTERN2 = parseByte("01010101");
    private static final byte BYTE_PATTERN3 = parseByte("11111111");
    private static final byte BYTE_PATTERN4 = parseByte("00000000");
    private static final byte BYTE_PATTERN5 = parseByte("00001000");

    private static final short SHORT_PATTERN1 = parseShort("1010101010101010");
    private static final short SHORT_PATTERN2 = parseShort("0101010101010101");
    private static final short SHORT_PATTERN3 = parseShort("1111111111111111");
    private static final short SHORT_PATTERN4 = parseShort("0000000000000000");
    private static final short SHORT_PATTERN5 = parseShort("0000000010000000");

    private static final int INT_PATTERN1 = parseInt("10101010101010101010101010101010");
    private static final int INT_PATTERN2 = parseInt("01010101010101010101010101010101");
    private static final int INT_PATTERN3 = parseInt("11111111111111111111111111111111");
    private static final int INT_PATTERN4 = parseInt("00000000000000000000000000000000");
    private static final int INT_PATTERN5 = parseInt("00000000000000000100000000000000");

    private static final long LONG_PATTERN1 = parseLong("1010101010101010101010101010101010101010101010101010101010101010");
    private static final long LONG_PATTERN2 = parseLong("0101010101010101010101010101010101010101010101010101010101010101");
    private static final long LONG_PATTERN3 = parseLong("1111111111111111111111111111111111111111111111111111111111111111");
    private static final long LONG_PATTERN4 = parseLong("0000000000000000000000000000000000000000000000000000000000000000");
    private static final long LONG_PATTERN5 = parseLong("0000000000000000000000000000000100000000000000000000000000000000");

    private static final byte[] BYTE_PATTERNS = new byte[] { BYTE_PATTERN1, BYTE_PATTERN2, BYTE_PATTERN3, BYTE_PATTERN4, BYTE_PATTERN5 };
    private static final short[] SHORT_PATTERNS = new short[] { SHORT_PATTERN1, SHORT_PATTERN2, SHORT_PATTERN3, SHORT_PATTERN4, SHORT_PATTERN5 };
    private static final int[] INT_PATTERNS = new int[] { INT_PATTERN1, INT_PATTERN2, INT_PATTERN3, INT_PATTERN4, INT_PATTERN5 };
    private static final long[] LONG_PATTERNS = new long[] { LONG_PATTERN1, LONG_PATTERN2, LONG_PATTERN3, LONG_PATTERN4, LONG_PATTERN5 };

    @Test
    public void testEncodeDecodeByte() {

        for (int i = Byte.MIN_VALUE; i <= Byte.MAX_VALUE; i++) {

            byte b = (byte) i;
            assertEquals(b, decode(encode(b, BYTE_PATTERNS), BYTE_PATTERNS));

            byte encodedP = encodePreserveSign(b, BYTE_PATTERNS);
            assertSameSignum(b, encodedP);
            assertEquals(b, decodePreserveSign(encodedP, BYTE_PATTERNS));

        }

    }

    @Test
    public void testEncodeDecodeShort() {

        for (int i = Short.MIN_VALUE; i <= Short.MAX_VALUE; i++) {

            short s = (short) i;

            assertEquals(s, decode(encode(s, SHORT_PATTERNS), SHORT_PATTERNS));

            short encodedP = encodePreserveSign(s, SHORT_PATTERNS);
            assertSameSignum(s, encodedP);
            assertEquals(s, decodePreserveSign(encodedP, SHORT_PATTERNS));

        }

    }

    @Test
    @Ignore("Slow test of all possible integers")
    public void testEncodeDecodeIntFull() {

        GenStats stats = new GenStats();
        GenStats statsP = new GenStats(true);

        for (long l = Integer.MIN_VALUE; l <= Integer.MAX_VALUE; l++) {

            int i = (int) l;

            int encoded = encode(i, INT_PATTERNS);

            stats.consume(i, encoded);

            assertEquals(i, decode(encoded, INT_PATTERNS));

            int encodedP = encodePreserveSign(i, INT_PATTERNS);

            statsP.consume(i, encodedP);

            assertSameSignum(i, encodedP);
            assertEquals(i, decodePreserveSign(encodedP, INT_PATTERNS));

            if (i % 100_000_000 == 0) {
                LOGGER.info("-----\n{}\n\n-sign preserved-\n{}", stats, statsP);
            }

        }

    }

    @Test
    public void testEncodeDecodeIntSample() {

        int[] sampleEdgeCases = new int[] { Integer.MIN_VALUE, Integer.MIN_VALUE + 1, -2, -1, 0, 1, 2, Integer.MAX_VALUE - 1, Integer.MAX_VALUE };

        for (int i = 0; i < sampleEdgeCases.length; i++) {
            assertEquals(sampleEdgeCases[i], decode(encode(sampleEdgeCases[i], INT_PATTERNS), INT_PATTERNS));
            assertEquals(sampleEdgeCases[i], decodePreserveSign(encodePreserveSign(sampleEdgeCases[i], INT_PATTERNS), INT_PATTERNS));
        }

        Random rand = new Random(3826346821L);

        for (int i = 0; i < 10000; i++) {
            int value = rand.nextInt();
            assertEquals(value, decode(encode(value, INT_PATTERNS), INT_PATTERNS));
            assertEquals(value, decodePreserveSign(encodePreserveSign(value, INT_PATTERNS), INT_PATTERNS));

        }

    }

    @Test
    public void testEncodeDecodeLongSample() {

        long[] sampleEdgeCases = new long[] { Long.MIN_VALUE, Long.MIN_VALUE + 1L, -2L, -1L, 0L, 1L, 2L, Long.MAX_VALUE - 1L, Long.MAX_VALUE };

        for (int i = 0; i < sampleEdgeCases.length; i++) {
            assertEquals(sampleEdgeCases[i], decode(encode(sampleEdgeCases[i], LONG_PATTERNS), LONG_PATTERNS));
            assertEquals(sampleEdgeCases[i], decodePreserveSign(encodePreserveSign(sampleEdgeCases[i], LONG_PATTERNS), LONG_PATTERNS));
        }

        Random rand = new Random(3826346821L);

        for (int i = 0; i < 10000; i++) {
            long value = (Integer.toUnsignedLong(rand.nextInt()) << 32) | Integer.toUnsignedLong(rand.nextInt());
            assertEquals(value, decode(encode(value, LONG_PATTERNS), LONG_PATTERNS));
            assertEquals(value, decodePreserveSign(encodePreserveSign(value, LONG_PATTERNS), LONG_PATTERNS));

        }

    }

    private static void assertSameSignum(long expected, long actual) {
        assertTrue((expected < 0 && actual < 0) || (expected >= 0 && actual >= 0));
    }

}
