//@formatter:off
/*
 * Customer - entity in the second subsystem
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
package de.calamanari.pk.mapper.secondsys;

/**
 * Customer - entity in the second subsystem
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
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
     * customer lifetime in days
     */
    private int customerLifeTimeDays = 0;

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
     * Creates customer
     * 
     * @param customerId identifier
     */
    public Customer(String customerId) {
        this.customerId = customerId;
    }

    /**
     * Returns id of customer
     * 
     * @return customerId
     */
    public String getCustomerId() {
        return customerId;
    }

    /**
     * Returns the customer's lifetime in days
     * 
     * @return customerLifeTimeDays
     */
    public int getCustomerLifeTimeDays() {
        return customerLifeTimeDays;
    }

    /**
     * Sets the customer lifetime in days
     * 
     * @param customerLifeTimeDays time in day since date of birth
     */
    public void setCustomerLifeTimeDays(int customerLifeTimeDays) {
        this.customerLifeTimeDays = customerLifeTimeDays;
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

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "({customerId=" + customerId + ", lastName=" + lastName + ", firstName=" + firstName
                + ", customerLifeTimeDays=" + customerLifeTimeDays + ", street=" + street + ", zipCode=" + zipCode + ", city=" + city + "})";
    }

}
