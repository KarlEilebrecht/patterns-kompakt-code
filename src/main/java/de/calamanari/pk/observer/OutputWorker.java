//@formatter:off
/*
 * Output Worker
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

import java.util.logging.Logger;

import de.calamanari.pk.util.MiscUtils;

/**
 * Output Worker is a concrete observable in this OBSERVER example.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class OutputWorker extends Thread implements OutputObservable {

    /**
     * logger
     */
    protected static final Logger LOGGER = Logger.getLogger(OutputWorker.class.getName());

    /**
     * load simulation delay
     */
    private static final long WAIT_DELAY_MILLIS = 100;

    /**
     * The "work" to do for this worker
     */
    private final long bytesToWrite;

    /**
     * identifies the worker
     */
    private final int workerNumber;

    /**
     * registered output observer, if any
     */
    private volatile OutputObserver observer = null;

    /**
     * Creates new worker with the given "work" to do.
     * 
     * @param workerNumber identifier of the worker
     * @param bytesToWrite number of bytes to be written by the worker
     */
    public OutputWorker(int workerNumber, long bytesToWrite) {
        this.workerNumber = workerNumber;
        this.bytesToWrite = bytesToWrite;
    }

    @Override
    public void run() {
        long bytesWritten = 0;
        while (bytesWritten < bytesToWrite) {
            MiscUtils.sleepThrowRuntimeException(WAIT_DELAY_MILLIS);
            if (observer != null) {
                LOGGER.fine(this.getClass().getSimpleName() + " no. " + this.workerNumber + " notifies observer!");
                observer.handleBytesWritten(this.workerNumber, 1L);
            }
            bytesWritten++;
        }
    }

    /**
     * Returns the number (id) of this worker
     * 
     * @return workerNumber
     */
    public int getWorkerNumber() {
        return this.workerNumber;
    }

    /**
     * This implementation supports exactly one observer, calling this method twice replaces an oberserver registered before.
     * 
     * @param outputObserver the instance that wants to listen
     */
    @Override
    public void addOutputObserver(OutputObserver outputObserver) {
        LOGGER.fine(this.getClass().getSimpleName() + ".addOutputObserver called.");
        this.observer = outputObserver;
    }

    @Override
    public void removeOutputObserver(OutputObserver outputObserver) {
        LOGGER.fine(this.getClass().getSimpleName() + ".removeOutputObserver called.");
        this.observer = null;
    }

}
