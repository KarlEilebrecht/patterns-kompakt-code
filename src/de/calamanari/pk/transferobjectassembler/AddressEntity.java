/*
 * Address Entity - one of the business entities in this example.
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

import java.util.logging.Logger;

/**
 * Address Entity - one of the business entities in this example.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class AddressEntity {

    /**
     * logger
     */
    protected static final Logger LOGGER = Logger.getLogger(AddressEntity.class.getName());

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
     * Creates new address entity
     */
    public AddressEntity() {

    }

    /**
     * Creates new address entity from the given data
     * @param addressId identifier
     * @param customerId related customer's identifier
     * @param street address field
     * @param zipCode address field
     * @param city address field
     * @param country address field
     * @param salutation address field
     */
    public AddressEntity(String addressId, String customerId, String street, String zipCode, String city,
            String country, String salutation) {
        this.addressId = addressId;
        this.customerId = customerId;
        this.street = street;
        this.zipCode = zipCode;
        this.city = city;
        this.country = country;
        this.salutation = salutation;
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

    /**
     * Returns a corresponding data transfer object for this Address
     * @return dto
     */
    public AddressDto toDto() {
        LOGGER.fine(this.getClass().getSimpleName() + ".toDto() called");
        return new AddressDto(addressId, customerId, street, zipCode, city, country, salutation);
    }

    /**
     * Updates this entity from the given data transfer object
     * @param dto source data transfer object to copy data from into this instance
     */
    public void fromDto(AddressDto dto) {
        this.addressId = dto.getAddressId();
        this.customerId = dto.getCustomerId();
        this.salutation = dto.getSalutation();
        this.street = dto.getStreet();
        this.zipCode = dto.getZipCode();
        this.city = dto.getCity();
        this.country = dto.getCountry();
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "({addressId=" + addressId + ", customerId=" + customerId
                + ", street=" + street + ", zipCode=" + zipCode + ", city=" + city + ", country=" + country
                + ", salutation=" + salutation + "})";
    }

}
