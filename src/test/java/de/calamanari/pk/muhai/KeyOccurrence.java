//@formatter:off
/*
 * KeyOccurrence
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
package de.calamanari.pk.muhai;

import java.util.Arrays;
import java.util.Comparator;
import java.util.concurrent.atomic.AtomicReference;

import de.calamanari.pk.util.BoxingUtils;

/**
 * {@link KeyOccurrence} is a holder that represents a key found at one or many positions.<br />
 * Elements are mutable and don't define any natural order or identity based on the content.
 * <p>
 * The underlying data type is long, but we treat it as unsigned.
 * <p>
 * Elements are safe to be accessed concurrently by multiple threads.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class KeyOccurrence {

    /**
     * Orders a list of multi-occurrences by the <i>second position</i>.
     * <p>
     * This comparator will throw an {@link IllegalArgumentException} should the list to be sorted contain any duplicates or single occurrences.
     */
    public static final Comparator<KeyOccurrence> ORDER_BY_SECOND_OCCURRENCE = (o1, o2) -> {
        long[] op1 = o1.getOrderedPositions();
        long[] op2 = o2.getOrderedPositions();

        int res = 0;
        if (op1.length < 2 || op2.length < 0) {
            throw new IllegalArgumentException(String.format("Cannot compare single occurrences, found: %s vs. %s", o1, o2));
        }
        else {
            res = Long.compareUnsigned(op1[1], op2[1]);
        }

        if (res == 0) {
            throw new IllegalArgumentException(String.format("Cannot compare duplicate occurrences, found: %s vs. %s", o1, o2));
        }
        return res;
    };

    /**
     * generated key
     */
    private final long key;

    /**
     * positions
     */
    private AtomicReference<long[]> orderedPositions;

    /**
     * @param key
     * @param positions NOT NULL, not empty
     */
    public KeyOccurrence(long key, long... positions) {
        this.key = key;
        if (positions == null || positions.length == 0) {
            throw new IllegalArgumentException("The argument 'positions' must not be null or empty, given: " + Arrays.toString(positions));
        }
        Long[] toOrder = BoxingUtils.boxArray(positions);
        Arrays.sort(toOrder, Long::compareUnsigned);
        this.orderedPositions.set(BoxingUtils.unboxArray(toOrder));
    }

    /**
     * Adds a new position to the sorted set of positions. Should the position already exist this is a no-op.
     * @param newPos (treated unsigned)
     */
    public void addPosition(long newPos) {
        long[] existingPositions = null;
        long[] updatedOrderedPositions = null;
        do {
            int insertionPosition = 0;
            existingPositions = this.orderedPositions.get();
            for (int i = 0; i < existingPositions.length; i++) {
                long existingPosition = existingPositions[i];
                if (existingPosition == newPos) {
                    return;
                }
                else if (Long.compareUnsigned(existingPosition, newPos) < 0) {
                    insertionPosition++;
                }
            }
            updatedOrderedPositions = new long[existingPositions.length + 1];
            if (insertionPosition > 0) {
                System.arraycopy(existingPositions, 0, updatedOrderedPositions, 0, insertionPosition);
            }
            System.arraycopy(existingPositions, insertionPosition, updatedOrderedPositions, insertionPosition + 1,
                    existingPositions.length - insertionPosition);
            updatedOrderedPositions[insertionPosition] = newPos;
        } while (!this.orderedPositions.compareAndSet(existingPositions, updatedOrderedPositions));
    }

    /**
     * Convenience method to add a new position
     * @param keyAtPos
     * @throws IllegalArgumentException if the key is not the same as {@link #getKey()}
     */
    public void addPosition(KeyAtPos keyAtPos) {
        if (keyAtPos.getKey() == this.key) {
            addPosition(keyAtPos.getPos());
        }
        else {
            throw new IllegalArgumentException(String.format("Cannot add occurrence with different key, expected: %s, found %s.", Long.toUnsignedString(key),
                    Long.toUnsignedString(keyAtPos.getKey())));
        }
    }

    /**
     * @return key
     */
    public long getKey() {
        return key;
    }

    /**
     * @return copy of the internal positions array, in ascending order
     */
    public long[] getOrderedPositions() {
        long[] op = orderedPositions.get();
        return Arrays.copyOf(op, op.length);
    }

    public String toString() {
        return KeyOccurrence.class.getSimpleName() + "(" + key + ", " + Arrays.toString(getOrderedPositions()) + ")";
    }
}
