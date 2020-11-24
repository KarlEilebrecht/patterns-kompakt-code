//@formatter:off
/*
 * Crc32 Hash Strategy - concrete hash STRATEGY using CRC32.
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
package de.calamanari.pk.strategy;

import java.nio.charset.StandardCharsets;
import java.util.zip.CRC32;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Crc32 Hash Strategy - concrete hash STRATEGY using CRC32.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class Crc32HashStrategy extends HashStrategy {

    private static final Logger LOGGER = LoggerFactory.getLogger(Crc32HashStrategy.class);

    /**
     * name (key) of this strategy
     */
    public static final String STRATEGY_NAME = "CRC32";

    /**
     * Creates new hash strategy instance
     */
    public Crc32HashStrategy() {
        super(STRATEGY_NAME);
    }

    @Override
    public String computeHash(String text) {
        LOGGER.debug("Concrete Strategy {} computes hash ...", STRATEGY_NAME);
        CRC32 crc32 = new CRC32();
        crc32.update(text.getBytes(StandardCharsets.UTF_8));
        return Long.toHexString(crc32.getValue());
    }

}
