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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * This enum contains the known java primitives and their wrapper types as an easy lookup.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public enum JavaWrapperType {
    //@formatter:off
    BOOLEAN(boolean.class, Boolean.class, true),
    BYTE(byte.class, Byte.class, true),
    CHARACTER(char.class, Character.class, true),
    DOUBLE(double.class, Double.class, true),
    FLOAT(float.class, Float.class, true),
    INTEGER(int.class, Integer.class, true),
    LONG(long.class, Long.class, true),
    SHORT(short.class, Short.class, true),
    VOID(void.class, Void.class, false);

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

    public final Class<?> primitiveType;
    public final Class<?> wrapperType;
    public final boolean primitiveArraysSupported;

    JavaWrapperType(Class<?> primitiveType, Class<?> wrapperType, boolean primitiveArraysSupported) {
        this.primitiveType = primitiveType;
        this.wrapperType = wrapperType;
        this.primitiveArraysSupported = primitiveArraysSupported;
    }

    /**
     * Returns the {@link JavaWrapperType} instance if the given class is either a primitive or a wrapper type.
     * @param clazz
     * @return wrappable type instance or null if the given class is not supported
     */
    public static JavaWrapperType forClass(Class<?> clazz) {
        return LOOKUP.get(clazz);
    }

    public String getDescription() {
        return String.format("%s(primitiveType=%s%s, wrapperType=%s)", this.toString(), this.primitiveType.getSimpleName(),
                (this == VOID ? " (no arrays)" : ""), this.wrapperType.getSimpleName());
    }

}
