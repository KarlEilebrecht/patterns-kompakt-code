//@formatter:off
/*
 * Simple Number sequence is a concrete component to be decorated applying the DECORATOR pattern
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
package de.calamanari.pk.decorator;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Simple Number sequence is a concrete component to be decorated applying the DECORATOR pattern<br>
 * This implementation is thread-safe.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class SimpleNumberSequence implements NumberSequence {

    private static final Logger LOGGER = LoggerFactory.getLogger(SimpleNumberSequence.class);

    /**
     * simulates a sequence management
     */
    private static final ConcurrentHashMap<String, AtomicLong> SEQUENCE_MAP = new ConcurrentHashMap<>();

    /**
     * name of the sequence
     */
    private final String sequenceName;

    /**
     * Creates new sequence of the given name
     * 
     * @param sequenceName name of sequence
     */
    public SimpleNumberSequence(String sequenceName) {
        this.sequenceName = sequenceName;
    }

    @Override
    public long getNextId() {
        AtomicLong counter = SEQUENCE_MAP.get(sequenceName);
        if (counter == null) {
            counter = new AtomicLong(0);
            AtomicLong counter2 = SEQUENCE_MAP.putIfAbsent(sequenceName, counter);
            if (counter2 != null) {
                counter = counter2;
            }
        }
        long id = counter.incrementAndGet();
        LOGGER.trace("created id={}", id);
        return id;
    }

    @Override
    public String getSequenceName() {
        return sequenceName;
    }

}
