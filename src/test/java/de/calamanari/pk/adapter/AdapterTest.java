//@formatter:off
/*
 * Adapter test - demonstrates ADAPTER pattern.
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
package de.calamanari.pk.adapter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.calamanari.pk.util.MiscUtils;

/**
 * Adapter test demonstrates the ADAPTER pattern.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class AdapterTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(AdapterTest.class);

    /**
     * Instance for testing (ADAPTEE)
     */
    private SourceSystemPersonView sourceSystemPersonViewInstance;

    @Before
    public void setUp() throws Exception {
        sourceSystemPersonViewInstance = new SourceSystemPersonView(12345, "Jack", "Miller", "Special Agent, works for Secret Shopper Service");
    }

    @Test
    public void testProperties() {

        // Adjust the log-level above to FINE to see the ADAPTER working

        LOGGER.info("Test Adapter properties ...");
        long startTimeNanos = System.nanoTime();
        PersonViewAdapter personViewAdapter = new PersonViewAdapter(sourceSystemPersonViewInstance);
        String id = personViewAdapter.getId();
        String name = personViewAdapter.getName();
        String description = personViewAdapter.getDescription();
        boolean valid = personViewAdapter.isValid();
        LOGGER.debug(
                "\n=====================================================================\nID:          '{}',\nname:        '{}',\ndescription: '{}',"
                        + "\nvalid:       {}\n=====================================================================",
                id, name, description, (valid ? "YES" : "NO"));
        assertEquals("12345", id);
        assertEquals("Jack Miller", name);
        assertEquals("Special Agent, works for Secret Shopper Service", description);
        assertTrue(valid);
        String elapsedSeconds = MiscUtils.formatNanosAsSeconds(System.nanoTime() - startTimeNanos);
        LOGGER.info("Test Adapter properties successful! Elapsed time: {} s", elapsedSeconds);
    }

    @Test
    public void testOperations() {
        LOGGER.info("Test Adapter modify ...");
        long startTimeNanos = System.nanoTime();
        PersonViewAdapter personViewAdapter = new PersonViewAdapter(sourceSystemPersonViewInstance);
        assertTrue(personViewAdapter.isValid());
        boolean success = personViewAdapter.setValid(true);
        assertTrue(success);
        assertTrue(personViewAdapter.isValid());
        success = personViewAdapter.setValid(false);
        assertTrue(success);
        assertFalse(personViewAdapter.isValid());
        success = personViewAdapter.setValid(true);
        assertTrue(success);
        assertTrue(personViewAdapter.isValid());
        personViewAdapter.remove();
        assertFalse(personViewAdapter.isValid());
        success = personViewAdapter.setValid(true);
        assertFalse(success);
        assertFalse(personViewAdapter.isValid());
        String elapsedSeconds = MiscUtils.formatNanosAsSeconds(System.nanoTime() - startTimeNanos);
        LOGGER.info("Test Adapter operations successful! Elapsed time: {} s", elapsedSeconds);
    }

}
