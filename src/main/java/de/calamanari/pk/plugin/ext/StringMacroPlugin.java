//@formatter:off
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
//@formatter:on
package de.calamanari.pk.plugin.ext;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.calamanari.pk.plugin.MacroPlugin;
import de.calamanari.pk.plugin.MacroPluginFramework;

/**
 * String macro plugin allows string manipulation
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class StringMacroPlugin implements MacroPlugin {

    private static final Logger LOGGER = LoggerFactory.getLogger(StringMacroPlugin.class);

    /**
     * reference to the execution framework
     */
    private MacroPluginFramework frameworkReference = null;

    /**
     * Constructor
     */
    public StringMacroPlugin() {
        LOGGER.debug("{} created.", this.getClass().getSimpleName());
    }

    @Override
    public String getName() {
        LOGGER.debug("{}.getName() called.", this.getClass().getSimpleName());
        return "String Macro Plugin";
    }

    @Override
    public String getVendor() {
        LOGGER.debug("{}.getVendor() called.", this.getClass().getSimpleName());
        return "Crap IT-Systems Inc.";
    }

    @Override
    public String getVersion() {
        LOGGER.debug("{}.getVersion() called.", this.getClass().getSimpleName());
        return "0.2";
    }

    @Override
    public String[] getMacros() {
        LOGGER.debug("{}.getMacros() called.", this.getClass().getSimpleName());
        return new String[] { "concat", "length", "reverse", "newString" };
    }

    @Override
    public void setFrameworkReference(MacroPluginFramework frameworkReference) {
        LOGGER.debug("{}.setFrameworkReference() called.", this.getClass().getSimpleName());
        this.frameworkReference = frameworkReference;
    }

    @Override
    public Object executeMacro(String macroName, Object[] args) {
        LOGGER.debug("{}.executeMacro('{}', ...) called.", this.getClass().getSimpleName(), macroName);
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
     * 
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
     * 
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
     * 
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
     * 
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
