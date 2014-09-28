//@formatter:off
/*
 * Bridge Test - demonstrates BRIDGE pattern.
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
package de.calamanari.pk.bridge;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.calamanari.pk.bridge.halcorp.HalCorpPersonDataConnectorImp;
import de.calamanari.pk.bridge.halcorp.HalCorpSecurePersonDataConnectorImp;
import de.calamanari.pk.bridge.multiglom.MultiGlomPersonDataConnectorImp;
import de.calamanari.pk.bridge.multiglom.MultiGlomUdpPersonDataConnectorImp;
import de.calamanari.pk.util.LogUtils;
import de.calamanari.pk.util.MiscUtils;

/**
 * Bridge Test - demonstrates BRIDGE pattern.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class BridgeTest {

    /**
     * logger
     */
    protected static final Logger LOGGER = Logger.getLogger(BridgeTest.class.getName());

    /**
     * Log-level for this test
     */
    private static final Level LOG_LEVEL = Level.INFO;

    /**
     * Test-IDs
     */
    private List<String> halCorpPersonTestIds = new ArrayList<>();

    /**
     * PersonDataConnectorImp for HalCorp
     */
    private PersonDataConnectorImp halCorpImp = null;

    /**
     * Test-IDs
     */
    private List<String> multiGlomPersonTestIds = new ArrayList<>();

    /**
     * PersonDataConnectorImp for MultiGlom
     */
    private PersonDataConnectorImp multiGlomImp = null;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        LogUtils.setConsoleHandlerLogLevel(LOG_LEVEL);
        LogUtils.setLogLevel(LOG_LEVEL, BridgeTest.class, PersonDataConnector.class, RwPersonDataConnector.class, HalCorpPersonDataConnectorImp.class,
                HalCorpSecurePersonDataConnectorImp.class, MultiGlomPersonDataConnectorImp.class, MultiGlomUdpPersonDataConnectorImp.class);
    }

    @Before
    public void setUp() throws Exception {
        halCorpPersonTestIds.clear();
        multiGlomPersonTestIds.clear();
        HalCorpSecurePersonDataConnectorImp imp1 = new HalCorpSecurePersonDataConnectorImp();
        MultiGlomUdpPersonDataConnectorImp imp2 = new MultiGlomUdpPersonDataConnectorImp();
        Person p1 = new Person(null, "Jack", "Miller", "General Manager");
        Person p2 = new Person(null, "Susi", "Miller", "General Manager");

        halCorpPersonTestIds.add(imp1.createNewPerson(p1));
        multiGlomPersonTestIds.add(imp2.createNewPerson(p2));
        Address a1 = new Address(null, p1.getId(), "Lame Duck Valley 180", "Rugby", "13476");
        Address a2 = new Address(null, p2.getId(), "Lame Duck Valley 180", "Rugby", "13476");
        imp1.createNewAddress(a1);
        imp2.createNewAddress(a2);

        Person p3 = new Person(null, "Udo", "Lohmeier", "Controller");
        Person p4 = new Person(null, "Chantalle", "Lohmeier", "Senior Consultant");
        halCorpPersonTestIds.add(imp1.createNewPerson(p3));
        multiGlomPersonTestIds.add(imp2.createNewPerson(p4));
        Address a3 = new Address(null, p3.getId(), "Ockerway 11", "Rugby", "13476");
        Address a4 = new Address(null, p4.getId(), "Ockerway 11", "Rugby", "13476");
        imp1.createNewAddress(a3);
        imp2.createNewAddress(a4);

        halCorpImp = imp1;
        multiGlomImp = imp2;
    }

    @Test
    public void testBridge() {

        // hint: set the log-level above to FINE to watch BRIDGE working.

        LOGGER.info("Test Bridge ...");
        long startTimeNanos = System.nanoTime();

        PersonDataConnector halCorpConnector = new PersonDataConnector(halCorpImp);
        for (String id : halCorpPersonTestIds) {
            assertTrue(halCorpConnector.checkPersonExistsById(id));
        }

        assertEquals("Person({id=HC1001, firstName=Jack, lastName=Miller, role=General Manager})", halCorpConnector.findPersonById(halCorpPersonTestIds.get(0))
                .toString());

        PersonDataConnector multiGlomConnector = new PersonDataConnector(multiGlomImp);
        for (String id : multiGlomPersonTestIds) {
            assertTrue(multiGlomConnector.checkPersonExistsById(id));
        }

        assertEquals("Person({id=MG6667, firstName=Susi, lastName=Miller, role=General Manager})",
                multiGlomConnector.findPersonById(multiGlomPersonTestIds.get(0)).toString());

        assertEquals("XyPerson({id=HC1001, firstName=Jack, lastName=Miller, role=General Manager, addressId=HC1002, "
                + "street=Lame Duck Valley 180, city=Rugby, zipCode=13476})", halCorpConnector.findXyPersonById(halCorpPersonTestIds.get(0)).toString());

        assertEquals("XyPerson({id=MG6667, firstName=Susi, lastName=Miller, role=General Manager, addressId=MG6668, "
                + "street=Lame Duck Valley 180, city=Rugby, zipCode=13476})", multiGlomConnector.findXyPersonById(multiGlomPersonTestIds.get(0)).toString());

        // now let's have a look at another member of the main inheritence hierarchy
        RwPersonDataConnector rwHalCorpConnector = new RwPersonDataConnector(halCorpImp);

        XyPerson xyPerson = rwHalCorpConnector.findXyPersonById(halCorpPersonTestIds.get(0));
        xyPerson.setFirstName("John");
        xyPerson.setZipCode("88906");
        rwHalCorpConnector.createOrUpdateXyPerson(xyPerson);

        assertEquals("XyPerson({id=HC1001, firstName=John, lastName=Miller, role=General Manager, addressId=HC1002, "
                + "street=Lame Duck Valley 180, city=Rugby, zipCode=88906})", halCorpConnector.findXyPersonById(halCorpPersonTestIds.get(0)).toString());

        LOGGER.info("Test Bridge successful! Elapsed time: " + MiscUtils.formatNanosAsSeconds(System.nanoTime() - startTimeNanos) + " s");

    }

}
