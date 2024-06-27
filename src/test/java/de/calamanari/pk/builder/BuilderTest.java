//@formatter:off
/*
 * Builder Test - demonstrates BUILDER pattern.
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
package de.calamanari.pk.builder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.calamanari.pk.util.TimeUtils;

/**
 * Builder Test - demonstrates BUILDER pattern.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class BuilderTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(BuilderTest.class);

    /**
     * The checksum-builder leverages a BUILDER. In the builder pattern this is the DIRECTOR.
     */
    private ChecksumHelper checksumHelper = null;

    @Before
    public void setUp() {
        checksumHelper = new ChecksumHelper(new Crc32ChecksumBuilder());
    }

    @Test
    public void testBuilder() {

        // hint: adjust the log-levels in logback.xml to DEBUG to see BUILDER working

        LOGGER.info("Test Builder ...");
        long startTimeNanos = System.nanoTime();

        Object[] testData = new Object[] { "Lucy", 1L, 4, 6.0, new byte[] { 1, 2, 3 }, "Peter", 9 };

        List<Object[]> toBeNotEqualArrays = new ArrayList<>();
        toBeNotEqualArrays.add(new Object[] { "Luke", 1L, 4, 6.0, new byte[] { 1, 2, 3 }, "Peter", 9 });
        toBeNotEqualArrays.add(new Object[] { 1L, 4, 6.0, new byte[] { 1, 2, 3 }, "Peter", 9 });
        toBeNotEqualArrays.add(new Object[] { "Lucy", 1L, 4, 6.1, new byte[] { 1, 2, 3 }, "Peter", 9 });
        toBeNotEqualArrays.add(new Object[] { "Lucy", 1L, 4, 6.0, new byte[] { 1, 2, 0 }, "Peter", 9 });
        toBeNotEqualArrays.add(new Object[] { 1L, 4, 6.0, new byte[] { 1, 2, 3 }, "Peter", 9, "Lucy" });

        List<Object> toBeEqualList = new ArrayList<>();
        toBeEqualList.add("Lucy");
        toBeEqualList.add(1L);
        toBeEqualList.add(4);
        toBeEqualList.add(6.0);
        toBeEqualList.add(new byte[] { 1, 2, 3 });
        toBeEqualList.add("Peter");
        toBeEqualList.add(9);
        Object[] toBeEqual = toBeEqualList.toArray();
        long checksum = checksumHelper.computeChecksum(testData);

        LOGGER.debug("Original array checksum: {}", checksum);

        assertEquals(checksum, checksumHelper.computeChecksum(toBeEqual));

        for (Object[] toBeNotEqual : toBeNotEqualArrays) {
            assertNotSame(checksum, checksumHelper.computeChecksum(toBeNotEqual));
        }
        String elapsedSeconds = TimeUtils.formatNanosAsSeconds(System.nanoTime() - startTimeNanos);
        LOGGER.info("Test Builder successful! Elapsed time: {} s", elapsedSeconds);

    }

}
