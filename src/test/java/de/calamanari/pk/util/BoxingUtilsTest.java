//@formatter:off
/*
 * BoxingUtilsTest
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

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;

public class BoxingUtilsTest {

    private static final List<TestInput> TEST_INPUTS_POSITIVE;
    static {

        List<TestInput> input = new ArrayList<>();
        input.add(new TestInput(new boolean[0]));
        input.add(new TestInput(new boolean[] { true }));
        input.add(new TestInput(new boolean[] { false }));
        input.add(new TestInput(new boolean[] { false, true, false }));
        input.add(new TestInput(new Boolean[0]));
        input.add(new TestInput(new Boolean[] { true }));
        input.add(new TestInput(new Boolean[] { false }));
        input.add(new TestInput(new Boolean[] { false, true, false }));

        input.add(new TestInput(new byte[0]));
        input.add(new TestInput(new byte[] { 127 }));
        input.add(new TestInput(new byte[] { -34 }));
        input.add(new TestInput(new byte[] { -11, 0, 17, 127, -128 }));
        input.add(new TestInput(new Byte[0]));
        input.add(new TestInput(new Byte[] { 127 }));
        input.add(new TestInput(new Byte[] { -34 }));
        input.add(new TestInput(new Byte[] { -11, 0, 17, 127, -128 }));

        input.add(new TestInput(new char[0]));
        input.add(new TestInput(new char[] { 'A' }));
        input.add(new TestInput(new char[] { 'B' }));
        input.add(new TestInput(new char[] { 'A', 'B', 1829, 127, 'C' }));
        input.add(new TestInput(new Character[0]));
        input.add(new TestInput(new Character[] { 'A' }));
        input.add(new TestInput(new Character[] { 'B' }));
        input.add(new TestInput(new Character[] { 'A', 'B', 1829, 127, 'C' }));

        input.add(new TestInput(new double[0]));
        input.add(new TestInput(new double[] { -9274.0 }));
        input.add(new TestInput(new double[] { 123.9283 }));
        input.add(new TestInput(new double[] { Double.MIN_VALUE, Double.MAX_VALUE, 1829.321, -127.9, 0.00 }));
        input.add(new TestInput(new Double[0]));
        input.add(new TestInput(new Double[] { -9274.0 }));
        input.add(new TestInput(new Double[] { 123.9283 }));
        input.add(new TestInput(new Double[] { Double.MIN_VALUE, Double.MAX_VALUE, 1829.321, -127.9, 0.00 }));

        input.add(new TestInput(new float[0]));
        input.add(new TestInput(new float[] { -9274.0f }));
        input.add(new TestInput(new float[] { 123.9283f }));
        input.add(new TestInput(new float[] { Float.MIN_VALUE, Float.MAX_VALUE, 1829.321f, -127.9f, 0.00f }));
        input.add(new TestInput(new Float[0]));
        input.add(new TestInput(new Float[] { -9274.0f }));
        input.add(new TestInput(new Float[] { 123.9283f }));
        input.add(new TestInput(new Float[] { Float.MIN_VALUE, Float.MAX_VALUE, 1829.321f, -127.9f, 0.00f }));

        input.add(new TestInput(new int[0]));
        input.add(new TestInput(new int[] { 127 }));
        input.add(new TestInput(new int[] { -34 }));
        input.add(new TestInput(new int[] { Integer.MIN_VALUE, -11, 0, 17, 127, 983467, -128, Integer.MAX_VALUE }));
        input.add(new TestInput(new Integer[0]));
        input.add(new TestInput(new Integer[] { 127 }));
        input.add(new TestInput(new Integer[] { -34 }));
        input.add(new TestInput(new Integer[] { Integer.MIN_VALUE, -11, 0, 17, 127, 983467, -128, Integer.MAX_VALUE }));

        input.add(new TestInput(new long[0]));
        input.add(new TestInput(new long[] { 127 }));
        input.add(new TestInput(new long[] { -34 }));
        input.add(new TestInput(new long[] { Long.MIN_VALUE, -11, 0, 17, 127, 983467, -128, Long.MAX_VALUE }));
        input.add(new TestInput(new Long[0]));
        input.add(new TestInput(new Long[] { 127L }));
        input.add(new TestInput(new Long[] { -34L }));
        input.add(new TestInput(new Long[] { Long.MIN_VALUE, -11L, 0L, 17L, 127L, 983467L, -128L, Long.MAX_VALUE }));

        input.add(new TestInput(new short[0]));
        input.add(new TestInput(new short[] { 127 }));
        input.add(new TestInput(new short[] { -34 }));
        input.add(new TestInput(new short[] { Short.MIN_VALUE, -11, 0, 17, 127, 15467, -128, Short.MAX_VALUE }));
        input.add(new TestInput(new Short[0]));
        input.add(new TestInput(new Short[] { 127 }));
        input.add(new TestInput(new Short[] { -34 }));
        input.add(new TestInput(new Short[] { Short.MIN_VALUE, -11, 0, 17, 127, 15467, -128, Short.MAX_VALUE }));

        TEST_INPUTS_POSITIVE = Collections.unmodifiableList(input);

    }

    private static final List<TestInput> TEST_INPUTS_NEGATIVE;
    static {

        List<TestInput> input = new ArrayList<>();
        input.add(new TestInput(new Void[0], "but an array of java.lang.Void"));
        input.add(new TestInput(new Void[] { null, null }, "but an array of java.lang.Void"));

        input.add(new TestInput(new String[0], "but an array of java.lang.String"));
        input.add(new TestInput("Just a string", "The given argument is not an array but an instance of java.lang.String"));
        input.add(new TestInput(1938, "The given argument is not an array but an instance of java.lang.Integer"));

        TEST_INPUTS_NEGATIVE = Collections.unmodifiableList(input);

    }

    @Test
    public void testFailingBidiConversions() {
        for (TestInput testInput : TEST_INPUTS_NEGATIVE) {
            Throwable expectedException = null;
            try {
                BoxingUtils.unboxArray(BoxingUtils.unboxArray(testInput.input));
            }
            catch (Throwable t) {
                expectedException = t;
            }

            assertNotNull(expectedException);

            if (testInput.expectPartOfExceptionMessage != null) {
                assertTrue(expectedException.getMessage().contains(testInput.expectPartOfExceptionMessage));
            }
        }

    }

    @Test
    public void testSuccessfulBidiConversions() {
        for (TestInput testInput : TEST_INPUTS_POSITIVE) {
            Object input = testInput.input;

            Object output1 = BoxingUtils.boxArray(input);
            Object wrappers = output1;
            if (output1 == input) {
                Object output2 = BoxingUtils.unboxArray(input);
                wrappers = BoxingUtils.boxArray(output2);
                assertArrayEquals((Object[]) input, (Object[]) wrappers);
            }
            else {
                Object primitives = BoxingUtils.unboxArray(output1);
                Class<?> type = primitives.getClass().getComponentType();
                if (type == boolean.class) {
                    assertArrayEquals((boolean[]) input, (boolean[]) primitives);
                }
                else if (type == byte.class) {
                    assertArrayEquals((byte[]) input, (byte[]) primitives);
                }
                else if (type == char.class) {
                    assertArrayEquals((char[]) input, (char[]) primitives);
                }
                else if (type == double.class) {
                    assertArrayEquals((double[]) input, (double[]) primitives, 0d);
                }
                else if (type == float.class) {
                    assertArrayEquals((float[]) input, (float[]) primitives, 0f);
                }
                else if (type == int.class) {
                    assertArrayEquals((int[]) input, (int[]) primitives);
                }
                else if (type == long.class) {
                    assertArrayEquals((long[]) input, (long[]) primitives);
                }
                else if (type == short.class) {
                    assertArrayEquals((short[]) input, (short[]) primitives);
                }
                else {
                    fail("unexpected array type: " + type);
                }
            }

        }
    }

    @Test
    public void testNull() {
        assertNull(BoxingUtils.boxArray(null));
        assertNull(BoxingUtils.unboxArray(null));
    }

    @Test
    public void testPassThrough() {

        int[] input = new int[] { 7, 8, 3, 1 };
        assertSame(input, BoxingUtils.unboxArray(input));

        Integer[] inputW = new Integer[] { 7, 8, 3, 1 };
        assertSame(inputW, BoxingUtils.boxArray(inputW));

    }

    private static class TestInput {

        final Object input;
        final String expectPartOfExceptionMessage;

        TestInput(Object input, String expectPartOfExceptionMessage) {
            super();
            this.input = input;
            this.expectPartOfExceptionMessage = expectPartOfExceptionMessage;
        }

        TestInput(Object input) {
            this(input, null);
        }

    }

}
