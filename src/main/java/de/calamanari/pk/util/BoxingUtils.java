//@formatter:off
/*
 * BoxingUtils
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

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Convenient auto-boxing support, i.e. for arrays.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class BoxingUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(BoxingUtils.class);

    private BoxingUtils() {
        // utilities, no instance
    }

    /**
     * Array boxing function with convenient behavior. See also {@link JavaWrapperType} for the list of supported types.
     * <ul>
     * <li>Any array of primitives will be converted into an array of the corresponding wrapper type.</li>
     * <li>null will be converted to null</li>
     * <li>If the input array is already an array of boxed values, the given array will be returned <i>as-is</i>.</li>
     * </ul>
     * @param <T> expected type
     * @param primitiveArray array of primitive values, i.e. int[]
     * @return boxed array
     * @throws IllegalArgumentException if the element type is not supported or the input is not an array or null
     */
    public static <T> T[] boxArray(Object primitiveArray) {
        if (primitiveArray == null) {
            @SuppressWarnings("unchecked")
            T[] res = (T[]) primitiveArray;
            return res;
        }
        Class<?> primitiveType = primitiveArray.getClass().getComponentType();
        if (primitiveType == null) {
            throw new IllegalArgumentException(String.format("The given argument is not an array but an instance of %s: %s",
                    primitiveArray.getClass().getName(), LogUtils.limitAndQuoteStringForMessage(primitiveArray.toString(), 100)));
        }
        JavaWrapperType wt = JavaWrapperType.forClass(primitiveType);
        if (wt == null) {
            throw new IllegalArgumentException(
                    String.format("The given argument is not an array of primitive values but an array of %s, supported types are %s", primitiveType.getName(),
                            Arrays.stream(JavaWrapperType.values()).map(JavaWrapperType::getDescription).collect(Collectors.joining(", "))));
        }
        if (primitiveType.isPrimitive()) {
            @SuppressWarnings("unchecked")
            T[] res = (T[]) Array.newInstance(wt.wrapperType, Array.getLength(primitiveArray));
            Arrays.parallelSetAll(res, idx -> Array.get(primitiveArray, idx));
            return res;
        }
        else {
            LOGGER.trace("The given array (componentType={}) is already a wrapper type array (pass-through).", primitiveType);
            @SuppressWarnings("unchecked")
            T[] res = (T[]) primitiveArray;
            return res;
        }
    }

    /**
     * Array unboxing function with convenient behavior. See also {@link JavaWrapperType} for the list of supported types.
     * <ul>
     * <li>Any array of a wrapper type will be converted into an array of the corresponding primitive type.</li>
     * <li>null will be converted to null</li>
     * <li>If the input array is already an array of primitive values, the given array will be returned <i>as-is</i>.</li>
     * </ul>
     * @param <T> expected type
     * @param wrapperArray array of boxed values like Integer[]
     * @return primitive array
     * @throws IllegalArgumentException if the element type is not supported or the input is not an array or null
     */
    public static <T> T unboxArray(Object wrapperArray) {
        if (wrapperArray == null) {
            return null;
        }
        Class<?> wrapperType = wrapperArray.getClass().getComponentType();
        if (wrapperType == null) {
            throw new IllegalArgumentException(String.format("The given argument is not an array but an instance of %s: %s", wrapperArray.getClass().getName(),
                    LogUtils.limitAndQuoteStringForMessage(wrapperArray.toString(), 100)));
        }
        JavaWrapperType wt = JavaWrapperType.forClass(wrapperType);
        if (wt == null || !wt.primitiveArraysSupported) {
            throw new IllegalArgumentException(String.format("The given argument is not an array of wrapped values but an array of %s, supported types are %s",
                    wrapperType.getName(), Arrays.stream(JavaWrapperType.values()).map(JavaWrapperType::getDescription).collect(Collectors.joining(", "))));
        }
        if (!wrapperType.isPrimitive()) {
            int len = Array.getLength(wrapperArray);
            @SuppressWarnings("unchecked")
            T res = (T) Array.newInstance(wt.primitiveType, Array.getLength(wrapperArray));
            for (int i = 0; i < len; i++) {
                Array.set(res, i, Array.get(wrapperArray, i));
            }
            return res;
        }
        else {
            LOGGER.trace("The given array (componentType={}) is already a primitive type array (pass-through).", wrapperType);
            @SuppressWarnings("unchecked")
            T res = (T) wrapperArray;
            return res;
        }

    }

}
