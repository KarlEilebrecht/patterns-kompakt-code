/*
 * Reader Thread
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

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Logger;

import de.calamanari.pk.util.MiscUtils;

/**
 * The reader thread reads the stream concurrently to the main thread.<br>
 * This is achieved by switching buffers. While one buffer is in use (consumer reads) the other one can be filled.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)web.de">Karl Eilebrecht</a>
 */
final class ReaderThread extends Thread {

    /**
     * logger
     */
    private static final Logger LOGGER = Logger.getLogger(ReaderThread.class.getName());

    /**
     * The queue for communication from main thread to the reader The reader will not read a new buffer before being
     * requested.
     */
    private final BlockingQueue<BufferEvent> bufferRequestQueue;

    /**
     * The queue for communication from reader thread to the main thread, the main thread waits for buffers delivered by
     * the reader thread.
     */
    private final BlockingQueue<BufferEvent> bufferDeliveryQueue;

    /**
     * stores the maximum buffer size
     */
    private final int maxBufferSize;

    /**
     * configuration of the buffer type to be used
     */
    private final BufferType bufferType;

    /**
     * file size determined when opening the file
     */
    private final long fileSize;

    /**
     * This is the file channel we work on, it is kept open during the life time
     */
    private final FileChannel fileChannel;

    /**
     * where to read next part of file
     */
    private long startNextPartition = 0;

    /**
     * flag to indicate reader is enabled
     */
    private volatile boolean reading = true;

    /**
     * Creates new reader thread without starting it, yet.
     * @param fileChannel the file channel we work on, it is kept open during the life time
     * @param fileSize file size determined when opening the file
     * @param bufferType configuration of the buffer type to be used
     * @param maxBufferSize stores the current maximum buffer size
     * @param bufferRequestQueue queue for communication from main thread to the reader
     * @param bufferDeliveryQueue queue for communication from reader thread to the main thread (answer)
     */
    ReaderThread(FileChannel fileChannel, long fileSize, BufferType bufferType, int maxBufferSize,
            BlockingQueue<BufferEvent> bufferRequestQueue, BlockingQueue<BufferEvent> bufferDeliveryQueue) {
        this.fileChannel = fileChannel;
        this.fileSize = fileSize;
        this.bufferType = bufferType;
        this.maxBufferSize = maxBufferSize;
        this.bufferRequestQueue = bufferRequestQueue;
        this.bufferDeliveryQueue = bufferDeliveryQueue;
        this.setDaemon(true);
    }

    /**
     * Allows the main thread to tell the reader thread to stop reading (asynchronous call, may take time to complete)
     */
    void requestStopReading() {
        this.reading = false;
    }

    @Override
    public void run() {
        try {
            while (reading) {
                BufferEvent bufferEvent = bufferRequestQueue.take();
                if (bufferEvent.eventType == BufferEventType.SHUTDOWN) {
                    break;
                }
                else {
                    processBufferEvent(bufferEvent);
                }
            }
        }
        catch (Throwable t) {
            BufferEvent bufferEvent = new BufferEvent();
            bufferEvent.eventType = BufferEventType.ERROR;
            bufferEvent.error = t;
            try {
                t.printStackTrace();
                bufferDeliveryQueue.put(bufferEvent);
            }
            catch (Throwable ex) {
                // this is extremely fatal, hope this will never happen
                LOGGER.severe("Unexpected error during critical queue operation, maybe consumer hangs now.");
            }
        }
        finally {
            MiscUtils.closeResourceCatch(fileChannel);
        }
    }

    /**
     * Processes an incoming buffer (request) event for reading data from file
     * @param bufferEvent incoming event
     * @throws IOException on file access error
     * @throws InterruptedException pass-through from waiting
     */
    private void processBufferEvent(BufferEvent bufferEvent) throws IOException, InterruptedException {
        if (bufferEvent.eventType == BufferEventType.REQUEST_NEXT
                || bufferEvent.eventType == BufferEventType.REQUEST_REPOSITION) {

            // set position if requested and prepare answer
            if (bufferEvent.eventType == BufferEventType.REQUEST_REPOSITION) {
                this.startNextPartition = bufferEvent.startPositionAbs;
                bufferEvent.repositionAnswer = true;
            }
            bufferEvent.eventType = BufferEventType.DELIVER_BUFFER;

            long bufferSize = maxBufferSize;

            if (startNextPartition + bufferSize > fileSize) {
                bufferSize = fileSize - startNextPartition;
            }

            if (bufferSize > 0) {
                fillBuffer(bufferEvent, bufferSize);
            }
            else {
                clearBuffer(bufferEvent);
            }

            bufferDeliveryQueue.put(bufferEvent);

        }
        else {
            throw new IllegalStateException("Unexpected buffer event " + bufferEvent.eventType);
        }
    }

    /**
     * This method fills the buffer of the given event instance from the underlying file channel
     * @param bufferEvent currently processed event
     * @param bufferSize number of requested bytes to be read from file
     * @throws IOException on file access error
     */
    private void fillBuffer(BufferEvent bufferEvent, long bufferSize) throws IOException {
        if (bufferType == BufferType.MEMORY_MAPPED) {
            bufferEvent.buffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, startNextPartition, bufferSize);
            ((MappedByteBuffer) bufferEvent.buffer).load();
        }
        else {
            fileChannel.position(startNextPartition);
            if (bufferEvent.buffer == null) {
                if (bufferType == BufferType.DIRECT) {
                    bufferEvent.buffer = ByteBuffer.allocateDirect(maxBufferSize);
                }
                else {
                    bufferEvent.buffer = ByteBuffer.allocate(maxBufferSize);
                }
            }
            else {
                bufferEvent.buffer.clear();
            }
            ByteBuffer[] buffers = new ByteBuffer[] { bufferEvent.buffer };

            fileChannel.read(buffers, 0, 1);
            bufferEvent.buffer.rewind();
        }
        startNextPartition = startNextPartition + bufferSize;
    }

    /**
     * This method clears the buffer, the strategy for this operation depends on the buffer's type
     * @param bufferEvent event that carries the buffer to be cleared
     */
    private void clearBuffer(BufferEvent bufferEvent) {
        if (bufferType == BufferType.MEMORY_MAPPED) {
            bufferEvent.buffer = ByteBuffer.allocate(0);
        }
        else {
            if (bufferEvent.buffer == null) {
                if (bufferType == BufferType.DIRECT) {
                    bufferEvent.buffer = ByteBuffer.allocateDirect(maxBufferSize);
                }
                else {
                    bufferEvent.buffer = ByteBuffer.allocate(maxBufferSize);
                }
            }
            else {
                bufferEvent.buffer.clear();
            }
        }
    }
}