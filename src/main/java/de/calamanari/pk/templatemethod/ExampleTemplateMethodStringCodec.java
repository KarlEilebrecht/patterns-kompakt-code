//@formatter:off
/*
 * Example Template Method String Codec
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

import de.calamanari.pk.util.SimpleScrambleCodec;

/**
 * Example Template Method String Codec - concrete Codec, demonstrates TEMPLATE METHOD pattern
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class ExampleTemplateMethodStringCodec extends AbstractTemplateMethodStringCodec {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExampleTemplateMethodStringCodec.class);

    /**
     * Prefix for decision whether a String is encoded or not
     */
    public static final String CODE_PREFIX = "*SCR/777+";

    @Override
    public String encode(String text) {
        LOGGER.debug("Encoding '{}'", text);
        return CODE_PREFIX + SimpleScrambleCodec.encode(text);
    }

    @Override
    public String decode(String text) {
        LOGGER.debug("Decoding '{}'", text);
        return SimpleScrambleCodec.decode(text.substring(CODE_PREFIX.length()));
    }

    @Override
    public boolean checkValid(String text) {
        LOGGER.debug("Checking whether '{}' is encoded", text);
        return (text != null && text.startsWith(CODE_PREFIX));
    }

}
