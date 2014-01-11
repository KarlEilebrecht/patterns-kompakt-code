/*
 * Account - supplementary class
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
package de.calamanari.pk.servicestub;

/**
 * Account - supplementary class
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class Account {

    /**
     * id of account
     */
    private String accountId = null;

    /**
     * first name
     */
    private String firstName = null;

    /**
     * last name
     */
    private String lastName = null;

    /**
     * Street
     */
    private String street = null;

    /**
     * zipCode
     */
    private String zipCode = null;

    /**
     * city
     */
    private String city = null;

    /**
     * Creates new address with the given attributes
     * @param accountId identifier
     * @param firstName person's first name
     * @param lastName person's last name
     * @param street address field
     * @param zipCode address field
     * @param city address field
     */
    public Account(String accountId, String firstName, String lastName, String street, String zipCode, String city) {
        this.accountId = accountId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.street = street;
        this.zipCode = zipCode;
        this.city = city;
    }

    /**
     * Returns the account id
     * @return account id
     */
    public String getAccountId() {
        return accountId;
    }

    /**
     * Returns the first name
     * @return first name
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the first name
     * @param firstName person's first name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Returns the last name
     * @return last name
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the last name
     * @param lastName person's last name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Returns the street
     * @return street
     */
    public String getStreet() {
        return street;
    }

    /**
     * Sets the street
     * @param street address field
     */
    public void setStreet(String street) {
        this.street = street;
    }

    /**
     * Returns the zip code
     * @return zip code
     */
    public String getZipCode() {
        return zipCode;
    }

    /**
     * Sets the zip code
     * @param zipCode address field
     */
    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    /**
     * Returns the city
     * @return city
     */
    public String getCity() {
        return city;
    }

    /**
     * Sets the city
     * @param city address field
     */
    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "({accountId='" + accountId + "', firstName='" + firstName
                + "', lastName='" + lastName + "', street='" + street + "', zipCode='" + zipCode + "', city='" + city
                + "'})";
    }

}
