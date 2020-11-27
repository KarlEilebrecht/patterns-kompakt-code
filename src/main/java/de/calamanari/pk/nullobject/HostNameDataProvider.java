//@formatter:off
/*
 * Host Name Data Provider - supplementary class in this example
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
package de.calamanari.pk.nullobject;

import java.util.Arrays;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Host Name Data Provider - supplementary class in this example, uses NULL OBJECT to tell a caller about missing HostNameData instances
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public final class HostNameDataProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(HostNameDataProvider.class);

    /**
     * for this key we have something to return
     */
    public static final String EXISTING_DATA_KEY = "existing";

    /**
     * Data to be returned by the provider
     */
    private static final HostNameData EXISTING_DATA = new ConcreteHostNameData("TEST", Arrays.asList("XENOS", "TREADSTONE", "ANDROMEDA", "GALAXY1"));

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
        LOGGER.debug("{}.getHostNameData('{}') called.", HostNameDataProvider.class.getSimpleName(), key);
        if (EXISTING_DATA_KEY.equals(key)) {
            LOGGER.debug("Known key -> return corresponding instance");
            return EXISTING_DATA;
        }
        LOGGER.debug("Unknown key -> return NULL OBJECT");
        return HostNameDataNullObject.INSTANCE;
    }

}
