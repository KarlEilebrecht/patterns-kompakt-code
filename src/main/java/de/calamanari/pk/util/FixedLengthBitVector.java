//@formatter:off
/*
 * FixedLengthBitVector 
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
package de.calamanari.pk.util;

import java.io.Serializable;

/**
 * Interface for bit vectors of a fixed size based on java.lang.long, thus aligned to multiples of 64 bits.
 * 
 * @author karl.eilebrecht
 *
 */
public interface FixedLengthBitVector extends Serializable {

    /**
     * Sets the bit at the given position to 1
     * 
     * @param position bit position in the vector
     * @throws IndexOutOfBoundsException if the position is negative or exceeds the capacity
     */
    void setBit(long position);

    /**
     * Sets the bit if it was not present before. In other words, the caller will know if it was already present in a single operation.
     * <p>
     * <b>Note:</b> In any cases the bit in the vector will be 1 afterwards.
     * 
     * @param position bit position in the vector
     * @return true if the bit has been set or false if it was set before
     * @throws IndexOutOfBoundsException if the position is negative or exceeds the capacity
     */
    public boolean setBitIfNotPresent(long position);

    /**
     * Sets the bit at the given position to 0
     * 
     * @param position bit position in the vector
     * @throws IndexOutOfBoundsException if the position is negative or exceeds the capacity
     */
    public void unsetBit(int position);

    /**
     * Sets the bit at the given position to 0, if it was 0 before. In other words, the caller will know if the bit was present before in a single operation.
     * <p>
     * <b>Note:</b> In any cases the bit in the vector will be 0 afterwards.
     * 
     * @param position bit position in the vector
     * @return true if the bit has been unset or false if the bit was 0 before
     * @throws IndexOutOfBoundsException if the position is negative or exceeds the capacity
     */
    public boolean unsetBitIfPresent(int position);

    /**
     * Flips the bit at the given position (0 -&gt; 1 resp. 1 -&gt; 0)
     * 
     * @param position bit position in the vector
     * @throws IndexOutOfBoundsException if the position is negative or exceeds the capacity
     */
    public void flipBit(long position);

    /**
     * @param position bit position in the vector
     * @return true if the bit was set, false otherwise
     * @throws IndexOutOfBoundsException if the position is negative or exceeds the capacity
     */
    public boolean isBitSet(long position);

    /**
     * @param position bit position in the vector
     * @return 1 if the bit was set, 0 otherwise
     * @throws IndexOutOfBoundsException if the position is negative or exceeds the capacity
     */
    public int getBit(long position);

    /**
     * Counts the 1-bits in the vector from left to right.
     * 
     * @return number of bits set to 1 in this vector
     */
    public long countNumberOfBitsSet();

    /**
     * Sets all bits in this vector to 0.
     */
    public void clear();

    /**
     * Returns the total capacity (always a multiple of 64)
     * 
     * @return number of bits the vector can store
     */
    public long getSize();

    /**
     * Returns a reference to the internal long-array of this bit vector
     * 
     * @return reference to the internal long array
     */
    public long[] toLongArray();

    /**
     * Only for debugging, for easier checking (single consistent vector) longs appear from right (LSB) to left
     * 
     * @return a string composed of 0s and 1s, may be huge!
     */
    public String toPaddedBinaryString();

}
