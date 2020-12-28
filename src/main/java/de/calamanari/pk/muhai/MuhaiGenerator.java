//@formatter:off
/*
 * MuhaiGenerator 
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
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import de.calamanari.pk.util.JavaWrapperType;

/**
 * {@link MuhaiGenerator} is an implementation of Mostly Unique Hashed Attributes Identifier.
 * <p>
 * This generator leverages SHA1 for hashing given attributes. From the hash value it takes up to 64 bits (from the left) and turns the bit-sequence in a long
 * value. The number of bits and thus the size of the keyspace is defined by a configurable {@link LongPrefix}. The maximum size of a keyspace with empty prefix
 * is <code>2^64 = 18_446_744_073_709_551_616</code>. As a result each key produced by {@link #createKey(Object...)} is a long value, where the underlying
 * 64-bits interpreted as unsigned integer {@link Long#toBinaryString(long)} starts with the configured <code><b>p</b></code> prefix bits followed by
 * <code><b>64-p</b></code> bits taken from the SHA1 hash.<br>
 * This creates a mostly unique identifier given that the provided combination of attributes is unique. The higher the number of bits after the prefix the lower
 * the chance of collisions. In general the probability of collisions relates to the number of keys generated. If <i><b>m</b></i> is the size of the keyspace
 * (aka the available number of unique keys) the chance of collisions with a previously generated key is 100 percent with the total number of generated keys
 * <i><b>n &gt;= m</b></i>.
 * <p>
 * <b><i>Hash procedure</i></b>
 * <p>
 * Hashing multiple attributes comes with a couple of challenges:
 * <ul>
 * <li>Each attribute A_n needs to be converted into a unique sequence of bytes B_n. We explicitly support some default conversion, described later.</li>
 * <li>Some attributes may be nullable, <b>null</b> shall be treated as a unique value.</li>
 * <li>For any unique combination of attributes [A_1, .., A_n] we want to create a unique sequence of bytes <code>b[] = join(B_1, .., B_n)</code> as the input
 * to the the SHA1-hash function. Obviously, we must avoid any "upfront-collisions" while preparing b[].</li>
 * </ul>
 * To avoid "upfront-collisions" and for handling null we specify a number of indicators as follows:
 * <ul>
 * <li>Every null value is represented by the null-indicator byte {@link #IND_NULL}</li>
 * <li>If any attribute A_n is a byte array, the byte sequence B_n will be preceded by {@link #IND_BYTE_ARRAY_VALUE}. We do this to make given byte arrays
 * distinguishable from any UTF-8-encoded Strings-representation of any non-array attribute value.</li>
 * <li>The byte sequence B_n for every non-null String attribute A_n will be preceded by {@link #IND_STRING_VALUE}.</li>
 * <li>The byte sequence B_n for every non-null attribute A_n, that is not a byte array and not a String, will be preceded by {@link #IND_OTHER_VALUE}</li>
 * <li>Every attribute's byte sequences B_n (or indicator) will be preceded by the spacer-byte {@link #IND_SPACER}.</li>
 * <li>If a hash pepper (see below) was specified, then the pepper will precede the total sequence of bytes <code>b[] = join(pepper, B_1, .., B_n)</code>.</li>
 * <li>If the spacer-byte {@link #IND_SPACER} occurs in any byte sequence B_n or in the hash pepper, it will be doubled to escape it.</li>
 * </ul>
 * <p>
 * The generator can be initialized with a <i>hash pepper</i> (see
 * <a href="https://en.wikipedia.org/wiki/Pepper_(cryptography)">https://en.wikipedia.org/wiki/Pepper_(cryptography)</a>). This is useful for easy
 * pseudonymization. Each pepper fills the keyspace with different keys for the same input. Usually, you won't fill the same keyspace with keys created based on
 * different peppers. However, it is worth mentioning that this would not affect the chance of collisions, because - as mentioned above - the likelihood
 * <i>only</i> depends on the number of generated keys for unique input. Consequently, n generated keys in a keyspace have the same likelihood to collide no
 * matter whether you use no, one or many peppers for creating these keys.
 * <p>
 * <b><i>Attribute conversion</i></b>
 * <ul>
 * <li>If A_n is a byte array it won't need any conversion.</li>
 * <li>If A_n is a String, B_n will be the corresponding UTF-8-encoded byte array.</li>
 * <li>If A_n is a primitive array or an array of a Java wrapper type, we will apply the corresponding Arrays.toString(A_n) method and use the UTF-8-encoded
 * byte-array. An array of primitives has the identical representation than an array of the corresponding wrappers.</li>
 * <li>If A_n is an Object-array, then we will call {@link Arrays#deepToString(Object[])} and use the UTF-8-encoded byte-array.</li>
 * <li>In any other case we will call String.valueOf(A_n) and and use the UTF-8-encoded byte-array.</li>
 * </ul>
 * The rules defined above still allow provoking certain "upfront-collisions". Given A_1 and A_2 with A_1 != A_2 it can happen that B_1 == B_2, if A_1 and A_2
 * have the same representation (after conversion to byte array) according to the specification above. However, MUHAIS are usually generated on a fixed set of
 * attributes in a defined order, so that this kind of collisions won't have any impact in practice. On the other hand the identical treatment of primitives vs.
 * wrapper types (even inside arrays) is a desired behavior.
 * <p>
 * Some detail steps like {@link #convertAttributeToByteArray(Object)} for attribute conversion are implemented as TEMPLATE METHODS, so implementors may
 * sub-class this class and override methods to modify the generator's behavior.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class MuhaiGenerator implements Serializable {

    private static final long serialVersionUID = 1212131876200805275L;

    /**
     * Thread-local holder for message digest of type SHA-1, static, because it can be used by any generator (independent from the prefix), making generator
     * instances light-weight.
     */
    private static final ThreadLocal<MessageDigest> DIGEST_HOLDER = ThreadLocal.withInitial(() -> {
        try {
            return MessageDigest.getInstance("SHA-1");
        }
        catch (NoSuchAlgorithmException ex) {
            throw new MuhaiException("Unexpected issue during SHA-1-MessageDigest initialization.", ex);
        }
    });

    /**
     * spacer indicator (between field values)
     */
    public static final byte IND_SPACER = 0;

    /**
     * indicator null
     */
    public static final byte IND_NULL = 1;

    /**
     * indicator preceding a non-empty byte array given as input
     */
    public static final byte IND_BYTE_ARRAY_VALUE = 2;

    /**
     * indicator to make encoded Strings distinguishable from other types
     */
    public static final byte IND_STRING_VALUE = 3;

    /**
     * indicator preceding encoded values not representing Strings or byte arrays
     */
    public static final byte IND_OTHER_VALUE = 4;

    /**
     * byte array of size 0
     */
    public static final byte[] EMPTY_BYTES = new byte[0];

    /**
     * Prefix for all keys, also defines the keyspace of the generator
     */
    private final LongPrefix prefix;

    /**
     * Optional pepper to be added to hashing
     */
    private final byte[] hashPepper;

    /**
     * Returns the cached instance of the digest after resetting it for the next use.
     * <p>
     * This default implementation returns an SHA-1-digest, cached in a ThreadLocal.
     * @return clean digest
     */
    protected MessageDigest initDigest() {
        MessageDigest res = DIGEST_HOLDER.get();
        res.reset();
        return res;
    }

    /**
     * Adds a single object to the given digest. This default implementation uses a couple of indicators to represent special values and escapes the
     * {@link #IND_SPACER} in the content (see {@link #escapeSpacerBytes(byte[])} and {@link MuhaiGenerator} spec).
     * @param md digest
     * @param srcValue value to be added
     */
    protected void addToDigest(MessageDigest md, Object srcValue) {
        byte[] sourceBytes = EMPTY_BYTES;
        if (srcValue == null) {
            md.update(IND_NULL);
        }
        else if (srcValue instanceof byte[]) {
            md.update(IND_BYTE_ARRAY_VALUE);
            sourceBytes = (byte[]) srcValue;
        }
        else {
            if (srcValue instanceof String) {
                md.update(IND_STRING_VALUE);
            }
            else {
                md.update(IND_OTHER_VALUE);
            }
            sourceBytes = convertAttributeToByteArray(srcValue);
        }
        if (sourceBytes.length > 0) {
            md.update(escapeSpacerBytes(sourceBytes));
        }
    }

    /**
     * Converts a given attribute to a byte array.
     * <p>
     * This default implementation follows the spec of {@link MuhaiGenerator}.
     * 
     * @param srcValue NOT NULL
     * @return a string replacing the attribute
     */
    protected byte[] convertAttributeToByteArray(Object srcValue) {
        String sourceString = null;
        Class<?> srcClass = srcValue.getClass();
        if (srcClass.isArray()) {
            Class<?> componentType = srcClass.getComponentType();
            if (componentType.isPrimitive()) {
                sourceString = JavaWrapperType.forClass(componentType).arrayToString(srcValue);
            }
            else {
                sourceString = Arrays.deepToString((Object[]) srcValue);
            }
        }
        else {
            sourceString = String.valueOf(srcValue);
        }

        return (sourceString.isEmpty() ? EMPTY_BYTES : sourceString.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Computes a hash over the given attributes using the {@link #IND_EMPTY} as delimiter, see the spec of {@link MuhaiGenerator}.
     * @param attributes source attributes (NOT EMPTY, NOT NULL), each attribute can be null.
     * @return hash value
     * @throws MuhaiException if the array was empty/null or the hashing failed for any reason
     */
    protected byte[] computeHashBytes(Object... attributes) {
        MessageDigest md = initDigest();

        if (hashPepper.length > 0) {
            md.update(hashPepper);
        }

        if (attributes == null || attributes.length == 0) {
            throw new MuhaiException(String.format("""
                    The Object array must not be null or empty (cannot hash "nothing"), found: %s.
                    In the special case that the only argument you want to pass to this method is an Object[]
                    that could be null or empty, you should cast it to Object before.""", (attributes == null ? "null" : "[]")));
        }
        try {
            for (int i = 0; i < attributes.length; i++) {
                md.update(IND_SPACER);
                addToDigest(md, attributes[i]);
            }
            return md.digest();
        }
        catch (RuntimeException ex) {
            throw new MuhaiException("Unexpected error during attribute hashing.", ex);
        }
    }

    /**
     * Escapes any occurrence of {@link #IND_SPACER} in the given byte sequence.<br />
     * This default implementation just doubles the spacer following the spec of {@link MuhaiGenerator}.
     * @param src not null, won't be modified
     * @return either src or a new longer byte sequence to replace the given one
     */
    protected byte[] escapeSpacerBytes(byte[] src) {
        byte[] res = src;
        for (int i = 0; i < src.length; i++) {
            if (src[i] == IND_SPACER) {
                res = doubleSpacerBytes(src, i);
                break;
            }
        }
        return res;
    }

    /**
     * @param src input byte sequence with at least one spacer byte at the start position
     * @param startPos first 0-byte
     * @return new byte array, where all spacers have been doubled
     */
    private static byte[] doubleSpacerBytes(byte[] src, int startPos) {
        int srcLen = src.length;
        // create a new array that has room for doubled zeros after the start position
        // we could implement more sophisticated repeated smaller increments, but as we expect
        // rather short sequences this higher complexity is not worth it.
        byte[] dest = new byte[srcLen + (srcLen - startPos)];
        if (startPos > 0) {
            System.arraycopy(src, 0, dest, 0, startPos);
        }
        int destPos = startPos;
        for (int i = startPos; i < srcLen; i++) {
            byte b = src[i];
            if (b == IND_SPACER) {
                // double the spacer byte
                dest[destPos] = IND_SPACER;
                destPos++;
                dest[destPos] = IND_SPACER;
            }
            else {
                dest[destPos] = b;
            }
            destPos++;
        }
        if (destPos < dest.length) {
            // truncate unused buffer bytes
            dest = Arrays.copyOf(dest, destPos);
        }
        return dest;
    }

    /**
     * Constructor to create a new generator
     * @param prefix bit sequence, also defines the keyspace of this generator, not null (instead use {@link LongPrefix#NONE}
     * @param pepper an optional byte sequence to be included in all hash computations, null or empty array will turn the pepper off
     */
    public MuhaiGenerator(LongPrefix prefix, byte[] pepper) {
        byte[] pepperBytes = EMPTY_BYTES;
        if (pepper != null && pepper.length > 0) {
            pepperBytes = escapeSpacerBytes(Arrays.copyOf(pepper, pepper.length));
        }
        this.prefix = prefix;
        this.hashPepper = pepperBytes;
    }

    /**
     * Constructor to create a new generator
     * @param prefix bit sequence, also defines the keyspace of this generator, not null (instead use {@link LongPrefix#NONE}
     * @param pepper an optional character sequence to be included in all hash computations, null or empty will turn the pepper off
     */
    public MuhaiGenerator(LongPrefix prefix, String pepper) {
        this(prefix, pepper == null ? EMPTY_BYTES : pepper.getBytes(StandardCharsets.UTF_8));
    }

    /**
     * Constructor to create a new generator without a hash pepper
     * @param prefix bit sequence, also defines the keyspace of this generator, not null (instead use {@link LongPrefix#NONE}
     */
    public MuhaiGenerator(LongPrefix prefix) {
        this(prefix, EMPTY_BYTES);
    }

    /**
     * @return the prefix this generator is using
     */
    public LongPrefix getPrefix() {
        return prefix;
    }

    /**
     * Returns a copy of the hash pepper byte sequence this generator uses.
     * @return byte array, if the pepper was specified as a string these are the UTF-8 bytes
     */
    public byte[] getHashPepper() {
        byte[] res = EMPTY_BYTES;
        if (this.hashPepper.length > 0) {
            res = Arrays.copyOf(hashPepper, hashPepper.length);
        }
        return res;
    }

    /**
     * Computes a key (MUHAI) from the given attributes.
     * @param attributes values to be included in hashing (NOT EMPTY, NOT NULL), each attribute can be null.
     * @return key prefixed hash value as a long following the spec of {@link MuhaiGenerator}
     */
    public long createKey(Object... attributes) {
        byte[] hashBytes = computeHashBytes(attributes);
        // we fill the bit-sequence (8 bytes, 64 bits) subsequently from the left
        long res = 0;
        for (int i = 0; i < 7; i++) {
            res = res | (hashBytes[i] & 0xff);
            res = res << 8;
        }
        // The "& 0xff"-operation below makes an unsigned byte out of a Java signed byte,
        // which is technically not necessary but will simplify later verification
        res |= hashBytes[7] & 0xff;

        if (prefix.getLength() > 0) {
            // the right-shift below "creates room" for the prefix
            res = prefix.applyTo(res >>> prefix.getLength());
        }

        return res;
    }

    /**
     * @return description
     */
    public String toString() {
        String prefixString = this.prefix.toBinaryString();
        return MuhaiGenerator.class.getSimpleName() + "(prefix=" + (prefixString.isEmpty() ? "<NONE>" : "'" + prefixString + "'") + ", hashPepper="
                + (hashPepper.length == 0 ? "<NONE>" : Arrays.toString(hashPepper)) + ", size of keyspace: " + this.prefix.getSizeOfKeyspace() + ")";
    }

    /**
     * The {@link MuhaiGenerator} has minimal thread-local state.
     * <p>
     * If (in a framework context) a thread no longer needs any generator, this method should be called to remove this cached state. The method does not affect
     * the correct function of the generator as the cache is only a performance optimization.<br />
     * In most scenarios NOT calling this method also won't have any negative impact.
     */
    public static void cleanup() {
        DIGEST_HOLDER.remove();
    }

}
