//@formatter:off
/*
 * IntermediateReferenceExpression
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

import java.util.Arrays;
import java.util.Map;
import java.util.Set;

import de.calamanari.pk.ohbf.LwGenericOHBF;

/**
 * An {@link IntermediateReferenceExpression} is an alias to an existing base or a previous post expression.
 * <p>
 * In case of a sub-expression reference it additionally includes the parent expression (AND)
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class IntermediateReferenceExpression implements IntermediateExpression {

    /**
     * identifier of an existing expression
     */
    private final String name;

    /**
     * unique id of the referenced {@link BbqExpression}
     */
    private final long expressionId;

    /**
     * In case of sub-expressions this is the parent expression id, 0 if not present
     */
    private final long parentExpressionId;

    /**
     * @param name identifier of an existing expression
     * @param expressionId existing {@link BbqExpression}'s id
     * @param parentExpressionId in case of a sub-expression reference, &lt;=0 means not present (main query reference)
     */
    public IntermediateReferenceExpression(String name, long expressionId, long parentExpressionId) {
        this.name = name;
        this.expressionId = expressionId;
        this.parentExpressionId = parentExpressionId;
    }

    @Override
    public IntermediateExpressionType getType() {
        return IntermediateExpressionType.REFERENCE;
    }

    @Override
    public BbqExpression createBbqEquivalent(LwGenericOHBF bloomFilter, Map<Long, BbqExpression> expressionCache) {
        BbqExpression res = expressionCache.get(this.expressionId);
        if (res == null) {
            res = BbqBooleanLiteral.FALSE;
        }
        else if (parentExpressionId > 0) {
            BbqExpression parentExpression = expressionCache.get(this.parentExpressionId);
            if (parentExpression == null) {
                res = BbqBooleanLiteral.FALSE;
            }
            else {
                res = new AndExpression(Arrays.asList(parentExpression, res));
                BbqExpression cachedExpression = expressionCache.putIfAbsent(res.getExpressionId(), res);
                res = (cachedExpression != null) ? cachedExpression : res;
            }
        }
        return res;
    }

    @Override
    public <T extends IntermediateExpression> T deepCopy() {
        @SuppressWarnings("unchecked")
        T res = (T) new IntermediateReferenceExpression(name, expressionId, parentExpressionId);
        return res;
    }

    @Override
    public String toString() {
        if (this.parentExpressionId > 0) {
            return String.format("ref('%s':%d.%d)", name, parentExpressionId, expressionId);
        }
        else {
            return String.format("ref('%s':%d)", name, expressionId);
        }
    }

    @Override
    public void collectRequiredBaseAttributes(Set<String> attributeNames) {
        // references are opaque and references must be collected otherwise
    }
}
