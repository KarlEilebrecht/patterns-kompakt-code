//@formatter:off
/*
 * DistributionCodecHash
 * Code-Beispiel zum Buch Patterns Kompakt, Verlag Springer Vieweg
 * Copyright 2024 Karl Eilebrecht
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
package de.calamanari.pk.drhe;

import de.calamanari.pk.drhe.util.BitUtils;
import de.calamanari.pk.drhe.util.PrimePatterns;

/**
 * Hashing (digest) based on the {@link DistributionCodec}.
 * <p/>
 * This implementation roughly follows the <a href="https://en.wikipedia.org/wiki/Merkle%E2%80%93Damg%C3%A5rd_construction">Merkle-Damgård construction</a>
 * using the distribution methods provided by {@link DistributionCodec} to encode a single block.
 * <ul>
 * <li>The internal block length is 16 bytes (128 bits) represented by two long values.</li>
 * <li>During the construction we take 16 bytes from the input (padded) and encode them into the digest</li>
 * <li>When obtaining the final hash (finalization), we only return 8 bytes (64 bits) hashes to be immune against any
 * <a href="https://en.wikipedia.org/wiki/Length_extension_attack">length-extension attacks.</a></li>
 * <li>Be aware that this is solely meant for demonstration purposes. The code runs rather slow due to the number of cycles which are required to make the
 * result look random even if the input is extremely uniform.</li>
 * </ul>
 * <p/>
 * Instances are stateful and must not be accessed concurrently by multiple threads.
 * <p/>
 * At <a href="https://mzsoltmolnar.github.io/random-bitstream-tester/">https://mzsoltmolnar.github.io/random-bitstream-tester/</a> you can check generated
 * output for randomness based on a <a href="https://csrc.nist.gov/Projects/Random-Bit-Generation/Documentation-and-Software">NIST test suite</a>. I found the
 * <a href="https://en.wikipedia.org/wiki/Wald%E2%80%93Wolfowitz_runs_test">Runs Test</a> the be the worst enemy.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class DistributionCodecHash {

    /**
     * This pattern is used to initialize the first half of the 16 bytes block.
     */
    private static final long START_0 = PrimePatterns.P_LONGM_32_S1_118;

    /**
     * This pattern is used to initialize the second half of the 16 bytes block.
     */
    private static final long START_1 = PrimePatterns.P_LONGM_32_S0_004;

    /**
     * first 64 bits of the block
     */
    private long hashPart0 = START_0;

    /**
     * second 64 bits of the block
     */
    private long hashPart1 = START_1;

    /**
     * length of the pending input buffer
     */
    private int bufferLen = 0;

    /**
     * pending input buffer if the appended input data size was not a multiple of 16. This is required because we can call the update method many times, and we
     * do not want to apply padding multiple times.
     */
    private byte[] buffer = new byte[16];

    /**
     * Very monotone input very soon leads to recurring patterns. Top avoid this effect we count the number of bits processed and use this information to vary
     * the input.
     */
    private long inputBitCount = 0;

    /**
     * Process a left-rotation of all 128 bits by 32 bits. We do this because our encoding mechanism only works on 64-bits. The rotation guarantees mixing the
     * bits of the first and the second half of the block.
     */
    private void rotate32() {

        int i00 = BitUtils.getIntAt(hashPart0, 0);
        int i01 = BitUtils.getIntAt(hashPart0, 1);

        int i10 = BitUtils.getIntAt(hashPart1, 0);
        int i11 = BitUtils.getIntAt(hashPart1, 1);

        hashPart0 = BitUtils.composeLong(i11, i00);

        hashPart1 = BitUtils.composeLong(i01, i10);

    }

    /**
     * Prepared this instance for a fresh run
     */
    public void reset() {
        hashPart0 = START_0;
        hashPart1 = START_1;
        bufferLen = 0;
        inputBitCount = 0;
    }

    /**
     * Convenience method, creates a new instance and performs hashing
     * 
     * @param input to be hashed
     * @return 64 bits hash value
     */
    public static final long hash(byte[] input) {
        DistributionCodecHash instance = new DistributionCodecHash();
        instance.update(input);
        return instance.getHashValue();
    }

    /**
     * Finalizes and returns the current hash value.
     * <p/>
     * This method may be called multiple times returning the same result until another update occurs.
     * 
     * @return 64-bits hash value
     */
    public long getHashValue() {

        // backup the internal state to make this method idempotent
        int bufferLenBkp = bufferLen;
        long hashPart0Bkp = hashPart0;
        long hashPart1Bkp = hashPart1;
        long inputBitCountBkp = inputBitCount;
        consumeBuffer();
        long res = DistributionCodec.encode(hashPart0 ^ hashPart1);

        // restore state
        bufferLen = bufferLenBkp;
        hashPart0 = hashPart0Bkp;
        hashPart1 = hashPart1Bkp;
        inputBitCount = inputBitCountBkp;
        return res;
    }

    /**
     * This is the heart of the implementation to process (encode) a single block of 16 bytes.
     */
    private void consumeBuffer() {

        // Important:
        // The slowness results from the fact that it is very hard to induce enough randomness into the block.
        // The biggest problem I found is passing the "Runs Test".

        if (bufferLen > 0) {

            // Here we do the implicit padding with
            // All bytes to be padded (not from the input) we replace with the encoded bytes of their buffer position
            // to avoid monotone padding.
            for (int i = bufferLen; i < 16; i++) {
                buffer[i] = DistributionCodec.encode((byte) i);
            }

            // now we update the bit count based on the first 4 bytes (one integer) from the input
            for (int i = 0; i < bufferLen && i < 4; i++) {
                inputBitCount = inputBitCount + Long.bitCount(Byte.toUnsignedLong(buffer[i]));
            }

            // first integer as a combination of the input bytes and the number of bits processed so far
            int i00 = BitUtils.composeInt(buffer[0], buffer[1], buffer[2], buffer[3]) ^ ((int) inputBitCount);

            // update bit count for the next 4 bytes
            for (int i = 4; i < bufferLen && i < 8; i++) {
                inputBitCount = inputBitCount + Long.bitCount(Byte.toUnsignedLong(buffer[i]));
            }

            // second integer as a combination of the input bytes and the number of bits processed so far
            int i01 = BitUtils.composeInt(buffer[4], buffer[5], buffer[6], buffer[7]) ^ ((int) inputBitCount);

            // update bit count for the next 4 bytes
            for (int i = 8; i < bufferLen && i < 12; i++) {
                inputBitCount = inputBitCount + Long.bitCount(Byte.toUnsignedLong(buffer[i]));
            }

            // third integer as a combination of the input bytes and the number of bits processed so far
            int i10 = BitUtils.composeInt(buffer[8], buffer[9], buffer[10], buffer[11]) ^ ((int) inputBitCount);

            // update bit count for the final 4 bytes
            for (int i = 12; i < bufferLen && i < 16; i++) {
                inputBitCount = inputBitCount + Long.bitCount(Byte.toUnsignedLong(buffer[i]));
            }

            // final integer as a combination of the input bytes and the number of bits processed so far
            int i11 = BitUtils.composeInt(buffer[12], buffer[13], buffer[14], buffer[15]) ^ ((int) inputBitCount);

            // encode the first 64 bits input for merging
            long input0 = DistributionCodec.encode(BitUtils.composeLong(i00, i01));

            // encode the second 64 bits input for merging
            long input1 = DistributionCodec.encode(BitUtils.composeLong(i10, i11));

            // update the internal block with the 128 bits encoded input
            hashPart0 = hashPart0 ^ input0;
            hashPart1 = hashPart1 ^ input1;

            // apply rotation to mix the first and the second half of the block continuously
            rotate32();

            // no pending input
            bufferLen = 0;
        }

    }

    /**
     * Updates the internal state with the given bytes
     * 
     * @param bytes input
     */
    public void update(byte[] bytes) {
        int inputLen = bytes.length;
        int offset = 0;
        while (bufferLen + inputLen >= 16) {
            for (int i = 0; i < 16 - bufferLen; i++) {
                buffer[bufferLen + i] = bytes[offset + i];
            }
            offset = offset + (16 - bufferLen);
            inputLen = inputLen - (16 - bufferLen);
            bufferLen = 16;
            consumeBuffer();
        }
        if (bufferLen + inputLen > 0) {
            for (int i = 0; i < (bytes.length - offset); i++) {
                buffer[bufferLen + i] = bytes[i + offset];
            }
            bufferLen = bufferLen + inputLen;
        }
    }

    /**
     * Updates the internal state with the given byte
     * 
     * @param b input
     */
    public void update(byte b) {
        buffer[bufferLen] = b;
        bufferLen++;
        if (bufferLen == 16) {
            consumeBuffer();
        }
    }

    /**
     * Encodes the value into bytes and updates the internal state
     * 
     * @param s input
     */
    public void update(short s) {

        byte b0 = BitUtils.getByteAt(s, 0);
        byte b1 = BitUtils.getByteAt(s, 1);

        if (bufferLen <= 14) {
            buffer[bufferLen] = b0;
            buffer[bufferLen + 1] = b1;
            bufferLen = bufferLen + 2;
            if (bufferLen == 16) {
                consumeBuffer();
            }
        }
        else {
            update(b0);
            update(b1);
        }

    }

    /**
     * Encodes the value into bytes and updates the internal state
     * 
     * @param i input
     */
    public void update(int i) {
        byte b0 = BitUtils.getByteAt(i, 0);
        byte b1 = BitUtils.getByteAt(i, 1);
        byte b2 = BitUtils.getByteAt(i, 2);
        byte b3 = BitUtils.getByteAt(i, 3);

        if (bufferLen <= 12) {
            buffer[bufferLen] = b0;
            buffer[bufferLen + 1] = b1;
            buffer[bufferLen + 2] = b2;
            buffer[bufferLen + 3] = b3;
            bufferLen = bufferLen + 4;
            if (bufferLen == 16) {
                consumeBuffer();
            }
        }
        else {
            update(b0);
            update(b1);
            update(b2);
            update(b3);
        }

    }

    /**
     * Encodes the value into bytes and updates the internal state
     * 
     * @param l input
     */
    public void update(long l) {
        byte b0 = BitUtils.getByteAt(l, 0);
        byte b1 = BitUtils.getByteAt(l, 1);
        byte b2 = BitUtils.getByteAt(l, 2);
        byte b3 = BitUtils.getByteAt(l, 3);
        byte b4 = BitUtils.getByteAt(l, 4);
        byte b5 = BitUtils.getByteAt(l, 5);
        byte b6 = BitUtils.getByteAt(l, 6);
        byte b7 = BitUtils.getByteAt(l, 7);

        if (bufferLen <= 8) {
            buffer[bufferLen] = b0;
            buffer[bufferLen + 1] = b1;
            buffer[bufferLen + 2] = b2;
            buffer[bufferLen + 3] = b3;
            buffer[bufferLen + 4] = b4;
            buffer[bufferLen + 5] = b5;
            buffer[bufferLen + 6] = b6;
            buffer[bufferLen + 7] = b7;
            bufferLen = bufferLen + 8;
            if (bufferLen == 16) {
                consumeBuffer();
            }
        }
        else {
            update(b0);
            update(b1);
            update(b2);
            update(b3);
            update(b4);
            update(b5);
            update(b6);
            update(b7);
        }

    }

    /**
     * Creates a new stateful instance.
     */
    public DistributionCodecHash() {
        // fresh instance
    }

}
