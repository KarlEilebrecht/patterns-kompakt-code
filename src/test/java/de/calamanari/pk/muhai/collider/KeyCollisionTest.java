//@formatter:off
/*
 * KeyCollisionTest
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
package de.calamanari.pk.muhai.collider;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apfloat.Apfloat;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;

import de.calamanari.pk.muhai.LongPrefix;
import de.calamanari.pk.muhai.MuhaiGenerator;
import de.calamanari.pk.muhai.MuhaiUtils;
import de.calamanari.pk.muhai.collider.KeyCollisionProcessor.SummaryBuilder;

/**
 * MUHAI Key Collision Test - here we demonstrate the collision behavior by creating large amount of keys and detecting collisions.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class KeyCollisionTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(KeyCollisionTest.class);

    private static File tempDirectory;

    @BeforeClass
    public static void beforeAllTests() {
        try {
            tempDirectory = Files.createTempDirectory(KeyCollisionTest.class.getSimpleName() + "_").toFile();
        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        LOGGER.info("Temporary directory is: {}", tempDirectory);
    }

    @Test
    public void testApfloatComputation() {

        Apfloat value = KeyCollisionProcessor.val(Long.MAX_VALUE);

        assertEquals(Long.MAX_VALUE, value.longValue());

        assertEquals(MuhaiUtils.toIntString(Long.MIN_VALUE), KeyCollisionProcessor.val(Long.MIN_VALUE).toString(true));

        assertEquals(MuhaiUtils.toIntString(-1), KeyCollisionProcessor.val(-1).toString(true));

    }

    @Test
    public void testUtilityMethods() {

        assertEquals(Long.toUnsignedString(0), KeyCollisionProcessor.toUnsignedBigInteger(0).toString());
        assertEquals(Long.toUnsignedString(1), KeyCollisionProcessor.toUnsignedBigInteger(1).toString());
        assertEquals(Long.toUnsignedString(Integer.MAX_VALUE), KeyCollisionProcessor.toUnsignedBigInteger(Integer.MAX_VALUE).toString());
        assertEquals(Long.toUnsignedString(Long.MAX_VALUE), KeyCollisionProcessor.toUnsignedBigInteger(Long.MAX_VALUE).toString());
        assertEquals(Long.toUnsignedString(Long.MIN_VALUE), KeyCollisionProcessor.toUnsignedBigInteger(Long.MIN_VALUE).toString());
        assertEquals(Long.toUnsignedString(-1L), KeyCollisionProcessor.toUnsignedBigInteger(-1L).toString());

        // 0.01 percent * 10_000 > Long.MAX_VALE
        long perc0_01 = Long.MAX_VALUE / 8_000L;
        long total = perc0_01 * 10_000L;

        NumberFormat nf = NumberFormat.getInstance(Locale.US);
        nf.setMinimumFractionDigits(2);
        nf.setMaximumFractionDigits(2);
        double perc = 0.0;
        for (int i = 1; i < 10_001; i++) {
            perc = perc + 0.01;
            double percComputed = KeyCollisionProcessor.computePercentage(perc0_01 * i, total);
            assertEquals(nf.format(perc), KeyCollisionProcessor.formatPercentage(percComputed));
        }

    }

    @Test
    public void testSummary() throws Exception {
        SummaryBuilder summaryBuilder = KeyCollisionProcessor.SummaryBuilder.forKeyspaceSizeAndNumberOfKeysGenerated(1024, 50);

        summaryBuilder.addCollision(new TrackingKeyCollision(34, 17, 30));
        summaryBuilder.addCollision(new TrackingKeyCollision(35, 18, 19, 20));
        summaryBuilder.addCollision(new TrackingKeyCollision(400, 1, 2, 3));
        summaryBuilder.addCollision(new TrackingKeyCollision(500, 40, 41));
        summaryBuilder.addCollision(new TrackingKeyCollision(501, 42, 43));
        summaryBuilder.addCollision(new TrackingKeyCollision(502, 44, 45, 46, 47));

        KeyCollisionSummary summary = summaryBuilder.getResult();

        assertEquals(1024, summary.getSizeOfKeyspace());
        assertEquals(50, summary.getNumberOfKeysGenerated());
        assertEquals(6, summary.getNumberOfCollidedKeys());
        assertEquals(10, summary.getNumberOfCollisions());
        assertEquals(2, summary.getFirstCollisionPosition());
        assertEquals(3, summary.getMultiOccurrenceStats().size());
        assertEquals(3L, summary.getMultiOccurrenceStats().get(2L));
        assertEquals(2L, summary.getMultiOccurrenceStats().get(3L));
        assertEquals(1L, summary.getMultiOccurrenceStats().get(4L));
        String expectedDataPoints = "Number of keys generated;Collisions detected;Collisions expected|1;0;0|2;0;0|3;1;0|4;2;0|5;2;0|6;2;0|7;2;0|"
                + "8;2;0|9;2;0|10;2;0|11;2;0|12;2;0|13;2;0|14;2;0|15;2;0|16;2;0|17;2;0|18;2;0|19;2;0|20;3;0|21;4;0|22;4;0|23;4;0|24;4;0|"
                + "25;4;0|26;4;0|27;4;0|28;4;0|29;4;0|30;4;0|31;5;0|32;5;0|33;5;0|34;5;0|35;5;0|36;5;0|37;5;0|38;5;0|39;5;0|40;5;0|41;5;0|"
                + "42;6;0|43;6;0|44;7;0|45;7;0|46;8;0|47;9;1|48;10;1|49;10;1|50;10;1";
        assertEquals(expectedDataPoints, summary.getCollisionStatsPcsv());

        String summaryJson = formatSummary(summary);

        ObjectMapper mapper = createObjectMapper();

        KeyCollisionSummary summary2 = mapper.readValue(summaryJson, KeyCollisionSummary.class);

        assertEquals(summary.getCollisionStats(), summary2.getCollisionStats());

    }

    @Test
    public void testNoCollisionAtAll() throws Exception {

        AtomicLong sequence = new AtomicLong();

        KeyCollisionProcessor<?> proc = KeyCollisionProcessor.createDefaultProcessor(tempDirectory);

        KeyCollisionSummary summary = proc.process(() -> sequence.incrementAndGet(), 10000, 100_000_000);

        assertEquals(0L, summary.getNumberOfCollidedKeys());
        assertEquals(0L, summary.getNumberOfCollisions());

    }

    @Test
    public void testAlwaysTheSameKey() throws Exception {

        KeyCollisionProcessor<?> proc = KeyCollisionProcessor.createDefaultProcessor(tempDirectory);

        KeyCollisionSummary summary = proc.process(() -> 1L, 10000, 1);

        assertEquals(1L, summary.getNumberOfCollidedKeys());
        assertEquals(9999L, summary.getNumberOfCollisions());

    }

    @Test
    @Ignore("takes a while, shows that 16 bits are definitely not enough for MUHAIs")
    public void testWith16BitsKeyspace() throws Exception {
        LongPrefix prefix48 = LongPrefix.fromBinaryString(Stream.generate(() -> "0").limit(48).collect(Collectors.joining()));

        assertEquals((int) Math.pow(2, 16), prefix48.getSizeOfKeyspace().intValue());

        MuhaiGenerator generator = new MuhaiGenerator(prefix48);

        AtomicLong sequence = new AtomicLong();

        KeyCollisionProcessor<?> proc = KeyCollisionProcessor.createDefaultProcessor(tempDirectory);

        KeyCollisionSummary summary = proc.process(() -> generator.createKey((Long) sequence.incrementAndGet()), prefix48.getSizeOfKeyspace().longValue(),
                prefix48.getSizeOfKeyspace().longValue());

        LOGGER.info(formatSummary(summary));

    }

    @Test
    @Ignore("Generates the full 4_294_967_296 keys in a 32-bit keyspace, runs a couple of hours and temporarily consumes more than 50 GB of disk space")
    public void testWith32BitsKeyspace() throws Exception {

        LongPrefix prefix32 = LongPrefix.fromBinaryString(Stream.generate(() -> "0").limit(32).collect(Collectors.joining()));

        assertEquals((long) Math.pow(2, 32), prefix32.getSizeOfKeyspace().longValue());

        MuhaiGenerator generator = new MuhaiGenerator(prefix32);

        AtomicLong sequence = new AtomicLong();

        KeyCollisionProcessor<?> proc = new KeyCollisionProcessor<>(tempDirectory, 5_000_000, 25_000_000,
                KeyCollisionCollectionPolicies.TRACK_POSITIONS_AND_DISCARD_KEYS, true);

        KeyCollisionSummary summary = proc.process(() -> generator.createKey((Long) sequence.incrementAndGet()), prefix32.getSizeOfKeyspace().longValue(),
                prefix32.getSizeOfKeyspace().longValue());

        LOGGER.info(formatSummary(summary));

    }

    private String formatSummary(KeyCollisionSummary summary) throws Exception {

        ObjectMapper mapper = createObjectMapper();

        return mapper.writeValueAsString(summary);

    }

    private static ObjectMapper createObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.setPropertyNamingStrategy(new PropertyNamingStrategies.SnakeCaseStrategy());
        return objectMapper;
    }
}
