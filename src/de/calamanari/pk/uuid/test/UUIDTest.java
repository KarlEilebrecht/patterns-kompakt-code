/*
 * UUID Test - demonstrates the Universally Unique Identifier (UUID/GUID) pattern.
 * Code-Beispiel zum Buch Patterns Kompakt, Verlag Springer Vieweg
 * Copyright 2014 Karl Eilebrecht
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
package de.calamanari.pk.uuid.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.BeforeClass;
import org.junit.Test;

import de.calamanari.pk.util.LogUtils;
import de.calamanari.pk.util.MiscUtils;
import de.calamanari.pk.uuid.Request;

/**
 * UUID Test - demonstrates the Universally Unique Identifier (UUID/GUID) pattern.<br>
 * <b>Note:</b> We do not provide an example implementation of a UUID-generator because Java (same for .NET) already
 * provides a professional UUID-class, that should be used rather than some do-it-yourself-version.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class UUIDTest {

    /**
     * logger
     */
    private static final Logger LOGGER = Logger.getLogger(UUIDTest.class.getName());

    /**
     * Log-level for this test
     */
    private static final Level LOG_LEVEL = Level.INFO;

    /**
     * Number of example requests in this test
     */
    private static final int NUMBER_OF_RUNS = 25;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        LogUtils.setConsoleHandlerLogLevel(LOG_LEVEL);
        LogUtils.setLogLevel(LOG_LEVEL, UUIDTest.class, Request.class);
    }

    @Test
    public void testUUID() throws Exception {

        LOGGER.info("Test UUID ...");
        long startTimeNanos = System.nanoTime();

        Set<Request> results = new HashSet<>();

        for (int i = 0; i < NUMBER_OF_RUNS; i++) {
            Request req = new Request(UUID.randomUUID().toString(), System.nanoTime(), "Request-" + i);

            // You can see here that the created UUIDs (even direct neighbors) show few similarities

            LOGGER.info(req.toString());

            results.add(req);
        }

        List<Request> orderedResults = new ArrayList<>(results);
        Collections.sort(orderedResults);

        // one of the disadvantages of UUIDs is: they don't provide any "useful" natural order.
        // The order you face here follows the order of the IDs and thus differs
        // significantly from the order the requests came in.

        for (Request req : orderedResults) {
            LOGGER.info(req.toString());
        }

        // Hint: Modify the Request.compareTo()-method to include requestTimeNanos.

        LOGGER.info("Test UUID successful! Elapsed time: "
                + MiscUtils.formatNanosAsSeconds(System.nanoTime() - startTimeNanos) + " s");

    }

}
