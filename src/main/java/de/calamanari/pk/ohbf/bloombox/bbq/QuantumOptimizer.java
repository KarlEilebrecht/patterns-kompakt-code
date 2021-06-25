//@formatter:off
/*
 * QuantumOptimizer
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

import static de.calamanari.pk.util.LambdaSupportLoggerProxy.defer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.calamanari.pk.ohbf.bloombox.PbDpavProbabilityManager;
import de.calamanari.pk.util.LambdaSupportLoggerProxy;

/**
 * The {@link QuantumOptimizer} was introduced later to better support probability-queries (quantity aggregation) which unfortunately suffer from nesting and
 * multiple refences to the same DPAV, see also comments in {@link PbDpavProbabilityManager}.
 * <p>
 * This implementation spends considerably more effort than the standard optimizer {@link IntermediateExpressionOptimizer} to reduce the complexity of
 * expression that will be used to aggregate the probabilities.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class QuantumOptimizer {

    private static final Logger LOGGER = LambdaSupportLoggerProxy.wrap(LoggerFactory.getLogger(QuantumOptimizer.class));

    /**
     * Comparator for sorting expressions by id to avoid flickering effects
     */
    public static final Comparator<BbqExpression> ID_ORDER_COMPARATOR = (BbqExpression e1, BbqExpression e2) -> {
        int res = 0;
        long v = e1.getExpressionId() - e2.getExpressionId();
        if (v < 0) {
            res = -1;
        }
        else if (v > 0) {
            res = 1;
        }
        return res;
    };

    /**
     * Tries to optimize the given query
     * 
     * @param query to be optimized
     * @return optimized query
     */
    public BloomFilterQuery optimize(BloomFilterQuery query) {

        final int complexityBefore = query.getExpression().computeComplexity();

        final BloomFilterQuery dbgQuery = query;
        LOGGER.trace("Query to be optimized (complexity={}): \n{}", complexityBefore, defer(() -> formatExpressionAsTree(dbgQuery.getExpression())));

        Map<Integer, BloomFilterQuery> expressionComplexityMap = new TreeMap<>();
        expressionComplexityMap.put(complexityBefore, query);

        int iteration = 0;
        BloomFilterQuery source = null;
        do {
            source = query;
            BbqExpression expression = query.getExpression();

            BbqExpression optimizedExpression = this.optimize(expression);

            if (optimizedExpression.getExpressionId() != expression.getExpressionId()) {
                query = new BloomFilterQuery(query.getSourceQuery(), optimizedExpression);
                expressionComplexityMap.put(optimizedExpression.computeComplexity(), query);
            }
            iteration++;
        } while (source.getId() != query.getId() && iteration < 10);

        // Above I limit the number of runs, just in case I overlooked any flickering ... ;)

        // take the version with the smallest complexity, optimizer should not increase complexity
        BloomFilterQuery best = expressionComplexityMap.values().iterator().next();
        if (query != best) {
            LOGGER.trace("Final optimization increased complexity, taking version with smallest complexity instead!");
            query = best;
        }

        final BloomFilterQuery dbgQueryAfter = query;
        LOGGER.trace("Optimized query (complexity={} ({})): \n{}", defer(() -> dbgQueryAfter.getExpression().computeComplexity()), complexityBefore,
                defer(() -> formatExpressionAsTree(dbgQueryAfter.getExpression())));
        return query;
    }

    /**
     * Optimizes the given expression, single run
     * 
     * @param expression to be optimized
     * @return expression or new expression
     */
    private BbqExpression optimize(BbqExpression expression) {
        if (isANDorOR(expression)) {

            final BbqExpression dbgExpression = expression;
            LOGGER.trace("analyzing: \n{}", defer(() -> formatExpressionAsTree(dbgExpression)));

            expression = pushDownNegations(mergeOverlaps(dissolveUselessNesting(dissolveUselessNandNor(dissolveUselessNesting(expression)))));

            final BbqExpression dbgExpression2 = expression;
            LOGGER.trace("result: \n{}", defer(() -> formatExpressionAsTree(dbgExpression2)));

        }
        return expression;
    }

    /**
     * Utility to format debugging output
     * 
     * @param expression to be explained
     * @return string for logging
     */
    protected String formatExpressionAsTree(BbqExpression expression) {
        StringBuilder sb = new StringBuilder();
        expression.appendAsTree(sb, 1, "");
        return sb.toString();
    }

    /**
     * Removes any useless nesting of ANDs and ORs
     * 
     * @param expression to be optimized
     * @return expression without any ANDs containing ANDs
     */
    BbqExpression dissolveUselessNesting(BbqExpression expression) {
        if (isANDorOR(expression)) {
            final BbqExpression dbgExpression2 = expression;
            LOGGER.trace("dissolveUselessNesting BEFORE: \n{}", defer(() -> formatExpressionAsTree(dbgExpression2)));
            boolean changed = false;
            List<BbqExpression> childExpressions = new ArrayList<>();
            for (BbqExpression childExpression : expression.getChildExpressions()) {
                BbqExpression fixed = dissolveUselessNesting(childExpression);
                changed = changed || (fixed.getExpressionId() != childExpression.getExpressionId());
                if (fixed.getClass() == expression.getClass()) {
                    childExpressions.addAll(fixed.getChildExpressions());
                    changed = true;
                }
                else {
                    childExpressions.add(fixed);
                }
            }
            if (changed) {
                expression = combine(childExpressions, expression.getClass());
            }
            final BbqExpression dbgExpression3 = expression;
            LOGGER.trace("dissolveUselessNesting AFTER: \n{}", defer(() -> formatExpressionAsTree(dbgExpression3)));
        }
        return expression;
    }

    /**
     * Looks for NOT AND resp. NOT OR and replaces them with the opposite if the members are negations
     * 
     * @param expression to be optimized
     * @return optimized expression
     */
    BbqExpression dissolveUselessNandNor(BbqExpression expression) {

        if (expression instanceof NegationExpression) {
            final BbqExpression dbgExpression2 = expression;
            LOGGER.trace("dissolveUselessNandNor BEFORE: \n{}", defer(() -> formatExpressionAsTree(dbgExpression2)));
            BbqExpression childExpression = expression.getChildExpressions().get(0);
            BbqExpression fixedChildExpression = dissolveMemberNegatedAndOrOfNegations(childExpression);
            if (fixedChildExpression.getExpressionId() != childExpression.getExpressionId()) {
                expression = negateExpression(fixedChildExpression);
            }
            final BbqExpression dbgExpression3 = expression;
            LOGGER.trace("dissolveUselessNandNor AFTER: \n{}", defer(() -> formatExpressionAsTree(dbgExpression3)));
        }
        else if (isANDorOR(expression)) {
            final BbqExpression dbgExpression2 = expression;
            LOGGER.trace("dissolveUselessNandNor BEFORE: \n{}", defer(() -> formatExpressionAsTree(dbgExpression2)));
            boolean changed = false;
            List<BbqExpression> childExpressions = new ArrayList<>();
            for (BbqExpression childExpression : expression.getChildExpressions()) {
                BbqExpression result = dissolveUselessNandNor(childExpression);
                changed = changed || (result.getExpressionId() != childExpression.getExpressionId());
                childExpressions.add(result);
            }
            if (changed) {
                expression = combine(childExpressions, expression.getClass());
            }
            final BbqExpression dbgExpression3 = expression;
            LOGGER.trace("dissolveUselessNandNor AFTER: \n{}", defer(() -> formatExpressionAsTree(dbgExpression3)));
        }
        return expression;
    }

    /**
     * Applies the optimization to all members of the expression (AND/OR)
     * 
     * @param expression AND or OR
     * @return AND or OR with optimized members
     */
    private BbqExpression dissolveMemberNegatedAndOrOfNegations(BbqExpression expression) {

        if (isANDorOR(expression)) {
            List<BbqExpression> childExpressions = expression.getChildExpressions();
            List<BbqExpression> fixedExpressions = new ArrayList<>();
            boolean changed = false;
            for (int i = 0; i < childExpressions.size(); i++) {
                BbqExpression childExpression = childExpressions.get(i);
                BbqExpression fixed = dissolveUselessNandNor(childExpression);
                changed = changed || (fixed.getExpressionId() != childExpression.getExpressionId());
                fixedExpressions.add(fixed);
            }

            if (changed) {
                expression = combine(fixedExpressions, expression.getClass());
            }
        }
        return expression;
    }

    /**
     * Pushing down negations unlocks some optization potential and at the same time it may positively impact the precision of the computation as every NOT is a
     * (1-P(X)).
     * 
     * @param expression to be optimized
     * @return optimized expression
     */
    private BbqExpression pushDownNegations(BbqExpression expression) {
        if (expression instanceof NegationExpression) {
            final BbqExpression dbgExpression2 = expression;
            LOGGER.trace("pushDownNegations BEFORE: \n{}", defer(() -> formatExpressionAsTree(dbgExpression2)));

            BbqExpression fixed = negatePushDown(expression.getChildExpressions().get(0));
            if (fixed.getExpressionId() != expression.getExpressionId()) {
                expression = fixed;
            }

            final BbqExpression dbgExpression3 = expression;
            LOGGER.trace("pushDownNegations AFTER: \n{}", defer(() -> formatExpressionAsTree(dbgExpression3)));
        }
        else if (isANDorOR(expression)) {
            final BbqExpression dbgExpression2 = expression;
            LOGGER.trace("pushDownNegations BEFORE: \n{}", defer(() -> formatExpressionAsTree(dbgExpression2)));

            boolean changed = false;
            List<BbqExpression> fixedChildExpressions = new ArrayList<>();
            for (BbqExpression childExpression : expression.getChildExpressions()) {
                BbqExpression fixed = pushDownNegations(childExpression);
                changed = changed || (fixed.getExpressionId() != childExpression.getExpressionId());
                fixedChildExpressions.add(fixed);
            }
            if (changed) {
                sortExpressionsByIdAndDedup(fixedChildExpressions);
                if (fixedChildExpressions.size() == 1) {
                    expression = fixedChildExpressions.get(0);
                }
                else {
                    expression = combine(fixedChildExpressions, expression.getClass());
                }
            }

            final BbqExpression dbgExpression3 = expression;
            LOGGER.trace("pushDownNegations AFTER: \n{}", defer(() -> formatExpressionAsTree(dbgExpression3)));
        }
        return expression;
    }

    /**
     * Negates the given expression, but so that the NOT goes down to the leaves (avoid NAND/NOR)
     * 
     * @param expression to be optimized
     * @return optimized expression
     */
    private BbqExpression negatePushDown(BbqExpression expression) {
        BbqExpression res = null;
        if (expression instanceof NegationExpression) {
            res = expression.getChildExpressions().get(0);
        }
        else if (isANDorOR(expression)) {
            List<BbqExpression> negatedChildBbqExpressions = new ArrayList<>();
            for (BbqExpression childExpression : expression.getChildExpressions()) {
                negatedChildBbqExpressions.add(negatePushDown(childExpression));
            }
            if (expression instanceof AndExpression) {
                res = combine(negatedChildBbqExpressions, OrExpression.class);
            }
            else {
                res = combine(negatedChildBbqExpressions, AndExpression.class);
            }
        }
        else {
            res = new NegationExpression(expression);
        }
        return res;
    }

    /**
     * This method detects overlaps between expressions and tries to rewrite the expression to avoid repetition.
     * <p>
     * Example: <code>(A and B and C) or (A and B and G)</code> should become <code>(A and B) and (C or G)</code>
     * 
     * @param expression to be optimized
     * @return optimized expression
     */
    BbqExpression mergeOverlaps(BbqExpression expression) {
        if (isANDorOR(expression)) {
            final BbqExpression dbgExpression2 = expression;
            LOGGER.trace("mergeOverlaps BEFORE: \n{}", defer(() -> formatExpressionAsTree(dbgExpression2)));

            List<BbqExpression> childExpressions = new ArrayList<>(expression.getChildExpressions());
            boolean changed = premergeChildExpressionOverlaps(childExpressions);
            changed = mergeIntraChildExpressionOverlaps(childExpressions, expression.getClass()) || changed;
            if (changed) {
                expression = combine(childExpressions, expression.getClass());
            }
            final BbqExpression dbgExpression3 = expression;
            LOGGER.trace("mergeOverlaps AFTER: \n{}", defer(() -> formatExpressionAsTree(dbgExpression3)));
        }
        return expression;
    }

    /**
     * Detectecs overlaps between the given child expressions of the parent
     * 
     * @param childExpressions members of an AND or OR
     * @param parentType AND or OR
     * @return true if there was any change
     */
    private boolean mergeIntraChildExpressionOverlaps(List<BbqExpression> childExpressions, Class<? extends BbqExpression> parentType) {
        boolean changed = false;
        for (int i = childExpressions.size() - 1; i > -1; i--) {
            for (int k = i - 1; k > -1; k--) {
                BbqExpression result = mergeIfOverlap(childExpressions.get(i), childExpressions.get(k), parentType);
                if (result != null) {
                    childExpressions.set(k, result);
                    childExpressions.remove(i);
                    changed = true;
                    break;
                }
            }
        }
        return changed;
    }

    /**
     * Ensures that the child expressions are all optimized (bottom up)
     * 
     * @param childExpressions members of an AND or OR
     * @return true if the list changed
     */
    private boolean premergeChildExpressionOverlaps(List<BbqExpression> childExpressions) {
        boolean changed = false;
        for (int i = 0; i < childExpressions.size(); i++) {
            BbqExpression result = mergeOverlaps(childExpressions.get(i));
            if (result != null) {
                childExpressions.set(i, result);
                changed = true;
            }
        }
        return changed;
    }

    /**
     * Merges the given expressions if possible
     * 
     * @param expression1 candidate
     * @param expression2 candidate
     * @param parentType AND or OR
     * @return merged expression or null if merge was not possible
     */
    private BbqExpression mergeIfOverlap(BbqExpression expression1, BbqExpression expression2, Class<? extends BbqExpression> parentType) {
        return (expression1.getExpressionId() == expression2.getExpressionId()) ? expression1 : mergeDetailsIfOverlap(expression1, expression2, parentType);
    }

    /**
     * After identifying a candidate as mergeable this method tries to perform the actual merge based on the overlap
     * 
     * @param expression1 candidate
     * @param expression2 candidate
     * @param parentType AND or OR
     * @return merged expression or null if the merge was unsuccessful
     */
    private BbqExpression mergeDetailsIfOverlap(BbqExpression expression1, BbqExpression expression2, Class<? extends BbqExpression> parentType) {
        BbqExpression res = null;
        if (isANDorOR(expression1) && isANDorOR(expression2) && expression1.getClass() == expression2.getClass()) {
            if (expression1.getClass() == parentType) {
                res = merge(expression1, expression2, parentType);
            }
            else {
                List<BbqExpression> overlap = findOverlap(expression1.getChildExpressions(), expression2.getChildExpressions());
                if (!overlap.isEmpty()) {
                    res = mergeWithOverlap(expression1, expression2, overlap, parentType);
                }
            }
        }
        else if (isANDorOR(expression1) && !isANDorOR(expression2)) {
            res = condenseIfContained(expression2, expression1, parentType);
        }
        else if (!isANDorOR(expression1) && isANDorOR(expression2)) {
            res = condenseIfContained(expression1, expression2, parentType);
        }
        return res;
    }

    /**
     * Performs the core merge of two expressions and returns the best combined result
     * 
     * @param expression1 candidate
     * @param expression2 candidate
     * @param type AND or OR (influences the combination strategy)
     * @return merged expression, NOT NULL
     */
    private BbqExpression merge(BbqExpression expression1, BbqExpression expression2, Class<? extends BbqExpression> type) {
        BbqExpression res = null;
        List<BbqExpression> childExpressions = new ArrayList<>();
        childExpressions.addAll(expression1.getChildExpressions());
        childExpressions.addAll(expression2.getChildExpressions());
        sortExpressionsByIdAndDedup(childExpressions);
        if (childExpressions.size() == 1) {
            res = childExpressions.get(0);
        }
        else {
            res = combine(childExpressions, type);
        }
        return res;
    }

    /**
     * Combines two expressions with the overlap
     * 
     * @param expression1 candidate
     * @param expression2 candidate
     * @param overlap previously computed overlap
     * @param parentType AND or OR, the type of the enclosing expression, to decides how to merge
     * @return new expression, NOT NULL
     */
    private BbqExpression mergeWithOverlap(BbqExpression expression1, BbqExpression expression2, List<BbqExpression> overlap,
            Class<? extends BbqExpression> parentType) {

        BbqExpression res = null;

        List<BbqExpression> additional1 = subtractExclusions(expression1.getChildExpressions(), overlap);
        List<BbqExpression> additional2 = subtractExclusions(expression2.getChildExpressions(), overlap);
        if (additional1.isEmpty()) {
            // expression2 is stricter than expression1
            if (parentType == AndExpression.class) {
                res = expression2;
            }
            else {
                res = expression1;
            }
        }
        else if (additional2.isEmpty()) {
            // expression1 is stricter than expression2
            if (parentType == AndExpression.class) {
                res = expression1;
            }
            else {
                res = expression2;
            }
        }
        else {
            if (parentType == AndExpression.class) {
                res = mergeWithOverlapAND(additional1, additional2, overlap);
            }
            else {
                res = mergeWithOverlapOR(additional1, additional2, overlap);
            }
        }
        return res;
    }

    /**
     * Merges OR candidates, producing AND to separate repeated conditions
     * 
     * @param additional1 sub expressions unique to candidate 1
     * @param additional2 sub expressions unique to candidate 2
     * @param overlap detected overlap
     * @return AND expression or single expression, NOT NULL
     */
    private BbqExpression mergeWithOverlapOR(List<BbqExpression> additional1, List<BbqExpression> additional2, List<BbqExpression> overlap) {
        List<BbqExpression> members = new ArrayList<>();
        List<BbqExpression> additionals = new ArrayList<>();
        if (!additional1.isEmpty()) {
            additionals.add(combine(additional1, AndExpression.class));
        }
        if (!additional2.isEmpty()) {
            additionals.add(combine(additional2, AndExpression.class));
        }
        members.addAll(overlap);
        members.add(combine(additionals, OrExpression.class));
        return combine(members, AndExpression.class);
    }

    /**
     * Merges AND candidates, producing AND to separate repeated conditions
     * 
     * @param additional1 sub expressions unique to candidate 1
     * @param additional2 sub expressions unique to candidate 2
     * @param overlap detected overlap
     * @return AND expression or single expression, NOT NULL
     */
    private BbqExpression mergeWithOverlapAND(List<BbqExpression> additional1, List<BbqExpression> additional2, List<BbqExpression> overlap) {
        List<BbqExpression> members = new ArrayList<>();
        members.addAll(overlap);
        List<BbqExpression> andMembers = new ArrayList<>();
        if (!additional1.isEmpty()) {
            andMembers.add(combine(additional1, OrExpression.class));
        }
        if (!additional2.isEmpty()) {
            andMembers.add(combine(additional2, OrExpression.class));
        }
        members.add(combine(andMembers, AndExpression.class));

        return combine(members, OrExpression.class);
    }

    /**
     * Creates a list with the elements in the exclusions list removed
     * 
     * @param expressions base list
     * @param exclusions elements NOT to be copied
     * @return expressions without exclusions
     */
    private List<BbqExpression> subtractExclusions(List<BbqExpression> expressions, List<BbqExpression> exclusions) {
        List<BbqExpression> res = new ArrayList<>();
        for (int i = 0; i < expressions.size(); i++) {
            BbqExpression candidate = expressions.get(i);
            boolean copy = true;
            for (int k = 0; k < exclusions.size() && copy; k++) {
                if (exclusions.get(k).getExpressionId() == candidate.getExpressionId()) {
                    copy = false;
                }
            }
            if (copy) {
                res.add(candidate);
            }
        }
        return sortExpressionsByIdAndDedup(res);
    }

    /**
     * @param expressions1 list of expressions
     * @param expressions2 list of expressions
     * @return list with elements contained in both lists
     */
    private List<BbqExpression> findOverlap(List<BbqExpression> expressions1, List<BbqExpression> expressions2) {
        List<BbqExpression> res = new ArrayList<>();
        for (int i = 0; i < expressions1.size(); i++) {
            for (int k = 0; k < expressions2.size(); k++) {
                BbqExpression candidate = expressions1.get(i);
                if (candidate.getExpressionId() == expressions2.get(k).getExpressionId()) {
                    res.add(candidate);
                }
            }
        }
        return sortExpressionsByIdAndDedup(res);
    }

    /**
     * Detects whether the given expression is contained in the AND or OR and may create a new expression to replace the AND or OR. This way we address
     * anomalies like <code>(A or B) AND B</code>.
     * 
     * @param singleExpression candidate (outside)
     * @param andOrOrExpression AND or OR to be analized
     * @param parentType outer AND or OR
     * @return replacement expression or null if not required
     */
    private BbqExpression condenseIfContained(BbqExpression singleExpression, BbqExpression andOrOrExpression, Class<? extends BbqExpression> parentType) {
        BbqExpression res = null;
        for (BbqExpression childExpression : andOrOrExpression.getChildExpressions()) {
            if (childExpression.getExpressionId() == singleExpression.getExpressionId()) {
                if (parentType == andOrOrExpression.getClass()) {
                    // (A AND B) AND B <=> (A AND B) resp. (A OR B) OR B <=> (A OR B)
                    res = andOrOrExpression;
                }
                else if (parentType == AndExpression.class && andOrOrExpression.getClass() == OrExpression.class) {
                    // (A OR B) AND B <=> B
                    res = singleExpression;
                }
                else if (parentType == OrExpression.class && andOrOrExpression.getClass() == AndExpression.class) {
                    // (A AND B) OR B <=> B
                    res = singleExpression;
                }
            }
        }
        return res;
    }

    /**
     * @param expression to be checked
     * @return true if the expression is an AND or OR
     */
    private boolean isANDorOR(BbqExpression expression) {
        return (expression instanceof AndExpression) || (expression instanceof OrExpression);
    }

    /**
     * This method creates ANDs and ORs if required. Internally it takes care of sorting and de-duplication of the nembers as well as finding boolean literals.
     * 
     * @param expressions member list
     * @param type AND or OR
     * @return a combined expression of the desired type or a single expression
     */
    private BbqExpression combine(List<BbqExpression> expressions, Class<? extends BbqExpression> type) {
        condenseExpressions(expressions, type);
        if (expressions.size() == 1) {
            return expressions.get(0);
        }
        else if (type == AndExpression.class) {
            return new AndExpression(expressions);
        }
        else if (type == OrExpression.class) {
            return new OrExpression(expressions);
        }
        else {
            throw new IllegalArgumentException(
                    String.format("Cannot create combined expression of type %s for %s - BUG, check your code!", type.getSimpleName(), expressions.toString()));
        }
    }

    /**
     * Negates the given expression intelligently and returns the new expression
     * 
     * @param expression to be negated
     * @return negated expression
     */
    private BbqExpression negateExpression(BbqExpression expression) {
        BbqExpression res = null;
        List<BbqExpression> childExpressions = expression.getChildExpressions();
        if (expression instanceof NegationExpression) {
            res = childExpressions.get(0);
        }
        else if (isANDorOR(expression) && childExpressions.stream().noneMatch(Predicate.not(NegationExpression.class::isInstance))) {
            // the child expression is an AND or OR with only NOTs as members
            // Goal: NOT ( AND(NOT(A), NOT(B)) ) => NOT ( NOR(A, B) ) => OR(A, B)

            // Implicitly negate the negated child expressions
            childExpressions = childExpressions.stream().map(e -> e.getChildExpressions().get(0)).collect(Collectors.toList());
            if (expression.getClass() == AndExpression.class) {
                res = combine(childExpressions, OrExpression.class);
            }
            else {
                res = combine(childExpressions, AndExpression.class);
            }
        }
        else {
            res = new NegationExpression(expression);
        }
        return res;
    }

    /**
     * @param expressions candidates
     * @return deduplicated list ordered by expression-id
     */
    private List<BbqExpression> sortExpressionsByIdAndDedup(List<BbqExpression> expressions) {
        Collections.sort(expressions, ID_ORDER_COMPARATOR);
        for (int i = expressions.size() - 2; i > -1; i--) {
            if (expressions.get(i).getExpressionId() == expressions.get(i + 1).getExpressionId()) {
                expressions.remove(i + 1);
            }
        }
        return expressions;
    }

    /**
     * This method orders the given expressions, and removes then duplicates and boolean literals depending on the given purpose (parentType)
     * 
     * @param expressions candidates (will be modified)
     * @param parentType AND or OR (influences strategy)
     * @return same list (pass-through)
     */
    private List<BbqExpression> condenseExpressions(List<BbqExpression> expressions, Class<? extends BbqExpression> parentType) {
        sortExpressionsByIdAndDedup(expressions);
        if (parentType == AndExpression.class || parentType == OrExpression.class) {
            if (parentType == AndExpression.class) {
                replaceInnerNeigbourReferencesWithBooleanLiterals(expressions);
            }
            else {
                multiplyOnInnerAndsOfOr(expressions);
            }
            condenseExpressionsDetailed(expressions, parentType);
            fixEmptyChildExpressionList(expressions, parentType);
        }
        return expressions;
    }

    /**
     * Performs a cleanup on the expression list of an and or
     * 
     * @param expressions deduped and sorted, will be modified
     * @param parentType AND or OR (influences strategy)
     */
    private void condenseExpressionsDetailed(List<BbqExpression> expressions, Class<? extends BbqExpression> parentType) {
        ExpressionAdvice advice = ExpressionAdvice.PROCEED;
        for (int i = expressions.size() - 1; i > -1 && advice == ExpressionAdvice.PROCEED; i--) {
            BbqExpression expression = expressions.get(i);
            advice = checkBooleanLiteral(expression, parentType);
            for (int k = i - 1; k > -1 && advice == ExpressionAdvice.PROCEED; k--) {
                advice = checkContradiction(expression, expressions.get(k), parentType);
            }
            if (advice == ExpressionAdvice.REMOVE) {
                expressions.remove(i);
                advice = ExpressionAdvice.PROCEED;
            }
            else if (advice == ExpressionAdvice.ALWAYS_FALSE) {
                expressions.clear();
                expressions.add(BbqBooleanLiteral.FALSE);
            }
            else if (advice == ExpressionAdvice.ALWAYS_TRUE) {
                expressions.clear();
                expressions.add(BbqBooleanLiteral.TRUE);
            }
        }
    }

    /**
     * Theoretically, it could happen that we removed ALL elements from an AND/OR child expression list.<br>
     * Here we add a TRUE (for AND, always true) or FALSE literal for an OR (always false) to correct the list.
     * 
     * @param expressions child expressions, to be modified
     * @param parentType decides whether it should be always true or always false
     */
    private void fixEmptyChildExpressionList(List<BbqExpression> expressions, Class<? extends BbqExpression> parentType) {
        if (expressions.isEmpty()) {
            if (parentType == AndExpression.class) {
                expressions.add(BbqBooleanLiteral.TRUE);
            }
            else {
                expressions.add(BbqBooleanLiteral.FALSE);
            }
        }
    }

    /**
     * This step detects situations like this <code> ( NOT(A) AND B AND C) OR A )</code> and rephrases this as ((B OR A) AND (C OR A)), which usually increases
     * the chance for further optimization.
     * 
     * @param expression candidate
     * @return candidate or optimized replacement
     */
    private BbqExpression multiplyOnInnerAndsOfOr(BbqExpression expression) {
        if (expression instanceof OrExpression) {
            List<BbqExpression> childExpressions = sortExpressionsByIdAndDedup(new ArrayList<>(expression.getChildExpressions()));
            if (multiplyOnInnerAndsOfOr(childExpressions)) {
                expression = combine(childExpressions, AndExpression.class);
            }
        }
        else if (expression instanceof AndExpression) {
            List<BbqExpression> childExpressions = new ArrayList<>();
            boolean changed = false;
            for (BbqExpression childExpression : expression.getChildExpressions()) {
                BbqExpression fixed = multiplyOnInnerAndsOfOr(childExpression);
                changed = changed || (fixed.getExpressionId() != childExpression.getExpressionId());
                childExpressions.add(fixed);
            }
            if (changed) {
                expression = combine(childExpressions, AndExpression.class);
            }
        }
        return expression;
    }

    /**
     * Processes the members of a detected OR
     * 
     * @param orMemberExpressions child expressions of an OR, will be modified
     * @return true if the list was modified
     */
    private boolean multiplyOnInnerAndsOfOr(List<BbqExpression> orMemberExpressions) {
        boolean res = false;
        for (int i = 0; i < orMemberExpressions.size(); i++) {
            BbqExpression childExpression = orMemberExpressions.get(i);
            for (int k = 0; k < orMemberExpressions.size(); k++) {
                if (k != i) {
                    BbqExpression candidate = orMemberExpressions.get(k);
                    BbqExpression fixed = multiplyIfRequired(childExpression, candidate);
                    if (fixed.getExpressionId() != childExpression.getExpressionId()) {
                        orMemberExpressions.set(i, fixed);
                        res = true;
                    }
                }
            }
        }
        return res;

    }

    /**
     * Processes a single candidate (AND) inside an OR, if it needs to be rephrased
     * 
     * @param expression candidate (AND)
     * @param orConditionExpression outer condition
     * @return candidate or optimized expression
     */
    private BbqExpression multiplyIfRequired(BbqExpression expression, BbqExpression orConditionExpression) {
        if ((expression instanceof AndExpression) && expression.getChildExpressions().stream().anyMatch(e -> isContradiction(e, orConditionExpression))) {
            List<BbqExpression> childExpressions = new ArrayList<>();
            for (BbqExpression childExpression : expression.getChildExpressions()) {
                if (!isContradiction(childExpression, orConditionExpression)) {
                    List<BbqExpression> pair = new ArrayList<>(2);
                    pair.add(childExpression);
                    pair.add(orConditionExpression);
                    childExpressions.add(combine(pair, OrExpression.class));
                }
            }
            expression = combine(childExpressions, AndExpression.class);
        }
        expression = multiplyOnInnerAndsOfOr(expression);
        return expression;
    }

    /**
     * We assume that the given expressions are members of an enclosing AND expression.
     * <p>
     * The method picks each element and recursively tests any OTHER member if it is or contains the same expression or its negation.<br>
     * If it is the same, we can replace it with TRUE, because due to the enclosing AND it is guaranteed to be fulfilled.<br>
     * If the another direct or nested member of the same AND is the negation of the given expression, we can replace that occurrence with FALSE, because due to
     * the enclosing condition it cannot be fulfilled.
     * 
     * @param andMemberExpressions list of child expressions without duplicates, may be modified
     * @param true if the list was modified
     */
    private boolean replaceInnerNeigbourReferencesWithBooleanLiterals(List<BbqExpression> andMemberExpressions) {
        boolean res = false;
        for (int i = andMemberExpressions.size() - 1; i > -1; i--) {
            BbqExpression childExpression = andMemberExpressions.get(i);
            List<BbqExpression> trueFilterExpressions = new ArrayList<>(andMemberExpressions);
            trueFilterExpressions.remove(i);
            BbqExpression fixed = replaceInnerReferencesWithBooleanLiterals(childExpression, trueFilterExpressions);
            if (fixed.getExpressionId() != childExpression.getExpressionId()) {
                andMemberExpressions.set(i, fixed);
                res = true;
            }
        }
        return res;
    }

    /**
     * We assume that the enclosing expression is an AND and the given expression is a member of that list.
     * <p>
     * The method recursively tests any OTHER member if it is or contains the same expression or its negation.<br>
     * If it is the same, we can replace it with TRUE, because due to the enclosing AND it is guaranteed to be fulfilled.<br>
     * If another direct or nested member of the same AND is the negation of the given expression, we can replace that occurrence with FALSE, because due to the
     * enclosing condition it cannot be fulfilled.
     * 
     * @param expression AND expression (others will be ignored)
     * @return expression or a new expression with any subsequent appearance of other members of the same enclosing AND replaced with a boolean literal
     */
    private BbqExpression replaceInnerReferencesWithBooleanLiterals(BbqExpression expression) {
        if (expression instanceof AndExpression) {
            List<BbqExpression> childExpressions = sortExpressionsByIdAndDedup(new ArrayList<>(expression.getChildExpressions()));
            if (replaceInnerNeigbourReferencesWithBooleanLiterals(childExpressions)) {
                expression = combine(childExpressions, AndExpression.class);
            }
        }
        else if (expression instanceof OrExpression) {
            List<BbqExpression> childExpressions = new ArrayList<>();
            boolean changed = false;
            for (BbqExpression childExpression : expression.getChildExpressions()) {
                BbqExpression fixed = replaceInnerReferencesWithBooleanLiterals(childExpression);
                changed = changed || (fixed.getExpressionId() != childExpression.getExpressionId());
                childExpressions.add(fixed);
            }
            if (changed) {
                expression = combine(childExpressions, OrExpression.class);
            }
        }
        return expression;
    }

    /**
     * Processes a selected member of an AND against the other members.
     * 
     * @param expression member of the enclosing AND
     * @param trueFilterExpressions expressions to be assumed TRUE, must not include the given expression
     * @return expression or a new expression with any subsequence appearance of expressions in the list replaced with a boolean literal
     */
    private BbqExpression replaceInnerReferencesWithBooleanLiterals(BbqExpression expression, List<BbqExpression> trueFilterExpressions) {
        for (BbqExpression trueFilterExpression : trueFilterExpressions) {

            if (trueFilterExpression.getExpressionId() == expression.getExpressionId()) {
                expression = BbqBooleanLiteral.TRUE;
            }
            else if (isContradiction(trueFilterExpression, expression)) {
                expression = BbqBooleanLiteral.FALSE;
            }
            else if (isANDorOR(expression)) {
                expression = replaceAndOrInnerReferencesWithBooleanLiterals(expression, trueFilterExpressions);
            }
        }
        return expression;
    }

    /**
     * Processes an AND/OR recursively
     * 
     * @param expression member of the enclosing AND, which is itself an AND or OR
     * @param trueFilterExpressions expressions to be assumed TRUE, must not include the given expression
     * @return expression or a new expression with any subsequence appearance of expressions in the list replaced with a boolean literal
     */
    private BbqExpression replaceAndOrInnerReferencesWithBooleanLiterals(BbqExpression expression, List<BbqExpression> trueFilterExpressions) {
        List<BbqExpression> childExpressions = new ArrayList<>();
        boolean changed = false;
        for (BbqExpression childExpression : expression.getChildExpressions()) {
            BbqExpression fixed = replaceInnerReferencesWithBooleanLiterals(childExpression, trueFilterExpressions);
            changed = changed || (fixed.getExpressionId() != childExpression.getExpressionId());
            childExpressions.add(fixed);
        }
        if (changed) {
            expression = combine(childExpressions, expression.getClass());
        }
        expression = replaceInnerReferencesWithBooleanLiterals(expression);
        return expression;
    }

    /**
     * @param expression1 candidate
     * @param expression2 candidate
     * @return true if expression1 negates expression2
     */
    private boolean isContradiction(BbqExpression expression1, BbqExpression expression2) {
        return (expression1 instanceof NegationExpression && ((NegationExpression) expression1).isNegationOf(expression2))
                || (expression2 instanceof NegationExpression && ((NegationExpression) expression2).isNegationOf(expression1));
    }

    /**
     * Evaluates if the given two expressions contradict each other and gives an advice how to proceed in the parent context.
     * 
     * @param expression1 candidate
     * @param expression2 candidate
     * @param parentType AND or OR (influences the advice)
     * @return advice how to proceed
     */
    private ExpressionAdvice checkContradiction(BbqExpression expression1, BbqExpression expression2, Class<? extends BbqExpression> parentType) {
        ExpressionAdvice res = ExpressionAdvice.PROCEED;

        if (isContradiction(expression1, expression2)) {
            if (parentType == AndExpression.class) {
                res = ExpressionAdvice.ALWAYS_FALSE;
            }
            else {
                res = ExpressionAdvice.ALWAYS_TRUE;
            }
        }
        return res;
    }

    /**
     * Checks whether the given candidate is a boolean literal and returns an advice how to proceed in the given parent context.
     * 
     * @param candidate expression to check
     * @param parentType AND or OR
     * @return advice how to proceed
     */
    private ExpressionAdvice checkBooleanLiteral(BbqExpression candidate, Class<? extends BbqExpression> parentType) {
        ExpressionAdvice res = ExpressionAdvice.PROCEED;
        if (candidate == BbqBooleanLiteral.TRUE) {
            if (parentType == AndExpression.class) {
                res = ExpressionAdvice.REMOVE;
            }
            else if (parentType == OrExpression.class) {
                res = ExpressionAdvice.ALWAYS_TRUE;
            }
        }
        else if (candidate == BbqBooleanLiteral.FALSE) {
            if (parentType == OrExpression.class) {
                res = ExpressionAdvice.REMOVE;
            }
            else if (parentType == AndExpression.class) {
                res = ExpressionAdvice.ALWAYS_FALSE;
            }
        }
        return res;
    }

    /**
     * Processing advice when dealing with anomalies (always true/false) in AND or OR
     */
    private enum ExpressionAdvice {
        PROCEED, REMOVE, ALWAYS_TRUE, ALWAYS_FALSE;
    }

}
