//@formatter:off
/*
 * Person Data Connector
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Person Data Connector - this is the service abstraction (root of a hierarchy) where we have "bridged-out" a parallel hierarchy implementing core
 * functionality.<br>
 * This shows the BRIDGE-pattern.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 * 
 */
public class PersonDataConnector {

    private static final Logger LOGGER = LoggerFactory.getLogger(PersonDataConnector.class);

    /**
     * The reference to the corresponding PersonDataConnectorImp
     */
    protected PersonDataConnectorImp personDataConnectorImp;

    /**
     * Creates new PersonDataConnector using the given PersonDataConnectorImp
     * 
     * @param personDataConnectorImp implementation
     */
    public PersonDataConnector(PersonDataConnectorImp personDataConnectorImp) {
        LOGGER.debug("New {} created and connected to other side of bridge ({}).", this.getClass().getSimpleName(),
                personDataConnectorImp.getClass().getSimpleName());
        this.personDataConnectorImp = personDataConnectorImp;
    }

    /**
     * Finds the person by id
     * 
     * @param personId identifier
     * @return person or null
     */
    public Person findPersonById(String personId) {
        LOGGER.debug("{}.findPersonById() called and directly delegated to other side of bridge.", this.getClass().getSimpleName());
        return personDataConnectorImp.findPersonById(personId);
    }

    /**
     * Returns the address for the given id
     * 
     * @param addressId address identifier
     * @return address id or null
     */
    public Address findAddressById(String addressId) {
        LOGGER.debug("{}.findAddressById() called and directly delegated to other side of bridge.", this.getClass().getSimpleName());
        return personDataConnectorImp.findAddressById(addressId);
    }

    /**
     * Returns the address of the specified person
     * 
     * @param personId identifier
     * @return address or null
     */
    public Address findAddressOfPersonById(String personId) {
        LOGGER.debug("{}.findAddressOfPersonById() called and directly delegated to other side of bridge.", this.getClass().getSimpleName());
        return personDataConnectorImp.findAddressOfPersonById(personId);
    }

    /**
     * Finds the corresponding person for the given address
     * 
     * @param addressId address identifier
     * @return person or null
     */
    public Person findPersonForAddressById(String addressId) {
        LOGGER.debug("{}.findPersonForAddressById() called.", this.getClass().getSimpleName());
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
     * 
     * @param personId identifier
     * @return XY-person or null
     */
    public XyPerson findXyPersonById(String personId) {
        LOGGER.debug("{}.findXyPersonById() called.", this.getClass().getSimpleName());
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
     * 
     * @param personId identifier
     * @return true if person exists, otherwise false
     */
    public boolean checkPersonExistsById(String personId) {
        LOGGER.debug("{}.checkPersonExistsById() called.", this.getClass().getSimpleName());
        return (findPersonById(personId) != null);
    }

}
