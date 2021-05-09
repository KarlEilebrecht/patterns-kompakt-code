//@formatter:off
/*
 * BbqExpression
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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * A {@link BbqExpression} is any technical representation of an expression of the BBQ-language.
 * <p>
 * The process transforms the textual language (infix-notation) step by step into a tree of {@link BbqExpression}s (prefix-notation) that can be matched against
 * the content.
 * <p>
 * By definition a {@link BbqExpression} is stateless.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public interface BbqExpression extends Serializable {

    /**
     * @param source vector to match the expression against
     * @param startPos position in the source to start
     * @param resultCache to avoid repetitive matching to the same content
     * @return true if the content matched, otherwise false
     */
    public boolean match(long[] source, int startPos, Map<Long, Boolean> resultCache);

    /**
     * @return unique id of this expression, see {@link ExpressionIdUtil}
     */
    public long getExpressionId();

    /**
     * Creates a textual tree-like representation (for debugging) and adds it to the given builder
     * 
     * @param sb string builder
     * @param level indentation level
     * @param prefix something to prepend to the output right after first indentation)
     */
    public void appendAsTree(StringBuilder sb, int level, String prefix);

    /**
     * appends some indentation spaces
     * 
     * @param sb string builder
     * @param level number of times to repeat indentation
     */
    default void appendIndent(StringBuilder sb, int level) {
        for (int i = 0; i < level; i++) {
            sb.append("    ");
        }
    }

    /**
     * Debugging cosmetics, appends a textual arrow (from right to left), where the length depends on the indentation level
     * 
     * @param sb string builder
     * @param level indentation level, determines arrow length
     */
    default void appendArrow(StringBuilder sb, int level) {
        int indented = level * 4;
        int arrowLength = Math.max(80 - indented, 4);
        String format = "%1$" + arrowLength + "s";
        sb.append(" <");
        sb.append(String.format(format, "").replace(' ', '-'));
        sb.append(" ");
    }

    /**
     * Creates a de-duplicated full list of all expressions and sub-expressions (recursively, including <b>this</b>) involved in <b>this</b> expression. The
     * order respects dependencies so that no earlier expression will reference a later one.
     * 
     * @return expression list
     */
    default List<BbqExpression> collectUniqueDepthFirst() {
        Map<Long, BbqExpression> sortDedupMap = new LinkedHashMap<>();
        collectUniqueDepthFirst(sortDedupMap);
        return new ArrayList<>(sortDedupMap.values());
    }

    /**
     * Maps all expressions and sub-expressions (recursively, including <b>this</b>) involved in <b>this</b> expression to expression-ids (key). The returned
     * map is ordered and respects dependencies so that no earlier expression will reference a later one.
     * 
     * @param result maps expression-ids to expressions
     */
    default void collectUniqueDepthFirst(Map<Long, BbqExpression> result) {
        getChildExpressions().forEach(c -> c.collectUniqueDepthFirst(result));
        result.put(getExpressionId(), this);
    }

    /**
     * @return list of all <i>direct</i> child expressions of <b>this</b> expression, by default an empty array, never null
     */
    default List<BbqExpression> getChildExpressions() {
        return Collections.emptyList();
    }
}
