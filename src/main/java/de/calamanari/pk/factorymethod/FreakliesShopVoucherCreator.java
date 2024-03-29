//@formatter:off
/*
 * FreakliesShop Voucher Creator
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
 * FreakliesShop Voucher Creator is a concrete creator in this FACTORY METHOD example scenario.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class FreakliesShopVoucherCreator extends AbstractVoucherCreator {

    private static final Logger LOGGER = LoggerFactory.getLogger(FreakliesShopVoucherCreator.class);

    /**
     * Numbers will start with {@value} + 1;
     */
    private static final int START_NUMBER = 11_111_111;

    /**
     * this should be some kind of number generator, of course ...
     */
    private int lastNumber = START_NUMBER;

    @Override
    public AbstractVoucher createVoucher(String firstName, String lastName, double value) {
        LOGGER.debug("createVoucher() on concrete creator {} called.", this.getClass().getSimpleName());
        AbstractVoucher voucher = new FreakliesShopVoucher(lastNumber, firstName, lastName, value);
        lastNumber++;
        return voucher;
    }

}
