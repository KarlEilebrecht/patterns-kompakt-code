//@formatter:off
/*
 * PbDpav
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
 * A {@link PbDpav} is a feeding element that represents a key/value combination with attached probability.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class PbDpav extends Dpav {

    private static final long serialVersionUID = 2084751987865254150L;

    /**
     * Allows ordering of low-precision DPAVs according to their {@link #getLpDpavId()}
     */
    public static final Comparator<PbDpav> LP_DPAV_ID_ORDER_COMPARATOR = (PbDpav dp1, PbDpav dp2) -> dp1.lpDpavId - dp2.lpDpavId;

    /**
     * probability for this data point attribute combination [0..1]
     */
    private final double probability;

    /**
     * low precision DPAV-id to identify a dpav within a row (low precision, see {@link ExpressionIdUtil#createLpDpavId(String, Object)}
     */
    private final int lpDpavId;

    /**
     * @param columnId field name <b>case-sensitive</b>
     * @param columnValue field value <b>case-sensitive</b>
     * @param probability <code>0.0 &lt;= value &lt;= 1.0</code>
     */
    public PbDpav(String columnId, String columnValue, double probability) {
        super(columnId, columnValue);
        this.lpDpavId = ExpressionIdUtil.createLpDpavId(super.getDpavId());
        this.probability = probability;
    }

    /**
     * @return dpav probability
     */
    public double getProbability() {
        return probability;
    }

    /**
     * @return low precision DPAV-id to identify an attribute/value combination in a row, see {@link ExpressionIdUtil#createLpDpavId(String, Object)}
     */
    public int getLpDpavId() {
        return lpDpavId;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " [dpavId=" + super.getDpavId() + ", lpDpavId=" + lpDpavId + ", columnId=" + super.getColumnId()
                + ", columnValue=" + super.getColumnValue() + ", probability=" + probability + "]";
    }

}
