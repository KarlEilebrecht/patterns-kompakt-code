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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Delete Text Command - a concrete COMMAND.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class DeleteTextCommand extends InputCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteTextCommand.class);

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
        LOGGER.debug("New {} created to delete {} character(s).", this.getClass().getSimpleName(), size);
        this.numberOfCharactersToBeDeleted = size;
    }

    @Override
    public void execute() {
        LOGGER.debug("Executing {} to delete {} character(s).", this.getClass().getSimpleName(), this.numberOfCharactersToBeDeleted);
        int newLength = receiver.length() - this.numberOfCharactersToBeDeleted;
        removedCharacters = receiver.substring(newLength);
        receiver.setLength(newLength);
    }

    @Override
    public void executeUndo() {
        LOGGER.debug("Undoing {} to undelete {} character(s).", this.getClass().getSimpleName(), this.numberOfCharactersToBeDeleted);
        receiver.append(removedCharacters);
    }

}
