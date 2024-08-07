//@formatter:off
/*
 * Coarse Grained Lock Test - demonstrates COARSE GRAINED LOCK pattern.
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
package de.calamanari.pk.coarsegrainedlock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.calamanari.pk.util.TimeUtils;

/**
 * Coarse Grained Lock Test - demonstrates COARSE GRAINED LOCK pattern.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
@SuppressWarnings("resource")
public class CoarseGrainedLockTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(CoarseGrainedLockTest.class);

    /**
     * allow stress test only in info mode and if number of processors > 1 :-)
     */
    private static final boolean STRESS_TEST = !LOGGER.isDebugEnabled() && (Runtime.getRuntime().availableProcessors() > 1);

    /**
     * for thread coordination
     */
    private final CountDownLatch countDown = new CountDownLatch(5);

    /**
     * mocks the customer database table
     */
    private final Map<String, Customer> customerDb = new ConcurrentHashMap<>();

    /**
     * mocks the address database table
     */
    private final Map<String, Address> addressDb = new ConcurrentHashMap<>();

    /**
     * mocks the order database table
     */
    private final Map<String, Order> orderDb = new ConcurrentHashMap<>();

    private final AtomicReference<String> firstError = new AtomicReference<>();

    @Before
    public void setUp() {
        customerDb.clear();
        addressDb.clear();
        orderDb.clear();
        customerDb.put("4711", new Customer("4711", "Jack", "Miller"));
        addressDb.put("8877", new Address("8877", "4711", "17, Citrus Ave", "286736", "Lemon Village"));
        orderDb.put("9966", new Order("8877", "4711", "XCDBVGHJKLIUZTTRD55FRW"));
        firstError.set(null);
    }

    @Test
    public void testCoarseGrainedLock() throws Exception {

        // we lock always on customer level regardless
        // of which part of the information structure to be displayed
        // (customer, address or order).

        // _______________________________________________________________________
        // _______________________________CUSTOMER__<-------\__L__________________
        // ________________________________/_____\___________|_O__________________
        // _______________________________/_______\__________|_C__________________
        // ___________________________ADDRESS____ORDER______/__K__________________
        // _______________________________________________________________________

        // In some situations this makes sense, but often this kind of locking
        // is too coarse and may slow-down user's work.

        // Adjust the log-level in logback.xml to DEBUG to see the COARSE GRAINED LOCK working

        LOGGER.info("Test Coarse Grained Lock ...");
        long startTimeNanos = System.nanoTime();

        firstUserStartsToWork();
        secondUserStartsToWork();
        thirdUserStartsToWork();
        fourthUserStartsToWork();
        fifthUserStartsToWork();

        countDown.await();

        assertNull(firstError.get());

        assertNull(InMemoryLockManager.getLockInfo("4711"));

        assertEquals("Address({id='8877', customerId='4711', street='19, Lucky Road', zipCode='286736', city='Lemon Village'})",
                addressDb.get("8877").toString());

        assertEquals("Order({id='8877', customerId='4711', orderData='POLKIJUHZGT77653FF'})", orderDb.get("9966").toString());

        LOGGER.info("Test Coarse Grained Lock successful! Elapsed time: {} s", TimeUtils.formatNanosAsSeconds(System.nanoTime() - startTimeNanos));
    }

    @Test
    public void testLockStress() throws Exception {
        if (STRESS_TEST) {
            LOGGER.info("Test Lock Stress ...");
            long startTimeNanos = System.nanoTime();

            final CountDownLatch latch = new CountDownLatch(10000);
            ExecutorService service = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
            final int[] valueHolder = new int[1];
            valueHolder[0] = 0;
            try {
                for (int i = 0; i < 10000; i++) {
                    final String name = "User_" + i;
                    service.execute(new Runnable() {
                        @Override
                        public void run() {
                            String oldName = Thread.currentThread().getName();
                            Thread.currentThread().setName(name);
                            boolean success = false;
                            while (!success) {
                                success = InMemoryLockManager.acquireWriteLock("theElement", name);
                                if (success) {
                                    valueHolder[0] = valueHolder[0] + 1;
                                    InMemoryLockManager.releaseLock("theElement", name);
                                }
                            }
                            Thread.currentThread().setName(oldName);
                            latch.countDown();
                        }
                    });
                }
            }
            finally {
                service.shutdown();
            }
            latch.await();

            assertNull(firstError.get());

            assertEquals(10000, valueHolder[0]);

            LOGGER.info("Test Lock Stress successful! Elapsed time: {} s", TimeUtils.formatNanosAsSeconds(System.nanoTime() - startTimeNanos));

        }
        else {
            if (LOGGER.isDebugEnabled()) {
                LOGGER.info("Skipped lock stress test in debug mode.");
            }
            else {
                LOGGER.warn("Skipped lock stress test in single core mode!");
            }
        }
    }

    /**
     * First user works on the record
     */
    private void firstUserStartsToWork() {
        (new Thread("User_1") {

            @Override
            public void run() {

                boolean lockSuccess = InMemoryLockManager.acquireReadLock("4711", "User_1");

                if (!lockSuccess) {
                    firstError.compareAndSet(null, "firstUserUnsuccessful");
                }

                try {

                    Customer customer = customerDb.get("4711");
                    Address address = addressDb.get("8877"); // address of
                                                             // customer

                    LOGGER.debug("User_1 displays customer: {}", customer);
                    LOGGER.debug("User_1 displays address: {}", address);

                    TimeUtils.sleepIgnoreException(5000);

                    // test reentrance
                    lockSuccess = InMemoryLockManager.acquireReadLock("4711", "User_1");
                    if (!lockSuccess) {
                        firstError.compareAndSet(null, "firstUserUnsuccessful");
                    }

                    // get write lock, we want to change address
                    lockSuccess = InMemoryLockManager.acquireWriteLock("4711", "User_1");

                    if (!lockSuccess) {
                        firstError.compareAndSet(null, "firstUserUnsuccessful");
                    }

                    address = new Address(address.getId(), address.getCustomerId(), "19, Lucky Road", address.getZipCode(), address.getCity());
                    addressDb.put("8877", address);

                    TimeUtils.sleepIgnoreException(2000);
                }
                finally {
                    InMemoryLockManager.releaseLock("4711", "User_1");
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

                boolean lockSuccess = InMemoryLockManager.acquireReadLock("4711", "User_2");

                if (!lockSuccess) {
                    firstError.compareAndSet(null, "secondUserUnsuccessful");
                }

                try {

                    Customer customer = customerDb.get("4711");
                    LOGGER.debug("User_2 displays customer: {}", customer);

                    TimeUtils.sleepIgnoreException(2000);

                }
                finally {
                    InMemoryLockManager.releaseLock("4711", "User_2");
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

                boolean lockFailed = !InMemoryLockManager.acquireWriteLock("4711", "User_3");

                if (!lockFailed) {
                    firstError.compareAndSet(null, "thirdUserDidNotFailAsExpected");
                }

                InMemoryLockManager.ElementLock lock = InMemoryLockManager.getLockInfo("4711");
                LOGGER.debug("User_3 failed to get write lock! - Existing Lock found: {}", lock);

                LOGGER.debug("User_3 phones the two other users and presses [Wait for]-button ... ");
                try {
                    lock.await();
                }
                catch (InterruptedException ex) {
                    Thread.currentThread().interrupt();
                    // should not happen
                    throw new RuntimeException(ex);
                }

                LOGGER.debug("User_3 has been notified and tries again ... ");

                try {
                    boolean lockSuccess = InMemoryLockManager.acquireWriteLock("4711", "User_3");
                    if (!lockSuccess) {
                        firstError.compareAndSet(null, "thirdUserUnsuccessful");
                    }
                    Order order = orderDb.get("9966");
                    order = new Order(order.getId(), order.getCustomerId(), "POLKIJUHZGT77653FF");
                    orderDb.put("9966", order);
                }
                finally {
                    InMemoryLockManager.releaseLock("4711", "User_3");
                }

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

                boolean lockFailed = !InMemoryLockManager.acquireReadLock("4711", "User_4");

                if (!lockFailed) {
                    firstError.compareAndSet(null, "fourthUserDidNotFailAsExpected");
                }

                LOGGER.debug("User_4 failed to get read lock! - Existing Lock found: {}", InMemoryLockManager.getLockInfo("4711"));

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

                boolean lockSuccess = InMemoryLockManager.acquireReadLock("4711", "User_5");

                if (!lockSuccess) {
                    firstError.compareAndSet(null, "fifthUserUnsuccessful");
                }

                try {

                    Order order = orderDb.get("9966");
                    LOGGER.debug("User_5 displays order: {}", order);

                }
                finally {
                    InMemoryLockManager.releaseLock("4711", "User_5");
                }
                countDown.countDown();
            }

        }).start();
    }

}
