/*
 * Customer - interface for customer representations
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
package de.calamanari.pk.datatransferobject;

import java.io.IOException;

/**
 * Customer - interface for customer representations, introduced in this example to allow transparently handling
 * different representations (DATA TRANSFER OBJECT vs. entity remote interface) by the client - for demonstration
 * purposes only.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)web.de">Karl Eilebrecht</a>
 */
public interface Customer {

    /**
     * Returns id of customer
     * @return customerId
     * @throws IOException on data access error
     */
    public String getCustomerId() throws IOException;

    /**
     * Sets the id of customer
     * @param customerId identifier
     * @throws IOException on data access error
     */
    public void setCustomerId(String customerId) throws IOException;

    /**
     * Returns the customer's last name
     * @return last name of customer
     * @throws IOException on data access error
     */
    public String getLastName() throws IOException;

    /**
     * Sets the last name of customer
     * @param lastName person's last name
     * @throws IOException on data access error
     */
    public void setLastName(String lastName) throws IOException;

    /**
     * Returns the first name of the customer
     * @return firstName
     * @throws IOException on data access error
     */
    public String getFirstName() throws IOException;

    /**
     * Sets the first name of the customer
     * @param firstName person's first name
     * @throws IOException on data access error
     */
    public void setFirstName(String firstName) throws IOException;

    /**
     * Returns the street address of customer
     * @return street
     * @throws IOException on data access error
     */
    public String getStreet() throws IOException;

    /**
     * Sets the street of the customer
     * @param street address field
     * @throws IOException on data access error
     */
    public void setStreet(String street) throws IOException;

    /**
     * Returns the zip-code of the customer
     * @return zipCode address field
     * @throws IOException on data access error
     */
    public String getZipCode() throws IOException;

    /**
     * Sets the zip-code of the customer
     * @param zipCode address field
     * @throws IOException on data access error
     */
    public void setZipCode(String zipCode) throws IOException;

    /**
     * Returns the city of the customer
     * @return city address field
     * @throws IOException on data access error
     */
    public String getCity() throws IOException;

    /**
     * Sets the city of the customer
     * @param city address field
     * @throws IOException on data access error
     */
    public void setCity(String city) throws IOException;

}
