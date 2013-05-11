/*
 * Buffer Event Type
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
package de.calamanari.pk.util.pfis;

/**
 * The BufferEventType identifies the type of event in one of the queues for communication between main thread and
 * reader thread
 * @author <a href="mailto:Karl.Eilebrecht(a/t)web.de">Karl Eilebrecht</a>
 */
enum BufferEventType {

    /**
     * the reader shall provide the next part of the file, starting at the position the last part ended
     */
    REQUEST_NEXT,

    /**
     * The reader shall reposition, the position can be obtained from the event's properties
     */
    REQUEST_REPOSITION,

    /**
     * The reader shall stop reading and close resources
     */
    SHUTDOWN,

    /**
     * The reader has provided another part of the file, the corresponding buffer is attached.
     */
    DELIVER_BUFFER,

    /**
     * The reader got an error and has closed resources.
     */
    ERROR

}