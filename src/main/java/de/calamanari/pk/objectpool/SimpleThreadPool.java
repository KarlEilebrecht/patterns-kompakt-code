//@formatter:off
/*
 * Simple Thread Pool
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

import java.util.ArrayList;

/**
 * Simple Thread-Pool implementation using worker pool threads.<br>
 * I originally wrote this class in 2004 and although nowadays using the Executors framework (java.util.concurrent) should be preferred(!), this simple thread
 * pool is still a good example of an object pool maintaining expensive objects (the threads), to be handed out to a caller for exclusive use and to be returned
 * afterwards to the pool for later reuse.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class SimpleThreadPool {

    /**
     * Default value for maximum number of threads ({@value} )
     */
    public static final int DEFAULT_MAX_THREADS = 25;

    /**
     * Default value for minimum number of Threads ({@value} )
     */
    public static final int DEFAULT_MIN_THREADS = 1;

    /**
     * Default value for incrementing the number of threads, if no more available and maximum not yet reached ({@value} )
     */
    public static final int DEFAULT_INCREMENT = 1;

    /**
     * Internal Lock for accessing the list of idle threads in the pool
     */
    private final Object idleListLock = new Object();

    /**
     * This lock will be used if the pool is exhausted (pool empty and maximum allowed threads out). Then we'll have to wait until the next thread will be
     * returned.
     */
    private Object poolLock = new Object();

    /**
     * Maximum number of threads in the pool
     */
    private final int maxThreads;

    /**
     * Minumum number of threads in the pool (fill at start)
     */
    private final int minThreads;

    /**
     * Number of threads for incrementing or decrementing the pool.
     */
    private final int incThreads;

    /**
     * Thread group for all managed threads.
     */
    private final ThreadGroup threadGroup = new ThreadGroup(this.getClass().getSimpleName() + " - Pool Threads");

    /**
     * List of available threads (the pool itself), guarded by IDLE_LIST_LOCK
     */
    private ArrayList<PoolThread> idleThreads = null;

    /**
     * Count of threads (idle + currently used), guarded by IDLE_LIST_LOCK
     */
    private int threadCount = 0;

    /**
     * Creates pool with default values but does not start any thread.
     */
    public SimpleThreadPool() {
        this(DEFAULT_MIN_THREADS, DEFAULT_MAX_THREADS, DEFAULT_INCREMENT, false);
    }

    /**
     * Creates a new pool with the given values.<br>
     * With incThreads the caller can configure the increment steps. If the distance between minThreads and maxThreads is not divisible by incThreads without
     * remainder, we increase minThreads to fit. The specified number of threads will be initialized but not yet started, this will happen on first usage.
     * 
     * @param minThreads initial number of threads in the pool
     * @param maxThreads maximum number of threads in the pool
     * @param incThreads count of threads for pool increment until maximum is reached.
     * @param warmUp force threads to be started initially
     */
    public SimpleThreadPool(int minThreads, int maxThreads, int incThreads, boolean warmUp) {
        if (minThreads < 0 || maxThreads < minThreads || (minThreads + maxThreads) == 0 || incThreads < 0) {
            throw new IllegalArgumentException("minThreads=" + minThreads + ", maxThreads=" + maxThreads + ", incThreads=" + incThreads
                    + " not allowed (expected: minThreads >= 0, maxThreads >= minThreads, " + "(minThreads + maxThreads) > 0, incThreads >= 0).");
        }
        if (incThreads + minThreads > maxThreads) {
            throw new IllegalArgumentException("(minThreads + incThreads) > maxThreads not allowed");
        }
        if (incThreads > 0) { // The distance between min and max must be
                              // divisible by inc without remainder
            minThreads = minThreads + ((maxThreads - minThreads) % incThreads);
        }
        else if (minThreads < maxThreads) {
            throw new IllegalArgumentException("for maxThreads > minThreads an increment > 0 must be specified.");
        }
        this.minThreads = minThreads;
        this.maxThreads = maxThreads;
        this.incThreads = incThreads;
        initialize(warmUp);
    }

    /**
     * Initializes the pool according to the settings.<br>
     * We create the minimum number of threads.
     * 
     * @param warmUp if true, all threads will be started initially
     */
    protected void initialize(boolean warmUp) {
        synchronized (idleListLock) {
            if (idleThreads == null) {
                idleThreads = new ArrayList<>(maxThreads);
                for (int i = 0; i < minThreads; i++) {
                    PoolThread thread = createNewPoolThread();
                    idleThreads.add(thread);
                    threadCount++;
                }
            }
        }
        if (warmUp) { // warm up threads (start with a no-op)
            Thread[] warmUpThreads = new Thread[threadCount];
            for (int i = 0; i < threadCount; i++) {
                warmUpThreads[i] = getNextAvailableThread();
            }
            for (int i = 0; i < threadCount; i++) {
                warmUpThreads[i].start();
            }
        }
    }

    /**
     * Creates a new pool thread.<br>
     * Subclasses may override this method to return their own PoolThread (subclass) implementation.
     * 
     * @return new pool thread instance.
     */
    protected PoolThread createNewPoolThread() {
        return new PoolThread(this);
    }

    /**
     * The FACTORY METHOD returns a Thread for executing the given Runnable. <br>
     * After the job is done the thread returns ITSELF to the pool. <br>
     * If the pool is exhausted, the method blocks until the next thread becomes available.
     * 
     * @param job Runnable to be executed, null is allowed (for warm-up only)
     * @return configured Thread (client must call start())
     */
    public Thread createThread(Runnable job) {
        PoolThread thread = getNextAvailableThread();
        thread.setNextJob(job);
        return thread;
    }

    /**
     * This method checks the pool and will resize it if necessary and possible.<br>
     * If the number of available threads is greater than (minThreads+incThreads), we will remove incThreads threads from the pool.
     * 
     * @return number of currently available threads in the pool
     */
    protected int checkIdleThreads() {
        synchronized (idleListLock) {
            if (idleThreads.size() > (minThreads + incThreads)) {
                for (int i = 0; i < incThreads; i++) {
                    PoolThread thread = idleThreads.remove(idleThreads.size() - 1);
                    thread.dispose();
                    threadCount--;
                }
            }
            else if (idleThreads.size() == 0 && threadCount < maxThreads) {
                for (int i = 0; i < incThreads; i++) {
                    PoolThread thread = createNewPoolThread();
                    idleThreads.add(thread);
                    threadCount++;
                }
            }
            return idleThreads.size();
        }

    }

    /**
     * Returns the next available thread from the pool.<br>
     * If required the pool gets resized. If resizing is not possible, this method blocks until a thread has been returned to the pool by a client.
     * 
     * @return PoolThread from the pool
     */
    protected synchronized PoolThread getNextAvailableThread() {
        PoolThread thread = null;
        while (true) {
            synchronized (idleListLock) {
                if (checkIdleThreads() > 0) {
                    thread = idleThreads.remove(idleThreads.size() - 1);
                    break;
                }
            }
            try {
                synchronized (poolLock) {
                    poolLock.wait();
                }
            }
            catch (InterruptedException ex) {
                // This should not happen at all
                // we must throw an exception because there is the danger that
                // the next POOL_LOCK.wait() will never return,
                // because the notify() happened before.
                Thread.currentThread().interrupt();
                throw new RuntimeException("Unexpected InterruptedException in ThreadPool.POOL_LOCK.wait()!");
            }
        }
        return thread;
    }

    /**
     * Return the pool thread to the idle pool.<br>
     * This method MUST ONLY be called by pool threads!
     * 
     * @param poolThread idle thread to be returned to idle pool
     */
    protected void returnThreadToIdlePool(PoolThread poolThread) {
        synchronized (idleListLock) {
            idleThreads.add(poolThread);
            synchronized (poolLock) {
                poolLock.notifyAll();
            }
        }
    }

    /**
     * Returns the internal thread group backing the pool's current threads.
     * 
     * @return thread group all pool threads belong to
     */
    public ThreadGroup getThreadGroup() {
        return threadGroup;
    }

    /**
     * Returns the number of threads maintained by the pool (idle + working).<br>
     * <b>Note:</b> To avoid deadlocks, this method should not be called from inside worker-Runnables.
     * 
     * @return number of threads
     */
    public int getThreadCount() {
        synchronized (idleListLock) {
            return threadCount;
        }
    }

    /**
     * Returns an int array [total number of threads, number of working threads, number of idle threads].<br>
     * <b>Note:</b> To avoid deadlocks, this method should not be called from inside worker-Runnables.
     * 
     * @return [total, working, idle]
     */
    public int[] getPoolInfo() {
        int total = 0;
        int idle = 0;
        synchronized (idleListLock) {
            total = this.threadCount;
            idle = this.idleThreads.size();
        }
        return new int[] { total, (total - idle), idle };
    }

}
