//@formatter:off
/*
 * IntermediateMinusExpression
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

/**
 * An {@link IntermediateMinusExpression} describes an expression that matches all the items matching the first member expression but none of the other member
 * expressions.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class IntermediateMinusExpression extends IntermediateCombinedExpression {

    public IntermediateMinusExpression() {
        this.setType(IntermediateExpressionType.MINUS);
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
                List<BbqExpression> conditions = new ArrayList<>();
                conditions.add(expressions.get(0));
                for (int i = 1; i < expressions.size(); i++) {
                    conditions.add(new NegationExpression(expressions.get(i)));
                }
                res = new AndExpression(conditions);
            }
        }
        return res;
    }

    @Override
    protected IntermediateCombinedExpression createNewInstance() {
        return new IntermediateMinusExpression();
    }

    @Override
    public boolean yieldsAlwaysFalse() {
        return this.getSubExpressionList().isEmpty() || getSubExpressionList().get(0).yieldsAlwaysFalse() || getSubExpressionList().stream().skip(1)
                .anyMatch(e -> (e.yieldsAlwaysTrue() || e.toString().equals(getSubExpressionList().get(0).toString())));
    }

    @Override
    public boolean yieldsAlwaysTrue() {
        return !this.getSubExpressionList().isEmpty() && getSubExpressionList().get(0).yieldsAlwaysTrue()
                && getSubExpressionList().stream().skip(1).allMatch(IntermediateExpression::yieldsAlwaysFalse);
    }

}
