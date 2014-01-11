/*
 * Charset Utilities
 * Code-Beispiel zum Buch Patterns Kompakt, Verlag Springer Vieweg
 * Copyright 2014 Karl Eilebrecht
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
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
package de.calamanari.pk.util;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CodingErrorAction;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This utility class provides support to work with characters of different character sets especially related to the
 * encoded byte-length of their characters.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public final class CharsetUtils {

    /**
     * A CSV-list of {@linkplain Charset}s where the encoded <i>byte-length</i> of each character is independent from
     * the preceding characters, and thus the byte position can be calculated by summing up the byte-length of any
     * preceding character.<br>
     * For certain {@linkplain Charset}s this is in general <i>not</i> the case because they use control sequences to
     * switch to different character subsets. {@linkplain Charset}s using BOMs are <i>semi-agnostic</i>, the BOM must be
     * handled properly.<br>
     * This list might be incomplete.
     * <p>
     * <code>
     * {@value}
     * </code>
     */
    public static final String CONTEXT_AGNOSTIC_CHARSET_NAMES = "big5, big5-hkscs, euc-jp, euc-kr, gb18030, gb2312, "
            + "gbk, ibm-thai, ibm00858, ibm01140, ibm01141, ibm01142, ibm01143, ibm01144, ibm01145, ibm01146, "
            + "ibm01147, ibm01148, ibm01149, ibm037, ibm1026, ibm1047, ibm273, ibm277, ibm278, ibm280, ibm284, "
            + "ibm285, ibm297, ibm420, ibm424, ibm437, ibm500, ibm775, ibm850, ibm852, ibm855, ibm857, ibm860, "
            + "ibm861, ibm862, ibm863, ibm864, ibm865, ibm866, ibm868, ibm869, ibm870, ibm871, ibm918, iso-8859-1, "
            + "iso-8859-13, iso-8859-15, iso-8859-2, iso-8859-3, iso-8859-4, iso-8859-5, iso-8859-6, iso-8859-7, "
            + "iso-8859-8, iso-8859-9, jis_x0201, jis_x0212-1990, koi8-r, koi8-u, macroman, shift_jis, tis-620, "
            + "us-ascii, utf-16, utf-16be, utf-16le, utf-32, utf-32be, utf-32le, utf-8, windows-1250, windows-1251, "
            + "windows-1252, windows-1253, windows-1254, windows-1255, windows-1256, windows-1257, windows-1258, "
            + "windows-31j, x-big5-hkscs-2001, x-big5-solaris, x-euc-jp-linux, x-eucjp-open, x-ibm1006, x-ibm1025, "
            + "x-ibm1046, x-ibm1097, x-ibm1098, x-ibm1112, x-ibm1122, x-ibm1123, x-ibm1124, x-ibm1381, x-ibm1383, "
            + "x-ibm33722, x-ibm737, x-ibm833, x-ibm856, x-ibm874, x-ibm875, x-ibm921, x-ibm922, x-ibm942, x-ibm942c, "
            + "x-ibm943, x-ibm943c, x-ibm948, x-ibm950, x-ibm964, x-iscii91, x-iso-8859-11, x-jis0208, x-johab, "
            + "x-macarabic, x-maccentraleurope, x-maccroatian, x-maccyrillic, x-macdingbat, x-macgreek, x-machebrew, "
            + "x-maciceland, x-macromania, x-macsymbol, x-macthai, x-macturkish, x-macukraine, x-ms950-hkscs, "
            + "x-ms950-hkscs-xp, x-mswin-936, x-pck, x-utf-16le-bom, x-utf-32be-bom, x-utf-32le-bom, x-windows-874, "
            + "x-windows-949, x-windows-950";

    /**
     * List of context-agnostic {@linkplain Charset}s, see {@link #CONTEXT_AGNOSTIC_CHARSET_NAMES}.
     */
    public static final List<String> CONTEXT_AGNOSTIC_CHARSETS;
    static {
        String[] supportedCharsets = CONTEXT_AGNOSTIC_CHARSET_NAMES.split("[,]");
        List<String> supportedCharsetList = new ArrayList<>(supportedCharsets.length);
        for (String charsetName : supportedCharsets) {
            supportedCharsetList.add(charsetName.trim().toLowerCase());
        }
        CONTEXT_AGNOSTIC_CHARSETS = Collections.unmodifiableList(supportedCharsetList);
    }
    /**
     * A registry for indexes storing the length in bytes per character, here we cache index table for fast lookup
     */
    private static final Map<String, byte[]> CHAR_LENGTH_LOOKUP_REGISTRY = new ConcurrentHashMap<>();

    /**
     * This registry stores the surrogate byte-length for character sets.
     */
    private static final Map<Charset, Byte> SURROGATE_PAIR_LENGTH_REGISTRY = new ConcurrentHashMap<>();

    /**
     * character code for line break
     */
    public static final int LINE_BREAK_CODE = (int) '\n';

    /**
     * character code for carriage return
     */
    public static final int CARRIAGE_RETURN_CODE = (int) '\r';

    /**
     * minumum surrogate character code: {@value}
     */
    public static final int MIN_SURROGATE_CODE = 55296;

    /**
     * maxumum surrogate character code: {@value}
     */
    public static final int MAX_SURROGATE_CODE = 57343;
    
    /**
     * minimum high surrogate chararacter code: {@value}
     */
    public static final int MIN_HIGH_SURROGATE_CODE = 56320;
    
    /**
     * maximum bytes for buffering a character
     */
    private static final int CHARACTER_BUFFER_BYTES = 20;
    
    /**
     * Utility class
     */
    private CharsetUtils() {
        // no instances
    }
    
    /**
     * Surrogates are special characters that cannot occur alone (when encoded) and thus will never be read alone. They
     * occur as pairs.<br>
     * Fortunately the pair-size (number of encoded bytes) is constant.<br>
     * This method detects this pair-length.
     * @param charset character set
     * @return number of bytes
     */
    public static final byte detectSurrogatePairLength(Charset charset) {
        Byte res = SURROGATE_PAIR_LENGTH_REGISTRY.get(charset);
        if (res == null) {
            CharBuffer charBuffer = CharBuffer.allocate(2);
            ByteBuffer byteBuffer = ByteBuffer.allocate(CHARACTER_BUFFER_BYTES);
            CharsetEncoder encoder = charset.newEncoder();
            encoder.onUnmappableCharacter(CodingErrorAction.REPLACE);
            // some encoders write a BOM, we use a rewind-trick to effectively skip the BOM
            charBuffer.append('x');
            charBuffer.rewind();
            encoder.encode(charBuffer, byteBuffer, true);
            byteBuffer.clear();
            charBuffer.clear();
            charBuffer.append("\uD800\uDC00");
            charBuffer.rewind();
            encoder.encode(charBuffer, byteBuffer, true);
            res = (byte) byteBuffer.position();
            SURROGATE_PAIR_LENGTH_REGISTRY.put(charset, res);
        }
        return res;
    }

    /**
     * Creates a character length lookup for the given character set.<br>
     * Internally a static cache is used for performance reasons.
     * @param charsetName name of character set
     * @return byte array with length per code for all 65536 char-values
     */
    public static final byte[] createCharLengthLookup(String charsetName) {
        byte[] charLengthLookup = CHAR_LENGTH_LOOKUP_REGISTRY.get(charsetName);
        if (charLengthLookup == null) {
            charLengthLookup = new byte[(int) Character.MAX_VALUE + 1];
            CharBuffer charBuffer = CharBuffer.allocate(1);
            ByteBuffer byteBuffer = ByteBuffer.allocate(CHARACTER_BUFFER_BYTES);
            Charset charset = Charset.forName(charsetName);
            byte surrogatePairSize = detectSurrogatePairLength(charset);
            CharsetEncoder encoder = charset.newEncoder();
            // we use ignore here, because characters not mappable cannot be read from a source
            encoder.onUnmappableCharacter(CodingErrorAction.IGNORE);
            for (int i = 0; i <= Character.MAX_VALUE; i++) {
                char ch = (char) i;
                if (i >= MIN_SURROGATE_CODE && i <= MAX_SURROGATE_CODE) {
                    // surrogate handling works as follows:
                    // high surrogate is mapped to zero-size, low surrogate is mapped to pair-size
                    // this makes sense since the position before the low surrogate has no corresponding byte-position
                    if (i >= MIN_HIGH_SURROGATE_CODE && i <= MAX_SURROGATE_CODE) {
                        // low surrogate
                        charLengthLookup[i] = surrogatePairSize;
                    }
                }
                else {
                    byteBuffer.clear();
                    charBuffer.clear();
                    charBuffer.append(ch);
                    charBuffer.rewind();
                    encoder.encode(charBuffer, byteBuffer, false);
                    charLengthLookup[i] = (byte) byteBuffer.position();
                }
            }
            CHAR_LENGTH_LOOKUP_REGISTRY.put(charsetName, charLengthLookup);
        }
        // cannot risk to return the original cached array
        return charLengthLookup.clone();
    }
}
