/*
 * Example Object Pool - demonstrates an OBJECT POOL.
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

import java.util.ArrayList;
import java.util.concurrent.Semaphore;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

/**
 * Example Object Pool - demonstrates an OBJECT POOL.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class ExampleObjectPool {

    /**
     * logger
     */
    public static final Logger LOGGER = Logger.getLogger(ExampleObjectPool.class.getName());

    /**
     * maximum pool size
     */
    public static final int MAX_POOL_SIZE = 4;

    /**
     * This lock will be used for concurrent access to the pool.
     */
    private final ReentrantLock poolLock = new ReentrantLock(true);

    /**
     * List of available instances (the pool itself)
     */
    private final ArrayList<ExampleReusableObject> idleInstances = new ArrayList<>();

    /**
     * Only a fixed number of clients are allowed use the pool at the same time
     */
    private final Semaphore poolUsageSemaphore = new Semaphore(MAX_POOL_SIZE, true);

    /**
     * Creates the initially empty pool
     */
    public ExampleObjectPool() {
    }

    /**
     * Acquires a free pooled instance from the pool and returns it.<br>
     * The pool will auto-resize until the maximum number of instances is reached. If the maximum number has already
     * been reached, a call to this method blocks until an instance will be returned.<br>
     * The caller is responsible for returning an acquired instance.
     * @return instance of pooled object
     */
    public ExampleReusableObject acquireInstance() {
        ExampleReusableObject res = null;
        LOGGER.fine(this.getClass().getSimpleName() + "(" + Thread.currentThread().getName()
                + ").acquireInstance called ...");

        try {
            poolUsageSemaphore.acquire();
        }
        catch (InterruptedException ex) {
            throw new RuntimeException("Pool error", ex);
        }

        LOGGER.fine(this.getClass().getSimpleName() + "(" + Thread.currentThread().getName() + "): accessing pool ...");

        poolLock.lock();
        try {
            if (idleInstances.size() > 0) {
                LOGGER.fine(this.getClass().getSimpleName() + "(" + Thread.currentThread().getName()
                        + "): idle instance available.");
                res = idleInstances.remove(0);
            }
        }
        finally {
            poolLock.unlock();
        }
        if (res == null) {
            LOGGER.fine(this.getClass().getSimpleName() + "(" + Thread.currentThread().getName()
                    + "): no idle instance available, increasing pool size.");
            res = new ExampleReusableObject();
        }
        LOGGER.fine(this.getClass().getSimpleName() + "(" + Thread.currentThread().getName()
                + "): instance successfully acquired.");
        return res;
    }

    /**
     * This method simulates cleanup work to guarantee that the next user gets a "fresh" instance.
     * @param instance an object to be cleaned after returning
     */
    private void cleanReturnedInstance(ExampleReusableObject instance) {
        LOGGER.fine(this.getClass().getSimpleName() + "(" + Thread.currentThread().getName()
                + "): cleanReturnedInstance called ...");
        instance.reset();
        LOGGER.fine(this.getClass().getSimpleName() + "(" + Thread.currentThread().getName()
                + "): returned instance cleaned.");

    }

    /**
     * Method for returning an instance to the pool after usage.
     * @param instance idle instance for recycling
     */
    public void returnInstance(ExampleReusableObject instance) {
        LOGGER.fine(this.getClass().getSimpleName() + "(" + Thread.currentThread().getName()
                + "): returnInstance called ...");
        cleanReturnedInstance(instance);
        poolLock.lock();
        try {
            idleInstances.add(instance);
            LOGGER.fine(this.getClass().getSimpleName() + "(" + Thread.currentThread().getName()
                    + "): returned instance back in idle pool.");
            poolUsageSemaphore.release();
        }
        finally {
            poolLock.unlock();
        }
    }

}
