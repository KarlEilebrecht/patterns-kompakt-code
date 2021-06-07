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
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This in-memory-store supports attached probabilities.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class PbInMemoryDataStore extends DefaultDataStore implements PbDataStore {

    private static final long serialVersionUID = -7795764836613656147L;

    private static final Logger LOGGER = LoggerFactory.getLogger(PbInMemoryDataStore.class);

    /**
     * Probability vectors, compressed to save memory, will be decompressed on demand
     */
    private final byte[][] compressedProbabilities;

    /**
     * This is used to gracefully support {@link #feedRow(long[], long)}, which is logically a mistake.
     */
    private final byte[] compressedMissingDPPs = new byte[] { 0, 0, 0, 0 };

    /**
     * maps collected data point ids to short ids
     */
    private final PbDataPointDictionary dataPointDictionary = new PbDataPointDictionary();

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
            PbInMemoryDataStore res = new PbInMemoryDataStore(header.getVectorSize(), (int) header.getNumberOfRows());
            loadPbStoreIntoMemory(is, res, header);
            res.totalSize = res.computeOverallSize();
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
    protected static void loadPbStoreIntoMemory(InputStream is, PbInMemoryDataStore dataStore, DataStoreHeader header) {
        try (BufferedInputStream bis = new BufferedInputStream(is, DEFAULT_IO_BUFFER_SIZE)) {
            DefaultDataStore.loadDataStoreIntoMemory(bis, dataStore, header);
            loadDataPointDictionary(bis, dataStore, header);
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
     * Restores the data point dictionary
     * 
     * @param bis source stream
     * @param dataStore store to be filled
     * @param header stream header information
     */
    protected static void loadDataPointDictionary(BufferedInputStream bis, PbInMemoryDataStore dataStore, DataStoreHeader header) {

        try {
            LOGGER.debug("Loading data point dictionary into memory ...");
            byte[] buffer = new byte[4];
            int bytesFound = bis.read(buffer, 0, 4);
            if (bytesFound != 4) {
                throw new BloomBoxException(String.format("Error loading data point dictionary: 4 bytes expected, found %d - header: %s", bytesFound, header));
            }
            int numberOfEntries = PbVectorCodec.readInt(buffer, 0);

            for (int i = 0; i < numberOfEntries; i++) {
                bytesFound = bis.read(buffer, 0, 4);
                if (bytesFound != 4) {
                    throw new BloomBoxException(String.format("Error loading data point dictionary (entry %d / %d): 4 bytes expected, found %d - header: %s",
                            (i + 1), numberOfEntries, bytesFound, header));
                }
                dataStore.dataPointDictionary.feed(PbVectorCodec.readInt(buffer, 0));
            }
            LOGGER.debug("Data point dictionary restored with {} entries.", numberOfEntries);
        }
        catch (BloomBoxException ex) {
            throw ex;
        }
        catch (IOException | RuntimeException ex) {
            throw new BloomBoxException(String.format("Error restoring probability vectors %s.", header), ex);
        }

    }

    /**
     * Reads the stored compressed probability vectors from the stream and fills the in-memory array
     * 
     * @param bis source stream
     * @param dataStore store to be filled
     * @param header stream header information
     */
    protected static void loadProbabilityVectors(BufferedInputStream bis, PbInMemoryDataStore dataStore, DataStoreHeader header) {

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
                int entryLength = PbVectorCodec.readInt(buffer, 0);
                byte[] compressedDpp = new byte[entryLength + 4];
                System.arraycopy(buffer, 0, compressedDpp, 0, 4);
                bytesFound = bis.read(compressedDpp, 4, entryLength);
                if (bytesFound != entryLength) {
                    throw new BloomBoxException(String.format("Error loading probability vectors (row %d): %d bytes expected, found %d - header: %s", rowIdx,
                            entryLength, bytesFound, header));
                }
                dataStore.compressedProbabilities[(int) rowIdx] = compressedDpp;
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

    @Override
    public void dispatch(QueryDelegate queryDelegate) {
        queryDelegate.prepareLpDataPointIds(dataPointDictionary);
        PbDataPointProbabilityManager dppFetcher = new PbDataPointProbabilityManager();
        queryDelegate.registerDataPointOccurrences(dppFetcher);
        for (long rowIdx = 0; rowIdx < getNumberOfRows(); rowIdx++) {
            dppFetcher.initialize(this.compressedProbabilities[(int) rowIdx]);
            queryDelegate.execute(vector, (int) (rowIdx * vectorSize), dppFetcher);
        }
    }

    @Override
    public void feedRow(long[] rowVector, long rowIdx) {
        super.feedRow(rowVector, rowIdx);
        LOGGER.warn("Feeding without probabilities will lead to zero counts, offending rowIdx={} (most likely a mistake)", rowIdx);
        this.compressedProbabilities[(int) rowIdx] = compressedMissingDPPs;
    }

    @Override
    public void feedRow(long[] rowVector, long rowIdx, long[] dppVector) {
        super.feedRow(rowVector, rowIdx);
        collectAndMapLpDataPointIds(dppVector);
        this.compressedProbabilities[(int) rowIdx] = PbVectorCodec.getInstance().encode(dppVector);
    }

    /**
     * Collects lpDataPointIds in the dictionary and replaces the ids in the vector with the mapped ones.
     * <p>
     * On average this leads to shorter ids and better vector compression ratio.
     * 
     * @param dppVector vector with data point probabilities (will be modified)
     */
    protected void collectAndMapLpDataPointIds(long[] dppVector) {
        for (int i = 0; i < dppVector.length; i++) {
            long dpp = dppVector[i];
            int lpDataPointId = PbVectorCodec.decodeLpDataPointId(dpp);
            int mappedLpDataPointId = dataPointDictionary.feed(lpDataPointId);
            if (mappedLpDataPointId != lpDataPointId) {
                dppVector[i] = PbVectorCodec.encodeDataPointProbability(mappedLpDataPointId, PbVectorCodec.decodeDataPointProbability(dpp));
            }
        }
        // by replacing the ids with the ones from the dictionary we have changed the order
        // the vector MUST be sorted
        Arrays.sort(dppVector);
    }

    @Override
    public void serializeToStream(OutputStream os) throws IOException {
        DataStoreHeader header = new DataStoreHeader(BloomBox.VERSION, super.getNumberOfRows(), super.getVectorSize(), this.getClass().getName());
        try {
            HeaderUtil.writeDataStoreHeader(os, header);

            int outputBufferSize = 10_000_000;
            try (BufferedOutputStream bos = new BufferedOutputStream(os, outputBufferSize)) {
                writeRawBBS(bos);
                writeDataPointDictionary(bos);
                writeProbabilityVectors(bos);
            }
        }
        catch (IOException | RuntimeException ex) {
            throw new BloomBoxException(String.format("Error writing in-memory data store to stream (%s).", header), ex);
        }

    }

    /**
     * This method appends the length of the dictionary and all dictionary entries to the stream
     * 
     * @param bos destination
     * @throws IOException on error
     */
    protected void writeDataPointDictionary(BufferedOutputStream bos) throws IOException {
        LOGGER.debug("Storing data point dictionary ...");
        int[] lpDataPointIdsInLookupOrder = this.dataPointDictionary.toIntArray();
        LOGGER.debug("Writing {} lpDataPointIds ...", lpDataPointIdsInLookupOrder.length);
        byte[] buffer = new byte[4];
        PbVectorCodec.writeInt(lpDataPointIdsInLookupOrder.length, buffer, 0);
        bos.write(buffer);
        for (int lpDataPointId : lpDataPointIdsInLookupOrder) {
            PbVectorCodec.writeInt(lpDataPointId, buffer, 0);
            bos.write(buffer);
        }
        LOGGER.debug("Data point dictionary stored.");
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
        res = res + Arrays.stream(compressedProbabilities).map(arr -> arr.length).collect(Collectors.summingLong(Integer::intValue));
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
    public void mergeRow(long[] rowVector, long rowIdx) {
        throw new UnsupportedOperationException(this.getClass().getSimpleName() + " does not support row merging.");
    }

    @Override
    public boolean isRowMergeCapable() {
        return false;
    }

}
