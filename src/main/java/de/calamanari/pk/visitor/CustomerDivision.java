//@formatter:off
/*
 * Customer Division - demonstrates VISITOR pattern
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
package de.calamanari.pk.visitor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Customer Division - part of customer structure, a customer's business division may carry a special discount negotiated between us (seller) and the division
 * manager. Division discounts ALWAYS replace company discounts.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class CustomerDivision {

    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerDivision.class);

    /**
     * Division name
     */
    private String name = null;

    /**
     * discount percentage value
     */
    private double divisionDiscountPerc = 0;

    /**
     * mandatory reference to the company the division belongs to
     */
    private CustomerCompany company = null;

    /**
     * Creates new Customer division
     * 
     * @param company customer company the division belongs to
     * @param name divison's name
     * @param divisionDiscountPerc discount percentage for this division
     */
    public CustomerDivision(CustomerCompany company, String name, double divisionDiscountPerc) {
        this.company = company;
        this.name = name;
        this.divisionDiscountPerc = divisionDiscountPerc;
    }

    /**
     * Returns division name
     * 
     * @return name of division
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the division name
     * 
     * @param name division's name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the discount percentage value, to be applied to all orders placed by members of this division.
     * 
     * @return discount
     */
    public double getDivisionDiscountPerc() {
        return divisionDiscountPerc;
    }

    /**
     * Sets the discount percentage value, to be applied to all orders placed by members of this division.
     * 
     * @param discountPerc discount percentage for this division
     */
    public void setDivisionDiscountPerc(double discountPerc) {
        this.divisionDiscountPerc = discountPerc;
    }

    /**
     * Returns the company this division belongs to
     * 
     * @return customer company
     */
    public CustomerCompany getCompany() {
        return company;
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
        return this.getClass().getSimpleName() + "({name='" + name + "', divisionDiscountPerc=" + divisionDiscountPerc + ", company='" + company.getName()
                + "'})";
    }

}
