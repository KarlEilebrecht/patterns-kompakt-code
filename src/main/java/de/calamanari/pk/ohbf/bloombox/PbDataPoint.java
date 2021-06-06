//@formatter:off
/*
 * PbDataPoint
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

import java.util.Comparator;

import de.calamanari.pk.ohbf.bloombox.bbq.ExpressionIdUtil;

/**
 * A {@link PbDataPoint} is a feeding element that represents a key/value combination with attached probability.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class PbDataPoint extends DataPoint {

    private static final long serialVersionUID = 2084751987865254150L;

    /**
     * Allows ordering of data points according to their {@link #getLpDataPointId()}
     */
    public static final Comparator<PbDataPoint> LPID_ORDER_COMPARATOR = (PbDataPoint dp1, PbDataPoint dp2) -> dp1.lpDataPointId - dp2.lpDataPointId;

    /**
     * probability for this data point [0..1]
     */
    private final double probability;

    /**
     * low precision data point id to identify a point in a row (low precision, see {@link ExpressionIdUtil#createLpDataPointId(String, Object)}
     */
    private final int lpDataPointId;

    /**
     * @param columnId field name <b>case-sensitive</b>
     * @param columnValue field value <b>case-sensitive</b>
     * @param probability <code>0.0 &lt;= value &lt;= 1.0</code>
     */
    public PbDataPoint(String columnId, String columnValue, double probability) {
        super(columnId, columnValue);
        this.lpDataPointId = ExpressionIdUtil.createLpDataPointId(super.getDataPointId());
        this.probability = probability;
    }

    /**
     * @return data point probability
     */
    public double getProbability() {
        return probability;
    }

    /**
     * @return low precision data point id to identify a point in a row, see {@link ExpressionIdUtil#createLpDataPointId(String, Object)}
     */
    public int getLpDataPointId() {
        return lpDataPointId;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " [dataPointId=" + super.getDataPointId() + ", lpDataPointId=" + lpDataPointId + ", columnId="
                + super.getColumnId() + ", columnValue=" + super.getColumnValue() + ", probability=" + probability + "]";
    }

}
