//@formatter:off
/*
 * IntermediateExpression
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

import java.util.Map;

import de.calamanari.pk.ohbf.LwGenericOHBF;

/**
 * On the way from a textual query in BBQ-language to an unmodifiable composition of technical {@link BbqExpression}s the {@link IntermediateExpression} is a
 * data container that already shows the structure and its elements but allows modification. This way the expression optimizer can find an ideal setup (e.g. by
 * removing duplicate expressions) before doing the final transformation into a {@link BbqExpression}.
 * <p>
 * {@link IntermediateExpression} cover basic queries and post queries (same structure).
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public interface IntermediateExpression {

    /**
     * @return type of the expression
     */
    public IntermediateExpressionType getType();

    /**
     * Returns the corresponding expression for this query
     * 
     * @param bloomFilter for building the expression
     * @param expressionCache expressions already built
     * @return expression
     */
    public BbqExpression createBbqEquivalent(LwGenericOHBF bloomFilter, Map<Long, BbqExpression> expressionCache);

    /**
     * For debugging, appends the textual representation formatted as tree to the given builder.
     * 
     * @param sb string builder
     * @param level query depth, for indentation.
     * @param prefix some string to be prepended right after the indentation
     */
    default void appendAsTree(StringBuilder sb, int level, String prefix) {
        this.appendIndent(sb, level);
        sb.append(prefix);
        sb.append(this.toString());
        if (this.yieldsAlwaysFalse()) {
            sb.append(" <- ALWAYS FALSE!");
        }
        else if (this.yieldsAlwaysTrue()) {
            sb.append(" <- ALWAYS TRUE!");
        }
        sb.append("\n");
    }

    /**
     * @return true if there is any expression contained that is always true or always false
     */
    default boolean containsAnyAlwaysTrueOrAlwaysFalseExpression() {
        return this.yieldsAlwaysFalse() || this.yieldsAlwaysTrue();
    }

    /**
     * utility to append the indentation
     * 
     * @param sb string builder
     * @param level number of times to append 4 spaces
     */
    default void appendIndent(StringBuilder sb, int level) {
        for (int i = 0; i < level; i++) {
            sb.append("    ");
        }
    }

    /**
     * For nicer debugging this method appends a textual arrow from right to left
     * 
     * @param sb string builder
     * @param level indentation (determines the length of the arrow)
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
     * Creates a copy of this expressions and any of its members, so that the result is independent from this one
     * 
     * @param <T> expression type
     * @return deep copy
     */
    public <T extends IntermediateExpression> T deepCopy();

    /**
     * @return true if this expression is aware that it can only return false
     */
    default boolean yieldsAlwaysFalse() {
        return false;
    }

    /**
     * @return false if this expression is aware that it can only return true
     */
    default boolean yieldsAlwaysTrue() {
        return false;
    }

    /**
     * For debugging, adds a warning that this expression will always return false or true
     * 
     * @param sb string builder
     * @param level for arrow formatting
     */
    default void appendTrueFalseWarning(StringBuilder sb, int level) {
        if (this.yieldsAlwaysFalse()) {
            this.appendArrow(sb, level);
            sb.append("ALWAYS FALSE!");
        }
        else if (this.yieldsAlwaysTrue()) {
            this.appendArrow(sb, level);
            sb.append("ALWAYS TRUE!");
        }
    }
}
