//@formatter:off
/*
 * Component With Setter Injection - demonstrates SETTER INJECTION
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
package de.calamanari.pk.dependencyinjection;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Component With Setter Injection - demonstrates SETTER INJECTION
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class ComponentWithSetterInjection implements Component {

    private static final Logger LOGGER = LoggerFactory.getLogger(ComponentWithSetterInjection.class);

    /**
     * A reference to the print service injected via setter
     */
    private PrintService printService;

    /**
     * data of the component
     */
    private String data = null;

    /**
     * Default constructor
     */
    public ComponentWithSetterInjection() {
        LOGGER.debug("{} created, service not yet injected!", this.getClass().getSimpleName());
    }

    /**
     * Allows the framework to set the reference to the print service.<br>
     * In the wildlife you'll probably see setter injection in conjunction with annotations. Annotations allow to choose an individual setter name (because the
     * annotation indicates the need, not the name of the method), and they support properties. Maybe one component needs a thread-safe printer service while
     * the most do not. Using an injection annotation the component can describe what it needs on a more fine-grained level.
     * 
     * @param printService service to be injected
     */
    public void setPrintService(PrintService printService) {
        LOGGER.debug("{}.setPrintService(...) called, service injected by setter!", this.getClass().getSimpleName());
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
