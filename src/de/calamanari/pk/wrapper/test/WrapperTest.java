/*
 * Wrapper Test - demonstrates WRAPPER pattern
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
package de.calamanari.pk.wrapper.test;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.calamanari.pk.util.LogUtils;
import de.calamanari.pk.util.MiscUtils;
import de.calamanari.pk.wrapper.CustomerInfo;
import de.calamanari.pk.wrapper.LegacyCustomerInfo;
import de.calamanari.pk.wrapper.LegacyCustomerInfoProvider;
import de.calamanari.pk.wrapper.legacy.OldSysCustomerMgr;

/**
 * Wrapper Test - demonstrates WRAPPER pattern
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class WrapperTest {

    /**
     * logger
     */
    protected static final Logger LOGGER = Logger.getLogger(WrapperTest.class.getName());

    /**
     * Log-level for this test
     */
    private static final Level LOG_LEVEL = Level.INFO;

    /**
     * Legacy Customer Info Provider for testing the wrapper
     */
    private LegacyCustomerInfoProvider legacyCustomerInfoProvider = null;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        LogUtils.setConsoleHandlerLogLevel(LOG_LEVEL);
        LogUtils.setLogLevel(LOG_LEVEL, WrapperTest.class, OldSysCustomerMgrMock.class, OldSysHistoryMgrMock.class,
                OldSysCustomerMgr.class, OldSysHistoryMgrMock.class, LegacyCustomerInfo.class,
                LegacyCustomerInfoProvider.class);
    }

    @Before
    public void setUp() throws Exception {
        OldSysCustomerMgrMock testCustomerManager = null;
        OldSysHistoryMgrMock testHistoryManager = null;

        testCustomerManager = new OldSysCustomerMgrMock();
        testHistoryManager = new OldSysHistoryMgrMock();
        testCustomerManager.addTestRecord(1, new String[] { LegacyCustomerInfo.KEY_ID, "1",
                LegacyCustomerInfo.KEY_NAME, "John Doe", LegacyCustomerInfo.KEY_SEGMENT, "67" });
        testCustomerManager.addTestRecord(2, new String[] { LegacyCustomerInfo.KEY_ID, "2",
                LegacyCustomerInfo.KEY_NAME, "Lara Crofft", LegacyCustomerInfo.KEY_SEGMENT, "67" });
        testCustomerManager.addTestRecord(3, new String[] { LegacyCustomerInfo.KEY_ID, "3",
                LegacyCustomerInfo.KEY_NAME, "Sandy Bridge", LegacyCustomerInfo.KEY_SEGMENT, "11" });
        testCustomerManager.addTestRecord(4, new String[] { LegacyCustomerInfo.KEY_ID, "4",
                LegacyCustomerInfo.KEY_NAME, "Jack Miller", LegacyCustomerInfo.KEY_SEGMENT, "11" });
        testCustomerManager.addTestRecord(5, new String[] { LegacyCustomerInfo.KEY_ID, "5",
                LegacyCustomerInfo.KEY_NAME, "Ren Stimpy", LegacyCustomerInfo.KEY_SEGMENT, "20" });

        testHistoryManager.addTestRecord(1, new String[] { LegacyCustomerInfo.KEY_ID, "1",
                LegacyCustomerInfo.KEY_LAST_ORDER_DATE, "2010-12-11" });
        testHistoryManager.addTestRecord(2, new String[] { LegacyCustomerInfo.KEY_ID, "2",
                LegacyCustomerInfo.KEY_LAST_ORDER_DATE, "2010-02-01" });
        testHistoryManager.addTestRecord(3, new String[] { LegacyCustomerInfo.KEY_ID, "3",
                LegacyCustomerInfo.KEY_LAST_ORDER_DATE, "2011-01-20" });
        testHistoryManager.addTestRecord(4, new String[] { LegacyCustomerInfo.KEY_ID, "4",
                LegacyCustomerInfo.KEY_LAST_ORDER_DATE, "2011-03-24" });
        testHistoryManager.addTestRecord(5, new String[] { LegacyCustomerInfo.KEY_ID, "5",
                LegacyCustomerInfo.KEY_LAST_ORDER_DATE, "2010-12-20" });

        legacyCustomerInfoProvider = new LegacyCustomerInfoProvider(testCustomerManager, testHistoryManager);

    }

    @Test
    public void testWrapper() {

        // hint: adjust the log-levels above to FINE to see the Wrapper working

        LOGGER.info("Test Wrapper ...");
        long startTimeNanos = System.nanoTime();

        CustomerInfo info3 = legacyCustomerInfoProvider.getCustomerInfo("3");
        String sInfo3 = info3.toString();
        LOGGER.fine(sInfo3);

        assertEquals("3", info3.getId());
        assertEquals("Sandy Bridge", info3.getName());
        assertEquals(11, info3.getCustomerSegment());
        assertEquals("LegacyCustomerInfo({ID='3', Name='Sandy Bridge', Segment=11, Last Order Date='2011-01-20'})",
                sInfo3);

        List<CustomerInfo> list = legacyCustomerInfoProvider.findCustomerInfosOfSegment(11);
        assertEquals(2, list.size());
        LOGGER.fine("" + list);

        List<CustomerInfo> list2 = legacyCustomerInfoProvider.findCustomerInfosOfSegment(20);
        assertEquals(1, list2.size());
        CustomerInfo info5 = list2.get(0);
        String sInfo5 = info5.toString();
        assertEquals("LegacyCustomerInfo({ID='5', Name='Ren Stimpy', Segment=20, Last Order Date='2010-12-20'})",
                sInfo5);
        LOGGER.fine(sInfo5);

        LOGGER.info("Test Wrapper successful! Elapsed time: "
                + MiscUtils.formatNanosAsSeconds(System.nanoTime() - startTimeNanos) + " s");

    }

}
