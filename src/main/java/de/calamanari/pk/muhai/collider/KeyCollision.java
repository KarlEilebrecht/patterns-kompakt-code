//@formatter:off
/*
 * KeyCollision
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
package de.calamanari.pk.muhai.collider;

import java.io.Serializable;

/**
 * Interface for collision items, used for collecting, intermediate storage and aggregation
 * <p>
 * A {@link KeyCollision} always represents multiple occurrences at different positions of the same key.
 * <p>
 * Implementing classes shall deal with the type long as unsigned (see {@link Long#toUnsignedString(long)})
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public interface KeyCollision<T extends KeyCollision<T>> extends Comparable<T>, Serializable {

    /**
     * Optional key field (the key involved in the collision)
     * @return the key that occurred at least twice, depending on the concrete implementation the key can be unavailable, in this case 0 should always be
     *         returned.
     */
    public long getKey();

    /**
     * Mandatory property to be returned by every implementing class, by definition the value is <b>&gt;=1</b> assuming that the positions start at 0
     * @return the second occurrence of the key (by definition the collision), <b>&gt;=1</b>
     */
    public long getFirstCollisionPosition();

    /**
     * Mandatory property to be returned by every implementing class, by definition the value is <b>&gt;=2</b> assuming that a collision involves two unique
     * positions.
     * @return number of positions the key was detected <b>&gt;=2</b>
     */
    public long getNumberOfKeyOccurrences();

    /**
     * Optional positions the key was found in their natural order {@link Long#compareUnsigned(long, long)}
     * <p>
     * @return positions or empty array if there are no positions available (default behavior), NOT NULL
     */
    default long[] getPositions() {
        return new long[0];
    }

    /**
     * @return number of positions after first occurrence, in other words <code>{@link #getNumberOfKeyOccurrences()}-1</code>
     */
    default long getNumberOfDuplicates() {
        return getNumberOfKeyOccurrences() - 1;
    }

}
