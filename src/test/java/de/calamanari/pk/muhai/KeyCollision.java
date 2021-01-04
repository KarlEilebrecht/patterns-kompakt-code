//@formatter:off
/*
 * KeyCollision
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
 * A {@link KeyCollision} is by definition a key that occurred at two or more positions.
 * <p>
 * This VALUE OBJECT stores the key and the positions in ascending order and defines equality on its members.<br />
 * When comparing two elements the <b>second
 * </p>
 * positions will be compared first (before it was no collision).
 * <p>
 * The type long is treated here unsigned, using {@link Long#compareUnsigned(long, long)} for sorting positions.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class KeyCollision implements Comparable<KeyCollision> {

    /**
     * Codec for writing {@link KeyCollision}s line by line to a file and read it back
     */
    public static final ItemStringCodec<KeyCollision> LINE_CODEC = new ItemStringCodec<>() {

        @Override
        public String itemToString(KeyCollision item) {
            if (item == null) {
                throw new IllegalArgumentException("Cannot encode null");
            }

            StringBuilder sb = new StringBuilder();
            sb.append(Long.toUnsignedString(item.key));
            sb.append('@');
            for (int i = 0; i < item.positions.length; i++) {
                if (i > 0) {
                    sb.append(";");
                }
                sb.append(Long.toUnsignedString(item.positions[i]));
            }
            return sb.toString();
        }

        @Override
        public KeyCollision stringToItem(String line) {
            if (line == null) {
                throw new ItemConversionException("Cannot decode null");
            }
            int delimPos = line.indexOf('@');
            int delimPos2 = line.indexOf(';');
            if (delimPos < 1 || delimPos2 < delimPos || delimPos2 > line.length() - 2) {
                throw new ItemConversionException("Line corrupted, expected <unsigned long>@<unsigned long>;<unsigned long>; ..., given: " + line);
            }
            KeyCollision res = null;
            try {
                long key = Long.parseUnsignedLong(line.substring(0, delimPos));
                String positionsPart = line.substring(delimPos + 1);
                Long[] rawPositions = Stream.of(positionsPart.split(";")).map(r -> Long.parseUnsignedLong(r)).toArray(Long[]::new);
                long[] positions = BoxingUtils.unboxArray(rawPositions);
                res = new KeyCollision(key, positions);
            }
            catch (RuntimeException ex) {
                throw new ItemConversionException(
                        String.format("Line corrupted, expected <unsigned long>@<unsigned long>;<unsigned long>; ..., given: %s", line), ex);
            }
            return res;
        }

    };

    /**
     * generated key
     */
    private final long key;

    /**
     * positions the key was found in ascending order
     */
    private final long[] positions;

    /**
     * @param key
     * @param positions at least 2 positions
     */
    public KeyCollision(long key, long... positions) {
        List<Long> posList = new ArrayList<>();
        for (long pos : positions) {
            if (!posList.contains(pos)) {
                posList.add(pos);
            }
        }
        if (posList.size() < 2) {
            throw new IllegalArgumentException(String.format("A collision must contain at least two unique positions, given: key=%s, positions=%s",
                    Long.toUnsignedString(key), arrayToUnsignedString(positions)));
        }
        Collections.sort(posList, Long::compareUnsigned);
        this.key = key;
        this.positions = BoxingUtils.unboxArray(posList.toArray(new Long[posList.size()]));
    }

    /**
     * @return the key that occurred at least twice
     */
    public long getKey() {
        return this.key;
    }

    /**
     * @return a copy of the internal positions array, minimum 2 elements, ordered ascending unsigned
     */
    public long[] getPositions() {
        return Arrays.copyOf(this.positions, this.positions.length);
    }

    /**
     * helper method to display the positions in a message
     * @param positions
     * @return stringified array
     */
    static final String arrayToUnsignedString(long[] positions) {
        String res = null;
        if (positions == null) {
            res = "null";
        }
        else if (positions.length == 0) {
            res = "[]";
        }
        else {
            StringBuilder sb = new StringBuilder();
            sb.append("[");
            for (int i = 0; i < positions.length; i++) {
                if (i > 0) {
                    sb.append(", ");
                }
                sb.append(Long.toUnsignedString(positions[i]));
            }
            sb.append("[");
            res = sb.toString();
        }
        return res;
    }

    @Override
    public int compareTo(KeyCollision o) {
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
        if (res == 0) {
            res = Long.compareUnsigned(this.key, o.key);
        }
        return res;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (key ^ (key >>> 32));
        result = prime * result + Arrays.hashCode(positions);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        KeyCollision other = (KeyCollision) obj;
        if (key != other.key)
            return false;
        if (!Arrays.equals(positions, other.positions))
            return false;
        return true;
    }

    public String toString() {
        return KeyCollision.class.getSimpleName() + "(" + Long.toUnsignedString(this.key) + ", " + arrayToUnsignedString(this.positions) + ")";
    }

}
