//@formatter:off
/*
 * Order - entity in COARSE GRAINED LOCK example
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
package de.calamanari.pk.coarsegrainedlock;

/**
 * Order - entity in COARSE GRAINED LOCK example
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class Order {

    /**
     * id of this order
     */
    private String id;

    /**
     * id of the customer this address belongs to
     */
    private String customerId;

    /**
     * details
     */
    private String orderData;

    /**
     * Creates new order related to the specified customer
     * 
     * @param id order identifier
     * @param customerId associated customer identifier
     * @param orderData data for this order
     */
    public Order(String id, String customerId, String orderData) {
        this.id = id;
        this.customerId = customerId;
        this.orderData = orderData;
    }

    /**
     * Returns order id
     * 
     * @return order id
     */
    public String getId() {
        return id;
    }

    /**
     * Returns the related customer id
     * 
     * @return customerId
     */
    public String getCustomerId() {
        return customerId;
    }

    /**
     * Returns the order detail data
     * 
     * @return order data
     */
    public String getOrderData() {
        return orderData;
    }

    /**
     * Sets the order detail data
     * 
     * @param orderData data for this order
     */
    public void setOrderData(String orderData) {
        this.orderData = orderData;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "({id='" + id + "', customerId='" + customerId + "', orderData='" + orderData + "'})";
    }

}
