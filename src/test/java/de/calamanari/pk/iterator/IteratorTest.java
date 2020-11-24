//@formatter:off
/*
 * Iterator Test - demonstrates ITERATOR pattern.
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
package de.calamanari.pk.iterator;

import static org.junit.Assert.assertEquals;

import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.calamanari.pk.util.TimeUtils;

/**
 * Iterator Test - demonstrates ITERATOR pattern.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class IteratorTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(IteratorTest.class);

    /**
     * Entry point for test worker hierarchy (the concrete AGGREGATE)
     */
    private AbstractWorker rootWorker = null;

    @Before
    public void setUp() throws Exception {

        Worker mary = new Worker("Mary");
        Worker john = new Worker("John");
        Worker larry = new Worker("Larry");
        Worker hans = new Worker("Hans");
        Worker linda = new Worker("Linda");
        Worker jake = new Worker("Jake");

        mary.addDirectSubordinate(john);
        mary.addDirectSubordinate(linda);
        john.addDirectSubordinate(larry);
        larry.addDirectSubordinate(jake);
        linda.addDirectSubordinate(hans);

        rootWorker = mary;
    }

    @Test
    public void testIterator() {
        LOGGER.info("Test Iterator ...");
        long startTimeNanos = System.nanoTime();

        StringBuilder sb = new StringBuilder();
        Iterator<? extends AbstractWorker> iterator = rootWorker.createSubordinatesIterator();
        while (iterator.hasNext()) {
            AbstractWorker worker = iterator.next();
            LOGGER.info("--> " + worker);
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(worker.getName());
        }

        String res = sb.toString();
        LOGGER.info("All subordinates of {} are: {}", rootWorker.getName(), res);
        assertEquals("Hans, Jake, John, Larry, Linda", res);

        String elapsedTimeString = TimeUtils.formatNanosAsSeconds(System.nanoTime() - startTimeNanos);
        LOGGER.info("Test Iterator successful! Elapsed time: {} s", elapsedTimeString);
    }

}
