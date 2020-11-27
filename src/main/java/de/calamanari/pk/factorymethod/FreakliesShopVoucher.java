//@formatter:off
/*
 * FreakliesShop Voucher
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
package de.calamanari.pk.factorymethod;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * FreakliesShop Voucher (Voucher from the little shop owned by Mrs. Freakly in Chicago) is a concrete product in this FACTORY METHOD example scenario.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class FreakliesShopVoucher extends AbstractVoucher {

    private static final Logger LOGGER = LoggerFactory.getLogger(FreakliesShopVoucher.class);

    /**
     * identifier for this voucher
     */
    private final int serialNumber;

    /**
     * Creates a new voucher.
     * 
     * @param serialNumber identifies the voucher
     * @param firstName first name of the voucher owner
     * @param lastName last name of the voucher owner
     * @param value voucher's value
     */
    public FreakliesShopVoucher(int serialNumber, String firstName, String lastName, double value) {
        super(firstName, lastName, value);
        this.serialNumber = serialNumber;
        LOGGER.debug("Concrete Product {} created.", this.getClass().getSimpleName());
    }

    @Override
    public String getId() {
        LOGGER.debug("getId() on concrete product {} called.", this.getClass().getSimpleName());
        return "S" + serialNumber;
    }

    @Override
    public String getVoucherDisplayCode() {
        LOGGER.debug("getVoucherDisplayCode() on concrete product {} called.", this.getClass().getSimpleName());
        return "v" + this.getFirstName().toLowerCase().charAt(0) + this.getLastName().toLowerCase().charAt(0) + serialNumber;
    }

    /**
     * Returns the serialNumber that was specified initially.
     * 
     * @return serial number of this FreakliesShop Voucher
     */
    public int getSerialNumber() {
        return this.serialNumber;
    }

}
