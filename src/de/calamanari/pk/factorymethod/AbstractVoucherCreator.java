/*
 * Abstract Voucher Creator
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
package de.calamanari.pk.factorymethod;

import java.util.logging.Logger;

/**
 * Abstract Voucher Creator is the abstract creator in this FACTORY METHOD example scenario.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)web.de">Karl Eilebrecht</a>
 */
public abstract class AbstractVoucherCreator {

    /**
     * logger
     */
    protected static final Logger LOGGER = Logger.getLogger(AbstractVoucherCreator.class.getName());

    /**
     * Central method in this FACTORY METHOD example.<br>
     * Concrete voucher creators return a new voucher using the given arguments and their special data/logic.<br>
     * @param firstName first name of the voucher owner
     * @param lastName last name of the voucher owner
     * @param value voucher's value
     * @return new concrete voucher
     */
    public abstract AbstractVoucher createVoucher(String firstName, String lastName, double value);

}