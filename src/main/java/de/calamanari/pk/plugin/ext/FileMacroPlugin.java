//@formatter:off
/*
 * File macro plugin 
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
package de.calamanari.pk.plugin.ext;

import java.io.File;
import java.nio.charset.Charset;
import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.calamanari.pk.plugin.ExampleScriptingFrameworkException;
import de.calamanari.pk.plugin.MacroPlugin;
import de.calamanari.pk.plugin.MacroPluginFramework;
import de.calamanari.pk.util.FileUtils;

/**
 * File Macro Plugin provides macros for file access.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class FileMacroPlugin implements MacroPlugin {

    private static final Logger LOGGER = LoggerFactory.getLogger(FileMacroPlugin.class);

    /**
     * min number of arguments
     */
    private static final int MIN_ARGS = 2;

    /**
     * max number of arguments
     */
    private static final int MAX_ARGS = 3;

    /**
     * reference to the execution framework
     */
    private MacroPluginFramework frameworkReference = null;

    /**
     * Constructor
     */
    public FileMacroPlugin() {
        LOGGER.debug("{} created.", this.getClass().getSimpleName());
    }

    @Override
    public String getName() {
        LOGGER.debug("{}.getName() called.", this.getClass().getSimpleName());
        return "File Macro Plugin";
    }

    @Override
    public String getVendor() {
        LOGGER.debug("{}.getVendor() called.", this.getClass().getSimpleName());
        return "Crap IT-Systems Inc.";
    }

    @Override
    public String getVersion() {
        LOGGER.debug("{}.getVersion() called.", this.getClass().getSimpleName());
        return "0.5";
    }

    @Override
    public String[] getMacros() {
        LOGGER.debug("{}.getMacros() called.", this.getClass().getSimpleName());
        return new String[] { "writeStringToFile", "readFileToString", "createHomeFile" };
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
        case "writeStringToFile":
            return executeMacroWriteStringToFile(args);
        case "readFileToString":
            return executeMacroReadFileToString(args);
        case "createHomeFile":
            return executeMacroCreateHomeFile(args);
        default:
            throw new ExampleScriptingFrameworkException("Could not execute macro '" + macroName + "' - unknown macro.");
        }
    }

    /**
     * Creates a file of the given name in the home directory
     * 
     * @param args name of the file
     * @return created file reference
     */
    private File executeMacroCreateHomeFile(Object[] args) {
        if (args == null || args.length != 1) {
            throw new ExampleScriptingFrameworkException("Could not execute macro 'createHomeFile' - insufficient arguments.");
        }
        Object arg1 = args[0];
        if (!(arg1 instanceof String)) {
            throw new ExampleScriptingFrameworkException("Could not execute macro 'createHomeFile' - illegal argument (String expected).");
        }
        return new File(FileUtils.getHomeDirectory(), (String) arg1);
    }

    /**
     * Reads the given file to string
     * 
     * @param args 0=file name, optionally 1=charset name
     * @return text from file
     */
    private String executeMacroReadFileToString(Object[] args) {
        if (args == null || args.length < 1 || args.length > 2) {
            throw new ExampleScriptingFrameworkException("Could not execute macro 'readFileToString' - insufficient arguments.");
        }
        Object arg1 = args[0];
        Object arg2 = null;
        if (args.length == 2) {
            arg2 = args[1];
        }
        if (arg1 instanceof String) {
            arg1 = new File((String) arg1);
        }
        if (!(arg1 instanceof File) || (arg2 != null && !(arg2 instanceof String))) {
            throw new ExampleScriptingFrameworkException("Could not execute macro 'readFileToString' - illegal arguments.");
        }
        return FileUtils.readFileToString((File) arg1, (arg2 == null ? null : Charset.forName((String) arg2)));
    }

    /**
     * Executes the writeStringToFile macro
     * 
     * @param args macro arguments: 0=string, 1=file, optionally 2=charset name
     * @return file size in bytes
     */
    private Long executeMacroWriteStringToFile(Object[] args) {
        if (args == null || args.length < MIN_ARGS || args.length > MAX_ARGS) {
            throw new ExampleScriptingFrameworkException("Could not execute macro 'writeStringToFile' - insufficient arguments.");
        }
        Object arg1 = args[0];
        Object arg2 = args[1];
        Object arg3 = null;
        if (args.length == MAX_ARGS) {
            arg3 = args[2];
        }
        if (arg2 instanceof String) {
            arg2 = new File((String) arg2);
        }
        if (!(arg1 instanceof String) || !(arg2 instanceof File) || (arg3 != null && !(arg3 instanceof String))) {
            throw new ExampleScriptingFrameworkException("Could not execute macro 'writeStringToFile' - illegal arguments.");
        }
        return Long.valueOf(FileUtils.writeStringToFile((String) arg1, (File) arg2, (arg3 == null ? null : Charset.forName((String) arg3))));
    }

}
