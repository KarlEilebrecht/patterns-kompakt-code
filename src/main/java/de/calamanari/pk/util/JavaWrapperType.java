//@formatter:off
/*
 * JavaWrapperType
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

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

/**
 * This enum contains the known java primitives and their wrapper types as an easy lookup.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public enum JavaWrapperType {
    //@formatter:off

    /**
     * boolean vs. Boolean
     */
    BOOLEAN(boolean.class, Boolean.class, true, array -> Arrays.toString((boolean[])array)),

    /**
     * byte vs. Byte
     */
    BYTE(byte.class, Byte.class, true, array -> Arrays.toString((byte[])array)),

    /**
     * char vs. Character
     */
    CHARACTER(char.class, Character.class, true, array -> Arrays.toString((char[])array)),
    
    /**
     * double vs. Double
     */
    DOUBLE(double.class, Double.class, true, array -> Arrays.toString((double[])array)),
    
    /**
     * float vs. Float
     */
    FLOAT(float.class, Float.class, true, array -> Arrays.toString((float[])array)),
    
    /**
     * int vs. Integer
     */
    INTEGER(int.class, Integer.class, true, array -> Arrays.toString((int[])array)),
    
    /**
     * long vs. Long
     */
    LONG(long.class, Long.class, true, array -> Arrays.toString((long[])array)),
    
    /**
     * short vs. Short
     */
    SHORT(short.class, Short.class, true, array -> Arrays.toString((short[])array)),
    
    /**
     * void vs. Void
     */
    VOID(void.class, Void.class, false, array -> "[]");

    //@formatter:on

    private static final Map<Class<?>, JavaWrapperType> LOOKUP;
    static {
        HashMap<Class<?>, JavaWrapperType> lookup = new HashMap<>();
        for (JavaWrapperType element : JavaWrapperType.values()) {
            lookup.put(element.primitiveType, element);
            lookup.put(element.wrapperType, element);
        }
        LOOKUP = Collections.unmodifiableMap(lookup);
    }

    /**
     * The primitive type that can be wrapped
     */
    public final Class<?> primitiveType;

    /**
     * Wrapper type for Boxing in Java
     */
    public final Class<?> wrapperType;

    /**
     * For symmetry reasons VOID is included, but you cannot create any primitive array of <b>void</b><br>
     * This flag indicates this for generic processing.
     */
    public final boolean primitiveArraysSupported;

    /**
     * A function that converts any array of the given primitive type to a string
     */
    public final Function<Object, String> primitiveArrayToStringFunction;

    /**
     * @param primitiveType
     * @param wrapperType
     * @param primitiveArraysSupported always true except for {@link #VOID}
     */
    JavaWrapperType(Class<?> primitiveType, Class<?> wrapperType, boolean primitiveArraysSupported, Function<Object, String> arrayToStringFunction) {
        this.primitiveType = primitiveType;
        this.wrapperType = wrapperType;
        this.primitiveArraysSupported = primitiveArraysSupported;
        this.primitiveArrayToStringFunction = arrayToStringFunction;
    }

    /**
     * Returns the {@link JavaWrapperType} instance if the given class is either a primitive or a wrapper type.
     * @param clazz primitive class or wrapper type (i.e. int.class or Integer.class)
     * @return wrappable type instance or null if the given class is not supported
     */
    public static JavaWrapperType forClass(Class<?> clazz) {
        return LOOKUP.get(clazz);
    }

    /**
     * Provides a convenient method to print an array, no matter whether it is primitive or not.
     * @param array source object, which must be an array or null (returns <code>"null"</code>)
     * @return the corresponding Arrays.toString() result for primitives arrays or any Object[]
     * @throws {@link IllegalArgumentException} if the given array's component type is neither the primitive nor the wrapper type
     */
    public String arrayToString(Object array) {
        String res = null;
        if (array != null) {
            Class<?> componentType = array.getClass().getComponentType();
            if (componentType == this.primitiveType) {
                res = primitiveArrayToStringFunction.apply(array);
            }
            else if (componentType == this.wrapperType) {
                res = Arrays.toString((Object[]) array);
            }
            else {
                throw new IllegalArgumentException(String.format("This method is only applicable to arrays of type %s or %s, found: %s", this.primitiveType,
                        this.wrapperType, componentType));
            }
        }
        return String.valueOf(res);
    }

    /**
     * For debugging (can't overwrite toString() of an enum)
     * @return description
     */
    public String getDescription() {
        return String.format("%s(primitiveType=%s%s, wrapperType=%s)", this.toString(), this.primitiveType.getSimpleName(),
                (this == VOID ? " (no arrays)" : ""), this.wrapperType.getSimpleName());
    }

}
