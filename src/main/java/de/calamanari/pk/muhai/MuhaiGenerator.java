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
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class MuhaiGenerator implements Serializable {

    private static final long serialVersionUID = 1212131876200805275L;

    private static final ConcurrentHashMap<String, MuhaiGenerator> INSTANCES = new ConcurrentHashMap<>();

    /**
     * Thread-local holder for message digest of type SHA-1, static, because it can be used with by any generator (independent from the prefix)
     */
    private static final ThreadLocal<MessageDigest> DIGEST_HOLDER = ThreadLocal.withInitial(() -> {
        try {
            // DO NOT CHANGE DIGEST! (would affect existing hash codes)
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
     * Prefix and cache key, the only non-transient information to be serialized for later resolving the registered instance, a SINGLETON per prefix.
     */
    private final String prefixString;

    private final transient String someOtherPayload;

    public static MuhaiGenerator getInstance(String prefix) {
        return INSTANCES.computeIfAbsent(prefix, MuhaiGenerator::new);
    }

    private MuhaiGenerator(String prefix) {
        this.prefixString = prefix;
        this.someOtherPayload = "Banana_" + UUID.randomUUID();
    }

    /**
     * Returns the cached instance of the digest after resetting it for the next use.
     * @return clean digest
     */
    static MessageDigest initDigest() {
        MessageDigest res = DIGEST_HOLDER.get();
        res.reset();
        return res;
    }

    /**
     * Adds a single object to the given digest.
     * @param md digest
     * @param srcValue value to be added
     * @param geminateZeroBytes if true all occurrences of 0 (the {@link #IND_SPACER}) will be doubled
     */
    private static void addToDigest(MessageDigest md, Object srcValue, boolean geminateZeroBytes) {
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
            // we could add here support for arbitrary types (short, int, long, boolean etc.) with a
            // positive impact on performance, but this would make the code very complex.
            // I have decided to focus on the simple default case and leave special conversions to the clients of this class.
            // Here we only support byte array and use UTF-8-encoded toString() for anything else.
            // A conversion STRATEGY (convert(Object):String) could be an alternative implementation approach.
            String sourceString = String.valueOf(srcValue);
            if (!sourceString.isEmpty()) {
                sourceBytes = sourceString.getBytes(StandardCharsets.UTF_8);
            }
            else {
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
     * Computes a hash over the given attributes
     * @param attributes source attributes (can be empty)
     * @return hash value
     */
    static byte[] computeHashBytes(Object... attributes) {
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
    static byte[] geminateSpacerBytes(byte[] src) {
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

    public String toString() {
        return this.prefixString + " -- " + this.someOtherPayload;
    }

    /**
     * Replaces the instance during deserialization with the SINGLETON per prefix, so that serialization won't create duplicates in the same VM.
     * @return generator instance
     */
    Object readResolve() {
        return getInstance(this.prefixString);
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
