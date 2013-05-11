/*
 * Article Manager - class of the component we create a FACADE for.
 * Code-Beispiel zum Buch Patterns Kompakt, Verlag Springer Vieweg
 * Copyright 2013 Karl Eilebrecht
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
package de.calamanari.pk.facade.article;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Article Manager - class of the component we create a FACADE for.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)web.de">Karl Eilebrecht</a>
 */
public class ArticleManager {

    /**
     * logger
     */
    protected static final Logger LOGGER = Logger.getLogger(ArticleManager.class.getName());

    /**
     * the article "database"
     */
    private final Map<String, Object[]> dataStore = new HashMap<>();

    /**
     * Returns the corresponding article for the given id
     * @param articleId identifier
     * @return found article instance or null if not found
     */
    public Article findArticleById(String articleId) {
        LOGGER.fine("" + this.getClass().getSimpleName() + ".findArticleById() called");
        Article article = null;
        Object[] data = dataStore.get(articleId);
        if (data != null) {
            article = new Article((String) data[0], (String) data[1], (Double) data[2]);
        }
        return article;
    }

    /**
     * Stores the article in the internal database
     * @param article to be stored
     */
    public void storeArticle(Article article) {
        LOGGER.fine("" + this.getClass().getSimpleName() + ".storeArticle() called");
        Object[] data = new Object[] { article.getId(), article.getName(), article.getPrice() };
        dataStore.put(article.getId(), data);
    }

}
