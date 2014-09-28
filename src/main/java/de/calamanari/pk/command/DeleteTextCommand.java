//@formatter:off
/*
 * Delete Text Command - a concrete COMMAND.
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

import java.util.logging.Logger;

/**
 * Delete Text Command - a concrete COMMAND.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class DeleteTextCommand extends InputCommand {

    /**
     * logger
     */
    protected static final Logger LOGGER = Logger.getLogger(DeleteTextCommand.class.getName());

    /**
     * Number of characters to be deleted
     */
    private final int numberOfCharactersToBeDeleted;

    /**
     * for undoing we store the characters that were deleted.
     */
    private String removedCharacters = null;

    /**
     * Creates new deletion command.
     * 
     * @param receiver command destination
     * @param size number of characters to be deleted.
     */
    public DeleteTextCommand(TextComponent receiver, int size) {
        super(receiver);
        LOGGER.fine("New " + this.getClass().getSimpleName() + " created to delete " + size + " character(s).");
        this.numberOfCharactersToBeDeleted = size;
    }

    @Override
    public void execute() {
        LOGGER.fine("Executing " + this.getClass().getSimpleName() + " to delete " + this.numberOfCharactersToBeDeleted + " character(s).");
        int newLength = receiver.length() - this.numberOfCharactersToBeDeleted;
        removedCharacters = receiver.substring(newLength);
        receiver.setLength(newLength);
    }

    @Override
    public void executeUndo() {
        LOGGER.fine("Undoing " + this.getClass().getSimpleName() + " to undelete " + this.numberOfCharactersToBeDeleted + " character(s).");
        receiver.append(removedCharacters);
    }

}
