//@formatter:off
/*
 * CountingKeyCollision
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A {@link CountingKeyCollision} is a key that occurred at two or more positions counting but not storing multi-occurrences.
 * <p>
 * This VALUE OBJECT stores the key and the second position and defines equality on its members.
 * <p />
 * The type long is treated here unsigned, using {@link Long#compareUnsigned(long, long)} for sorting positions.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class CountingKeyCollision implements KeyCollision<CountingKeyCollision> {

    private static final long serialVersionUID = -612947977346185511L;

    /**
     * Codec for writing {@link CountingKeyCollision}s line by line to a file and read it back
     */
    public static final ItemStringCodec<CountingKeyCollision> LINE_CODEC = new ItemStringCodec<>() {

        @Override
        public String itemToString(CountingKeyCollision item) {
            if (item == null) {
                throw new IllegalArgumentException("Cannot encode null");
            }
            return Long.toUnsignedString(item.key) + "@" + Long.toUnsignedString(item.firstCollisionPosition) + "#"
                    + Long.toUnsignedString(item.numberOfKeyOccurrences);
        }

        @Override
        public CountingKeyCollision stringToItem(String line) {
            if (line == null) {
                throw new ItemConversionException("Cannot decode null");
            }
            int delimPos = line.indexOf('@');
            int delimPos2 = line.indexOf('#');
            if (delimPos < 1 || delimPos2 < delimPos || delimPos2 > line.length() - 2) {
                throw new ItemConversionException("Line corrupted, expected <unsigned long>@<unsigned long>#<unsigned long>, given: " + line);
            }
            CountingKeyCollision res = null;
            try {
                long keyParsed = Long.parseUnsignedLong(line.substring(0, delimPos));
                long firstCollisionPositionParsed = Long.parseUnsignedLong(line.substring(delimPos + 1, delimPos2));
                long numberOfKeyOccurrencesParsed = Long.parseUnsignedLong(line.substring(delimPos2 + 1));
                res = new CountingKeyCollision(keyParsed, firstCollisionPositionParsed, numberOfKeyOccurrencesParsed);
            }
            catch (RuntimeException ex) {
                throw new ItemConversionException(String.format("Line corrupted, expected <unsigned long>@<unsigned long>#<unsigned long>, given: %s", line),
                        ex);
            }
            return res;
        }

    };

    /**
     * generated key
     */
    private final long key;

    /**
     * position of the second occurrence of the key
     */
    private final long firstCollisionPosition;

    /**
     * Total number of times the key was seen including first occurrence
     */
    private final long numberOfKeyOccurrences;

    /**
     * @param key the key that occurred at least twice
     * @param firstCollisionPosition second position the key occurred, &gt;0 ({@link Long#compareUnsigned(long, long)})
     * @param numberOfKeyOccurrences Total number of times the key was seen including first occurrence, &gt;2 ({@link Long#compareUnsigned(long, long)})
     */
    public CountingKeyCollision(long key, long firstCollisionPosition, long numberOfKeyOccurrences) {
        if (Long.compareUnsigned(firstCollisionPosition, 1) < 0) {
            throw new IllegalArgumentException(String.format(
                    "The first collision cannot happen before the second possible position: key=%s, firstCollisionPosition=%s (expected >=1), numberOfKeyOccurrences=%s",
                    Long.toUnsignedString(key), Long.toUnsignedString(firstCollisionPosition), Long.toUnsignedString(numberOfKeyOccurrences)));
        }
        if (Long.compareUnsigned(numberOfKeyOccurrences, 2) < 0) {
            throw new IllegalArgumentException(
                    String.format("Invalid number of occurrences: key=%s, firstCollisionPosition=%s, numberOfKeyOccurrences=%s (expected >=2)",
                            Long.toUnsignedString(key), Long.toUnsignedString(firstCollisionPosition), Long.toUnsignedString(numberOfKeyOccurrences)));
        }
        this.key = key;
        this.firstCollisionPosition = firstCollisionPosition;
        this.numberOfKeyOccurrences = numberOfKeyOccurrences;
    }

    /**
     * @param key the key that occurred at least twice
     * @param positions at least 2 positions
     */
    public CountingKeyCollision(long key, long... positions) {
        List<Long> posList = new ArrayList<>();
        for (long pos : positions) {
            if (!posList.contains(pos)) {
                posList.add(pos);
            }
        }
        if (posList.size() < 2) {
            throw new IllegalArgumentException(String.format("A collision must contain at least two unique positions, given: key=%s, positions=%s",
                    Long.toUnsignedString(key), TrackingKeyCollision.arrayToUnsignedString(positions)));
        }
        Collections.sort(posList, Long::compareUnsigned);
        this.key = key;
        this.firstCollisionPosition = posList.get(1);
        this.numberOfKeyOccurrences = posList.size();
    }

    @Override
    public int compareTo(CountingKeyCollision o) {
        int res = Long.compareUnsigned(this.firstCollisionPosition, o.firstCollisionPosition);
        if (res == 0) {
            // not expected, only to fulfill comparable/equals contract
            res = Long.compareUnsigned(this.key, o.key);
        }
        if (res == 0) {
            // not expected, only to fulfill comparable/equals contract
            res = Long.compareUnsigned(this.numberOfKeyOccurrences, o.numberOfKeyOccurrences);
        }
        return res;
    }

    @Override
    public long getKey() {
        return key;
    }

    @Override
    public long getFirstCollisionPosition() {
        return firstCollisionPosition;
    }

    @Override
    public long getNumberOfKeyOccurrences() {
        return numberOfKeyOccurrences;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (firstCollisionPosition ^ (firstCollisionPosition >>> 32));
        result = prime * result + (int) (key ^ (key >>> 32));
        result = prime * result + (int) (numberOfKeyOccurrences ^ (numberOfKeyOccurrences >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        CountingKeyCollision other = (CountingKeyCollision) obj;
        return (firstCollisionPosition == other.firstCollisionPosition && key == other.key && numberOfKeyOccurrences == other.numberOfKeyOccurrences);
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " [key=" + Long.toUnsignedString(key) + ", firstCollisionPosition="
                + Long.toUnsignedString(firstCollisionPosition) + ", numberOfKeyOccurrences=" + Long.toUnsignedString(numberOfKeyOccurrences) + "]";
    }

}
