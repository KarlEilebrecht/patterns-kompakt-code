package de.calamanari.other;

import java.lang.management.ManagementFactory;
import java.lang.ref.WeakReference;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicLongArray;
import java.util.concurrent.atomic.AtomicReference;


/**
 * A {@link ThroughputLimiter} allows to limit and observe the number of events (i.e. executions or requests) in
 * a configurable time interval (floating), see also {@link #MAX_STORABLE_NANOS}.
 * @author <a href="mailto:Karl.Eilebrecht@freenet.de">Karl Eilebrecht</a>
 */
public class ThroughputLimiter {

    /**
     * System time (for time measurement)
     */
    private static final long STARTUP_NANO_TIME;
    static {
        long deltaNanosSinceJvmStart = (System.currentTimeMillis() - ManagementFactory.getRuntimeMXBean().getStartTime()) * 1_000_000;
        STARTUP_NANO_TIME = System.nanoTime() - deltaNanosSinceJvmStart;
    }
    
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
     * The first 10 bits encode the collisionDetectionCounter value of the request,
     * the remaining 54 bits encode [time of request nanos plus the interval length (permission lifetime)].
     */
    private final AtomicLongArray permissions;

    /**
     * length of the interval in nanoseconds, this is also a single permission's lifetime
     */
    private final long intervalNanos;
    
    /**
     * Half the interval time. We use this as the maximum single wait time for threads expecting 
     * a further chance to get a permission {@link #tryGetPermission(long, TimeUnit)}.
     */
    private final long halfIntervalNanos;
    
    /**
     * number of slots (permissions to execute at the same time)
     */
    private final int numberOfSlots;

    /**
     * constant placeholder to represent the state not overloaded
     */
    private final LoadState notOverloaded = new LoadState(-1);
    
    /**
     * If the system is overloaded this variable holds a reference to the {@link LoadState}, otherwise {@link #notOverloaded} 
     */
    private final AtomicReference<LoadState> overloadState = new AtomicReference<>(notOverloaded);
    
    /**
     * time in nanoseconds the system was overloaded (estimated time since start)
     */
    private final AtomicLong overloadNanos = new AtomicLong(0); 
    
    /**
     * Count granted permissions
     */
    private final AtomicLong passedCount = new AtomicLong(0);
    
    /**
     * Count denied permissions
     */
    private final AtomicLong deniedCount = new AtomicLong(0);

    /**
     * List of currently registered throughput checker threads (one per listener)
     */
    private List<ThroughputCheckerThread> throughputListenerCheckerThreads = new ArrayList<>();
    
    /**
     * Nanotime since system startup
     * @return positive time in nanoseconds
     */
    public static long getSystemTimeNanos() {
        long nowNanos = System.nanoTime();
        long startNanos = STARTUP_NANO_TIME;
        long deltaNanos = 0;
        if (startNanos > 0 && nowNanos < 0) {
            startNanos = startNanos - Long.MAX_VALUE;
            nowNanos = nowNanos - Long.MAX_VALUE;
        }
        if (startNanos <= 0) {
            deltaNanos = startNanos - nowNanos;
            if (deltaNanos < 0) {
                deltaNanos = deltaNanos * (-1);
            }
        }
        else {
            deltaNanos = nowNanos - startNanos;
        }
        return deltaNanos;
    }
    
    /**
     * Interval time (sys time modulo {@link #MAX_STORABLE_NANOS}
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
     * @param limit number of concurrent events in the interval, positive integer
     * @param interval length of the interval, positive value
     * @param unit time unit of the interval time
     */
    public ThroughputLimiter(int limit, long interval, TimeUnit unit) {
        if (limit <= 0) {
            throw new IllegalArgumentException("The limit must be > 0, given: " + limit);
        }
        this.permissions = new AtomicLongArray(limit);
        long intervalNanos = unit.toNanos(interval);
        if (intervalNanos <= 0 || intervalNanos > MAX_INTERVAL_TIME_NANOS) {
            throw new IllegalArgumentException("Interval time in nanoseconds out of range (1 .. " + MAX_INTERVAL_TIME_NANOS
                    + "), given: "
                    + interval + " " + unit + "(" + intervalNanos + " nanoseconds)");
        }
        this.intervalNanos = intervalNanos;
        if (intervalNanos > 1) {
            this.halfIntervalNanos = (intervalNanos / 2);
        }
        else {
            this.halfIntervalNanos = 1;
        }
        this.numberOfSlots = limit;
    }

