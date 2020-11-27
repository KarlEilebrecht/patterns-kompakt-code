//@formatter:off
/*
 * Throughput Limiter
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

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongArray;
import java.util.concurrent.atomic.AtomicReference;

import de.calamanari.pk.util.TimeUtils;

/**
 * A {@link ThroughputLimiter} allows to limit and observe the number of events (i.e. executions or requests) in a configurable time interval (floating), see
 * also {@link #MAX_STORABLE_NANOS}.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public final class ThroughputLimiter {

    /**
     * Number of bits to encode the permission lifetime
     */
    private static final int TIME_PART_BIT_COUNT = 54;

    /**
     * Maximum storable time is 2^54 = {@value} nanoseconds (ca. 6 months).
     */
    private static final long MAX_STORABLE_NANOS = 18_014_398_509_481_984L;

    /**
     * Maximum interval time is {@value} nanoseconds, round about 3 months.
     */
    public static final long MAX_INTERVAL_TIME_NANOS = (MAX_STORABLE_NANOS / 2);

    /**
     * we can detect max {@value} x slots really concurrent requests in the same nanosecond (time precision varies on different systems).
     */
    private static final int MAX_COLLISION_DETECTION_LIMIT = 1024;

    /**
     * for internal calculation
     */
    private static final int HALF_COLLISION_DETECTION_LIMIT = (MAX_COLLISION_DETECTION_LIMIT / 2);

    /**
     * each request to execute gets a ticket number (rolling)
     */
    private final AtomicInteger ticketCounter = new AtomicInteger(0);

    /**
     * Each entry in this array represents a permission to execute in the configured interval.<br>
     * The first 10 bits encode the collisionDetectionCounter value of the request, the remaining 54 bits encode [time of request nanos plus the interval length
     * (permission lifetime)].
     */
    private final AtomicLongArray permissions;

    /**
     * length of the interval in nanoseconds, this is also a single permission's lifetime
     */
    private final long intervalNanos;

    /**
     * Half the interval time. We use this as the maximum single wait time for threads expecting a further chance to get a permission
     * {@link #tryGetPermission(long, TimeUnit)}.
     */
    private final long halfIntervalNanos;

    /**
     * number of slots (permissions to execute at the same time)
     */
    private final int numberOfSlots;

    /**
     * If the system is overloaded this variable holds a reference to the {@link OverloadState}, otherwise {@link #notOverloaded}
     */
    final AtomicReference<OverloadState> overloadState = new AtomicReference<>(OverloadState.NOT_OVERLOADED);

    /**
     * time in nanoseconds the system was overloaded (estimated time since start)
     */
    final AtomicLong overloadNanos = new AtomicLong(0);

    /**
     * Count granted permissions
     */
    final AtomicLong passedCount = new AtomicLong(0);

    /**
     * Count denied permissions
     */
    final AtomicLong deniedCount = new AtomicLong(0);

    /**
     * List of currently registered throughput checker threads (one per listener)
     */
    private List<ThroughputCheckerThread> throughputListenerCheckerThreads = new ArrayList<>();

    /**
     * Interval time (sys time modulo {@link #MAX_STORABLE_NANOS}
     * 
     * @param sysTimeNanos {@link #getSystemTimeNanos()}
     * @return positive time in nanoseconds
     */
    private static final long computeIntervalTimeNanos(long sysTimeNanos) {
        long intervalTimeNanos = sysTimeNanos;
        if (intervalTimeNanos > MAX_STORABLE_NANOS) {
            long elapsedPeriods = sysTimeNanos / MAX_STORABLE_NANOS;
            intervalTimeNanos = sysTimeNanos - (MAX_STORABLE_NANOS * elapsedPeriods);
        }
        return intervalTimeNanos;
    }

    /**
     * Creates a {@link ThroughputLimiter} which allows <i>limit</i> events in the specified interval.
     * 
     * @param limit number of concurrent events in the interval, positive integer
     * @param interval length of the interval, positive value
     * @param unit time unit of the interval time
     */
    public ThroughputLimiter(int limit, long interval, TimeUnit unit) {
        if (limit <= 0) {
            throw new IllegalArgumentException("The limit must be > 0, given: " + limit);
        }
        this.permissions = new AtomicLongArray(limit);
        long intervalTimeNanos = unit.toNanos(interval);
        if (intervalTimeNanos <= 0 || intervalTimeNanos > MAX_INTERVAL_TIME_NANOS) {
            throw new IllegalArgumentException("Interval time in nanoseconds out of range (1 .. " + MAX_INTERVAL_TIME_NANOS + "), given: " + interval + " "
                    + unit + "(" + intervalTimeNanos + " nanoseconds)");
        }
        this.intervalNanos = intervalTimeNanos;
        if (intervalTimeNanos > 1) {
            this.halfIntervalNanos = (intervalTimeNanos / 2);
        }
        else {
            this.halfIntervalNanos = 1;
        }
        this.numberOfSlots = limit;
    }

    /**
     * After each call we update the state of the {@link ThroughputLimiter} which is either overloaded or not.
     * 
     * @param sysTimeNanos see {@link #getSystemTimeNanos()}
     * @param permissionAvailable true if the permission was granted (passed) or false (denied) otherwise
     */
    private void updateOverloadState(long sysTimeNanos, boolean permissionAvailable) {
        if (!permissionAvailable) {
            if (overloadState.get() == OverloadState.NOT_OVERLOADED) {
                overloadState.compareAndSet(OverloadState.NOT_OVERLOADED, new OverloadState(sysTimeNanos));
            }
        }
        else {
            OverloadState state = overloadState.get();
            if (state != OverloadState.NOT_OVERLOADED && overloadState.compareAndSet(state, OverloadState.NOT_OVERLOADED)) {
                state.waitLatch.countDown();
                if (sysTimeNanos > state.overloadStartTimeNanos) {
                    long estimatedOverloadTime = sysTimeNanos - state.overloadStartTimeNanos;
                    if (estimatedOverloadTime > intervalNanos) {
                        estimatedOverloadTime = intervalNanos;
                    }
                    overloadNanos.addAndGet(estimatedOverloadTime);
                }
            }
        }
    }

    /**
     * We collect internal statistics during the {@link ThroughputLimiter}'s lifetime
     * 
     * @param permissionAvailable true if the permission was granted (passed) or false (denied) otherwise
     */
    private void updateCounts(boolean permissionAvailable) {
        if (permissionAvailable) {
            passedCount.incrementAndGet();
        }
        else {
            deniedCount.incrementAndGet();
        }
    }

    /**
     * This method tries to get a permission but aborts the attempt after the given timeout.
     * 
     * @param timeout maximum wait time (method blocks)
     * @param unit time unit
     * @return true if a permission is available, false otherwise
     * @throws InterruptedException if the waiting thread was interrupted
     */
    public boolean tryGetPermission(long timeout, TimeUnit unit) throws InterruptedException {
        boolean permissionAvailable = false;
        boolean tryAgain = false;
        long startNanos = TimeUtils.getSystemUptimeNanos();
        long sysTime = startNanos;
        long timeoutNanos = TimeUnit.NANOSECONDS.convert(timeout, unit);
        do {
            tryAgain = false;
            permissionAvailable = tryGetPermissionInternal(sysTime);
            if (!permissionAvailable && timeoutNanos > 0) {
                tryAgain = waitForNextAttempt(startNanos, timeoutNanos);
                sysTime = TimeUtils.getSystemUptimeNanos();
            }
        } while (tryAgain);
        updateCounts(permissionAvailable);
        return permissionAvailable;
    }

    /**
     * Pauses the calling thread until the next attempt to get a permission
     * 
     * @param startNanos to compute the remaining time we can wait
     * @param timeoutNanos maximum time to wait before the permission is finally denied
     * @return true if we should try again to get a permission, otherwise false (denied)
     * @throws InterruptedException if the calling thread was interrupted
     */
    // Ignoring squid:S899 because this await() SonarLint complains about is semantically
    // an OR (either the overload has been fixed or the wait time elapsed)
    // in either case we need to try again
    @SuppressWarnings("squid:S899")
    private boolean waitForNextAttempt(long startNanos, long timeoutNanos) throws InterruptedException {
        boolean tryAgain = false;
        long elapsedNanos = TimeUtils.getSystemUptimeNanos() - startNanos;
        long remainingTimeoutNanos = timeoutNanos - elapsedNanos;
        if (remainingTimeoutNanos > 0) {
            long latchWaitNanos = remainingTimeoutNanos;
            if (latchWaitNanos > halfIntervalNanos) {
                latchWaitNanos = halfIntervalNanos;
            }
            overloadState.get().waitLatch.await(latchWaitNanos, TimeUnit.NANOSECONDS);
            tryAgain = true;
        }
        return tryAgain;
    }

    /**
     * Tries to get a permission at the given system time.
     * 
     * @param sysTimeNanos see {@link #getSystemTimeNanos()}
     * @return true if a permission is available, false otherwise
     */
    private boolean tryGetPermissionInternal(long sysTimeNanos) {
        boolean permissionAvailable = false;
        long newExpiryTime = computeIntervalTimeNanos(sysTimeNanos) + intervalNanos;
        if (newExpiryTime > MAX_STORABLE_NANOS) {
            newExpiryTime = newExpiryTime - MAX_STORABLE_NANOS;
        }
        int ticketNumber = ticketCounter.incrementAndGet();
        if (ticketNumber < 0) {
            ticketNumber = ticketNumber + Integer.MIN_VALUE;
        }
        int slot = ticketNumber % numberOfSlots;
        long oldPermission = permissions.get(slot);
        long oldExpiryTime = (oldPermission & (MAX_STORABLE_NANOS - 1));
        long diffTime = newExpiryTime - oldExpiryTime;
        if (diffTime >= intervalNanos || (diffTime * -1) > intervalNanos) {
            long permissionBase = ((long) ((ticketNumber % MAX_COLLISION_DETECTION_LIMIT) - HALF_COLLISION_DETECTION_LIMIT) << TIME_PART_BIT_COUNT);
            long newPermission = permissionBase + newExpiryTime;
            permissionAvailable = permissions.compareAndSet(slot, oldPermission, newPermission);
        }
        updateOverloadState(sysTimeNanos, permissionAvailable);
        return permissionAvailable;
    }

    /**
     * Checks whether a permission is available (will be instantly acquired) or not.
     * 
     * @return true if a permission is available, false otherwise
     */
    public boolean tryGetPermission() {
        long sysTime = TimeUtils.getSystemUptimeNanos();
        boolean permissionAvailable = tryGetPermissionInternal(sysTime);
        updateCounts(permissionAvailable);
        return permissionAvailable;
    }

    /**
     * This method blocks until a permission becomes available.
     * 
     * @throws InterruptedException if the waiting thread was interrupted while waiting for a permission
     */
    public void getPermission() throws InterruptedException {
        tryGetPermission(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
    }

    /**
     * Registers the given listener. If it was already registered, the operation only adjusts the interval time, if necessary.
     * <p>
     * <b>Note:</b><br>
     * A {@link ThroughputListener} runs in its own thread, the measurement interval is totally independent from the {@link ThroughputLimiter}'s limitation
     * interval. This can affect the computations of the listener and may lead to irritating numbers, such as the throughput per second.
     * <p>
     * The listener's interval time should be greater or equal to the limiter's interval and aligned to the <i>full second</i>. Otherwise reported throughput
     * per second may be reported too high or too low from time to time or even toggle between a way too high and a corresponding too low value. <br>
     * <p>
     * Example:<br>
     * Imagine 100 events allowed in a 5-seconds-interval and a listener configured with a 2-seconds-interval. One would naively expect 20 events per second
     * throughput.<br>
     * But this can be wrong: 100 events might occur during the first second. Because the limiter will automatically cut the throughput, the computed value
     * reported by the listener will be 50 per second correctly reported for the current interval, followed by 0 per second reported for the next interval and
     * <i>something</i> for the following interval.
     * <p>
     * <b>Keep in mind the effects mentioned above when interpreting the reported data!</b>
     * 
     * @param listener the observer to be registered NOT NULL
     * @param intervalTimeMillis positive number of milliseconds (interval duration)
     */
    public synchronized void registerThroughputListener(ThroughputListener listener, int intervalTimeMillis) {
        if (listener == null) {
            throw new IllegalArgumentException("Argument listener must not be null.");
        }
        if (intervalTimeMillis <= 0) {
            throw new IllegalArgumentException("Argument intervalTimeMillis must be a positive value, given: " + intervalTimeMillis);
        }
        long intervalTimeNanos = ((long) TplConstants.MILLION) * intervalTimeMillis;
        if (!adjustExistingListener(listener, intervalTimeNanos)) {
            ThroughputCheckerThread checkerThread = new ThroughputCheckerThread(this, listener, intervalTimeNanos);
            this.throughputListenerCheckerThreads.add(checkerThread);
            checkerThread.start();
        }
    }

    /**
     * If the given listener already exists, we adjust the interval time
     * 
     * @param listener observer
     * @param intervalTimeNanos wait time between two checks
     * @return true if the listener already existed, otherwise false
     */
    private boolean adjustExistingListener(ThroughputListener listener, long intervalTimeNanos) {
        boolean listenerExisted = false;
        for (ThroughputCheckerThread existingThread : this.throughputListenerCheckerThreads) {
            if (existingThread.listener.equals(listener)) {
                if (existingThread.intervalTimeNanos != intervalTimeNanos) {
                    existingThread.intervalTimeNanos = intervalTimeNanos;
                }
                listenerExisted = true;
            }
        }
        return listenerExisted;
    }

    /**
     * Removes the given listener, if it was registered.
     * 
     * @param listener observer to be removed or <i>null</i> to force removal
     */
    public synchronized void removeThroughputListener(ThroughputListener listener) {
        for (int i = 0; i < this.throughputListenerCheckerThreads.size(); i++) {
            ThroughputCheckerThread existingThread = this.throughputListenerCheckerThreads.get(i);
            if (existingThread.listener.equals(listener)) {
                existingThread.stopCheck();
                this.throughputListenerCheckerThreads.remove(i);
                break;
            }
        }
    }

    /**
     * Removes all the registered listeners
     */
    public synchronized void removeAllThroughputListeners() {
        for (ThroughputCheckerThread existingThread : this.throughputListenerCheckerThreads) {
            existingThread.stopCheck();
        }
        this.throughputListenerCheckerThreads.clear();
    }

    /**
     * Returns the number of permissions this listener granted since start
     * 
     * @return number of granted permissions
     */
    public long getNumberOfGrantedPermissions() {
        return this.passedCount.get();
    }

    /**
     * Returns the number of permissions this listener denied since start
     * 
     * @return number of denied permissions
     */
    public long getNumberOfDeniedPermissions() {
        return this.deniedCount.get();
    }

    /**
     * Returns the configured interval length in nanoseconds
     * 
     * @return interval time in nanoseconds
     */
    public long getIntervalTimeNanos() {
        return this.intervalNanos;
    }

    /**
     * Returns the maximum number of permissions to be granted during an interval (see {@link #getIntervalTimeNanos()}).
     * 
     * @return allowed number of permissions per interval
     */
    public int getMaxNumberOfPermissions() {
        return numberOfSlots;
    }

}
