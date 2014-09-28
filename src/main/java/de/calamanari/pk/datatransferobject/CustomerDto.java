//@formatter:off
/*
 * Customer DTO - the DATA TRANSFER OBJECT
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
//@formatter:on

package de.calamanari.pk.datatransferobject;

import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.calamanari.pk.util.LogUtils;

/**
 * Customer DTO - the DATA TRANSFER OBJECT
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class CustomerDto implements Customer, Serializable {

    /**
     * logger
     */
    private static final Logger LOGGER = Logger.getLogger(CustomerDto.class.getName());

    /**
     * for serialization, DTOs are typically serializable
     */
    private static final long serialVersionUID = 7996392672467364829L;

    static {
        LogUtils.setLogLevel(Level.FINE, CustomerDto.class);
    }

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
        LOGGER.fine(this.getClass().getSimpleName() + " created");
    }

    @Override
    public String getCustomerId() {
        LOGGER.fine(this.getClass().getSimpleName() + ".getCustomerId() called");
        return customerId;
    }

    @Override
    public void setCustomerId(String customerId) {
        LOGGER.fine(this.getClass().getSimpleName() + ".setCustomerId('" + customerId + "') called");
        this.customerId = customerId;
    }

    @Override
    public String getLastName() {
        LOGGER.fine(this.getClass().getSimpleName() + ".getLastName() called");
        return lastName;
    }

    @Override
    public void setLastName(String lastName) {
        LOGGER.fine(this.getClass().getSimpleName() + ".setLastName('" + lastName + "') called");
        this.lastName = lastName;
    }

    @Override
    public String getFirstName() {
        LOGGER.fine(this.getClass().getSimpleName() + ".getFirstName() called");
        return firstName;
    }

    @Override
    public void setFirstName(String firstName) {
        LOGGER.fine(this.getClass().getSimpleName() + ".setFirstName('" + firstName + "') called");
        this.firstName = firstName;
    }

    @Override
    public String getStreet() {
        LOGGER.fine(this.getClass().getSimpleName() + ".getStreet() called");
        return street;
    }

    @Override
    public void setStreet(String street) {
        LOGGER.fine(this.getClass().getSimpleName() + ".setStreet('" + street + "') called");
        this.street = street;
    }

    @Override
    public String getZipCode() {
        LOGGER.fine(this.getClass().getSimpleName() + ".getZipCode() called");
        return zipCode;
    }

    @Override
    public void setZipCode(String zipCode) {
        LOGGER.fine(this.getClass().getSimpleName() + ".setZipCode('" + zipCode + "') called");
        this.zipCode = zipCode;
    }

    @Override
    public String getCity() {
        LOGGER.fine(this.getClass().getSimpleName() + ".getCity() called");
        return this.city;
    }

    @Override
    public void setCity(String city) {
        LOGGER.fine(this.getClass().getSimpleName() + ".setCity('" + city + "') called");
        this.city = city;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "({customerId=" + customerId + ", lastName=" + lastName + ", firstName=" + firstName + ", street=" + street
                + ", zipCode=" + zipCode + ", city=" + city + "})";
    }

}
