//@formatter:off
/*
 * IntermediateCombinedExpression
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
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import de.calamanari.pk.ohbf.LwGenericOHBF;
import de.calamanari.pk.ohbf.bloombox.Dpav;

/**
 * Base class for expressions composed of other expressions to consolidate most of the logic.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public abstract class IntermediateCombinedExpression implements IntermediateExpression {

    /**
     * Members of this expression
     */
    private final List<IntermediateExpression> subExpressionList = new ArrayList<>();

    /**
     * Type of this expression
     */
    private IntermediateExpressionType type = IntermediateExpressionType.BRACES;

    /**
     * @return member expressions
     */
    public List<IntermediateExpression> getSubExpressionList() {
        return subExpressionList;
    }

    @Override
    public void collectRequiredDpavs(Map<Long, Dpav> dpavs) {
        this.subExpressionList.forEach(e -> e.collectRequiredDpavs(dpavs));
    }

    /**
     * TEMPLATE METHOD that generates and returns a technical {@link BbqExpression} that reflects <br>
     * this intermediate expression's type, taking into account the given members
     * 
     * @param expressions members
     * @return new technical expression
     */
    protected abstract BbqExpression createNewCombinedBbqExpression(List<BbqExpression> expressions);

    @Override
    public BbqExpression createBbqEquivalent(LwGenericOHBF bloomFilter, Map<Long, BbqExpression> expressionCache) {
        List<BbqExpression> expressions = this.getSubExpressionList().stream()
                .map(intermediateExpression -> intermediateExpression.createBbqEquivalent(bloomFilter, expressionCache)).collect(Collectors.toList());
        BbqExpression combinedExpression = createNewCombinedBbqExpression(expressions);
        BbqExpression cachedExpression = expressionCache.putIfAbsent(combinedExpression.getExpressionId(), combinedExpression);
        return (cachedExpression != null) ? cachedExpression : combinedExpression;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        if (getSubExpressionList().isEmpty()) {
            sb.append("()");
        }
        else if (getSubExpressionList().size() == 1) {
            sb.append(getJoinOperator());
            sb.append(" ( ");
            sb.append(getSubExpressionList().get(0).toString());
            sb.append(" )");
        }
        else {
            String delim = getJoinOperator();
            if (!delim.isBlank()) {
                delim = " " + delim + " ";
            }
            sb.append("( ");
            sb.append(getSubExpressionList().stream().map(IntermediateExpression::toString).collect(Collectors.joining(delim)));
            sb.append(" )");
        }
        return sb.toString();
    }

    @Override
    public void appendAsTree(StringBuilder sb, int level, String prefix) {
        this.appendIndent(sb, level);
        sb.append(prefix);
        sb.append("( ");
        if (getSubExpressionList().isEmpty()) {
            sb.append(")");
        }
        else {
            sb.append("\n");

            getSubExpressionList().get(0).appendAsTree(sb, level + 1, "");
            getSubExpressionList().stream().skip(1).forEach(expression -> expression.appendAsTree(sb, level + 1, getJoinOperator() + " "));
            sb.append("\n");
            this.appendIndent(sb, level);
            sb.append(")");
        }
        appendTrueFalseWarning(sb, level);
        sb.append("\n");
    }

    @Override
    public IntermediateExpressionType getType() {
        return type;
    }

    /**
     * @param type expression type
     */
    public void setType(IntermediateExpressionType type) {
        this.type = type;
    }

    /**
     * @return the join operator (a string required for nice-looking debug output)
     */
    protected String getJoinOperator() {
        return type.name();
    }

    /**
     * TEMPLATE METHOD to create a new combined expression of the same type
     * 
     * @return new instance of the same type
     */
    protected abstract IntermediateCombinedExpression createNewInstance();

    @Override
    public <T extends IntermediateExpression> T deepCopy() {
        IntermediateCombinedExpression copyInstance = this.createNewInstance();

        for (IntermediateExpression subExpression : this.subExpressionList) {
            copyInstance.subExpressionList.add(subExpression.deepCopy());
        }

        @SuppressWarnings("unchecked")
        T res = (T) copyInstance;
        return res;
    }

    @Override
    public boolean containsAnyAlwaysTrueOrAlwaysFalseExpression() {
        return this.yieldsAlwaysFalse() || this.yieldsAlwaysTrue()
                || getSubExpressionList().stream().anyMatch(IntermediateExpression::containsAnyAlwaysTrueOrAlwaysFalseExpression);
    }

}
