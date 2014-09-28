//@formatter:off
/*
 * Database - static placeholder for any kind of persistence in this example
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
package de.calamanari.pk.identitymap;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Database - static placeholder for any kind of persistence in this example
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public final class Database {

    /**
     * logger
     */
    protected static final Logger LOGGER = Logger.getLogger(Database.class.getName());

    /**
     * The "database table" for customers
     */
    public static final Map<String, CustomerEntity> CUSTOMERS = new HashMap<>();

    /**
     * The "database table" for customer addresses
     */
    public static final Map<String, AddressEntity> ADDRESSES = new HashMap<>();

    /**
     * Utility class
     */
    private Database() {
        // no instances
    }

    /**
     * For testing this allows to feed the "database"
     * 
     * @param customerId identifier
     * @param title person's title
     * @param lastName person's last name
     * @param firstName person's first name
     * @param phone telephone number
     * @param email email-address
     * @param promotionOptIn opt-in-flag for promotion events
     * @param addressId address field
     * @param street address field
     * @param zipCode address field
     * @param city address field
     * @param country address field
     * @param salutation address field
     */
    public static void addTestData(String customerId, String title, String lastName, String firstName, String phone, String email, boolean promotionOptIn,
            String addressId, String street, String zipCode, String city, String country, String salutation) {

        CustomerEntity customer = new CustomerEntity(customerId, title, lastName, firstName, phone, email, promotionOptIn);
        CUSTOMERS.put(customerId, customer);
        AddressEntity address = new AddressEntity(addressId, customerId, street, zipCode, city, country, salutation);
        ADDRESSES.put(addressId, address);

    }

}
