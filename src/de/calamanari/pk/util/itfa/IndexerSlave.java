/*
 * Indexer Slave
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
package de.calamanari.pk.util.itfa;

import static de.calamanari.pk.util.CharsetUtils.CARRIAGE_RETURN_CODE;
import static de.calamanari.pk.util.CharsetUtils.LINE_BREAK_CODE;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

import de.calamanari.pk.util.CharsetUtils;

/**
 * Indexer Slave<br>
 * Indexing is implemented using the MASTER-SLAVE pattern. While the master (see {@link IndexerMaster}) splits the work
 * into parts the slaves perform the indexing. Finally the master collects the partial results and sets up the full
 * index.<br>
 * Slaves can be recycled.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
final class IndexerSlave implements Runnable {

    /**
     * Number of index entries this slave may use for characters
     */
    public int maxNumberOfCharEntries;

    /**
     * Number of index entries this slave may use for lines
     */
    public int maxNumberOfLineEntries;

    /**
     * Position within the partition where the slave shall start indexing
     */
    public int startIdx;

    /**
     * Size of the slaves workspace within the partition
     */
    public int subPartitionSize;

    /**
     * Current partition
     */
    public char[] partition;

    /**
     * Result of this slave
     */
    public final AtomicReference<IndexerSlaveResult> result = new AtomicReference<>();

    /**
     * lookup to re-calculate the length in bytes of a particular character
     */
    private final byte[] charLengthLookup;

    /**
     * Errors will be memorized here to be propagated to/by the master
     */
    public final AtomicReference<Throwable> propagateError = new AtomicReference<>();

    /**
     * The countdown latch is a slave-to-master signal, the master waits until all slaves have completed
     */
    public CountDownLatch latch;

    /**
     * Creates new slave
     * @param charLengthLookup character length lookup used while indexing
     */
    public IndexerSlave(byte[] charLengthLookup) {
        this.charLengthLookup = charLengthLookup;
    }

    @Override
    public void run() {

        // This method is pretty long, of course you could extract smaller methods
        // if you created a new IndexerSlaveState object covering all the flags
        // and position data required/modified during the run.
        // Good starting points to try extraction are the if-blocks inside the for-loop.
        //
        // But be careful:
        //
        // (1) The for-loop below runs per PER CHARACTER!
        // One or more calls to extracted methods million times will have
        // an impact on performance. Try it out ...
        //
        // (2) The computation below is not trivial, be sure not to scatter
        // logic for the sake of short methods. If you cannot find any
        // reasonable name for an extracted method, you are probably
        // just going to obfuscate your code!
        //
        // Conclusion: This is a classical dilemma, the method looks ugly,
        // but refactoring may have a bad impact on performance
        // and readability.

        try {
            // copy instructions from master and prepare local data structures
            final int currentMaxNumberOfCharEntries =
                    (this.maxNumberOfCharEntries <= subPartitionSize ? this.maxNumberOfCharEntries
                            : this.subPartitionSize);
            final int currentMaxNumberOfLineEntries =
                    (this.maxNumberOfLineEntries <= subPartitionSize ? this.maxNumberOfLineEntries
                            : this.subPartitionSize);
            final int currentStartIdx = this.startIdx;
            final int currentSubPartitionSize = this.subPartitionSize;
            final char[] currentPartition = this.partition;
            final long[][] charIndex = new long[currentMaxNumberOfCharEntries][];
            final long[][] lineIndex = new long[currentMaxNumberOfLineEntries][];
            final int charEntryDistance = currentMaxNumberOfCharEntries == 0 ? 0 : (int) Math
                    .floor((double) currentSubPartitionSize
                            / (double) currentMaxNumberOfCharEntries);
            final int lineEntryDistance = currentMaxNumberOfLineEntries == 0 ? 0 : (int) Math
                    .floor((double) currentSubPartitionSize
                            / (double) currentMaxNumberOfLineEntries);

            int availableCharEntries = charEntryDistance == 0 ? 0 : currentSubPartitionSize / charEntryDistance;
            int availableLineEntries = lineEntryDistance == 0 ? 0 : currentSubPartitionSize / lineEntryDistance;

            int backupCharEntries = currentMaxNumberOfCharEntries - availableCharEntries;
            int backupLineEntries = currentMaxNumberOfLineEntries - availableLineEntries;

            // Line breaks may consist of a carriage return optionally followed by a line feed.
            // To handle both options, a flag helps.
            boolean lastCharWasCR = false;
            long bytePosAfterCR = 0L;
            boolean lastCharWasIndexed = false;

            int charEntryCount = 0;
            int lineEntryCount = 0;
            long numberOfCharactersRead = 0L;
            long numberOfLinesRead = 0L;

            long currentBytePos = 0L;
            long lastBytePos = 0L;
            long beforeLastBytePos = 0L;

            long lastIndexedLineStartCharNumber = 0L;

            long lastIndexedCharNumber = 0L;

            int read = 0;
            for (int i = 0; i < currentSubPartitionSize; i++) {
                read = (int) currentPartition[currentStartIdx + i];

                // increase the byte position using reverse lookup
                currentBytePos = currentBytePos + charLengthLookup[read];

                lastCharWasIndexed = false;

                if (read == LINE_BREAK_CODE || lastCharWasCR) {
                    // new Line
                    numberOfLinesRead++;
                    long newLineStart = currentBytePos;
                    long newLineStartCharNumber = numberOfCharactersRead;

                    if (read != LINE_BREAK_CODE) {
                        // this position WAS already the line start
                        newLineStart = bytePosAfterCR;
                        newLineStartCharNumber--;
                    }
                    if (currentMaxNumberOfLineEntries > lineEntryCount
                            && (newLineStartCharNumber == 0
                                    || (newLineStartCharNumber - lastIndexedLineStartCharNumber >= lineEntryDistance)
                                    || (backupLineEntries > 0 && (newLineStartCharNumber
                                    - lastIndexedLineStartCharNumber >= lineEntryDistance / 2)))) {
                        if (newLineStartCharNumber - lastIndexedLineStartCharNumber < lineEntryDistance) {
                            backupLineEntries--;
                        }
                        lineEntryCount++;
                        lineIndex[lineEntryCount - 1] = new long[] { numberOfLinesRead, newLineStart };
                        // lastLineStartPos = newLineStart;
                        lastIndexedLineStartCharNumber = newLineStartCharNumber;
                    }
                }
                if (read == CARRIAGE_RETURN_CODE) {
                    lastCharWasCR = true;
                    bytePosAfterCR = currentBytePos;
                }
                else {
                    lastCharWasCR = false;
                }
                // never create entries for low surrogate characters (56320-57343), because it is impossible to
                // directly read (decode) them without reading the mandatory high surrogate before;
                // low surrogates don't have any absolute position
                if ((read < CharsetUtils.MIN_HIGH_SURROGATE_CODE || read > CharsetUtils.MAX_SURROGATE_CODE)
                        && currentMaxNumberOfCharEntries > charEntryCount
                        && (numberOfCharactersRead == 0
                                || numberOfCharactersRead - lastIndexedCharNumber >= charEntryDistance 
                                || (backupCharEntries > 0 && numberOfCharactersRead
                                - lastIndexedCharNumber >= charEntryDistance / 2))) {
                    if (numberOfCharactersRead - lastIndexedCharNumber < charEntryDistance) {
                        backupCharEntries--;
                    }
                    charEntryCount++;
                    charIndex[charEntryCount - 1] = new long[] { numberOfCharactersRead, lastBytePos };
                    // lastCharPos = lastBytePos;
                    lastCharWasIndexed = true;
                    lastIndexedCharNumber = numberOfCharactersRead;
                }
                numberOfCharactersRead++;
                beforeLastBytePos = lastBytePos;
                lastBytePos = currentBytePos;
            }

            if (!lastCharWasIndexed && charEntryCount < currentMaxNumberOfCharEntries 
                    && (read < CharsetUtils.MIN_HIGH_SURROGATE_CODE || read > CharsetUtils.MAX_SURROGATE_CODE)) {
                charEntryCount++;
                charIndex[charEntryCount - 1] = new long[] { numberOfCharactersRead - 1, beforeLastBytePos };
            }
            if (lastCharWasCR) {
                numberOfLinesRead++;
                if (lineEntryCount < currentMaxNumberOfLineEntries) {
                    lineEntryCount++;
                    lineIndex[lineEntryCount - 1] = new long[] { numberOfLinesRead, bytePosAfterCR };
                }
            }

            // slave results for master
            IndexerSlaveResult localResult = new IndexerSlaveResult(charIndex, lineIndex, currentBytePos,
                    numberOfCharactersRead, numberOfLinesRead, charEntryCount, lineEntryCount);
            this.result.set(localResult);
        }
        catch (Throwable e) {
            e.printStackTrace();
            this.propagateError.set(e);
        }
        finally {
            this.latch.countDown();
        }

    }

}