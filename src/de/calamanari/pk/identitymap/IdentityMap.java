/*
 * Identity Map - demonstrates IDENTITY MAP
 * Code-Beispiel zum Buch Patterns Kompakt, Verlag Springer Vieweg
 * Copyright 2013 Karl Eilebrecht
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
package de.calamanari.pk.identitymap;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Identity Map - demonstrates IDENTITY MAP
 * @author <a href="mailto:Karl.Eilebrecht(a/t)web.de">Karl Eilebrecht</a>
 * @param <K> type of the entity key
 * @param <E> entity type
 */
public class IdentityMap<K, E extends Entity<K>> {

    /**
     * logger
     */
    protected static final Logger LOGGER = Logger.getLogger(IdentityMap.class.getName());

    /**
     * internal map to maintain entities
     */
    private final Map<K, E> instances = new HashMap<>();

    /**
     * we store a reference to the entity type class only for demonstration purposes, this is not required, but useful
     * for debugging
     */
    private final Class<E> entityType;

    /**
     * Creates identity map
     * @param entityType class of entity to be maintained
     */
    public IdentityMap(Class<E> entityType) {
        this.entityType = entityType;
    }

    /**
     * Adds an entity to the map
     * @param entity (null will be silently ignored)
     */
    public void add(E entity) {
        if (entity != null) {
            LOGGER.fine(this.getClass().getSimpleName() + "(entityType=" + entityType.getSimpleName()
                    + ").add() called");
            instances.put(entity.getId(), entity);
        }
    }

    /**
     * Returns the requested entity corresponding to the key
     * @param id (primary key)
     * @return entity or null if unknown
     */
    public E get(K id) {
        LOGGER.fine(this.getClass().getSimpleName() + "(entityType=" + entityType.getSimpleName() + ").get('" + id
                + "') called");
        return instances.get(id);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "(entityType=" + entityType.getSimpleName() + ", currentSize="
                + instances.size() + ")";
    }

}