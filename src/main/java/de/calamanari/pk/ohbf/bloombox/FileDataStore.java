//@formatter:off
/*
 * FileDataStore
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

package de.calamanari.pk.ohbf.bloombox;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.calamanari.pk.util.CloseUtils;

/**
 * The {@link FileDataStore} is a {@link BloomBoxDataStore} implementation backed by a single file in the file system which just contains the bytes of all the
 * longs (encoded big endian) of the store. This format without any metadata is called BBS, and each query execution scans the file sequentially.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class FileDataStore implements BloomBoxDataStore {

    private static final long serialVersionUID = 1965732964635192092L;

    private static final Logger LOGGER = LoggerFactory.getLogger(FileDataStore.class);

    /**
     * Cache size for buffered reading during scans: {@value} bytes.
     */
    private static final int MAX_CACHE_BYTES = 8 * 2_000_000;

    /**
     * Dummy stream (NULL-OBJECT) to be returned if the stream is not open.
     */
    private static final BufferedOutputStream DUMMY_BEFORE = new BufferedOutputStream(new OutputStream() {

        @Override
        public void write(int b) throws IOException {
            throw new IOException("File not open! BUG!");
        }

    }, 1);

    /**
     * Dummy stream (NULL-OBJECT) to be returned after the store has been de-serialized.
     */
    private static final BufferedOutputStream DUMMY_AFTER_DESERIALIZATION = new BufferedOutputStream(new OutputStream() {

        @Override
        public void write(int b) throws IOException {
            throw new IOException("Store cannot be fed after de-serialization!");
        }

    }, 1);

    /**
     * file with the vector's bytes (big-endian encoded longs)
     */
    private File file;

    /**
     * Capacity of the store (number of records)
     */
    private final long numberOfRows;

    /**
     * size of a single bloom filter vector counted in longs
     */
    private final int vectorSize;

    /**
     * Buffer size for scans
     */
    private final int bufferSize;

    /**
     * position in the data file where the vector data starts
     */
    private final long offset;

    /**
     * The current output stream for feeding
     */
    private transient BufferedOutputStream output = DUMMY_BEFORE;

    /**
     * For recovery purposes only, restores the data store from the data only (without any headers)
     * 
     * @param vectorSize size of a single bloom filter vector counted in longs
     * @param numberOfRows Capacity of the store (number of records)
     * @param dataOnlyfile option to restore a bloom-box from a BBS file if the exact settings are known
     * @return recovered store
     */
    public static BloomBoxDataStore recover(int vectorSize, long numberOfRows, File dataOnlyfile) {
        FileDataStore res = new FileDataStore(vectorSize, numberOfRows, dataOnlyfile, 0L);
        res.output = DUMMY_AFTER_DESERIALIZATION;
        return res;
    }

    /**
     * Restores the file data store from the given stream (called by the {@link BloomBox} during de-serialization).<br>
     * The new BBS-file will be created in the 'dataDirectory' if this property is configured in the envSettings.<br>
     * Otherwise we try to restore the file in its original location.<br>
     * For performance reasons an existing file will be reused. Thus, in case of a damaged BBS-file being present in the data directory a manual clean-up may be
     * required to enforce restoring it from the box stream.
     * 
     * @param is source stream
     * @param header {@link FileDataStoreHeader}
     * @param envSettings global settings
     * @return restored data store
     */
    public static BloomBoxDataStore restore(InputStream is, DataStoreHeader header, Map<String, String> envSettings) {

        try {
            FileDataStoreHeader castedHeader = (FileDataStoreHeader) header;

            String boxFilePath = envSettings.get(BloomBox.ENV_BLOOM_BOX_RESTORE_FILE);

            File storeFile = null;
            long offset = 0L;
            if (boxFilePath != null) {
                storeFile = new File(boxFilePath);
                offset = Long.parseLong(envSettings.get(BloomBox.ENV_BLOOM_BOX_RESTORE_AFTER_HEADER_OFFSET));
            }
            else {
                storeFile = castedHeader.getFile();
            }
            FileDataStore res = new FileDataStore(header.getVectorSize(), header.getNumberOfRows(), storeFile, offset);
            res.output = DUMMY_AFTER_DESERIALIZATION;
            if (!storeFile.exists()) {
                String dataDirectoryName = envSettings.get("dataDirectory");
                if (dataDirectoryName != null) {
                    LOGGER.info("BloomBox dataDirectory={}", dataDirectoryName);
                    File parent = new File(dataDirectoryName);
                    storeFile = new File(parent, castedHeader.getFile().getName());
                    castedHeader.setFile(storeFile);
                }
                else {
                    LOGGER.warn("No BloomBox dataDirectory configured in environment settings, using information from header: {}", header);
                }
                restoreDataStoreFile(is, res, castedHeader);
            }
            else {
                LOGGER.info("Using {}.", res);
            }
            return res;

        }
        catch (BloomBoxException ex) {
            throw ex;
        }
        catch (RuntimeException ex) {
            throw new BloomBoxException(String.format("Error restoring data store %s.", header), ex);
        }
    }

    /**
     * Copies the data store from the input stream to the BBS file
     * 
     * @param is source stream
     * @param fileDataStore the store currently being restored
     * @param header store header
     */
    private static void restoreDataStoreFile(InputStream is, FileDataStore fileDataStore, FileDataStoreHeader header) {

        try {
            LOGGER.info("Restoring data store file {} ...", header);
            int inputBufferSize = Math.max(fileDataStore.bufferSize / 10, 1_000_000);
            try (FileOutputStream fos = new FileOutputStream(fileDataStore.file);
                    BufferedOutputStream bos = new BufferedOutputStream(fos, fileDataStore.bufferSize);
                    BufferedInputStream bis = new BufferedInputStream(is, inputBufferSize)) {
                bis.transferTo(fos);
            }
            LOGGER.info("Data store file restored.");
        }
        catch (IOException | RuntimeException ex) {
            throw new BloomBoxException(String.format("Error restoring data store %s.", header), ex);
        }

    }

    /**
     * @param vectorSize size of a single bloom filter vector counted in longs
     * @param numberOfRows Capacity of the store (number of records)
     * @param file destination of the BBS file to be created
     * @param offset where the vector data starts
     */
    public FileDataStore(int vectorSize, long numberOfRows, File file, long offset) {
        this.vectorSize = vectorSize;
        this.numberOfRows = numberOfRows;
        this.file = file;
        this.offset = offset;
        long vectorSizeInBytes = 8L * vectorSize;
        if (vectorSizeInBytes >= MAX_CACHE_BYTES) {
            this.bufferSize = MAX_CACHE_BYTES;
        }
        else {
            this.bufferSize = (int) ((MAX_CACHE_BYTES / vectorSizeInBytes) * vectorSizeInBytes);
        }
    }

    @Override
    public void close() {
        closeOutputStream();
    }

    /**
     * Closes the output stream after feeding
     */
    private void closeOutputStream() {
        if (output != null && this.output != DUMMY_BEFORE && this.output != DUMMY_AFTER_DESERIALIZATION) {
            try {
                output.close();
            }
            catch (IOException ex) {
                LOGGER.error("Error closing file {}", file, ex);
            }
            finally {
                output = null;
            }
        }
    }

    @Override
    public long getNumberOfRows() {
        return numberOfRows;
    }

    @Override
    public int getVectorSize() {
        return vectorSize;
    }

    @Override
    public void feedRow(long[] rowVector, long rowIdx) {
        try {
            byte[] buffer = new byte[8];
            for (int i = 0; i < vectorSize; i++) {
                BloomBox.longToBytes(rowVector[i], buffer);
                output.write(buffer);
            }
        }
        catch (IOException | RuntimeException ex) {
            throw new BloomBoxException(String.format("Error feeding data store '%s', rowIdx=%d", file, rowIdx), ex);
        }
    }

    @Override
    public boolean ensureIsOpenForFeeding() {
        boolean res = false;
        if (output == DUMMY_AFTER_DESERIALIZATION) {
            LOGGER.error("Attempt to feed {} after de-serialization, file was: {}", this.getClass().getSimpleName(), file);
        }
        else if (output == DUMMY_BEFORE) {
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(file);
                output = new BufferedOutputStream(fos, bufferSize);
                res = true;
            }
            catch (IOException | RuntimeException ex) {
                CloseUtils.closeResourceCatch(output, fos);
                throw new BloomBoxException(String.format("Unable to open %s on file '%s'", this.getClass().getSimpleName(), file), ex);
            }
        }
        else {
            res = true;
        }
        return res;
    }

    @Override
    public void notifyFeedingComplete() {
        this.closeOutputStream();

    }

    @Override
    public <Q extends QueryDelegate<Q>> void dispatch(Q queryDelegate) {
        long[] vector = new long[vectorSize];
        byte[] buffer = new byte[8];
        try (FileInputStream fis = new FileInputStream(file); BufferedInputStream bis = new BufferedInputStream(fis, bufferSize)) {
            if (offset > 0 && bis.skip(offset) < offset) {
                throw new IOException("Unable to skip to offset=%d" + offset);
            }
            for (long rowIdx = 0; rowIdx < numberOfRows; rowIdx++) {
                for (int i = 0; i < vectorSize; i++) {
                    int bytesFound = bis.read(buffer, 0, 8);
                    if (bytesFound != 8) {
                        throw new BloomBoxException(String.format("Error reading data store file '%s' (row %d, index %d): 8 bytes expected, found %d", file,
                                rowIdx, i, bytesFound));
                    }
                    vector[i] = BloomBox.bytesToLong(buffer);
                }
                queryDelegate.execute(vector, 0);
            }
        }
        catch (BloomBoxException ex) {
            throw ex;
        }
        catch (IOException | RuntimeException ex) {
            throw new BloomBoxException(String.format("Error reading data store file '%s'", file), ex);
        }

    }

    @Override
    public void serializeToStream(OutputStream os) throws IOException {
        DataStoreHeader header = new FileDataStoreHeader(BloomBox.VERSION, numberOfRows, vectorSize, this.getClass().getName(), file);
        try {
            HeaderUtil.writeDataStoreHeader(os, header);

            int outputBufferSize = Math.max(bufferSize / 10, 1_000_000);
            try (BufferedOutputStream bos = new BufferedOutputStream(os, outputBufferSize);
                    FileInputStream fis = new FileInputStream(file);
                    BufferedInputStream bis = new BufferedInputStream(fis, bufferSize)) {
                if (offset > 0 && bis.skip(offset) < offset) {
                    throw new IOException("Unable to skip to offset=%d" + offset);
                }
                bis.transferTo(bos);
            }
        }
        catch (IOException | RuntimeException ex) {
            throw new BloomBoxException(String.format("Error writing data store '%s' to stream (%s).", file, header), ex);
        }

    }

    /**
     * @return de-serialized box
     */
    Object readResolve() {
        this.output = DUMMY_AFTER_DESERIALIZATION;
        return this;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " [file=" + file + ", numberOfRows=" + numberOfRows + ", vectorSize=" + vectorSize + ", bufferSize="
                + bufferSize + ", offset=" + offset + "]";
    }

}
