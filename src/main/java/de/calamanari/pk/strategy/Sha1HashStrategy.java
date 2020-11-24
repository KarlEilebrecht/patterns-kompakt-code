//@formatter:off
/*
 * Sha1HashStrategy - concrete hash STRATEGY using SHA-1.
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
//@formatter:on
package de.calamanari.pk.strategy;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Sha1HashStrategy - concrete hash STRATEGY using SHA-1.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class Sha1HashStrategy extends HashStrategy {

    private static final Logger LOGGER = LoggerFactory.getLogger(Sha1HashStrategy.class);

    /**
     * name (key) of this strategy
     */
    public static final String STRATEGY_NAME = "SHA1";

    /**
     * base for hey conversion (AND)
     */
    private static final int HASH_BYTES_BASE = 0xFF;

    /**
     * Creates new hash strategy instance
     */
    public Sha1HashStrategy() {
        super(STRATEGY_NAME);
    }

    @Override
    public String computeHash(String text) {
        LOGGER.debug("Concrete Strategy {} computes hash ...", STRATEGY_NAME);
        return computeSecureHash(text);
    }

    /**
     * Computes a secure hash (SHA-1, according to ISO/IEC 10118-3:2004) from the given string.<br>
     * <i>For further information on SHA-1 see also:</i>
     * <ul>
     * <li>http://www.itl.nist.gov/fipspubs/fip180-1.htm</li>
     * <li>http://java.sun.com/javase/6/docs/technotes/guides/security/crypto/CryptoSpec.html</li>
     * <li>http://java.sun.com/javase/6/docs/technotes/guides/security/SunProviders.html</li>
     * </ul>
     * 
     * @param text to be hashed
     * @return hash-string (hex), never null
     */
    private String computeSecureHash(String text) {
        String result = "";
        if (text != null && text.length() > 0) {
            try {
                MessageDigest md = MessageDigest.getInstance("SHA-1");
                md.reset();
                md.update(text.getBytes(StandardCharsets.UTF_8));
                result = convertBytesToHexString(md.digest());
            }
            catch (Exception ex) {
                throw new RuntimeException("Error applying SHA-1 hash.", ex);
            }
        }
        return result;
    }

    /**
     * Converts the given bytes to a hex-string.
     * 
     * @param hashBytes source bytes
     * @return hex-string
     */
    protected static String convertBytesToHexString(byte[] hashBytes) {
        StringBuilder sbResult = new StringBuilder();
        int len = hashBytes.length;
        for (int i = 0; i < len; i++) {
            sbResult.append(Integer.toHexString(HASH_BYTES_BASE & hashBytes[i]));
        }
        return sbResult.toString();
    }

}
