/*
 * Combined Method test - demonstrates COMBINED METHOD pattern
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
package de.calamanari.pk.combinedmethod.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import de.calamanari.pk.combinedmethod.Product;
import de.calamanari.pk.combinedmethod.ProductManager;
import de.calamanari.pk.combinedmethod.ProductManagerServer;
import de.calamanari.pk.util.ExternalProcessManager;
import de.calamanari.pk.util.LogUtils;
import de.calamanari.pk.util.MiscUtils;

/**
 * Combined Method test - demonstrates COMBINED METHOD pattern.<br>
 * This test ("the client") calls remote methods, first sequentially than using a combined method to avoid
 * inconsistencies.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)web.de">Karl Eilebrecht</a>
 */
public class CombinedMethodTest {

    /**
     * logger
     */
    private static final Logger LOGGER = Logger.getLogger(CombinedMethodTest.class.getName());

    /**
     * Log-level for this test
     */
    private static final Level LOG_LEVEL = Level.INFO;

    /**
     * The port out private RMI-registry shall use.
     */
    private static final int REGISTRY_PORT = ProductManagerServer.DEFAULT_REGISTRY_PORT;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        LogUtils.setConsoleHandlerLogLevel(LOG_LEVEL);
        LogUtils.setLogLevel(LOG_LEVEL, CombinedMethodTest.class);

        // an external java-process, the product manager server
        if (Level.FINE.equals(LOG_LEVEL)) {
            ExternalProcessManager.getInstance().startExternal(ProductManagerServer.class, LOGGER, "" + REGISTRY_PORT,
                    "logfine");
        }
        else {
            ExternalProcessManager.getInstance().startExternal(ProductManagerServer.class, LOGGER, "" + REGISTRY_PORT);
        }

        // to be sure the server is up, wait 8 seconds
        Thread.sleep(8000);

    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        // stop the product manager server
        ExternalProcessManager.getInstance().stopExternal(ProductManagerServer.class, "q", 5000);
    }

    @Test
    public void testWithoutCombinedMethod() throws Exception {

        LOGGER.info("Test without combined method  ...");
        long startTimeNanos = System.nanoTime();

        Registry registry = LocateRegistry.getRegistry(REGISTRY_PORT);

        ProductManager productManager = (ProductManager) registry.lookup("ProductManager");

        String productId = productManager.acquireProductId();

        assertEquals("ID1001", productId);

        Product product = new Product(productId, "Pencil No. 5");

        productManager.setNextProductRegistrationMustFail();

        Exception caughtEx = null;
        try {
            productManager.registerProduct(product);
        }
        catch (RuntimeException ex) {
            caughtEx = ex;
        }
        assertEquals("Some error!", (caughtEx == null ? "" : caughtEx.getMessage()));

        // new attempt, we don't know the state of the server
        String productId2 = productManager.acquireProductId();
        product.setProductId(productId2);
        productManager.registerProduct(product);

        assertNull(productManager.findProductById(productId));

        Product product2 = productManager.findProductById(productId2);
        assertEquals("Product({productId=ID1002, productName=Pencil No. 5})", product2.toString());

        // The id ID1001 has been wasted, because the two steps (acquire id and register product) run independently

        long elapsed = (System.nanoTime() - startTimeNanos);

        Thread.sleep(1000);

        LOGGER.info("Test without combined method successful! Elapsed time: " + MiscUtils.formatNanosAsSeconds(elapsed)
                + " s");
    }

    @Test
    public void testWithCombinedMethod() throws Exception {

        // Adjust the log-level above to FINE to see the COMBINED METHOD working

        LOGGER.info("Test with combined method  ...");
        long startTimeNanos = System.nanoTime();

        Registry registry = LocateRegistry.getRegistry(REGISTRY_PORT);

        ProductManager productManager = (ProductManager) registry.lookup("ProductManager");

        productManager.reset();

        Product product = new Product(null, "Pencil No. 17");

        productManager.setNextProductRegistrationMustFail();

        Exception caughtEx = null;
        try {
            product = productManager.combinedCreateAndRegisterProduct(product);
        }
        catch (RuntimeException ex) {
            caughtEx = ex;
        }
        assertEquals("Some error!", (caughtEx == null ? "" : caughtEx.getMessage()));

        product = productManager.combinedCreateAndRegisterProduct(product);

        assertEquals("ID1001", product.getProductId());

        Product product2 = productManager.findProductById("ID1001");
        assertEquals("Product({productId=ID1001, productName=Pencil No. 17})", product2.toString());

        // The id ID1001 has NOT been wasted, because because a server-side
        // roll-back saved the id for reuse

        long elapsed = (System.nanoTime() - startTimeNanos);

        Thread.sleep(1000);

        LOGGER.info("Test with combined method successful! Elapsed time: " + MiscUtils.formatNanosAsSeconds(elapsed)
                + " s");

    }

}