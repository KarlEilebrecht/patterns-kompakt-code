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
import java.util.Arrays;

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

        for (long l = 0; l < Integer.toUnsignedLong(Integer.MIN_VALUE) * 2; l++) {
            long src = l - Integer.MAX_VALUE;

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

        }

        assertTrue(stats.getCount() > 0);
        LOGGER.info("{}", stats);

    }

    private long hash(DistributionCodecHashBuilder digest, String input, int blockNumber) {
        return hash(digest, input.getBytes(StandardCharsets.UTF_8), blockNumber);
    }

    private long hash(DistributionCodecHashBuilder digest, byte[] input, int blockNumber) {

        digest.update(blockNumber);
        digest.update((byte) '>');
        digest.update(input);
        digest.update((byte) '<');
        digest.update(blockNumber);
        long res = digest.getHashValue();

        LOGGER.debug("{} --> {} ({})", input, res, BitUtils.binStr(res));
        digest.reset();

        return res;
    }

    @Test
    public void testCreateRandomPatternFromFile() throws Exception {

        // The example payload is an uncompressed image with a lot of redundancy.
        // We take as much as we need from this file to concatenate hash values until we reach the required 1 M digits
        // to perform tests at https://mzsoltmolnar.github.io/random-bitstream-tester/

        byte[] input = DistributionCodecHashBuilderTest.class.getResourceAsStream("/example-payload.png").readAllBytes();

        DistributionCodecHashBuilder digest = new DistributionCodecHashBuilder();

        StringBuilder sb = new StringBuilder();

        int len = 1_000_000;

        int offset = 0;
        int blockNumber = 0;
        do {
            blockNumber++;
            sb.append(binStr(hash(digest, Arrays.copyOfRange(input, offset, offset + 100), blockNumber)));
            offset = offset + 100;
        } while (sb.length() < len);

        assertTrue(sb.length() >= len);

        sb.setLength(len);

        LOGGER.info("{}", sb);

    }

    @Test
    public void testCreateRandomPatternFromHash() {

        // In this extreme test we take a very short sentence as a basis.
        // We subsequently hash the input and concatenate hash values until we reach the required 1 M digits
        // to perform tests at https://mzsoltmolnar.github.io/random-bitstream-tester/

        DistributionCodecHashBuilder digest = new DistributionCodecHashBuilder();

        String input = "Fluffy, Tuffy and Muffy went to town. They all got killed in a terrible accident.";

        StringBuilder sb = new StringBuilder();

        int len = 1_000_000;
        int blockNumber = 0;

        do {
            blockNumber++;
            sb.append(binStr(hash(digest, input, blockNumber)));
        } while (sb.length() < len);

        assertTrue(sb.length() >= len);

        sb.setLength(len);

        LOGGER.info("{}", sb);

    }

}