/*
 * Property Registry - demonstrates REGISTRY pattern
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
package de.calamanari.pk.registry;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Logger;

/**
 * Property Registry - a simple registry: safe to be accessed concurrently with minimum synchronization, providing the
 * ability to be refreshed at any time.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)web.de">Karl Eilebrecht</a>
 */
public final class PropertyRegistry {

    /**
     * logger
     */
    private static final Logger LOGGER = Logger.getLogger(PropertyRegistry.class.getName());

    /**
     * Maintains the current registry instance, which can be exchanged by the surrounding framework
     */
    private static final AtomicReference<PropertyRegistry> REGISTRY_INSTANCE = new AtomicReference<>(null);

    /**
     * The current registry data, no need to be synchronized
     */
    private final Map<String, String> propertyMap;

    /**
     * Returns the registry instance which is read-only.<br>
     * Clients may cache the obtained instance i.e. while processing a single request. They should not cache the
     * obtained instance permanently.
     * @return registry instance
     */
    public static PropertyRegistry getInstance() {
        LOGGER.fine(PropertyRegistry.class.getSimpleName() + ".getInstance() called ...");
        PropertyRegistry instance = REGISTRY_INSTANCE.get();
        if (instance == null) {
            LOGGER.fine("No instance, throwing exception.");
            throw new IllegalStateException("Registry not initialized.");
        }
        else {
            LOGGER.fine("Returning registry instance");
        }
        return instance;
    }

    /**
     * This method is used by the surrounding framework to re-initialize the registry. The method can be called at any
     * time concurrently. <br>
     * Clients will see the update when they call {@link #getInstance()} next time.
     * @param properties set of initial properties
     */
    public static void initialize(Properties properties) {
        LOGGER.fine(PropertyRegistry.class.getSimpleName() + ".initialize(...) called");
        PropertyRegistry newInstance = new PropertyRegistry(properties);
        LOGGER.fine("Setting new registry instance @" + Integer.toHexString(newInstance.hashCode()));
        REGISTRY_INSTANCE.set(newInstance);
    }

    /**
     * The framework may decide to uninitialize the registry during shutdown.<br>
     * Any further request to {@link #getInstance()} will produce an exception.
     */
    public static void uninitialize() {
        LOGGER.fine(PropertyRegistry.class.getSimpleName() + ".initialize(...) called");
        LOGGER.fine("Removing registry instance");
        REGISTRY_INSTANCE.set(null);
    }

    /**
     * Private constructor used internally to build a fresh read-only registry instance.
     * @param properties set of initial properties
     */
    private PropertyRegistry(Properties properties) {
        LOGGER.fine(PropertyRegistry.class.getSimpleName() + "-instance created");
        Map<String, String> map = new HashMap<>(properties.size());
        for (Enumeration<?> en = properties.propertyNames(); en.hasMoreElements();) {
            String propertyName = (String) en.nextElement();
            String propertyValue = properties.getProperty(propertyName);
            map.put(propertyName, propertyValue);
        }
        this.propertyMap = map;
    }

    /**
     * Returns the property value for the given key.
     * @param propertyName name of requested property
     * @param defaultValue return this value if property was not found
     * @return property value or defaultValue if not found
     */
    public String getProperty(String propertyName, String defaultValue) {
        String res = defaultValue;
        if (propertyMap.containsKey(propertyName)) {
            res = propertyMap.get(propertyName);
        }
        return res;
    }

    /**
     * Returns the property value or empty string if not found
     * @param propertyName name of requested property
     * @return property value or empty string if not found
     */
    public String getProperty(String propertyName) {
        LOGGER.fine(PropertyRegistry.class.getSimpleName() + "@" + Integer.toHexString(this.hashCode())
                + ".getProperty('" + propertyName + "') called");
        return getProperty(propertyName, "");
    }

    /**
     * Returns whether the specified property exists or not
     * @param propertyName name of requested property
     * @return true if property exists, otherwise false
     */
    public boolean containsProperty(String propertyName) {
        LOGGER.fine(PropertyRegistry.class.getSimpleName() + "@" + Integer.toHexString(this.hashCode())
                + ".containsProperty('" + propertyName + "') called");
        return propertyMap.containsKey(propertyName);
    }

    /**
     * Returns the set of currently valid property names
     * @return set of names (unmodifiable)
     */
    public Set<String> getPropertyNames() {
        LOGGER.fine(PropertyRegistry.class.getSimpleName() + "@" + Integer.toHexString(this.hashCode())
                + ".getPropertyNames() called");
        return Collections.unmodifiableSet(propertyMap.keySet());
    }
}
