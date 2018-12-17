//@formatter:off
/*
 * Persistence Session - supplementary class for LAZY LOAD demonstration
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
package de.calamanari.pk.lazyload;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.calamanari.pk.util.MiscUtils;

/**
 * Persistence Session - supplementary class for LAZY LOAD demonstration<br>
 * Placeholder for some kind of persistence management.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class PersistenceSession {

    private static final Logger LOGGER = LoggerFactory.getLogger(PersistenceSession.class);

    /**
     * our "database"
     */
    private static Map<String, String[]> database = new HashMap<>();

    /**
     * simulated time for accessing database
     */
    private static final long SIMULATED_DATABASE_DELAY_MILLIS = 100;

    /**
     * flag to indicate closed session
     */
    private boolean closed = false;

    /**
     * Method to fill-in test data
     * 
     * @param invoiceId identifier
     * @param amountClaimed monetary value to be payed by the debtor
     * @param debtorName name of the person who has to pay
     * @param street address field
     * @param zipCode address field
     * @param city address field
     */
    public static void addInvoice(String invoiceId, String amountClaimed, String debtorName, String street, String zipCode, String city) {
        database.put(invoiceId, new String[] { invoiceId, amountClaimed, debtorName, street, zipCode, city });
    }

    /**
     * finds the invoice in the database by id
     * 
     * @param invoiceId identifier
     * @return invoice or null if not found
     */
    public Invoice findInvoice(String invoiceId) {
        LOGGER.debug("{}.findInvoice('{}') called", this.getClass().getSimpleName(), invoiceId);
        ensureValidSession();
        simulateDatabaseDelay();
        Invoice res = null;
        String[] invoiceData = database.get(invoiceId);
        if (invoiceData != null) {
            LOGGER.debug("Creating lazy-load invoice instance ...");
            res = new Invoice(this, invoiceData[0], invoiceData[1]);
        }
        return res;
    }

    /**
     * Returns a list of all invoices
     * 
     * @param lazy if true enable lazy load, otherwise load all
     * @return list of invoice instances
     */
    public List<Invoice> findAllInvoices(boolean lazy) {
        LOGGER.debug("{}.findAllInvoices({}) called ...", this.getClass().getSimpleName(), lazy);
        ensureValidSession();
        simulateDatabaseDelay();
        List<Invoice> res = new ArrayList<>(database.size());
        for (String[] invoiceData : database.values()) {
            if (lazy) {
                LOGGER.debug("Creating lazy-load invoice instance ...");
                res.add(new Invoice(this, invoiceData[0], invoiceData[1]));
            }
            else {
                LOGGER.debug("Creating complete invoice instance ...");
                res.add(new Invoice(this, invoiceData[0], invoiceData[1], invoiceData[2], invoiceData[3], invoiceData[4], invoiceData[5]));
            }
        }
        Collections.sort(res, Invoice.BY_ID_COMPARATOR);
        return res;
    }

    /**
     * Returns a list of all invoices, using lazy load
     * 
     * @return list of invoice instances
     */
    public List<Invoice> findAllInvoices() {
        return findAllInvoices(true);
    }

    /**
     * Loads the data for the lazy load fields from the database and sets the fields
     * 
     * @param invoice instance of the invoice to be loaded
     */
    public void loadLazyFields(Invoice invoice) {
        LOGGER.debug("{}.loadLazyFields({}) called ...", this.getClass().getSimpleName(), invoice);
        ensureValidSession();
        simulateDatabaseDelay();
        String[] invoiceData = database.get(invoice.getInvoiceId());
        if (invoiceData != null) {
            invoice.setLazyFields(invoiceData[2], invoiceData[3], invoiceData[4], invoiceData[5]);
        }
    }

    /**
     * Simulate delay for accessing the underlying database
     */
    private static void simulateDatabaseDelay() {
        MiscUtils.sleepIgnoreException(SIMULATED_DATABASE_DELAY_MILLIS);
    }

    /**
     * Checks whether the session is valid or not
     */
    private void ensureValidSession() {
        LOGGER.debug("{}.ensureValidSession() called ...", this.getClass().getSimpleName());
        if (closed) {
            LOGGER.debug("Found closed session, cannot load, throwing exception");
            throw new IllegalStateException("Session closed!");
        }
    }

    /**
     * closes the persistence session
     */
    public void close() {
        LOGGER.debug("{}.close() called ...", this.getClass().getSimpleName());
        this.closed = true;
    }

}
