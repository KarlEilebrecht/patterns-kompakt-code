//@formatter:off
/*
 * Article View Facade
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
package de.calamanari.pk.facade.article;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.calamanari.pk.facade.article.history.ArticleHistory;

/**
 * Article View Facade- a FACADE for article management, with restricted access (read-only) and extended by an option to get the mark down price.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class ArticleViewFacade {

    private static final Logger LOGGER = LoggerFactory.getLogger(ArticleViewFacade.class);

    /**
     * Reference to the article manager
     */
    private final ArticleManager articleManager;

    /**
     * Reference to the article history
     */
    private final ArticleHistory articleHistory;

    /**
     * Creates a new facade connected to the manager and the history.
     * 
     * @param articleManager manager instance for accessing articles
     * @param articleHistory history system reference
     */
    public ArticleViewFacade(ArticleManager articleManager, ArticleHistory articleHistory) {
        LOGGER.debug("Creating new {}", this.getClass().getSimpleName());
        this.articleManager = articleManager;
        this.articleHistory = articleHistory;
    }

    /**
     * Returns the corresponding article for the given id
     * 
     * @param articleId identifier
     * @return found article or null if not found
     */
    public Article findArticleById(String articleId) {
        LOGGER.debug("{}.findArticleById() called, delegating to internal manager ...", this.getClass().getSimpleName());
        return this.articleManager.findArticleById(articleId);
    }

    /**
     * Returns the current markdown price for the specified article, if and only if the markdown price is greater than current price
     * 
     * @param articleId identifier
     * @return current mark down price or -1 to indicate unknown
     */
    public double getMarkDownPrice(String articleId) {
        LOGGER.debug("{}.getMarkDownPrice() called, delegating to internal history ...", this.getClass().getSimpleName());
        double res = -1;
        Article article = findArticleById(articleId);
        if (article != null) {
            double currentPrice = article.getPrice();
            double markDownPrice = articleHistory.getCurrentMarkDownPrice(articleId);
            if (markDownPrice != -1 && markDownPrice > currentPrice) {
                res = markDownPrice;
            }
            else {
                LOGGER.debug("Current price is less than proposed mark down price, ignoring mark down price!");
            }
        }
        return res;
    }

}
