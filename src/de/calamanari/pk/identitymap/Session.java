/*
 * Session - supplementary class
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
 * Session - supplementary class, the place where the IDENTITY MAPs reside in this example.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)web.de">Karl Eilebrecht</a>
 */
public class Session {

    /**
     * logger
     */
    protected static final Logger LOGGER = Logger.getLogger(Session.class.getName());

    /**
     * The session maintains session-local identity maps in this example
     */
    private final Map<Class<?>, IdentityMap<?, ?>> identityMaps = new HashMap<>();

    /**
     * This simulates some kind of session management
     */
    private static final ThreadLocal<Session> SESSION = new ThreadLocal<Session>() {

        @Override
        protected Session initialValue() {
            return new Session();
        }

    };

    /**
     * Returns the current session from our simulated session management
     * @return session
     */
    public static final Session getCurrentSession() {
        return SESSION.get();
    }

    /**
     * This is used by the finder to lookup the corresponding identity map for an entity
     * @param entityType class object of entity
     * @param <K> some key type
     * @param <E> some entity with key of corresponding type
     * @return identity map for the given entity type, never null
     */
    public <K extends Object, E extends Entity<K>> IdentityMap<K, E> getIdentityMap(Class<E> entityType) {
        Object identityMap = identityMaps.get(entityType);

        @SuppressWarnings({ "unchecked" })
        IdentityMap<K, E> result = (IdentityMap<K, E>) identityMap;

        if (result == null) {
            result = new IdentityMap<>(entityType);
            identityMaps.put(entityType, result);
        }

        return result;
    }

}
