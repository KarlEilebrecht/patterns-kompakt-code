/*
 * SecuManga Gateway Server - demonstrates the server part of GATEWAY pattern
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
package de.calamanari.pk.gateway;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.ws.Endpoint;

import de.calamanari.pk.util.AbstractConsoleServer;
import de.calamanari.pk.util.LogUtils;
import de.calamanari.pk.util.MiscUtils;

/**
 * The SecuManga Gateway provides access to the fictional native legacy SecuManga security library.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 * 
 */
public class SecuMangaGatewayServer extends AbstractConsoleServer {

    /**
     * logger
     */
    protected static final Logger LOGGER = Logger.getLogger(SecuMangaGatewayServer.class.getName());

    /**
     * default port
     */
    public static final int DEFAULT_PORT = 8091;

    /**
     * default port
     */
    public static final int DEFAULT_SECU_MANGA_PORT = 1627;

    /**
     * default port
     */
    public static final String DEFAULT_SECU_MANGA_HOST = "localhost";

    /**
     * property key
     */
    public static final String PROPERTY_SECU_MANGA_HOST = "secumanga.host";

    /**
     * property key
     */
    public static final String PROPERTY_SECU_MANGA_PORT = "secumanga.port";

    /**
     * While communicating with SecuManga this line ends the current message.
     */
    public static final String END_OF_TRANSMISSION = "#EOT";

    /**
     * Occurrences of END_OF_TRANSMISSION must be replaced.
     */
    public static final String END_OF_TRANSMISSION_REPLACEMENT = "##EOT";

    /**
     * Operation in SecuManga API
     */
    public static final String COMMAND_SCRAMBLE = "scramble";

    /**
     * Operation in SecuManga API
     */
    public static final String COMMAND_UNSCRAMBLE = "unscramble";

    /**
     * delay for request processing
     */
    private static final long REQUEST_DELAY_MILLIS = 500;
    
    /**
     * some properties of the gateway server
     */
    private static final Map<String, String> SYSTEM_PROPERTIES = new ConcurrentHashMap<>();
    static {
        SYSTEM_PROPERTIES.put(PROPERTY_SECU_MANGA_HOST, DEFAULT_SECU_MANGA_HOST);
        SYSTEM_PROPERTIES.put(PROPERTY_SECU_MANGA_PORT, "" + DEFAULT_SECU_MANGA_PORT);
    }

    /**
     * returns the server properties
     * @return property map
     */
    public static Map<String, String> getSystemProperties() {
        return Collections.unmodifiableMap(SYSTEM_PROPERTIES);
    }

    /**
     * port the mock server listens
     */
    private volatile int serverPort;

    /**
     * reference to service endpoint
     */
    protected volatile Endpoint serviceEndpoint;

    /**
     * Creates new server instance without starting it yet.<br>
     * We want to focus on the GATEWAY PATTERN, the most of the server/request stuff you'll find in the super class.
     */
    public SecuMangaGatewayServer() {
        super(SecuMangaGatewayServer.class.getSimpleName());
    }

    @Override
    protected void configureInstance(String[] cmdLineArgs) {
        int port = DEFAULT_PORT;
        if (cmdLineArgs != null && cmdLineArgs.length > 0) {
            try {
                port = Integer.parseInt(cmdLineArgs[0]);
            }
            catch (Exception ex) {
                LOGGER.log(Level.WARNING, "Error parsing port='" + cmdLineArgs[0] + "', using default=" + port, ex);
            }
            if (cmdLineArgs.length > 1) {
                String secuMangaHost = cmdLineArgs[1];
                SYSTEM_PROPERTIES.put(PROPERTY_SECU_MANGA_HOST, secuMangaHost);
            }
            if (cmdLineArgs.length > 2) {
                String sSecuMangaPort = cmdLineArgs[2];
                int secuMangaPort = DEFAULT_SECU_MANGA_PORT;
                try {
                    secuMangaPort = Integer.parseInt(cmdLineArgs[2]);
                }
                catch (Exception ex) {
                    LOGGER.log(Level.WARNING, "Error parsing secuMangaPort='" + sSecuMangaPort + "', using default="
                            + secuMangaPort, ex);
                }
                SYSTEM_PROPERTIES.put(PROPERTY_SECU_MANGA_PORT, "" + secuMangaPort);
            }
        }
        this.serverPort = port;
    }

    @Override
    protected void prepare() {
        serviceEndpoint = Endpoint.publish("http://localhost:" + serverPort + "/SecuMangaWebService?wsdl",
                new SecuMangaGatewayWebService());
    }

    @Override
    protected String createStartupCompletedMessage() {
        return this.getServerName() + " started - webservice published at " + "http://localhost:" + serverPort
                + "/SecuMangaWebService?wsdl";
    }

    @Override
    protected void doRequestProcessing() {
        while (true) {
            MiscUtils.sleepIgnoreException(REQUEST_DELAY_MILLIS);
            if (getServerState() != ServerState.ONLINE) {
                break;
            }
        }
    }

    @Override
    protected void initiateShutdown() {
        // nothing to do

    }

    @Override
    protected void cleanUp() {
        try {
            if (serviceEndpoint != null) {
                serviceEndpoint.stop();
            }
        }
        catch (Throwable t) {
            LOGGER.log(Level.WARNING, "Error during clean-up!", t);
        }
    }

    /**
     * Creates stand-alone console server mocking the SecuManga-legacy API
     * @param args first argument may optionally specify the port
     */
    public static void main(String[] args) {
        LogUtils.setConsoleHandlerLogLevel(Level.INFO);
        (new SecuMangaGatewayServer()).setupAndStart(args);
    }

}
