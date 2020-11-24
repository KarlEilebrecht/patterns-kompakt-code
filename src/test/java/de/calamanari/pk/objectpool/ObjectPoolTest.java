//@formatter:off
/*
 * Example Object Pool test case - demonstrates an OBJECT POOL.
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
package de.calamanari.pk.objectpool;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

import org.awaitility.Awaitility;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.calamanari.pk.util.TimeUtils;

/**
 * Testcase for ExampleObjectPool and related stuff
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 * 
 */
public class ObjectPoolTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ObjectPoolTest.class);

    /**
     * This one shows the result without pooling.
     */
    @Test
    public void testWithoutPool() {

        // HINTS:
        // * adjust the log-level in logback.xml to DEBUG and see OBJECT POOL working

        LOGGER.info("Test without pool started ... ");

        long startTimeNanos = System.nanoTime();
        ExampleReusableObject instance1 = new ExampleReusableObject();
        String callResult1 = instance1.computeResult();
        String callResult2 = instance1.computeResult();
        String callResult3 = instance1.computeResult();
        String callResult4 = instance1.computeResult();

        assertEquals("X1", callResult1);
        assertEquals("X2", callResult2);
        assertEquals("X3", callResult3);
        assertEquals("X4", callResult4);

        ExampleReusableObject instance2 = new ExampleReusableObject();
        String callResult5 = instance2.computeResult();
        String callResult6 = instance2.computeResult();
        String callResult7 = instance2.computeResult();
        String callResult8 = instance2.computeResult();

        assertEquals("X1", callResult5);
        assertEquals("X2", callResult6);
        assertEquals("X3", callResult7);
        assertEquals("X4", callResult8);

        ExampleReusableObject instance3 = new ExampleReusableObject();
        String callResult9 = instance3.computeResult();
        String callResult10 = instance3.computeResult();
        String callResult11 = instance3.computeResult();
        String callResult12 = instance3.computeResult();

        assertEquals("X1", callResult9);
        assertEquals("X2", callResult10);
        assertEquals("X3", callResult11);
        assertEquals("X4", callResult12);

        String elapsedTimeString = TimeUtils.formatNanosAsSeconds(System.nanoTime() - startTimeNanos);
        LOGGER.info("Test without pool successful! Elapsed time: {} s", elapsedTimeString);

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

        assertEquals("X1", callResult1);
        assertEquals("X2", callResult2);
        assertEquals("X3", callResult3);
        assertEquals("X4", callResult4);

        ExampleReusableObject instance2 = pool.acquireInstance();
        String callResult5 = instance2.computeResult();
        String callResult6 = instance2.computeResult();
        String callResult7 = instance2.computeResult();
        String callResult8 = instance2.computeResult();

        pool.returnInstance(instance2);

        assertEquals("X1", callResult5);
        assertEquals("X2", callResult6);
        assertEquals("X3", callResult7);
        assertEquals("X4", callResult8);

        ExampleReusableObject instance3 = pool.acquireInstance();
        String callResult9 = instance3.computeResult();
        String callResult10 = instance3.computeResult();
        String callResult11 = instance3.computeResult();
        String callResult12 = instance3.computeResult();

        pool.returnInstance(instance3);

        assertEquals("X1", callResult9);
        assertEquals("X2", callResult10);
        assertEquals("X3", callResult11);
        assertEquals("X4", callResult12);

        String elapsedTimeString = TimeUtils.formatNanosAsSeconds(System.nanoTime() - startTimeNanos);
        LOGGER.info("Test with pool (no warm-up) successful! Elapsed time: {} s", elapsedTimeString);

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

        assertEquals("X1", callResult1);
        assertEquals("X2", callResult2);
        assertEquals("X3", callResult3);
        assertEquals("X4", callResult4);

        ExampleReusableObject instance2 = pool.acquireInstance();
        String callResult5 = instance2.computeResult();
        String callResult6 = instance2.computeResult();
        String callResult7 = instance2.computeResult();
        String callResult8 = instance2.computeResult();

        pool.returnInstance(instance2);

        assertEquals("X1", callResult5);
        assertEquals("X2", callResult6);
        assertEquals("X3", callResult7);
        assertEquals("X4", callResult8);

        ExampleReusableObject instance3 = pool.acquireInstance();
        String callResult9 = instance3.computeResult();
        String callResult10 = instance3.computeResult();
        String callResult11 = instance3.computeResult();
        String callResult12 = instance3.computeResult();

        pool.returnInstance(instance3);

        assertEquals("X1", callResult9);
        assertEquals("X2", callResult10);
        assertEquals("X3", callResult11);
        assertEquals("X4", callResult12);

        String elapsedTimeString = TimeUtils.formatNanosAsSeconds(System.nanoTime() - startTimeNanos);
        LOGGER.info("Test with pool (warm-up) successful! Elapsed time: {} s", elapsedTimeString);

    }

    /**
     * This one shows the result with pooling, starting with an empty pool, with concurrency.
     * 
     * @throws Exception on any error
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

        AtomicReference<Throwable> firstError = new AtomicReference<>();

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
                            if (!"X1".equals(callResult)) {
                                firstError.compareAndSet(null, new Exception("Assertion X1 failed in asynchronous thread " + threadNo));
                            }
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

        assertNull(firstError.get());
        long endNanos = System.nanoTime();

        // give some time for log output
        Awaitility.await().pollDelay(1, TimeUnit.SECONDS).until(() -> true);

        String elapsedTimeString = TimeUtils.formatNanosAsSeconds(endNanos - startTimeNanos);
        LOGGER.info("Test with pool concurrent successful! Elapsed time: {} s", elapsedTimeString);

    }

}
