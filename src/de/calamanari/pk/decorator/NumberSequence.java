/*
 * Number sequence
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
package de.calamanari.pk.decorator;

/**
 * Number sequence is the common interface components to be decorated as well as the decorator implement when applying
 * the DECORATOR pattern
 * @author <a href="mailto:Karl.Eilebrecht(a/t)web.de">Karl Eilebrecht</a>
 * 
 */
public interface NumberSequence {

    /**
     * Returns the next value
     * @return next unique id
     */
    public long getNextId();

    /**
     * returns the name of the sequence
     * @return sequence name
     */
    public String getSequenceName();

}
