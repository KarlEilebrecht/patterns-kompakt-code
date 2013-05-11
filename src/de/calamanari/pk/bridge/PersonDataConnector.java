/*
 * Person Data Connector
 * Code-Beispiel zum Buch Patterns Kompakt, Verlag Springer Vieweg
 * Copyright 2013 Karl Eilebrecht
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
package de.calamanari.pk.bridge;

import java.util.logging.Logger;

/**
 * Person Data Connector - this is the service abstraction (root of a hierarchy) where we have "bridged-out" a parallel
 * hierarchy implementing core functionality.<br>
 * This shows the BRIDGE-pattern.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)web.de">Karl Eilebrecht</a>
 * 
 */
public class PersonDataConnector {

    /**
     * logger
     */
    protected static final Logger LOGGER = Logger.getLogger(PersonDataConnector.class.getName());

    /**
     * The reference to the corresponding PersonDataConnectorImp
     */
    protected PersonDataConnectorImp personDataConnectorImp;

    /**
     * Creates new PersonDataConnector using the given PersonDataConnectorImp
     * @param personDataConnectorImp implementation
     */
    public PersonDataConnector(PersonDataConnectorImp personDataConnectorImp) {
        LOGGER.fine("New " + this.getClass().getSimpleName() + " created and connected to other side of bridge ("
                + personDataConnectorImp.getClass().getSimpleName() + ").");
        this.personDataConnectorImp = personDataConnectorImp;
    }

    /**
     * Finds the person by id
     * @param personId identifier
     * @return person or null
     */
    public Person findPersonById(String personId) {
        LOGGER.fine(this.getClass().getSimpleName()
                + ".findPersonById() called and directly delegated to other side of bridge.");
        return personDataConnectorImp.findPersonById(personId);
    }

    /**
     * Returns the address for the given id
     * @param addressId address identifier
     * @return address id or null
     */
    public Address findAddressById(String addressId) {
        LOGGER.fine(this.getClass().getSimpleName()
                + ".findAddressById() called and directly delegated to other side of bridge.");
        return personDataConnectorImp.findAddressById(addressId);
    }

    /**
     * Returns the address of the specified person
     * @param personId identifier
     * @return address or null
     */
    public Address findAddressOfPersonById(String personId) {
        LOGGER.fine(this.getClass().getSimpleName()
                + ".findAddressOfPersonById() called and directly delegated to other side of bridge.");
        return personDataConnectorImp.findAddressOfPersonById(personId);
    }

    /**
     * Finds the corresponding person for the given address
     * @param addressId address identifier
     * @return person or null
     */
    public Person findPersonForAddressById(String addressId) {
        LOGGER.fine(this.getClass().getSimpleName() + ".findPersonForAddressById() called.");
        Address address = findAddressById(addressId);
        if (address != null) {
            return findPersonById(address.getPersonId());
        }
        else {
            return null;
        }
    }

    /**
     * Returns XY-person (including address) for given id
     * @param personId identifier
     * @return XY-person or null
     */
    public XyPerson findXyPersonById(String personId) {
        LOGGER.fine(this.getClass().getSimpleName() + ".findXyPersonById() called.");
        XyPerson xyPerson = null;
        Person person = findPersonById(personId);
        if (person != null) {
            xyPerson = new XyPerson(person.getId());
            xyPerson.setFirstName(person.getFirstName());
            xyPerson.setLastName(person.getLastName());
            xyPerson.setRole(person.getRole());
            Address address = findAddressOfPersonById(personId);
            if (address != null) {
                xyPerson.setAddressId(address.getId());
                xyPerson.setCity(address.getCity());
                xyPerson.setStreet(address.getStreet());
                xyPerson.setZipCode(address.getZipCode());
            }
        }
        return xyPerson;
    }

    /**
     * Checks whether the given person exists
     * @param personId identifier
     * @return true if person exists, otherwise false
     */
    public boolean checkPersonExistsById(String personId) {
        LOGGER.fine(this.getClass().getSimpleName() + ".checkPersonExistsById() called.");
        return (findPersonById(personId) != null);
    }

}
