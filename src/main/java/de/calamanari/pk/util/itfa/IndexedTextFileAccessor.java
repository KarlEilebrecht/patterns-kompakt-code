//@formatter:off
/*
 * Indexed Text File Accessor
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

import static de.calamanari.pk.util.CharsetUtils.CARRIAGE_RETURN_CODE;
import static de.calamanari.pk.util.CharsetUtils.CONTEXT_AGNOSTIC_CHARSETS;
import static de.calamanari.pk.util.CharsetUtils.LINE_BREAK_CODE;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.io.Reader;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.util.Map;

import de.calamanari.pk.util.CharsetUtils;
import de.calamanari.pk.util.CloseUtils;

/**
 * Indexed Text File Accessor creates an index for a given file and allows to jump to specific characters or lines.<br>
 * This is intended for reading large text files which won't be modified after index creation.<br>
 * An instance is safe to be used concurrently by multiple threads.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class IndexedTextFileAccessor {

    /**
     * Default configuration (not public because configurations are mutable)
     */
    private static final ItfaConfiguration DEFAULT_CONFIGURATION = new ItfaConfiguration();

    /**
     * Stores the mapping character position to file position
     */
    private final Map<Long, Long> characterPositionIndex;

    /**
     * Stores the mapping line position to file position
     */
    private final Map<Long, Long> linePositionIndex;

    /**
     * size of the indexed file
     */
    private final long fileSize;

    /**
     * number of entries in the character position index
     */
    private final int numberOfCharacterIndexEntries;

    /**
     * number of entries in the line position index
     */
    private final int numberOfLineIndexEntries;

    /**
     * Number of lines in file
     */
    private final long numberOfLines;

    /**
     * sorted array of indexed line numbers in index
     */
    private final long[] indexedLineNumbers;

    /**
     * sorted array of indexed character numbers in index
     */
    private final long[] indexedCharNumbers;

    /**
     * Number of characters in file
     */
    private final long numberOfCharacters;

    /**
     * The file the indexed file belongs to
     */
    private final File sourceFile;

    /**
     * name of the charset used to read the file
     */
    private final String charsetName;

    /**
     * To enable users of this instance to get the physical byte position the last managed position is stored here.
     */
    private final ThreadLocal<Long> lastKnownFilePosition = new ThreadLocal<>();

    /**
     * Buffer size in bytes for readers returned by the textfile accessor.
     */
    private final int defaultChildReaderBufferSize;

    /**
     * A lookup for all character codes to the length (in bytes) of the character represented using the current charset.
     */
    private final byte[] charLengthLookup;

    /**
     * Helper method to create configuration
     * 
     * @param maxNumberOfCharIndexEntries maximum number of entries in the character index
     * @param maxNumberOfLineIndexEntries maximum number of entries in the line index
     * @param bomSize number of inital bytes to be skipped
     * @return new default configuration with the given changes applied
     */
    private static ItfaConfiguration createConfiguration(int maxNumberOfCharIndexEntries, int maxNumberOfLineIndexEntries, int bomSize) {
        ItfaConfiguration res = new ItfaConfiguration();
        res.bomSize = bomSize;
        res.maxNumberOfCharIndexEntries = maxNumberOfCharIndexEntries;
        res.maxNumberOfLineIndexEntries = maxNumberOfLineIndexEntries;
        return res;
    }

    /**
     * Creates new IndexedTextFileAccessor of default size <br>
     * see {@link #IndexedTextFileAccessor(File, String, ItfaConfiguration)}, {@link #DEFAULT_CONFIGURATION}
     * 
     * @param file readable file
     * @param charsetName name of character set
     * @throws IOException on any error while indexing the file
     */
    public IndexedTextFileAccessor(File file, String charsetName) throws IOException {
        this(file, charsetName, null);
    }

    /**
     * Creates new IndexedTextFileAccessor, this opens the file followed by an index run, does NOT keep the file open.<br>
     * By specifying the maximum number of index entries the caller has some control over the memory consumption. One entry consists of 3 Longs accompanied by
     * some overhead.<br>
     * This implementation does not support charset auto-detection but can ignore byte order mark by skipping a specified number of bytes. However, the job to
     * decide whether or not there is a byte order mark is up to the caller, see also: http://bugs.sun.com/view_bug.do?bug_id=4508058
     * 
     * @param file readable file
     * @param charsetName name of character set
     * @param maxNumberOfCharIndexEntries maximum number of index entries for characters (min=1)
     * @param maxNumberOfLineIndexEntries maximum number of index entries for lines (min=1)
     * @param bomSize number of bytes to be skipped
     * @throws IOException on any error while indexing the file
     */
    public IndexedTextFileAccessor(File file, String charsetName, int maxNumberOfCharIndexEntries, int maxNumberOfLineIndexEntries, int bomSize)
            throws IOException {
        this(file, charsetName, createConfiguration(maxNumberOfCharIndexEntries, maxNumberOfLineIndexEntries, bomSize));
    }

    /**
     * Creates new IndexedTextFileAccessor, this opens the file followed by an index run, does NOT keep the file open.<br>
     * 
     * @param file readable file
     * @param charsetName name of character set
     * @param configuration indexer settings, null means default
     * @throws IOException on any error while indexing the file
     */
    public IndexedTextFileAccessor(File file, String charsetName, ItfaConfiguration configuration) throws IOException {

        if (configuration == null) {
            configuration = DEFAULT_CONFIGURATION;
        }
        else {
            configuration.validate();
        }

        if (!CONTEXT_AGNOSTIC_CHARSETS.contains(charsetName.toLowerCase())) {
            throw new IOException("Error: charset '" + charsetName + "' not supported.");
        }

        this.charsetName = charsetName;
        this.charLengthLookup = CharsetUtils.createCharLengthLookup(charsetName);
        this.defaultChildReaderBufferSize = configuration.childReaderBufferSize;
        this.sourceFile = file;

        IndexerLeader leader = new IndexerLeader(configuration, file, charsetName, charLengthLookup);
        IndexerLeaderResult indexData = leader.createIndex();

        this.fileSize = indexData.fileSize;
        this.characterPositionIndex = indexData.characterPositionIndex;
        this.linePositionIndex = indexData.linePositionIndex;
        this.numberOfCharacterIndexEntries = indexData.numberOfCharacterIndexEntries;
        this.numberOfLineIndexEntries = indexData.numberOfLineIndexEntries;
        this.numberOfCharacters = indexData.numberOfCharacters;
        this.numberOfLines = indexData.numberOfLines;
        this.indexedLineNumbers = indexData.indexedLineNumbers;
        this.indexedCharNumbers = indexData.indexedCharNumbers;
    }

    /**
     * Creates a new UNBUFFERED InputStreamReader at the specified file position (byte).
     * 
     * @param filePosition number of the byte the new Reader shall start reading at, see also {@link #getLastKnownFilePosition()}
     * @return new InputStreamReader, MUST BE CLOSED BY CALLER!
     * @throws IOException on file access problems
     */
    // This method's job is to return an open resource, thus suppressing SonarLint complaint
    @SuppressWarnings("squid:S2093")
    public InputStreamReader createInputStreamReaderAtFilePosition(long filePosition) throws IOException {
        InputStreamReader isr = null;
        FileChannel channel = null;
        InputStream is = null;
        try {
            channel = createChannelAndSetPosition(filePosition);
            is = Channels.newInputStream(channel);
            isr = new InputStreamReader(is, charsetName);
            this.lastKnownFilePosition.set(filePosition);
        }
        finally {
            if (isr == null) {
                CloseUtils.closeResourceCatch(is, channel);
            }
        }
        return isr;
    }

    /**
     * Creates the reader at the specified byte position, either buffered or not
     * 
     * @param filePosition byte position, where to start the input stream
     * @param bufferSize size of the buffer to be used, for 0 or less the result will be the same as {@link #createInputStreamReaderAtFilePosition(long)}
     * @return input stream reader
     * @throws IOException on file access problems
     */
    // This method's job is to return an open resource, thus suppressing SonarLint complaint
    @SuppressWarnings("squid:S2093")
    public Reader createInputStreamReaderAtFilePosition(long filePosition, int bufferSize) throws IOException {
        Reader isr = null;
        boolean ok = false;
        try {
            isr = createInputStreamReaderAtFilePosition(filePosition);
            if (bufferSize > 0) {
                isr = new BufferedReader(isr, bufferSize);
            }
            ok = true;
        }
        finally {
            if (!ok) {
                CloseUtils.closeResourceCatch(isr);
            }
        }
        return isr;
    }

    /**
     * Creates a new InputStreamReader at the given character position using default buffer size.
     * 
     * @param charNumber number of requested character (starts with 0), MUST BE CLOSED BY CALLER!
     * @return input stream reader starting with the requested character
     * @throws IOException on file access problems
     */
    public Reader createInputStreamReaderAtChar(long charNumber) throws IOException {
        return createInputStreamReaderAtChar(charNumber, this.defaultChildReaderBufferSize);
    }

    /**
     * Creates a new InputStreamReader at the given character position.
     * 
     * @param charNumber number of requested character (starts with 0), MUST BE CLOSED BY CALLER!
     * @param bufferSize for performance reasons a buffered reader will be used
     * @return input stream reader starting with the requested character
     * @throws IOException on file access problems
     */
    public Reader createInputStreamReaderAtChar(long charNumber, int bufferSize) throws IOException {
        Reader isr = null;
        if (charNumber >= this.numberOfCharacters) {
            throw new IndexOutOfBoundsException("charNumber=" + charNumber);
        }
        Long filePos = null;
        long ignoredCharacters = 0;

        long nearestIndexedCharNumber = findNearestIndexedKeyBefore(indexedCharNumbers, charNumber);
        if (nearestIndexedCharNumber > -1) {
            filePos = characterPositionIndex.get(nearestIndexedCharNumber);
            ignoredCharacters = charNumber - nearestIndexedCharNumber;
        }

        if (filePos != null) {
            // we don't want to risk that a resource is open and nobody can close it, see below.
            boolean ok = false;

            long skipped = 0;
            try {
                isr = createInputStreamReaderAtFilePosition(filePos, bufferSize);
                long[] bytePositionHolder = new long[] { 0L };
                skipped = skip(isr, ignoredCharacters, bytePositionHolder, charLengthLookup);
                this.lastKnownFilePosition.set(bytePositionHolder[0]);
                ok = true;
            }
            finally {
                if (!ok && isr != null || skipped < ignoredCharacters) {
                    CloseUtils.closeResourceCatch(isr);
                }
            }
            if (skipped < ignoredCharacters) {
                throw new IOException("Positioning failed (charNumber=" + charNumber + " unreachable) - file truncated since index creation?");
            }
        }
        else {
            throw new IndexOutOfBoundsException("charNumber=" + charNumber);
        }
        return isr;
    }

    /**
     * Creates a new InputStreamReader at the given line position using default buffer size.
     * 
     * @param lineNumber number of requested line (starts with 0)
     * @return input stream reader starting with the requested line, MUST BE CLOSED BY CALLER!
     * @throws IOException on file access problems
     */
    public Reader createInputStreamReaderAtLine(long lineNumber) throws IOException {
        return createInputStreamReaderAtLine(lineNumber, this.defaultChildReaderBufferSize);
    }

    /**
     * Creates a new InputStreamReader at the given line position.
     * 
     * @param lineNumber number of requested line (starts with 0)
     * @param bufferSize for performance reasons a buffered reader will be used
     * @return input stream reader starting with the requested line, MUST BE CLOSED BY CALLER!
     * @throws IOException on file access problems
     */
    public Reader createInputStreamReaderAtLine(long lineNumber, int bufferSize) throws IOException {
        Reader isr = null;
        if (lineNumber >= this.numberOfLines) {
            throw new IndexOutOfBoundsException("lineNumber=" + lineNumber);
        }
        Long filePos = null;
        long ignoredLines = 0;

        long nearestIndexedLineNumber = findNearestIndexedKeyBefore(indexedLineNumbers, lineNumber);
        if (nearestIndexedLineNumber > -1) {
            filePos = linePositionIndex.get(nearestIndexedLineNumber);
            ignoredLines = lineNumber - nearestIndexedLineNumber;
        }

        if (filePos != null) {
            // we don't want to risk that a resource is open and nobody can close it, see below.
            boolean ok = false;

            long skipped = 0;
            try {
                isr = createInputStreamReaderAtFilePosition(filePos, bufferSize);
                long[] bytePositionHolder = new long[] { 0L };
                skipped = skipLines(isr, ignoredLines, bytePositionHolder, charLengthLookup);
                this.lastKnownFilePosition.set(bytePositionHolder[0]);
                ok = true;
            }
            finally {
                if (!ok && isr != null || skipped < ignoredLines) {
                    CloseUtils.closeResourceCatch(isr);
                }
            }
            if (skipped < ignoredLines) {
                throw new IOException("Positioning failed (lineNumber=" + lineNumber + " unreachable) - file truncated since index creation?");
            }
        }
        else {
            throw new IndexOutOfBoundsException("lineNumber=" + lineNumber);
        }
        return isr;
    }

    /**
     * Returns the physical byte position for the given character position.<br>
     * <b>Note:</b>
     * <ul>
     * <li>Since this is no learning index, it might be a good idea to cache results externally.</li>
     * <li>Technically a low surrogate has no absolute byte position, thus the position of the low surrogate will be <i>equal to</i> the position of the
     * preceding high surrogate character!</li>
     * </ul>
     * See also: {@link #getLastKnownFilePosition()}
     * 
     * @param charNumber (starts with 0)
     * @return position of the first byte of that character in the file
     * @throws IOException on file access problems
     */
    public long getFilePositionAtChar(long charNumber) throws IOException {
        long res = -1;
        Long indexHit = characterPositionIndex.get(charNumber);
        if (indexHit != null) {
            res = indexHit.longValue();
        }
        else {
            try (Reader isr = createInputStreamReaderAtChar(charNumber)) {
                res = getLastKnownFilePosition();
            }
        }
        return res;
    }

    /**
     * Returns the physical byte position for the given line position.<br>
     * Note: Since this is no learning index, it might be a good idea to cache results externally.<br>
     * See also: {@link #getLastKnownFilePosition()}
     * 
     * @param lineNumber (starts with 0)
     * @return position line start
     * @throws IOException on file access problems
     */
    public long getFilePositionAtLine(long lineNumber) throws IOException {
        long res = -1;
        Long indexHit = linePositionIndex.get(lineNumber);
        if (indexHit != null) {
            res = indexHit.longValue();
        }
        else {
            try (Reader isr = createInputStreamReaderAtLine(lineNumber)) {
                res = getLastKnownFilePosition();
            }
        }
        return res;
    }

    /**
     * Returns the nearest smaller or equal indexed item number for the given key
     * 
     * @param indexedKeys sorted array of indexed keys
     * @param key search key
     * @return valid key
     */
    protected Long findNearestIndexedKeyBefore(long[] indexedKeys, long key) {
        if (key < 0) {
            throw new IllegalArgumentException("The given position (key) must be a positive value (given=" + key + ").");
        }
        int len = indexedKeys.length;
        long last = -1;
        for (int i = 0; i < len; i++) {
            long current = indexedKeys[i];
            if (current > key) {
                break;
            }
            last = current;
        }
        return Long.valueOf(last);
    }

    /**
     * Returns a new Channel
     * 
     * @return new Channel to sourceFile, MUST BE CLOSED by caller!
     * @throws FileNotFoundException if file was not available
     */
    // This method's job is to return an open resource, thus suppressing SonarLint complaint
    @SuppressWarnings({ "resource", "squid:S2095" })
    protected FileChannel createChannel() throws FileNotFoundException {
        return new RandomAccessFile(sourceFile, "r").getChannel();
    }

    /**
     * Creates new channel and sets the specified position.
     * 
     * @param filePosition requested position in the channel
     * @return new channel
     * @throws IOException on file access problems
     */
    public FileChannel createChannelAndSetPosition(long filePosition) throws IOException {
        FileChannel channel = createChannel();
        channel.position(filePosition);
        long realPosition = channel.position();
        if (realPosition != filePosition) {
            CloseUtils.closeResourceCatch(channel);
            throw new IndexOutOfBoundsException(
                    "Specified filePosition=" + filePosition + " is greater than the file size - file truncated after index creation?");
        }
        return channel;
    }

    /**
     * Returns the number of indexed characters.
     * 
     * @return number of index entries
     */
    public int getNumberOfCharacterIndexEntries() {
        return numberOfCharacterIndexEntries;
    }

    /**
     * Returns the number of indexed lines.
     * 
     * @return number of index entries
     */
    public int getNumberOfLineIndexEntries() {
        return numberOfLineIndexEntries;
    }

    /**
     * Returns the file size
     * 
     * @return size of file
     */
    public long getFileSize() {
        return fileSize;
    }

    /**
     * Returns the number of lines in the file
     * 
     * @return number of lines
     */
    public long getNumberOfLines() {
        return numberOfLines;
    }

    /**
     * Returns the number of characters in the file
     * 
     * @return number of characters
     */
    public long getNumberOfCharacters() {
        return numberOfCharacters;
    }

    /**
     * Whenever this instance returns a stream at a certain character or line position, it memorizes the related byte position.<br>
     * A caller may call this method subsequently to get the physical position. <br>
     * Since IndexedTextFileAccessor might be accessed concurrently by different threads, this information is managed thread-locally. Means: This method returns
     * the least recent position known to the callers thread.
     * <p>
     * <b>Note:</b><br>
     * Since there is no absolute byte position <i>after</i> a high surrogate character, the returned position after returning a high surrogate will be equal to
     * the position before the high surrogate while the position after the following low surrogate (after the pair) will be correct.
     * 
     * @return last known file position (byte-position) or -1 to indicate unknown
     */
    public long getLastKnownFilePosition() {
        long res = -1;
        Long memorizedPosition = lastKnownFilePosition.get();
        if (memorizedPosition != null) {
            res = memorizedPosition.longValue();
        }
        return res;
    }

    /**
     * Reads from the reader until the requested number of characters has been skipped or the end of the stream was reached.
     * 
     * @param reader input
     * @param numberOfCharactersToSkip number of characters to be skipped starting at the current position
     * @param bytePositionHolder a single-element long array, where the bytePositionHolder[0] will be incremented while reading characters
     * @param charLengthLookup the character length lookup maps the length of each character to its length in bytes according to a charset.
     * @return number of skipped characters
     * @throws IOException on file access problems
     */
    private static final long skip(Reader reader, long numberOfCharactersToSkip, long[] bytePositionHolder, byte[] charLengthLookup) throws IOException {

        int read = -1;
        long skipped = 0;
        long currentBytePosition = bytePositionHolder[0];

        while (numberOfCharactersToSkip > 0 && (read = reader.read()) != -1) {

            // determine the new exact position
            currentBytePosition = currentBytePosition + charLengthLookup[read];

            numberOfCharactersToSkip--;
            skipped++;
        }
        bytePositionHolder[0] = currentBytePosition;
        return skipped;
    }

    /**
     * Reads from the reader until the given number of lines has been skipped or the end of the stream was reached.
     * 
     * @param reader input
     * @param numberOfLinesToSkip specifies the number of lines to be skipped
     * @param bytePositionHolder a single-element long array, where the bytePositionHolder[0] will be incremented while reading characters
     * @param charLengthLookup the character length lookup maps the length of each character to its length in bytes according to a charset.
     * @return number of skipped lines
     * @throws IOException on file access problems
     */
    private static final long skipLines(Reader reader, long numberOfLinesToSkip, long[] bytePositionHolder, byte[] charLengthLookup) throws IOException {
        int read = -1;
        long skipped = 0;

        // last character was a carriage return
        boolean lastCharWasCR = false;

        long currentBytePosition = bytePositionHolder[0];

        while (numberOfLinesToSkip > 0 && (read = reader.read()) != -1) {

            // determine the new exact position
            currentBytePosition = currentBytePosition + charLengthLookup[read];

            if (read == LINE_BREAK_CODE || lastCharWasCR) {
                // new Line
                numberOfLinesToSkip--;
                skipped++;
            }
            if (read == CARRIAGE_RETURN_CODE) {
                lastCharWasCR = true;
            }
            else {
                lastCharWasCR = false;
            }
        }
        if (lastCharWasCR) {
            skipped++;
        }
        bytePositionHolder[0] = currentBytePosition;
        return skipped;
    }

    /**
     * If a caller does not need the text file accessor anymore, this method should be called to free thread-local resources.
     */
    public void cleanup() {
        lastKnownFilePosition.remove();
    }
}
