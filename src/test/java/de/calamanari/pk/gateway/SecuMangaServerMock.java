//@formatter:off
/*
 * SecuManga Server Mock - mocks the system we provide a GATEWAY for.
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
package de.calamanari.pk.gateway;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.calamanari.pk.util.AbstractThreadedSocketServer;
import de.calamanari.pk.util.SimpleScrambleCodec;
import de.calamanari.pk.util.SocketCommunicationException;

/**
 * SecuManga is a fictional native encryption library a GATEWAY will be provided for in this demonstration.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class SecuMangaServerMock extends AbstractThreadedSocketServer {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecuMangaServerMock.class);

    /**
     * While communicating with SecuManga this line ends the current message.
     */
    private static final String END_OF_TRANSMISSION = "#EOT";

    /**
     * Occurrences of END_OF_TRANSMISSION must be replaced.
     */
    private static final String END_OF_TRANSMISSION_REPLACEMENT = "##EOT";

    /**
     * Operation in SecuManga API
     */
    private static final String COMMAND_SCRAMBLE = "scramble";

    /**
     * Operation in SecuManga API
     */
    private static final String COMMAND_UNSCRAMBLE = "unscramble";

    /**
     * default port
     */
    public static final int DEFAULT_PORT = 1627;

    /**
     * Creates new mock without starting it yet.<br>
     * We want to focus on the fictional legacy API, the most of the server/request stuff you'll find in the super
     * classes.
     */
    public SecuMangaServerMock() {
        super(SecuMangaServerMock.class.getSimpleName());
    }

    @Override
    protected void handleSocketCommunication(final Socket socket) throws SocketCommunicationException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {

            StringBuilder sbCommandName = new StringBuilder();
            StringBuilder sbContent = new StringBuilder();

            readRequestMessage(socket, br, sbCommandName, sbContent);

            String commandName = sbCommandName.toString();
            String content = sbContent.toString();

            content = content.replace(END_OF_TRANSMISSION_REPLACEMENT, END_OF_TRANSMISSION);
            if (commandName.length() > 0 && content.length() > 0) {
                String result = processText(commandName, content);
                bw.write(result);
                bw.write("\n" + END_OF_TRANSMISSION + "\n");
            }
            else {
                LOGGER.warn("Illegal call: '{}' / {}", commandName, content);
                bw.write("ERR! command '" + commandName + "'" + (content.length() > 0 ? "" : " MISSING CONTENT"));
            }
            bw.flush();
        }
        catch (IOException | RuntimeException ex) {
            throw new SocketCommunicationException(ex);
        }
    }

    /**
     * Reads the request (command name and content) from the open client socket reader
     * 
     * @param socket open socket for reading the client's request and check status
     * @param clientReader input reader
     * @param sbCommandName for storing the parsed command name provided by the client
     * @param sbContent for storing the message content
     * @throws IOException on communication error
     */
    private void readRequestMessage(final Socket socket, BufferedReader clientReader, StringBuilder sbCommandName, StringBuilder sbContent) throws IOException {
        int count = 0;
        boolean commandFound = false;
        String inputLine = null;
        while (!socket.isClosed() && (inputLine = clientReader.readLine()) != null) {
            if (inputLine.startsWith(END_OF_TRANSMISSION)) {
                break;
            }
            if (commandFound || inputLine.startsWith("!")) {
                if (!commandFound) {
                    commandFound = true;
                    inputLine = inputLine.substring(1);

                }
                if (sbCommandName.length() == 0) {
                    sbCommandName.append(inputLine.trim());
                }
                else {
                    if (count > 0) {
                        sbContent.append("\n");
                    }
                    sbContent.append(inputLine);
                    count++;
                }
            }
        }
    }

    /**
     * The heart of the SecuManga API, processes the text
     * 
     * @param commandName identifies operation
     * @param text data to process
     * @return processes text
     */
    private String processText(String commandName, String text) {
        String result = null;
        if (COMMAND_SCRAMBLE.equalsIgnoreCase(commandName)) {
            result = SimpleScrambleCodec.encode(text);
        }
        else if (COMMAND_UNSCRAMBLE.equals(commandName)) {
            result = SimpleScrambleCodec.decode(text);
        }
        else {
            result = "ERR! unsupported command '" + commandName + "'";
        }
        result = result.replace(END_OF_TRANSMISSION, END_OF_TRANSMISSION_REPLACEMENT);
        return result;
    }

    @Override
    protected int getDefaultPort() {
        return DEFAULT_PORT;
    }

    /**
     * Creates new SecuManga Server Mock
     * 
     * @param args command line arguments, arg[0]=port (optional)
     */
    public static void main(String[] args) {
        (new SecuMangaServerMock()).setupAndStart(args);
    }

}
