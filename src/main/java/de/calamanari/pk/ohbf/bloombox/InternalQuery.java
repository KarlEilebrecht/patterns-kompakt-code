//@formatter:off
/*
 * InternalQuery
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

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import de.calamanari.pk.ohbf.bloombox.bbq.BloomFilterQuery;

/**
 * An {@link InternalQuery} represents a parsed and optimized query with sub queries ready to be executed on the store.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class InternalQuery implements PbDpavOccurrenceAware, Serializable {

    private static final long serialVersionUID = -7248298268398187149L;

    /**
     * Name of the query
     */
    private final String name;

    /**
     * main filter query, the sub-queries are based on
     */
    private final BloomFilterQuery baseQuery;

    /**
     * optional sub-queries based on the main query
     */
    protected final BloomFilterQuery[] subQueries;

    /**
     * labels of the sub-queries
     */
    protected final String[] subQueryLabels;

    /**
     * query options
     */
    private final Map<String, String> queryOptions;

    /**
     * tells whether a protocol should be written
     */
    private final boolean protocolEnabled;

    /**
     * @param name the query's name
     * @param baseQuery main query
     * @param subQueries optional sub-queries (label to query map), null will be gracefully handles like an empty map
     * @param queryOptions optional map with options for this query, may be null
     */
    public InternalQuery(String name, BloomFilterQuery baseQuery, Map<String, BloomFilterQuery> subQueries, Map<String, String> queryOptions) {
        this.name = name;
        this.baseQuery = baseQuery;
        if (subQueries != null) {
            this.subQueries = new BloomFilterQuery[subQueries.size()];
            this.subQueryLabels = new String[subQueries.size()];
            int idx = 0;
            for (Map.Entry<String, BloomFilterQuery> entry : subQueries.entrySet()) {
                this.subQueryLabels[idx] = entry.getKey();
                this.subQueries[idx] = entry.getValue();
                idx++;
            }
        }
        else {
            this.subQueries = new BloomFilterQuery[0];
            this.subQueryLabels = new String[0];
        }
        this.queryOptions = queryOptions == null ? Collections.emptyMap() : Collections.unmodifiableMap(new HashMap<>(queryOptions));
        this.protocolEnabled = BloomBoxOption.PROTOCOL.isEnabled(queryOptions);
    }

    /**
     * @return the query's name
     */
    public String getName() {
        return name;
    }

    /**
     * @param idx index of the sub 0 .. {@link #getNumberOfSubQueries()} -1
     * @return fully qualified name of the sub query, which is name + dot + label
     */
    public String getSubQueryName(int idx) {
        return this.name + "." + subQueryLabels[idx];
    }

    /**
     * @param idx index of the sub 0 .. {@link #getNumberOfSubQueries()} -1
     * @return just the label of the query
     */
    public String getSubQueryLabel(int idx) {
        return subQueryLabels[idx];
    }

    /**
     * @return number of sub queries
     */
    public int getNumberOfSubQueries() {
        return this.subQueries.length;
    }

    /**
     * @return main query
     */
    public BloomFilterQuery getBaseQuery() {
        return baseQuery;
    }

    /**
     * @return true if a protocol should be written
     */
    public boolean isProtocolEnabled() {
        return protocolEnabled;
    }

    /**
     * @return query options, if any, NOT NULL
     */
    public Map<String, String> getQueryOptions() {
        return queryOptions;
    }

    /**
     * Applies this query to the given long array (a single record's vector from the store)
     * 
     * @param source long array
     * @param startPos start position of the vector in the source array
     * @param resultCache we avoid duplicate work by keeping results for already executed expressions
     * @param result to be updated
     */
    public void execute(long[] source, int startPos, Map<Long, Boolean> resultCache, BloomBoxQueryResult result) {

        int numberOfSubQueries = subQueries.length;

        boolean baseResult = baseQuery.execute(source, startPos, resultCache);

        if (baseResult) {
            result.incrementBaseQueryCount();
            for (int i = 0; i < numberOfSubQueries; i++) {
                if (subQueries[i].execute(source, startPos, resultCache)) {
                    result.incrementSubQueryCount(i);
                }
            }
        }

    }

    /**
     * Applies this query to the given long array (a single record's vector from the store) with probabilities
     * 
     * @param source long array
     * @param startPos start position of the vector in the source array
     * @param probabilities fetcher with probabilities for computing match probability
     * @param resultCache we avoid duplicate work by keeping results for already executed expressions
     * @param result to be updated
     */
    public void execute(long[] source, int startPos, DpavProbabilityFetcher probabilities, Map<Long, Boolean> resultCache, BloomBoxQueryResult result) {

        int numberOfSubQueries = subQueries.length;

        double baseMatchProbability = baseQuery.execute(source, startPos, probabilities, resultCache);

        if (baseMatchProbability > 0.0) {
            result.getProbabilityResult().incrementBaseQuerySum(baseMatchProbability);

            for (int i = 0; i < numberOfSubQueries; i++) {

                double subMatchProbability = subQueries[i].execute(source, startPos, probabilities, resultCache);
                if (subMatchProbability > 0.0) {
                    result.getProbabilityResult().incrementSubQuerySum(i, subMatchProbability);
                }
            }
        }

    }

    @Override
    public void prepareLpDpavs(PbDpavDictionary dictionary) {
        baseQuery.prepareLpDpavs(dictionary);
        Arrays.stream(subQueries).forEach(q -> q.prepareLpDpavs(dictionary));
    }

    @Override
    public void registerDpavOccurrences(PbDpavOccurrenceCollector collector) {
        registerDpavOccurrencesIfRequired(this.name, baseQuery, collector);
        for (int i = 0; i < subQueries.length; i++) {
            this.registerDpavOccurrencesIfRequired(this.getSubQueryName(i), subQueries[i], collector);
        }
    }

    /**
     * Registers the DPAVs for the given query
     * 
     * @param queryName textual identifier
     * @param query the currently processed bloom filter query
     * @param collector DPAV occurrence collector
     */
    private void registerDpavOccurrencesIfRequired(String queryName, BloomFilterQuery query, PbDpavOccurrenceCollector collector) {
        if (collector.startCollection(query, queryName)) {
            query.registerDpavOccurrences(collector);
        }
    }

    /**
     * Appends a better readable debug string to the given builder (multi-line)
     * 
     * @param sb string builder
     */
    public void appendAsTree(StringBuilder sb) {
        sb.append("\nquery name: '");
        sb.append(name);
        sb.append("'\n    base query");
        if (baseQuery == null) {
            sb.append(": null");
        }
        else {
            sb.append(" (complexity=");
            sb.append(baseQuery.getExpression().computeComplexity());
            sb.append("): ");
            baseQuery.appendAsTree(2, sb);
        }
        if (subQueries != null && subQueries.length > 0) {
            for (int i = 0; i < subQueries.length; i++) {
                sb.append("\n        sub query '");
                sb.append(String.valueOf(subQueryLabels[i]));
                sb.append("'");
                if (subQueries[i] == null) {
                    sb.append(": null\n");
                }
                else {
                    sb.append(" (complexity=");
                    sb.append(subQueries[i].getExpression().computeComplexity());
                    sb.append("): ");
                    subQueries[i].appendAsTree(3, sb);
                }
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getClass().getSimpleName());
        sb.append(" [name=");
        sb.append(name);
        sb.append(", ");
        sb.append("baseQuery=");
        sb.append(String.valueOf(baseQuery));
        sb.append(", protocolEnabled=");
        sb.append(this.protocolEnabled);
        sb.append(", ");
        if (subQueries == null) {
            sb.append("subQueries={}");
        }
        else {
            sb.append("subQueries={");
            for (int i = 0; i < subQueries.length; i++) {
                if (i > 0) {
                    sb.append(", ");
                }
                sb.append(String.valueOf(subQueryLabels[i]));
                sb.append("=");
                sb.append(String.valueOf(subQueries[i]));
            }
            sb.append("}");
        }
        sb.append("]");
        return sb.toString();
    }
}
