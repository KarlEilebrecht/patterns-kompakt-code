//@formatter:off
/*
 * LongPrefix 
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

import java.io.Serializable;
import java.math.BigInteger;

/**
 * The {@link LongPrefix} is a VALUE-object that represents a <i>valid</i> prefix sequence for a long value to define keyspace. The size of the prefix can be up
 * to 63 bits. 64 bits is invalid because it would create a <i>strange keyspace</i> that has a single member (the prefix itself) but cannot contain any keys. A
 * valid special case is an empty prefix (2^64 keys).
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class LongPrefix implements Serializable {

    private static final long serialVersionUID = 1189853182590506689L;

    /**
     * Special case, an empty prefix means no prefix at all to leverage the maximum number of keys: 2^64.
     */
    public static final LongPrefix NONE = new LongPrefix("");

    /**
     * This single-bit prefix '0' (2^63 keys possible) causes the signed long representation to never turn negative. This is recommended if the storage
     * (db-table) also uses signed longs and negative keys would cause confusion or any processing issues.
     */
    public static final LongPrefix POSITIVE = new LongPrefix("0");

    /**
     * This two-bits prefix '00' (2^62 keys possible) is a convenient default as it still spans a large keyspace, but eliminates the negative values when
     * represented as signed long and it reserves 3 subspaces (e.g. for migration purposes in future). Because the bits on the left are both zero, the keys in
     * the defined space will have variable length. For economical reasons (e.g. String representation) this is recommended, especially if the keys to be
     * prefixed are expected to be/start rather small.
     */
    public static final LongPrefix DEFAULT = new LongPrefix("00");

    /**
     * This two-bits prefix '01' (2^62 keys possible) spans a large keyspace, eliminates the negative values when represented as signed long and it reserves 3
     * subspaces (e.g. for migration purposes in future). The leading 1-bit on the left causes all keys to have the same length, no matter whether displayed as
     * binary String (62), as signed long (19), unsigned integer (19) or hex String (16).
     */
    public static final LongPrefix STRAIGHT = new LongPrefix("01");

    /**
     * The prefix passed to the constructor
     */
    private final String prefixString;

    /**
     * The prefix as an 8-bytes long at the end of the bit vector.
     */
    private final long prefixWithLeadingZeros;

    /**
     * The prefix, but moved to the start of the bit vector, at the same time the start key of the keyspace.
     */
    private final long prefixWithTrailingZeros;

    /**
     * Creates a custom prefix from the given bit vector.
     * @param prefix composed of 0s and 1s, length in range [0 .. 63], empty String is valid, NOT NULL.
     * @throws InvalidPrefixException if the given prefix cannot be used
     */
    public LongPrefix(String prefix) {
        if (prefix == null) {
            throw new InvalidPrefixException("Prefix must not be null, instead specify an empty prefix explicitly as an empty String.");
        }
        else if (prefix.length() > 63) {
            throw new InvalidPrefixException(String.format("Prefix length must be in range [0 .. 63], given: '%s'", prefix));
        }
        long prefixBinary = 0;

        for (int i = 0; i < prefix.length(); i++) {
            char ch = prefix.charAt(i);
            if (ch == '1') {
                prefixBinary |= 1L << (63 - i);
            }
            else if (ch != '0') {
                throw new InvalidPrefixException(String.format("Prefix must be specified as a binary string composed of 0s and 1s, given: '%s'", prefix));
            }
        }

        this.prefixString = prefix;
        this.prefixWithTrailingZeros = prefixBinary;
        this.prefixWithLeadingZeros = prefixBinary >> (64 - prefix.length());
    }

    /**
     * Returns the string representation of this prefix (provided at construction time), a character (0, 1) per bit.
     * @return prefix
     */
    public String getPrefixString() {
        return prefixString;
    }

    /**
     * @return the long representation of the prefix (8 bytes, prefix at the end of the bit vector)
     */
    public long getPrefixWithLeadingZeros() {
        return prefixWithLeadingZeros;
    }

    /**
     * @return the long representation of the prefix (8 bytes, prefix at the start of the bit vector)
     */
    public long getPrefixWithTrailingZeros() {
        return prefixWithTrailingZeros;
    }

    /**
     * @return length of the prefix (number of bits starting from the left [0 .. 63])
     */
    public int getLength() {
        return prefixString.length();
    }

    /**
     * Checks whether the given key is prefixed with <b>this</b> prefix. The <i>empty prefix</i> matches all keys.
     * @param key to be tested
     * @return true if the leading bits from the left of the given long value match this prefix
     */
    public boolean match(long key) {
        return (key >>> (64 - prefixString.length())) == prefixWithLeadingZeros;
    }

    /**
     * Returns a description
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(LongPrefix.class.getSimpleName());
        sb.append("('");
        sb.append(this.prefixString);
        sb.append("', length=");
        sb.append(prefixString.length());
        sb.append(", ");
        sb.append(prefixString.length() == 0 ? "<EMPTY>" : Long.toBinaryString(prefixWithLeadingZeros));
        sb.append(", start key: ");
        sb.append(Long.toBinaryString(prefixWithTrailingZeros));
        sb.append(", size of keyspace: ");
        sb.append(BigInteger.TWO.pow(64 - prefixString.length()));
        sb.append(")");
        return sb.toString();
    }

}
