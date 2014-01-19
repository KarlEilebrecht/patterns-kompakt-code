/*
 * HalCorp Person Data Connector Imp
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
package de.calamanari.pk.bridge.halcorp;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import de.calamanari.pk.bridge.Address;
import de.calamanari.pk.bridge.Person;
import de.calamanari.pk.bridge.PersonDataConnectorImp;

/**
 * HalCorp Person Data Connector Imp - a concrete member of the "bridged-out" hierarchy. In this example this is the
 * connector of the fictional HalCorp company.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class HalCorpPersonDataConnectorImp implements PersonDataConnectorImp {

    /**
     * logger
     */
    protected static final Logger LOGGER = Logger.getLogger(HalCorpPersonDataConnectorImp.class.getName());

    /**
     * begin ids with {@value} + 1
     */
    private static final int START_LAST_ID = 1000;
    
    /**
     * the "person database"
     */
    private Map<String, Person> personDb = new HashMap<>();

    /**
     * the "address database"
     */
    private Map<String, Address> addressDb = new HashMap<>();

    /**
     * for id creation
     */
    private int lastId = START_LAST_ID;

    /**
     * Creates new HalCorp-ID
     * @return new id
     */
    private String createNewId() {
        lastId++;
        return "HC" + lastId;
    }

    @Override
    public Person findPersonById(String personId) {
        LOGGER.fine(this.getClass().getSimpleName() + ".findPersonById() called on the other side of the bridge");
        return personDb.get(personId);
    }

    @Override
    public Address findAddressById(String addressId) {
        LOGGER.fine(this.getClass().getSimpleName() + ".findAddressById() called on the other side of the bridge");
        return addressDb.get(addressId);
    }

    @Override
    public Address findAddressOfPersonById(String personId) {
        LOGGER.fine(this.getClass().getSimpleName()
                + ".findAddressOfPersonById() called on the other side of the bridge");
        Address res = null;
        for (Address address : addressDb.values()) {
            if (personId != null && personId.equals(address.getPersonId())) {
                res = address;
            }
        }
        return res;
    }

    @Override
    public String createNewPerson(Person person) {
        LOGGER.fine(this.getClass().getSimpleName() + ".createNewPerson() called on the other side of the bridge");
        String newId = createNewId();
        person.setId(newId);
        this.personDb.put(newId, person);
        return newId;
    }

    @Override
    public String createNewAddress(Address address) {
        LOGGER.fine(this.getClass().getSimpleName() + ".createNewAddress() called on the other side of the bridge");
        String newId = createNewId();
        address.setId(newId);
        this.addressDb.put(newId, address);
        return newId;
    }

    @Override
    public void updatePerson(Person person) {
        LOGGER.fine(this.getClass().getSimpleName() + ".updatePerson() called on the other side of the bridge");
        if (this.personDb.containsKey(person.getId())) {
            this.personDb.put(person.getId(), person);
        }
        else {
            throw new RuntimeException("Unable to update " + person + " (no such entry).");
        }
    }

    @Override
    public void updateAddress(Address address) {
        LOGGER.fine(this.getClass().getSimpleName() + ".updateAddress() called on the other side of the bridge");
        if (this.addressDb.containsKey(address.getId())) {
            this.addressDb.put(address.getId(), address);
        }
        else {
            throw new RuntimeException("Unable to update " + address + " (no such entry).");
        }
    }

}
