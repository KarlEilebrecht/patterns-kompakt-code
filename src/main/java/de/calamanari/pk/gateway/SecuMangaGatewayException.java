//@formatter:off
/*
 * SecuMangaGatewayException
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
package de.calamanari.pk.gateway;

/**
 * Exception to be thrown by the gateway to report (wrapped) exceptions.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class SecuMangaGatewayException extends RuntimeException {

    private static final long serialVersionUID = 1040347863727567113L;

    public SecuMangaGatewayException(String message) {
        super(message);
    }

    public SecuMangaGatewayException(Throwable cause) {
        super(cause);
    }

    public SecuMangaGatewayException(String message, Throwable cause) {
        super(message, cause);
    }

    public SecuMangaGatewayException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

}
