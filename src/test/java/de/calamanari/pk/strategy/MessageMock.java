/*
 * Message Mock
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
package de.calamanari.pk.strategy;

import java.util.logging.Logger;

import de.calamanari.pk.strategy.HashStrategy;

/**
 * Message mock supports testing the hash STRATEGY.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class MessageMock {

    /**
     * logger
     */
    protected static final Logger LOGGER = Logger.getLogger(MessageMock.class.getName());

    /**
     * Name of hash strategy (KEY)
     */
    private final String hashMethodName;

    /**
     * Value of hash
     */
    private final String messageHash;

    /**
     * The message text
     */
    private final String text;

    /**
     * Creates new message using the given text and the specified hash strategy
     * @param text raw information
     * @param hashStrategy hash method to be applied
     */
    public MessageMock(String text, HashStrategy hashStrategy) {
        this.text = text;
        this.hashMethodName = hashStrategy.getName();
        LOGGER.fine("Message mock uses hash strategy ... ");
        this.messageHash = hashStrategy.computeHash(text);
        LOGGER.fine("Hash computed: " + this.messageHash);
    }

    /**
     * Returns the name (key) of the used hash strategy
     * @return strategy name
     */
    public String getHashMethodName() {
        return hashMethodName;
    }

    /**
     * Returns the hash value
     * @return hash value
     */
    public String getMessageHash() {
        return messageHash;
    }

    /**
     * Validates the message using the given hash method
     * @param hashStrategy hash method to be applied
     * @return true if hash matches otherwise false
     */
    public boolean validate(HashStrategy hashStrategy) {
        LOGGER.fine("Message mock uses hash strategy ... ");
        String hash = hashStrategy.computeHash(this.getText());
        LOGGER.fine("Hash computed: " + hash);
        return hash.equals(this.getMessageHash());
    }

    /**
     * Returns the message string itself
     * @return text
     */
    public String getText() {
        return text;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "({hashMethodName='" + this.hashMethodName + "', messageHash='"
                + this.messageHash + "', text='" + this.text + "'})";
    }

}
