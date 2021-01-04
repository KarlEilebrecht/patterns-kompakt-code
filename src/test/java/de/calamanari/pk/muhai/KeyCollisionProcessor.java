//@formatter:off
/*
 * KeyCollisionProcessor
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
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.zip.GZIPInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.calamanari.pk.util.CloseUtils;

public class KeyCollisionProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(KeyCollisionProcessor.class);

    private List<BufferedReader> openChunkReaders = Collections.emptyList();

    public Summary scan(long sizeOfKeyspace, Supplier<Long> keyGenerator, long limit, File outputDir, int maxKeysInMemory, int maxKeysInChunk)
            throws IOException {

        LOGGER.info("Phase I: Key generation and chunked storage");
        LOGGER.info("Processing run with {} keys to chunk files at {} ...", limit, outputDir);
        long lastReportedPos = 0;
        Summary res = new Summary();
        res.setSizeOfKeyspace(sizeOfKeyspace);
        List<File> chunkFiles = null;
        try (OrderedChunkFilewriter<KeyAtPos> ocfw = new OrderedChunkFilewriter<>(KeyAtPos.LINE_CODEC, outputDir, "keys-", maxKeysInMemory, maxKeysInChunk)) {
            for (long pos = 0; pos < limit; pos++) {
                ocfw.writeItem(new KeyAtPos(keyGenerator.get(), pos));
                if (pos >= lastReportedPos + 10_000_000) {
                    LOGGER.info("{} keys generated", pos);
                    lastReportedPos = pos;
                }
            }
            ocfw.flush();
            chunkFiles = ocfw.getChunkFiles();
        }
        res.setNumberOfKeysGenerated(limit);
        LOGGER.info("{} keys generated", limit);
        LOGGER.info("Phase II: Collision detection");
        LOGGER.info("Merging and iterating over {} chunk files ...", chunkFiles.size());
        try (OrderedChunkFilewriter<KeyCollision> ocfw = new OrderedChunkFilewriter<>(KeyCollision.LINE_CODEC, outputDir, "collisions-", maxKeysInMemory,
                maxKeysInChunk)) {
            chunkFiles.forEach(this::openChunkReader);
            Collection<Iterator<KeyAtPos>> chunkIterators = openChunkReaders.stream().map(this::toKeyIterator).collect(Collectors.toList());
            CombinedOrderedItemIterator<KeyAtPos> allKeysOrderedIterator = new CombinedOrderedItemIterator<KeyAtPos>(chunkIterators);
            KeyCollisionIterator collisionIterator = new KeyCollisionIterator(allKeysOrderedIterator);
            while (collisionIterator.hasNext()) {
                ocfw.writeItem(collisionIterator.next());
            }
            ocfw.flush();
            res.setCollisionKeyFiles(ocfw.getChunkFiles());
        }
        finally {
            openChunkReaders.forEach(CloseUtils::closeResourceCatch);
            openChunkReaders.clear();
        }
        chunkFiles.forEach(File::delete);
        LOGGER.info("Phase III: Compute collision stats");
        LOGGER.info("Merging and iterating over {} chunk files ...", res.collisionKeyFiles.size());
        boolean firstCollision = true;

        try {
            chunkFiles.forEach(this::openChunkReader);
            Collection<Iterator<KeyCollision>> chunkIterators = openChunkReaders.stream().map(this::toCollisionIterator).collect(Collectors.toList());
            CombinedOrderedItemIterator<KeyCollision> allCollisionsOrderedIterator = new CombinedOrderedItemIterator<KeyCollision>(chunkIterators);
            long lastCollisionReportedAt = 0;
            while (allCollisionsOrderedIterator.hasNext()) {
                KeyCollision collision = allCollisionsOrderedIterator.next();
                res.addCollisionToStats(collision);
                if (firstCollision || collision.getPositions()[1] >= lastCollisionReportedAt + 100_000) {
                    LOGGER.info("Collision detected: {}, {} collisions found in total, {} keys involved", collision, res.numberOfCollisions,
                            res.numberOfCollidedKeys);
                    lastCollisionReportedAt = collision.getPositions()[1];
                    firstCollision = false;
                }
            }
        }
        finally {
            openChunkReaders.forEach(CloseUtils::closeResourceCatch);
            openChunkReaders.clear();
        }

        return res;
    }

    private Iterator<KeyCollision> toCollisionIterator(BufferedReader br) {
        return new ItemConversionIterator<KeyCollision, ItemStringCodec<KeyCollision>>(br, KeyCollision.LINE_CODEC);
    }

    private Iterator<KeyAtPos> toKeyIterator(BufferedReader br) {
        return new ItemConversionIterator<KeyAtPos, ItemStringCodec<KeyAtPos>>(br, KeyAtPos.LINE_CODEC);
    }

    private void openChunkReader(File chunkFile) {
        FileInputStream fis = null;
        BufferedReader res = null;
        try {
            fis = new FileInputStream(chunkFile);
            BufferedInputStream bis = new BufferedInputStream(fis);
            GZIPInputStream gis = new GZIPInputStream(bis);
            InputStreamReader isr = new InputStreamReader(gis, StandardCharsets.UTF_8);
            res = new BufferedReader(isr);
        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        finally {
            if (res == null && fis != null) {
                CloseUtils.closeResourceCatch(fis);
            }
        }
        this.openChunkReaders.add(res);
    }

    /**
     * result of the collision detection
     */
    public static class Summary {

        private long sizeOfKeyspace = 0;

        private long numberOfKeysGenerated = 0;

        private long numberOfCollidedKeys = 0;

        private long numberOfCollisions = 0;

        private List<File> collisionKeyFiles = new ArrayList<>();

        private File collisionStatsFile = null;

        private Map<Integer, Long> multiOccurrenceStats = new TreeMap<>();

        Summary() {
            // default constructor
        }

        public long getNumberOfKeysGenerated() {
            return numberOfKeysGenerated;
        }

        public void setNumberOfKeysGenerated(long numberOfKeysGenerated) {
            this.numberOfKeysGenerated = numberOfKeysGenerated;
        }

        public long getNumberOfCollidedKeys() {
            return numberOfCollidedKeys;
        }

        public void setNumberOfCollidedKeys(long numberOfCollidedKeys) {
            this.numberOfCollidedKeys = numberOfCollidedKeys;
        }

        public long getNumberOfCollisions() {
            return numberOfCollisions;
        }

        public void setNumberOfCollisions(long numberOfCollisions) {
            this.numberOfCollisions = numberOfCollisions;
        }

        public List<File> getCollisionKeyFiles() {
            return collisionKeyFiles;
        }

        public void setCollisionKeyFiles(List<File> collisionKeyFiles) {
            this.collisionKeyFiles = collisionKeyFiles;
        }

        public File getCollisionStatsFile() {
            return collisionStatsFile;
        }

        public void setCollisionStatsFile(File collisionStatsFile) {
            this.collisionStatsFile = collisionStatsFile;
        }

        public Map<Integer, Long> getMultiOccurrenceStats() {
            return multiOccurrenceStats;
        }

        public void setMultiOccurrenceStats(Map<Integer, Long> multiOccurrenceStats) {
            this.multiOccurrenceStats = multiOccurrenceStats;
        }

        public long getSizeOfKeyspace() {
            return sizeOfKeyspace;
        }

        public void setSizeOfKeyspace(long sizeOfKeyspace) {
            this.sizeOfKeyspace = sizeOfKeyspace;
        }

        void addCollisionToStats(KeyCollision collision) {
            numberOfCollisions = numberOfCollisions + collision.getPositions().length - 1;
            numberOfCollidedKeys++;

            Long occurrenceCount = multiOccurrenceStats.get(collision.getPositions().length);
            if (occurrenceCount == null) {
                occurrenceCount = 1L;
            }
            else {
                occurrenceCount++;
            }
            multiOccurrenceStats.put(collision.getPositions().length, occurrenceCount);

        }
    }

    public static class DataPoint {

        private final long position;

        private final long numberOfCollisionsDetected;

        private final long numberOfCollisionsExpected;

        public DataPoint(long position, long numberOfCollisionsDetected, long numberOfCollisionsExpected) {
            super();
            this.position = position;
            this.numberOfCollisionsDetected = numberOfCollisionsDetected;
            this.numberOfCollisionsExpected = numberOfCollisionsExpected;
        }

        public long getPosition() {
            return position;
        }

        public long getNumberOfCollisionsDetected() {
            return numberOfCollisionsDetected;
        }

        public long getNumberOfCollisionsExpected() {
            return numberOfCollisionsExpected;
        }

    }
}
