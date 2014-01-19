/*
 * Component Framework - supplementary class in DEPENDENCY INJECTION example
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

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Logger;

import javax.annotation.Resource;

/**
 * Component Framework - supplementary class in DEPENDENCY INJECTION example<br>
 * Allows the client to create a component, injects the necessary references before handing over the component to the
 * client.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public final class ComponentFramework {

    /**
     * logger
     */
    private static final Logger LOGGER = Logger.getLogger(ComponentFramework.class.getName());

    /**
     * constant in this example to obtain component 1
     */
    public static final String COMPONENT_1 = "comp1";

    /**
     * constant in this example to obtain component 2
     */
    public static final String COMPONENT_2 = "comp2";

    /**
     * constant in this example to obtain component 3
     */
    public static final String COMPONENT_3 = "comp3";

    /**
     * constant in this example to obtain component 4
     */
    public static final String COMPONENT_4 = "comp4";

    /**
     * The framework's print service
     */
    private static final PrintService THE_PRINT_SERVICE = new PrintServiceImpl();

    /**
     * Utility class
     */
    private ComponentFramework() {
        // no instances
    }
    
    /**
     * Creates the requested component and injects necessary services
     * @param componentIdentifier identifies component type
     * @return created component
     * @throws Exception on any error
     */
    public static Component createComponent(String componentIdentifier) throws Exception {
        LOGGER.fine(ComponentFramework.class.getSimpleName() + ".createComponent(" + componentIdentifier + ") called");
        Component component = null;
        Class<? extends Component> componentClass = resolveComponentClass(componentIdentifier);

        if (PrintServiceInjectable.class.isAssignableFrom(componentClass)) {
            component = createAndPerformInterfaceInjection(componentClass);
        }
        else {
            Constructor<? extends Component> constructor = getInjectionConstructor(componentClass);
            if (constructor != null) {
                component = createAndPerformConstructorInjection(constructor);
            }
            else {
                component = createAndPerformSetterOrAnnotationBasedInjection(componentIdentifier, componentClass);
            }
        }
        return component;
    }

    /**
     * Creates component and performs a setter or annotation based injection (involves reflection).
     * @param componentIdentifier for debugging
     * @param componentClass resolved class to create an instance of
     * @return component with injected reference
     * @throws InstantiationException if component could not be created
     * @throws IllegalAccessException injection method or field could not be accessed
     * @throws InvocationTargetException injection method or field could not be invoked
     */
    private static Component createAndPerformSetterOrAnnotationBasedInjection(String componentIdentifier,
            Class<? extends Component> componentClass) throws InstantiationException, IllegalAccessException,
            InvocationTargetException {
        Component component = null;
        Method method = getInjectionMethod(componentClass);
        Field field = getInjectionField(componentClass);
        if (method != null) {
            component = componentClass.newInstance();
            LOGGER.fine("Executing setter injection ...");
            method.invoke(component, THE_PRINT_SERVICE);
        }
        else if (field != null) {
            component = componentClass.newInstance();
            LOGGER.fine("Executing annotation based injection ...");
            field.set(component, THE_PRINT_SERVICE);
        }
        else {
            throw new IllegalArgumentException("Unable to inject reference into component: " + componentIdentifier
                    + ", which was resolved to " + componentClass.getName());
        }
        return component;
    }

    /**
     * Creates the component and injects the reference directly via constructor.
     * @param constructor the component constructor to be called
     * @return component with injected reference
     * @throws InstantiationException if component could not be created
     * @throws IllegalAccessException injection method or field could not be accessed
     * @throws InvocationTargetException injection method or field could not be invoked
     */
    private static Component createAndPerformConstructorInjection(Constructor<? extends Component> constructor)
            throws InstantiationException, IllegalAccessException, InvocationTargetException {
        Component component = null;
        LOGGER.fine("Executing constructor injection ...");
        component = constructor.newInstance(THE_PRINT_SERVICE);
        return component;
    }

    /**
     * Creates a new instance of the component and injects the reference via an interface method.
     * @param componentClass resolved class to create an instance of
     * @return component with injected reference
     * @throws InstantiationException if component could not be created
     * @throws IllegalAccessException injection method or field could not be accessed
     */
    private static Component createAndPerformInterfaceInjection(Class<? extends Component> componentClass)
            throws InstantiationException, IllegalAccessException {
        Component component = null;
        component = componentClass.newInstance();
        LOGGER.fine("Executing interface injection ...");
        ((PrintServiceInjectable) component).injectPrintService(THE_PRINT_SERVICE);
        return component;
    }

    /**
     * Resolves the component class by the given identifier
     * @param componentIdentifier {@link #COMPONENT_1}, {@link #COMPONENT_2} or {@link #COMPONENT_3}
     * @return component class
     */
    private static Class<? extends Component> resolveComponentClass(String componentIdentifier) {
        Class<? extends Component> componentClass = null;
        if (COMPONENT_1.equals(componentIdentifier)) {
            componentClass = ComponentWithConstructorInjection.class;
        }
        else if (COMPONENT_2.equals(componentIdentifier)) {
            componentClass = ComponentWithInterfaceInjection.class;
        }
        else if (COMPONENT_3.equals(componentIdentifier)) {
            componentClass = ComponentWithSetterInjection.class;
        }
        else if (COMPONENT_4.equals(componentIdentifier)) {
            componentClass = ComponentWithAnnotationBasedInjection.class;
        }
        else {
            throw new IllegalArgumentException("No such component: " + componentIdentifier);
        }
        return componentClass;
    }

    /**
     * Return an injection constructor for print service injection or null if no such constructor.
     * @param cl class to be reflected
     * @return appropriate constructor
     */
    private static Constructor<? extends Component> getInjectionConstructor(Class<? extends Component> cl) {
        try {
            return cl.getConstructor(PrintService.class);
        }
        catch (Exception ex) {
            // ignore here
        }
        return null;
    }

    /**
     * Return an injection method setPrintService() for print service injection or null if no such method.
     * @param cl class to be reflected
     * @return appropriate method or null
     */
    private static Method getInjectionMethod(Class<?> cl) {
        try {
            return cl.getMethod("setPrintService", PrintService.class);
        }
        catch (Exception ex) {
            // ignore here
        }
        return null;
    }

    /**
     * Return an annotated injection field for print service injection or null if no such field.
     * @param cl class to be reflected
     * @return appropriate field or null
     */
    private static Field getInjectionField(Class<?> cl) {
        try {
            Field[] fields = cl.getDeclaredFields();
            for (Field field : fields) {
                if (field.getAnnotation(Resource.class) != null && field.getType().isAssignableFrom(PrintService.class)) {
                    field.setAccessible(true);
                    return field;
                }
            }
        }
        catch (Exception ex) {
            // ignore here
        }
        return null;
    }
}
