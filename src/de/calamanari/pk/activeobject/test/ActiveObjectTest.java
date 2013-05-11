/*
 * Active Object Test - demonstrates ACTIVE OBJECT pattern.
 * Code-Beispiel zum Buch Patterns Kompakt, Verlag Springer Vieweg
 * Copyright 2013 Karl Eilebrecht
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
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
package de.calamanari.pk.activeobject.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.BeforeClass;
import org.junit.Test;

import de.calamanari.pk.activeobject.AbstractHistoryQueryEngine;
import de.calamanari.pk.activeobject.HistoryQueryComponent;
import de.calamanari.pk.activeobject.HistoryQueryScheduler;
import de.calamanari.pk.activeobject.QueryRequest;
import de.calamanari.pk.activeobject.QueryRequestFuture;
import de.calamanari.pk.util.LogUtils;
import de.calamanari.pk.util.MiscUtils;

/**
 * Active Object Test - demonstrates ACTIVE OBJECT pattern.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)web.de">Karl Eilebrecht</a>
 */
public class ActiveObjectTest {

    /**
     * logger
     */
    private static final Logger LOGGER = Logger.getLogger(ActiveObjectTest.class.getName());

    /**
     * Log-level for this test
     */
    private static final Level LOG_LEVEL = Level.INFO;

    /**
     * some test data
     */
    private static final List<String[]> MOCK_HISTORY_DATA;
    static {
        MOCK_HISTORY_DATA = new ArrayList<>();
        MOCK_HISTORY_DATA.add(new String[] { "Jack", "Miller", "1976-08-08",
                "The quick brown fox jumped over the lazy dog." });
        MOCK_HISTORY_DATA.add(new String[] { "Lisa", "Miller", "1978-10-17", "Bad bananas punished by old trees." });
        MOCK_HISTORY_DATA.add(new String[] { "Mary", "Gin-Tonic", "1978-10-17", "Blue apes on skyscrapers." });
    }

    /**
     * test duration for mock calls
     */
    private static final long CALL_DURATION = 5000;

    /**
     * number of worker threads for test
     */
    private static final int NUMBER_OF_WORKERS = 2; // Runtime.getRuntime().availableProcessors()

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        LogUtils.setConsoleHandlerLogLevel(LOG_LEVEL);
        LogUtils.setLogLevel(LOG_LEVEL, ActiveObjectTest.class, HistoryQueryEngineMock.class,
                HistoryQueryComponent.class, HistoryQueryScheduler.class, QueryRequest.class, QueryRequestFuture.class);
    }

    @Test
    public void testActiveObject() throws Exception {

        // HINTS:
        // * Adjust the log-level above to FINE to see the ACTIVE OBJECT working
        // * Play with the settings for CALL_DURATION / NUMBER_OF_WORKERS and watch execution time

        LOGGER.info("Test Active Object ...");
        long startTimeNanos = System.nanoTime();

        AbstractHistoryQueryEngine engine = new HistoryQueryEngineMock(MOCK_HISTORY_DATA, CALL_DURATION);
        HistoryQueryScheduler scheduler = new HistoryQueryScheduler(NUMBER_OF_WORKERS);

        // component configured to execute queries on the engine using the scheduler
        HistoryQueryComponent queryComponent = new HistoryQueryComponent(engine, scheduler);

        // now let's query
        QueryRequestFuture future1 = queryComponent.queryHistoryData("Jack", "Miller", "1976-08-08");
        QueryRequestFuture future2 = queryComponent.queryHistoryData(null, "Miller", "1978-10-17");
        QueryRequestFuture future3 = queryComponent.queryHistoryData(null, null, "1978-10-17");

        while (!future1.isQueryDone() || !future2.isQueryDone() || !future3.isQueryDone()) {
            Thread.sleep(2000);
        }

        List<String[]> result1 = future1.getResult();
        List<String[]> result2 = future2.getResult();
        List<String[]> result3 = future3.getResult();

        assertNotNull(result1);
        assertNotNull(result2);
        assertNotNull(result3);

        assertEquals(1, result1.size());
        assertEquals(1, result2.size());
        assertEquals(2, result3.size());

        assertEquals("The quick brown fox jumped over the lazy dog.", result1.get(0)[3]);
        assertEquals("Bad bananas punished by old trees.", result2.get(0)[3]);
        assertEquals("Blue apes on skyscrapers.", result3.get(1)[3]);

        LOGGER.info("Test Active Object successful! Elapsed time: "
                + MiscUtils.formatNanosAsSeconds(System.nanoTime() - startTimeNanos) + " s");

    }

    @Test
    public void testActiveObjectCancel() throws Exception {

        LOGGER.info("Test Active Object cancel ...");
        long startTimeNanos = System.nanoTime();

        AbstractHistoryQueryEngine engine = new HistoryQueryEngineMock(MOCK_HISTORY_DATA, 10000); // 10 seconds
        HistoryQueryScheduler scheduler = new HistoryQueryScheduler(1); // one call in parallel

        // component configured to execute queries on the engine using the scheduler
        HistoryQueryComponent queryComponent = new HistoryQueryComponent(engine, scheduler);

        // now let's query
        QueryRequestFuture future1 = queryComponent.queryHistoryData("Jack", "Miller", "1976-08-08");

        int waitCount = 0;
        while (!future1.isQueryDone()) {
            if (waitCount >= 3) {
                future1.cancelQuery();
                break;
            }
            Thread.sleep(2000);
            waitCount++;
        }

        assertTrue(future1.isQueryCancelled());
        assertNull(future1.getResult());

        LOGGER.info("Test Active Object cancel successful! Elapsed time: "
                + MiscUtils.formatNanosAsSeconds(System.nanoTime() - startTimeNanos) + " s");

    }

}
