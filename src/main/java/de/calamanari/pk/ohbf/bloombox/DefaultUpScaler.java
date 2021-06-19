//@formatter:off
/*
 * DefaultUpScaler
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

import de.calamanari.pk.ohbf.bloombox.bbq.AndExpression;
import de.calamanari.pk.ohbf.bloombox.bbq.BbqBooleanLiteral;
import de.calamanari.pk.ohbf.bloombox.bbq.BbqExpression;
import de.calamanari.pk.ohbf.bloombox.bbq.BinaryMatchExpression;
import de.calamanari.pk.ohbf.bloombox.bbq.NegationExpression;
import de.calamanari.pk.ohbf.bloombox.bbq.OrExpression;

/**
 * The {@link DefaultUpScaler} extrapolates counts from a BloomBox query to a target population based on a given {@link UpScalingConfig}. With the preparation
 * query we count on all levels (per sub-expression), so that we know the weights and can compute a total upscaling factor based on the configured factors.
 * <p>
 * <b>Note: This implementation is EXPERIMENTAL!</b> - Carefully review results!
 * <p>
 * Scaling with individual factors for different attributes can be tricky and needs careful adjustments. Be aware that this implementation relies on numerous
 * auxiliary queries to handle AND-expressions. This may have negative performance impact on large query bundles. Also setting factors for attribute values is
 * very hard to use, because it can lead to problematic deviations between expressions that should return the same results. E.g. be there a column
 * <code>Status</code> with the possible values <code>On</code>, <code>Off</code> and <code>Unknown</code>. The raw results for the expression
 * <code>A=On or A=Unknown</code> and <code>A!=On</code> should be equivalent. Depending on the factor configuration this cannot always be guaranteed as it
 * leads to different technical queries. Another problem that may lead to surprises is the size of the target population. If the target population is too small
 * you can try scaling a single attribute without seeing measurable effects anymore on a given AND-expression.
 * <p>
 * <b>Advice:</b> Whenever possible, feed the bloom box with a representative sample without any need for sophisticated upscaling.
 * <p>
 * <b>Implementation Notes:</b>
 * <ul>
 * <li>The handling of AND-expressions is still somewhat a compromise between accuracy and combinatoric explosion.</li>
 * <li>This upscaler does <i>bottom-up scaling</i> taking into account all member expressions to compute an estimate for the full expression.</li>
 * <li>In an expression <code>E := (A AND B)</code> when you look at A to upscale E the upper limit of your estimate must be <code>scaled(B)</code>, because
 * <code>scaled(E) &lt;= scaled(A)</code> and <code>scaled(E) &lt;= scaled(B)</code>.</li>
 * <li>In an expression <code>E := (A AND B AND C)</code> you also need to consider C.</li>
 * <li>Unfortunately, when you look at A to upscale E the upper limit additionally could be lowered by the combination of B and C,
 * <code>scaled(E) &lt;= scaled(B and C)</code></li>
 * <li>The higher the number of members in an AND-expression the bigger the complexity.</li>
 * <li>This implementation is cheating a little bit as I perform some extra queries to get the missing combination counts but not all of them. Given the
 * expression <code>E := (A AND B AND C AND D)</code> we additionally query <code>E1 := (A AND B AND C)</code>, <code>E2 := (B AND C AND D)</code>,
 * <code>E3 := (A AND C AND D)</code> as well as <code>E4 := (A AND B AND D)</code> but not also the 2-tuples like <code>(B AND D)</code>, which would be needed
 * to do it perfectly.</li>
 * <li>These correction query counts can be used to drastically improve the estimation of a maximum target count for <code>scaled(A)</code>,
 * <code>scaled(B)</code>, <code>scaled(C)</code> and <code>scaled(D)</code>.</li>
 * <li>In most practical cases this will create the illusion of correctness to a user comparing the counts from different queries. However, you can still easily
 * construct cases where the outcome of two queries may not match as expected!</li>
 * </ul>
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class DefaultUpScaler implements UpScaler {

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultUpScaler.class);

    /**
     * Default upscaler factory
     */
    public static final UpScalerFactory FACTORY = new UpScalerFactory() {

        private static final long serialVersionUID = 1399817345238496377L;

        @Override
        public UpScaler createUpScaler(BloomBoxQueryRunner runner, UpScalingConfig config) {
            return new DefaultUpScaler(config);
        }

        /**
         * @return de-serialized factory (stateless, don't duplicate)
         */
        Object readResolve() {
            return FACTORY;
        }

    };

    /**
     * scaling configuration
     */
    protected final UpScalingConfig config;

    /**
     * the results from the previously executed counting query
     */
    protected PreparationQueryStats stats;

    /**
     * Maps expression-ids to leverages
     */
    private final Map<Long, Leverage> leverageCache = new HashMap<>();

    /**
     * If we do not know the leverage we take this one which would scale FALSE to the max and also TRUE.<br>
     * This leverage is defined as sizeOf(targetPopulation)/sizeOf(sourcePopulation).
     */
    protected Leverage defaultMaximumLeverage;

    /**
     * @param config scaling configuration, not null
     */
    public DefaultUpScaler(UpScalingConfig config) {
        this.config = config;
        if (LOGGER.isTraceEnabled()) {
            LOGGER.trace("Created upscaler for \nconfig={}", config);
        }
        else {
            LOGGER.debug("Created upscaler for targetPopulationSize={}", config.getTargetPopulationSize());
        }
    }

    @Override
    public void handleQueryBundleResults(PreparationQueryStats stats) {
        this.stats = stats;
        this.defaultMaximumLeverage = new Leverage(((double) config.getTargetPopulationSize()) / stats.getNumberOfRows(),
                ((double) config.getTargetPopulationSize()) / stats.getNumberOfRows());

    }

    @Override
    public void handleQueryExpressionsPrepared(InternalQuery query, Map<Long, BbqExpression> bundleExpressionMap, PreparationQueryStats stats) {
        this.addAndCorrectionExpressions(bundleExpressionMap, stats);
    }

    @Override
    public void adjustResultCounts(InternalQuery query, BloomBoxQueryResult result, PreparationQueryStats stats) {
        result.setBaseQueryCount(computeScaledCount(query.getName(), result, query.getBaseQuery().getExpression(), config.getTargetPopulationSize()));
        long baseQueryCount = result.getBaseQueryCount();
        if (query.subQueries != null) {
            for (int i = 0; i < query.subQueries.length; i++) {
                AndExpression andExpression = new AndExpression(Arrays.asList(query.getBaseQuery().getExpression(), query.subQueries[i].getExpression()));
                result.getSubQueryCounts()[i] = computeScaledCount(query.getName() + "." + query.subQueryLabels[i], result, andExpression, baseQueryCount);
            }
        }
    }

    /**
     * Computes the scaled count based on the source count of the expression and the cached scale factor
     * 
     * @param queryName name of the query / sub query (for warnings)
     * @param result for warnings
     * @param expression key
     * @param maximum to prevent overscaling
     * @return scaled count
     */
    private long computeScaledCount(String queryName, BloomBoxQueryResult result, BbqExpression expression, long maxAcceptable) {
        computeAndCacheLeverages(expression);
        double factor = getCachedLeverage(expression).scaleFactor;
        long sourceCount = getSourceCount(expression);
        long res = (long) Math.ceil(factor * sourceCount);
        LOGGER.trace("Scaling '{}': {} -> {} ({})", queryName, sourceCount, res, factor);
        if (res > maxAcceptable) {
            result.setWarningMessage((result.getWarningMessage() == null ? "" : result.getWarningMessage() + "\n") + BbxMessage.WARN_OVERSCALE
                    .format(String.format("Overscaling detected for query '%s', adjusted %d -> %d. ", queryName, res, maxAcceptable)));
            res = maxAcceptable;
        }
        return res;
    }

    /**
     * Method to be called to fill the leverage cache. The main computation only works if the cache is ready.
     * 
     * @param rootExpression a query we need the leverages for the element expressions to be cached
     */
    private void computeAndCacheLeverages(BbqExpression rootExpression) {
        List<BbqExpression> expressionsBottomUp = rootExpression.collectUniqueDepthFirst();
        Leverage leverage = null;
        for (BbqExpression expression : expressionsBottomUp) {
            if (expression instanceof BinaryMatchExpression) {
                leverage = computeLeverage((BinaryMatchExpression) expression);
            }
            else if (expression instanceof NegationExpression) {
                leverage = computeLeverage((NegationExpression) expression);
            }
            else if (expression instanceof AndExpression) {
                leverage = computeLeverage((AndExpression) expression);
            }
            else if (expression instanceof OrExpression) {
                leverage = computeLeverage((OrExpression) expression);
            }
            else if (expression instanceof BbqBooleanLiteral) {
                leverage = computeLeverage((BbqBooleanLiteral) expression);
            }
            else {
                throw new QueryPreparationException(String.format("Unsupported BbqExpression type: %s, root expression was: %s", expression, rootExpression));
            }
            LOGGER.trace("Computed leverage for BbqExpression {}: {}", expression, leverage);
        }
    }

    /**
     * Computes the combined leverage for an AND-expression
     * 
     * @param expression key
     * @return combined leverage
     */
    private Leverage computeLeverage(AndExpression expression) {
        return leverageCache.computeIfAbsent(expression.getExpressionId(), key -> computeLeverageForAND(expression));
    }

    /**
     * Computes the combined leverage for an OR-expression
     * 
     * @param expression key
     * @return combined leverage
     */
    private Leverage computeLeverage(OrExpression expression) {
        return leverageCache.computeIfAbsent(expression.getExpressionId(),
                key -> computeWeightedLeverageForOR(getSourceCount(expression), expression.getChildExpressions()));
    }

    /**
     * @param expression key
     * @return substitute average leverage (extrapolatesource matches to population)
     */
    private Leverage computeLeverage(BbqBooleanLiteral expression) {
        return leverageCache.computeIfAbsent(expression.getExpressionId(), key -> defaultMaximumLeverage);
    }

    /**
     * @param expression key
     * @return complement leverage of the expression inside the negation
     */
    private Leverage computeLeverage(NegationExpression expression) {
        return leverageCache.computeIfAbsent(expression.getExpressionId(), key -> getCachedLeverage(expression.getChildExpressions().get(0)).negate());
    }

    /**
     * @param expression key
     * @return leverage for this leave expression
     */
    private Leverage computeLeverage(BinaryMatchExpression expression) {
        return leverageCache.computeIfAbsent(expression.getExpressionId(), key -> computeBaseLeverage(expression));
    }

    /**
     * @param expression key
     * @return return leverage with closest configured scale factors
     */
    private Leverage computeBaseLeverage(BinaryMatchExpression expression) {
        double scaleFactor = getConfiguredScaleFactor(expression.getArgName(), expression.getArgValue());
        // note: I don't use the complement here to make queries more consistent/intuitive (equals vs. NOT IN),
        // even if this is not quite correct
        return new Leverage(scaleFactor, getConfiguredScaleFactor(expression.getArgName(), null));
    }

    /**
     * Computes the complement scale factor (how to scale negative matches) based on the total population
     * 
     * @param sourceCount number of matches
     * @param scaleFactor the positive scale factor
     * @return complement factor
     */
    protected double computeComplementScaleFactor(long sourceCount, double scaleFactor) {
        double targetCount = sourceCount * scaleFactor;
        double complementScaleFactor = 0;
        if (targetCount > config.getTargetPopulationSize()) {
            double overScaleWeight = (targetCount / config.getTargetPopulationSize());
            double overScaledPopulation = stats.getNumberOfRows() * scaleFactor * overScaleWeight;
            double complementTargetCount = overScaledPopulation - targetCount;
            double complementSourceCount = ((double) stats.getNumberOfRows()) - sourceCount;
            complementScaleFactor = (complementTargetCount / complementSourceCount);
        }
        else {
            double complementTargetCount = config.getTargetPopulationSize() - targetCount;
            double complementSourceCount = ((double) stats.getNumberOfRows()) - sourceCount;
            complementScaleFactor = (complementSourceCount == 0) ? 0 : (complementTargetCount / complementSourceCount);
        }
        return complementScaleFactor;
    }

    /**
     * Computes the weighted average leverage (for a logical OR) based on multiple expressions. Each factor will be weighted by the number of rows that
     * contributed to the expressions result on the total number of rows.
     * 
     * @param expressions input
     * @return combined leverage
     */
    protected Leverage computeWeightedLeverageForOR(long parentSourceCount, List<BbqExpression> expressions) {

        double sumOfSourceCounts = 0;
        double sumOfScaledCounts = 0;
        for (BbqExpression expression : expressions) {
            Leverage leverage = getCachedLeverage(expression);
            long sourceCount = getSourceCount(expression);
            if (sourceCount > 0) {
                double sourceWeight = ((double) parentSourceCount) / sourceCount;
                sumOfSourceCounts = sumOfSourceCounts + (sourceCount * sourceWeight);
                sumOfScaledCounts = sumOfScaledCounts + (leverage.scaleFactor * sourceCount * sourceWeight);
            }
        }

        double scaleFactor = sumOfSourceCounts == 0 ? 0 : sumOfScaledCounts / sumOfSourceCounts;

        Leverage res = null;

        if (scaleFactor > 0) {
            double parentTargetCount = Math.min(parentSourceCount * scaleFactor, config.getTargetPopulationSize());
            scaleFactor = parentTargetCount / parentSourceCount;
            res = new Leverage(scaleFactor, computeComplementScaleFactor(parentSourceCount, scaleFactor));
            LOGGER.trace("Computed leverage {} for OR: parentSourceCount={}, expressions={}, sumOfScaledCounts={}, sumOfSourceCounts={}", res,
                    parentSourceCount, expressions, sumOfScaledCounts, sumOfSourceCounts);
        }
        else {
            res = new Leverage(0, defaultMaximumLeverage.complementScaleFactor);
            LOGGER.trace("Computed res={} for OR (parentSourceCount=0): expressions={}", res, expressions);
        }
        return res;
    }

    /**
     * Computes the weighted leverage for an AND taking into account the minimum number records reachable.
     *
     * @param andExpression expression to be processed
     * @return leverage
     */
    protected Leverage computeLeverageForAND(AndExpression andExpression) {

        long parentSourceCount = getSourceCount(andExpression);
        List<BbqExpression> expressions = andExpression.getChildExpressions();

        Leverage res = null;

        if (parentSourceCount > 0) {

            double sumOfSourceCounts = 0;

            double sumScaledSourceCount = 0;

            for (BbqExpression expression : expressions) {
                long sourceCount = getSourceCount(expression);

                if (sourceCount > 0 && expression != BbqBooleanLiteral.TRUE) {

                    double maxTargetCount = computeMaxTargetCount(andExpression.getExpressionId(), expressions, expression);

                    Leverage leverage = getCachedLeverage(expression);

                    double scaledSource = Math.min(leverage.scaleFactor * sourceCount, maxTargetCount);
                    sumScaledSourceCount = sumScaledSourceCount + scaledSource;

                    sumOfSourceCounts = sumOfSourceCounts + sourceCount;

                }
            }

            double scaleFactor = sumOfSourceCounts == 0 ? 0 : sumScaledSourceCount / sumOfSourceCounts;

            double parentTargetCount = Math.min(scaleFactor * parentSourceCount, config.getTargetPopulationSize());

            scaleFactor = parentTargetCount / parentSourceCount;
            res = new Leverage(scaleFactor, computeComplementScaleFactor(parentSourceCount, scaleFactor));
            LOGGER.trace("Computed leverage={} for AND: parentSourceCount={}, expressions={}, parentTargetCount={}", res, parentSourceCount, expressions,
                    parentTargetCount);
        }
        else {
            res = new Leverage(0, defaultMaximumLeverage.complementScaleFactor);
            LOGGER.trace("Computed res={} for AND (parentSourceCount=0): expressions={}", res, expressions);
        }
        return res;
    }

    /**
     * Estimates the maximum target count based on the assumption that an overscaled member of an AND cannot cause higher numbers than the remaining
     * expressions.
     * 
     * @param andExpressionId id of the and expression
     * @param childExpressions members of the AND expression
     * @param skippedExpression expression not to take into account when computing maximum
     * @return upper bound for scaled counts
     */
    private double computeMaxTargetCount(long andExpressionId, List<BbqExpression> childExpressions, BbqExpression skippedExpression) {
        if (childExpressions.isEmpty()) {
            return 0;
        }
        else if (childExpressions.size() == 1) {
            return config.getTargetPopulationSize();
        }
        else {

            double maxTargetCount = config.getTargetPopulationSize();
            long correctionAndExpressionId = getAndCorrectionExpressionId(andExpressionId, skippedExpression.getExpressionId());

            for (BbqExpression childExpression : childExpressions) {
                if (childExpression.getExpressionId() != skippedExpression.getExpressionId()) {
                    maxTargetCount = Math.max(computeScaledSourceCount(correctionAndExpressionId, childExpression), maxTargetCount);
                }
            }
            return maxTargetCount;
        }
    }

    /**
     * Computes the scaled count based on member and parent counts, uses the leverage of the member
     * 
     * @param parentAndExpressionId the parent AND
     * @param childExpression the member to compute the estimated target count
     * @return target count related to this parent
     */
    protected double computeScaledSourceCount(long parentAndExpressionId, BbqExpression childExpression) {
        double parentSourceCount = getSourceCount(parentAndExpressionId);
        double sourceCount = getSourceCount(childExpression);
        double sourceWeight = sourceCount == 0 ? 0 : parentSourceCount / sourceCount;
        return sourceCount * sourceWeight * getCachedLeverage(childExpression).scaleFactor;
    }

    /**
     * @param expression key
     * @return cached or computed leverage
     * @throws QueryPreparationException if preconditions are not met
     */
    protected Leverage getCachedLeverage(BbqExpression expression) {
        Leverage leverage = leverageCache.get(expression.getExpressionId());
        if (leverage == null) {
            throw new QueryPreparationException("Wrong preparation order (BUG!), could not find leverage for expression " + expression.toString());
        }
        return leverage;
    }

    /**
     * @param expression key
     * @return number of rows the given expression evaluated to true
     * @throws QueryPreparationException if preconditions are not met
     */
    protected long getSourceCount(BbqExpression expression) {
        Long matchCount = stats.getExpressionLevelCounts().get(expression.getExpressionId());
        if (matchCount == null) {
            throw new QueryPreparationException("Insufficient query stats (BUG!), could not find count for expression " + expression.toString());
        }
        return matchCount;
    }

    /**
     * @param expressionId key
     * @return number of rows the given expression evaluated to true
     * @throws QueryPreparationException if preconditions are not met
     */
    protected long getSourceCount(long expressionId) {
        Long matchCount = stats.getExpressionLevelCounts().get(expressionId);
        if (matchCount == null) {
            throw new QueryPreparationException("Insufficient query stats (BUG!), could not find count for expression " + expressionId);
        }
        return matchCount;
    }

    /**
     * Returns the source count of an and-expression without a skipped member (aka TRUE)
     * 
     * @param andExpressionId source expression id
     * @param skipExpressionId for concatenation (skip expression)
     * @return expressionId (correction expression)
     * @throws QueryPreparationException if preconditions are not met
     */
    private long getAndCorrectionExpressionId(long andExpressionId, long skipExpressionId) {
        String key = andExpressionId + "." + skipExpressionId;
        Long correctionExpressionId = stats.getAndCorrectionQueryMap().get(key);
        if (correctionExpressionId == null) {
            throw new QueryPreparationException("Wrong preparation order (BUG!), could not find and-correction expression: " + key);
        }
        return correctionExpressionId;
    }

    /**
     * Returns the finest scale factor that is configured based on attribute and value
     * 
     * @param argName attribute
     * @param argValue value (null skips attribute value checking)
     * @return finest configured factor
     */
    protected double getConfiguredScaleFactor(String argName, String argValue) {
        double res = config.getBaseScalingFactor();
        Map<String, AttributeScalingConfig> attrScales = config.getAttributeScalingFactors();
        if (attrScales != null) {
            AttributeScalingConfig attrCnf = attrScales.get(argName);
            if (attrCnf != null) {
                res = attrCnf.getScalingFactor();
                if (argValue != null) {
                    Map<String, Double> valueScales = attrCnf.getValueScalingFactors();
                    if (valueScales != null) {
                        Double valueScale = valueScales.get(argValue);
                        res = valueScale != null ? valueScale.doubleValue() : res;
                    }
                }
            }
        }
        LOGGER.trace("Returning configured scaleFactor={} for attribute={}, value={}, config={}", res, argName, argValue, config);
        return res;
    }

    /**
     * Adds the correction queries for the AND expressions to the map
     * <p>
     * These are auxiliary queries needed to estimate counts if certain members counts of an AND need to be determined considering the <i>remaining</i> members
     * scale differently.
     * 
     * @param allExpressions expression map, key is expression-id
     * @param res prepared stats, to be modified (register auxiliary expressions)
     */
    protected void addAndCorrectionExpressions(Map<Long, BbqExpression> allExpressions, PreparationQueryStats res) {
        List<BbqExpression> candidates = new ArrayList<>(allExpressions.values());
        for (BbqExpression expression : candidates) {
            if (expression instanceof AndExpression) {
                AndExpression andExpression = (AndExpression) expression;
                addAndCorrectionExpressions(andExpression, allExpressions, res);
            }
        }
    }

    /**
     * Adds the correction queries for this AND expression to the map
     * 
     * @param andExpression expression map, key is expression-id
     * @param allExpressions expression map, key is expression-id
     * @param res prepared stats, to be modified (register auxiliary expressions)
     */
    private void addAndCorrectionExpressions(AndExpression andExpression, Map<Long, BbqExpression> allExpressions, PreparationQueryStats res) {
        List<BbqExpression> childExpressionList = andExpression.getChildExpressions();

        for (BbqExpression childExpression : childExpressionList) {
            BbqExpression correctionExpression = createReducedCorrectionExpression(childExpressionList, childExpression.getExpressionId());
            allExpressions.putIfAbsent(correctionExpression.getExpressionId(), correctionExpression);
            String alias = "" + andExpression.getExpressionId() + "." + childExpression.getExpressionId();
            LOGGER.trace("Created auxiliary query {} -> {} to upscale {}", alias, correctionExpression.getExpressionId(), andExpression);
            res.getAndCorrectionQueryMap().put(alias, correctionExpression.getExpressionId());
        }
    }

    /**
     * Creates auxiliary expression to cover AND-expression scaling
     * 
     * @param childExpressionList members of AND
     * @param skippedExpressionId the expression to be excluded
     * @return new AND-expression with the reduced list of members
     */
    private BbqExpression createReducedCorrectionExpression(List<BbqExpression> childExpressionList, long skippedExpressionId) {

        if (childExpressionList.isEmpty()) {
            return BbqBooleanLiteral.FALSE;
        }
        else if (childExpressionList.size() == 1) {
            return BbqBooleanLiteral.TRUE;
        }
        else if (childExpressionList.size() == 2) {
            if (childExpressionList.get(0).getExpressionId() == skippedExpressionId) {
                return childExpressionList.get(1);
            }
            else {
                return childExpressionList.get(0);
            }
        }
        else {
            List<BbqExpression> reducedChildExpressionList = new ArrayList<>();
            for (BbqExpression childExpression : childExpressionList) {
                if (childExpression.getExpressionId() != skippedExpressionId) {
                    reducedChildExpressionList.add(childExpression);
                }
            }
            return new AndExpression(reducedChildExpressionList);
        }
    }

    /**
     * Container for the two scale factors (match/non-match) to be able to switch them ({@link #negate()}) whenever a NOT occurs.
     */
    protected static class Leverage {

        /**
         * scaling of matches against an expression
         */
        private final double scaleFactor;

        /**
         * scaling of unsuccessful matches against an expression
         */
        private final double complementScaleFactor;

        /**
         * @param scaleFactor scaling of matches against an expression
         * @param complementScaleFactor scaling of unsuccessful matches against an expression
         */
        protected Leverage(double scaleFactor, double complementScaleFactor) {
            this.scaleFactor = scaleFactor;
            this.complementScaleFactor = complementScaleFactor;
        }

        /**
         * Returns a new instance with exchanged factors (NOT)
         * 
         * @return complement leverage
         */
        protected Leverage negate() {
            return new Leverage(complementScaleFactor, scaleFactor);
        }

        @Override
        public String toString() {
            return this.getClass().getSimpleName() + "[" + scaleFactor + ", " + complementScaleFactor + "]";
        }
    }

}
