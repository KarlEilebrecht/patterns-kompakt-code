/*
 * Pool thread
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
package de.calamanari.pk.objectpool;

import java.util.logging.Logger;

/**
 * Managed thread for pooling.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class PoolThread extends Thread {

    /**
     * logger
     */
    public static final Logger LOGGER = Logger.getLogger(PoolThread.class.getName());

    /**
     * number of digits in hashcode
     */
    private static final int HASH_CODE_LENGTH = 8;
    
    /**
     * Thread knows the pool it is owned by.
     */
    protected final SimpleThreadPool threadPool;

    /**
     * This lock manages the waiting mode of a thread
     */
    private final Object lock = new Object();

    /**
     * Runnable to be executed by the thread. The variable is declared volatile to be sure the thread sees the new
     * content after notification.
     */
    private volatile Runnable job = null;

    /**
     * Time job execution started.
     */
    private volatile long jobStartTimeMillis = 0;

    /**
     * This flag will be set to true if a thread should be removed from the pool and thus no longer execute new jobs.
     * Guarded by LOCK.
     */
    private boolean disposed = false;

    /**
     * store default name (cache), only accessed by this thread.
     */
    private String techName = null;

    /**
     * Constructor<br>
     * Creates a new thread.<br>
     * The thread is configured to be a deamon.
     * @param threadPool backward reference to the pool for interaction.
     */
    protected PoolThread(SimpleThreadPool threadPool) {
        super(threadPool.getThreadGroup(), "");
        this.threadPool = threadPool;
        setDaemon(true);
    }

    /**
     * This method will be EXCLUSIVELY called by job management to set the next job.
     * @param nextJob some runnable
     */
    protected void setNextJob(Runnable nextJob) {
        this.job = nextJob;
    }

    /**
     * Starts resp. reactivates the thread to do a job.
     */
    @Override
    public void start() {
        synchronized (lock) {
            if (this.isAlive()) {
                if (!disposed) {
                    lock.notify();
                }
                else {
                    throw new IllegalStateException("PoolThread is already disposed!");
                }
                return;
            }
        }
        super.start();
    }

    /**
     * The run()-method executes until the thread's disposal. It MUST NOT be called from outside!
     */
    @Override
    public void run() {
        if (Thread.currentThread() != this) {
            throw new IllegalStateException(this.getClass().getSimpleName() + ".run() must not be called directly!");
        }
        setupState();
        do {
            try {
                if (job != null) {
                    this.executeJob();
                    this.resetState();
                }
                returnToPoolAndWait();
            }
            catch (Throwable t) {
                t.printStackTrace();
                this.resetState();
            }
        } while (!disposed);
    }

    /**
     * Returns the time when the current job execution was started.
     * @return execution start time of current job, 0 if no job is currently executing
     */
    protected long getJobStartTimeMillis() {
        return jobStartTimeMillis;
    }

    /**
     * Creates the initial state for the thread that is wrapped by this instance.<br>
     * This method will be called once inside the worker thread.<br>
     * Implementation MUST NOT throw any exception!
     */
    protected void setupState() {
        String hash = ("00000000" + Integer.toHexString(hashCode()));
        hash = hash.substring(hash.length() - HASH_CODE_LENGTH);
        this.techName = "PoolThread-@" + hash;
        this.setName(techName);
    }

    /**
     * Clears the state of this pool thread, removes any (unwanted) settings <br>
     * evtl. collected during a job execution.<br>
     * This method will be called repeatedly inside the worker thread.<br>
     * Implementation MUST NOT throw any exception!
     */
    protected void resetState() {
        job = null;
        // name could have been changed by the client
        this.setName(techName);
        this.jobStartTimeMillis = 0;
    }

    /**
     * Executes the current job.
     */
    protected void executeJob() {
        this.jobStartTimeMillis = System.currentTimeMillis();
        job.run();
    }

    /**
     * Return this pool thread and wait for next job execution.
     * 
     * @throws InterruptedException pass-through from waiting
     */
    protected void returnToPoolAndWait() throws InterruptedException {
        synchronized (lock) {
            threadPool.returnThreadToIdlePool(this);
            lock.wait();
        }
    }

    /**
     * This method is used by the pool to clean-up a worker thread that is no longer required.<br>
     * The method must not be called in any other thread-state than idle!
     */
    protected void dispose() {
        synchronized (lock) {
            disposed = true;
            // call comes from pool management while this thread
            // is in wait mode, now
            // notify the thread that it can die gracefully
            lock.notify();
        }
    }

}