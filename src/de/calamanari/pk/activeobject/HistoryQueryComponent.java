/*
 * History Query Component - demonstrates ACTIVE OBJECT
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
package de.calamanari.pk.activeobject;

import java.util.logging.Logger;

/**
 * History Query Component - the api from the client's point of view, it provides asynchronous access to the underlying
 * engine to perform queries possibly taking some time.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)web.de">Karl Eilebrecht</a>
 */
public class HistoryQueryComponent {

    /**
     * logger
     */
    public static final Logger LOGGER = Logger.getLogger(HistoryQueryComponent.class.getName());

    /**
     * underlying engine (injected by constructor)
     */
    private final AbstractHistoryQueryEngine engine;

    /**
     * Scheduler used to schedule queries to be executed
     */
    private final HistoryQueryScheduler scheduler;

    /**
     * Creates a new query component leveraging the given engine
     * @param engine engine to be used for querying
     * @param scheduler used to schedule queries to be executed
     */
    public HistoryQueryComponent(AbstractHistoryQueryEngine engine, HistoryQueryScheduler scheduler) {
        this.engine = engine;
        this.scheduler = scheduler;
    }

    /**
     * Triggers a query according to the given parameters and returns immediately. The returned Future allows the client
     * to poll for the result from time to time.
     * @param firstName person's first name to query for
     * @param lastName person's last name to query for
     * @param birthday person's birthday to query for
     * @return Future for communication and result retrieval
     */
    public QueryRequestFuture queryHistoryData(String firstName, String lastName, String birthday) {
        LOGGER.fine(this.getClass().getSimpleName() + ".queryHistoryData('" + firstName + "', '" + lastName + "', '"
                + birthday + "') called");
        QueryRequest objectifiedRequest = new QueryRequest(engine, firstName, lastName, birthday);
        QueryRequestFuture future = new QueryRequestFuture(objectifiedRequest);
        scheduler.schedule(objectifiedRequest);
        return future;
    }

}