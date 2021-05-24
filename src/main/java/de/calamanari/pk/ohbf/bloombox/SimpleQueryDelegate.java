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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A {@link SimpleQueryDelegate} decouples both the {@link BloomBoxQueryRunner} and the {@link BloomBoxDataStore} from the details of the query execution and
 * the state.
 * <p>
 * <b>Note:</b> On the same delegate calls to {@link #execute(long[], int)} and {@link #execute(long[], int, float[])} must not be mixed, because this leads to
 * erratic result counts.
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
     * temporary probability results while processing several expressions on a single record, the key is the expression-id, the value is the probability
     */
    private final Map<Long, Double> probabilityResultCache = new HashMap<>();

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
     * Support for probability result summing
     */
    private final List<PbBloomBoxQueryResult> pbResults = new ArrayList<>();

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

    @Override
    public void execute(long[] vector, int startPos, DppFetcher probabilities) {
        resultCache.clear();
        probabilityResultCache.clear();
        ensurePbResultsInitialized();
        for (int i = 0; i < queries.length; i++) {
            try {
                if (!queryInErrorFlags[i]) {
                    queries[i].execute(vector, startPos, probabilities, resultCache, probabilityResultCache, pbResults.get(i));
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
     * Creates the result wrappers for working with probabilities
     */
    private void ensurePbResultsInitialized() {
        if (pbResults.isEmpty() && !results.isEmpty()) {
            results.stream().map(PbBloomBoxQueryResult::new).forEach(pbResults::add);
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
    public void finish() {
        if (!pbResults.isEmpty()) {
            pbResults.forEach(PbBloomBoxQueryResult::transferCounts);
        }
    }

    @Override
    public List<BloomBoxQueryResult> getResults() {
        return results;
    }

    @Override
    public void prepareDataPointIds(DataPointDictionary dictionary) {
        Arrays.stream(queries).forEach(q -> q.prepareDataPointIds(dictionary));
    }

    @Override
    public String toString() {
        if (pbResults.isEmpty()) {
            return this.getClass().getSimpleName() + " [resultCache=" + resultCache + ", queryInErrorFlags=" + Arrays.toString(queryInErrorFlags) + ", queries="
                    + Arrays.toString(queries) + ", results=" + results + "]";
        }
        else {
            return this.getClass().getSimpleName() + " [resultCache=" + resultCache + ", probabilityResultCache=" + probabilityResultCache
                    + ", queryInErrorFlags=" + Arrays.toString(queryInErrorFlags) + ", queries=" + Arrays.toString(queries) + ", pbResults=" + pbResults + "]";

        }
    }

}
