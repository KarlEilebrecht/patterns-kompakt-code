//@formatter:off
/*
 * Example Reusable Object
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
package de.calamanari.pk.objectpool;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.calamanari.pk.util.MiscUtils;

/**
 * Example reusable object for being pooled
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class ExampleReusableObject {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExampleReusableObject.class);

    /**
     * load simulation delay
     */
    private static final long CREATION_DELAY_MILLIS = 4000;

    /**
     * the internal "state" which cannot be shared between threads
     */
    private int intrinsicState = 0;

    /**
     * Creates an object and performs expensive initialization
     */
    public ExampleReusableObject() {
        LOGGER.debug("Creating new ExampleReusableObject ...");

        // expensive setup, such as open files, create connection or
        // compute seeds
        MiscUtils.sleepIgnoreException(CREATION_DELAY_MILLIS);
        LOGGER.debug("New ExampleReusableObject created.");
    }

    /**
     * some operation the reusable object provides.
     * 
     * @return result of operation
     */
    public String computeResult() {
        LOGGER.debug("computeResult called ...");

        intrinsicState++;
        MiscUtils.sleepIgnoreException(1);
        LOGGER.debug("computeResult completed.");
        return "X" + intrinsicState;
    }

    /**
     * clears the state of the pooled instance
     */
    public void reset() {
        LOGGER.debug("reset called ...");
        this.intrinsicState = 0;
        LOGGER.debug("reset completed.");
    }
}
