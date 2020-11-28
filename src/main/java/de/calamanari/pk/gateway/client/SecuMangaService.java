//@formatter:off
/*
 * SecuManga Service
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
package de.calamanari.pk.gateway.client;

import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.namespace.QName;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import jakarta.xml.ws.Service;
import jakarta.xml.ws.WebEndpoint;
import jakarta.xml.ws.WebServiceClient;
import jakarta.xml.ws.WebServiceFeature;

/**
 * This class was generated.
 * 
 */
@WebServiceClient(name = "SecuMangaService", targetNamespace = "http://gateway.pk.calamanari.de/", wsdlLocation = "http://localhost:8091/SecuMangaWebService?wsdl")
public class SecuMangaService extends Service {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecuMangaService.class);

    private static final String HTTP_GATEWAY_PK_CALAMANARI_DE = "http://gateway.pk.calamanari.de/";

    /**
     * location
     */
    private static final URL SECUMANGASERVICE_WSDL_LOCATION;

    static {
        URL url = null;
        try {
            URL baseUrl;
            baseUrl = de.calamanari.pk.gateway.client.SecuMangaService.class.getResource(".");
            url = new URL(baseUrl, "http://localhost:8091/SecuMangaWebService?wsdl");
        }
        catch (MalformedURLException e) {
            LOGGER.warn("Failed to create URL for the wsdl Location: " + "'http://localhost:8091/SecuMangaWebService?wsdl', retrying as a local file");
            LOGGER.warn(e.getMessage());
        }
        SECUMANGASERVICE_WSDL_LOCATION = url;
    }

    /**
     * Creates service from url and name
     * 
     * @param wsdlLocation url
     * @param serviceName name of service
     */
    public SecuMangaService(URL wsdlLocation, QName serviceName) {
        super(wsdlLocation, serviceName);
    }

    /**
     * Creates service with default settings
     */
    public SecuMangaService() {
        super(SECUMANGASERVICE_WSDL_LOCATION, new QName(HTTP_GATEWAY_PK_CALAMANARI_DE, "SecuMangaService"));
    }

    /**
     * 
     * @return returns SecuMangaWebService
     */
    @WebEndpoint(name = "SecuMangaWebServicePort")
    public SecuMangaWebService getSecuMangaWebServicePort() {
        return super.getPort(new QName(HTTP_GATEWAY_PK_CALAMANARI_DE, "SecuMangaWebServicePort"), SecuMangaWebService.class);
    }

    /**
     * 
     * @param features A list of {@linkplain jakarta.xml.ws.WebServiceFeature} to configure on the proxy. Supported features not in the <code>features</code>
     *            parameter will have their default values.
     * @return returns SecuMangaWebService
     */
    @WebEndpoint(name = "SecuMangaWebServicePort")
    public SecuMangaWebService getSecuMangaWebServicePort(WebServiceFeature... features) {
        return super.getPort(new QName(HTTP_GATEWAY_PK_CALAMANARI_DE, "SecuMangaWebServicePort"), SecuMangaWebService.class, features);
    }

}
