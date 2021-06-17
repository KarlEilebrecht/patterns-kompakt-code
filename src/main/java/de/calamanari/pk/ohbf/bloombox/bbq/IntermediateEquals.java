//@formatter:off
/*
 * IntermediateEquals
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

import de.calamanari.pk.ohbf.LwGenericOHBF;
import de.calamanari.pk.ohbf.bloombox.Dpav;

/**
 * An {@link IntermediateEquals} is the equivalent of the equals operator in BBQ language with field name and field value, e.g. <code>color=blue</code> after
 * transforming infix-notation to prefix notation.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class IntermediateEquals implements IntermediateExpression {

    /**
     * name of the "column"
     */
    final String argName;

    /**
     * value to match
     */
    final String argValue;

    /**
     * @param argName name of the "column"
     * @param argValue value to match
     */
    public IntermediateEquals(String argName, String argValue) {
        this.argName = argName;
        this.argValue = argValue;
    }

    @Override
    public BbqExpression createBbqEquivalent(LwGenericOHBF bloomFilter, Map<Long, BbqExpression> expressionCache) {
        bloomFilter.clear();
        bloomFilter.put(argName, argValue);
        BinaryMatchExpression expression = new BinaryMatchExpression(argName, argValue, bloomFilter.getBitVectorAsLongArray());
        BbqExpression cachedExpression = expressionCache.putIfAbsent(expression.getExpressionId(), expression);
        return (cachedExpression != null) ? cachedExpression : expression;
    }

    @Override
    public String toString() {
        return "( " + argName + " = " + argValue + " )";
    }

    @Override
    public IntermediateExpressionType getType() {
        return IntermediateExpressionType.EQUALS;
    }

    @Override
    public <T extends IntermediateExpression> T deepCopy() {
        @SuppressWarnings("unchecked")
        T res = (T) new IntermediateEquals(argName, argValue);
        return res;
    }

    @Override
    public void collectRequiredDpavs(Map<Long, Dpav> dpavs) {
        Dpav dpav = new Dpav(argName, argValue);
        dpavs.putIfAbsent(dpav.getDpavId(), dpav);
    }

}
