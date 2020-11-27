//@formatter:off
/*
 * Query Request - demonstrates ACTIVE OBJECT
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

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Query Request - objectified request
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class QueryRequest extends FutureTask<List<String[]>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(QueryRequest.class);

    /**
     * first name filter, parameter of the initial call
     */
    private final String paramFirstName;

    /**
     * last name filter, parameter of the initial call
     */
    private final String paramLastName;

    /**
     * birthday filter, parameter of the initial call
     */
    private final String paramBirthday;

    /**
     * Creates an objectified request
     * 
     * @param engine query engine to be used
     * @param paramFirstName person's first name to query for
     * @param paramLastName person's last name to query for
     * @param paramBirthday person's birthday to query for
     */
    public QueryRequest(final AbstractHistoryQueryEngine engine, final String paramFirstName, final String paramLastName, final String paramBirthday) {
        super(new Callable<List<String[]>>() {

            @Override
            public List<String[]> call() throws Exception {
                return engine.queryHistoryData(paramFirstName, paramLastName, paramBirthday);
            }

        });
        this.paramFirstName = paramFirstName;
        this.paramLastName = paramLastName;
        this.paramBirthday = paramBirthday;
        LOGGER.debug("{} created", this);
    }

    /**
     * Returns the first name filter
     * 
     * @return first name to query for
     */
    public String getParamFirstName() {
        return paramFirstName;
    }

    /**
     * Returns the last name filter
     * 
     * @return last name to query for
     */
    public String getParamLastName() {
        return paramLastName;
    }

    /**
     * Returns the birthday filter
     * 
     * @return birthday to query for
     */
    public String getParamBirthday() {
        return paramBirthday;
    }

    @Override
    public void run() {
        LOGGER.debug("{}.run() called for {}", this.getClass().getSimpleName(), this);
        super.run();
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "({paramFirstName=" + paramFirstName + ", paramLastName=" + paramLastName + ", paramBirthday=" + paramBirthday
                + "})";
    }

}
