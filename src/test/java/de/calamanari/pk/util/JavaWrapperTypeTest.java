//@formatter:off
/*
 * JavaWrapperTypeTest
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

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

public class JavaWrapperTypeTest {

    private static final List<TestEntry<?, ?>> TEST_ENTRIES;
    static {
        List<TestEntry<?, ?>> list = new ArrayList<>();
        list.add(new TestEntry<>(boolean.class, Boolean.class, true, new boolean[] { false, true, false }, new Boolean[] { false, true, false },
                "[false, true, false]"));
        list.add(new TestEntry<>(byte.class, Byte.class, true, new byte[] { -128, 0, 127 }, new Byte[] { -128, 0, 127 }, "[-128, 0, 127]"));
        list.add(new TestEntry<>(char.class, Character.class, true, new char[] { '0', '9', 'x' }, new Character[] { '0', '9', 'x' }, "[0, 9, x]"));
        list.add(new TestEntry<>(double.class, Double.class, true, new double[] { 0.1, 0.2, 0.8 }, new Double[] { 0.1, 0.2, 0.8 }, "[0.1, 0.2, 0.8]"));
        list.add(new TestEntry<>(float.class, Float.class, true, new float[] { 0.2f, 0.8f, 1.0f }, new Float[] { 0.2f, 0.8f, 1.0f }, "[0.2, 0.8, 1.0]"));
        list.add(new TestEntry<>(int.class, Integer.class, true, new int[] { 1, 0, 89766 }, new Integer[] { 1, 0, 89766 }, "[1, 0, 89766]"));
        list.add(
                new TestEntry<>(long.class, Long.class, true, new long[] { 19L, 0L, 7927374323L }, new Long[] { 19L, 0L, 7927374323L }, "[19, 0, 7927374323]"));
        list.add(new TestEntry<>(short.class, Short.class, true, new short[] { 1, 0, 8976 }, new Short[] { 1, 0, 8976 }, "[1, 0, 8976]"));
        list.add(new TestEntry<>(void.class, Void.class, false, null, new Void[] {}, "[]"));
        TEST_ENTRIES = Collections.unmodifiableList(list);
    }

    @Test
    public void testBasics() {
        for (TestEntry<?, ?> entry : TEST_ENTRIES) {
            assertValidType(entry.primitiveType, entry.wrapperType, entry.primitiveArraySupportExpected);
        }

        assertNull(JavaWrapperType.forClass(this.getClass()));
        assertNull(JavaWrapperType.forClass(null));
    }

    @Test
    public void testArrayToString() {
        for (TestEntry<?, ?> entry : TEST_ENTRIES) {
            String expectedArrayAsString = entry.primitiveArraySupportExpected ? entry.expectedArrayAsString : "null";
            assertEquals(expectedArrayAsString, JavaWrapperType.forClass(entry.primitiveType).arrayToString(entry.primitiveArray));
            assertEquals(entry.expectedArrayAsString, JavaWrapperType.forClass(entry.wrapperType).arrayToString(entry.wrapperArray));
        }

        Throwable expectedError = null;
        try {
            JavaWrapperType.forClass(int.class).arrayToString(new Object());
        }
        catch (RuntimeException ex) {
            expectedError = ex;
        }
        assertTrue(expectedError instanceof IllegalArgumentException);

        expectedError = null;
        try {
            JavaWrapperType.forClass(int.class).arrayToString(new Short[] { 1, 2, 3 });
        }
        catch (RuntimeException ex) {
            expectedError = ex;
        }
        assertTrue(expectedError instanceof IllegalArgumentException);

    }

    private void assertValidType(Class<?> primitiveType, Class<?> wrapperType, boolean primitiveArraySupportExpected) {

        assertSame(JavaWrapperType.forClass(primitiveType), JavaWrapperType.forClass(wrapperType));
        assertEquals(primitiveType, JavaWrapperType.forClass(primitiveType).primitiveType);
        assertEquals(wrapperType, JavaWrapperType.forClass(primitiveType).wrapperType);
        assertEquals(primitiveType, JavaWrapperType.forClass(wrapperType).primitiveType);
        assertEquals(wrapperType, JavaWrapperType.forClass(wrapperType).wrapperType);

        if (primitiveArraySupportExpected) {
            assertTrue(JavaWrapperType.forClass(primitiveType).primitiveArraysSupported);
            assertTrue(JavaWrapperType.forClass(wrapperType).primitiveArraysSupported);
        }
        else {
            assertFalse(JavaWrapperType.forClass(primitiveType).primitiveArraysSupported);
            assertFalse(JavaWrapperType.forClass(wrapperType).primitiveArraysSupported);
        }

    }

    private static class TestEntry<P, W> {
        final Class<P> primitiveType;
        final Class<W> wrapperType;
        final boolean primitiveArraySupportExpected;
        final W[] wrapperArray;
        final Object primitiveArray;
        final String expectedArrayAsString;

        TestEntry(Class<P> primitiveType, Class<W> wrapperType, boolean primitiveArraySupportExpected, Object primitiveArray, W[] wrapperArray,
                String expectedArrayAsString) {
            this.primitiveType = primitiveType;
            this.wrapperType = wrapperType;
            this.primitiveArraySupportExpected = primitiveArraySupportExpected;
            this.primitiveArray = primitiveArray;
            this.wrapperArray = wrapperArray;
            this.expectedArrayAsString = expectedArrayAsString;
        }

    }
}
