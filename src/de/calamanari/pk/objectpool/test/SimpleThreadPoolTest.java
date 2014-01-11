/*
 * Simple Thread Pool test case - demonstrates a thread pool.
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
package de.calamanari.pk.objectpool.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.calamanari.pk.objectpool.PoolThread;
import de.calamanari.pk.objectpool.SimpleThreadPool;
import de.calamanari.pk.util.LogUtils;
import de.calamanari.pk.util.MiscUtils;

/**
 * Test case to demonstrate thread pool.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class SimpleThreadPoolTest {

    /**
     * logger
     */
    private static final Logger LOGGER = Logger.getLogger(SimpleThreadPoolTest.class.getName());

    /**
     * Log-level for this test
     */
    private static final Level LOG_LEVEL = Level.INFO;

    /**
     * number of test runs (threaded calls)
     */
    private static final int NUMBER_OF_RUNS = 500;

    /**
     * count down latch to be notified when all threads have finished their work Note: this reference will be
     * re-initialized before each test (see setUp()) and accessed by multiple threads concurrently. To avoid that a
     * thread during the second test gets the (exhausted) latch object from the first test, I declare the reference
     * volatile. This has nothing to do with the latch itself!
     */
    private static volatile CountDownLatch doneCount;

    /**
     * variable for numbering threads (accessed by multiple threads)
     */
    private static final AtomicInteger THREAD_NO = new AtomicInteger(0);

    /**
     * work sample, each thread processes
     */
    private static final Runnable WORK_SAMPLE = new Runnable() {
        @Override
        public void run() {

            int no = THREAD_NO.incrementAndGet() + 1;
            LOGGER.fine("Thread " + no + ": " + Integer.toHexString(Thread.currentThread().hashCode()) + " working ...");

            // test takes some time, no random
            MiscUtils.sleepIgnoreException(((no * 73) % 13) * 10);
            doneCount.countDown();
            LOGGER.fine("Thread " + no + " completed!");
        }
    };

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        LogUtils.setConsoleHandlerLogLevel(LOG_LEVEL);
        LogUtils.setLogLevel(LOG_LEVEL, SimpleThreadPoolTest.class, SimpleThreadPool.class, PoolThread.class);
    }

    @Before
    public void setUp() throws Exception {
        LOGGER.info("Test setup ... ");
        doneCount = new CountDownLatch(NUMBER_OF_RUNS);
        THREAD_NO.set(0);
        LOGGER.info("Test setup completed. ");
    }

    /**
     * This one shows the result without pooling.
     */
    @Test
    public void testWithoutPool() throws Exception {

        LOGGER.info("Test without pool started ...");
        long startTimeNanos = System.nanoTime();
        for (int i = 0; i < NUMBER_OF_RUNS; i++) {
            Thread thread = new Thread(WORK_SAMPLE);
            thread.start();
        }
        LOGGER.info("Waiting for results ...");

        // wait for the threads to finish, but no longer than 2 minutes
        boolean success = doneCount.await(2, TimeUnit.MINUTES);
        assertTrue(success);

        LOGGER.info("Test without pool successful! Elapsed time: "
                + MiscUtils.formatNanosAsSeconds(System.nanoTime() - startTimeNanos) + " s");
    }

    /**
     * This one shows the result with an empty growing pool.
     */
    @Test
    public void testWithEmptyGrowingPool() throws Exception {
        SimpleThreadPool growingPool = new SimpleThreadPool(0, NUMBER_OF_RUNS, 5, false);

        LOGGER.info("Test with empty growing pool started ...");
        long startTimeNanos = System.nanoTime();
        for (int i = 0; i < NUMBER_OF_RUNS; i++) {
            Thread thread = growingPool.createThread(WORK_SAMPLE);
            thread.start();
            LOGGER.fine("Number of threads: " + growingPool.getThreadCount());
        }
        LOGGER.info("Waiting for results ...");

        // wait for the threads to finish, but no longer than 2 minutes
        boolean success = doneCount.await(2, TimeUnit.MINUTES);
        assertTrue(success);

        LOGGER.info("Test with empty growing pool successful! Elapsed time: "
                + MiscUtils.formatNanosAsSeconds(System.nanoTime() - startTimeNanos) + " s");
        int countBefore = growingPool.getThreadCount();

        int[] poolInfo = growingPool.getPoolInfo();
        LOGGER.info("Number of threads: total=" + poolInfo[0] + ", working=" + poolInfo[1] + ", idle=" + poolInfo[2]);

        // test whether the pool correctly shrinks
        growingPool.createThread(null).start();
        growingPool.createThread(null).start();

        assertEquals(growingPool.getThreadCount(), countBefore - 10);

        poolInfo = growingPool.getPoolInfo();
        LOGGER.info("Number of threads after shrink: total=" + poolInfo[0] + ", working=" + poolInfo[1] + ", idle="
                + poolInfo[2]);

    }

    /**
     * This one shows the result with a small restricted pool (causing wait times).
     */
    @Test
    public void testWithSmallPool() throws Exception {
        SimpleThreadPool smallPool = new SimpleThreadPool(5, 25, 5, true);

        LOGGER.info("Test with small restricted pool started ...");
        long startTimeNanos = System.nanoTime();
        for (int i = 0; i < NUMBER_OF_RUNS; i++) {
            Thread thread = smallPool.createThread(WORK_SAMPLE);
            thread.start();
        }
        LOGGER.info("Waiting for results ...");

        // wait for the threads to finish, but no longer than 2 minutes
        boolean success = doneCount.await(2, TimeUnit.MINUTES);
        assertTrue(success);

        LOGGER.info("Test with small restricted pool successful! Elapsed time: "
                + MiscUtils.formatNanosAsSeconds(System.nanoTime() - startTimeNanos) + " s");

        int[] poolInfo = smallPool.getPoolInfo();
        LOGGER.info("Number of threads: total=" + poolInfo[0] + ", working=" + poolInfo[1] + ", idle=" + poolInfo[2]);

        // Hint: Look at the runtime, synchronization comes at a cost
        // Try to leverage java.util.concurrent.Executors framework
        // and compare results.
    }

    /**
     * This one shows the result using a large pool (no resizing necessary).
     */
    @Test
    public void testWithLargePool() throws Exception {
        SimpleThreadPool largeWarmPool = new SimpleThreadPool(NUMBER_OF_RUNS, NUMBER_OF_RUNS, 0, true);

        LOGGER.info("Test with large pool started ...");
        long startTimeNanos = System.nanoTime();
        for (int i = 0; i < NUMBER_OF_RUNS; i++) {
            Thread thread = largeWarmPool.createThread(WORK_SAMPLE);
            thread.start();
        }
        LOGGER.info("Waiting for results ...");

        // wait for the threads to finish, but no longer than 2 minutes
        boolean success = doneCount.await(2, TimeUnit.MINUTES);
        assertTrue(success);

        LOGGER.info("Test with large pool successful! Elapsed time: "
                + MiscUtils.formatNanosAsSeconds(System.nanoTime() - startTimeNanos) + " s");

        int[] poolInfo = largeWarmPool.getPoolInfo();
        LOGGER.info("Number of threads: total=" + poolInfo[0] + ", working=" + poolInfo[1] + ", idle=" + poolInfo[2]);

    }

}
