/*
 * Command Processor Test - demonstrates COMMAND PROCESSOR pattern.
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
package de.calamanari.pk.commandprocessor.test;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.BeforeClass;
import org.junit.Test;

import de.calamanari.pk.command.AppendTextCommand;
import de.calamanari.pk.command.DeleteTextCommand;
import de.calamanari.pk.command.InputCommand;
import de.calamanari.pk.command.TextComponent;
import de.calamanari.pk.commandprocessor.InputCommandProcessor;
import de.calamanari.pk.util.LogUtils;
import de.calamanari.pk.util.MiscUtils;

/**
 * Command Processor Test - demonstrates COMMAND PROCESSOR pattern.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)web.de">Karl Eilebrecht</a>
 */
public class CommandProcessorTest {

    /**
     * logger
     */
    protected static final Logger LOGGER = Logger.getLogger(CommandProcessorTest.class.getName());

    /**
     * Log-level for this test
     */
    private static final Level LOG_LEVEL = Level.INFO;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        LogUtils.setConsoleHandlerLogLevel(LOG_LEVEL);
        LogUtils.setLogLevel(LOG_LEVEL, CommandProcessorTest.class, InputCommandProcessor.class,
                AppendTextCommand.class, DeleteTextCommand.class);
    }

    @Test
    public void testCommandProcessor() {

        // hint: set the log-level above to FINE to watch COMMAND PROCESSOR working.

        LOGGER.info("Test Commmand Processor ...");
        long startTimeNanos = System.nanoTime();

        TextComponent textComponent = new TextComponent();

        InputCommandProcessor inputCommandProcessor = new InputCommandProcessor();

        List<InputCommand> commandList = Arrays.asList(new InputCommand[] {

        new AppendTextCommand(textComponent, "The"), new AppendTextCommand(textComponent, "quick"),
                new DeleteTextCommand(textComponent, 1), new DeleteTextCommand(textComponent, 1),
                new DeleteTextCommand(textComponent, 1), new DeleteTextCommand(textComponent, 1),
                new DeleteTextCommand(textComponent, 1), new AppendTextCommand(textComponent, " "),
                new AppendTextCommand(textComponent, "quick"), new AppendTextCommand(textComponent, " "),
                new AppendTextCommand(textComponent, "brown"), new AppendTextCommand(textComponent, " "),
                new AppendTextCommand(textComponent, "d"), new DeleteTextCommand(textComponent, 1),
                new AppendTextCommand(textComponent, "f"), new AppendTextCommand(textComponent, "o"),
                new AppendTextCommand(textComponent, "x"), new AppendTextCommand(textComponent, " "),
                new AppendTextCommand(textComponent, "jumped over the lazy frog"),
                new DeleteTextCommand(textComponent, 4), new AppendTextCommand(textComponent, "duck"),
                new DeleteTextCommand(textComponent, 1), new DeleteTextCommand(textComponent, 1),
                new DeleteTextCommand(textComponent, 1), new AppendTextCommand(textComponent, "og."), });

        for (InputCommand ic : commandList) {
            inputCommandProcessor.execute(ic);
        }

        assertEquals("The quick brown fox jumped over the lazy dog.", textComponent.toString());

        boolean res = false;
        do {
            res = inputCommandProcessor.undo();
        } while (res);

        assertEquals("", textComponent.toString());

        do {
            res = inputCommandProcessor.redo();
        } while (res);

        assertEquals("The quick brown fox jumped over the lazy dog.", textComponent.toString());

        LOGGER.info("Test Commmand Processor successful! Elapsed time: "
                + MiscUtils.formatNanosAsSeconds(System.nanoTime() - startTimeNanos) + " s");
    }

}