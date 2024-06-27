//@formatter:off
/*
 * DistributionCodecTest
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
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.calamanari.pk.drhe.util.GenStats;

public class DistributionCodecTest {

    static final Logger LOGGER = LoggerFactory.getLogger(DistributionCodecTest.class);

    @Test
    public void testByteCodecFull() {

        GenStats stats = new GenStats(false);

        for (int i = 0; i < Byte.toUnsignedInt(Byte.MIN_VALUE) * 2; i++) {
            byte src = (byte) i;
            byte encoded = DistributionCodec.encode(src);
            byte decoded = DistributionCodec.decode(encoded);

            if (encoded == src) {
                LOGGER.error(String.format("SELF-MAPPED: %4d (%s) ---> %4d (%s)", src, binStr(src), encoded, binStr(encoded)));
            }

            assertNotEquals(src, encoded);

            assertEquals(src, decoded);

            stats.consume(src, encoded);

        }
        LOGGER.debug("{}", stats);

    }

    @Test
    public void testByteCodecPreserveSignFull() {

        GenStats stats = new GenStats(true);

        for (int i = 0; i < Byte.toUnsignedInt(Byte.MIN_VALUE) * 2; i++) {
            byte src = (byte) i;
            byte encoded = DistributionCodec.encodePreserveSign(src);

            assertTrue((src < 0 && encoded < 0) || (src >= 0 && encoded >= 0));
            byte decoded = DistributionCodec.decodePreserveSign(encoded);

            if (encoded == src) {
                LOGGER.error(String.format("SELF-MAPPED: %4d (%s) ---> %4d (%s)", src, binStr(src), encoded, binStr(encoded)));
            }

            assertNotEquals(src, encoded);

            assertEquals(src, decoded);

            stats.consume(src, encoded);

        }
        LOGGER.debug("{}", stats);

    }

    @Test
    public void testShortCodecFull() {

        GenStats stats = new GenStats(false);

        for (int i = 0; i < Short.toUnsignedInt(Short.MIN_VALUE) * 2; i++) {
            short src = (short) i;
            short encoded = DistributionCodec.encode(src);
            short decoded = DistributionCodec.decode(encoded);

            if (encoded == src) {
                LOGGER.error(String.format("SELF-MAPPED: %6d (%s) ---> %6d (%s)", src, binStr(src), encoded, binStr(encoded)));
            }

            assertNotEquals(src, encoded);

            assertEquals(src, decoded);

            stats.consume(src, encoded);

        }
        LOGGER.debug("{}", stats);

    }

    @Test
    public void testShortCodecPreserveSignFull() {

        GenStats stats = new GenStats(true);

        for (int i = 0; i < Short.toUnsignedInt(Short.MIN_VALUE) * 2; i++) {
            short src = (short) i;
            short encoded = DistributionCodec.encodePreserveSign(src);

            assertTrue((src < 0 && encoded < 0) || (src >= 0 && encoded >= 0));
            short decoded = DistributionCodec.decodePreserveSign(encoded);

            if (encoded == src) {
                LOGGER.error(String.format("SELF-MAPPED: %6d (%s) ---> %6d (%s)", src, binStr(src), encoded, binStr(encoded)));
            }

            assertNotEquals(src, encoded);

            assertEquals(src, decoded);

            stats.consume(src, encoded);

        }
        LOGGER.debug("{}", stats);

    }

    @Test
    @Ignore("Long running test")
    public void testIntCodecFull() {

        GenStats stats = new GenStats(false);

        for (long l = 0; l < Integer.toUnsignedLong(Integer.MIN_VALUE) * 2; l++) {
            int src = (int) l;
            int encoded = DistributionCodec.encode(src);
            int decoded = DistributionCodec.decode(encoded);

            if (encoded == src) {
                LOGGER.error(String.format("SELF-MAPPED: %11d (%s) ---> %11d (%s)", src, binStr(src), encoded, binStr(encoded)));
            }

            assertNotEquals(src, encoded);

            assertEquals(src, decoded);

            stats.consume(src, encoded);

            if (stats.getCount() % 100_000_000 == 0) {
                LOGGER.info("=============================================\n{}", stats);
            }

        }
        LOGGER.info("{}", stats);

    }

    @Test
    @Ignore("Long running test")
    public void testIntCodecPreserveSignFull() {

        GenStats stats = new GenStats(true);

        for (long l = 0; l < Integer.toUnsignedLong(Integer.MIN_VALUE) * 2; l++) {
            int src = (int) l;
            int encoded = DistributionCodec.encodePreserveSign(src);

            assertTrue((src < 0 && encoded < 0) || (src >= 0 && encoded >= 0));
            int decoded = DistributionCodec.decodePreserveSign(encoded);

            if (encoded == src) {
                LOGGER.error(String.format("SELF-MAPPED: %11d (%s) ---> %11d (%s)", src, binStr(src), encoded, binStr(encoded)));
            }

            assertNotEquals(src, encoded);

            assertEquals(src, decoded);

            stats.consume(src, encoded);

            if (stats.getCount() % 100_000_000 == 0) {
                LOGGER.info("=============================================\n{}", stats);
            }

        }
        LOGGER.info("{}", stats);

    }

    @Test
    @Ignore("Long running test")
    public void testLongCodec() {

        GenStats stats = new GenStats();

        for (long l = 0; l < Integer.toUnsignedLong(Integer.MIN_VALUE) * 10; l++) {
            long src = l;
            long encoded = DistributionCodec.encode(src);
            long decoded = DistributionCodec.decode(encoded);

            if (encoded == src) {
                LOGGER.warn(String.format("SELF-MAPPED: %20d (%s) ---> %20d (%s)", src, binStr(src), encoded, binStr(encoded)));
            }

            assertEquals(src, decoded);

            stats.consume(src, encoded);

            if (stats.getCount() % 100_000_000 == 0) {
                LOGGER.info("=============================================\n{}", stats);
            }

        }

        LOGGER.info("{}", stats);

    }

    @Test
    @Ignore("Long running test to gather some stats")
    public void testLongCodecPreserveSign() {

        GenStats stats = new GenStats(true);

        for (long l = Integer.MIN_VALUE; l < Integer.toUnsignedLong(Integer.MIN_VALUE) * 2; l++) {
            long src = l;
            long encoded = DistributionCodec.encodePreserveSign(src);

            assertTrue((src < 0 && encoded < 0) || (src >= 0 && encoded >= 0));
            long decoded = DistributionCodec.decodePreserveSign(encoded);

            if (encoded == src) {
                LOGGER.warn(String.format("SELF-MAPPED: %20d (%s) ---> %20d (%s)", src, binStr(src), encoded, binStr(encoded)));
            }

            assertEquals(src, decoded);

            stats.consume(src, encoded);

            if (stats.getCount() % 100_000_000 == 0) {
                LOGGER.info("=============================================\n{}", stats);
            }

        }
        LOGGER.info("{}", stats);

    }

}