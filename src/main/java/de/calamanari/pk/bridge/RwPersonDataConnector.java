//@formatter:off
/*
 * Rw Person Data Connector
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
package de.calamanari.pk.bridge;

import java.util.logging.Logger;

/**
 * Rw Person Data Connector - this is a subclass in the main hierarchy, which evolves independently from the "bridged-out" hierarchy.<br>
 * In this example the subclass adds additional behavior based on the super class and the "bridged-out" core functionality.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class RwPersonDataConnector extends PersonDataConnector {

    /**
     * logger
     */
    protected static final Logger LOGGER = Logger.getLogger(RwPersonDataConnector.class.getName());

    /**
     * Creates new RwPersonDataConnector
     * 
     * @param personDataConnectorImp implementation
     */
    public RwPersonDataConnector(PersonDataConnectorImp personDataConnectorImp) {
        super(personDataConnectorImp);
    }

    /**
     * Creates or updates the given person and returns the person id, which will also be set to the person
     * 
     * @param person instance to be created
     * @return person id
     */
    public String createOrUpdateXyPerson(XyPerson person) {
        LOGGER.fine(this.getClass().getSimpleName() + ".createOrUpdateXyPerson() called.");
        if (checkPersonExistsById(person.getId())) {
            Person person1 = new Person(person.getId(), person.getFirstName(), person.getLastName(), person.getRole());
            personDataConnectorImp.updatePerson(person1);
            Address address = findAddressById(person.getAddressId());
            if (address != null) {
                updateAddress(address, person);
            }
            else {
                createAddress(person);
            }
        }
        else {
            Person person1 = new Person(null, person.getFirstName(), person.getLastName(), person.getRole());
            String personId = personDataConnectorImp.createNewPerson(person1);
            person.setId(personId);
            createAddress(person);
        }
        return person.getId();
    }

    /**
     * Creates an address object with the field values from the given person and sets the new addressId at the person
     * 
     * @param person instance to create address for
     */
    private void createAddress(XyPerson person) {
        Address address = new Address();
        address.setPersonId(person.getId());
        address.setCity(person.getCity());
        address.setStreet(person.getStreet());
        address.setZipCode(person.getZipCode());
        String addressId = personDataConnectorImp.createNewAddress(address);
        person.setAddressId(addressId);
    }

    /**
     * Updates the address object with field values from the the person
     * 
     * @param address instance to be updated
     * @param person source data
     */
    private void updateAddress(Address address, XyPerson person) {
        address.setCity(person.getCity());
        address.setPersonId(person.getId());
        address.setStreet(person.getStreet());
        address.setZipCode(person.getZipCode());
        personDataConnectorImp.updateAddress(address);
    }

}
