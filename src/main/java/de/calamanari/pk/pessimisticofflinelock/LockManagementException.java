//@formatter:off
/*
 * LockManagementException
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
package de.calamanari.pk.pessimisticofflinelock;

/**
 * Unchecked exception to be thrown by the lock manager in case of severe internal errors.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class LockManagementException extends RuntimeException {

    private static final long serialVersionUID = 1623356216217118788L;

    public LockManagementException(String message) {
        super(message);
    }

    public LockManagementException(Throwable cause) {
        super(cause);
    }

    public LockManagementException(String message, Throwable cause) {
        super(message, cause);
    }

}