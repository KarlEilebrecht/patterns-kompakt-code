//@formatter:off
/*
 * DerbyDriverSetupException
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
package de.calamanari.pk.util.db;

/**
 * Unchecked exception to be thrown if the embedded driver could not be loaded or instantiated.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class DerbyDriverSetupException extends RuntimeException {

    private static final long serialVersionUID = 8957010821026518340L;

    /**
     * @param message information
     */
    public DerbyDriverSetupException(String message) {
        super(message);
    }

    /**
     * @param cause Exception to be wrapped
     */
    public DerbyDriverSetupException(Throwable cause) {
        super(cause);
    }

    /**
     * @param message information
     * @param cause Exception to be wrapped
     */
    public DerbyDriverSetupException(String message, Throwable cause) {
        super(message, cause);
    }

}
