//@formatter:off
/*
 * PbDataStoreHeader
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

/**
 * Header to read a {@link PbInMemoryDataStore} with attached probabilities
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class PbDataStoreHeader extends DataStoreHeader {

    private static final long serialVersionUID = -6408640336679249654L;

    /**
     * number of known columns (for the probability index)
     */
    private int numberOfColumns = 0;

    public PbDataStoreHeader() {
        // for deserialization
    }

    public PbDataStoreHeader(String version, long numberOfRows, int numberOfColumns, int vectorSize, String dataStoreClass) {
        super(version, numberOfRows, vectorSize, dataStoreClass);
        this.numberOfColumns = numberOfColumns;
    }

    /**
     * @return number of known columns (for reading the probability index)
     */
    public int getNumberOfColumns() {
        return numberOfColumns;
    }

    /**
     * @param numberOfColumns number of known columns (for reading the probability index)
     */
    public void setNumberOfColumns(int numberOfColumns) {
        this.numberOfColumns = numberOfColumns;
    }

}
