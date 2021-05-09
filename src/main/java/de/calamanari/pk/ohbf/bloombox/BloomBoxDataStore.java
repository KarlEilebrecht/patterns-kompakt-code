//@formatter:off
/*
 * BloomBoxDataStore
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

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;

/**
 * The {@link BloomBoxDataStore} is an abstraction from the raw data (bloom filter records resp. there vectors). To leave it open to a concrete implementation
 * how this data is stored (e.g. in-memory vs. a file) the bloom box queries do not directly access the store but they get <i>dispatched by the store</i>. The
 * method {@link #dispatch(QueryDelegate)} decouples the bloom filter operation (matching) from iterating the records in the the store.
 * <p>
 * <b>Important:</b> Part of the {@link BloomBoxDataStore} contract is providing a static method<br>
 * <b><code>public static BloomBoxDataStore restore(InputStream is, DataStoreHeader header, Map<String, String> envSettings)</code></b> that will allow
 * de-serialization of stores serialized by {@link #serializeToStream(OutputStream)}.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public interface BloomBoxDataStore extends Serializable {

    /**
     * Called before records get ingested
     * 
     * @return true if the store can take more records, otherwise false
     */
    public boolean ensureIsOpenForFeeding();

    /**
     * Gives a hint to the store that the feeder has completed and no more rows are expected.
     */
    public void notifyFeedingComplete();

    /**
     * Tells the store to close the underlying data store (if applicable)
     */
    public void close();

    /**
     * Returns the capacity of the store
     * 
     * @return fixed maximum number of rows, this is <i>not</i> the number of rows fed so far!
     */
    public long getNumberOfRows();

    /**
     * @return the number of longs in one bloom filter (record size in required longs)
     */
    public int getVectorSize();

    /**
     * @return number of bytes (across all rows and records) in this store
     */
    default long getTotalSizeInBytes() {
        return getNumberOfRows() * getVectorSize() * 8L;
    }

    /**
     * Passes a query to the store expecting the query to be executed on each record's vector in the store
     * 
     * @param queryDelegate query to be matched against each record in the store
     */
    public void dispatch(QueryDelegate queryDelegate);

    /**
     * Feeds a single row (bloom filter vector per record) into the the store
     * 
     * @param rowVector the bloom filter's vector as longs
     * @param rowIdx upcounting number of rows fed (never decreases)
     */
    public void feedRow(long[] rowVector, long rowIdx);

    /**
     * Writes the store to the given stream, individual implementations may decide only to store the header with metadata.
     * <p>
     * The method first writes a {@link DataStoreHeader} (or subclass), see {@link HeaderUtil#writeDataStoreHeader(OutputStream, DataStoreHeader)} followed by a
     * format of choice.
     * 
     * @param os destination
     * @throws IOException on any error
     */
    public void serializeToStream(OutputStream os) throws IOException;
}
