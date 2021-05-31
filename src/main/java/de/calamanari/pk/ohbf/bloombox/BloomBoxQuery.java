//@formatter:off
/*
 * BloomBoxQuery
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

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * A {@link BloomBoxQuery} is a single query on a bloom box written in BBQ language.
 * <p>
 * Query types are defined in {@link QueryType}. Each query can have subquery expressions. This for typical scenarios where you have a master query and you also
 * need the sub counts regarding a further criterion. Instead of running multiple queries you can get all the result within one bloom box scan.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class BloomBoxQuery implements Serializable {

    private static final long serialVersionUID = 35826978397945146L;

    private static final Logger LOGGER = LoggerFactory.getLogger(BloomBoxQuery.class);

    /**
     * Mandatory, each query must have a unique name (in a {@link QueryBundle})
     */
    private String name = null;

    /**
     * The query given as a BBQ string, e.g. <code>carVendor=Ford AND type=cabrio</code>
     */
    private String query = null;

    /**
     * query type
     */
    private QueryType type;

    /**
     * Optional option map or null
     */
    private Map<String, String> options;

    /**
     * sub query expressions in BBQ, which will be executed on top of the main query's outcome
     * <p>
     * The key of the map is the sub query label, e.g.
     * <ul>
     * <li><code>blue</code> : <code>color=blue</code></li>
     * <li><code>red</code> : <code>color=red</code></li>
     * <li><code>black</code> : <code>color=black</code></li>
     * <li><code>other</code> : <code>color not in (blue, red, black)</code></li>
     * </ul>
     */
    private Map<String, String> subQueryMap = new TreeMap<>();

    /**
     * Returns a builder for conveniently creating a basic query (see {@link QueryType#BASIC_QUERY})
     * 
     * @param name unique name of the query in a bundle, not null
     * @return builder
     */
    public static Builder basicQuery(String name) {
        return new Builder(QueryType.BASIC_QUERY, name);
    }

    /**
     * Returns a builder for conveniently creating a post query (see {@link QueryType#POST_QUERY})
     * 
     * @param name unique name of the query in a bundle, not null
     * @return builder
     */
    public static Builder postQuery(String name) {
        return new Builder(QueryType.POST_QUERY, name);
    }

    public BloomBoxQuery() {
        // default constructor, e.g. required by frameworks
    }

    /**
     * Convenience constructor, auto-assigns labels to sub queries
     * 
     * @param name unique name in bundle
     * @param type query type
     * @param queryString BBQ expressions
     * @param subQueryStrings BBQ expressions
     */
    public BloomBoxQuery(String name, QueryType type, String queryString, String... subQueryStrings) {
        this.setType(type);
        this.setName(name);
        this.setQuery(queryString);
        for (int i = 0; i < subQueryStrings.length; i++) {
            subQueryMap.put("sq_" + i, subQueryStrings[i]);
        }
    }

    /**
     * Main constructor
     * 
     * @param name unique name in bundle
     * @param type query type
     * @param queryString BBQ expressions
     * @param subQueryMap BBQ expressions with unique labels
     */
    public BloomBoxQuery(String name, QueryType type, String queryString, Map<String, String> subQueryMap) {
        this.setType(type);
        this.setName(name);
        this.setQuery(queryString);
        if (subQueryMap != null) {
            this.subQueryMap.putAll(subQueryMap);
        }
    }

    /**
     * @return query type
     */
    public QueryType getType() {
        return type;
    }

    /**
     * @param type query type
     */
    public void setType(QueryType type) {
        this.type = type;
    }

    /**
     * @return name of the query
     */
    public String getName() {
        return name;
    }

    /**
     * @param name query name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return BBQ expression
     */
    public String getQuery() {
        return query;
    }

    /**
     * @param query BBQ expression
     */
    public void setQuery(String query) {
        this.query = query;
    }

    /**
     * @return map of sub queries, keys are the labels
     */
    public Map<String, String> getSubQueryMap() {
        return subQueryMap;
    }

    /**
     * @param subQueryMap map of sub queries, keys are the labels
     */
    public void setSubQueryMap(Map<String, String> subQueryMap) {
        this.subQueryMap = subQueryMap;
    }

    /**
     * @return map with options or null if no options available
     */
    public Map<String, String> getOptions() {
        return options;
    }

    /**
     * @param options map with settings for this query
     */
    public void setOptions(Map<String, String> options) {
        this.options = options;
    }

    /**
     * To avoid dealing with nulls and strange inconsistencies deeply inside the engine this validation performs a series of checks and throws an exception in
     * case of violations.
     * 
     * @throws BasicValidationException if requirements are not fulfilled
     */
    public void validateShallow() {
        if (type == null || name == null || query == null || name.isBlank() || query.isBlank()) {
            throw new BasicValidationException(BbxMessage.ERR_MAIN_QUERY_META_DATA
                    .format(String.format("Invalid %s, type, name and query string must not be null or blank, given: type=%s, name='%s', query='%s'.",
                            this.getClass().getSimpleName(), this.type, this.name, this.query)));
        }
        else if (name.contains(".")) {
            throw new BasicValidationException(BbxMessage.ERR_QUERY_NAMING_CONVENTION
                    .format(String.format("Invalid %s, type, name must not contain '.' (reserved sub query separator), given: type=%s, name='%s', query='%s'.",
                            this.getClass().getSimpleName(), this.type, this.name, this.query)));
        }
        else if (subQueryMap != null) {
            for (Map.Entry<String, String> subQueryEntry : subQueryMap.entrySet()) {
                String sqName = subQueryEntry.getKey();
                String sq = subQueryEntry.getValue();
                if (sqName == null || sq == null || sqName.isBlank() || sq.isBlank()) {
                    throw new BasicValidationException(BbxMessage.ERR_SUB_QUERY_META_DATA.format(String.format(
                            "Invalid sub query in %s, name and query string both must be not null and not blank, given: type=%s, name='%s', query='%s', subQueryMap=%s.",
                            this.getClass().getSimpleName(), this.type, this.name, this.query, this.subQueryMap)));
                }
                else if (sqName.contains(".")) {
                    throw new BasicValidationException(BbxMessage.ERR_QUERY_NAMING_CONVENTION.format(String.format(
                            "Invalid sub query in %s, name must not contain '.' (reserved sub query separator), given: type=%s, name='%s', query='%s', subQueryMap=%s.",
                            this.getClass().getSimpleName(), this.type, this.name, this.query, this.subQueryMap)));
                }
            }
        }
        else {
            subQueryMap = new HashMap<>();
        }
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " [type=" + type + ", name=" + name + ", query=" + query + ", subQueryMap=" + subQueryMap
                + (options == null ? "" : ", options=" + options.toString()) + "]";
    }

    /**
     * BUILDER for convenient creation of queries
     *
     */
    public static class Builder {

        /**
         * query name
         */
        final String name;

        /**
         * query type
         */
        final QueryType type;

        /**
         * main BBQ expression
         */
        String queryString;

        /**
         * sub query labels mapped to BBQ expressions
         */
        final Map<String, String> subQueryStrings = new HashMap<>();

        /**
         * @param type query type
         * @param name query name
         */
        private Builder(QueryType type, String name) {
            this.name = name;
            this.type = type;
            String lowerCaseName = name == null ? "" : name.toLowerCase();
            if (lowerCaseName.contains("=") || lowerCaseName.contains("union") || lowerCaseName.contains("intersect")
                    || lowerCaseName.toLowerCase().contains("minus")) {
                LOGGER.warn("The query name='{}' contains '=', 'union', 'intersect' or 'minus', which often indicates a mistake. No name specified?", name);
            }

        }

        /**
         * @param queryString main BBQ expression
         * @return builder
         */
        public Builder query(String queryString) {
            this.queryString = queryString;
            return this;
        }

        /**
         * @param name sub query label
         * @param queryString BBQ expression
         * @return builder
         */
        public Builder subQuery(String name, String queryString) {
            this.subQueryStrings.put(name, queryString);
            return this;
        }

        /**
         * Adds a sub query with auto label
         * 
         * @param queryString BBQ expression
         * @return builder
         */
        public Builder subQuery(String queryString) {
            return this.subQuery("sq_" + subQueryStrings.size(), queryString);
        }

        /**
         * Adds the given sub queries with auto labels
         * 
         * @param subQueries BBQ expressions
         * @return builder
         */
        public Builder subQueries(String... subQueries) {
            for (String subQuery : subQueries) {
                this.subQuery(subQuery);
            }
            return this;
        }

        /**
         * @return bloom box query
         */
        public BloomBoxQuery build() {
            return new BloomBoxQuery(name, type, queryString, subQueryStrings);
        }

    }
}
