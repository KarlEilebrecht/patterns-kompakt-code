//@formatter:off
/*
 * Dependency injection test - demonstrates DEPENDENCY INJECTION
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

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.calamanari.pk.util.TimeUtils;

/**
 * Dependency injection test - demonstrates DEPENDENCY INJECTION
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class DependencyInjectionTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(DependencyInjectionTest.class);

    @Test
    public void testDependencyInjection() throws Exception {

        // Hint: set the log-level in logback.xml to DEBUG to see DEPENDENCY INJECTION working.

        String[] componentTypes = new String[] { ComponentFramework.COMPONENT_1, ComponentFramework.COMPONENT_2, ComponentFramework.COMPONENT_3,
                ComponentFramework.COMPONENT_4 };

        LOGGER.info("Test dependency injection ...");
        long startTimeNanos = System.nanoTime();

        for (String componentType : componentTypes) {
            Component component = ComponentFramework.createComponent(componentType);
            component.setData("Run(" + componentType + ")");
            component.printData();
        }

        LOGGER.info("Test dependency injection successful! Elapsed time: " + TimeUtils.formatNanosAsSeconds(System.nanoTime() - startTimeNanos) + " s");
    }

}
