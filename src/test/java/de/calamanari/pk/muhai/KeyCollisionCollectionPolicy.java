//@formatter:off
/*
 * KeyCollisionCollectionPolicy
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

/**
 * A {@link KeyCollisionCollectionPolicy} is a POLICY that defines how to collect collisions
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 * @param <T> collision representation type
 */
public interface KeyCollisionCollectionPolicy<T extends KeyCollision<T>> {

    /**
     * FACTORY METHOD for collisions
     * @param key the key that occurred at least twice
     * @param positions at least 2 positions
     * @return representation of the collision
     */
    public T createKeyCollision(long key, long... positions);

    /**
     * @return the coded to transform items for storing in files
     */
    public ItemStringCodec<T> getLineCodec();

}
