//@formatter:off
/*
 * KeyCollisionIterator
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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import de.calamanari.pk.util.BoxingUtils;

/**
 * The {@link KeyCollisionIterator} is a DECORATOR for a {@link KeyAtPos}-ITERATOR. Assuming all source items occur in their natural order, we aggregate
 * subsequent positions for the same key and drop any keys that only occur at a single position.
 * <p>
 * This way the {@link KeyCollisionIterator} spools all the source items and only returns the collisions.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 * @param <T> collision representation type
 */
public class KeyCollisionIterator<K extends KeyCollision<K>> implements Iterator<K> {

    /**
     * Type indicator
     */
    private static final Long[] EMPTY_LONG_ARRAY = new Long[0];

    /**
     * The wrapped iterator
     */
    private final Iterator<KeyAtPos> sourceIterator;

    /**
     * for creating collision items
     */
    private final KeyCollisionCollectionPolicy<K> keyCollisionCollectionPolicy;

    /**
     * Next collision to be returned
     */
    private K bufferedItem = null;

    /**
     * single element prefetched from the underlying iterator
     */
    private KeyAtPos readAhead = null;

    /**
     * marks the end of the iteration
     */
    private boolean done;

    /**
     * @param sourceIterator iterator returning elements in key-order
     * @param keyCollisionCollectionPolicy policy for creating collision items
     */
    public KeyCollisionIterator(Iterator<KeyAtPos> sourceIterator, KeyCollisionCollectionPolicy<K> keyCollisionCollectionPolicy) {
        this.sourceIterator = sourceIterator;
        this.keyCollisionCollectionPolicy = keyCollisionCollectionPolicy;
    }

    /**
     * Spool source iterator until we find the same key at at least two positions
     * @return detected multi-occurrence
     */
    private K findNextCollision() {
        K res = null;
        while (!done && res == null && sourceIterator.hasNext()) {
            KeyAtPos base = readAhead;
            List<Long> rawPositions = new ArrayList<>();
            if (base == null) {
                base = sourceIterator.next();
            }
            rawPositions.add(base.getPos());

            while (sourceIterator.hasNext()) {
                readAhead = sourceIterator.next();
                if (readAhead.getKey() == base.getKey()) {
                    rawPositions.add(readAhead.getPos());
                }
                else {
                    break;
                }
            }
            if (rawPositions.size() > 1) {
                long[] positions = BoxingUtils.unboxArray(rawPositions.toArray(EMPTY_LONG_ARRAY));
                res = keyCollisionCollectionPolicy.createKeyCollision(base.getKey(), positions);
            }
        }
        return res;
    }

    @Override
    public boolean hasNext() {
        boolean res = false;
        if (bufferedItem == null) {
            bufferedItem = findNextCollision();
            if (bufferedItem == null) {
                done = true;
            }
            else {
                res = true;
            }
        }
        else {
            res = true;
        }
        return res;
    }

    @Override
    public K next() {
        K res = null;
        if (this.hasNext()) {
            res = bufferedItem;
            bufferedItem = null;
        }
        else {
            throw new NoSuchElementException("End of input");
        }
        return res;

    }

}
