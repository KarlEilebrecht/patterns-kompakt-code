//@formatter:off
/*
 * Customer Order - demonstrates VISITOR pattern
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

/**
 * Customer Order - each order can carry a special discount. This discount can be added to a company or division discount but it is not applicable to order
 * items carrying a special promotion discount. Instead the higher discount will be chosen.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class CustomerOrder {

    /**
     * logger
     */
    public static final Logger LOGGER = Logger.getLogger(CustomerOrder.class.getName());

    /**
     * optional reference to a company division the order is related to
     */
    private CustomerDivision division = null;

    /**
     * person responsible for the order
     */
    private String contactPerson = null;

    /**
     * special discount for this order
     */
    private double specialDiscountPerc = 0;

    /**
     * items related to this order
     */
    private List<OrderItem> orderItems = new ArrayList<>();

    /**
     * Creates new order
     * 
     * @param contactPerson person related to order
     * @param division optional division the order belongs to
     * @param specialDiscountPerc order-related discount
     */
    public CustomerOrder(String contactPerson, CustomerDivision division, double specialDiscountPerc) {
        this.division = division;
        this.contactPerson = contactPerson;
        this.specialDiscountPerc = specialDiscountPerc;
    }

    /**
     * Returns the division
     * 
     * @return division the order belongs to or null if no division is involved
     */
    public CustomerDivision getDivision() {
        return division;
    }

    /**
     * Sets the division
     * 
     * @param division assigned divison
     */
    public void setDivision(CustomerDivision division) {
        this.division = division;
    }

    /**
     * Returns the contact person
     * 
     * @return contact person responsible for the order
     */
    public String getContactPerson() {
        return contactPerson;
    }

    /**
     * Sets the contact person
     * 
     * @param contactPerson order-related contact person
     */
    public void setContactPerson(String contactPerson) {
        this.contactPerson = contactPerson;
    }

    /**
     * Returns the special discount percentage value, to be applied to the value of this order
     * 
     * @return discount
     */
    public double getSpecialDiscountPerc() {
        return specialDiscountPerc;
    }

    /**
     * Sets the special discount percentage value, to be applied to the value of this order
     * 
     * @param specialDiscountPerc order-related discount
     */
    public void setSpecialDiscountPerc(double specialDiscountPerc) {
        this.specialDiscountPerc = specialDiscountPerc;
    }

    /**
     * Adds the order item and evtl. merges with existing one.<br>
     * The caller should always use the returned item because the item passed to this method may have been merged.<br>
     * In case of merge the attributes we use the smaller value for price and the higher value for discount (customer advantage rule :-) ).
     * 
     * @param item order item to be added
     * @return the added item
     */
    public OrderItem addOrMergeOrderItem(OrderItem item) {
        OrderItem mergeItem = null;
        for (OrderItem existingItem : orderItems) {
            if (existingItem.getProduct().equals(item.getProduct())) {
                mergeItem = existingItem;
            }
        }
        if (mergeItem != null) {
            mergeItem.setAmount(mergeItem.getAmount() + item.getAmount());
            mergeItem.setPromotionDiscountPerc(Math.max(item.getPromotionDiscountPerc(), mergeItem.getPromotionDiscountPerc()));
            mergeItem.setPricePerUnit(Math.min(item.getPricePerUnit(), mergeItem.getPricePerUnit()));
            item = mergeItem;
        }
        else {
            item.setOrder(this);
            orderItems.add(item);
        }
        return item;
    }

    /**
     * Removes the given item from the order
     * 
     * @param item the order item to be removed
     */
    public void removeOrderItem(OrderItem item) {
        this.orderItems.remove(item);
        item.setOrder(null);
    }

    /**
     * Returns an unmodifiable list of the order items assigned to this order
     * 
     * @return list of order items
     */
    public List<OrderItem> getOrderItems() {
        return Collections.unmodifiableList(this.orderItems);
    }

    /**
     * Method to accept enterprise visitor
     * 
     * @param visitor current visitor
     */
    public void accept(EnterpriseVisitor visitor) {
        LOGGER.fine(this.getClass().getSimpleName() + ".accept(" + visitor.getClass().getSimpleName() + ") called");
        LOGGER.fine("DOUBLE DISPATCH");
        visitor.visit(this);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "({contactPersion='" + contactPerson + "', specialDiscountPerc=" + specialDiscountPerc + ", division='"
                + (division == null ? "Private person" : division.getName()) + "'})";
    }

}
