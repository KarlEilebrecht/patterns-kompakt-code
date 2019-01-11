//@formatter:off
/*
 * OldSys History Manager Mock
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

import java.util.HashMap;
import java.util.Map;

import de.calamanari.pk.wrapper.legacy.OldSysHistoryMgr;

/**
 * OldSys History Manager Mock - mocks the old system's history manager.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class OldSysHistoryMgrMock extends OldSysHistoryMgr {

    /**
     * stores the test data for system test
     */
    private final Map<Integer, String[]> testData = new HashMap<>();

    /**
     * New mock
     */
    public OldSysHistoryMgrMock() {

    }

    /**
     * Method allows to add test data.
     * 
     * @param customerId id of customer
     * @param historyData historical data about customer
     */
    public void addTestRecord(int customerId, String[] historyData) {
        testData.put(customerId, historyData);
    }

    @Override
    public String[] getHistory(int customerId) {
        return testData.getOrDefault(customerId, OldSysHistoryMgr.NO_RESULT);
    }

}
