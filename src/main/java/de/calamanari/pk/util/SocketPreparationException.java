//@formatter:off
/*
 * Socket Preparation Exception
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
//@formatter:on
package de.calamanari.pk.util;

/**
 * A Runtime Exception to be thrown if the socket preparation went wrong
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class SocketPreparationException extends RuntimeException {

    private static final long serialVersionUID = -1807346722092035633L;

    public SocketPreparationException(String message) {
        super(message);
    }

    public SocketPreparationException(Throwable cause) {
        super(cause);
    }

    public SocketPreparationException(String message, Throwable cause) {
        super(message, cause);
    }

}
