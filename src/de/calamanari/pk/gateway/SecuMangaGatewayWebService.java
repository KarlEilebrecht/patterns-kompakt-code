/*
 * SecuManga Gateway Web Service
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
package de.calamanari.pk.gateway;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Map;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;

/**
 * Secu Manga Gateway Web Service is the server-side implementation of the web service, the server-part of GATEWAY.<br>
 * Here we delegate calls to the "native" SecuManga-API.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)web.de">Karl Eilebrecht</a>
 */
@WebService(name = "SecuMangaWebService", serviceName = "SecuMangaService")
@SOAPBinding(style = SOAPBinding.Style.RPC)
public class SecuMangaGatewayWebService {

    /**
     * Constructor used by framework
     */
    public SecuMangaGatewayWebService() {
    }

    /**
     * scramble text
     * @param text to be scrambled
     * @return scrambled version
     */
    @WebMethod
    public String getScrambledText(String text) {
        return processText(text, SecuMangaGatewayServer.COMMAND_SCRAMBLE);
    }

    /**
     * unscramble text
     * @param text scrambled text
     * @return unscrambled text
     */
    @WebMethod
    public String getUnscrambledText(String text) {
        return processText(text, SecuMangaGatewayServer.COMMAND_UNSCRAMBLE);
    }

    /**
     * Internal method because the logic of both web methods is basically the same here.
     * @param text input (base64)
     * @param secuMangaCommand what to do
     * @return response (base64)
     */
    private String processText(String text, String secuMangaCommand) {

        String response = null;
        try {

            Map<String, String> properties = SecuMangaGatewayServer.getSystemProperties();

            String host = properties.get(SecuMangaGatewayServer.PROPERTY_SECU_MANGA_HOST);
            int port = Integer.parseInt(properties.get(SecuMangaGatewayServer.PROPERTY_SECU_MANGA_PORT));

            response = doSecuMangaRequest(host, port, secuMangaCommand, text);

        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return response;
    }

    /**
     * This method sends a SecuManga-command to the remote-server, receives and returns the result.
     * @param host host name
     * @param port port
     * @param command one of the SecuManga commands
     * @param sendContent content to be sent
     * @return received content (answer)
     * @throws Exception on any problem
     */
    private String doSecuMangaRequest(String host, int port, String command, String sendContent) throws Exception {

        try (Socket secuMangaClientSocket = new Socket(host, port)) {

            PrintWriter bw = new PrintWriter(new OutputStreamWriter(secuMangaClientSocket.getOutputStream()));
            BufferedReader br = new BufferedReader(new InputStreamReader(secuMangaClientSocket.getInputStream()));

            writeRequestMessage(bw, command, sendContent);

            String content = readResponseMessage(secuMangaClientSocket, br);

            bw.close();
            br.close();
            return content;
        }
    }

    /**
     * Writes the request to the given writer in SecuManga format
     * @param serverWriter writer to server
     * @param command one of the SecuManga commands
     * @param sendContent content to be sent
     */
    private void writeRequestMessage(PrintWriter serverWriter, String command, String sendContent) {
        sendContent = sendContent.replace(SecuMangaGatewayServer.END_OF_TRANSMISSION,
                SecuMangaGatewayServer.END_OF_TRANSMISSION_REPLACEMENT);
        serverWriter.write("!" + command + "\n" + sendContent + "\n" + SecuMangaGatewayServer.END_OF_TRANSMISSION
                + "\n");
        serverWriter.flush();
    }

    /**
     * Reads the SecuManga server response from the given reader
     * @param secuMangaClientSocket open socket (to check status while reading)
     * @param serverReader delivers data from the SecuManaga server
     * @return obtained response from the SecuManga server
     * @throws IOException on message processing error
     */
    private String readResponseMessage(Socket secuMangaClientSocket, BufferedReader serverReader) throws IOException {
        StringBuilder sbContent = new StringBuilder();
        String inputLine = null;
        int count = 0;
        while (secuMangaClientSocket.isConnected() && (inputLine = serverReader.readLine()) != null) {
            if (inputLine.startsWith(SecuMangaGatewayServer.END_OF_TRANSMISSION)) {
                break;
            }
            if (count > 0) {
                sbContent.append("\n");
            }
            sbContent.append(inputLine);
            count++;
        }
        String content = sbContent.toString();
        content = content.replace(SecuMangaGatewayServer.END_OF_TRANSMISSION_REPLACEMENT,
                SecuMangaGatewayServer.END_OF_TRANSMISSION);
        return content;
    }

}