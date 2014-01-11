/*
 * Legay Customer Info
 * Code-Beispiel zum Buch Patterns Kompakt, Verlag Springer Vieweg
 * Copyright 2014 Karl Eilebrecht
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
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
package de.calamanari.pk.wrapper;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

/**
 * Legacy Customer Info - a WRAPPER for the OldSys legacy system
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 * 
 */
public class LegacyCustomerInfo implements CustomerInfo {

    /**
     * logger
     */
    protected static final Logger LOGGER = Logger.getLogger(LegacyCustomerInfo.class.getName());

    /**
     * legacy id field
     */
    public static final String KEY_ID = "id";

    /**
     * legacy name field
     */
    public static final String KEY_NAME = "name";

    /**
     * legacy customer segment field
     */
    public static final String KEY_SEGMENT = "seg";

    /**
     * legacy last order date
     */
    public static final String KEY_LAST_ORDER_DATE = "lod";

    /**
     * wrapped customer data
     */
    private final String[] legacyCustomerData;

    /**
     * wrapped history data
     */
    private final String[] legacyHistoryData;

    /**
     * Creates new legacy customer info
     * @param legacyCustomerData NOT NULL
     * @param legacyHistoryData may be null
     */
    public LegacyCustomerInfo(String[] legacyCustomerData, String[] legacyHistoryData) {
        LOGGER.fine("creating new Legacy Customer Info ...");
        this.legacyCustomerData = legacyCustomerData;
        this.legacyHistoryData = legacyHistoryData;
        LOGGER.fine("returning Legacy Customer Info.");
    }

    @Override
    public String getId() {
        LOGGER.fine("getId() called");
        String id = getValue(legacyCustomerData, KEY_ID);
        LOGGER.fine("returning id=" + id);
        return id;
    }

    @Override
    public String getName() {
        LOGGER.fine("getName() called");
        String name = getValue(legacyCustomerData, KEY_NAME);
        LOGGER.fine("returning name=" + name);
        return name;
    }

    @Override
    public int getCustomerSegment() {
        LOGGER.fine("getCustomerSegment() called");
        String sSegment = getValue(legacyCustomerData, KEY_SEGMENT);
        int segment = 0;
        if (sSegment != null) {
            try {
                segment = Integer.parseInt(sSegment);
            }
            catch (Exception ex) {
                throw new RuntimeException(ex);
            }
        }
        LOGGER.fine("returning segment=" + segment);
        return segment;
    }

    @Override
    public Date getLastOrderDate() {
        LOGGER.fine("getLastOrderDate() called");
        String sLod = getValue(legacyHistoryData, KEY_LAST_ORDER_DATE);
        Date lod = null;
        if (sLod != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            try {
                lod = sdf.parse(sLod);
            }
            catch (ParseException ex) {
                throw new RuntimeException(ex);
            }
        }
        LOGGER.fine("returning lastOrderDate=" + lod);
        return lod;
    }

    /**
     * Returns the value from the legacy record for the given key
     * @param record customer data or history data
     * @param key field name
     * @return value
     */
    private String getValue(String[] record, String key) {
        LOGGER.fine("Interpreting legacy data structure ...");
        String res = null;
        if (record != null) {
            int len = record.length;
            for (int i = 0; i < len; i = i + 2) {
                if (key.equals(record[i])) {
                    res = record[i + 1];
                    break;
                }
            }
        }
        LOGGER.fine("Returning converted legacy data.");
        return res;
    }

    /**
     * Returns string representation for debugging purposes
     * @return string representation
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.getClass().getSimpleName() + "({");
        sb.append("ID='");
        sb.append(getId());
        sb.append("', ");
        sb.append("Name='");
        sb.append(getName());
        sb.append("', ");
        sb.append("Segment=");
        sb.append(getCustomerSegment());
        sb.append(", ");
        sb.append("Last Order Date='");
        String sDate = "N/A";
        Date lod = getLastOrderDate();
        if (lod != null) {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            sDate = sdf.format(lod);
        }
        sb.append(sDate);
        sb.append("'})");
        return sb.toString();
    }

}
