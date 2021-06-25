//@formatter:off
/*
 * DataStoreHeader
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

import java.io.Serializable;

/**
 * The {@link DataStoreHeader} is part of the bloom box file format (BBX) and contains the core information to restore a serialized data store.<br>
 * Technically (see {@link HeaderUtil}) it is a single line of JSON containing the box version (see {@link BloomBox#VERSION} and the exact settings plus the
 * data store class name.<br>
 * This way an individual data store can be restored in the destination environment if the custom {@link BloomBoxDataStore} class is present.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class DataStoreHeader implements Serializable {

    private static final long serialVersionUID = 4241795917540717100L;

    /**
     * Major.Minor, see {@link BloomBox#VERSION}
     */
    private String version;

    /**
     * Capacity of the store
     */
    private long numberOfRows;

    /**
     * Bloom filter vector size in long values
     */
    private int vectorSize;

    /**
     * Implementation class of the data store
     */
    private String dataStoreClass;

    public DataStoreHeader() {
        // default constructor
    }

    /**
     * @param version box version, Major.Minor, see {@link BloomBox#VERSION}
     * @param numberOfRows capacity of the store (number of records)
     * @param vectorSize bloom filter vector size as number of longs
     * @param dataStoreClass implementation class name, fully qualified
     */
    public DataStoreHeader(String version, long numberOfRows, int vectorSize, String dataStoreClass) {
        this.numberOfRows = numberOfRows;
        this.vectorSize = vectorSize;
        this.version = version;
        this.dataStoreClass = dataStoreClass;
    }

    /**
     * @return box version, Major.Minor, see {@link BloomBox#VERSION}
     */
    public String getVersion() {
        return version;
    }

    /**
     * @param version box version, Major.Minor, see {@link BloomBox#VERSION}
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * @return capacity of the store
     */
    public long getNumberOfRows() {
        return numberOfRows;
    }

    /**
     * @param numberOfRows capacity of the store
     */
    public void setNumberOfRows(long numberOfRows) {
        this.numberOfRows = numberOfRows;
    }

    /**
     * @return bloom filter vector size as number of longs
     */
    public int getVectorSize() {
        return vectorSize;
    }

    /**
     * @param vectorSize bloom filter vector size as number of longs
     */
    public void setVectorSize(int vectorSize) {
        this.vectorSize = vectorSize;
    }

    /**
     * @return implementation class name, fully qualified
     */
    public String getDataStoreClass() {
        return dataStoreClass;
    }

    /**
     * @param dataStoreClass implementation class name, fully qualified
     */
    public void setDataStoreClass(String dataStoreClass) {
        this.dataStoreClass = dataStoreClass;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " [version=" + version + ", numberOfRows=" + numberOfRows + ", vectorSize=" + vectorSize + ", dataStoreClass="
                + this.dataStoreClass + "]";
    }

}