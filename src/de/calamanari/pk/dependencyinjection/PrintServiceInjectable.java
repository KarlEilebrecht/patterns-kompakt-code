/*
 * Print Service Injectable - demonstrates INTERFACE INJECTION
 * Code-Beispiel zum Buch Patterns Kompakt, Verlag Springer Vieweg
 * Copyright 2013 Karl Eilebrecht
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
package de.calamanari.pk.dependencyinjection;

/**
 * Print Service Injectable - demonstrates INTERFACE INJECTION<br>
 * Components implementing this interface signal to the framework that they want to have injected a reference to print
 * service.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)web.de">Karl Eilebrecht</a>
 */
public interface PrintServiceInjectable {

    /**
     * The framework uses this method to inject the print service before handing the component over to a client.
     * @param printService instance of service to be injected
     */
    public void injectPrintService(PrintService printService);

}
