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
import java.util.Arrays;
import java.util.Collections;
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

    /**
     * Optional, only available if the underlying bloom box used probability queries
     */
    private PbBloomBoxQueryResult probabilityResult = null;

    /**
     * Optional protocol data, see {@link #logProtocolMessage(String)}
     */
    private BbxProtocol protocol = null;

    /**
     * Unique execution-id, individual per query, set by the runner, 0 if unknown
     */
    private long executionId = 0;

    public BloomBoxQueryResult() {
        // empty instance
    }

    /**
     * Creates an empty instance with the given meta data
     * 
     * @param executionId context (usually the bundle execution)
     * @param name query name
     * @param subQueryLabels labels of the sub queries (also defining the number of sub queries)
     */
    public BloomBoxQueryResult(long executionId, String name, String... subQueryLabels) {
        this.name = name;
        this.subQueryCounts = new long[subQueryLabels.length];
        this.subQueryLabels = subQueryLabels;
        this.executionId = executionId;
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
     * @return mapping of sub query labels to sub query sums (if available)
     */
    public Map<String, Double> buildSubQuerySumMap() {
        Map<String, Double> res = null;
        if (probabilityResult == null) {
            res = Collections.emptyMap();
        }
        else {
            double[] subQuerySums = probabilityResult.getSubQuerySums();
            res = new TreeMap<>();
            if (subQuerySums != null) {
                int len = Math.min((subQueryCounts == null ? 0 : subQueryCounts.length), subQuerySums.length);
                for (int i = 0; i < len; i++) {
                    res.put(subQueryLabels[i], subQuerySums[i]);
                }
            }
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

    /**
     * @return probability data if the bloom box used probability queries, otherwise null
     */
    public PbBloomBoxQueryResult getProbabilityResult() {
        return probabilityResult;
    }

    /**
     * @param probabilityResult optional probability details
     */
    public void setProbabilityResult(PbBloomBoxQueryResult probabilityResult) {
        this.probabilityResult = probabilityResult;
    }

    /**
     * @return optional protocol or null
     */
    public BbxProtocol getProtocol() {
        return protocol;
    }

    /**
     * @param protocol sets the optional protocol
     */
    public void setProtocol(BbxProtocol protocol) {
        this.protocol = protocol;
    }

    /**
     * @param message some information to be added to the execution protocol
     */
    public void logProtocolMessage(String message) {
        if (this.protocol == null) {
            this.protocol = new BbxProtocol();
        }
        this.protocol.logEntry(executionId, message);
    }

    /**
     * Adds the data from the other result to <b>this result</b> assuming it has the exact same structure.
     * <p>
     * This method is intended to quickly sum up partial results and does not perform any plausibility checks, and it does not merge different sets of queries.
     * 
     * @param otherResult will not be modified
     */
    public void addResultData(BloomBoxQueryResult otherResult) {
        this.baseQueryCount = this.baseQueryCount + otherResult.baseQueryCount;
        if (otherResult.subQueryCounts != null) {
            addResultSubQueryCounts(otherResult);
        }
        if (otherResult.probabilityResult != null) {
            addResultProbabilities(otherResult);
        }
        if (otherResult.protocol != null) {
            if (this.protocol == null) {
                this.protocol = new BbxProtocol();
            }
            this.protocol.addProtocol(otherResult.protocol);
        }
        if (otherResult.errorMessage != null && this.errorMessage == null) {
            this.errorMessage = otherResult.errorMessage;
        }
        if (otherResult.warningMessage != null && this.warningMessage == null) {
            this.warningMessage = otherResult.warningMessage;
        }
    }

    /**
     * copies the probabilities into <b>this</b>
     * 
     * @param otherResult must contain probabilities
     */
    private void addResultProbabilities(BloomBoxQueryResult otherResult) {
        if (this.probabilityResult == null) {
            this.probabilityResult = new PbBloomBoxQueryResult();
        }
        this.probabilityResult.setBaseQuerySum(this.probabilityResult.getBaseQuerySum() + otherResult.probabilityResult.getBaseQuerySum());
        if (otherResult.probabilityResult.getSubQuerySums() != null) {
            double[] otherSubQuerySums = otherResult.probabilityResult.getSubQuerySums();
            if (this.probabilityResult.getSubQuerySums() != null) {
                double[] subQuerySums = this.probabilityResult.getSubQuerySums();
                for (int i = 0; i < subQuerySums.length; i++) {
                    subQuerySums[i] = subQuerySums[i] + otherSubQuerySums[i];
                }
            }
            else {
                this.probabilityResult.setSubQuerySums(Arrays.copyOf(otherSubQuerySums, otherSubQuerySums.length));
            }
        }
    }

    /**
     * copies counts into <b>this</b>
     * 
     * @param otherResult must contain sub query counts
     */
    private void addResultSubQueryCounts(BloomBoxQueryResult otherResult) {
        if (this.subQueryCounts != null) {
            for (int i = 0; i < subQueryCounts.length; i++) {
                this.subQueryCounts[i] = this.subQueryCounts[i] + otherResult.subQueryCounts[i];
            }
        }
        else {
            this.subQueryCounts = Arrays.copyOf(otherResult.subQueryCounts, otherResult.subQueryCounts.length);
        }
    }

    /**
     * Ensures that the {@link #probabilityResult} is not null and initialized properly
     */
    void ensurePbResultsInitialized() {
        if (this.probabilityResult == null) {
            this.probabilityResult = new PbBloomBoxQueryResult();
            this.probabilityResult.setSubQuerySums(new double[this.subQueryCounts == null ? 0 : this.subQueryCounts.length]);
        }
    }

    /**
     * @return unique execution id, positive, 0 if unknown
     */
    public long getExecutionId() {
        return executionId;
    }

    /**
     * @param executionId unique id of execution
     */
    public void setExecutionId(long executionId) {
        this.executionId = executionId;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getClass().getSimpleName());
        sb.append("(executionId=");
        sb.append(Long.toHexString(executionId));
        sb.append(", name=");
        sb.append(this.name);
        sb.append(", baseQueryCount=");
        sb.append(baseQueryCount);
        sb.append(", subQueryResults=");
        sb.append(buildSubQueryResultMap().toString());
        if (this.probabilityResult != null) {
            sb.append(", baseQuerySum=");
            sb.append(probabilityResult.getBaseQuerySum());
            sb.append(", subQuerySums=");
            sb.append(buildSubQuerySumMap().toString());
        }
        if (this.protocol != null) {
            sb.append(this.protocol.toString());
        }
        sb.append(", errorMessage=");
        sb.append(errorMessage);
        sb.append(", warningMessage=");
        sb.append(warningMessage);
        sb.append(")");
        return sb.toString();
    }

    /**
     * @return empty duplicate of this result, structurally equivalent but independent counts, messages and protocol
     */
    public BloomBoxQueryResult createSpawn() {
        BloomBoxQueryResult res = new BloomBoxQueryResult();
        res.name = this.name;
        res.subQueryCounts = this.subQueryCounts == null ? null : new long[this.subQueryCounts.length];
        res.subQueryLabels = this.subQueryLabels;
        res.probabilityResult = this.probabilityResult == null ? null : this.probabilityResult.createSpawn();
        res.executionId = this.executionId;
        return res;
    }
}
