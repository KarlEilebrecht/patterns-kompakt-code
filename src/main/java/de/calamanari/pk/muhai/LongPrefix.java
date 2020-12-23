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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * The {@link LongPrefix} is a VALUE-object that represents a <i>valid</i> prefix sequence for a long value to define a keyspace. The size of the prefix can be
 * up to 63 bits. 64 bits is invalid because it would create a <i>strange keyspace</i> that has a single member (the prefix itself) but cannot contain any keys.
 * A valid special case is an empty prefix (2^64 keys).
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public final class LongPrefix implements Serializable, Comparable<LongPrefix> {

    private static final long serialVersionUID = 1189853182590506689L;

    /**
     * Special case, an empty prefix means no prefix at all to leverage the maximum number of keys: 2^64.
     */
    public static final LongPrefix NONE = new LongPrefix("");

    /**
     * This two-bits prefix '00' (2^62 keys possible) is a convenient default as it still spans a large keyspace, but eliminates the negative values when
     * represented as signed long and it reserves 3 subspaces (e.g. for migration purposes in future). Because the bits on the left are both zero, the keys in
     * the defined space will have variable length. For economical reasons (e.g. String representation) this is recommended, especially if the keys to be
     * prefixed are expected to be/start rather small.
     */
    public static final LongPrefix DEFAULT = new LongPrefix("00");

    /**
     * This single-bit prefix '0' (2^63 keys possible) causes the signed long representation to never turn negative. This is recommended if the storage
     * (db-table) also uses signed longs and negative keys would cause confusion or any processing issues.
     */
    public static final LongPrefix POSITIVE = new LongPrefix("0");

    /**
     * This two-bits prefix '01' (2^62 keys possible) spans a large keyspace, eliminates the negative values when represented as signed long and it reserves 3
     * subspaces (e.g. for migration purposes in future). The leading 1-bit on the left causes all keys to have the same length, no matter whether displayed as
     * binary String (62 digits), as signed long (19 digits), unsigned integer (19 digits) or hex String (16 digits).
     */
    public static final LongPrefix STRAIGHT = new LongPrefix("01");

    /**
     * This prefix sets the first 33 bits '0', so any key in that space of 2^31 keys will be a positive 32-bit integer.
     */
    public static final LongPrefix POSITIVE_31 = new LongPrefix("000000000000000000000000000000000");

    /**
     * This prefix sets the first 33 bits to '0' followed by a '1' (2^30 keys possible) creating a rather small keyspace with 3 optional subspaces. All keys are
     * positive 32-bit-integer values. Because of the single '1' to the left, keys will all have the same length no matter if represented binary (31 digits), as
     * signed integer (10 digits) or hex String (8 digits).
     */
    public static final LongPrefix STRAIGHT_30 = new LongPrefix("0000000000000000000000000000000001");

    /**
     * A common list of prefixes to avoid duplicates (static caching).
     */
    private static final Map<String, LongPrefix> STANDARD_PREFIXES;
    static {
        HashMap<String, LongPrefix> map = new HashMap<>();
        map.put(NONE.toBinaryString(), NONE);
        map.put(DEFAULT.toBinaryString(), DEFAULT);
        map.put(POSITIVE.toBinaryString(), POSITIVE);
        map.put(POSITIVE_31.toBinaryString(), POSITIVE_31);
        map.put(STRAIGHT.toBinaryString(), STRAIGHT);
        map.put(STRAIGHT_30.toBinaryString(), STRAIGHT_30);
        STANDARD_PREFIXES = Collections.unmodifiableMap(map);
    }

    /**
     * The prefix passed to the constructor<br />
     * The only non-transient state to be included in serialization.
     */
    private final String prefixString;

    /**
     * The prefix as an 8-bytes long at the end of the bit vector.
     */
    private final transient long prefixWithLeadingZeros;

    /**
     * The prefix, but moved to the start of the bit vector, at the same time the start key of the keyspace.
     */
    private final transient long prefixWithTrailingZeros;

    /**
     * Returns the {@link LongPrefix} instance for the given prefix string, each character ('0' or '1') stands for a bit.
     * @param prefixString composed of '0's and '1's, supports leading zeros, length in range [0 .. 63], empty String is valid, NOT NULL.
     * @return prefix instance
     * @throws InvalidPrefixException if the given prefix cannot be used
     */
    public static LongPrefix fromBinaryString(String prefixString) {
        LongPrefix res = STANDARD_PREFIXES.get(prefixString);
        if (res == null) {
            res = new LongPrefix(prefixString);
        }
        return res;
    }

    /**
     * Creates a custom prefix from the given bit vector.
     * @param prefix composed of 0s and 1s, length in range [0 .. 63], empty String is valid, NOT NULL.
     * @throws InvalidPrefixException if the given prefix cannot be used
     */
    private LongPrefix(String prefix) {
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
        this.prefixWithLeadingZeros = prefixBinary >>> (64 - prefix.length());
    }

    /**
     * Returns the string representation of this prefix (provided at construction time), a character (0, 1) per bit.
     * @return prefix as a binary string, may have leading zeros
     */
    public String toBinaryString() {
        return prefixString;
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
        return prefixString.isEmpty() || ((key >>> (64 - prefixString.length())) == prefixWithLeadingZeros);
    }

    /**
     * Returns a new long value where the leading bits (length of prefix) have been <b>replaced</b> with the bits of the prefix. Trailing bits will remain
     * unchanged.
     * @param key source bits
     * @return value with the leading prefix
     */
    public long applyTo(long key) {
        long res = key;
        if (prefixString.length() > 0) {
            res = ((key << prefixString.length()) >>> prefixString.length()) | prefixWithTrailingZeros;
        }
        return res;
    }

    @Override
    public int hashCode() {
        return this.prefixString.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return (obj == this || (obj.getClass() == LongPrefix.class && ((LongPrefix) obj).prefixString.equals(this.prefixString)));
    }

    @Override
    public int compareTo(LongPrefix other) {
        return this.prefixString.compareTo(other.prefixString);
    }

    /**
     * Returns a description
     */
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(LongPrefix.class.getSimpleName());
        sb.append("('");
        sb.append(this.prefixString);
        sb.append("' [");
        sb.append(prefixString.isEmpty() ? "<NONE>"
                : String.format("%1$" + prefixString.length() + "s", Long.toBinaryString(prefixWithLeadingZeros)).replace(' ', '0'));
        sb.append("], length=");
        sb.append(prefixString.length());
        sb.append(", start key: ");
        sb.append(Long.toBinaryString(prefixWithTrailingZeros));
        sb.append(", size of keyspace: ");
        sb.append(BigInteger.TWO.pow(64 - prefixString.length()));
        sb.append(")");
        return sb.toString();
    }

    /**
     * Replaces the instance during deserialization with, so that serialization won't create duplicates in the same VM.
     * @return generator instance
     */
    Object readResolve() {
        return fromBinaryString(this.prefixString);
    }

}
