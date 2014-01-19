/*
 * Proxy Test demonstrates PROXY pattern.
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
package de.calamanari.pk.proxy;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.BeforeClass;
import org.junit.Test;

import de.calamanari.pk.proxy.DocumentManager;
import de.calamanari.pk.proxy.DocumentManagerSecurityProxy;
import de.calamanari.pk.proxy.SimpleDocumentManager;
import de.calamanari.pk.util.LogUtils;
import de.calamanari.pk.util.MiscUtils;
import de.calamanari.pk.util.SimpleAccessManager;

/**
 * Proxy Test demonstrates PROXY pattern.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class ProxyTest {

    /**
     * logger
     */
    protected static final Logger LOGGER = Logger.getLogger(ProxyTest.class.getName());

    /**
     * Name (key) for testing
     */
    private static final String TEST_DOCUMENT_NAME = "testDocument";

    /**
     * Name (key) for testing
     */
    private static final String TEST_DOCUMENT_NAME2 = "testDocument2";

    /**
     * Log-level for this test
     */
    private static final Level LOG_LEVEL = Level.INFO;

    /**
     * Mocks some kind of system registry
     */
    private static final Map<String, DocumentManager> SYSTEM_REGISTRY = new ConcurrentHashMap<>();

    /**
     * Key for "system registry
     */
    private static final String DOCUMENT_MANAGER_KEY = "documentMgr";

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        LogUtils.setConsoleHandlerLogLevel(LOG_LEVEL);
        LogUtils.setLogLevel(LOG_LEVEL, ProxyTest.class, DocumentManagerSecurityProxy.class,
                SimpleDocumentManager.class);
    }

    @Test
    public void testProxy() throws Exception {

        // Hint: set the log-level above to FINE to watch PROXY working.

        LOGGER.info("Test Proxy ...");
        long startTimeNanos = System.nanoTime();

        SimpleAccessManager accessManager = SimpleAccessManager.getInstance();

        SimpleDocumentManager concreteDocumentManager = new SimpleDocumentManager();

        concreteDocumentManager.storeDocument(TEST_DOCUMENT_NAME, "The fat yellow cow flew over the rotton lake.");

        SYSTEM_REGISTRY.put(DOCUMENT_MANAGER_KEY, new DocumentManagerSecurityProxy(concreteDocumentManager));

        // for notification after both threads have done their work
        final CountDownLatch doneCount = new CountDownLatch(2);

        final AtomicReference<Exception> unexpectedException = new AtomicReference<>();

        final AtomicReference<Exception> expectedException = new AtomicReference<>();

        // in this example the permissions are granted per thread
        // thus there is no sense in working with a single thread,
        // instead we will use two additional threads for testing

        Thread testThread1 = new Thread() {

            @Override
            public void run() {
                try {
                    DocumentManager documentManager = SYSTEM_REGISTRY.get(DOCUMENT_MANAGER_KEY);
                    String document = documentManager.findDocumentByName(TEST_DOCUMENT_NAME);
                    document = "All the time, t" + document.substring(1);
                    documentManager.storeDocument(TEST_DOCUMENT_NAME, document);
                }
                catch (Exception ex) {
                    unexpectedException.set(ex);
                }
                finally {
                    doneCount.countDown();
                }
            }

        };

        // give the first thread read and write privileges
        accessManager.allow(testThread1, DocumentManagerSecurityProxy.class.getName() + ".findDocumentByName");
        accessManager.allow(testThread1, DocumentManagerSecurityProxy.class.getName() + ".storeDocument");

        Thread testThread2 = new Thread() {

            @Override
            public void run() {
                try {
                    String document = null;
                    DocumentManager documentManager = SYSTEM_REGISTRY.get(DOCUMENT_MANAGER_KEY);

                    try {
                        document = documentManager.findDocumentByName(TEST_DOCUMENT_NAME);
                        document = "All the time, t" + document.substring(1);
                    }
                    catch (Exception ex) {
                        unexpectedException.set(ex);
                    }

                    try {
                        documentManager.storeDocument(TEST_DOCUMENT_NAME2, document);
                    }
                    catch (Exception ex) {
                        expectedException.set(ex);
                    }
                }
                finally {
                    doneCount.countDown();
                }
            }

        };

        // give the second thread only read permission
        accessManager.allow(testThread2, DocumentManagerSecurityProxy.class.getName() + ".findDocumentByName");

        testThread1.start();
        testThread2.start();

        // wait for the threads, but 1 minute max
        boolean success = doneCount.await(1, TimeUnit.MINUTES);
        assertTrue(success);

        assertTrue(expectedException.get() instanceof RuntimeException);
        assertEquals(null, unexpectedException.get());
        assertEquals("All the time, the fat yellow cow flew over the rotton lake.",
                concreteDocumentManager.findDocumentByName(TEST_DOCUMENT_NAME));
        assertEquals(null, concreteDocumentManager.findDocumentByName(TEST_DOCUMENT_NAME2));

        LOGGER.info("Test Proxy successful! Elapsed time: "
                + MiscUtils.formatNanosAsSeconds(System.nanoTime() - startTimeNanos) + " s");

    }

}
