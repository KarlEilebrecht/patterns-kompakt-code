//@formatter:off
/*
 * Plugin test demonstrates the PLUGIN pattern.
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

import static org.junit.Assert.assertEquals;

import java.io.File;

import org.junit.AfterClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.calamanari.pk.util.FileUtils;
import de.calamanari.pk.util.TimeUtils;

/**
 * Plugin test demonstrates the PLUGIN pattern
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class PluginTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(PluginTest.class);

    /**
     * By default the files will be deleted after test, set this to true if you want to take a look into
     */
    private static final boolean KEEP_FILES_AFTER_TEST = false;

    @AfterClass
    public static void setUpAfterClass() throws Exception {
        if (!KEEP_FILES_AFTER_TEST) {
            File file1 = new File(FileUtils.getHomeDirectory(), "plugin-test-script.out");
            File file2 = new File(FileUtils.getHomeDirectory(), "plugin-test-script2.out");
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

        // Hint: set the log-level in logback.xml to DEBUG to watch PLUGIN working.

        // Suggestions for refactoring/experiments:
        // - find a way to avoid literals for the macro names in the plugin-classes
        // - what does fit better: constants or enumeration?
        // - why not making macro-names case-INsensitive?
        // - you could resolve available plugin-methods via reflection

        LOGGER.info("Test plugin ...");
        long startTimeNanos = System.nanoTime();
        ExampleScriptingFramework framework = new ExampleScriptingFramework();
        MacroPluginFactory pluginFactory = new MacroPluginFactory(framework);
        framework.executeScriptFile(FileUtils.findFile("de/calamanari/pk/plugin/plugin-test-script.txt"), pluginFactory);

        String expectedResult = "The funny frog jumped over the lazy duck.\n.kcud yzal eht revo depmuj gorf ynnuf ehT;" + " length=41";
        String result = FileUtils.readFileToString(new File(FileUtils.getHomeDirectory(), "plugin-test-script.out"));
        assertEquals(expectedResult, result);

        String elapsedTimeString = TimeUtils.formatNanosAsSeconds(System.nanoTime() - startTimeNanos);
        LOGGER.info("Test plugin successful! Elapsed time: {} s", elapsedTimeString);
        LOGGER.debug(framework.getProtocol());
    }

}
