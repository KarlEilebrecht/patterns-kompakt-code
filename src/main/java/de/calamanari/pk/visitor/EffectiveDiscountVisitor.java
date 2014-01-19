/*
 * Effective Discount Visitor - demonstrates VISITOR pattern
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
 * Effective Discount Visitor - a VISITOR collecting information to determine the effective discount.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class EffectiveDiscountVisitor implements EnterpriseVisitor {

    /**
     * logger
     */
    public static final Logger LOGGER = Logger.getLogger(EffectiveDiscountVisitor.class.getName());

    /**
     * Const for percentage calculation: {@value}
     */
    private static final double PERCENT_BASE_100 = 100.0; 
    
    /**
     * holding discount percentage value
     */
    private double holdingDiscountPerc = 0;

    /**
     * company discount percentage value
     */
    private double companyDiscountPerc = 0;

    /**
     * division discount percentage value
     */
    private double divisionDiscountPerc = 0;

    /**
     * order discount percentage value
     */
    private double orderDiscountPerc = 0;

    /**
     * promotion order discount percentage value
     */
    private double promotionOrderDiscountPerc = 0;

    /**
     * Visit a holding to determine holding discount
     * {@inheritDoc}
     */
    @Override
    public void visit(CustomerHolding holding) {
        LOGGER.fine(this.getClass().getSimpleName() + ".visit(" + CustomerHolding.class.getSimpleName() + ") called");
        holdingDiscountPerc = holding.getHoldingDiscountPerc();
    }

    /**
     * Visit a company to determine company discount
     * {@inheritDoc}
     */
    @Override
    public void visit(CustomerCompany company) {
        LOGGER.fine(this.getClass().getSimpleName() + ".visit(" + CustomerCompany.class.getSimpleName() + ") called");
        companyDiscountPerc = company.getDiscountPerc();
        CustomerHolding holding = company.getHolding();
        if (holding != null) {
            holding.accept(this);
        }
    }

    /**
     * Visit a customer company division to determine division discount
     * {@inheritDoc}
     */
    @Override
    public void visit(CustomerDivision division) {
        LOGGER.fine(this.getClass().getSimpleName() + ".visit(" + CustomerDivision.class.getSimpleName() + ") called");
        divisionDiscountPerc = division.getDivisionDiscountPerc();
        CustomerCompany company = division.getCompany();
        if (company != null) {
            company.accept(this);
        }
    }

    /**
     * Visit a customer order to determine effective order discount
     * {@inheritDoc}
     */
    @Override
    public void visit(CustomerOrder order) {
        LOGGER.fine(this.getClass().getSimpleName() + ".visit(" + CustomerOrder.class.getSimpleName() + ") called");
        this.orderDiscountPerc = order.getSpecialDiscountPerc();
        this.promotionOrderDiscountPerc = calculateEffectivePromotionDiscountPerc(order);

        CustomerDivision division = order.getDivision();
        if (division != null) {
            division.accept(this);
        }
    }

    /**
     * This method returns the effective discount after visiting
     * @return effective discount percentage value
     */
    public double getEffectiveDiscountPerc() {

        double res = 0;

        // During our visits we have only collected information.
        // Next step is to calculate the effective discount from
        // the basic discounts.
        // This part could be refactored into a strategy or
        // even delegated to a rule engine.

        // Business Rules:
        // (1) holding discount will always be added
        // (2) division discount replaces company discount
        // (3) order discount replaces division discount (and thus company discount)
        // (4) if there is a special order discount it will be
        // replaced by order promotion discount if
        // and only if the latter is higher
        // (5) if there is no special order discount the
        // the promotion order discount will be ADDED
        // to any other discount (from division, company and holding)
        if (orderDiscountPerc > 0) {
            res = holdingDiscountPerc + Math.max(orderDiscountPerc, promotionOrderDiscountPerc);
        }
        else if (divisionDiscountPerc > 0) {
            res = promotionOrderDiscountPerc + divisionDiscountPerc + holdingDiscountPerc;
        }
        else if (companyDiscountPerc > 0) {
            res = promotionOrderDiscountPerc + companyDiscountPerc + holdingDiscountPerc;
        }
        else {
            res = promotionOrderDiscountPerc + holdingDiscountPerc;
        }
        return res;
    }

    /**
     * Inspect the order items individual promotion discounts to calculate the effective promotion discount for the
     * whole order.
     * @param order customer order to calculate the effective discount for
     * @return effective promotion discount as percentage value
     */
    private double calculateEffectivePromotionDiscountPerc(CustomerOrder order) {
        double rawOrderPrice = 0;
        double promoOrderPrice = 0;

        boolean hasPromoDiscount = false;
        for (OrderItem item : order.getOrderItems()) {
            if (item.getPromotionDiscountPerc() > 0) {
                hasPromoDiscount = true;
            }
            double rawItemPrice = (item.getPricePerUnit() * item.getAmount());
            double promoFactor = (PERCENT_BASE_100 - item.getPromotionDiscountPerc()) / PERCENT_BASE_100;
            rawOrderPrice = rawOrderPrice + rawItemPrice;
            promoOrderPrice = promoOrderPrice + (promoFactor * rawItemPrice);
        }
        double res = 0.0;
        if (hasPromoDiscount) {
            res = PERCENT_BASE_100 - ((promoOrderPrice / rawOrderPrice) * PERCENT_BASE_100);
        }
        return res;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "({holdingDiscountPerc=" + holdingDiscountPerc
                + ", companyDiscountPerc=" + companyDiscountPerc + ", divisionDiscountPerc=" + divisionDiscountPerc
                + ", orderDiscountPerc=" + orderDiscountPerc + ", promotionOrderDiscountPerc="
                + promotionOrderDiscountPerc + "})";
    }

}
