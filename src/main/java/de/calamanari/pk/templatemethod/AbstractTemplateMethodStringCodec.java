//@formatter:off
/*
 * Abstract Template Method String Codec - demonstrates TEMPLATE METHOD pattern
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
package de.calamanari.pk.templatemethod;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Abstract Template Method String Codec<br>
 * This demonstrates TEMPLATE METHOD pattern
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public abstract class AbstractTemplateMethodStringCodec {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractTemplateMethodStringCodec.class);

    /**
     * This TEMPLATE METHOD processes the string using operations implemented by concrete subclasses.
     * 
     * @param text input text
     * @return output text
     */
    public String processText(String text) {
        String res = null;
        LOGGER.debug("{}: Using subclass operation to encode input!", this.getClass().getSimpleName());
        if (this.checkValid(text)) {
            LOGGER.debug("{}: Using subclass operation to decode input!", this.getClass().getSimpleName());
            res = this.decode(text);
        }
        else {
            LOGGER.debug("{}: Using subclass operation to encode input!", this.getClass().getSimpleName());
            res = this.encode(text);
        }
        return res;
    }

    /**
     * Operation to be implemented by subclasses: Encodes the given text.
     * 
     * @param text raw data
     * @return encoded string
     */
    public abstract String encode(String text);

    /**
     * Operation to be implemented by subclasses: Decodes the given text
     * 
     * @param text encoded string
     * @return decoded string
     */
    public abstract String decode(String text);

    /**
     * Operation to be implemented by subclasses: Checks whether the given string is a properly encoded string.
     * 
     * @param encodedText encoded string
     * @return true if encoded string is valid and could be decoded
     */
    public abstract boolean checkValid(String encodedText);

}
