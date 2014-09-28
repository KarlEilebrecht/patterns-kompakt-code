//@formatter:off
/*
 * CRC32 Checksum Builder - concrete checksum BUILDER implementation using CRC32.
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
package de.calamanari.pk.builder;

import java.util.logging.Logger;
import java.util.zip.CRC32;

import de.calamanari.pk.util.MiscUtils;

/**
 * CRC32 Checksum Builder - concrete checksum BUILDER implementation using CRC32.<br>
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class Crc32ChecksumBuilder implements ChecksumBuilder {

    /**
     * logger
     */
    protected static final Logger LOGGER = Logger.getLogger(Crc32ChecksumBuilder.class.getName());

    /**
     * Checksum type key
     */
    public static final String CHECKSUM_TYPE = "CRC32";

    /**
     * Instead of NULL this is used
     */
    private static final String NULL_REPLACEMENT = "!+NULL";

    /**
     * If the null-replacement occurs, this one is used instead.
     */
    private static final String NULL_NULL_REPLACEMENT = "!!+NULL";

    /**
     * internal reference to checksum method
     */
    private final CRC32 crc32 = new CRC32();

    /**
     * Creates new checksum builder
     */
    public Crc32ChecksumBuilder() {

    }

    @Override
    public void addString(String text) {
        LOGGER.fine("addString('" + text + "')");
        this.addStringInternal(text);
    }

    @Override
    public void addLong(Long value) {
        LOGGER.fine("addLong(" + value + ")");
        if (value == null) {
            this.addStringInternal(null);
        }
        else {
            this.addStringInternal(Long.toHexString(value));
        }
    }

    @Override
    public void addInteger(Integer value) {
        LOGGER.fine("addInteger(" + value + ")");
        if (value == null) {
            this.addStringInternal(null);
        }
        else {
            this.addStringInternal(Integer.toHexString(value));
        }
    }

    @Override
    public void addDouble(Double value) {
        LOGGER.fine("addDouble(" + value + ")");
        if (value == null) {
            this.addStringInternal(null);
        }
        else {
            this.addStringInternal(Double.toHexString(value));
        }
    }

    @Override
    public void addBytes(byte[] bytes) {
        LOGGER.fine("addBytes(" + (bytes == null ? 0 : bytes.length) + ")");
        addBytesInternal(bytes);
    }

    /**
     * Adds the string to the checksum
     * 
     * @param text some text
     */
    private void addStringInternal(String text) {
        if (text == null) {
            text = NULL_NULL_REPLACEMENT;
        }
        else {
            text = text.replace(NULL_REPLACEMENT, NULL_NULL_REPLACEMENT);
        }
        this.addBytesInternal(MiscUtils.getUtf8Bytes(text));
    }

    /**
     * Adds the bytes
     * 
     * @param bytes bytes to be added
     */
    private void addBytesInternal(byte[] bytes) {
        if (bytes != null) {
            crc32.update(bytes);
        }
    }

    @Override
    public Checksum getChecksum() {
        LOGGER.fine("creating now the result (Checksum)");
        Checksum checksum = new Checksum(CHECKSUM_TYPE, crc32.getValue());
        LOGGER.fine("returning checksum (" + checksum + ")");
        return checksum;
    }

    @Override
    public void reset() {
        crc32.reset();
    }

}
