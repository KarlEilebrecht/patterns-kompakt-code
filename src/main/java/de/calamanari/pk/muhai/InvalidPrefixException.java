//@formatter:off
/*
 * InvalidPrefixException 
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
package de.calamanari.pk.muhai;

/**
 * Exception to be thrown if the provided prefix is invalid, e.g. because it is too long or contains bad characters.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class InvalidPrefixException extends RuntimeException {

    private static final long serialVersionUID = 5969157132613727146L;

    /**
     * @param message explanation
     */
    public InvalidPrefixException(String message) {
        super(message);
    }

    /**
     * @param message context
     * @param cause exception that was detected when processing the prefix
     */
    public InvalidPrefixException(String message, Throwable cause) {
        super(message, cause);
    }

}
