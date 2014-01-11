/*
 * Input Command Processor - a COMMAND PROCESSOR for InputCommands.
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
package de.calamanari.pk.commandprocessor;

import java.util.Stack;
import java.util.logging.Logger;

import de.calamanari.pk.command.InputCommand;

/**
 * Input Command Processor - a COMMAND PROCESSOR for InputCommands.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class InputCommandProcessor {

    /**
     * logger
     */
    protected static final Logger LOGGER = Logger.getLogger(InputCommandProcessor.class.getName());

    /**
     * Stack to put executed commands
     */
    private final Stack<InputCommand> undoStack = new Stack<>();

    /**
     * Stack to put undone commands
     */
    private final Stack<InputCommand> redoStack = new Stack<>();

    /**
     * executes the given command
     * @param command input command to be executed
     */
    public void execute(InputCommand command) {
        LOGGER.fine(this.getClass().getSimpleName() + ".execute() called");
        command.execute();
        this.undoStack.push(command);
    }

    /**
     * Undoes the last command
     * @return true if an operation was undone
     */
    public boolean undo() {
        LOGGER.fine(this.getClass().getSimpleName() + ".undo() called");
        boolean canUndo = !undoStack.isEmpty();
        if (canUndo) {
            LOGGER.fine("Undoing last command.");
            InputCommand ic = undoStack.pop();
            ic.executeUndo();
            redoStack.push(ic);
        }
        else {
            LOGGER.fine("Nothing to undo.");
        }
        return canUndo;
    }

    /**
     * Re-does the last command that was undone.
     * @return true if an operation was re-done
     */
    public boolean redo() {
        LOGGER.fine(this.getClass().getSimpleName() + ".redo() called");
        boolean canRedo = !redoStack.isEmpty();
        if (canRedo) {
            LOGGER.fine("Redoing last command.");
            InputCommand ic = redoStack.pop();
            ic.execute();
            undoStack.push(ic);
        }
        else {
            LOGGER.fine("Nothing to redo.");
        }
        return canRedo;
    }

}
