//@formatter:off
/*
 * Data Transfer Object test - demonstrates DATA TRANSFER OBJECT pattern
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
package de.calamanari.pk.datatransferobject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.calamanari.pk.datatransferobject.server.CustomerManagerServer;
import de.calamanari.pk.util.ExternalProcessManager;
import de.calamanari.pk.util.MiscUtils;

/**
 * Data Transfer Object test - demonstrates DATA TRANSFER OBJECT pattern
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class DataTransferObjectTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataTransferObjectTest.class);

    /**
     * number of runs to show the runtime difference
     */
    private static final int NUMBER_OF_RUNS = 100;

    /**
     * The port out private RMI-registry shall use.
     */
    private static final int REGISTRY_PORT = CustomerManagerServer.DEFAULT_REGISTRY_PORT;

    /**
     * customer manager for the test
     */
    private CustomerManager customerManager;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        ExternalProcessManager.getInstance().startExternal(CustomerManagerServer.class, LOGGER, "" + REGISTRY_PORT);

        // to be sure the server is up, wait 8 seconds
        Thread.sleep(8000);

    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        // stop the product manager server
        ExternalProcessManager.getInstance().stopExternal(CustomerManagerServer.class, "q", 5000);
    }

    @Before
    public void setUp() throws Exception {
        Registry registry = LocateRegistry.getRegistry(REGISTRY_PORT);
        customerManager = (CustomerManager) registry.lookup("CustomerManager");
        customerManager.addCustomer("ID0001", "Boy", "John", "Ponderosa Ave", "9183722", "Nirvana Lake");
    }

    @Test
    public void testWithoutDataTransferObject() throws Exception {

        // HINTS:
        // * Adjust the log-level above to FINE to see the DATA TRANSFER OBJECT working
        // * Compare the runtime values of the two tests, the difference results from remoting overhead

        LOGGER.info("Test without data transfer object  ...");
        long startTimeNanos = System.nanoTime();

        Customer customer = customerManager.findCustomer("ID0001");

        assertTrue(customer instanceof CustomerRemote);

        testInternal(customer);

        long elapsed = (System.nanoTime() - startTimeNanos);

        Thread.sleep(1000);

        LOGGER.info("Test without data transfer object successful! Elapsed time: " + MiscUtils.formatNanosAsSeconds(elapsed) + " s");

    }

    @Test
    public void testWithDataTransferObject() throws Exception {

        LOGGER.info("Test with data transfer object  ...");
        long startTimeNanos = System.nanoTime();

        Customer customer = customerManager.findCustomerReturnDto("ID0001");

        assertTrue(customer instanceof CustomerDto);

        testInternal(customer);

        long elapsed = (System.nanoTime() - startTimeNanos);

        Thread.sleep(1000);

        LOGGER.info("Test with data transfer object successful! Elapsed time: " + MiscUtils.formatNanosAsSeconds(elapsed) + " s");

    }

    /**
     * Do some reads on the customer (could be a DTO or not)
     * 
     * @param customer
     * @throws Exception
     */
    private void testInternal(Customer customer) throws Exception {
        assertEquals("ID0001", customer.getCustomerId());
        assertEquals("Boy", customer.getLastName());
        assertEquals("John", customer.getFirstName());
        assertEquals("Ponderosa Ave", customer.getStreet());
        assertEquals("9183722", customer.getZipCode());
        assertEquals("Nirvana Lake", customer.getCity());

        // now do some reads
        for (int i = 0; i < NUMBER_OF_RUNS; i++) {
            customer.getCustomerId();
            customer.getLastName();
            customer.getFirstName();
            customer.getStreet();
            customer.getZipCode();
            customer.getCity();
        }
    }

}
