//@formatter:off
/*
 * Article - a supplementary class in FACADE example.
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
package de.calamanari.pk.facade.article;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Article - a supplementary class in FACADE example.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class Article {

    /**
     * article id
     */
    private String id;

    /**
     * name of article
     */
    private String name;

    /**
     * price of article
     */
    private double price;

    /**
     * Creates new Article
     * 
     * @param id identifier
     * @param name article name
     * @param price article price
     */
    public Article(String id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    /**
     * Returns article identifier
     * 
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * Sets the article identifier
     * 
     * @param id identifier
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Returns article name
     * 
     * @return name of article
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the name of the article
     * 
     * @param name article name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns the article price
     * 
     * @return price of article
     */
    public double getPrice() {
        return price;
    }

    /**
     * Sets the article price
     * 
     * @param price article price to set
     */
    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public int hashCode() {
        return this.id == null ? 0 : this.id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        boolean res = false;
        if (obj instanceof Article) {
            Article other = (Article) obj;
            if (this.id != null && other.id != null) {
                res = this.id.equals(other.id);
            }
        }
        return res;
    }

    @Override
    public String toString() {
        NumberFormat nf = NumberFormat.getInstance(Locale.US);
        nf.setMinimumFractionDigits(2);
        nf.setMaximumFractionDigits(2);
        return this.getClass().getSimpleName() + "({id=" + this.id + ", name='" + this.name + "', price=" + nf.format(this.price) + "})";
    }

}
