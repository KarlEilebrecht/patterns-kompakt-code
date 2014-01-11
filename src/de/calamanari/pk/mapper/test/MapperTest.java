/*
 * Mapper Test - demonstrates MAPPER pattern.
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
package de.calamanari.pk.mapper.test;

import static org.junit.Assert.assertEquals;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.calamanari.pk.mapper.AbstractMapper;
import de.calamanari.pk.mapper.CustomerMapper;
import de.calamanari.pk.mapper.DataManager;
import de.calamanari.pk.mapper.Session;
import de.calamanari.pk.mapper.firstsys.Address;
import de.calamanari.pk.mapper.firstsys.Person;
import de.calamanari.pk.mapper.secondsys.Customer;
import de.calamanari.pk.util.LogUtils;
import de.calamanari.pk.util.MiscUtils;

/**
 * Mapper Test - demonstrates MAPPER pattern.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class MapperTest {

    /**
     * logger
     */
    protected static final Logger LOGGER = Logger.getLogger(MapperTest.class.getName());

    /**
     * Log-level for this test
     */
    private static final Level LOG_LEVEL = Level.INFO;

    /**
     * supplementary manager (simulates persistence)
     */
    private DataManager dataManager = null;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        LogUtils.setConsoleHandlerLogLevel(LOG_LEVEL);
        LogUtils.setLogLevel(LOG_LEVEL, MapperTest.class, CustomerMapper.class, AbstractMapper.class,
                DataManager.class, Session.class, Address.class, Person.class, Customer.class);
    }

    @Before
    public void setUp() throws Exception {
        dataManager = new DataManager();
        Person person1 = new Person("1", "Rudy", "Rubbish", "2001-08-17");
        Address address1 = new Address("1", "Laurel-Street 16", "71627", "Sucksburg");
        dataManager.addPerson(person1, address1);
        Person person2 = new Person("2", "Ren", "Stimpy", "2011-10-06");
        Address address2 = new Address("2", "Rattlesnake Road 2", "11333", "Vanisham");
        dataManager.addPerson(person2, address2);
    }

    @Test
    public void testMapper() {

        // Adjust the log-level above to FINE to see the MAPPER working

        LOGGER.info("Test Mapper  ...");
        long startTimeNanos = System.nanoTime();

        Session session = new Session();
        Customer customer1 = dataManager.findCustomer(session, "1");
        Customer customer2 = dataManager.findCustomer(session, "2");

        assertEquals("1", customer1.getCustomerId());
        assertEquals("2", customer2.getCustomerId());

        String customerString1 = customer1.toString();

        customer1.setFirstName("Hugo");

        session.discard();

        session = new Session();
        customer1 = dataManager.findCustomer(session, "1");
        // nothing changed
        assertEquals(customerString1, customer1.toString());

        customer1.setFirstName("John");
        customerString1 = customerString1.replace("Rudy", "John");
        customer1.setStreet("Laurel-Street 17");
        customerString1 = customerString1.replace("Laurel-Street 16", "Laurel-Street 17");

        session.confirm();

        session = new Session();
        customer1 = dataManager.findCustomer(session, "1");
        assertEquals(customerString1, customer1.toString());

        LOGGER.info("Test Mapper successful! Elapsed time: "
                + MiscUtils.formatNanosAsSeconds(System.nanoTime() - startTimeNanos) + " s");

    }

}
