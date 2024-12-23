//@formatter:off
/*
 * Pessimistic Offline Lock Test - demonstrates PESSIMISTIC OFFLINE LOCK pattern.
 * Code-Beispiel zum Buch Patterns Kompakt, Verlag Springer Vieweg
 * Copyright 2014 Karl Eilebrecht
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"):
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
package de.calamanari.pk.pessimisticofflinelock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.calamanari.pk.util.TimeUtils;

/**
 * Pessimistic Offline Lock Test - demonstrates PESSIMISTIC OFFLINE LOCK pattern.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class PessimisticOfflineLockTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(PessimisticOfflineLockTest.class);

    /**
     * for thread coordination
     */
    private final CountDownLatch countDown = new CountDownLatch(5);

    /**
     * mocks the customer database
     */
    private final Map<String, Customer> customerDb = new ConcurrentHashMap<>();

    private final AtomicReference<String> firstError = new AtomicReference<>();

    @Before
    public void setUp() {
        customerDb.clear();
        customerDb.put("4711", new Customer("4711", "Jack", "Miller", "17, Citrus Ave", "286736", "Lemon Village"));
        firstError.set(null);
    }

    @Test
    public void testPessimisticOfflineLock() throws Exception {

        // Hints: - Adjust the log-level in lockback.xml to DEBUG to see the PESSIMISTIC OFFLINE LOCK working
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

        assertNull(firstError.get());

        assertEquals("LockInfo({elementId='4711', lockType='NONE', ownerIds=[]})", LockManager.getLockInfo("4711").toString());

        assertEquals("Customer({customerId='4711', lastName='Miller', firstName='Jack', street='19, Lucky Road', " + "zipCode='286736', city='Lemon Village'})",
                customerDb.get("4711").toString());

        String elapsedTimeString = TimeUtils.formatNanosAsSeconds(System.nanoTime() - startTimeNanos);
        LOGGER.info("Test Pessimistic Offline Lock successful! Elapsed time: {} s", elapsedTimeString);
    }

    /**
     * First user works on the record
     */
    private void firstUserStartsToWork() {
        (new Thread("User_1") {

            @Override
            public void run() {

                boolean lockSuccess = LockManager.acquireReadLock("4711", "User_1");

                if (!lockSuccess) {
                    firstError.compareAndSet(null, "User_1 failed to acquireLock (1)");
                }

                try {

                    Customer customer = customerDb.get("4711");

                    LOGGER.debug("User_1 displays: {}", customer);

                    TimeUtils.sleepIgnoreException(5000);

                    // test reentrance
                    lockSuccess = LockManager.acquireReadLock("4711", "User_1");
                    if (!lockSuccess) {
                        firstError.compareAndSet(null, "User_1 failed to acquireLock (2)");
                    }

                    // get write lock
                    lockSuccess = LockManager.acquireWriteLock("4711", "User_1");
                    if (!lockSuccess) {
                        firstError.compareAndSet(null, "User_1 failed to acquireLock (3)");
                    }

                    customer = new Customer(customer.getCustomerId(), customer.getFirstName(), customer.getLastName(), "19, Lucky Road", customer.getZipCode(),
                            customer.getCity());
                    customerDb.put("4711", customer);

                    TimeUtils.sleepIgnoreException(2000);
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
                TimeUtils.sleepIgnoreException(2000);

                boolean lockSuccess = LockManager.acquireReadLock("4711", "User_2");

                if (!lockSuccess) {
                    firstError.compareAndSet(null, "User_2 failed to acquireLock");
                }

                try {

                    Customer customer = customerDb.get("4711");
                    LOGGER.debug("User_2 displays: {}", customer);

                    TimeUtils.sleepIgnoreException(2000);

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

                TimeUtils.sleepIgnoreException(3000);

                boolean lockFailed = !LockManager.acquireWriteLock("4711", "User_3");

                if (!lockFailed) {
                    firstError.compareAndSet(null, "User_3 unexpectedly succeeded to acquireLock");
                }

                LOGGER.debug("User_3 failed to get write lock! - Existing Lock found: {}", LockManager.getLockInfo("4711"));

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

                TimeUtils.sleepIgnoreException(6000);

                boolean lockFailed = !LockManager.acquireReadLock("4711", "User_4");

                if (!lockFailed) {
                    firstError.compareAndSet(null, "User_4 unexpectedly succeeded to acquireLock");
                }

                LOGGER.debug("User_4 failed to get read lock! - Existing Lock found: {}", LockManager.getLockInfo("4711"));

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

                TimeUtils.sleepIgnoreException(8000);

                boolean lockSuccess = LockManager.acquireReadLock("4711", "User_5");

                if (!lockSuccess) {
                    firstError.compareAndSet(null, "User_5 failed to acquireLock");
                }

                try {

                    Customer customer = customerDb.get("4711");
                    LOGGER.debug("User_5 displays: {}", customer);

                }
                finally {
                    LockManager.releaseLock("4711", "User_5");
                }
                countDown.countDown();
            }

        }).start();
    }

}
