/*
 * Address Validator Service Mock - SERVICE STUB (aka Mock) for AddressValidator Service 
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
package de.calamanari.pk.servicestub.test;

import java.util.logging.Logger;

import de.calamanari.pk.servicestub.adrchk.AddressValidatorService;

/**
 * Address Validator Service Mock - SERVICE STUB (aka Mock) for AddressValidator Service<br>
 * This is a very simple mock, always returning a predefined result, more sophisticated mocks create matching results
 * for different input sets. <br>
 * Mock frameworks (like http://code.google.com/p/mockito/) simplify mocking.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)web.de">Karl Eilebrecht</a>
 */
public class AddressValidatorServiceMock implements AddressValidatorService {

    /**
     * logger
     */
    private static final Logger LOGGER = Logger.getLogger(AddressValidatorServiceMock.class.getName());

    /**
     * The predefined result
     */
    private boolean validationResult = true;

    /**
     * Creates a new mock object always returning the given validation result
     * @param validationResult result to be returned
     */
    public AddressValidatorServiceMock(boolean validationResult) {
        LOGGER.fine(this.getClass().getSimpleName() + "(" + validationResult + ") created.");
        this.validationResult = validationResult;
    }

    @Override
    public boolean validateAddress(String street, String zipCode, String city) {
        LOGGER.fine(this.getClass().getSimpleName() + ".validateAddress('" + street + "', '" + zipCode + "', '" + city
                + "') called.");
        LOGGER.fine("Returning " + validationResult);
        return validationResult;
    }

    /**
     * Sets the result to be returned
     * @param validationResult result of the validation process
     */
    public void setValidationResult(boolean validationResult) {
        LOGGER.fine(this.getClass().getSimpleName() + ".setValidationResult(" + validationResult + ") called.");
        this.validationResult = validationResult;
    }

}
