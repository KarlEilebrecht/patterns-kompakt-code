//@formatter:off
/*
 * Identity Field Test - demonstrates compound key in IDENTITY FIELD pattern
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
package de.calamanari.pk.identityfield;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.calamanari.pk.util.TimeUtils;

/**
 * Identity Field Test - demonstrates compound key in IDENTITY FIELD pattern
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class IdentityFieldTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(IdentityFieldTest.class);

    /**
     * number of test runs
     */
    private static final int NUMBER_OF_RUNS = 1000;

    /**
     * data manager for this example
     */
    private DataManager dataManager = null;

    @Before
    public void setUp() {
        this.dataManager = new DataManager();
        dataManager.addEntityRow("47", "11", "Marty", "McFly");
        dataManager.addEntityRow("47", "99", "Ernie", "Bert");
    }

    @Test
    public void testIdentityField() {

        // Hint: adjust the log-level in logback.xml to DEBUG to see IDENTITY FIELD working

        LOGGER.info("Test identity field ...");
        long startTimeNanos = System.nanoTime();
        Entity e4711 = dataManager.findEntityByXY("Marty", "McFly");
        Entity e4799 = dataManager.findEntityByXY("Ernie", "Bert");
        assertNotEquals(e4711, e4799);
        assertEquals("Entity({id=CompoundKey([47, 11]), x='Marty', y='McFly'})", e4711.toString());
        assertEquals("Entity({id=CompoundKey([47, 99]), x='Ernie', y='Bert'})", e4799.toString());
        String elapsedTimeString = TimeUtils.formatNanosAsSeconds(System.nanoTime() - startTimeNanos);
        LOGGER.info("Test identity field successful! Elapsed time: {} s", elapsedTimeString);
    }

    @Test
    public void testCompoundKey() {

        // Hints:
        // Keys must not be modified after creation (VALUE OBJECTs).
        // Thus, if you provide multiple objects as key-parts you
        // must be sure that none of them changes
        // internally afterwards: properties used to check equality and hash code must
        // remain unchanged over their entire lifetime.
        // Any change to a key may jeopardize HashMap-management.
        // The intrinsic relation between equals() and hashCode() must be preserved.

        LOGGER.info("Test compound key ...");
        long startTimeNanos = System.nanoTime();

        int numberOfKeys = 10000;

        List<CompoundKey> keyList = new ArrayList<>(numberOfKeys);
        Map<CompoundKey, Integer> keyMap = new HashMap<>(numberOfKeys);

        int keyCounter = 0;

        for (int r = 0; r < NUMBER_OF_RUNS; r++) {
            keyList.clear();
            keyMap.clear();
            keyCounter = 0;
            for (int i = 0; i < 10; i++) {
                for (int j = 0; j < 10; j++) {
                    for (int k = 0; k < 10; k++) {
                        for (int l = 0; l < 10; l++) {
                            CompoundKey key = new CompoundKey(i, j, k, l);
                            keyList.add(key);
                            keyMap.put(key, keyCounter);
                            keyCounter++;
                        }
                    }
                }
            }
        }

        assertEquals(numberOfKeys, keyCounter);
        assertEquals(numberOfKeys, keyList.size());
        assertEquals(numberOfKeys, keyMap.size());

        assertEquals("CompoundKey([9, 9, 9, 9])", keyList.get(9999).toString());

        for (int i = 0; i < numberOfKeys; i++) {
            CompoundKey key = keyList.get(i);
            assertEquals(i, keyMap.get(key).intValue());
        }
        String elapsedTimeString = TimeUtils.formatNanosAsSeconds(System.nanoTime() - startTimeNanos);
        LOGGER.info("Test compound key successful! Elapsed time: {} s", elapsedTimeString);

    }

}
