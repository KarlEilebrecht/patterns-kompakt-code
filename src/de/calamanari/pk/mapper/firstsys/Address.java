/*
 * Address - entity in the first subsystem
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
package de.calamanari.pk.mapper.firstsys;

/**
 * Address - entity in the first subsystem
 * @author <a href="mailto:Karl.Eilebrecht(a/t)web.de">Karl Eilebrecht</a>
 */
public class Address {

    /**
     * ID of person this address belongs to
     */
    private String personId = null;

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
     * Creates address for a person
     * @param personId identifier
     * @param street address field
     * @param zipCode address field
     * @param city address field
     */
    public Address(String personId, String street, String zipCode, String city) {
        this.personId = personId;
        this.city = city;
        this.street = street;
        this.zipCode = zipCode;
    }

    /**
     * Returns id of person, this address belongs to
     * @return personId
     */
    public String getPersonId() {
        return personId;
    }

    /**
     * Returns the street address of customer
     * @return street
     */
    public String getStreet() {
        return street;
    }

    /**
     * Sets the street of the customer
     * @param street address field
     */
    public void setStreet(String street) {
        this.street = street;
    }

    /**
     * Returns the zip-code of the customer
     * @return zipCode
     */
    public String getZipCode() {
        return zipCode;
    }

    /**
     * Sets the zip-code of the customer
     * @param zipCode address field
     */
    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    /**
     * Returns the city of the customer
     * @return city
     */
    public String getCity() {
        return city;
    }

    /**
     * Sets the city of the customer
     * @param city address field
     */
    public void setCity(String city) {
        this.city = city;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "({personId=" + personId + ", street=" + street + ", zipCode="
                + zipCode + ", city=" + city + "})";
    }

}
