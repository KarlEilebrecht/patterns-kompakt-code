//@formatter:off
/*
 * Factory method test case - demonstrates FACTORY METHOD.
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
package de.calamanari.pk.factorymethod;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.calamanari.pk.util.LogUtils;
import de.calamanari.pk.util.MiscUtils;

/**
 * Test case for FACTOY METHOD
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class FactoryMethodTest {

    /**
     * logger
     */
    protected static final Logger LOGGER = Logger.getLogger(FactoryMethodTest.class.getName());

    /**
     * Log-level for this test
     */
    private static final Level LOG_LEVEL = Level.INFO;

    /**
     * for the testcases we simulate some kind of registry.
     */
    protected static final HashMap<String, AbstractVoucherCreator> SYSTEM_REGISTRY = new HashMap<>();

    /**
     * Key for "registry access"
     */
    private static final String COMPANY_KEY_MORONSTORE = "Moron Store";

    /**
     * Key for "registry access"
     */
    private static final String COMPANY_KEY_FREAKLIES = "Freaklies Shop";

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        LogUtils.setConsoleHandlerLogLevel(LOG_LEVEL);
        LogUtils.setLogLevel(LOG_LEVEL, FactoryMethodTest.class, AbstractVoucher.class, AbstractVoucherCreator.class, FreakliesShopVoucher.class,
                FreakliesShopVoucherCreator.class, MoronStoreVoucher.class, MoronStoreVoucherCreator.class);
    }

    @Before
    public void setUp() throws Exception {
        SYSTEM_REGISTRY.put(COMPANY_KEY_MORONSTORE, new MoronStoreVoucherCreator());
        SYSTEM_REGISTRY.put(COMPANY_KEY_FREAKLIES, new FreakliesShopVoucherCreator());
    }

    @Test
    public void testFactoryMethod() {

        // Hint: set the log-level above to FINE to see FACTORY METHOD at work.

        LOGGER.info("Test Factory Method ...");
        long startTimeNanos = System.nanoTime();

        String[] companies = new String[] { COMPANY_KEY_MORONSTORE, COMPANY_KEY_FREAKLIES };

        StringBuilder sb = new StringBuilder();

        for (String companyKey : companies) {
            AbstractVoucherCreator creator = SYSTEM_REGISTRY.get(companyKey);
            String voucher = creator.createVoucher("Jack", "Miller", 100).toString();
            LOGGER.info("Voucher for " + companyKey + ": " + voucher);
            sb.append(voucher);
        }

        assertEquals("MoronStoreVoucher({id=777777, displayCode=bde31, firstName=Jack, lastName=Miller, value=100.0})"
                + "FreakliesShopVoucher({id=S11111111, displayCode=vjm11111111, " + "firstName=Jack, lastName=Miller, value=100.0})", sb.toString());

        LOGGER.info("Test Factory Method successful! Elapsed time: " + MiscUtils.formatNanosAsSeconds(System.nanoTime() - startTimeNanos) + " s");
    }

}
