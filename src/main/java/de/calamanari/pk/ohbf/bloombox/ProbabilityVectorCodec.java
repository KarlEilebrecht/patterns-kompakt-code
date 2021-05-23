//@formatter:off
/*
 * ProbabilityVectorCodec
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
package de.calamanari.pk.ohbf.bloombox;

import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

/**
 * The {@link ProbabilityVectorCodec} encodes and compresses a data point probability vector (long array) into a byte array.
 * <p>
 * Instances are safe to be used by multiple threads concurrently.
 * 
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class ProbabilityVectorCodec implements Serializable {

    private static final long serialVersionUID = -8203709991440990978L;

    /**
     * SINGLETON
     */
    private static final ProbabilityVectorCodec DEFAULT_CODEC = new ProbabilityVectorCodec();

    /**
     * deflater for compression
     */
    protected final transient ThreadLocal<Deflater> deflaterHolder = ThreadLocal.withInitial(() -> new Deflater(Deflater.BEST_COMPRESSION, true));

    /**
     * inflater for decompression
     */
    protected final transient ThreadLocal<Inflater> inflaterHolder = ThreadLocal.withInitial(() -> new Inflater(true));

    protected ProbabilityVectorCodec() {
        // nothing to do here
    }

    /**
     * @return SINGLETON instance
     */
    public static ProbabilityVectorCodec getInstance() {
        return DEFAULT_CODEC;
    }

    /**
     * This method should be called if the invoking thread knows the instance is no longer required.
     * <p>
     * Afterwards the instance can still be used by the invoking thread, but recreating internal structures will cause a certain overhead.
     */
    public void cleanUp() {
        deflaterHolder.get().end();
        deflaterHolder.remove();
        inflaterHolder.get().end();
        inflaterHolder.remove();
    }

    /**
     * @return de-serialized box
     */
    Object readResolve() {
        return DEFAULT_CODEC;
    }

    /**
     * Encodes the given long array into a byte array
     * 
     * @param src long vector
     * @param inputStartIdx where to start reading
     * @param srcLen number of longs to read from the input
     * @param dest buffer, capacity (after start index) must be at least 8 x input length
     * @param outputStartIdx where to start writing to output
     */
    protected static void longArrayToBytes(long[] src, int srcPos, int srcLen, byte[] dest, int destPos) {
        for (int vPos = 0; vPos < srcLen; vPos++) {
            long value = src[srcPos + vPos];
            int offset = destPos + (vPos * 8);
            for (int i = 7; i >= 0; i--) {
                dest[offset + i] = (byte) (value & 0xFF);
                value >>= 8;
            }
        }
    }

    /**
     * Encodes the given long array into a byte array
     * 
     * @param src long values
     * @return byte array size is 8 x size of src
     */
    protected static byte[] longArrayToBytes(long[] src) {
        byte[] res = new byte[src.length * 8];
        longArrayToBytes(src, 0, src.length, res, 0);
        return res;
    }

    /**
     * Restores a long array from the byte array
     * 
     * @param src encoded data
     * @param srcPos where to start reading bytes
     * @param srcLen number of bytes to read, must be a multiple of 8 (!)
     * @param dest for writing the result, capacity after start must be at least inputLength/8
     * @param destPos where to start writing
     */
    protected static void bytesToLongArray(byte[] src, int srcPos, int srcLen, long[] dest, int destPos) {
        int outputLength = srcLen / 8;
        for (int vPos = 0; vPos < outputLength; vPos++) {
            int offset = srcPos + (vPos * 8);
            long value = 0;
            for (int i = 0; i < 8; i++) {
                value <<= 8;
                value |= (src[offset + i] & 0xFF);
            }
            dest[destPos + vPos] = value;
        }
    }

    /**
     * Restores a long array from bytes
     * 
     * @param bytes encoded data
     * @return long array (size / 8)
     */
    protected static long[] bytesToLongArray(byte[] bytes) {
        long[] res = new long[bytes.length / 8];
        bytesToLongArray(bytes, 0, bytes.length, res, 0);
        return res;
    }

    /**
     * This method compresses the given byte array into output, the size is worst-case 4 bytes longer than the input.
     * 
     * @param src source bytes
     * @param srcPos where to start reading
     * @param srcLen number of bytes to compress
     * @param dest destination capacity after start must be at least srcLen + 8
     * @param destPos where to start writing
     * @return number of compressed bytes written
     */
    protected int compress(byte[] src, int srcPos, int srcLen, byte[] dest, int destPos) {

        int res = 0;
        int capacity = dest.length - destPos - 8;

        if (capacity < srcLen) {
            capacity = 0;
        }

        int encodedPartLength = 0;

        if (capacity > 0) {
            Deflater deflater = deflaterHolder.get();
            deflater.reset();

            deflater.setInput(src, srcPos, srcLen);
            deflater.finish();

            res = deflater.deflate(dest, destPos + 8, capacity, Deflater.FULL_FLUSH) + 8;

            encodedPartLength = res - 4;
        }
        if (res == 0 || res >= srcLen) {
            // compression does not make sense, use raw data
            System.arraycopy(src, srcPos, dest, destPos + 4, srcLen);
            res = srcLen + 4;
            encodedPartLength = srcLen * -1;
        }
        else {
            // when compressing, then encode original length
            writeInt(srcLen, dest, destPos + 4);
        }

        // write the length of the compressed or uncompressed data following the length field
        writeInt(encodedPartLength, dest, destPos);
        return res;

    }

    /**
     * This method uncompresses the given byte array into output.
     * 
     * @param src compressed source bytes
     * @param srcPos where to start reading
     * @param dest destination ensure the buffer has enough capacity
     * @param destPos where to start writing
     * @return number of uncompressed bytes written
     */
    protected int uncompress(byte[] src, int srcPos, byte[] dest, int destPos) {
        int res = 0;
        int encodedPartLength = readInt(src, srcPos);

        if (encodedPartLength == 0) {
            // empty, nothing to do
        }
        else if (encodedPartLength < 0) {
            // uncompressed
            encodedPartLength = encodedPartLength * -1;
            System.arraycopy(src, srcPos + 4, dest, destPos, encodedPartLength);
            res = encodedPartLength;
        }
        else {
            Inflater inflater = inflaterHolder.get();
            inflater.reset();
            inflater.setInput(src, srcPos + 8, encodedPartLength - 4);
            try {
                res = inflater.inflate(dest, destPos, dest.length - destPos);
            }
            catch (DataFormatException ex) {
                throw new BloomBoxException("Problem inflating compressed data", ex);
            }
        }
        return res;
    }

    /**
     * Determines the original byte array length from the compressed data
     * 
     * @param src compressed bytes
     * @param srcPos where to start reading
     * @return original array length
     */
    protected int readOriginalLength(byte[] src, int srcPos) {
        int length = readInt(src, srcPos);
        if (length <= 0) {
            // uncompressed
            length = length * -1;
        }
        else {
            length = readInt(src, srcPos + 4);
        }
        return length;
    }

    /**
     * Reads a 4-bytes integer value (e.g. length)
     * 
     * @param src source bytes
     * @param srcPos where to start reading
     * @return value
     */
    public static int readInt(byte[] src, int srcPos) {
        int res = 0;
        for (int i = 0; i < 4; i++) {
            res <<= 8;
            res |= (src[srcPos + i] & 0xFF);
        }
        return res;
    }

    /**
     * Writes the value as a 4-bytes integer to the destination
     * 
     * @param value to write
     * @param dest target
     * @param destPos where to start writing
     */
    public static void writeInt(int value, byte[] dest, int destPos) {
        for (int i = 3; i >= 0; i--) {
            dest[destPos + i] = (byte) (value & 0xFF);
            value >>= 8;
        }
    }

    /**
     * We encode the data point id with its probability (subsequent bits), to that the id comes first and order will be preserved.
     * 
     * @param dataPointId key/value identifier
     * @param probability float precision
     * @return long
     */
    public long encodeDataPointProbability(int dataPointId, float probability) {
        long res = dataPointId;
        res = (res << 32L) + Float.floatToRawIntBits(probability);
        return res;
    }

    /**
     * Returns the encoded proability of this data point
     * 
     * @param dpp data point with probability
     * @return probability
     */
    public float decodeDataPointProbability(long dpp) {
        return Float.intBitsToFloat((int) ((dpp << 32L) >>> 32L));
    }

    /**
     * Creates a (naturally) ordered vector of positive long values.
     * <p>
     * The dataPointId is encoded in the first 32 bits and can be easily determined (compared) by doing a right-shift by 32 bits. This way the order of the
     * returned array of DPPs reflects the natural order of the underlying dataPointIds.
     * 
     * @param dpProbabilities maps dataPointId to probability
     * @return sorted array of DPPs
     */
    public long[] encodeDataPointProbabilities(Map<Integer, Float> dpProbabilities) {
        long[] res = new long[dpProbabilities.size()];
        int idx = 0;
        for (Map.Entry<Integer, Float> entry : dpProbabilities.entrySet()) {
            res[idx] = encodeDataPointProbability(entry.getKey(), entry.getValue());
            idx++;
        }
        Arrays.sort(res);
        return res;
    }

    /**
     * Decodes the given dpps array into a map
     * 
     * @param dpps data proint probabilities
     * @return map dataPointId to probability
     */
    public Map<Integer, Float> decodeDataPointProbabilities(long[] dpps) {
        Map<Integer, Float> res = new HashMap<>(dpps.length);
        for (long dpp : dpps) {
            int dataPointId = (int) (dpp >>> 32L);
            float probability = decodeDataPointProbability(dpp);
            res.put(dataPointId, probability);
        }
        return res;
    }

    /**
     * Encodes the given data point probability vector and writes the compressed bytes
     * 
     * @param vector input
     * @param dest target (size should be (vector size * 8) + 8 to be safe)
     * @param destPos where to start writing bytes
     * @return number of bytes written
     */
    public int encode(long[] vector, byte[] dest, int destPos) {
        byte[] encodedUncompressed = longArrayToBytes(vector);
        return compress(encodedUncompressed, 0, encodedUncompressed.length, dest, destPos);
    }

    /**
     * Encodes the given data point probability vector
     * 
     * @param vector input
     * @return a byte array, max length (vector size * 8) + 8 (depends on compression success)
     */
    public byte[] encode(long[] vector) {
        byte[] buffer = new byte[(vector.length * 8) + 8];
        int len = encode(vector, buffer, 0);
        return Arrays.copyOf(buffer, len);
    }

    /**
     * Decodes the data point probability vector
     * 
     * @param src encoded bytes
     * @param srcPos where to start reading
     * @return decoded long vector with DPPs
     */
    public long[] decode(byte[] src, int srcPos) {
        byte[] encodedUncompressed = new byte[readOriginalLength(src, srcPos)];
        uncompress(src, srcPos, encodedUncompressed, 0);
        return bytesToLongArray(encodedUncompressed);
    }

    /**
     * Decodes the data point probability vector
     * 
     * @param src encoded bytes
     * @return decoded long vector with DPPs
     */
    public long[] decode(byte[] src) {
        return decode(src, 0);
    }

}
