/*
 * Article History - another class of the component we create a FACADE for.
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
package de.calamanari.pk.facade.article.history;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Article History - another class of the component we create a FACADE for.<br>
 * The history delivers details related to an article but not part of the core information.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class ArticleHistory {

    /**
     * logger
     */
    protected static final Logger LOGGER = Logger.getLogger(ArticleHistory.class.getName());

    /**
     * In this example the history is a little featureless :-) Only markdown prices are provided.
     */
    private Map<String, Double> articleMarkDownPrices = new HashMap<>();

    /**
     * Returns the current markdown price for the specified article.
     * @param articleId identifier
     * @return current mark down price or -1 to indicate unknown
     */
    public double getCurrentMarkDownPrice(String articleId) {
        LOGGER.fine("" + this.getClass().getSimpleName() + ".getCurrentMarkDownPrice() called");
        double res = -1; // unknown
        Double markDownPrice = articleMarkDownPrices.get(articleId);
        if (markDownPrice != null) {
            res = markDownPrice.doubleValue();
        }
        return res;
    }

    /**
     * Sets the current markdown price for the given article
     * @param articleId identifier
     * @param markDownPrice 0 or less means unknown
     */
    public void setMarkDownPrice(String articleId, double markDownPrice) {
        if (markDownPrice > 0) {
            articleMarkDownPrices.put(articleId, markDownPrice);
        }
        else {
            articleMarkDownPrices.remove(articleId);
        }
    }

}
