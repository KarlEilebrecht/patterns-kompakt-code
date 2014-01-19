/*
 * Output Observer
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
package de.calamanari.pk.observer;

/**
 * Output Observer is the interface a concrete OBSERVER implements.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public interface OutputObserver {

    /**
     * Concrete OBSERVERs will receive the number of bytes (from some instance that writes bytes for some reason) and do
     * something with this information. The call should return immediately to not block the event-firing process.
     * @param id identifies the observable
     * @param numberOfBytes byte count
     */
    public void handleBytesWritten(int id, long numberOfBytes);

}
