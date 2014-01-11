/*
 * Abstract Template Method String Codec - demonstrates TEMPLATE METHOD pattern
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
package de.calamanari.pk.templatemethod;

import java.util.logging.Logger;

/**
 * Abstract Template Method String Codec<br>
 * This demonstrates TEMPLATE METHOD pattern
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public abstract class AbstractTemplateMethodStringCodec {

    /**
     * logger
     */
    private static final Logger LOGGER = Logger.getLogger(AbstractTemplateMethodStringCodec.class.getName());

    /**
     * This TEMPLATE METHOD processes the string using operations implemented by concrete subclasses.
     * @param text input text
     * @return output text
     */
    public String processText(String text) {
        String res = null;
        LOGGER.fine("Using subclass operation to encode input!");
        if (this.checkValid(text)) {
            LOGGER.fine("Using subclass operation to decode input!");
            res = this.decode(text);
        }
        else {
            LOGGER.fine("Using subclass operation to encode input!");
            res = this.encode(text);
        }
        return res;
    }

    /**
     * Operation to be implemented by subclasses: Encodes the given text.
     * @param text raw data
     * @return encoded string
     */
    public abstract String encode(String text);

    /**
     * Operation to be implemented by subclasses: Decodes the given text
     * @param text encoded string
     * @return decoded string
     */
    public abstract String decode(String text);

    /**
     * Operation to be implemented by subclasses: Checks whether the given string is a properly encoded string.
     * @param encodedText encoded string
     * @return true if encoded string is valid and could be decoded
     */
    public abstract boolean checkValid(String encodedText);

}
