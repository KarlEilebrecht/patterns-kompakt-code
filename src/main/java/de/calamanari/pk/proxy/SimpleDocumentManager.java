//@formatter:off
/*
 * Simple Document Manager
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
package de.calamanari.pk.proxy;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

/**
 * Simple Document Manager is a concrete document manager. In this PROXY example we will protect it using a SECURITY PROXY.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class SimpleDocumentManager implements DocumentManager {

    /**
     * logger
     */
    protected static final Logger LOGGER = Logger.getLogger(SimpleDocumentManager.class.getName());

    /**
     * here we "store" the documents
     */
    private Map<String, String> documentStore = new ConcurrentHashMap<>();

    @Override
    public String findDocumentByName(String documentName) {
        LOGGER.fine("findDocumentByName(...) called on " + this.getClass().getSimpleName() + "");
        return documentStore.get(documentName);
    }

    @Override
    public void storeDocument(String documentName, String document) {
        LOGGER.fine("storeDocument(...) called on " + this.getClass().getSimpleName() + "");
        documentStore.put(documentName, document);
    }

}
