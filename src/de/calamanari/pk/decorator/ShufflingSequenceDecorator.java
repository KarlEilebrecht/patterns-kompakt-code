/*
 * Shuffling sequence decorator is decorating part of the DECORATOR pattern
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
package de.calamanari.pk.decorator;

import java.util.logging.Logger;

import de.calamanari.pk.util.OrbOfConfusion;

/**
 * Shuffling sequence decorator decorates a number sequence, scrambles the ids (bijectively) and adds further
 * operations.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)web.de">Karl Eilebrecht</a>
 */
public class ShufflingSequenceDecorator implements NumberSequence {

    /**
     * logger
     */
    private static final Logger LOGGER = Logger.getLogger(ShufflingSequenceDecorator.class.getName());

    /**
     * The decorated instance
     */
    private final NumberSequence decoratedInstance;

    /**
     * The OrbOfConfusion will be used for shuffling the sequence values
     */
    private final OrbOfConfusion orbOfConfusion;

    /**
     * Creates a decorator for the given sequence
     * @param sequenceToBeDecorated source sequence
     * @param upperSequenceBound max sequence value + 1
     */
    public ShufflingSequenceDecorator(NumberSequence sequenceToBeDecorated, long upperSequenceBound) {
        this.decoratedInstance = sequenceToBeDecorated;
        this.orbOfConfusion = new OrbOfConfusion(OrbOfConfusion.createDefaultPartitions(), 0, 0, upperSequenceBound);
    }

    @Override
    public long getNextId() {
        LOGGER.finest("getNextId() called, taking id from decorated sequence ...");
        long rawId = decoratedInstance.getNextId();
        LOGGER.finest("now decorating ...");
        long shuffledId = shuffleId(rawId);
        LOGGER.finest("decoration done: id " + rawId + " transformed to " + shuffledId);
        return shuffledId;
    }

    @Override
    public String getSequenceName() {
        return decoratedInstance.getSequenceName() + " (shuffled)";
    }

    /**
     * Behavior exclusively added by the decorator to return a shuffled value
     * @param rawId id to be transformed
     * @return transformed id
     */
    protected long shuffleId(long rawId) {
        LOGGER.finest("shuffleId(" + rawId + ") called ...");
        long shuffledId = orbOfConfusion.transform(rawId);
        return shuffledId;
    }

}
