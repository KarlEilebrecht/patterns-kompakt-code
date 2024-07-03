//@formatter:off
/*
 * DistributionCodecHashTest
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
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.StandardCharsets;

import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.calamanari.pk.drhe.util.BitUtils;
import de.calamanari.pk.drhe.util.GenStats;

/**
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class DistributionCodecHashBuilderTest {

    static final Logger LOGGER = LoggerFactory.getLogger(DistributionCodecHashBuilderTest.class);

    @Test
    @Ignore("Long running, just to gather some stats")
    public void testHash() {

        GenStats stats = new GenStats();

        DistributionCodecHashBuilder digest = new DistributionCodecHashBuilder();

        // the long-range (2^64) is too big to iterate through all possible values
        // thus we take a large sample (2^32 values) and use the increment 2^32
        // this way we are sure to consider values across the whole range

        long increment = Integer.toUnsignedLong(Integer.MIN_VALUE) * 2;

        long src = Long.MIN_VALUE;

        for (long l = 0; l < increment; l++) {

            digest.update(src);

            long hash = digest.getHashValue();
            digest.reset();

            if (hash == src) {
                LOGGER.info(String.format("SELF-HASHED: %20d (%s) ---> %20d (%s)", src, binStr(src), hash, binStr(hash)));
            }

            stats.consume(src, hash);

            if (stats.getCount() % 100_000_000 == 0) {
                LOGGER.info("=============================================\n{}", stats);
            }
            src = src + increment;

        }

        assertTrue(stats.getCount() > 0);
        LOGGER.info("\n{}", stats);

    }

    private long hash(DistributionCodecHashBuilder digest, String input, String salt) {

        digest.update(salt.getBytes(StandardCharsets.UTF_8));
        digest.update('>');
        digest.update(input.getBytes(StandardCharsets.UTF_8));
        digest.update('<');
        long res = digest.getHashValue();

        LOGGER.debug("{} --> {} ({})", input, res, BitUtils.binStr(res));
        digest.reset();

        return res;
    }

    @Test
    public void testRandomPatternFromHash() {

        // In this simulation we take a very short sentence as a basis.
        // Because a single hash value won't be long enough
        // we concatenate hash values until we reach the required 1 M digits
        // to perform tests at https://mzsoltmolnar.github.io/random-bitstream-tester/

        DistributionCodecHashBuilder digest = new DistributionCodecHashBuilder();

        String input = "Fluffy, Tuffy and Muffy went to town. They all got killed in a terrible accident.";

        StringBuilder sb = new StringBuilder();

        int len = 1_000_000;
        int salt = 0;

        do {
            salt++;
            sb.append(binStr(hash(digest, input, Integer.toHexString(salt))));
        } while (sb.length() < len);

        assertTrue(sb.length() >= len);

        sb.setLength(len);

        LOGGER.debug("\n{}", sb);

    }

}