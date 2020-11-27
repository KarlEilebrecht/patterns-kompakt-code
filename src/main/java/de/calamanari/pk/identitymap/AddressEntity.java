//@formatter:off
/*
 * Address Entity - one of the business entities in this example.
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
package de.calamanari.pk.identitymap;

/**
 * Address Entity - one of the business entities in this example.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class AddressEntity implements Entity<String> {

    /**
     * address identifier
     */
    private String id = null;

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
     * 
     * @param addressId identifier
     * @param customerId owning customer identifier
     * @param street address field
     * @param zipCode address field
     * @param city address field
     * @param country address field
     * @param salutation address field
     */
    public AddressEntity(String addressId, String customerId, String street, String zipCode, String city, String country, String salutation) {
        this.id = addressId;
        this.customerId = customerId;
        this.street = street;
        this.zipCode = zipCode;
        this.city = city;
        this.country = country;
        this.salutation = salutation;
    }

    /**
     * Returns the address-ID
     * 
     * @return id
     */
    @Override
    public String getId() {
        return id;
    }

    /**
     * Sets the address-ID
     * 
     * @param id identifier
     */
    public void setAddressId(String id) {
        this.id = id;
    }

    /**
     * Returns the customer ID
     * 
     * @return customerId
     */
    public String getCustomerId() {
        return customerId;
    }

    /**
     * Sets the customer ID
     * 
     * @param customerId identifier of the owning customer
     */
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    /**
     * Returns street address
     * 
     * @return street
     */
    public String getStreet() {
        return street;
    }

    /**
     * Sets street address
     * 
     * @param street address field
     */
    public void setStreet(String street) {
        this.street = street;
    }

    /**
     * Returns the zipcode
     * 
     * @return zipCode address field
     */
    public String getZipCode() {
        return zipCode;
    }

    /**
     * Sets the zipCode
     * 
     * @param zipCode address field
     */
    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    /**
     * Returns the city name
     * 
     * @return city address field
     */
    public String getCity() {
        return city;
    }

    /**
     * Sets the city name
     * 
     * @param city address field
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * Returns the country name
     * 
     * @return country
     */
    public String getCountry() {
        return country;
    }

    /**
     * Sets the country
     * 
     * @param country address field
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * Returns salutation for letters
     * 
     * @return salutation
     */
    public String getSalutation() {
        return salutation;
    }

    /**
     * Sets the salutation for letters
     * 
     * @param salutation address field
     */
    public void setSalutation(String salutation) {
        this.salutation = salutation;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "({id=" + id + ", customerId=" + customerId + ", street=" + street + ", zipCode=" + zipCode + ", city=" + city
                + ", country=" + country + ", salutation=" + salutation + "})";
    }

    /**
     * Creates a duplicate with the same values as this object
     * @return duplicate
     */
    public AddressEntity shallowCopy() {
        return new AddressEntity(id, customerId, street, zipCode, city, country, salutation);
    }

}
