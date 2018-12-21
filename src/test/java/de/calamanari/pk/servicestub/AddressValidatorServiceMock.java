//@formatter:off
/*
 * Address Validator Service Mock - SERVICE STUB (aka Mock) for AddressValidator Service 
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
package de.calamanari.pk.servicestub;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.calamanari.pk.servicestub.adrchk.AddressValidatorService;

/**
 * Address Validator Service Mock - SERVICE STUB (aka Mock) for AddressValidator Service<br>
 * This is a very simple mock, always returning a predefined result, more sophisticated mocks create matching results for different input sets. <br>
 * Mock frameworks (like http://code.google.com/p/mockito/) simplify mocking.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class AddressValidatorServiceMock implements AddressValidatorService {

    private static final Logger LOGGER = LoggerFactory.getLogger(AddressValidatorServiceMock.class);

    /**
     * The predefined result
     */
    private boolean validationResult = true;

    /**
     * Creates a new mock object always returning the given validation result
     * 
     * @param validationResult result to be returned
     */
    public AddressValidatorServiceMock(boolean validationResult) {
        LOGGER.debug("{}({}) created.", this.getClass().getSimpleName(), validationResult);
        this.validationResult = validationResult;
    }

    @Override
    public boolean validateAddress(String street, String zipCode, String city) {
        LOGGER.debug(this.getClass().getSimpleName() + ".validateAddress('" + street + "', '" + zipCode + "', '" + city + "') called.");
        LOGGER.debug("Returning {}", validationResult);
        return validationResult;
    }

    /**
     * Sets the result to be returned
     * 
     * @param validationResult result of the validation process
     */
    public void setValidationResult(boolean validationResult) {
        LOGGER.debug("{}.setValidationResult({}) called.", this.getClass().getSimpleName(), validationResult);
        this.validationResult = validationResult;
    }

}
