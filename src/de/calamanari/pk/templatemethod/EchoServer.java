/*
 * Echo server - concrete class implementing the operations used by TEMPLATE METHODs.
 * Code-Beispiel zum Buch Patterns Kompakt, Verlag Springer Vieweg
 * Copyright 2013 Karl Eilebrecht
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
package de.calamanari.pk.templatemethod;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.calamanari.pk.util.AbstractThreadedSocketServer;
import de.calamanari.pk.util.LogUtils;

/**
 * Echo server - concrete class implementing the operations used by TEMPLATE METHODs of AbstractThreadedSocketServer.<br>
 * This socket server returns each received line immediately.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)web.de">Karl Eilebrecht</a>
 */
public class EchoServer extends AbstractThreadedSocketServer {

    /**
     * logger
     */
    private static final Logger LOGGER = Logger.getLogger(EchoServer.class.getName());

    /**
     * command to end communication
     */
    public static final String CMD_EXIT = "exit";

    /**
     * default port
     */
    public static final int DEFAULT_PORT = 1628;

    /**
     * Creates new instance
     */
    public EchoServer() {
        super("Echo Server");
    }

    /**
     * Concrete implementation of an operation used by a TEMPLATE METHOD in the super class.<br>
     * This implementation returns each received line immediately to the sender.
     * @param socket accepted socket
     * @throws Exception on any communication error
     */
    @Override
    protected void handleSocketCommunication(Socket socket) throws Exception {

        try (BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {

            LOGGER.info("Connected to " + socket.getInetAddress());

            String inputLine = null;

            bw.write("Welcome to " + this.getServerName() + "!\n");
            bw.flush();

            while (!socket.isClosed() && (inputLine = br.readLine()) != null) {
                if (CMD_EXIT.equals(inputLine)) {
                    break;
                }
                bw.write(inputLine + "\n");
                bw.flush();
            }
            bw.write("Goodby!");
            bw.flush();
            LOGGER.info("Closing connection to " + socket.getInetAddress());

            bw.flush();
        }
    }

    /**
     * Another concrete operation to be used by the TEMPLATE METHOD in the super class.<br>
     * @return default port
     */
    @Override
    protected int getDefaultPort() {
        return DEFAULT_PORT;
    }

    /**
     * Creates new Echo Server
     * @param args command line arguments, arg[0]=port (optional)
     */
    public static void main(String[] args) {
        LogUtils.setConsoleHandlerLogLevel(Level.INFO);
        (new EchoServer()).setupAndStart(args);
    }

}
