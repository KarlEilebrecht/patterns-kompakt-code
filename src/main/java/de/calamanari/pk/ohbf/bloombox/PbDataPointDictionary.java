//@formatter:off
/*
 * PbDataPointDictionary
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
 * The {@link PbDataPointDictionary} reduces the memory consumption for the data point probability vector by using shorter identifiers.
 * <p>
 * A regular low-precision lpDataPointId (see {@link ExpressionIdUtil#createLpDataPointId(String, String)} consumes a certain number of bits randomly. This
 * randomness negatively impacts the ability to compress the vector.<br>
 * Since a data point is a combination of key and value (column name, column value) in a row. Because we know that in any realistic scenario the number of
 * columns is limited and also the number of possible values (aka the domain of an attribute), it is worth building a dictionary, where the dataPointIds are
 * mapped to the index in order of first appearance.<br>
 * Not to overload the memory we collect {@link ExpressionIdUtil#MIN_GENERATED_DATA_POINT_ID} ids max.
 * <p>
 * When we now replace the lpDataPointId with the index value the ids are no longer random and compression works way better.
 * <p>
 * <b>Note:</b> Feeding by multiple threads concurrently requires external synchronization.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class PbDataPointDictionary implements Serializable {

    private static final long serialVersionUID = 8124461164964608724L;

    /**
     * the lookup of lpDataPointId to indexed value
     */
    private final Map<Integer, Integer> lookup;

    /**
     * Creates new dictionary with an initial capacity of {@link ExpressionIdUtil#MIN_GENERATED_DATA_POINT_ID} / 2 elements
     */
    public PbDataPointDictionary() {
        this.lookup = new HashMap<>(ExpressionIdUtil.MIN_GENERATED_DATA_POINT_ID / 2);
    }

    /**
     * Feeds the given lpDataPointId if it is not already present and the dictionary is not yet full.
     * 
     * @param lpDataPointId key candidate &gt;= {@value ExpressionIdUtil#MIN_GENERATED_DATA_POINT_ID}
     * @return lookup id &lt; {@value ExpressionIdUtil#MIN_GENERATED_DATA_POINT_ID} or lpDataPointId &gt;= {@value ExpressionIdUtil#MIN_GENERATED_DATA_POINT_ID}
     * @throws IllegalArgumentException if the lpDataPointId < {@value ExpressionIdUtil#MIN_GENERATED_DATA_POINT_ID}
     */
    public int feed(int lpDataPointId) {
        if (lpDataPointId < ExpressionIdUtil.MIN_GENERATED_DATA_POINT_ID) {
            throw new IllegalArgumentException(String.format("A dataPointId to be included in dictionary must not be < %d, given: %d",
                    ExpressionIdUtil.MIN_GENERATED_DATA_POINT_ID, lpDataPointId));
        }
        Integer res = lookup.get(lpDataPointId);
        if (res == null && lookup.size() < ExpressionIdUtil.MIN_GENERATED_DATA_POINT_ID) {
            res = lookup.size();
            lookup.put(lpDataPointId, res);
        }
        return res != null ? res : lpDataPointId;
    }

    /**
     * Maps the given id to the one found in the dictionary or returns the given id.
     * 
     * @param lpDataPointId raw id to be mapped
     * @return mapped id or the given id if not found
     */
    public int lookup(int lpDataPointId) {
        Integer res = lookup.get(lpDataPointId);
        return res != null ? res : lpDataPointId;
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
