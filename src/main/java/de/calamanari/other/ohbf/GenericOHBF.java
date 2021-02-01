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

import de.calamanari.pk.muhai.MuhaiGenerator;
import de.calamanari.pk.util.AtomicFixedLengthBitVector;

/**
 * The {@link GenericOHBFFirstAttempt} is a general-purpose thread-safe serializable fixed-size <b>One-Hashing Bloom Filter</b>.
 * <p>
 * After playing with cryptographic hashes in 2020 to create keys (see {@link MuhaiGenerator}) and being fascinated about the randomness of these hashes, I
 * wondered if it was possible to leverage the output of a single cryptographic hash to "simulate" multiple hash-functions required for a bloom filter. The
 * point is: a cryptographic hash computation may be expensive, but iterating over the input k-times for k cheap hash-functions isn't for free either.
 * Furthermore, selecting the "right" k hash-functions can be a headache, sometimes highly depending on the use-case and its data types. So, why not spending
 * more effort for computing a single well-investigated cryptographic hash? No need to "guess" any hash functions anymore, and with a large k most likely
 * comparable speed.<br/>
 * Soon I realized that I was obviously not the first one reasoning about this :) - and found the conference paper <i>One-Hashing Bloom Filter</i> published
 * 2015 by Jianyuan Lu, Tong Yang, Yi Wang, Huichen Dai, Linxiao Jin, Haoyu Song and Bin Liu, which can be found at <a href=
 * "https://www.researchgate.net/publication/284283336_One-Hashing_Bloom_Filter">https://www.researchgate.net/publication/284283336_One-Hashing_Bloom_Filter</a>.
 * <p>
 * The authors have successfully demonstrated that the k independent hash-functions a bloom filter requires can be replaced by leveraging the output of a single
 * cryptographic hash.<br/>
 * They suggest a multi-stage approach, where the first stage is computing a single hash followed by the application to k partitions of the filter (m).<br/>
 * Their partition mechanism creates k partitions to fit m, where the individual partition sizes are unique and prime. This way they can "simulate k
 * hash-functions" using modulo(partition size) to find the bit to set in each partition for a particular hashed input.
 * <p>
 * While I find the modulo-approach with prime-aligned partitions clever, I wondered if we were able to avoid that. The point is: We assume the hash to be
 * random and not to contain any internal patterns (otherwise the cryptographic hash would be vulnerable). So, for randomization we should not need any
 * additional sophisticated shuffling. The challenge is finding a mechanism to derive k pseudo-hash-values to set bits in the k partitions.
 * <p>
 * My idea was to use hash-bits as-is. Assumption: every integer number created of b bits from left to right should be random in range [0..2^b].<br/>
 * Thus we can create k partitions and derive k random numbers from the hash. Finally, we distribute the random number (range [0..2^x] to the partition's bits
 * <b>in a fair manner</b>.
 * <p>
 * I decided this to be an interesting POC and implemented this class. Performance was not my main concern, I wanted to create a filter that is easy to use to
 * encourage experimentation. However, one goal was to avoid the modulo operation to distribute the pseudo-random numbers to the partitions.
 * <p>
 * One thing I learned quickly was, that the partition sizes cannot be chosen arbitrarily. The best results can be achieved if the k partitions are all of the
 * same size. Having larger size differences between partitions in the same setup leads to an unpredictable behavior of the measured false-positive rate. I
 * believe that this can be explained to be a "premature virtual decrease of k", because the tiny partitions get "full" right after a few inserts (making the
 * related k irrelevant). Thus I decided to make the partition size fixed as <code>Math.ceil(m/k)</code>. Assuming I can create a fair distribution this also
 * reduces the waste (defined as vector size minus configured m) to a minimum. As a result <b>the maximum waste is <code>k - 1 + 63</code> bits</b> because the
 * bit vector is aligned to 64 bits (long-based), not counting some structural overhead this specific implementation comes with.
 * <p>
 * <b>Conclusion:</b>
 * <p>
 * I could demonstrate that the technique works and leads to a configurable and reliable bloom filter. It is easy to use and does not require the user to do any
 * complicated "guessing". The measured false-positive rate being less than the configured rate (almost always) and the results from
 * {@link #getEstimatedNumberOfElementsInserted()} compared to the measured insert count indicate high reliability. It is interesting how well theory (see
 * formulas at <a href="https://en.wikipedia.org/wiki/Bloom_filter">https://en.wikipedia.org/wiki/Bloom_filter</a> fits the measurements, at least for the few
 * tests I did.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class GenericOHBF implements Serializable {

    private static final long serialVersionUID = 2617291705844763246L;

    private static final Logger LOGGER = LoggerFactory.getLogger(GenericOHBF.class);

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
     * Hash generator for this filter
     */
    private final HashGenerator hasher;

    /**
     * size of the partition related to k, close to m/k, partitionSize &gt;= m/k
     */
    private final long partitionSize;

    /**
     * Creates a new empty filter based on the give configuration
     * @param config static settings for the filter
     */
    public GenericOHBF(BloomFilterConfig config) {
        this.config = config;
        this.partitionSize = (long) Math.ceil(((double) config.getRequiredNumberOfBitsM()) / config.getNumberOfHashesK());
        this.vector = new AtomicFixedLengthBitVector(partitionSize * config.getNumberOfHashesK());
        this.hasher = HashGenerators.createInstance(computeRequiredHashBitCount(config.getNumberOfHashesK()));
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
        return partitionSize * config.getNumberOfHashesK();
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
        for (int i = 0; i < config.getNumberOfHashesK(); i++) {
            long position = fetchBitPosition(hashBytes, i, partitionSize);
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
        for (int i = 0; i < config.getNumberOfHashesK(); i++) {
            long position = fetchBitPosition(hashBytes, i, partitionSize);
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
     * @return rough estimate or -1 if the estimation is not possible (filter full)
     */
    public long getEstimatedNumberOfElementsInserted() {
        return computeEstimatedNumberOfElementsInserted(getNumberOfBitsUsed(), getSize(), config.getNumberOfHashesK());
    }

    /**
     * Only for debugging, for easier checking (single consistent vector) longs appear from right (LSB) to left
     * @return a string composed of 0s and 1s, may be huge and will fail if the vector exceeds any reasonable size!
     */
    public String getBitVectorAsPaddedBinaryString() {
        String res = this.vector.toPaddedBinaryString();
        if (getSize() < res.length()) {
            res = res.substring(res.length() - (int) getSize());
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
     * Computes the required length of the hash bit vector
     * @param k number of partitions
     * @return length of the hash to be computed in bits
     */
    static int computeRequiredHashBitCount(int k) {

        // hash bits are precious, thus we limit the consumption for 1 partition to 16 bits
        // each next 32-bit hash will overlap with the last one by 16 bits
        return (k + 1) * 16;
    }

    /**
     * This method derives the next bit position (to check or to verify) from the given hash
     * @param hashBytes bytes from the hash
     * @param partition the partition (in other words the k's hash function to compute)
     * @param partitionSize size of the partition i.e. (m/k)
     * @return bit position in the global bit vector of the bloom filter
     */
    static long fetchBitPosition(byte[] hashBytes, int partition, long partitionSize) {

        // instead of moving the reader by 4 bytes, we move only 2 bytes per partition ("shingled" hash conversion)
        int byteOffset = partition * 2;

        long position = 0;

        for (int i = byteOffset; i < byteOffset + 4; i++) {

            // move the bits from the last byte to the left (fill from left to right)
            position = position << 8;

            // put the bits of the unsigned byte to the right of the long
            long bitMask = hashBytes[i] & 0xff;

            // copy the bits into the position
            position = position | bitMask;

        }

        // now we have an unsigned 32-bit position, which must be distributed "fair" to the partition elements
        // a good explanation can be found here: https://lemire.me/blog/2016/06/27/a-fast-alternative-to-the-modulo-reduction/
        position = (position * partitionSize) >>> 32L;

        // Now make a global vector position out of the partition-local position
        position = (partitionSize * partition) + position;

        return position;
    }

}
