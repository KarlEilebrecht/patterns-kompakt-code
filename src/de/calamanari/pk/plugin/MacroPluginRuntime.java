/*
 * Macro plugin Runtime
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
package de.calamanari.pk.plugin;

/**
 * Macro Plugin Runtime Interface must be implemented by each plugin, it defines the methods for calling macros.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)web.de">Karl Eilebrecht</a>
 */
public interface MacroPluginRuntime {

    /**
     * Allows to inject a reference to the framework when initializing the plugin.<br>
     * This is called SETTER-INJECTION (M. Fowler)
     * @param frameworkReference reference to framework for plugin
     */
    public void setFrameworkReference(MacroPluginFramework frameworkReference);

    /**
     * Executes the specified macro and returns the result.
     * @param macroName name of macro
     * @param args some optional arguments NOT NULL
     * @return string result or null
     */
    public Object executeMacro(String macroName, Object[] args);

}
