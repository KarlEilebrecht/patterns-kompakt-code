//@formatter:off
/*
 * SecuManga Web Service
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

import jakarta.jws.WebMethod;
import jakarta.jws.WebParam;
import jakarta.jws.WebResult;
import jakarta.jws.WebService;
import jakarta.jws.soap.SOAPBinding;

/**
 * This class was generated by the JAX-WS RI. JAX-WS RI 2.1.6 in JDK 6 Generated source version: 2.1
 * 
 */
@WebService(name = "SecuMangaWebService", targetNamespace = "http://gateway.pk.calamanari.de/")
@SOAPBinding(style = SOAPBinding.Style.RPC)
public interface SecuMangaWebService {

    /**
     * scramble text
     * 
     * @param arg0 text
     * @return returns java.lang.String
     */
    @WebMethod
    @WebResult(partName = "return")
    public String getScrambledText(@WebParam(name = "arg0", partName = "arg0") String arg0);

    /**
     * unscramble text
     * 
     * @param arg0 text
     * @return returns java.lang.String
     */
    @WebMethod
    @WebResult(partName = "return")
    public String getUnscrambledText(@WebParam(name = "arg0", partName = "arg0") String arg0);

}
