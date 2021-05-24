//@formatter:off
/*
 * DataPointDictionary
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
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.calamanari.pk.ohbf.bloombox.bbq.ExpressionIdUtil;

/**
 * The {@link DataPointDictionary} reduces the memory consumption for the data point probability vector by using shorter identifiers.
 * <p>
 * A regular dataPointId (see {@link ExpressionIdUtil#createDataPointId(String, String)} consumes a certain number of bits randomly. This randomness negatively
 * impacts the ability to compress the vector.<br>
 * Since a data point is a combination of key and value (column name, column value) in a row. Because we know that in any realistic scenario the number of
 * columns is limited and also the number of possible values (aka the domain of an attribute), it is worth building a dictionary, where the dataPointIds are
 * mapped to the index in order of first appearance.<br>
 * Not to overload the memory we collect {@link ExpressionIdUtil#MIN_GENERATED_DATA_POINT_ID} ids max.
 * <p>
 * When we now replace the dataPointId with the index value the ids are no longer random and compression works way better.
 * <p>
 * <b>Note:</b> Feeding by multiple threads concurrently requires external synchronization.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class DataPointDictionary implements Serializable {

    private static final long serialVersionUID = 8124461164964608724L;

    /**
     * the lookup of dataPointId to indexed value
     */
    private final Map<Integer, Integer> lookup;

    /**
     * Creates new dictionary with an initial capacity of {@link ExpressionIdUtil#MIN_GENERATED_DATA_POINT_ID} / 2 elements
     */
    public DataPointDictionary() {
        this.lookup = new HashMap<>(ExpressionIdUtil.MIN_GENERATED_DATA_POINT_ID / 2);
    }

    /**
     * Feeds the given dataPointId if it is not already present and the dictionary is not yet full.
     * 
     * @param key candidate &gt;= {@value ExpressionIdUtil#MIN_GENERATED_DATA_POINT_ID}
     * @return lookup id &lt; {@value ExpressionIdUtil#MIN_GENERATED_DATA_POINT_ID} or dataPointId &gt;= {@value ExpressionIdUtil#MIN_GENERATED_DATA_POINT_ID}
     * @throws IllegalArgumentException if the dataPointId < {@value ExpressionIdUtil#MIN_GENERATED_DATA_POINT_ID}
     */
    public int feed(int dataPointId) {
        if (dataPointId < ExpressionIdUtil.MIN_GENERATED_DATA_POINT_ID) {
            throw new IllegalArgumentException(String.format("A dataPointId to be included in dictionary must not be < %d, given: %d",
                    ExpressionIdUtil.MIN_GENERATED_DATA_POINT_ID, dataPointId));
        }
        Integer res = lookup.get(dataPointId);
        if (res == null && lookup.size() < ExpressionIdUtil.MIN_GENERATED_DATA_POINT_ID) {
            res = lookup.size();
            lookup.put(dataPointId, res);
        }
        return res != null ? res : dataPointId;
    }

    /**
     * Maps the given id to the one found in the dictionary or returns the given id.
     * 
     * @param dataPointId raw id to be mapped
     * @return mapped id or the given id if not found
     */
    public int lookup(int dataPointId) {
        Integer res = lookup.get(dataPointId);
        return res != null ? res : dataPointId;
    }

    /**
     * @return all keys in the dictionary in insertion order, so that the original lookup can be re-built from it.
     */
    public int[] toIntArray() {
        List<Map.Entry<Integer, Integer>> entries = new ArrayList<>(lookup.entrySet());
        Collections.sort(entries, (e1, e2) -> e1.getValue().compareTo(e2.getValue()));
        return entries.stream().map(Map.Entry::getKey).mapToInt(x -> x).toArray();
    }

}
