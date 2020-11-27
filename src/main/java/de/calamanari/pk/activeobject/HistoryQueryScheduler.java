//@formatter:off
/*
 * History Query Scheduler - demonstrates ACTIVE OBJECT
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
package de.calamanari.pk.activeobject;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * History Query Scheduler - isolates the history query component from thread management and control of maximum number of parallel calls.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class HistoryQueryScheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger(HistoryQueryScheduler.class);

    /**
     * Thread and queue management
     */
    private final Executor executor;

    /**
     * Creates new scheduler allowing the specified amount of parallel executions
     * 
     * @param maxParallelWorkers number of calls that shall execute in parallel
     */
    public HistoryQueryScheduler(int maxParallelWorkers) {
        LOGGER.debug("{} created - working on queue with {} worker threads in parallel.", this.getClass().getSimpleName(), maxParallelWorkers);
        // the chosen executor internally manages a queue and ensures that
        // only the given maximum of calls will execute in parallel
        this.executor = Executors.newFixedThreadPool(maxParallelWorkers);
    }

    /**
     * schedules the specified task and returns immediately
     * 
     * @param task some task to be executed in the future
     */
    public void schedule(QueryRequest task) {
        LOGGER.debug("{}.schedule() called.", this.getClass().getSimpleName());
        LOGGER.debug("enqueuing objectified request: {}", task);
        executor.execute(task);
    }

}
