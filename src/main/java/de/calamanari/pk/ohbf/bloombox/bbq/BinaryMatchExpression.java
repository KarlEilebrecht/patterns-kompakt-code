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
import java.util.List;
import java.util.Map;

import de.calamanari.pk.ohbf.bloombox.DataPoint;
import de.calamanari.pk.ohbf.bloombox.DppFetcher;
import de.calamanari.pk.ohbf.bloombox.PbDataPointDictionary;
import de.calamanari.pk.ohbf.bloombox.PbDataPointDictionaryAware;
import de.calamanari.pk.util.SimpleFixedLengthBitVector;

/**
 * The {@link BinaryMatchExpression} is the only BBQ-expression that executes matches against a vector from the bloom box (all others are composites).
 * <p>
 * <b>Note:</b> Although expressions should be immutable I have made a compromise for performance reasons to allow changing the data point id (see
 * {@link #getLpDataPointId()}, {@link #prepareLpDataPointIds(PbDataPointDictionary)}) at runtime. Avoiding this would have meant to replicate the whole
 * expression tree (to preserve immutability), which I found too costly.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class BinaryMatchExpression implements BbqExpression, PbDataPointDictionaryAware {

    private static final long serialVersionUID = -2940750763497459402L;

    /**
     * Search vector to match against the vector from the box
     */
    private final long[] pattern;

    /**
     * unique data point (key/value combination)
     */
    private final DataPoint dataPoint;

    /**
     * unique id of this expression
     */
    private final long expressionId;

    /**
     * The local low-precision data point id identifies the key/Value combination, see {@link ExpressionIdUtil#createLpDataPointId(String, String)}<br>
     * This is the only mutable id, because it might change in preparation to the execution, see {@link #prepareLpDataPointIds(PbDataPointDictionary)}
     */
    private int lpDataPointId = -1;

    /**
     * @param argName name of the "column"
     * @param argValue field value
     * @param pattern bloom filter vector to match against the box
     */
    public BinaryMatchExpression(String argName, String argValue, long[] pattern) {
        this.dataPoint = new DataPoint(argName, argValue);
        this.pattern = Arrays.copyOf(pattern, pattern.length);
        this.expressionId = ExpressionIdUtil.createExpressionId("BinaryMatch" + this.getDataPoint().getDataPointId(), this.pattern);
    }

    /**
     * @return name of the "column"
     */
    public String getArgName() {
        return dataPoint.getColumnId();
    }

    /**
     * @return column value to match
     */
    public String getArgValue() {
        return dataPoint.getColumnValue();
    }

    /**
     * @return the data point (key/value) this match expression identifies
     */
    public DataPoint getDataPoint() {
        return dataPoint;
    }

    /**
     * This id is for internal use and may change
     * 
     * @return low precision data point id for key/value pair, see {@link ExpressionIdUtil#createLpDataPointId(String, Object)}
     */
    public int getLpDataPointId() {
        if (lpDataPointId < 0) {
            lpDataPointId = ExpressionIdUtil.createLpDataPointId(this.dataPoint.getDataPointId());
        }
        return lpDataPointId;
    }

    @Override
    public boolean match(long[] source, int startPos, Map<Long, Boolean> resultCache) {
        return resultCache.computeIfAbsent(this.expressionId,
                key -> SimpleFixedLengthBitVector.compareAND(source, startPos, this.pattern, 0, this.pattern.length));
    }

    @Override
    public double computeMatchProbability(long rootExpressionId, DppFetcher probabilities) {
        return probabilities.fetchDataPointProbability(rootExpressionId, this.lpDataPointId);
    }

    /**
     * This id uniquely identifies the key/value pair with the given binary pattern, so the same key/value equals expression will have different ids on
     * different stores but it will have the same {@link #getDataPointId()}.
     */
    @Override
    public long getExpressionId() {
        return this.expressionId;
    }

    @Override
    public void collectLiterals(List<BbqExpression> result) {
        result.add(this);
    }

    @Override
    public String toString() {
        return BinaryMatchExpression.class.getSimpleName() + "( " + this.expressionId + " -> {" + dataPoint.getColumnId() + "==" + dataPoint.getColumnValue()
                + "} DP" + dataPoint.getDataPointId() + (lpDataPointId < 0 ? " )" : "[" + lpDataPointId + "] )");
    }

    @Override
    public void prepareLpDataPointIds(PbDataPointDictionary dictionary) {
        this.lpDataPointId = dictionary.lookup(this.getLpDataPointId());
    }

    @Override
    public void appendAsTree(StringBuilder sb, int level, String prefix) {
        this.appendIndent(sb, level);
        sb.append(prefix);
        sb.append("MATCH ( ");
        sb.append(expressionId);
        sb.append(" -> {");
        sb.append(dataPoint.getColumnId());
        sb.append("==");
        sb.append(dataPoint.getColumnValue());
        sb.append("} ");
        sb.append("DP");
        sb.append(dataPoint.getDataPointId());
        if (lpDataPointId >= 0) {
            sb.append("[");
            sb.append(lpDataPointId);
            sb.append("]");
        }
        sb.append(" )");
        sb.append("\n");
    }

    @Override
    public int computeComplexity() {
        return 1;
    }
}
