//@formatter:off
/*
 * PbDataStore
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

import java.util.Map;

/**
 * Interface for data stores that support probabilities
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public interface PbDataStore extends BloomBoxDataStore {

    /**
     * Feeds the row and sets the given probabilities, any column not mapped will get a default probability of 1.0.
     * 
     * @param rowVector long vector (bloom filter)
     * @param rowIdx number of the row
     * @param dppVector data point probability vector, encoded by {@link ProbabilityVectorCodec#encodeDataPointProbabilities(Map)}
     * @throws BloomBoxException if the map contains an unknown column or if a probability is not in range 0..1
     */
    public void feedRow(long[] rowVector, long rowIdx, long[] dppVector);

}
