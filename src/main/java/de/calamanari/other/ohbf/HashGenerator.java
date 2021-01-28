//@formatter:off
/*
 * HashGenerator 
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
package de.calamanari.other.ohbf;

import java.io.Serializable;

/**
 * Interface for generators to compute a hash value of fixed length (see {@link #getHashLength()}) over a number of attributes
 * <p>
 * Instances must be thread-safe.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public interface HashGenerator extends Serializable {

    /**
     * @param attributes content to be hashed
     * @return computed hash as a byte array
     */
    byte[] computeHashBytes(Object... attributes);

    /**
     * Returns the fixed length of the generated hash
     * @return number of bytes in the array returned by {@link #computeHashBytes(Object...)}
     */
    int getHashLength();
}
