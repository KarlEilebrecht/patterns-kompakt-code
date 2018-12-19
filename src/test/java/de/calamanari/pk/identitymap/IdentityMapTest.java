//@formatter:off
/*
 * Identity Map Test - demonstrates IDENTITY MAP pattern.
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
package de.calamanari.pk.identitymap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.calamanari.pk.util.MiscUtils;

/**
 * Identity Map Test - demonstrates IDENTITY MAP pattern.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class IdentityMapTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(IdentityMapTest.class);

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        Database.addTestData("ID0001", "Mr.", "McFlurry", "Dick", "0815-987", "Dick.Flurry@neversend.com", true, "ADR00091", "Quark-Street 70", "91827",
                "Gotham City", "USA", "Dear Mr.");
        Database.addTestData("ID0002", "Mrs.", "Clark", "Petula", "0817-871", "Petula.Clark@neversend.com", false, "ADR01071", "Black Owl Way 34", "1217",
                "Pork Town", "USA", "Dear Mrs.");
        Database.addTestData("ID0003", "Mrs.", "Blum", "Shanya", "089 8172 2123", "Shanya.Blum@neversend.com", true, "ADR00912", "Hauptstrasse 11", "71622",
                "Blasenhain", "GER", "Sehr gehrte Frau");
        Database.addTestData("ID0004", "Mr.", "De-Vil", "Sa Tan", "666 666 666", "souleater@neversend.com", false, "ADR00666", "Road to Hell 666", "666666",
                "Blackhole", "USA", "Your Majesty");
    }

    @Test
    public void testIdentityMap() {

        // Hint: adjust the log-level in logback.xml to DEBUG to see IDENTITY MAP working

        LOGGER.info("Test identity map ...");
        long startTimeNanos = System.nanoTime();

        CustomerEntity customer1 = DataManager.findCustomerById("ID0001");
        assertEquals("CustomerEntity({id=ID0001, title=Mr., lastName=McFlurry, firstName=Dick, phone=0815-987, "
                + "email=Dick.Flurry@neversend.com, promotionOptIn=true})", customer1.toString());

        // the following assertion ensures that we do not shoot ourselves in the foot
        // which can easily happen when mocking data management due to the fact
        // that an overlooked "call by reference" can jeopardize the whole test.
        assertNotSame(Database.CUSTOMERS.get("ID0001"), customer1);

        // now the essential tests

        AddressEntity address1 = DataManager.findAddressById("ADR00091");
        assertEquals("AddressEntity({id=ADR00091, customerId=ID0001, street=Quark-Street 70, zipCode=91827, "
                + "city=Gotham City, country=USA, salutation=Dear Mr.})", address1.toString());

        CustomerEntity customer2 = DataManager.findCustomerById(address1.getCustomerId());

        // the identity map ensures we won't get a duplicate but the same instance again
        assertSame(customer1, customer2);

        String elapsedTimeString = MiscUtils.formatNanosAsSeconds(System.nanoTime() - startTimeNanos);
        LOGGER.info("Test identity map successful! Elapsed time: {} s", elapsedTimeString);

    }

}
