//@formatter:off
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
//@formatter:on
package de.calamanari.pk.gateway;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.TimeUnit;

import org.awaitility.Awaitility;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.calamanari.pk.gateway.client.DefaultSecuMangaGatewayClient;
import de.calamanari.pk.gateway.client.SecuMangaGatewayClient;
import de.calamanari.pk.util.ExternalProcessManager;
import de.calamanari.pk.util.MiscUtils;

/**
 * Gateway test, demonstrates GATEWAY pattern
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class GatewayTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(GatewayTest.class);

    /**
     * number of test runs
     */
    private static final int NUMBER_OF_RUNS = 500;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {

        // an external java-process mocks the legacy api
        ExternalProcessManager.getInstance().startExternal(SecuMangaServerMock.class, LOGGER);

        // an external java-process, the server-part of the GATEWAY
        ExternalProcessManager.getInstance().startExternal(SecuMangaGatewayServer.class, LOGGER, "" + SecuMangaGatewayServer.DEFAULT_PORT,
                SecuMangaGatewayServer.DEFAULT_SECU_MANGA_HOST, "" + SecuMangaGatewayServer.DEFAULT_SECU_MANGA_PORT);

        // to be sure the servers are up, wait for 5 seconds
        Awaitility.await().pollDelay(5, TimeUnit.SECONDS).until(() -> true);

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

        SecuMangaGatewayClient client = new DefaultSecuMangaGatewayClient("localhost", DefaultSecuMangaGatewayClient.DEFAULT_PORT);

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
        long avgTime = (long) ((System.nanoTime() - startTimeNanos) / ((double) NUMBER_OF_RUNS * 2));
        String elapsedSeconds = MiscUtils.formatNanosAsSeconds(avgTime);
        LOGGER.info("Average runtime per call: {} s", elapsedSeconds);

        LOGGER.debug("'{}'", scrambled);
        LOGGER.debug("'{}'", unscrambled);

        assertEquals(testText, unscrambled);

        elapsedSeconds = MiscUtils.formatNanosAsSeconds(System.nanoTime() - startTimeNanos);
        LOGGER.info("Test gateway successful! Elapsed time: {} s", elapsedSeconds);

    }

}
