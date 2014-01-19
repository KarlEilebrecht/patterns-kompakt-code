/*
 * Customer Manager - interface for customer persistence service
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
package de.calamanari.pk.datatransferobject;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Customer Manager - interface for customer persistence service, implemented by the customer manager server for finding
 * customers
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public interface CustomerManager extends Remote {

    /**
     * For testing, adds a customer to the manager's database
     * @param customerId identifier
     * @param lastName person's last name
     * @param firstName person's first name
     * @param street address field
     * @param zipCode address field
     * @param city address field
     * @throws RemoteException on communication error
     */
    public void addCustomer(String customerId, String lastName, String firstName, String street, String zipCode,
            String city) throws RemoteException;

    /**
     * Returns the remote interface for the customer entity
     * @param customerId identifier
     * @return entity or null if not found
     * @throws RemoteException on communication error
     */
    public Customer findCustomer(String customerId) throws RemoteException;

    /**
     * Returns the DATA TRANSFER OBJECT for the customer entity
     * @param customerId identifier
     * @return dto or null if not found
     * @throws RemoteException on communication error
     */
    public Customer findCustomerReturnDto(String customerId) throws RemoteException;

}
