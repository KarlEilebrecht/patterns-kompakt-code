//@formatter:off
/*
 * Null Object Test - demonstrates NULL OBJECT pattern.
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.calamanari.pk.util.MiscUtils;

/**
 * Null Object Test - demonstrates NULL OBJECT pattern.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class NullObjectTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(NullObjectTest.class);

    @Test
    public void testNullObject() throws Exception {

        // Adjust the log-level in logback.xml to DEBUG to see NULL OBJECT working

        LOGGER.info("Test Null Object ...");
        long startTimeNanos = System.nanoTime();

        HostNameData nameData = null;

        // first see what typically happens when using nulls
        // at the wrong place
        Exception caughtEx = null;
        try {
            concatNames(nameData);
        }
        catch (Exception ex) {
            caughtEx = ex;
        }
        assertTrue(caughtEx instanceof NullPointerException);

        // positive case (find a concrete data object)
        nameData = HostNameDataProvider.getHostNameData(HostNameDataProvider.EXISTING_DATA_KEY);
        assertEquals("XENOS, TREADSTONE, ANDROMEDA, GALAXY1", concatNames(nameData));

        // clone / produce a duplicate
        HostNameData nameDataClone = (HostNameData) nameData.clone();
        assertNotSame(nameData, nameDataClone);

        // serialization / produce a duplicate
        HostNameData nameDataSer = MiscUtils.passByValue(nameData);
        assertNotSame(nameData, nameDataSer);

        // negative case (unknown key)
        nameData = HostNameDataProvider.getHostNameData("bla");
        assertEquals("", concatNames(nameData));

        // null objects should not be duplicated
        HostNameData nameData2 = (HostNameData) nameData.clone();
        assertSame(nameData, nameData2);

        // serialization of a NULL OBJECT followed by deserialization should not produce a duplicate
        HostNameData nameData3 = MiscUtils.passByValue(nameData);
        assertSame(nameData, nameData3);

        String elapsedTimeString = MiscUtils.formatNanosAsSeconds(System.nanoTime() - startTimeNanos);
        LOGGER.info("Test Null Object successful! Elapsed time: {} s", elapsedTimeString);
    }

    /**
     * A simple method to concatenate names
     * 
     * @param nameData some names, NOT null
     * @return concatenated string of all names
     */
    private static String concatNames(Iterable<String> nameData) {
        StringBuilder sb = new StringBuilder();
        for (String name : nameData) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(name);
        }
        return sb.toString();
    }

}
