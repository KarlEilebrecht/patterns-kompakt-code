//@formatter:off
/*
 * Customer Entity - the server entity (from persistence)
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
package de.calamanari.pk.datatransferobject.server;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.logging.Level;
import java.util.logging.Logger;

import de.calamanari.pk.datatransferobject.CustomerDto;
import de.calamanari.pk.datatransferobject.CustomerRemote;
import de.calamanari.pk.util.LogUtils;

/**
 * Customer Entity - the server entity (from persistence) <b>Note:</b> To better visualize the runtime differences between direct remoting and using DATA
 * TRANSFER OBJECT, CustomerEntity itself has been implemented as remote object, this is only for testing here and rather atypical!
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class CustomerEntity extends UnicastRemoteObject implements CustomerRemote {

    /**
     * logger
     */
    private static final Logger LOGGER = Logger.getLogger(CustomerEntity.class.getName());

    /**
     * for serialization
     */
    private static final long serialVersionUID = 944660653625665398L;

    static {
        LogUtils.setLogLevel(Level.FINE, CustomerEntity.class);
    }

    /**
     * Constructor
     * 
     * @throws RemoteException on communication error
     */
    public CustomerEntity() throws RemoteException {
        super();
    }

    /**
     * Creates new entity from the given data
     * 
     * @param customerId identifier
     * @param lastName person's last name
     * @param firstName person's first name
     * @param street address field
     * @param zipCode address field
     * @param city address field
     * @throws RemoteException on communication error
     */
    public CustomerEntity(String customerId, String lastName, String firstName, String street, String zipCode, String city) throws RemoteException {
        super();
        this.customerId = customerId;
        this.lastName = lastName;
        this.firstName = firstName;
        this.street = street;
        this.zipCode = zipCode;
        this.city = city;
    }

    /**
     * id of customer
     */
    private String customerId = null;

    /**
     * last name
     */
    private String lastName = null;

    /**
     * first name
     */
    private String firstName = null;

    /**
     * street
     */
    private String street = null;

    /**
     * Zip code
     */
    private String zipCode = null;

    /**
     * city
     */
    private String city = null;

    @Override
    public String getCustomerId() throws RemoteException {
        LOGGER.fine(this.getClass().getSimpleName() + ".getCustomerId() called");
        return this.customerId;
    }

    @Override
    public void setCustomerId(String customerId) throws RemoteException {
        LOGGER.fine(this.getClass().getSimpleName() + ".setCustomerId('" + customerId + "') called");
        this.customerId = customerId;
    }

    @Override
    public String getLastName() throws RemoteException {
        LOGGER.fine(this.getClass().getSimpleName() + ".getLastName() called");
        return this.lastName;
    }

    @Override
    public void setLastName(String lastName) throws RemoteException {
        LOGGER.fine(this.getClass().getSimpleName() + ".setLastName('" + lastName + "') called");
        this.lastName = lastName;
    }

    @Override
    public String getFirstName() throws RemoteException {
        LOGGER.fine(this.getClass().getSimpleName() + ".getFirstName() called");
        return this.firstName;
    }

    @Override
    public void setFirstName(String firstName) throws RemoteException {
        LOGGER.fine(this.getClass().getSimpleName() + ".setFirstName('" + firstName + "') called");
        this.firstName = firstName;
    }

    @Override
    public String getStreet() throws RemoteException {
        LOGGER.fine(this.getClass().getSimpleName() + ".getStreet() called");
        return this.street;
    }

    @Override
    public void setStreet(String street) throws RemoteException {
        LOGGER.fine(this.getClass().getSimpleName() + ".setStreet('" + street + "') called");
        this.street = street;
    }

    @Override
    public String getZipCode() throws RemoteException {
        LOGGER.fine(this.getClass().getSimpleName() + ".getZipCode() called");
        return this.zipCode;
    }

    @Override
    public void setZipCode(String zipCode) throws RemoteException {
        LOGGER.fine(this.getClass().getSimpleName() + ".setZipCode('" + zipCode + "') called");
        this.zipCode = zipCode;
    }

    @Override
    public String getCity() throws RemoteException {
        LOGGER.fine(this.getClass().getSimpleName() + ".getCity() called");
        return this.city;
    }

    @Override
    public void setCity(String city) throws RemoteException {
        LOGGER.fine(this.getClass().getSimpleName() + ".setCity('" + city + "') called");
        this.city = city;
    }

    /**
     * Returns the corresponding DATA TRANSFER OBJECT for this entity
     * 
     * @return dto
     */
    CustomerDto toDto() {
        LOGGER.fine(this.getClass().getSimpleName() + ".toDto() called");
        CustomerDto dto = new CustomerDto();
        dto.setCustomerId(customerId);
        dto.setLastName(lastName);
        dto.setFirstName(firstName);
        dto.setStreet(street);
        dto.setZipCode(zipCode);
        dto.setCity(city);
        return dto;
    }

    /**
     * Sets the fields of this entity from the given DATA TRANSFER OBJECT
     * 
     * @param dto data transfer object to copy data from
     */
    void fromDto(CustomerDto dto) {
        LOGGER.fine(this.getClass().getSimpleName() + ".fromDto(...) called");
        this.customerId = dto.getCustomerId();
        this.lastName = dto.getLastName();
        this.firstName = dto.getFirstName();
        this.street = dto.getStreet();
        this.zipCode = dto.getZipCode();
        this.city = dto.getCity();
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "({customerId=" + customerId + ", lastName=" + lastName + ", firstName=" + firstName + ", street=" + street
                + ", zipCode=" + zipCode + ", city=" + city + "})";
    }

}
