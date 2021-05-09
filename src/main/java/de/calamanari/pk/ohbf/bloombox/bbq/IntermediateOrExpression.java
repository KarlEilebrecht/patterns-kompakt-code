//@formatter:off
/*
 * IntermediateOrExpression
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
 * An {@link IntermediateOrExpression} is the equivalent of a logical OR-combination of other {@link IntermediateExpression}s.<br>
 * So, if you think in terms of prefix-semantics <code>OR(A, B, C, ...)</code>, it is just a list of expressions <br>
 * to be matched against the same input. If any yields true, the whole expression yields true.
 * 
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class IntermediateOrExpression extends IntermediateCombinedExpression {

    public IntermediateOrExpression() {
        this.setType(IntermediateExpressionType.OR);
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
            OrExpression resOr = new OrExpression(expressions);
            if (resOr.yieldsAlwaysTrue()) {
                res = BbqBooleanLiteral.TRUE;
            }
            else {
                res = resOr;
            }
        }
        return res;
    }

    @Override
    protected IntermediateCombinedExpression createNewInstance() {
        return new IntermediateOrExpression();
    }

    @Override
    public boolean yieldsAlwaysFalse() {
        return this.getSubExpressionList().isEmpty() || getSubExpressionList().stream().allMatch(IntermediateExpression::yieldsAlwaysFalse);
    }

    @Override
    public boolean yieldsAlwaysTrue() {
        return !this.getSubExpressionList().isEmpty() && getSubExpressionList().stream().anyMatch(IntermediateExpression::yieldsAlwaysTrue)
                || getSubExpressionList().stream().filter(IntermediateNotEquals.class::isInstance).map(IntermediateNotEquals.class::cast)
                        .anyMatch(e -> e.negatesAnyOf(getSubExpressionList()));
    }

}