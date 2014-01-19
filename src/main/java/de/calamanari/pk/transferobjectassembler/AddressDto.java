/*
 * Address DTO - one of the data transfer objects in this example.
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
package de.calamanari.pk.transferobjectassembler;

import java.io.Serializable;
import java.util.logging.Logger;

/**
 * Address DTO - one of the data transfer objects in this example.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class AddressDto implements Serializable {

    /**
     * logger
     */
    protected static final Logger LOGGER = Logger.getLogger(AddressDto.class.getName());

    /**
     * for serialization
     */
    private static final long serialVersionUID = -2118803089345086069L;

    /**
     * address identifier
     */
    private String addressId = null;

    /**
     * Id of customer
     */
    private String customerId = null;

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
     * country
     */
    private String country = null;

    /**
     * salutation
     */
    private String salutation = null;

    /**
     * Creates new address DATA TRANSFER OBJECT
     */
    public AddressDto() {

    }

    /**
     * Creates new address DATA TRANSFER OBJECT from the given data
     * @param addressId identifier
     * @param customerId related customer's identifier
     * @param street address field
     * @param zipCode address field
     * @param city address field
     * @param country address field
     * @param salutation address field
     */
    public AddressDto(String addressId, String customerId, String street, String zipCode, String city, String country,
            String salutation) {
        this.addressId = addressId;
        this.customerId = customerId;
        this.street = street;
        this.zipCode = zipCode;
        this.city = city;
        this.country = country;
        this.salutation = salutation;
        LOGGER.fine(this.getClass().getSimpleName() + " created: " + this.toString());
    }

    /**
     * Returns the address ID
     * @return addressId
     */
    public String getAddressId() {
        return addressId;
    }

    /**
     * Sets the address ID
     * @param addressId identifier
     */
    public void setAddressId(String addressId) {
        this.addressId = addressId;
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
     * @param customerId related customer's identifier
     */
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    /**
     * Returns street address
     * @return street
     */
    public String getStreet() {
        return street;
    }

    /**
     * Sets street address
     * @param street address field
     */
    public void setStreet(String street) {
        this.street = street;
    }

    /**
     * Returns the zipcode
     * @return zipCode
     */
    public String getZipCode() {
        return zipCode;
    }

    /**
     * Sets the zipCode
     * @param zipCode address field
     */
    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    /**
     * Returns the city name
     * @return city
     */
    public String getCity() {
        return city;
    }

    /**
     * Sets the city name
     * @param city address field
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Returns the country name
     * @return country
     */
    public String getCountry() {
        return country;
    }

    /**
     * Sets the country
     * @param country address field
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * Returns salutation for letters
     * @return salutation
     */
    public String getSalutation() {
        return salutation;
    }

    /**
     * Sets the salutation for letters
     * @param salutation address field
     */
    public void setSalutation(String salutation) {
        this.salutation = salutation;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "({addressId=" + addressId + ", customerId=" + customerId
                + ", street=" + street + ", zipCode=" + zipCode + ", city=" + city + ", country=" + country
                + ", salutation=" + salutation + "})";
    }

}
