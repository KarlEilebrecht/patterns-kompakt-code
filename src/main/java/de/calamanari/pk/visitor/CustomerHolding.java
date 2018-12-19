//@formatter:off
/*
 * Customer Company - demonstrates VISITOR pattern
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
package de.calamanari.pk.visitor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Customer Holding - part of customer structure, there can be a holding discount which applies to all orders from any company related to this holding. This
 * (additional) discount shall be an appeal for other companies from the same holding to become our customers.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class CustomerHolding {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerHolding.class);

    /**
     * Holding name
     */
    private String name = null;

    /**
     * discount percentage value
     */
    private double holdingDiscountPerc = 0;

    /**
     * Creates new holding
     * 
     * @param name holding's name
     * @param discountPerc discount percentage for the holding
     */
    public CustomerHolding(String name, double discountPerc) {
        this.name = name;
        this.holdingDiscountPerc = discountPerc;
    }

    /**
     * Returns holding name
     * 
     * @return name of holding
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the holding name
     * 
     * @param name holding's name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the discount percentage value, to be applied to all orders placed by companies related to this holding.
     * 
     * @return discount
     */
    public double getHoldingDiscountPerc() {
        return holdingDiscountPerc;
    }

    /**
     * Sets the discount percentage value, to be applied to all orders placed by companies related to this holding.
     * 
     * @param discountPerc discount percentage for the holding
     */
    public void setHoldingDiscountPerc(double discountPerc) {
        this.holdingDiscountPerc = discountPerc;
    }

    /**
     * Method to accept enterprise visitor
     * 
     * @param visitor current visitor
     */
    public void accept(EnterpriseVisitor visitor) {
        LOGGER.debug("{}.accept({}) called", this.getClass().getSimpleName(), visitor.getClass().getSimpleName());
        LOGGER.debug("DOUBLE DISPATCH");
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "({name='" + name + "', discountPerc=" + holdingDiscountPerc + "})";
    }

}
