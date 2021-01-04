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
import java.math.BigInteger;
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

import org.apfloat.Apfloat;
import org.apfloat.ApfloatMath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.calamanari.pk.util.CloseUtils;

public class KeyCollisionProcessor {

    private static final Logger LOGGER = LoggerFactory.getLogger(KeyCollisionProcessor.class);

    private List<BufferedReader> openChunkReaders = Collections.emptyList();

    public Summary scan(File outputDir, int maxKeysInMemory, int maxKeysInChunk, long sizeOfKeyspace, Supplier<Long> keyGenerator, long limit)
            throws IOException {

        LOGGER.info("Phase I: Key generation and chunked storage");
        LOGGER.info("Processing run with {} keys to chunk files at {} ...", limit, outputDir);
        long lastReportedPos = 0;
        SummaryBuilder summaryBuilder = SummaryBuilder.forKeyspaceSizeAndNumberOfKeysGenerated(sizeOfKeyspace, limit);
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
        LOGGER.info("{} keys generated", limit);
        LOGGER.info("Phase II: Collision detection");
        LOGGER.info("Merging and iterating over {} chunk files ...", chunkFiles.size());
        List<File> collisionKeyFiles = Collections.emptyList();
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
            collisionKeyFiles = ocfw.getChunkFiles();
        }
        finally {
            openChunkReaders.forEach(CloseUtils::closeResourceCatch);
            openChunkReaders.clear();
        }
        chunkFiles.forEach(File::delete);

        LOGGER.info("Phase III: Compute collision stats");
        LOGGER.info("Merging and iterating over {} chunk files ...", collisionKeyFiles.size());
        boolean firstCollision = true;

        try {
            collisionKeyFiles.forEach(this::openChunkReader);
            Collection<Iterator<KeyCollision>> chunkIterators = openChunkReaders.stream().map(this::toCollisionIterator).collect(Collectors.toList());
            CombinedOrderedItemIterator<KeyCollision> allCollisionsOrderedIterator = new CombinedOrderedItemIterator<KeyCollision>(chunkIterators);
            long lastCollisionReportedAt = 0;
            long collidedKeysTotal = 0;
            while (allCollisionsOrderedIterator.hasNext()) {
                collidedKeysTotal++;
                KeyCollision collision = allCollisionsOrderedIterator.next();
                summaryBuilder.addCollision(collision);
                if (limit < 1000 || firstCollision || collision.getPositions()[1] >= lastCollisionReportedAt + 100_000) {
                    LOGGER.info("Collision detected: {}, total number of collided keys: {}", collision, collidedKeysTotal);
                    lastCollisionReportedAt = collision.getPositions()[1];
                    firstCollision = false;
                }
            }
        }
        finally {
            openChunkReaders.forEach(CloseUtils::closeResourceCatch);
            openChunkReaders.clear();
        }
        collisionKeyFiles.forEach(File::delete);

        return summaryBuilder.getResult();
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
     * Computes the expected number of collisions after creating a number of random keys in a limited keyspace
     * @param sizeOfKeyspace
     * @param numberOfKeysGenerated
     * @return expected number of collisions
     */
    static long computeExpectedNumberOfCollisions(long sizeOfKeyspace, long numberOfKeysGenerated) {

        var m = val(sizeOfKeyspace);
        var one = val(1);
        var n = val(numberOfKeysGenerated);

        var q = one.subtract(one.divide(m));
        var qPowN = ApfloatMath.pow(q, n);

        // See formula c(m, n) depicted in the book
        var c = m.multiply(qPowN.subtract(q)).add(n.subtract(one));

        return c.longValue();
    }

    /**
     * Helper method to make code above more readable
     * @param l (treaded as unsigned integer value)
     * @return Apfloat with a precision of 500
     */
    static Apfloat val(long l) {
        if (l < 0) {
            int upper = (int) (l >>> 32);
            int lower = (int) l;

            BigInteger unsignedBigInteger = (BigInteger.valueOf(Integer.toUnsignedLong(upper))).shiftLeft(32)
                    .add(BigInteger.valueOf(Integer.toUnsignedLong(lower)));
            return new Apfloat(unsignedBigInteger, 500);
        }
        return new Apfloat(l, 500);
    }

