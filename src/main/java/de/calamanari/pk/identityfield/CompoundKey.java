//@formatter:off
/*
 * Compound Key - used in IDENTITY FIELD example
 * Code-Beispiel zum Buch Patterns Kompakt, Verlag Springer Vieweg
 * Copyright 2014 Karl Eilebrecht
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
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
package de.calamanari.pk.identityfield;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collection;
import java.util.Objects;

import de.calamanari.pk.util.MiscUtils;

/**
 * Compound Key - used in IDENTITY FIELD example <br>
 * Simple compound key (composite key) implementation following Martin Fowler's approach.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public final class CompoundKey implements Serializable {

    /**
     * for serialization
     */
    private static final long serialVersionUID = -8596433855453532813L;

    /**
     * parts of the compound key
     */
    private final Serializable[] keyFieldValues;

    /**
     * Pre-computed hash code, Since keys are VALUE OBJECTs, it is safe to cache the hash code.
     */
    private final int hashCode;

    /**
     * Creates new compound keys from the given objects
     * 
     * @param keyFieldValues parts of the key
     */
    public CompoundKey(Serializable... keyFieldValues) {
        int len = keyFieldValues.length;
        Serializable[] copy = new Serializable[len];
        System.arraycopy(checkNotEmptyAndNoNulls(keyFieldValues), 0, copy, 0, len);
        this.keyFieldValues = copy;
        this.hashCode = Objects.hashCode(this.keyFieldValues);
    }

    /**
     * Creates new compound keys from the given long values
     * 
     * @param keyFieldValues parts of the key
     */
    public CompoundKey(long[] keyFieldValues) {
        this(MiscUtils.boxArray(keyFieldValues));
    }

    /**
     * Creates new compound keys from the given objects
     * 
     * @param keyFieldValues parts of the key
     */
    public CompoundKey(Collection<? extends Object> keyFieldValues) {
        this(keyFieldValues.toArray());
    }

    /**
     * Returns the value at the specified index
     * 
     * @param idx part of the key
     * @return key part
     */
    public Serializable getKeyFieldValueAt(int idx) {
        return keyFieldValues[idx];
    }

    /**
     * Returns the key value if this is a single field key (not a compound key)
     * 
     * @return key part
     */
    public Serializable getSingleKeyValue() {
        if (isCompoundKey()) {
            throw new UnsupportedOperationException("Compound key has no single key value.");
        }
        return keyFieldValues[0];
    }

    /**
     * Returns whether this key has more than one field
     * 
     * @return true if number of key fields is greater than 1, otherwise false
     */
    public boolean isCompoundKey() {
        return keyFieldValues.length > 1;
    }

    /**
     * Returns the length of the key (number of parts)
     * 
     * @return number of key fields
     */
    public int getKeyLength() {
        return keyFieldValues.length;
    }

    /**
     * Checks the validity of the key field values
     * 
     * @param keyFieldValues array to be verified
     * @return keyFieldValues pass-through
     * @throws IllegalArgumentException if array is empty or contains null
     */
    private static Serializable[] checkNotEmptyAndNoNulls(Serializable[] keyFieldValues) {
        if (keyFieldValues.length < 1) {
            throw new IllegalArgumentException(CompoundKey.class.getSimpleName() + " must have at least one field.");
        }
        for (Serializable value : keyFieldValues) {
            if (value == null) {
                throw new IllegalArgumentException(CompoundKey.class.getSimpleName() + " cannot contain nulls, given: " + Arrays.asList(keyFieldValues));
            }
        }
        return keyFieldValues;
    }

    @Override
    public int hashCode() {
        return this.hashCode;
    }

    @Override
    public boolean equals(Object obj) {

        boolean res = false;
        if (obj instanceof CompoundKey) {
            CompoundKey other = (CompoundKey) obj;
            int numberOfParts = other.keyFieldValues.length;
            if (other.hashCode == this.hashCode && numberOfParts == this.keyFieldValues.length) {
                res = true; // maybe
                for (int i = 0; i < numberOfParts; i++) {
                    if (!other.keyFieldValues[i].equals(this.keyFieldValues[i])) {
                        res = false;
                        break;
                    }
                }
            }
        }
        return res;

        // Note:
        // Considering a scenario with extreme high key-comparison rate compared
        // to a low rate of key-creation it might improve performance significantly
        // to stringify the complete compound key once during creation,
        // store the result in a final field and then delegate
        // equals() and hashCode() directly to this string.
        // However this comes at the price of higher memory consumption.

    }

    @Override
    public String toString() {
        return CompoundKey.class.getSimpleName() + "(" + Arrays.asList(keyFieldValues) + ")";
    }

}
