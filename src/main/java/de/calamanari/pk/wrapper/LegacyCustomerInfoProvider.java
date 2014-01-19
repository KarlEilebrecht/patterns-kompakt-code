/*
 * Legacy Customer Info Provider
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

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import de.calamanari.pk.wrapper.legacy.OldSysCustomerMgr;
import de.calamanari.pk.wrapper.legacy.OldSysHistoryMgr;

/**
 * Legacy Customer Info Provider - creates customer infos, uses WRAPPERs for customer infos loaded from the legacy
 * OldSys
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class LegacyCustomerInfoProvider {

    /**
     * logger
     */
    protected static final Logger LOGGER = Logger.getLogger(LegacyCustomerInfoProvider.class.getName());

    /**
     * The new system uses string-IDs with this prefix, such customers are not in the legacy system.
     */
    private static final String STRING_ID_PREFIX = "ID";

    /**
     * Reference to customer manager of the legacy system
     */
    private final OldSysCustomerMgr customerMgr;

    /**
     * Recerence to history manager of the legacy system
     */
    private final OldSysHistoryMgr historyMgr;

    /**
     * Creates a new customer info provider for the legacy system.
     * @param customerMgr reference to the legacy customer manager (CONSTRUCTOR INJECTION)
     * @param historyMgr reference to the legacy customer manager (CONSTRUCTOR INJECTION)
     */
    public LegacyCustomerInfoProvider(OldSysCustomerMgr customerMgr, OldSysHistoryMgr historyMgr) {
        this.customerMgr = customerMgr;
        this.historyMgr = historyMgr;
    }

    /**
     * Returns the customer or null if not available
     * @param id identifies customer
     * @return customer info or null if not found
     */
    public CustomerInfo getCustomerInfo(String id) {
        LOGGER.fine("getCustomerInfo(" + id + ") called ...");
        CustomerInfo res = null;
        if (id != null && !(id.startsWith(STRING_ID_PREFIX))) {
            int legacyId = 0;
            try {
                legacyId = Integer.parseInt(id);
            }
            catch (Exception ex) {
                throw new IllegalArgumentException("Unable to parse the given ID!");
            }
            LOGGER.fine("calling Legacy API ...");
            String[] legacyCustomerData = customerMgr.getCustomerData(legacyId);
            String[] legacyHistoryData = historyMgr.getHistory(legacyId);
            if (legacyCustomerData != null) {
                res = new LegacyCustomerInfo(legacyCustomerData, legacyHistoryData);
            }
        }
        LOGGER.fine("returning customer info");
        return res;
    }

    /**
     * Returns all customers in the given customer segment
     * @param customerSegment segment to search
     * @return list of customer infos NEVER NULL
     */
    public List<CustomerInfo> findCustomerInfosOfSegment(int customerSegment) {
        LOGGER.fine("findCustomerInfosOfSegment(" + customerSegment + ") called ...");
        List<CustomerInfo> res = new ArrayList<>();
        LOGGER.fine("calling Legacy API ...");
        int[] ids = customerMgr.findCustomerByExample(new String[] { LegacyCustomerInfo.KEY_SEGMENT,
                "" + customerSegment });
        int len = ids.length;
        for (int i = 0; i < len; i++) {
            CustomerInfo customerInfo = getCustomerInfo("" + ids[i]);
            res.add(customerInfo);
        }
        LOGGER.fine("returning list");
        return res;
    }

}
