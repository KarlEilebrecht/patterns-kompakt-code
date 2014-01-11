/*
 * Gateway test - demonstrates GATEWAY pattern
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
package de.calamanari.pk.gateway.test;

import static org.junit.Assert.assertEquals;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import de.calamanari.pk.gateway.SecuMangaGatewayServer;
import de.calamanari.pk.gateway.client.DefaultSecuMangaGatewayClient;
import de.calamanari.pk.gateway.client.SecuMangaGatewayClient;
import de.calamanari.pk.util.ExternalProcessManager;
import de.calamanari.pk.util.LogUtils;
import de.calamanari.pk.util.MiscUtils;

/**
 * Gateway test, demonstrates GATEWAY pattern
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class GatewayTest {

    /**
     * logger
     */
    private static final Logger LOGGER = Logger.getLogger(GatewayTest.class.getName());

    /**
     * Log-level for this test
     */
    private static final Level LOG_LEVEL = Level.INFO;

    /**
     * number of test runs
     */
    private static final int NUMBER_OF_RUNS = 500;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        LogUtils.setConsoleHandlerLogLevel(LOG_LEVEL);
        LogUtils.setLogLevel(LOG_LEVEL, GatewayTest.class, SecuMangaGatewayClient.class);

        // an external java-process mocks the legacy api
        ExternalProcessManager.getInstance().startExternal(SecuMangaServerMock.class, LOGGER);

        // an external java-process, the server-part of the GATEWAY
        ExternalProcessManager.getInstance().startExternal(SecuMangaGatewayServer.class, LOGGER,
                "" + SecuMangaGatewayServer.DEFAULT_PORT, SecuMangaGatewayServer.DEFAULT_SECU_MANGA_HOST,
                "" + SecuMangaGatewayServer.DEFAULT_SECU_MANGA_PORT);

        // to be sure the servers are up, wait for 5 seconds
        Thread.sleep(5000);

    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        // stop the SecuMangaServerMock
        ExternalProcessManager.getInstance().stopExternal(SecuMangaServerMock.class, "q", 5000);

        // stop the server-part of the gateway
        ExternalProcessManager.getInstance().stopExternal(SecuMangaGatewayServer.class, "q", 5000);
    }

    @Test
    public void test() throws Exception {

        LOGGER.info("Test gateway ...");
        long startTimeNanos = System.nanoTime();

        SecuMangaGatewayClient client = new DefaultSecuMangaGatewayClient("localhost",
                DefaultSecuMangaGatewayClient.DEFAULT_PORT);

        String testText = "Take a coffee to survive - Keep alive!";

        String scrambled = null;
        String unscrambled = null;

        for (int i = 0; i < NUMBER_OF_RUNS; i++) {
            scrambled = client.scramble(testText);
            unscrambled = client.unscramble(scrambled);
        }

        // As you'll see, the most time takes the client setup
        // Because the client instances are NOT safe to be used concurrently,
        // SecuMangaGatewayClient instances should be cached in an OBJECT POOL!
        long avgTime = (long) ((double) (System.nanoTime() - startTimeNanos) / ((double) NUMBER_OF_RUNS * 2));
        LOGGER.info("Average runtime per call: " + MiscUtils.formatNanosAsSeconds(avgTime) + " s");

        LOGGER.fine("'" + scrambled + "'");
        LOGGER.fine("'" + unscrambled + "'");

        assertEquals(testText, unscrambled);

        LOGGER.info("Test gateway successful! Elapsed time: "
                + MiscUtils.formatNanosAsSeconds(System.nanoTime() - startTimeNanos) + " s");

    }

}
