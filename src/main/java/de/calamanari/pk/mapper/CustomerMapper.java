/*
 * Customer Mapper - demonstrates MAPPER pattern
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

import java.util.Date;
import java.util.logging.Logger;

import de.calamanari.pk.mapper.firstsys.Address;
import de.calamanari.pk.mapper.firstsys.Person;
import de.calamanari.pk.mapper.secondsys.Customer;
import de.calamanari.pk.util.MiscUtils;

/**
 * Customer Mapper - demonstrates MAPPER pattern<br>
 * In the first subsystem there exists a Person entity and an address entity.<br>
 * For some reason in a second subsystem a Customer entity exists including address information.<br>
 * This mapper is responsible for transparently mapping data between the two without giving one subsystem any knowledge
 * of the other one.<br>
 * Neither of the subsystems is aware of the mapper.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class CustomerMapper extends AbstractMapper {

    /**
     * logger
     */
    protected static final Logger LOGGER = Logger.getLogger(CustomerMapper.class.getName());

    /**
     * customer reference
     */
    private final Customer customer;

    /**
     * address reference
     */
    private final Address address;

    /**
     * person reference
     */
    private final Person person;

    /**
     * Creates new customer mapper for the two entities from the first subsystem and the customer entity from the second
     * subsystem
     * @param person the person to be mapped
     * @param address the address to be mapped
     * @param customer the customer to be mapped
     */
    public CustomerMapper(Person person, Address address, Customer customer) {
        LOGGER.fine(this.getClass().getSimpleName() + " created");
        this.person = person;
        this.address = address;
        this.customer = customer;
    }

    @Override
    public void map() {
        LOGGER.fine(this.getClass().getSimpleName() + ".map() called");

        LOGGER.fine("Mapping data from subsystem1 (Person+Address) to subsystem2 structure (Customer[customerId='"
                + customer.getCustomerId() + "'])");
        customer.setFirstName(person.getFirstName());
        customer.setLastName(person.getLastName());

        // a common task of a (data) mapper is to handle structural differences
        int customerLifeTimeDays = 0;
        Date firstOrderDate = person.getFirstOrderDate();
        if (firstOrderDate != null) {
            customerLifeTimeDays = MiscUtils.dayDiff(firstOrderDate, new Date(System.currentTimeMillis()));
        }
        customer.setCustomerLifeTimeDays(customerLifeTimeDays);

        customer.setStreet(address.getStreet());
        customer.setZipCode(address.getZipCode());
        customer.setCity(address.getCity());
    }

    @Override
    public void mapBack() {
        LOGGER.fine(this.getClass().getSimpleName() + ".mapBack() called");

        LOGGER.fine("Mapping data from subsystem2 (Customer[customerId='" + customer.getCustomerId()
                + "']) back to subsystem1 structures (Person+Address)");

        // here a merge strategy could be placed as there might
        // be scenarios allowing concurrent changes in both subsystems

        person.setFirstName(customer.getFirstName());
        person.setLastName(customer.getLastName());

        // in some cases not all information can/should be mapped back
        // here the customer lifetime cannot be changed

        address.setStreet(customer.getStreet());
        address.setZipCode(customer.getZipCode());
        address.setCity(customer.getCity());

    }

}
