//@formatter:off
/*
 * DataPointProbabilityManager
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

import java.util.HashMap;
import java.util.Map;

import de.calamanari.pk.ohbf.bloombox.bbq.BbqExpression;
import de.calamanari.pk.ohbf.bloombox.bbq.BloomFilterQuery;
import de.calamanari.pk.ohbf.bloombox.bbq.QuantumOptimizer;

/**
 * The {@link DataPointProbabilityManager} is a {@link DataPointOccurrenceCollector} to scan queries once for data point usages.<br>
 * Its second role is the {@link DppFetcher} which allows fetching the stored probabilities at query execution time.<br>
 * This implementation gets prepared with an encoded (compressed) probability vector, which only gets inflated if needed (delayed operation).
 * <p>
 * <b>Note:</b> There is a general problem with predicate logic with attached probabilities. As long as every atom (data point like <code>color=blue</code>with
 * its probability only appears only once in an expression, the way we recursively create a product based on the {@link BbqExpression}s is correct. <br>
 * In the moment any atom is referenced more than once in the expression, this estimation is wrong, because the conditions become <i>dependent</i> in an
 * unpredictable way. <br>
 * A trivial example is the expression <code>(A or B) and B</code>. For any probability <code>0 < P(B) &lt; 1</code> you will see that
 * <code>P( (A or B) and B ) <b>!=</b> (1 - ((1 - P(A)) * (1 - P(B)))) * P(B)</code>, obviously correct is: <code>P( (A or B) and B ) == P(B)</code>.<br>
 * Simple cases like this can be handled by the {@link QuantumOptimizer}, but there are cases the optimizer cannot handle.<br>
 * As we do not know the dependency between the events to do a perfect computation, and I cannot do a proper estimation either to correct the results (at least
 * not with feasible effort), I decided to apply the SQUAREROOT-hack which reduces the impact of the error on the overall product. The problem with this hack is
 * that the error grows (unpredictably) with the number of multi-references and the deviation of a feature's probability from random distribution.
 * <p>
 * In many cases the results are ok, so I decided to cover problem cases with a warning, so a user may decide to reduce complexity of the query.
 * <p>
 * Maybe we will find a better approach later. :)
 * 
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class DataPointProbabilityManager implements DppFetcher, DataPointOccurrenceCollector {

    private static final long serialVersionUID = -6666709991440990978L;

    /**
     * compressed probability vector, to be uncompressed on demand
     */
    private byte[] compressedDPPs = null;

    /**
     * Cached uncompressed probability vector
     */
    private long[] cachedDppVector = null;

    /**
     * Maps the number of occurrences of the same data point in the same expression
     */
    private final Map<Long, Integer> multiOccurrenceMap = new HashMap<>();

    /**
     * Maps processed query names to unique id
     */
    private Map<String, Long> nameToQueryIdMap = new HashMap<>();

    /**
     * Maps bloom filter query ids to counts
     */
    private Map<Long, Integer> multiReferenceMap = new HashMap<>();

    /**
     * id of the bloom filter query currently processed by the collector
     */
    private long currentQueryId = -1;

    @Override
    public double fetchDataPointProbability(long rootExpressionId, int dataPointId) {

        long[] dppVector = getDppVector();

        // The data point probability vector is an array, effectively ordered ascending
        // by data point id. Thus, we can perform a binary search to find the candidate

        int idxL = 0;
        int idxR = dppVector.length - 1;

        double res = 0.0d;
        while (idxL <= idxR) {

            int idxM = (int) Math.floor((idxL + idxR) / 2.0d);

            int candidateDppId = ProbabilityVectorCodec.decodeDataPointId(dppVector[idxM]);

            if (candidateDppId < dataPointId) {
                idxL = idxM + 1;
            }
            else if (candidateDppId > dataPointId) {
                idxR = idxM - 1;
            }
            else {
                // strip data point id and return encoded float
                res = ProbabilityVectorCodec.decodeDataPointProbability(dppVector[idxM]);
                break;
            }
        }

        if (res > 0.0) {
            Integer numberOfOccurrences = multiOccurrenceMap.get(createMultiOccurrenceKey(rootExpressionId, dataPointId));
            if (numberOfOccurrences != null && numberOfOccurrences > 1) {
                // SQUAREROOT hack to mitigate the error because we do not know the correct value
                double correction = Math.sqrt(res);
                res = correction;
            }
        }

        return res;
    }

    /**
     * To be called to initialize the fetcher for the current row.
     * 
     * @param compressedDPPs encoded data point probabilities
     */
    public void initialize(byte[] compressedDPPs) {
        this.compressedDPPs = compressedDPPs;
        this.cachedDppVector = null;
    }

    /**
     * @return the uncompressed data point probability vector
     */
    protected long[] getDppVector() {
        if (cachedDppVector == null) {
            cachedDppVector = ProbabilityVectorCodec.getInstance().decode(compressedDPPs);
            compressedDPPs = null;
        }
        return cachedDppVector;
    }

    /**
     * Creates a key for the multi-occurrence map that identifies reuse of the same data point within an expression
     * 
     * @param rootExpressionId the expression using this data point
     * @param dataPointId key/value identifier
     * @return key for the multi-occurrence map
     */
    private static final long createMultiOccurrenceKey(long rootExpressionId, int dataPointId) {
        return (rootExpressionId << 32L) | dataPointId;
    }

    @Override
    public void addDataPointOccurrence(long expressionId, int dataPointId) {
        if (currentQueryId > -1) {
            long key = createMultiOccurrenceKey(expressionId, dataPointId);
            Integer numberOfOccurrences = this.multiOccurrenceMap.get(key);
            numberOfOccurrences = numberOfOccurrences == null ? 1 : numberOfOccurrences + 1;
            if (numberOfOccurrences > 1) {
                this.multiReferenceMap.put(currentQueryId, Math.max(numberOfOccurrences, this.multiReferenceMap.get(currentQueryId)));
            }
            this.multiOccurrenceMap.put(key, numberOfOccurrences);
        }
    }

    @Override
    public boolean startCollection(BloomFilterQuery query, String queryName) {

        this.nameToQueryIdMap.put(queryName, query.getId());

        if (this.multiReferenceMap.putIfAbsent(query.getId(), 0) == null) {
            this.currentQueryId = query.getId();
            return true;
        }
        this.currentQueryId = -1;
        return false;
    }

    @Override
    public Map<String, Integer> getMaxOccurrenceMap() {
        Map<String, Integer> res = new HashMap<>();
        for (Map.Entry<String, Long> entry : this.nameToQueryIdMap.entrySet()) {
            long queryId = entry.getValue();
            int max = multiReferenceMap.get(queryId);
            if (max > 1) {
                res.put(entry.getKey(), max);
            }
        }
        return res;
    }

}
