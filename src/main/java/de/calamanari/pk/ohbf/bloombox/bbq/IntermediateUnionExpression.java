//@formatter:off
/*
 * IntermediateUnionExpression
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
 * Logical OR between the results of two or more referenced query results
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class IntermediateUnionExpression extends IntermediateCombinedExpression {

    public IntermediateUnionExpression() {
        this.setType(IntermediateExpressionType.UNION);
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
                OrExpression resOr = new OrExpression(expressions);
                if (resOr.yieldsAlwaysTrue()) {
                    res = BbqBooleanLiteral.TRUE;
                }
                else {
                    res = resOr;
                }
            }
        }
        return res;
    }

    @Override
    protected IntermediateCombinedExpression createNewInstance() {
        return new IntermediateUnionExpression();
    }

    @Override
    public boolean yieldsAlwaysFalse() {
        return this.getSubExpressionList().isEmpty() || getSubExpressionList().stream().allMatch(IntermediateExpression::yieldsAlwaysFalse);
    }

    @Override
    public boolean yieldsAlwaysTrue() {
        return !this.getSubExpressionList().isEmpty() && getSubExpressionList().stream().anyMatch(IntermediateExpression::yieldsAlwaysTrue);
    }

}
