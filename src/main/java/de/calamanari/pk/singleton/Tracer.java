//@formatter:off
/*
 * Tracer
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
package de.calamanari.pk.singleton;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.file.Files;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.calamanari.pk.util.FileUtils;

/**
 * Tracer - a straight forward thread-safe SINGLETON implementation
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public final class Tracer {

    private static final Logger LOGGER = LoggerFactory.getLogger(Tracer.class);

    /**
     * This will be the SINGLETON instance
     */
    private static Tracer instance = null;

    /**
     * For concurrency control we use a fair lock
     */
    private static final ReentrantLock LOCK = new ReentrantLock(true);

    /**
     * For concurrency control (file access) we use a fair lock (of course not the same lock as for singleton management, this would be counterproductive!)
     */
    private final ReentrantLock fileAccessLock = new ReentrantLock(true);

    /**
     * the output writer, the tracer uses
     */
    private BufferedWriter traceWriter;

    /**
     * Reference to output file for optional delete (cleanup)
     */
    private final File outputFile;

    /**
     * Method to retrieve the only instance of Tracer - the SINGLETON instance
     * 
     * @return the tracer instance
     */
    public static Tracer getInstance() {
        LOGGER.debug("{}.getInstance() called.", Tracer.class.getSimpleName());
        LOCK.lock();
        try {
            if (instance == null) {
                LOGGER.debug("No instance of {} exists, yet. Creating one...", Tracer.class.getSimpleName());

                // here could for example take place a system registry lookup to determine the output directory
                // and the configured file name.
                File outputFile = new File(FileUtils.getHomeDirectory(), "tracer.log");

                instance = new Tracer(outputFile);

            }
        }
        catch (TracerException ex) {
            throw ex;
        }
        catch (RuntimeException ex) {
            throw new TracerException("Could not create tracer", ex);
        }
        finally {
            LOCK.unlock();
        }
        LOGGER.debug("Returning {} singleton instance.", Tracer.class.getSimpleName());
        return instance;
    }

    /**
     * Nobody but the getInstance()-method will be able to create a Tracer instance
     * 
     * @param outputFile destination for tracer
     */
    private Tracer(File outputFile) {
        try {
            this.outputFile = outputFile;
            this.traceWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile)));
        }
        catch (FileNotFoundException | RuntimeException ex) {
            throw new TracerException("Unexpected error creating tracer output file " + outputFile, ex);
        }
        LOGGER.debug("New instance of {} created, using file: {}", Tracer.class.getSimpleName(), outputFile);
    }

    /**
     * The task the SINGLETON performs, in this case write messages to a file
     * 
     * @param message some textual information
     */
    public void trace(String message) {
        StringBuilder sb = new StringBuilder();
        sb.append("" + System.currentTimeMillis());
        sb.append(" | ");
        sb.append(message);
        sb.append("\n");

        fileAccessLock.lock();
        try {
            traceWriter.append(sb);
        }
        catch (IOException | RuntimeException ex) {
            throw new TracerException("Unexpected error accessing tracer output file!", ex);
        }
        finally {
            fileAccessLock.unlock();
        }
    }

    /**
     * shuts down the Tracer
     * 
     * @param deleteLogFile if true, immediately delete the created log file
     */
    public static void shutdown(boolean deleteLogFile) {
        LOCK.lock();
        try {
            if (instance != null) {
                instance.closeFile(deleteLogFile);
                instance = null;
            }
        }
        catch (TracerException ex) {
            LOGGER.error("Error during shutdown", ex);
        }
        finally {
            LOCK.unlock();
        }
    }

    /**
     * at last close the internal writer and the underlying file
     * 
     * @param deleteLogFile if true, immediately delete the created log file
     */
    protected void closeFile(boolean deleteLogFile) {
        fileAccessLock.lock();
        try {
            if (traceWriter != null) {
                traceWriter.close();
                traceWriter = null;
                if (deleteLogFile) {
                    boolean haveDeleted = Files.deleteIfExists(outputFile.toPath());
                    LOGGER.trace("log file deleted: {}", haveDeleted);
                }
            }
        }
        catch (IOException | RuntimeException ex) {
            throw new TracerException("Could not close tracer file.", ex);
        }
        finally {
            fileAccessLock.unlock();
        }
        LOGGER.debug("{} singleton destroyed", Tracer.class.getSimpleName());
    }

}
