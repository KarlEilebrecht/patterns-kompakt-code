//@formatter:off
/*
 * Component With Annotation Based Injection
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

import java.util.logging.Logger;

import javax.annotation.Resource;

/**
 * Component With annotation based Injection
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class ComponentWithAnnotationBasedInjection implements Component {

    /**
     * logger
     */
    private static final Logger LOGGER = Logger.getLogger(ComponentWithAnnotationBasedInjection.class.getName());

    /**
     * A reference to the print service injected by framework
     */
    @Resource
    private PrintService printService;

    /**
     * data of the component
     */
    private String data = null;

    /**
     * Default constructor
     */
    public ComponentWithAnnotationBasedInjection() {
        LOGGER.fine(this.getClass().getSimpleName() + " created, service not yet injected!");
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
