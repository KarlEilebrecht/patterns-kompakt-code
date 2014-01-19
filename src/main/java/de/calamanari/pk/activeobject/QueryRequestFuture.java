/*
 * Query Request Future - demonstrates ACTIVE OBJECT
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
package de.calamanari.pk.activeobject;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Query Request Future - allows the client to check the status and finally to retrieve the result of the scheduled
 * operation.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class QueryRequestFuture {

    /**
     * logger
     */
    public static final Logger LOGGER = Logger.getLogger(QueryRequestFuture.class.getName());

    /**
     * The task being observed
     */
    private final QueryRequest queryRequest;

    /**
     * Creates a new Future based on the request data
     * @param queryRequest objectified request which has to be observed
     */
    public QueryRequestFuture(QueryRequest queryRequest) {
        this.queryRequest = queryRequest;
    }

    /**
     * Returns the result if - and only if computation is done, otherwise null (non-blocking)
     * @return result or null (if not completed or cancelled)
     */
    public List<String[]> getResult() {
        LOGGER.fine(this.getClass().getSimpleName() + ".getResult() called");
        try {
            if (queryRequest.isDone() && !queryRequest.isCancelled()) {
                return queryRequest.get();
            }
        }
        catch (Exception ex) {
            LOGGER.log(Level.SEVERE, "Failed to get result!", ex);
        }
        return null;
    }

    /**
     * Cancels the query
     */
    public void cancelQuery() {
        LOGGER.fine(this.getClass().getSimpleName() + ".cancelQuery() called");
        queryRequest.cancel(true);
    }

    /**
     * Determines whether the query is cancelled
     * @return true if this query is cancelled
     */
    public boolean isQueryCancelled() {
        boolean res = queryRequest.isCancelled();
        LOGGER.fine(this.getClass().getSimpleName() + ".isQueryCancelled() called - " + res);
        return res;
    }

    /**
     * Determines whether the query has finished
     * @return true if this query is done
     */
    public boolean isQueryDone() {
        boolean res = queryRequest.isDone();
        LOGGER.fine(this.getClass().getSimpleName() + ".isQueryDone() called - " + res);
        return res;
    }

}
