/*
 * Address Validator Service - a service interface
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
package de.calamanari.pk.servicestub.adrchk;

/**
 * Address Validator Service - interface of a service our component needs and that will not be available when testing
 * our component.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public interface AddressValidatorService {

    /**
     * This service method validates the given address
     * @param street address field
     * @param zipCode address field
     * @param city address field
     * @return true of the given address exists, otherwise false
     */
    public boolean validateAddress(String street, String zipCode, String city);

}