    private static class SummaryBuilder {

        private Summary result = new Summary();

        static SummaryBuilder forKeyspaceSizeAndNumberOfKeysGenerated(long sizeOfKeyspace, long numberOfKeysGenerated) {
            SummaryBuilder res = new SummaryBuilder();
            res.result.setSizeOfKeyspace(sizeOfKeyspace);
            res.result.setNumberOfKeysGenerated(numberOfKeysGenerated);
            int numberOfDataPoints = 500;
            if (numberOfKeysGenerated < numberOfDataPoints) {
                numberOfDataPoints = (int) numberOfKeysGenerated;
            }
            long numberOfKeysPerSlot = numberOfKeysGenerated / numberOfDataPoints;
            for (int i = 0; i < numberOfDataPoints; i++) {
                // initalize every slot with the upper bound and the computed expected number of collisions
                long position = numberOfKeysPerSlot * (i + 1);
                res.result.getCollisionStats().add(new DataPoint(position, 0L, computeExpectedNumberOfCollisions(sizeOfKeyspace, position)));
            }
            return res;
        }

        SummaryBuilder addCollision(KeyCollision collision) {
            result.numberOfCollisions = result.numberOfCollisions + collision.getPositions().length - 1;
            result.numberOfCollidedKeys++;

            Long occurrenceCount = result.multiOccurrenceStats.get(collision.getPositions().length);
            if (occurrenceCount == null) {
                occurrenceCount = 1L;
            }
            else {
                occurrenceCount++;
            }
            result.multiOccurrenceStats.put(collision.getPositions().length, occurrenceCount);

            createOrUpdateDataPointForCollision(collision);
            return this;
        }

        /**
         * Final action to obtain the result, afterwards this builder becomes invalid
         * @return summary
         */
        Summary getResult() {
            finalizeDataPoints();
            Summary res = result;
            result = null;
            return res;
        }

        private void createOrUpdateDataPointForCollision(KeyCollision collision) {
            List<DataPoint> dataPoints = result.collisionStats;
            long[] positions = collision.getPositions();
            // start with the second to (first position is not a collision)
            for (int i = 1; i < positions.length; i++) {
                long pos = positions[i];
                int slot = (int) (pos % ((long) dataPoints.size()));
                DataPoint dataPoint = dataPoints.get(slot);
                DataPoint newDataPoint = new DataPoint(dataPoint.position, dataPoint.numberOfCollisionsDetected + 1, dataPoint.numberOfCollisionsExpected);
                dataPoints.set(slot, newDataPoint);
            }
        }

        private void finalizeDataPoints() {
            List<DataPoint> dataPoints = result.collisionStats;
            long numberOfCollisionsDetected = dataPoints.get(0).numberOfCollisionsDetected;
            for (int slot = 1; slot < dataPoints.size(); slot++) {
                DataPoint current = dataPoints.get(slot);
                numberOfCollisionsDetected = numberOfCollisionsDetected + current.numberOfCollisionsDetected;
                DataPoint newDataPoint = new DataPoint(current.position, numberOfCollisionsDetected, current.numberOfCollisionsExpected);
                dataPoints.set(slot, newDataPoint);
            }
        }

    }

    /**
     * result of the collision detection
     */
    public static class Summary {

        private long sizeOfKeyspace = 0;

        private long numberOfKeysGenerated = 0;

        private long numberOfCollidedKeys = 0;

        private long numberOfCollisions = 0;

        private List<DataPoint> collisionStats = new ArrayList<>();

        private Map<Integer, Long> multiOccurrenceStats = new TreeMap<>();

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

        public List<DataPoint> getCollisionStats() {
            return collisionStats;
        }

        public void setCollisionStats(List<DataPoint> collisionStats) {
            this.collisionStats = collisionStats;
        }

    }

    public static class DataPoint {

        public static final String HEADER = "Generated Keys;Detected Collisions;Expected Collisions";

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

        @Override
        public String toString() {
            return "" + position + ";" + numberOfCollisionsDetected + ";" + numberOfCollisionsExpected;
        }

    }
}
