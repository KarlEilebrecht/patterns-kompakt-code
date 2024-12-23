//@formatter:off
/*
 * Example Scripting Framework
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

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This minimum framework demonstrates the usage of the PLUGIN pattern.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class ExampleScriptingFramework implements MacroPluginFramework {

    private static final String ERROR_IN_LINE = "Error in line ";

    private static final Logger LOGGER = LoggerFactory.getLogger(ExampleScriptingFramework.class);

    /**
     * simulates some kind of system registry (JNDI or whatever)
     */
    private final Map<String, Object> properties = new ConcurrentHashMap<>();

    /**
     * allows protocol messages from macro
     */
    private final StringBuilder protocol = new StringBuilder();

    @Override
    public Object getProperty(String propertyName) {
        LOGGER.debug("{}.getProperty('{}') called.", this.getClass().getSimpleName(), propertyName);
        return properties.get(propertyName.toLowerCase());
    }

    @Override
    public void setProperty(String propertyName, Object value) {
        LOGGER.debug("{}.setProperty('{}', '{}') called.", this.getClass().getSimpleName(), propertyName, value);
        properties.put(propertyName.toLowerCase(), value);
    }

    @Override
    public boolean isPropertyAvailable(String propertyName) {
        LOGGER.debug("{}.isPropertyAvailable('{}') called.", this.getClass().getSimpleName(), propertyName);
        return properties.containsKey(propertyName.toLowerCase());
    }

    @Override
    public synchronized void addProtocolMessage(String source, String message) {
        LOGGER.debug("{}.addProtocolMessage() called.", this.getClass().getSimpleName());
        protocol.append(source);
        protocol.append(": ");
        protocol.append(message);
        protocol.append('\n');

    }

    /**
     * Returns the protocol.
     * 
     * @return protocol as a string
     */
    public synchronized String getProtocol() {
        return this.protocol.toString();
    }

    /**
     * Executes the script from the given file using the specified factory.
     * 
     * @param scriptFile file with script line by line
     * @param pluginFactory factory for retrieving plugins for macro execution
     */
    public void executeScriptFile(File scriptFile, MacroPluginFactory pluginFactory) {
        LOGGER.debug("{}.executeScriptFile({}) called.", this.getClass().getSimpleName(), scriptFile);
        List<String> lines = null;
        try {
            lines = Files.readAllLines(scriptFile.toPath(), StandardCharsets.ISO_8859_1);
        }
        catch (IOException ex) {
            throw new ExampleScriptingFrameworkException(String.format("Error executing scriptFile=%s", String.valueOf(scriptFile)), ex);
        }
        int i = 0;
        for (String line : lines) {
            line = line.trim();
            if (line.length() > 0 && !line.startsWith("#")) {
                LOGGER.debug("Script line: {}", line);
            }
            else {
                continue;
            }
            executeScriptLine(line, i, pluginFactory);
            i++;
        }
    }

    /**
     * Executes a single line of a script
     * 
     * @param line the instruction, not null empty, no comment
     * @param lineNumber position in script
     * @param pluginFactory factory for retrieving plugins for macro execution
     */
    private void executeScriptLine(String line, int lineNumber, MacroPluginFactory pluginFactory) {

        // find variable name for result assignment
        String resultVarName = null;
        int eqPos = line.indexOf('=');
        if (eqPos > 0) {
            resultVarName = line.substring(0, eqPos).trim();
        }
        else {
            throw new ExampleScriptingFrameworkException(ERROR_IN_LINE + (lineNumber + 1) + ": no result variable (expected x=macro(y,..))");
        }

        Object result = executeMacro(line.substring(eqPos + 1), lineNumber, pluginFactory);

        setProperty(resultVarName, result);

    }

    /**
     * Executes the given macro expression and returns the result
     * 
     * @param expression script line part, expected to be a macro expression <code>macroName(...)</code>
     * @param lineNumber position in script (for debugging)
     * @param pluginFactory factory for retrieving plugins for macro execution
     * @return result of macro execution
     */
    private Object executeMacro(String expression, int lineNumber, MacroPluginFactory pluginFactory) {

        List<String> macroCall = parseMacroCall(expression, lineNumber);
        String macroName = macroCall.get(0);
        Object[] macroArguments = resolveMacroArguments(macroCall.subList(1, macroCall.size()));

        Object result = null;
        try {
            MacroPluginRuntime plugin = pluginFactory.getPluginForMacro(macroName);
            result = plugin.executeMacro(macroName, macroArguments);
        }
        catch (Exception ex) {
            throw new ExampleScriptingFrameworkException("Unexpected Error in line " + (lineNumber + 1), ex);
        }
        return result;
    }

    /**
     * This method parses the macro with its arguments from the given expression
     * 
     * @param expression script line part, expected to be a macro expression <code>macroName(...)</code>
     * @param lineNumber script position for debugging
     * @return list of strings, 0=macro name, 1..n macro argument names
     */
    private List<String> parseMacroCall(String expression, int lineNumber) {
        List<String> res = null;
        String macroName = null;
        String[] argumentNames = new String[0];
        int openPos = expression.indexOf('(');
        int closePos = expression.indexOf(')');
        if (openPos > 0 && closePos > 0 && closePos > openPos) {
            macroName = expression.substring(0, openPos).trim();
            if (macroName.length() == 0) {
                throw new ExampleScriptingFrameworkException(ERROR_IN_LINE + (lineNumber + 1) + ": no expression (expected x=macro(y,..))");
            }
            String argumentString = expression.substring(openPos + 1, closePos).trim();
            if (argumentString.length() > 0) {
                argumentNames = argumentString.split(",");
            }
        }
        else {
            throw new ExampleScriptingFrameworkException(ERROR_IN_LINE + (lineNumber + 1) + ": no expression (expected x=macro(y,..))");
        }
        res = new ArrayList<>(argumentNames.length + 1);
        res.add(macroName);
        res.addAll(Arrays.asList(argumentNames));
        return res;
    }

    /**
     * Resolves the argument values for the given argument names
     * 
     * @param argumentNames may be empty NOT NULL
     * @return array of argument values
     */
    private Object[] resolveMacroArguments(List<String> argumentNames) {
        int numberOfArguments = argumentNames.size();
        Object[] macroArguments = new Object[numberOfArguments];
        for (int i = 0; i < numberOfArguments; i++) {
            String argumentName = argumentNames.get(i).trim();
            Object arg = null;
            if (isPropertyAvailable(argumentName)) {
                arg = getProperty(argumentName);
            }
            if (arg == null && argumentName.length() > 0) {
                if (argumentName.startsWith("\"")) {
                    argumentName = argumentName.substring(1);
                }
                if (argumentName.endsWith("\"")) {
                    argumentName = argumentName.substring(0, argumentName.length() - 1);
                }
                arg = argumentName;
            }
            macroArguments[i] = arg;
        }
        return macroArguments;
    }

}
