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

import java.io.IOException;

/**
 * Exception to be thrown if the {@link ExternalProcessManager} detected problems handling the child process.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class ExternalProcessManagementException extends IOException {

    private static final long serialVersionUID = -7116263797973971206L;

    public ExternalProcessManagementException(String message) {
        super(message);
    }

    public ExternalProcessManagementException(Throwable cause) {
        super(cause);
    }

    public ExternalProcessManagementException(String message, Throwable cause) {
        super(message, cause);
    }

}