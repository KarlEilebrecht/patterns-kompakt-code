/*
 * Service Stub test - demonstrates SERVICE STUB (aka MOCK) pattern.
 * Code-Beispiel zum Buch Patterns Kompakt, Verlag Springer Vieweg
 * Copyright 2013 Karl Eilebrecht
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
package de.calamanari.pk.servicestub.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.BeforeClass;
import org.junit.Test;

import de.calamanari.pk.servicestub.Account;
import de.calamanari.pk.servicestub.AccountManager;
import de.calamanari.pk.servicestub.AccountValidationException;
import de.calamanari.pk.util.LogUtils;
import de.calamanari.pk.util.MiscUtils;

/**
 * Service Stub test - demonstrates SERVICE STUB (aka MOCK) pattern.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)web.de">Karl Eilebrecht</a>
 */
public class ServiceStubTest {

    /**
     * logger
     */
    private static final Logger LOGGER = Logger.getLogger(ServiceStubTest.class.getName());

    /**
     * Log-level for this test
     */
    private static final Level LOG_LEVEL = Level.INFO;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        LogUtils.setConsoleHandlerLogLevel(LOG_LEVEL);
        LogUtils.setLogLevel(LOG_LEVEL, ServiceStubTest.class, Account.class, AccountManager.class);
    }

    @Test
    public void testServiceStub() throws Exception {

        // Hint: set the log-level above to FINE to watch SERVICE STUB working.

        LOGGER.info("Test service stub ...");
        long startTimeNanos = System.nanoTime();

        // Scenario: We're going to test our famous new AccountManager class
        // unfortunately to create an AccountManager, we need a valid reference to
        // the address validation service, which we don't have ...

        AccountManager accountManager = new AccountManager(null);
        Account account = null;

        Exception caughtEx = null;
        try {
            account = accountManager.createAccount("Jack", "Miller", "4711, Angry Road", "827382", "Strange Town");
        }
        catch (Exception ex) {
            caughtEx = ex;
        }
        assertTrue(caughtEx instanceof NullPointerException);

        // Problem: We cannot test AccountManager, because address validation service is
        // not available for testing.

        // Solution: Use a SERVICE STUB (aka Mock) to simulate the missing service.
        // There is no need to provide a fully functional address validation service
        // because we want to test our AccountManager rather than the validation service.
        // Thus out mock implementation can be very simple.

        AddressValidatorServiceMock serviceStub = new AddressValidatorServiceMock(true);

        accountManager = new AccountManager(serviceStub);

        // positive test
        account = accountManager.createAccount("Jack", "Miller", "4711, Angry Road", "827382", "Strange Town");
        assertEquals("Account({accountId='ID_1', firstName='Jack', lastName='Miller', street='4711, Angry Road', "
                + "zipCode='827382', city='Strange Town'})", account.toString());

        // negative test
        serviceStub.setValidationResult(false);
        caughtEx = null;
        try {
            account = accountManager.createAccount("Jack", "Miller", "4711, Angry Road", "827382", "Strange Town");
        }
        catch (Exception ex) {
            caughtEx = ex;
        }
        assertTrue(caughtEx instanceof AccountValidationException);
        assertEquals("Could not create account, invalid address.", caughtEx.getMessage());

        LOGGER.info("Test service stub successful! Elapsed time: "
                + MiscUtils.formatNanosAsSeconds(System.nanoTime() - startTimeNanos) + " s");
    }

}
