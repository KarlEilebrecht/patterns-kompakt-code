/*
 * Template Method Test - demonstrates the TEMPLATE METHOD pattern.
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
package de.calamanari.pk.templatemethod.test;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import de.calamanari.pk.templatemethod.AbstractTemplateMethodStringCodec;
import de.calamanari.pk.templatemethod.EchoServer;
import de.calamanari.pk.templatemethod.ExampleTemplateMethodStringCodec;
import de.calamanari.pk.util.ExternalProcessManager;
import de.calamanari.pk.util.LogUtils;
import de.calamanari.pk.util.MiscUtils;

/**
 * Template Method Test - demonstrates the TEMPLATE METHOD pattern.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class TemplateMethodTest {

    /**
     * logger
     */
    private static final Logger LOGGER = Logger.getLogger(TemplateMethodTest.class.getName());

    /**
     * Log-level for this test
     */
    private static final Level LOG_LEVEL = Level.INFO;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        LogUtils.setConsoleHandlerLogLevel(LOG_LEVEL);
        LogUtils.setLogLevel(LOG_LEVEL, TemplateMethodTest.class, AbstractTemplateMethodStringCodec.class,
                ExampleTemplateMethodStringCodec.class);

        // an external java-process for the EchoServer
        ExternalProcessManager.getInstance().startExternal(EchoServer.class, LOGGER);

        // To be sure the server is up, wait 3 seconds.
        Thread.sleep(3000);
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        // stop the server process
        ExternalProcessManager.getInstance().stopExternal(EchoServer.class, "q", 5000);
    }

    @Test
    public void testExample() {

        // Hint: set the log level above to FINE to see TEMPLATE METHODs at work.

        LOGGER.info("Test ExampleTemplateMethodStringCodec ...");
        long startTimeNanos = System.nanoTime();

        String testText = "The mad cat stumbled across the fat bat.";

        ExampleTemplateMethodStringCodec codec = new ExampleTemplateMethodStringCodec();
        String output1 = codec.processText(testText);
        LOGGER.fine(output1);
        String output2 = codec.processText(output1);
        LOGGER.fine(output2);

        assertEquals(testText, output2);

        LOGGER.info("Test ExampleTemplateMethodStringCodec successful! Elapsed time: "
                + MiscUtils.formatNanosAsSeconds(System.nanoTime() - startTimeNanos) + " s");

    }

    @Test
    public void testEchoServer() throws Exception {
        LOGGER.info("Test EchoServer ...");
        long startTimeNanos = System.nanoTime();

        String testText1 = "An apple a day keeps the doctor away!";
        String testText2 = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy";

        String answer1 = null;
        String answer2 = null;

        Socket clientSocket = null;

        try {

            clientSocket = new Socket("localhost", EchoServer.DEFAULT_PORT);
            PrintWriter bw = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            String inputLine = null;
            inputLine = br.readLine();
            LOGGER.info(inputLine);

            bw.println(testText1);
            bw.flush();
            answer1 = br.readLine();
            LOGGER.info(answer1);

            bw.println(testText2);
            bw.flush();
            answer2 = br.readLine();
            LOGGER.info(answer2);

            bw.println(EchoServer.CMD_EXIT);
            bw.flush();

            inputLine = br.readLine();
            LOGGER.info(inputLine);

            bw.close();
            br.close();
        }
        finally {
            MiscUtils.closeResourceCatch(clientSocket);
        }

        assertEquals(testText1, answer1);
        assertEquals(testText2, answer2);

        LOGGER.info("Test EchoServer successful! Elapsed time: "
                + MiscUtils.formatNanosAsSeconds(System.nanoTime() - startTimeNanos) + " s");
    }

}
