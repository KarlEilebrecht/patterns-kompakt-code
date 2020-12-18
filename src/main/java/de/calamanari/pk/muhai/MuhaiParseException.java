//@formatter:off
/*
 * MuhaiParseException 
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
 * Exception to be thrown if a given String representation cannot be converted into a MUHAI (long).
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class MuhaiParseException extends MuhaiException {

    private static final long serialVersionUID = 8660318326481567592L;

    /**
     * @param message problem description
     */
    public MuhaiParseException(String message) {
        super(message);
    }

    /**
     * @param message problem description / context
     * @param cause any exception that was caused while parsing the given muhai
     */
    public MuhaiParseException(String message, Throwable cause) {
        super(message, cause);
    }

}
