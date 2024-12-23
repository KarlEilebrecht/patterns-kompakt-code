//@formatter:off
/*
 * Decorator test - demonstrates DECORATOR pattern
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
package de.calamanari.pk.decorator;

import static org.junit.Assert.assertFalse;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.calamanari.pk.util.TimeUtils;

/**
 * Decorator test - demonstrates DECORATOR pattern
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class DecoratorTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(DecoratorTest.class);

    /**
     * name of test sequence
     */
    private static final String TEST_SEQUENCE_NAME = "testSequence";

    /**
     * number of test runs (sequence numbers)
     */
    private static final int NUMBER_OF_RUNS = 100_000;

    @Test
    public void testShuffle() {

        // First we create a sequence which provides long ids in order, then
        // we decorate the new sequence with a shuffling sequence decorator
        // which will bijectively replace each value with another from
        // a defined range, so that the result sequence seems to be random.

        // Hints: - set the log-level in logback.xml to DEBUG or TRACE (will take some time) to see details.
        //
        // - see the sequenceblock example and decorate it like in the SimpleNumberSequence example

        LOGGER.info("Test decorator shuffling ...");

        List<Long> usedIdsInOrder = new ArrayList<>();
        Set<Long> usedIds = new HashSet<>();
        long startTimeNanos = System.nanoTime();
        NumberSequence sequenceToBeDecorated = new SimpleNumberSequence(TEST_SEQUENCE_NAME);
        NumberSequence decoratedSequence = new ShufflingSequenceDecorator(sequenceToBeDecorated, NUMBER_OF_RUNS);

        for (int i = 0; i < NUMBER_OF_RUNS; i++) {
            long nextId = decoratedSequence.getNextId();
            assertFalse(usedIds.contains(nextId));
            usedIds.add(nextId);
            usedIdsInOrder.add(nextId);
        }

        LOGGER.info("Test decorator shuffling successful! Elapsed time: {} s", TimeUtils.formatNanosAsSeconds(System.nanoTime() - startTimeNanos));

        logUsedIds(usedIdsInOrder);

    }

    /**
     * Logs the used ids if logging is enabled
     * 
     * @param usedIdsInOrder
     */
    private void logUsedIds(List<Long> usedIdsInOrder) {
        if (LOGGER.isDebugEnabled()) {
            StringBuilder sb = new StringBuilder();
            sb.append("All used IDs:");
            for (Long l : usedIdsInOrder) {
                sb.append("\n" + l);
            }
            LOGGER.debug(sb.toString());
        }
    }

}
