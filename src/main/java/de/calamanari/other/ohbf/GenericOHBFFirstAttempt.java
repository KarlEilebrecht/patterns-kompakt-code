//@formatter:off
/*
 * GenericOHBF
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
package de.calamanari.other.ohbf;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.calamanari.pk.util.AtomicFixedLengthBitVector;

/**
 * The {@link GenericOHBFFirstAttempt} was my first implementation of a general-purpose thread-safe serializable fixed-size <b>One-Hashing Bloom
 * Filter</b>.<br/>
 * I kept it for comparision purposes although it creates too much waste. The final implementation is {@link GenericOHBF}.
 * <p>
 * This implementation works on partitions of variable sizes aligned to 2^x. My first partition optimizer tried to fit m with k partitions of any 2^x-lengths
 * (choosing x arbitrarily down to two), which creates a potentially very unbalanced partition setup but minimizes waste. Unfortunately, this leads to a rather
 * bad and unpredictable real false-positive-rate. Sometimes the measured rate is close to the configured one, sometimes it is many times higher. Even worse, it
 * can vary heavily with different m's, even after small changes, a totally unwanted behavior. I have not investigated this further, but I believe that this can
 * be explained to be a "premature virtual decrease of k", because the tiny partitions get "full" right after a few inserts (making the related k irrelevant).
 * Thus, the current {@link Partitioner} works differently. It creates partitions as close as possible to the average (m/k), accepting more waste. For many
 * setups I saw a small waste but <b>the maximum waste is <code>((m/k)-1 + 63)</code></b>. This potentially unacceptable high waste is the biggest disadvantage
 * of my {@link GenericOHBFFirstAttempt}. Due to the 2^x-scaling for partition sizes it depends on the configured m whether it is possible to create any
 * partition setup with only one or two different sizes close to each other and small waste. Here the original approach described in the paper "One-Hashing
 * Bloom Filter" with the prime-aligned partitions produces way smaller waste. However, I suspect that the "premature virtual decrease of k"-effect for small
 * partitions in unbalanced partition setups is a common issue. With the prime number alignment they just have a better chance to avoid huge differences between
 * the biggest and the smallest partition.
 * <p>
 * <b>Conclusion:</b>
 * <p>
 * I could demonstrate that the technique works and leads to a configurable and reliable bloom filter. It is easy to use and does not require the user to do any
 * complicated "guessing". The measured false-positive rate being less than the configured rate and the results from
 * {@link #getEstimatedNumberOfElementsInserted()} compared to the measured insert count indicate high reliability. In the next iteration I will try to find a
 * better partitioning scheme to reduce waste.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 * @deprecated This implementation tends to create too much waste, {@link GenericOHBF} is the successor
 */
@Deprecated(since = "N/A", forRemoval = false)
public class GenericOHBFFirstAttempt implements Serializable {

    private static final long serialVersionUID = 2617291704844763246L;

    private static final Logger LOGGER = LoggerFactory.getLogger(GenericOHBFFirstAttempt.class);

    /**
     * static setup
     */
    private final BloomFilterConfig config;

    /**
     * The internal bit vector for the bloom filter
     */
    private final AtomicFixedLengthBitVector vector;

    /**
     * Counts the number of bits which have been set in this filter
     */
    private final AtomicLong bitsInUseCounter = new AtomicLong();

    /**
     * Partitions to manage the k results
     */
    private final Partition[] partitions;

    /**
     * Hash generator for this filter
     */
    private final HashGenerator hasher;

    /**
     * The number of bits we use (the vector may have up to 63 more unusable bits)
     */
    private final long effectiveNumberOfBitsM;

