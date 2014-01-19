/*
 * Order Item - demonstrates VISITOR pattern
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

/**
 * Order Item - one or more order items are assigned to one order, each item specifies a product and the amount.
 * Furthermore an order item can carry a promotion discount.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class OrderItem {

    /**
     * The product which is ordered
     */
    private String product = null;

    /**
     * amount (number of units) to be ordered
     */
    private int amount = 0;

    /**
     * Unit price
     */
    private double pricePerUnit = 0;

    /**
     * promotion discount for this order item
     */
    private double promotionDiscountPerc = 0;

    /**
     * The order this item belongs to
     */
    private CustomerOrder order = null;

    /**
     * Creates new order item. If the order already contains an item for the specified product a merge will take place.
     * @param order the order this item belongs to
     * @param product ordered product
     * @param amount number of items
     * @param pricePerUnit price of one item
     * @param promotionDiscountPerc special promotion discount percentage
     * @return valid order item
     */
    public static OrderItem createOrderItem(CustomerOrder order, String product, int amount, double pricePerUnit,
            double promotionDiscountPerc) {
        // a factory method allows us to handle the cases "new item" and "merged items" transparently for the caller
        return order.addOrMergeOrderItem(new OrderItem(product, amount, pricePerUnit, promotionDiscountPerc));
    }

    /**
     * Creates new order item
     * @param product ordered product
     * @param amount number of items
     * @param pricePerUnit price of one item
     * @param promotionDiscountPerc special promotion discount percentage
     */
    protected OrderItem(String product, int amount, double pricePerUnit, double promotionDiscountPerc) {
        this.product = product;
        this.amount = amount;
        this.pricePerUnit = pricePerUnit;
        this.promotionDiscountPerc = promotionDiscountPerc;
    }

    /**
     * Returns the product this item carries
     * @return product
     */
    public String getProduct() {
        return product;
    }

    /**
     * Returns the amount (number of units) this item carries
     * @return amount
     */
    public int getAmount() {
        return amount;
    }

    /**
     * Sets the amount (number of units) to be ordered
     * @param amount number of units
     */
    public void setAmount(int amount) {
        this.amount = amount;
    }

    /**
     * Returns the unit price
     * @return unit price
     */
    public double getPricePerUnit() {
        return pricePerUnit;
    }

    /**
     * Sets the unit price
     * @param pricePerUnit price of one item
     */
    public void setPricePerUnit(double pricePerUnit) {
        this.pricePerUnit = pricePerUnit;
    }

    /**
     * Returns the optional promotion discount for this item
     * @return promotion discount as percentage value
     */
    public double getPromotionDiscountPerc() {
        return promotionDiscountPerc;
    }

    /**
     * Sets the optional promotion discount for this item
     * @param promotionDiscountPerc special promotion discount percentage
     */
    public void setPromotionDiscountPerc(double promotionDiscountPerc) {
        this.promotionDiscountPerc = promotionDiscountPerc;
    }

    /**
     * Returns the order this item belongs to
     * @return order
     */
    public CustomerOrder getOrder() {
        return order;
    }

    /**
     * Sets the order this item belongs to
     * @param order related order
     */
    public void setOrder(CustomerOrder order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "({product='" + product + "', amount=" + amount + ", pricePerUnit="
                + pricePerUnit + ", promotionDiscountPerc=" + promotionDiscountPerc + "})";
    }

}
