/*
 * Data Manager - supplementary class for MAPPER demonstration
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
package de.calamanari.pk.mapper;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import de.calamanari.pk.mapper.firstsys.Address;
import de.calamanari.pk.mapper.firstsys.Person;
import de.calamanari.pk.mapper.secondsys.Customer;

/**
 * Data Manager - supplementary class for MAPPER demonstration, this is rather a placeholder for some way to deal with
 * the database.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class DataManager {

    /**
     * logger
     */
    protected static final Logger LOGGER = Logger.getLogger(DataManager.class.getName());

    /**
     * simulated database table with persons
     */
    private Map<String, Person> persons = new HashMap<>();

    /**
     * simulated database table with addresses
     */
    private Map<String, Address> addresses = new HashMap<>();

    /**
     * utility method to fill "test database"
     * @param person instance of a person
     * @param address person's address
     */
    public void addPerson(Person person, Address address) {
        String id = person.getPersonId();
        this.persons.put(id, person);
        this.addresses.put(id, address);
    }

    /**
     * Finds the customer according to the given id and adds it to session management
     * @param session persistence session
     * @param customerId identifier
     * @return customer or null if not found
     */
    public Customer findCustomer(Session session, String customerId) {
        LOGGER.fine(this.getClass().getSimpleName() + ".findCustomer('" + customerId + "') called");

        Customer customer = null;
        Person person = persons.get(customerId);
        if (person != null) {
            Address address = addresses.get(customerId);
            LOGGER.fine("Person and Address found, preparing customer");
            customer = new Customer(customerId);
            CustomerMapper mapper = new CustomerMapper(person, address, customer);

            LOGGER.fine("Adding Mapper to session");
            session.add(mapper);

        }
        return customer;
    }

}
