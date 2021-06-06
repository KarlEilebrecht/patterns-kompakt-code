package de.calamanari.pk.ohbf.bloombox;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.TreeMap;
import java.util.UUID;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

import org.junit.Ignore;
import org.junit.Test;

import de.calamanari.pk.muhai.MuhaiUtils;
import de.calamanari.pk.ohbf.bloombox.bbq.ExpressionIdUtil;

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

    @Test
    public void testCompare() {

        Map<Long, Integer> map = new HashMap<>();

        map.put(7L, 1928);
        map.put(9L, 6);
        map.put(9222L, 2);
        map.put(733L, 10000);

        System.out.println(map.toString());

        List<Map.Entry<Long, Integer>> entries = new ArrayList<>(map.entrySet());
        System.out.println(entries.toString());

        Collections.sort(entries, (e1, e2) -> e1.getValue().compareTo(e2.getValue()));

        System.out.println(entries.toString());
    }

    @Test
    public void testDataPointId() {

        for (int i = 0; i < 100; i++) {
            int lpDataPointId = ExpressionIdUtil.createLpDataPointId(UUID.randomUUID().toString(), UUID.randomUUID().toString());

            System.out.println("" + lpDataPointId + " ----> " + MuhaiUtils.toPaddedBinaryString(lpDataPointId));
        }

    }

    @Test
    public void testDataPointProbabilityMap() {

        Map<Integer, Double> sourceMap = new TreeMap<>();

        long[] dpps = ProbabilityVectorCodec.createDataPointProbabilityVector(sourceMap);

        assertEquals(0, dpps.length);

        Map<Integer, Double> targetMap = new TreeMap<>(ProbabilityVectorCodec.dataPointProbabilityVectorToMap(dpps));

        assertEquals(sourceMap.toString(), targetMap.toString());

        sourceMap.put(8273, 1.0d);

        dpps = ProbabilityVectorCodec.createDataPointProbabilityVector(sourceMap);

        assertEquals(1, dpps.length);

        targetMap = new TreeMap<>(ProbabilityVectorCodec.dataPointProbabilityVectorToMap(dpps));

        assertEquals(sourceMap.toString(), targetMap.toString());

        sourceMap.put(23944, 1.0d);
        sourceMap.put(1, 0.0005d);
        sourceMap.put(2, 0.9999d);
        sourceMap.put(3, 0.9999d);
        sourceMap.put(4, 0.9999d);

        dpps = ProbabilityVectorCodec.createDataPointProbabilityVector(sourceMap);

        Arrays.stream(dpps).forEach(v -> System.out.println(MuhaiUtils.toPaddedBinaryString(v)));

        assertEquals(6, dpps.length);

        targetMap = new TreeMap<>(ProbabilityVectorCodec.dataPointProbabilityVectorToMap(dpps));

        assertEquals(sourceMap.toString(), targetMap.toString());

    }

    @Test
    public void testLongVectorEncoding() {
        Random rand = new Random(29477);

        for (int i = 0; i < 1000; i++) {

            int count = rand.nextInt(25000) + 1;

            long[] vector = new long[count];

            for (int j = 0; j < count; j++) {
                String argName = "col" + i;
                String argValue = "val" + rand.nextInt(2);

                float probability = 0.0f; // rand.nextFloat();

                int lpDataPointId = ExpressionIdUtil.createLpDataPointId(argName, argValue);

                vector[j] = ProbabilityVectorCodec.encodeDataPointProbability(lpDataPointId, probability);

                assertEquals(lpDataPointId, ProbabilityVectorCodec.decodeLpDataPointId(vector[j]));
                assertEquals(probability, ProbabilityVectorCodec.decodeDataPointProbability(vector[j]), 0.00000001d);

            }

            System.out.println("vector size: " + vector.length);

            byte[] bytes = ProbabilityVectorCodec.getInstance().encode(vector);

            System.out.println("encoded size: " + bytes.length);

            long[] vector2 = ProbabilityVectorCodec.getInstance().decode(bytes);

            System.out.println("decoded vector size: " + vector2.length);

            assertArrayEquals(vector, vector2);

        }

        long[] empty = new long[0];

        byte[] encoded = ProbabilityVectorCodec.getInstance().encode(empty);

        System.out.println(Arrays.toString(encoded));

        long[] empty2 = ProbabilityVectorCodec.getInstance().decode(encoded);

        assertArrayEquals(empty, empty2);

    }

    @Test
    public void testReverseBits() {

        for (long l = 0; l < 100; l++) {
            int i = (int) l;

            long val = i;
            System.out.println(MuhaiUtils.toPaddedBinaryString(i));
            val = Long.reverse(i) >>> 32L;
            System.out.println(MuhaiUtils.toPaddedBinaryString(val));
            System.out.println();

        }

        long l = Long.parseLong("11111111111111111111111111111111", 2);

        System.out.println(MuhaiUtils.toPaddedBinaryString(l));

    }

    @Test
    public void testEncodeDecode() {
        ProbabilityVectorCodec codec = ProbabilityVectorCodec.getInstance();

        long encoded = codec.encodeLpDataPointId(1);

        System.out.println(MuhaiUtils.toPaddedBinaryString(encoded));

        System.out.println(codec.decodeLpDataPointId(encoded));

        // for (int i = 0; i < 1000; i++) {
        // System.out.println(MuhaiUtils.toPaddedBinaryString(codec.encodeDataPointId(i)));
        //
        // }

        // for (long l = Integer.MIN_VALUE; l <= Integer.MAX_VALUE; l++) {
        // if (codec.decodeDataPointId(codec.encodeDataPointId((int) l)) != l) {
        // throw new RuntimeException();
        // }
        // }

        Random rand = new Random(7919);

        for (int i = 0; i < ExpressionIdUtil.MIN_GENERATED_DATA_POINT_ID; i++) {
            double probability = rand.nextDouble();

            long dpp = ProbabilityVectorCodec.encodeDataPointProbability(i, probability);
            System.out.println(MuhaiUtils.toPaddedBinaryString(dpp));

            int lpDataPointId = ProbabilityVectorCodec.decodeLpDataPointId(dpp);
            double probabilityAfter = ProbabilityVectorCodec.decodeDataPointProbability(dpp);

            assertEquals(i, lpDataPointId);

            assertEquals(probability, probabilityAfter, 0.00000001d);

        }

    }
}
