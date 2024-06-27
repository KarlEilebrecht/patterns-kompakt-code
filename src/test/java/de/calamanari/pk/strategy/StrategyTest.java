//@formatter:off
/*
 * Strategy Test - demonstrates STRATEGY pattern.
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
package de.calamanari.pk.strategy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.calamanari.pk.util.TimeUtils;

/**
 * Strategy Test - demonstrates STRATEGY pattern.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class StrategyTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(StrategyTest.class);

    /**
     * Mocks the system context/registry
     */
    private static final Context CONTEXT = new Context();

    @Before
    public void setUp() {
        CONTEXT.clear();
        HashStrategy hashStrategyCrc32 = new Crc32HashStrategy();
        CONTEXT.putHashStrategy(hashStrategyCrc32.getName(), hashStrategyCrc32);
        HashStrategy hashStrategySha1 = new Sha1HashStrategy();
        CONTEXT.putHashStrategy(hashStrategySha1.getName(), hashStrategySha1);
    }

    @Test
    public void testStrategy() {

        // hint: adjust the log-level in logback.xml to DEBUG to see STRATEGY working

        LOGGER.info("Test Strategy ...");
        long startTimeNanos = System.nanoTime();
        String messageText = "Nowadays the scorpion would await the riverside, press money and - of course" + " - then kill the stupid frog ...";

        List<MessageMock> messageList = new ArrayList<>();
        List<String> availableStrategyNames = new ArrayList<>(CONTEXT.getAllHashStrategyNames());
        Collections.sort(availableStrategyNames);
        for (String hashStrategyName : availableStrategyNames) {
            HashStrategy hashStrategy = CONTEXT.getHashStrategy(hashStrategyName);
            MessageMock messageMock = new MessageMock(messageText, hashStrategy);
            messageList.add(messageMock);
        }

        for (MessageMock messageMock : messageList) {
            LOGGER.debug(messageMock.toString());
            String hashStrategyName = messageMock.getHashMethodName();
            HashStrategy hashStrategy = CONTEXT.getHashStrategy(hashStrategyName);
            assertTrue(messageMock.validate(hashStrategy));
            assertEquals(messageText, messageMock.getText());
        }

        String elapsedTimeString = TimeUtils.formatNanosAsSeconds(System.nanoTime() - startTimeNanos);
        LOGGER.info("Test Strategy successful! Elapsed time: {} s", elapsedTimeString);

    }

}
