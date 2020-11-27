//@formatter:off
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
//@formatter:on
package de.calamanari.pk.uuid;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.calamanari.pk.util.TimeUtils;

/**
 * UUID Test - demonstrates the Universally Unique Identifier (UUID/GUID) pattern.<br>
 * <b>Note:</b> We do not provide an example implementation of a UUID-generator because Java (same for .NET) already provides a professional UUID-class, that
 * should be used rather than some do-it-yourself-version.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class UUIDTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(UUIDTest.class);

    /**
     * Number of example requests in this test
     */
    private static final int NUMBER_OF_RUNS = 25;

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

        assertEquals(NUMBER_OF_RUNS, results.size());

        List<Request> orderedResults = new ArrayList<>(results);
        Collections.sort(orderedResults);

        // one of the disadvantages of UUIDs is: they don't provide any "useful" natural order.
        // The order you face here follows the order of the IDs and thus differs
        // significantly from the order the requests came in.

        for (Request req : orderedResults) {
            LOGGER.info(req.toString());
        }

        // Hint: Modify the Request.compareTo()-method to include requestTimeNanos.

        String elapsedTimeString = TimeUtils.formatNanosAsSeconds(System.nanoTime() - startTimeNanos);
        LOGGER.info("Test UUID successful! Elapsed time: {} s", elapsedTimeString);

    }

}
