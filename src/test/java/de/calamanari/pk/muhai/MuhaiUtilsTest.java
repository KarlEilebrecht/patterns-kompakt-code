//@formatter:off
/*
 * MuhaiUtilsTest
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

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Random;
import java.util.function.Function;

import org.junit.Test;

public class MuhaiUtilsTest {

    @Test
    public void testForwardBackwardConversion() {
        assertFromToXString(MuhaiUtils::toIntString, MuhaiUtils::fromIntString);
        assertFromToXString(MuhaiUtils::toBinaryString, MuhaiUtils::fromBinaryString);
        assertFromToXString(MuhaiUtils::toHexString, MuhaiUtils::fromHexString);
    }

    private void assertFromToXString(Function<Long, String> convertToString, Function<String, Long> convertFromString) {
        assertEquals(Long.MIN_VALUE, convertFromString.apply(convertToString.apply(Long.MIN_VALUE)));

        assertEquals(Long.MIN_VALUE, convertFromString.apply(convertToString.apply(Long.MIN_VALUE)));
        assertEquals(0L, convertFromString.apply(convertToString.apply(0L)));
        assertEquals(-1L, convertFromString.apply(convertToString.apply(-1L)));
        assertEquals(Long.MAX_VALUE, convertFromString.apply(convertToString.apply(Long.MAX_VALUE)));
        Random rand = new Random(91837);
        for (int i = 0; i < 500; i++) {
            long value = rand.nextLong();
            assertEquals(value, convertFromString.apply(convertToString.apply(value)));
        }

        assertEquals(354L, convertFromString.apply("000" + convertToString.apply(354L)));

    }

    @Test
    public void testPaddedIntString() {

        System.out.println(MuhaiUtils.toIntString(Long.MIN_VALUE));
        System.out.println(MuhaiUtils.toIntString(-1));
        System.out.println(MuhaiUtils.toIntString(Long.MAX_VALUE));

        assertEquals("09223372036854775808", MuhaiUtils.toPaddedIntString(Long.MIN_VALUE));
        assertEquals("09223372036854775807", MuhaiUtils.toPaddedIntString(Long.MAX_VALUE));
        assertEquals("18446744073709551615", MuhaiUtils.toPaddedIntString(-1L));
        Random rand = new Random(91837);
        for (int i = 0; i < 500; i++) {
            int value = rand.nextInt(Integer.MAX_VALUE);
            assertEquals(String.format("%1$20s", value).replace(' ', '0'), MuhaiUtils.toPaddedIntString(value));
        }

    }

}
