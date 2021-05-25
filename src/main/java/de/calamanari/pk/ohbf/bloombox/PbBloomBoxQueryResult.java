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
 * The {@link PbBloomBoxQueryResult} wraps a {@link BloomBoxQueryResult} to delay the effective counting, because probabilities need to be summed-up before we
 * can fill the total results properly.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class PbBloomBoxQueryResult implements Serializable {

    private static final long serialVersionUID = -7813607887943108607L;

    /**
     * The wrapped result to be updated finally
     */
    private final BloomBoxQueryResult result;

    /**
     * accumulated probabilies (this is the count)
     */
    private double baseQuerySum = 0.0;

    /**
     * accumulated probabilities for the sub queries (aka the counts)
     */
    private final double[] subQuerySums;

    public PbBloomBoxQueryResult(BloomBoxQueryResult result) {
        this.result = result;
        this.subQuerySums = new double[result.getSubQueryCounts().length];
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
     * writes the sums as counts into the wrapped result
     * 
     */
    void transferCounts() {
        result.setBaseQueryCount(Math.round(baseQuerySum));
        for (int i = 0; i < subQuerySums.length; i++) {
            result.getSubQueryCounts()[i] = Math.round(subQuerySums[i]);
        }
    }

    /**
     * @param message error to set at the result
     */
    void setErrorMessage(String message) {
        result.setErrorMessage(message);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(PbBloomBoxQueryResult.class.getSimpleName());
        sb.append(" [ baseQuerySum=");
        sb.append(baseQuerySum);
        sb.append(", subQuerySums=");
        sb.append(Arrays.toString(subQuerySums));
        sb.append(", wrapping ");
        sb.append(String.valueOf(result));
        sb.append(" ]");
        return sb.toString();
    }
}
