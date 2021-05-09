//@formatter:off
/*
 * IntermediateIntersectExpression
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

/**
 * Logical AND between the results of two or more referenced query results
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class IntermediateIntersectExpression extends IntermediateCombinedExpression {

    public IntermediateIntersectExpression() {
        this.setType(IntermediateExpressionType.INTERSECT);
    }

    @Override
    protected BbqExpression createNewCombinedBbqExpression(List<BbqExpression> expressions) {
        BbqExpression res = null;
        if (this.yieldsAlwaysFalse()) {
            res = BbqBooleanLiteral.FALSE;
        }
        else if (this.yieldsAlwaysTrue()) {
            res = BbqBooleanLiteral.TRUE;
        }
        else {
            if (expressions.isEmpty()) {
                res = BbqBooleanLiteral.FALSE;
            }
            else if (expressions.size() < 2) {
                res = expressions.get(0);
            }
            else {
                AndExpression resAnd = new AndExpression(expressions);
                if (resAnd.yieldsAlwaysFalse()) {
                    res = BbqBooleanLiteral.FALSE;
                }
                else {
                    res = resAnd;
                }
            }
        }
        return res;
    }

    @Override
    protected IntermediateCombinedExpression createNewInstance() {
        return new IntermediateIntersectExpression();
    }

    @Override
    public boolean yieldsAlwaysFalse() {
        return this.getSubExpressionList().isEmpty() || getSubExpressionList().stream().anyMatch(IntermediateExpression::yieldsAlwaysFalse);
    }

    @Override
    public boolean yieldsAlwaysTrue() {
        return !this.getSubExpressionList().isEmpty() && getSubExpressionList().stream().allMatch(IntermediateExpression::yieldsAlwaysTrue);
    }

}