    /**
     * After each call we update the state of the {@link ThroughputLimiter} which is either overloaded or not.
     * @param sysTimeNanos see {@link #getSystemTimeNanos()}
     * @param permissionAvailable true if the permission was granted (passed) or false (denied) otherwise
     */
    private void updateOverloadState(long sysTimeNanos, boolean permissionAvailable) {
        LoadState state = null;
        if (!permissionAvailable) {
            overloadState.compareAndSet(notOverloaded, new LoadState(sysTimeNanos));
        }
        else if ((state = overloadState.get()) != notOverloaded && overloadState.compareAndSet(state, notOverloaded)) {
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
    
    /**
     * We collect internal statistics during the {@link ThroughputLimiter}'s lifetime
     * @param sysTimeNanos 
     * @param permissionAvailable true if the permission was granted (passed) or false (denied) otherwise
     */
    private void updateCounts(long sysTimeNanos, boolean permissionAvailable) {
        if (permissionAvailable) {
            passedCount.incrementAndGet();
        }
        else {
            deniedCount.incrementAndGet();
        }        
    }

    /**
     * This method tries to get a permission but aborts the attempt after the given timeout.
     * @param timeout maximum wait time (method blocks)
     * @param unit time unit
     * @return true if a permission is available, false otherwise
     * @throws InterruptedException if the waiting thread was interrupted
     */
    public boolean tryGetPermission(long timeout, TimeUnit unit) throws InterruptedException {
        boolean permissionAvailable = false;
        boolean tryAgain = false;
        long startNanos = getSystemTimeNanos();
        long sysTime = startNanos;
        long timeoutNanos = TimeUnit.NANOSECONDS.convert(timeout, unit);
        do {
            tryAgain = false;
            permissionAvailable = tryGetPermissionInternal(sysTime);
            if (!permissionAvailable && timeoutNanos > 0) {
                tryAgain = waitForNextAttempt(startNanos, timeoutNanos);
                sysTime = getSystemTimeNanos();
            }
        }
        while (tryAgain);
        updateCounts(sysTime, permissionAvailable);
        return permissionAvailable;
    }

    /**
     * Pauses the calling thread until the next attempt to get a permission
     * @param startNanos to compute the remaining time we can wait
     * @param timeoutNanos maximum time to wait before the permission is finally denied
     * @return true if we should try again to get a permission, otherwise false (denied)
     * @throws InterruptedException if the calling thread was interrupted
     */
    private boolean waitForNextAttempt(long startNanos, long timeoutNanos) throws InterruptedException {
        boolean tryAgain = false;
        long elapsedNanos = getSystemTimeNanos() - startNanos;
        long remainingTimeoutNanos = timeoutNanos - elapsedNanos;
        if (remainingTimeoutNanos > 0) {
            long latchWaitNanos = remainingTimeoutNanos;
            if (latchWaitNanos > halfIntervalNanos) {
                latchWaitNanos = halfIntervalNanos;
            }
            LoadState state = overloadState.get();
            if (state != notOverloaded) {
                state.waitLatch.await(latchWaitNanos, TimeUnit.NANOSECONDS);
            }
            tryAgain = true;
        }
        return tryAgain;
    }
    
    /**
     * Tries to get a permission at the given system time.
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
            long permissionBase = ((long) ((ticketNumber % MAX_COLLISION_DETECTION_LIMIT) - HALF_COLLISION_DETECTION_LIMIT) << 54);
            long newPermission = permissionBase + newExpiryTime;
            permissionAvailable = permissions.compareAndSet(slot, oldPermission, newPermission);
        }
        updateOverloadState(sysTimeNanos, permissionAvailable);
        return permissionAvailable;
    }

    /**
     * Checks whether a permission is available (will be instantly acquired) or not.
     * @return true if a permission is available, false otherwise
     */
    public boolean tryGetPermission() {
        long sysTime = getSystemTimeNanos();
        boolean permissionAvailable = tryGetPermissionInternal(sysTime);
        updateCounts(sysTime, permissionAvailable);
        return permissionAvailable;
    }
    
    /**
     * This method blocks until a permission becomes available.
     * @throws InterruptedException
     */
    public void getPermission() throws InterruptedException {
        tryGetPermission(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
    }

    /**
     * Registers the given listener. If it was already registered, the operation only adjusts the interval time, if necessary.
     * <p>
     * <b>Note:</b><br>
     * A {@link ThroughputListener} runs in its own thread, the measurement interval is totally independent from the {@link ThroughputLimiter}'s 
     * limitation interval. This can affect the computations of the listener and may lead to irritating numbers, such as the throughput per second.
     * <p>
     * The listener's interval time should be aligned to the <i>full second</i>. Otherwise reported throughput per second
     * may be reported too high or too low from time to time or even toggle between a way too high and a corresponding too low value. <br>
     * However, you may still see unexpected high values for the throughput per second, if the occurrences of incoming events
     * are not uniformly distributed across the measurement interval. <p>
     * Example:<br>
     * Imagine 100 events allowed in a 5-seconds-interval and a listener configured with a 2-seconds-interval. 
     * One would naively expect 20 events per second throughput.<br>
     * But this can be wrong: 100 events might occur during the first second. 
     * Because the limiter will automatically cut the throughput, the computed value reported by the listener will 50 per second
     * correctly reported for the current interval, followed by 0 per second reported for the next interval and <i>something</i> 
     * for the following interval.<p>
     * <b>Keep the above effects in mind when interpreting the reported data!</b>
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
        long intervalTimeNanos = 1_000_000L * intervalTimeMillis;
        if (!adjustExistingListener(listener, intervalTimeNanos)) {
            ThroughputCheckerThread checkerThread = new ThroughputCheckerThread(this, listener, intervalTimeNanos);
            this.throughputListenerCheckerThreads.add(checkerThread);
            checkerThread.start();
        }
    }

    /**
     * If the given listener already exists, we adjust the interval time
     * @param listener 
     * @param intervalTimeNanos
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
     * @return number of granted permissions
     */
    public long getNumberOfGrantedPermissions() {
        return this.passedCount.get();
    }

    /**
     * Returns the number of permissions this listener denied since start 
     * @return number of denied permissions
     */
    public long getNumberOfDeniedPermissions() {
        return this.deniedCount.get();
    }
    
    @Override
    protected void finalize() throws Throwable {
        try {
            removeAllThroughputListeners();
        }
        finally {
            super.finalize();
        }
    }

    /**
     * A {@link ThroughputEvent} contains some KPIs about a {@link ThroughputLimiter}'s current state.
     * @author <a href="mailto:Karl.Eilebrecht@freenet.de">Karl Eilebrecht</a>
     *
     */
    public static class ThroughputEvent {
        
        /**
         * clock UTC-time (System.currentTimeMillis())
         */
        final long eventTimeMillis = System.currentTimeMillis();
        
        /**
         * elapsed time (total) in nanoseconds
         */
        final long sysTimeNanos;
        
        /**
         * duration of the past interval in nanoseconds
         */
        final long intervalNanos;
        
        /**
         * estimated time in nanoseconds the system was overloaded during the past interval
         */
        final long intervalOverloadNanos;
        
        /**
         * number of granted permissions since limiter started to work
         */
        final long passedCount;
        
        /**
         * number of denied permissions since limiter started to work
         */
        final long deniedCount;
        
        /**
         * true if the limiter is currently overloaded, otherwise false
         */
        final boolean overloaded;
        
        /**
         * average throughput during the past interval (granted permissions per second)
         */
        final double intervalPerSecondThroughput;
        
        /**
         * average throughput since measurement started (granted permissions per second)
         */
        final double totalPerSecondThroughput;

        /**
         * Creates a new throughput event to be reported
         * @param sysTimeNanos
         * @param intervalNanos
         * @param intervalOverloadNanos
         * @param passedCount
         * @param deniedCount
         * @param currentPerSecondThroughput
         * @param totalPerSecondThroughput
         * @param overloaded
         */
        public ThroughputEvent(long sysTimeNanos, long intervalNanos, long intervalOverloadNanos, long passedCount, long deniedCount, double currentPerSecondThroughput, double totalPerSecondThroughput, boolean overloaded) {
            this.sysTimeNanos = sysTimeNanos;
            this.intervalNanos = intervalNanos;
            this.intervalOverloadNanos = intervalOverloadNanos;
            this.passedCount = passedCount;
            this.deniedCount = deniedCount;
            this.overloaded = overloaded;
            this.intervalPerSecondThroughput = currentPerSecondThroughput;
            this.totalPerSecondThroughput = totalPerSecondThroughput;
        }

        @Override
        public String toString() {
            NumberFormat nf = NumberFormat.getInstance(Locale.US);
            nf.setGroupingUsed(false);
            nf.setMaximumFractionDigits(2);
            nf.setMinimumFractionDigits(2);
            return this.getClass().getSimpleName() + "[eventTimeMillis=" + eventTimeMillis + ", sysTimeNanos=" + sysTimeNanos + ", intervalNanos=" + intervalNanos + ", intervalOverloadNanos=" + intervalOverloadNanos
                    + ", passedCount=" + passedCount + ", deniedCount=" + deniedCount + ", overloaded=" + overloaded
                    + ", totalThroughput=" + nf.format(totalPerSecondThroughput) + "/s, intervalThroughput=" + nf.format(intervalPerSecondThroughput) + "/s]";
        }

        /**
         * Returns clock UTC-time in milliseconds, the event was created (System.currentTimeMillis())
         * @return time in milliseconds
         */
        public long getEventTimeMillis() {
            return eventTimeMillis;
        }
        
        /**
         * @return elapsed time (total) in nanoseconds
         */
        public long getSysTimeNanos() {
            return sysTimeNanos;
        }

        /**
         * @return duration of the past interval in nanoseconds
         */
        public long getIntervalNanos() {
            return intervalNanos;
        }

        /**
         * @return estimated time in nanoseconds the system was overloaded during the past interval
         */
        public long getIntervalOverloadNanos() {
            return intervalOverloadNanos;
        }

        /**
         * @return number of granted permissions since limiter started to work
         */
        public long getPassedCount() {
            return passedCount;
        }

        /**
         * @return number of denied permissions since limiter started to work
         */
        public long getDeniedCount() {
            return deniedCount;
        }

        /**
         * @return true if the {@link ThroughputLimiter} is currently overloaded, otherwise false
         */
        public boolean isOverloaded() {
            return overloaded;
        }

        /**
         * @return average throughput during the past interval (granted permissions per second)
         */
        public double getIntervalPerSecondThroughput() {
            return intervalPerSecondThroughput;
        }

        /**
         * @return average throughput since measurement started (granted permissions per second)
         */
        public double getTotalPerSecondThroughput() {
            return totalPerSecondThroughput;
        }
        
    }
    

    /**
     * Clients may register a {@link ThroughputListener} to monitor the system.<br>
     * The listener will be called periodically in a separate thread.
     * @author <a href="mailto:Karl.Eilebrecht@freenet.de">Karl Eilebrecht</a>
     */
    public static interface ThroughputListener {

        /**
         * Notifies the listener about the current state of the {@link ThroughputLimiter}.<p>
         * <b>Note:</b> This method runs synchronously related to the checker thread. On the one hand there
         * cannot be a second call in parallel. On the other hand a blocking or overly long executing listener method
         * impacts the monitoring as it delays the next notification. Thus concrete implementations should run fast.
         * @param event current status of the observed {@link ThroughputLimiter}
         */
        public void handleThroughputData(ThroughputEvent event);
        
        /**
         * This method will be called if the periodically running checker thread has died for an unknown reason.
         * @param problem throwable which might give a hint why the listener died
         */
        public void handleListenerDied(Throwable problem);
        
    }
    
    /**
     * A {@link ThroughputCheckerThread} manages a {@link ThroughputListener} at runtime.<br>
     * It is obviously impossible to notify the observer synchronously since there are possibly millions of
     * concurrent calls to a {@link ThroughputLimiter}. Thus the {@link ThroughputCheckerThread} periodically
     * computes the current state and notifies the registered listener. 
     * @author <a href="mailto:Karl.Eilebrecht@freenet.de">Karl Eilebrecht</a>
     */
    public static final class ThroughputCheckerThread extends Thread {
        
        /**
         * life-cycle control true->false->true
         */
        private final AtomicBoolean stopped = new AtomicBoolean(true);
        
        /**
         * Observer which monitors the limiter
         */
        final ThroughputListener listener;
        
        /**
         * Reference to the observed {@link ThroughputLimiter}, we use
         * a weak reference to support garbage collection in case the listener never de-registers. 
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
         * @param limiter
         * @param listener
         */
        public ThroughputCheckerThread(ThroughputLimiter limiter, ThroughputListener listener, long intervalTimeNanos) {
            this.setDaemon(true);
            this.limiter = new WeakReference<>(limiter);
            this.listener = listener;
            this.intervalTimeNanos = intervalTimeNanos;
        }
        
        /**
         * Waits for the currently configured time
         * @return wait nanos
         * @throws InterruptedException
         */
        private long doWait() throws InterruptedException {
            long waitNanos = intervalTimeNanos - lastCheckOperationTimeNanos;
            if (waitNanos > 0) {
                long waitMillis = waitNanos / 1_000_000;
                long remainingWaitNanos = waitNanos - (waitMillis * 1_000_000);
                Thread.sleep(waitMillis, (int)remainingWaitNanos);
            }
            return waitNanos;
        }

        /**
         * This method will be called periodically to convert the current state of the {@link ThroughputLimiter} into
         * a {@link ThroughputEvent} to be passed to the registered listener.
         */
        private void doCheck() {
            ThroughputLimiter instance = limiter.get();
            if (instance != null) {
                long passedCount = instance.passedCount.get();
                long deniedCount = instance.deniedCount.get();
                long overloadNanos = instance.overloadNanos.get();
                LoadState state = instance.overloadState.get();
                ThroughputEvent event = null;
                long nowNanoTime =  getSystemTimeNanos();
                boolean overload = (state != instance.notOverloaded);
                if (lastEvent == null) {
                    event = new ThroughputEvent(nowNanoTime, 0, 0, passedCount, deniedCount, 0, 0, overload);
                    firstCheckTimeNanos = nowNanoTime;
                    firstCheckTimePassedCount = passedCount;
                    lastCheckOverloadNanos = overloadNanos;
                }
                else {
                    ThroughputEvent newEvent = createThroughputEvent(nowNanoTime, passedCount, deniedCount,
                            overloadNanos, state.overloadStartTimeNanos, overload);
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
         * @param nowNanoTime system time
         * @param passedCount number of granted permissions
         * @param deniedCount number of denied permissions
         * @param overloadNanos estimated duration during the past interval where the limiter was overloaded
         * @param overloadStartTimeNanos last overload start for delta calculation  
         * @param overload true if the system is currently overloaded, otherwise false
         * @return newly created event to be passed to the observer
         */
        private ThroughputEvent createThroughputEvent(long nowNanoTime, long passedCount, long deniedCount,
                long overloadNanos, long overloadStartTimeNanos, boolean overload) {
            long intervalNanos = nowNanoTime - lastEvent.sysTimeNanos;
            long totalElapsedNanos = nowNanoTime - firstCheckTimeNanos;
            double totalPerSecondThroughput = 0;
            if (totalElapsedNanos > 0) {
                totalPerSecondThroughput = (((double)(passedCount - firstCheckTimePassedCount)) / totalElapsedNanos) *  1_000_000_000;
            }
            double currentPerSecondThroughput = 0;
            if (intervalNanos > 0) {
                currentPerSecondThroughput = (((double)(passedCount - lastEvent.passedCount)) / intervalNanos) *  1_000_000_000;
            }
            if (overload) {
                overloadNanos = computeOverloadNanosWithDelta(nowNanoTime, overloadNanos, overloadStartTimeNanos);
            }
            else {
                overloadNanos = overloadNanos - lastCheckOverloadNanos;
            }
            overloadNanos = adjustComputedOverloadTime(overloadNanos, intervalNanos);
            ThroughputEvent newEvent = new ThroughputEvent(nowNanoTime, intervalNanos, overloadNanos, passedCount, deniedCount, currentPerSecondThroughput, totalPerSecondThroughput, overload);
            return newEvent;
        }

        /**
         * It can happen due to the massive concurrency that the estimated overload time is bigger
         * than the interval or even negative. These bad values will be adjusted properly.
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
         * @param nowNanoTime
         * @param overloadNanos
         * @param overloadStartTimeNanos
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
                    while (!stopped.get()) {
                        long time = System.nanoTime();
                        long waitNanos = doWait();
                        if (!stopped.get()) {
                            doCheck();
                        }
                        lastCheckOperationTimeNanos = Math.abs(System.nanoTime() - time) - waitNanos;
                    }
                }
                catch (Throwable ex) {
                    if (!stopped.get()) {
                        lastProblem = ex;
                    }
                }
                if (!stopped.get()) {
                    try {
                        listener.handleListenerDied(lastProblem);
                    }
                    catch (Throwable t) {
                        t.printStackTrace();
                    }
                }
            }
        }
    }
    
    /**
     * Represents the state (overload state) of the system 
     * including an overload start time and a latch (where others can wait for)
     */
    public static final class LoadState {

        /**
         * system when overload started
         */
        final long overloadStartTimeNanos;
        
        /**
         * Threads use this latch to wait for the end of the overload state.
         */
        final CountDownLatch waitLatch = new CountDownLatch(1);

        /**
         * Creates new overload state using the given start time
         * @param startTimeNanos
         */
        public LoadState(long startTimeNanos) {
            this.overloadStartTimeNanos = startTimeNanos;
        }

    }
    
}
