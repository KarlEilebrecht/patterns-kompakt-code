//@formatter:off
/*
 * BloomFilterQuery
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

package de.calamanari.pk.ohbf.bloombox.bbq;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Map;

import de.calamanari.pk.ohbf.bloombox.PbDataPointDictionary;
import de.calamanari.pk.ohbf.bloombox.PbDataPointDictionaryAware;
import de.calamanari.pk.ohbf.bloombox.PbDataPointOccurrenceAware;
import de.calamanari.pk.ohbf.bloombox.PbDataPointOccurrenceCollector;
import de.calamanari.pk.ohbf.bloombox.DppFetcher;
import de.calamanari.pk.ohbf.bloombox.QueryPreparationException;

/**
 * Container that keeps the orinal query string together with the technical root expression
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class BloomFilterQuery implements PbDataPointOccurrenceAware, Serializable {

    private static final long serialVersionUID = -2442237403084251215L;

    /**
     * root expression of a query
     */
    private final BbqExpression expression;

    /**
     * query string before parsing
     */
    private final String sourceQuery;

    /**
     * @param sourceQuery query string before parsing NOT NULL
     * @param expression root expression of a query NOT NULL
     */
    public BloomFilterQuery(String sourceQuery, BbqExpression expression) {
        if (sourceQuery == null || expression == null) {
            throw new QueryPreparationException(
                    String.format("Both constructor arguments are mandatory, given: sourceQuery=%s, expression=%s", sourceQuery, expression));
        }
        this.sourceQuery = sourceQuery;
        this.expression = expression;
    }

    /**
     * Executes the query
     * 
     * @param source bloom filter vector
     * @param startPos start position of the vector
     * @param resultCache binary result cache
     * @return true if the item matches, otherwise false
     */
    @SuppressWarnings({ "java:S3824" })
    public boolean execute(long[] source, int startPos, Map<Long, Boolean> resultCache) {
        Long key = expression.getExpressionId();
        Boolean res = resultCache.get(key);
        if (res == null) {
            res = expression.match(source, startPos, resultCache);
            resultCache.put(key, res);
        }
        return res;
    }

    /**
     * Executes the query and takes into account the contained probability
     * 
     * @param source bloom filter vector
     * @param startPos start position of the vector
     * @param probabilities fetcher with the data point probabilities
     * @param resultCache binary result cache
     * @return probability of the match
     */
    public double execute(long[] source, int startPos, DppFetcher probabilities, Map<Long, Boolean> resultCache) {
        double res = 0.0;
        if (this.execute(source, startPos, resultCache)) {
            res = expression.computeMatchProbability(expression.getExpressionId(), probabilities);
        }
        return res;
    }

    @Override
    public void prepareLpDataPointIds(PbDataPointDictionary dictionary) {
        expression.collectUniqueDepthFirst().stream().filter(PbDataPointDictionaryAware.class::isInstance).map(PbDataPointDictionaryAware.class::cast)
                .forEach(dpa -> dpa.prepareLpDataPointIds(dictionary));
    }

    @Override
    public void registerDataPointOccurrences(PbDataPointOccurrenceCollector collector) {
        expression.collectLiterals().stream().filter(BinaryMatchExpression.class::isInstance).map(BinaryMatchExpression.class::cast)
                .forEach(e -> collector.addDataPointOccurrence(expression.getExpressionId(), e.getLpDataPointId()));
    }

    /**
     * Creates a logical AND, does not change this query object
     * 
     * @param subQuery query to be joined
     * @return and-query of this parent query and the given child
     */
    public BloomFilterQuery combinedWith(BloomFilterQuery subQuery) {
        return new BloomFilterQuery("(" + sourceQuery + ") AND (" + subQuery.sourceQuery + ")",
                new AndExpression(Arrays.asList(this.expression, subQuery.expression)));
    }

    /**
     * @return technical root expression of the query
     */
    public BbqExpression getExpression() {
        return expression;
    }

    /**
     * @return root expression of a query
     */
    public String getSourceQuery() {
        return sourceQuery;
    }

    /**
     * unique id of the {@link #getExpression()}
     * 
     * @return expression id
     */
    public long getId() {
        return this.expression.getExpressionId();
    }

    /**
     * Appends the indentation
     * 
     * @param level number of times indent 4 spaces
     * @param sb string builder
     */
    private void appendIndent(int level, StringBuilder sb) {
        for (int i = 0; i < level; i++) {
            sb.append("    ");
        }
    }

    /**
     * Appends a better readable debug string to the given builder (multi-line)
     * 
     * @param sb string builder
     */
    public void appendAsTree(int level, StringBuilder sb) {
        sb.append("expressionId=").append(expression.getExpressionId());
        sb.append("\n");
        appendIndent(level, sb);
        sb.append("sourceQuery: ");
        sb.append(sourceQuery);
        sb.append("\n");
        appendIndent(level, sb);
        sb.append("expression: ");
        sb.append("\n");
        expression.appendAsTree(sb, level + 1, "");
        sb.append("\n");
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(BloomFilterQuery.class.getSimpleName());
        sb.append("[ID=").append(expression.getExpressionId()).append(", sourceQuery: ");
        sb.append(sourceQuery);
        sb.append(", ");
        sb.append("expression: ");
        sb.append(expression.toString());
        sb.append("]");
        return sb.toString();
    }

}
