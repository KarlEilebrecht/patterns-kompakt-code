//@formatter:off
/*
 * SecureDistributionCodec
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

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import de.calamanari.pk.drhe.util.BitUtils;

/**
 * This implementation uses the previously created classes {@link DistributionCodec}, {@link DistributionCodecRandomGenerator} and {@link DistributionCodecHashBuilder}
 * to implement a secure symmetric encryption.
 * <p/>
 * Be aware that this is solely meant for demonstration purposes.
 * <p/>
 * Instances are stateful and must not be accessed concurrently by multiple threads.
 * <p/>
 * At <a href="https://mzsoltmolnar.github.io/random-bitstream-tester/">https://mzsoltmolnar.github.io/random-bitstream-tester/</a> you can check generated
 * output for randomness based on a <a href="https://csrc.nist.gov/Projects/Random-Bit-Generation/Documentation-and-Software">NIST test suite</a>.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class SecureDistributionCodec {

    /**
     * To easily detect that we cannot decode data, we encode a MAGIC byte sequence into the header. When we don't see this value during decoding we know the
     * data is either corrupt or the given password is wrong.
     */
    private static final int MAGIC = 0xDCDEC;

    /**
     * @param input to be encoded
     * @param password for encoding
     * @return encoded data
     */
    public static byte[] encode(byte[] input, String password) {
        long passcode = DistributionCodecHashBuilder.hash(password.getBytes(StandardCharsets.UTF_8));
        SecureDistributionEncoder encoder = new SecureDistributionEncoder(passcode, input);
        return encoder.encode();
    }

    /**
     * @param input to be decoded
     * @param password for decoding
     * @return decoded data
     */
    public static byte[] decode(byte[] input, String password) {
        long passcode = DistributionCodecHashBuilder.hash(password.getBytes(StandardCharsets.UTF_8));
        SecureDistributionDecoder decoder = new SecureDistributionDecoder(passcode, input);
        return decoder.decode();
    }

    /**
     * Returns the encoded data as a Base-64-encoded String
     * 
     * @param input to be encoded
     * @param password for encoding
     * @return base-64 String
     */
    public static String encodeBase64(byte[] input, String password) {
        return Base64.getEncoder().encodeToString(encode(input, password));
    }

    /**
     * Decodes the base-64 encoded version of the encoded data
     * 
     * @param input to be decoded
     * @param password for decoding
     * @return original content
     */
    public static byte[] decodeBase64(String input, String password) {
        return decode(Base64.getDecoder().decode(input), password);
    }

    private SecureDistributionCodec() {
        // no instances
    }

    /**
     * Class responsible for encoding
     */
    private static final class SecureDistributionEncoder {

        /**
         * We use the random generator to create a pseudo-random pattern for encoding
         */
        private final DistributionCodecRandomGenerator rand;

        /**
         * original content
         */
        private final byte[] content;

        /**
         * encoded data
         */
        private final byte[] output;

        /**
         * hash of the original content, we use this as a salt for the passcode, this ensures that different content encrypted with the same password won't show
         * similarities due to different one-time-pads.
         */
        private final long hash;

        /**
         * marks the output position
         */
        private int position = 0;

        /**
         * @param passcode for encoding
         * @param input content to be encrypted, not null
         */
        private SecureDistributionEncoder(long passcode, byte[] input) {

            if (input == null) {
                throw new IllegalArgumentException("input array must not be null");
            }

            int outputLen = (input.length / 8) * 8;
            if (input.length % 8 > 0) {
                outputLen = outputLen + 8;
            }

            // magic and length information prefix
            outputLen = outputLen + 8;

            // hash-prefix
            outputLen = outputLen + 8;

            this.hash = input.length == 0 ? 0 : DistributionCodecHashBuilder.hash(input);
            this.content = input;
            this.rand = new DistributionCodecRandomGenerator(passcode ^ hash);
            this.output = new byte[outputLen];
        }

        /**
         * The 128-bits header consists of 3 information fields
         * <ul>
         * <li>content hash, 64 bits, not encrypted, serves as a salt for the following encryption</li>
         * <li>{@link SecureDistributionCodec#MAGIC} (encrypted, for validation), 32 bits.</li>
         * <li>Length of the original content (encrypted), 32 bits.</li>
         * </ul>
         */
        private void writeHeader() {

            writeChunk(hash);

            byte b0 = DistributionCodec.encode(BitUtils.getByteAt(MAGIC, 0));
            byte b1 = DistributionCodec.encode(BitUtils.getByteAt(MAGIC, 1));
            byte b2 = DistributionCodec.encode(BitUtils.getByteAt(MAGIC, 2));
            byte b3 = DistributionCodec.encode(BitUtils.getByteAt(MAGIC, 3));
            byte b4 = DistributionCodec.encode(BitUtils.getByteAt(content.length, 0));
            byte b5 = DistributionCodec.encode(BitUtils.getByteAt(content.length, 1));
            byte b6 = DistributionCodec.encode(BitUtils.getByteAt(content.length, 2));
            byte b7 = DistributionCodec.encode(BitUtils.getByteAt(content.length, 3));

            writeEncodedChunk(BitUtils.composeLong(b0, b1, b2, b3, b4, b5, b6, b7));

        }

        /**
         * @param input chunk to be encoded and written to the output buffer
         */
        private void writeEncodedChunk(long input) {
            writeChunk(input ^ rand.nextValue());
        }

        /**
         * @param input chunk to be written to the output buffer
         */
        private void writeChunk(long input) {

            output[position] = BitUtils.getByteAt(input, 0);
            output[position + 1] = BitUtils.getByteAt(input, 1);
            output[position + 2] = BitUtils.getByteAt(input, 2);
            output[position + 3] = BitUtils.getByteAt(input, 3);
            output[position + 4] = BitUtils.getByteAt(input, 4);
            output[position + 5] = BitUtils.getByteAt(input, 5);
            output[position + 6] = BitUtils.getByteAt(input, 6);
            output[position + 7] = BitUtils.getByteAt(input, 7);

            position = position + 8;
        }

        /**
         * Takes the chunk at the given position in the input, encrypts 4 bytes, and writes them to the output buffer
         * 
         * @param offset where to take the next 4 bytes
         */
        private void processChunk(int offset) {
            long value = 0;
            for (int i = offset; i < content.length && (i - offset) < 8; i++) {
                value = BitUtils.setByteAt(value, DistributionCodec.encode(content[i]), i - offset);
            }
            writeEncodedChunk(value);
        }

        /**
         * @return encoded data
         */
        byte[] encode() {

            writeHeader();
            int offset = 0;
            while (position < output.length) {
                processChunk(offset);
                offset = offset + 8;
            }
            return output;
        }

    }

    /**
     * Class responsible for decoding
     */
    private static final class SecureDistributionDecoder {

        /**
         * We use the random generator to create a pseudo-random pattern for decoding
         */
        private final DistributionCodecRandomGenerator rand;

        /**
         * data to be decoded
         */
        private final byte[] input;

        /**
         * restored data
         */
        private final byte[] content;

        /**
         * position in the output
         */
        private int position = 0;

        /**
         * @param passcode for decoding
         * @param input to be decoded
         */
        private SecureDistributionDecoder(long passcode, byte[] input) {
            if (input == null || input.length < 2 || input.length % 8 > 0) {
                throw new DecodingException("Unrecognized input format.");
            }
            this.input = input;
            long hash = readChunk();
            this.rand = new DistributionCodecRandomGenerator(passcode ^ hash);
            this.content = new byte[readContentLength()];
        }

        /**
         * Reads the rest of the header to determine the original content length.
         * 
         * @return original content length
         */
        private int readContentLength() {

            long value = readDecodedChunk();

            byte b0 = DistributionCodec.decode(BitUtils.getByteAt(value, 0));
            byte b1 = DistributionCodec.decode(BitUtils.getByteAt(value, 1));
            byte b2 = DistributionCodec.decode(BitUtils.getByteAt(value, 2));
            byte b3 = DistributionCodec.decode(BitUtils.getByteAt(value, 3));
            byte b4 = DistributionCodec.decode(BitUtils.getByteAt(value, 4));
            byte b5 = DistributionCodec.decode(BitUtils.getByteAt(value, 5));
            byte b6 = DistributionCodec.decode(BitUtils.getByteAt(value, 6));
            byte b7 = DistributionCodec.decode(BitUtils.getByteAt(value, 7));

            if (BitUtils.composeInt(b0, b1, b2, b3) != MAGIC) {
                throw new DecodingException("Wrong password!");
            }

            int res = BitUtils.composeInt(b4, b5, b6, b7);

            if (res < 0) {
                throw new DecodingException("Input corrupted.");
            }
            return res;
        }

        /**
         * @return nexted decrypted chunk
         */
        private long readDecodedChunk() {
            return readChunk() ^ rand.nextValue();
        }

        /**
         * @return next 4-bytes chunk from the data to be decoded
         */
        private long readChunk() {

            byte b0 = input[position];
            byte b1 = input[position + 1];
            byte b2 = input[position + 2];
            byte b3 = input[position + 3];
            byte b4 = input[position + 4];
            byte b5 = input[position + 5];
            byte b6 = input[position + 6];
            byte b7 = input[position + 7];

            position = position + 8;

            return BitUtils.composeLong(b0, b1, b2, b3, b4, b5, b6, b7);
        }

        /**
         * Takes the next 4 bytes from the input and writes the decoded version to the output
         * 
         * @param offset where to read the next chunk
         */
        private void processChunk(int offset) {
            long raw = readDecodedChunk();

            for (int i = 0; i < 8 && (offset + i) < content.length; i++) {
                content[offset + i] = DistributionCodec.decode(BitUtils.getByteAt(raw, i));
            }
        }

        /**
         * @return decoded original content
         */
        byte[] decode() {
            int offset = 0;
            while (position < input.length) {
                processChunk(offset);
                offset = offset + 8;
            }
            return content;
        }

    }

    /**
     * Exception to inform the caller about problems during decoding
     */
    public static class DecodingException extends RuntimeException {

        private static final long serialVersionUID = 5969905535929814280L;

        public DecodingException(String message, Throwable cause) {
            super(message, cause);
        }

        public DecodingException(String message) {
            super(message);
        }

    }

}