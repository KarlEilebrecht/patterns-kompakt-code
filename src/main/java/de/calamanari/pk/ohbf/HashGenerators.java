//@formatter:off
/*
 * HashGenerators 
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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.calamanari.pk.muhai.LongPrefix;
import de.calamanari.pk.muhai.MuhaiException;
import de.calamanari.pk.muhai.MuhaiGenerator;

/**
 * Provides generators for any required bit length
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class HashGenerators {

    private static final Logger LOGGER = LoggerFactory.getLogger(HashGenerators.class);

    /**
     * Returns a hash generator with an output greater than or equals to the requested bit count
     * @param bitCount number of required bits in each hash
     * @return hash generator instance that fulfills the requirements
     */
    public static HashGenerator createInstance(int bitCount) {
        HashGenerator res = null;
        if (bitCount <= DefaultHashGenerator.MAX_BITS) {
            res = DefaultHashGenerator.getInstance(bitCount);
        }
        else {
            res = new CompositeHashGenerator(bitCount);
        }
        LOGGER.debug("Created {}", res);
        return res;
    }

    /**
     * Utility
     */
    private HashGenerators() {
        // No instances
    }

    /**
     * Default implementation based on {@link MuhaiGenerator}, up to {@value #MAX_BITS} bits in a single hash run
     * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
     *
     */
    static class DefaultHashGenerator extends MuhaiGenerator implements HashGenerator {

        private static final long serialVersionUID = -3665494083875854023L;

        /**
         * Because of the limitation of the underlying message digest, a single run can return max this number of bits
         */
        private static final int MAX_BITS = 512;

        /**
         * Reusable message digests, thread-local to function in multi-threaded scenarios
         */
        private static final DefaultHashGenerator INSTANCE_160 = new DefaultHashGenerator(ThreadLocal.withInitial(() -> {
            try {
                return MessageDigest.getInstance("SHA-1");
            }
            catch (NoSuchAlgorithmException ex) {
                throw new MuhaiException("Unexpected issue during SHA-1-MessageDigest initialization.", ex);
            }
        }), 160);

        /**
         * Reusable message digests, thread-local to function in multi-threaded scenarios
         */
        private static final DefaultHashGenerator INSTANCE_256 = new DefaultHashGenerator(ThreadLocal.withInitial(() -> {
            try {
                return MessageDigest.getInstance("SHA-256");
            }
            catch (NoSuchAlgorithmException ex) {
                throw new MuhaiException("Unexpected issue during SHA-256-MessageDigest initialization.", ex);
            }
        }), 256);

        /**
         * Reusable message digests, thread-local to function in multi-threaded scenarios
         */
        private static final DefaultHashGenerator INSTANCE_512 = new DefaultHashGenerator(ThreadLocal.withInitial(() -> {
            try {
                return MessageDigest.getInstance("SHA-512");
            }
            catch (NoSuchAlgorithmException ex) {
                throw new MuhaiException("Unexpected issue during SHA-512-MessageDigest initialization.", ex);
            }
        }), 512);

        /**
         * This thread-local is not part of the persistent state and thus marked transient
         */
        private final transient ThreadLocal<MessageDigest> messageDigestHolder;

        /**
         * length of the byte array returned by {@link #computeHashBytes(Object...)}
         */
        private final int hashLength;

        /**
         * Returns the default hash generator with the given (or next higher) bit length
         * @param bitCount number of required bits
         * @return default instance
         */
        static DefaultHashGenerator getInstance(int bitCount) {
            if (bitCount < 1 || bitCount > 512) {
                throw new IllegalArgumentException("The parameter bitCount must be in range [1..512], given: " + bitCount);
            }
            if (bitCount > 256) {
                return INSTANCE_512;
            }
            else if (bitCount > 160) {
                return INSTANCE_256;
            }
            else {
                return INSTANCE_160;
            }
        }

        /**
         * internal constructor
         * @param messageDigestHolder digest thread-local
         * @param bitCount number of bits, a multiple of 8, defines the length of the hash
         */
        private DefaultHashGenerator(ThreadLocal<MessageDigest> messageDigestHolder, int bitCount) {
            super(LongPrefix.NONE);
            this.messageDigestHolder = messageDigestHolder;
            this.hashLength = bitCount / 8;
        }

        @Override
        protected MessageDigest initDigest() {
            MessageDigest res = messageDigestHolder.get();
            res.reset();
            return res;
        }

        @Override
        public byte[] computeHashBytes(Object... attributes) {
            return super.computeHashBytes(attributes);
        }

        @Override
        public int getHashLength() {
            return this.hashLength;
        }

        /**
         * Replaces the instance during deserialization with, so that serialization won't create duplicates in the same VM.
         * @return generator instance
         */
        Object readResolve() {
            return getInstance(this.hashLength * 8);
        }

        /**
         * Remove minimal thread-local state.
         * <p>
         * If (in a framework context) a thread no longer needs any generator, this method should be called to remove this cached state. The method does not
         * affect the correct function of the generator as the cache is only a performance optimization.<br />
         * In most scenarios NOT calling this method also won't have any negative impact.
         */
        public void release() {
            messageDigestHolder.remove();
        }

        @Override
        public String toString() {
            return this.getClass().getSimpleName() + " [hashBits=" + (hashLength * 8) + "]";
        }

    }

    /**
     * This generator involves multiple hash runs to return longer sequences of hash bytes
     * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
     *
     */
    static class CompositeHashGenerator implements HashGenerator {

        private static final long serialVersionUID = -7707320148469606436L;

        /**
         * Generators to be called in a row to compute the byte sequence
         */
        private final HashGenerator[] chainedGenerators;

        /**
         * Number of bytes returned by {@link #computeHashBytes(Object...)}
         */
        private final int hashLength;

        /**
         * Internal constructor
         * @param bitCount required number of bits, defines the length of the hash byte array, {@link #getHashLength()}
         */
        private CompositeHashGenerator(int bitCount) {
            if (bitCount < 1) {
                throw new IllegalArgumentException("The argument bitCount must be >= 1, given: " + bitCount);
            }
            List<HashGenerator> generatorList = new ArrayList<>();
            int length = 0;
            while (bitCount >= 512) {
                generatorList.add(DefaultHashGenerator.getInstance(512));
                bitCount = bitCount - 512;
                length = length + 64;
            }
            while (bitCount >= 256) {
                generatorList.add(DefaultHashGenerator.getInstance(256));
                bitCount = bitCount - 256;
                length = length + 32;
            }
            while (bitCount > 0) {
                generatorList.add(DefaultHashGenerator.getInstance(160));
                bitCount = bitCount - 160;
                length = length + 20;
            }
            this.chainedGenerators = generatorList.toArray(new HashGenerator[generatorList.size()]);
            this.hashLength = length;
        }

        @Override
        public byte[] computeHashBytes(Object... attributes) {

            byte[] res = new byte[hashLength];
            attributes = Arrays.copyOf(attributes, attributes.length + 1);
            int position = 0;
            for (int i = 0; i < chainedGenerators.length; i++) {
                // add salt to leverage avalanche effect
                attributes[attributes.length - 1] = i;
                byte[] partialHash = chainedGenerators[i].computeHashBytes(attributes);
                System.arraycopy(partialHash, 0, res, position, partialHash.length);
                position = position + partialHash.length;
            }
            return res;
        }

        @Override
        public int getHashLength() {
            return hashLength;
        }

        @Override
        public String toString() {
            return this.getClass().getSimpleName() + " [hashBits=" + (hashLength * 8) + "]";
        }

    }

}
