//@formatter:off
/*
 * OrExpression
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

import de.calamanari.pk.ohbf.bloombox.DpavProbabilityFetcher;

/**
 * An {@link OrExpression} is the equivalent of a logical OR-combination of other {@link BbqExpression}s.<br>
 * So, if you think in terms of prefix-semantics <code>OR(A, B, C, ...)</code>, it is just a list of expressions <br>
 * to be matched against the same input. If any yields true, the whole expression yields true.
 * 
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class OrExpression implements BbqExpression {

    private static final long serialVersionUID = -4883366653011222210L;

    /**
     * list of expressions to match
     */
    private final BbqExpression[] expressions;

    /**
     * unique id of this expression
     */
    private final long expressionId;

    public OrExpression(List<BbqExpression> expressions) {
        this.expressions = expressions.toArray(new BbqExpression[expressions.size()]);
        long[] subIds = expressions.stream().map(BbqExpression::getExpressionId).mapToLong(v -> v).toArray();
        this.expressionId = ExpressionIdUtil.createExpressionId("OR", subIds);
    }

    @Override
    @SuppressWarnings({ "java:S3824" })
    public boolean match(long[] source, int startPos, Map<Long, Boolean> resultCache) {
        Boolean res = resultCache.get(expressionId);
        if (res == null) {
            res = lazyMatch(source, startPos, resultCache);
            resultCache.put(expressionId, res);
        }
        return res;
    }

    @Override
    public double computeMatchProbability(long rootExpressionId, DpavProbabilityFetcher probabilities) {

        // A OR B = NOT (NOT(A) AND NOT(B)) := 1.0 - ( (1.0 - P(A)) * (1.0 - P(B)))

        double combinedComplementProbability = -1;
        for (BbqExpression expression : expressions) {
            double complementProbability = 1.0d - expression.computeMatchProbability(rootExpressionId, probabilities);
            combinedComplementProbability = combinedComplementProbability < 0 ? complementProbability : combinedComplementProbability * complementProbability;
        }
        return combinedComplementProbability < 0 ? 1.0 : 1.0d - combinedComplementProbability;
    }

    /**
     * Matches the expressions and returns on the first true
     * 
     * @param source content to match
     * @param startPos positition in the content to start
     * @param resultCache cache for checking if the result is already known
     * @return true if any member-expression matches, otherwise false
     */
    private boolean lazyMatch(long[] source, int startPos, Map<Long, Boolean> resultCache) {
        boolean res = false;
        for (BbqExpression expression : expressions) {
            res = expression.match(source, startPos, resultCache);
            if (res) {
                break;
            }
        }
        return res;
    }

    /**
     * @return true if this expression can only return true
     */
    public boolean yieldsAlwaysTrue() {
        boolean res = false;
        for (int i = 0; !res && i < expressions.length; i++) {
            BbqExpression expression = expressions[i];
            if (expression == BbqBooleanLiteral.TRUE) {
                res = true;
            }
            else {
                for (int j = i + 1; !res && j < expressions.length; j++) {
                    BbqExpression otherExpression = expressions[j];
                    if (expression instanceof NegationExpression negation) {
                        res = negation.isNegationOf(otherExpression);
                    }
                    else if (otherExpression instanceof NegationExpression negation) {
                        res = negation.isNegationOf(expression);
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
        return OrExpression.class.getSimpleName() + "(" + this.expressionId + " := " + Arrays.toString(this.expressions) + ")";
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
            Arrays.stream(expressions).skip(1).forEach(expression -> expression.appendAsTree(sb, level + 1, "OR "));
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
        return Arrays.stream(this.expressions).mapToInt(BbqExpression::computeComplexity).sum() * expressions.length;
    }

}
