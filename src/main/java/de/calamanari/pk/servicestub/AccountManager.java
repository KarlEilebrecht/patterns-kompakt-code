//@formatter:off
/*
 * Account Manager - a component to be tested
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

import java.util.logging.Logger;

import de.calamanari.pk.servicestub.adrchk.AddressValidatorService;

/**
 * Account Manager - a component to be tested<br>
 * An instance can only be tested with a valid AddressValidator reference injected.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class AccountManager {

    /**
     * logger
     */
    private static final Logger LOGGER = Logger.getLogger(AccountManager.class.getName());

    /**
     * reference to service for validating addresses
     */
    private final AddressValidatorService addressValidatorService;

    /**
     * for incrementing account IDs
     */
    private int lastAccountNumber = 0;

    /**
     * Constructor (to be used by framework) allowing service reference injection
     * 
     * @param addressValidatorService injected validation service
     */
    public AccountManager(AddressValidatorService addressValidatorService) {
        if (addressValidatorService == null) {
            LOGGER.fine(this.getClass().getSimpleName() + "(null) created.");
        }
        else {
            LOGGER.fine(this.getClass().getSimpleName() + "(instance of " + addressValidatorService.getClass() + ") created.");
        }
        this.addressValidatorService = addressValidatorService;
    }

    /**
     * Creates new valid account - the method we will test, it uses address validation service internally
     * 
     * @param firstName person's first name
     * @param lastName person's last name
     * @param street address field
     * @param zipCode address field
     * @param city address field
     * @return new account
     * @throws AccountValidationException if account could not be created
     */
    public Account createAccount(String firstName, String lastName, String street, String zipCode, String city) throws AccountValidationException {
        LOGGER.fine(this.getClass().getSimpleName() + ".createAccount('" + firstName + "', '" + lastName + "', '" + street + "', '" + zipCode + "', '" + city
                + "') called ...");
        if (firstName == null || firstName.trim().length() == 0 || lastName == null || lastName.trim().length() == 0) {
            throw new AccountValidationException("Could not create account, invalid name.");
        }
        LOGGER.fine("Validating address ...");
        if (addressValidatorService == null) {
            LOGGER.fine("Improperly initilized AccountManager causes NullPointer");
            throw new NullPointerException("addressValidatorService null");
        }
        else {
            if (!addressValidatorService.validateAddress(street, zipCode, city)) {
                LOGGER.fine("Validation failed, throwing exception.");
                throw new AccountValidationException("Could not create account, invalid address.");
            }
        }
        LOGGER.fine("Data validated, creating account.");
        lastAccountNumber++;
        return new Account("ID_" + lastAccountNumber, firstName.trim(), lastName.trim(), street, zipCode, city);
    }

    // some more methods ...

}
