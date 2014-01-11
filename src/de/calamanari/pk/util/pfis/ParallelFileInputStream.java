/*
 * Parallel File Input Stream
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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.atomic.AtomicLong;

import de.calamanari.pk.util.MiscUtils;

/**
 * Parallel File Input Stream - input stream using a concurrent reader thread.<br>
 * This class is useful for reading large files especially if processing takes longer than reading. In this case the
 * reader thread can do a parallel read ahead while the main thread processes the data from the last chunk and so on.<br>
 * Because the behavior of java NIO can differ from OS to OS the caller can choose which buffer type to be used (see
 * {@link BufferType}).<br>
 * <b>Note:</b><br>
 * <ul>
 * <li>For small files this technique is not recommended and may result in slow processing.</li>
 * <li>Instances of ParallelFileInputStream MUST NOT be accessed concurrently.</li>
 * </ul>
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public final class ParallelFileInputStream extends InputStream {

    /**
     * Maximum buffer size, the default is {@value} bytes.
     */
    public static final int DEFAULT_MAX_BUFFER_SIZE = 52_428_800;

    /**
     * stores the maximum buffer size
     */
    private final int maxBufferSize;

    /**
     * file size determined when opening the file
     */
    private final long fileSize;

    /**
     * the current buffer
     */
    private ByteBuffer buffer = null;

    /**
     * Number of bytes returned to a client
     */
    private final AtomicLong bytesDelivered = new AtomicLong(0L);

    /**
     * The queue for communication from main thread to the reader The reader will not read a new buffer before being
     * requested.
     */
    private final BlockingQueue<BufferEvent> bufferRequestQueue = new ArrayBlockingQueue<>(5);

    /**
     * The queue for communication from reader thread to the main thread, the main thread waits for buffers delivered by
     * the reader thread.
     */
    private final BlockingQueue<BufferEvent> bufferDeliveryQueue = new ArrayBlockingQueue<>(5);

    /**
     * This is the file channel we work on, it is kept open during the life time
     */
    private final FileChannel fileChannel;

    /**
     * The position the reader has reached after the last call to a read method.
     */
    private long position = 0;

    /**
     * Separate reader thread
     */
    private final ReaderThread readerThread;

    /**
     * We support the mark() operation, this stores the mark position relative to the current buffer. If the mark
     * position is within the range of the current buffer, reset() is extremely cheap.
     */
    private int markPositionRel = -1;

    /**
     * We support the mark() operation for the whole file, if the mark position is outside the current buffer, we can
     * reach it using an absolute repositioning to this memorized position.
     */
    private long markPositionAbs = 0;

    /**
     * container for single byte, an internal variable just for recycling
     */
    private final byte[] singleByte = new byte[1];

    /**
     * We store the last exception that occurred.
     */
    private IOException readError = null;

    /**
     * Creates a new Memory Mapped Parallel File Input Stream on the given file using the specified buffer size.<br>
     * Note: A small buffer may lead to extremely bad performance!
     * @param file underlying source file
     * @param maxBufferSize maximum size a buffer may have (0 or less means {@link #DEFAULT_MAX_BUFFER_SIZE})
     * @param bufferType the type of buffer to be used, see {@link BufferType}
     * @return input stream
     * @throws IOException on file access error
     */
    public static ParallelFileInputStream createInputStream(File file, int maxBufferSize, BufferType bufferType)
            throws IOException {
        ParallelFileInputStream pfis = new ParallelFileInputStream(file, maxBufferSize, bufferType);
        pfis.readerThread.start();
        pfis.readNextPartitionParallel();
        return pfis;
    }

    /**
     * Creates a new Memory Mapped Parallel File Input Stream on the given file using the specified buffer size. Uses
     * memory mapped buffer (see {@link BufferType}).<br>
     * Note: A small buffer may lead to extremely bad performance!
     * @param file underlying source file
     * @param maxBufferSize maximum size a buffer may have (0 or less means {@link #DEFAULT_MAX_BUFFER_SIZE})
     * @return input stream
     * @throws IOException on file access error
     */
    public static ParallelFileInputStream createInputStream(File file, int maxBufferSize) throws IOException {
        return createInputStream(file, maxBufferSize, BufferType.MEMORY_MAPPED);
    }

    /**
     * Creates a new Memory Mapped Parallel File Input Stream on the given file using default buffer size (
     * {@link #DEFAULT_MAX_BUFFER_SIZE}). Uses memory mapped buffer (see {@link BufferType}).
     * @param file underlying source file
     * @return input stream
     * @throws IOException on file access error
     */
    public static ParallelFileInputStream createInputStream(File file) throws IOException {
        return createInputStream(file, DEFAULT_MAX_BUFFER_SIZE);
    }

    /**
     * Creates a new Memory Mapped Parallel File Input Stream on the given file using default buffer size (
     * {@link #DEFAULT_MAX_BUFFER_SIZE}).
     * @param file underlying source file
     * @param bufferType the type of buffer to be used, see {@link BufferType}
     * @return input stream
     * @throws IOException on file access error
     */
    public static ParallelFileInputStream createInputStream(File file, BufferType bufferType) throws IOException {
        return createInputStream(file, DEFAULT_MAX_BUFFER_SIZE, bufferType);
    }

    /**
     * Creates a new Memory Mapped Parallel File Input Stream on the given file using the specified buffer size.<br>
     * Note: A small buffer may lead to extremely bad performance!
     * @param fileName underlying source file's name
     * @param maxBufferSize maximum size a buffer may have
     * @param bufferType the type of buffer to be used, see {@link BufferType}
     * @return input stream
     * @throws IOException on file access error
     */
    public static ParallelFileInputStream createInputStream(String fileName, int maxBufferSize, BufferType bufferType)
            throws IOException {
        return createInputStream(new File(fileName), maxBufferSize, bufferType);
    }

    /**
     * Creates a new Memory Mapped Parallel File Input Stream on the given file using the specified buffer size. Uses
     * memory mapped buffer (see {@link BufferType}).<br>
     * Note: A small buffer may lead to extremely bad performance!
     * @param fileName underlying source file's name
     * @param maxBufferSize maximum size a buffer may have
     * @return input stream
     * @throws IOException on file access error
     */
    public static ParallelFileInputStream createInputStream(String fileName, int maxBufferSize) throws IOException {
        return createInputStream(new File(fileName), maxBufferSize);
    }

    /**
     * Creates a new Memory Mapped Parallel File Input Stream on the given file using default buffer size (
     * {@link #DEFAULT_MAX_BUFFER_SIZE}). Uses memory mapped buffer (see {@link BufferType}).
     * @param fileName underlying source file's name
     * @return input stream
     * @throws IOException on file access error
     */
    public static ParallelFileInputStream createInputStream(String fileName) throws IOException {
        return createInputStream(fileName, DEFAULT_MAX_BUFFER_SIZE);
    }

    /**
     * Creates a new Memory Mapped Parallel File Input Stream on the given file using default buffer size (
     * {@link #DEFAULT_MAX_BUFFER_SIZE}).
     * @param fileName underlying source file's name
     * @param bufferType the type of buffer to be used, see {@link BufferType}
     * @return input stream
     * @throws IOException on file access error
     */
    public static ParallelFileInputStream createInputStream(String fileName, BufferType bufferType) throws IOException {
        return createInputStream(fileName, DEFAULT_MAX_BUFFER_SIZE, bufferType);
    }

    /**
     * Creates a new Memory Mapped Parallel File Input Stream on the given file using the specified buffer size.<br>
     * Note: A small buffer may lead to extremely bad performance!
     * @param file underlying source file
     * @param maxBufferSize maximum size a buffer may have (0 or less means {@link #DEFAULT_MAX_BUFFER_SIZE})
     * @param bufferType the type of buffer to be used, see {@link BufferType}
     * @throws IOException on file access error
     */
    private ParallelFileInputStream(File file, int maxBufferSize, BufferType bufferType) throws IOException {
        if (maxBufferSize <= 0) {
            maxBufferSize = DEFAULT_MAX_BUFFER_SIZE;
        }

        fileSize = file.length();
        if (maxBufferSize > fileSize) {
            maxBufferSize = (int) fileSize;
        }
        this.maxBufferSize = maxBufferSize;

        fileChannel = (new RandomAccessFile(file, "r")).getChannel();

        // tell the reader thread to fetch the first partition
        try {
            bufferRequestQueue.put(new BufferEvent());
        }
        catch (InterruptedException ex) {
            throw new IOException("Unexpected interruption during queue setup.", ex);
        }

        this.readerThread = new ReaderThread(fileChannel, fileSize, bufferType, maxBufferSize, bufferRequestQueue,
                bufferDeliveryQueue);
    }

    /**
     * Returns the number of bytes that has been returned to the caller during read()-operations until now.<br>
     * This method does ignore skip() and mark(), so at the end of file the value may be smaller than the file size or
     * even greater.
     * @return number of bytes delivered to the caller
     */
    public long getNumberOfBytesDelivered() {
        return this.bytesDelivered.get();
    }

    /**
     * Checks whether there was a read error and evtl. rethrows it
     * @throws IOException on file access error
     */
    private void assertNoError() throws IOException {
        if (this.readError != null) {
            throw this.readError;
        }
    }

    /**
     * Checks whether there was a read error and evtl. rethrows it
     * @param bufferEvent an event that could have an error
     * @throws IOException on file access error
     */
    private void assertNoError(BufferEvent bufferEvent) throws IOException {
        if (this.readError != null) {
            if (bufferEvent.eventType == BufferEventType.ERROR) {
                handleError(bufferEvent.error);
            }
        }
        assertNoError();
    }

    /**
     * Take error, memorize it and rethrow IOException
     * @param error processing error to be memorized
     * @throws IOException on file access error
     */
    private void handleError(Throwable error) throws IOException {
        if (this.readError != null) {
            if (error instanceof IOException) {
                this.readError = (IOException) error;
            }
            else {
                this.readError = new IOException(error);
            }
        }
        assertNoError();
    }

    /**
     * Internal method to read the next partition.<br>
     * Buffers will be switched and the reader will be triggered to fill the other buffer asynchronously.
     * @throws IOException on file access error
     */
    private void readNextPartitionParallel() throws IOException {
        markPositionRel = -1;
        try {
            if (readerThread == null) {
                throw new IllegalStateException("Reader thread unavailable - check code!");
            }

            assertNoError();

            BufferEvent bufferEvent = bufferDeliveryQueue.take();

            assertNoError(bufferEvent);

            ByteBuffer recycleBuffer = this.buffer;
            this.buffer = bufferEvent.buffer;

            bufferEvent.buffer = recycleBuffer;

            // now tell the reader to read next part of file
            // we can recycle event and the old buffer
            bufferEvent.eventType = BufferEventType.REQUEST_NEXT;
            bufferRequestQueue.put(bufferEvent);

        }
        catch (Throwable t) {
            handleError(t);
        }
    }

    @Override
    public int read(byte[] b) throws IOException {
        return this.read(b, 0, b.length);
    }

    @Override
    public int read(byte[] b, int off, int len) throws IOException {

        assertNoError();

        if (off + len > b.length) {
            throw new IndexOutOfBoundsException("The given byte[" + b.length + "]-array is too small to store " + len
                    + " bytes starting at " + off + ".");
        }

        int lenReal = len;
        if (lenReal > fileSize - position) {
            lenReal = (int) (fileSize - position);
        }
        if (fileSize - position > 0) {
            if (buffer.remaining() < 1) {
                readNextPartitionParallel();
            }
            int lenTodo = lenReal;
            while (lenTodo > 0 && buffer.remaining() > 0) {
                int remaining = buffer.remaining();
                if (remaining > 0) {
                    int lenPart = lenTodo;
                    if (lenPart > remaining) {
                        lenPart = remaining;
                    }
                    lenTodo = lenTodo - lenPart;
                    buffer.get(b, off, lenPart);
                    off = off + lenPart;
                    position = position + lenPart;
                }
                if (lenTodo > 0) {
                    readNextPartitionParallel();
                }
            }
            lenReal = lenReal - lenTodo;
        }
        if (lenReal > 0) {
            bytesDelivered.addAndGet(lenReal);
            return lenReal;
        }
        else {
            return -1;
        }
    }

    /**
     * Repositions the pointer on the stream so that the next buffer access will read at the given absolute position<br>
     * If this given position is greater than file size, the pointer will be set at the end of file.
     * @param positionAbs absolute position in file (if greater than file size, we will reposition at EOF)
     * @throws IOException on file access error
     */
    public void repositionFileStream(long positionAbs) throws IOException {

        assertNoError();

        markPositionRel = -1;

        if (positionAbs > fileSize) {
            positionAbs = fileSize;
        }

        BufferEvent bufferRequest = new BufferEvent();
        bufferRequest.eventType = BufferEventType.REQUEST_REPOSITION;
        bufferRequest.startPositionAbs = positionAbs;
        bufferRequest.buffer = this.buffer;
        try {
            bufferRequestQueue.put(bufferRequest);
        }
        catch (InterruptedException ex) {
            handleError(ex);
        }

        BufferEvent answer = null;
        boolean done = false;
        do {
            try {
                answer = bufferDeliveryQueue.take();
            }
            catch (InterruptedException ex) {
                handleError(ex);
            }
            if (answer.eventType == BufferEventType.DELIVER_BUFFER && answer.repositionAnswer) {
                // this is the one we requested
                done = true;
            }
            else if (answer.eventType == BufferEventType.DELIVER_BUFFER) {
                // this is a part of file that was read BEFORE the repositioning
                // happened (useless data)
                // recycle the buffer by requesting the next part
                bufferRequest = answer;
                bufferRequest.eventType = BufferEventType.REQUEST_NEXT;
                try {
                    bufferRequestQueue.put(bufferRequest);
                }
                catch (InterruptedException ex) {
                    handleError(ex);
                }
            }
            else {
                done = true; // must be an error
            }
        } while (!done);

        assertNoError(answer);

        this.buffer = answer.buffer;
        position = positionAbs;
    }

    @Override
    public long skip(long bytesToBeSkipped) throws IOException {

        assertNoError();

        if (position + bytesToBeSkipped > fileSize) {
            bytesToBeSkipped = fileSize - position;
        }
        position = position + bytesToBeSkipped;

        long leftBytesToBeSkipped = bytesToBeSkipped;
        long remaining = buffer.remaining();
        if (remaining < bytesToBeSkipped) {
            leftBytesToBeSkipped = bytesToBeSkipped - remaining;
            if (leftBytesToBeSkipped > maxBufferSize) {
                repositionFileStream(position);
                leftBytesToBeSkipped = 0;
            }
            else {
                readNextPartitionParallel();
            }
        }
        buffer.position(buffer.position() + (int) leftBytesToBeSkipped);

        return bytesToBeSkipped;
    }

    @Override
    public int available() throws IOException {
        return buffer.remaining();
    }

    @Override
    public void close() throws IOException {

        try {
            readerThread.requestStopReading();
        }
        catch (Throwable t) {
            // ignore
        }

        try {
            BufferEvent bufferEvent = new BufferEvent();
            bufferEvent.eventType = BufferEventType.SHUTDOWN;
            bufferRequestQueue.put(bufferEvent);
        }
        catch (Throwable t) {
            // try it by interrupt
            readerThread.interrupt();
        }

        fileChannel.close();

        if (MiscUtils.isWindowsOS()) {
            this.buffer = ByteBuffer.allocate(0);
            System.gc();

            // What the hell ... ?!
            // The two lines above may seem a little esoteric,
            // but they can help to release files on windows.
            // I think this is a problem with memory mapped IO (buffer).
            // As long as the mapping is active, a file cannot
            // be deleted/renamed etc.
            //
            // The line
            // this.buffer = ByteBuffer.allocate(0);
            //
            // removes the strong reference to the old buffer
            // which (in principle) allows garbage collection.
            // I don't use null but an empty buffer because this
            // is more robust, I don't have to cope with
            // null-handling all over the code.
            //
            // The other line
            // System.gc();
            //
            // causes a hint to garbage collection.
            // However, this does not force the file release but it
            // highly increases the chance ...
        }

    }

    @Override
    public void mark(int readlimit) {
        this.markPositionAbs = position;
        this.markPositionRel = buffer.position();
    }

    @Override
    public void reset() throws IOException {

        assertNoError();

        if (markPositionRel > -1) {
            position = markPositionAbs;
            buffer.position(markPositionRel);
            markPositionRel = -1;
        }
        else {
            repositionFileStream(markPositionAbs);
        }
    }

    /**
     * Mark is supported.
     * {@inheritDoc}
     */
    @Override
    public boolean markSupported() {
        return true;
    }

    @Override
    public int read() throws IOException {
        int res = read(singleByte, 0, 1);
        if (res < 0) {
            return -1;
        }
        else {
            return singleByte[0];
        }
    }

    @Override
    protected void finalize() throws Throwable {
        try {
            this.close();
        }
        finally {
            super.finalize();
        }
    }

}
