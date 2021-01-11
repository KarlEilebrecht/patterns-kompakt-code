//@formatter:off
/*
 * ItemStringCodec
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
package de.calamanari.pk.muhai.collider;

/**
 * The {@link ItemStringCodec} is an interface for a POLICY to transform an element to a String and vice-versa for writing/reading files.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 * @param <E> type of the elements
 */
public interface ItemStringCodec<E> {

    /**
     * Encodes an item into a String that can be written as a single line
     * @param item element to be converted to a string
     * @return encoded item string, no line breaks
     * @throws ItemConversionException on any error
     */
    public String itemToString(E item);

    /**
     * Encodes an item into a String that can be written as a single line
     * @param line encoded item string, no line breaks
     * @return converted item
     * @throws ItemConversionException on any error
     */
    public E stringToItem(String line);

}
