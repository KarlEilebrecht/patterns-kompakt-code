//@formatter:off
/*
 * BinaryMatchExpression
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

import de.calamanari.pk.util.SimpleFixedLengthBitVector;

/**
 * The {@link BinaryMatchExpression} is the only BBQ-expression that executes matches against a vector from the bloom box (all others are composites).
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class BinaryMatchExpression implements BbqExpression {

    private static final long serialVersionUID = -2940750763497459402L;

    /**
     * Search vector to match against the vector from the box
     */
    private final long[] pattern;

    /**
     * unique id of this expression
     */
    private final long expressionId;

    /**
     * name of the "column"
     */
    private final String argName;

    /**
     * column value to match
     */
    private final String argValue;

    /**
     * @param argName name of the "column"
     * @param argValue field value
     * @param pattern bloom filter vector to match against the box
     */
    public BinaryMatchExpression(String argName, String argValue, long[] pattern) {
        this.argName = argName;
        this.argValue = argValue;
        this.pattern = Arrays.copyOf(pattern, pattern.length);
        this.expressionId = ExpressionIdUtil.createExpressionId("BinaryMatch", this.pattern);
    }

    /**
     * @return name of the "column"
     */
    public String getArgName() {
        return argName;
    }

    /**
     * @return column value to match
     */
    public String getArgValue() {
        return argValue;
    }

    @Override
    public boolean match(long[] source, int startPos, Map<Long, Boolean> resultCache) {
        return resultCache.computeIfAbsent(this.expressionId,
                key -> SimpleFixedLengthBitVector.compareAND(source, startPos, this.pattern, 0, this.pattern.length));
    }

    @Override
    public long getExpressionId() {
        return this.expressionId;
    }

    @Override
    public String toString() {
        return BinaryMatchExpression.class.getSimpleName() + "( " + this.expressionId + " -> {" + argName + "==" + argValue + "} )";
    }

    @Override
    public void appendAsTree(StringBuilder sb, int level, String prefix) {
        this.appendIndent(sb, level);
        sb.append(prefix);
        sb.append("MATCH ( ");
        sb.append(expressionId);
        sb.append(" -> {");
        sb.append(argName);
        sb.append("==");
        sb.append(argValue);
        sb.append("} )");
        sb.append("\n");
    }

}
