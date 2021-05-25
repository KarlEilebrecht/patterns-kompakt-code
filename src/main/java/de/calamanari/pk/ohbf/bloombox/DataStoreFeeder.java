//@formatter:off
/*
 * DataStoreFeeder
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
 * The {@link DataStoreFeeder} decouples the responsibility for getting data into the data store of a bloom box from the box and also from the store. This way
 * implementors can create special feeders as best fits for specific data stores. E.g. some may allow concurrent feeding or distributed feeding.
 * <p>
 * The default feeder synchronizes calls to protect the data store from concurrent access.
 * <p>
 * <b>Important:</b> Clients should always feed all fields of a record (including missing values, consistently use e.g. empty string) to get best results.<br>
 * If you don't do that, you can't query for records with missing values without specifying all possible values in a NOT IN condition.<br>
 * A BloomBox has nothing like a <code>HAS NOT</code> expression. If missing values are indicated by an empty string or 'N/A' or 'null', you can query the count
 * of records with these values explicitly.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class DataStoreFeeder {

    /**
     * monitor for locking write access on the data store
     */
    protected final Object storeMonitor = new Object();

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
    protected final ThreadLocal<LwGenericOHBF> bloomFilterHolder;

    /**
     * position of the "cursor" in the store
     */
    protected long currentRowIndex = -1;

    /**
     * @param config filter configuration
     * @param dataStore store
     * @param markSealed if true feeding will be disabled
     */
    public DataStoreFeeder(BloomFilterConfig config, BloomBoxDataStore dataStore, boolean markSealed) {
        this.config = config;
        this.dataStore = dataStore;
        this.numberOfRows = this.dataStore.getNumberOfRows();
        this.bloomFilterHolder = ThreadLocal.withInitial(() -> new LwGenericOHBF(this.config));
        if (markSealed) {
            this.currentRowIndex = numberOfRows;
        }
    }

    /**
     * @return true if all rows have been fed
     */
    public boolean isFeedingComplete() {
        synchronized (storeMonitor) {
            return (currentRowIndex + 1) >= numberOfRows;
        }
    }

    /**
     * Adds the given key-value pairs to the record's bloom filter and adds the corresponding entry to the store if there is room. If you specify other values
     * than strings, you can find the conversion rules here: {@link MuhaiGenerator}.
     * 
     * @param columnMap key-value pairs, column name to column value, <b>please read the class comment</b>
     * @return true if the operation succeed or false if this box is full or sealed
     */
    public boolean addRow(Map<String, ?> columnMap) {
        synchronized (storeMonitor) {
            if (moveToNextRow()) {
                LwGenericOHBF bloomFilter = bloomFilterHolder.get();
                bloomFilter.clear();
                for (Map.Entry<String, ?> entry : columnMap.entrySet()) {
                    bloomFilter.put(entry.getKey(), entry.getValue());
                }
                dataStore.feedRow(bloomFilter.getBitVectorAsLongArray(), currentRowIndex);
                return true;
            }
            else {
                return false;
            }
        }
    }

    /**
     * Adds the given key-value pairs to the record's bloom filter and adds the corresponding entry to the store if there is room. If you specify other values
     * than strings, you can find the conversion rules here: {@link MuhaiGenerator}.
     * 
     * @param columnIds the column names
     * @param columnValues same length as columnIds, <b>please read the class comment</b>
     * @return true if the operation succeed or false if this box is full or sealed
     */
    public boolean addRow(String[] columnIds, Object[] columnValues) {
        synchronized (storeMonitor) {
            if (moveToNextRow()) {
                LwGenericOHBF bloomFilter = bloomFilterHolder.get();
                bloomFilter.clear();
                for (int i = 0; i < columnIds.length; i++) {
                    String columnId = columnIds[i];
                    Object columnValue = columnValues[i];
                    bloomFilter.put(columnId, columnValue);
                }
                dataStore.feedRow(bloomFilter.getBitVectorAsLongArray(), currentRowIndex);
                return true;
            }
            else {
                return false;
            }
        }
    }

    /**
     * Adds the given key-value pairs to the record's bloom filter and adds the corresponding entry to the store if there is room. If you specify other values
     * than strings, you can find the conversion rules here: {@link MuhaiGenerator}.
     * 
     * @param columnIds the column names
     * @param columnValues same length as columnIds, <b>please read the class comment</b>
     * @return true if the operation succeed or false if this box is full or sealed
     */
    public boolean addRow(List<String> columnIds, List<?> columnValues) {
        synchronized (storeMonitor) {
            if (moveToNextRow()) {
                LwGenericOHBF bloomFilter = bloomFilterHolder.get();
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
    }

    /**
     * Attempts to move the cursor for the next ingestion
     * 
     * @return true if next row is ready or false if the box is full or sealed
     */
    protected boolean moveToNextRow() {
        boolean res = false;
        synchronized (storeMonitor) {
            if (currentRowIndex + 1 < numberOfRows) {
                currentRowIndex++;
                res = true;
            }
            return res && dataStore.ensureIsOpenForFeeding();
        }

    }

    /**
     * removes resources and notifies the data store that feeding is complete
     */
    public void close() {
        bloomFilterHolder.remove();
        synchronized (storeMonitor) {
            dataStore.notifyFeedingComplete();
        }
    }
}
