/*
 * Append Text Command - a concrete COMMAND.
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
package de.calamanari.pk.command;

import java.util.logging.Logger;

/**
 * Text Command - a concrete COMMAND.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class AppendTextCommand extends InputCommand {

    /**
     * logger
     */
    protected static final Logger LOGGER = Logger.getLogger(AppendTextCommand.class.getName());

    /**
     * characters to be appended by this command
     */
    private final String text;

    /**
     * Creates new append text command for the given text.
     * @param receiver instance to execute the action
     * @param text string to be appended
     */
    public AppendTextCommand(TextComponent receiver, String text) {
        super(receiver);
        LOGGER.fine("New " + this.getClass().getSimpleName() + " created to append '" + text + "'.");
        this.text = text;
    }

    @Override
    public void execute() {
        LOGGER.fine("Executing " + this.getClass().getSimpleName() + " to append '" + text + "'.");
        receiver.append(text);
    }

    @Override
    public void executeUndo() {
        LOGGER.fine("Undoing " + this.getClass().getSimpleName() + " to remove '" + text + "'.");
        receiver.setLength(receiver.length() - text.length());
    }

}
