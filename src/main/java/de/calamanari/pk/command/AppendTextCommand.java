//@formatter:off
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
//@formatter:on
package de.calamanari.pk.command;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Text Command - a concrete COMMAND.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class AppendTextCommand extends InputCommand {

    private static final Logger LOGGER = LoggerFactory.getLogger(AppendTextCommand.class);

    /**
     * characters to be appended by this command
     */
    private final String text;

    /**
     * Creates new append text command for the given text.
     * 
     * @param receiver instance to execute the action
     * @param text string to be appended
     */
    public AppendTextCommand(TextComponent receiver, String text) {
        super(receiver);
        LOGGER.debug("New {} created to append '{}'.", this.getClass().getSimpleName(), text);
        this.text = text;
    }

    @Override
    public void execute() {
        LOGGER.debug("Executing {} to append '{}'.", this.getClass().getSimpleName(), text);
        receiver.append(text);
    }

    @Override
    public void executeUndo() {
        LOGGER.debug("Undoing {} to remove '{}'.", this.getClass().getSimpleName(), text);
        receiver.setLength(receiver.length() - text.length());
    }

}
