/*
 * Output Observable
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
 * Output Observable is the interface concrete observables have to implement to allow concrete OBSERVERS to register.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public interface OutputObservable {

    /**
     * This registers the observer to the observable
     * @param outputObserver instance which is interested in events
     */
    public void addOutputObserver(OutputObserver outputObserver);

    /**
     * This de-registers the observer from the observable.<br>
     * A call with an instance that was not observing (not in list) behaves like a no-op.
     * @param outputObserver instance which shall no longer be informed about events.
     */
    public void removeOutputObserver(OutputObserver outputObserver);

}
