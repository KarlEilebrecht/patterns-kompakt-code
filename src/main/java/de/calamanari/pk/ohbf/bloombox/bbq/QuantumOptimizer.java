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
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.calamanari.pk.ohbf.bloombox.DataPointProbabilityManager;
import de.calamanari.pk.util.LambdaSupportLoggerProxy;

/**
 * The {@link QuantumOptimizer} was introduced to better support probability-queries (quantity aggregation) which unfortunately suffer from nesting and multiple
 * refences to the same data point, see also comments in {@link DataPointProbabilityManager}.
 * <p>
 * This implementation spends considerably more effort than the standard optimizer {@link IntermediateExpressionOptimizer} to reduce the complexity of
 * expression that will be used to aggregate the probabilities.
 * <p>
 * I decided not to integrate this in the standard optimizer, as it takes time and at the end latest for combining sub queries with base queries this on-demand
 * step is necessary anyway (per sub query). Instead this optimizer runs only on demand.
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

        final BloomFilterQuery dbgQuery = query;
        LOGGER.trace("Query to be optimized (complexity={}): \n{}", defer(() -> dbgQuery.getExpression().computeComplexity()),
                defer(() -> formatExpressionAsTree(dbgQuery.getExpression())));

        int iteration = 0;
        BloomFilterQuery source = null;
        do {
            source = query;
            BbqExpression expression = query.getExpression();
            BbqExpression optimizedExpression = this.optimize(expression);

            if (optimizedExpression.getExpressionId() != expression.getExpressionId()) {
                query = new BloomFilterQuery(query.getSourceQuery(), optimizedExpression);
            }
            iteration++;
        } while (source.getId() != query.getId() && iteration < 5);

        // Above I reduce the number of runs to 5, just in case I overlooked any flickering ... ;)

        final BloomFilterQuery dbgQueryAfter = query;
        LOGGER.trace("Optimized query (complexity={}): \n{}", defer(() -> dbgQueryAfter.getExpression().computeComplexity()),
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
            BbqExpression childExpression = expression.getChildExpressions().get(0);
            BbqExpression fixedChildExpression = dissolveMemberNegatedAndOrOfNegations(childExpression);
            if (fixedChildExpression.getExpressionId() != childExpression.getExpressionId()) {
                expression = negateExpression(fixedChildExpression);
            }
        }
        else if (isANDorOR(expression)) {
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
            BbqExpression fixed = negatePushDown(expression.getChildExpressions().get(0));
            if (fixed.getExpressionId() != expression.getExpressionId()) {
                expression = fixed;
            }
        }
        else if (isANDorOR(expression)) {
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
            List<BbqExpression> childExpressions = new ArrayList<>(expression.getChildExpressions());
            boolean changed = premergeChildExpressionOverlaps(childExpressions);
            changed = mergeIntraChildExpressionOverlaps(childExpressions, expression.getClass()) || changed;
            if (changed) {
                sortExpressionsByIdAndDedup(childExpressions);
                if (childExpressions.size() == 1) {
                    expression = childExpressions.get(0);
                }
                else {
                    expression = combine(childExpressions, expression.getClass());
                }
            }
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
            res = expression2;
        }
        else if (additional2.isEmpty()) {
            res = expression1;
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
        List<BbqExpression> additionals = new ArrayList<>();
        additionals.addAll(additional1);
        additionals.addAll(additional2);
        members.add(combine(overlap, OrExpression.class));
        members.add(combine(additionals, OrExpression.class));
        return combine(members, AndExpression.class);
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
                    res = andOrOrExpression;
                }
                else if (parentType == AndExpression.class && andOrOrExpression.getClass() == OrExpression.class) {
                    res = singleExpression;
                }
                else if (parentType == OrExpression.class && andOrOrExpression.getClass() == AndExpression.class) {
                    res = andOrOrExpression;
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
        ExpressionAdvice advice = ExpressionAdvice.PROCEED;
        if (parentType == AndExpression.class || parentType == OrExpression.class) {
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
        return expressions;
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

        if ((expression1 instanceof NegationExpression && ((NegationExpression) expression1).isNegationOf(expression2))
                || (expression2 instanceof NegationExpression && ((NegationExpression) expression2).isNegationOf(expression1))) {
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
     * Processing advice when dealing with anomalities (always true/false) in AND or OR
     */
    private enum ExpressionAdvice {
        PROCEED, REMOVE, ALWAYS_TRUE, ALWAYS_FALSE;
    }

}