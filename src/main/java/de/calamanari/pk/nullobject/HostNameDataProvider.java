//@formatter:off
/*
 * Host Name Data Provider - supplementary class in this example
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
package de.calamanari.pk.nullobject;

import java.util.Arrays;
import java.util.logging.Logger;

/**
 * Host Name Data Provider - supplementary class in this example, uses NULL OBJECT to tell a caller about missing HostNameData instances
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public final class HostNameDataProvider {

    /**
     * logger
     */
    protected static final Logger LOGGER = Logger.getLogger(HostNameDataProvider.class.getName());

    /**
     * for this key we have something to return
     */
    public static final String EXISTING_DATA_KEY = "existing";

    /**
     * Data to be returned by the provider
     */
    private static final HostNameData EXISTING_DATA = new ConcreteHostNameData("TEST", Arrays.asList(new String[] { "XENOS", "TREADSTONE", "ANDROMEDA",
            "GALAXY1" }));

    /**
     * Utility class
     */
    private HostNameDataProvider() {
        // no instances
    }

    /**
     * Client can retrieve the data by key<br>
     * A NULL OBJECT will be returned to indicate that there was no entry for the given key
     * 
     * @param key identifier
     * @return HostNameData instance or {@link HostNameDataNullObject#INSTANCE}, never null
     */
    public static final HostNameData getHostNameData(String key) {
        LOGGER.fine(HostNameDataProvider.class.getSimpleName() + ".getHostNameData('" + key + "') called.");
        if (EXISTING_DATA_KEY.equals(key)) {
            LOGGER.fine("Known key -> return corresponding instance");
            return EXISTING_DATA;
        }
        LOGGER.fine("Unknown key -> return NULL OBJECT");
        return HostNameDataNullObject.INSTANCE;
    }

}
