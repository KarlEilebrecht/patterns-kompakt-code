/*
 * Example Object Pool test case - demonstrates an OBJECT POOL.
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
package de.calamanari.pk.objectpool.test;

import static org.junit.Assert.assertEquals;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.BeforeClass;
import org.junit.Test;

import de.calamanari.pk.objectpool.ExampleObjectPool;
import de.calamanari.pk.objectpool.ExampleReusableObject;
import de.calamanari.pk.util.LogUtils;
import de.calamanari.pk.util.MiscUtils;

/**
 * Testcase for ExampleObjectPool and related stuff
 * @author <a href="mailto:Karl.Eilebrecht(a/t)web.de">Karl Eilebrecht</a>
 * 
 */
public class ObjectPoolTest {

    /**
     * logger
     */
    private static final Logger LOGGER = Logger.getLogger(ObjectPoolTest.class.getName());

    /**
     * Log-level for this test
     */
    private static final Level LOG_LEVEL = Level.INFO;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        LogUtils.setConsoleHandlerLogLevel(LOG_LEVEL);
        LogUtils.setLogLevel(LOG_LEVEL, ObjectPoolTest.class, ExampleObjectPool.class, ExampleReusableObject.class);
    }

    /**
     * This one shows the result without pooling.
     */
    @Test
    public void testWithoutPool() {

        // HINTS:
        // * adjust the log-level above to FINE and see OBJECT POOL working

        LOGGER.info("Test without pool started ... ");

        long startTimeNanos = System.nanoTime();
        ExampleReusableObject instance1 = new ExampleReusableObject();
        String callResult1 = instance1.computeResult();
        String callResult2 = instance1.computeResult();
        String callResult3 = instance1.computeResult();
        String callResult4 = instance1.computeResult();

        assertEquals(callResult1, "X1");
        assertEquals(callResult2, "X2");
        assertEquals(callResult3, "X3");
        assertEquals(callResult4, "X4");

        ExampleReusableObject instance2 = new ExampleReusableObject();
        String callResult5 = instance2.computeResult();
        String callResult6 = instance2.computeResult();
        String callResult7 = instance2.computeResult();
        String callResult8 = instance2.computeResult();

        assertEquals(callResult5, "X1");
        assertEquals(callResult6, "X2");
        assertEquals(callResult7, "X3");
        assertEquals(callResult8, "X4");

        ExampleReusableObject instance3 = new ExampleReusableObject();
        String callResult9 = instance3.computeResult();
        String callResult10 = instance3.computeResult();
        String callResult11 = instance3.computeResult();
        String callResult12 = instance3.computeResult();

        assertEquals(callResult9, "X1");
        assertEquals(callResult10, "X2");
        assertEquals(callResult11, "X3");
        assertEquals(callResult12, "X4");

        LOGGER.info("Test without pool successful! Elapsed time: "
                + MiscUtils.formatNanosAsSeconds(System.nanoTime() - startTimeNanos) + " s");

    }

    /**
     * This one shows the result with pooling, starting with an empty pool.
     */
    @Test
    public void testWithPoolNoWarmUp() {

        LOGGER.info("Test with pool (no warm-up) started ... ");

        long startTimeNanos = System.nanoTime();
        ExampleObjectPool pool = new ExampleObjectPool();
        ExampleReusableObject instance1 = pool.acquireInstance();
        String callResult1 = instance1.computeResult();
        String callResult2 = instance1.computeResult();
        String callResult3 = instance1.computeResult();
        String callResult4 = instance1.computeResult();

        pool.returnInstance(instance1);

        assertEquals(callResult1, "X1");
        assertEquals(callResult2, "X2");
        assertEquals(callResult3, "X3");
        assertEquals(callResult4, "X4");

        ExampleReusableObject instance2 = pool.acquireInstance();
        String callResult5 = instance2.computeResult();
        String callResult6 = instance2.computeResult();
        String callResult7 = instance2.computeResult();
        String callResult8 = instance2.computeResult();

        pool.returnInstance(instance2);

        assertEquals(callResult5, "X1");
        assertEquals(callResult6, "X2");
        assertEquals(callResult7, "X3");
        assertEquals(callResult8, "X4");

        ExampleReusableObject instance3 = pool.acquireInstance();
        String callResult9 = instance3.computeResult();
        String callResult10 = instance3.computeResult();
        String callResult11 = instance3.computeResult();
        String callResult12 = instance3.computeResult();

        pool.returnInstance(instance3);

        assertEquals(callResult9, "X1");
        assertEquals(callResult10, "X2");
        assertEquals(callResult11, "X3");
        assertEquals(callResult12, "X4");

        LOGGER.info("Test with pool (no warm-up) successful! Elapsed time: "
                + MiscUtils.formatNanosAsSeconds(System.nanoTime() - startTimeNanos) + " s");

    }

    /**
     * This one shows the result with pooling, starting with a filled pool.
     */
    @Test
    public void testWithPoolWarmUp() {

        ExampleObjectPool warmPool = new ExampleObjectPool();
        ExampleReusableObject warmUpInstance = warmPool.acquireInstance();
        warmPool.returnInstance(warmUpInstance);

        LOGGER.info("Test with pool (warm-up) started ... ");

        ExampleObjectPool pool = warmPool;

        long startTimeNanos = System.nanoTime();
        ExampleReusableObject instance1 = pool.acquireInstance();
        String callResult1 = instance1.computeResult();
        String callResult2 = instance1.computeResult();
        String callResult3 = instance1.computeResult();
        String callResult4 = instance1.computeResult();

        pool.returnInstance(instance1);

        assertEquals(callResult1, "X1");
        assertEquals(callResult2, "X2");
        assertEquals(callResult3, "X3");
        assertEquals(callResult4, "X4");

        ExampleReusableObject instance2 = pool.acquireInstance();
        String callResult5 = instance2.computeResult();
        String callResult6 = instance2.computeResult();
        String callResult7 = instance2.computeResult();
        String callResult8 = instance2.computeResult();

        pool.returnInstance(instance2);

        assertEquals(callResult5, "X1");
        assertEquals(callResult6, "X2");
        assertEquals(callResult7, "X3");
        assertEquals(callResult8, "X4");

        ExampleReusableObject instance3 = pool.acquireInstance();
        String callResult9 = instance3.computeResult();
        String callResult10 = instance3.computeResult();
        String callResult11 = instance3.computeResult();
        String callResult12 = instance3.computeResult();

        pool.returnInstance(instance3);

        assertEquals(callResult9, "X1");
        assertEquals(callResult10, "X2");
        assertEquals(callResult11, "X3");
        assertEquals(callResult12, "X4");

        LOGGER.info("Test with pool (warm-up) successful! Elapsed time: "
                + MiscUtils.formatNanosAsSeconds(System.nanoTime() - startTimeNanos) + " s");

    }

    /**
     * This one shows the result with pooling, starting with an empty pool, with concurrency.
     */
    @Test
    public void testWithPoolConcurrent() throws Exception {

        // Have a look on the runtime of the concurrent test:
        // Here we create ExampleObjectPool.MAX_POOL_SIZE instances.
        // Why don't do the times for creation sum-up as they did
        // in the first (non-pooling) test?
        // This has to do with the team play of the Semaphore on the one
        // hand and the lock on the other (see ExampleObjectPool class).
        // The Sempaphore keeps track not to put too many instances
        // into the pool while the poolLock protects the pool from concurrent
        // modification. Thus we can execute the expensive object creation
        // res = new ExampleReusableObject();
        // outside the lock-protected code area.
        // Thus multiple instances can be created in parallel.

        final ExampleObjectPool pool = new ExampleObjectPool();

        int numberOfRuns = ExampleObjectPool.MAX_POOL_SIZE + 1;

        LOGGER.info("Test with pool concurrent started ... ");

        long startTimeNanos = System.nanoTime();
        final CountDownLatch latch = new CountDownLatch(numberOfRuns);

        ExecutorService executorService = Executors.newFixedThreadPool(25);
        try {
            for (int i = 0; i < numberOfRuns; i++) {
                final int threadNo = i + 1;
                Runnable work = new Runnable() {
                    @Override
                    public void run() {
                        Thread.currentThread().setName("Thread-" + threadNo);
                        ExampleReusableObject instance = pool.acquireInstance();
                        try {
                            String callResult = instance.computeResult();
                            assertEquals(callResult, "X1");
                        }
                        finally {
                            latch.countDown();
                            pool.returnInstance(instance);
                        }
                    }
                };
                executorService.execute(work);
            }
            latch.await();
        }
        finally {
            executorService.shutdown();
        }
        long endNanos = System.nanoTime();

        // give some time for log output
        Thread.sleep(1000);

        LOGGER.info("Test with pool concurrent successful! Elapsed time: "
                + MiscUtils.formatNanosAsSeconds(endNanos - startTimeNanos) + " s");

    }

}
