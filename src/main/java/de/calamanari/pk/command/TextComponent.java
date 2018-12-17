//@formatter:off
/*
 * Text Component - the RECEIVER in this COMMAND example.
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
 * Text Component - the RECEIVER in this COMMAND example.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class TextComponent {

    private static final Logger LOGGER = LoggerFactory.getLogger(DeleteTextCommand.class);

    /**
     * To keep example simple we just wrap a string builder
     */
    private final StringBuilder stringBuilder = new StringBuilder();

    /**
     * Returns current size of text, number of characters
     * 
     * @return length
     */
    public int length() {
        return stringBuilder.length();
    }

    /**
     * Sets the new length of the internal buffer
     * 
     * @param newLength length to be set
     */
    public void setLength(int newLength) {
        LOGGER.debug("{}.setLength({}) called.", this.getClass().getSimpleName(), newLength);
        stringBuilder.setLength(newLength);
    }

    /**
     * appends the given string to the internal buffer
     * 
     * @param str string to be appended
     * @return this instance
     */
    public TextComponent append(String str) {
        LOGGER.debug("{}.append('{}') called.", this.getClass().getSimpleName(), str);
        stringBuilder.append(str);
        return this;
    }

    /**
     * Returns a string containing the string copied beginning with the given position
     * 
     * @param start first character to be copied
     * @return string
     */
    public String substring(int start) {
        return stringBuilder.substring(start);
    }

    @Override
    public String toString() {
        return stringBuilder.toString();
    }

}
