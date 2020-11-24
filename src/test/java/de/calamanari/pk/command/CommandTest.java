//@formatter:off
/*
 * Command Test - demonstrates COMMAND pattern.
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
package de.calamanari.pk.command;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;
import java.util.Stack;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.calamanari.pk.util.TimeUtils;

/**
 * Command Test - demonstrates COMMAND pattern.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class CommandTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(CommandTest.class);

    /**
     * Stack to put executed commands
     */
    private Stack<InputCommand> undoStack = new Stack<>();

    /**
     * Stack to put undone commands
     */
    private Stack<InputCommand> redoStack = new Stack<>();

    @Test
    public void testCommand() {

        // hint: set the log-level in logback.xml to DEBUG to watch COMMAND working.

        LOGGER.info("Test Commmand ...");
        long startTimeNanos = System.nanoTime();

        TextComponent textComponent = new TextComponent();

        List<InputCommand> commandList = Arrays.asList(new InputCommand[] {

                new AppendTextCommand(textComponent, "The"), new AppendTextCommand(textComponent, "quick"), new DeleteTextCommand(textComponent, 1),
                new DeleteTextCommand(textComponent, 1), new DeleteTextCommand(textComponent, 1), new DeleteTextCommand(textComponent, 1),
                new DeleteTextCommand(textComponent, 1), new AppendTextCommand(textComponent, " "), new AppendTextCommand(textComponent, "quick"),
                new AppendTextCommand(textComponent, " "), new AppendTextCommand(textComponent, "brown"), new AppendTextCommand(textComponent, " "),
                new AppendTextCommand(textComponent, "d"), new DeleteTextCommand(textComponent, 1), new AppendTextCommand(textComponent, "f"),
                new AppendTextCommand(textComponent, "o"), new AppendTextCommand(textComponent, "x"), new AppendTextCommand(textComponent, " "),
                new AppendTextCommand(textComponent, "jumped over the lazy duck."), });

        for (InputCommand ic : commandList) {
            ic.execute();
            undoStack.push(ic);
        }

        assertEquals("The quick brown fox jumped over the lazy duck.", textComponent.toString());

        while (!undoStack.empty()) {
            InputCommand ic = undoStack.pop();
            ic.executeUndo();
            redoStack.push(ic);
        }

        assertEquals("", textComponent.toString());

        while (!redoStack.empty()) {
            InputCommand ic = redoStack.pop();
            ic.execute();
            undoStack.push(ic);
        }

        assertEquals("The quick brown fox jumped over the lazy duck.", textComponent.toString());

        LOGGER.info("Test Commmand successful! Elapsed time: " + TimeUtils.formatNanosAsSeconds(System.nanoTime() - startTimeNanos) + " s");
    }

}
