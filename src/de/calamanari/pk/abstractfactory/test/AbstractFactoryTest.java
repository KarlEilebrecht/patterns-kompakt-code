/*
 * Abstract factory test case - demonstrates ABSTRACT FACTORY.
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
package de.calamanari.pk.abstractfactory.test;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import de.calamanari.pk.abstractfactory.AbstractDataManager;
import de.calamanari.pk.abstractfactory.AbstractDataReader;
import de.calamanari.pk.abstractfactory.AbstractDataWriter;
import de.calamanari.pk.abstractfactory.PlainFileDataManager;
import de.calamanari.pk.abstractfactory.PlainFileDataReader;
import de.calamanari.pk.abstractfactory.PlainFileDataWriter;
import de.calamanari.pk.abstractfactory.SecureFileDataManager;
import de.calamanari.pk.abstractfactory.SecureFileDataReader;
import de.calamanari.pk.abstractfactory.SecureFileDataWriter;
import de.calamanari.pk.util.LogUtils;
import de.calamanari.pk.util.MiscUtils;

/**
 * Test case for ABSTRACT FACTORY
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class AbstractFactoryTest {

    /**
     * logger
     */
    private static final Logger LOGGER = Logger.getLogger(AbstractFactoryTest.class.getName());

    /**
     * Log-level for this test
     */
    private static final Level LOG_LEVEL = Level.INFO;

    /**
     * for the testcases we simulate some kind of registry.
     */
    private static final HashMap<String, AbstractDataManager> SYSTEM_REGISTRY = new HashMap<>();

    /**
     * name of configuration 1
     */
    private static final String CONFIG_KEY1 = "config1";

    /**
     * name of configuration 2
     */
    private static final String CONFIG_KEY2 = "config2";

    /**
     * Test text sample
     */
    private static final String FUNNY_TEXT = "Three little birds\n(Anton, Jenny and Laura)\neat "
            + "five big pigs\nwith potato wedges.";

    /**
     * name of the file to be created
     */
    private static final String FUNNY_TEXT_FILE_NAME = "Funny Text";

    /**
     * By default the files will be deleted after test, set this to true if you want to look into
     */
    private static final boolean KEEP_FILES_AFTER_TEST = false;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        LogUtils.setConsoleHandlerLogLevel(LOG_LEVEL);
        LogUtils.setLogLevel(LOG_LEVEL, AbstractFactoryTest.class, AbstractDataManager.class, AbstractDataReader.class,
                AbstractDataWriter.class, PlainFileDataManager.class, PlainFileDataReader.class,
                PlainFileDataWriter.class, SecureFileDataManager.class, SecureFileDataReader.class,
                SecureFileDataWriter.class);
        SYSTEM_REGISTRY.put(CONFIG_KEY1, new PlainFileDataManager());
        SYSTEM_REGISTRY.put(CONFIG_KEY2, new SecureFileDataManager());
    }

    @AfterClass
    public static void setUpAfterClass() throws Exception {
        if (!KEEP_FILES_AFTER_TEST) {
            File file1 = new File(MiscUtils.getHomeDirectory(), FUNNY_TEXT_FILE_NAME + ".txt");
            File file2 = new File(MiscUtils.getHomeDirectory(), FUNNY_TEXT_FILE_NAME + ".sec");
            if (file1.exists()) {
                file1.delete();
            }
            if (file2.exists()) {
                file2.delete();
            }
        }
    }

    @Test
    public void testWithConfig1() {

        // Adjust the log-level above to FINE to see the ABSTRACT FACTORY working

        LOGGER.info("Performing test with configuration 1 ...");
        long startTimeNanos = System.nanoTime();
        commonTestInternal(CONFIG_KEY1, FUNNY_TEXT);
        LOGGER.info("Execution of test with configuration 1 was successful! Elapsed time: "
                + MiscUtils.formatNanosAsSeconds(System.nanoTime() - startTimeNanos) + " s");
    }

    @Test
    public void testWithConfig2() {
        LOGGER.info("Performing test with configuration 2 ...");
        long startTimeNanos = System.nanoTime();
        commonTestInternal(CONFIG_KEY2, FUNNY_TEXT);
        LOGGER.info("Execution of test with configuration 2 was successful! Elapsed time: "
                + MiscUtils.formatNanosAsSeconds(System.nanoTime() - startTimeNanos) + " s");
    }

    /**
     * Central generic part of the test.
     * @param configKey which configuration to be used
     * @param funnyText text to store
     */
    private void commonTestInternal(String configKey, String funnyText) {

        LOGGER.fine("Text: \n============================\n" + funnyText + "\n============================\n.");

        // retrieve a concrete manager (CONCRETE FACTORY)
        AbstractDataManager mgr = SYSTEM_REGISTRY.get(configKey);

        LOGGER.fine("Using Manager: " + mgr.getName());

        // retrieve concrete writer (CONCRETE PRODUCT)
        AbstractDataWriter writer = mgr.createDataWriter("Funny Text");
        LOGGER.fine("Writing to destination: " + writer.getDestinationInfo());
        writer.writeString(funnyText);

        // retrieve concrete reader (CONCRETE PRODUCT)
        AbstractDataReader reader = mgr.createDataReader("Funny Text");
        LOGGER.fine("Reading from source: " + reader.getSourceInfo());
        String result = reader.readString();

        assertEquals(result, funnyText);
    }

}
