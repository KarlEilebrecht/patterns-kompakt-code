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
package de.calamanari.pk.dependencyinjection;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.BeforeClass;
import org.junit.Test;

import de.calamanari.pk.dependencyinjection.Component;
import de.calamanari.pk.dependencyinjection.ComponentFramework;
import de.calamanari.pk.dependencyinjection.ComponentWithAnnotationBasedInjection;
import de.calamanari.pk.dependencyinjection.ComponentWithConstructorInjection;
import de.calamanari.pk.dependencyinjection.ComponentWithInterfaceInjection;
import de.calamanari.pk.dependencyinjection.ComponentWithSetterInjection;
import de.calamanari.pk.util.LogUtils;
import de.calamanari.pk.util.MiscUtils;

/**
 * Dependency injection test - demonstrates DEPENDENCY INJECTION
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class DependencyInjectionTest {

    /**
     * logger
     */
    private static final Logger LOGGER = Logger.getLogger(DependencyInjectionTest.class.getName());

    /**
     * Log-level for this test
     */
    private static final Level LOG_LEVEL = Level.INFO;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        LogUtils.setConsoleHandlerLogLevel(LOG_LEVEL);
        LogUtils.setLogLevel(LOG_LEVEL, DependencyInjectionTest.class, ComponentFramework.class,
                ComponentWithConstructorInjection.class, ComponentWithSetterInjection.class,
                ComponentWithInterfaceInjection.class, ComponentWithAnnotationBasedInjection.class);
    }

    @Test
    public void testDependencyInjection() throws Exception {

        // Hint: set the log-level above to FINE to see DEPENDENCY INJECTION working.

        String[] componentTypes = new String[] { ComponentFramework.COMPONENT_1, ComponentFramework.COMPONENT_2,
                ComponentFramework.COMPONENT_3, ComponentFramework.COMPONENT_4 };

        LOGGER.info("Test dependency injection ...");
        long startTimeNanos = System.nanoTime();

        for (String componentType : componentTypes) {
            Component component = ComponentFramework.createComponent(componentType);
            component.setData("Run(" + componentType + ")");
            component.printData();
        }

        LOGGER.info("Test dependency injection successful! Elapsed time: "
                + MiscUtils.formatNanosAsSeconds(System.nanoTime() - startTimeNanos) + " s");
    }

}
