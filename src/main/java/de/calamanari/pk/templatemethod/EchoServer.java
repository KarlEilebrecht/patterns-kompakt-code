//@formatter:off
/*
 * Echo server - concrete class implementing the operations used by TEMPLATE METHODs.
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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.calamanari.pk.util.AbstractThreadedSocketServer;
import de.calamanari.pk.util.SocketCommunicationException;

/**
 * Echo server - concrete class implementing the operations used by TEMPLATE METHODs of AbstractThreadedSocketServer.<br>
 * This socket server returns each received line immediately.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class EchoServer extends AbstractThreadedSocketServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(EchoServer.class);

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
     * 
     * @param socket accepted socket
     * @throws SocketCommunicationException on any communication error
     */
    @Override
    protected void handleSocketCommunication(Socket socket) throws SocketCommunicationException {

        try (BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {

            LOGGER.info("Connected to {}", socket.getInetAddress());

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
            LOGGER.info("Closing connection to {}", socket.getInetAddress());

            bw.flush();
        }
        catch (IOException | RuntimeException ex) {
            throw new SocketCommunicationException(ex);
        }
    }

    /**
     * Another concrete operation to be used by the TEMPLATE METHOD in the super class.<br>
     * 
     * @return default port
     */
    @Override
    protected int getDefaultPort() {
        return DEFAULT_PORT;
    }

    /**
     * Creates new Echo Server
     * 
     * @param args command line arguments, arg[0]=port (optional)
     */
    public static void main(String[] args) {
        (new EchoServer()).setupAndStart(args);
    }

}
