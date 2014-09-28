//@formatter:off
/*
 * Data Manager - handles persistence in this OPTIMISTIC OFFLINE LOCK example 
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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ConcurrentModificationException;
import java.util.logging.Logger;

import javax.sql.DataSource;

/**
 * Data Manager - handles persistence in this OPTIMISTIC OFFLINE LOCK example<br>
 * Placeholder for any kind of database access management (i.e. DAO is compatible with OPTIMISTIC OFFLINE LOCK pattern).
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class DataManager {

    /**
     * logger
     */
    protected static final Logger LOGGER = Logger.getLogger(DataManager.class.getName());

    /**
     * prepared statement to store a customer: <code>{@value}</code>
     */
    private static final String SQL_INSERT_NEW_CUSTOMER = "insert into CUSTOMER (CUSTOMER_ID, FIRST_NAME, " + "LAST_NAME, STREET, ZIPCODE, CITY, VERSION) "
            + "values (?, ?, ?, ?, ?, ?, 0)";

    /**
     * prepared statement to query a customer by id: <code>{@value}</code>
     */
    private static final String SQL_SELECT_CUSTOMER_BY_ID = "select CUSTOMER_ID, FIRST_NAME, " + "LAST_NAME, STREET, ZIPCODE, CITY, VERSION from CUSTOMER "
            + "where CUSTOMER_ID=?";

    /**
     * prepared statement to update a customer and increase version: <code>{@value}</code>
     */
    private static final String SQL_UPDATE_CUSTOMER = "update CUSTOMER set FIRST_NAME=?, " + "LAST_NAME=?, STREET=?, ZIPCODE=?, CITY=?, VERSION=VERSION + 1 "
            + "where CUSTOMER_ID=? and VERSION=?";

    /**
     * datasource to be used
     */
    private final DataSource dataSource;

    /**
     * Creates a new instance on the given dataSource
     * 
     * @param dataSource underlying data source (DB)
     */
    public DataManager(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * Create a customer, in this example used to add test data
     * 
     * @param customerId identifier
     * @param firstName person's first name
     * @param lastName person's last name
     * @param street address field
     * @param zipCode address field
     * @param city address field
     */
    public void addCustomer(int customerId, String firstName, String lastName, String street, String zipCode, String city) {
        try (Connection con = dataSource.getConnection(); PreparedStatement stmt = con.prepareStatement(SQL_INSERT_NEW_CUSTOMER)) {
            stmt.setInt(1, customerId);
            stmt.setString(2, firstName);
            stmt.setString(3, lastName);
            stmt.setString(4, street);
            stmt.setString(5, zipCode);
            stmt.setString(6, city);
            stmt.executeUpdate();
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    /**
     * Returns the corresponding customer for the given id
     * 
     * @param customerId identifier
     * @return Customer or null if not found
     */
    private Customer findCustomerByIdInternal(int customerId) {
        Customer res = null;
        try (Connection con = dataSource.getConnection(); PreparedStatement stmt = con.prepareStatement(SQL_SELECT_CUSTOMER_BY_ID)) {
            stmt.setInt(1, customerId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    res = new Customer(customerId, rs.getString(2), rs.getString(3), rs.getString(4), rs.getString(5), rs.getString(6));
                    res.setVersion(rs.getLong(7));
                }
            }
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return res;
    }

    /**
     * Returns the corresponding customer for the given id
     * 
     * @param customerId identifier
     * @return Customer or null if not found
     */
    public Customer findCustomerById(int customerId) {
        LOGGER.fine(Thread.currentThread().getName() + ": " + DataManager.class.getSimpleName() + ".findCustomerById('" + customerId + "') called");

        LOGGER.fine(Thread.currentThread().getName() + ": " + "Performing transactional select on database ...");

        Customer res = findCustomerByIdInternal(customerId);

        if (res != null) {
            LOGGER.fine(Thread.currentThread().getName() + ": " + "Found customer, record version: " + res.getVersion());
        }
        else {
            LOGGER.fine(Thread.currentThread().getName() + ": " + "Customer not found.");
        }
        return res;
    }

    /**
     * Writes the customer data to the database (updates existing record).
     * 
     * @param customer instance to be stored in the database
     */
    public void storeCustomer(Customer customer) {
        LOGGER.fine(Thread.currentThread().getName() + ": " + DataManager.class.getSimpleName() + ".storeCustomer(" + customer + ") called");

        LOGGER.fine(Thread.currentThread().getName() + ": " + "Performing transactional update on database, expecting record version: " + customer.getVersion());

        int numberOfUpdatedRecords = updateCustomer(customer);

        if (numberOfUpdatedRecords == 1) {
            customer.setVersion(customer.getVersion() + 1);
            LOGGER.fine(Thread.currentThread().getName() + ": " + "Database update successful, new record version: " + customer.getVersion());
        }
        else {
            Customer currentCustomerVersion = findCustomerByIdInternal(customer.getCustomerId());
            if (currentCustomerVersion == null) {
                LOGGER.fine(Thread.currentThread().getName() + ": " + "Database update failed, record deletion detected, throwing exception");
                throw new ConcurrentModificationException("Customer could not be updated: " + customer + " (deleted)");
            }
            else {
                LOGGER.fine(Thread.currentThread().getName() + ": " + "Database update failed, concurrent update detected (version mismatch, current version: "
                        + currentCustomerVersion.getVersion() + ", expected: " + customer.getVersion() + "), throwing exception");
                throw new ConcurrentModificationException("Customer could not be updated: " + customer + " (version mismatch, current version: "
                        + currentCustomerVersion.getVersion() + ", expected: " + customer.getVersion() + ")");
            }
            // The client may then decide to select to
            // merge the data - either automatically or with help of the user.
        }

    }

    /**
     * Performs the database update
     * 
     * @param customer instance to be updated (into the database)
     * @return number of updated rows, either 1 (success) or 0 (failed)
     */
    private int updateCustomer(Customer customer) {
        int numberOfUpdatedRecords = 0;
        try (Connection con = dataSource.getConnection(); PreparedStatement stmt = con.prepareStatement(SQL_UPDATE_CUSTOMER)) {
            stmt.setString(1, customer.getFirstName());
            stmt.setString(2, customer.getLastName());
            stmt.setString(3, customer.getStreet());
            stmt.setString(4, customer.getZipCode());
            stmt.setString(5, customer.getCity());
            stmt.setInt(6, customer.getCustomerId());
            stmt.setLong(7, customer.getVersion());
            numberOfUpdatedRecords = stmt.executeUpdate();
        }
        catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return numberOfUpdatedRecords;
    }

}
