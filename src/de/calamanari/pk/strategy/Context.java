/*
 * Context - some kind of system registry in this STRATEGY demonstration.
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
package de.calamanari.pk.strategy;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * Context - some kind of system registry in this STRATEGY demonstration.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)web.de">Karl Eilebrecht</a>
 */
public class Context {

    /**
     * logger
     */
    protected static final Logger LOGGER = Logger.getLogger(Context.class.getName());

    /**
     * holds the available strategies.
     */
    private Map<String, HashStrategy> hashStrategies = new ConcurrentHashMap<>();

    /**
     * Stores the strategy for the given key.
     * @param strategyName strategy identifier
     * @param strategy hash strategy
     */
    public void putHashStrategy(String strategyName, HashStrategy strategy) {
        hashStrategies.put(strategyName, strategy);
    }

    /**
     * Returns the corresponding strategy
     * @param strategyName strategy identifier
     * @return strategy or null if not found
     */
    public HashStrategy getHashStrategy(String strategyName) {
        LOGGER.fine(this.getClass().getSimpleName() + ".getHashStrategy('" + strategyName + "') called ...");
        return hashStrategies.get(strategyName);
    }

    /**
     * Returns a set of the available hash strategy names
     * @return collection of strategies
     */
    public Set<String> getAllHashStrategyNames() {
        return hashStrategies.keySet();
    }

    /**
     * Clears the context
     */
    public void clear() {
        hashStrategies.clear();
    }

}
