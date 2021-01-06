//@formatter:off
/*
 * ChunkedSortedKeyPosWriter
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
package de.calamanari.pk.muhai.collider;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link OrderedChunkFilewriter} writes items to files, so that a single file will not contain more that a specified number of items.<br />
 * The items will be written according to their natural order, so that a single chunk file will appear sorted.
 * <p>
 * The chunks will be g-zipped to safe disk space.
 * <p>
 * Instances are NOT safe to be accessed by multiple threads concurrently.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 * @param <E> type of the items
 */
public class OrderedChunkFilewriter<E extends Comparable<E>> implements Closeable {

    private static final Logger LOGGER = LoggerFactory.getLogger(OrderedChunkFilewriter.class);

    /**
     * Buffer for reading and writing files
     */
    private static final int IO_BUFFER_BYTES = 5_000_000;

    /**
     * Emergency stop at 5GB, it can create a big mess if the disk runs out of space ;)
     */
    public static final long LOW_DISK_SPACE_LIMIT_GB = 5;

    /**
     * buffer size
     */
    private final int maxItemsInMemory;

    /**
     * chunk file size
     */
    private final int maxItemsInChunk;

    /**
     * directory for writing chunks
     */
    private final File outputDir;

    /**
     * in-memory buffer for pending items to be written to a file
     */
    private final List<E> buffer;

    /**
     * List of created chunks
     */
    private final List<File> chunkFiles = new ArrayList<>();

    /**
     * Converter to write/read stringified items
     */
    private final ItemStringCodec<E> codec;

    /**
     * file name prefix to identify the process and to distinguish files
     */
    private final String fileNamePrefix;

    /**
     * counts the items in the current chunk file to limit the file size
     */
    private long numberOfItemsInCurrentChunk = 0;

    /**
     * count of chunks, start with 1
     */
    private int chunkNumber = 0;

    /**
     * Total number of items written to a chunk (excl. pending buffered items)
     */
    private long numberOfItemsWritten = 0;

    /**
     * Creates a new writer, all parameters are mandatory, not null
     * @param codec
     * @param outputDir
     * @param fileNamePrefix
     * @param maxKeysInMemory
     * @param maxKeysInChunk this should be a multiple of maxKeysInMemory
     */
    public OrderedChunkFilewriter(ItemStringCodec<E> codec, File outputDir, String fileNamePrefix, int maxKeysInMemory, int maxKeysInChunk) {
        this.codec = codec;
        this.maxItemsInChunk = maxKeysInChunk;
        this.maxItemsInMemory = maxKeysInMemory;
        this.outputDir = outputDir;
        this.buffer = new ArrayList<>(maxKeysInMemory);
        this.fileNamePrefix = fileNamePrefix;
        if (maxKeysInMemory > maxKeysInChunk) {
            throw new IllegalArgumentException(String.format("Chunk size must be larger than memory buffer size, given: maxKeysInMemory=%d, maxKeysInChunk=%d",
                    maxKeysInMemory, maxKeysInChunk));
        }
    }

    /**
     * Creates a new chunk file and drains the buffer
     * @throws IOException
     */
    private void writeBufferedItemsToNewChunkFile() throws IOException {
        ensureEnoughDiskSpace();
        chunkNumber++;
        String chunkId = "00000" + chunkNumber;
        chunkId = chunkId.substring(chunkId.length() - 5);

        File chunkFile = new File(outputDir, String.join("", fileNamePrefix, "chunk-", chunkId, ".gz"));
        LOGGER.debug("Creating new chunk file: {} with {} items ...", chunkFile, buffer.size());
        try (FileOutputStream fos = new FileOutputStream(chunkFile);
                BufferedOutputStream bos = new BufferedOutputStream(fos, IO_BUFFER_BYTES);
                GZIPOutputStream gos = new GZIPOutputStream(bos);
                OutputStreamWriter osw = new OutputStreamWriter(gos, StandardCharsets.UTF_8);
                BufferedWriter destWriter = new BufferedWriter(osw)) {
            for (int i = 0; i < buffer.size(); i++) {
                if (i > 0) {
                    destWriter.newLine();
                }
                destWriter.write(codec.itemToString(buffer.get(i)));
            }
        }
        chunkFiles.add(chunkFile);
        numberOfItemsInCurrentChunk = buffer.size();
        numberOfItemsWritten = numberOfItemsWritten + buffer.size();
        buffer.clear();
        LOGGER.debug("New chunk file created, {} items written in total.", numberOfItemsWritten);
    }

