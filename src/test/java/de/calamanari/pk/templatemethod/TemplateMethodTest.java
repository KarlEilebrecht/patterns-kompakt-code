//@formatter:off
/*
 * Template Method Test - demonstrates the TEMPLATE METHOD pattern.
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
package de.calamanari.pk.templatemethod;

import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.calamanari.pk.util.ExternalProcessManager;
import de.calamanari.pk.util.TimeUtils;

/**
 * Template Method Test - demonstrates the TEMPLATE METHOD pattern.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class TemplateMethodTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(TemplateMethodTest.class);

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {

        // an external java-process for the EchoServer
        ExternalProcessManager.getInstance().startExternal(EchoServer.class, LOGGER);

        // To be sure the server is up, wait 3 seconds.
        TimeUtils.sleepThrowRuntimeException(3000);
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        // stop the server process
        ExternalProcessManager.getInstance().stopExternal(EchoServer.class, "q", 5000);
    }

    @Test
    public void testExample() {

        // Hint: set the log level in logback.xml to DEBUG to see TEMPLATE METHODs at work.

        LOGGER.info("Test ExampleTemplateMethodStringCodec ...");
        long startTimeNanos = System.nanoTime();

        String testText = "The mad cat stumbled across the fat bat.";

        ExampleTemplateMethodStringCodec codec = new ExampleTemplateMethodStringCodec();
        String output1 = codec.processText(testText);
        LOGGER.debug(output1);
        String output2 = codec.processText(output1);
        LOGGER.debug(output2);

        assertEquals(testText, output2);

        String elapsedSeconds = TimeUtils.formatNanosAsSeconds(System.nanoTime() - startTimeNanos);
        LOGGER.info("Test ExampleTemplateMethodStringCodec successful! Elapsed time: {} s", elapsedSeconds);

    }

    @Test
    public void testEchoServer() throws Exception {
        LOGGER.info("Test EchoServer ...");
        long startTimeNanos = System.nanoTime();

        String testText1 = "An apple a day keeps the doctor away!";
        String testText2 = "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy";

        String answer1 = null;
        String answer2 = null;

        try (Socket clientSocket = new Socket("localhost", EchoServer.DEFAULT_PORT);
                PrintWriter bw = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
                BufferedReader br = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))) {
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
        }

        assertEquals(testText1, answer1);
        assertEquals(testText2, answer2);

        String elapsedSeconds = TimeUtils.formatNanosAsSeconds(System.nanoTime() - startTimeNanos);
        LOGGER.info("Test EchoServer successful! Elapsed time: {} s", elapsedSeconds);
    }

}
