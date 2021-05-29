//@formatter:off
/*
 * AndExpression
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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import de.calamanari.pk.ohbf.bloombox.DppFetcher;

/**
 * An {@link AndExpression} is the equivalent of a logical AND-combination of other {@link BbqExpression}s.<br>
 * So, if you think in terms of prefix-semantics <code>AND(A, B, C, ...)</code>, it is just a list of expressions <br>
 * to be matched against the same input. If all match, the whole expression is true, if any yields false, the whole expression yields false.
 * 
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class AndExpression implements BbqExpression {

    private static final long serialVersionUID = -4883349253011222219L;

    /**
     * list of expressions to match
     */
    private final BbqExpression[] expressions;

    /**
     * unique ID of this expression
     */
    private final long expressionId;

    /**
     * @param expressions list of expressions to match
     */
    public AndExpression(List<BbqExpression> expressions) {
        this.expressions = expressions.toArray(new BbqExpression[expressions.size()]);
        long[] subIds = expressions.stream().map(BbqExpression::getExpressionId).mapToLong(v -> v).toArray();
        this.expressionId = ExpressionIdUtil.createExpressionId("AND", subIds);
    }

    @Override
    @SuppressWarnings({ "java:S3824" })
    public boolean match(long[] source, int startPos, Map<Long, Boolean> resultCache) {

        // note: the warning above is suppressed because "computeIfAbsent" fails when called recursively

        Boolean res = resultCache.get(expressionId);
        if (res == null) {
            res = lazyMatch(source, startPos, resultCache);
            resultCache.put(expressionId, res);
        }
        return res;
    }

    @Override
    public double computeMatchProbability(long rootExpressionId, DppFetcher probabilities) {

        // AND: multiply probabilities

        double combinedProbability = -1;
        for (BbqExpression expression : expressions) {
            double probability = expression.computeMatchProbability(rootExpressionId, probabilities);
            combinedProbability = combinedProbability < 0 ? probability : combinedProbability * probability;
        }
        return combinedProbability < 0 ? 0.0 : combinedProbability;
    }

    /**
     * Matches the expressions and returns on the first false
     * 
     * @param source content to match
     * @param startPos positition in the content to start
     * @param resultCache cache for checking if the result is already known
     * @return true if all member-expressions match, otherwise false
     */
    private boolean lazyMatch(long[] source, int startPos, Map<Long, Boolean> resultCache) {
        boolean res = false;
        for (BbqExpression expression : expressions) {
            res = expression.match(source, startPos, resultCache);
            if (!res) {
                break;
            }
        }
        return res;
    }

    /**
     * @return true if this expression can only return false
     */
    public boolean yieldsAlwaysFalse() {
        boolean res = false;
        for (int i = 0; !res && i < expressions.length; i++) {
            BbqExpression expression = expressions[i];
            if (expression == BbqBooleanLiteral.FALSE) {
                res = true;
            }
            else {
                for (int j = i + 1; !res && j < expressions.length; j++) {
                    BbqExpression otherExpression = expressions[j];
                    if (expression instanceof NegationExpression) {
                        res = ((NegationExpression) expression).isNegationOf(otherExpression);
                    }
                    else if (otherExpression instanceof NegationExpression) {
                        res = ((NegationExpression) otherExpression).isNegationOf(expression);
                    }
                }
            }
        }
        return res;
    }

    @Override
    public long getExpressionId() {
        return this.expressionId;
    }

    @Override
    public String toString() {
        return AndExpression.class.getSimpleName() + "(" + this.expressionId + " := " + Arrays.toString(this.expressions) + ")";
    }

    @Override
    public void appendAsTree(StringBuilder sb, int level, String prefix) {
        this.appendIndent(sb, level);
        sb.append(prefix);
        sb.append("(");
        if (expressions.length == 0) {
            sb.append(")");
        }
        else {
            sb.append("\n");

            expressions[0].appendAsTree(sb, level + 1, "");
            Arrays.stream(expressions).skip(1).forEach(expression -> expression.appendAsTree(sb, level + 1, "AND "));
            sb.append("\n");
            this.appendIndent(sb, level);
            sb.append(")");
        }
        this.appendArrow(sb, level);
        sb.append(expressionId);
        sb.append(" ");
        sb.append(this.getClass().getSimpleName());
        sb.append("\n");
    }

    @Override
    public List<BbqExpression> getChildExpressions() {
        return this.expressions.length > 0 ? new ArrayList<>(Arrays.asList(expressions)) : Collections.emptyList();
    }

    @Override
    public int computeComplexity() {
        return Arrays.stream(this.expressions).mapToInt(BbqExpression::computeComplexity).sum() + 2;
    }

}
