//@formatter:off
/*
 * DefaultDataStore
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
import java.util.List;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The {@link DefaultDataStore} is an in-memory implementation of a {@link BloomBoxDataStore} that uses a large array of longs to represent the records' bloom
 * filter vectors.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class DefaultDataStore implements BloomBoxDataStore {

    private static final long serialVersionUID = -8712764836613656147L;

    private static final Logger LOGGER = LoggerFactory.getLogger(DefaultDataStore.class);

    /**
     * for serialization / de-serialization
     */
    protected static final int DEFAULT_IO_BUFFER_SIZE = 10_000_000;

    /**
     * We query this once and once only to keep it simple, which is not ideal in VMs where this property may change occasionally.
     */
    protected static final int NUMBER_OF_CORES = Runtime.getRuntime().availableProcessors();

    /**
     * thread-pool for parallel execution, shared by all stores as a pool per store does not make any sense because threads are limited by the system.
     */
    protected static ExecutorService executorService = null;

    /**
     * all rows x filter vectors long array
     */
    protected final long[] vector;

    /**
     * capacity of the store
     */
    protected final long numberOfRows;

    /**
     * number of longs representing a vector (for navigation)
     */
    protected final int vectorSize;

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
            DefaultDataStore res = new DefaultDataStore(header.getVectorSize(), (int) header.getNumberOfRows());
            loadDataStoreIntoMemory(is, res, header);
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
    private static void loadDataStoreIntoMemory(InputStream is, DefaultDataStore dataStore, DataStoreHeader header) {

        try (BufferedInputStream bis = new BufferedInputStream(is, DEFAULT_IO_BUFFER_SIZE)) {
            loadDataStoreIntoMemory(bis, dataStore, header);
        }
        catch (BloomBoxException ex) {
            throw ex;
        }
        catch (IOException | RuntimeException ex) {
            throw new BloomBoxException(String.format("Error restoring data store %s.", header), ex);
        }

    }

    /**
     * Reads the stores longs from the stream and fills the in-memory array
     * 
     * @param bis source stream
     * @param dataStore store to be filled
     * @param header stream header information
     */
    protected static void loadDataStoreIntoMemory(BufferedInputStream bis, DefaultDataStore dataStore, DataStoreHeader header) {

        try {
            LOGGER.debug("Loading data store {} into memory ...", header);
            byte[] buffer = new byte[8];
            int numberOfRows = (int) header.getNumberOfRows();
            int vectorSize = header.getVectorSize();
            long[] vector = dataStore.vector;
            for (long rowIdx = 0; rowIdx < numberOfRows; rowIdx++) {
                for (int i = 0; i < vectorSize; i++) {
                    int bytesFound = bis.read(buffer, 0, 8);
                    if (bytesFound != 8) {
                        throw new BloomBoxException(String.format("Error loading data store (row %d, index %d): 8 bytes expected, found %d - header: %s",
                                rowIdx, i, bytesFound, header));
                    }
                    vector[(int) ((rowIdx * vectorSize) + i)] = BloomBox.bytesToLong(buffer);
                }
            }
            LOGGER.debug("Data store loaded.");
        }
        catch (BloomBoxException ex) {
            throw ex;
        }
        catch (IOException | RuntimeException ex) {
            throw new BloomBoxException(String.format("Error restoring data store %s.", header), ex);
        }

    }

    /**
     * @param vectorSize number of longs to represent a single record as bloom filter
     * @param numberOfRows capacity of the store
     */
    public DefaultDataStore(int vectorSize, int numberOfRows) {
        this.vectorSize = vectorSize;
        this.numberOfRows = numberOfRows;
        this.vector = new long[vectorSize * numberOfRows];
    }

    @Override
    public long getNumberOfRows() {
        return numberOfRows;
    }

    @Override
    public <Q extends QueryDelegate<Q>> void dispatch(Q queryDelegate) {
        if (checkParallelQueryRequest(queryDelegate)) {
            dispatchParallel(queryDelegate);
        }
        else {
            dispatchPartition(queryDelegate, 0, (int) numberOfRows);
        }
    }

    /**
     * TEMPLATE METHOD to dispatch the query to an area of rows
     * 
     * @param <Q> delegate type
     * @param queryDelegate the delegate
     * @param startRowIdx incl.
     * @param endRowIdx excl.
     */
    protected <Q extends QueryDelegate<Q>> void dispatchPartition(Q queryDelegate, int startRowIdx, int endRowIdx) {
        for (int rowIdx = startRowIdx; rowIdx < endRowIdx; rowIdx++) {
            queryDelegate.execute(vector, (rowIdx * vectorSize));
        }
    }

    @Override
    public void feedRow(long[] rowVector, long rowIdx) {
        System.arraycopy(rowVector, 0, vector, (int) (rowIdx * vectorSize), vectorSize);
    }

    @Override
    public void mergeRow(long[] rowVector, long rowIdx) {
        int offset = (int) (rowIdx * vectorSize);
        for (int i = 0; i < vectorSize; i++) {
            vector[offset + i] = vector[offset + i] | rowVector[i];
        }
    }

    @Override
    public boolean isRowMergeCapable() {
        return true;
    }

    @Override
    public int getVectorSize() {
        return vectorSize;
    }

    @Override
    public boolean ensureIsOpenForFeeding() {
        return true;
    }

    @Override
    public void close() {
        // nothing to do
    }

    @Override
    public void notifyFeedingComplete() {
        // nothing to do
    }

    @Override
    public void serializeToStream(OutputStream os) throws IOException {
        DataStoreHeader header = new DataStoreHeader(BloomBox.VERSION, numberOfRows, vectorSize, this.getClass().getName());
        try {
            HeaderUtil.writeDataStoreHeader(os, header);

            int outputBufferSize = 10_000_000;
            try (BufferedOutputStream bos = new BufferedOutputStream(os, outputBufferSize)) {
                writeRawBBS(bos);
            }
        }
        catch (IOException | RuntimeException ex) {
            throw new BloomBoxException(String.format("Error writing in-memory data store to stream (%s).", header), ex);
        }

    }

    /**
     * Writes the BBS-section (raw vectors) to the stream as bytes
     * 
     * @param bos output stream
     * @throws IOException on error
     */
    protected void writeRawBBS(BufferedOutputStream bos) throws IOException {
        byte[] buffer = new byte[8];
        for (long rowIdx = 0; rowIdx < numberOfRows; rowIdx++) {
            for (int i = 0; i < vectorSize; i++) {
                BloomBox.longToBytes(vector[(int) ((rowIdx * vectorSize) + i)], buffer);
                bos.write(buffer);
            }
        }
    }

    /**
     * Checks if any query requests parallel execution (applies to all queries of the delegate execution)
     * 
     * @param queryDelegate current execution
     * @return true if the query execution should be parallelized
     */
    protected boolean checkParallelQueryRequest(QueryDelegate<?> queryDelegate) {
        return Arrays.stream(queryDelegate.getQueries()).map(InternalQuery::getQueryOptions).anyMatch(BloomBoxOption.PARALLEL_QUERY::isEnabled);
    }

    /**
     * Dispatches the rows of the box in multiple partitions using multiple threads
     * 
     * @param queryDelegate the delegate to be dispatched in parallel mode
     */
    protected <Q extends QueryDelegate<Q>> void dispatchParallel(Q queryDelegate) {
        if (NUMBER_OF_CORES > 1 && numberOfRows > NUMBER_OF_CORES) {
            LOGGER.debug("Executing query delegate {} with {} threads ...", queryDelegate, NUMBER_OF_CORES);
            int partitionSize = (int) (numberOfRows / NUMBER_OF_CORES);
            int remainingRows = (int) (numberOfRows - (partitionSize * NUMBER_OF_CORES));
            List<DispatchJob<Q>> dispatchJobs = new ArrayList<>(NUMBER_OF_CORES);
            CountDownLatch completionLatch = new CountDownLatch(NUMBER_OF_CORES);
            int startRowIdx = 0;
            ExecutorService executorServiceRef = getExecutorService();
            for (int i = 0; i < NUMBER_OF_CORES; i++) {
                int size = partitionSize;
                if (remainingRows > 0) {
                    size++;
                    remainingRows = remainingRows - 1;
                }
                int endRowIdx = startRowIdx + size;
                DispatchJob<Q> job = new DispatchJob<>(queryDelegate, startRowIdx, endRowIdx, completionLatch);
                dispatchJobs.add(job);
                executorServiceRef.execute(job);
                startRowIdx = endRowIdx;
            }
            try {
                completionLatch.await();
            }
            catch (InterruptedException ex) {
                Thread.currentThread().interrupt();
                throw new BloomBoxException("Unexpected interruption processing query delegate " + queryDelegate.toString(), ex);
            }
            dispatchJobs.stream().forEach(job -> job.transferSpawnResults(queryDelegate));
            LOGGER.debug("Parallel execution of query delegate {} with {} threads completed.", queryDelegate, NUMBER_OF_CORES);
        }
        else {
            LOGGER.debug("Executing query delegate {} single-threaded (not enough cores or more cores {} than rows {}) ...", queryDelegate, NUMBER_OF_CORES,
                    numberOfRows);
            for (long rowIdx = 0; rowIdx < numberOfRows; rowIdx++) {
                queryDelegate.execute(vector, (int) (rowIdx * vectorSize));
            }
        }
    }

    /**
     * @return the executor service to be used for parallel query dispatching
     */
    protected static synchronized ExecutorService getExecutorService() {
        if (executorService == null) {
            // we use the number of cores without any multiplier because the operations are rather
            // computation intensive rather than IO-intensive
            // Using daemon threads is a lazy habit, so we don't have to worry about shutdown
            executorService = Executors.newFixedThreadPool(NUMBER_OF_CORES, r -> {
                Thread t = new Thread(r);
                t.setName("BBX-Worker:@" + Integer.toHexString(t.hashCode()));
                t.setDaemon(true);
                return t;
            });

        }
        return executorService;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " [numberOfRows=" + numberOfRows + ", vectorSize=" + vectorSize + ", totalSizeInBytes=" + getTotalSizeInBytes()
                + "]";
    }

    /**
     * Encapsulates a partial execution on a partition
     *
     * @param <Q> the concrete delegate type
     */
    private class DispatchJob<Q extends QueryDelegate<Q>> implements Runnable {

        /**
         * the spawned delegate
         */
        private final Q queryDelegate;

        /**
         * Start of partition (incl.)
         */
        private final int startRowIdx;

        /**
         * Start of partition (excl.)
         */
        private final int endRowIdx;

        /**
         * latch to wait for completion of all jobs of a query
         */
        private final CountDownLatch completionLatch;

        /**
         * hard execution error, if any
         */
        private RuntimeException error = null;

        /**
         * @param queryDelegate delegate (to be spawned)
         * @param partition area to work on
         */
        DispatchJob(Q queryDelegate, int startRowIdx, int endRowIdx, CountDownLatch completionLatch) {
            this.queryDelegate = queryDelegate.createSpawn();
            this.startRowIdx = startRowIdx;
            this.endRowIdx = endRowIdx;
            this.completionLatch = completionLatch;
        }

        @Override
        public void run() {
            try {
                dispatchPartition(queryDelegate, startRowIdx, endRowIdx);
            }
            catch (RuntimeException ex) {
                this.error = ex;
            }
            finally {
                this.completionLatch.countDown();
            }
        }

        /**
         * Merges the partial results from this job into the given original delegate
         * 
         * @param queryDelegate the original delegate (destination)
         */
        void transferSpawnResults(Q queryDelegate) {
            if (error != null) {
                throw error;
            }
            queryDelegate.addSpawnResults(this.queryDelegate);
        }
    }

}
