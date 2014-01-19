/*
 * Facade Test demonstrates FACADE pattern.
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
package de.calamanari.pk.facade;

import static org.junit.Assert.assertEquals;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.calamanari.pk.facade.article.Article;
import de.calamanari.pk.facade.article.ArticleManager;
import de.calamanari.pk.facade.article.ArticleViewFacade;
import de.calamanari.pk.facade.article.history.ArticleHistory;
import de.calamanari.pk.util.LogUtils;
import de.calamanari.pk.util.MiscUtils;

/**
 * Facade Test demonstrates FACADE pattern.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class FacadeTest {

    /**
     * logger
     */
    protected static final Logger LOGGER = Logger.getLogger(FacadeTest.class.getName());

    /**
     * Log-level for this test
     */
    private static final Level LOG_LEVEL = Level.INFO;

    /**
     * article manager (part of article component)
     */
    private ArticleManager articleManager = null;

    /**
     * article history (part of article component)
     */
    private ArticleHistory articleHistory = null;

    /**
     * The facade
     */
    ArticleViewFacade articleViewfacade = null;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        LogUtils.setConsoleHandlerLogLevel(LOG_LEVEL);
        LogUtils.setLogLevel(LOG_LEVEL, FacadeTest.class, ArticleViewFacade.class, Article.class, ArticleManager.class,
                ArticleHistory.class);
    }

    @Before
    public void setUp() throws Exception {
        articleManager = new ArticleManager();
        articleHistory = new ArticleHistory();
        articleManager.storeArticle(new Article("0815", "Pencil \"Golden Delicious\"", 7.50));
        articleManager.storeArticle(new Article("0821", "Pencil Case Banana Blue", 11.90));
        articleManager.storeArticle(new Article("0822", "Pencil Case Miranda", 5.00));
        articleHistory.setMarkDownPrice("0815", 9.00);
        articleHistory.setMarkDownPrice("0822", 4.90);
        articleViewfacade = new ArticleViewFacade(articleManager, articleHistory);
    }

    @Test
    public void testFacade() {

        // Hint: set the log-level above to FINE to watch FACADE working.

        LOGGER.info("Test Facade ...");
        long startTimeNanos = System.nanoTime();

        Article article1 = articleViewfacade.findArticleById("0815");
        double markDownPrice1 = articleViewfacade.getMarkDownPrice("0815");

        Article article2 = articleViewfacade.findArticleById("0821");
        double markDownPrice2 = articleViewfacade.getMarkDownPrice("0821");

        Article article3 = articleViewfacade.findArticleById("0822");
        double markDownPrice3 = articleViewfacade.getMarkDownPrice("0822");

        NumberFormat nf = NumberFormat.getInstance(Locale.US);

        assertEquals("Article({id=0815, name='Pencil \"Golden Delicious\"', price=7.50})", article1.toString());
        assertEquals("Article({id=0821, name='Pencil Case Banana Blue', price=11.90})", article2.toString());
        assertEquals("Article({id=0822, name='Pencil Case Miranda', price=5.00})", article3.toString());

        assertEquals(nf.format(9), nf.format(markDownPrice1));
        assertEquals(nf.format(-1), nf.format(markDownPrice2));
        assertEquals(nf.format(-1), nf.format(markDownPrice3));

        LOGGER.info("Test Facade successful! Elapsed time: "
                + MiscUtils.formatNanosAsSeconds(System.nanoTime() - startTimeNanos) + " s");

    }

}