    /**
     * Creates a new empty filter based on the give configuration
     * @param config static settings for the filter
     */
    public GenericOHBFFirstAttempt(BloomFilterConfig config) {
        this.config = config;
        this.partitions = createPartitions(config);
        this.effectiveNumberOfBitsM = computeEffectiveM(partitions);
        this.vector = new AtomicFixedLengthBitVector(effectiveNumberOfBitsM);
        this.hasher = HashGenerators.createInstance(computeRequiredHashBitCount(partitions));
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
    public BloomFilterConfig getConfig() {
        return config;
    }

    /**
     * Returns the size of this bloom filter (m)
     * @return number of bits usable, the internal bit-vector is 64-aligned (long) and may be up to 63 bits bigger
     */
    public long getSize() {
        return effectiveNumberOfBitsM;
    }

    /**
     * For technical reasons the filter uses more bits than configured. This is called waste.
     * @return number of bits acquired but not required resp. unused
     */
    public long getWaste() {
        return vector.getSize() - config.getRequiredNumberOfBitsM();
    }

    /**
     * Puts the given key into the bloom filter
     * @param attributes key, optionally composed of multiple values
     * @return true if the bloom filter changed (item was not in the filter before), otherwise false
     */
    public boolean put(Object... attributes) {
        boolean res = false;
        byte[] hashBytes = hasher.computeHashBytes(attributes);
        int[] hashOffsets = new int[2];
        for (Partition partition : partitions) {
            long position = fetchBitPosition(hashBytes, hashOffsets, partition);
            boolean modified = vector.setBitIfNotPresent(position);
            res = res || modified;
            if (modified) {
                bitsInUseCounter.incrementAndGet();
            }
        }
        return res;
    }

    /**
     * Checks whether the key is in the bloom filter, with a certain probability of false-positive results and no false-negatives.
     * @param attributes key, optionally composed of multiple values
     * @return true if the key is probably in the filter, false if it is guaranteed not
     */
    public boolean mightContain(Object... attributes) {
        byte[] hashBytes = hasher.computeHashBytes(attributes);
        int[] hashOffsets = new int[2];
        for (Partition partition : partitions) {
            long position = fetchBitPosition(hashBytes, hashOffsets, partition);
            if (!vector.isBitSet(position)) {
                return false;
            }
        }
        return true;
    }

    /**
     * @return the total number of 1s in this bloom filter's bit-vector
     */
    public long getNumberOfBitsUsed() {
        return bitsInUseCounter.get();
    }

    /**
     * Estimates the number elements in the bloom filter based on X ({@link #getNumberOfBitsUsed()}), k and m
     * <p>
     * <b>Note:</b> Above 90% bits used (1s) the estimation starts deviating heavily from the real number of inserts.
     * @return rough estimate or -1 if the estimation is not possible (filter full)
     */
    public long getEstimatedNumberOfElementsInserted() {
        return computeEstimatedNumberOfElementsInserted(getNumberOfBitsUsed(), effectiveNumberOfBitsM, partitions.length);
    }

    /**
     * Only for debugging, for easier checking (single consistent vector) longs appear from right (LSB) to left
     * @return a string composed of 0s and 1s, may be huge and will fail if the vector exceeds any reasonable size!
     */
    public String getBitVectorAsPaddedBinaryString() {
        String res = this.vector.toPaddedBinaryString();
        if (effectiveNumberOfBitsM < res.length()) {
            res = res.substring(res.length() - (int) effectiveNumberOfBitsM);
        }
        return res;
    }

    /**
     * @return de-serialized consistent filter
     */
    Object readResolve() {
        // in a multi-threading scenario we can only hope that people will not serialize while still updating the vector.
        // However, if anybody did, the counter could get inconsistent to the vector
        // Thus, during de-serialization we set the counter to the correct value and can at least guarantee that the
        // de-serialized filter is internally consistent
        this.bitsInUseCounter.set(vector.countNumberOfBitsSet());
        return this;
    }

    /**
     * Formula (Swamidass &amp; Baldi (2007), see <a
     * href=https://en.wikipedia.org/wiki/Bloom_filter#Approximating_the_number_of_items_in_a_Bloom_filter">https://en.wikipedia.org/wiki/Bloom_filter#Approximating_the_number_of_items_in_a_Bloom_filter</a>)
     * <p>
     * <b>Note:</b> Above 90% bits used (1s) the estimation starts deviating heavily from the real number of inserts.
     * @param x number of 1-bits in the filter, <b>0 &lt;= x &lt;= m</b>
     * @param m available bits, <b>m &gt; 0</b>
     * @param k number of hash-functions (here partitions), <b>k &gt; 0</b>
     * @return approximation of the number of successful inserts
     */
    static long computeEstimatedNumberOfElementsInserted(long x, long m, long k) {
        if (x == m && m > 0) {
            // edge case formula cannot be applied to
            return computeEstimatedNumberOfElementsInserted(x - 1, m, k) + 1;
        }
        double xdivM = ((double) x) / m;
        double mdivK = ((double) m) / k;
        double estimate = -mdivK * Math.log(1 - xdivM);
        return (long) Math.ceil(estimate);
    }

    /**
     * Computes the size of the bit vector based on the partition layout
     * @param partitions space for the k's result bits
     * @return total size of the bit vector
     */
    static long computeEffectiveM(Partition[] partitions) {
        long res = 0;
        for (Partition p : partitions) {
            res = res + (long) Math.pow(2, p.bitCount);
        }
        return res;
    }

    /**
     * Computes the required length of the hash bit vector
     * @param partitions space for the k's result bits
     * @return length of the hash to be computed in bits
     */
    static int computeRequiredHashBitCount(Partition[] partitions) {
        int res = 0;
        for (Partition p : partitions) {
            res = res + p.bitCount;
        }
        return res;
    }

    /**
     * Estimates best partition layout based on k and m
     * @param config settings for k and m
     * @return List of partitions
     */
    static Partition[] createPartitions(BloomFilterConfig config) {
        Partitioner partitioner = new Partitioner();
        int[] bitCounts = partitioner.computePartitions(config.getRequiredNumberOfBitsM(), config.getNumberOfHashesK());

        Partition[] res = new Partition[bitCounts.length];

        long offset = 0;
        for (int i = 0; i < bitCounts.length; i++) {
            int bitCount = bitCounts[i];
            res[i] = new Partition(offset, bitCount);
            offset = offset + (long) Math.pow(2, bitCount);
        }
        return res;
    }

    /**
     * This method derives the next bit position (to check or to verify) from the given hash
     * @param hashBytes bytes from the hash
     * @param hashOffsets [0] byte position, [2] bit position in that byte, this state array will be modified in each run
     * @param partition the partition (in other words the k's hash function to compute)
     * @return bit position in the global bit vector of the bloom filter
     */
    static long fetchBitPosition(byte[] hashBytes, int[] hashOffsets, Partition partition) {
        int byteOffset = hashOffsets[0];
        int byteBitOffset = hashOffsets[1];

        long bitsToFetch = partition.bitCount;
        long bitMaskOffset = 0;

        long position = 0;
        while (bitsToFetch > 0) {

            // put the bits of the unsigned byte to the right of the long
            long bitMask = hashBytes[byteOffset] & 0xff;

            // the number of bits taken from the byte not already used before
            int bitsTaken = 8 - byteBitOffset;
            if (bitsToFetch < bitsTaken) {
                long numberOfBitsToDiscardOnTheRight = bitsTaken - bitsToFetch;
                // remove the bits we do not want to fetch
                bitMask = bitMask >>> numberOfBitsToDiscardOnTheRight;
                bitsTaken = (int) bitsToFetch;
            }

            // move the byte's bits to the left, discard already used bits on the left (before offset)
            bitMask = bitMask << (64L - bitsTaken);

            // move the bits to the right by the number of bits already set in the position value (fill from left to right)
            bitMask = bitMask >>> bitMaskOffset;

            // copy the bits taken from the current byte into the position value (OR)
            position = position | bitMask;

            bitsToFetch = bitsToFetch - bitsTaken;
            bitMaskOffset = bitMaskOffset + bitsTaken;
            byteBitOffset = byteBitOffset + bitsTaken;
            if (byteBitOffset == 8) {
                // byte complete, take next byte
                byteBitOffset = 0;
                byteOffset = byteOffset + 1;
            }
        }
        // now the position value is complete but sits on the left, so we need to shift it to the right
        // and we need to add the partition offset to make a global bit vector position out of the local position
        position = (position >>> (64L - partition.bitCount)) + partition.offset;
        hashOffsets[0] = byteOffset;
        hashOffsets[1] = byteBitOffset;
        return position;
    }

    /**
     * A partition keeps the bit count (implicitly the size) of the partition together with the bit vector offset
     *
     */
    static class Partition implements Serializable {

        private static final long serialVersionUID = 7484425282484833804L;

        final long offset;

        final int bitCount;

        Partition(long offset, int bitCount) {
            this.offset = offset;
            this.bitCount = bitCount;
        }

        @Override
        public String toString() {
            return this.getClass().getSimpleName() + " [offset=" + offset + ", bitCount=" + bitCount + "]";
        }

    }

}
