//@formatter:off
/*
 * QueryBundleResult
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
import java.util.List;
import java.util.Map;

/**
 * A {@link QueryBundleResult} contains the results (counts, errors, warnings) for a {@link QueryBundle}.
 * <p>
 * This way we can deal with partially successful runs (only a single query broken won't necessarily affect the others).
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class QueryBundleResult implements Serializable {

    private static final long serialVersionUID = 806894102326224055L;

    /**
     * results of the base queries
     */
    private List<BloomBoxQueryResult> baseQueryResults = new ArrayList<>();

    /**
     * results of the post queries
     */
    private List<BloomBoxQueryResult> postQueryResults = new ArrayList<>();

    /**
     * A master error indicates that the bundle failed
     */
    private String masterError = null;

    /**
     * @return base query results, same order as queries in {@link QueryBundle}
     */
    public List<BloomBoxQueryResult> getBaseQueryResults() {
        return baseQueryResults;
    }

    /**
     * @param baseQueryResults query results, same order as queries in {@link QueryBundle} !
     */
    public void setBaseQueryResults(List<BloomBoxQueryResult> baseQueryResults) {
        this.baseQueryResults = baseQueryResults;
    }

    /**
     * @return query results, same order as queries in {@link QueryBundle}
     */
    public List<BloomBoxQueryResult> getPostQueryResults() {
        return postQueryResults;
    }

    /**
     * @param postQueryResults query results, same order as queries in {@link QueryBundle} !
     */
    public void setPostQueryResults(List<BloomBoxQueryResult> postQueryResults) {
        this.postQueryResults = postQueryResults;
    }

    /**
     * @return true if any error occured, if so, the client should call {@link #getMasterError()} before further result processing
     */
    public boolean checkIfAnyError() {
        boolean errorInAnyBaseQuery = (baseQueryResults != null && baseQueryResults.stream().anyMatch(BloomBoxQueryResult::checkIfError));
        boolean errorInAnyPostQuery = (postQueryResults != null && postQueryResults.stream().anyMatch(BloomBoxQueryResult::checkIfError));
        return (masterError != null && !masterError.isEmpty()) || errorInAnyBaseQuery || errorInAnyPostQuery;
    }

    /**
     * @return bundle execution error, if not null/empty then the bundle is invalid and should not be processed any further by a client
     */
    public String getMasterError() {
        return masterError;
    }

    /**
     * @param masterError bundle execution error to indicate an invalid bundle
     */
    public void setMasterError(String masterError) {
        this.masterError = masterError;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " [baseQueryResults=" + baseQueryResults + ", postQueryResults=" + postQueryResults + ", masterError="
                + masterError + "]";
    }

    /**
     * @return tree-format for easier review
     */
    public String toDebugString() {
        StringBuilder sb = new StringBuilder();

        if (baseQueryResults != null && !baseQueryResults.isEmpty()) {

            sb.append("Base query results: \n");
            List<BloomBoxQueryResult> results = baseQueryResults;

            appendQueryResultsToDebugString(sb, results);
        }
        if (postQueryResults != null && !postQueryResults.isEmpty()) {

            sb.append("\nPost query results: \n");

            List<BloomBoxQueryResult> results = postQueryResults;
            appendQueryResultsToDebugString(sb, results);
        }
        if (masterError != null && !masterError.isBlank()) {
            sb.append("\nBundle master error: ");
            sb.append(masterError);
            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * @param sb destination
     * @param results the query results
     */
    private void appendQueryResultsToDebugString(StringBuilder sb, List<BloomBoxQueryResult> results) {
        for (BloomBoxQueryResult result : results) {
            sb.append("    '");
            sb.append(result.getName());
            sb.append("': ");
            sb.append(result.getBaseQueryCount());
            sb.append("\n");
            for (Map.Entry<String, Long> entry : result.buildSubQueryResultMap().entrySet()) {
                sb.append("        '");
                sb.append(entry.getKey());
                sb.append("': ");
                sb.append(entry.getValue());
                sb.append("\n");
            }
            if (result.getErrorMessage() != null && !result.getErrorMessage().isBlank()) {
                sb.append("\nErrors: ");
                sb.append(result.getErrorMessage());
                sb.append("\n");
            }
            else if (result.getWarningMessage() != null && !result.getWarningMessage().isBlank()) {
                sb.append("\nWarnings: ");
                sb.append(result.getWarningMessage());
                sb.append("\n");
            }
        }
    }

}
