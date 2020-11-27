//@formatter:off
/*
 * Counter Flyweight Factory - demonstrates a FLYWEIGHT factory.
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
package de.calamanari.pk.flyweight;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Counter Flyweight Factory - demonstrates a FLYWEIGHT factory.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class CounterFlyweightFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(CounterFlyweightFactory.class);

    /**
     * wait time between status messages
     */
    private static final long INTERVAL_MILLIS_100 = 100;

    /**
     * This factory caches the flyweights, they can be used concurrently
     */
    private final Map<Character, CounterFlyweight> pool = new ConcurrentHashMap<>();

    /**
     * in this example a flag decides whether to returned normal flyweights or unshared, in real-life scenarios this would depend on other factors
     */
    private final boolean forceUnshared;

    /**
     * In this example, some workload will be added to each flyweight, in real-life scenarios this may consist of resources, pre-calculated tables, buffers ...
     */
    private final String workload;

    /**
     * counts the instances returned
     */
    private final AtomicInteger outputCounter = new AtomicInteger(0);

    /**
     * counts the instances created
     */
    private final AtomicInteger createdCounter = new AtomicInteger(0);

    /**
     * we don't want to generate tons of messages, thus we only log from time to time
     */
    private final AtomicLong lastLogMessageTimeMillis = new AtomicLong(0);

    /**
     * Creates new Factory for flyweights
     * 
     * @param workload simulated workload
     * @param forceUnshared if true, this factory will only return unshared flyweights
     */
    public CounterFlyweightFactory(String workload, boolean forceUnshared) {
        LOGGER.debug("{}(len={}, forceUnshared={}) created.", CounterFlyweightFactory.class.getSimpleName(), workload.length(), forceUnshared);
        this.workload = workload;
        this.forceUnshared = forceUnshared;
    }

    /**
     * Returns a concrete Flyweight (created once on demand)
     * 
     * @param c the character to be counted
     * @return CountStrategyFlyweight
     */
    public CounterFlyweight createCounterFlyweight(char c) {
        CounterFlyweight res = null;
        if (forceUnshared) {
            createdCounter.incrementAndGet();
            res = new UnsharedConcreteCounterFlyweight(c, workload);
        }
        else {
            // Note: I forbear from synchronizing the block, because creating a duplicate
            // flyweight (rare case) does not matter but synchronization on the other hand
            // is expensive
            res = pool.computeIfAbsent(c, k -> {
                CounterFlyweight newFlyweight = new ConcreteCounterFlyweight(c, workload);
                createdCounter.incrementAndGet();
                return newFlyweight;
            });
        }
        outputCounter.incrementAndGet();
        if (LOGGER.isDebugEnabled()) {
            long last = lastLogMessageTimeMillis.get();
            long current = System.currentTimeMillis();
            if (current >= last + INTERVAL_MILLIS_100) {
                boolean success = lastLogMessageTimeMillis.compareAndSet(last, current);
                if (success) {
                    LOGGER.debug("--> created: {}, returned: {}", createdCounter.get(), outputCounter.get());
                }
            }
        }
        return res;
    }

}
