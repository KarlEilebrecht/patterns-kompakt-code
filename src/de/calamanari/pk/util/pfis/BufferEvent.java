/*
 * Buffer Event
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
package de.calamanari.pk.util.pfis;

import java.nio.ByteBuffer;

/**
 * BufferEvents are for communication between reader thread and main thread and vice-versa.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
final class BufferEvent {

    /**
     * identifies the type of this event
     */
    public BufferEventType eventType = BufferEventType.REQUEST_NEXT;

    /**
     * new start position for repositioning (absolute file position)
     */
    public long startPositionAbs = 0L;

    /**
     * a reference to a buffer. <br>
     * The Reader sends a filled buffer to the main thread. <br>
     * The main thread may use this attribute to return an existing buffer for recycling.
     */
    public ByteBuffer buffer = null;

    /**
     * The reader will indicate this when delivering the first buffer after having repositioned the stream.
     */
    public boolean repositionAnswer = false;

    /**
     * if this is an error event, this is the exception that occurred while reading
     */
    public Throwable error = null;

}