//@formatter:off
/*
 * Dpav
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
import java.util.Comparator;

import de.calamanari.pk.ohbf.bloombox.bbq.ExpressionIdUtil;

/**
 * A {@link Dpav} (data point attribute value) globally uniquely identifies a key/value combination like <code>color=blue</code>, see also
 * {@link ExpressionIdUtil#createDpavId(String, Object)}. By collecting all DPAVs referenced in a query you can find out what source data needs to be included
 * for feeding the underlying BloomBox.
 * <p>
 * In the context of a row the absence or presence of a DPAV marks the binary yes/no decision whether an attribute/value combination exists in that row or not.
 * In the above example: <code>color=blue</code>.<br>
 * In a scenario with multi-value-support for a given attribute like <code>favoriteColor</code> both DPAVs <code>favoriteColor=blue</code> and
 * <code>favoriteColor=red</code> could be present in the same row.
 * <p>
 * <b>Note:</b> Values have been restricted to type String to avoid unnecessary complexity, so for leveraging DPAVs the user must convert any other value types
 * beforehand.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class Dpav implements Serializable {

    private static final long serialVersionUID = 2084766687865254150L;

    /**
     * Allows sorting of DPAVs according to the natural order of their {@link #getDpavId()}s
     */
    public static final Comparator<Dpav> DPAV_ID_ORDER_COMPARATOR = (Dpav dp1, Dpav dp2) -> {
        long delta = (dp1.dpavId - dp2.dpavId);
        int res = 0;
        if (delta < 0) {
            res = -1;
        }
        else if (delta > 0) {
            res = 1;
        }
        return res;
    };

    /**
     * Field name
     */
    private final String columnId;

    /**
     * Field value
     */
    private final String columnValue;

    /**
     * Globally unique data point attribute value id to identify a DPAV (see {@link ExpressionIdUtil#createDpavId(String, Object)}
     */
    private final long dpavId;

    /**
     * @param columnId field name <b>case-sensitive</b>
     * @param columnValue field value <b>case-sensitive</b>
     */
    public Dpav(String columnId, String columnValue) {
        this.columnId = columnId;
        this.columnValue = columnValue;
        this.dpavId = ExpressionIdUtil.createDpavId(columnId, columnValue);
    }

    /**
     * @return field name
     */
    public String getColumnId() {
        return columnId;
    }

    /**
     * @return field value
     */
    public String getColumnValue() {
        return columnValue;
    }

    /**
     * @return globally unique DPAV-id created from {@link #columnId} and {@link #columnValue} (case-sensitive), see
     *         {@link ExpressionIdUtil#createDpavId(String, Object)}
     */
    public long getDpavId() {
        return dpavId;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " [dpavId=" + dpavId + ", columnId=" + columnId + ", columnValue=" + columnValue + "]";
    }

}
