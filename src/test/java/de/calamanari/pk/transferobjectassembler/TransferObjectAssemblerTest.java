//@formatter:off
/*
 * Transfer Object Assembler Test - demonstrates TRANSFER OBJECT ASSEMBLER pattern.
 * Code-Beispiel zum Buch Patterns Kompakt, Verlag Springer Vieweg
 * Copyright 2014 Karl Eilebrecht
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"):
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

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.calamanari.pk.util.TimeUtils;

/**
 * Transfer Object Assembler Test - demonstrates TRANSFER OBJECT ASSEMBLER pattern.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class TransferObjectAssemblerTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(TransferObjectAssemblerTest.class);

    /**
     * customer service for testing, usually this would involve remoting
     */
    private CustomerService customerService = new CustomerService();

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {

        // @formatter:off

        Database.prepareCustomer("ID0001")
            .withTitle("Mr.")
            .withLastName("McFlurry")
            .withFirstName("Dick")
            .withPhone("0815-987")
            .withEmail("Dick.Flurry@neversend.com")
            .withPromotionOptIn(true)
            .withAddressId("ADR00091")
            .withStreet("Quark-Street 70")
            .withZipCode("91827")
            .withCity("Gotham City")
            .withCountry("USA")
            .withSalutation("Dear Mr.")
            .withCustomerType("Active Multi Buyer")
            .withScorePoints(82)
            .withFirstOrderDate("2009-09-12")
            .withLastOrderDate("2011-10-01")
            .withDueInvoice(false)
            .withFraudSuspicion(false)
            .withBadPayer(false)
        .commit();
        
        Database.prepareCustomer("ID0002")
            .withTitle("Mrs.")
            .withLastName("Clark")
            .withFirstName("Petula")
            .withPhone("0817-871")
            .withEmail("Petula.Clark@neversend.com")
            .withPromotionOptIn(false)
            .withAddressId("ADR01071")
            .withStreet("Black Owl Way 34")
            .withZipCode("1217")
            .withCity("Pork Town")
            .withCountry("USA")
            .withSalutation("Dear Mrs.")
            .withCustomerType("New Customer")
            .withScorePoints(31)
            .withFirstOrderDate("2011-08-21")
            .withLastOrderDate("2011-08-21")
            .withDueInvoice(false)
            .withFraudSuspicion(false)
            .withBadPayer(true)
        .commit();

        Database.prepareCustomer("ID0003")
            .withTitle("Mrs.")
            .withLastName("Blum")
            .withFirstName("Shanya")
            .withPhone("089 8172 2123")
            .withEmail("Shanya.Blum@neversend.com")
            .withPromotionOptIn(true)
            .withAddressId("ADR00912")
            .withStreet("Hauptstrasse 11")
            .withZipCode("71622")
            .withCity("Blasenhain")
            .withCountry("GER")
            .withSalutation("Sehr gehrte Frau")
            .withCustomerType("Inactive Multi Buyer")
            .withScorePoints(57)
            .withFirstOrderDate("2005-09-21")
            .withLastOrderDate("2010-12-17")
            .withDueInvoice(false)
            .withFraudSuspicion(false)
            .withBadPayer(false)
        .commit();

        Database.prepareCustomer("ID0004")
            .withTitle("Mr.")
            .withLastName("De-Vil")
            .withFirstName("Sa Tan")
            .withPhone("666 666 666")
            .withEmail("souleater@neversend.com")
            .withPromotionOptIn(false)
            .withAddressId("ADR00666")
            .withStreet("Road to Hell 666")
            .withZipCode("666666")
            .withCity("Blackhole")
            .withCountry("USA")
            .withSalutation("Your Majesty")
            .withCustomerType("Active Multi Buyer")
            .withScorePoints(20)
            .withFirstOrderDate("2006-06-06")
            .withLastOrderDate("2011-08-30")
            .withDueInvoice(true)
            .withFraudSuspicion(true)
            .withBadPayer(true)
        .commit();
        
        // @formatter:off
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

                String displayText = "{" + "customerId=" + customerDto.getCustomerId() + ", title=" + customerDto.getTitle() + ", lastName="
                        + customerDto.getLastName() + ", firstName=" + customerDto.getFirstName() + ", zipCode=" + addressDto.getZipCode() + ", city="
                        + addressDto.getCity() + ", country=" + addressDto.getCountry() + ", customerType=" + dwhInfoDto.getCustomerType() + ", dueInvoice="
                        + dwhInfoDto.isDueInvoice() + "}";

                display.add(displayText);
            }
        }

        assertEquals("[{customerId=ID0002, title=Mrs., lastName=Clark, firstName=Petula, zipCode=1217, "
                + "city=Pork Town, country=USA, customerType=New Customer, dueInvoice=false}, "
                + "{customerId=ID0004, title=Mr., lastName=De-Vil, firstName=Sa Tan, zipCode=666666, "
                + "city=Blackhole, country=USA, customerType=Active Multi Buyer, dueInvoice=true}]", display.toString());

        String elapsedTimeString = TimeUtils.formatNanosAsSeconds(System.nanoTime() - startTimeNanos);
        LOGGER.info("Test without transfer object assembler successful! Elapsed time: {} s", elapsedTimeString);

    }

    @Test
    public void testWithTransferObjectAssembler() {

        // HINTS:
        // * Adjust the log-level in logback.xml to DEBUG to see TRANSFER OBJECT ASSEMBLER working
        // * Compare the runtime values of the two tests

        LOGGER.info("Test with transfer object assembler ...");
        long startTimeNanos = System.nanoTime();

        List<String> display = new ArrayList<>();

        List<GeoBadPayerInfoDto> badPayerInfos = customerService.findGeoBadPayerInfos();

        for (GeoBadPayerInfoDto infoDto : badPayerInfos) {
            String displayText = "{" + "customerId=" + infoDto.getCustomerId() + ", title=" + infoDto.getTitle() + ", lastName=" + infoDto.getLastName()
                    + ", firstName=" + infoDto.getFirstName() + ", zipCode=" + infoDto.getZipCode() + ", city=" + infoDto.getCity() + ", country="
                    + infoDto.getCountry() + ", customerType=" + infoDto.getCustomerType() + ", dueInvoice=" + infoDto.isDueInvoice() + "}";

            display.add(displayText);
        }

        assertEquals("[{customerId=ID0002, title=Mrs., lastName=Clark, firstName=Petula, zipCode=1217, "
                + "city=Pork Town, country=USA, customerType=New Customer, dueInvoice=false}, "
                + "{customerId=ID0004, title=Mr., lastName=De-Vil, firstName=Sa Tan, zipCode=666666, "
                + "city=Blackhole, country=USA, customerType=Active Multi Buyer, dueInvoice=true}]", display.toString());

        String elapsedTimeString = TimeUtils.formatNanosAsSeconds(System.nanoTime() - startTimeNanos);
        LOGGER.info("Test with transfer object assembler successful! Elapsed time: {} s", elapsedTimeString);

    }

}
