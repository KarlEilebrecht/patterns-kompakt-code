//@formatter:off
/*
 * PbDataStoreFeeder
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.calamanari.pk.ohbf.BloomFilterConfig;
import de.calamanari.pk.ohbf.LwGenericOHBF;

/**
 * Specialized store feeder for a {@link PbDataStore} with attached probabilities.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class PbDataStoreFeeder extends DataStoreFeeder {

    private static final Logger LOGGER = LoggerFactory.getLogger(PbDataStoreFeeder.class);

    /**
     * @param config bloom filter configuration
     * @param dataStore the underlying store with probabilities
     * @param markSealed true to mark this store sealed
     */
    public PbDataStoreFeeder(BloomFilterConfig config, BloomBoxDataStore dataStore, boolean markSealed) {
        super(config, dataStore, markSealed);
        if (!(dataStore instanceof PbDataStore)) {
            throw new IllegalArgumentException(String.format("%s requires a %s, given: %s", this.getClass().getSimpleName(), PbDataStore.class.getSimpleName(),
                    String.valueOf(dataStore)));
        }
    }

    @Override
    public boolean addRow(Map<String, ?> columnMap) {
        return this.addRow(columnMap.entrySet().stream().map(e -> new DataPoint(e.getKey(), convertValueToString(e.getKey(), e.getValue()), 1.0d))
                .collect(Collectors.toList()));
    }

    @Override
    public boolean addRow(String[] columnIds, Object[] columnValues) {
        List<DataPoint> dataPoints = new ArrayList<>(columnIds.length);
        for (int i = 0; i < columnIds.length; i++) {
            String columnName = columnIds[i];
            Object columnValue = columnValues[i];
            dataPoints.add(new DataPoint(columnName, convertValueToString(columnName, columnValue), 1.0d));
        }
        return this.addRow(dataPoints);
    }

    @Override
    public boolean addRow(List<String> columnIds, List<?> columnValues) {
        List<DataPoint> dataPoints = new ArrayList<>(columnIds.size());
        for (int i = 0; i < columnIds.size(); i++) {
            String columnName = columnIds.get(i);
            Object columnValue = columnValues.get(i);
            dataPoints.add(new DataPoint(columnName, convertValueToString(columnName, columnValue), 1.0d));
        }
        return this.addRow(dataPoints);
    }

    /**
     * Adds the given list of data points as a row to the store including the probabilities
     * 
     * @param dataPoints key/value combination (unique within a row) with attached probability
     * @return true if the row was added, false if the store was already full
     */
    public boolean addRow(List<DataPoint> dataPoints) {
        long[] dppVector = ProbabilityVectorCodec.createDataPointProbabilityVector(dataPoints);
        synchronized (storeMonitor) {
            if (moveToNextRow()) {
                LwGenericOHBF bloomFilter = bloomFilterHolder.get();
                bloomFilter.clear();
                for (int i = 0; i < dataPoints.size(); i++) {
                    DataPoint dataPoint = dataPoints.get(i);
                    if (!ProbabilityVectorCodec.isEffectivelyZero(dataPoint.getProbability())) {
                        bloomFilter.put(dataPoint.getColumnId(), dataPoint.getColumnValue());
                    }
                }
                ((PbDataStore) dataStore).feedRow(bloomFilter.getBitVectorAsLongArray(), currentRowIndex, dppVector);
                return true;
            }
            else {
                return false;
            }
        }
    }

    /**
     * Converts the given value to a String for further processing
     * 
     * @param columnName field name
     * @param columnValue field value
     * @return String value of columnValue
     */
    protected String convertValueToString(String columnName, Object columnValue) {
        String res = null;
        if (columnValue != null) {
            if (columnValue instanceof String) {
                res = (String) columnValue;
            }
            else {
                LOGGER.warn("Feeding with implicit columnValue.toString() conversion may lead to unexpected results, given: columnName={}, columnValue={} ({})",
                        columnName, columnValue, columnValue.getClass().getSimpleName());
                res = columnValue.toString();
            }
        }
        return res;
    }

}
