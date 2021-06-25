//@formatter:off
/*
 * IntermediateExpressionType
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

/**
 * All expression types that can appear in basic queries and post queries.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public enum IntermediateExpressionType {

    /**
     * match column value (basic operation)
     */
    EQUALS(true, false),

    /**
     * match column value and return the negation
     */
    NOT_EQUALS(true, false),

    /**
     * hold together expressions, braces must be eliminated (by the optimizer) before final expression building
     */
    BRACES(false, false),

    /**
     * enclosing NOT expression
     */
    NOT(false, false),

    /**
     * condition that yields true if the member expressions are all true, otherwise false
     */
    AND(false, false),

    /**
     * condition that yields true if any of the member expressions yields true, otherwise false
     */
    OR(false, false),

    /**
     * logical OR between two referenced query result
     */
    UNION(false, true),

    /**
     * logical AND between two referenced query result
     */
    INTERSECT(false, true),

    /**
     * condition yields true if the record is in the first but not in the subsequent referenced query results
     */
    MINUS(false, true),

    /**
     * surrounds a reference query to handle it as opaque (protect from further optimization)
     */
    REFERENCE(true, true);

    /**
     * indicates whether it is a simple query (condition that is not combined)
     */
    private final boolean simple;

    /**
     * indicates that this is a post query
     */
    private final boolean post;

    /**
     * @return true if it is a simple query (condition that is not combined), otherwise false
     */
    public boolean isSimple() {
        return simple;
    }

    /**
     * @return true if it is a post query, otherwise false
     */
    public boolean isPost() {
        return post;
    }

    /**
     * @param simple true if it is a simple query type (condition that is not combined), otherwise false
     * @param post true if it is a post query type, otherwise false
     */
    IntermediateExpressionType(boolean simple, boolean post) {
        this.simple = simple;
        this.post = post;
    }

}
