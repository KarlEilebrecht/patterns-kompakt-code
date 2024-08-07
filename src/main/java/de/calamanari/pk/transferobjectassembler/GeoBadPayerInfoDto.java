//@formatter:off
/*
 * Geo Bad Payer Info Dto - data transfer object to be assembled
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
package de.calamanari.pk.transferobjectassembler;

import java.io.Serializable;

/**
 * Geo Bad Payer Info Dto - data transfer object to be assembled, contains information to be collected from different business entities.<br>
 * In our fictional scenario a list of bad payers related to their location has to be displayed.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class GeoBadPayerInfoDto implements Serializable {

    /**
     * for serialization (DTOs are typically serializable)
     */
    private static final long serialVersionUID = 2410275631582225430L;

    /**
     * id of customer
     */
    private String customerId = null;

    /**
     * title field
     */
    private String title = null;

    /**
     * last name
     */
    private String lastName = null;

    /**
     * first name
     */
    private String firstName = null;

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
     * some type classification
     */
    private String customerType = null;

    /**
     * there is an invoice not payed yet
     */
    private boolean dueInvoice = false;

    /**
     * Returns a BUILDER to simplify construction
     * @param customerId mandatory
     * @return builder instance
     */
    public static Builder forCustomer(String customerId) {
        return new Builder(customerId);
    }

    /**
     * Returns the customerId
     * 
     * @return customerId
     */
    public String getCustomerId() {
        return customerId;
    }

    /**
     * Sets the customerId
     * 
     * @param customerId identifier
     */
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    /**
     * Returns customer title
     * 
     * @return title of customer
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the customer's title
     * 
     * @param title person's title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * Returns the last name of customer
     * 
     * @return lastName
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * Sets the customer's last name
     * 
     * @param lastName person's last name
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * Returns the customer's first name
     * 
     * @return firstName
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * Sets the customer's first name
     * 
     * @param firstName person's first name
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * Returns the zipcode
     * 
     * @return zipCode
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
     * @return city
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
     * Returns the type of customer (classification)
     * 
     * @return customerType
     */
    public String getCustomerType() {
        return customerType;
    }

    /**
     * Sets the customer type
     * 
     * @param customerType type of customer
     */
    public void setCustomerType(String customerType) {
        this.customerType = customerType;
    }

    /**
     * Returns whether this customer has an open invoice
     * 
     * @return true whether there is an open invoice
     */
    public boolean isDueInvoice() {
        return dueInvoice;
    }

    /**
     * Sets the due invoice flag
     * 
     * @param dueInvoice true indicates an open invoice not payed yet
     */
    public void setDueInvoice(boolean dueInvoice) {
        this.dueInvoice = dueInvoice;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "({customerId=" + customerId + ", title=" + title + ", lastName=" + lastName + ", firstName=" + firstName
                + ", zipCode=" + zipCode + ", city=" + city + ", country=" + country + ", customerType=" + customerType + ", dueInvoice=" + dueInvoice + "})";
    }

    /**
     * Builder to avoid over-parameterized constructor
     */
    public static class Builder {

        private final GeoBadPayerInfoDto result;

        /**
         * Initializes the builder
         * @param customerId mandatory
         */
        Builder(String customerId) {
            result = new GeoBadPayerInfoDto();
            result.setCustomerId(customerId);
        }

        /**
         * @param title customer property
         * @return builder
         */
        public Builder withTitle(String title) {
            result.setTitle(title);
            return this;
        }

        /**
         * @param lastName customer property
         * @return builder
         */
        public Builder withLastName(String lastName) {
            result.setLastName(lastName);
            return this;
        }

        /**
         * @param firstName customer property
         * @return builder
         */
        public Builder withFirstName(String firstName) {
            result.setFirstName(firstName);
            return this;
        }

        /**
         * @param zipCode address property
         * @return builder
         */
        public Builder withZipCode(String zipCode) {
            result.setZipCode(zipCode);
            return this;
        }

        /**
         * @param city address property
         * @return builder
         */
        public Builder withCity(String city) {
            result.setCity(city);
            return this;
        }

        /**
         * @param country address property
         * @return builder
         */
        public Builder withCountry(String country) {
            result.setCountry(country);
            return this;
        }

        /**
         * @param customerType customer property
         * @return builder
         */
        public Builder withCustomerType(String customerType) {
            result.setCustomerType(customerType);
            return this;
        }

        /**
         * @param dueInvoice customer property
         * @return builder
         */
        public Builder withDueInvoice(boolean dueInvoice) {
            result.setDueInvoice(dueInvoice);
            return this;
        }

        /**
         * @return dto with all information
         */
        public GeoBadPayerInfoDto build() {
            return result;
        }
    }
}
