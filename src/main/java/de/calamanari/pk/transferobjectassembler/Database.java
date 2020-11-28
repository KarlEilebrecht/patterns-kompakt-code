//@formatter:off
/*
 * Database - static placeholder for any kind of persistence in this example
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Database - static placeholder for any kind of persistence in this example
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public final class Database {

    /**
     * The "database table" for customers
     */
    static final Map<String, CustomerEntity> CUSTOMERS = new HashMap<>();

    /**
     * The "database table" for customer addresses
     */
    static final Map<String, AddressEntity> ADDRESSES = new HashMap<>();

    /**
     * The "database table" for dwh data
     */
    static final Map<String, CustomerDwhInfoEntity> CUSTOMER_DWH_INFOS = new HashMap<>();

    /**
     * Utility class
     */
    private Database() {
        // no instances
    }

    /**
     * @param customerId mandatory
     * @return BUILDER instance with customerId set
     */
    public static Builder prepareCustomer(String customerId) {
        return new Builder(customerId);
    }

    /**
     * Builder to avoid huge constructor for setting up our "database"
     */
    public static class Builder {

        private final CustomerEntity customerEntity;
        private final AddressEntity addressEntity;
        private final CustomerDwhInfoEntity.Builder customerBuilder;

        /**
         * initializes the builder (customer/address) for the given id
         * @param customerId mandatory
         */
        Builder(String customerId) {
            this.customerEntity = new CustomerEntity();
            customerEntity.setCustomerId(customerId);
            this.addressEntity = new AddressEntity();
            addressEntity.setCustomerId(customerId);
            this.customerBuilder = CustomerDwhInfoEntity.forCustomerId(customerId);
        }

        /**
         * @param title customer property
         * @return builder
         */
        public Builder withTitle(String title) {
            customerEntity.setTitle(title);
            return this;
        }

        /**
         * @param lastName customer property
         * @return builder
         */
        public Builder withLastName(String lastName) {
            customerEntity.setLastName(lastName);
            return this;
        }

        /**
         * @param firstName customer property
         * @return builder
         */
        public Builder withFirstName(String firstName) {
            customerEntity.setFirstName(firstName);
            return this;
        }

        /**
         * @param phone customer property
         * @return builder
         */
        public Builder withPhone(String phone) {
            customerEntity.setPhone(phone);
            return this;
        }

        /**
         * @param email customer property
         * @return builder
         */
        public Builder withEmail(String email) {
            customerEntity.setEmail(email);
            return this;
        }

        /**
         * @param promotionOptIn customer property
         * @return builder
         */
        public Builder withPromotionOptIn(boolean promotionOptIn) {
            customerEntity.setPromotionOptIn(promotionOptIn);
            return this;
        }

        /**
         * @param addressId address property
         * @return builder
         */
        public Builder withAddressId(String addressId) {
            addressEntity.setAddressId(addressId);
            return this;
        }

        /**
         * @param street address property
         * @return builder
         */
        public Builder withStreet(String street) {
            addressEntity.setStreet(street);
            return this;
        }

        /**
         * @param zipCode address property
         * @return builder
         */
        public Builder withZipCode(String zipCode) {
            addressEntity.setZipCode(zipCode);
            return this;
        }

        /**
         * @param city address property
         * @return builder
         */
        public Builder withCity(String city) {
            addressEntity.setCity(city);
            return this;
        }

        /**
         * @param country address property
         * @return builder
         */
        public Builder withCountry(String country) {
            addressEntity.setCountry(country);
            return this;
        }

        /**
         * @param salutation address property
         * @return builder
         */
        public Builder withSalutation(String salutation) {
            addressEntity.setSalutation(salutation);
            return this;
        }

        /**
         * @param customerType customer identifier
         * @return builder
         */
        public Builder withCustomerType(String customerType) {
            customerBuilder.withCustomerType(customerType);
            return this;
        }

        /**
         * @param scorePoints customer property
         * @return builder
         */
        public Builder withScorePoints(int scorePoints) {
            customerBuilder.withScorePoints(scorePoints);
            return this;
        }

        /**
         * @param firstOrderDateISO as 'yyyy-MM-dd' or null
         * @return builder
         */
        public Builder withFirstOrderDate(String firstOrderDateISO) {
            Date firstOrderDate = parseOrderDate(firstOrderDateISO);
            customerBuilder.withFirstOrderDate(firstOrderDate);
            return this;
        }

        /**
         * @param lastOrderDateISO as 'yyyy-MM-dd' or null
         * @return builder
         */
        public Builder withLastOrderDate(String lastOrderDateISO) {
            Date lastOrderDate = parseOrderDate(lastOrderDateISO);
            customerBuilder.withLastOrderDate(lastOrderDate);
            return this;
        }

        /**
         * @param dueInvoice customer property
         * @return builder
         */
        public Builder withDueInvoice(boolean dueInvoice) {
            customerBuilder.withDueInvoice(dueInvoice);
            return this;
        }

        /**
         * @param fraudSuspicion customer property
         * @return builder
         */
        public Builder withFraudSuspicion(boolean fraudSuspicion) {
            customerBuilder.withFraudSuspicion(fraudSuspicion);
            return this;
        }

        /**
         * @param badPayer customer property
         * @return builder
         */
        public Builder withBadPayer(boolean badPayer) {
            customerBuilder.withBadPayer(badPayer);
            return this;
        }

        /**
         * Adds the customer with all information to the database
         */
        public void commit() {
            CUSTOMERS.put(customerEntity.getCustomerId(), customerEntity);
            ADDRESSES.put(customerEntity.getCustomerId(), addressEntity);
            CUSTOMER_DWH_INFOS.put(customerEntity.getCustomerId(), customerBuilder.build());
        }

        /**
         * Parses the order date from String to Date
         * 
         * @param orderDateISO 'yyyy-MM-dd' or null
         * @return parsed date, can be null
         * @throws RuntimeException if the date was in wrong format
         */
        private Date parseOrderDate(String orderDateISO) {
            Date orderDate = null;
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                orderDate = (orderDateISO == null ? null : sdf.parse(orderDateISO));
            }
            catch (ParseException ex) {
                throw new DatabaseException("Error parsing orderDateISO=" + orderDateISO, ex);
            }
            return orderDate;
        }

    }
}
