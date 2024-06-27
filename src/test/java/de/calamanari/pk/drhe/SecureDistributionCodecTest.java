//@formatter:off
/*
 * SecureDistributionCodecTest
 * Code-Beispiel zum Buch Patterns Kompakt, Verlag Springer Vieweg
 * Copyright 2024 Karl Eilebrecht
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
package de.calamanari.pk.drhe;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.nio.charset.StandardCharsets;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.calamanari.pk.drhe.util.BitUtils;

/**
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class SecureDistributionCodecTest {

    static final Logger LOGGER = LoggerFactory.getLogger(SecureDistributionCodecTest.class);

    @Test
    public void testSecureEncode() {

        for (int i = 0; i < 65536; i++) {
            String input = "" + i;
            assertEquals(input, encodeDecode(input));
        }
    }

    private String encodeDecode(String input) {
        String encoded = SecureDistributionCodec.encodeBase64(input.getBytes(StandardCharsets.UTF_8), "Test");

        LOGGER.debug("{} -------------------------------> {} encoded", input, encoded);

        String decoded = new String(SecureDistributionCodec.decodeBase64(encoded, "Test"), StandardCharsets.UTF_8);
        LOGGER.debug("{} -------------------------------> {} decoded", encoded, decoded);
        return decoded;
    }

    @Test
    public void testCreateRandomPattern() {

        // We create a very repetitive input document by concatenating the same sentence over and over again.
        // Then we encode (encrypt) it using our codec to create a bit sequence so we can perform tests
        // at https://mzsoltmolnar.github.io/random-bitstream-tester/

        String input = "The quick brown fox jumped over the lazy dog.";

        StringBuilder sb = new StringBuilder();
        do {
            sb.append(input);
            sb.append("\n");
        } while (sb.length() < 150_000);

        input = sb.toString();

        byte[] encoded = SecureDistributionCodec.encode(input.getBytes(StandardCharsets.UTF_8), "1234");

        assertEquals(input, new String(SecureDistributionCodec.decode(encoded, "1234"), StandardCharsets.UTF_8));

        StringBuilder sbResult = new StringBuilder();
        for (byte b : encoded) {
            sbResult.append(BitUtils.binStr(b));
        }

        LOGGER.debug("{}", sbResult);
    }
}