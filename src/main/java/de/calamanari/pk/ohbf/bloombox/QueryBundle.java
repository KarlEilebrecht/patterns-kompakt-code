//@formatter:off
/*
 * QueryBundle
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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A {@link QueryBundle} is a set of (dependent) queries to be executed in one scan.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class QueryBundle implements Serializable {

    private static final long serialVersionUID = -3805661724395179033L;

    /**
     * The base queries do not depend on any other query.
     */
    private List<BloomBoxQuery> baseQueries = new ArrayList<>();

    /**
     * Post-queries intersect, union or subtract queries from other queries
     */
    private List<BloomBoxQuery> postQueries = new ArrayList<>();

    /**
     * Optional upscaling configuration
     */
    private UpScalingConfig upScalingConfig = null;

    /**
     * Creates a bundle from easy-script syntax (nice to edit and to discuss).
     * <p>
     * Specification:
     * <ul>
     * <li>Line-based format, 1 line is one query or sub-query</li>
     * <li>A comment line starts with a '#'</li>
     * <li>Any line that does not start with whitespace is a taken as a main query.</li>
     * <li>A main query that contains any '${' is lazily interpreted as post query.</li>
     * <li>Any line that starts with whitespace will be added as a sub query to the previous main query.</li>
     * <li>Each query line must start with a name (query identifier), followed by a colon, followed by a valid BBQ expression.</li>
     * </ul>
     * 
     * @param script user input
     * @return bundle
     * @throws QueryPreparationException on any problem parsing the script
     */
    public static QueryBundle fromEasyScript(String script) {
        QueryBundle bundle = new QueryBundle();
        String currentLineParsed = script;
        try {
            String[] lines = script.split("[\\r\\n]+");
            BloomBoxQuery currentQuery = null;
            List<String> contentLines = Arrays.stream(lines).filter(line -> line != null && !line.trim().startsWith("#")).collect(Collectors.toList());

            for (String line : contentLines) {
                currentLineParsed = line;
                boolean subQueryFlag = line.substring(0, 1).isBlank();
                boolean postQueryFlag = !subQueryFlag && line.contains("${");
                line = line.trim();
                int pos = line.indexOf(':');
                String queryName = "";
                String bbq = "";

                if (pos <= 0 || pos > line.length() - 1) {
                    throw new QueryPreparationException(
                            BbxMessage.ERR_QUERY_SYNTAX_EASY_SCRIPT.format("Missing colon, expected <name>:<bbq-expression>, found " + currentLineParsed));
                }

                queryName = line.substring(0, pos).trim();
                bbq = line.substring(pos + 1).trim();
                if (postQueryFlag) {
                    currentQuery = BloomBoxQuery.postQuery(queryName).query(bbq).build();
                    bundle.getPostQueries().add(currentQuery);
                }
                else if (subQueryFlag) {
                    if (currentQuery != null) {
                        currentQuery.getSubQueryMap().put(queryName, bbq);
                    }
                }
                else {
                    currentQuery = BloomBoxQuery.basicQuery(queryName).query(bbq).build();
                    bundle.getBaseQueries().add(currentQuery);
                }
            }
        }
        catch (QueryPreparationException ex) {
            throw ex;
        }
        catch (RuntimeException ex) {
            throw new QueryPreparationException("Error parsing easy-script, syntax error at: " + currentLineParsed, ex);
        }
        return bundle;

    }

    /**
     * @return optional upscaling configuration, null means no upscaling
     */
    public UpScalingConfig getUpScalingConfig() {
        return upScalingConfig;
    }

    /**
     * @param upScalingConfig upscaling configuration, null means not upscaling
     */
    public void setUpScalingConfig(UpScalingConfig upScalingConfig) {
        this.upScalingConfig = upScalingConfig;
    }

    /**
     * @return list of base queries to be executed
     */
    public List<BloomBoxQuery> getBaseQueries() {
        return baseQueries;
    }

    /**
     * @param baseQueries list of base queries to be executed
     */
    public void setBaseQueries(List<BloomBoxQuery> baseQueries) {
        this.baseQueries = baseQueries;
    }

    /**
     * @return list of post queries to be executed
     */
    public List<BloomBoxQuery> getPostQueries() {
        return postQueries;
    }

    /**
     * @param postQueries list of post queries to be executed
     */
    public void setPostQueries(List<BloomBoxQuery> postQueries) {
        this.postQueries = postQueries;
    }

    /**
     * Validates the settings of this bundle, called by the framework before execution to detect any obvious problem
     * 
     * @throws BasicValidationException on rule violations
     */
    public void validateShallow() {
        if (baseQueries == null || baseQueries.isEmpty()) {
            throw new BasicValidationException(BbxMessage.ERR_BUNDLE_NO_BASE_QUERY
                    .format(String.format("Invalid %s, baseQueries must not be null or empty, given baseQueries=%s, postQueries=%s",
                            this.getClass().getSimpleName(), baseQueries, postQueries)));
        }
        try {
            baseQueries.forEach(BloomBoxQuery::validateShallow);
            if (postQueries != null) {
                postQueries.forEach(BloomBoxQuery::validateShallow);
            }
            else {
                postQueries = new ArrayList<>();
            }
        }
        catch (BasicValidationException ex) {
            throw new BasicValidationException(BbxMessage.ERR_INVALID_BUNDLE
                    .format(String.format("Invalid %s, given baseQueries=%s, postQueries=%s", this.getClass().getSimpleName(), baseQueries, postQueries), ex));
        }
        if (baseQueries.stream().anyMatch(q -> q.getType() != QueryType.BASIC_QUERY)) {
            throw new BasicValidationException(BbxMessage.ERR_BUNDLE_UNEXPECTED_QUERY_TYPE
                    .format(String.format("Invalid %s, baseQueries must not contain any queries of types other than %s given baseQueries=%s, postQueries=%s",
                            this.getClass().getSimpleName(), QueryType.BASIC_QUERY, baseQueries, postQueries)));
        }
        if (postQueries.stream().anyMatch(q -> q.getType() != QueryType.POST_QUERY)) {
            throw new BasicValidationException(BbxMessage.ERR_BUNDLE_UNEXPECTED_QUERY_TYPE
                    .format(String.format("Invalid %s, postQueries must not contain any queries of types other than %s given baseQueries=%s, postQueries=%s",
                            this.getClass().getSimpleName(), QueryType.POST_QUERY, baseQueries, postQueries)));
        }
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " [baseQueries=" + this.baseQueries + ", postQueries=" + this.postQueries + "]";
    }
}
