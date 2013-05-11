/*
 * Parallel File Input Stream Test
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
package de.calamanari.pk.util.test;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.FileOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import de.calamanari.pk.util.LogUtils;
import de.calamanari.pk.util.MiscUtils;
import de.calamanari.pk.util.pfis.BufferType;
import de.calamanari.pk.util.pfis.ParallelFileInputStream;

/**
 * Parallel File Input Stream Test - tests for ParallelFileInputStream
 * @author <a href="mailto:Karl.Eilebrecht(a/t)web.de">Karl Eilebrecht</a>
 */
public class ParallelFileInputStreamTest {

    /**
     * logger
     */
    protected static final Logger LOGGER = Logger.getLogger(ParallelFileInputStreamTest.class.getName());

    /**
     * Log-level for this test
     */
    private static final Level LOG_LEVEL = Level.INFO;

    /**
     * empty File
     */
    private static File emptyFile = null;

    /**
     * file with one byte
     */
    private static File oneByteFile = null;

    /**
     * file with two bytes
     */
    private static File twoBytesFile = null;

    /**
     * file with ten bytes
     */
    private static File tenBytesFile = null;

    /**
     * file with eleven bytes
     */
    private static File elevenBytesFile = null;

    /**
     * file with thirty bytes
     */
    private static File thirtyBytesFile = null;

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        LogUtils.setConsoleHandlerLogLevel(LOG_LEVEL);
        LogUtils.setLogLevel(LOG_LEVEL, ParallelFileInputStreamTest.class, ParallelFileInputStream.class);
        emptyFile = createBytesFile(0);
        oneByteFile = createBytesFile(1);
        twoBytesFile = createBytesFile(2);
        tenBytesFile = createBytesFile(10);
        elevenBytesFile = createBytesFile(11);
        thirtyBytesFile = createBytesFile(30);
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        emptyFile.delete();
        oneByteFile.delete();
        twoBytesFile.delete();
        tenBytesFile.delete();
        elevenBytesFile.delete();
        thirtyBytesFile.delete();
    }

    @Test
    public void testWithEmptyFile() throws Exception {
        for (BufferType bufferType : BufferType.values()) {
            testInternal(emptyFile, 1, bufferType);
            testInternal(emptyFile, 10, bufferType);
        }
    }

    @Test
    public void testWithOneByteFile() throws Exception {
        for (BufferType bufferType : BufferType.values()) {
            testInternal(oneByteFile, 1, bufferType);
            testInternal(oneByteFile, 10, bufferType);
        }
    }

    @Test
    public void testWithTwoBytesFile() throws Exception {
        for (BufferType bufferType : BufferType.values()) {
            testInternal(twoBytesFile, 1, bufferType);
            testInternal(twoBytesFile, 10, bufferType);
        }
    }

    @Test
    public void testWithTenBytesFile() throws Exception {
        for (BufferType bufferType : BufferType.values()) {
            testInternal(tenBytesFile, 1, bufferType);
            testInternal(tenBytesFile, 10, bufferType);
        }
    }

    @Test
    public void testWithElevenBytesFile() throws Exception {
        for (BufferType bufferType : BufferType.values()) {
            testInternal(elevenBytesFile, 1, bufferType);
            testInternal(elevenBytesFile, 10, bufferType);
        }
    }

    @Test
    public void testWithThirtyBytesFile() throws Exception {
        for (BufferType bufferType : BufferType.values()) {
            testInternal(thirtyBytesFile, 1, bufferType);
            testInternal(thirtyBytesFile, 10, bufferType);
        }
    }

    /**
     * Test with the given input file and the specified buffer size
     * @param inputFile
     * @param bufferSize
     */
    private void testInternal(File inputFile, int bufferSize, BufferType bufferType) throws Exception {
        ParallelFileInputStream mis = ParallelFileInputStream.createInputStream(inputFile, bufferSize, bufferType);

        long len = inputFile.length();

        int bMis = 0;
        int count = 0;
        while ((bMis = mis.read()) != -1) {
            assertEquals(count, bMis);
            count++;
        }
        assertEquals(len, count);

        mis.repositionFileStream(0);
        bMis = 0;
        count = 0;
        while ((bMis = mis.read()) != -1) {
            assertEquals(count, bMis);
            count++;
        }
        assertEquals(len, count);

        if (len >= 10 && bufferSize >= 10) {
            mis.repositionFileStream(0);
            byte[] buf = new byte[10];

            count = 0;
            int haveRead = 0;
            while ((haveRead = mis.read(buf)) > 0) {
                for (int i = 0; i < haveRead; i++) {
                    assertEquals((count + i), buf[i]);
                }
                count = count + haveRead;
            }
            assertEquals(len, count);

            mis.repositionFileStream(0);

            count = 0;
            for (int i = 0; i < 10; i++) {
                mis.read();
                count++;
            }
            mis.mark(1000);
            while ((bMis = mis.read()) != -1) {
                assertEquals(count, bMis);
                count++;
            }
            mis.reset();
            count = 10;
            while ((bMis = mis.read()) != -1) {
                assertEquals(count, bMis);
                count++;
            }

            mis.repositionFileStream(0);
            mis.skip(10);
            count = 10;
            while ((bMis = mis.read()) != -1) {
                assertEquals(count, bMis);
                count++;
            }
        }
        mis.close();
    }

    /**
     * Creates a file of the given size, each written byte equals the sequence number (starting with 0)
     * @param size 0..255
     */
    private static File createBytesFile(int size) throws Exception {
        File res = new File(MiscUtils.getHomeDirectory(), "testBytes" + size);
        FileOutputStream fos = new FileOutputStream(res);
        for (int i = 0; i < size; i++) {
            fos.write(i);
        }
        fos.close();
        return res;
    }

}
