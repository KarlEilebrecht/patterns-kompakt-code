/*
 * Component With Constructor Injection - demonstrates CONSTRUCTOR INJECTION
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
 * Component With Constructor Injection - demonstrates CONSTRUCTOR INJECTION
 * @author <a href="mailto:Karl.Eilebrecht(a/t)web.de">Karl Eilebrecht</a>
 */
public class ComponentWithConstructorInjection implements Component {

    /**
     * logger
     */
    private static final Logger LOGGER = Logger.getLogger(ComponentWithConstructorInjection.class.getName());

    /**
     * A reference to the print service injected by constructor
     */
    private final PrintService printService;

    /**
     * data of the component
     */
    private String data = null;

    /**
     * Constructor, only used by the framework, allows DEPENDENCY INJECTION
     * @param printService injected reference to print service
     */
    public ComponentWithConstructorInjection(PrintService printService) {
        LOGGER.fine(this.getClass().getSimpleName() + " created, service injected by constructor!");
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