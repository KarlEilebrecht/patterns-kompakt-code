/*
 * Product - supplementary product class
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
package de.calamanari.pk.combinedmethod;

import java.io.Serializable;

/**
 * Product - supplementary product class
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class Product implements Serializable {

    /**
     * for serialization (for RMI-transport)
     */
    private static final long serialVersionUID = -8407595608317841572L;

    /**
     * id of this product
     */
    private String productId;

    /**
     * product name
     */
    private String productName;

    /**
     * Creates product
     * @param productId system-id of this product
     * @param productName name of the product
     */
    public Product(String productId, String productName) {
        this.productId = productId;
        this.productName = productName;
    }

    /**
     * Returns the name of the product
     * @return name of product
     */
    public String getProductName() {
        return productName;
    }

    /**
     * Sets the name of the product
     * @param productName name of product
     */
    public void setProductName(String productName) {
        this.productName = productName;
    }

    /**
     * Sets the product-ID
     * @param productId identifier
     */
    public void setProductId(String productId) {
        this.productId = productId;
    }

    /**
     * Returns the system-id of the product
     * @return product id
     */
    public String getProductId() {
        return productId;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + "({productId=" + productId + ", productName=" + productName + "})";
    }

    // usually some more attributes :-)

}
