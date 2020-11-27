//@formatter:off
/*
 * Input Command Processor - a COMMAND PROCESSOR for InputCommands.
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
package de.calamanari.pk.commandprocessor;

import java.util.ArrayDeque;
import java.util.Deque;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.calamanari.pk.command.InputCommand;

/**
 * Input Command Processor - a COMMAND PROCESSOR for InputCommands.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class InputCommandProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(InputCommandProcessor.class);

    /**
     * Stack to put executed commands
     */
    private final Deque<InputCommand> undoStack = new ArrayDeque<>();

    /**
     * Stack to put undone commands
     */
    private final Deque<InputCommand> redoStack = new ArrayDeque<>();

    /**
     * executes the given command
     * 
     * @param command input command to be executed
     */
    public void execute(InputCommand command) {
        LOGGER.debug("{}.execute() called", this.getClass().getSimpleName());
        command.execute();
        this.undoStack.push(command);
    }

    /**
     * Undoes the last command
     * 
     * @return true if an operation was undone
     */
    public boolean undo() {
        LOGGER.debug("{}.undo() called", this.getClass().getSimpleName());
        boolean canUndo = !undoStack.isEmpty();
        if (canUndo) {
            LOGGER.debug("Undoing last command.");
            InputCommand ic = undoStack.pop();
            ic.executeUndo();
            redoStack.push(ic);
        }
        else {
            LOGGER.debug("Nothing to undo.");
        }
        return canUndo;
    }

    /**
     * Re-does the last command that was undone.
     * 
     * @return true if an operation was re-done
     */
    public boolean redo() {
        LOGGER.debug("{}.redo() called", this.getClass().getSimpleName());
        boolean canRedo = !redoStack.isEmpty();
        if (canRedo) {
            LOGGER.debug("Redoing last command.");
            InputCommand ic = redoStack.pop();
            ic.execute();
            undoStack.push(ic);
        }
        else {
            LOGGER.debug("Nothing to redo.");
        }
        return canRedo;
    }

}
