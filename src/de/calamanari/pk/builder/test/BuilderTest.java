/*
 * Builder Test - demonstrates BUILDER pattern.
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
package de.calamanari.pk.builder.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.calamanari.pk.builder.Checksum;
import de.calamanari.pk.builder.ChecksumHelper;
import de.calamanari.pk.builder.Crc32ChecksumBuilder;
import de.calamanari.pk.util.LogUtils;
import de.calamanari.pk.util.MiscUtils;

/**
 * Builder Test - demonstrates BUILDER pattern.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)web.de">Karl Eilebrecht</a>
 */
public class BuilderTest {

    /**
     * logger
     */
    protected static final Logger LOGGER = Logger.getLogger(BuilderTest.class.getName());

    /**
     * Log-level for this test
     */
    private static final Level LOG_LEVEL = Level.INFO;

    /**
     * The checksum-builder leverages a BUILDER. In the builder pattern this is the DIRECTOR.
     */
    private ChecksumHelper checksumHelper = null;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        LogUtils.setConsoleHandlerLogLevel(LOG_LEVEL);
        LogUtils.setLogLevel(LOG_LEVEL, BuilderTest.class, Crc32ChecksumBuilder.class, ChecksumHelper.class,
                Checksum.class);
    }

    @Before
    public void setUp() throws Exception {
        checksumHelper = new ChecksumHelper(new Crc32ChecksumBuilder());
    }

    @Test
    public void testBuilder() throws Exception {

        // hint: adjust the log-levels above to FINE to see BUILDER working

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

        LOGGER.fine("Original array checksum: " + checksum);

        assertEquals(checksum, checksumHelper.computeChecksum(toBeEqual));

        for (Object[] toBeNotEqual : toBeNotEqualArrays) {
            assertNotSame(checksum, checksumHelper.computeChecksum(toBeNotEqual));
        }
        LOGGER.info("Test Builder successful! Elapsed time: "
                + MiscUtils.formatNanosAsSeconds(System.nanoTime() - startTimeNanos) + " s");

    }

}
