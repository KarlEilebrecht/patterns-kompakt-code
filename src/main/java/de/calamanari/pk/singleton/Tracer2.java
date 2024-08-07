//@formatter:off
/*
 * Tracer2
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
package de.calamanari.pk.singleton;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.nio.file.Files;
import java.util.concurrent.locks.ReentrantLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.calamanari.pk.util.FileUtils;

/**
 * Tracer2 - a more sophisticated thread-safe SINGLETON implementation<br>
 * <b>Discussion: </b> <br>
 * It is highly questionable whether the fewer synchronization effect justifies the much more complex (harder to read) implementation.<br>
 * For example error handling gets really weird, since the point where errors can occur (here: file access problem during class initialization) is surprising.
 * This implicitly moves responsibility to the caller.<br>
 * Furthermore the cause of a problem may be blurred terribly - or what do <i>you</i> think, if you find a <code>{@linkplain NoClassDefFoundError}</code> in
 * your log file? :-) <br>
 * Thus usually I'd vote for singletons leveraging locks or even old-fashioned synchronization. <br>
 * However, under certain circumstances avoiding synchronization the way shown in this example might have a measurable performance advantage. <br>
 * Advice: don't implement this type of singleton just because it seems to be cool ...<br>
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public final class Tracer2 implements Serializable {

    /**
     * for serialization
     */
    private static final long serialVersionUID = -6359333920241183969L;

    private static final Logger LOGGER = LoggerFactory.getLogger(Tracer2.class);

    /**
     * This class is only pseudo-synchronization
     */
    private static final class Internal {

        /**
         * This will be the SINGLETON instance
         */
        private static final Tracer2 INSTANCE;

        static {
            LOGGER.debug("No instance of {} exists, yet. Creating one...", Tracer2.class.getSimpleName());

            // here could for example take place a system registry lookup to determine the output directory
            // and the configured file name.
            File outputFile = new File(FileUtils.getHomeDirectory(), "tracer2.log");

            INSTANCE = new Tracer2(outputFile);

        }
    }

    /**
     * the output writer, the tracer uses this is declared transient, we do not want to serialize this one!
     */
    private transient BufferedWriter traceWriter;

    /**
     * Reference to output file for optional delete (cleanup)
     */
    private final transient File outputFile;

    /**
     * For concurrent file access control we use a fair lock, this has nothing to do with singleton management
     */
    private final transient ReentrantLock fileAccessLock = new ReentrantLock(true);

    /**
     * Method to retrieve the only instance of Tracer2 - the SINGLETON instance
     * 
     * @return the tracer2 instance
     */
    public static final Tracer2 getInstance() {
        LOGGER.debug("{}.getInstance() called.", Tracer2.class.getSimpleName());
        Tracer2 res = Internal.INSTANCE;
        LOGGER.debug("Returning {} singleton instance.", Tracer2.class.getSimpleName());
        return res;
    }

    /**
     * Nobody but the getInstance()-method will be able to create a Tracer2 instance
     * 
     * @param outputFile destination for tracer
     */
    private Tracer2(File outputFile) {
        try {
            this.outputFile = outputFile;
            this.traceWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outputFile)));
        }
        catch (FileNotFoundException | RuntimeException ex) {
            throw new TracerException("Unexpected error creating tracer output file " + outputFile, ex);
        }
        LOGGER.debug("New instance of {} created, using file: {}", Tracer2.class.getSimpleName(), outputFile);
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
     * shuts down the Tracer2
     * 
     * @param deleteLogFile if true, immediately delete the created log file
     */
    public static void shutdown(boolean deleteLogFile) {
        try {
            Internal.INSTANCE.closeFile(deleteLogFile);
        }
        catch (TracerException ex) {
            LOGGER.error("Error during shutdown", ex);
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
        LOGGER.debug("{} singelton destroyed", Tracer2.class.getSimpleName());
    }

    /**
     * No, of course, we cannot serialize/deserialize the instance correctly which would cause a duplication, but we can return the official instance during
     * deserialization.
     * 
     * @return {@link #getInstance()}
     */
    public Object readResolve() {
        return getInstance();
    }

}
