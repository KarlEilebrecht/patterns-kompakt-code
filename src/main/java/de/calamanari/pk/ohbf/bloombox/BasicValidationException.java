//@formatter:off
/*
 * BasicValidationException
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

package de.calamanari.pk.ohbf.bloombox;

/**
 * Internal exception to indicate validation issues
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class BasicValidationException extends BloomBoxException {

    private static final long serialVersionUID = -2903729117643410358L;

    /**
     * @param message text
     */
    public BasicValidationException(String message) {
        super(message);
    }

    /**
     * @param message text
     * @param cause caught exception
     */
    public BasicValidationException(String message, Throwable cause) {
        super(message, cause);
    }

}
