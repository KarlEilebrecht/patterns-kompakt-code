/*
 * Data Manager - provides finder methods in this example
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
package de.calamanari.pk.identitymap;

import java.util.logging.Logger;

/**
 * Data Manager - provides finder methods in this example, placeholder for whatever persistence framework/strategies may
 * be in use. <br>
 * If DAOs come into play, you would have to decide where to place the calls to the identity map. On the one hand
 * logically the lookup belongs ON TOP of the DAO layer, introducing an extra layer (yuck!).<br>
 * On the other hand INSIDE DAO would be convenient to reduce lines of code.<br>
 * But with different DAOs (for XML, Oracle, MySQL), which is the primary reason for implementing a DAO-layer, the
 * identity map lookup code inside the DAO would have to be duplicated (yuck! again).<br>
 * One solution to this dilemma could be an abstract DAO base class containing a generic identity map lookup procedure.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public final class DataManager {

    /**
     * logger
     */
    protected static final Logger LOGGER = Logger.getLogger(DataManager.class.getName());

    /**
     * Utility class
     */
    private DataManager() {
        // no instances
    }
    
    /**
     * Customer finder method
     * @param customerId identifier
     * @return customer entity or null if not found
     */
    public static CustomerEntity findCustomerById(String customerId) {

        LOGGER.fine(DataManager.class.getSimpleName() + ".findCustomerById('" + customerId + "') called");

        CustomerEntity res = null;

        Session session = Session.getCurrentSession();

        LOGGER.fine("Getting Identity Map for Type: " + CustomerEntity.class.getSimpleName());

        IdentityMap<String, CustomerEntity> identityMap = session.getIdentityMap(CustomerEntity.class);

        LOGGER.fine("Performing lookup on " + identityMap.toString() + "");

        res = identityMap.get(customerId);

        if (res == null) {
            LOGGER.fine("Instance not in " + identityMap.toString() + " yet!");
            res = findCustomerByIdInDatabase(customerId);
            LOGGER.fine("Putting Instance @" + Integer.toHexString(res.hashCode()) + " into " + identityMap.toString());
            identityMap.add(res);
        }
        else {
            LOGGER.fine("Instance found: @" + Integer.toHexString(res.hashCode()));
        }

        return res;
    }

    /**
     * loads the entity from the persistence layer
     * @param customerId identifier
     * @return entity or null if not found
     */
    private static CustomerEntity findCustomerByIdInDatabase(String customerId) {

        LOGGER.fine(DataManager.class.getSimpleName() + ".findCustomerByIdInDatabase('" + customerId + "') called");

        CustomerEntity entity = Database.CUSTOMERS.get(customerId);

        // The database must always returns a fresh entity, otherwise the example was a little useless :-)
        return (CustomerEntity) entity.clone();

    }

    // Note:
    // the code could be written more generic (the code below is almost the same as above),
    // but for demonstration purposes (logging, tracing) it's easier to duplicate the lines

    /**
     * Finder method for addresses
     * @param addressId identifier
     * @return address entity or null if not found
     */
    public static AddressEntity findAddressById(String addressId) {

        LOGGER.fine(DataManager.class.getSimpleName() + ".findAddressById('" + addressId + "') called");

        AddressEntity res = null;

        Session session = Session.getCurrentSession();

        LOGGER.fine("Getting Identity Map for Type: " + AddressEntity.class.getSimpleName());

        IdentityMap<String, AddressEntity> identityMap = session.getIdentityMap(AddressEntity.class);

        LOGGER.fine("Performing lookup on " + identityMap.toString() + "");

        res = identityMap.get(addressId);

        if (res == null) {
            LOGGER.fine("Instance not in " + identityMap.toString() + " yet!");
            res = findAddressByIdInDatabase(addressId);
            LOGGER.fine("Putting Instance @" + Integer.toHexString(res.hashCode()) + " into " + identityMap.toString());
            identityMap.add(res);
        }
        else {
            LOGGER.fine("Instance found: @" + Integer.toHexString(res.hashCode()));
        }

        return res;
    }

    /**
     * loads the entity from the persistence layer
     * @param addressId identifier
     * @return entity or null if not found
     */
    private static AddressEntity findAddressByIdInDatabase(String addressId) {

        LOGGER.fine(DataManager.class.getSimpleName() + ".findAddressByIdInDatabase('" + addressId + "') called");

        AddressEntity entity = Database.ADDRESSES.get(addressId);

        // The database must always returns a fresh entity, otherwise the example was a little useless :-)
        return (AddressEntity) entity.clone();
    }

}
