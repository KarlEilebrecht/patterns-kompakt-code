//@formatter:off
/*
 * Optimistic Offline Lock Test - demonstrates OPTIMISTIC OFFLINE LOCK pattern.
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
package de.calamanari.pk.optimisticofflinelock;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.sql.Connection;
import java.sql.Statement;
import java.util.ConcurrentModificationException;
import java.util.concurrent.CountDownLatch;

import javax.sql.DataSource;

import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.bridge.SLF4JBridgeHandler;

import de.calamanari.pk.util.MiscUtils;
import de.calamanari.pk.util.db.EmbeddedJavaDbDataSource;

/**
 * Optimistic Offline Lock Test - demonstrates OPTIMISTIC OFFLINE LOCK pattern.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class OptimisticOfflineLockTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(OptimisticOfflineLockTest.class);

    /**
     * for thread coordination
     */
    private final CountDownLatch countDown = new CountDownLatch(2);

    /**
     * In this example the database access manager, could be some DAO
     */
    private static volatile DataManager dataManager;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
        LOGGER.info("Database setup ... ");
        DataSource dataSource = EmbeddedJavaDbDataSource.getInstance();
        try (Connection con = dataSource.getConnection(); Statement stmt = con.createStatement()) {
            // There is no "IF EXISTS", thus brute-force drop.
            // However, the cleaner way would be using DatabaseMetaData to check for existence in advance.
            stmt.executeUpdate("drop table CUSTOMER");
        }
        catch (Exception ex) {
            // ignore (first time the table is not there
        }

        try (Connection con = dataSource.getConnection(); Statement stmt = con.createStatement()) {
            stmt.executeUpdate("create table CUSTOMER (CUSTOMER_ID int PRIMARY KEY, FIRST_NAME varchar(20), "
                    + "LAST_NAME varchar(20), STREET varchar(100), ZIPCODE varchar(20), " + "CITY varchar(50), VERSION int)");
        }
        LOGGER.info("Database setup completed. ");

        dataManager = new DataManager(dataSource);
        dataManager.addCustomer(4711, "Jack", "Miller", "17, Citrus Ave", "286736", "Lemon Village");
    }

    @Test
    public void testOptimisticOfflineLock() throws Exception {

        // Hints: - Adjust the log-level in logback.xml to DEBUG to see the OPTIMISTIC OFFLINE LOCK working
        //
        // - You can easily replace the data source above with a real one

        LOGGER.info("Test Optimistic Offline Lock ...");
        long startTimeNanos = System.nanoTime();

        firstUserStartsToWork();
        secondUserStartsToWork();

        countDown.await();

        assertEquals("Customer({customerId='4711', lastName='Miller', firstName='Jane', street='19, Lucky Road', "
                + "zipCode='286736', city='Lemon Village', version=2})", dataManager.findCustomerById(4711).toString());

        String elapsedTimeString = MiscUtils.formatNanosAsSeconds(System.nanoTime() - startTimeNanos);
        LOGGER.info("Test Optimistic Offline Lock successful! Elapsed time: {} s", elapsedTimeString);
    }

    /**
     * First user works on the record
     */
    private void firstUserStartsToWork() {
        (new Thread("User_1") {

            @Override
            public void run() {
                Customer customer = dataManager.findCustomerById(4711);
                customer.setStreet("19, Lucky Road");
                MiscUtils.sleepIgnoreException(5000);
                Exception caughtEx = null;
                try {
                    dataManager.storeCustomer(customer);
                }
                catch (Exception ex) {
                    caughtEx = ex;
                }
                assertTrue(caughtEx instanceof ConcurrentModificationException);

                // oops, user 1 was too optimistic, let's try again
                customer = dataManager.findCustomerById(4711);
                customer.setStreet("19, Lucky Road");
                dataManager.storeCustomer(customer);
                countDown.countDown();
            }

        }).start();
    }

    /**
     * First user works concurrently on the record
     */
    private void secondUserStartsToWork() {
        (new Thread("User_2") {
            @Override
            public void run() {
                MiscUtils.sleepIgnoreException(2000);
                Customer customer = dataManager.findCustomerById(4711);
                customer.setFirstName("Jane");
                dataManager.storeCustomer(customer);
                countDown.countDown();
            }

        }).start();
    }

}
