/*
 * String macro plugin 
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
package de.calamanari.pk.plugin.ext;

import java.util.Arrays;
import java.util.logging.Logger;

import de.calamanari.pk.plugin.MacroPlugin;
import de.calamanari.pk.plugin.MacroPluginFramework;

/**
 * String macro plugin allows string manipulation
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class StringMacroPlugin implements MacroPlugin {

    /**
     * logger
     */
    public static final Logger LOGGER = Logger.getLogger(StringMacroPlugin.class.getName());

    /**
     * reference to the execution framework
     */
    private MacroPluginFramework frameworkReference = null;

    /**
     * Constructor
     */
    public StringMacroPlugin() {
        LOGGER.fine(this.getClass().getSimpleName() + " created.");
    }

    @Override
    public String getName() {
        LOGGER.fine(this.getClass().getSimpleName() + ".getName() called.");
        return "String Macro Plugin";
    }

    @Override
    public String getVendor() {
        LOGGER.fine(this.getClass().getSimpleName() + ".getVendor() called.");
        return "Crap IT-Systems Inc.";
    }

    @Override
    public String getVersion() {
        LOGGER.fine(this.getClass().getSimpleName() + ".getVersion() called.");
        return "0.2";
    }

    @Override
    public String[] getMacros() {
        LOGGER.fine(this.getClass().getSimpleName() + ".getMacros() called.");
        return new String[] { "concat", "length", "reverse", "newString" };
    }

    @Override
    public void setFrameworkReference(MacroPluginFramework frameworkReference) {
        LOGGER.fine(this.getClass().getSimpleName() + ".setFrameworkReference() called.");
        this.frameworkReference = frameworkReference;
    }

    @Override
    public Object executeMacro(String macroName, Object[] args) {
        LOGGER.fine(this.getClass().getSimpleName() + ".executeMacro('" + macroName + "', ...) called.");
        if (args == null) {
            frameworkReference.addProtocolMessage(this.getName() + "." + macroName, "args=null");
        }
        else {
            frameworkReference.addProtocolMessage(this.getName() + "." + macroName, "args=" + Arrays.asList(args));
        }

        switch (macroName) {
            case "concat":
                return executeMacroConcat(args);
            case "length":
                return executeMacroLength(args);
            case "reverse":
                return executeMacroReverse(args);
            case "newString":
                return executeMacroNewString(args);
            default:
                throw new RuntimeException("Could not execute macro '" + macroName + "' - unknown macro.");
        }
    }

    /**
     * Creates a new string instance from a given string
     * @param args single string or {@linkplain CharSequence}, not null
     * @return string (either the argument itself or a copy as string)
     */
    private String executeMacroNewString(Object[] args) {
        String res = null;
        if (args == null || args.length != 1) {
            throw new RuntimeException("Could not execute macro 'newString' - insufficient arguments.");
        }
        Object arg1 = args[0];
        if (arg1 instanceof CharSequence) {
            res = ((CharSequence) arg1).toString();
        }
        else {
            throw new RuntimeException("Could not execute macro 'newString' - illegal argument (String expected).");
        }
        return res;
    }

    /**
     * Returns a reversed copy of the given string
     * @param args single string argument, which may be null
     * @return reversed string or null if given argument was null
     */
    private String executeMacroReverse(Object[] args) {
        String res = null;
        if (args == null || args.length != 1) {
            throw new RuntimeException("Could not execute macro 'reverse' - insufficient arguments.");
        }
        Object arg1 = args[0];
        if (arg1 != null) {
            if (!(arg1 instanceof String)) {
                throw new RuntimeException("Could not execute macro 'reverse' - illegal argument (String expected).");
            }
            StringBuilder sb = new StringBuilder((String) arg1);
            res = sb.reverse().toString();
        }
        return res;
    }

    /**
     * Computes the length of a string
     * @param args a string
     * @return length of the string
     */
    private Integer executeMacroLength(Object[] args) {
        if (args == null || args.length != 1) {
            throw new RuntimeException("Could not execute macro 'length' - insufficient arguments.");
        }
        Object arg1 = args[0];
        if (!(arg1 instanceof String)) {
            throw new RuntimeException("Could not execute macro 'length' - illegal argument (String expected).");
        }
        return Integer.valueOf(((String) arg1).length());
    }

    /**
     * Concatenates the given arguments
     * @param args at least two arguments
     * @return concatenated string
     */
    private String executeMacroConcat(Object[] args) {
        if (args == null || args.length < 2) {
            throw new RuntimeException("Could not execute macro 'concat' - insufficient arguments.");
        }
        StringBuilder sb = new StringBuilder();
        for (Object arg : args) {
            if ("\\n".equals(arg)) {
                sb.append("\n");
            }
            else {
                sb.append(arg);
            }
        }
        return sb.toString();
    }

}
