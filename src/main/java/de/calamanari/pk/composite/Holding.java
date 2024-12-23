//@formatter:off
/*
 * Holding
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
package de.calamanari.pk.composite;

/**
 * Holding is a concrete item type inheriting from AbstractEnterpriseUnit and thus implementing the interface of the COMPOSITE (EnterpriseNode).
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class Holding extends AbstractEnterpriseUnit {

    /**
     * Creates new instance of that name
     * 
     * @param name holding name
     */
    public Holding(String name) {
        super(name);
    }

    // there should be some more functionality here :-)

}
