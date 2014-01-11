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
package de.calamanari.pk.factorymethod;

import java.util.logging.Logger;

/**
 * MoronStore Voucher (Voucher from the MoronStore Worldwide Company) is a concrete product in this FACTORY METHOD
 * example scenario.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class MoronStoreVoucher extends AbstractVoucher {

    /**
     * logger
     */
    protected static final Logger LOGGER = Logger.getLogger(MoronStoreVoucher.class.getName());

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
        LOGGER.fine("Concrete Product " + this.getClass().getSimpleName() + " created.");
    }

    @Override
    public String getId() {
        LOGGER.fine("getId() on concrete product " + this.getClass().getSimpleName() + " called.");
        return this.id;
    }

    @Override
    public String getVoucherDisplayCode() {
        LOGGER.fine("getVoucherDisplayCode() on concrete product " + this.getClass().getSimpleName() + " called.");
        return this.displayCode;
    }

}
