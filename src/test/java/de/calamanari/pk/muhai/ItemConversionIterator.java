//@formatter:off
/*
* ItemConversionIterator
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
package de.calamanari.pk.muhai;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * For working with files this ITERATOR returns the elements converted from the data the a buffered reader returns.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 * @param <E> element
 * @param <C> converter
 */
public class ItemConversionIterator<E, C extends ItemStringCodec<E>> implements Iterator<E> {

    /**
     * Wrapped reader
     */
    private final BufferedReader sourceReader;

    /**
     * converter
     */
    private final C codec;

    /**
     * read ahead buffer
     */
    private E bufferedElement = null;

    /**
     * signal that the end of the input has been reached
     */
    private boolean done = false;

    /**
     * Creates the iterator as a decorator for given reader
     * @param sourceReader
     * @param codec
     */
    public ItemConversionIterator(BufferedReader sourceReader, C codec) {
        this.sourceReader = sourceReader;
        this.codec = codec;
    }

    /**
     * Closes the underlying resources
     * @throws IOException
     */
    public void close() throws IOException {
        sourceReader.close();
    }

    @Override
    public boolean hasNext() {
        boolean res = false;
        if (bufferedElement != null) {
            res = true;
        }
        else {
            if (!done) {
                String line = null;
                try {
                    line = sourceReader.readLine();
                }
                catch (IOException ex) {
                    throw new ItemConversionException("Error converting items from reader", ex);
                }
                if (line != null) {
                    bufferedElement = codec.stringToItem(line);
                    res = true;
                }
                else {
                    done = true;
                }
            }
        }
        return res;

    }

    @Override
    public E next() {
        E res = bufferedElement;
        if (this.hasNext()) {
            bufferedElement = null;
        }
        else {
            throw new NoSuchElementException("End of input");
        }
        return res;
    }
}
