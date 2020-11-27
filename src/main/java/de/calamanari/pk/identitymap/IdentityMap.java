//@formatter:off
/*
 * Identity Map - demonstrates IDENTITY MAP
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
package de.calamanari.pk.identitymap;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Identity Map - demonstrates IDENTITY MAP
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 * @param <K> type of the entity key
 * @param <E> entity type
 */
public class IdentityMap<K, E extends Entity<K>> {

    private static final Logger LOGGER = LoggerFactory.getLogger(IdentityMap.class);

    /**
     * internal map to maintain entities
     */
    private final Map<K, E> instances = new HashMap<>();

    /**
     * we store a reference to the entity type class only for demonstration purposes, this is not required, but useful for debugging
     */
    private final Class<E> entityType;

    /**
     * Creates identity map
     * 
     * @param entityType class of entity to be maintained
     */
    public IdentityMap(Class<E> entityType) {
        this.entityType = entityType;
    }

    /**
     * Adds an entity to the map
     * 
     * @param entity (null will be silently ignored)
     */
    public void add(E entity) {
        if (entity != null) {
            LOGGER.debug("{}(entityType={}).add() called", this.getClass().getSimpleName(), entityType.getSimpleName());
            instances.put(entity.getId(), entity);
        }
    }

    /**
     * Returns the requested entity corresponding to the key
     * 
     * @param id (primary key)
     * @return entity or null if unknown
     */
    public E get(K id) {
        LOGGER.debug("{}(entityType={}).get('{}') called", this.getClass().getSimpleName(), entityType.getSimpleName(), id);
        return instances.get(id);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "(entityType=" + entityType.getSimpleName() + ", currentSize=" + instances.size() + ")";
    }

}
