//@formatter:off
/*
 * HashGeneratorTest 
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

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Arrays;

import org.junit.Test;

import de.calamanari.pk.ohbf.HashGenerators.CompositeHashGenerator;
import de.calamanari.pk.ohbf.HashGenerators.DefaultHashGenerator;
import de.calamanari.pk.util.CloneUtils;

/**
 * Test coverage for hash generator creation
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class HashGeneratorTest {

    @Test
    public void testBasics() throws Exception {
        HashGenerator generator1 = HashGenerators.createInstance(1);
        HashGenerator generator160 = HashGenerators.createInstance(160);

        assertSame(generator1, generator160);

        assertSame(generator1, CloneUtils.passByValue(generator160));

        for (int bitCount : new int[] { 1, 156, 200, 256, 300, 400, 512 }) {
            HashGenerator generator = HashGenerators.createInstance(bitCount);
            assertTrue(generator instanceof DefaultHashGenerator);
            assertEquals(generator.getHashLength(), generator.computeHashBytes("Rama").length);
            assertSame(generator, CloneUtils.passByValue(generator));
            assertNotEquals(Arrays.toString(generator.computeHashBytes("Rama")), Arrays.toString(generator.computeHashBytes("Rama1")));
        }

        for (int bitCount : new int[] { 513, 600, 1024, 3000 }) {
            HashGenerator generator = HashGenerators.createInstance(bitCount);
            assertTrue(generator instanceof CompositeHashGenerator);

            int expectedHashLength = 0;
            int sum = bitCount;
            while (sum >= 512) {
                sum = sum - 512;
                expectedHashLength = expectedHashLength + 64;
            }
            while (sum >= 256) {
                sum = sum - 256;
                expectedHashLength = expectedHashLength + 32;
            }
            while (sum > 0) {
                sum = sum - 160;
                expectedHashLength = expectedHashLength + 20;
            }
            assertEquals(expectedHashLength, generator.getHashLength());
            assertEquals(generator.getHashLength(), generator.computeHashBytes("Rama").length);
            HashGenerator clone = CloneUtils.passByValue(generator);
            assertNotSame(generator, clone);
            assertArrayEquals(generator.computeHashBytes("Rama"), clone.computeHashBytes("Rama"));

            assertNotEquals(Arrays.toString(generator.computeHashBytes("Rama")), Arrays.toString(generator.computeHashBytes("Rama1")));
        }

        assertThrows(IllegalArgumentException.class, () -> HashGenerators.createInstance(-1));
        assertThrows(IllegalArgumentException.class, () -> HashGenerators.createInstance(0));

    }

}
