//@formatter:off
/*
 * Indexer Leader Result
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
package de.calamanari.pk.util.itfa;

import java.util.Map;

/**
 * Result from an indexer leader (a completed indexer run)
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
final class IndexerLeaderResult {

    /**
     * Stores the mapping character position to file position
     */
    Map<Long, Long> characterPositionIndex;

    /**
     * Stores the mapping line position to file position
     */
    Map<Long, Long> linePositionIndex;

    /**
     * number of entries in the character position index
     */
    int numberOfCharacterIndexEntries;

    /**
     * number of entries in the line position index
     */
    int numberOfLineIndexEntries;

    /**
     * Number of lines in file
     */
    long numberOfLines;

    /**
     * sorted array of indexed line numbers in index
     */
    long[] indexedLineNumbers;

    /**
     * sorted array of indexed character numbers in index
     */
    long[] indexedCharNumbers;

    /**
     * Number of characters in file
     */
    long numberOfCharacters;

    /**
     * size of the file in bytes
     */
    long fileSize;
}
