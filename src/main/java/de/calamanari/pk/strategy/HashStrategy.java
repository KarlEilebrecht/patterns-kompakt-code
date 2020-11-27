//@formatter:off
/*
 * Hash Strategy
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
package de.calamanari.pk.strategy;

/**
 * Hash Strategy - abstract base class for concrete hash-STRATEGY classes.<br>
 * Implementing classes compute hashes for strings.<br>
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public abstract class HashStrategy {

    /**
     * Name of the strategy
     */
    private final String name;

    /**
     * Creates new HashStrategy instance with given technical name
     * 
     * @param name (ascii-letters, ciphers and underscore only!)
     */
    public HashStrategy(String name) {
        this.name = name;
    }

    /**
     * Returns an identifier (KEY) for this particular hash strategy
     * 
     * @return name (ascii-letters, ciphers and underscore only!)
     */
    public String getName() {
        return this.name;
    }

    /**
     * Concrete STRATEGY instances return a hash for the given text.
     * 
     * @param text string a hash shall be computed for
     * @return hash string
     */
    public abstract String computeHash(String text);

}
