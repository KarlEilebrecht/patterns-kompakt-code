//@formatter:off
/*
 * Abstract console server
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

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This abstract class can be sub-classed to easily create simple servers running on the console.<br>
 * Several TEMPLATE METHODs allow to define the concrete logic and to control the behavior in subclasses.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public abstract class AbstractConsoleServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractConsoleServer.class);

    /**
     * monitor used for internal wait operations
     */
    private final Object monitor = new Object();

    /**
     * Name of this server
     */
    private final String serverName;

    /**
     * flag to indicate server is up, access guarded by lock on MONITOR
     */
    private ServerState serverState = ServerState.OFFLINE;

    /**
     * Creates new server without starting it yet.
     * 
     * @param serverName name of this server
     */
    public AbstractConsoleServer(String serverName) {
        this.serverName = serverName;
    }

    /**
     * Creates stand-alone console server and starts it.<br>
     * This method shall be called from main()-method of subclass.<br>
     * This TEMPLATE METHOD uses operations implemented by subclasses.
     * 
     * @param args command line argument
     */
    public void setupAndStart(String[] args) {
        configureInstance(args);
        this.start();

        if (this.getServerState() == ServerState.ONLINE) {
            LOGGER.info("\nPress 'q' <ENTER> to stop server");
            while (true) {
                int input = 0;
                try {
                    input = System.in.read();
                }
                catch (IOException ex) {
                    continue;
                }
                if (input == 'q') {
                    break;
                }
            }
            this.stop();
        }
    }

    /**
     * Starts the mock server using the configured port <br>
     * This method will return immediately after having started the server thread.<br>
     * This TEMPLATE METHOD uses operations implemented by subclasses.
     */
    public void start() {

        // Note: To provide a better overview I kept all the stuff in a single large method.
        // Usually you would split it into smaller methods, as for example:
        // - move the server thread to a private sub-class
        // - move the code from inside the try-block to a method prepareAndRun()
        // - move the code from inside the finally-block to a method completeShutDown()

        synchronized (monitor) {
            if (serverState != ServerState.OFFLINE) {
                throw new IllegalStateException("Cannot startup, server is in state " + serverState + ", expected: " + ServerState.OFFLINE);
            }
            serverState = ServerState.START_UP;
        }

        // setup the server thread
        Thread serverThread = new Thread() {

            @Override
            public void run() {
                try {

                    prepare();

                    // inform the thread waiting inside the start()-method
                    synchronized (monitor) {
                        boolean mustNotify = (serverState == ServerState.START_UP_WAITING);
                        if (serverState == ServerState.START_UP || serverState == ServerState.START_UP_WAITING) {
                            serverState = ServerState.ONLINE;
                            if (mustNotify) {
                                monitor.notifyAll();
                            }
                        }
                    }

                    doRequestProcessing();

                }
                finally {
                    try {
                        cleanUp();
                    }
                    catch (Throwable t) {
                        LOGGER.warn("Unexpected Error during cleanUp.", t);
                    }
                    // inform the thread waiting inside the start()-method
                    synchronized (monitor) {
                        boolean mustNotify = (serverState == ServerState.SHUT_DOWN_WAITING);
                        if (serverState == ServerState.SHUT_DOWN || serverState == ServerState.SHUT_DOWN_WAITING) {
                            serverState = ServerState.OFFLINE;
                            if (mustNotify) {
                                monitor.notifyAll();
                            }
                        }
                    }
                }
            }
        };

        serverThread.setDaemon(true);
        serverThread.start();

        // the thread start method above returns immediately, may be initialization has
        // not completed, yet; thus we wait using a monitor object:
        synchronized (monitor) {
            if (serverState == ServerState.START_UP) {
                try {
                    serverState = ServerState.START_UP_WAITING;
                    monitor.wait();
                }
                catch (InterruptedException ex) {
                    LOGGER.warn("Unexpected interruption during startup - server {} up?!", this.getServerName());
                }
            }
        }
        synchronized (monitor) {
            if (serverState == ServerState.ONLINE) {
                LOGGER.info(createStartupCompletedMessage());
            }
            else {
                LOGGER.info(this.getServerName() + " startup failed!");
            }
        }
    }

    /**
     * Stops the server<br>
     * This TEMPLATE METHOD uses operations implemented by subclasses.
     */
    public void stop() {
        synchronized (monitor) {
            if (serverState != ServerState.ONLINE) {
                throw new IllegalStateException(
                        "Cannot shutdown " + this.getServerName() + ", server is in state " + serverState + ", expected: " + ServerState.ONLINE);
            }
            serverState = ServerState.SHUT_DOWN;
        }

        try {
            initiateShutdown();
            synchronized (monitor) {
                if (serverState == ServerState.SHUT_DOWN) {
                    try {
                        serverState = ServerState.SHUT_DOWN_WAITING;
                        monitor.wait();
                    }
                    catch (InterruptedException ex) {
                        LOGGER.warn("Unexpected interruption during shutdown - server {} down?!", this.getServerName());
                    }
                }
                if (serverState == ServerState.OFFLINE) {
                    LOGGER.info(this.getServerName() + " stopped!");
                }
                else {
                    LOGGER.info(this.getServerName() + " shutdown failed!");
                }
            }
        }
        catch (Throwable ex) {
            LOGGER.error("Error stopping server - server {} down?", this.getServerName(), ex);
        }
    }

    /**
     * Returns the current state of this server
     * 
     * @return server state
     */
    public ServerState getServerState() {
        ServerState state = null;
        synchronized (monitor) {
            state = this.serverState;
        }
        return state;
    }

    /**
     * Returns the name of this server
     * 
     * @return server's name
     */
    public String getServerName() {
        return this.serverName;
    }

    /**
     * Method for configuring the concrete server instance. Subclass instances shall pass command line arguments.
     * 
     * @param cmdLineArgs command line arguments
     */
    protected abstract void configureInstance(String[] cmdLineArgs);

    /**
     * Method for preparation during startup.<br>
     * Subclasses shall do the new server's initialization here (open port, resources etc.) and return immediately.<br>
     * This method shall not block.
     */
    protected abstract void prepare();

    /**
     * Method for creating a message after successful startup.<br>
     * This is called when initialization has completed.
     * 
     * @return messsage to be displayed on console.
     */
    protected abstract String createStartupCompletedMessage();

    /**
     * Method for request processing.<br>
     * The "heart" of the server, typically an endless loop accepting and handling requests.
     */
    protected abstract void doRequestProcessing();

    /**
     * Method for initiating regular shutdown<br>
     * Subclass implementations may use this to set states etc.<br>
     * The method shall i.g. not block.
     */
    protected abstract void initiateShutdown();

    /**
     * Method for cleanUp during shutdown<br>
     * Subclasses shall close resources.
     */
    protected abstract void cleanUp();

    /**
     * States for a server's state machine<br>
     * Why so many states?<br>
     * Reason: whenever multiple threads come into play using monitors (wait/notify) there is a risk of race conditions (notify before wait). By leveraging a
     * monitor synchronization as a guard for state changes, we can design the code so that the normal case AND the rare race-condition case will be handled
     * properly.
     */
    public static enum ServerState {
        /**
         * during startup
         */
        START_UP,

        /**
         * waiting for startup
         */
        START_UP_WAITING,

        /**
         * server ready
         */
        ONLINE,

        /**
         * during shutdown
         */
        SHUT_DOWN,

        /**
         * waiting for shutdown
         */
        SHUT_DOWN_WAITING,

        /**
         * server offline
         */
        OFFLINE
    }

}
