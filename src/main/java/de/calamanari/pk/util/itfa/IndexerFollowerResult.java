//@formatter:off
/*
 * Indexer Follower Result
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
//@formatter:off
package de.calamanari.pk.util.itfa;

/**
 * Result from an indexer follower
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
final class IndexerFollowerResult {

    /**
     * The folllower uses a simple array to store the index positions: charIndex[1][235][280] means: second index entry,
     * character no. 235 at byte position 280
     */
    public final long[][] charIndex;

    /**
     * The follower uses a simple array to store the index positions: lineIndex[7][3895][107898] means: 8th index entry,
     * line no. 3895 starts at byte position 107898
     */
    public final long[][] lineIndex;

    /**
     * number of bytes read (total)
     */
    public final long numberOfBytesProcessed;

    /**
     * number of characters read (total)
     */
    public final long numberOfCharactersRead;

    /**
     * number of lines read (total)
     */
    public final long numberOfLinesRead;

    /**
     * number of entries in charIndex
     */
    public final int numberOfCharIndexEntries;

    /**
     * number of entries in lineIndex
     */
    public final int numberOfLineIndexEntries;

    /**
     * Creates new result
     * @param charIndex file positions
     * @param lineIndex file positions
     * @param numberOfBytesProcessed count
     * @param numberOfCharactersRead count
     * @param numberOfLinesRead count
     * @param numberOfCharIndexEntries count
     * @param numberOfLineIndexEntries count
     */
    public IndexerFollowerResult(long[][] charIndex, long[][] lineIndex, long numberOfBytesProcessed,
            long numberOfCharactersRead, long numberOfLinesRead, int numberOfCharIndexEntries,
            int numberOfLineIndexEntries) {
        this.charIndex = charIndex;
        this.lineIndex = lineIndex;
        this.numberOfBytesProcessed = numberOfBytesProcessed;
        this.numberOfCharactersRead = numberOfCharactersRead;
        this.numberOfLinesRead = numberOfLinesRead;
        this.numberOfCharIndexEntries = numberOfCharIndexEntries;
        this.numberOfLineIndexEntries = numberOfLineIndexEntries;
    }

}