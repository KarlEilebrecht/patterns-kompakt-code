//@formatter:off
/*
 * BloomBoxQueryRunner
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
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;
import java.util.stream.Collectors;

import org.antlr.v4.runtime.CharStream;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.calamanari.pk.ohbf.BloomFilterConfig;
import de.calamanari.pk.ohbf.LwGenericOHBF;
import de.calamanari.pk.ohbf.bloombox.bbq.AndExpression;
import de.calamanari.pk.ohbf.bloombox.bbq.BbqBooleanLiteral;
import de.calamanari.pk.ohbf.bloombox.bbq.BbqExpression;
import de.calamanari.pk.ohbf.bloombox.bbq.BbqLexer;
import de.calamanari.pk.ohbf.bloombox.bbq.BbqParser;
import de.calamanari.pk.ohbf.bloombox.bbq.BbqParser.QueryContext;
import de.calamanari.pk.ohbf.bloombox.bbq.BloomFilterQuery;
import de.calamanari.pk.ohbf.bloombox.bbq.ExpressionIdUtil;
import de.calamanari.pk.ohbf.bloombox.bbq.IntermediateExpression;
import de.calamanari.pk.ohbf.bloombox.bbq.IntermediateExpressionBuilder;
import de.calamanari.pk.ohbf.bloombox.bbq.IntermediateExpressionOptimizer;
import de.calamanari.pk.ohbf.bloombox.bbq.IntermediatePostExpressionBuilder;
import de.calamanari.pk.ohbf.bloombox.bbq.PostBbqLexer;
import de.calamanari.pk.ohbf.bloombox.bbq.PostBbqParser;
import de.calamanari.pk.ohbf.bloombox.bbq.QuantumOptimizer;
import de.calamanari.pk.util.LambdaSupportLoggerProxy;

/**
 * The {@link BloomBoxQueryRunner} prepares and executes queries on a {@link BloomBoxDataStore}.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class BloomBoxQueryRunner {

    private static final Logger LOGGER = LambdaSupportLoggerProxy.wrap(LoggerFactory.getLogger(BloomBoxQueryRunner.class));

    /**
     * Bloom filter configuration
     */
    protected final BloomFilterConfig config;

    /**
     * Data store the runner is operating on
     */
    protected final BloomBoxDataStore dataStore;

    /**
     * Factory for upscaler instances, standard is {@link DefaultUpScaler#FACTORY}
     */
    private UpScalerFactory upScalerFactory = DefaultUpScaler.FACTORY;

    /**
     * Creates a runner on the given box
     * 
     * @param bloomBox source, queries will go to the box's data store
     */
    public BloomBoxQueryRunner(BloomBox bloomBox) {
        this.config = bloomBox.getConfig();
        this.dataStore = bloomBox.getDataStore();
    }

    /**
     * Convenience method to execute a single query
     * 
     * @param queryString BBQ expression
     * @param subQueryStrings sub query BBQ expressions
     * @return query result
     */
    public BloomBoxQueryResult execute(String queryString, String... subQueryStrings) {
        BloomBoxQuery query = BloomBoxQuery.basicQuery("UNNAMED").query(queryString).subQueries(subQueryStrings).build();
        return execute(query).get(0);
    }

    /**
     * @param queries prepared queries of the bundle
     * @param nameReferenceMap for debugging
     * @param warnings for debugging
     * @param message
     */
    private void logPreparedInternalQueries(InternalQuery[] queries, Map<String, Long> nameReferenceMap, List<String> warnings, String message) {
        if (LOGGER.isTraceEnabled()) {
            StringBuilder sb = new StringBuilder();
            sb.append(message);
            sb.append("\n");
            if (queries == null) {
                sb.append("null");
            }
            else {
                for (int i = 0; i < queries.length; i++) {
                    queries[i].appendAsTree(sb);
                    appendWarning(warnings, sb, i);
                }
            }
            appendReferenceMap(nameReferenceMap, sb);
            LOGGER.trace(sb.toString());
        }
        else if (LOGGER.isDebugEnabled()) {
            StringBuilder sb = new StringBuilder();
            sb.append(message);
            sb.append(" ");
            sb.append(Arrays.toString(queries));
            sb.append(", nameReferenceMap=");
            sb.append(nameReferenceMap);
            LOGGER.debug(sb.toString());
        }
    }

    /**
     * Appends the warning part to the debug message string if
     * 
     * @param warnings to be added if not blank
     * @param sb destination
     * @param i index in warnings array
     */
    private void appendWarning(List<String> warnings, StringBuilder sb, int i) {
        String warning = warnings.size() > i ? warnings.get(i) : "";
        if (!warning.isBlank()) {
            sb.append("    warnings: \n");
            sb.append("        ");
            sb.append(warning);
            sb.append("\n");
        }
    }

    /**
     * Appends the reference map to the debug string
     * 
     * @param nameReferenceMap name to reference mapping
     * @param sb destination
     */
    private void appendReferenceMap(Map<String, Long> nameReferenceMap, StringBuilder sb) {
        String lineBreakIndent = "\n    ";
        sb.append("\nnameReferenceMap {");
        if (nameReferenceMap != null && !nameReferenceMap.isEmpty()) {
            if (nameReferenceMap.size() > 1) {
                sb.append(lineBreakIndent);
            }
            sb.append(nameReferenceMap.entrySet().stream().map(e -> "" + e.getValue() + " <- " + e.getKey()).collect(Collectors.joining(lineBreakIndent)));
        }
        sb.append("\n}");
        sb.append("\n");
    }

    /**
     * Parses BBQ expressions from {@link BloomBoxQuery}s and converts them into {@link InternalQuery}s.
     * 
     * @param queries raw queries
     * @param nameReferenceMap maps query and sub query names to the corresponding expression ids
     * @param warnings collection of all warnings picked up during processing, one entry per query, empty string for no warning
     * @return array with the internal queries to be executed
     */
    private InternalQuery[] prepareInternalQueries(List<BloomBoxQuery> queries, Map<String, Long> nameReferenceMap, List<String> warnings) {
        InternalQuery[] res = new InternalQuery[queries.size()];
        IntermediateExpressionBuilder basicExpressionBuilder = new IntermediateExpressionBuilder();
        IntermediatePostExpressionBuilder postExpressionBuilder = new IntermediatePostExpressionBuilder(nameReferenceMap);
        IntermediateExpressionOptimizer optimizer = new IntermediateExpressionOptimizer();
        StringBuilder warningBuilder = new StringBuilder();
        LwGenericOHBF bloomFilter = new LwGenericOHBF(config);

        Map<Long, BbqExpression> expressionCache = new HashMap<>();

        for (int i = 0; i < queries.size(); i++) {
            BloomBoxQuery query = queries.get(i);
            try {
                warningBuilder.setLength(0);
                BloomFilterQuery baseQuery = null;
                if (query.getType() == QueryType.POST_QUERY) {
                    baseQuery = createPostQuery(bloomFilter, query.getName(), query.getQuery(), postExpressionBuilder, optimizer, expressionCache,
                            warningBuilder);
                }
                else {
                    baseQuery = createBasicQuery(bloomFilter, query.getName(), query.getQuery(), basicExpressionBuilder, optimizer, expressionCache,
                            warningBuilder);
                }
                registerQueryByName(query.getName(), baseQuery, nameReferenceMap);

                Map<String, BloomFilterQuery> subQueries = new HashMap<>();
                for (Map.Entry<String, String> entry : query.getSubQueryMap().entrySet()) {
                    String subQueryName = query.getName() + "." + entry.getKey();
                    BloomFilterQuery subQuery = createBasicQuery(bloomFilter, subQueryName, entry.getValue(), basicExpressionBuilder, optimizer,
                            expressionCache, warningBuilder);
                    registerQueryByName(subQueryName, subQuery, nameReferenceMap);
                    subQueries.put(entry.getKey(), subQuery);
                }

                res[i] = createInternalQuery(query.getName(), baseQuery, subQueries, query.getOptions());
                warnings.add(warningBuilder.toString());
            }
            catch (IndexOutOfBoundsException ex) {
                String errorMessage = BbxMessage.ERR_CREATE_QUERY.format(String.format("Failed to create query '%s' from %s%ncause: %s", query.getName(),
                        query.getQuery(), BbxMessage.ERR_QUERY_SYNTAX_QUOTES.format("Unmatching/missing quotes?", ex)));
                LOGGER.error(errorMessage, ex);
                res[i] = new ErrorPlaceholderQuery(query.getName(), errorMessage);
                warnings.add("");
            }
            catch (RuntimeException ex) {
                String errorMessage = BbxMessage.ERR_CREATE_QUERY.format(
                        String.format("Failed to create query '%s' from %s%ncause: %s", query.getName(), query.getQuery(), BbxMessage.ERR_COMMON.format(ex)));
                LOGGER.error(errorMessage, ex);
                res[i] = new ErrorPlaceholderQuery(query.getName(), errorMessage);
                warnings.add("");
            }
        }
        logPreparedInternalQueries(res, nameReferenceMap, warnings, "Prepared internal queries:");
        return res;
    }

    /**
     * Creates an internal query after applying the QuantumOptimizer to reduce complexity.
     * <p>
     * <b>Note:</b> We cannot do this optimization earlier without breaking the reference query concept.
     * 
     * @param name query name
     * @param baseQuery main query
     * @param subQueries sub query
     * @param options query settings
     */
    private InternalQuery createInternalQuery(String name, BloomFilterQuery baseQuery, Map<String, BloomFilterQuery> subQueries, Map<String, String> options) {
        QuantumOptimizer quantumOptimizer = new QuantumOptimizer();
        BloomFilterQuery optimizedBaseQuery = quantumOptimizer.optimize(baseQuery);
        Map<String, BloomFilterQuery> optimizedCombinedSubQueries = new HashMap<>();
        for (Map.Entry<String, BloomFilterQuery> entry : subQueries.entrySet()) {
            BloomFilterQuery combinedFilterQuery = quantumOptimizer.optimize(optimizedBaseQuery.combinedWith(entry.getValue()));
            optimizedCombinedSubQueries.put(entry.getKey(), combinedFilterQuery);
        }
        return new InternalQuery(name, optimizedBaseQuery, optimizedCombinedSubQueries, options);
    }

    /**
     * Puts the query into the reference map after checking for duplicates
     * 
     * @param name query name
     * @param query filter query
     * @param nameReferenceMap reference map
     * @throws QueryPreparationException if there was already another query mapped to the given name
     */
    private void registerQueryByName(String name, BloomFilterQuery query, Map<String, Long> nameReferenceMap) {
        if (nameReferenceMap.get(name) != null) {
            throw new QueryPreparationException(
                    BbxMessage.ERR_DUPLICATE_QUERY.format(String.format("Duplicate query name detected: '%s' (query names in a bundle must be unique)", name)));
        }
        nameReferenceMap.put(name, query.getExpression().getExpressionId());
    }

    /**
     * BBQ {@link QueryType#BASIC_QUERY} have a specific grammar to be used for parsing. This method parses the query and returns the corresponding filter
     * query.
     * 
     * @param bloomFilter filter instance to create query vector
     * @param queryName unique name of the query
     * @param queryString BBQ expression
     * @param builder expression builder instance
     * @param optimizer query optimizer
     * @param expressionCache expression cache (avoids duplication)
     * @param warningBuilder for adding warnings
     * @return new filter query
     */
    private BloomFilterQuery createBasicQuery(LwGenericOHBF bloomFilter, String queryName, String queryString, IntermediateExpressionBuilder builder,
            IntermediateExpressionOptimizer optimizer, Map<Long, BbqExpression> expressionCache, StringBuilder warningBuilder) {
        LOGGER.trace("Parsing basic query '{}' from queryString= {} ...", queryName, queryString);
        parseBbqQuery(queryString, builder);
        IntermediateExpression expression = optimizer.process(builder.getResult());
        BbqExpression bbqe = expression.createBbqEquivalent(bloomFilter, expressionCache);
        validateQuery(queryName, warningBuilder, expression, bbqe);
        return new BloomFilterQuery(queryString, bbqe);
    }

    /**
     * BBQ {@link QueryType#POST_QUERY} have a specific grammar to be used for parsing. This method parses the query and returns the corresponding filter query.
     * 
     * @param bloomFilter filter instance to create query vector
     * @param queryName unique name of the query
     * @param queryString BBQ expression
     * @param builder expression builder instance
     * @param optimizer query optimizer
     * @param expressionCache expression cache (avoids duplication)
     * @param warningBuilder for adding warnings
     * @return new filter query
     */
    private BloomFilterQuery createPostQuery(LwGenericOHBF bloomFilter, String queryName, String queryString, IntermediatePostExpressionBuilder builder,
            IntermediateExpressionOptimizer optimizer, Map<Long, BbqExpression> expressionCache, StringBuilder warningBuilder) {
        LOGGER.trace("Parsing post query '{}' from queryString= {} ...", queryName, queryString);
        CharStream codePointCharStream = CharStreams.fromString(queryString + "\n");
        PostBbqLexer lexer = new PostBbqLexer(codePointCharStream);
        lexer.removeErrorListeners();
        lexer.addErrorListener(builder.getErrorListener());
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        PostBbqParser parser = new PostBbqParser(tokens);
        parser.removeErrorListeners();
        parser.addErrorListener(builder.getErrorListener());
        PostBbqParser.QueryContext queryContext = parser.query();
        ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(builder, queryContext);
        IntermediateExpression expression = optimizer.process(builder.getResult());
        BbqExpression bbqe = expression.createBbqEquivalent(bloomFilter, expressionCache);
        validateQuery(queryName, warningBuilder, expression, bbqe);
        return new BloomFilterQuery(queryString, bbqe);
    }

    /**
     * Collects all referenced base attributes in the given set
     * <p>
     * <b>Note:</b> This method won't fail on invalid queries, it just does not count its required attributes.
     * 
     * @param queryName name of the query
     * @param queryString base or sub query
     * @param result target collection to be updated
     */
    private static void collectRequiredAttributes(String queryName, String queryString, Set<String> result) {
        LOGGER.trace("Collecting referenced attributes in query '{}' from queryString= {} ...", queryName, queryString);
        try {
            IntermediateExpressionBuilder builder = new IntermediateExpressionBuilder();
            parseBbqQuery(queryString, builder);
            builder.getResult().collectRequiredBaseAttributes(result);
        }
        catch (RuntimeException ex) {
            LOGGER.error("Could not collect required attributes of query '{}': queryString={}", queryName, queryString, ex);
        }
    }

    /**
     * Parses the raw query string
     * 
     * @param queryString raw bbq query string (base query or sub query, no post query)
     * @param builder result collector
     */
    private static void parseBbqQuery(String queryString, IntermediateExpressionBuilder builder) {
        CharStream codePointCharStream = CharStreams.fromString(queryString + "\n");
        BbqLexer lexer = new BbqLexer(codePointCharStream);
        lexer.removeErrorListeners();
        lexer.addErrorListener(builder.getErrorListener());
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        BbqParser parser = new BbqParser(tokens);
        parser.removeErrorListeners();
        parser.addErrorListener(builder.getErrorListener());
        QueryContext queryContext = parser.query();
        ParseTreeWalker walker = new ParseTreeWalker();
        walker.walk(builder, queryContext);
    }

    /**
     * Collects all referenced base attributes in the given query bundle
     * <p>
     * <b>Note:</b> This method won't fail on invalid queries, it just does not count its required attributes.
     *
     * @param bundle query bundle
     * @return set with the names of all base attributes referenced in queries of the given bundle, in alphabetical order
     */
    public static Set<String> collectRequiredAttributes(QueryBundle bundle) {
        Set<String> res = new TreeSet<>();
        if (bundle.getBaseQueries() != null) {
            for (BloomBoxQuery baseQuery : bundle.getBaseQueries()) {
                collectRequiredAttributes(baseQuery.getName(), baseQuery.getQuery(), res);
                if (baseQuery.getSubQueryMap() != null) {
                    baseQuery.getSubQueryMap().entrySet().stream().forEach(entry -> collectRequiredAttributes(entry.getKey(), entry.getValue(), res));
                }
            }
        }
        if (bundle.getPostQueries() != null) {
            for (BloomBoxQuery postQuery : bundle.getPostQueries()) {
                if (postQuery.getSubQueryMap() != null) {
                    postQuery.getSubQueryMap().entrySet().stream().forEach(entry -> collectRequiredAttributes(entry.getKey(), entry.getValue(), res));
                }
            }
        }
        return res;
    }

    /**
     * This method adds warnings on certain findings
     * 
     * @param queryName name of the query
     * @param warningBuilder for adding warnings
     * @param expression intermediate expression
     * @param bbqe final expression
     */
    private void validateQuery(String queryName, StringBuilder warningBuilder, IntermediateExpression expression, BbqExpression bbqe) {
        if (bbqe == BbqBooleanLiteral.FALSE) {
            warningBuilder.append(
                    BbxMessage.WARN_ALWAYS_FALSE.format(String.format("Query expression '%s' always yields false, so the result must be empty!%n", queryName)));
        }
        else if (bbqe == BbqBooleanLiteral.TRUE) {
            warningBuilder.append(BbxMessage.WARN_ALWAYS_TRUE
                    .format(String.format("Query expression '%s' always yields true, so the result will contain all rows!%n", queryName)));
        }
        else if (expression.containsAnyAlwaysTrueOrAlwaysFalseExpression()) {
            warningBuilder.append(BbxMessage.WARN_ALWAYS_TRUE_OR_ALWAYS_FALSE
                    .format(String.format("At least one condition in the query expression '%s' yields always false or always true.%n", queryName)));
        }
    }

    /**
     * This method performs the bloom filter queries to prepare scaling. Therefore it determines some additional counts.
     * 
     * @param executionId id of execution
     * @param baseExpressions all expressions to be counted
     * @param stats preparation count statistics
     * @return stats (pass-through)
     */
    private PreparationQueryStats executePreparationQuery(long executionId, List<? extends BbqExpression> baseExpressions, PreparationQueryStats stats) {
        InternalQuery[] countQueries = new InternalQuery[baseExpressions.size()];

        for (int i = 0; i < baseExpressions.size(); i++) {
            BloomFilterQuery query = new BloomFilterQuery("", baseExpressions.get(i));
            countQueries[i] = new InternalQuery(baseExpressions.get(i).toString(), query, null, null);
        }
        List<BloomBoxQueryResult> countQueryResults = execute(executionId, countQueries);

        stats.setNumberOfRows(dataStore.getNumberOfRows());
        for (int i = 0; i < baseExpressions.size(); i++) {
            BloomBoxQueryResult countQueryResult = countQueryResults.get(i);

            stats.getExpressionLevelCounts().put(baseExpressions.get(i).getExpressionId(), countQueryResult.getBaseQueryCount());

            BloomBoxQueryResult queryResult = stats.getMainQueryResultMap().get(baseExpressions.get(i).getExpressionId());
            if (queryResult != null && countQueryResult.getErrorMessage() != null) {
                queryResult.setErrorMessage(queryResult.getErrorMessage() == null ? countQueryResult.getErrorMessage()
                        : queryResult.getErrorMessage() + "\n" + countQueryResult.getErrorMessage());
            }
            if (queryResult != null && countQueryResult.getWarningMessage() != null) {
                queryResult.setWarningMessage(queryResult.getWarningMessage() == null ? countQueryResult.getWarningMessage()
                        : queryResult.getWarningMessage() + "\n" + countQueryResult.getWarningMessage());
            }

        }
        return stats;
    }

    /**
     * Collects the required statistic counts to prepare upscaling
     * 
     * @param executionId context id
     * @param allQueriesInBundle ist of all queries in the bundle
     * @param results list of query results, to be filled with raw counts
     * @return stats
     */
    private PreparationQueryStats collectPreparationStats(long executionId, InternalQuery[] allQueriesInBundle, List<BloomBoxQueryResult> results,
            UpScaler upScaler) {
        PreparationQueryStats res = upScaler.createNewStatsInstance();

        Map<Long, BbqExpression> allExpressions = new HashMap<>();

        for (int i = 0; i < allQueriesInBundle.length; i++) {
            InternalQuery query = allQueriesInBundle[i];
            if (query instanceof ErrorPlaceholderQuery) {
                results.get(i).setErrorMessage(((ErrorPlaceholderQuery) query).getErrorMessage());
            }
            else {
                try {
                    BbqExpression expression = query.getBaseQuery().getExpression();
                    res.getMainQueryResultMap().put(expression.getExpressionId(), results.get(i));
                    expression.collectUniqueDepthFirst().forEach(e -> allExpressions.put(e.getExpressionId(), e));

                    if (query.subQueries != null) {

                        List<BbqExpression> subExpressionList = Arrays.stream(query.subQueries).map(BloomFilterQuery::getExpression)
                                .collect(Collectors.toList());

                        subExpressionList
                                .forEach(subExpression -> subExpression.collectUniqueDepthFirst().forEach(e -> allExpressions.put(e.getExpressionId(), e)));
                        subExpressionList.stream().map(subExpression -> new AndExpression(Arrays.asList(expression, subExpression)))
                                .forEach(e -> allExpressions.put(e.getExpressionId(), e));

                        upScaler.handleQueryExpressionsPrepared(query, allExpressions, res);

                        final BloomBoxQueryResult queryResult = results.get(i);
                        subExpressionList.forEach(subExpression -> res.getMainQueryResultMap().put(subExpression.getExpressionId(), queryResult));
                    }

                }
                catch (RuntimeException ex) {
                    results.get(i).setErrorMessage(
                            BbxMessage.ERR_PREPARING.format(String.format(" Error preparing query %s: %s", query, BbxMessage.ERR_COMMON.format(ex))));
                }
            }
        }
        List<BbqExpression> allExpressionsList = new ArrayList<>(allExpressions.values());
        return executePreparationQuery(executionId, allExpressionsList, res);
    }

    /**
     * Executes the given query bundle after validation
     * 
     * @param queryBundle one or multiple queries in a bundle
     * @return result of the bundle
     */
    public QueryBundleResult execute(QueryBundle queryBundle) {
        QueryBundleResult res = new QueryBundleResult();
        try {
            long executionId = queryBundle.getExecutionId();
            if (executionId <= 0) {
                executionId = ExpressionIdUtil.createExpressionId(UUID.randomUUID().toString());
            }
            queryBundle.validateShallow();
            Map<String, Long> nameReferenceMap = new HashMap<>();

            List<BloomBoxQuery> allQueries = new ArrayList<>();
            allQueries.addAll(queryBundle.getBaseQueries());
            allQueries.addAll(queryBundle.getPostQueries());

            List<String> warnings = new ArrayList<>();
            InternalQuery[] allInternalQueries = prepareInternalQueries(allQueries, nameReferenceMap, warnings);

            List<BloomBoxQueryResult> allResults = execute(executionId, queryBundle.getUpScalingConfig(), allInternalQueries);

            for (int i = 0; i < queryBundle.getBaseQueries().size(); i++) {
                BloomBoxQueryResult queryResult = allResults.get(i);
                if (warnings.get(i) != null && !warnings.get(i).isEmpty()) {
                    queryResult.setWarningMessage(warnings.get(i));
                }
                res.getBaseQueryResults().add(queryResult);
            }
            for (int i = 0; i < queryBundle.getPostQueries().size(); i++) {
                int idx = i + queryBundle.getBaseQueries().size();
                BloomBoxQueryResult queryResult = allResults.get(idx);
                if (warnings.get(idx) != null && !warnings.get(idx).isEmpty()) {
                    queryResult.setWarningMessage(warnings.get(idx));
                }
                res.getPostQueryResults().add(queryResult);
            }
        }
        catch (RuntimeException ex) {
            res.setMasterError(BbxMessage.ERR_COMMON.format(ex));
        }
        return res;

    }

    /**
     * Convenience method to execute some queries (auto-creates a bundle)
     * 
     * @param queries some queries to be executed
     * @return results in the same order as input queries
     */
    public List<BloomBoxQueryResult> execute(BloomBoxQuery... queries) {
        Arrays.stream(queries).forEach(BloomBoxQuery::validateShallow);
        QueryBundle bundle = new QueryBundle();
        for (BloomBoxQuery query : queries) {
            if (query.getType() == QueryType.BASIC_QUERY) {
                bundle.getBaseQueries().add(query);
            }
            else if (query.getType() == QueryType.POST_QUERY) {
                bundle.getPostQueries().add(query);
            }
        }
        QueryBundleResult result = execute(bundle);
        int idxBasic = 0;
        int idxPost = 0;
        List<BloomBoxQueryResult> res = new ArrayList<>();
        for (BloomBoxQuery query : queries) {
            if (query.getType() == QueryType.BASIC_QUERY) {
                res.add(result.getBaseQueryResults().get(idxBasic));
                idxBasic++;
            }
            else if (query.getType() == QueryType.POST_QUERY) {
                res.add(result.getPostQueryResults().get(idxPost));
                idxPost++;
            }
        }
        return res;
    }

    /**
     * Executes a list of internal queries
     * 
     * @param executionId context id of the execution, usually the bundle
     * @param queries internal queries
     * @return result list
     */
    List<BloomBoxQueryResult> execute(long executionId, InternalQuery... queries) {

        logExecutionPlan(queries);
        List<BloomBoxQueryResult> results = new ArrayList<>(queries.length);

        Arrays.stream(queries).forEach(query -> results.add(new BloomBoxQueryResult(executionId, query.getName(), query.subQueryLabels)));

        QueryDelegate queryDelegate = new SimpleQueryDelegate(queries, results);

        dataStore.dispatch(queryDelegate);

        queryDelegate.finish();
        logExecutionResults(results);

        return results;

    }

    /**
     * @param queries to be logged
     */
    private void logExecutionPlan(InternalQuery... queries) {
        if (LOGGER.isTraceEnabled()) {

            StringBuilder sb = new StringBuilder();
            sb.append("Executing: \n");

            for (InternalQuery query : queries) {
                sb.append("    query '");
                sb.append(query.getName());
                sb.append("': \n");
                query.getBaseQuery().getExpression().appendAsTree(sb, 4, "");
                sb.append("\n");
                if (query.subQueries != null) {
                    for (int i = 0; i < query.subQueries.length; i++) {
                        sb.append("        sub query '");
                        sb.append(query.subQueryLabels[i]);
                        sb.append("': \n");
                        query.subQueries[i].getExpression().appendAsTree(sb, 6, "");
                        sb.append("\n");
                    }
                }
            }

            LOGGER.trace(sb.toString());
        }
        else if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Executing {}", Arrays.stream(queries).map(InternalQuery::getName).collect(Collectors.toList()));
        }
    }

    /**
     * @param results to be logged
     */
    private void logExecutionResults(List<BloomBoxQueryResult> results) {
        if (LOGGER.isTraceEnabled()) {

            StringBuilder sb = new StringBuilder();
            sb.append("Query Results: \n");

            for (BloomBoxQueryResult result : results) {
                sb.append("    query '");
                sb.append(result.getName());
                sb.append("': ");
                sb.append(result.getBaseQueryCount());
                sb.append("\n");
                for (Map.Entry<String, Long> entry : result.buildSubQueryResultMap().entrySet()) {
                    sb.append("        sub query '");
                    sb.append(entry.getKey());
                    sb.append("': ");
                    sb.append(entry.getValue());
                    sb.append("\n");
                }
                if (result.getProtocol() != null) {
                    sb.append("\n");
                    sb.append(result.getProtocol().getEntries().stream().collect(Collectors.joining("\n")));
                    sb.append("\n\n");
                }
            }
            LOGGER.trace(sb.toString());
        }
        else if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("Query Results: {}", results);
        }
    }

    /**
     * If there is only a single scale factor we can do linear result scaling without involving an upscaler
     * 
     * @param scaleFactor fixed factor
     * @param maxPopulation population limit to detect and cut overscaling
     * @param results the query results to be scaled
     */
    void applyLinearScaleFactor(double scaleFactor, long maxPopulation, List<BloomBoxQueryResult> results) {
        for (int i = 0; i < results.size(); i++) {
            BloomBoxQueryResult result = results.get(i);
            if (!result.checkIfError()) {
                long scaledCount = scaleCount(result.getBaseQueryCount(), scaleFactor, maxPopulation, result.getName(), result);
                result.setBaseQueryCount(scaledCount);
                long[] subQueryCounts = result.getSubQueryCounts();
                if (subQueryCounts != null) {
                    for (int j = 0; j < subQueryCounts.length; j++) {
                        subQueryCounts[j] = scaleCount(subQueryCounts[j], scaleFactor, maxPopulation, result.getName() + "." + result.getSubQueryLabels()[i],
                                result);
                    }
                }
            }
        }
    }

    /**
     * Scales a single count by applying the factor
     * 
     * @param rawCount number to be scaled
     * @param scaleFactor fixed factor
     * @param maxPopulation population limit to detect and cut overscaling
     * @param queryName name of the query (for debugging)
     * @param result for writing warnings
     * @return scaled count
     */
    private long scaleCount(long rawCount, double scaleFactor, long maxPopulation, String queryName, BloomBoxQueryResult result) {
        long scaledCount = (long) Math.ceil(scaleFactor * rawCount);
        if (scaledCount > maxPopulation) {
            result.setWarningMessage((result.getWarningMessage() == null ? "" : result.getWarningMessage() + "\n") + BbxMessage.WARN_OVERSCALE
                    .format(String.format("Overscaling detected for query '%s', adjusted %d -> %d.", queryName, scaledCount, maxPopulation)));
            scaledCount = maxPopulation;

        }
        return scaledCount;
    }

    /**
     * Runs the queries and performs scaling if configured
     * 
     * @param executionId identifies execution, usually the bundle execution
     * @param config upscaling config, null disables scaling
     * @param queries internal queries to be executed
     * @return result list
     */
    List<BloomBoxQueryResult> execute(long executionId, UpScalingConfig config, InternalQuery... queries) {

        List<BloomBoxQueryResult> res = null;
        if (config == null || config.getAttributeScalingFactors() == null || config.getAttributeScalingFactors().isEmpty()) {
            res = execute(executionId, queries);
            if (config != null) {
                applyLinearScaleFactor(config.getBaseScalingFactor(), config.getTargetPopulationSize(), res);
                updateOversizeWarnings(res, config.getTargetPopulationSize(), true);
            }
            else {
                updateOversizeWarnings(res, dataStore.getNumberOfRows(), false);
            }
        }
        else {
            res = executeWithUpScaling(executionId, config, queries);
            updateOversizeWarnings(res, config.getTargetPopulationSize(), true);
        }
        return res;
    }

    /**
     * Adds warnings on high counts
     * 
     * @param results current query result
     * @param max maximum expected count
     * @param scaled if the result is scaled to population
     */
    private void updateOversizeWarnings(List<BloomBoxQueryResult> results, long max, boolean scaled) {
        for (BloomBoxQueryResult result : results) {
            if (result.getBaseQueryCount() >= max) {
                String queryName = result.getName();
                addOverscaleWarning(result, queryName, max, scaled);
            }
            if (result.getSubQueryCounts() != null) {
                for (int i = 0; i < result.getSubQueryCounts().length; i++) {
                    long count = result.getSubQueryCounts()[i];
                    if (count >= max) {
                        String queryName = result.getName() + "." + result.getSubQueryLabels()[i];
                        addOverscaleWarning(result, queryName, max, scaled);
                    }
                }
            }
        }
    }

    /**
     * Adds a single overscale warning to the result
     * 
     * @param result current result
     * @param queryName name of the related query or sub query
     * @param max the maximum value that was exceeded
     * @param scaled if true this is a scaled result
     */
    private void addOverscaleWarning(BloomBoxQueryResult result, String queryName, long max, boolean scaled) {
        String warning = result.getWarningMessage() == null ? "" : (result.getWarningMessage() + "\n");
        if (scaled) {
            warning = warning + BbxMessage.WARN_ALWAYS_MAX_SCALED
                    .format(String.format("The result count for query '%s' includes the total population (%d).", queryName, max));
        }
        else {
            warning = warning + BbxMessage.WARN_ALWAYS_MAX_COUNT
                    .format(String.format("The result count for query '%s' includes all %d rows, this may indicate a wrong query.", queryName, max));
        }
        result.setWarningMessage(warning);
    }

    /**
     * Runs the queries and performs scaling
     * 
     * @param executionId identifies usually the bundle execution
     * @param config scaling config
     * @param queries internal queries to be executed
     * @return result list (after scaling)
     */
    private List<BloomBoxQueryResult> executeWithUpScaling(long executionId, UpScalingConfig config, InternalQuery... queries) {
        config.validateSettings(this.dataStore.getNumberOfRows());

        List<BloomBoxQueryResult> results = new ArrayList<>(queries.length);

        Arrays.stream(queries).forEach(query -> results.add(new BloomBoxQueryResult(executionId, query.getName(), query.subQueryLabels)));

        UpScaler upScaler = null;
        PreparationQueryStats stats = null;

        try {
            upScaler = upScalerFactory.createUpScaler(this, config);
            stats = collectPreparationStats(executionId, queries, results, upScaler);
            upScaler.handleQueryBundleResults(stats);
        }
        catch (RuntimeException ex) {
            LOGGER.error("Unexpected error calling upscaler factory", ex);
        }

        if (upScaler != null) {
            applyUpScaler(stats, results, upScaler, queries);
        }
        else {
            for (int i = 0; i < queries.length; i++) {
                if (!results.get(i).checkIfError()) {
                    results.get(i).setErrorMessage(BbxMessage.ERR_UPSCALER_UNAVAILABLE.format("General upscaling error (upscaler unavailable)"));
                }
            }
        }

        return results;
    }

    /**
     * Runs the upscaler on the given results
     * 
     * @param stats preparation query results
     * @param results list of results to be scaled
     * @param upScaler scaler instance
     * @param queries internal queries that were executed, same order as results
     */
    private void applyUpScaler(PreparationQueryStats stats, List<BloomBoxQueryResult> results, UpScaler upScaler, InternalQuery... queries) {
        for (int i = 0; i < queries.length; i++) {
            if (!results.get(i).checkIfError()) {
                try {
                    upScaler.adjustResultCounts(queries[i], results.get(i), stats);
                }
                catch (RuntimeException ex) {
                    results.get(i).setErrorMessage(BbxMessage.ERR_UPSCALING.format(String.format("Upscaling error for query %s", queries[i]), ex));
                }
            }
        }
    }

    /**
     * @return configured {@link UpScalerFactory}, by default this is {@link DefaultUpScaler#FACTORY}
     */
    public UpScalerFactory getUpScalerFactory() {
        return upScalerFactory;
    }

    /**
     * @param upScalerFactory factory, by default this is {@link DefaultUpScaler#FACTORY}
     */
    public void setUpScalerFactory(UpScalerFactory upScalerFactory) {
        this.upScalerFactory = upScalerFactory;
    }

}
