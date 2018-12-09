//@formatter:off
/*
 * External Console Handler Thread
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
package de.calamanari.pk.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

/**
 * This thread observes an input stream from an external console and passes the data line by line to a given logger.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class ExternalConsoleHandlerThread extends Thread {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExternalConsoleHandlerThread.class);

    /**
     * Input stream of the external console
     */
    private final BufferedReader consoleReader;

    /**
     * Logger to write output
     */
    private final Logger targetLogger;

    /**
     * level for logging output
     */
    private final Level logLevel;

    /**
     * Creates new handler thread
     * 
     * @param name name to be used for the thread
     * @param consoleStream stream to be handled
     * @param logger output logger
     * @param logLevel level to be used for messages
     */
    public ExternalConsoleHandlerThread(String name, InputStream consoleStream, Logger logger, Level logLevel) {
        super(name);
        this.setDaemon(true);
        this.consoleReader = new BufferedReader(new InputStreamReader(consoleStream));
        this.targetLogger = logger;
        this.logLevel = logLevel;
    }

    @Override
    public void run() {
        try {
            String line = null;
            while ((line = consoleReader.readLine()) != null) {
                LogUtils.log(targetLogger, logLevel, this.getName() + "\n" + line);
            }
            LOGGER.debug("Observation of '{}' terminated.", this.getName());
        }
        catch (IOException | RuntimeException t) {
            LOGGER.warn("Observation of '{}' terminated with exception!", this.getName(), t);
        }
    }

}
