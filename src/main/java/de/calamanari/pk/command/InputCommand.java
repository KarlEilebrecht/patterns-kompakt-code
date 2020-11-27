//@formatter:off
/*
 * Input Command - to be extended by concrete COMMANDs.
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
package de.calamanari.pk.command;

/**
 * Input Command - to be extended by concrete COMMANDs.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public abstract class InputCommand {

    /**
     * The component to delegate to
     */
    protected final TextComponent receiver;

    /**
     * Creates new input command
     * 
     * @param receiver the component we delegate to for executing the action
     */
    public InputCommand(TextComponent receiver) {
        this.receiver = receiver;
    }

    /**
     * Executes the command
     */
    public abstract void execute();

    /**
     * Applies the reverse command
     */
    public abstract void executeUndo();

}
