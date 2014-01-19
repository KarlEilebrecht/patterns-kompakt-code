/*
 * External Process Manager
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

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class (singleton) helps to start and stop external java program instances.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public final class ExternalProcessManager {

    /**
     * logger
     */
    private static final Logger LOGGER = Logger.getLogger(ExternalProcessManager.class.getName());

    /**
     * The only instance
     */
    private static final ExternalProcessManager INSTANCE = new ExternalProcessManager();

    /**
     * minimum wait time for external process shutdown
     */
    private static final long MIN_SHUTDOWN_WAIT_TIME_MILLIS = 500;

    /**
     * map with managed processes, one per class
     */
    private final Map<Class<?>, Process> externalServerProcesses = new HashMap<>();

    /**
     * no public constructor, no subclassing
     */
    private ExternalProcessManager() {
        // no special instances
    }

    /**
     * Returns manager instance
     * @return the onley instance
     */
    public static ExternalProcessManager getInstance() {
        return INSTANCE;
    }

    /**
     * Starts an external instance
     * @param mainClass class to be executed (must provide main(arg[])-method NOT NULL
     * @param logger target for redirecting process console output NOT NULL
     * @param commandLineArgs optional arguments
     * @throws Exception on any error during start
     */
    public synchronized void startExternal(Class<?> mainClass, Logger logger, String... commandLineArgs)
            throws Exception {

        if (mainClass == null) {
            throw new IllegalArgumentException("Argument 'mainClass' must not be null.");
        }

        if (logger == null) {
            throw new IllegalArgumentException("Argument 'logger' must not be null.");
        }

        Process externalServerProcess = externalServerProcesses.get(mainClass);

        if (externalServerProcess != null) {
            throw new IllegalStateException("Instance is already running!");
        }

        List<String> args = new ArrayList<>();

        args.add("java");
        args.add("-classpath");
        args.add(System.getProperties().getProperty("java.class.path", null));
        args.add(mainClass.getName());
        args.addAll(Arrays.asList(commandLineArgs));

        ProcessBuilder pb = new ProcessBuilder(args);

        externalServerProcess = pb.start();
        (new ExternalConsoleHandlerThread(mainClass.getSimpleName() + " Console",
                externalServerProcess.getInputStream(), logger, Level.INFO)).start();
        (new ExternalConsoleHandlerThread(mainClass.getSimpleName() + " Console",
                externalServerProcess.getErrorStream(), logger, Level.INFO)).start();
        externalServerProcesses.put(mainClass, externalServerProcess);
    }

    /**
     * Stops the running external instance.<br>
     * The system uses the input stream of the existing process, it will NOT execute "mainClass stopCommand"!<br>
     * If the process does not terminate it will be destroyed after approx. maxWaitTimeMillis.
     * @param mainClass identifies the process
     * @param stopCommand quit command to send via process' input stream, if null/empty force termination
     * @param maxWaitTimeMillis maximum wait time in milliseconds, after this time the process will be killed
     * @return exit code or Integer.MIN_VALUE to indicate abnormal/forced termination
     * @throws Exception on any error while trying to stop
     */
    public synchronized int stopExternal(Class<?> mainClass, String stopCommand, long maxWaitTimeMillis)
            throws Exception {

        if (mainClass == null) {
            throw new IllegalArgumentException("Argument 'mainClass' must not be null.");
        }

        Process externalServerProcess = externalServerProcesses.remove(mainClass);
        int res = Integer.MIN_VALUE;
        if (externalServerProcess != null) {
            int waitAttempts = 0;
            int maxWaitAttempts = (int) (maxWaitTimeMillis / MIN_SHUTDOWN_WAIT_TIME_MILLIS);
            if (maxWaitAttempts <= 0) {
                maxWaitAttempts = 1;
            }

            if (stopCommand != null && stopCommand.trim().length() > 0) {
                PrintWriter pw = new PrintWriter(externalServerProcess.getOutputStream());
                pw.println(stopCommand.trim());
                pw.flush();
                // give the shutdown some time
                for (; waitAttempts < maxWaitAttempts && res == Integer.MIN_VALUE; waitAttempts++) {
                    Thread.sleep(MIN_SHUTDOWN_WAIT_TIME_MILLIS);
                    try {
                        res = externalServerProcess.exitValue();
                    }
                    catch (Exception ex) {
                        continue;
                    }

                }
            }
            if (stopCommand == null || stopCommand.trim().length() > 0 || waitAttempts >= maxWaitAttempts) {
                externalServerProcess.destroy();
                if (waitAttempts >= maxWaitAttempts) {
                    LOGGER.warning("Forcibly terminated external process " + mainClass.getSimpleName() + ".");
                }
            }
        }
        return res;
    }

}
