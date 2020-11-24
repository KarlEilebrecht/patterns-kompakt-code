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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.calamanari.pk.util.TimeUtils;

/**
 * Output Worker is a concrete observable in this OBSERVER example.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class OutputWorker extends Thread implements OutputObservable {

    private static final Logger LOGGER = LoggerFactory.getLogger(OutputWorker.class);

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
    @SuppressWarnings("java:S3077")
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
            TimeUtils.sleepThrowRuntimeException(WAIT_DELAY_MILLIS);
            if (observer != null) {
                LOGGER.debug("{} no. {} notifies observer!", this.getClass().getSimpleName(), this.workerNumber);
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
        LOGGER.debug("{}.addOutputObserver called.", this.getClass().getSimpleName());
        this.observer = outputObserver;
    }

    @Override
    public void removeOutputObserver(OutputObserver outputObserver) {
        LOGGER.debug("{}.removeOutputObserver called.", this.getClass().getSimpleName());
        this.observer = null;
    }

}
