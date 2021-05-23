//@formatter:off
/*
 * DefaultDppFetcher
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
 * The {@link DefaultDppFetcher} gets prepared with an encoded (compressed) probability vector, which only gets inflated if needed (delayed operation).
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class DefaultDppFetcher implements DppFetcher {

    private static final long serialVersionUID = -6666709991440990978L;

    /**
     * compressed probability vector, to be uncompressed on demand
     */
    private byte[] compressedDPPs = null;

    /**
     * Cached uncompressed probability vector
     */
    private long[] cachedDppVector = null;

    @Override
    public double fetchDataPointProbability(int dataPointId) {

        long[] dppVector = getDppVector();

        // The data point probability vector is an array, effectively sorted
        // by data point id. Thus, we can perform a binary search to find the candidate

        long searchDppId = dataPointId;

        int idxL = 0;
        int idxR = dppVector.length - 1;

        double res = 0.0d;
        while (idxL <= idxR) {

            int idxM = (int) Math.floor((idxL + idxR) / 2.0d);

            long candidateDppId = dppVector[idxM] >>> 32L;

            if (candidateDppId < searchDppId) {
                idxL = idxM + 1;
            }
            else if (candidateDppId > searchDppId) {
                idxR = idxM - 1;
            }
            else {
                // strip data point id and return encoded float
                res = Float.intBitsToFloat((int) ((dppVector[idxM] << 32L) >>> 32L));
                if (res < 0) {
                    res = 0.0d;
                }
                else if (res > 1.0) {
                    res = 1.0d;
                }
                break;
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
}