    /**
     * Merges the current buffer into the latest chunk in the list
     * @throws IOException
     */
    private void mergeBufferedItemsIntoCurrentChunkFile() throws IOException {
        ensureEnoughDiskSpace();
        File chunkFile = chunkFiles.get(chunkNumber - 1);
        File tmpFile = new File(outputDir, chunkFile.getName() + "-merge");
        if (!chunkFile.renameTo(tmpFile)) {
            throw new IOException(String.format("Unable to prepare merge, could not rename %s to %s", chunkFile.getAbsolutePath(), tmpFile.toString()));
        }
        LOGGER.debug("Merging {} buffered items into chunk file {} containing already {} items ...", buffer.size(), chunkFile, numberOfItemsInCurrentChunk);
        try (FileOutputStream fos = new FileOutputStream(chunkFile);
                BufferedOutputStream bos = new BufferedOutputStream(fos, IO_BUFFER_BYTES);
                GZIPOutputStream gos = new GZIPOutputStream(bos);
                OutputStreamWriter osw = new OutputStreamWriter(gos, StandardCharsets.UTF_8);
                BufferedWriter destWriter = new BufferedWriter(osw);
                FileInputStream fis = new FileInputStream(tmpFile);
                BufferedInputStream bis = new BufferedInputStream(fis, IO_BUFFER_BYTES);
                GZIPInputStream gis = new GZIPInputStream(bis);
                InputStreamReader isr = new InputStreamReader(gis, StandardCharsets.UTF_8);
                BufferedReader sourceReader = new BufferedReader(isr)) {
            Iterator<E> bufferIterator = buffer.iterator();
            Iterator<E> tempFileIterator = new ItemConversionIterator<>(sourceReader, codec);
            Iterator<E> combinedIterator = new CombinedOrderedItemIterator<>(Arrays.asList(bufferIterator, tempFileIterator));
            boolean subsequent = false;
            while (combinedIterator.hasNext()) {
                if (subsequent) {
                    destWriter.newLine();
                }
                destWriter.write(codec.itemToString(combinedIterator.next()));
                subsequent = true;
            }
        }
        Files.delete(tmpFile.toPath());
        numberOfItemsInCurrentChunk = numberOfItemsInCurrentChunk + buffer.size();
        numberOfItemsWritten = numberOfItemsWritten + buffer.size();
        buffer.clear();
        LOGGER.debug("Merge complete, {} items written in total.", numberOfItemsWritten);
    }

    /**
     * Check remaining space in file system
     * @throws IOException if the space runs below {@link #LOW_DISK_SPACE_LIMIT_GB}
     */
    private void ensureEnoughDiskSpace() throws IOException {
        if (outputDir.getUsableSpace() < (LOW_DISK_SPACE_LIMIT_GB * 1_073_741_824L)) {
            throw new IOException("The file system reports less than " + LOW_DISK_SPACE_LIMIT_GB + " GB left on device, aborting!");
        }
    }

    /**
     * Writes any pending items processed by {@link #writeItem(Comparable)} got to a chunk file
     * @throws IOException
     */
    public synchronized void flush() throws IOException {
        if (!buffer.isEmpty()) {
            Collections.sort(buffer);
            if (numberOfItemsInCurrentChunk > 0 && buffer.size() + numberOfItemsInCurrentChunk <= maxItemsInChunk) {
                mergeBufferedItemsIntoCurrentChunkFile();
            }
            else {
                writeBufferedItemsToNewChunkFile();
            }
        }
    }

    /**
     * Writes the given item
     * @param item
     * @throws IOException
     */
    public synchronized void writeItem(E item) throws IOException {
        if (item == null) {
            throw new IllegalArgumentException("null is not a valid item");
        }
        if (buffer.size() < maxItemsInMemory) {
            buffer.add(item);
        }
        else {
            this.flush();
            buffer.add(item);
        }
    }

    @Override
    public void close() throws IOException {
        this.flush();
    }

    /**
     * Returns the total number of items written to chunks. When writing huge numbers of items, the return value can be negative. In this case it should be
     * treated as an unsigned long representing the correct value.
     * @return total number of items written (treat as unsigned)
     */
    public long getNumberOfItemsWritten() {
        return numberOfItemsWritten;
    }

    /**
     * @return unmodifiable list of the created chunk files
     */
    public List<File> getChunkFiles() {
        return Collections.unmodifiableList(chunkFiles);
    }

}
