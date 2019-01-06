//@formatter:off
/*
 * Geo Bad Payer Info DTO Assembler - the TRANSFER OBJECT ASSEMBLER
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
package de.calamanari.pk.transferobjectassembler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Geo Bad Payer Info DTO Assembler - the TRANSFER OBJECT ASSEMBLER<br>
 * The knowlegde which entities' data to query and which particular attributes to include has been centralized here.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class GeoBadPayerInfoDtoAssembler {

    private static final Logger LOGGER = LoggerFactory.getLogger(GeoBadPayerInfoDtoAssembler.class);

    /**
     * Creates new data transfer object for the customer
     * 
     * @param customerId identifier
     * @return data transfer object or null if customer not found or no bad payer
     */
    public GeoBadPayerInfoDto assembleDto(String customerId) {

        LOGGER.debug("{}.assembleDto('{}' called", this.getClass().getSimpleName(), customerId);

        GeoBadPayerInfoDto res = null;
        CustomerDwhInfoEntity dwhInfoEntity = Database.CUSTOMER_DWH_INFOS.get(customerId);

        if (dwhInfoEntity != null) {
            CustomerEntity customerEntity = Database.CUSTOMERS.get(customerId);
            AddressEntity addressEntity = Database.ADDRESSES.get(customerId);

            if (dwhInfoEntity.isBadPayer() && customerEntity != null && addressEntity != null) {
                LOGGER.debug("Creating data transfer object from entities customer, address and dwh information");

                // @formatter:off
                res = GeoBadPayerInfoDto.forCustomer(customerEntity.getCustomerId())
                            .withTitle(customerEntity.getTitle())
                            .withLastName(customerEntity.getLastName())
                            .withFirstName(customerEntity.getFirstName())
                            .withZipCode(addressEntity.getZipCode())          
                            .withCity(addressEntity.getCity())
                            .withCountry(addressEntity.getCountry())
                            .withCustomerType(dwhInfoEntity.getCustomerType())
                            .withDueInvoice(dwhInfoEntity.isDueInvoice())
                      .build();
                // @formatter:on

            }
        }
        if (res == null) {
            LOGGER.debug("No data transfer object created.");
        }
        return res;
    }

}
