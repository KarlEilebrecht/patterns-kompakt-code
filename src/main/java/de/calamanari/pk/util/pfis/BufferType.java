//@formatter:off
/*
 * Parallel File Input Stream Buffer Type
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
package de.calamanari.pk.util.pfis;

/**
 * The {@link ParallelFileInputStream} supports three types of buffers.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public enum BufferType {

    /**
     * Typical byte buffer<br>
     * <b>Caution:</b> The buffer will reside on the VM-heap.
     */
    NON_DIRECT,

    /**
     * Direct byte buffer<br>
     * Typically direct buffers do reside outside normal VM-heap, but (implementation dependent) may reside inside.
     */
    DIRECT,

    /**
     * A memory-mapped byte buffer resides outside the normal VM-heap, this is the recommended type for high performance reading.
     */
    MEMORY_MAPPED
}