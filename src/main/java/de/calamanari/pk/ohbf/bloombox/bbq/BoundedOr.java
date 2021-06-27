//@formatter:off
/*
 * BoundedOr
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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import de.calamanari.pk.ohbf.bloombox.BbxMessage;
import de.calamanari.pk.ohbf.bloombox.BloomBoxException;

/**
 * BUILDER for creating a <i>constrained or-expression</i>, optionally limiting the minimum and maximum number of matching expressions in a given condition
 * list.
 * <p>
 * <b>Note:</b> This implementation does not introduce any new BBQ element, instead, the bounds will be implemented by combining boolean conditions
 * (AND/OR).<br>
 * As a consequence the resulting expressions can get VERY large and complex. However, the advantage of this approach is that the language remains simple, so
 * that mapping to other languages (like SQL) is possible without special requirements to these languages. The downside is high expression complexity. <br>
 * As an alternative, bounds could be be implemented by simply evaluating all conditions within the braces and counting the matches, which would require an
 * entirely new {@link BbqExpression} and it would break compatibility to many languages which do not offer any feature like that.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class BoundedOr {

    /**
     * minimum number of {@link #expressions} that must hold true
     */
    private int lowerBound = 0;

    /**
     * maximum number of {@link #expressions} allowed to hold true, others must be false
     */
    private int upperBound = 0;

    /**
     * list of available expressions
     */
    private List<IntermediateExpression> expressions = new ArrayList<>();

    /**
     * internal state, we only negate the list elements once and only on demand, see {@link #getExpressionsNegated()}
     */
    private List<IntermediateExpression> expressionsNegated = null;

    /**
     * @param expressions candidates
     * @return builder instance, see {@link #build()}
     */
    public static BoundedOr of(List<IntermediateExpression> expressions) {
        BoundedOr res = new BoundedOr();
        res.expressions = expressions;
        return res;
    }

    private BoundedOr() {
        // internal
    }

    /**
     * @param lowerBound minimum number of expressions that must hold true
     * @return this builder
     */
    public BoundedOr min(int lowerBound) {
        this.lowerBound = lowerBound;
        return this;
    }

    /**
     * @param upperBound maximum number of expressions allowed to hold true, others must be false
     * @return this builder
     */
    public BoundedOr max(int upperBound) {
        this.upperBound = upperBound;
        return this;
    }

    /**
     * Creates the result expression from this builder.
     * 
     * @return expression taking into account the available expressions as well as lower and upper bounds
     * @throws InvalidBoundsException if the any of the bounds or there combination is incorrect
     */
    public IntermediateExpression build() {
        if (upperBound > 0 && upperBound < lowerBound) {
            throw new InvalidBoundsException(
                    String.format("Expected: upperBound <= 0 (not specified) or upperBound >= lowerBound, found: %d < %d", upperBound, lowerBound));
        }
        if (lowerBound > expressions.size()) {
            throw new InvalidBoundsException(String.format("Expected: lowerBound <= number of expressions (%d), found: %d", expressions.size(), lowerBound));
        }
        if (lowerBound <= 0 && upperBound <= 0) {
            throw new InvalidBoundsException("Expected: any of the bounds > 0 (otherwise the expression would always yield true).");
        }
        if (upperBound >= expressions.size()) {
            upperBound = 0;
        }
        IntermediateExpression res = null;
        if (lowerBound == expressions.size()) {
            IntermediateAndExpression expression = new IntermediateAndExpression();
            expression.getSubExpressionList().addAll(expressions);
            res = expression;
        }
        else if (lowerBound == 1 && upperBound == 0) {
            IntermediateOrExpression expression = new IntermediateOrExpression();
            expression.getSubExpressionList().addAll(expressions);
            res = expression;
        }
        else if (expressions.size() == 1) {
            res = expressions.get(0);
        }
        else if (upperBound <= 0) {
            res = createCombinedOrOfAndGroups(expressions, lowerBound);
        }
        else {
            res = buildWithUpperBound();
        }
        return res;
    }

    /**
     * If any upper bound is specified we need to create a negative condition that implicitly limits the number of expressions which are true to the expected
     * range.
     * 
     * @return expression that limits the number of expressions which hold true at the same time
     */
    private IntermediateExpression buildWithUpperBound() {
        IntermediateExpression res = null;

        // compute the number of conditions that must be FALSE
        int lowerBoundNegated = expressions.size() - upperBound;

        if (lowerBound <= 0) {
            // ONLY apply upper bound by computing all possible combinations
            res = createCombinedOrOfAndGroups(getExpressionsNegated(), lowerBoundNegated);
        }
        else {
            IntermediateOrExpression fullExpression = createCombinedOrOfAndGroups(expressions, lowerBound);

            List<IntermediateAndExpression> andGroups = fullExpression.getSubExpressionList().stream().map(IntermediateAndExpression.class::cast)
                    .collect(Collectors.toList());

            for (IntermediateAndExpression andGroup : andGroups) {
                List<IntermediateExpression> othersNegated = getOthersNegated(andGroup.getSubExpressionList());
                if (upperBound == lowerBound) {
                    // all other conditions must be false
                    andGroup.getSubExpressionList().addAll(othersNegated);
                }
                else {
                    // some (lowerBoundNegated) of the other conditions must be false to fulfill upper bound
                    IntermediateOrExpression negativeCondition = createCombinedOrOfAndGroups(othersNegated, lowerBoundNegated);
                    andGroup.getSubExpressionList().add(negativeCondition);
                }
            }
            res = fullExpression;
        }

        return res;
    }

    /**
     * @return a list that contains all {@link #expressions} in their negated form in the same order
     */
    private List<IntermediateExpression> getExpressionsNegated() {
        if (expressionsNegated == null) {
            expressionsNegated = expressions.stream().map(IntermediateNotExpression::new).collect(Collectors.toList());
        }
        return expressionsNegated;
    }

    /**
     * Returns for every element that is NOT in the childExpressions list the negated expression for building the negative condition (upper bound handling)
     * 
     * @param childExpressions filter (positive condition)
     * @return other expressions negated
     */
    private List<IntermediateExpression> getOthersNegated(List<IntermediateExpression> childExpressions) {
        Set<IntermediateExpression> filter = new HashSet<>(childExpressions);
        List<IntermediateExpression> res = new ArrayList<>();
        for (int i = 0; i < expressions.size(); i++) {
            IntermediateExpression candidate = expressions.get(i);
            if (!filter.contains(candidate)) {
                res.add(getExpressionsNegated().get(i));
            }
        }
        return res;
    }

    /**
     * This method creates a (potentially) large expression of by combining all AND-groups of the desired size with an enclosing OR.
     * 
     * @param childExpressions available expressions
     * @param groupSize desired group size
     * @return expression that reflects that at least groupSize expressions must hold true
     */
    IntermediateOrExpression createCombinedOrOfAndGroups(List<IntermediateExpression> childExpressions, int groupSize) {
        List<IntermediateAndExpression> groupList = new ArrayList<>();
        createPermutations(childExpressions, 0, new IntermediateAndExpression(), groupList, groupSize);
        IntermediateOrExpression res = new IntermediateOrExpression();
        res.getSubExpressionList().addAll(groupList);
        return res;
    }

    /**
     * This method recursively creates all possible groups (AND) of the desired size based on the base expression with taking further elements from the srcList
     * and adds them to the dest list.
     * 
     * @param srcList all available elements
     * @param srcPos current start position in srcList (left to right) to take further elements
     * @param base current candidate (incomplete group), will not be modified but copied
     * @param groupList list for adding further groups
     * @param groupSize desired group size
     */
    private void createPermutations(List<IntermediateExpression> srcList, int srcPos, IntermediateAndExpression base, List<IntermediateAndExpression> groupList,
            int groupSize) {
        for (int i = srcPos; i < srcList.size(); i++) {
            IntermediateAndExpression candidate = new IntermediateAndExpression();
            candidate.getSubExpressionList().addAll(base.getSubExpressionList());
            candidate.getSubExpressionList().add(srcList.get(i));
            if (candidate.getSubExpressionList().size() < groupSize && checkEnoughElementsAvailable(srcList, srcPos, candidate, groupSize)) {
                createPermutations(srcList, i + 1, candidate, groupList, groupSize);
            }
            else if (candidate.getSubExpressionList().size() == groupSize) {
                groupList.add(candidate);
            }
        }
    }

    /**
     * @param srcList all elements for grouping
     * @param srcPos position in the source list where to start taking elements (left to right)
     * @param candidate the current unfinished group
     * @param groupSize desired size of the group to build
     * @return true if there are enough elements in the srcList to complete a group of the desired groupSize, otherwise false
     */
    private boolean checkEnoughElementsAvailable(List<IntermediateExpression> srcList, int srcPos, IntermediateAndExpression candidate, int groupSize) {
        int required = groupSize - candidate.getSubExpressionList().size();
        int available = srcList.size() - srcPos;
        return required <= available;
    }

    /**
     * Returns the bound value parsed from the given text
     * 
     * @param text to be parsed
     * @param debugInfo to be added to potential error message
     * @return positive value or 0
     * @throws InvalidBoundsException if the string cannot be parsed to a number
     */
    public static int parseBoundValue(String text, String debugInfo) {
        try {
            return Math.max(Integer.parseInt(text), 0);
        }
        catch (InvalidBoundsException ex) {
            throw new InvalidBoundsException(BbxMessage.ERR_QUERY_SYNTAX_BOUNDS
                    .format(String.format("Syntax error at '%s', bounds must be integer values, found: '%s'", debugInfo, text)));
        }
    }

    /**
     * Exception to indicate invalid bounds
     * 
     */
    public static class InvalidBoundsException extends BloomBoxException {

        private static final long serialVersionUID = 1643376646580261062L;

        /**
         * @param message problem
         */
        public InvalidBoundsException(String message) {
            super(message);
        }

    }
}
