/*
 * Lazy Load Test - demonstrates LAZY LOAD  pattern.
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
package de.calamanari.pk.lazyload.test;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.BeforeClass;
import org.junit.Test;

import de.calamanari.pk.lazyload.Invoice;
import de.calamanari.pk.lazyload.PersistenceSession;
import de.calamanari.pk.util.LogUtils;
import de.calamanari.pk.util.MiscUtils;

/**
 * Lazy Load Test - demonstrates LAZY LOAD pattern.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)web.de">Karl Eilebrecht</a>
 */
public class LazyLoadTest {

    /**
     * logger
     */
    protected static final Logger LOGGER = Logger.getLogger(LazyLoadTest.class.getName());

    /**
     * Log-level for this test
     */
    private static final Level LOG_LEVEL = Level.INFO;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        LogUtils.setConsoleHandlerLogLevel(LOG_LEVEL);
        LogUtils.setLogLevel(LOG_LEVEL, LazyLoadTest.class, Invoice.class, PersistenceSession.class);

        PersistenceSession.addInvoice("INV-001", "154.23", "Charly Brown", "Dogstreet 12", "64354", "Pumpkin Lake");
        PersistenceSession.addInvoice("INV-002", "623.98", "Sandy Bridge", "Paganini Pines 1", "44323", "Lost Hills");
        PersistenceSession.addInvoice("INV-003", "788.11", "Song Lan Yi", "Subway 7", "72632", "Shocking");
        PersistenceSession.addInvoice("INV-004", "1245.4", "Dr. Ren Stimpy", "Ruby Road 18", "32156", "Bundy Bay");
        PersistenceSession.addInvoice("INV-005", "998.51", "Chuck Nickle", "Oneway 66", "92834", "Seven Oaks");
        PersistenceSession.addInvoice("INV-006", "542.12", "Chilly Beats", "Kelly Street 8", "64354", "Pumpkin Lake");
        PersistenceSession.addInvoice("INV-007", "900.13", "Kalimba Luna", "Wonderway 713", "44323", "Lost Hills");
        PersistenceSession.addInvoice("INV-008", "781.55", "Ken Yashimoda", "Old Mill 33", "32156", "Bundy Bay");
        PersistenceSession.addInvoice("INV-009", "332.64", "Ronald Doe", "Road to hell 87", "72632", "Shocking");
        PersistenceSession.addInvoice("INV-010", "9875.7", "Micky van Splatter", "Helloween Road 23", "64354",
                "Pumpkin Lake");

    }

    @Test
    public void testLazyLoad() {
        // Adjust the log-level above to FINE to see LAZY LOAD working

        LOGGER.info("Test Lazy Load ...");
        long startTimeNanos = System.nanoTime();

        PersistenceSession persistenceSession = new PersistenceSession();

        Invoice invoice = persistenceSession.findInvoice("INV-001");

        assertEquals("Invoice({invoiceId=INV-001, amountClaimed=154.23, debtorName=null, street=null, zipCode=null, "
                + "city=null, dataComplete=false, detached=false})", invoice.toString());

        assertEquals("Charly Brown", invoice.getDebtorName());

        assertEquals("Invoice({invoiceId=INV-001, amountClaimed=154.23, debtorName=Charly Brown, street=Dogstreet 12, "
                + "zipCode=64354, city=Pumpkin Lake, dataComplete=true, detached=false})", invoice.toString());

        LOGGER.info("Test Lazy Load successful! Elapsed time: "
                + MiscUtils.formatNanosAsSeconds(System.nanoTime() - startTimeNanos) + " s");

    }

    @Test
    public void testLazyLoadShowRippleEffect() {

        LOGGER.info("Test Lazy Load Show Ripple Effect ...");
        long startTimeNanos = System.nanoTime();

        PersistenceSession persistenceSession = new PersistenceSession();

        Set<String> debtorCities = new HashSet<>();

        List<Invoice> allInvoices = persistenceSession.findAllInvoices();

        for (Invoice invoice : allInvoices) {
            debtorCities.add(invoice.getCity());
        }

        assertEquals("[Pumpkin Lake, Lost Hills, Shocking, Seven Oaks, Bundy Bay]", debtorCities.toString());

        // Hint: change the finder-call to
        // PersistenceManager.findAllInvoices(false);
        // and compare runtimes!

        LOGGER.info("Test Lazy Load Show Ripple Effect successful! Elapsed time: "
                + MiscUtils.formatNanosAsSeconds(System.nanoTime() - startTimeNanos) + " s");

    }

    @Test
    public void testLazyLoadShowClosedSessionEffect() throws Exception {

        LOGGER.info("Test Lazy Load Show Closed Session Effect ...");
        long startTimeNanos = System.nanoTime();

        PersistenceSession persistenceSession = new PersistenceSession();

        Invoice invoice = persistenceSession.findInvoice("INV-001");

        assertEquals("Invoice({invoiceId=INV-001, amountClaimed=154.23, debtorName=null, street=null, zipCode=null, "
                + "city=null, dataComplete=false, detached=false})", invoice.toString());

        // now we have a local reference to the (incompletely loaded) invoice

        // cross session boundary - this sometimes happens accidently, i.e. passing the reference to the web layer!
        crossSessionBoundary(persistenceSession);

        // we have still the same reference to the invoice

        Exception caughtEx = null;
        try {
            invoice.getDebtorName();
        }
        catch (Exception ex) {
            caughtEx = ex;
        }
        assertEquals("Session closed!", (caughtEx == null ? "" : caughtEx.getMessage()));

        LOGGER.info("Test Lazy Load Show Closed Session Effect successful! Elapsed time: "
                + MiscUtils.formatNanosAsSeconds(System.nanoTime() - startTimeNanos) + " s");

    }

    @Test
    public void testLazyLoadShowLostSessionEffect() throws Exception {

        LOGGER.info("Test Lazy Load Show Lost Session Effect ...");
        long startTimeNanos = System.nanoTime();

        PersistenceSession persistenceSession = new PersistenceSession();

        Invoice invoice = persistenceSession.findInvoice("INV-001");

        assertEquals("Invoice({invoiceId=INV-001, amountClaimed=154.23, debtorName=null, street=null, zipCode=null, "
                + "city=null, dataComplete=false, detached=false})", invoice.toString());

        // simulate passing the invoice around, somewhere "by-value" (instance gets serialized and deserialized,
        // sometimes unnoticed)
        invoice = MiscUtils.passByValue(invoice);

        Exception caughtEx = null;
        try {
            invoice.getDebtorName();
        }
        catch (Exception ex) {
            caughtEx = ex;
        }
        assertEquals("Cannot load data, entity detached!", (caughtEx == null ? "" : caughtEx.getMessage()));

        LOGGER.info("Test Lazy Load Show Lost Session Effect successful! Elapsed time: "
                + MiscUtils.formatNanosAsSeconds(System.nanoTime() - startTimeNanos) + " s");

    }

    /**
     * Simulates crossing the session boundary
     * @param persistenceSession
     */
    private static void crossSessionBoundary(PersistenceSession persistenceSession) {
        persistenceSession.close();
    }

}