//@formatter:off
/*
 * SimpleQueryDelegate
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
package de.calamanari.pk.ohbf.bloombox;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A {@link SimpleQueryDelegate} decouples both the {@link BloomBoxQueryRunner} and the {@link BloomBoxDataStore} from the details of the query execution and
 * the state.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class SimpleQueryDelegate implements QueryDelegate {

    private static final long serialVersionUID = -5389698263683134435L;

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleQueryDelegate.class);

    /**
     * temporary results while processing several expressions on a single record, the key is the expression-id, the value is the boolean match result
     */
    private final Map<Long, Boolean> resultCache = new HashMap<>();

    /**
     * a flag per query that tells whether it is broken, this way a we can avoid executing an erratic query multiple times
     */
    private final boolean[] queryInErrorFlags;

    /**
     * list of internal queries to be executed per record vector
     */
    private final InternalQuery[] queries;

    /**
     * list of results (same orders as queries), results (counts) will be updated during execution
     */
    private final List<BloomBoxQueryResult> results;

    /**
     * @param queries list of internal queries to be executed per record vector
     * @param results list of results (same orders as queries), results (counts) will be updated during execution
     */
    public SimpleQueryDelegate(InternalQuery[] queries, List<BloomBoxQueryResult> results) {
        this.queries = queries;
        this.queryInErrorFlags = new boolean[queries.length];
        this.results = results;
    }

    @Override
    public void execute(long[] vector, int startPos) {
        resultCache.clear();
        for (int i = 0; i < queries.length; i++) {
            try {
                if (!queryInErrorFlags[i]) {
                    queries[i].execute(vector, startPos, resultCache, results.get(i));
                }
            }
            catch (RuntimeException ex) {
                queryInErrorFlags[i] = true;
                String msg = BbxMessage.ERR_COMMON.format(ex);
                LOGGER.error("Unable to execute query {}: {}", queries[i], msg, ex);
                results.get(i).setErrorMessage(BbxMessage.ERR_QUERY_EXECUTION.format(
                        String.format("Unable to execute query '%s' (%s)%ncause: %s", queries[i].getName(), queries[i].getBaseQuery().getSourceQuery(), msg)));
            }
        }
    }

    /**
     * @return temporary results while processing several expressions on a single record, the key is the expression-id, the value is the boolean match result
     */
    public Map<Long, Boolean> getResultCache() {
        return resultCache;
    }

    @Override
    public boolean[] getQueryInErrorFlags() {
        return queryInErrorFlags;
    }

    @Override
    public InternalQuery[] getQueries() {
        return queries;
    }

    @Override
    public List<BloomBoxQueryResult> getResults() {
        return results;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " [resultCache=" + resultCache + ", queryInErrorFlags=" + Arrays.toString(queryInErrorFlags) + ", queries="
                + Arrays.toString(queries) + ", results=" + results + "]";
    }

}
