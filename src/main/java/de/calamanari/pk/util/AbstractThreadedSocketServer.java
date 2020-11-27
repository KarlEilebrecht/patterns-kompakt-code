//@formatter:off
/*
 * Abstract Threaded Socket server
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

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

/**
 * Abstract Threaded Socket server<br>
 * This is a threaded socket server, it can accept and process an arbitrary number of connections concurrently.<br>
 * A subclass only has to implement the concrete communication operations, the other stuff is handled by logic in the super classes (TEMPLATE METHOD
 * pattern).<br>
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public abstract class AbstractThreadedSocketServer extends AbstractConsoleServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractThreadedSocketServer.class);

    /**
     * port the mock server listens
     */
    private volatile int serverPort;

    /**
     * reference to serverSocket
     */
    // Volatile is sufficient as there is no race condition in this scenario
    @SuppressWarnings("java:S3077")
    protected volatile ServerSocket serverSocket = null;

    /**
     * Thread management
     */
    private final ExecutorService executorService;

    /**
     * Creates new mock without starting it yet.
     * 
     * @param serverName name of the new server
     */
    public AbstractThreadedSocketServer(String serverName) {
        super(serverName);
        this.executorService = createExecutorService();
    }

    /**
     * This method handles the socket communication (usually the job of the native legacy library).<br>
     * A call to this method returns immediately while the execution part runs in its own thread. This allows the server (the caller of this method) to proceed
     * with accepting further requests. <br>
     * This TEMPLATE METHOD delegates single communication handling to subclasses.
     * 
     * @param socket accepted socket
     */
    protected void handleSocketCommunicationThreaded(final Socket socket) {
        executorService.execute(() -> {
            try {
                LOGGER.debug("{} Connected to {}", Thread.currentThread().getName(), socket.getInetAddress());
                handleSocketCommunication(socket);
            }
            catch (Exception ex) {
                LOGGER.error("Error during socket communication.", ex);
            }
            finally {
                CloseUtils.closeResourceCatch(socket);
            }
            LOGGER.debug("{} disconnected.", Thread.currentThread().getName());
        });
    }

    @Override
    protected void configureInstance(String[] cmdLineArgs) {
        int port = getDefaultPort();
        if (cmdLineArgs != null && cmdLineArgs.length > 0) {
            try {
                port = Integer.parseInt(cmdLineArgs[0]);
            }
            catch (Exception ex) {
                LOGGER.warn("Error parsing port='{}', using default={}", cmdLineArgs[0], port, ex);
            }
        }
        this.serverPort = port;
    }

    @Override
    protected void prepare() {
        try {
            serverSocket = new ServerSocket(serverPort);
        }
        catch (IOException | RuntimeException ex) {
            throw new SocketPreparationException(ex);
        }
    }

    @Override
    protected String createStartupCompletedMessage() {
        return this.getServerName() + " started - listening on port " + this.serverPort;
    }

    @Override
    protected void doRequestProcessing() {
        while (serverSocket != null) {
            try {
                // socket will be closed by the responsible worker thread
                Socket socket = serverSocket.accept();
                handleSocketCommunicationThreaded(socket);
            }
            catch (Exception ex) {
                if (serverSocket.isClosed()) {
                    serverSocket = null;
                    break;
                }
                else {
                    LOGGER.error("Communication error!", ex);
                }
            }
        }
    }

    @Override
    protected void initiateShutdown() {
        CloseUtils.closeResourceCatch(Level.WARN, serverSocket);
    }

    @Override
    protected void cleanUp() {
        CloseUtils.closeResourceCatch(Level.WARN, serverSocket);
        executorService.shutdown();
    }

    /**
     * Method for handling a single socket communication<br>
     * Subclasses communicate to the client using the socket's streams.<br>
     * This method must be implemented thread-safe.
     * 
     * @param socket (for communication, will be closed by caller automatically)
     * @throws SocketCommunicationException delegate handling to caller
     */
    protected abstract void handleSocketCommunication(Socket socket) throws SocketCommunicationException;

    /**
     * Method returning a default port.<br>
     * Subclasses return here a standard port for this server.
     * 
     * @return default port
     */
    protected abstract int getDefaultPort();

    /**
     * This method returns an appropriate ExecutorService for Thread-management. <br>
     * Subclasses may override this method to gain control about pool size and behavior.
     * 
     * @return executor service
     */
    protected ExecutorService createExecutorService() {
        return Executors.newCachedThreadPool(new WorkerThreadFactory());
    }

    /**
     * Returns new factory for threads, the default implementation returns daemon threads
     */
    protected class WorkerThreadFactory implements ThreadFactory {
        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            t.setName("WorkerThread('" + getServerName() + "'):@" + Integer.toHexString(t.hashCode()));
            t.setDaemon(true);
            return t;
        }
    }

}
