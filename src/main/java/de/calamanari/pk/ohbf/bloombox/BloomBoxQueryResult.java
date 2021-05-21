//@formatter:off
/*
 * BloomBoxQueryResult
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
import java.util.Map;
import java.util.TreeMap;

/**
 * The {@link BloomBoxQueryResult} is the counterpart (outcome) of a single {@link BloomBoxQuery} including the counts for the query and sub query along with
 * warnings and errors.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class BloomBoxQueryResult implements Serializable {

    private static final long serialVersionUID = 7776279886724144628L;

    /**
     * Query name
     */
    private String name = null;

    /**
     * number of records that matched the main query
     */
    private long baseQueryCount = 0;

    /**
     * counts (number of records) for each sub query in order
     */
    private long[] subQueryCounts = null;

    /**
     * labels for each sub query in order
     */
    private String[] subQueryLabels = null;

    /**
     * Error message or null/empty if no error occured
     */
    private String errorMessage = null;

    /**
     * Warning message or null/empty if no problem occured
     */
    private String warningMessage = null;

    public BloomBoxQueryResult() {
        // empty instance
    }

    /**
     * Creates an empty instance with the given meta data
     * 
     * @param name query name
     * @param subQueryLabels labels of the sub queries (also defining the number of sub queries)
     */
    public BloomBoxQueryResult(String name, String... subQueryLabels) {
        this.name = name;
        this.subQueryCounts = new long[subQueryLabels.length];
        this.subQueryLabels = subQueryLabels;
    }

    /**
     * @return query name
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
     * @return number of records that matched the main query
     */
    public long getBaseQueryCount() {
        return baseQueryCount;
    }

    /**
     * increments the number of matches of the main query
     */
    void incrementBaseQueryCount() {
        baseQueryCount++;
    }

    /**
     * @param baseQueryCount number of records that matched the main query
     */
    public void setBaseQueryCount(long baseQueryCount) {
        this.baseQueryCount = baseQueryCount;
    }

    /**
     * Increment a sub query count (number of matching records)
     * 
     * @param idx index of the sub query
     */
    void incrementSubQueryCount(int idx) {
        subQueryCounts[idx]++;
    }

    /**
     * @return array with the sub query counts (reference to the internal array)
     */
    public long[] getSubQueryCounts() {
        return subQueryCounts;
    }

    /**
     * @param subQueryCounts array with the sub query counts
     */
    public void setSubQueryCounts(long[] subQueryCounts) {
        this.subQueryCounts = subQueryCounts;
    }

    /**
     * @return array with the sub query labels (reference to the internal array)
     */
    public String[] getSubQueryLabels() {
        return subQueryLabels;
    }

    /**
     * @param subQueryLabels array with the sub query labels
     */
    public void setSubQueryLabels(String[] subQueryLabels) {
        this.subQueryLabels = subQueryLabels;
    }

    /**
     * @return mapping of sub query labels to sub query counts
     */
    public Map<String, Long> buildSubQueryResultMap() {
        int len = (subQueryCounts == null ? 0 : subQueryCounts.length);
        Map<String, Long> res = new TreeMap<>();
        for (int i = 0; i < len; i++) {
            res.put(subQueryLabels[i], subQueryCounts[i]);
        }
        return res;
    }

    /**
     * @return error message or null/empty, see also {@link BbxMessage}
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * @param errorMessage error message or null/empty
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    /**
     * @return true if there was an error (error message present)
     */
    public boolean checkIfError() {
        return this.errorMessage != null;
    }

    /**
     * @return warning messages or null/empty
     */
    public String getWarningMessage() {
        return warningMessage;
    }

    /**
     * @param warningMessage warning messages or null/empty
     */
    public void setWarningMessage(String warningMessage) {
        this.warningMessage = warningMessage;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getClass().getSimpleName());
        sb.append("(name=");
        sb.append(this.name);
        sb.append(", baseQueryCount=");
        sb.append(baseQueryCount);
        sb.append(", subQueryResults=");
        sb.append(buildSubQueryResultMap().toString());
        sb.append(", errorMessage=");
        sb.append(errorMessage);
        sb.append(", warningMessage=");
        sb.append(warningMessage);
        sb.append(")");
        return sb.toString();
    }

}
