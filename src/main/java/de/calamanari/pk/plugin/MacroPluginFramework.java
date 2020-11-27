//@formatter:off
/*
 * Macro plugin Framework
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
package de.calamanari.pk.plugin;

/**
 * Macro plugin framework interface defines the methods a plugin may use to access properties and methods of the surrounding framework.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public interface MacroPluginFramework {

    /**
     * Returns the value of the requested property.
     * 
     * @param propertyName name of the property, case insensitive, NOT NULL
     * @return mapped value
     */
    public Object getProperty(String propertyName);

    /**
     * Sets the value of the denoted property.
     * 
     * @param propertyName name of the property, case insensitive, NOT NULL
     * @param value new value
     */
    public void setProperty(String propertyName, Object value);

    /**
     * Returns whether the given property is currently known
     * 
     * @param propertyName name of property, case insensitive, NOT NULL
     * @return true if property exists, otherwise false
     */
    public boolean isPropertyAvailable(String propertyName);

    /**
     * Allows protocol messages.
     * 
     * @param source usually the plugin name
     * @param message info to be logged
     */
    public void addProtocolMessage(String source, String message);

}
