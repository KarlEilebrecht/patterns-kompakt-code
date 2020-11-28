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
package de.calamanari.pk.identitymap;

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
     * BUILDER for easier setup of a our fake {@link Database}
     */
    public static class Builder {

        private final CustomerEntity customerEntity = new CustomerEntity();

        private final AddressEntity addressEntity = new AddressEntity();

        /**
         * initializes the builder (customer/address) for the given id
         * @param customerId mandatory
         */
        Builder(String customerId) {
            customerEntity.setId(customerId);
            addressEntity.setCustomerId(customerId);
        }

        /**
         * @param title customer property
         * @return this builder
         */
        public Builder withTitle(String title) {
            customerEntity.setTitle(title);
            return this;
        }

        /**
         * @param lastName customer property
         * @return this builder
         */
        public Builder withLastName(String lastName) {
            customerEntity.setLastName(lastName);
            return this;
        }

        /**
         * @param firstName customer property
         * @return this builder
         */
        public Builder withFirstName(String firstName) {
            customerEntity.setFirstName(firstName);
            return this;
        }

        /**
         * @param phone customer property
         * @return this builder
         */
        public Builder withPhone(String phone) {
            customerEntity.setPhone(phone);
            return this;
        }

        /**
         * @param email customer property
         * @return this builder
         */
        public Builder withEmail(String email) {
            customerEntity.setEmail(email);
            return this;
        }

        /**
         * @param promotionOptIn customer property
         * @return this builder
         */
        public Builder withPromotionOptIn(boolean promotionOptIn) {
            customerEntity.setPromotionOptIn(promotionOptIn);
            return this;
        }

        /**
         * @param addressId address identifier
         * @return this builder
         */
        public Builder withAddressId(String addressId) {
            addressEntity.setAddressId(addressId);
            return this;
        }

        /**
         * @param street address property
         * @return this builder
         */
        public Builder withStreet(String street) {
            addressEntity.setStreet(street);
            return this;
        }

        /**
         * @param zipCode address property
         * @return this builder
         */
        public Builder withZipCode(String zipCode) {
            addressEntity.setZipCode(zipCode);
            return this;
        }

        /**
         * @param city address property
         * @return this builder
         */
        public Builder withCity(String city) {
            addressEntity.setCity(city);
            return this;
        }

        /**
         * @param country address property
         * @return this builder
         */
        public Builder withCountry(String country) {
            addressEntity.setCountry(country);
            return this;
        }

        /**
         * @param salutation address property
         * @return this builder
         */
        public Builder withSalutation(String salutation) {
            addressEntity.setSalutation(salutation);
            return this;
        }

        /**
         * Adds the customer with all information to the database
         */
        public void commit() {
            CUSTOMERS.put(customerEntity.getId(), customerEntity);
            ADDRESSES.put(addressEntity.getId(), addressEntity);
        }
    }
}
