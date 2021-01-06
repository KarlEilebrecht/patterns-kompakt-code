//@formatter:off
/*
 * KeyCollisionSummary
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * A {@link KeyCollisionSummary} contains the collision statistics collected while processing a number of keys in a keyspace.
 */
public class KeyCollisionSummary {

    /**
     * total number of possible keys in the target keyspace
     */
    long sizeOfKeyspace = 0;

    /**
     * number of keys in this experiment
     */
    long numberOfKeysGenerated = 0;

    /**
     * Number of keys that occurred at least twice numberOfCollidedKeys &lt; numberOfCollisions
     */
    long numberOfCollidedKeys = 0;

    /**
     * Number of collisions (a key that occurs three times is involved in two collisions)
     */
    long numberOfCollisions = 0;

    /**
     * The position of the first detected collision in a sequence of generated keys
     */
    long firstCollisionPosition = 0;

    /**
     * data collision statistics
     */
    List<KeyCollisionDataPoint> collisionStats = new ArrayList<>();

    /**
     * Collects information about multi-occurrences (twice, three times, four times etc.)
     * <p>
     * Because with a huge keyspace the number of distinct count of occurrences will potentially exhaust memory, buckets may be consolidated by the processor.
     * So, each item with the map-key (integer) k tells us that the key appeared <b>up to k times</b> but not more often.
     */
    Map<Long, Long> multiOccurrenceStats = new TreeMap<>();

    /**
     * @return number of keys in this experiment
     */
    public long getNumberOfKeysGenerated() {
        return numberOfKeysGenerated;
    }

    /**
     * @param numberOfKeysGenerated number of keys in this experiment
     */
    public void setNumberOfKeysGenerated(long numberOfKeysGenerated) {
        this.numberOfKeysGenerated = numberOfKeysGenerated;
    }

    /**
     * @return Number of keys that occurred at least twice {@link #getNumberOfCollidedKeys()} &lt; {@link #getNumberOfCollisions()}
     */
    public long getNumberOfCollidedKeys() {
        return numberOfCollidedKeys;
    }

    /**
     * @param numberOfCollidedKeys Number of keys that occurred at least twice
     */
    public void setNumberOfCollidedKeys(long numberOfCollidedKeys) {
        this.numberOfCollidedKeys = numberOfCollidedKeys;
    }

    /**
     * @return Number of collisions (a key that occurs three times is involved in two collisions)
     */
    public long getNumberOfCollisions() {
        return numberOfCollisions;
    }

    /**
     * @param numberOfCollisions Number of collisions (a key that occurs three times is involved in two collisions)
     */
    public void setNumberOfCollisions(long numberOfCollisions) {
        this.numberOfCollisions = numberOfCollisions;
    }

    /**
     * @return key-ordered map with information about multi-occurrences (twice, three times, four times etc.)
     */
    public Map<Long, Long> getMultiOccurrenceStats() {
        return multiOccurrenceStats;
    }

    /**
     * @param multiOccurrenceStats key-ordered map with information about multi-occurrences (twice, three times, four times etc.)
     */
    public void setMultiOccurrenceStats(Map<Long, Long> multiOccurrenceStats) {
        this.multiOccurrenceStats = multiOccurrenceStats;
    }

    /**
     * @return total number of possible keys in the target keyspace
     */
    public long getSizeOfKeyspace() {
        return sizeOfKeyspace;
    }

    /**
     * @param sizeOfKeyspace total number of possible keys in the target keyspace
     */
    public void setSizeOfKeyspace(long sizeOfKeyspace) {
        this.sizeOfKeyspace = sizeOfKeyspace;
    }

    /**
     * @return The position of the first detected collision in a sequence of generated keys
     */
    public long getFirstCollisionPosition() {
        return firstCollisionPosition;
    }

    /**
     * @param firstCollisionPosition The position of the first detected collision in a sequence of generated keys
     */
    public void setFirstCollisionPosition(long firstCollisionPosition) {
        this.firstCollisionPosition = firstCollisionPosition;
    }

    /**
     * @return List of {@link KeyCollisionDataPoint}s (collision statistics)
     */
    @JsonIgnore
    public List<KeyCollisionDataPoint> getCollisionStats() {
        return collisionStats;
    }

    /**
     * @param collisionStats List of {@link KeyCollisionDataPoint}s (collision statistics)
     */
    @JsonIgnore
    public void setCollisionStats(List<KeyCollisionDataPoint> collisionStats) {
        this.collisionStats = collisionStats;
    }

    /**
     * @return csv with the stats using ';' as separator, all line breaks replaced by pipes
     */
    public String getCollisionStatsPcsv() {
        StringBuilder sb = new StringBuilder();
        sb.append("Number of keys generated;Collisions detected;Collisions expected");
        for (KeyCollisionDataPoint dataPoint : collisionStats) {
            sb.append("|");
            sb.append(Long.toUnsignedString(dataPoint.getPosition()));
            sb.append(";");
            sb.append(Long.toUnsignedString(dataPoint.getNumberOfCollisionsDetected()));
            sb.append(";");
            sb.append(Long.toUnsignedString(dataPoint.getNumberOfCollisionsExpected()));
        }
        return sb.toString();
    }

    /**
     * @param csv data produced by {@link #getCollisionStatsPcsv()} and reconstructs the stats
     */
    public void setCollisionStatsPcsv(String csv) {
        this.collisionStats = Stream.of(csv.split("\\|")).skip(1).map(s -> {
            String[] arr = s.split(";");
            return new KeyCollisionDataPoint(Long.parseUnsignedLong(arr[0]), Long.parseUnsignedLong(arr[1]), Long.parseUnsignedLong(arr[2]));
        }).collect(Collectors.toList());
    }

}