/*
 * Customer Service - customer data access component
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
package de.calamanari.pk.transferobjectassembler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import de.calamanari.pk.util.MiscUtils;

/**
 * Customer Service - customer data access component<br>
 * In this example I was just too lazy :-) to implement real remoting here. <br>
 * Thus I decided to work with a network delay simulation.<br>
 * However, you can see a full-blown RMI-scenario in the DATA TRANSFER OBJECT example.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class CustomerService {

    /**
     * logger
     */
    protected static final Logger LOGGER = Logger.getLogger(CustomerService.class.getName());

    /**
     * some simulated network delay
     */
    private static final long NETWORK_DELAY = 30L;

    /**
     * utility method to simulate some delay
     */
    private static void simulateNetworkDelay() {
        MiscUtils.sleepIgnoreException(NETWORK_DELAY);
    }

    /**
     * Finds a customer by ID
     * @param customerId identifier
     * @return customer or null if not found
     */
    public CustomerDto findCustomerById(String customerId) {
        simulateNetworkDelay();
        LOGGER.fine(this.getClass().getSimpleName() + ".findCustomerById('" + customerId + "') called");
        CustomerEntity entity = Database.CUSTOMERS.get(customerId);
        return entity == null ? null : entity.toDto();
    }

    /**
     * Finds a customer address
     * @param customerId identifier
     * @return customer address or null if not found
     */
    public AddressDto findAddressByCustomerId(String customerId) {
        simulateNetworkDelay();
        LOGGER.fine(this.getClass().getSimpleName() + ".findAddressByCustomerId('" + customerId + "') called");
        AddressEntity entity = Database.ADDRESSES.get(customerId);
        return entity == null ? null : entity.toDto();
    }

    /**
     * Finds a customer dwh info
     * @param customerId identifier
     * @return customer dwh info or null if not found
     */
    public CustomerDwhInfoDto findDwhInfoByCustomerId(String customerId) {
        simulateNetworkDelay();
        LOGGER.fine(this.getClass().getSimpleName() + ".findDwhInfoByCustomerId('" + customerId + "') called");
        CustomerDwhInfoEntity entity = Database.CUSTOMER_DWH_INFOS.get(customerId);
        return entity == null ? null : entity.toDto();
    }

    /**
     * Returns dwh infos for all bad payers
     * @return list of dwh info DTOs, may be empty, never null
     */
    public List<CustomerDwhInfoDto> findBadPayerDwhInfos() {
        simulateNetworkDelay();
        LOGGER.fine(this.getClass().getSimpleName() + ".findBadPayerDwhInfos() called");
        List<CustomerDwhInfoDto> result = new ArrayList<>();
        for (CustomerDwhInfoEntity entity : Database.CUSTOMER_DWH_INFOS.values()) {
            if (entity.isBadPayer()) {
                CustomerDwhInfoDto dto = entity.toDto();
                LOGGER.fine("Adding " + dto.toString() + " to result");
                result.add(dto);
            }
        }
        Collections.sort(result, CustomerDwhInfoDto.BY_ID_COMPARATOR);
        return result;
    }

    /**
     * Returns a list of data transfer objects with information about bad payer customers
     * @return list of DTOs, may be empty, never null
     */
    public List<GeoBadPayerInfoDto> findGeoBadPayerInfos() {
        simulateNetworkDelay();
        LOGGER.fine(this.getClass().getSimpleName() + ".findBadPayerDwhInfos() called");
        List<GeoBadPayerInfoDto> result = new ArrayList<>();
        GeoBadPayerInfoDtoAssembler assembler = new GeoBadPayerInfoDtoAssembler();

        List<String> orderedCustomerIds = new ArrayList<>(Database.CUSTOMERS.keySet());
        Collections.sort(orderedCustomerIds);

        for (String customerId : orderedCustomerIds) {
            GeoBadPayerInfoDto info = assembler.assembleDto(customerId);
            if (info != null) {
                LOGGER.fine("Adding " + info.toString() + " to result");
                result.add(info);
            }
        }

        // By the way: Did you catch the design flaw in the code above?
        //
        // The problem here is that the filter (bad payer or not) is at the wrong level
        // (inside assembler.assembleDto()). This results in first loading ALL dwh infos before
        // checking whether a particular entity is relevant or not (waste of time and memory).
        //
        // Common mistakes like this often reach production and evolve there to real performance killers,
        // because the effect is only noticeable with large data volumes while tests are often
        // executed on small volumes. This is sometimes called "the n+1-problem", because everything
        // runs smoothly until you "add one more" ...
        //
        // Conclusion:
        // * Filtering should always happen as near as possible to the data source.
        // * Plan tests on large data volumes
        //
        // However, for the moment we pretend to have overlooked the problem ... :-)

        return result;
    }

}
