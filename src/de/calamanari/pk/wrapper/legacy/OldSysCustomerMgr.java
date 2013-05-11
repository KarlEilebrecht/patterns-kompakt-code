/*
 * OldSys Customer Manager
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
package de.calamanari.pk.wrapper.legacy;

import java.util.logging.Logger;

/**
 * OldSys Customer Manager - part of the fictional legacy system, we want to create a WRAPPER for. <br>
 * This class provides access to the customer database.<br>
 * @author <a href="mailto:Karl.Eilebrecht(a/t)web.de">Karl Eilebrecht</a>
 */
public class OldSysCustomerMgr {

    /**
     * logger
     */
    protected static final Logger LOGGER = Logger.getLogger(OldSysCustomerMgr.class.getName());

    /**
     * Returns an array with customer record:<br>
     * <ul>
     * <li>key</li>
     * <li>value</li>
     * </ul>
     * @param customerId identifier
     * @return array with customer data or null if not found
     */
    public String[] getCustomerData(int customerId) {
        // internal legacy database access

        return null;
    }

    /**
     * Returns an array of the customerIds matching the search data:<br>
     * <ul>
     * <li>key</li>
     * <li>value</li>
     * </ul>
     * @param searchData array with customer data to search for
     * @return array of customerIds or empty array NEVER NULL
     */
    public int[] findCustomerByExample(String[] searchData) {
        return new int[0];
    }

}
