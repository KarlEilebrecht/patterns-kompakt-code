//@formatter:off
/*
 * Concrete Counter Flyweight - demonstrates a FLYWEIGHT.
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
package de.calamanari.pk.flyweight;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Concrete Counter Flyweight - a concrete FLYWEIGHT implementation.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class ConcreteCounterFlyweight implements CounterFlyweight {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConcreteCounterFlyweight.class);

    /**
     * The flyweight's intrinsic state, here the first character is the character to be counted, rest of it simulates workload
     */
    protected final String intrinsicState;

    /**
     * Creates a concrete Flyweight with its intrinsic state.
     * 
     * @param characterToBeCounted here we count occurrences of this character in strings
     * @param workload some data to simulate workload
     */
    public ConcreteCounterFlyweight(char characterToBeCounted, String workload) {
        this.intrinsicState = "" + characterToBeCounted + workload;
    }

    @Override
    public int count(String extrinsicState) {
        LOGGER.debug("{}.count('{}') called, counting occurrences of '{}' ...", this.getClass().getSimpleName(), extrinsicState, intrinsicState.charAt(0));
        int count = 0;
        for (int i = 0, size = extrinsicState.length(); i < size; i++) {
            if (extrinsicState.charAt(i) == intrinsicState.charAt(0)) {
                count++;
            }
        }
        return count;
    }

}
