//@formatter:off
/*
 * SimpleFixedLengthBitVector 
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

import java.util.Arrays;

/**
 * {@link SimpleFixedLengthBitVector} is a fixed-size bit-vector implementation. It is <b>not safe</b> to be used by multiple threads concurrently.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class SimpleFixedLengthBitVector implements FixedLengthBitVector {

    private static final long serialVersionUID = -7795199789391156117L;

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
    private final long[] vector;

    /**
     * Creates a new bit vector of the given size aligned to multiples of 64.
     * <p>
     * Thus {@link #getSize()} may be greater than the specified size, max difference 63.
     * 
     * @param size &gt;=1
     */
    public SimpleFixedLengthBitVector(long size) {
        if (size <= 0) {
            throw new IllegalArgumentException(String.format("Size must be >= 1, given: %d", size));
        }

        long vectorSizeL = (size + 63) / 64;

        if (vectorSizeL >= Integer.MAX_VALUE) {
            throw new IllegalArgumentException(String.format("Vector size must be < %d, given: %d (vector size: %d)", Integer.MAX_VALUE, size, vectorSizeL));
        }

        this.size = vectorSizeL * 64;

        this.vector = new long[(int) vectorSizeL];

    }

    /**
     * Creates the bit vector from the given number of longs, see {@link #toLongArray()}<br>
     * <b>Important:</b> For efficiency reasons we do not copy the given array but reference it internally!
     * 
     * @param longArray data
     */
    public SimpleFixedLengthBitVector(long[] longArray) {
        this.vector = longArray;
        this.size = this.vector.length * 64L;
    }

    /**
     * Sets the bit at the given position to 1
     * 
     * @param position bit position in the vector
     * @throws IndexOutOfBoundsException if the position is negative or exceeds the capacity
     */
    @Override
    public void setBit(long position) {

        // replaced division by 64 with bit-shifting
        int longIdx = (int) (position >>> 6L);

        // create a pattern with one bit set by shifting the bit "around"
        // this is the pattern we want to "OR" with the long value from the vector
        long singleBitMask = 1L << position;

        vector[longIdx] = vector[longIdx] | singleBitMask;
    }

    /**
     * Sets the bit if it was not present before. In other words, the caller will know if it was already present in a single operation.
     * <p>
     * <b>Note:</b> In any cases the bit in the vector will be 1 afterwards.
     * 
     * @param position bit position in the vector
     * @return true if the bit has been set or false if it was set before
     * @throws IndexOutOfBoundsException if the position is negative or exceeds the capacity
     */
    @Override
    public boolean setBitIfNotPresent(long position) {
        // replaced division by 64 with bit-shifting
        int longIdx = (int) (position >>> 6L);

        // create a pattern with one bit set by shifting the bit "around"
        // this is the pattern we want to "OR" with the long value from the vector
        long singleBitMask = 1L << position;

        long found = vector[longIdx];
        if ((found & singleBitMask) == singleBitMask) {
            // bit is already set
            return false;
        }
        vector[longIdx] = found | singleBitMask;
        return true;
    }

    /**
     * Sets the bit at the given position to 0
     * 
     * @param position bit position in the vector
     * @throws IndexOutOfBoundsException if the position is negative or exceeds the capacity
     */
    @Override
    public void unsetBit(int position) {

        // replaced division by 64 with bit-shifting
        int longIdx = position >>> 6L;

        // create a pattern of 1s with a single bit set to zero
        // this is the pattern we want to "AND" with the long value from the vector
        long singleBitMask = (1L << position) ^ -1L;

        vector[longIdx] = vector[longIdx] & singleBitMask;
    }

    /**
     * Sets the bit at the given position to 0, if it was 0 before. In other words, the caller will know if the bit was present before in a single operation.
     * <p>
     * <b>Note:</b> In any cases the bit in the vector will be 0 afterwards.
     * 
     * @param position bit position in the vector
     * @return true if the bit has been unset or false if the bit was 0 before
     * @throws IndexOutOfBoundsException if the position is negative or exceeds the capacity
     */
    @Override
    public boolean unsetBitIfPresent(int position) {

        // replaced division by 64 with bit-shifting
        int longIdx = position >>> 6L;

        // create a pattern of 1s with a single bit set to zero
        // this is the pattern we want to "AND" with the long value from the vector
        long singleBitMask = (1L << position) ^ -1L;

        long found = vector[longIdx];
        if ((found | singleBitMask) == singleBitMask) {
            // bit is not set
            return false;
        }
        vector[longIdx] = found & singleBitMask;
        return true;
    }

    /**
     * Flips the bit at the given position (0 -&gt; 1 resp. 1 -&gt; 0)
     * 
     * @param position bit position in the vector
     * @throws IndexOutOfBoundsException if the position is negative or exceeds the capacity
     */
    @Override
    public void flipBit(long position) {
        // replaced division by 64 with bit-shifting
        int longIdx = (int) (position >>> 6L);

        // create a pattern with one bit set by shifting the bit "around"
        // this is the pattern we want to "XOR" with the long value from the vector
        long singleBitMask = 1L << position;

        vector[longIdx] = vector[longIdx] ^ singleBitMask;
    }

    /**
     * @param position bit position in the vector
     * @return true if the bit was set, false otherwise
     * @throws IndexOutOfBoundsException if the position is negative or exceeds the capacity
     */
    @Override
    public boolean isBitSet(long position) {
        // replaced division by 64 with bit-shifting
        int longIdx = (int) (position >>> 6L);

        // create a pattern with one bit set by shifting the bit "around"
        // this is the pattern we want to "AND" with the long value from the vector for testing
        long singleBitMask = 1L << position;

        return (singleBitMask & vector[longIdx]) == singleBitMask;
    }

    /**
     * @param position bit position in the vector
     * @return 1 if the bit was set, 0 otherwise
     * @throws IndexOutOfBoundsException if the position is negative or exceeds the capacity
     */
    @Override
    public int getBit(long position) {

        // replaced division by 64 with bit-shifting
        int longIdx = (int) (position >>> 6L);

        // right shift by position modulo 64, then AND with 1 (00000...0001), result is either 1 or 0
        return (int) ((vector[longIdx] >> (position & 63)) & 1L);
    }

    /**
     * Counts the 1-bits in the vector from left to right.
     * 
     * @return number of bits set to 1 in this vector
     */
    @Override
    public long countNumberOfBitsSet() {
        long res = 0;
        for (int i = 0; i < vector.length; i++) {
            long l = vector[i];
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
     */
    @Override
    public void clear() {
        Arrays.fill(vector, EMPTY_LONG);
    }

    /**
     * Returns the total capacity (always a multiple of 64)
     * 
     * @return number of bits the vector can store
     */
    @Override
    public long getSize() {
        return this.size;
    }

    /**
     * Returns a reference to the internal long-array of this bit vector
     * 
     * @return reference to the internal long array
     */
    @Override
    public long[] toLongArray() {
        return vector;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "[size: " + this.size + ", size of long vector: " + this.vector.length + "]";
    }

    /**
     * Only for debugging, for easier checking (single consistent vector) longs appear from right (LSB) to left
     * 
     * @return a string composed of 0s and 1s, may be huge!
     */
    @Override
    public String toPaddedBinaryString() {
        StringBuilder sb = new StringBuilder();

        for (int i = vector.length - 1; i >= 0; i--) {
            String binary = Long.toBinaryString(vector[i]);
            binary = "0000000000000000000000000000000000000000000000000000000000000000" + binary;
            binary = binary.substring(binary.length() - 64);
            sb.append(binary);
        }
        return sb.toString();
    }

    /**
     * Utility method that performs a logical AND and returns true if all 1-bits in cmp are also present in src.<br>
     * E.g. <code>res = compareAND(src, 0, cmp, 0, 1)</code> is equivalent to <code>res = (src[0] &amp; cmp[0]) == cmp[0]</code>
     * <p>
     * To simplify usage the effective array length of src and cmp is irrelevant and independent from the given length, the operation automatically performs
     * right-padding with zeros.
     * 
     * @param src source array with long values
     * @param srcPos position to start in the source, <b>array index</b> (not the bit index), <code>&gt;=0</code>
     * @param cmp comparison array
     * @param cmpPos position to start in the comparison array, <b>array index</b> (not the bit index), <code>&gt;=0</code>
     * @param length number of longs to compare, for 0 or negative length this method gracefully returns true
     * @return true if all 1-bits in cmp are also set in src
     * @throws ArrayIndexOutOfBoundsException if srcPos or cmpPos were negative
     * @throws NullPointerException if src or cmp were null
     */
    public static final boolean compareAND(long[] src, int srcPos, long[] cmp, int cmpPos, int length) {
        if (srcPos < 0 || cmpPos < 0) {
            throw new ArrayIndexOutOfBoundsException("Array indexes must not be negative, given: srcPos=" + srcPos + ", cmpPos=" + cmpPos);
        }
        int srcLen = src.length;
        int cmpLen = cmp.length;
        for (int i = 0; i < length; i++) {
            int cmpIdx = cmpPos + i;
            if (cmpIdx >= cmpLen) {
                // means: the remainder of the pattern cmp consists of zeros, so AND will return zeros
                // no matter how src looks like
                return true;
            }
            int srcIdx = srcPos + i;
            long srcVal = (srcIdx < srcLen) ? src[srcIdx] : EMPTY_LONG;
            long cmpVal = cmp[cmpIdx];
            if ((srcVal & cmpVal) != cmpVal) {
                return false;
            }
        }
        return true;
    }
}
