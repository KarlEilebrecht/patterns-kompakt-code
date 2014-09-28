//@formatter:off
/*
 * HalCorp Secure Person Data Connector Imp
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
package de.calamanari.pk.bridge.halcorp;

import de.calamanari.pk.bridge.Address;
import de.calamanari.pk.bridge.Person;

/**
 * HalCorp Person Data Connector Imp - a concrete member of the "bridged-out" hierarchy. In this example this is a second connector of the fictional HalCorp
 * company, which supports some kind of super secure transport :-).
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class HalCorpSecurePersonDataConnectorImp extends HalCorpPersonDataConnectorImp {

    /**
     * Some additional feature the new connector supports
     */
    private void startSecureTransport() {
        // do some tricky things
    }

    /**
     * Some additional feature the new connector supports
     */
    private void endSecureTransport() {
        // do some tricky things
    }

    @Override
    public Person findPersonById(String personId) {
        startSecureTransport();
        try {
            return super.findPersonById(personId);
        }
        finally {
            endSecureTransport();
        }
    }

    @Override
    public Address findAddressById(String addressId) {
        startSecureTransport();
        try {
            return super.findAddressById(addressId);
        }
        finally {
            endSecureTransport();
        }
    }

    @Override
    public Address findAddressOfPersonById(String personId) {
        startSecureTransport();
        try {
            return super.findAddressOfPersonById(personId);
        }
        finally {
            endSecureTransport();
        }
    }

    @Override
    public String createNewPerson(Person person) {
        startSecureTransport();
        try {
            return super.createNewPerson(person);
        }
        finally {
            endSecureTransport();
        }
    }

    @Override
    public String createNewAddress(Address address) {
        startSecureTransport();
        try {
            return super.createNewAddress(address);
        }
        finally {
            endSecureTransport();
        }
    }

    @Override
    public void updatePerson(Person person) {
        startSecureTransport();
        try {
            super.updatePerson(person);
        }
        finally {
            endSecureTransport();
        }
    }

    @Override
    public void updateAddress(Address address) {
        startSecureTransport();
        try {
            super.updateAddress(address);
        }
        finally {
            endSecureTransport();
        }
    }

}
