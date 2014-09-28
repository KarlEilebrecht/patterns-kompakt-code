//@formatter:off
/*
 * Customer Info
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
package de.calamanari.pk.wrapper;

import java.util.Date;

/**
 * Customer Info - interface of the target component, which the WRAPPER provides.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public interface CustomerInfo {

    /**
     * Returns the customer-ID
     * 
     * @return customerId
     */
    public String getId();

    /**
     * Returns the customer's name
     * 
     * @return name of customer
     */
    public String getName();

    /**
     * Returns customer segment
     * 
     * @return customer segment
     */
    public int getCustomerSegment();

    /**
     * Returns the last order date of this customer.
     * 
     * @return last order date or null
     */
    public Date getLastOrderDate();

}
