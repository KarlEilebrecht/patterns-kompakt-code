/*
 * Plugin test demonstrates the PLUGIN pattern.
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
package de.calamanari.pk.plugin.test;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import de.calamanari.pk.plugin.ExampleScriptingFramework;
import de.calamanari.pk.plugin.MacroPluginFactory;
import de.calamanari.pk.plugin.ext.FileMacroPlugin;
import de.calamanari.pk.plugin.ext.StringMacroPlugin;
import de.calamanari.pk.util.LogUtils;
import de.calamanari.pk.util.MiscUtils;

/**
 * Plugin test demonstrates the PLUGIN pattern
 * @author <a href="mailto:Karl.Eilebrecht(a/t)web.de">Karl Eilebrecht</a>
 */
public class PluginTest {

    /**
     * logger
     */
    private static final Logger LOGGER = Logger.getLogger(PluginTest.class.getName());

    /**
     * Log-level for this test
     */
    private static final Level LOG_LEVEL = Level.INFO;

    /**
     * By default the files will be deleted after test, set this to true if you want to take a look into
     */
    private static final boolean KEEP_FILES_AFTER_TEST = false;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        LogUtils.setConsoleHandlerLogLevel(LOG_LEVEL);
        LogUtils.setLogLevel(LOG_LEVEL, PluginTest.class, ExampleScriptingFramework.class, MacroPluginFactory.class,
                FileMacroPlugin.class, StringMacroPlugin.class);
    }

    @AfterClass
    public static void setUpAfterClass() throws Exception {
        if (!KEEP_FILES_AFTER_TEST) {
            File file1 = new File(MiscUtils.getHomeDirectory(), "plugin-test-script.out");
            File file2 = new File(MiscUtils.getHomeDirectory(), "plugin-test-script2.out");
            if (file1.exists()) {
                file1.delete();
            }
            if (file2.exists()) {
                file2.delete();
            }
        }
    }

    @Test
    public void testPlugin() {

        // Hint: set the log-level above to FINE to watch PLUGIN working.

        // Suggestions for refactoring/experiments:
        // - find a way to avoid literals for the macro names in the plugin-classes
        // - what does fit better: constants or enumeration?
        // - why not making macro-names case-INsensitive?
        // - you could resolve available plugin-methods via reflection

        LOGGER.info("Test plugin ...");
        long startTimeNanos = System.nanoTime();
        ExampleScriptingFramework framework = new ExampleScriptingFramework();
        MacroPluginFactory pluginFactory = new MacroPluginFactory(framework);
        framework.executeScriptFile(MiscUtils.findFile("de/calamanari/pk/plugin/test/plugin-test-script.txt"),
                pluginFactory);

        String expectedResult = "The funny frog jumped over the lazy duck.\n.kcud yzal eht revo depmuj gorf ynnuf ehT;"
                + " length=41";
        String result = MiscUtils.readFileToString(new File(MiscUtils.getHomeDirectory(), "plugin-test-script.out"));
        assertEquals(expectedResult, result);

        LOGGER.info("Test plugin successful! Elapsed time: "
                + MiscUtils.formatNanosAsSeconds(System.nanoTime() - startTimeNanos) + " s");
        LOGGER.fine(framework.getProtocol());
    }

}
