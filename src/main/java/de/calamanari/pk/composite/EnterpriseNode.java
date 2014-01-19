/*
 * Enterprise Node is the COMPONENT interface in this COMPOSITE-example.
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
package de.calamanari.pk.composite;

/**
 * Enterprise Node is the COMPONENT interface in this COMPOSITE-example.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public interface EnterpriseNode {

    /**
     * Name of organizational unit or member
     * @return display name
     */
    public String getName();

    /**
     * Returns some descriptive text
     * @return description
     */
    public String getDescription();

    /**
     * Returns the parent of this node or null for the root element.
     * @return parent node or null
     */
    public EnterpriseNode getParentNode();

}
