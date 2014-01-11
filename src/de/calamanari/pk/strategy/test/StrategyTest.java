/*
 * Strategy Test - demonstrates STRATEGY pattern.
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
package de.calamanari.pk.strategy.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.calamanari.pk.strategy.Context;
import de.calamanari.pk.strategy.Crc32HashStrategy;
import de.calamanari.pk.strategy.HashStrategy;
import de.calamanari.pk.strategy.Sha1HashStrategy;
import de.calamanari.pk.util.LogUtils;
import de.calamanari.pk.util.MiscUtils;

/**
 * Strategy Test - demonstrates STRATEGY pattern.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class StrategyTest {

    /**
     * logger
     */
    protected static final Logger LOGGER = Logger.getLogger(StrategyTest.class.getName());

    /**
     * Log-level for this test
     */
    private static final Level LOG_LEVEL = Level.INFO;

    /**
     * Mocks the system context/registry
     */
    private static final Context CONTEXT = new Context();

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        LogUtils.setConsoleHandlerLogLevel(LOG_LEVEL);
        LogUtils.setLogLevel(LOG_LEVEL, Context.class, StrategyTest.class, HashStrategy.class, MessageMock.class,
                Crc32HashStrategy.class, Sha1HashStrategy.class);
    }

    @Before
    public void setUp() throws Exception {
        CONTEXT.clear();
        HashStrategy hashStrategyCrc32 = new Crc32HashStrategy();
        CONTEXT.putHashStrategy(hashStrategyCrc32.getName(), hashStrategyCrc32);
        HashStrategy hashStrategySha1 = new Sha1HashStrategy();
        CONTEXT.putHashStrategy(hashStrategySha1.getName(), hashStrategySha1);
    }

    @Test
    public void testStrategy() {

        // hint: adjust the log-levels above to FINE to see STRATEGY working

        LOGGER.info("Test Strategy ...");
        long startTimeNanos = System.nanoTime();
        String messageText = "Nowadays the scorpion would await the riverside, press money and - of course"
                + " - then kill the stupid frog ...";

        List<MessageMock> messageList = new ArrayList<>();
        List<String> availableStrategyNames = new ArrayList<>(CONTEXT.getAllHashStrategyNames());
        Collections.sort(availableStrategyNames);
        for (String hashStrategyName : availableStrategyNames) {
            HashStrategy hashStrategy = CONTEXT.getHashStrategy(hashStrategyName);
            MessageMock messageMock = new MessageMock(messageText, hashStrategy);
            messageList.add(messageMock);
        }

        for (MessageMock messageMock : messageList) {
            LOGGER.fine(messageMock.toString());
            String hashStrategyName = messageMock.getHashMethodName();
            HashStrategy hashStrategy = CONTEXT.getHashStrategy(hashStrategyName);
            assertTrue(messageMock.validate(hashStrategy));
            assertEquals(messageText, messageMock.getText());
        }

        LOGGER.info("Test Strategy successful! Elapsed time: "
                + MiscUtils.formatNanosAsSeconds(System.nanoTime() - startTimeNanos) + " s");

    }

}
