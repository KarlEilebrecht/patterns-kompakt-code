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
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

/**
 * The {@link ProbabilityVectorCodec} encodes and compresses a probability vector (float array) into a byte array.
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
     * Encodes the given float array into a byte array, so the source value can be restored by {@link #bytesToFloatArray(byte[])}
     * 
     * @param vector floats
     * @return byte array
     */
    protected static byte[] floatArrayToBytes(float[] vector) {
        byte[] rawBytes = new byte[vector.length * 4];
        floatArrayToBytes(vector, 0, vector.length, rawBytes, 0);
        return rawBytes;
    }

    /**
     * Encodes the given float array into a byte array, so the source value can be restored by {@link #bytesToFloatArray(byte[])}
     * 
     * @param src float vector
     * @param inputStartIdx where to start reading
     * @param srcLen number of floats to read from the input
     * @param dest buffer, capacity (after start index) must be at least 4 x input length
     * @param outputStartIdx where to start writing to output
     */
    protected static void floatArrayToBytes(float[] src, int srcPos, int srcLen, byte[] dest, int destPos) {
        for (int vPos = 0; vPos < srcLen; vPos++) {
            writeInt(Float.floatToRawIntBits(src[srcPos + vPos]), dest, destPos + (vPos * 4));
        }
    }

    /**
     * Restores a float array previously encoded by {@link #floatArrayToBytes(float[])}
     * 
     * @param bytes encoded data
     * @return float array
     */
    protected static float[] bytesToFloatArray(byte[] bytes) {
        float[] vector = new float[bytes.length / 4];
        bytesToFloatArray(bytes, 0, bytes.length, vector, 0);
        return vector;
    }

    /**
     * Restores a float array previously encoded by {@link #floatArrayToBytes(float[])}
     * 
     * @param src encoded data
     * @param srcPos where to start reading bytes
     * @param srcLen number of bytes to read, must be a multiple of 4 (!)
     * @param dest for writing the result, capacity after start must be at least inputLength/4
     * @param destPos where to start writing
     */
    protected static void bytesToFloatArray(byte[] src, int srcPos, int srcLen, float[] dest, int destPos) {
        int outputLength = srcLen / 4;
        for (int vPos = 0; vPos < outputLength; vPos++) {
            dest[destPos + vPos] = Float.intBitsToFloat(readInt(src, srcPos + (vPos * 4)));
        }
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
     * Encodes the given vector and writes the compressed bytes
     * 
     * @param vector input
     * @param dest target (size should be (vector size * 4) + 4 to be safe)
     * @param destPos where to start writing bytes
     * @return number of bytes written
     */
    public int encode(float[] vector, byte[] dest, int destPos) {
        byte[] encodedUncompressed = floatArrayToBytes(vector);
        return compress(encodedUncompressed, 0, encodedUncompressed.length, dest, destPos);
    }

    /**
     * Encodes the given vector
     * 
     * @param vector input
     * @return a byte array, max length (vector size * 4) + 4 (depends on compression success)
     */
    public byte[] encode(float[] vector) {
        byte[] buffer = new byte[(vector.length * 4) + 8];
        int len = encode(vector, buffer, 0);
        return Arrays.copyOf(buffer, len);
    }

    /**
     * Decodes the vector previously encoded by {@link #encode(float[])}
     * 
     * @param src encoded bytes
     * @param srcPos where to start reading
     * @return decoded float vector
     */
    public float[] decode(byte[] src, int srcPos) {
        byte[] encodedUncompressed = new byte[readOriginalLength(src, srcPos)];
        uncompress(src, srcPos, encodedUncompressed, 0);
        return bytesToFloatArray(encodedUncompressed);
    }

    /**
     * Decodes the vector previously encoded by {@link #encode(float[])}
     * 
     * @param src encoded bytes
     * @return decoded float vector
     */
    public float[] decode(byte[] src) {
        return decode(src, 0);
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
}
