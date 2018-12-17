//@formatter:off
/*
 * MoronStore Voucher
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
package de.calamanari.pk.factorymethod;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * MoronStore Voucher (Voucher from the MoronStore Worldwide Company) is a concrete product in this FACTORY METHOD example scenario.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class MoronStoreVoucher extends AbstractVoucher {

    private static final Logger LOGGER = LoggerFactory.getLogger(MoronStoreVoucher.class);

    /**
     * id of this voucher
     */
    private final String id;

    /**
     * display code of this voucher
     */
    private final String displayCode;

    /**
     * Creates new MoronStore Voucher
     * 
     * @param id voucher's id
     * @param displayCode human readable display code
     * @param firstName first name of the voucher owner
     * @param lastName last name of the voucher owner
     * @param value this voucher's value
     */
    public MoronStoreVoucher(String id, String displayCode, String firstName, String lastName, double value) {
        super(firstName, lastName, value);
        this.id = id;
        this.displayCode = displayCode;
        LOGGER.debug("Concrete Product {} created.", this.getClass().getSimpleName());
    }

    @Override
    public String getId() {
        LOGGER.debug("getId() on concrete product {} called.", this.getClass().getSimpleName());
        return this.id;
    }

    @Override
    public String getVoucherDisplayCode() {
        LOGGER.debug("getVoucherDisplayCode() on concrete product {} called.", this.getClass().getSimpleName());
        return this.displayCode;
    }

}
