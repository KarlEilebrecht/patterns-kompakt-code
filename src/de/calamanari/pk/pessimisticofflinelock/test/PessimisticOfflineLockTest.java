/*
 * Pessimistic Offline Lock Test - demonstrates PESSIMISTIC OFFLINE LOCK pattern.
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
package de.calamanari.pk.pessimisticofflinelock.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.calamanari.pk.pessimisticofflinelock.Customer;
import de.calamanari.pk.pessimisticofflinelock.LockManager;
import de.calamanari.pk.util.LogUtils;
import de.calamanari.pk.util.MiscUtils;

/**
 * Pessimistic Offline Lock Test - demonstrates PESSIMISTIC OFFLINE LOCK pattern.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class PessimisticOfflineLockTest {

    /**
     * logger
     */
    protected static final Logger LOGGER = Logger.getLogger(PessimisticOfflineLockTest.class.getName());

    /**
     * Log-level for this test
     */
    private static final Level LOG_LEVEL = Level.INFO;

    /**
     * for thread coordination
     */
    private final CountDownLatch countDown = new CountDownLatch(5);

    /**
     * mocks the customer database
     */
    private final Map<String, Customer> customerDb = new ConcurrentHashMap<>();

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        LogUtils.setConsoleHandlerLogLevel(LOG_LEVEL);
        LogUtils.setLogLevel(LOG_LEVEL, PessimisticOfflineLockTest.class, LockManager.class);
    }

    @Before
    public void setUp() throws Exception {
        customerDb.clear();
        customerDb.put("4711", new Customer("4711", "Jack", "Miller", "17, Citrus Ave", "286736", "Lemon Village"));
    }

    @Test
    public void testPessimisticOfflineLock() throws Exception {

        // Hints: - Adjust the log-level above to FINE to see the PESSIMISTIC OFFLINE LOCK working
        //
        // - Re-implement the LockManager with real database access (see EmbeddedJavaDbDataSource.getInstance())

        LOGGER.info("Test Pessimistic Offline Lock ...");
        long startTimeNanos = System.nanoTime();

        firstUserStartsToWork();
        secondUserStartsToWork();
        thirdUserStartsToWork();
        fourthUserStartsToWork();
        fifthUserStartsToWork();

        countDown.await();

        assertEquals("LockInfo({elementId='4711', lockType='NONE', ownerIds=[]})", LockManager.getLockInfo("4711")
                .toString());

        assertEquals("Customer({customerId='4711', lastName='Miller', firstName='Jack', street='19, Lucky Road', "
                + "zipCode='286736', city='Lemon Village'})", customerDb.get("4711").toString());

        LOGGER.info("Test Pessimistic Offline Lock successful! Elapsed time: "
                + MiscUtils.formatNanosAsSeconds(System.nanoTime() - startTimeNanos) + " s");
    }

    /**
     * First user works on the record
     */
    private void firstUserStartsToWork() {
        (new Thread("User_1") {

            @Override
            public void run() {

                boolean lockSuccess = LockManager.acquireReadLock("4711", "User_1");

                assertTrue(lockSuccess);

                try {

                    Customer customer = customerDb.get("4711");

                    LOGGER.fine("User_1 displays: " + customer.toString());

                    MiscUtils.sleepIgnoreException(5000);

                    // test reentrance
                    lockSuccess = LockManager.acquireReadLock("4711", "User_1");
                    assertTrue(lockSuccess);

                    // get write lock
                    lockSuccess = LockManager.acquireWriteLock("4711", "User_1");
                    assertTrue(lockSuccess);

                    customer = new Customer(customer.getCustomerId(), customer.getFirstName(), customer.getLastName(),
                            "19, Lucky Road", customer.getZipCode(), customer.getCity());
                    customerDb.put("4711", customer);

                    MiscUtils.sleepIgnoreException(2000);
                }
                finally {
                    LockManager.releaseLock("4711", "User_1");
                }
                countDown.countDown();
            }

        }).start();
    }

    /**
     * Second user works on the record
     */
    private void secondUserStartsToWork() {
        (new Thread("User_2") {

            @Override
            public void run() {
                MiscUtils.sleepIgnoreException(2000);

                boolean lockSuccess = LockManager.acquireReadLock("4711", "User_2");

                assertTrue(lockSuccess);

                try {

                    Customer customer = customerDb.get("4711");
                    LOGGER.fine("User_2 displays: " + customer.toString());

                    MiscUtils.sleepIgnoreException(2000);

                }
                finally {
                    LockManager.releaseLock("4711", "User_2");
                }
                countDown.countDown();
            }

        }).start();
    }

    /**
     * Third user works on the record
     */
    private void thirdUserStartsToWork() {
        (new Thread("User_3") {

            @Override
            public void run() {

                MiscUtils.sleepIgnoreException(3000);

                boolean lockFailed = !LockManager.acquireWriteLock("4711", "User_3");

                assertTrue(lockFailed);

                LOGGER.fine("User_3 failed to get write lock! - Existing Lock found: "
                        + LockManager.getLockInfo("4711"));

                countDown.countDown();
            }

        }).start();
    }

    /**
     * Fourth user works on the record
     */
    private void fourthUserStartsToWork() {
        (new Thread("User_4") {

            @Override
            public void run() {

                MiscUtils.sleepIgnoreException(6000);

                boolean lockFailed = !LockManager.acquireReadLock("4711", "User_4");

                assertTrue(lockFailed);

                LOGGER.fine("User_4 failed to get read lock! - Existing Lock found: " + LockManager.getLockInfo("4711"));

                countDown.countDown();
            }

        }).start();
    }

    /**
     * Fifth user works on the record
     */
    private void fifthUserStartsToWork() {
        (new Thread("User_5") {

            @Override
            public void run() {

                MiscUtils.sleepIgnoreException(8000);

                boolean lockSuccess = LockManager.acquireReadLock("4711", "User_5");

                assertTrue(lockSuccess);

                try {

                    Customer customer = customerDb.get("4711");
                    LOGGER.fine("User_5 displays: " + customer.toString());

                }
                finally {
                    LockManager.releaseLock("4711", "User_5");
                }
                countDown.countDown();
            }

        }).start();
    }

}
