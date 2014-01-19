/*
 * Example Template Method String Codec
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

import de.calamanari.pk.util.MiscUtils;

/**
 * Example Template Method String Codec - concrete Codec, demonstrates TEMPLATE METHOD pattern
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class ExampleTemplateMethodStringCodec extends AbstractTemplateMethodStringCodec {

    /**
     * logger
     */
    private static final Logger LOGGER = Logger.getLogger(ExampleTemplateMethodStringCodec.class.getName());

    /**
     * Prefix for decision whether a String is encoded or not
     */
    public static final String CODE_PREFIX = "*SCR/777+";

    @Override
    public String encode(String text) {
        LOGGER.fine("Encoding '" + text + "'");
        return CODE_PREFIX + MiscUtils.scramble(text);
    }

    @Override
    public String decode(String text) {
        LOGGER.fine("Decoding '" + text + "'");
        return MiscUtils.unscramble(text.substring(CODE_PREFIX.length()));
    }

    @Override
    public boolean checkValid(String text) {
        LOGGER.fine("Checking whether '" + text + "' is encoded");
        return (text != null && text.startsWith(CODE_PREFIX));
    }

}
