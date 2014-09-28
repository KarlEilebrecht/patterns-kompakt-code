//@formatter:off
/*
 * Sequence block
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
package de.calamanari.pk.sequenceblock;

import java.util.concurrent.atomic.AtomicLong;
import java.util.logging.Logger;

/**
 * The sequence block holds a chunk of sequence numbers (a unique range)
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class SequenceBlock {

    /**
     * logger
     */
    public static final Logger LOGGER = Logger.getLogger(SequenceBlock.class.getName());

    /**
     * Current value of this block
     */
    private AtomicLong lastReturnedValue;

    /**
     * end of block (exclusive), this is the end of the sequence block
     */
    private final long endOfBlock;

    /**
     * start of block (incl.) first value to be returned
     */
    private final long startOfBlock;

    /**
     * Creates new Sequence block
     * 
     * @param startOfBlock this positive long value will be the first value to be returned by {@link #getNextId()}
     * @param endOfBlock (exclusive) this positive long value ( (endOfBlock -1) will be last valid value to be returned by {@link #getNextId()}
     */
    public SequenceBlock(long startOfBlock, long endOfBlock) {
        if (startOfBlock < 0 || endOfBlock <= startOfBlock) {
            throw new IllegalArgumentException("Unable to create sequence block with negative or conflicting arguments (initialValue: " + startOfBlock
                    + ", maxValue: " + endOfBlock + ").");
        }
        LOGGER.fine(this.getClass().getSimpleName() + " created (" + startOfBlock + " <= val < " + endOfBlock + " ).");
        this.startOfBlock = startOfBlock;
        this.lastReturnedValue = new AtomicLong(startOfBlock - 1);
        this.endOfBlock = endOfBlock;
    }

    /**
     * Returns the next value or -1 if sequence is exhausted<br>
     * This method is safe to be used concurrently by multiple threads.
     * 
     * @return next long id or -1 to indicate exhausted block
     */
    public long getNextId() {
        LOGGER.fine(this.getClass().getSimpleName() + ".getNextId() called.");
        long res = lastReturnedValue.addAndGet(1);
        if (res >= endOfBlock) {
            res = -1;
        }
        return res;
    }

    /**
     * returns true if this block is currently exhausted
     * 
     * @return true if block is exhausted
     */
    public boolean isExhausted() {
        return (lastReturnedValue.get() >= endOfBlock - 1);
    }

    @Override
    public String toString() {
        long lastValue = lastReturnedValue.get();
        long remaining = (lastValue < startOfBlock ? endOfBlock - startOfBlock : endOfBlock - lastValue - 1);
        if (remaining < 0) {
            remaining = 0;
        }
        String debugLast = (lastValue < startOfBlock ? "N/A" : ("" + lastValue));
        if (remaining == 0) {
            debugLast = "" + (endOfBlock - 1);
        }
        return SequenceBlock.class.getSimpleName() + "(startOfBlock: " + startOfBlock + ", endOfBlock: " + endOfBlock + ", lastReturnedValue: " + debugLast
                + ", remaining: " + remaining + ")";
    }

}
