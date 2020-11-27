//@formatter:off
/*
 * Throughput Checker Thread
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
package de.calamanari.pk.util.tpl;

import java.lang.ref.WeakReference;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.calamanari.pk.util.TimeUtils;
import de.calamanari.pk.util.tpl.ThroughputEvent.Timing;

/**
 * A {@link ThroughputCheckerThread} manages a {@link ThroughputListener} at runtime.<br>
 * It is obviously impossible to notify the observer synchronously since there are possibly millions of concurrent calls to a {@link ThroughputLimiter}. Thus
 * the {@link ThroughputCheckerThread} periodically computes the current state and notifies the registered listener.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public final class ThroughputCheckerThread extends Thread {

    private static final Logger LOGGER = LoggerFactory.getLogger(ThroughputCheckerThread.class);

    /**
     * life-cycle control true->false->true
     */
    private final AtomicBoolean stopped = new AtomicBoolean(true);

    /**
     * Observer which monitors the limiter
     */
    final ThroughputListener listener;

    /**
     * Reference to the observed {@link ThroughputLimiter}, we use a weak reference to support garbage collection in case the listener never de-registers.
     */
    private final WeakReference<ThroughputLimiter> limiter;

    /**
     * Interval time in nanoseconds
     */
    volatile long intervalTimeNanos;

    /**
     * The last event is used for delta calculation.
     */
    private ThroughputEvent lastEvent = null;

    /**
     * The number of passed request passed count must be ignored in total throughput calculation.
     */
    private long firstCheckTimePassedCount = 0;

    /**
     * time the first check ran (for delta calculation)
     */
    private long firstCheckTimeNanos = 0;

    /**
     * overload nanos sum at the time of the privious check (for delta calculation)
     */
    private long lastCheckOverloadNanos = 0;

    /**
     * Time the last calculation and notification took
     */
    private long lastCheckOperationTimeNanos = 0;

    /**
     * May be called at any time to disable the checker.
     */
    public void stopCheck() {
        if (stopped.compareAndSet(false, true)) {
            this.interrupt();
        }
    }

    /**
     * Creates a checker thread for the given limiter and listener.
     * 
     * @param limiter the observed instance
     * @param listener observer
     * @param intervalTimeNanos wait time in nanoseconds between two checks
     */
    public ThroughputCheckerThread(ThroughputLimiter limiter, ThroughputListener listener, long intervalTimeNanos) {
        this.setDaemon(true);
        this.limiter = new WeakReference<>(limiter);
        this.listener = listener;
        this.intervalTimeNanos = intervalTimeNanos;
    }

    /**
     * Waits for the currently configured time
     * 
     * @return wait nanos
     * @throws InterruptedException if the wait was interrupted externally
     */
    private long doWait() throws InterruptedException {
        long waitNanos = intervalTimeNanos - lastCheckOperationTimeNanos;
        if (waitNanos > 0) {
            long waitMillis = waitNanos / TplConstants.MILLION;
            long remainingWaitNanos = waitNanos - (waitMillis * TplConstants.MILLION);
            Thread.sleep(waitMillis, (int) remainingWaitNanos);
        }
        return waitNanos;
    }

    /**
     * This method will be called periodically to convert the current state of the {@link ThroughputLimiter} into a {@link ThroughputEvent} to be passed to the
     * registered listener.
     */
    private void doCheck() {
        ThroughputLimiter instance = limiter.get();
        if (instance != null) {
            long passedCount = instance.passedCount.get();
            long deniedCount = instance.deniedCount.get();
            long overloadNanos = instance.overloadNanos.get();
            OverloadState state = instance.overloadState.get();
            ThroughputEvent event = null;
            long nowNanoTime = TimeUtils.getSystemUptimeNanos();
            boolean overload = (state != OverloadState.NOT_OVERLOADED);
            if (lastEvent == null) {
                event = new ThroughputEvent(new Timing(nowNanoTime, 0, 0), passedCount, deniedCount, 0, 0, overload);
                firstCheckTimeNanos = nowNanoTime;
                firstCheckTimePassedCount = passedCount;
                lastCheckOverloadNanos = overloadNanos;
            }
            else {
                ThroughputEvent newEvent = createThroughputEvent(nowNanoTime, passedCount, deniedCount, overloadNanos, state.overloadStartTimeNanos, overload);
                event = newEvent;
                listener.handleThroughputData(event);
            }
            this.lastEvent = event;
        }
        else {
            stopped.set(true);
        }
    }

    /**
     * Creates a new event to be passed to the observing client
     * 
     * @param nowNanoTime system time
     * @param passedCount number of granted permissions
     * @param deniedCount number of denied permissions
     * @param overloadNanos estimated duration during the past interval where the limiter was overloaded
     * @param overloadStartTimeNanos last overload start for delta calculation
     * @param overload true if the system is currently overloaded, otherwise false
     * @return newly created event to be passed to the observer
     */
    private ThroughputEvent createThroughputEvent(long nowNanoTime, long passedCount, long deniedCount, long overloadNanos, long overloadStartTimeNanos,
            boolean overload) {
        long intervalNanos = nowNanoTime - lastEvent.sysTimeNanos;
        long totalElapsedNanos = nowNanoTime - firstCheckTimeNanos;
        double totalPerSecondThroughput = 0;
        if (totalElapsedNanos > 0) {
            totalPerSecondThroughput = (((double) (passedCount - firstCheckTimePassedCount)) / totalElapsedNanos) * TplConstants.BILLION;
        }
        double currentPerSecondThroughput = 0;
        if (intervalNanos > 0) {
            currentPerSecondThroughput = (((double) (passedCount - lastEvent.passedCount)) / intervalNanos) * TplConstants.BILLION;
        }
        if (overload) {
            overloadNanos = computeOverloadNanosWithDelta(nowNanoTime, overloadNanos, overloadStartTimeNanos);
        }
        else {
            overloadNanos = overloadNanos - lastCheckOverloadNanos;
        }
        overloadNanos = adjustComputedOverloadTime(overloadNanos, intervalNanos);
        return new ThroughputEvent(new Timing(nowNanoTime, intervalNanos, overloadNanos), passedCount, deniedCount, currentPerSecondThroughput,
                totalPerSecondThroughput, overload);
    }

    /**
     * It can happen due to the massive concurrency that the estimated overload time is bigger than the interval or even negative. These bad values will be
     * adjusted properly.
     * 
     * @param overloadNanos computed overload time in nanoseconds during the interval
     * @param intervalNanos duration of the interval in nanoseconds
     * @return adjusted plausible overload time
     */
    private long adjustComputedOverloadTime(long overloadNanos, long intervalNanos) {
        if (overloadNanos > intervalNanos) {
            overloadNanos = intervalNanos;
        }
        else if (overloadNanos < 0) {
            overloadNanos = 0;
        }
        return overloadNanos;
    }

    /**
     * In overload state we compute the estimated overload nanos including the current delta to the overload start time.
     * 
     * @param nowNanoTime current time in nanoseconds, see {@link TimeUtils#getSystemUptimeNanos()}
     * @param overloadNanos estimated time in nanoseconds the limiter was overloaded during the past interval
     * @param overloadStartTimeNanos start time in nanoseconds the overload phase started, see {@link TimeUtils#getSystemUptimeNanos()}
     * @return overload time in past interval in nanoseconds
     */
    private long computeOverloadNanosWithDelta(long nowNanoTime, long overloadNanos, long overloadStartTimeNanos) {
        long deltaOverloadNanos = (nowNanoTime - overloadStartTimeNanos);
        if (deltaOverloadNanos < 0) {
            deltaOverloadNanos = overloadStartTimeNanos;
        }
        long increasedOverloadNanos = overloadNanos + deltaOverloadNanos;
        overloadNanos = increasedOverloadNanos - lastCheckOverloadNanos;
        if (overloadNanos < 0 && deltaOverloadNanos > 0) {
            overloadNanos = deltaOverloadNanos;
        }
        lastCheckOverloadNanos = increasedOverloadNanos;
        return overloadNanos;
    }

    @Override
    public void run() {
        if (stopped.compareAndSet(true, false)) {
            this.lastEvent = null;
            Throwable lastProblem = null;
            try {
                executeChecks();
            }
            catch (InterruptedException ex) {
                if (!stopped.get()) {
                    lastProblem = ex;
                }
                Thread.currentThread().interrupt();
            }
            catch (RuntimeException ex) {
                if (!stopped.get()) {
                    lastProblem = ex;
                }
            }
            handleAfterExecution(lastProblem);
        }
    }

    private void handleAfterExecution(Throwable lastProblem) {
        if (!stopped.get()) {
            try {
                listener.handleListenerDied(lastProblem);
            }
            catch (RuntimeException t) {
                LOGGER.error("Listener died unexpectedly", t);
            }
        }
    }

    private void executeChecks() throws InterruptedException {
        while (!stopped.get()) {
            long time = System.nanoTime();
            long waitNanos = doWait();
            if (!stopped.get()) {
                doCheck();
            }
            lastCheckOperationTimeNanos = Math.abs(System.nanoTime() - time) - waitNanos;
        }
    }
}