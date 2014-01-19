/*
 * IndexedTextFileAccessorTest
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
package de.calamanari.pk.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.util.Arrays;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import de.calamanari.pk.util.LogUtils;
import de.calamanari.pk.util.MiscUtils;
import de.calamanari.pk.util.itfa.IndexedTextFileAccessor;

/**
 * IndexedTextFileAccessorTest - some tests for IndexedTextFileAccessor class
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class IndexedTextFileAccessorTest {

    /**
     * logger
     */
    private static final Logger LOGGER = Logger.getLogger(IndexedTextFileAccessorTest.class.getName());

    /**
     * Log-level for this test
     */
    private static final Level LOG_LEVEL = Level.INFO;

    /**
     * Defines whether the huge file (ca. 1GB) should be created for testing or not
     */
    private static final boolean HUGE_TEST_ALLOWED = false;

    /**
     * Charset for test, we use "UTF-8" to test multi-byte decoding
     */
    private static final String CHARSET_NAME = "UTF-8";

    /**
     * verly large file
     */
    private static File huge1000000LinesFile = null;

    /**
     * empty File
     */
    private static File emptyFile = null;

    /**
     * File with 3 empty lines only.
     */
    private static File onlyEmptyLines3File = null;

    /**
     * File with one character
     */
    private static File singleCharFile = null;

    /**
     * File with one line
     */
    private static File singleLineFile = null;

    /**
     * File with one line followed by new line
     */
    private static File singleLinePlusNewLineFile = null;

    /**
     * File with odd number (3) of characters
     */
    private static File oddChar3NumFile = null;

    /**
     * File with even number (4) of characters
     */
    private static File evenChar4NumFile = null;

    /**
     * File with odd number (3) of lines
     */
    private static File oddLine3NumFile = null;

    /**
     * File with even number (4) of lines
     */
    private static File evenLine4NumFile = null;

    /**
     * File with many (1000) lines
     */
    private static File multiLines1000File = null;

    /**
     * A UTF-8-file that only contains a surrogate pair
     */
    private static File surrogatePairFile = null;

    /**
     * A UTF-8-file that starts with a surrogate pair
     */
    private static File leadingSurrogatePairFile = null;

    /**
     * A UTF-8-file that ends with a surrogate pair
     */
    private static File trailingSurrogatePairFile = null;

    /**
     * A UTF-8-file that contains surrogate pairs mixed with normal characters
     */
    private static File miscSurrogatePairFile = null;

    /**
     * Test string
     */
    private static final String MULTI_LINE_BASE = " ----> \u00C4\u00D6\u00DC!";

    /**
     * Test string
     */
    private static final String HUGE_LINE_BASE = " --------> \u00C4\u00D6\u00DC!01234567890123456789";

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        LogUtils.setConsoleHandlerLogLevel(LOG_LEVEL);
        LogUtils.setLogLevel(LOG_LEVEL, IndexedTextFileAccessorTest.class, IndexedTextFileAccessor.class);

        emptyFile = createTextFile("emptyFile_" + CHARSET_NAME + ".txt", null, CHARSET_NAME);
        onlyEmptyLines3File = createTextFile("onlyEmptyLines3File_" + CHARSET_NAME + ".txt",
                Arrays.asList(new String[] { "", "\r" }), CHARSET_NAME);
        singleCharFile = createTextFile("singleCharFile_" + CHARSET_NAME + ".txt",
                Arrays.asList(new String[] { "\u00C4" }), CHARSET_NAME);
        singleLineFile = createTextFile("singleLineFile_" + CHARSET_NAME + ".txt",
                Arrays.asList(new String[] { "\u00C4 is a german letter." }), CHARSET_NAME);
        singleLinePlusNewLineFile = createTextFile("singleLinePlusNewLineFile_" + CHARSET_NAME + ".txt",
                Arrays.asList(new String[] { "\u00C4 is a german letter.", "" }), CHARSET_NAME);
        oddChar3NumFile = createTextFile("oddChar3NumFile_" + CHARSET_NAME + ".txt",
                Arrays.asList(new String[] { "\u00C4\u00D6\u00DC" }), CHARSET_NAME);
        evenChar4NumFile = createTextFile("evenChar4NumFile_" + CHARSET_NAME + ".txt",
                Arrays.asList(new String[] { "\u00C4\u00D6\u00DC!" }), CHARSET_NAME);
        oddLine3NumFile = createTextFile(
                "oddLine3NumFile_" + CHARSET_NAME + ".txt",
                Arrays.asList(new String[] { "\u00C4 is a german letter.", "\u00D6 is a german letter.",
                        "\u00DC is a german letter." }), CHARSET_NAME);
        evenLine4NumFile = createTextFile(
                "evenLine4NumFile_" + CHARSET_NAME + ".txt",
                Arrays.asList(new String[] { "\u00C4 is a german letter.", "\u00D6 is a german letter.",
                        "\u00DC is a german letter.", "Lorem ipsum." }), CHARSET_NAME);
        String[] manyLines = new String[1000];

        for (int i = 0; i < 1000; i++) {
            manyLines[i] = "" + i + MULTI_LINE_BASE;
        }

        surrogatePairFile = createTextFile("surrogatePairFile_UTF8.txt",
                Arrays.asList(new String[] { "\uD800\uDC00" }), "UTF-8");
        leadingSurrogatePairFile = createTextFile("leadingSurrogatePairFile_UTF8.txt",
                Arrays.asList(new String[] { "\uD800\uDC00ABC" }), "UTF-8");
        trailingSurrogatePairFile = createTextFile("trailingSurrogatePairFile_UTF8.txt",
                Arrays.asList(new String[] { "ABC\uD800\uDC00" }), "UTF-8");
        miscSurrogatePairFile = createTextFile("miscSurrogatePairFile_UTF8.txt",
                Arrays.asList(new String[] { "A\uD800\uDC00BC\uD911\uDC14E\uD912\uDC15F" }), "UTF-8");

        multiLines1000File = createTextFile("multiLines1000File_" + CHARSET_NAME + ".txt", Arrays.asList(manyLines),
                CHARSET_NAME);

        if (HUGE_TEST_ALLOWED) {
            long startTimeNanos = System.nanoTime();
            LOGGER.info("Creating huge file ... ");
            huge1000000LinesFile = createHugeFile("hugeFile1000000_" + CHARSET_NAME + ".txt");
            LOGGER.info("Huge file '" + huge1000000LinesFile + "' created after "
                    + MiscUtils.formatNanosAsSeconds(System.nanoTime() - startTimeNanos) + " s.");
        }

    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
        emptyFile.delete();
        onlyEmptyLines3File.delete();
        singleCharFile.delete();
        singleLineFile.delete();
        singleLinePlusNewLineFile.delete();
        oddChar3NumFile.delete();
        evenChar4NumFile.delete();
        oddLine3NumFile.delete();
        evenLine4NumFile.delete();
        multiLines1000File.delete();
        surrogatePairFile.delete();
        leadingSurrogatePairFile.delete();
        trailingSurrogatePairFile.delete();
        miscSurrogatePairFile.delete();
        if (HUGE_TEST_ALLOWED) {
            System.gc();
            Thread.sleep(5000);
            huge1000000LinesFile.delete();
            huge1000000LinesFile = null;
        }
    }

    @Test
    public void testEmptyFile() throws Exception {
        IndexedTextFileAccessor ifa = new IndexedTextFileAccessor(emptyFile, CHARSET_NAME);
        assertEquals(0, ifa.getFileSize());
        assertEquals(0, ifa.getNumberOfCharacters());
        assertEquals(0, ifa.getNumberOfLines());
        assertEquals(0, ifa.getNumberOfCharacterIndexEntries());
        assertEquals(0, ifa.getNumberOfLineIndexEntries());
    }

    @Test
    public void testOnlyEmptyLines3File() throws Exception {
        IndexedTextFileAccessor ifa = new IndexedTextFileAccessor(onlyEmptyLines3File, CHARSET_NAME);
        assertEquals(2, ifa.getFileSize());
        assertEquals(2, ifa.getNumberOfCharacters());
        assertEquals(3, ifa.getNumberOfLines());
        assertEquals(2, ifa.getNumberOfCharacterIndexEntries());
        assertEquals(3, ifa.getNumberOfLineIndexEntries());

        assertEquals("", readLineAndClose(ifa.createInputStreamReaderAtLine(1)));
        // implicit line after last character, must be null
        assertEquals(null, readLineAndClose(ifa.createInputStreamReaderAtLine(2)));
    }

    @Test
    public void testSingleCharFile() throws Exception {
        IndexedTextFileAccessor ifa = new IndexedTextFileAccessor(singleCharFile, CHARSET_NAME);
        assertEquals(2, ifa.getFileSize());
        assertEquals(1, ifa.getNumberOfCharacters());
        assertEquals(1, ifa.getNumberOfLines());
        assertEquals(1, ifa.getNumberOfCharacterIndexEntries());
        assertEquals(1, ifa.getNumberOfLineIndexEntries());
        assertEquals("\u00C4", readAndClose(ifa.createInputStreamReaderAtChar(0), 1));
    }

    @Test
    public void testSingleLineFile() throws Exception {
        IndexedTextFileAccessor ifa = new IndexedTextFileAccessor(singleLineFile, CHARSET_NAME);
        assertEquals(22, ifa.getFileSize());
        assertEquals(21, ifa.getNumberOfCharacters());
        assertEquals(1, ifa.getNumberOfLines());
        assertEquals(21, ifa.getNumberOfCharacterIndexEntries());
        assertEquals(1, ifa.getNumberOfLineIndexEntries());
    }

    @Test
    public void testSingleLinePlusNewLineFile() throws Exception {
        IndexedTextFileAccessor ifa = new IndexedTextFileAccessor(singleLinePlusNewLineFile, CHARSET_NAME);
        assertEquals(23, ifa.getFileSize());
        assertEquals(22, ifa.getNumberOfCharacters());
        assertEquals(2, ifa.getNumberOfLines());
        assertEquals(22, ifa.getNumberOfCharacterIndexEntries());
        assertEquals(2, ifa.getNumberOfLineIndexEntries());
    }

    @Test
    public void testOddChar3NumFile() throws Exception {
        IndexedTextFileAccessor ifa = new IndexedTextFileAccessor(oddChar3NumFile, CHARSET_NAME);
        assertEquals(6, ifa.getFileSize());
        assertEquals(3, ifa.getNumberOfCharacters());
        assertEquals(1, ifa.getNumberOfLines());
        assertEquals(3, ifa.getNumberOfCharacterIndexEntries());
        assertEquals(1, ifa.getNumberOfLineIndexEntries());
    }

    @Test
    public void testEvenChar4NumFile() throws Exception {
        IndexedTextFileAccessor ifa = new IndexedTextFileAccessor(evenChar4NumFile, CHARSET_NAME);
        assertEquals(7, ifa.getFileSize());
        assertEquals(4, ifa.getNumberOfCharacters());
        assertEquals(1, ifa.getNumberOfLines());
        assertEquals(4, ifa.getNumberOfCharacterIndexEntries());
        assertEquals(1, ifa.getNumberOfLineIndexEntries());
    }

    @Test
    public void testOddLine3NumFile() throws Exception {
        IndexedTextFileAccessor ifa = new IndexedTextFileAccessor(oddLine3NumFile, CHARSET_NAME);
        assertEquals(68, ifa.getFileSize());
        assertEquals(65, ifa.getNumberOfCharacters());
        assertEquals(3, ifa.getNumberOfLines());
        assertEquals(65, ifa.getNumberOfCharacterIndexEntries());
        assertEquals(3, ifa.getNumberOfLineIndexEntries());
    }

    @Test
    public void testEvenLine4NumFile() throws Exception {
        IndexedTextFileAccessor ifa = new IndexedTextFileAccessor(evenLine4NumFile, CHARSET_NAME);
        assertEquals(81, ifa.getFileSize()); // 3
        assertEquals(78, ifa.getNumberOfCharacters());
        assertEquals(4, ifa.getNumberOfLines());
        assertEquals(78, ifa.getNumberOfCharacterIndexEntries());
        assertEquals(4, ifa.getNumberOfLineIndexEntries());
    }

    @Test
    public void testSurrogatePairFile() throws Exception {
        IndexedTextFileAccessor ifa = new IndexedTextFileAccessor(surrogatePairFile, "UTF-8");
        assertEquals(4, ifa.getFileSize());
        assertEquals(2, ifa.getNumberOfCharacters());
        assertEquals(1, ifa.getNumberOfLines());
        assertEquals(1, ifa.getNumberOfCharacterIndexEntries());
        assertEquals(1, ifa.getNumberOfLineIndexEntries());
        assertEquals("\uD800\uDC00", readLineAndClose(ifa.createInputStreamReaderAtLine(0)));
        assertEquals("\uDC00", readLineAndClose(ifa.createInputStreamReaderAtChar(1)));
    }

    @Test
    public void testLeadingSurrogatePairFile() throws Exception {
        IndexedTextFileAccessor ifa = new IndexedTextFileAccessor(leadingSurrogatePairFile, "UTF-8");
        assertEquals(7, ifa.getFileSize());
        assertEquals(5, ifa.getNumberOfCharacters());
        assertEquals(1, ifa.getNumberOfLines());
        assertEquals(4, ifa.getNumberOfCharacterIndexEntries());
        assertEquals(1, ifa.getNumberOfLineIndexEntries());
        assertEquals("\uD800\uDC00ABC", readLineAndClose(ifa.createInputStreamReaderAtLine(0)));
        assertEquals("\uDC00ABC", readLineAndClose(ifa.createInputStreamReaderAtChar(1)));
        assertEquals("ABC", readLineAndClose(ifa.createInputStreamReaderAtChar(2)));
        assertEquals("C", readLineAndClose(ifa.createInputStreamReaderAtChar(4)));
    }

    @Test
    public void testTrailingSurrogatePairFile() throws Exception {
        IndexedTextFileAccessor ifa = new IndexedTextFileAccessor(trailingSurrogatePairFile, "UTF-8");
        assertEquals(7, ifa.getFileSize());
        assertEquals(5, ifa.getNumberOfCharacters());
        assertEquals(1, ifa.getNumberOfLines());
        assertEquals(4, ifa.getNumberOfCharacterIndexEntries());
        assertEquals(1, ifa.getNumberOfLineIndexEntries());
        assertEquals("ABC\uD800\uDC00", readLineAndClose(ifa.createInputStreamReaderAtLine(0)));
        assertEquals("BC\uD800\uDC00", readLineAndClose(ifa.createInputStreamReaderAtChar(1)));
        assertEquals("\uDC00", readLineAndClose(ifa.createInputStreamReaderAtChar(4)));
    }

    @Test
    public void testMiscSurrogatePairFile() throws Exception {
        IndexedTextFileAccessor ifa = new IndexedTextFileAccessor(miscSurrogatePairFile, "UTF-8");
        assertEquals(17, ifa.getFileSize());
        assertEquals(11, ifa.getNumberOfCharacters());
        assertEquals(1, ifa.getNumberOfLines());
        assertEquals(8, ifa.getNumberOfCharacterIndexEntries());
        assertEquals(1, ifa.getNumberOfLineIndexEntries());
        assertEquals("A\uD800\uDC00BC\uD911\uDC14E\uD912\uDC15F",
                readLineAndClose(ifa.createInputStreamReaderAtLine(0)));
        assertEquals("\uD800\uDC00BC\uD911\uDC14E\uD912\uDC15F", readLineAndClose(ifa.createInputStreamReaderAtChar(1)));
        assertEquals("\uDC00BC\uD911\uDC14E\uD912\uDC15F", readLineAndClose(ifa.createInputStreamReaderAtChar(2)));
        assertEquals("BC\uD911\uDC14E\uD912\uDC15F", readLineAndClose(ifa.createInputStreamReaderAtChar(3)));
        assertEquals("F", readLineAndClose(ifa.createInputStreamReaderAtChar(10)));
    }

    @Test
    public void testMultiLines1000File() throws Exception {
        // use a too small index
        IndexedTextFileAccessor ifa = new IndexedTextFileAccessor(multiLines1000File, CHARSET_NAME, 250, 250, 0);
        assertEquals(17889, ifa.getFileSize());
        assertEquals(14889, ifa.getNumberOfCharacters());
        assertEquals(1000, ifa.getNumberOfLines());
        assertEquals(248, ifa.getNumberOfCharacterIndexEntries());
        assertEquals(245, ifa.getNumberOfLineIndexEntries());

        for (int i = 0; i < 1000; i++) {
            assertEquals("" + i + MULTI_LINE_BASE, readLineAndClose(ifa.createInputStreamReaderAtLine(i)));
        }

        assertEquals(MULTI_LINE_BASE, readLineAndClose(ifa.createInputStreamReaderAtChar(132)));

    }

    @Test
    public void test1000000LinesFile() throws Exception {
        if (HUGE_TEST_ALLOWED) {
            long startTimeNanos = System.nanoTime();
            LOGGER.info("Creating IndexedTextFileAccessor ... ");
            IndexedTextFileAccessor ifa = new IndexedTextFileAccessor(huge1000000LinesFile, CHARSET_NAME);
            LOGGER.info("Index ready after " + MiscUtils.formatNanosAsSeconds(System.nanoTime() - startTimeNanos)
                    + " s.");
            assertEquals(1146888889, ifa.getFileSize());
            assertEquals(1056888889, ifa.getNumberOfCharacters());
            assertEquals(1000000, ifa.getNumberOfLines());
            assertTrue(9990 < ifa.getNumberOfCharacterIndexEntries());
            assertTrue(9980 < ifa.getNumberOfLineIndexEntries());
            assertEquals("---> \u00C4\u00D6\u00DC!01234567890123456789",
                    readLineAndClose(ifa.createInputStreamReaderAtChar(999052)));
            String s = readLineAndClose(ifa.createInputStreamReaderAtLine(999999));
            assertEquals(1056, s.length());
            assertEquals("999999 --------> \u00C4\u00D6\u00DC!01234567890123456789", s.substring(0, 41));
        }

    }

    /**
     * helper to read some characters and close the file
     * @param isr reader from index
     * @param length characters to read
     * @return string with characters
     * @throws Exception
     */
    private String readAndClose(Reader isr, int length) throws Exception {
        StringBuilder sb = new StringBuilder();
        try {
            for (int i = 0; i < length; i++) {
                sb.append((char) isr.read());
            }
        }
        finally {
            MiscUtils.closeResourceCatch(isr);
        }
        return sb.toString();
    }

    /**
     * helper to read some characters and close the file
     * @param isr reader from index
     * @param length characters to read
     * @return string with characters
     * @throws Exception
     */
    private String readLineAndClose(Reader isr) throws Exception {
        String res = null;
        try {
            BufferedReader br = new BufferedReader(isr);
            res = br.readLine();
        }
        finally {
            MiscUtils.closeResourceCatch(isr);
        }
        return res;
    }

    /**
     * Method to create test file
     * @param fileName name of file (path will be home)
     * @param lines the lines of the file, may be empty/null
     * @param charsetName name of the character set
     * @return the created file
     * @throws Exception on any error
     */
    public static File createTextFile(String fileName, Collection<String> lines, String charsetName) throws Exception {
        File file = new File(MiscUtils.getHomeDirectory(), fileName);
        BufferedWriter bw = null;
        try {
            FileOutputStream fos = new FileOutputStream(file);
            OutputStreamWriter osw = new OutputStreamWriter(fos, charsetName);
            bw = new BufferedWriter(osw);
            int count = 0;
            if (lines != null) {
                for (String line : lines) {
                    if (count > 0) {
                        // unix-style
                        bw.append('\n');
                    }
                    bw.append(line);
                    count++;
                }
            }
        }
        finally {
            MiscUtils.closeResourceCatch(bw);
        }
        return file;
    }

    /**
     * Creates the huge file with 1000000 lines
     * @param fileName name of file without path
     * @return new file
     * @throws Exception
     */
    private static File createHugeFile(String fileName) throws Exception {
        File file = new File(MiscUtils.getHomeDirectory(), fileName);

        StringBuilder line = new StringBuilder();
        for (int i = 0; i < 30; i++) {
            line.append(HUGE_LINE_BASE);
        }
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file), CHARSET_NAME), 1048576);
            for (int i = 0; i < 1000000; i++) {
                if (i > 0) {
                    // unix-style
                    bw.append('\n');
                }
                bw.append("" + i);
                bw.append(line);
            }
        }
        finally {
            MiscUtils.closeResourceCatch(bw);
        }
        return file;
    }

}
