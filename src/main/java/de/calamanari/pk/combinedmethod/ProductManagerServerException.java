//@formatter:off
/*
 * ProductManagerServerException 
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
package de.calamanari.pk.combinedmethod;

/**
 * Unchecked exception to be thrown by the product manager server to indicate technical issues.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class ProductManagerServerException extends RuntimeException {

    private static final long serialVersionUID = -7817630198726740705L;

    /**
     * @param message information
     */
    public ProductManagerServerException(String message) {
        super(message);
    }

    /**
     * @param cause Exception to be wrapped
     */
    public ProductManagerServerException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message information
     * @param cause Exception to be wrapped
     */
    public ProductManagerServerException(String message, Throwable cause) {
        super(message, cause);
    }

}
