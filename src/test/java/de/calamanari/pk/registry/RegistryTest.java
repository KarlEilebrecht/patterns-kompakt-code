/*
 * Registry test - demonstrates the REGISTRY pattern.
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
package de.calamanari.pk.registry;

import static org.junit.Assert.assertEquals;

import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.BeforeClass;
import org.junit.Test;

import de.calamanari.pk.registry.PropertyRegistry;
import de.calamanari.pk.util.LogUtils;
import de.calamanari.pk.util.MiscUtils;

/**
 * Registry test - demonstrates the REGISTRY pattern.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class RegistryTest {

    /**
     * logger
     */
    private static final Logger LOGGER = Logger.getLogger(RegistryTest.class.getName());

    /**
     * Log-level for this test
     */
    private static final Level LOG_LEVEL = Level.INFO;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        LogUtils.setConsoleHandlerLogLevel(LOG_LEVEL);
        LogUtils.setLogLevel(LOG_LEVEL, RegistryTest.class, PropertyRegistry.class);
    }

    @Test
    public void testRegistry() {

        // Hint: set the log-level above to FINE to watch REGISTRY working.

        LOGGER.info("Test Registry ...");
        long startTimeNanos = System.nanoTime();

        PropertyRegistry registry = null;
        Exception caughtEx = null;
        try {
            registry = PropertyRegistry.getInstance();
        }
        catch (Exception ex) {
            caughtEx = ex;
        }
        // framework has not yet initialized the registry
        assertEquals("Registry not initialized.", (caughtEx == null ? "" : caughtEx.getMessage()));

        frameworkInitilizesPropertyRegistry();

        registry = PropertyRegistry.getInstance();

        String s = "Host: " + registry.getProperty("hostName");

        frameworkReInitilizesPropertyRegistry(); // <-- simulated concurrent refresh

        s = s + ", secure: " + registry.getProperty("secureTransfer");

        assertEquals("Host: localhost, secure: false", s);

        // Please have a look at the lines above
        // the framework just refreshed the registry content CONCURRENTLY
        // but since our registry-instance itself is read-only,
        // data does not become inconsistent.

        registry = PropertyRegistry.getInstance();

        s = "Host: " + registry.getProperty("hostName");

        s = s + ", secure: " + registry.getProperty("secureTransfer");

        assertEquals("Host: TREADSTONE, secure: true", s);

        LOGGER.info("Test Registry successful! Elapsed time: "
                + MiscUtils.formatNanosAsSeconds(System.nanoTime() - startTimeNanos) + " s");

    }

    /**
     * Simulates framework activity: initialize the properties
     */
    private static void frameworkInitilizesPropertyRegistry() {
        Properties props = new Properties();
        props.setProperty("hostName", "localhost");
        props.setProperty("secureTransfer", "false");
        PropertyRegistry.initialize(props);
    }

    /**
     * Simulates framework activity: re-initialize the properties
     */
    private static void frameworkReInitilizesPropertyRegistry() {
        Properties props = new Properties();
        props.setProperty("hostName", "TREADSTONE");
        props.setProperty("secureTransfer", "true");
        PropertyRegistry.initialize(props);
    }
}
