/*
 * Customer Dwh Info DTO - one of the data transfer objects in this example.
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
package de.calamanari.pk.transferobjectassembler;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Logger;

/**
 * Customer Dwh Info DTO - one of the data transfer objects in this example.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)web.de">Karl Eilebrecht</a>
 */
public class CustomerDwhInfoDto implements Comparable<CustomerDwhInfoDto>, Serializable {

    /**
     * logger
     */
    protected static final Logger LOGGER = Logger.getLogger(CustomerDwhInfoDto.class.getName());

    /**
     * for serialization
     */
    private static final long serialVersionUID = -5043567945618104295L;

    /**
     * ID of customer
     */
    private String customerId = null;

    /**
     * some type classification
     */
    private String customerType = null;

    /**
     * technical score value
     */
    private int scorePoints = 0;

    /**
     * date of first order
     */
    private Date firstOrderDate = null;

    /**
     * least recent order date
     */
    private Date lastOrderDate = null;

    /**
     * there is an invoice not payed yet
     */
    private boolean dueInvoice = false;

    /**
     * data leads to conculion that this is customer is a fraud
     */
    private boolean fraudSuspicion = false;

    /**
     * customer usually pays late
     */
    private boolean badPayer = false;

    // more flags and infos

    /**
     * Creates new data warehouse info data transfer object
     */
    public CustomerDwhInfoDto() {

    }

    /**
     * Creates new data warehouse info data transfer object from the given data
     * @param customerId identifier
     * @param customerType type of customer
     * @param scorePoints value from scoring process
     * @param firstOrderDate date of the first order the customer placed
     * @param lastOrderDate least recent order date of the customer
     * @param dueInvoice flag to indicate unpaid bill
     * @param fraudSuspicion flag to indicate that we suspect illegal activities
     * @param badPayer flag to indicate a customer paying late or has unpaid bills
     */
    public CustomerDwhInfoDto(String customerId, String customerType, int scorePoints, Date firstOrderDate,
            Date lastOrderDate, boolean dueInvoice, boolean fraudSuspicion, boolean badPayer) {
        this.customerId = customerId;
        this.customerType = customerType;
        this.scorePoints = scorePoints;
        this.firstOrderDate = firstOrderDate;
        this.lastOrderDate = lastOrderDate;
        this.dueInvoice = dueInvoice;
        this.fraudSuspicion = fraudSuspicion;
        this.badPayer = badPayer;
        LOGGER.fine(this.getClass().getSimpleName() + " created: " + this.toString());
    }

    /**
     * Returns the customer ID
     * @return customerId
     */
    public String getCustomerId() {
        return customerId;
    }

    /**
     * Sets the customer ID
     * @param customerId identifier
     */
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    /**
     * Returns the type of customer (classification)
     * @return customerType
     */
    public String getCustomerType() {
        return customerType;
    }

    /**
     * Sets the customer type
     * @param customerType type of customer
     */
    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    /**
     * Returns the score points for this customer
     * @return scorePoints
     */
    public int getScorePoints() {
        return scorePoints;
    }

    /**
     * Sets the score points
     * @param scorePoints value from scoring
     */
    public void setScorePoints(int scorePoints) {
        this.scorePoints = scorePoints;
    }

    /**
     * Returns the date of first order placed by this customer
     * @return firstOrderDate
     */
    public Date getFirstOrderDate() {
        return firstOrderDate;
    }

    /**
     * Sets the date of first order placed by this customer
     * @param firstOrderDate date of first order
     */
    public void setFirstOrderDate(Date firstOrderDate) {
        this.firstOrderDate = firstOrderDate;
    }

    /**
     * Returns the date of least recent order placed by this customer
     * @return lastOrderDate
     */
    public Date getLastOrderDate() {
        return lastOrderDate;
    }

    /**
     * Sets the date of least recent order placed by this customer
     * @param lastOrderDate date of least recent order
     */
    public void setLastOrderDate(Date lastOrderDate) {
        this.lastOrderDate = lastOrderDate;
    }

    /**
     * Returns whether this customer has an open invoice
     * @return true whether there is an open invoice
     */
    public boolean isDueInvoice() {
        return dueInvoice;
    }

    /**
     * Sets the due invoice flag
     * @param dueInvoice true indicates an open invoice not payed yet
     */
    public void setDueInvoice(boolean dueInvoice) {
        this.dueInvoice = dueInvoice;
    }

    /**
     * Returns whether this customer could be a fraud
     * @return fraudSuspicion
     */
    public boolean isFraudSuspicion() {
        return fraudSuspicion;
    }

    /**
     * Sets the fraud suspicion flag
     * @param fraudSuspicion flag to indicate that we suspect illegal activities
     */
    public void setFraudSuspicion(boolean fraudSuspicion) {
        this.fraudSuspicion = fraudSuspicion;
    }

    /**
     * Returns whether this customer is a bad payer
     * @return badPayer flag
     */
    public boolean isBadPayer() {
        return badPayer;
    }

    /**
     * Sets the bad payer flag
     * @param badPayer flag to indicate a customer who pays late or only after reminding
     */
    public void setBadPayer(boolean badPayer) {
        this.badPayer = badPayer;
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        return this.getClass().getSimpleName() + "({customerId=" + customerId + ", customerType=" + customerType
                + ", scorePoints=" + scorePoints + ", firstOrderDate="
                + (firstOrderDate == null ? null : "'" + sdf.format(firstOrderDate) + "'") + ", lastOrderDate="
                + (lastOrderDate == null ? null : "'" + sdf.format(lastOrderDate) + "'") + ", dueInvoice=" + dueInvoice
                + ", fraudSuspicion=" + fraudSuspicion + ", badPayer=" + badPayer + "})";
    }

    @Override
    public int compareTo(CustomerDwhInfoDto o) {
        int res = -1;
        if (o != null) {
            res = this.customerId.compareTo(o.customerId);
        }
        return res;
    }

}