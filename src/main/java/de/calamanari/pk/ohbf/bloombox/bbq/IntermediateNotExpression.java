//@formatter:off
/*
 * IntermediateNotExpression
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
 * An {@link IntermediateNotExpression} negates the included expression.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class IntermediateNotExpression extends IntermediateCombinedExpression {

    public IntermediateNotExpression() {
        this.setType(IntermediateExpressionType.NOT);
    }

    /**
     * @param delegate to be negated
     */
    public IntermediateNotExpression(IntermediateExpression delegate) {
        this();
        this.getSubExpressionList().add(delegate);
    }

    @Override
    protected BbqExpression createNewCombinedBbqExpression(List<BbqExpression> expressions) {
        BbqExpression res = BbqBooleanLiteral.TRUE;
        if (expressions.size() > 1) {
            throw new IllegalStateException("At this point the list of expressions should not contain more than 1 element, given: " + expressions);
        }
        else if (!expressions.isEmpty()) {
            BbqExpression childExpression = expressions.get(0);
            if (childExpression instanceof NegationExpression) {
                res = childExpression.getChildExpressions().get(0);
            }
            else {
                res = new NegationExpression(childExpression);
            }
        }
        return res;
    }

    @Override
    public void appendAsTree(StringBuilder sb, int level, String prefix) {
        this.appendIndent(sb, level);
        sb.append(prefix);
        sb.append("NOT ( ");
        if (getSubExpressionList().isEmpty()) {
            sb.append(")");
        }
        else {
            sb.append("\n");

            getSubExpressionList().get(0).appendAsTree(sb, level + 1, "");
            getSubExpressionList().stream().skip(1).forEach(expression -> expression.appendAsTree(sb, level + 1, ", "));
            sb.append("\n");
            this.appendIndent(sb, level);
            sb.append(")");
        }
        appendTrueFalseWarning(sb, level);
        sb.append("\n");
    }

    @Override
    protected IntermediateCombinedExpression createNewInstance() {
        return new IntermediateNotExpression();
    }

}
