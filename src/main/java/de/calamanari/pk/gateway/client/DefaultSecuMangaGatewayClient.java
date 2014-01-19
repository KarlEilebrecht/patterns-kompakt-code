/*
 * SecuManga Gateway Client
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
package de.calamanari.pk.gateway.client;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.namespace.QName;

/**
 * SecuManga Gateway Client - The client part of the GATEWAY.<br>
 * This class encapsulate webservice handling, the client just uses normal methods.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class DefaultSecuMangaGatewayClient implements SecuMangaGatewayClient {

    /**
     * logger
     */
    protected static final Logger LOGGER = Logger.getLogger(DefaultSecuMangaGatewayClient.class.getName());

    /**
     * default port
     */
    public static final int DEFAULT_PORT = 8091;

    /**
     * for webservice access
     */
    private static final QName WEBSERVICE_QNAME = new QName("http://gateway.pk.calamanari.de/", "SecuMangaService");

    /**
     * URL for webservice access
     */
    private final URL webServiceUrl;

    /**
     * Service port reference
     */
    private final SecuMangaWebService webService;

    /**
     * Creates the URL for SecuMangaService
     * @param hostName name of host
     * @param port webservice port
     * @return webservice URL
     */
    private static URL createWebServiceUrl(String hostName, int port) {
        URL url = null;
        String sUrl = null;
        try {
            sUrl = "http://" + hostName + ":" + port + "/SecuMangaWebService?wsdl";
            url = new URL(sUrl);
        }
        catch (MalformedURLException ex) {
            LOGGER.log(Level.SEVERE, "Could not create URL(" + sUrl + ")!");
        }
        return url;
    }

    /**
     * Creates a new Client, client instances are NOT thread-safe, no concurrent access allowed.
     * @param hostName name of host
     * @param port number of webservice port
     */
    public DefaultSecuMangaGatewayClient(String hostName, int port) {
        this.webServiceUrl = createWebServiceUrl(hostName, port);
        this.webService = new SecuMangaService(this.webServiceUrl, WEBSERVICE_QNAME).getSecuMangaWebServicePort();
    }

    @Override
    public String scramble(String text) {
        return this.webService.getScrambledText(text);
    }

    @Override
    public String unscramble(String text) {
        return this.webService.getUnscrambledText(text);
    }

}
