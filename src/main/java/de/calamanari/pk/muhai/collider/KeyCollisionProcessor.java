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
package de.calamanari.pk.muhai.collider;

import static de.calamanari.pk.util.LambdaSupportLoggerProxy.defer;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.function.LongSupplier;
import java.util.zip.GZIPInputStream;

import org.apfloat.Apfloat;
import org.apfloat.ApfloatMath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.calamanari.pk.util.CloseUtils;
import de.calamanari.pk.util.LambdaSupportLoggerProxy;

/**
 * A {@link KeyCollisionProcessor} generates a specified number of keys provided by a supplier in a keyspace and analyzes key collisions.
 * <p>
 * To support really large number of keys, this processor uses the disk to store the keys during operation.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class KeyCollisionProcessor<K extends KeyCollision<K>> {

    private static final Logger LOGGER = LambdaSupportLoggerProxy.wrap(LoggerFactory.getLogger(KeyCollisionProcessor.class));

    /**
     * For limiting log output of a single process step
     */
    private static final long MAX_PROGRESS_MESSAGES = 500;

    /**
     * For limiting stats we do not collect more collision data points, it also limits the stats map (distinct count of occurrences)
     */
    private static final int MAX_DATA_POINTS = 500;

    /**
     * for writing temp files (large data)
     */
    private final File outputDir;

    /**
     * defines how many keys we want to store in memory before writing to disk
     */
    private final int maxKeysInMemory;

    /**
     * defines the size (number of items) of a single file on the disk, it should be a multiple of {@link #maxKeysInMemory}
     */
    private final int maxKeysInChunk;

    /**
     * Defines how to handle collected collisions
     */
    private final KeyCollisionCollectionPolicy<K> keyCollisionCollectionPolicy;

    /**
     * If true, we won't do any cleanup
     */
    private final boolean keepFiles;

    /**
     * Builder for the result report data
     */
    private SummaryBuilder summaryBuilder = null;

    /**
     * Currently open files
     */
    private List<BufferedReader> openChunkReaders = new ArrayList<>();

    /**
     * The limit in the current run
     */
    private long numberOfKeysToBeGenerated = 0;

    /**
     * To avoid too much log output we compute a threshold to limit log statements
     */
    private long reportingThreshold = 0;

    /**
     * total number of keys having any collisions
     */
    private long numberOfKeysInCollision = 0;

    /**
     * Creates a processor with default settings:
     * <ul>
     * <li>5M items in memory</li>
     * <li>25M items per chunk</li>
     * <li>{@link KeyCollisionCollectionPolicies#TRACK_POSITIONS_AND_DISCARD_KEYS}</li>
     * <li>keepFiles=false (cleanup after successful processing)</li>
     * </ul>
     * 
     * @param outputDir storage location
     * @return default processor instance
     */
    public static KeyCollisionProcessor<AnonymousTrackingKeyCollision> createDefaultProcessor(File outputDir) {
        return new KeyCollisionProcessor<>(outputDir, 5_000_000, 25_000_000, KeyCollisionCollectionPolicies.TRACK_POSITIONS_AND_DISCARD_KEYS, false);
    }

    /**
     * Creates a new processor with the given environment settings
     * 
     * @param outputDir storage location
     * @param maxKeysInMemory defines how many keys we want to store in memory before writing to disk
     * @param maxKeysInChunk size (number of items) of a single file on the disk, it should be a multiple of maxKeysInMemory
     * @param keyCollisionCollectionPolicy policy for storing/handling key collections
     * @param keepFiles leave all temporary processing files in place for further analysis
     */
    public KeyCollisionProcessor(File outputDir, int maxKeysInMemory, int maxKeysInChunk, KeyCollisionCollectionPolicy<K> keyCollisionCollectionPolicy,
            boolean keepFiles) {
        this.outputDir = outputDir;
        this.maxKeysInMemory = maxKeysInMemory;
        this.maxKeysInChunk = maxKeysInChunk;
        this.keyCollisionCollectionPolicy = keyCollisionCollectionPolicy;
        this.keepFiles = keepFiles;
    }

    /**
     * This method takes the specified number of keys from the given supplier and reports occurrences of the same key
     * 
     * @param keySupplier key generator
     * @param limit keys to be generated
     * @param sizeOfKeyspace the total size of the keyspace (for computing the expected collions)
     * @return collision summary
     * @throws IOException on any problem with the file system
     */
    public KeyCollisionSummary process(LongSupplier keySupplier, long limit, long sizeOfKeyspace) throws IOException {

        summaryBuilder = SummaryBuilder.forKeyspaceSizeAndNumberOfKeysGenerated(sizeOfKeyspace, limit);
        this.numberOfKeysToBeGenerated = limit;

        this.reportingThreshold = Math.max(1L, (limit / MAX_PROGRESS_MESSAGES));

        List<File> chunkFiles = generateKeys(keySupplier);
        List<File> collisionKeyFiles = detectCollisions(chunkFiles);
        computeCollisionStats(collisionKeyFiles);

        return summaryBuilder.getResult();
    }

    /**
     * Phase I: create the keys and store them in chunk files
     * 
     * @param keySupplier key generator
     * @return list of created chunk files
     * @throws IOException on any problem with the file system
     */
    private List<File> generateKeys(LongSupplier keySupplier) throws IOException {
        List<File> chunkFiles = null;
        LOGGER.info("Phase I: Key generation and chunked storage");
        LOGGER.info("Phase I: Processing run with {} keys to chunk files at {} ...", numberOfKeysToBeGenerated, outputDir);
        long lastReportedPos = 0;
        try (OrderedChunkFilewriter<KeyAtPos> ocfw = new OrderedChunkFilewriter<>(KeyAtPos.LINE_CODEC, outputDir, "keys-", maxKeysInMemory, maxKeysInChunk)) {

            for (long pos = 0; Long.compareUnsigned(pos, numberOfKeysToBeGenerated) < 0; pos++) {
                ocfw.writeItem(new KeyAtPos(keySupplier.getAsLong(), pos));
                if (Long.compareUnsigned(pos, lastReportedPos + reportingThreshold) >= 0) {
                    final long posF = pos;
                    LOGGER.info("Phase I: {} / {} keys generated ({} %) ...", defer(() -> Long.toUnsignedString(posF)),
                            defer(() -> Long.toUnsignedString(numberOfKeysToBeGenerated)),
                            defer(() -> formatPercentage(computePercentage(posF, numberOfKeysToBeGenerated))));
                    lastReportedPos = pos;
                }
            }
            ocfw.flush();
            chunkFiles = ocfw.getChunkFiles();
        }
        LOGGER.info("Phase I: {} keys generated into {} chunk files", defer(() -> Long.toUnsignedString(numberOfKeysToBeGenerated)), chunkFiles.size());
        return chunkFiles;
    }

    /**
     * Phase II: Iterate over all keys in key-order and group occurrences
     * 
     * @param keyChunkFiles input
     * @return list of collision chunk files
     * @throws IOException on any error with the file system
     */
    private List<File> detectCollisions(List<File> keyChunkFiles) throws IOException {
        LOGGER.info("Phase II: Collision detection");
        LOGGER.info("Phase II: Merging and iterating over {} chunk files ...", keyChunkFiles.size());
        List<File> collisionKeyFiles = Collections.emptyList();
        try (OrderedChunkFilewriter<K> ocfw = new OrderedChunkFilewriter<>(keyCollisionCollectionPolicy.getLineCodec(), outputDir, "collisions-",
                maxKeysInMemory, maxKeysInChunk)) {

            CollisionAggregationProgressObserver observer = new CollisionAggregationProgressObserver();

            keyChunkFiles.forEach(this::openAndRegisterChunkReader);
            Collection<Iterator<KeyAtPos>> chunkIterators = openChunkReaders.stream().map(this::toKeyIterator).toList();
            CombinedOrderedItemIterator<KeyAtPos> allKeysOrderedIterator = new CombinedOrderedItemIterator<>(chunkIterators);

            KeyCollisionIterator<K> collisionIterator = new KeyCollisionIterator<>(allKeysOrderedIterator, keyCollisionCollectionPolicy,
                    observer::reportCollisionAggregationProgress);

            while (collisionIterator.hasNext()) {
                numberOfKeysInCollision++;
                K item = collisionIterator.next();
                ocfw.writeItem(item);
            }
            ocfw.flush();
            collisionKeyFiles = ocfw.getChunkFiles();
        }
        finally {
            openChunkReaders.forEach(CloseUtils::closeResourceCatch);
            openChunkReaders.clear();
        }
        if (!keepFiles) {
            LOGGER.info("Phase II: Cleaning-up key files ...");
            keyChunkFiles.forEach(this::deleteChunkFile);
        }
        else {
            LOGGER.info("Phase II: Leaving key chunk files on disk.");
        }
        LOGGER.info("Phase II: Detected {} of {} keys involved in collisions", defer(() -> Long.toUnsignedString(numberOfKeysInCollision)),
                defer(() -> Long.toUnsignedString(numberOfKeysToBeGenerated)));
        return collisionKeyFiles;
    }

    /**
     * Phase III: Iterate over the collisions ordered by occurrence and create statistics
     * 
     * @param keyCollisionChunkFiles collision chunk files
     */
    private void computeCollisionStats(List<File> keyCollisionChunkFiles) {
        LOGGER.info("Phase III: Compute collision stats");
        LOGGER.info("Phase III: Merging and iterating over {} chunk files ...", keyCollisionChunkFiles.size());
        boolean firstCollision = true;

        try {
            keyCollisionChunkFiles.forEach(this::openAndRegisterChunkReader);
            Collection<Iterator<K>> chunkIterators = openChunkReaders.stream().map(this::toCollisionIterator).toList();
            CombinedOrderedItemIterator<K> allCollisionsOrderedIterator = new CombinedOrderedItemIterator<>(chunkIterators);
            long lastCollisionReportedAt = 0;
            long collidedKeysProcessed = 0;

            while (allCollisionsOrderedIterator.hasNext()) {
                collidedKeysProcessed++;
                K collision = allCollisionsOrderedIterator.next();
                summaryBuilder.addCollision(collision);
                long collisionPos = collision.getFirstCollisionPosition();
                if (firstCollision || Long.compareUnsigned(collisionPos, lastCollisionReportedAt + reportingThreshold) >= 0) {
                    final long collidedKeysProcessedF = collidedKeysProcessed;
                    LOGGER.info("Phase III: {} / {} collided keys processed ({} %) ...", defer(() -> Long.toUnsignedString(collidedKeysProcessedF)),
                            defer(() -> Long.toUnsignedString(numberOfKeysInCollision)),
                            defer(() -> formatPercentage(computePercentage(collidedKeysProcessedF, numberOfKeysInCollision))));
                    lastCollisionReportedAt = collisionPos;
                    firstCollision = false;
                }
            }
        }
        finally {
            openChunkReaders.forEach(CloseUtils::closeResourceCatch);
            openChunkReaders.clear();
        }
        if (!keepFiles) {
            LOGGER.info("Phase III: Cleaning-up collision chunk files ...");
            keyCollisionChunkFiles.forEach(this::deleteChunkFile);
        }
        else {
            LOGGER.info("Phase III: Leaving collision chunk files on disk.");
        }

        LOGGER.info("Phase III: Collision stats collected.");
    }

    /**
     * @param chunkFile to be deleted
     */
    private void deleteChunkFile(File chunkFile) {
        try {
            Files.delete(chunkFile.toPath());
        }
        catch (IOException ex) {
            LOGGER.error("Unable to delete chunk file {}", chunkFile);
        }
    }

    /**
     * @param br buffered reader for a chunk file (collisions)
     * @return iterator
     */
    private Iterator<K> toCollisionIterator(BufferedReader br) {
        return new ItemConversionIterator<>(br, keyCollisionCollectionPolicy.getLineCodec());
    }

    /**
     * @param br buffered reader for a chunk file (keys)
     * @return iterator
     */
    private Iterator<KeyAtPos> toKeyIterator(BufferedReader br) {
        return new ItemConversionIterator<>(br, KeyAtPos.LINE_CODEC);
    }

    /**
     * Creates a reader for the given chunk and puts it into the list
     * 
     * @param chunkFile file, a buffered reader shall be created for
     */
    // suppressing this try-with-resource sonar rule because this method is intended to supply open resources
    @SuppressWarnings("java:S2093")
    private void openAndRegisterChunkReader(File chunkFile) {
        FileInputStream fis = null;
        BufferedReader res = null;
        try {
            fis = new FileInputStream(chunkFile);
            BufferedInputStream bis = new BufferedInputStream(fis, 500_000);
            GZIPInputStream gis = new GZIPInputStream(bis);
            InputStreamReader isr = new InputStreamReader(gis, StandardCharsets.UTF_8);
            res = new BufferedReader(isr);
        }
        catch (IOException ex) {
            throw new KeyCollisionProcessException("Error opening chunk file reader for " + chunkFile, ex);
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
     * 
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
     * Helper method to make computation formula code more readable
     * 
     * @param l (treaded as unsigned integer value)
     * @return Apfloat with a precision of 500
     */
    static Apfloat val(long l) {
        if (l < 0) {
            return new Apfloat(toUnsignedBigInteger(l), 500);
        }
        return new Apfloat(l, 500);
    }

    /**
     * Shorthand for formatting completion status
     * 
     * @param perc percentage
     * @return percentage value with two decimal digits as a string
     */
    static String formatPercentage(double perc) {
        NumberFormat nf = NumberFormat.getInstance(Locale.US);
        nf.setMinimumFractionDigits(2);
        nf.setMaximumFractionDigits(2);
        return nf.format(perc);
    }

    /**
     * compute percentage of completion
     * 
     * @param processed number of items processed
     * @param total number of items total
     * @return percentage value
     */
    static double computePercentage(long processed, long total) {
        BigInteger processedCollisions = toUnsignedBigInteger(processed).multiply(BigInteger.valueOf(10000));
        BigInteger totalCollisions = toUnsignedBigInteger(total);
        return processedCollisions.divide(totalCollisions).doubleValue() / 100;
    }

    /**
     * Converts the given long into an unsigned big integer value for further computation
     * 
     * @param l source value (negative included)
     * @return unsigned big integer (negatives become positive after {@link Long#MAX_VALUE})
     */
    static BigInteger toUnsignedBigInteger(long l) {
        int upper = (int) (l >>> 32);
        int lower = (int) l;
        return (BigInteger.valueOf(Integer.toUnsignedLong(upper))).shiftLeft(32).add(BigInteger.valueOf(Integer.toUnsignedLong(lower)));
    }

    /**
     * OBSERVER to frequently display progress information during collision aggregation
     */
    private class CollisionAggregationProgressObserver {

        /**
         * state of the progress observer
         */
        private double lastPercReported = 0;

        /**
         * logs the progress information
         * 
         * @param consumed number of items read from the source
         * @param returned number of aggregated items returned
         */
        private void reportCollisionAggregationProgress(long consumed, long returned) {
            double perc = computePercentage(consumed, numberOfKeysToBeGenerated);
            if (perc >= lastPercReported + 1) {
                LOGGER.info("Phase II: {} collided keys detected, {} / {} keys processed ({} %) ...", defer(() -> Long.toUnsignedString(returned)),
                        defer(() -> Long.toUnsignedString(consumed)), defer(() -> Long.toUnsignedString(numberOfKeysToBeGenerated)),
                        defer(() -> formatPercentage(perc)));
                lastPercReported = perc;
            }
        }

    }

    /**
     * This BUILDER helps to subsequently fill the summary from the different processing steps
     * 
     * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
     *
     */
    static class SummaryBuilder {

        /**
         * summary to be built
         */
        private KeyCollisionSummary result = new KeyCollisionSummary();

        /**
         * for computation
         */
        private long numberOfKeysPerSlot = 0;

        /**
         * @param sizeOfKeyspace numbers of keys in total
         * @param numberOfKeysGenerated number of keys we will process
         * @return this builder
         */
        static SummaryBuilder forKeyspaceSizeAndNumberOfKeysGenerated(long sizeOfKeyspace, long numberOfKeysGenerated) {
            SummaryBuilder res = new SummaryBuilder();
            res.result.setSizeOfKeyspace(sizeOfKeyspace);
            res.result.setNumberOfKeysGenerated(numberOfKeysGenerated);
            int numberOfDataPoints = MAX_DATA_POINTS;

            if (Long.compareUnsigned(numberOfKeysGenerated, numberOfDataPoints) < 0) {
                numberOfDataPoints = (int) numberOfKeysGenerated;
            }

            long numberOfKeysPerSlot = toUnsignedBigInteger(numberOfKeysGenerated).divide(BigInteger.valueOf(numberOfDataPoints)).longValue();
            LOGGER.info("Computing estimates for expected collisions ...");
            for (int i = 0; i < numberOfDataPoints; i++) {
                // initalize every slot with the upper bound of the slot and the computed expected number of collisions
                long position = (numberOfKeysPerSlot * (i + 1));
                KeyCollisionDataPoint dataPoint = new KeyCollisionDataPoint(position, 0L, computeExpectedNumberOfCollisions(sizeOfKeyspace, position));
                res.result.getCollisionStats().add(dataPoint);
                LOGGER.debug("Data point {}/{}: {}", (i + 1), numberOfDataPoints, dataPoint);
            }
            LOGGER.info("Data point estimation complete.");
            res.numberOfKeysPerSlot = numberOfKeysPerSlot;
            return res;
        }

        /**
         * Analyzes the given collision and updates the stats
         * 
         * @param collision a keys that occurred at least 2 times
         * @return this builder
         */
        SummaryBuilder addCollision(KeyCollision<?> collision) {
            result.numberOfCollisions = result.numberOfCollisions + collision.getNumberOfDuplicates();
            result.numberOfCollidedKeys++;

            long firstCollisionPosition = collision.getFirstCollisionPosition();
            if (result.firstCollisionPosition == 0 || Long.compareUnsigned(firstCollisionPosition, result.firstCollisionPosition) < 0) {
                result.firstCollisionPosition = firstCollisionPosition;
            }

            updateMultiOccurrenceStats(collision.getNumberOfKeyOccurrences());

            createOrUpdateDataPointForCollision(collision);
            return this;
        }

        /**
         * Counts and collects the number of times the key occurred (number of different positions)
         * 
         * @param numberOfOccurences (this is the slot in the histogram)
         */
        private void updateMultiOccurrenceStats(long numberOfOccurences) {
            Long occurrenceCount = result.multiOccurrenceStats.get(numberOfOccurences);
            if (occurrenceCount == null) {
                // This would create a new slot and potentially blast the memory over time if the keyspace is large
                // and collisions are distributed
                // The solution is to limit the size of the ordered map by pushing the element upwards
                // Instead of exactly n occurrences we then express "up-to-n" occurrences in numberOfOccurences slot
                if (result.multiOccurrenceStats.size() >= MAX_DATA_POINTS) {
                    boolean foundSlotAbove = false;
                    long maxOccurrenceCount = 0;
                    for (long count : result.multiOccurrenceStats.keySet()) {
                        maxOccurrenceCount = count;
                        if (count > numberOfOccurences) {
                            // save existing count of the slot
                            occurrenceCount = result.multiOccurrenceStats.get(count) + 1L;

                            // change histogram slot to be updated
                            numberOfOccurences = count;
                            foundSlotAbove = true;
                            break;
                        }
                    }
                    if (!foundSlotAbove) {
                        // no slot found for more number of occurrences
                        // thus, we push the maximum up without increasing the map size (effectively replace last element)
                        occurrenceCount = result.multiOccurrenceStats.remove(maxOccurrenceCount) + 1L;
                    }
                }
                else {
                    // add a new slot to histogram map
                    occurrenceCount = 1L;
                }
            }
            else {
                // update existing slot
                occurrenceCount++;
            }
            result.multiOccurrenceStats.put(numberOfOccurences, occurrenceCount);
        }

        /**
         * Final action to obtain the result, afterwards this builder becomes <b>invalid</b>
         * 
         * @return summary
         */
        KeyCollisionSummary getResult() {
            finalizeDataPoints();
            KeyCollisionSummary res = result;
            result = null;
            return res;
        }

        /**
         * We collect data for {@link KeyCollisionProcessor#MAX_DATA_POINTS} slots, this method computes the correct slots and updates the detected collision
         * count.
         * 
         * @param collision to be added
         */
        private void createOrUpdateDataPointForCollision(KeyCollision<?> collision) {
            List<KeyCollisionDataPoint> dataPoints = result.collisionStats;
            long[] positions = collision.getPositions();
            if (positions.length > 0) {
                // start with the second (first position is not a collision)
                for (int i = 1; i < positions.length; i++) {
                    long pos = positions[i];

                    int slot = Math.min(toUnsignedBigInteger(pos).divide(BigInteger.valueOf(numberOfKeysPerSlot)).intValue(), dataPoints.size() - 1);
                    KeyCollisionDataPoint dataPoint = dataPoints.get(slot);
                    KeyCollisionDataPoint newDataPoint = new KeyCollisionDataPoint(dataPoint.getPosition(), dataPoint.getNumberOfCollisionsDetected() + 1L,
                            dataPoint.getNumberOfCollisionsExpected());
                    dataPoints.set(slot, newDataPoint);
                }
            }
        }

        /**
         * So far, each slot contains the count of collisions in that slot. This terminal operation adds up the slot values, so that every collision count in a
         * slot tells how many collisions have occurred "until now" (upper bound)<br />
         * <p>
         * This method must not be called twice.
         */
        private void finalizeDataPoints() {
            LOGGER.info("Finalizing collision statistics (sum-up collisions) ...");
            List<KeyCollisionDataPoint> dataPoints = result.collisionStats;
            long numberOfCollisionsDetected = 0;
            for (int slot = 0; slot < dataPoints.size(); slot++) {
                KeyCollisionDataPoint current = dataPoints.get(slot);
                KeyCollisionDataPoint newDataPoint = null;
                numberOfCollisionsDetected = numberOfCollisionsDetected + current.getNumberOfCollisionsDetected();
                newDataPoint = new KeyCollisionDataPoint(current.getPosition(), numberOfCollisionsDetected, current.getNumberOfCollisionsExpected());
                dataPoints.set(slot, newDataPoint);
            }
        }

    }
}
