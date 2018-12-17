//@formatter:off
/*
 * Invoice - supplementary class to demonstrate LAZY LOAD
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

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.Comparator;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Invoice - supplementary class to demonstrate LAZY LOAD pattern.<br>
 * Each invoice has a number of fields, but only the invoiceId and the amountClaimed will be loaded by default. Other fields will be loaded on demand.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class Invoice implements Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(Invoice.class);

    /**
     * Compares the ids, for sorting
     */
    public static final Comparator<Invoice> BY_ID_COMPARATOR = new Comparator<>() {

        @Override
        public int compare(Invoice o1, Invoice o2) {
            return o1.getInvoiceId().compareTo(o2.getInvoiceId());
        }

    };

    /**
     * for serialization
     */
    private static final long serialVersionUID = -4060236383606470171L;

    /**
     * Reference to the persistence manager
     */
    private transient PersistenceSession persistenceSession;

    /**
     * identifier of invoice, also primary key in the database
     */
    private String invoiceId;

    /**
     * specific amount of invoice
     */
    private double amountClaimed;

    /**
     * name of debtor
     */
    private String debtorName;

    /**
     * debtor address field
     */
    private String street;

    /**
     * debtor address zipCode
     */
    private String zipCode;

    /**
     * debtor address city
     */
    private String city;

    /**
     * flag to indicate whether the instance is complete or lazy loaded
     */
    private boolean dataComplete = false;

    /**
     * Creates new invoice instance
     * 
     * @param persistenceSession current session
     * @param invoiceId identifier
     * @param amountClaimed requested monetary value
     * @param debtorName name of the one who has to pay
     * @param street address field
     * @param zipCode address field
     * @param city address field
     */
    public Invoice(PersistenceSession persistenceSession, String invoiceId, String amountClaimed, String debtorName, String street, String zipCode,
            String city) {
        LOGGER.debug("{}({invoiceId={}, amountClaimed={}, debtorName={}, street={}, zipCode={}, city={}}) created.", this.getClass().getSimpleName(), invoiceId,
                amountClaimed, debtorName, street, zipCode, city);
        double amount = 0;
        try {
            amount = Double.parseDouble(amountClaimed);
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        this.invoiceId = invoiceId;
        this.amountClaimed = amount;
        this.debtorName = debtorName;
        this.street = street;
        this.zipCode = zipCode;
        this.city = city;
        this.dataComplete = true;
        this.persistenceSession = persistenceSession;
    }

    /**
     * Creates new invoice instance
     * 
     * @param persistenceSession current session
     * @param invoiceId identifier
     * @param amountClaimed value to be payed by the debtor
     */
    public Invoice(PersistenceSession persistenceSession, String invoiceId, String amountClaimed) {
        this(persistenceSession, invoiceId, amountClaimed, null, null, null, null);
        this.dataComplete = false;
    }

    /**
     * If not loaded yet, use the underlying persistence to fill all the fields.
     */
    private void ensureInstanceCompletelyLoaded() {
        LOGGER.debug("{}.ensureInstanceCompletelyLoaded() called", this.getClass().getSimpleName());
        if (!dataComplete) {
            LOGGER.debug("Loading missing fields ...");
            if (this.persistenceSession == null) {
                LOGGER.debug("No session available, cannot load, throwing exception");
                throw new IllegalStateException("Cannot load data, entity detached!");
            }
            this.persistenceSession.loadLazyFields(this);
            dataComplete = true;
        }
    }

    /**
     * This method is used to set the lazy-loaded fields when requested
     * 
     * @param debtorName name of the person who has to pay
     * @param street address field
     * @param zipCode address field
     * @param city address field
     */
    public void setLazyFields(String debtorName, String street, String zipCode, String city) {
        LOGGER.debug("{}.setLazyFields('{}', '{}', '{}', '{}') called.", this.getClass().getSimpleName(), debtorName, street, zipCode, city);
        this.debtorName = debtorName;
        this.street = street;
        this.zipCode = zipCode;
        this.city = city;
    }

    /**
     * Returns the invoice id
     * 
     * @return invoiceId
     */
    public String getInvoiceId() {
        LOGGER.debug("{}.getInvoiceId() called.", this.getClass().getSimpleName());
        return invoiceId;
    }

    /**
     * Returns the amount
     * 
     * @return amount of invoice
     */
    public double getAmountClaimed() {
        LOGGER.debug("{}.getAmountClaimed() called.", this.getClass().getSimpleName());
        return amountClaimed;
    }

    /**
     * Returns the debtor name
     * 
     * @return debtor name
     */
    public String getDebtorName() {
        LOGGER.debug("{}.getDebtorName() called.", this.getClass().getSimpleName());
        ensureInstanceCompletelyLoaded();
        return debtorName;
    }

    /**
     * Returns the address field street
     * 
     * @return street
     */
    public String getStreet() {
        LOGGER.debug("{}.getStreet() called.", this.getClass().getSimpleName());
        ensureInstanceCompletelyLoaded();
        return street;
    }

    /**
     * Returns the address field zipCode
     * 
     * @return zipCoe
     */
    public String getZipCode() {
        LOGGER.debug("{}.getZipCode() called.", this.getClass().getSimpleName());
        ensureInstanceCompletelyLoaded();
        return zipCode;
    }

    /**
     * Returns the address field city
     * 
     * @return city
     */
    public String getCity() {
        LOGGER.debug("{}.getCity() called.", this.getClass().getSimpleName());
        ensureInstanceCompletelyLoaded();
        return city;
    }

    @Override
    public String toString() {
        NumberFormat nf = NumberFormat.getInstance(Locale.US);
        nf.setMinimumFractionDigits(2);
        nf.setMaximumFractionDigits(2);

        return this.getClass().getSimpleName() + "({invoiceId=" + invoiceId + ", amountClaimed=" + nf.format(amountClaimed) + ", debtorName=" + debtorName
                + ", street=" + street + ", zipCode=" + zipCode + ", city=" + city + ", dataComplete=" + dataComplete + ", detached="
                + (persistenceSession == null) + "})";
    }

}
