//@formatter:off
/*
 * Article Manager - class of the component we create a FACADE for.
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

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Article Manager - class of the component we create a FACADE for.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class ArticleManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(ArticleManager.class);

    /**
     * the article "database"
     */
    private final Map<String, Object[]> dataStore = new HashMap<>();

    /**
     * Returns the corresponding article for the given id
     * 
     * @param articleId identifier
     * @return found article instance or null if not found
     */
    public Article findArticleById(String articleId) {
        LOGGER.debug("{}.findArticleById() called", this.getClass().getSimpleName());
        Article article = null;
        Object[] data = dataStore.get(articleId);
        if (data != null) {
            article = new Article((String) data[0], (String) data[1], (Double) data[2]);
        }
        return article;
    }

    /**
     * Stores the article in the internal database
     * 
     * @param article to be stored
     */
    public void storeArticle(Article article) {
        LOGGER.debug("{}.storeArticle() called", this.getClass().getSimpleName());
        Object[] data = new Object[] { article.getId(), article.getName(), article.getPrice() };
        dataStore.put(article.getId(), data);
    }

}
