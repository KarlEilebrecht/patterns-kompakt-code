//@formatter:off
/*
 * Person Data Connector Imp
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
package de.calamanari.pk.bridge;

/**
 * Person Data Connector Imp - interface to be provided by concrete PersonDataConnectorImps. <br>
 * These classes build a parallel hierarchy (core functionality) "bridged-out" from the PersonDataConnector hierarchy.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public interface PersonDataConnectorImp {

    /**
     * Finds the person by id
     * 
     * @param personId identifier
     * @return person or null
     */
    public Person findPersonById(String personId);

    /**
     * Returns the address for the given id
     * 
     * @param addressId address identifier
     * @return address id or null
     */
    public Address findAddressById(String addressId);

    /**
     * Returns the address of the specified person
     * 
     * @param personId identifier
     * @return address or null
     */
    public Address findAddressOfPersonById(String personId);

    /**
     * Creates a new person and returns the new id.
     * 
     * @param person to be created
     * @return person Id
     */
    public String createNewPerson(Person person);

    /**
     * Creates a new address and returns the new id.
     * 
     * @param address to be created
     * @return address id
     */
    public String createNewAddress(Address address);

    /**
     * Updates the person.
     * 
     * @param person to be updated
     */
    public void updatePerson(Person person);

    /**
     * Updates the address.
     * 
     * @param address to be updated
     */
    public void updateAddress(Address address);

}
