//@formatter:off
/*
 * FileDataStoreHeader
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

import java.io.File;

/**
 * The {@link FileDataStoreHeader} adds the file information to the {@link DataStoreHeader}.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class FileDataStoreHeader extends DataStoreHeader {

    private static final long serialVersionUID = -7931332221285200549L;

    /**
     * BBS file (just the bytes, big-endian ecoded vector-longs)
     */
    private File file;

    public FileDataStoreHeader() {
        // default constructor
    }

    /**
     * @param version box version, see {@link BloomBox#VERSION}
     * @param numberOfRows Capacity of the store (number of records)
     * @param vectorSize size of a single bloom filter vector counted in longs
     * @param dataStoreClass fully qualified name of the implementation class of the store
     * @param file BBS file to be used
     */
    public FileDataStoreHeader(String version, long numberOfRows, int vectorSize, String dataStoreClass, File file) {
        super(version, numberOfRows, vectorSize, dataStoreClass);
        this.file = file;
    }

    /**
     * @return BBS file of the store
     */
    public File getFile() {
        return file;
    }

    /**
     * @param file BBS file of the store
     */
    public void setFile(File file) {
        this.file = file;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " [version=" + this.getVersion() + ", numberOfRows=" + this.getNumberOfRows() + ", vectorSize="
                + this.getVectorSize() + ", dataStoreClass=" + this.getDataStoreClass() + ", file=" + file + "]";
    }

}
