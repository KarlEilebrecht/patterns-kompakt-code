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

    public static Builder prepareCustomer(String customerId) {
        return new Builder(customerId);
    }

    public static class Builder {

        private final CustomerEntity customerEntity = new CustomerEntity();

        private final AddressEntity addressEntity = new AddressEntity();

        Builder(String customerId) {
            customerEntity.setId(customerId);
            addressEntity.setCustomerId(customerId);
        }

        public Builder withTitle(String title) {
            customerEntity.setTitle(title);
            return this;
        }

        public Builder withLastName(String lastName) {
            customerEntity.setLastName(lastName);
            return this;
        }

        public Builder withFirstName(String firstName) {
            customerEntity.setFirstName(firstName);
            return this;
        }

        public Builder withPhone(String phone) {
            customerEntity.setPhone(phone);
            return this;
        }

        public Builder withEmail(String email) {
            customerEntity.setEmail(email);
            return this;
        }

        public Builder withPromotionOptIn(boolean promotionOptIn) {
            customerEntity.setPromotionOptIn(promotionOptIn);
            return this;
        }

        public Builder withAddressId(String addressId) {
            addressEntity.setAddressId(addressId);
            return this;
        }

        public Builder withStreet(String street) {
            addressEntity.setStreet(street);
            return this;
        }

        public Builder withZipCode(String zipCode) {
            addressEntity.setZipCode(zipCode);
            return this;
        }

        public Builder withCity(String city) {
            addressEntity.setCity(city);
            return this;
        }

        public Builder withCountry(String country) {
            addressEntity.setCountry(country);
            return this;
        }

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
