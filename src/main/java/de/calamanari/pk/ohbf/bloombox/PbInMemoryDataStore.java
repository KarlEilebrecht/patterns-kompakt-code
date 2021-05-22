//@formatter:off
/*
 * PbInMemoryDataStore
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
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.calamanari.pk.ohbf.bloombox.bbq.ExpressionIdUtil;

/**
 * This in-memory-store supports attached probabilities.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class PbInMemoryDataStore extends DefaultDataStore {

    private static final long serialVersionUID = -7795764836613656147L;

    private static final Logger LOGGER = LoggerFactory.getLogger(PbInMemoryDataStore.class);

    /**
     * Probability vectors, compressed to save memory, will be decompressed on demand
     */
    private final byte[][] compressedProbabilities;

    /**
     * for rows fed without probabilties we assume all 1
     */
    private float[] defaultProbabilities = null;

    /**
     * maps the column idenitifiers to the index
     */
    private Map<Long, Integer> probabilityIndexMap = new HashMap<>();

    /**
     * cached total size in bytes (after feeding complete)
     */
    private long totalSize = 0;

    /**
     * Part of the {@link BloomBoxDataStore} contract, a method to restore a previously serialized store from a stream.
     * 
     * @param is source stream
     * @param header this is the header that was previously written by {@link #serializeToStream(OutputStream)}
     * @param envSettings global settings
     * @return restored data store
     */
    @SuppressWarnings("java:S1172")
    public static BloomBoxDataStore restore(InputStream is, DataStoreHeader header, Map<String, String> envSettings) {

        try {
            PbDataStoreHeader castedHeader = (PbDataStoreHeader) header;
            PbInMemoryDataStore res = new PbInMemoryDataStore(header.getVectorSize(), (int) header.getNumberOfRows());
            loadPbStoreIntoMemory(is, res, castedHeader);
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
     * Reads the stores longs from the stream and fills the in-memory array
     * 
     * @param is source stream
     * @param dataStore store to be filled
     * @param header stream header information
     */
    protected static void loadPbStoreIntoMemory(InputStream is, PbInMemoryDataStore dataStore, PbDataStoreHeader header) {
        try (BufferedInputStream bis = new BufferedInputStream(is, DEFAULT_IO_BUFFER_SIZE)) {
            DefaultDataStore.loadDataStoreIntoMemory(bis, dataStore, header);
            loadProbabilityIndexMap(bis, dataStore, header);
            loadProbabilityVectors(bis, dataStore, header);
        }
        catch (BloomBoxException ex) {
            throw ex;
        }
        catch (IOException | RuntimeException ex) {
            throw new BloomBoxException(String.format("Error restoring data store %s.", header), ex);
        }

    }

    /**
     * Restores the probability index map from a sequence of column-identifiers
     * 
     * @param bis source stream
     * @param dataStore store to be filled
     * @param header stream header information
     */
    protected static void loadProbabilityIndexMap(BufferedInputStream bis, PbInMemoryDataStore dataStore, PbDataStoreHeader header) {
        try {
            LOGGER.debug("Loading probability index map into memory ...");
            byte[] buffer = new byte[8];
            int numberOfColumns = header.getNumberOfColumns();
            for (int i = 0; i < numberOfColumns; i++) {
                int bytesFound = bis.read(buffer, 0, 8);
                if (bytesFound != 8) {
                    throw new BloomBoxException(
                            String.format("Error loading column identifiers (entry %d): 8 bytes expected, found %d - header: %s", i, bytesFound, header));
                }
                dataStore.probabilityIndexMap.put(BloomBox.bytesToLong(buffer), i);
            }
            LOGGER.debug("Probability index map restored: {} columns.", dataStore.probabilityIndexMap.size());
        }
        catch (BloomBoxException ex) {
            throw ex;
        }
        catch (IOException | RuntimeException ex) {
            throw new BloomBoxException(String.format("Error restoring probability index map %s.", header), ex);
        }
    }

    /**
     * Reads the stores longs from the stream and fills the in-memory array
     * 
     * @param bis source stream
     * @param dataStore store to be filled
     * @param header stream header information
     */
    protected static void loadProbabilityVectors(BufferedInputStream bis, PbInMemoryDataStore dataStore, PbDataStoreHeader header) {

        try {
            LOGGER.debug("Loading probability vectors into memory ...");
            byte[] buffer = new byte[4];
            int numberOfRows = (int) header.getNumberOfRows();
            for (long rowIdx = 0; rowIdx < numberOfRows; rowIdx++) {
                int bytesFound = bis.read(buffer, 0, 4);
                if (bytesFound != 4) {
                    throw new BloomBoxException(
                            String.format("Error loading probability vectors (row %d): 4 bytes expected, found %d - header: %s", rowIdx, bytesFound, header));
                }
                int entryLength = ProbabilityVectorCodec.readInt(buffer, 4);
                byte[] entryBuffer = new byte[entryLength + 4];
                System.arraycopy(buffer, 0, entryBuffer, 0, 4);
                bytesFound = bis.read(entryBuffer, 4, entryLength);
                if (bytesFound != entryLength) {
                    throw new BloomBoxException(String.format("Error loading probability vectors (row %d): %d bytes expected, found %d - header: %s", rowIdx,
                            entryLength, bytesFound, header));
                }
                dataStore.compressedProbabilities[(int) rowIdx] = entryBuffer;
            }
            LOGGER.debug("Probability vectors loaded.");
        }
        catch (BloomBoxException ex) {
            throw ex;
        }
        catch (IOException | RuntimeException ex) {
            throw new BloomBoxException(String.format("Error restoring probability vectors %s.", header), ex);
        }

    }

    /**
     * @param vectorSize see {@link DefaultDataStore}
     * @param numberOfRows see {@link DefaultDataStore}
     */
    public PbInMemoryDataStore(int vectorSize, int numberOfRows) {
        super(vectorSize, numberOfRows);
        this.compressedProbabilities = new byte[numberOfRows][0];
    }

    /**
     * Defines the column index and builds an internal map
     * 
     * @param columnNames unique names of all columns in order
     */
    public void defineColumnIndex(List<String> columnNames) {
        if (probabilityIndexMap.isEmpty()) {
            int idx = 0;
            for (String columnName : columnNames) {
                if (probabilityIndexMap.putIfAbsent(ExpressionIdUtil.createExpressionId(columnName), idx) != null) {
                    throw new BloomBoxException("Column names must be unique, offending entry: " + columnName);
                }
                idx++;
            }
            float[] defaultProbs = new float[probabilityIndexMap.size()];
            Arrays.fill(defaultProbs, 1.0f);
            this.defaultProbabilities = defaultProbs;
        }
        else {
            throw new IllegalStateException("Re-defining the column names is not supported.");
        }
    }

    @Override
    public void dispatch(QueryDelegate queryDelegate) {
        queryDelegate.prepareProbabilityIndex(probabilityIndexMap);
        DefaultProbabilityVectorSupplier vectorSupplier = new DefaultProbabilityVectorSupplier();
        for (long rowIdx = 0; rowIdx < getNumberOfRows(); rowIdx++) {
            vectorSupplier.initialize(this.compressedProbabilities[(int) rowIdx]);
            queryDelegate.execute(vector, (int) (rowIdx * vectorSize), vectorSupplier);
        }
    }

    @Override
    public void feedRow(long[] rowVector, long rowIdx) {
        super.feedRow(rowVector, rowIdx);
        this.compressedProbabilities[(int) rowIdx] = ProbabilityVectorCodec.getInstance().encode(defaultProbabilities);
    }

    /**
     * Feeds the row and sets the given probabilities, any column not mapped will get a default probability of 1.0.
     * 
     * @param rowVector long vector (bloom filter)
     * @param rowIdx number of the row
     * @param probabilityMap maps column identifiers (see {@link ExpressionIdUtil#createExpressionId(String, long...)} ) to probabilities
     * @throws BloomBoxException if the map contains an unknown column or if a probability is not in range 0..1
     */
    public void feedRow(long[] rowVector, long rowIdx, Map<Long, Float> probabilityMap) {
        super.feedRow(rowVector, rowIdx);

        float[] vector = Arrays.copyOf(defaultProbabilities, defaultProbabilities.length);

        for (Map.Entry<Long, Float> entry : probabilityMap.entrySet()) {
            Integer idx = probabilityIndexMap.get(entry.getKey());
            if (idx == null) {
                throw new BloomBoxException(
                        String.format("Unable to feed probability for unknown column: columnId=%d, probability=%s", entry.getKey(), entry.getValue()));
            }
            float value = entry.getValue();
            if (value < 0 || value > 1.0) {
                throw new BloomBoxException(
                        String.format("Probability out of range: columnId=%d, probability=%s (expected 0.0 <= p <= 1.0)", entry.getKey(), entry.getValue()));
            }
            vector[idx] = value;
        }
        this.compressedProbabilities[(int) rowIdx] = ProbabilityVectorCodec.getInstance().encode(vector);
    }

    @Override
    public void serializeToStream(OutputStream os) throws IOException {
        PbDataStoreHeader header = new PbDataStoreHeader(BloomBox.VERSION, super.getNumberOfRows(), probabilityIndexMap.size(), super.getVectorSize(),
                this.getClass().getName());
        try {
            HeaderUtil.writeDataStoreHeader(os, header);

            int outputBufferSize = 10_000_000;
            try (BufferedOutputStream bos = new BufferedOutputStream(os, outputBufferSize)) {
                writeRawBBS(bos);
                writeProbabilityIndexMap(bos);

            }
        }
        catch (IOException | RuntimeException ex) {
            throw new BloomBoxException(String.format("Error writing in-memory data store to stream (%s).", header), ex);
        }

    }

    /**
     * Stores the map as an ordered sequence of the column identifiers
     * 
     * @param bos destination
     * @throws IOException on error
     */
    protected void writeProbabilityIndexMap(BufferedOutputStream bos) throws IOException {

        LOGGER.debug("Storing probability index map ...");
        List<Map.Entry<Long, Integer>> entries = new ArrayList<>(probabilityIndexMap.entrySet());
        // sort entries by column index, so we can later recreate the map easily
        Collections.sort(entries, (e1, e2) -> e1.getValue().compareTo(e2.getValue()));
        byte[] buffer = new byte[8];
        for (Map.Entry<Long, Integer> entry : entries) {
            BloomBox.longToBytes(entry.getKey(), buffer);
            bos.write(buffer);
        }
        LOGGER.debug("Probability index map stored.");
    }

    /**
     * This method appends the compressed probability vectors as is to the stream (one after another)
     * 
     * @param bos destination
     * @throws IOException on error
     */
    protected void writeProbabilityVectors(BufferedOutputStream bos) throws IOException {
        LOGGER.debug("Storing compressed probability vectors ...");
        for (int i = 0; i < this.getNumberOfRows(); i++) {
            bos.write(this.compressedProbabilities[i]);
        }
        LOGGER.debug("Compressed probability vectors stored.");
    }

    /**
     * @return approximate total size including probability data
     */
    private long computeOverallSize() {
        long res = super.getTotalSizeInBytes();
        res = res + (probabilityIndexMap.size() * 8L);
        res = res + Arrays.stream(compressedProbabilities).map(arr -> arr.length).collect(Collectors.summingInt(Integer::intValue));
        return res;
    }

    @Override
    public void notifyFeedingComplete() {
        this.totalSize = computeOverallSize();
    }

    @Override
    public long getTotalSizeInBytes() {
        if (totalSize > 0) {
            return totalSize;
        }
        else {
            return computeOverallSize();
        }
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " [numberOfRows=" + numberOfRows + ", numberOfColumns=" + probabilityIndexMap.size() + ", vectorSize="
                + vectorSize + ", totalSizeInBytes=" + getTotalSizeInBytes() + "]";
    }

}
