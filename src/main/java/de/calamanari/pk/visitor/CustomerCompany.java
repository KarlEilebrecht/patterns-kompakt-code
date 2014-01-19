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
package de.calamanari.pk.visitor;

import java.util.logging.Logger;

/**
 * Customer Company - part of customer structure, may carry a discount negotiated between us (seller) and the company.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class CustomerCompany {

    /**
     * logger
     */
    public static final Logger LOGGER = Logger.getLogger(CustomerCompany.class.getName());

    /**
     * Company name
     */
    private String name = null;

    /**
     * discount percentage value
     */
    private double discountPerc = 0;

    /**
     * optional a company belongs to a holding
     */
    private CustomerHolding holding = null;

    /**
     * Creates new company
     * @param holding optional holding this company belongs to
     * @param name company's name
     * @param discountPerc discount percentage for the company
     */
    public CustomerCompany(CustomerHolding holding, String name, double discountPerc) {
        this.holding = holding;
        this.name = name;
        this.discountPerc = discountPerc;
    }

    /**
     * Returns comany name
     * @return name of company
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the company name
     * @param name company's name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the discount percentage value, to be applied to all orders placed by members of this company.
     * @return discount
     */
    public double getDiscountPerc() {
        return discountPerc;
    }

    /**
     * Sets the discount percentage value, to be applied to all orders placed by members of this company.
     * @param discountPerc discount percentage for the company
     */
    public void setDiscountPerc(double discountPerc) {
        this.discountPerc = discountPerc;
    }

    /**
     * Returns the holding this company belongs to
     * @return holding or null
     */
    public CustomerHolding getHolding() {
        return holding;
    }

    /**
     * Sets the holding this company belongs to
     * @param holding or null
     */
    public void setHolding(CustomerHolding holding) {
        this.holding = holding;
    }

    /**
     * Method to accept enterprise visitor
     * @param visitor current visitor
     */
    public void accept(EnterpriseVisitor visitor) {
        LOGGER.fine(this.getClass().getSimpleName() + ".accept(" + visitor.getClass().getSimpleName() + ") called");
        LOGGER.fine("DOUBLE DISPATCH");
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "({name='" + name + "', discountPerc=" + discountPerc + ", holding='"
                + (holding == null ? "N/A" : holding.getName()) + "'})";
    }

}
