//@formatter:off
/*
 * LwGenericOHBF
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
package de.calamanari.pk.ohbf;

import java.text.NumberFormat;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.calamanari.pk.muhai.MuhaiGenerator;
import de.calamanari.pk.util.SimpleFixedLengthBitVector;

/**
 * Light-weight implementation of the {@link GenericOHBF}, <b>NOT</b> safe to be used by multiple threads concurrently.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class LwGenericOHBF implements BloomFilter {

    private static final long serialVersionUID = 2633391705844763277L;

    private static final Logger LOGGER = LoggerFactory.getLogger(LwGenericOHBF.class);

    /**
     * static setup
     */
    private final BloomFilterConfig config;

    /**
     * The internal bit vector for the bloom filter
     */
    private final SimpleFixedLengthBitVector vector;

    /**
     * Counts the number of bits which have been set in this filter
     */
    private long bitsInUseCounter = 0L;

    /**
     * Hash generator for this filter
     */
    private final HashGenerator hasher;

    /**
     * size of the partition related to k, close to m/k, partitionSize &gt;= m/k
     */
    private final long partitionSize;

    /**
     * Creates a new empty filter based on the give configuration
     * 
     * @param config static settings for the filter
     */
    public LwGenericOHBF(BloomFilterConfig config) {
        this.config = config;
        this.partitionSize = (long) Math.ceil(((double) config.getRequiredNumberOfBitsM()) / config.getNumberOfHashesK());
        this.vector = new SimpleFixedLengthBitVector(partitionSize * config.getNumberOfHashesK());
        this.hasher = HashGenerators.createInstance(GenericOHBF.computeRequiredHashBitCount(config.getNumberOfHashesK()));
        if (LOGGER.isDebugEnabled()) {
            long waste = this.getWaste();
            double percentage = (((double) waste) / config.getRequiredNumberOfBitsM()) * 100d;
            NumberFormat nf = NumberFormat.getInstance(Locale.US);
            nf.setMaximumFractionDigits(5);
            LOGGER.debug("Created new empty filter for config {}, effective m={}, total waste={} ({}%)", config, vector.getSize(), waste,
                    nf.format(percentage));
        }
    }

    /**
     * @return the static configuration of the filter provided to the constructor
     */
    @Override
    public BloomFilterConfig getConfig() {
        return config;
    }

    /**
     * Returns the size of this bloom filter (m)
     * 
     * @return number of bits usable, the internal bit-vector is 64-aligned (long) and may be up to 63 bits bigger
     */
    @Override
    public long getSize() {
        return partitionSize * config.getNumberOfHashesK();
    }

    /**
     * For technical reasons the filter uses more bits than configured. This is called waste.
     * 
     * @return number of bits acquired but not required resp. unused
     */
    @Override
    public long getWaste() {
        return vector.getSize() - config.getRequiredNumberOfBitsM();
    }

    /**
     * Puts the given key into the bloom filter.
     * <p>
     * Please refer to the documentation of {@link MuhaiGenerator} to understand how the attributes are handled.
     * 
     * @param attributes key, optionally composed of multiple values
     * @return true if the bloom filter changed (item was not in the filter before), otherwise false
     */
    @Override
    public boolean put(Object... attributes) {
        boolean res = false;
        byte[] hashBytes = hasher.computeHashBytes(attributes);
        for (int i = 0; i < config.getNumberOfHashesK(); i++) {
            long position = GenericOHBF.fetchBitPosition(hashBytes, i, partitionSize);
            boolean modified = vector.setBitIfNotPresent(position);
            res = res || modified;
            if (modified) {
                bitsInUseCounter++;
            }
        }
        return res;
    }

    /**
     * Checks whether the key is in the bloom filter, with a certain probability of false-positive results and no false-negatives.
     * <p>
     * Please refer to the documentation of {@link MuhaiGenerator} to understand how the attributes are handled.
     * 
     * @param attributes key, optionally composed of multiple values
     * @return true if the key is probably in the filter, false if it is guaranteed not
     */
    @Override
    public boolean mightContain(Object... attributes) {
        byte[] hashBytes = hasher.computeHashBytes(attributes);
        for (int i = 0; i < config.getNumberOfHashesK(); i++) {
            long position = GenericOHBF.fetchBitPosition(hashBytes, i, partitionSize);
            if (!vector.isBitSet(position)) {
                return false;
            }
        }
        return true;
    }

    /**
     * @return the total number of 1s in this bloom filter's bit-vector
     */
    @Override
    public long getNumberOfBitsUsed() {
        return bitsInUseCounter;
    }

    /**
     * Estimates the number elements in the bloom filter based on X ({@link #getNumberOfBitsUsed()}), k and m
     * 
     * @return rough estimate or -1 if the estimation is not possible (filter full)
     */
    @Override
    public long getEstimatedNumberOfElementsInserted() {
        return GenericOHBF.computeEstimatedNumberOfElementsInserted(getNumberOfBitsUsed(), getSize(), config.getNumberOfHashesK());
    }

    /**
     * Only for debugging, for easier checking (single consistent vector) longs appear from right (LSB) to left
     * 
     * @return a string composed of 0s and 1s, may be huge and will fail if the vector exceeds any reasonable size!
     */
    @Override
    public String getBitVectorAsPaddedBinaryString() {
        String res = this.vector.toPaddedBinaryString();
        if (getSize() < res.length()) {
            res = res.substring(res.length() - (int) getSize());
        }
        return res;
    }

    /**
     * Returns a <b>reference</b> to the internal long array (not a copy!)
     * <p>
     * This operation is meant for scenarios dealing with many instances of bloom filters for fast matching after creation.
     * 
     * @return reference to the long array of the internal bit vector
     */
    @Override
    public long[] getBitVectorAsLongArray() {
        return this.vector.toLongArray();
    }

    /**
     * Resets this filter to its initial state (empty vector)
     */
    public void clear() {
        this.vector.clear();
        this.bitsInUseCounter = 0;
    }

    /**
     * @return de-serialized filter
     */
    Object readResolve() {
        this.bitsInUseCounter = vector.countNumberOfBitsSet();
        return this;
    }

}
