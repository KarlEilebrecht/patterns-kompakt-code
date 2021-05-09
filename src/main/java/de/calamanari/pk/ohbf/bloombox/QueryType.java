//@formatter:off
/*
 * QueryType
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

package de.calamanari.pk.ohbf.bloombox;

/**
 * Types of queries. By introducing distinguishable types with different purpose it was much easier to resolve relationships and perform optimization.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public enum QueryType {

    /**
     * A simple query is a BBQ-expression only based on the attributes, no references.
     */
    BASIC_QUERY,

    /**
     * A post-query can union, intersect or subtract named queries from other names queries.
     */
    POST_QUERY;

}
