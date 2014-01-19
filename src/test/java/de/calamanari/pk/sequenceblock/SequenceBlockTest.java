/*
 * Sequence block test case - demonstrates SEQUENCE BLOCK.
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
package de.calamanari.pk.sequenceblock;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.calamanari.pk.sequenceblock.SequenceBlock;
import de.calamanari.pk.sequenceblock.SequenceBlockCache;
import de.calamanari.pk.util.LogUtils;
import de.calamanari.pk.util.MiscUtils;
import de.calamanari.pk.util.db.EmbeddedJavaDbDataSource;

/**
 * Sequence block test case to demonstrate SEQUENCE BLOCK pattern
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class SequenceBlockTest {

    /**
     * logger
     */
    private static final Logger LOGGER = Logger.getLogger(SequenceBlockTest.class.getName());

    /**
     * Log-level for this test
     */
    private static final Level LOG_LEVEL = Level.INFO;

    /**
     * number of test runs (threaded calls)
     */
    private static final int NUMBER_OF_RUNS = 500;

    /**
     * variable for numbering threads (accessed by multiple threads)
     */
    private static final AtomicInteger THREAD_NO = new AtomicInteger(0);

    /**
     * count down latch to be notified when all threads have finished their work Note: this reference will be
     * re-initialized before each test (see setUp()) and accessed by multiple threads concurrently. To avoid that a
     * thread during the second test gets the (exhausted) latch object from the first test, I declare the reference
     * volatile. This has nothing to do with the latch itself!
     */
    private static volatile CountDownLatch doneCount;

    /**
     * name of test sequence
     */
    private static final String TEST_SEQUENCE_NAME = "testSequence";

    /**
     * Collects all the used ids.
     */
    private static final Set<Long> USED_ID_SET = new HashSet<>();

    /**
     * datasource to be used
     */
    private static volatile DataSource dataSource;

    /**
     * global instance of a sequence block cache
     */
    private static volatile SequenceBlockCache globalCache;

    /**
     * work sample, each thread processes, all using the same global sequence block cache
     */
    private static final Runnable WORK_SAMPLE_GLOBAL = new Runnable() {
        @Override
        public void run() {

            int no = THREAD_NO.incrementAndGet() + 1;
            LOGGER.fine("Thread " + no + ": " + Integer.toHexString(Thread.currentThread().hashCode()) + " working ...");

            // no random, but avoid the same value for all threads
            int magic = ((no * 73) % 23);
            for (int i = 0; i < magic; i++) {

                long id = globalCache.getNextId(TEST_SEQUENCE_NAME);

                LOGGER.finest("Thread " + no + ": " + Integer.toHexString(Thread.currentThread().hashCode())
                        + " retrieved ID=" + id + ".");

                collectId(id);

                MiscUtils.sleepThrowRuntimeException(magic);
            }
            doneCount.countDown();
            LOGGER.fine("Thread " + no + " completed!");
        }
    };

    /**
     * work sample, each thread processes, all using a local sequence block cache but the same simulated database (hint:
     * SequenceBlockCache.simulatedDatabase is declared static).
     */
    private static final Runnable WORK_SAMPLE_LOCAL = new Runnable() {
        @Override
        public void run() {

            int no = THREAD_NO.incrementAndGet() + 1;
            LOGGER.fine("Thread " + no + ": " + Integer.toHexString(Thread.currentThread().hashCode()) + " working ...");

            SequenceBlockCache localSequenceBlockCache = new SequenceBlockCache(dataSource);

            // no random, but avoid the same value for all threads
            int magic = ((no * 73) % 23);
            for (int i = 0; i < magic; i++) {

                long id = localSequenceBlockCache.getNextId(TEST_SEQUENCE_NAME);

                LOGGER.finest("Thread " + no + ": " + Integer.toHexString(Thread.currentThread().hashCode())
                        + " retrieved ID=" + id + ".");

                collectId(id);

                MiscUtils.sleepThrowRuntimeException(magic);
            }
            doneCount.countDown();
            LOGGER.fine("Thread " + no + " completed!");
        }
    };

    /**
     * collects id from thread run and checks whether it already exists (which indicates an error)
     * @param id new id to be collected
     */
    private static void collectId(Long id) {
        synchronized (USED_ID_SET) {
            assertFalse(USED_ID_SET.contains(id));
            USED_ID_SET.add(id);
        }
    }

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        LogUtils.setConsoleHandlerLogLevel(LOG_LEVEL);
        LogUtils.setLogLevel(LOG_LEVEL, SequenceBlockTest.class, SequenceBlock.class, SequenceBlockCache.class);
        LOGGER.info("Database setup ... ");
        dataSource = EmbeddedJavaDbDataSource.getInstance();
        try (Connection con = dataSource.getConnection(); Statement stmt = con.createStatement()) {
            // There is no "IF EXISTS", thus brute-force drop.
            // However, the cleaner way was using DatabaseMetaData to check for existence in advance.
            stmt.executeUpdate("drop table SEQUENCE_TABLE");
        }
        catch (Exception ex) {
            // ignore (first time the table is not there
        }

        try (Connection con = dataSource.getConnection(); Statement stmt = con.createStatement()) {
            stmt.executeUpdate("create table SEQUENCE_TABLE (SEQ_NAME varchar(20) PRIMARY KEY, SEQ_ID int)");
        }
        LOGGER.info("Database setup completed. ");
        globalCache = new SequenceBlockCache(dataSource);
    }

    @Before
    public void setUp() throws Exception {
        LOGGER.info("Test setup ... ");
        doneCount = new CountDownLatch(NUMBER_OF_RUNS);
        THREAD_NO.set(0);
        USED_ID_SET.clear();
        LOGGER.info("Test setup completed. ");
    }

    @Test
    public void testGlobalSequenceBlockCache() throws Exception {

        // Hints: - set the log-level above to FINE to watch SEQUENCE BLOCK working.
        // before, you should redirect console output to a file, it's a lot of stuff ...
        //
        // - You can easily replace the embedded JavaDB dataSource (Derby) by a real one:
        // see above in setUpBeforeClass()

        LOGGER.info("Test with global sequence block cache ...");
        long startTimeNanos = System.nanoTime();
        for (int i = 0; i < NUMBER_OF_RUNS; i++) {
            Thread thread = new Thread(WORK_SAMPLE_GLOBAL);
            thread.start();
        }
        LOGGER.info("Waiting for results ...");

        // wait for the threads to finish, but no longer than 2 minutes
        boolean success = doneCount.await(2, TimeUnit.MINUTES);
        assertTrue(success);

        LOGGER.info("Number of used ids: " + USED_ID_SET.size() + ", next ID="
                + globalCache.getNextId(TEST_SEQUENCE_NAME));
        LOGGER.info("Test with global sequence block cache successful! Elapsed time: "
                + MiscUtils.formatNanosAsSeconds(System.nanoTime() - startTimeNanos) + " s");

        if (LOGGER.isLoggable(Level.FINEST)) {
            LOGGER.finest("All used ids: " + USED_ID_SET);
        }
    }

    @Test
    public void testLocalSequenceBlockCache() throws Exception {
        LOGGER.info("Test with local sequence block caches (but common database) ...");
        long startTimeNanos = System.nanoTime();
        for (int i = 0; i < NUMBER_OF_RUNS; i++) {
            Thread thread = new Thread(WORK_SAMPLE_LOCAL);
            thread.start();
        }
        LOGGER.info("Waiting for results ...");

        // wait for the threads to finish, but no longer than 2 minutes
        boolean success = doneCount.await(2, TimeUnit.MINUTES);
        assertTrue(success);

        LOGGER.info("Number of used ids: " + USED_ID_SET.size() + ", next independent block starts with ID="
                + (new SequenceBlockCache(dataSource)).getNextId(TEST_SEQUENCE_NAME));
        // Hint: The database is not cleared between tests calls in the test case.
        // Please have a look at the "next independent block starts with" message. Although we have requested exactly
        // the same
        // number of IDs again, the number of used IDs gets more than doubled! Hmm ...
        // Because each thread here uses its own concurrent block accessing a common database table, a large amount of
        // IDs gets wasted.
        // In fact in this example most blocks will be thrown away before getting exhausted.
        // This is no error but shows a typical side effect, when multiple servers (here the threads) work on the same
        // database.
        LOGGER.info("Test with local sequence block caches successful! Elapsed time: "
                + MiscUtils.formatNanosAsSeconds(System.nanoTime() - startTimeNanos) + " s");

        if (LOGGER.isLoggable(Level.FINEST)) {
            LOGGER.finest("All used ids: " + USED_ID_SET);
        }
    }

}
