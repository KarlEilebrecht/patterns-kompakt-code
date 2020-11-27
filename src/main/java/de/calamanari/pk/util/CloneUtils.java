//@formatter:off
/*
 * CloneUtils
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Methods for cloning objects to ensure independent copies.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class CloneUtils {

    private CloneUtils() {
        // util, no instance
    }

    /**
     * Simulates a pass-by-value situation (serialize/deserialize the given object, aka deep-cloning)
     * 
     * @param input must be serializable, not null
     * @param <T> type to be serialized and deserialized
     * @return object passed by value
     * @throws IOException if serialization/deserialization failed
     */
    public static <T extends Object> T passByValue(T input) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(input);
        }
        bos.close();

        try (ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray()); ObjectInputStream ois = new ObjectInputStream(bis)) {
            @SuppressWarnings("unchecked")
            T res = (T) input.getClass().cast(ois.readObject());
            return res;
        }
        catch (ClassNotFoundException | ClassCastException ex) {
            throw new IOException("Unexpected type change after serialization/deserialization." + ex);
        }
    }

}
