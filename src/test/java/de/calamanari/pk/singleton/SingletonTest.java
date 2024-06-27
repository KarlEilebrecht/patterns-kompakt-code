//@formatter:off
/*
 * Singleton Test - demonstrates SINGLETON pattern.
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
package de.calamanari.pk.singleton;

import static org.junit.Assert.assertSame;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;

import org.junit.AfterClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.calamanari.pk.util.TimeUtils;

/**
 * Singleton Test - demonstrates SINGLETON pattern.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class SingletonTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(SingletonTest.class);

    /**
     * number of runs for averaging runtime
     */
    private static final int NUMBER_OF_RUNS = 1;

    /**
     * number of threads working with the singleton
     */
    private static final int NUMBER_OF_THREADS = 100;

    /**
     * Flag to indicate whether to clean-up or not
     */
    private static final boolean DELETE_LOG_FILES_AFTER_TEST = true;

    @AfterClass
    public static void tearDownAfterClass() {
        Tracer.shutdown(DELETE_LOG_FILES_AFTER_TEST);
        Tracer2.shutdown(DELETE_LOG_FILES_AFTER_TEST);
    }

    @Test
    public void testSingleton() throws Exception {

        // HINTS:
        // * set the log-level in logback.xml to DEBUG to see SINGLETON details.
        // * set DELETE_LOG_FILES_AFTER_TEST=false to control the order threads have processed

        LOGGER.info("Test Singleton ...");
        long startTimeNanos = System.nanoTime();

        for (int r = 0; r < NUMBER_OF_RUNS; r++) {
            final ConcurrentHashMap<Integer, Tracer> results = new ConcurrentHashMap<>();
            final CountDownLatch latch = new CountDownLatch(NUMBER_OF_THREADS);
            for (int i = 0; i < NUMBER_OF_THREADS; i++) {
                final int currentNum = i + 1;
                (new Thread() {
                    @Override
                    public void run() {
                        Tracer myTracer = Tracer.getInstance();
                        myTracer.trace("Thread No. " + currentNum + " says hello!");
                        results.put(currentNum, myTracer);
                        latch.countDown();
                    }
                }).start();
            }

            latch.await();

            Tracer lastCallResult = Tracer.getInstance();

            for (Tracer tracer : results.values()) {
                // all threads must have worked with the same instance(!)
                assertSame(lastCallResult, tracer);
            }
        }

        String elapsedTimeString = TimeUtils.formatNanosAsSeconds((long) ((double) (System.nanoTime() - startTimeNanos) / NUMBER_OF_RUNS));
        LOGGER.info("Test Singleton successful! Elapsed time: {} s", elapsedTimeString);

    }

    @Test
    public void testSingleton2() throws Exception {

        LOGGER.info("Test Singleton2 ...");
        long startTimeNanos = System.nanoTime();

        for (int r = 0; r < NUMBER_OF_RUNS; r++) {

            final ConcurrentHashMap<Integer, Tracer2> results = new ConcurrentHashMap<>();
            final CountDownLatch latch = new CountDownLatch(NUMBER_OF_THREADS);
            for (int i = 0; i < NUMBER_OF_THREADS; i++) {
                final int currentNum = i + 1;
                (new Thread() {
                    @Override
                    public void run() {
                        try {
                            Tracer2 myTracer = Tracer2.getInstance();
                            myTracer.trace("Thread No. " + currentNum + " says hello!");
                            results.put(currentNum, myTracer);
                        }
                        finally {
                            latch.countDown();
                        }
                    }
                }).start();
            }

            latch.await();

            Tracer2 lastCallResult = Tracer2.getInstance();

            for (Tracer2 tracer2 : results.values()) {
                // all threads must have worked with the same instance(!)
                assertSame(lastCallResult, tracer2);
            }
        }

        // Note: When comparing runtimes of the different singleton implementations
        // please take into account JVM-"ramp-up"-effects.
        // To get any meaningful results, you will have to repeat the test
        // several times and also change the order of the tests for the
        // different implementations.

        String elapsedTimeString = TimeUtils.formatNanosAsSeconds((long) ((double) (System.nanoTime() - startTimeNanos) / NUMBER_OF_RUNS));
        LOGGER.info("Test Singleton2 successful! Elapsed time: {} s", elapsedTimeString);

    }

    @Test
    public void testSingleton3() throws Exception {
        LOGGER.info("Test Singleton3 ...");
        long startTimeNanos = System.nanoTime();

        Tracer2 firstCallResult = Tracer2.getInstance();

        ByteArrayOutputStream bos = new ByteArrayOutputStream();

        try (ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(firstCallResult);
        }

        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        ObjectInputStream ois = new ObjectInputStream(bis);
        Tracer2 restored = (Tracer2) ois.readObject();

        assertSame(firstCallResult, restored);
        String elapsedTimeString = TimeUtils.formatNanosAsSeconds(System.nanoTime() - startTimeNanos);
        LOGGER.info("Test Singleton3 successful! Elapsed time: {} s", elapsedTimeString);

    }

}
