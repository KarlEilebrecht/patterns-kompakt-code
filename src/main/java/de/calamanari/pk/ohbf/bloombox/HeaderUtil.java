//@formatter:off
/*
 * HeaderUtil
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

package de.calamanari.pk.ohbf.bloombox;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicLong;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.util.StdDateFormat;

/**
 * {@link HeaderUtil} is a utility collection to deal with headers when serializing/de-serializing bloom boxes and data stores.
 * <p>
 * To simplify the analysis of existing files headers are very simple, just single-line jsons.
 * <p>
 * The BBX-format consists of 4 parts, separated by {@link #LINE_FEED}:
 * <ol>
 * <li>Bloom box header, single line of json</li>
 * <li>Fully qualified name of the data store header class</li>
 * <li>Data store header, single line of json, to be de-serialized into the given class</li>
 * <li>Data format of the store, usually BBS, means just all longs from the vectors encoded big-endian.</li>
 * </ol>
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class HeaderUtil {

    /**
     * line feed to separate headers (each header is a single line)
     */
    public static final int LINE_FEED = 10;

    private HeaderUtil() {
        // utility
    }

    /**
     * Reads the header bytes until the next {@link #LINE_FEED}
     * 
     * @param is source stream
     * @return raw bytes of the header
     * @throws IOException on any error
     */
    public static byte[] readStreamHeader(InputStream is) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream(10_000);
        int consumed = -1;
        while ((consumed = is.read()) > -1) {
            if (consumed != LINE_FEED) {
                bos.write(consumed);
            }
            else {
                break;
            }
        }
        return bos.toByteArray();
    }

    /**
     * Writes the stream header
     * 
     * @param os destination
     * @param header bytes to write, MUST NOT contain {@link #LINE_FEED}
     * @return number of bytes written
     * @throws IOException
     */
    public static int writeStreamHeader(OutputStream os, byte[] header) throws IOException {
        if (header == null || header.length == 0) {
            throw new IOException("Header cannot be empty, given: " + Arrays.toString(header));
        }
        ByteArrayInputStream bis = new ByteArrayInputStream(header);
        int consumed = -1;
        while ((consumed = bis.read()) > -1) {
            if (consumed != LINE_FEED) {
                os.write(consumed);
            }
            else {
                throw new IOException("Header must not contain code 10 (LF), given: " + Arrays.toString(header));
            }
        }
        os.write(LINE_FEED);
        return header.length + 1;
    }

    /**
     * @return object mapper with the preferred settings to get smooth output
     */
    private static ObjectMapper createObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        objectMapper.setPropertyNamingStrategy(new PropertyNamingStrategies.SnakeCaseStrategy());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        // date format is ISO8601 for better readablility
        objectMapper.setDateFormat(new StdDateFormat().withColonInTimeZone(true));
        return objectMapper;
    }

    /**
     * Converts the given header to a byte array (JSON, UTF-8 encoded)
     * 
     * @param header to be encoded
     * @return byte array
     */
    public static byte[] headerToBytes(Object header) {
        try {
            return createObjectMapper().writeValueAsString(header).getBytes(StandardCharsets.UTF_8);
        }
        catch (JsonProcessingException | RuntimeException ex) {
            throw new BloomBoxException(String.format("Error converting header to byte array, given: %s", header), ex);
        }
    }

    /**
     * Restores the header of the specified type from the given byte array
     * 
     * @param <T> requested type
     * @param bytes serialized header (UTF-8-encoded JSON)
     * @param headerType class information for restoring the header
     * @return re-created header
     */
    public static <T> T bytesToHeader(byte[] bytes, Class<T> headerType) {
        String headerString = null;
        try {
            headerString = new String(bytes, StandardCharsets.UTF_8);
            return createObjectMapper().readValue(headerString, headerType);
        }
        catch (JsonProcessingException | RuntimeException ex) {
            throw new BloomBoxException(String.format("Error converting bytes (%s) to header of type %s, given: %s", headerString, headerType, bytes), ex);
        }

    }

    /**
     * Fetches the specific data store header from the given stream
     * 
     * @param <T> requested header type
     * @param is input stream
     * @param position byte position in stream, will be incremented
     * @return restored header
     * @throws IOException on any error
     */
    public static <T extends DataStoreHeader> T readDataStoreHeader(InputStream is, AtomicLong position) throws IOException {
        try {
            byte[] headerBytes = readStreamHeader(is);
            position.addAndGet(headerBytes.length + 1L);
            String headerClassName = new String(headerBytes, StandardCharsets.UTF_8);
            @SuppressWarnings("unchecked")
            Class<T> clazz = (Class<T>) Class.forName(headerClassName);
            headerBytes = readStreamHeader(is);
            position.addAndGet(headerBytes.length + 1L);
            return bytesToHeader(headerBytes, clazz);

        }
        catch (ClassNotFoundException | RuntimeException ex) {
            throw new IOException("Unable to read data store header", ex);
        }
    }

    /**
     * Writes the file store header to the stream
     * 
     * @param os destination
     * @param header to be encoded and written
     * @return total number of bytes written
     * @throws IOException on any error
     */
    public static int writeDataStoreHeader(OutputStream os, DataStoreHeader header) throws IOException {
        int numberOfBytesWritten = writeStreamHeader(os, header.getClass().getName().getBytes(StandardCharsets.UTF_8));
        return numberOfBytesWritten + writeStreamHeader(os, headerToBytes(header));
    }

    /**
     * Writes the header of the bloom box (bbx format)
     * 
     * @param os destination
     * @param header the header
     * @return number of bytes written
     * @throws IOException on any error
     */
    public static int writeBloomBoxHeader(OutputStream os, BloomBoxHeader header) throws IOException {
        return writeStreamHeader(os, headerToBytes(header));
    }

    /**
     * Fetches the {@link BloomBoxHeader} from the stream
     * 
     * @param is input stream
     * @param position byte position in stream, will be incremented
     * @return re-created header
     * @throws IOException on any error
     */
    public static BloomBoxHeader readBloomBoxHeader(InputStream is, AtomicLong position) throws IOException {
        byte[] headerBytes = readStreamHeader(is);
        position.addAndGet(headerBytes.length + 1L);
        return bytesToHeader(headerBytes, BloomBoxHeader.class);
    }
}
