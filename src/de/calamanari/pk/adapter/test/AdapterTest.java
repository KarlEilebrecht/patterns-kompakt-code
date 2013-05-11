/*
 * Adapter test - demonstrates ADAPTER pattern.
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
package de.calamanari.pk.adapter.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.calamanari.pk.adapter.PersonViewAdapter;
import de.calamanari.pk.adapter.SourceSystemPersonView;
import de.calamanari.pk.util.LogUtils;
import de.calamanari.pk.util.MiscUtils;

/**
 * Adapter test demonstrates the ADAPTER pattern.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)web.de">Karl Eilebrecht</a>
 */
public class AdapterTest {

    /**
     * logger
     */
    private static final Logger LOGGER = Logger.getLogger(AdapterTest.class.getName());

    /**
     * Log-level for this test
     */
    private static final Level LOG_LEVEL = Level.INFO;

    /**
     * Instance for testing (ADAPTEE)
     */
    private SourceSystemPersonView sourceSystemPersonViewInstance;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        LogUtils.setConsoleHandlerLogLevel(LOG_LEVEL);
        LogUtils.setLogLevel(LOG_LEVEL, AdapterTest.class, SourceSystemPersonView.class, PersonViewAdapter.class);
    }

    @Before
    public void setUp() throws Exception {
        sourceSystemPersonViewInstance = new SourceSystemPersonView(12345, "Jack", "Miller",
                "Special Agent, works for Secret Shopper Service");
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
        LOGGER.info("\n=====================================================================\nID:          '" + id
                + "',\nname:        '" + name + "',\ndescription: '" + description + "',\nvalid:       "
                + (valid ? "YES" : "NO") + "\n=====================================================================");
        assertEquals("12345", id);
        assertEquals("Jack Miller", name);
        assertEquals("Special Agent, works for Secret Shopper Service", description);
        assertTrue(valid);
        LOGGER.info("Test Adapter properties successful! Elapsed time: "
                + MiscUtils.formatNanosAsSeconds(System.nanoTime() - startTimeNanos) + " s");
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
        LOGGER.info("Test Adapter operations successful! Elapsed time: "
                + MiscUtils.formatNanosAsSeconds(System.nanoTime() - startTimeNanos) + " s");
    }

}
