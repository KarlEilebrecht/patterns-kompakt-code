//@formatter:off
/*
 * Customer - entity providing OPTIMISTIC OFFLINE LOCK capabilities
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
package de.calamanari.pk.optimisticofflinelock;

/**
 * Customer - entity providing OPTIMISTIC OFFLINE LOCK capabilities
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class Customer {

    /**
     * id of customer
     */
    private int customerId = 0;

    /**
     * last name
     */
    private String lastName = null;

    /**
     * first name
     */
    private String firstName = null;

    /**
     * street
     */
    private String street = null;

    /**
     * Zip code
     */
    private String zipCode = null;

    /**
     * city
     */
    private String city = null;

    /**
     * The version field manages OPTIMISTIC OFFLINE LOCK
     */
    private long version = 0;

    /**
     * Creates customer
     * 
     * @param customerId identifier
     * @param firstName person's first name
     * @param lastName person's last name
     * @param street address field
     * @param zipCode address field
     * @param city address field
     */
    public Customer(int customerId, String firstName, String lastName, String street, String zipCode, String city) {
        this.customerId = customerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.street = street;
        this.zipCode = zipCode;
        this.city = city;
    }

    /**
     * Returns id of customer
     * 
     * @return customerId
     */
    public int getCustomerId() {
        return customerId;
    }

    /**
     * Returns the customer's last name
     * 
     * @return last name of customer
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name of customer
     * 
     * @param lastName person's last name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Returns the first name of the customer
     * 
     * @return firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name of the customer
     * 
     * @param firstName person's first name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Returns the street address of customer
     * 
     * @return street
     */
    public String getStreet() {
        return street;
    }

    /**
     * Sets the street of the customer
     * 
     * @param street address field
     */
    public void setStreet(String street) {
        this.street = street;
    }

    /**
     * Returns the zip-code of the customer
     * 
     * @return zipCode
     */
    public String getZipCode() {
        return zipCode;
    }

    /**
     * Sets the zip-code of the customer
     * 
     * @param zipCode address field
     */
    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    /**
     * Returns the city of the customer
     * 
     * @return city
     */
    public String getCity() {
        return city;
    }

    /**
     * Sets the city of the customer
     * 
     * @param city address field
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Returns the version of this record<br>
     * Framework uses this to manage OPTIMISTIC OFFLINE LOCK
     * 
     * @return version
     */
    public long getVersion() {
        return version;
    }

    /**
     * Sets the version of this record<br>
     * Framework uses this to manage OPTIMISTIC OFFLINE LOCK
     * 
     * @param version record version
     */
    public void setVersion(long version) {
        this.version = version;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "({customerId='" + customerId + "', lastName='" + lastName + "', firstName='" + firstName + "', street='"
                + street + "', zipCode='" + zipCode + "', city='" + city + "', version=" + version + "})";
    }

}
