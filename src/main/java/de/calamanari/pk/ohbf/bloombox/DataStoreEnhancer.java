//@formatter:off
/*
 * DataStoreEnhancer
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

import java.util.List;
import java.util.Map;

import de.calamanari.pk.muhai.MuhaiGenerator;
import de.calamanari.pk.ohbf.BloomFilterConfig;
import de.calamanari.pk.ohbf.LwGenericOHBF;

/**
 * There may be cases where the creator of a {@link BloomBox} for some reason can estimate how many features (columns) to be fed but is not able (or willing) to
 * provide all data during the initial feeding operation. Data shall be added later on each row.
 * <p>
 * The {@link DataStoreEnhancer} can add additional key/value mappings to every row, if the underlying store supports this (see
 * {@link BloomBoxDataStore#isRowMergeCapable()}).<br>
 * Therefore the enhanced data has to be provided row by row in the exact same order as the box has been fed in the first place.
 * <p>
 * <b>Note:</b> {@link DataStoreEnhancer}s are stateful and <i>NOT</i> safe to be accessed by multiple threads concurrently.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class DataStoreEnhancer {

    /**
     * the data store of the bloom box
     */
    protected final BloomBoxDataStore dataStore;

    /**
     * bloom filter configuration for creating rows
     */
    protected final BloomFilterConfig config;

    /**
     * Capacity of the store (fixed)
     */
    protected final long numberOfRows;

    /**
     * Thread-local with the bloom filter (in case of multi-threaded feeding)
     */
    protected final LwGenericOHBF bloomFilter;

    /**
     * position of the "cursor" in the store
     */
    protected long currentRowIndex = -1;

    /**
     * @param config bloom filter config (for creating the vector to be merged)
     * @param dataStore destination store we want to merge data into
     */
    public DataStoreEnhancer(BloomFilterConfig config, BloomBoxDataStore dataStore) {
        this.config = config;
        this.dataStore = dataStore;
        if (!this.dataStore.isRowMergeCapable()) {
            throw new BloomBoxException(String.format("%s does not support data enhancement.", dataStore.getClass().getSimpleName()));
        }
        this.numberOfRows = this.dataStore.getNumberOfRows();
        this.bloomFilter = new LwGenericOHBF(this.config);
    }

    /**
     * @param bloomBox to be enhanced
     */
    public DataStoreEnhancer(BloomBox bloomBox) {
        this(bloomBox.getConfig(), bloomBox.getDataStore());
    }

    /**
     * Adds the given key-value pairs to the record's bloom filter and merges the corresponding entry with the existing row entry in the store if there is room.
     * If you specify other values than strings, you can find the conversion rules here: {@link MuhaiGenerator}.
     * 
     * @param columnMap key-value pairs, column name to column value, <b>please read the class comment</b>
     * @return true if the operation succeeded or false if you tried adding data after the last row in the store (no counterpart to merge)
     */
    public boolean enhanceRow(Map<String, ?> columnMap) {
        if (moveToNextRow()) {
            bloomFilter.clear();
            for (Map.Entry<String, ?> entry : columnMap.entrySet()) {
                bloomFilter.put(entry.getKey(), entry.getValue());
            }
            dataStore.mergeRow(bloomFilter.getBitVectorAsLongArray(), currentRowIndex);
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Adds the given key-value pairs to the record's bloom filter and merges the corresponding entry with the existing row entry in the store if there is room.
     * If you specify other values than strings, you can find the conversion rules here: {@link MuhaiGenerator}.
     * 
     * @param columnIds the column names
     * @param columnValues same length as columnIds, <b>please read the class comment</b>
     * @return true if the operation succeeded or false if you tried adding data after the last row in the store (no counterpart to merge)
     */
    public boolean enhanceRow(String[] columnIds, Object[] columnValues) {
        if (moveToNextRow()) {
            bloomFilter.clear();
            for (int i = 0; i < columnIds.length; i++) {
                String columnId = columnIds[i];
                Object columnValue = columnValues[i];
                bloomFilter.put(columnId, columnValue);
            }
            dataStore.mergeRow(bloomFilter.getBitVectorAsLongArray(), currentRowIndex);
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Adds the given key-value pairs to the record's bloom filter and merges the corresponding entry with the existing row entry in the store if there is room.
     * If you specify other values than strings, you can find the conversion rules here: {@link MuhaiGenerator}.
     * 
     * @param columnIds the column names
     * @param columnValues same length as columnIds, <b>please read the class comment</b>
     * @return true if the operation succeeded or false if you tried adding data after the last row in the store (no counterpart to merge)
     */
    public boolean enhanceRow(List<String> columnIds, List<?> columnValues) {
        if (moveToNextRow()) {
            bloomFilter.clear();
            for (int i = 0; i < columnIds.size(); i++) {
                String columnId = columnIds.get(i);
                Object columnValue = columnValues.get(i);
                bloomFilter.put(columnId, columnValue);
            }
            dataStore.feedRow(bloomFilter.getBitVectorAsLongArray(), currentRowIndex);
            return true;
        }
        else {
            return false;
        }
    }

    /**
     * Attempts to move the cursor for the next ingestion
     * 
     * @return true if next row is ready or false if the box is full or sealed
     */
    protected boolean moveToNextRow() {
        boolean res = false;
        if (currentRowIndex + 1 < numberOfRows) {
            currentRowIndex++;
            res = true;
        }
        return res;

    }

}
