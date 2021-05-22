//@formatter:off
/*
 * BbqBooleanLiteral
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

import java.util.Map;

import de.calamanari.pk.ohbf.bloombox.ProbabilityVectorSupplier;

/**
 * Although the BBQ-language does not allow specifying anything like TRUE or FALSE, during the optimization process it can still happen that a condition gets
 * detected that must be always TRUE or always FALSE, e.g. <code>(color=blue or color!=blue)</code>.
 * <p>
 * In this case the optimizer will use a {@link BbqBooleanLiteral} expression to avoid unnecessary matching.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public enum BbqBooleanLiteral implements BbqExpression {
    /**
     * Always matches any input
     */
    TRUE(true),

    /**
     * Never matches any input
     */
    FALSE(false);

    /**
     * Unique ID
     */
    private final transient long expressionId;

    /**
     * the fixed match result
     */
    private final boolean booleanValue;

    private BbqBooleanLiteral(boolean value) {
        this.booleanValue = value;
        this.expressionId = ExpressionIdUtil.createExpressionId("" + value);
    }

    /**
     * Returns the corresponding expression
     * 
     * @param booleanValue fixed result
     * @return expression
     */
    public static BbqBooleanLiteral fromBoolean(boolean booleanValue) {
        return booleanValue ? TRUE : FALSE;
    }

    @Override
    public boolean match(long[] source, int startPos, Map<Long, Boolean> resultCache) {
        return booleanValue;
    }

    @Override
    public double computeProbability(ProbabilityVectorSupplier probabilities, Map<Long, Double> resultCache) {
        return booleanValue ? 1.0 : 0.0;
    }

    @Override
    public long getExpressionId() {
        return expressionId;
    }

    @Override
    public String toString() {
        return (booleanValue ? "<TRUE>" : "<FALSE>");
    }

    @Override
    public void appendAsTree(StringBuilder sb, int level, String prefix) {
        this.appendIndent(sb, level);
        sb.append(prefix);
        sb.append(this.toString());
        sb.append("\n");
    }

}
