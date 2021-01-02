//@formatter:off
/*
 * KeyAtPos
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

/**
 * {@link KeyAtPos} is a VALUE OBJECT that represents a key at a certain position.<br />
 * When comparing keys to bring them in an order, the {@link #getKey()} takes <b>precendence</b> over {@link #getPos()}. So, sorting elements of this type
 * automatically groups items with the same key.
 * <p>
 * The underlying data type is long, but we treat it as unsigned and use {@link Long#compareUnsigned(long, long)} for comparison.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class KeyAtPos implements Comparable<KeyAtPos> {

    /**
     * generated key
     */
    private final long key;

    /**
     * position
     */
    private final long pos;

    /**
     * @param key treated as unsigned long
     * @param pos treated as unsigned long
     */
    public KeyAtPos(long key, long pos) {
        super();
        this.key = key;
        this.pos = pos;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (key ^ (key >>> 32));
        result = prime * result + (int) (pos ^ (pos >>> 32));
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
        KeyAtPos other = (KeyAtPos) obj;
        if (key != other.key)
            return false;
        if (pos != other.pos)
            return false;
        return true;
    }

    @Override
    public int compareTo(KeyAtPos o) {
        int res = Long.compareUnsigned(this.key, o.key);
        if (res == 0) {
            res = Long.compareUnsigned(this.pos, o.pos);
        }
        return res;
    }

    /**
     * @return key
     */
    public long getKey() {
        return key;
    }

    /**
     * @return position the key was found
     */
    public long getPos() {
        return pos;
    }

    /**
     * Description including the unsigned values
     */
    public String toString() {
        return KeyAtPos.class.getSimpleName() + "(" + Long.toUnsignedString(key) + ", " + Long.toUnsignedString(pos) + ")";
    }

}
