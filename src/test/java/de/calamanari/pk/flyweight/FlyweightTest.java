//@formatter:off
/*
 * Flyweight Test - demonstrates FLYWEIGHT pattern.
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
package de.calamanari.pk.flyweight;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.calamanari.pk.util.TimeUtils;

/**
 * Flyweight Test - demonstrates FLYWEIGHT pattern.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class FlyweightTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(FlyweightTest.class);

    /**
     * number of counter flyweight carrying items, each carrying a flyweight instance
     */
    private static final int NUMBER_OF_ITEMS = 250_000;

    /**
     * workload size for counter flyweight carrying item
     */
    private static final int ITEM_WORKLOAD_SIZE = 50;

    /**
     * workload size for intrinsic state of counter flyweight
     */
    private static final int FLYWEIGHT_WORKLOAD_SIZE = 20;

    /**
     * Character set containing characters the example workloads consist of
     */
    private static final String CHARS = "abcdefghijklmnopqrstuvwxyz0123456789";

    private static volatile long memInitial = 0;

    /**
     * workload for counter flyweight carrying item
     */
    private static final String ITEM_WORK_LOAD;
    static {
        StringBuilder sb = new StringBuilder(ITEM_WORKLOAD_SIZE);
        int max = CHARS.length();
        for (int i = 0; i < ITEM_WORKLOAD_SIZE; i++) {
            sb.append(CHARS.charAt(i % max));
        }
        ITEM_WORK_LOAD = sb.toString();
    }

    /**
     * workload for the flyweight
     */
    private static final String FLYWEIGHT_WORKLOAD;
    static {
        StringBuilder sb = new StringBuilder(FLYWEIGHT_WORKLOAD_SIZE);
        int max = CHARS.length();
        for (int i = 0; i < FLYWEIGHT_WORKLOAD_SIZE; i++) {
            sb.append(CHARS.charAt(i % max));
        }
        FLYWEIGHT_WORKLOAD = sb.toString();

    }

    /**
     * array as working memory for the test
     */
    private final CounterFlyweightCarryingItem[] workingMemory = new CounterFlyweightCarryingItem[NUMBER_OF_ITEMS];

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        memInitial = getApproxMemory();
    }

    @Before
    public void setUp() throws Exception {
        for (int i = 0; i < NUMBER_OF_ITEMS; i++) {
            workingMemory[i] = null;
        }

        triggerGC();

    }

    @Test
    public void testFlyweight() {

        // HINTS:
        // * adjust the log-level in logback.xml to DEBUG to watch FLYWEIGHT details
        // * compare the (not very exact!) information about memory usage from this test and the second
        // one which only uses unshared flyweights, causing high memory consumption
        // * compare the elapsed time values
        // * play with the settings for NUMBER_OF_ITEMS and for the payloads until OutOfMemory-death

        LOGGER.info("Test Flyweight ...");
        LOGGER.info("Approx. memory consumption BEFORE: " + getApproxMemory() + " bytes");
        long startTimeNanos = System.nanoTime();
        CounterFlyweightFactory flyweightFactory = new CounterFlyweightFactory(FLYWEIGHT_WORKLOAD, false);
        for (int i = 0; i < NUMBER_OF_ITEMS; i++) {
            String workloadCopy = new String(ITEM_WORK_LOAD.toCharArray()); // huh?! -> Force a copy (don't use the
                                                                            // string reference)
            workingMemory[i] = new CounterFlyweightCarryingItem(workloadCopy, flyweightFactory.createCounterFlyweight(CHARS.charAt(i % CHARS.length())));
        }

        assertEquals("Counter-Info: 2", workingMemory[2].getCounterInfo());

        long elapsed = (System.nanoTime() - startTimeNanos);

        triggerGC();

        LOGGER.info("Approx. memory consumption AFTER: " + getApproxMemory() + " bytes");

        LOGGER.info("Test Flyweight successful! Elapsed time: " + TimeUtils.formatNanosAsSeconds(elapsed) + " s");

    }

    @Test
    public void testFlyweightUnshared() {

        // this test uses only unshared flyweights - effectively disabling all advantages of FLYWEIGHT
        // This is for comparing memory consumption.

        LOGGER.info("Test Flyweight Unshared ...");
        LOGGER.info("Approx. memory consumption BEFORE: " + getApproxMemory() + " bytes");
        long startTimeNanos = System.nanoTime();
        CounterFlyweightFactory flyweightFactory = new CounterFlyweightFactory(FLYWEIGHT_WORKLOAD, true);
        for (int i = 0; i < NUMBER_OF_ITEMS; i++) {
            String workloadCopy = new String(ITEM_WORK_LOAD.toCharArray()); // huh?! -> Force a copy (don't use the
                                                                            // string reference)
            workingMemory[i] = new CounterFlyweightCarryingItem(workloadCopy, flyweightFactory.createCounterFlyweight(CHARS.charAt(i % CHARS.length())));
        }

        assertEquals("Counter-Info: 2", workingMemory[2].getCounterInfo());

        long elapsed = (System.nanoTime() - startTimeNanos);

        triggerGC();

        LOGGER.info("Approx. memory consumption AFTER: " + getApproxMemory() + " bytes");

        LOGGER.info("Test Flyweight Unshared successful! Elapsed time: " + TimeUtils.formatNanosAsSeconds(elapsed) + " s");

    }

    /**
     * This method tries to trigger a garbage collection, this is not always reliable but seems to work in most cases.
     */
    private static void triggerGC() {
        int attempt = 0;
        while (getApproxMemory() > memInitial && attempt < 10) {
            System.gc();
            attempt++;
            TimeUtils.sleepIgnoreException(500);
        }
    }

    /**
     * Returns used memory information
     * 
     * @return memory in bytes
     */
    private static final long getApproxMemory() {
        Runtime runtime = Runtime.getRuntime();
        return runtime.totalMemory() - runtime.freeMemory();

    }
}
