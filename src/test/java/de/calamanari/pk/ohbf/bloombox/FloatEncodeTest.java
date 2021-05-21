package de.calamanari.pk.ohbf.bloombox;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import org.junit.Ignore;
import org.junit.Test;

public class FloatEncodeTest {

    @Ignore
    @Test
    public void testFloat() {
        for (long l = 0; l <= Integer.MAX_VALUE; l++) {
            int i = (int) l;
            float f = Float.intBitsToFloat(i);
            System.out.println("" + i + " --> " + f);

            long conv = i & 0x00000000ffffffffL;

            System.out.println("!" + conv + " --> " + Double.longBitsToDouble(conv));

        }
    }

    @Test
    public void testEncodeDecodeFloats() {

        float[] source = new float[] {};

        float[] target = decodeFloats(encodeFloats(source));

        assertEquals(Arrays.toString(source), Arrays.toString(target));

        source = new float[] { 0.0f };
        target = decodeFloats(encodeFloats(source));

        assertEquals(Arrays.toString(source), Arrays.toString(target));

        source = new float[] { 0.9010f, 1.98f, 0.9999f, 0.0002f };
        target = decodeFloats(encodeFloats(source));

        assertEquals(Arrays.toString(source), Arrays.toString(target));

    }

    @Test
    public void testCompressUncompress() {
        float[] source = new float[] {};

        assertEquals(Arrays.toString(source), Arrays.toString(compressUncompress(source)));

        source = new float[] { 0.0f };

        assertEquals(Arrays.toString(source), Arrays.toString(compressUncompress(source)));

        source = new float[] { 0.9010f, 1.98f, 0.9999f, 0.0002f, 0.0f, 0.0f, 0.9010f, 1.98f, 0.9999f, 0.0002f, 0.0f };

        assertEquals(Arrays.toString(source), Arrays.toString(compressUncompress(source)));

    }

    private float[] compressUncompress(float[] source) {

        Deflater deflater = new Deflater(Deflater.BEST_COMPRESSION, true);

        byte[] encoded = encodeFloats(source);

        byte[] compressed = new byte[encoded.length + 4];

        compress2(deflater, encoded, compressed);

        byte[] uncompressed = new byte[encoded.length];

        Inflater inflater = new Inflater(true);

        uncompress2(inflater, compressed, uncompressed);

        return decodeFloats(uncompressed);

    }

    private byte[] encodeFloats(float[] vector) {

        byte[] rawBytes = new byte[vector.length * 4];

        for (int vPos = 0; vPos < vector.length; vPos++) {
            int offset = vPos * 4;

            int value = Float.floatToIntBits(vector[vPos]);

            for (int i = 3; i >= 0; i--) {
                rawBytes[offset + i] = (byte) (value & 0xFF);
                value >>= 8;
            }
        }

        return rawBytes;
    }

    private float[] decodeFloats(byte[] bytes) {
        float[] vector = new float[bytes.length / 4];

        for (int vPos = 0; vPos < vector.length; vPos++) {

            int offset = vPos * 4;

            int value = 0;
            for (int i = 0; i < 4; i++) {
                value <<= 8;
                value |= (bytes[offset + i] & 0xFF);
            }

            vector[vPos] = Float.intBitsToFloat(value);
        }

        return vector;
    }

    /**
     * Compresses the given input if the result would be smaller. Worst case the output will be 1 byte longer than the input.
     * <p>
     * The first byte is 0 to indicate uncompressed data, a 1 indicates compressed data.
     * 
     * @param deflater will be reset to be used by this method (each compression separately)
     * @param input to be compressed
     * @param output must be at least 4 bytes bigger than input
     * @return length of the output
     */
    private int compress(Deflater deflater, byte[] input, byte[] output) {
        // Deflater deflater = new Deflater(Deflater.BEST_COMPRESSION, true);

        if (output.length < input.length + 1) {
            throw new IllegalArgumentException(
                    String.format("The output buffer must be at least 1 byte bigger than the input[%d], given output[%d]", input.length, output.length));
        }

        deflater.reset();

        deflater.setInput(input, 0, input.length);
        deflater.finish();
        int res = deflater.deflate(output, 1, output.length - 1, Deflater.FULL_FLUSH) + 1;

        if (res < input.length) {
            // success, mark compressed
            output[0] = 1;
        }
        else {
            // compression does not make sense, use raw data
            System.arraycopy(input, 0, output, 1, input.length);
            res = input.length + 1;
        }
        System.out.println(Arrays.toString(output) + " --> " + res + " / " + output.length);
        return res;

    }

    private int uncompress(Inflater inflater, byte[] input, byte[] output) {

        int res = 0;

        if (input[0] == 0) {
            // uncompressed
            System.arraycopy(input, 1, output, 0, input.length - 1);
            res = input.length - 1;
        }
        else {
            inflater.reset();
            inflater.setInput(input, 1, input.length - 1);
            try {
                res = inflater.inflate(output);
            }
            catch (DataFormatException ex) {
                throw new BloomBoxException("Problem inflating", ex);
            }
        }
        return res;
    }

    private int compress2(Deflater deflater, byte[] input, byte[] output) {
        // Deflater deflater = new Deflater(Deflater.BEST_COMPRESSION, true);

        if (output.length < input.length + 4) {
            throw new IllegalArgumentException(
                    String.format("The output buffer must be at least 4 bytes bigger than the input[%d], given output[%d]", input.length, output.length));
        }

        deflater.reset();

        deflater.setInput(input, 0, input.length);
        deflater.finish();
        int res = deflater.deflate(output, 4, output.length - 4, Deflater.FULL_FLUSH) + 4;

        int length = res - 4;
        if (res >= input.length) {
            // compression does not make sense, use raw data
            System.arraycopy(input, 0, output, 4, input.length);
            res = input.length + 4;
            length = input.length * -1;
        }

        for (int i = 3; i >= 0; i--) {
            output[i] = (byte) (length & 0xFF);
            length >>= 8;
        }

        System.out.println(Arrays.toString(output) + " --> " + res + " / " + output.length);
        return res;

    }

    private int uncompress2(Inflater inflater, byte[] input, byte[] output) {

        int res = 0;

        int length = 0;
        for (int i = 0; i < 4; i++) {
            length <<= 8;
            length |= (input[i] & 0xFF);
        }

        if (length < 0) {
            // uncompressed
            length = length * -1;
            System.arraycopy(input, 4, output, 0, length);
            res = length;
        }
        else {
            inflater.reset();
            inflater.setInput(input, 4, length);
            try {
                res = inflater.inflate(output);
            }
            catch (DataFormatException ex) {
                throw new BloomBoxException("Problem inflating", ex);
            }
        }
        return res;
    }

}
