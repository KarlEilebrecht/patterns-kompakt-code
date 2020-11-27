//@formatter:off
/*
 * Document Manager Security Proxy
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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.calamanari.pk.util.SimpleAccessManager;

/**
 * Document Manager Security Proxy protects a Document Manager in this PROXY example.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class DocumentManagerSecurityProxy implements DocumentManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(DocumentManagerSecurityProxy.class);

    /**
     * The instance we protect
     */
    private final DocumentManager protectedInstance;

    /**
     * Creates a new proxy checking each call for permission before delegating it to the given document manager instance
     * 
     * @param instanceToBeProtected protected object
     */
    public DocumentManagerSecurityProxy(DocumentManager instanceToBeProtected) {
        LOGGER.debug("Creating new {} for {}", this.getClass().getSimpleName(), instanceToBeProtected.getClass().getSimpleName());
        this.protectedInstance = instanceToBeProtected;
    }

    @Override
    public String findDocumentByName(String documentName) {
        LOGGER.debug("findDocumentByName(...) called on {} called - checking permission ...", this.getClass().getSimpleName());
        if (SimpleAccessManager.getInstance().checkPermission()) {
            LOGGER.debug("Permission granted - Delegating findDocumentByName(...) to concrete document manager {}",
                    protectedInstance.getClass().getSimpleName());
            return protectedInstance.findDocumentByName(documentName);
        }
        else {
            LOGGER.debug("Permission denied, call will not be delegated to concrete document manager {}", protectedInstance.getClass().getSimpleName());
            throw new DocumentAccessException(String.format("Read access denied to document %s", documentName));
        }
    }

    @Override
    public void storeDocument(String documentName, String document) {
        LOGGER.debug("storeDocument(...) called on {} called - checking permission ...", this.getClass().getSimpleName());
        if (SimpleAccessManager.getInstance().checkPermission()) {
            LOGGER.debug("Permission granted - Delegating storeDocument(...) to concrete document manager {}", protectedInstance.getClass().getSimpleName());
            protectedInstance.storeDocument(documentName, document);
        }
        else {
            LOGGER.debug("Permission denied, call will not be delegated to concrete document manager {}", protectedInstance.getClass().getSimpleName());
            throw new DocumentAccessException(String.format("Write access denied to document %s", documentName));
        }
    }

}
