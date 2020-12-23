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
    static final byte IND_SPACER = 0;

    /**
     * indicator to make values, null and empty distinguishable
     */
    static final byte IND_VALUE = 2;

    /**
     * indicator to make values, null and empty distinguishable
     */
    static final byte IND_EMPTY = 3;

    /**
     * indicator to make values, null and empty distinguishable
     */
    static final byte IND_NULL = 5;

    /**
     * indicator to make values, null and empty distinguishable
     */
    static final byte IND_EMPTY_BYTE_ARRAY = 7;

    /**
     * byte array of size 0
     */
    static final byte[] EMPTY_BYTES = new byte[0];

    /**
     * Prefix for all keys, also defines the keyspace of the generator
     */
    private final LongPrefix prefix;

    /**
     * Optional pepper to be added to hashing
     */
    private final byte[] hashPepper;

    /**
     * Returns the cached instance of the digest after resetting it for the next use and updating it with the optional pepper.
     * @return prepared digest
     */
    protected MessageDigest initDigest() {
        MessageDigest res = DIGEST_HOLDER.get();
        res.reset();
        if (hashPepper.length > 0) {
            res.update(hashPepper);
        }
        return res;
    }

    /**
     * Adds a single object to the given digest.
     * @param md digest
     * @param srcValue value to be added
     * @param geminateZeroBytes if true all occurrences of 0 (the {@link #IND_SPACER}) will be doubled
     */
    protected void addToDigest(MessageDigest md, Object srcValue, boolean geminateZeroBytes) {
        byte[] sourceBytes = EMPTY_BYTES;
        if (srcValue == null) {
            md.update(IND_NULL);
        }
        else if (srcValue instanceof byte[]) {
            sourceBytes = (byte[]) srcValue;
            if (sourceBytes.length == 0) {
                md.update(IND_EMPTY_BYTE_ARRAY);
            }
        }
        else {
            sourceBytes = convertAttributeToByteArray(srcValue);
            if (sourceBytes.length == 0) {
                md.update(IND_EMPTY);
            }
        }
        if (sourceBytes.length > 0) {
            md.update(IND_VALUE);
            if (geminateZeroBytes) {
                md.update(geminateSpacerBytes(sourceBytes));
            }
            else {
                md.update(sourceBytes);
            }
        }
    }

    /**
     * Converts a given attribute to a byte array.
     * <p>
     * This default implementation performs String.valueOf(not-an-array) or Arrays.deepToString(array) and UTF-8-encodes the result.
     * <p>
     * <b>Note:</b> We could add here support for arbitrary types (short, int, long, boolean etc.) with a positive impact on performance, but this would make
     * the code very complex. I have decided to focus on the simple default case and leave special conversions to the clients of this class.
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
     * Computes a hash over the given attributes
     * @param attributes source attributes (can be empty)
     * @return hash value
     */
    protected byte[] computeHashBytes(Object... attributes) {
        MessageDigest md = initDigest();
        int numberOfAttributes = (attributes == null ? 0 : attributes.length);
        boolean geminateZeroBytes = (numberOfAttributes > 1);
        for (int i = 0; i < numberOfAttributes; i++) {
            if (i > 0) {
                md.update(IND_SPACER);
            }
            addToDigest(md, attributes[i], geminateZeroBytes);
        }
        return md.digest();
    }

    /**
     * We use the {@link #IND_SPACER} byte to separate input fields. Unfortunately, the spacer byte can also occur in regular data. If so, we double the
     * occurrence to make a byte array for a set of fields unique.
     * @param src not null
     * @return either src or a new longer byte sequence to replace the given one
     */
    protected byte[] geminateSpacerBytes(byte[] src) {
        byte[] res = src;
        for (int i = 0; i < src.length; i++) {
            if (src[i] == IND_SPACER) {
                res = geminateSpacerBytes(src, i);
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
    private static byte[] geminateSpacerBytes(byte[] src, int startPos) {
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
            pepperBytes = Arrays.copyOf(pepper, pepper.length);
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
     * @param attributes values to be included in hashing, an empty array is a valid special case
     * @return key
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
        return MuhaiGenerator.class.getSimpleName() + "(prefix=" + (prefixString.isEmpty() ? "NONE>" : "'" + prefixString + "'") + ", hashPepper="
                + (hashPepper.length == 0 ? "<NONE>" : Arrays.toString(hashPepper)) + ")";
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
