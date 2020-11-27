//@formatter:off
/*
 * Data Manager - supplementary class in IDENTITY FIELD pattern
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
package de.calamanari.pk.identityfield;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Data Manager - supplementary class in IDENTITY FIELD pattern
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class DataManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataManager.class);

    /**
     * mocks the database in this example
     */
    private final List<String[]> database = new ArrayList<>();

    /**
     * Adds entity row for testing
     * 
     * @param pkField1 first primary key field
     * @param pkField2 second primary key field
     * @param x some entity field
     * @param y some entity field
     */
    public void addEntityRow(String pkField1, String pkField2, String x, String y) {
        database.add(new String[] { pkField1, pkField2, x, y });
    }

    /**
     * Finder method to retrieve entity from DB
     * 
     * @param x some value
     * @param y some value
     * @return entity or null if not found
     */
    public Entity findEntityByXY(String x, String y) {
        LOGGER.debug("{}.findByXY('{}', '{}') called ...", this.getClass().getSimpleName(), x, y);
        Entity res = null;
        for (String[] data : database) {
            if (((data[2] == null && x == null) || (data[2] != null && data[2].equals(x)))
                    && ((data[3] == null && y == null) || (data[3] != null && data[3].equals(y)))) {
                LOGGER.debug("found data, creating compound key from '{}' and '{}' ...", data[0], data[1]);
                CompoundKey key = new CompoundKey(data[0], data[1]);
                LOGGER.debug("created: {}", key);
                LOGGER.debug("creating entity with compound key id ...");
                res = new Entity(key, data[2], data[3]);
                LOGGER.debug("Returning entity {}", res);
                break;
            }
        }
        return res;
    }

}
