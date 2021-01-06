//@formatter:off
/*
 * AnonymousTrackingKeyCollision
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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import de.calamanari.pk.util.BoxingUtils;

/**
 * An {@link AnonymousTrackingKeyCollision} represents a key occurrence at two or more positions including all positions but discarding the key.
 * <p>
 * This VALUE OBJECT stores the positions in ascending order and defines equality on its members.<br />
 * When comparing two elements the <b>second </b> positions will be compared first (before it was no collision).
 * <p>
 * The type long is treated here unsigned, using {@link Long#compareUnsigned(long, long)} for sorting positions.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class AnonymousTrackingKeyCollision implements KeyCollision<AnonymousTrackingKeyCollision> {

    private static final long serialVersionUID = 489338795124757425L;

    /**
     * Codec for writing {@link TrackingKeyCollision}s line by line to a file and read it back
     */
    public static final ItemStringCodec<AnonymousTrackingKeyCollision> LINE_CODEC = new ItemStringCodec<>() {

        @Override
        public String itemToString(AnonymousTrackingKeyCollision item) {
            if (item == null) {
                throw new IllegalArgumentException("Cannot encode null");
            }

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < item.positions.length; i++) {
                if (i > 0) {
                    sb.append(";");
                }
                sb.append(Long.toUnsignedString(item.positions[i]));
            }
            return sb.toString();
        }

        @Override
        public AnonymousTrackingKeyCollision stringToItem(String line) {
            if (line == null) {
                throw new ItemConversionException("Cannot decode null");
            }
            int delimPos = line.indexOf(';');
            if (delimPos < 1) {
                throw new ItemConversionException("Line corrupted, expected <unsigned long>;<unsigned long>; ..., given: " + line);
            }
            AnonymousTrackingKeyCollision res = null;
            try {
                Long[] rawPositions = Stream.of(line.split(";")).map(r -> Long.parseUnsignedLong(r)).toArray(Long[]::new);
                long[] positions = BoxingUtils.unboxArray(rawPositions);
                res = new AnonymousTrackingKeyCollision(positions);
            }
            catch (RuntimeException ex) {
                throw new ItemConversionException(
                        String.format("Line corrupted, expected <unsigned long>@<unsigned long>;<unsigned long>; ..., given: %s", line), ex);
            }
            return res;
        }

    };

    /**
     * positions the key was found in ascending order
     */
    private final long[] positions;

    /**
     * @param key the key that occurred at least twice
     * @param positions at least 2 positions
     */
    public AnonymousTrackingKeyCollision(long... positions) {
        List<Long> posList = new ArrayList<>();
        for (long pos : positions) {
            if (!posList.contains(pos)) {
                posList.add(pos);
            }
        }
        if (posList.size() < 2) {
            throw new IllegalArgumentException(String.format("A collision must contain at least two unique positions, given: positions=%s",
                    TrackingKeyCollision.arrayToUnsignedString(positions)));
        }
        Collections.sort(posList, Long::compareUnsigned);
        this.positions = BoxingUtils.unboxArray(posList.toArray(new Long[posList.size()]));
    }

    /**
     * @return always 0 (key discarded)
     */
    @Override
    public long getKey() {
        return 0;
    }

    /**
     * @return a copy of the internal positions array, minimum 2 elements, ordered ascending unsigned
     */
    @Override
    public long[] getPositions() {
        return Arrays.copyOf(this.positions, this.positions.length);
    }

    @Override
    public int compareTo(AnonymousTrackingKeyCollision o) {
        int res = Long.compareUnsigned(this.positions[1], o.positions[1]);
        // It is unexpected to see here 0, but we must handle this in a deterministic way
        // to be consistent with equals() and to avoid endless loops when sorting
        // Thus we check the members further
        if (res == 0) {
            int len = Math.min(this.positions.length, o.positions.length);
            for (int i = 0; res == 0 && i < len; i++) {
                res = Long.compareUnsigned(this.positions[i], o.positions[i]);
            }
        }
        if (res == 0) {
            res = this.positions.length - o.positions.length;
        }
        return res;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(positions);
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
        AnonymousTrackingKeyCollision other = (AnonymousTrackingKeyCollision) obj;
        if (!Arrays.equals(positions, other.positions)) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return AnonymousTrackingKeyCollision.class.getSimpleName() + "(" + TrackingKeyCollision.arrayToUnsignedString(this.positions) + ")";
    }

    @Override
    public long getFirstCollisionPosition() {
        return this.positions[1];
    }

    @Override
    public long getNumberOfKeyOccurrences() {
        return this.positions.length;
    }

}
