//@formatter:off
/*
 * Macro Plugin Tech Info
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
package de.calamanari.pk.plugin;

/**
 * Technical interface each Macro-plugin must implement to be recognized by framework.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public interface MacroPluginTechInfo {

    /**
     * Returns the plugin's name
     * 
     * @return name of plugin
     */
    public String getName();

    /**
     * Returns information about Vendor
     * 
     * @return vendor string
     */
    public String getVendor();

    /**
     * Returns the version of the plugin
     * 
     * @return plugin version
     */
    public String getVersion();

    /**
     * Each macro-plugin must list its provided macros.
     * 
     * @return array with the supported macro names
     */
    public String[] getMacros();

}
