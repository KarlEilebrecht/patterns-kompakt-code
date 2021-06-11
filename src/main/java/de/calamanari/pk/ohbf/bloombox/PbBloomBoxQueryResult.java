//@formatter:off
/*
 * PbBloomBoxQueryResult
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

/**
 * The {@link PbBloomBoxQueryResult} adds optional probability sums to a {@link BloomBoxQueryResult}.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class PbBloomBoxQueryResult implements Serializable {

    private static final long serialVersionUID = -7813607887943108607L;

    /**
     * accumulated probabilies (this is the count)
     */
    private double baseQuerySum = 0.0;

    /**
     * accumulated probabilities for the sub queries (aka the counts)
     */
    private double[] subQuerySums;

    public PbBloomBoxQueryResult() {
        // default constructor
    }

    /**
     * increments the number of matches of the main query
     * 
     * @param match probability of a single row
     */
    void incrementBaseQuerySum(double probability) {
        baseQuerySum = baseQuerySum + probability;
    }

    /**
     * Increment a sub query count (number of matching records)
     * 
     * @param idx index of the sub query
     * @param match probability of a single row
     */
    void incrementSubQuerySum(int idx, double probability) {
        subQuerySums[idx] = subQuerySums[idx] + probability;
    }

    /**
     * writes the sums as counts into the result
     * 
     * @param result target
     */
    void transferCounts(BloomBoxQueryResult result) {
        result.setBaseQueryCount(Math.round(baseQuerySum));
        for (int i = 0; i < subQuerySums.length; i++) {
            result.getSubQueryCounts()[i] = Math.round(subQuerySums[i]);
        }
    }

    /**
     * @return number of matches of the main query
     */
    public double getBaseQuerySum() {
        return baseQuerySum;
    }

    /**
     * @param baseQuerySum number of matches of the main query
     */
    public void setBaseQuerySum(double baseQuerySum) {
        this.baseQuerySum = baseQuerySum;
    }

    /**
     * @return sub query count (number of matching records)
     */
    public double[] getSubQuerySums() {
        return subQuerySums;
    }

    /**
     * @param subQuerySums sub query count (number of matching records)
     */
    public void setSubQuerySums(double[] subQuerySums) {
        this.subQuerySums = subQuerySums;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(PbBloomBoxQueryResult.class.getSimpleName());
        sb.append(" [ baseQuerySum=");
        sb.append(baseQuerySum);
        sb.append(", subQuerySums=");
        sb.append(Arrays.toString(subQuerySums));
        sb.append(" ]");
        return sb.toString();
    }

    /**
     * @return empty duplicate of this result, structurally equivalent but independent
     */
    public PbBloomBoxQueryResult createSpawn() {
        PbBloomBoxQueryResult res = new PbBloomBoxQueryResult();
        res.subQuerySums = this.subQuerySums == null ? null : new double[this.subQuerySums.length];
        return res;
    }
}
