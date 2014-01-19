/*
 * Macro plugin Factory
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
package de.calamanari.pk.plugin;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * Macro plugin factory finds, initializes and manages plugins.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class MacroPluginFactory {

    /**
     * logger
     */
    public static final Logger LOGGER = Logger.getLogger(MacroPluginFactory.class.getName());

    /**
     * framework reference
     */
    private final MacroPluginFramework frameworkReference;

    /**
     * This list contains the available plugins collected at startup.
     */
    private final List<MacroPlugin> availablePlugins = Collections.synchronizedList(new ArrayList<MacroPlugin>());

    /**
     * Lookup map for available plugins, key is the macro's name.toLowerCase()
     */
    private final Map<String, MacroPluginRuntime> macroPluginLookup = new ConcurrentHashMap<>();

    /**
     * Creates a new Factory for finding and managing the system's plugins.
     * @param frameworkReference reference to the framework which will be passed to the plugins
     */
    public MacroPluginFactory(MacroPluginFramework frameworkReference) {
        LOGGER.fine(this.getClass().getSimpleName() + " created.");
        this.frameworkReference = frameworkReference;
        collectAvailablePlugins();
        initializeMacroPluginLookup();
    }

    /**
     * Returns plugin for the given macro
     * @param macroName name of the requested macro
     * @return plugin for the macro
     */
    public MacroPluginRuntime getPluginForMacro(String macroName) {
        LOGGER.fine(this.getClass().getSimpleName() + ".getPluginForMacro('" + macroName + "') called.");
        MacroPluginRuntime plugin = macroPluginLookup.get(macroName.toLowerCase());
        if (plugin == null) {
            List<String> allMacros = new ArrayList<>(macroPluginLookup.keySet());
            Collections.sort(allMacros);
            // hmm, very lazy, here we better should work with a checked exception :-)
            throw new RuntimeException("No plugin available for macro '" + macroName
                    + "',\nThe following macros are currently supported: " + allMacros);
        }
        return plugin;
    }

    /**
     * Initializes the macro plugin lookup
     */
    private void initializeMacroPluginLookup() {
        for (MacroPlugin plugin : availablePlugins) {
            // inject framework reference
            plugin.setFrameworkReference(frameworkReference);
            for (String macroName : plugin.getMacros()) {
                if (macroName != null && macroName.trim().length() > 0) {
                    macroPluginLookup.put(macroName.trim().toLowerCase(), plugin);
                }
            }
        }
    }

    /**
     * Collects the plugins in the file system<br>
     * This is a way too simple implementation for demonstration only! <br>
     * The problem is that usually you would allow macros to reside in third party packages!
     */
    private void collectAvailablePlugins() {

        String pluginPackageName = this.getClass().getPackage().getName() + ".ext";
        File pluginFolder = determinePluginFolder();
        try {
            ArrayList<String> pluginNames = findPluginNames(pluginFolder);
            installPlugins(pluginPackageName, pluginNames);

        }
        catch (Exception ex) {
            throw new RuntimeException("Exception while collecting plugins from " + pluginFolder, ex);
        }

        if (availablePlugins.size() == 0) {
            throw new RuntimeException("No Plugin found at " + pluginFolder);
        }
        else {
            LOGGER.info("" + availablePlugins.size() + " plugins installed.");
        }

    }

    /**
     * Find all plugins in the given plugin-folder
     * @param pluginFolder the folder (package) where plugin classes are located
     * @return list of plugin names (simple class names)
     */
    private ArrayList<String> findPluginNames(File pluginFolder) {
        ArrayList<String> pluginNames = new ArrayList<>();
        File[] candidates = pluginFolder.listFiles();
        for (File candidate : candidates) {
            String candidateName = candidate.getName();
            if (candidate.isFile() && candidateName.endsWith(".class") && candidateName.indexOf('$') == -1) {
                String pluginName = candidateName.substring(0, candidateName.lastIndexOf('.'));
                pluginNames.add(pluginName);
            }
        }
        return pluginNames;
    }

    /**
     * Installs the plugins
     * @param pluginPackageName package of the plugins
     * @param pluginNames simple class names
     * @throws ClassNotFoundException if specified class could not be found
     * @throws InstantiationException if plugin-class could not be instantiated
     * @throws IllegalAccessException if there was a problem with the accessibility of the plugin's methods or properties
     */
    private void installPlugins(String pluginPackageName, ArrayList<String> pluginNames) throws ClassNotFoundException,
            InstantiationException, IllegalAccessException {
        Collections.sort(pluginNames);
        ClassLoader loader = getThisClassLoader();
        for (String pluginName : pluginNames) {
            String pluginFullClassName = pluginPackageName + "." + pluginName;
            Class<?> pluginClass = Class.forName(pluginFullClassName, true, loader);
            if (!pluginClass.isInterface() && MacroPlugin.class.isAssignableFrom(pluginClass)) {
                MacroPlugin plugin = (MacroPlugin) pluginClass.newInstance();
                if (plugin.getMacros().length > 0) {
                    availablePlugins.add(plugin);
                    LOGGER.info("Plugin installed: name=" + plugin.getName() + ",  version=" + plugin.getVersion()
                            + ", vendor=" + plugin.getVendor());
                }
                else {
                    LOGGER.warning("Plugin skipped (no makros): name=" + plugin.getName() + ",  version="
                            + plugin.getVersion() + ", vendor=" + plugin.getVendor());
                }
            }
        }
    }

    /**
     * Returns the class loader that loaded <i>this</i> class
     * @return class loader
     */
    private ClassLoader getThisClassLoader() {
        ClassLoader loader = this.getClass().getClassLoader();
        if (loader == null) {
            loader = ClassLoader.getSystemClassLoader();
        }
        return loader;
    }

    /**
     * This method derives the plugin-folder
     * @return folder to look for plugins
     */
    private File determinePluginFolder() {
        ClassLoader loader = getThisClassLoader();
        URL url = loader.getResource(this.getClass().getName().replace('.', '/') + ".class");
        if (url == null) {
            throw new RuntimeException("Unable to find plugin path.");
        }

        File classFile = new File(url.getFile());

        File pluginFolder = new File(classFile.getParent(), "ext");

        if (!pluginFolder.exists()) {
            throw new RuntimeException("Missing plugin-folder: " + pluginFolder);
        }
        return pluginFolder;
    }

}
