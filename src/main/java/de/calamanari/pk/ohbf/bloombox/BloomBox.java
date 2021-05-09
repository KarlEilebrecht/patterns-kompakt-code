//@formatter:off
/*
 * BloomBox
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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.function.BiFunction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.calamanari.pk.ohbf.BloomFilterConfig;
import de.calamanari.pk.ohbf.LwGenericOHBF;

/**
 * The {@link BloomBox} is a probabilistic <i>zero-data</i> counting engine based on bloom filters.
 * <p>
 * Each record is represented by a {@link LwGenericOHBF} bloom filter, where the field values are hashed together with the field value. So, each combination
 * (field, value) turns into a key that gets fed into the record's bloom filter.
 * <p>
 * This way no data is present in the bloom box after feeding. The bloom box does not allow selecting records but it allows querying how many records fulfill a
 * certain condition. Therefore a condition (expression like <code>CarBrand=Audi</code>) will be fed into a new {@link LwGenericOHBF} bloom filter.<br>
 * By comparing (logical AND) of the query's bloom filters to each record's bloom filter in the box we can determine how many records fulfill the query
 * condition. The precision of the returned counts depend on the complexity of the query and the configured false-positive rate epsilon of the filter (see
 * {@link BloomFilterConfig}.
 * <p>
 * Compared to the original input records the bloom filter records are quite small. Their size (and thus the size of the box) depends on the number of rows, the
 * number of columns and the configured epsilon.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class BloomBox implements Serializable {

    private static final long serialVersionUID = 778260740181870709L;

    private static final Logger LOGGER = LoggerFactory.getLogger(BloomBox.class);

    /**
     * Major.Minor version. A change of the major version makes newer serialized boxes incompatible to earlier versions of the implementation. Minor version
     * updates should not break compatibility.
     */
    public static final String VERSION = "0.1";

    /**
     * During de-serialization from file this setting will be mapped to the file
     */
    public static final String ENV_BLOOM_BOX_RESTORE_FILE = "bloombox.restore.file";

    /**
     * The position in the stream right after all headers
     */
    public static final String ENV_BLOOM_BOX_RESTORE_AFTER_HEADER_OFFSET = "bloombox.restore.after.header.offset";

    /**
     * bloom filter configuration
     */
    private final BloomFilterConfig config;

    /**
     * data store, e.g. in memory or disk
     */
    private final BloomBoxDataStore dataStore;

    /**
     * the feeder responsible for filling the data into the box
     */
    private transient DataStoreFeeder feeder;

    /**
     * Comment (optional)
     */
    private String description;

    /**
     * Date this box was created
     */
    private Date dateCreated = new Date();

    /**
     * Restores a previously serialized box from the given input stream
     * 
     * @param is source stream
     * @param envSettings global settings, key/value
     * @return restored box
     * @throws IOException on any error
     */
    public static BloomBox deserializeFromStream(InputStream is, Map<String, String> envSettings) throws IOException {
        BloomBox res = null;
        LOGGER.debug("Restoring bloom box from stream with settings {} ...", envSettings);
        try {
            AtomicLong streamPosition = new AtomicLong();
            BloomBoxHeader bloomBoxHeader = HeaderUtil.readBloomBoxHeader(is, streamPosition);
            LOGGER.debug("{}", bloomBoxHeader);
            String version = bloomBoxHeader.getVersion();
            int majorVersion = Integer.parseInt(version.substring(0, version.indexOf('.')));
            int majorVersionExpected = Integer.parseInt(VERSION.substring(0, version.indexOf('.')));
            if (majorVersion > majorVersionExpected) {
                throw new BloomBoxException(String.format("Unsupported BloomBox version, expected <= %d, found: %s", majorVersion, version));
            }
            DataStoreHeader dataStoreHeader = HeaderUtil.readDataStoreHeader(is, streamPosition);
            LOGGER.debug("{}", dataStoreHeader);
            @SuppressWarnings("unchecked")
            Class<BloomBoxDataStore> dataStoreClass = (Class<BloomBoxDataStore>) Class.forName(dataStoreHeader.getDataStoreClass());

            Method restoreMethod = null;
            for (Method m : dataStoreClass.getDeclaredMethods()) {
                if (Modifier.isStatic(m.getModifiers()) && "restore".equals(m.getName())) {
                    restoreMethod = m;
                    break;
                }
            }
            if (restoreMethod == null) {
                throw new BloomBoxException(String.format(
                        "Could not find static method with signature 'BloomBoxDataStore restore(InputStream is, DataStoreHeader header, Map<String, String> envSettings)' in class %s",
                        dataStoreClass));
            }
            envSettings.put(ENV_BLOOM_BOX_RESTORE_AFTER_HEADER_OFFSET, String.valueOf(streamPosition.get()));
            BloomBoxDataStore dataStore = (BloomBoxDataStore) restoreMethod.invoke(null, is, dataStoreHeader, envSettings);
            BloomFilterConfig config = BloomFilterConfig.createUnchecked(bloomBoxHeader.getRequiredNumberOfBitsM(),
                    bloomBoxHeader.getNumberOfInsertedElementsN(), bloomBoxHeader.getFalsePositiveRateEpsilon(), bloomBoxHeader.getNumberOfHashesK());
            res = new BloomBox(config, dataStore);
            if (bloomBoxHeader.getDateCreated() != null) {
                res.dateCreated = bloomBoxHeader.getDateCreated();
            }
            res.description = bloomBoxHeader.getDescription();
        }
        catch (InvocationTargetException | IllegalAccessException | ClassNotFoundException | RuntimeException ex) {
            throw new IOException("Unable to restore BloomBox", ex);
        }
        LOGGER.debug("Bloom box successfully restored.");
        return res;
    }

    /**
     * Loads a previously stored bloom box from the given file.
     * 
     * @param bbxFile source
     * @param envSettings global settings
     * @return sealed box (no feeding anymore)
     */
    public static BloomBox loadFromFile(File bbxFile, Map<String, String> envSettings) {
        envSettings = envSettings == null ? new HashMap<>() : new HashMap<>(envSettings);
        LOGGER.debug("Loading bloom box from file {} with settings {} ...", bbxFile, envSettings);
        try (FileInputStream fis = new FileInputStream(bbxFile)) {
            envSettings.put(ENV_BLOOM_BOX_RESTORE_FILE, bbxFile.getAbsolutePath());
            return deserializeFromStream(fis, envSettings);
        }
        catch (IOException | RuntimeException ex) {
            throw new BloomBoxException("Unable to load BloomBox from file " + bbxFile, ex);
        }
    }

    /**
     * Returns a builder to create a bloom box for the specified number of rows
     * 
     * @param numberOfRows number of records that will be fed into the box
     * @return builder
     */
    public static Builder forNumberOfRows(long numberOfRows) {
        return new Builder().forNumberOfRows(numberOfRows);
    }

    /**
     * For internal use: creates a sealed box (no feeding anymore) with the given config and the given store
     * 
     * @param config filter configuration
     * @param dataStore existing store (MUST match the config!)
     */
    protected BloomBox(BloomFilterConfig config, BloomBoxDataStore dataStore) {
        this.config = config;
        this.dataStore = dataStore;
        // no feeding
        this.feeder = new DataStoreFeeder(config, dataStore, true);
    }

    /**
     * For internal use: creates a box with the given settings, clients should use {@link #forNumberOfRows(long)} instead.
     * 
     * @param numberOfRows number of records to be fed into the box
     * @param numberOfColumns number of fields in a record
     * @param falsePositiveRateEpsilon the false-positive ratio (influences the box size and later query precision)
     * @param storeCreatorFunction function to create/provide a data store during construction
     * @param feederCreatorFunction function to create a feeder during construction
     */
    protected BloomBox(long numberOfRows, int numberOfColumns, double falsePositiveRateEpsilon,
            BiFunction<Integer, Long, BloomBoxDataStore> storeCreatorFunction,
            BiFunction<BloomFilterConfig, BloomBoxDataStore, DataStoreFeeder> feederCreatorFunction) {
        this.config = new BloomFilterConfig(numberOfColumns, falsePositiveRateEpsilon);
        LOGGER.debug("Creating bloom box for {}", this.config);
        LwGenericOHBF bloomFilter = new LwGenericOHBF(this.config);
        int vectorSize = bloomFilter.getBitVectorAsLongArray().length;
        long arraySize = numberOfRows * vectorSize;
        LOGGER.debug("Array size: {} ({} bytes)", arraySize, (arraySize * 8L));
        try {
            this.dataStore = storeCreatorFunction.apply(vectorSize, numberOfRows);
        }
        catch (OutOfMemoryError ex) {
            LOGGER.error("Unable to create BloomBox because configuration exceeds memory limits "
                    + "(reduce number of rows, try greater epsilon or provide a different store). Given configuration: {}", this.config);
            throw ex;
        }
        catch (RuntimeException ex) {
            throw new BloomBoxException(String.format("Unable to create BloomBox. Given configuration: %s", this.config), ex);
        }
        LOGGER.debug("Created dataStore for {} rows with {} columns, size: {} bytes", numberOfRows, numberOfColumns, 8L * arraySize);
        this.feeder = feederCreatorFunction.apply(this.config, this.dataStore);
    }

    /**
     * Closes the underlying data store (store implementations may behave differently)
     */
    public void close() {
        this.dataStore.close();
    }

    /**
     * @return this box's feeder
     */
    public DataStoreFeeder getFeeder() {
        return feeder;
    }

    /**
     * @return bloom filter configuration of this box
     */
    public BloomFilterConfig getConfig() {
        return config;
    }

    /**
     * @return reference to the data store
     */
    public BloomBoxDataStore getDataStore() {
        return dataStore;
    }

    /**
     * writes the box along with its datastore to the given stream, BBX-format
     * 
     * @param os destination
     * @throws IOException on any error
     */
    public void serializeToStream(OutputStream os) throws IOException {
        LOGGER.debug("Serializing bloom box to stream ...");
        HeaderUtil.writeBloomBoxHeader(os, new BloomBoxHeader(BloomBox.VERSION, config, dateCreated, description));
        dataStore.serializeToStream(os);
        LOGGER.debug("Bloom box serialization completed.");
    }

    /**
     * Writes the box including the datastore to the given file, BBX-format
     * 
     * @param bbxFile output file
     */
    public void saveToFile(File bbxFile) {
        LOGGER.debug("Saving bloom box to file {} ...", bbxFile);
        try (FileOutputStream fos = new FileOutputStream(bbxFile)) {
            serializeToStream(fos);
        }
        catch (IOException | RuntimeException ex) {
            throw new BloomBoxException("Unable to store BloomBox to file " + bbxFile, ex);
        }
    }

    /**
     * @return comment (optional)
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description a comment
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return creation date, by default date of instantiation of the instance (UTC)
     */
    public Date getDateCreated() {
        return dateCreated;
    }

    /**
     * @param dateCreated modify date of creation
     */
    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    /**
     * @return de-serialized box
     */
    Object readResolve() {
        // after de-serialization we seal the box, feeding is not possible anymore
        this.feeder = new DataStoreFeeder(config, dataStore, true);
        return this;
    }

    /**
     * Utility method for writing long values to bytes
     * 
     * @param value source value
     * @param result the long value's bytes, big endian
     */
    public static void longToBytes(long value, byte[] result) {
        for (int i = 7; i >= 0; i--) {
            result[i] = (byte) (value & 0xFF);
            value >>= 8;
        }
    }

    /**
     * Utility method for reading long values from a byte array
     * 
     * @param bytes buffer, size &gt;=8, big endian
     * @return long value
     */
    public static final long bytesToLong(byte[] bytes) {
        long result = 0;
        for (int i = 0; i < 8; i++) {
            result <<= 8;
            result |= (bytes[i] & 0xFF);
        }
        return result;
    }

    /**
     * BUILDER to conveniently setup a {@link BloomBox}
     *
     */
    public static final class Builder {

        /**
         * number of records to be fed into the box
         */
        private long numberOfRows = -1;

        /**
         * max number of fields expected in a record
         */
        private int numberOfColumns = -1;

        /**
         * false-positive rate of the bloom filter (each record)
         */
        private double falsePositiveRateEpsilon = -1;

        /**
         * Function to create a new store, by creates a {@link DefaultDataStore} by default
         */
        private BiFunction<Integer, Long, BloomBoxDataStore> storeCreatorFunction = (vectorSize, rowCount) -> new DefaultDataStore(vectorSize,
                rowCount.intValue());

        /**
         * Function to create a new feeder for the box, by default a {@link DataStoreFeeder}
         */
        private BiFunction<BloomFilterConfig, BloomBoxDataStore, DataStoreFeeder> feederCreatorFunction = (conf, store) -> new DataStoreFeeder(conf, store,
                false);

        /**
         * internal
         */
        private Builder() {
            // default
        }

        /**
         * @param numberOfRows number of records to be fed into the box
         * @return builder
         */
        public Builder forNumberOfRows(long numberOfRows) {
            this.numberOfRows = numberOfRows;
            return this;
        }

        /**
         * @param numberOfColumns number of fields per record
         * @return builder
         */
        public Builder withNumberOfColumns(int numberOfColumns) {
            this.numberOfColumns = numberOfColumns;
            return this;
        }

        /**
         * @param falsePositiveRateEpsilon false-positive rate for the underlying bloom filters
         * @return builder
         */
        public Builder withFalsePositiveRateEpsilon(double falsePositiveRateEpsilon) {
            this.falsePositiveRateEpsilon = falsePositiveRateEpsilon;
            return this;
        }

        /**
         * @param storeCreatorFunction the data store creation function
         * @return builder
         */
        public Builder withDataStore(BiFunction<Integer, Long, BloomBoxDataStore> storeCreatorFunction) {
            this.storeCreatorFunction = storeCreatorFunction;
            return this;
        }

        /**
         * @param feederCreatorFunction the feeder creation function
         * @return builder
         */
        public Builder withFeeder(BiFunction<BloomFilterConfig, BloomBoxDataStore, DataStoreFeeder> feederCreatorFunction) {
            this.feederCreatorFunction = feederCreatorFunction;
            return this;
        }

        /**
         * @return new box with the given settings
         */
        public BloomBox build() {
            if (numberOfRows <= 0 || numberOfColumns <= 0 || falsePositiveRateEpsilon <= 0d) {
                throw new BloomBoxException(String.format(
                        "Unable to create BloomBox, all basic settings must be greater than 0, given: numberOfRows=%d, numberOfColumns=%d, falsePositiveRateEpsilon=%d",
                        numberOfRows, numberOfColumns, falsePositiveRateEpsilon));
            }
            return new BloomBox(numberOfRows, numberOfColumns, falsePositiveRateEpsilon, storeCreatorFunction, feederCreatorFunction);
        }
    }
}
