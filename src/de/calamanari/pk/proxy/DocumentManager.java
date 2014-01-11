/*
 * Document Manager
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

/**
 * Document Manager is the interface the concrete manager as well as its PROXY will implement.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public interface DocumentManager {

    /**
     * Searches for the requested document and returns it.
     * @param documentName name of the document to be returned
     * @return document or null if not found
     */
    public String findDocumentByName(String documentName);

    /**
     * Stores the document using the specified name. A previously existing document with the same name will be
     * overwritten.
     * @param documentName name to be used (key)
     * @param document the document to be stored
     */
    public void storeDocument(String documentName, String document);

}
