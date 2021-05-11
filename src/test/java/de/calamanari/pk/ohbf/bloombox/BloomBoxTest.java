//@formatter:off
/*
 * BloomBoxTest
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

package de.calamanari.pk.ohbf.bloombox;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

import org.awaitility.Awaitility;
import org.junit.Ignore;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.calamanari.pk.ohbf.bloombox.demo.BloomBoxDemoController;
import de.calamanari.pk.ohbf.bloombox.demo.BloomBoxDemoView;
import de.calamanari.pk.util.SimpleFixedLengthBitVector;

/**
 * A few tests and currently the way to start the demo app
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class BloomBoxTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(BloomBoxTest.class);

    private Random rand = null;

    private int numberOfColumns = 0;

    private String OTHER = "other";
    private BloomBox bloomBox = null;

    private GenStats stats = null;

    private ValueRequest defaultDomain = null;

    private String[] columnNames = null;

    private int lineNumber = -1;

    @Ignore("Creates a box in memory (takes a couple of minutes), stores it to file and loads it again")
    @Test
    public void testSaveLoadMem() {
        this.rand = new Random(3843745);
        this.numberOfColumns = 10_000;
        int numberOfRows = 10_000;
        // @formatter:off
        this.bloomBox = BloomBox.forNumberOfRows(numberOfRows)
                                .withNumberOfColumns(this.numberOfColumns)
                                .withFalsePositiveRateEpsilon(0.0001)
                                .build();
        // @formatter:on
        this.stats = new GenStats();

        defaultDomain = vdomain(3);
        Map<Integer, ValueRequest> columnValueRequestMap = new HashMap<>();
        columnValueRequestMap.put(0, domain("A", "B", ""));
        columnValueRequestMap.put(1, req(0.1, domain("Karin", "Justus", "Benjamin"), "Hans", "Klaus"));
        columnValueRequestMap.put(3, req(0.003, domain("0", "1", ""), "Lisa"));
        columnValueRequestMap.put(4, domain("Abend", "Morgen", "Mittag", "N/A"));
        columnValueRequestMap.put(5, req(0.1, vdomain(5), "other"));
        columnValueRequestMap.put(6, domain("0", "1"));
        columnValueRequestMap.put(7, vdomain(20));
        columnValueRequestMap.put(8, domain("Q", "R", "S"));
        columnValueRequestMap.put(9, domain("Red", "Yellow", "Green", "Black", "White", "var'ious"));

        DataStoreFeeder feeder = bloomBox.getFeeder();

        for (int i = 0; i < numberOfRows; i++) {
            feeder.addRow(createRow(i, columnValueRequestMap));
            if (i % 10_000 == 0) {
                LOGGER.info("{} row(s) fed ...", (i + 1));
            }
        }

        feeder.close();
        System.out.println(stats.toString());

        File testBox = new File("/mytemp/test2.bbx");

        bloomBox.saveToFile(testBox);

        this.bloomBox = BloomBox.loadFromFile(testBox, Collections.emptyMap());

        BloomBoxQueryRunner runner = new BloomBoxQueryRunner(bloomBox);

        QueryBundle bundle = new QueryBundle();
        // @formatter:off
        bundle.getBaseQueries()
                .add(BloomBoxQuery.basicQuery("Hans and Klaus")
                        .query("col1=Hans and col2=Klaus")
                        .subQuery("col7=v1")
                        .subQuery("col5=other")
                        .subQuery("col4=Abend")
                        .subQuery("col9 in (Red, Yellow, Green)")
                        .build());
        // @formatter:on

        LOGGER.info(runner.execute(bundle).toDebugString());
        assertEquals(numberOfRows, bloomBox.getDataStore().getNumberOfRows());
        assertEquals(numberOfColumns, bloomBox.getConfig().getNumberOfInsertedElementsN());

    }

    @Ignore("Small generated data box and some queries")
    @Test
    public void testSimple() {

        this.rand = new Random(3843745);
        this.numberOfColumns = 10;
        int numberOfRows = 10_000;
        // @formatter:off
        this.bloomBox = BloomBox.forNumberOfRows(numberOfRows)
                                .withNumberOfColumns(this.numberOfColumns)
                                .withFalsePositiveRateEpsilon(0.0001)
                                .build();
        // @formatter:on
        this.stats = new GenStats();

        defaultDomain = vdomain(3);
        Map<Integer, ValueRequest> columnValueRequestMap = new HashMap<>();
        columnValueRequestMap.put(0, domain("A", "B", ""));
        columnValueRequestMap.put(1, req(0.1, domain("Karin", "Justus", "Benjamin"), "Hans", "Klaus"));
        columnValueRequestMap.put(3, req(0.003, domain("0", "1", ""), "Lisa"));
        columnValueRequestMap.put(4, domain("Abend", "Morgen", "Mittag", "N/A"));
        columnValueRequestMap.put(5, req(0.1, vdomain(5), "other"));
        columnValueRequestMap.put(6, domain("0", "1"));
        columnValueRequestMap.put(7, vdomain(20));
        columnValueRequestMap.put(8, domain("Q", "R", "S"));
        columnValueRequestMap.put(9, domain("Red", "Yellow", "Green", "Black", "White", "var'ious"));

        DataStoreFeeder feeder = bloomBox.getFeeder();

        for (int i = 0; i < numberOfRows; i++) {
            feeder.addRow(createRow(i, columnValueRequestMap));
            if (i % 10_000 == 0) {
                LOGGER.info("{} row(s) fed ...", (i + 1));
            }
        }

        feeder.close();
        System.out.println(stats.toString());

        BloomBoxQueryRunner runner = new BloomBoxQueryRunner(bloomBox);

        QueryBundle bundle = new QueryBundle();
        // @formatter:off
        bundle.getBaseQueries()
                .add(BloomBoxQuery.basicQuery("Hans and Klaus")
                        .query("col1=Hans and col2=Klaus")
                        .subQuery("col7=v1")
                        .subQuery("col5=other")
                        .subQuery("col4=Abend")
                        .subQuery("col9 in (Red, Yellow, Green)")
                        .build());
        bundle.getBaseQueries()
                .add(BloomBoxQuery.basicQuery("Hans or Klaus")
                        .query("col1=Hans and col2=Klaus")
                        .subQuery("col7=v1")
                        .subQuery("col5=other")
                        .subQuery("col4=Abend")
                        .subQuery("col9 in (Red, Yellow, Green)")
                        .build());
        bundle.getBaseQueries()
                .add(BloomBoxQuery.basicQuery("ABEND")
                        .query("col4=Abend")
                        .subQuery("col7=v1")
                        .subQuery("col5=other")
                        .subQuery("col6=0")
                        .subQuery("col9 in (Red, Yellow, Green)")
                        .build());
        bundle.getBaseQueries()
                .add(BloomBoxQuery.basicQuery("MORGEN")
                        .query("col4=Morgen")
                        .subQuery("col6=0")
                        .build());
        bundle.getBaseQueries()
                .add(BloomBoxQuery.basicQuery("Various")
                        .query("col9='var\\'ious'")
                        .subQuery("Lisa", "col3=Lisa")
                        .build());

        bundle.getPostQueries()
                .add(BloomBoxQuery.postQuery("P1")
                        .query("${ABEND}")
                        .subQuery("col6=0")
                        .subQuery("col6!=0")
                        .build());
        bundle.getPostQueries()
                .add(BloomBoxQuery.postQuery("P2")
                        .query("${P1} union ${MORGEN}")
                        .subQuery("col6=0")
                        .build());
        bundle.getPostQueries()
                .add(BloomBoxQuery.postQuery("P3")
                        .query("${ABEND} intersect ${MORGEN}")
                        .build());
        bundle.getPostQueries()
                .add(BloomBoxQuery.postQuery("P4")
                        .query("${ABEND} minus ${MORGEN}")
                        .build());
        bundle.getPostQueries().add(BloomBoxQuery.postQuery("P5")
                        .query("${ABEND} minus ${P1.sq_0}")
                        .build());
        bundle.getPostQueries().add(BloomBoxQuery.postQuery("P6")
                        .query("${ABEND} minus ${P1.sq_1}")
                        .subQuery("col8=Q")
                        .build());
        // @formatter:on

        UpScalingConfig usc = new UpScalingConfig();
        usc.setBaseScalingFactor(1.0);
        usc.setTargetPopulationSize(numberOfRows);

        LOGGER.info(runner.execute(bundle).toDebugString());
        assertEquals(numberOfRows, bloomBox.getDataStore().getNumberOfRows());
        assertEquals(numberOfColumns, bloomBox.getConfig().getNumberOfInsertedElementsN());

    }

    @Ignore("Small generated data box and some queries and upscaling")
    @Test
    public void testScale() {

        this.rand = new Random(3843745);
        this.numberOfColumns = 10;
        int numberOfRows = 10_000;
        // @formatter:off
        this.bloomBox = BloomBox.forNumberOfRows(numberOfRows)
                                .withNumberOfColumns(this.numberOfColumns)
                                .withFalsePositiveRateEpsilon(0.00001)
                                .build();
        // @formatter:on
        this.stats = new GenStats();

        defaultDomain = vdomain(3);
        Map<Integer, ValueRequest> columnValueRequestMap = new HashMap<>();
        columnValueRequestMap.put(0, domain("A", "B", ""));
        columnValueRequestMap.put(1, req(0.1, domain("Karin", "Justus", "Benjamin"), "Hans", "Klaus"));
        columnValueRequestMap.put(3, req(0.003, domain("0", "1", ""), "Lisa"));
        columnValueRequestMap.put(4, domain("Abend", "Morgen", "Mittag", "N/A"));
        columnValueRequestMap.put(5, req(0.1, vdomain(5), "other"));
        columnValueRequestMap.put(6, domain("0", "1"));
        columnValueRequestMap.put(7, vdomain(20));
        columnValueRequestMap.put(8, domain("Q", "R", "S"));
        columnValueRequestMap.put(9, domain("Red", "Yellow", "Green", "Black", "White", "var'ious"));

        DataStoreFeeder feeder = bloomBox.getFeeder();

        for (int i = 0; i < numberOfRows; i++) {
            feeder.addRow(createRow(i, columnValueRequestMap));
            if (i % 10_000 == 0) {
                LOGGER.info("{} row(s) fed ...", (i + 1));
            }
        }

        feeder.close();
        System.out.println(stats.toString());

        BloomBoxQueryRunner runner = new BloomBoxQueryRunner(bloomBox);

        QueryBundle bundle = new QueryBundle();
        // @formatter:off
        bundle.getBaseQueries()
                .add(BloomBoxQuery.basicQuery("Hans und Klaus")
                          .query("(col1=Hans or col2=Klaus) and (col4 not in (Abend, Mittag, Morgen))")
                          .subQuery("col6=0")
                        .build());

        bundle.getBaseQueries()
                .add(BloomBoxQuery.basicQuery("HasCol60")
                  .query("col6=0")
                .build());


        
        bundle.getPostQueries().add(BloomBoxQuery.postQuery("P1")
                .query("${'Hans und Klaus'}")
                .subQuery("col6=0")
                .subQuery("col6!=0")
                .build());

        
        // @formatter:on

        UpScalingConfig usc = new UpScalingConfig();
        usc.setBaseScalingFactor(1.2);
        AttributeScalingConfig acnf = new AttributeScalingConfig();
        acnf.setScalingFactor(1.2);
        usc.getAttributeScalingFactors().put("col4", acnf);

        acnf = new AttributeScalingConfig();
        acnf.setScalingFactor(2);
        usc.getAttributeScalingFactors().put("col6", acnf);

        usc.setTargetPopulationSize(numberOfRows * 2);

        bundle.setUpScalingConfig(usc);

        LOGGER.info("\n" + runner.execute(bundle));

        assertEquals(numberOfRows, bloomBox.getDataStore().getNumberOfRows());
        assertEquals(numberOfColumns, bloomBox.getConfig().getNumberOfInsertedElementsN());

    }

    /**
     * Helper converting a single line from file to an argument map, the keys are the header column names
     * 
     * @param line current line
     * @return argument map
     */
    private Map<String, String> lineToArgMap(String line) {
        lineNumber++;
        Map<String, String> res = new LinkedHashMap<>();
        String[] parts = line.split(";", -1);
        if (lineNumber == 0) {
            this.columnNames = parts;
        }
        else {
            for (int i = 0; i < columnNames.length; i++) {
                res.put(columnNames[i], parts[i]);
            }
        }
        return res;
    }

    /**
     * When creating the enriched box, this method attaches the additional columns
     * 
     * @param lineMap map converted from source
     * @param randomTable pre-initialized random table to guarantee some parameters
     * @return enriched argument map to be fed into the box
     */
    private Map<String, String> enrichMap(Map<String, String> lineMap, DistributedRandomTable randomTable) {
        int rowIdx = lineNumber - 1;
        for (int i = 0; i < randomTable.numberOfColumns; i++) {
            lineMap.put("occ_" + i, randomTable.getValue(rowIdx, i));
        }
        return lineMap;
    }

    @Ignore("Short running creation of bird strikes bloom box, only needed on demand")
    @Test
    public void testCreateBirdStrikesBloomBox() throws Exception {
        this.numberOfColumns = 17;
        int numberOfRows = 99_404;
        // @formatter:off
        this.bloomBox = BloomBox.forNumberOfRows(numberOfRows)
                                .withNumberOfColumns(this.numberOfColumns)
                                .withFalsePositiveRateEpsilon(0.00001)
                                .build();
        // @formatter:on

        DataStoreFeeder feeder = bloomBox.getFeeder();

        columnNames = null;
        lineNumber = -1;

        Files.lines(new File("/mytemp/birdstrikes.csv").toPath()).filter(Predicate.not(String::isBlank)).map(this::lineToArgMap)
                .filter(Predicate.not(Map::isEmpty)).forEach(argMap -> feeder.addRow(argMap));

        assertEquals(numberOfRows, lineNumber);

        LOGGER.info("Feeding complete: " + lineNumber + " entries processed.");

        feeder.close();

        File testBox = new File("/mytemp/birdstrikes.bbx");

        bloomBox.setDescription("Bird Strikes BloomBox, based on free data provided by Wisdom Axis\n"
                + "See https://www.wisdomaxis.com/technology/software/data/for-reports/bird-strikes-data-for-reports.php");

        bloomBox.saveToFile(testBox);

        assertEquals(numberOfRows, bloomBox.getDataStore().getNumberOfRows());
        assertEquals(numberOfColumns, bloomBox.getConfig().getNumberOfInsertedElementsN());

    }

    @Ignore("Long running creation of 2.9 GB box (enriched)")
    @Test
    public void testCreateEnrichedBirdStrikesBloomBox() throws Exception {
        this.numberOfColumns = 10017;
        int numberOfRows = 99_404;
        // @formatter:off
        this.bloomBox = BloomBox.forNumberOfRows(numberOfRows)
                                .withNumberOfColumns(this.numberOfColumns)
                                .withFalsePositiveRateEpsilon(0.00001)
                                .build();
        // @formatter:on

        Random rand = new Random(numberOfRows);
        DistributedRandomTable randomTable = new DistributedRandomTable(rand, numberOfRows, 10_000);
        LOGGER.debug("Random table complete");

        DataStoreFeeder feeder = bloomBox.getFeeder();

        columnNames = null;
        lineNumber = -1;

        Files.lines(new File("/mytemp/birdstrikes.csv").toPath()).filter(Predicate.not(String::isBlank)).map(this::lineToArgMap)
                .filter(Predicate.not(Map::isEmpty)).map(m -> this.enrichMap(m, randomTable)).forEach(argMap -> feeder.addRow(argMap));

        assertEquals(numberOfRows, lineNumber);

        LOGGER.info("Feeding complete: " + lineNumber + " entries processed.");

        feeder.close();

        File testBox = new File("/mytemp/birdstrikes_10Ka.bbx");

        bloomBox.setDescription("Bird Strikes BloomBox, based on free data provided by Wisdom Axis\n"
                + "See https://www.wisdomaxis.com/technology/software/data/for-reports/bird-strikes-data-for-reports.php\n"
                + "Enhanced with 10000 binary colums ('occ_0' - 'occ_9999'), the number indicates the number of rows where the\n"
                + "value is set to 1, otherwise 0, using a random distribution across the rows.");

        bloomBox.saveToFile(testBox);

        assertEquals(numberOfRows, bloomBox.getDataStore().getNumberOfRows());
        assertEquals(numberOfColumns, bloomBox.getConfig().getNumberOfInsertedElementsN());

    }

    @Ignore("Use this method to start the demo UI application")
    @Test
    public void testBloomBoxDemoUI() {

        this.bloomBox = BloomBox.loadFromFile(new File("/mytemp/birdstrikes_10K.bbx"), Collections.emptyMap());

        BloomBoxQueryRunner runner = new BloomBoxQueryRunner(bloomBox);

        final BloomBoxDemoView view = new BloomBoxDemoView();

        view.setVisible(true);

        new BloomBoxDemoController(view, bloomBox, runner);

        Awaitility.await().atMost(30, TimeUnit.DAYS).until(view::isDisposed);

    }

    /**
     * Creates a single generated row following the given instructions
     * 
     * @param rowNumber current row number
     * @param columnValueRequestMap requested settings and probabilities
     * @return row to be fed into a box
     */
    private Map<String, ?> createRow(int rowNumber, Map<Integer, ValueRequest> columnValueRequestMap) {

        Map<String, String> res = new HashMap<String, String>();

        for (int i = 0; i < numberOfColumns; i++) {

            ValueRequest req = columnValueRequestMap.get(i);

            if (req == null) {
                req = defaultDomain;
            }

            if (req != null && rand.nextDouble() < req.weight) {

                for (int j = 0; j < req.values.length; j++) {
                    res.put("col" + (i + j), req.values[j]);
                    countColumnValue((i + j), req.values[j]);
                    if (j > 0) {
                        i++;
                    }
                }
            }
            else {
                String otherValue = OTHER;
                if (req != null && req.domainForOtherValues != null && req.domainForOtherValues.length > 0) {
                    if (req.domainForOtherValues.length == 1) {
                        otherValue = req.domainForOtherValues[0];
                    }
                    else {
                        otherValue = req.domainForOtherValues[rand.nextInt(req.domainForOtherValues.length)];
                    }
                }
                res.put("col" + i, otherValue);
                countColumnValue(i, otherValue);
            }

        }
        stats.numberOfRows++;

        return res;
    }

    /**
     * Updates the statistics when generating test data
     * 
     * @param column index
     * @param value generated value
     */
    private void countColumnValue(int column, String value) {
        Map<String, Integer> valueStats = stats.columnValueStats.computeIfAbsent("col" + column, key -> new HashMap<>());

        Integer currentCount = valueStats.get(value);

        if (currentCount == null) {
            valueStats.put(value, 1);
        }
        else {
            valueStats.put(value, currentCount + 1);
        }
    }

    /**
     * Holds the statistics of generated values when creating a synthetic box
     */
    private class GenStats {

        int numberOfRows = 0;

        Map<String, Map<String, Integer>> columnValueStats = new TreeMap<>();

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append(this.getClass().getSimpleName());
            sb.append("(numberOfRows=");
            sb.append(numberOfRows);
            sb.append("\n");
            sb.append(columnValueStats);
            sb.append("\n)");

            return sb.toString();
        }

    }

    /**
     * @param weight for weighted random
     * @param domainForOtherValuesRequest values to use randomly to set another value (negative case)
     * @param values values for the positive case
     * @return request
     */
    private static ValueRequest req(double weight, ValueRequest domainForOtherValuesRequest, String... values) {

        ValueRequest res = new ValueRequest();
        res.weight = weight;
        res.values = values;
        res.domainForOtherValues = domainForOtherValuesRequest.domainForOtherValues;
        return res;
    }

    private static class ValueRequest {

        String[] values = null;
        double weight = -1;

        String[] domainForOtherValues = null;

    }

    /**
     * Creates a simple domain from the given values
     * 
     * @param values members
     * @return domain request
     */
    private static ValueRequest domain(String... values) {

        ValueRequest res = new ValueRequest();
        res.domainForOtherValues = values;
        return res;
    }

    /**
     * Creates a v-domain, which is a domain of the given number of elements and each item is a 'v' followed by the index number, <code>v0, v1, v2, ...</code>
     * 
     * @param numberOfValues size of the domain
     * @return request
     */
    private static ValueRequest vdomain(int numberOfValues) {
        String[] values = new String[numberOfValues];
        for (int i = 0; i < numberOfValues; i++) {
            values[i] = "v" + i;
        }
        return domain(values);
    }

    /**
     * A distributed random table allows to set binary attributes randomly (0,1). But instead of weighted random, which won't tell upfront how many elements
     * exactly will be set, we fix the numbers of rows per column to be set to 1 followed by a shuffle of the column, so the distribution within the column will
     * be random again.
     *
     */
    private static class DistributedRandomTable {

        private final int numberOfColumns;

        private final int numberOfRows;

        /**
         * holds the binary settings for all attributes (rows x columns)
         */
        private final SimpleFixedLengthBitVector bitVector;

        /**
         * sets the columnPositive count for each column to its index (0, 1, .., numberOfColumns-1)
         * 
         * @param numberOfColumns width of a row
         * @return input params to create table
         */
        private static int[] createAscendingPostiveCounts(int numberOfColumns) {
            int[] columnPositiveCounts = new int[numberOfColumns];
            for (int i = 0; i < numberOfColumns; i++) {
                columnPositiveCounts[i] = i;
            }
            return columnPositiveCounts;
        }

        /**
         * @param rand a pre-initialized random generator to create reproducible results
         * @param numberOfRows row count
         * @param columnPositives for each column the number of rows where the corresponding attribute should be 1
         */
        DistributedRandomTable(Random rand, int numberOfRows, int[] columnPositives) {
            this.numberOfColumns = columnPositives.length;
            this.numberOfRows = numberOfRows;
            SimpleFixedLengthBitVector vector = new SimpleFixedLengthBitVector(((long) numberOfRows) * numberOfColumns);

            List<Byte> columnBuffer = new ArrayList<>(numberOfRows);
            for (int i = 0; i < numberOfColumns; i++) {
                addColumn(rand, vector, i, columnPositives[i], columnBuffer);
            }
            this.bitVector = new SimpleFixedLengthBitVector(vector.toLongArray());

            for (int i = 0; i < columnPositives.length; i++) {
                int expected = columnPositives[i];
                int found = 0;
                for (int j = 0; j < numberOfRows; j++) {
                    long bitIdx = (((long) j) * numberOfColumns) + i;
                    if (vector.isBitSet(bitIdx)) {
                        found++;
                    }
                }
                assertEquals(expected, found);
            }
        }

        /**
         * @param rand a pre-initialized random generator to create reproducible results
         * @param numberOfRows row count
         * @param numberOfColumns to derive the columnPositive counts from (ascending)
         */
        DistributedRandomTable(Random rand, int numberOfRows, int numberOfColumns) {
            this(rand, numberOfRows, createAscendingPostiveCounts(numberOfColumns));
        }

        /**
         * Adds a column to the matrix
         * 
         * @param rand a pre-initialized random generator to create reproducible results
         * @param vector rows x columns all values
         * @param columnIdx column position
         * @param columnPositives how many to set to 1
         * @param columnBuffer for performance reasons the buffer will be reused
         */
        private void addColumn(Random rand, SimpleFixedLengthBitVector vector, int columnIdx, int columnPositives, List<Byte> columnBuffer) {

            columnBuffer.clear();
            for (int i = 0; i < numberOfRows; i++) {
                if (i < columnPositives) {
                    columnBuffer.add((byte) 1);
                }
                else {
                    columnBuffer.add((byte) 0);
                }
            }

            Collections.shuffle(columnBuffer, rand);

            for (int i = 0; i < numberOfRows; i++) {
                if (columnBuffer.get(i) == 1) {
                    long bitIdx = (((long) i) * numberOfColumns) + columnIdx;
                    vector.setBit(bitIdx);
                }
            }
        }

        String getValue(int rowIdx, int colIdx) {
            if (bitVector.isBitSet((((long) rowIdx) * numberOfColumns) + colIdx)) {
                return "1";
            }
            else {
                return "0";
            }
        }

    }

}
