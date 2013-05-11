/*
 * Customer - entity in COARSE GRAINED LOCK example
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
package de.calamanari.pk.coarsegrainedlock;

/**
 * Customer - entity in COARSE GRAINED LOCK example
 * @author <a href="mailto:Karl.Eilebrecht(a/t)web.de">Karl Eilebrecht</a>
 */
public class Customer {

    /**
     * id of customer
     */
    private String customerId = null;

    /**
     * last name
     */
    private String lastName = null;

    /**
     * first name
     */
    private String firstName = null;

    /**
     * Creates customer
     * @param customerId identifier
     * @param firstName person's first name
     * @param lastName person's last name
     */
    public Customer(String customerId, String firstName, String lastName) {
        this.customerId = customerId;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    /**
     * Returns id of customer
     * @return customerId
     */
    public String getCustomerId() {
        return customerId;
    }

    /**
     * Returns the customer's last name
     * @return last name of customer
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name of customer
     * @param lastName person's last name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Returns the first name of the customer
     * @return firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name of the customer
     * @param firstName person's first name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "({customerId='" + customerId + "', lastName='" + lastName
                + "', firstName='" + firstName + "'})";
    }

}
