/*
 * Product Manager - demonstrates COMBINED METHOD
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
package de.calamanari.pk.combinedmethod;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Product Manager - interface provided by the remote product manager server, originally with two methods to first
 * acquire a new id and than register the product. As always the reason for this strange interface is legacy behavior.
 * :-) <br>
 * A new COMBINED METHOD was introduced to combine the two steps into one transactional step to avoid inconsistencies in
 * case of error.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public interface ProductManager extends Remote {

    /**
     * Returns a new product-ID, the ID must be acquired before registering a new product
     * @return acquired product identifier
     * @throws RemoteException on remoting error
     */
    public String acquireProductId() throws RemoteException;

    /**
     * Registers a new product
     * @param product the product to be registered, including a previously registered id
     * @throws RemoteException on remoting error
     */
    public void registerProduct(Product product) throws RemoteException;

    /**
     * Finds the product and returns it
     * @param id the product's id
     * @return product or null if not found
     * @throws RemoteException on remoting error
     */
    public Product findProductById(String id) throws RemoteException;

    /**
     * COMBINED METHOD to acquire id and then register the product in one step
     * @param product without id
     * @return product including id from remote system
     * @throws RemoteException on remoting error
     */
    public Product combinedCreateAndRegisterProduct(Product product) throws RemoteException;

    /**
     * for testing, simulates error on next call
     * @throws RemoteException on remoting error
     */
    public void setNextProductRegistrationMustFail() throws RemoteException;

    /**
     * for testing, resets state
     * @throws RemoteException on remoting error
     */
    public void reset() throws RemoteException;

}
