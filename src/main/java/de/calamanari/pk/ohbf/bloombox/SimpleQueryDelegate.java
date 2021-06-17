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
import java.util.stream.Collectors;

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
public class SimpleQueryDelegate implements QueryDelegate<SimpleQueryDelegate> {

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

    @Override
    public SimpleQueryDelegate createSpawn() {
        return new SimpleQueryDelegate(queries, this.results.stream().map(BloomBoxQueryResult::createSpawn).collect(Collectors.toList()), true);
    }

    @Override
    public void addSpawnResults(SimpleQueryDelegate spawn) {
        List<BloomBoxQueryResult> spawnResults = spawn.getResults();
        for (int i = 0; i < results.size(); i++) {
            BloomBoxQueryResult result = results.get(i);
            result.addResultData(spawnResults.get(i));
        }
    }

    /**
     * @param queries list of internal queries to be executed per record vector
     * @param results list of results (same orders as queries), results (counts) will be updated during execution
     * @param quiet if true, no logging (copy constructor
     */
    protected SimpleQueryDelegate(InternalQuery[] queries, List<BloomBoxQueryResult> results, boolean quiet) {
        this.queries = queries;
        this.queryInErrorFlags = new boolean[queries.length];
        this.results = results;
        if (!quiet) {
            logQueriesToProtocolIfRequired();
        }
    }

    /**
     * @param queries list of internal queries to be executed per record vector
     * @param results list of results (same orders as queries), results (counts) will be updated during execution
     */
    public SimpleQueryDelegate(InternalQuery[] queries, List<BloomBoxQueryResult> results) {
        this(queries, results, false);
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
    public void execute(long[] vector, int startPos, DpavProbabilityFetcher probabilities) {
        resultCache.clear();
        ensurePbResultsInitialized();
        for (int i = 0; i < queries.length; i++) {
            try {
                if (!queryInErrorFlags[i]) {
                    queries[i].execute(vector, startPos, probabilities, resultCache, results.get(i));
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
     * Ensures the optional probability results are initialized correctly
     */
    private void ensurePbResultsInitialized() {
        results.stream().forEach(BloomBoxQueryResult::ensurePbResultsInitialized);
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
        results.forEach(r -> {
            if (r.getProbabilityResult() != null) {
                r.getProbabilityResult().transferCounts(r);
            }
        });
        for (int i = 0; i < queries.length; i++) {
            logQueryResultToProtocolIfRequired(i);
        }
    }

    /**
     * Logs the query result for the given query index to the protocol if it is configured
     * 
     * @param i index of the query
     */
    private void logQueryResultToProtocolIfRequired(int i) {
        InternalQuery query = queries[i];
        if (query.isProtocolEnabled()) {
            StringBuilder sb = new StringBuilder();
            BloomBoxQueryResult result = results.get(i);
            sb.setLength(0);
            sb.append("RESULT: base query '" + query.getName() + "': " + result.getBaseQueryCount());
            if (result.getProbabilityResult() != null) {
                sb.append(" (" + result.getProbabilityResult().getBaseQuerySum() + ")");
            }
            result.logProtocolMessage(sb.toString());
            long[] subQueryCounts = result.getSubQueryCounts();
            if (subQueryCounts != null) {
                for (int k = 0; k < subQueryCounts.length; k++) {
                    sb.setLength(0);
                    sb.append("RESULT: sub query '" + result.getSubQueryLabels()[k] + "': " + subQueryCounts[k]);
                    if (result.getProbabilityResult() != null) {
                        sb.append(" (" + result.getProbabilityResult().getSubQuerySums()[k] + ")");
                    }
                    result.logProtocolMessage(sb.toString());
                }
            }
        }
    }

    @Override
    public List<BloomBoxQueryResult> getResults() {
        return results;
    }

    @Override
    public void prepareLpDpavs(PbDpavDictionary dictionary) {
        Arrays.stream(queries).forEach(q -> q.prepareLpDpavs(dictionary));
    }

    @Override
    public void registerDpavOccurrences(PbDpavOccurrenceCollector collector) {
        Arrays.stream(queries).forEach(q -> q.registerDpavOccurrences(collector));
        for (int i = 0; i < queries.length; i++) {
            InternalQuery query = queries[i];
            BloomBoxQueryResult result = results.get(i);
            logQueryToProtocol(query, result);
            processMultiDpReferenceWarnings(query, collector, result);
        }
    }

    /**
     * Adds warnings to the corresponding query result if the collector shows multi DPAV-references
     * 
     * @param query current query
     * @param collector source of warnings based on DPAV-usage
     * @param result to be updated
     */
    protected void processMultiDpReferenceWarnings(InternalQuery query, PbDpavOccurrenceCollector collector, BloomBoxQueryResult result) {
        Map<String, Integer> maxOccurrenceMap = collector.getMaxOccurrenceMap();
        Integer max = maxOccurrenceMap.get(query.getName());
        if (max != null && max > 1) {
            result.setWarningMessage(createMultiReferenceWarning(result.getWarningMessage(), query.getName(), max));
            if (query.isProtocolEnabled()) {
                result.logProtocolMessage(
                        String.format("Warning: data point attribute value multi-references (%d) detected in base query '%s'.", max, query.getName()));
            }
        }
        for (int i = 0; i < query.getNumberOfSubQueries(); i++) {
            max = maxOccurrenceMap.get(query.getSubQueryName(i));
            if (max != null && max > 1) {
                result.setWarningMessage(createMultiReferenceWarning(result.getWarningMessage(), query.getSubQueryLabel(i), max));
                if (query.isProtocolEnabled()) {
                    result.logProtocolMessage(
                            String.format("Warning: data point attribute multi-references (%d) detected in sub query '%s'.", max, query.getSubQueryLabel(i)));
                }

            }
        }

    }

    /**
     * Writes all queries to the corresponding result protocols
     * 
     */
    private void logQueriesToProtocolIfRequired() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < queries.length; i++) {
            InternalQuery query = queries[i];
            // reduce verbosity if there was no sub query to optimize anyway
            if (query.isProtocolEnabled()) {
                sb.setLength(0);
                query.appendAsTree(sb);
                results.get(i).logProtocolMessage(sb.toString());
            }
        }
    }

    /**
     * Writes the given query to the corresponding result protocol
     * 
     * @param query to be logged
     * @param result to add message
     */
    private void logQueryToProtocol(InternalQuery query, BloomBoxQueryResult result) {
        if (query.isProtocolEnabled()) {
            StringBuilder sb = new StringBuilder();
            sb.append("Optimized query:\n");
            query.appendAsTree(sb);
            result.logProtocolMessage(sb.toString());
        }
    }

    /**
     * Creates a single updated warning message
     * 
     * @param existingWarnings string from result, may be null
     * @param queryName label of the query
     * @param maxOccurrence value to decide if high risk or medium
     * @return warning text
     */
    private String createMultiReferenceWarning(String existingWarnings, String queryName, int maxOccurrence) {
        String warnings = existingWarnings == null ? "" : "\n";
        warnings = warnings + BbxMessage.WARN_MULTI_REFERENCE.format(String.format(
                "For query '%s', the optimizer was unable to avoid data point attribute value multi-references. Results may be incorrect (risk level %d).",
                queryName, maxOccurrence));
        return warnings;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " [resultCache=" + resultCache + ", queryInErrorFlags=" + Arrays.toString(queryInErrorFlags) + ", queries="
                + Arrays.toString(queries) + ", results=" + results + "]";
    }

}
