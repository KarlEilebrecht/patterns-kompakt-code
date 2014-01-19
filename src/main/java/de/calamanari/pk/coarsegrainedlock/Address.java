/*
 * Address - entity in COARSE GRAINED LOCK example
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
package de.calamanari.pk.coarsegrainedlock;

/**
 * Address - entity in COARSE GRAINED LOCK example
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class Address {

    /**
     * id of this address
     */
    private String id;

    /**
     * id of the customer this address belongs to
     */
    private String customerId;

    /**
     * Street
     */
    private String street;

    /**
     * City
     */
    private String city;

    /**
     * Zip code
     */
    private String zipCode;

    /**
     * Creates a new Address using the given parameters
     * @param id identifier
     * @param customerId related person identifier
     * @param street address field
     * @param city address field
     * @param zipCode address field
     */
    public Address(String id, String customerId, String street, String city, String zipCode) {
        this.id = id;
        this.customerId = customerId;
        this.street = street;
        this.city = city;
        this.zipCode = zipCode;
    }

    /**
     * Returns address id
     * @return address id
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the related customer id
     * @return customerId
     */
    public String getCustomerId() {
        return customerId;
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
     * Returns the city
     * @return city name
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

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "({id='" + id + "', customerId='" + customerId + "', street='"
                + street + "', zipCode='" + zipCode + "', city='" + city + "'})";
    }

}
