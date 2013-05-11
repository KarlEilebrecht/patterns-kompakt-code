/*
 * Checksum - the PRODUCT to be build in this BUILDER pattern demonstration.
 * Code-Beispiel zum Buch Patterns Kompakt, Verlag Springer Vieweg
 * Copyright 2013 Karl Eilebrecht
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
package de.calamanari.pk.builder;

import java.util.logging.Logger;

/**
 * Checksum - the PRODUCT to be built in this BUILDER pattern demonstration.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)web.de">Karl Eilebrecht</a>
 */
public class Checksum {

    /**
     * logger
     */
    protected static final Logger LOGGER = Logger.getLogger(Checksum.class.getName());

    /**
     * Type of checksum
     */
    private final String type;

    /**
     * value of checksum
     */
    private final long value;

    /**
     * Creates a new checksum instance.
     * @param type technical name of method
     * @param value the result value of checksum algorithm
     */
    public Checksum(String type, long value) {
        LOGGER.fine("new Checksum('" + type + "', " + value + ")");
        this.type = type;
        this.value = value;
    }

    /**
     * Returns technical name of checksum algorithm
     * @return type of checksum
     */
    public String getType() {
        return type;
    }

    /**
     * returns the computed checkum value
     * @return value
     */
    public long getValue() {
        return value;
    }

}
