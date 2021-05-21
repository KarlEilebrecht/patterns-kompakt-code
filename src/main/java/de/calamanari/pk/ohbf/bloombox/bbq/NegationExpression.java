//@formatter:off
/*
 * NegationExpression
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
import java.util.List;
import java.util.Map;

/**
 * A {@link NegationExpression} wraps another {@link BbqExpression} and inverts its result.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class NegationExpression implements BbqExpression {

    private static final long serialVersionUID = 4971017120905479776L;

    /**
     * The wrapped expression to be negated
     */
    private final BbqExpression delegateExpression;

    /**
     * Unique expression id
     */
    private final long expressionId;

    /**
     * @param delegateExpression expression to be negated
     */
    public NegationExpression(BbqExpression delegateExpression) {
        this.delegateExpression = delegateExpression;
        this.expressionId = ExpressionIdUtil.createExpressionId("Negation", this.delegateExpression.getExpressionId());

    }

    @Override
    @SuppressWarnings({ "java:S3824" })
    public boolean match(long[] source, int startPos, Map<Long, Boolean> resultCache) {
        Boolean res = resultCache.get(expressionId);
        if (res == null) {
            res = !delegateExpression.match(source, startPos, resultCache);
            resultCache.put(expressionId, res);
        }
        return res;
    }

    @Override
    public double computeProbability(float[] probabilities, Map<Long, Double> resultCache) {
        return 1.0d - delegateExpression.computeProbability(probabilities, resultCache);
    }

    @Override
    public long getExpressionId() {
        return this.expressionId;
    }

    @Override
    public String toString() {
        return NegationExpression.class.getSimpleName() + "(" + this.expressionId + " := ![" + this.delegateExpression.toString() + "])";
    }

    @Override
    public void appendAsTree(StringBuilder sb, int level, String prefix) {
        this.appendIndent(sb, level);
        sb.append(prefix);
        sb.append("NOT (");
        sb.append("\n");
        delegateExpression.appendAsTree(sb, level + 1, "");
        this.appendIndent(sb, level);
        sb.append(")");
        this.appendArrow(sb, level);
        sb.append(expressionId);
        sb.append(" ");
        sb.append(this.getClass().getSimpleName());
        sb.append("\n");

    }

    /**
     * Determines whether this expression is the negation of the given expression.
     * <p>
     * <b>Note:</b> This method can <i>unwind</i> nested negations but for a given expression <code>other=A</code><br>
     * it does not detect effective singleton ANDs and ORs caused by boolean literals like <code>NOT((A AND TRUE))</code> or <code>NOT((A OR FALSE))</code>.
     * 
     * @param other tested expression
     * @return true if this expression is the negation of the given one
     */
    public boolean isNegationOf(BbqExpression other) {
        int inversionCounter = 0;

        while (other instanceof NegationExpression) {
            inversionCounter++;
            other = ((NegationExpression) other).delegateExpression;
        }
        return this.delegateExpression.getExpressionId() == other.getExpressionId() && (inversionCounter % 2 == 0);
    }

    @Override
    public void collectUniqueDepthFirst(Map<Long, BbqExpression> result) {
        getChildExpressions().forEach(c -> c.collectUniqueDepthFirst(result));
        result.put(getExpressionId(), this);
    }

    @Override
    public List<BbqExpression> getChildExpressions() {
        return new ArrayList<>(Arrays.asList(delegateExpression));
    }

}
