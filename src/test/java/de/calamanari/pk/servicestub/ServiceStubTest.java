//@formatter:off
/*
 * Service Stub test - demonstrates SERVICE STUB (aka MOCK) pattern.
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
package de.calamanari.pk.servicestub;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.calamanari.pk.util.TimeUtils;

/**
 * Service Stub test - demonstrates SERVICE STUB (aka MOCK) pattern.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class ServiceStubTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(ServiceStubTest.class);

    @Test
    public void testServiceStub() throws Exception {

        // Hint: set the log-level in logback.xml to DEBUG to watch SERVICE STUB working.

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
        assertEquals("Account({accountId='ID_1', firstName='Jack', lastName='Miller', street='4711, Angry Road', " + "zipCode='827382', city='Strange Town'})",
                account.toString());

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

        String elapsedTimeString = TimeUtils.formatNanosAsSeconds(System.nanoTime() - startTimeNanos);
        LOGGER.info("Test service stub successful! Elapsed time: {} s", elapsedTimeString);
    }

}
