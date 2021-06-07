//@formatter:off
/*
 * PbThresholdBinaryFeeder
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

import java.util.List;
import java.util.Random;

import de.calamanari.pk.ohbf.BloomFilterConfig;

/**
 * Specialized store for feeding a {@link DefaultDataStore} or {@link FileDataStore} (but NOT a {@link PbDataStore}!) with probabilities.
 * <p>
 * The probabilities will be eliminated by treating the input binary (to put vs. not to put the data point). Therefore we crate a random value
 * <b><code>R</code></b> in range <code>[0.0 .. 1.0]</code> and apply the data point's probability {@link PbDataPoint#getProbability()} as a threshold
 * <b><code>T</code></b>. If <b><code>R &lt;= T</code></b> we put the data point, otherwise not.
 * <p>
 * This technique is intended for cases where a {@link PbDataStore} with attached probabilities would be too big or too slow or (often a problem) the variables
 * have natural dependencies that negatively impact the probability based computation.
 * <p>
 * This way the estimations (results) of the bloom box will in general be less correct but easier to deal with than unpredictable deviations caused by
 * conditional probabilities.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class PbThresholdBinaryFeeder extends DataStoreFeeder {

    /**
     * Random generator, by default initialized with current time
     */
    private Random random = new Random();

    /**
     * @param config bloom filter configuration
     * @param dataStore the underlying store with probabilities
     * @param markSealed true to mark this store sealed
     */
    public PbThresholdBinaryFeeder(BloomFilterConfig config, BloomBoxDataStore dataStore, boolean markSealed) {
        super(config, dataStore, markSealed);
        if (dataStore instanceof PbDataStore) {
            throw new IllegalArgumentException(String.format("%s is not suitable for a %s, given: %s", this.getClass().getSimpleName(),
                    PbDataStore.class.getSimpleName(), String.valueOf(dataStore)));
        }
    }

    /**
     * Adds the given list of data points as a row to the store including the probabilities
     * 
     * @param dataPoints key/value combination (unique within a row) with attached probability
     * @return true if the row was added, false if the store was already full
     */
    public boolean addRow(List<PbDataPoint> dataPoints) {

        if (moveToNextRow()) {
            bloomFilter.clear();
            for (PbDataPoint dataPoint : dataPoints) {
                if (random.nextDouble() <= dataPoint.getProbability()) {
                    bloomFilter.put(dataPoint.getColumnId(), dataPoint.getColumnValue());
                }
            }
            dataStore.feedRow(bloomFilter.getBitVectorAsLongArray(), currentRowIndex);
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * @return the random instance used by this feeder
     */
    public Random getRandom() {
        return random;
    }

    /**
     * This method can be used to set a specific random before feeding to create reproducible results
     * 
     * @param random the generator used by this feeder
     */
    public void setRandom(Random random) {
        this.random = random;
    }

}
