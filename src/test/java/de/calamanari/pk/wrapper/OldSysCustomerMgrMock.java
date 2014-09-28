//@formatter:off
/*
 * OldSys Customer Manager Mock
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
//@formatter:on
package de.calamanari.pk.wrapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import de.calamanari.pk.wrapper.legacy.OldSysCustomerMgr;

/**
 * OldSys Customer Manager Mock - mocks the old system's customer manager.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class OldSysCustomerMgrMock extends OldSysCustomerMgr {

    /**
     * logger
     */
    protected static final Logger LOGGER = Logger.getLogger(OldSysCustomerMgrMock.class.getName());

    /**
     * stores the test data for system test
     */
    private final Map<Integer, String[]> testData = new HashMap<>();

    /**
     * New mock
     */
    public OldSysCustomerMgrMock() {

    }

    /**
     * Method allows to add test data.
     * 
     * @param customerId id of customer
     * @param customerData data about customer
     */
    public void addTestRecord(int customerId, String[] customerData) {
        testData.put(customerId, customerData);
    }

    @Override
    public String[] getCustomerData(int customerId) {
        return testData.get(customerId);
    }

    @Override
    public int[] findCustomerByExample(String[] searchData) {
        List<Integer> resultList = new ArrayList<>();
        Map<String, String> searchMap = createMapFromLegacyRecord(searchData);
        for (Map.Entry<Integer, String[]> entry : testData.entrySet()) {
            if (matchCustomer(searchMap, createMapFromLegacyRecord(entry.getValue()))) {
                resultList.add(entry.getKey());
            }
        }

        int len = resultList.size();
        int[] res = new int[len];
        for (int i = 0; i < len; i++) {
            res[i] = resultList.get(i);
        }
        return res;
    }

    /**
     * Matches customer with expected data
     * 
     * @param expectedData this fields are requested
     * @param customerData current customer
     * @return true if customer matches example
     */
    private boolean matchCustomer(Map<String, String> expectedData, Map<String, String> customerData) {
        boolean match = true;
        for (Map.Entry<String, String> entry : expectedData.entrySet()) {
            String key = entry.getKey();
            String expectedValue = entry.getValue();
            String customerValue = customerData.get(key);
            if ((customerValue == null && expectedValue == null) || (customerValue != null && customerValue.equals(expectedValue))) {
                continue;
            }
            else {
                match = false;
                break;
            }
        }
        return match;
    }

    /**
     * Helper method, creates map from a legacy record.
     * 
     * @param customerData fields from customer record
     * @return map key/value
     */
    private static Map<String, String> createMapFromLegacyRecord(String[] customerData) {
        Map<String, String> res = new HashMap<>();
        for (int i = 0; i < customerData.length; i = i + 2) {
            res.put(customerData[i], customerData[i + 1]);
        }
        return res;
    }
}
