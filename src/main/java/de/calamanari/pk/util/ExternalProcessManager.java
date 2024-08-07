//@formatter:off
/*
 * External Process Manager
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
package de.calamanari.pk.util;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

/**
 * This class (singleton) helps to start and stop external java program instances.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
// Singleton by intention
@SuppressWarnings("java:S6548")
public final class ExternalProcessManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExternalProcessManager.class);

    /**
     * Fully qualified java command that belongs to the current process, used to spawn further processes
     */
    private static final String JAVA_COMMAND = new File(new File(System.getProperties().getProperty("java.home", "<??java.home??>"), "bin"), "java").toString();

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
     * 
     * @return the onley instance
     */
    public static ExternalProcessManager getInstance() {
        return INSTANCE;
    }

    /**
     * Starts an external instance
     * 
     * @param mainClass class to be executed (must provide main(arg[])-method NOT NULL
     * @param logger target for redirecting process console output NOT NULL
     * @param commandLineArgs optional arguments
     * @throws ExternalProcessManagementException on any error during start
     */
    @SuppressWarnings("resource")
    public synchronized void startExternal(Class<?> mainClass, Logger logger, String... commandLineArgs) throws ExternalProcessManagementException {

        if (mainClass == null) {
            throw new ExternalProcessManagementException(
                    String.format("Argument 'mainClass' must not be null (mainClass=null, commandLineArgs=%s).", Arrays.toString(commandLineArgs)));
        }

        if (logger == null) {
            throw new ExternalProcessManagementException(String.format("Argument 'logger' must not be null (mainClass=%s, commandLineArgs=%s).",
                    mainClass.getName(), Arrays.toString(commandLineArgs)));
        }

        Process externalServerProcess = externalServerProcesses.get(mainClass);

        if (externalServerProcess != null) {
            throw new ExternalProcessManagementException(
                    String.format("Instance is already running! (mainClass=%s, commandLineArgs=%s)", mainClass.getName(), Arrays.toString(commandLineArgs)));
        }

        try {
            List<String> args = new ArrayList<>();

            args.add(JAVA_COMMAND);
            args.add("-classpath");
            args.add(System.getProperties().getProperty("java.class.path", null));
            args.add(mainClass.getName());
            args.addAll(Arrays.asList(commandLineArgs));

            ProcessBuilder pb = new ProcessBuilder(args);

            externalServerProcess = pb.start();
            (new ExternalConsoleHandlerThread(mainClass.getSimpleName() + " Console", externalServerProcess.getInputStream(), logger, Level.INFO)).start();
            (new ExternalConsoleHandlerThread(mainClass.getSimpleName() + " Console", externalServerProcess.getErrorStream(), logger, Level.INFO)).start();
            externalServerProcesses.put(mainClass, externalServerProcess);
        }
        catch (IOException | RuntimeException ex) {
            throw new ExternalProcessManagementException(
                    String.format("Error starting external process (mainClass=%s, commandLineArgs=%s).", mainClass.getName(), Arrays.toString(commandLineArgs)),
                    ex);
        }
    }

    /**
     * Stops the running external instance.<br>
     * The system uses the input stream of the existing process, it will NOT execute "mainClass stopCommand"!<br>
     * If the process does not terminate it will be destroyed after approx. maxWaitTimeMillis.
     * 
     * @param mainClass identifies the process
     * @param stopCommand quit command to send via process' input stream, if null/empty force termination
     * @param maxWaitTimeMillis maximum wait time in milliseconds, after this time the process will be killed
     * @return exit code or Integer.MIN_VALUE to indicate abnormal/forced termination
     * @throws ExternalProcessManagementException on any error while trying to stop
     */
    public synchronized int stopExternal(Class<?> mainClass, String stopCommand, long maxWaitTimeMillis) throws ExternalProcessManagementException {

        if (mainClass == null) {
            throw new ExternalProcessManagementException(String.format("Argument 'mainClass' must not be null (mainClass=null, stopCommand=%s).", stopCommand));
        }

        try {
            Process externalServerProcess = externalServerProcesses.remove(mainClass);
            int res = Integer.MIN_VALUE;
            if (externalServerProcess != null) {
                res = shutdownExternalProcess(externalServerProcess, mainClass, stopCommand, maxWaitTimeMillis, res);
            }
            return res;
        }
        catch (RuntimeException ex) {
            throw new ExternalProcessManagementException(
                    String.format("Error stopping external process (mainClass=%s, stopCommand=%s).", mainClass.getName(), stopCommand), ex);
        }
    }

    private int shutdownExternalProcess(Process externalServerProcess, Class<?> mainClass, String stopCommand, long maxWaitTimeMillis, int res) {
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
                TimeUtils.sleepThrowRuntimeException(MIN_SHUTDOWN_WAIT_TIME_MILLIS);
                try {
                    res = externalServerProcess.exitValue();
                }
                catch (Exception ex) {
                    LOGGER.trace("Error during shutdown ...", ex);
                }

            }
        }
        if (stopCommand == null || stopCommand.trim().length() > 0 || waitAttempts >= maxWaitAttempts) {
            externalServerProcess.destroy();
            if (waitAttempts >= maxWaitAttempts) {
                LOGGER.warn("Forcibly terminated external process {}.", mainClass.getSimpleName());
            }
        }
        return res;
    }

}
