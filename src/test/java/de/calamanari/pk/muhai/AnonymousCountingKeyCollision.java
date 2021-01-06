//@formatter:off
/*
 * AnonymousCountingKeyCollision
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
import java.util.Collections;
import java.util.List;

/**
 * An {@link AnonymousCountingKeyCollision} represents a key occurrence at two or more positions without the key counting but not storing multi-occurrences.
 * Often this is sufficient for statistics.
 * <p>
 * This VALUE OBJECT stores the the second position and defines equality on its members.
 * <p />
 * The type long is treated here unsigned, using {@link Long#compareUnsigned(long, long)} for sorting positions.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class AnonymousCountingKeyCollision implements KeyCollision<AnonymousCountingKeyCollision> {

    private static final long serialVersionUID = -21940161740975089L;

    /**
     * Codec for writing {@link CountingKeyCollision}s line by line to a file and read it back
     */
    public static final ItemStringCodec<AnonymousCountingKeyCollision> LINE_CODEC = new ItemStringCodec<>() {

        @Override
        public String itemToString(AnonymousCountingKeyCollision item) {
            if (item == null) {
                throw new IllegalArgumentException("Cannot encode null");
            }
            return Long.toUnsignedString(item.firstCollisionPosition) + "#" + Long.toUnsignedString(item.numberOfKeyOccurrences);
        }

        @Override
        public AnonymousCountingKeyCollision stringToItem(String line) {
            if (line == null) {
                throw new ItemConversionException("Cannot decode null");
            }
            int delimPos = line.indexOf('#');
            if (delimPos < 1) {
                throw new ItemConversionException("Line corrupted, expected <unsigned long>#<unsigned long>, given: " + line);
            }
            AnonymousCountingKeyCollision res = null;
            try {
                long firstCollisionPosition = Long.parseUnsignedLong(line.substring(0, delimPos));
                long numberOfKeyOccurrences = Long.parseUnsignedLong(line.substring(delimPos + 1));
                res = new AnonymousCountingKeyCollision(0L, firstCollisionPosition, numberOfKeyOccurrences);
            }
            catch (RuntimeException ex) {
                throw new ItemConversionException(String.format("Line corrupted, expected <unsigned long>#<unsigned long>, given: %s", line), ex);
            }
            return res;
        }

    };

    /**
     * position of the second occurrence of the key
     */
    private final long firstCollisionPosition;

    /**
     * Total number of times the key was seen including first occurrence
     */
    private final long numberOfKeyOccurrences;

    /**
     * @param firstCollisionPosition second position the key occurred, &gt;0 ({@link Long#compareUnsigned(long, long)})
     * @param numberOfKeyOccurrences Total number of times the key was seen including first occurrence, &gt;2 ({@link Long#compareUnsigned(long, long)})
     */
    public AnonymousCountingKeyCollision(long firstCollisionPosition, long numberOfKeyOccurrences) {
        if (Long.compareUnsigned(firstCollisionPosition, 1) < 0) {
            throw new IllegalArgumentException(String.format(
                    "The first collision cannot happen before the second possible position: firstCollisionPosition=%s (expected >=1), numberOfKeyOccurrences=%s",
                    Long.toUnsignedString(firstCollisionPosition), Long.toUnsignedString(numberOfKeyOccurrences)));
        }
        if (Long.compareUnsigned(numberOfKeyOccurrences, 2) < 0) {
            throw new IllegalArgumentException(
                    String.format("Invalid number of occurrences: firstCollisionPosition=%s, numberOfKeyOccurrences=%s (expected >=2)",
                            Long.toUnsignedString(firstCollisionPosition), Long.toUnsignedString(numberOfKeyOccurrences)));
        }
        this.firstCollisionPosition = firstCollisionPosition;
        this.numberOfKeyOccurrences = numberOfKeyOccurrences;
    }

    /**
     * @param positions at least 2 positions
     */
    public AnonymousCountingKeyCollision(long... positions) {
        List<Long> posList = new ArrayList<>();
        for (long pos : positions) {
            if (!posList.contains(pos)) {
                posList.add(pos);
            }
        }
        if (posList.size() < 2) {
            throw new IllegalArgumentException(String.format("A collision must contain at least two unique positions, given: key=%s, positions=%s",
                    TrackingKeyCollision.arrayToUnsignedString(positions)));
        }
        Collections.sort(posList, Long::compareUnsigned);
        this.firstCollisionPosition = posList.get(1);
        this.numberOfKeyOccurrences = posList.size();
    }

    @Override
    public int compareTo(AnonymousCountingKeyCollision o) {
        int res = Long.compareUnsigned(this.firstCollisionPosition, o.firstCollisionPosition);
        if (res == 0) {
            // not expected, only to fulfill comparable/equals contract
            res = Long.compareUnsigned(this.numberOfKeyOccurrences, o.numberOfKeyOccurrences);
        }
        return res;
    }

    /**
     * Always returns 0 as we don't store the key
     */
    @Override
    public long getKey() {
        return 0;
    }

    @Override
    public long getFirstCollisionPosition() {
        return this.firstCollisionPosition;
    }

    @Override
    public long getNumberOfKeyOccurrences() {
        return this.numberOfKeyOccurrences;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (firstCollisionPosition ^ (firstCollisionPosition >>> 32));
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
        AnonymousCountingKeyCollision other = (AnonymousCountingKeyCollision) obj;
        if (firstCollisionPosition != other.firstCollisionPosition) {
            return false;
        }
        if (numberOfKeyOccurrences != other.numberOfKeyOccurrences) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName() + " [firstCollisionPosition=" + Long.toUnsignedString(firstCollisionPosition) + ", numberOfKeyOccurrences="
                + Long.toUnsignedString(numberOfKeyOccurrences) + "]";
    }

}
