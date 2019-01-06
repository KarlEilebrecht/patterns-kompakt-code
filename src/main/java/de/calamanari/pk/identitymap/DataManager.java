//@formatter:off
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
//@formatter:on
package de.calamanari.pk.identitymap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Data Manager - provides finder methods in this example, placeholder for whatever persistence framework/strategies may be in use. <br>
 * If DAOs come into play, you would have to decide where to place the calls to the identity map. On the one hand logically the lookup belongs ON TOP of the DAO
 * layer, introducing an extra layer (yuck!).<br>
 * On the other hand INSIDE DAO would be convenient to reduce lines of code.<br>
 * But with different DAOs (for XML, Oracle, MySQL), which is the primary reason for implementing a DAO-layer, the identity map lookup code inside the DAO would
 * have to be duplicated (yuck! again).<br>
 * One solution to this dilemma could be an abstract DAO base class containing a generic identity map lookup procedure.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public final class DataManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(DataManager.class);

    /**
     * Utility class
     */
    private DataManager() {
        // no instances
    }

    /**
     * Customer finder method
     * 
     * @param customerId identifier
     * @return customer entity or null if not found
     */
    public static CustomerEntity findCustomerById(String customerId) {

        LOGGER.debug("{}.findCustomerById('{}') called", DataManager.class.getSimpleName(), customerId);

        CustomerEntity res = null;

        Session session = Session.getCurrentSession();

        LOGGER.debug("Getting Identity Map for Type: {}", CustomerEntity.class.getSimpleName());

        IdentityMap<String, CustomerEntity> identityMap = session.getIdentityMap(CustomerEntity.class);

        LOGGER.debug("Performing lookup on {}", identityMap);

        res = identityMap.get(customerId);

        if (res == null) {
            LOGGER.debug("Instance not in {} yet!", identityMap);
            res = findCustomerByIdInDatabase(customerId);
            LOGGER.debug("Putting Instance @{} into {}", Integer.toHexString(res.hashCode()), identityMap);
            identityMap.add(res);
        }
        else {
            LOGGER.debug("Instance found: @{}", Integer.toHexString(res.hashCode()));
        }

        return res;
    }

    /**
     * loads the entity from the persistence layer
     * 
     * @param customerId identifier
     * @return entity or null if not found
     */
    private static CustomerEntity findCustomerByIdInDatabase(String customerId) {

        LOGGER.debug("{}.findCustomerByIdInDatabase('{}') called", DataManager.class.getSimpleName(), customerId);

        CustomerEntity entity = Database.CUSTOMERS.get(customerId);

        // The database must always returns a fresh entity, otherwise the example was a little useless :-)
        return (CustomerEntity) entity.clone();

    }

    // Note:
    // the code could be written more generic (the code below is almost the same as above),
    // but for demonstration purposes (logging, tracing) it's easier to duplicate the lines

    /**
     * Finder method for addresses
     * 
     * @param addressId identifier
     * @return address entity or null if not found
     */
    public static AddressEntity findAddressById(String addressId) {

        LOGGER.debug("{}.findAddressById('{}') called", DataManager.class.getSimpleName(), addressId);

        AddressEntity res = null;

        Session session = Session.getCurrentSession();

        LOGGER.debug("Getting Identity Map for Type: {}", AddressEntity.class.getSimpleName());

        IdentityMap<String, AddressEntity> identityMap = session.getIdentityMap(AddressEntity.class);

        LOGGER.debug("Performing lookup on {}", identityMap);

        res = identityMap.get(addressId);

        if (res == null) {
            LOGGER.debug("Instance not in {} yet!", identityMap);
            res = findAddressByIdInDatabase(addressId);
            LOGGER.debug("Putting Instance @{} into {}", Integer.toHexString(res.hashCode()), identityMap);
            identityMap.add(res);
        }
        else {
            LOGGER.debug("Instance found: @{}", Integer.toHexString(res.hashCode()));
        }

        return res;
    }

    /**
     * loads the entity from the persistence layer
     * 
     * @param addressId identifier
     * @return entity or null if not found
     */
    private static AddressEntity findAddressByIdInDatabase(String addressId) {

        LOGGER.debug("{}.findAddressByIdInDatabase('{}') called", DataManager.class.getSimpleName(), addressId);

        AddressEntity entity = Database.ADDRESSES.get(addressId);

        // The database must always returns a fresh entity, otherwise the example was a little useless :-)
        return (AddressEntity) entity.clone();
    }

}
