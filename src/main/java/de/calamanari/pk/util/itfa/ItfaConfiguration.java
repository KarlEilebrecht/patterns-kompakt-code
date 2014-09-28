//@formatter:off
/*
 * Indexed Text File Accessor Configuration
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
package de.calamanari.pk.util.itfa;

import java.io.Serializable;

import de.calamanari.pk.util.pfis.BufferType;

/**
 * {@link ItfaConfiguration} covers several settings for the indexer.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class ItfaConfiguration implements Cloneable, Serializable {

    /**
     * for serialization
     */
    private static final long serialVersionUID = 1019452617131179610L;

    /**
     * Default maximum numbers of entries in the file index for character positions <br>
     * value={@value}
     */
    public static final int DEFAULT_MAX_NUMBER_OF_CHAR_INDEX_ENTRIES = 10_000;

    /**
     * Default maximum numbers of entries in the file index for line positions<br>
     * value={@value}
     */
    public static final int DEFAULT_MAX_NUMBER_OF_LINE_INDEX_ENTRIES = 10_000;

    /**
     * Default buffer size in bytes for readers returned by the textfile accessor.<br>
     * value={@value}
     */
    public static final int DEFAULT_CHILD_READER_BUFFER_SIZE = 2048;

    /**
     * Default buffer for character reading (number of characters) <br>
     * value={@value}
     */
    public static final int DEFAULT_CHAR_BUFFER_SIZE = 2_500_000;

    /**
     * Default threshold (file size in bytes) for using multiple threads
     */
    public static final int DEFAULT_MULTI_THREADING_THRESHOLD = 50_000;

    /**
     * Default maximum buffer size in bytes while indexing.<br>
     * value={@value}
     */
    public static final int DEFAULT_INDEXER_READ_BUFFER_SIZE = 52_428_800;

    /**
     * Default buffer type to be used by the indexer.<br>
     * value=MEMORY_MAPPED
     */
    public static final BufferType DEFAULT_INDEXER_READ_BUFFER_TYPE = BufferType.MEMORY_MAPPED;

    /**
     * Maximum numbers of entries in the file index for character positions <br>
     * see {@link #DEFAULT_MAX_NUMBER_OF_CHAR_INDEX_ENTRIES}
     */
    public int maxNumberOfCharIndexEntries = DEFAULT_MAX_NUMBER_OF_CHAR_INDEX_ENTRIES;

    /**
     * Maximum numbers of entries in the file index for line positions <br>
     * see {@link #DEFAULT_MAX_NUMBER_OF_LINE_INDEX_ENTRIES}
     */
    public int maxNumberOfLineIndexEntries = DEFAULT_MAX_NUMBER_OF_LINE_INDEX_ENTRIES;

    /**
     * Buffer size in bytes for readers returned by the textfile accessor.<br>
     * see {@link #DEFAULT_CHILD_READER_BUFFER_SIZE}
     */
    public int childReaderBufferSize = DEFAULT_CHILD_READER_BUFFER_SIZE;

    /**
     * Buffer for character reading (number of characters) <br>
     * see {@link #DEFAULT_CHAR_BUFFER_SIZE}
     */
    public int charBufferSize = DEFAULT_CHAR_BUFFER_SIZE;

    /**
     * Threshold (file size in bytes) for using multiple threads<br>
     * see {@link #DEFAULT_MULTI_THREADING_THRESHOLD}
     */
    public int multiThreadingThreshold = DEFAULT_MULTI_THREADING_THRESHOLD;

    /**
     * Maximum buffer size in bytes while indexing.<br>
     * see {@link #DEFAULT_INDEXER_READ_BUFFER_SIZE}
     */
    public int indexerReadBufferSize = DEFAULT_INDEXER_READ_BUFFER_SIZE;

    /**
     * Buffer type to be used by the indexer.<br>
     * see {@link #DEFAULT_INDEXER_READ_BUFFER_TYPE}
     */
    public BufferType indexerReadBufferType = DEFAULT_INDEXER_READ_BUFFER_TYPE;

    /**
     * BOM-size (default is 0).<br>
     * IndexedTextFileAccessor does not support charset auto-detection but can ignore any byte order mark by skipping a specified number of bytes. However, the
     * job to decide whether or not there is a byte order mark is up to the caller, see also: http://bugs.sun.com/view_bug.do?bug_id=4508058
     */
    public int bomSize = 0;

    /**
     * This method checks the values of certain arguments and throws an Exception if they don't match expectations.
     * 
     * @throws IllegalArgumentException if current settings are discouraged
     */
    public void validate() {
        if (maxNumberOfCharIndexEntries < 1) {
            throw new IllegalArgumentException("Argument maxNumberOfCharIndexEntries=" + maxNumberOfCharIndexEntries + " is too small.");
        }

        if (maxNumberOfLineIndexEntries < 1) {
            throw new IllegalArgumentException("Argument maxNumberOfLineIndexEntries=" + maxNumberOfLineIndexEntries + " is too small.");
        }

        if (bomSize < 0) {
            throw new IllegalArgumentException("Argument bomSize must not be negative, given: " + bomSize);
        }

        if (childReaderBufferSize < 0) {
            throw new IllegalArgumentException("Argument childReaderBufferSize must not be negative, given: " + childReaderBufferSize);
        }
    }

    @Override
    public Object clone() {
        ItfaConfiguration configuration = null;
        try {
            configuration = (ItfaConfiguration) super.clone();
        }
        catch (CloneNotSupportedException ex) {
            // won't happen
        }
        return configuration;
    }

}