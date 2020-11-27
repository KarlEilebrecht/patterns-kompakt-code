//@formatter:off
/*
 * Customer DTO - the DATA TRANSFER OBJECT
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

package de.calamanari.pk.datatransferobject;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Customer DTO - the DATA TRANSFER OBJECT
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class CustomerDto implements Customer, Serializable {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerDto.class);

    /**
     * for serialization, DTOs are typically serializable
     */
    private static final long serialVersionUID = 7996392672467364829L;

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
     * Constructor
     */
    public CustomerDto() {
        LOGGER.debug("{} created", this.getClass().getSimpleName());
    }

    @Override
    public String getCustomerId() {
        LOGGER.debug("{}.getCustomerId() called", this.getClass().getSimpleName());
        return customerId;
    }

    @Override
    public void setCustomerId(String customerId) {
        LOGGER.debug("{}.setCustomerId('{}') called", this.getClass().getSimpleName(), customerId);
        this.customerId = customerId;
    }

    @Override
    public String getLastName() {
        LOGGER.debug("{}.getLastName() called", this.getClass().getSimpleName());
        return lastName;
    }

    @Override
    public void setLastName(String lastName) {
        LOGGER.debug("{}.setLastName('{}') called", this.getClass().getSimpleName(), lastName);
        this.lastName = lastName;
    }

    @Override
    public String getFirstName() {
        LOGGER.debug("{}.getFirstName() called", this.getClass().getSimpleName());
        return firstName;
    }

    @Override
    public void setFirstName(String firstName) {
        LOGGER.debug("{}.setFirstName('{}') called", this.getClass().getSimpleName(), firstName);
        this.firstName = firstName;
    }

    @Override
    public String getStreet() {
        LOGGER.debug("{}.getStreet() called", this.getClass().getSimpleName());
        return street;
    }

    @Override
    public void setStreet(String street) {
        LOGGER.debug("{}.setStreet('{}') called", this.getClass().getSimpleName(), street);
        this.street = street;
    }

    @Override
    public String getZipCode() {
        LOGGER.debug("{}.getZipCode() called", this.getClass().getSimpleName());
        return zipCode;
    }

    @Override
    public void setZipCode(String zipCode) {
        LOGGER.debug("{}.setZipCode('{}') called", this.getClass().getSimpleName(), zipCode);
        this.zipCode = zipCode;
    }

    @Override
    public String getCity() {
        LOGGER.debug("{}.getCity() called", this.getClass().getSimpleName());
        return this.city;
    }

    @Override
    public void setCity(String city) {
        LOGGER.debug("{}.setCity('{}') called", this.getClass().getSimpleName(), city);
        this.city = city;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "({customerId=" + customerId + ", lastName=" + lastName + ", firstName=" + firstName + ", street=" + street
                + ", zipCode=" + zipCode + ", city=" + city + "})";
    }

}
