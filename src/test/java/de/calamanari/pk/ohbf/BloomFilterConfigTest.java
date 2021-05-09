//@formatter:off
/*
 * BloomFilterConfigTest
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
package de.calamanari.pk.ohbf;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.Test;

/**
 * Test coverage for the bloom filter configuration and estimation
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class BloomFilterConfigTest {

    @Test
    public void testWithNandM() {
        BloomFilterConfig config = new BloomFilterConfig(10000000, 600000);

        assertEquals(600000, config.getNumberOfInsertedElementsN());
        assertEquals(0.0003329393d, config.getFalsePositiveRateEpsilon(), 0.00000000005d);
        assertEquals(10000000, config.getRequiredNumberOfBitsM());
        assertEquals(12, config.getNumberOfHashesK());

    }

    @Test
    public void testWithNandEpsilon() {
        BloomFilterConfig config = new BloomFilterConfig(600000, 0.0003329393d);

        assertEquals(600000, config.getNumberOfInsertedElementsN());
        assertEquals(0.0003329393d, config.getFalsePositiveRateEpsilon(), 0.00000000001d);
        assertEquals(10000001, config.getRequiredNumberOfBitsM());
        assertEquals(12, config.getNumberOfHashesK());

        config = new BloomFilterConfig(600000, 0.000001d);

        assertEquals(600000, config.getNumberOfInsertedElementsN());
        assertEquals(0.000001d, config.getFalsePositiveRateEpsilon(), 0.0000001d);
        assertEquals(17253106, config.getRequiredNumberOfBitsM());
        assertEquals(20, config.getNumberOfHashesK());

        config = new BloomFilterConfig(600000, 0.00000001d);

        assertEquals(600000, config.getNumberOfInsertedElementsN());
        assertEquals(0.00000001d, config.getFalsePositiveRateEpsilon(), 0.000000001d);
        assertEquals(23004141, config.getRequiredNumberOfBitsM());
        assertEquals(27, config.getNumberOfHashesK());

        config = new BloomFilterConfig(600000, 0.000000001d);

        assertEquals(600000, config.getNumberOfInsertedElementsN());
        assertEquals(0.000000001d, config.getFalsePositiveRateEpsilon(), 0.0000000001d);
        assertEquals(25879658, config.getRequiredNumberOfBitsM());
        assertEquals(30, config.getNumberOfHashesK());

    }

    @Test
    public void testWithEpsilonAndM() {
        BloomFilterConfig config = new BloomFilterConfig(0.0003329393d, 10000000);

        assertEquals(600000, config.getNumberOfInsertedElementsN());
        assertEquals(0.0003329393d, config.getFalsePositiveRateEpsilon(), 0.0000000001d);
        assertEquals(10000000, config.getRequiredNumberOfBitsM());
        assertEquals(12, config.getNumberOfHashesK());

        config = new BloomFilterConfig(0.000001d, 17253106);

        assertEquals(600001, config.getNumberOfInsertedElementsN());
        assertEquals(0.000001d, config.getFalsePositiveRateEpsilon(), 0.0000001d);
        assertEquals(17253106, config.getRequiredNumberOfBitsM());
        assertEquals(20, config.getNumberOfHashesK());

        config = new BloomFilterConfig(0.00000001d, 23004141);
        assertEquals(600001, config.getNumberOfInsertedElementsN());
        assertEquals(0.00000001d, config.getFalsePositiveRateEpsilon(), 0.000000001d);
        assertEquals(23004141, config.getRequiredNumberOfBitsM());
        assertEquals(27, config.getNumberOfHashesK());

        config = new BloomFilterConfig(0.00000001d, 1000000);

        assertEquals(26083, config.getNumberOfInsertedElementsN());
        assertEquals(0.00000001d, config.getFalsePositiveRateEpsilon(), 0.000000001d);
        assertEquals(1000000, config.getRequiredNumberOfBitsM());
        assertEquals(27, config.getNumberOfHashesK());

    }

}
