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

import de.calamanari.pk.ohbf.bloombox.Dpav;
import de.calamanari.pk.ohbf.bloombox.DpavProbabilityFetcher;
import de.calamanari.pk.ohbf.bloombox.PbDpavDictionary;
import de.calamanari.pk.ohbf.bloombox.PbDpavDictionaryAware;
import de.calamanari.pk.util.SimpleFixedLengthBitVector;

/**
 * The {@link BinaryMatchExpression} is the only BBQ-expression that executes matches against a vector from the bloom box (all others are composites).
 * <p>
 * <b>Note:</b> Although expressions should be immutable I have made a compromise for performance reasons to allow changing the DPAV-id (see
 * {@link #getLpDpavId()}, {@link #prepareLpDpavs(PbDpavDictionary)}) at runtime. Avoiding this would have meant to replicate the whole expression tree (to
 * preserve immutability), which I found too costly.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class BinaryMatchExpression implements BbqExpression, PbDpavDictionaryAware {

    private static final long serialVersionUID = -2940750763497459402L;

    /**
     * Search vector to match against the vector from the box
     */
    private final long[] pattern;

    /**
     * unique DPAV (key/value combination)
     */
    private final Dpav dpav;

    /**
     * unique id of this expression, including the pattern
     */
    private final long expressionId;

    /**
     * The local low-precision DPAV-id identifies the key/Value combination, see {@link ExpressionIdUtil#createLpDpavId(String, String)}<br>
     * This is the only mutable id, because it might change in preparation to the execution, see {@link #prepareLpDpavs(PbDpavDictionary)}
     */
    private int lpDpavId = -1;

    /**
     * @param argName name of the "column"
     * @param argValue field value
     * @param pattern bloom filter vector to match against the box
     */
    public BinaryMatchExpression(String argName, String argValue, long[] pattern) {
        this.dpav = new Dpav(argName, argValue);
        this.pattern = Arrays.copyOf(pattern, pattern.length);
        this.expressionId = ExpressionIdUtil.createExpressionId("BinaryMatch" + this.getDpav().getDpavId(), this.pattern);
    }

    /**
     * @return name of the "column"
     */
    public String getArgName() {
        return dpav.getColumnId();
    }

    /**
     * @return column value to match
     */
    public String getArgValue() {
        return dpav.getColumnValue();
    }

    /**
     * @return the DPAV (key/value) this match expression identifies
     */
    public Dpav getDpav() {
        return dpav;
    }

    /**
     * This id is for internal use and may change
     * 
     * @return low precision DPAV-id for key/value pair, see {@link ExpressionIdUtil#createLpDpavId(String, Object)}
     */
    public int getLpDpavId() {
        if (lpDpavId < 0) {
            lpDpavId = ExpressionIdUtil.createLpDpavId(this.dpav.getDpavId());
        }
        return lpDpavId;
    }

    @Override
    public boolean match(long[] source, int startPos, Map<Long, Boolean> resultCache) {
        return resultCache.computeIfAbsent(this.expressionId,
                key -> SimpleFixedLengthBitVector.compareAND(source, startPos, this.pattern, 0, this.pattern.length));
    }

    @Override
    public double computeMatchProbability(long rootExpressionId, DpavProbabilityFetcher probabilities) {
        return probabilities.fetchDpavProbability(rootExpressionId, this.lpDpavId);
    }

    /**
     * This id uniquely identifies the key/value pair with the given binary pattern, so the same key/value equals expression will have different ids on
     * different stores but it will have the same {@link Dpav#getDpavId()}.
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
        return BinaryMatchExpression.class.getSimpleName() + "( " + this.expressionId + " -> {" + dpav.getColumnId() + "==" + dpav.getColumnValue() + "} DP"
                + dpav.getDpavId() + (lpDpavId < 0 ? " )" : "[" + lpDpavId + "] )");
    }

    @Override
    public void prepareLpDpavs(PbDpavDictionary dictionary) {
        this.lpDpavId = dictionary.lookup(this.getLpDpavId());
    }

    @Override
    public void appendAsTree(StringBuilder sb, int level, String prefix) {
        this.appendIndent(sb, level);
        sb.append(prefix);
        sb.append("MATCH ( ");
        sb.append(expressionId);
        sb.append(" -> {");
        sb.append(dpav.getColumnId());
        sb.append("==");
        sb.append(dpav.getColumnValue());
        sb.append("} ");
        sb.append("DP");
        sb.append(dpav.getDpavId());
        if (lpDpavId >= 0) {
            sb.append("[");
            sb.append(lpDpavId);
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
