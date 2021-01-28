//@formatter:off
/*
 * AtomicFixedLengthBitVector 
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
import java.util.concurrent.atomic.AtomicLongArray;

/**
 * {@link AtomicFixedLengthBitVector} is a fixed-size bit-vector implementation for concurrent access.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class AtomicFixedLengthBitVector implements Serializable {

    private static final long serialVersionUID = -8895199789391156337L;

    /**
     * Marks an empty long
     */
    private static final long EMPTY_LONG = 0L;

    /**
     * Total number of bits in this vector (fixed)
     */
    private final long size;

    /**
     * Long array (bit store), one long stores 64 bits
     */
    private final AtomicLongArray vector;

    /**
     * Creates a new bit vector of the given size aligned to multiples of 64.
     * <p>
     * Thus {@link #getSize()} may be greater than the specified size, max difference 63.
     * @param size &gt;=1
     */
    public AtomicFixedLengthBitVector(long size) {
        if (size <= 0) {
            throw new IllegalArgumentException(String.format("Size must be >= 1, given: %d", size));
        }

        long vectorSizeL = (size + 63) / 64;

        if (vectorSizeL >= Integer.MAX_VALUE) {
            throw new IllegalArgumentException(String.format("Vector size must be < %d, given: %d (vector size: %d)", Integer.MAX_VALUE, size, vectorSizeL));
        }

        this.size = vectorSizeL * 64;

        this.vector = new AtomicLongArray((int) vectorSizeL);

    }

    /**
     * Creates the bit vector from the given number of longs, see {@link #toLongArray()}
     * @param longArray data
     */
    public AtomicFixedLengthBitVector(long[] longArray) {
        this.vector = new AtomicLongArray(longArray);
        this.size = this.vector.length() * 64L;
    }

    /**
     * Sets the bit at the given position to 1
     * @param position bit position in the vector
     * @throws IndexOutOfBoundsException if the position is negative or exceeds the capacity
     */
    public void setBit(long position) {

        // replaced division by 64 with bit-shifting
        int longIdx = (int) (position >>> 6L);

        // create a pattern with one bit set by shifting the bit "around"
        // this is the pattern we want to "OR" with the long value from the vector
        long singleBitMask = 1L << position;

        boolean success = false;
        do {
            long found = vector.get(longIdx);
            long update = found | singleBitMask;
            success = (found == update) || vector.compareAndSet(longIdx, found, update);
        } while (!success);
    }

    /**
     * Sets the bit if it was not present before. In other words, the caller will know if it was already present in a single operation.
     * <p>
     * <b>Note:</b> In any cases the bit in the vector will be 1 afterwards.
     * @param position bit position in the vector
     * @return true if the bit has been set or false if it was set before
     * @throws IndexOutOfBoundsException if the position is negative or exceeds the capacity
     */
    public boolean setBitIfNotPresent(long position) {
        // replaced division by 64 with bit-shifting
        int longIdx = (int) (position >>> 6L);

        // create a pattern with one bit set by shifting the bit "around"
        // this is the pattern we want to "OR" with the long value from the vector
        long singleBitMask = 1L << position;

        boolean success = false;
        do {
            long found = vector.get(longIdx);
            if ((found & singleBitMask) == singleBitMask) {
                // bit is already set
                break;
            }
            long update = found | singleBitMask;
            success = (found == update) || vector.compareAndSet(longIdx, found, update);
        } while (!success);
        return success;
    }

    /**
     * Sets the bit at the given position to 0
     * @param position bit position in the vector
     * @throws IndexOutOfBoundsException if the position is negative or exceeds the capacity
     */
    public void unsetBit(int position) {

        // replaced division by 64 with bit-shifting
        int longIdx = position >>> 6L;

        // create a pattern of 1s with a single bit set to zero
        // this is the pattern we want to "AND" with the long value from the vector
        long singleBitMask = (1L << position) ^ -1L;

        boolean success = false;
        do {
            long found = vector.get(longIdx);
            long update = found & singleBitMask;
            success = (found == update) || vector.compareAndSet(longIdx, found, update);
        } while (!success);
    }

    /**
     * Sets the bit at the given position to 0, if it was 0 before. In other words, the caller will know if the bit was present before in a single operation.
     * <p>
     * <b>Note:</b> In any cases the bit in the vector will be 0 afterwards.
     * @param position bit position in the vector
     * @return true if the bit has been unset or false if the bit was 0 before
     * @throws IndexOutOfBoundsException if the position is negative or exceeds the capacity
     */
    public boolean unsetBitIfPresent(int position) {

        // replaced division by 64 with bit-shifting
        int longIdx = position >>> 6L;

        // create a pattern of 1s with a single bit set to zero
        // this is the pattern we want to "AND" with the long value from the vector
        long singleBitMask = (1L << position) ^ -1L;

        boolean success = false;
        do {
            long found = vector.get(longIdx);
            if ((found | singleBitMask) == singleBitMask) {
                // bit is not set
                break;
            }
            long update = found & singleBitMask;
            success = (found == update) || vector.compareAndSet(longIdx, found, update);
        } while (!success);
        return success;
    }

    /**
     * Flips the bit at the given position (0 -&gt; 1 resp. 1 -&gt; 0)
     * @param position bit position in the vector
     * @throws IndexOutOfBoundsException if the position is negative or exceeds the capacity
     */
    public void flipBit(long position) {
        // replaced division by 64 with bit-shifting
        int longIdx = (int) (position >>> 6L);

        // create a pattern with one bit set by shifting the bit "around"
        // this is the pattern we want to "XOR" with the long value from the vector
        long singleBitMask = 1L << position;

        boolean success = false;
        do {
            long found = vector.get(longIdx);
            long update = found ^ singleBitMask;
            success = (found == update) || vector.compareAndSet(longIdx, found, update);
        } while (!success);
    }

    /**
     * @param position bit position in the vector
     * @return true if the bit was set, false otherwise
     * @throws IndexOutOfBoundsException if the position is negative or exceeds the capacity
     */
    public boolean isBitSet(long position) {
        // replaced division by 64 with bit-shifting
        int longIdx = (int) (position >>> 6L);

        // create a pattern with one bit set by shifting the bit "around"
        // this is the pattern we want to "AND" with the long value from the vector for testing
        long singleBitMask = 1L << position;

        return (singleBitMask & vector.get(longIdx)) == singleBitMask;
    }

    /**
     * @param position bit position in the vector
     * @return 1 if the bit was set, 0 otherwise
     * @throws IndexOutOfBoundsException if the position is negative or exceeds the capacity
     */
    public int getBit(long position) {

        // replaced division by 64 with bit-shifting
        int longIdx = (int) (position >>> 6L);

        // right shift by position modulo 64, then AND with 1 (00000...0001), result is either 1 or 0
        return (int) ((vector.get(longIdx) >> (position & 63)) & 1L);
    }

    /**
     * Counts the 1-bits in the vector from left to right.
     * <p>
     * <b>Important:</b> This operation is not atomic but subsequently counts the bits in the underlying long values. Thus calling {@link #setBit(long)} or
     * {@link #flipBit(long)} concurrently is safe but may lead to unexpected results because the effect on the overall count will depend on the position the
     * concurrent change happens.
     * @return number of bits set to 1 in this vector
     */
    public long countNumberOfBitsSet() {
        long res = 0;
        for (int i = 0; i < vector.length(); i++) {
            long l = vector.get(i);
            if (l != EMPTY_LONG) {
                for (long bitPos = 0; bitPos < 64; bitPos++) {
                    res = res + ((l >> bitPos) & 1L);
                }
            }
        }

        return res;
    }

    /**
     * Sets all bits in this vector to 0.
     * <p>
     * <b>Important:</b> This operation is not atomic but consists on a sequence of updates. Calling it concurrently with other operations on this vector is
     * safe but may lead to surprising results.
     */
    public void clear() {
        for (int i = 0; i < vector.length(); i++) {
            vector.set(i, EMPTY_LONG);
        }
    }

    /**
     * Returns the total capacity (always a multiple of 64)
     * @return number of bits the vector can store
     */
    public long getSize() {
        return this.size;
    }

    /**
     * Returns the long-array representation of this bit vector
     * <p>
     * <b>Important:</b>
     * <ul>
     * <li>Consider memory consumption, this operation will create a copy and thus double the required memory.</li>
     * <li>This operation is not atomic. It is still safe to be called concurrently with other operations on this bit vector, but this may lead to unexpected
     * results.</li>
     * </ul>
     * @return copy of the internal long array
     */
    public long[] toLongArray() {
        long[] res = new long[vector.length()];
        for (int i = 0; i < vector.length(); i++) {
            res[i] = vector.get(i);
        }
        return res;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "[size: " + this.size + ", size of long vector: " + this.vector.length() + "]";
    }

    /**
     * Only for debugging, for easier checking (single consistent vector) longs appear from right (LSB) to left
     * @return a string composed of 0s and 1s, may be huge!
     */
    public String toPaddedBinaryString() {
        StringBuilder sb = new StringBuilder();

        for (int i = vector.length() - 1; i >= 0; i--) {
            String binary = Long.toBinaryString(vector.get(i));
            binary = "0000000000000000000000000000000000000000000000000000000000000000" + binary;
            binary = binary.substring(binary.length() - 64);
            sb.append(binary);
        }
        return sb.toString();
    }
}