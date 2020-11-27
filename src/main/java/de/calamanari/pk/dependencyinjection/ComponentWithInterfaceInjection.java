//@formatter:off
/*
 * Component With Interface Injection - demonstrates INTERFACE INJECTION
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
package de.calamanari.pk.dependencyinjection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Component With Interface Injection - demonstrates INTERFACE INJECTION
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class ComponentWithInterfaceInjection implements Component, PrintServiceInjectable {

    private static final Logger LOGGER = LoggerFactory.getLogger(ComponentWithInterfaceInjection.class);

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
        LOGGER.debug("{} created, service not yet injected!", this.getClass().getSimpleName());
    }

    @Override
    public void injectPrintService(PrintService printService) {
        LOGGER.debug("{}.injectPrintService(...) called, service injected by interface method!", this.getClass().getSimpleName());
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
        LOGGER.debug("{}.printData() called, using injected print service!", this.getClass().getSimpleName());
        this.printService.print(this.getClass().getSimpleName() + ": " + data);
    }

}
