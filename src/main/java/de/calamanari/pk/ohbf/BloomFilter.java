//@formatter:off
/*
 * BloomFilter
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

import java.io.Serializable;

/**
 * Interface for bloom filters
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public interface BloomFilter extends Serializable {

    /**
     * @return the static configuration of the filter provided to the constructor
     */
    public BloomFilterConfig getConfig();

    /**
     * Returns the size of this bloom filter (m)
     * 
     * @return number of bits usable
     */
    public long getSize();

    /**
     * For technical reasons the filter uses more bits than configured. This is called waste.
     * 
     * @return number of bits acquired but not required resp. unused
     */
    public long getWaste();

    /**
     * Puts the given key into the bloom filter.
     * 
     * @param attributes key, optionally composed of multiple values
     * @return true if the bloom filter changed (item was not in the filter before), otherwise false
     */
    public boolean put(Object... attributes);

    /**
     * Checks whether the key is in the bloom filter, with a certain probability of false-positive results and no false-negatives.
     * 
     * @param attributes key, optionally composed of multiple values
     * @return true if the key is probably in the filter, false if it is guaranteed not
     */
    public boolean mightContain(Object... attributes);

    /**
     * @return the total number of 1s in this bloom filter's bit-vector
     */
    public long getNumberOfBitsUsed();

    /**
     * Estimates the number elements in the bloom filter based on X ({@link #getNumberOfBitsUsed()}), k and m
     * 
     * @return rough estimate or -1 if the estimation is not possible (filter full)
     */
    public long getEstimatedNumberOfElementsInserted();

    /**
     * Only for debugging, for easier checking (single consistent vector) longs appear from right (LSB) to left
     * 
     * @return a string composed of 0s and 1s, may be huge and will fail if the vector exceeds any reasonable size!
     */
    public String getBitVectorAsPaddedBinaryString();

    /**
     * Returns a long array representing the internal bit vector. It is left to implementing classes to return a copy or a reference.
     * <p>
     * This operation is meant for scenarios dealing with many instances of bloom filters for fast matching after creation.
     * 
     * @return long array representing the internal bit vector
     */
    public long[] getBitVectorAsLongArray();
}
