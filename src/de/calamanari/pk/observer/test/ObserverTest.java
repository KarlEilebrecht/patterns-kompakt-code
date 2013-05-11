/*
 * Observer Test - demonstrates OBSERVER pattern.
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
package de.calamanari.pk.observer.test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.BeforeClass;
import org.junit.Test;

import de.calamanari.pk.observer.OutputObservable;
import de.calamanari.pk.observer.OutputWorker;
import de.calamanari.pk.observer.ProgressObserver;
import de.calamanari.pk.util.LogUtils;
import de.calamanari.pk.util.MiscUtils;

/**
 * Observer Test - demonstrates OBSERVER pattern.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)web.de">Karl Eilebrecht</a>
 */
public class ObserverTest {

    /**
     * logger
     */
    protected static final Logger LOGGER = Logger.getLogger(ObserverTest.class.getName());

    /**
     * Log-level for this test
     */
    private static final Level LOG_LEVEL = Level.INFO;

    /**
     * number of bytes each worker will "write"
     */
    private static final long NUMBER_OF_BYTES = 100;

    /**
     * number of workers (observables)
     */
    private static final int NUMBER_OF_WORKERS = 100;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        LogUtils.setConsoleHandlerLogLevel(LOG_LEVEL);
        LogUtils.setLogLevel(LOG_LEVEL, ObserverTest.class, OutputWorker.class, ProgressObserver.class);
    }

    @Test
    public void testObserver() throws Exception {

        // Hint: set the log-level above to FINE to watch OBSERVER working.

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
            Thread.sleep(1000);
        }

        for (OutputObservable observable : observableList) {
            observable.removeOutputObserver(progressObserver);
        }

        assertEquals(bytesAllInAll, progressObserver.getCounterValue());

        LOGGER.info("Test Observer successful! Elapsed time: "
                + MiscUtils.formatNanosAsSeconds(System.nanoTime() - startTimeNanos) + " s");

    }

}
