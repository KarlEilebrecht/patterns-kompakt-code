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
package de.calamanari.pk.util;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This thread observes an input stream from an external console and passes the data line by line to a given logger.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class ExternalConsoleHandlerThread extends Thread {

    /**
     * logger
     */
    private static final Logger LOGGER = Logger.getLogger(ExternalConsoleHandlerThread.class.getName());

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
                targetLogger.log(logLevel, this.getName() + "\n" + line);
            }
            LOGGER.fine("Observation of '" + this.getName() + "' terminated.");
        }
        catch (Throwable t) {
            LOGGER.log(Level.WARNING, "Observation of '" + this.getName() + "' terminated with exception!", t);
        }
    }

}
