/*
 * Geo Bad Payer Info DTO Assembler - the TRANSFER OBJECT ASSEMBLER
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
package de.calamanari.pk.transferobjectassembler;

import java.util.logging.Logger;

/**
 * Geo Bad Payer Info DTO Assembler - the TRANSFER OBJECT ASSEMBLER<br>
 * The knowlegde which entities' data to query and which particular attributes to include has been centralized here.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)web.de">Karl Eilebrecht</a>
 */
public class GeoBadPayerInfoDtoAssembler {

    /**
     * logger
     */
    protected static final Logger LOGGER = Logger.getLogger(GeoBadPayerInfoDtoAssembler.class.getName());

    /**
     * Creates new data transfer object for the customer
     * @param customerId identifier
     * @return data transfer object or null if customer not found or no bad payer
     */
    public GeoBadPayerInfoDto assembleDto(String customerId) {

        LOGGER.fine(this.getClass().getSimpleName() + ".assembleDto('" + customerId + "' called");

        GeoBadPayerInfoDto res = null;
        CustomerDwhInfoEntity dwhInfoEntity = Database.CUSTOMER_DWH_INFOS.get(customerId);

        if (dwhInfoEntity != null) {
            CustomerEntity customerEntity = Database.CUSTOMERS.get(customerId);
            AddressEntity addressEntity = Database.ADDRESSES.get(customerId);

            if (dwhInfoEntity.isBadPayer()) {
                if (customerEntity != null && addressEntity != null) {
                    LOGGER.fine("Creating data transfer object from entities customer, address and dwh information");
                    res = new GeoBadPayerInfoDto(customerEntity.getCustomerId(), customerEntity.getTitle(),
                            customerEntity.getLastName(), customerEntity.getFirstName(), addressEntity.getZipCode(),
                            addressEntity.getCity(), addressEntity.getCountry(), dwhInfoEntity.getCustomerType(),
                            dwhInfoEntity.isDueInvoice());
                }
            }
        }
        if (res == null) {
            LOGGER.fine("No data transfer object created.");
        }
        return res;
    }

}
