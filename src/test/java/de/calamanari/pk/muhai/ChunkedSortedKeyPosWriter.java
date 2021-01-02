//@formatter:off
/*
 * ChunkedSortedKeyPosWriter
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
package de.calamanari.pk.muhai;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

public class ChunkedSortedKeyPosWriter {

    /**
     * The '@' (ASCII 64) delimits key and position in a row
     */
    public static final byte KEY_POS_DELIMITER = (byte) 64;

    private static final byte[] LINE_SEPARATOR = System.lineSeparator().getBytes(StandardCharsets.US_ASCII);

    private final int maxKeysInMemory;

    private final int maxKeysInChunk;

    private final File outputDir;

    private final List<KeyAtPos> buffer;

    private final List<File> chunkFiles = new ArrayList<>();

    private long numberOfKeysInChunk = 0;

    private int chunkNumber = 0;

    private long numberOfKeysTotal = 0;

    public ChunkedSortedKeyPosWriter(File outputDir, int maxKeysInMemory, int maxKeysInChunk) {
        this.maxKeysInChunk = maxKeysInChunk;
        this.maxKeysInMemory = maxKeysInMemory;
        this.outputDir = outputDir;
        this.buffer = new ArrayList<>(maxKeysInMemory);
    }

    private void writeBufferedKeysToNewChunkFile() throws IOException {
        chunkNumber++;
        String chunkId = "00000" + chunkNumber;
        chunkId = chunkId.substring(chunkId.length() - 5);

        File chunkFile = new File(outputDir, String.join("", "chunk-", chunkId, ".gz"));
        try (FileOutputStream fos = new FileOutputStream(chunkFile);
                BufferedOutputStream bos = new BufferedOutputStream(fos);
                GZIPOutputStream gos = new GZIPOutputStream(bos)) {
            for (int i = 0; i < buffer.size(); i++) {
                KeyAtPos key = buffer.get(i);
                String keyString = Long.toUnsignedString(key.getKey());
                String posString = Long.toUnsignedString(key.getPos());
                if (i > 0) {
                    gos.write(LINE_SEPARATOR);
                }
                gos.write(keyString.getBytes(StandardCharsets.US_ASCII));
                gos.write(KEY_POS_DELIMITER);
                gos.write(posString.getBytes(StandardCharsets.US_ASCII));
            }
        }
        chunkFiles.add(chunkFile);
        numberOfKeysInChunk = numberOfKeysInChunk + buffer.size();
        buffer.clear();
    }

    private void mergeBufferedKeysIntoCurrentChunkFile() throws IOException {
        File chunkFile = chunkFiles.get(chunkNumber - 1);
        File tmpFile = new File(outputDir, chunkFile.getName() + "-merge");
        if (!chunkFile.renameTo(tmpFile)) {
            throw new IOException(String.format("Unable to prepare merge, could not rename %s to %s", chunkFile.getAbsolutePath(), tmpFile.toString()));
        }
        try (FileOutputStream fos = new FileOutputStream(chunkFile);
                BufferedOutputStream bos = new BufferedOutputStream(fos);
                GZIPOutputStream gos = new GZIPOutputStream(bos);
                FileInputStream fis = new FileInputStream(tmpFile);
                BufferedInputStream bis = new BufferedInputStream(fis);
                GZIPInputStream gis = new GZIPInputStream(bis);
                InputStreamReader isr = new InputStreamReader(gis, StandardCharsets.US_ASCII);
                BufferedReader br = new BufferedReader(isr)) {
            KeyAtPos currentKeyFromFile = null;
            KeyAtPos currentKeyFromBuffer = null;

        }
    }

    private KeyAtPos convertLine(String line) throws IOException {
        KeyAtPos res = null;
        if (line != null) {
            try {
                int delimPos = line.indexOf(KEY_POS_DELIMITER);
                long key = Long.parseUnsignedLong(line.substring(0, delimPos));
                long pos = Long.parseUnsignedLong(line.substring(delimPos + 1));
                res = new KeyAtPos(key, pos);
            }
            catch (RuntimeException ex) {
                throw new IOException(String.format("Unable to convert line, expected <key:long>%s<pos:long>, found: %s", ((char) KEY_POS_DELIMITER), line));
            }
        }
        return res;
    }

}
