//@formatter:off
/*
 * DistributionCodecRandomGeneratorTest
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

import static de.calamanari.pk.drhe.util.BitUtils.binStr;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.calamanari.pk.drhe.util.GenStats;

/**
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class DistributionCodecRandomGeneratorTest {

    static final Logger LOGGER = LoggerFactory.getLogger(DistributionCodecRandomGeneratorTest.class);

    @Test
    @Ignore("Long running test to gather some stats")
    public void testRandom() {

        GenStats stats = new GenStats();

        DistributionCodecRandomGenerator rand = new DistributionCodecRandomGenerator();

        long increment = Integer.toUnsignedLong(Integer.MIN_VALUE) * 2;

        // to avoid biasing the src-dest distance we use the same virtual sampling
        // we earlier applied in the distribution test
        long ref = Long.MIN_VALUE;
        for (long l = 0; l < increment; l++) {
            long val = rand.nextValue();

            stats.consume(ref, val);

            if (stats.getCount() % 100_000_000 == 0) {
                LOGGER.info("=============================================\n{}", stats);
            }

            ref = ref + increment;
        }
        assertEquals(4294967296L, stats.getCount());
        LOGGER.info("\n{}", stats);

    }

    @Test
    public void testRandomPattern() {

        // This test case creates the input of 1 million digits to be tested with the NIST test suite

        DistributionCodecRandomGenerator rl = new DistributionCodecRandomGenerator(1);

        StringBuilder sb = new StringBuilder();

        int len = 1_000_000;

        do {
            long encoded = rl.nextValue();
            LOGGER.debug("{}", encoded);
            sb.append(binStr(encoded));

        } while (sb.length() < len);

        assertTrue(sb.length() >= len);

        sb.setLength(len);

        LOGGER.debug("\n{}", sb);

    }

}