/*
 * Progress Observer
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
package de.calamanari.pk.observer;

import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

/**
 * Progress Observer is a concrete OBSERVER implementation, writing a progress message to log channel.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)web.de">Karl Eilebrecht</a>
 */
public class ProgressObserver implements OutputObserver {

    /**
     * logger
     */
    protected static final Logger LOGGER = Logger.getLogger(ProgressObserver.class.getName());

    /**
     * Constant 100 for progress calculation.
     */
    private static final int PROGRESS_100 = 100;
    
    /**
     * Report progress in {@value}%-steps.
     */
    private static final int PROGRESS_PERCENT_INCREMENT = 5;
    
    /**
     * In this simple example the the progress observer knows the number of bytes expected all in all. Think of this as
     * some intrinsic state of the OBSERVER.
     */
    private final long expectedSize;

    /**
     * Here we store the intermediate information to calculate the progress.
     */
    private final AtomicLong counter = new AtomicLong(0);

    /**
     * the last reported progress info
     */
    private volatile int lastProgressWritten = 0;

    /**
     * Creates new Progress observer with the expected size
     * @param expectedSize number of bytes expected to be written
     */
    public ProgressObserver(long expectedSize) {
        this.expectedSize = expectedSize;
    }

    @Override
    public void handleBytesWritten(int id, long numberOfBytes) {

        LOGGER.fine(this.getClass().getSimpleName() + ".handleBytesWritten() called from worker " + id);
        long newVal = counter.addAndGet(numberOfBytes);
        if (newVal >= expectedSize) {
            LOGGER.info("100% (DONE)");
        }
        else {
            int progressNew = (int) ((((double) newVal) / expectedSize) * PROGRESS_100);

            // Shouldn't the code section below be synchronized?
            // It depends ...
            // This implementation may (worst case) show a smaller
            // progress value AFTER a bigger one, because checking condition and
            // and setting lastProgressWritten is not synchronized.
            // Let T1 and T2 be two threads.
            // T1 may check the condition (true), and before updating
            // lastProgressWritten, T2 checks the condition (true).
            // Then for some arbitrary reason T1 gets paused by the VM, while
            // T2 keeps running. This would have two consequences:
            // (1) Because T2 "lapped" T1, the bigger progress value of T2
            // will show before the smaller one of T1.
            // (2) Because T1 updates lastProgressValue with a SMALLER value
            // the next call to handleBytesWritten will produce
            // an additional progress message.
            //
            // However in this example such behavior can be tolerated,
            // in other situations this might be critical. Thus be careful
            // when creating Observers to be used concurrently.

            // we want to report progress in defined steps
            if (progressNew >= (lastProgressWritten + PROGRESS_PERCENT_INCREMENT)) {
                lastProgressWritten = progressNew;
                LOGGER.info("" + progressNew + "% ...");
            }
        }
    }

    /**
     * Returns the current counter value
     * @return bytes written all in all
     */
    public long getCounterValue() {
        return this.counter.get();
    }
}
