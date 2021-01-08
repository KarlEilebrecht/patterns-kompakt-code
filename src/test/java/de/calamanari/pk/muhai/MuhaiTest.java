//@formatter:off
/*
 * MUHAI Test - demonstrates the Mostly Unique Hashed Attributes Identifier (MUHAI) pattern.
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

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;

import de.calamanari.pk.muhai.collider.KeyCollisionCollectionPolicies;
import de.calamanari.pk.muhai.collider.KeyCollisionProcessor;
import de.calamanari.pk.muhai.collider.KeyCollisionSummary;
import de.calamanari.pk.muhai.collider.KeyCollisionTest;

/**
 * MUHAI Test: demonstrates the creation of Mostly Unique Hashed Attributes Identifiers (MUHAI)
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class MuhaiTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(MuhaiTest.class);

    private static final String TEST_RECORDS = """
            GNA;Area Code;Tenant;Level;PXD;Provider;FColor;FIncident;opcode;fabric;sum
            H;237;Arimba;4;XXX65711;Colibra;6;LKRT;600;17;928
            H;237;Becola;4;T0893;Abtelo;1928;JOCP;203;17;75
            H;237;Arimba;2;XXX65711;Colibra;6;LKRT;209;17;80
            P;237;Arimba;4;OSMO20;Polic;6;LKRT;828;17;95
            Q;237;Arimba;4;XXX65711;Colibra;6;LKRT;908;17;12
            H;237;Zonk;4;XXX65711;Colibra;6;LKRT;858;17;13
            H;237;Arimba;4;XXX4213;Colibra;6;OMOP;811;17;555
            F;237;Arimba;4;XXX65711;Colibra;6;LKRT;803;17;80
            H;238;Arimba;4;XXX65711;Colibra;6;LKRT;501;17;90
            H;238;Becola;7;T0893;Abtelo;9;SSK3;901;17;11
            M;238;Becola;7;T0893;Abtelo;9;SSK3;702;11;100
            H;238;Arimba;9;XXX65711;Colibra;6;LKRT;802;17;9283
            H;238;Arimba;4;XXX65711;Colibra;6;LKRT;801;199;80
            """;

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
    public void testMuhaiCreation() {

        // HINTS:
        // * Adjust the log-level in logback.xml to DEBUG to see more details
        // * In the package de.calamanari.pk.muhai you can find the class KeyCollisionTest
        // There you can experiment with large amounts of MUHAIs and possible collisions

        List<Record> records = Stream.of(TEST_RECORDS.split("[\\r?\\n]+")).skip(1).map(RecordField::parse).collect(Collectors.toList());

        assertEquals(13, records.size());

        // Here we have some records which have some fields but not a single unique identifier

        // We know that the tuple (Area, tenant, level, PXD, Provider) identifies the item, it is stable over time
        // However, a composite key is way to big and clumsy

        // Thus we will create MUHAIs for these records

        // The default uses 62 bits of possible 64-bit keyspace
        // This has two advantages:
        // * We avoid negative signed longs
        // * We reserve 3 disjoint "backup keyspaces" of the same size (e.g. for re-keying in future, you never know ...)
        LongPrefix prefix = LongPrefix.DEFAULT;

        MuhaiGenerator generator = new MuhaiGenerator(prefix);

        records.forEach(record -> record.dwhId = generator.createKey(record.areaCode, record.tenant, record.level, record.pxdCode, record.provider));

        Map<Long, List<Record>> groupedByDwhId = records.stream().collect(Collectors.groupingBy(Record::getDwhId));

        logGroupingByDwhIdResult(groupedByDwhId);

        Map<List<Object>, List<Record>> groupedByAreaAndTenantAndLevelAndPxdAndProvider = groupRecordsBySourceFields(records);

        Set<List<Record>> expected = new HashSet<>(groupedByAreaAndTenantAndLevelAndPxdAndProvider.values());
        Set<List<Record>> dwhIdGrouped = new HashSet<>(groupedByDwhId.values());

        logGroupingBySomeFieldsResult(groupedByAreaAndTenantAndLevelAndPxdAndProvider);

        assertEquals(expected, dwhIdGrouped);

    }

    private Map<List<Object>, List<Record>> groupRecordsBySourceFields(List<Record> records) {
        Map<List<Object>, List<Record>> groupedByAreaAndTenantAndLevelAndPxdAndProvider = records.stream()
                .collect(Collectors.groupingBy(record -> Arrays.asList(record.areaCode, record.tenant, record.level, record.pxdCode, record.provider)));
        return groupedByAreaAndTenantAndLevelAndPxdAndProvider;
    }

    @Test
    public void testRecordGenerator() {

        int numberOfRecords = 10_000;
        Map<Long, List<Record>> groupedRecords = Stream.generate(new RecordGenerator(LongPrefix.DEFAULT)).limit(numberOfRecords)
                .collect(Collectors.groupingBy(Record::getDwhId));

        int numberOfGroups = groupedRecords.size();

        // nothing to group, each record has his own dwhKey
        assertEquals(numberOfRecords, numberOfGroups);

    }

    @Test
    @Ignore("""
            - Generates 1_000_000 keys in a 32-bit keyspace
            - Disabled, because it takes a while
            """)
    public void testCollisionsInSmallKeySpace() throws Exception {

        // No surprise, we will see a couple of collisions, even rather early

        LongPrefix prefix32 = LongPrefix.fromBinaryString(Stream.generate(() -> "0").limit(32).collect(Collectors.joining()));

        KeyCollisionProcessor<?> proc = new KeyCollisionProcessor<>(tempDirectory, 1_000_000, 1_000_000,
                KeyCollisionCollectionPolicies.TRACK_POSITIONS_AND_DISCARD_KEYS, false);

        RecordGenerator generator = new RecordGenerator(prefix32);

        KeyCollisionSummary summary = proc.process(() -> generator.get().dwhId, 1_000_000, prefix32.getSizeOfKeyspace().longValue());

        assertEquals(124, summary.getNumberOfCollisions());

        LOGGER.debug(formatSummary(summary));

    }

    @Test
    @Ignore("""
            - Generates 1_000_000 keys in a 32-bit keyspace
            - Disabled, because it takes a while
            """)
    public void testCollisionsInLongKeySpace() throws Exception {

        // No collision expected

        LongPrefix prefix62 = LongPrefix.DEFAULT;

        KeyCollisionProcessor<?> proc = new KeyCollisionProcessor<>(tempDirectory, 1_000_000, 1_000_000,
                KeyCollisionCollectionPolicies.TRACK_POSITIONS_AND_DISCARD_KEYS, false);

        RecordGenerator generator = new RecordGenerator(prefix62);

        KeyCollisionSummary summary = proc.process(() -> generator.get().dwhId, 1_000_000, prefix62.getSizeOfKeyspace().longValue());

        assertEquals(0, summary.getNumberOfCollisions());

        LOGGER.info(formatSummary(summary));

    }

    private void logGroupingByDwhIdResult(Map<Long, List<Record>> groupedByDwhId) {
        StringBuilder sb = new StringBuilder();
        groupedByDwhId.forEach((dwhId, groupedRecords) -> {
            sb.append("\nDWH-ID: ").append(dwhId).append("\n").append("-------------------------------\n");
            groupedRecords.forEach(record -> {
                sb.append(record).append("\n");
            });
        });

        LOGGER.debug(sb.toString());
    }

    private void logGroupingBySomeFieldsResult(Map<?, List<Record>> groupedByDwhId) {
        StringBuilder sb = new StringBuilder();
        groupedByDwhId.forEach((dwhId, groupedRecords) -> {
            sb.append("\nTuple: ").append(dwhId).append("\n").append("-------------------------------\n");
            groupedRecords.forEach(record -> {
                sb.append(record).append("\n");
            });
        });

        LOGGER.debug(sb.toString());
    }

    public static String formatSummary(KeyCollisionSummary summary) throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.setPropertyNamingStrategy(new PropertyNamingStrategies.SnakeCaseStrategy());
        return objectMapper.writeValueAsString(summary);

    }

    static class Record {

        private long dwhId = 0;

        private String gna;

        private int areaCode;

        private String tenant;

        private int level;

        private String pxdCode;

        private String provider;

        private int colorCode;

        private String incidentCode;

        private int opcode;

        private long sum;

        public long getDwhId() {
            return dwhId;
        }

        @Override
        public String toString() {
            return "Record [dwhId=" + dwhId + ", gna=" + gna + ", areaCode=" + areaCode + ", tenant=" + tenant + ", level=" + level + ", pxdCode=" + pxdCode
                    + ", provider=" + provider + ", colorCode=" + colorCode + ", incidentCode=" + incidentCode + ", opcode=" + opcode + ", sum=" + sum + "]";
        }

    }

    /**
     * An enum that precisely reflects the file header for parsing with few lines of code
     */
    enum RecordField {

        // @formatter:off
        GNA((sVal, record) -> {
            record.gna = sVal;
        }),
        
        AREA_CODE((sVal, record) -> {
            record.areaCode = Integer.parseInt(sVal);
        }),
        
        TENANT((sVal, record) -> {
            record.tenant = sVal;
        }),
        
        LEVEL((sVal, record) -> {
            record.level = Integer.parseInt(sVal);
        }),
        
        PXD((sVal, record) -> {
            record.pxdCode = sVal;
        }),
        
        PROVIDER((sVal, record) -> {
            record.provider = sVal;
        }),
        
        FCOLOR((sVal, record) -> {
            record.colorCode= Integer.parseInt(sVal);
        }),
        
        FINCIDENT((sVal, record) -> {
            record.incidentCode = sVal;
        }),
        
        OPCODE((sVal, record) -> {
            record.opcode = Integer.parseInt(sVal);
        }),
        
        FABRIC((sVal, record) -> {
            // unused, ignore
        }),
        
        SUM((sVal, record) -> {
            record.sum = Long.parseLong(sVal);
        });
        // @formatter:on

        final BiConsumer<String, Record> fieldUpdater;

        public void update(Record record, String[] fields) {
            fieldUpdater.accept(fields[this.ordinal()], record);
        }

        RecordField(BiConsumer<String, Record> fieldUpdater) {
            this.fieldUpdater = fieldUpdater;
        }

        /**
         * Creates a new Record from the given fields in expected order
         * @param row (from file)
         * @return new record
         */
        static Record parse(String row) {
            Record res = new Record();
            for (RecordField field : RecordField.values()) {
                field.update(res, row.split(";"));
            }
            return res;
        }

    }

    /**
     * Supplier for test records, no duplicates
     */
    private class RecordGenerator implements Supplier<Record> {

        // record.areaCode, record.tenant, record.level, record.pxdCode, record.provider)

        int[] counters = new int[] { 34283, 3009283, 199283, 12736, 500 };

        Random rand = new Random(1836338);

        final LongPrefix prefix;

        final MuhaiGenerator keyGen;

        RecordGenerator(LongPrefix keyPrefix) {
            this.prefix = keyPrefix;
            this.keyGen = new MuhaiGenerator(prefix);
        }

        private int[] nextIncrement() {
            for (int i = 0; i < counters.length; i++) {
                int val = (i < counters.length - 1) ? (counters[i] + 1 % 256) : counters[i] + 1;
                counters[i] = val;
                if (val != 0) {
                    break;
                }
            }
            return Arrays.copyOf(counters, counters.length);
        }

        @Override
        public Record get() {
            int[] base = nextIncrement();
            Record res = new Record();
            res.areaCode = base[0];
            res.tenant = Integer.toHexString(base[1]);
            res.level = base[2];
            res.pxdCode = Integer.toHexString(base[3]);
            res.provider = Integer.toHexString(base[4]);
            res.colorCode = rand.nextInt(256);
            res.gna = Integer.toHexString(rand.nextInt(256));
            res.incidentCode = Integer.toHexString(rand.nextInt(8000));
            res.opcode = rand.nextInt(16);
            res.sum = rand.nextInt(15_000_000);
            res.dwhId = keyGen.createKey(res.areaCode, res.tenant, res.level, res.pxdCode, res.provider);
            return res;
        }

    }
}
