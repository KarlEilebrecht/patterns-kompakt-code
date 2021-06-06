//@formatter:off
/*
 * IntermediateNotEquals
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

import java.util.List;
import java.util.Map;

import de.calamanari.pk.ohbf.LwGenericOHBF;
import de.calamanari.pk.ohbf.bloombox.DataPoint;

/**
 * An {@link IntermediateNotEquals} is the equivalent of the not-equals operator in BBQ language with field name and field value, e.g. <code>color!=blue</code>
 * after transforming infix-notation to prefix notation.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class IntermediateNotEquals implements IntermediateExpression {

    /**
     * name of the "column"
     */
    private final String argName;

    /**
     * value that must not match
     */
    private final String argValue;

    /**
     * @param argName name of the "column"
     * @param argValue value that must not match
     */
    public IntermediateNotEquals(String argName, String argValue) {
        this.argName = argName;
        this.argValue = argValue;
    }

    @Override
    public BbqExpression createBbqEquivalent(LwGenericOHBF bloomFilter, Map<Long, BbqExpression> expressionCache) {
        bloomFilter.clear();
        bloomFilter.put(argName, argValue);
        BinaryMatchExpression expression = new BinaryMatchExpression(argName, argValue, bloomFilter.getBitVectorAsLongArray());
        BbqExpression cachedExpression = expressionCache.putIfAbsent(expression.getExpressionId(), expression);
        BbqExpression positiveExpression = (cachedExpression != null) ? cachedExpression : expression;
        NegationExpression negationExpression = new NegationExpression(positiveExpression);
        cachedExpression = expressionCache.putIfAbsent(negationExpression.getExpressionId(), negationExpression);
        return (cachedExpression != null) ? cachedExpression : negationExpression;

    }

    @Override
    public String toString() {
        return "( " + argName + " != " + argValue + " )";
    }

    @Override
    public IntermediateExpressionType getType() {
        return IntermediateExpressionType.NOT_EQUALS;
    }

    @Override
    public <T extends IntermediateExpression> T deepCopy() {
        @SuppressWarnings("unchecked")
        T res = (T) new IntermediateNotEquals(argName, argValue);
        return res;
    }

    /**
     * @param otherExpressions expressions, usually sub expressions in AND/OR
     * @return true if this expression is the negation of any of the expressions in the given list, otherwise false
     */
    public boolean negatesAnyOf(List<IntermediateExpression> otherExpressions) {
        return otherExpressions.stream().filter(IntermediateEquals.class::isInstance).map(IntermediateEquals.class::cast)
                .anyMatch(eq -> eq.argName.equals(this.argName) && eq.argValue.equals(this.argValue));
    }

    @Override
    public void collectRequiredDataPoints(Map<Long, DataPoint> dataPoints) {
        DataPoint dataPoint = new DataPoint(argName, argValue);
        dataPoints.putIfAbsent(dataPoint.getDataPointId(), dataPoint);
    }

}
