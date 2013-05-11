/*
 * Component With Interface Injection - demonstrates INTERFACE INJECTION
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

import java.util.logging.Logger;

/**
 * Component With Interface Injection - demonstrates INTERFACE INJECTION
 * @author <a href="mailto:Karl.Eilebrecht(a/t)web.de">Karl Eilebrecht</a>
 */
public class ComponentWithInterfaceInjection implements Component, PrintServiceInjectable {

    /**
     * logger
     */
    private static final Logger LOGGER = Logger.getLogger(ComponentWithInterfaceInjection.class.getName());

    /**
     * A reference to the print service injected via interface method
     */
    private PrintService printService;

    /**
     * data of the component
     */
    private String data = null;

    /**
     * Default constructor
     */
    public ComponentWithInterfaceInjection() {
        LOGGER.fine(this.getClass().getSimpleName() + " created, service not yet injected!");
    }

    @Override
    public void injectPrintService(PrintService printService) {
        LOGGER.fine(this.getClass().getSimpleName()
                + ".injectPrintService(...) called, service injected by interface method!");
        this.printService = printService;
    }

    @Override
    public String getData() {
        return data;
    }

    @Override
    public void setData(String data) {
        this.data = data;
    }

    @Override
    public void printData() {
        LOGGER.fine(this.getClass().getSimpleName() + ".printData() called, using injected print service!");
        this.printService.print(this.getClass().getSimpleName() + ": " + data);
    }

}
