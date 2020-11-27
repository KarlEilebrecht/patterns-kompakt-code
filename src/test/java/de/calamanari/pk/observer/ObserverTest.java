//@formatter:off
/*
 * Observer Test - demonstrates OBSERVER pattern.
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
package de.calamanari.pk.observer;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.calamanari.pk.util.TimeUtils;
import de.calamanari.pk.util.tpl.ThroughputEvent;
import de.calamanari.pk.util.tpl.ThroughputLimiter;
import de.calamanari.pk.util.tpl.ThroughputListener;

/**
 * Observer Test - demonstrates OBSERVER pattern.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class ObserverTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ObserverTest.class);

    /**
     * number of bytes each worker will "write"
     */
    private static final long NUMBER_OF_BYTES = 100;

    /**
     * number of workers (observables)
     */
    private static final int NUMBER_OF_WORKERS = 100;

    /**
     * here we count the number of attempts to call our dummy service
     */
    private final AtomicInteger serviceCallAttemptCounter = new AtomicInteger();

    /**
     * here we count the number of calls to our dummy service
     */
    private final AtomicInteger serviceCallSuccessCounter = new AtomicInteger();

    /**
     * the number of successful calls the observer reported
     */
    private final AtomicInteger observedCallSuccessCounter = new AtomicInteger();

    /**
     * the number of unsuccessful calls the observer reported
     */
    private final AtomicInteger observedCallDeniedCounter = new AtomicInteger();

    @Test
    public void testObserver() throws Exception {

        // Hint: set the log-level in logback.xml to DEBUG to watch OBSERVER working.

        LOGGER.info("Test Observer ...");
        long startTimeNanos = System.nanoTime();

        long bytesAllInAll = NUMBER_OF_BYTES * NUMBER_OF_WORKERS;
        ProgressObserver progressObserver = new ProgressObserver(bytesAllInAll);
        List<OutputObservable> observableList = new ArrayList<>();
        for (int i = 0; i < NUMBER_OF_WORKERS; i++) {
            OutputWorker worker = new OutputWorker(i, NUMBER_OF_BYTES);
            worker.addOutputObserver(progressObserver);
            observableList.add(worker);
            worker.start();
        }

        while (progressObserver.getCounterValue() < bytesAllInAll) {
            TimeUtils.sleepThrowRuntimeException(1000);
        }

        for (OutputObservable observable : observableList) {
            observable.removeOutputObserver(progressObserver);
        }

        assertEquals(bytesAllInAll, progressObserver.getCounterValue());

        String elapsedTimeString = TimeUtils.formatNanosAsSeconds(System.nanoTime() - startTimeNanos);
        LOGGER.info("Test Observer successful! Elapsed time: {} s", elapsedTimeString);

    }

    @Test
    public void testWithAsyncObserver() throws Exception {

        LOGGER.info("Test with asynchronous Observer ...");
        long startTimeNanos = System.nanoTime();

        // The ThroughputLimiter allows to limit the number of requests to a given service.
        // Along with this functionality it provides the option for registering
        // a listener (OBSERVER) which logs or measures the current throughput.

        // Watch the log output during execution.

        // The limit shall be 50 calls per second to our dummy service
        ThroughputLimiter limiter = new ThroughputLimiter(50, 1, TimeUnit.SECONDS);

        ThroughputListener observer = new ThroughputListener() {

            @Override
            public void handleThroughputData(ThroughputEvent event) {
                LOGGER.info(event.toString());
                observedCallSuccessCounter.set((int) event.getPassedCount());
                observedCallDeniedCounter.set((int) event.getDeniedCount());
            }

            @Override
            public void handleListenerDied(Throwable problem) {
                LOGGER.error("Listener died.", problem);
            }
        };

        // The registered observer will not be called inside the executing thread(s)
        // but periodically (each second) in its own thread.
        // When performance is important, the observer's method runtime
        // should never affect the core function.
        limiter.registerThroughputListener(observer, 1000);

        this.serviceCallAttemptCounter.set(0);
        this.serviceCallSuccessCounter.set(0);
        this.observedCallDeniedCounter.set(0);
        this.observedCallSuccessCounter.set(0);

        ExecutorService service = Executors.newFixedThreadPool(100);

        AtomicBoolean stoppedFlag = new AtomicBoolean(false);
        for (int i = 0; i < 100; i++) {
            service.execute(new DummyServiceCaller(limiter, stoppedFlag));
        }
        TimeUtils.sleepThrowRuntimeException(15000);
        stoppedFlag.set(true);
        service.shutdown();
        service.awaitTermination(1, TimeUnit.SECONDS);

        // asynchronous tests with multiple threads are delicate, thus we
        // give a little time to finish pending actions
        TimeUtils.sleepThrowRuntimeException(1500);

        limiter.removeThroughputListener(observer);

        assertEquals(this.serviceCallAttemptCounter.get(), (observedCallDeniedCounter.get() + observedCallSuccessCounter.get()));
        assertEquals(this.serviceCallSuccessCounter.get(), observedCallSuccessCounter.get());

        String elapsedTimeString = TimeUtils.formatNanosAsSeconds(System.nanoTime() - startTimeNanos);
        LOGGER.info("Test with asynchronous Observer successful! Elapsed time: {} s", elapsedTimeString);

    }

    /**
     * This is the service we limit the access to protect it from overload.
     */
    private void callDummyService() {
        this.serviceCallSuccessCounter.incrementAndGet();
    }

    /**
     * Utility class to fire calls against the dummy service asynchronously.
     * 
     */
    private final class DummyServiceCaller implements Runnable {

        /**
         * for limiting the number of calls to the dummy service
         */
        private final ThroughputLimiter limiter;

        /**
         * Flag to stop execution
         */
        private final AtomicBoolean stoppedFlag;

        /**
         * Creates a new caller instance using the given limiter
         * 
         * @param limiter restricts access to the dummy service
         * @param stoppedFlag Flag to stop execution
         */
        private DummyServiceCaller(ThroughputLimiter limiter, AtomicBoolean stoppedFlag) {
            this.limiter = limiter;
            this.stoppedFlag = stoppedFlag;
        }

        @Override
        // Suppressing SonarLint's complaint about using Thread.sleep() here, see below
        @SuppressWarnings("squid:S2925")
        public void run() {
            while (!stoppedFlag.get()) {

                // Below I cheated a little to get a smooth log output ;-)
                // A short wait time reduces the concurrency stress within the JVM.
                // Try removing the try-block with the Thread.sleep() below
                // and compare the outputs.
                // The reported throughput information seems disturbing
                // but passedCount and deniedCount should still be in range.
                //
                // Please also watch your system's load indicators.
                // You should observe a drastic difference (CPU usage)!
                //
                // To virtually burn your CPU, replace the Thread.sleep() with
                // Thread.yield(). This will reduce the thread contention effects
                // within the JVM and cause maximum CPU usage.
                //
                // However, the limiter itself should still work within the configured
                // specifications.
                try {
                    Thread.sleep(0, 1);
                }
                catch (InterruptedException ex) {
                    LOGGER.error("Worker thread interrupted!");
                    Thread.currentThread().interrupt();
                    break;
                }

                serviceCallAttemptCounter.incrementAndGet();
                if (limiter.tryGetPermission()) {
                    callDummyService();
                }
            }
        }

    }

}
