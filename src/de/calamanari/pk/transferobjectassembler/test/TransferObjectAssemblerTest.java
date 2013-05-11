/*
 * Transfer Object Assembler Test - demonstrates TRANSFER OBJECT ASSEMBLER pattern.
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
package de.calamanari.pk.transferobjectassembler.test;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.BeforeClass;
import org.junit.Test;

import de.calamanari.pk.transferobjectassembler.AddressDto;
import de.calamanari.pk.transferobjectassembler.AddressEntity;
import de.calamanari.pk.transferobjectassembler.CustomerDto;
import de.calamanari.pk.transferobjectassembler.CustomerDwhInfoDto;
import de.calamanari.pk.transferobjectassembler.CustomerDwhInfoEntity;
import de.calamanari.pk.transferobjectassembler.CustomerEntity;
import de.calamanari.pk.transferobjectassembler.CustomerService;
import de.calamanari.pk.transferobjectassembler.Database;
import de.calamanari.pk.transferobjectassembler.GeoBadPayerInfoDto;
import de.calamanari.pk.transferobjectassembler.GeoBadPayerInfoDtoAssembler;
import de.calamanari.pk.util.LogUtils;
import de.calamanari.pk.util.MiscUtils;

/**
 * Transfer Object Assembler Test - demonstrates TRANSFER OBJECT ASSEMBLER pattern.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)web.de">Karl Eilebrecht</a>
 */
public class TransferObjectAssemblerTest {

    /**
     * logger
     */
    protected static final Logger LOGGER = Logger.getLogger(TransferObjectAssemblerTest.class.getName());

    /**
     * Log-level for this test
     */
    private static final Level LOG_LEVEL = Level.INFO;

    /**
     * customer service for testing, usually this would involve remoting
     */
    private CustomerService customerService = new CustomerService();

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        LogUtils.setConsoleHandlerLogLevel(LOG_LEVEL);
        LogUtils.setLogLevel(LOG_LEVEL, TransferObjectAssemblerTest.class, CustomerEntity.class, CustomerDto.class,
                AddressEntity.class, AddressDto.class, CustomerDwhInfoEntity.class, CustomerDwhInfoDto.class,
                CustomerService.class, Database.class, GeoBadPayerInfoDto.class, GeoBadPayerInfoDtoAssembler.class);

        Database.addTestData("ID0001", "Mr.", "McFlurry", "Dick", "0815-987", "Dick.Flurry@neversend.com", true,
                "ADR00091", "Quark-Street 70", "91827", "Gotham City", "USA", "Dear Mr.", "Active Multi Buyer", 82,
                "2009-09-12", "2011-10-01", false, false, false);
        Database.addTestData("ID0002", "Mrs.", "Clark", "Petula", "0817-871", "Petula.Clark@neversend.com", false,
                "ADR01071", "Black Owl Way 34", "1217", "Pork Town", "USA", "Dear Mrs.", "New Customer", 31,
                "2011-08-21", "2011-08-21", false, false, true);
        Database.addTestData("ID0003", "Mrs.", "Blum", "Shanya", "089 8172 2123", "Shanya.Blum@neversend.com", true,
                "ADR00912", "Hauptstrasse 11", "71622", "Blasenhain", "GER", "Sehr gehrte Frau",
                "Inactive Multi Buyer", 57, "2005-09-21", "2010-12-17", false, false, false);
        Database.addTestData("ID0004", "Mr.", "De-Vil", "Sa Tan", "666 666 666", "souleater@neversend.com", false,
                "ADR00666", "Road to Hell 666", "666666", "Blackhole", "USA", "Your Majesty", "Active Multi Buyer", 20,
                "2006-06-06", "2011-08-30", true, true, true);
    }

    @Test
    public void testWithoutTransferObjectAssembler() {

        LOGGER.info("Test without transfer object assembler ...");
        long startTimeNanos = System.nanoTime();

        List<String> display = new ArrayList<>();

        List<CustomerDwhInfoDto> badPayerDwhInfos = customerService.findBadPayerDwhInfos();
        for (CustomerDwhInfoDto dwhInfoDto : badPayerDwhInfos) {
            String customerId = dwhInfoDto.getCustomerId();
            CustomerDto customerDto = customerService.findCustomerById(customerId);
            AddressDto addressDto = customerService.findAddressByCustomerId(customerId);
            if (customerDto != null && addressDto != null) {

                String displayText = "{" + "customerId=" + customerDto.getCustomerId() + ", title="
                        + customerDto.getTitle() + ", lastName=" + customerDto.getLastName() + ", firstName="
                        + customerDto.getFirstName() + ", zipCode=" + addressDto.getZipCode() + ", city="
                        + addressDto.getCity() + ", country=" + addressDto.getCountry() + ", customerType="
                        + dwhInfoDto.getCustomerType() + ", dueInvoice=" + dwhInfoDto.isDueInvoice() + "}";

                display.add(displayText);
            }
        }

        assertEquals("[{customerId=ID0002, title=Mrs., lastName=Clark, firstName=Petula, zipCode=1217, "
                + "city=Pork Town, country=USA, customerType=New Customer, dueInvoice=false}, "
                + "{customerId=ID0004, title=Mr., lastName=De-Vil, firstName=Sa Tan, zipCode=666666, "
                + "city=Blackhole, country=USA, customerType=Active Multi Buyer, dueInvoice=true}]", display.toString());

        LOGGER.info("Test without transfer object assembler successful! Elapsed time: "
                + MiscUtils.formatNanosAsSeconds(System.nanoTime() - startTimeNanos) + " s");

    }

    @Test
    public void testWithTransferObjectAssembler() {

        // HINTS:
        // * Adjust the log-levels above to FINE to see TRANSFER OBJECT ASSEMBLER working
        // * Compare the runtime values of the two tests

        LOGGER.info("Test with transfer object assembler ...");
        long startTimeNanos = System.nanoTime();

        List<String> display = new ArrayList<>();

        List<GeoBadPayerInfoDto> badPayerInfos = customerService.findGeoBadPayerInfos();

        for (GeoBadPayerInfoDto infoDto : badPayerInfos) {
            String displayText = "{" + "customerId=" + infoDto.getCustomerId() + ", title=" + infoDto.getTitle()
                    + ", lastName=" + infoDto.getLastName() + ", firstName=" + infoDto.getFirstName() + ", zipCode="
                    + infoDto.getZipCode() + ", city=" + infoDto.getCity() + ", country=" + infoDto.getCountry()
                    + ", customerType=" + infoDto.getCustomerType() + ", dueInvoice=" + infoDto.isDueInvoice() + "}";

            display.add(displayText);
        }

        assertEquals("[{customerId=ID0002, title=Mrs., lastName=Clark, firstName=Petula, zipCode=1217, "
                + "city=Pork Town, country=USA, customerType=New Customer, dueInvoice=false}, "
                + "{customerId=ID0004, title=Mr., lastName=De-Vil, firstName=Sa Tan, zipCode=666666, "
                + "city=Blackhole, country=USA, customerType=Active Multi Buyer, dueInvoice=true}]", display.toString());

        LOGGER.info("Test with transfer object assembler successful! Elapsed time: "
                + MiscUtils.formatNanosAsSeconds(System.nanoTime() - startTimeNanos) + " s");

    }

}
