//@formatter:off
/*
 * MuhaiUtils 
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

import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A couple of utilities and shorthands for dealing with MUHAIs (the type long in Java is signed).
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class MuhaiUtils {

    /**
     * Minimum muhai represented as an unsigned integer String, left-padded with zeros, length 20.
     */
    public static final String MIN_INT_STRING = "00000000000000000000";

    /**
     * Maximum muhai represented as an unsigned integer String <code>(2^64 - 1)</code>, length 20.
     */
    public static final String MAX_INT_STRING = "18446744073709551615";

    /**
     * Minimum muhai represented as an unsigned binary String, left-padded with zeros, length 64.
     */
    public static final String MIN_BIN_STRING = Stream.generate(() -> "0").limit(64).collect(Collectors.joining());

    /**
     * Maximum muhai represented as an unsigned binary String. Be aware that this is the equivalent of the java long-value <b><code>-1</code></b>.
     */
    public static final String MAX_BIN_STRING = Stream.generate(() -> "1").limit(64).collect(Collectors.joining());

    /**
     * Minimum muhai represented as an unsigned integer hex-String, left-padded with zeros, length 16.
     */
    public static final String MIN_HEX_STRING = "0000000000000000";

    /**
     * Maximum muhai represented as an unsigned integer hex-String, length 16, lower-case letters.
     */
    public static final String MAX_HEX_STRING = "ffffffffffffffff";

    private MuhaiUtils() {
        // static helper methods
    }

    /**
     * Returns the unsigned integer representation of the given long, left-padded with 0s, fixed length of 20 digits.
     * @param muhai long representation of the identifier
     * @return unsigned integer representation as a String in the range [{@value #MIN_INT_STRING} .. {@value #MAX_INT_STRING}]
     */
    public static String toPaddedIntString(long muhai) {
        return applyPaddingIfRequired(Long.toUnsignedString(muhai), MIN_INT_STRING);
    }

    /**
     * Returns the unsigned integer representation of the given long.
     * @param muhai long representation of the identifier
     * @return unsigned integer representation as String (max length 20), no padding, same as {@link Long#toUnsignedString(long)}
     */
    public static String toIntString(long muhai) {
        return Long.toUnsignedString(muhai);
    }

    /**
     * Returns muhai's long representation.
     * @param muhai unsigned integer representation of the muhai (left-padding with 0s is optional)
     * @return long value of the muhai
     * @throws MuhaiParseException if the given string could not be converted
     */
    public static long fromIntString(String muhai) {
        return parseMuhaiString(muhai, 10,
                () -> String.format("Invalid MUHAI, expected integer String in range [%s .. %s], given: %s", MIN_INT_STRING, MAX_INT_STRING, muhai));
    }

    /**
     * Returns the unsigned integer's binary representation (0, 1) of the given long, left-padded with 0s, fixed length of 64 digits.
     * @param muhai long representation of the identifier
     * @return unsigned integer's binary representation as a String in the range [{@value #MIN_BIN_STRING} .. {@value #MAX_BIN_STRING}]
     */
    public static String toPaddedBinaryString(long muhai) {
        return applyPaddingIfRequired(Long.toBinaryString(muhai), MIN_BIN_STRING);
    }

    /**
     * Returns the unsigned integer's binary representation (0, 1) of the given long.
     * @param muhai long representation of the identifier
     * @return unsigned integer's binary representation as String (max length 64), no padding, same as {@link Long#toBinaryString(long)}
     */
    public static String toBinaryString(long muhai) {
        return Long.toBinaryString(muhai);
    }

    /**
     * Returns muhai's long representation.
     * @param muhai unsigned integer's binary representation of the muhai (left-padding with 0s is optional)
     * @return long value of the given muhai
     * @throws MuhaiParseException if the given string could not be converted
     */
    public static long fromBinaryString(String muhai) {
        return parseMuhaiString(muhai, 2, () -> String.format("Invalid MUHAI, expected binary String composed of 0s and 1s in range [%s .. %s], given: %s",
                MIN_BIN_STRING, MAX_BIN_STRING, muhai));
    }

    /**
     * Returns the unsigned integer's hex-representation (0 .. f) of the given long, left-padded with 0s, fixed length of 16 characters.
     * @param muhai long representation of the identifier
     * @return unsigned integer's hex-representation as a String in the range [{@value #MIN_HEX_STRING} .. {@value #MAX_HEX_STRING}]
     */
    public static String toPaddedHexString(long muhai) {
        return applyPaddingIfRequired(Long.toHexString(muhai), MIN_HEX_STRING);
    }

    /**
     * Returns the unsigned integer's binary representation (0 .. f) of the given long.
     * @param muhai long representation of the identifier
     * @return unsigned integer's binary hex-representation as String (max length 16), no padding, same as {@link Long#toHexString(long)}
     */
    public static String toHexString(long muhai) {
        return Long.toHexString(muhai);
    }

    /**
     * Returns muhai's long representation.
     * @param muhai unsigned integer's hex-representation (lower-case) of the muhai (left-padding with 0s is optional)
     * @return long value of the given muhai
     * @throws MuhaiParseException if the given string could not be converted
     */
    public static long fromHexString(String muhai) {
        return parseMuhaiString(muhai, 16, () -> String.format("Invalid MUHAI, expected hex-String composed of (0 .. f) in range [%s .. %s], given: %s",
                MIN_HEX_STRING, MAX_HEX_STRING, muhai));
    }

    /**
     * Apply left-padding using the given template to ensure a given size
     * @param str input string
     * @param template size definition
     * @return str or str with left-padding
     */
    static String applyPaddingIfRequired(String str, String template) {
        if (str.length() < template.length()) {
            char[] chars = template.toCharArray();
            str.getChars(0, str.length(), chars, template.length() - str.length());
            str = new String(chars);
        }
        return str;
    }

    /**
     * Removes optional padding zeros starting from the left
     * @param muhai string with optional leading zeros
     * @return stripped muhai
     */
    static String removeLeftPaddingZeros(String muhai) {
        int startPos = 0;
        for (int i = 0; i < muhai.length() - 1; i++) {
            if (muhai.charAt(i) == '0') {
                startPos++;
            }
            else {
                break;
            }
        }
        if (startPos > 0) {
            muhai = muhai.substring(startPos);
        }
        return muhai;
    }

    /**
     * Returns muhai's long representation, see see {@link Long#parseUnsignedLong(String, int)}
     * @param muhai unsigned integer representation of the muhai (left-padding with 0s is optional)
     * @param radix type of string representation
     * @param errorMessageSupplier message to include in exception in case of issues
     * @return long value of the given muhai
     * @throws MuhaiParseException if the given string could not be converted
     */
    static long parseMuhaiString(String muhai, int radix, Supplier<String> errorMessageSupplier) {
        if (muhai == null || muhai.isEmpty()) {
            throw new MuhaiParseException(errorMessageSupplier.get());
        }
        long res = 0;
        try {
            res = Long.parseUnsignedLong(removeLeftPaddingZeros(muhai), radix);
        }
        catch (NumberFormatException ex) {
            throw new MuhaiParseException(errorMessageSupplier.get(), ex);
        }
        return res;
    }

}
