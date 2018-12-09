//@formatter:off
/*
 * Checksum Helper - the DIRECTOR in this BUILDER example.
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
package de.calamanari.pk.builder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Checksum Helper - the DIRECTOR in this BUILDER example.<br>
 * Instances of this class create a checksum out of a given object array using a BUILDER.<br>
 * These helper instances are not safe to be used concurrently by multiple threads.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class ChecksumHelper {

    private static final Logger LOGGER = LoggerFactory.getLogger(ChecksumHelper.class);

    /**
     * Reference to the internally used builder.
     */
    private final ChecksumBuilder checksumBuilder;

    /**
     * Creates new helper instance using the given builder (kind of CONSTRUCTOR INJECTION).
     * 
     * @param checksumBuilder instance to be used internally
     */
    public ChecksumHelper(ChecksumBuilder checksumBuilder) {
        this.checksumBuilder = checksumBuilder;
    }

    /**
     * This method of the director uses the builder INTERNALLY to compute a checksum over the given array elements.
     * 
     * @param data array elements
     * @return checksum
     */
    public long computeChecksum(Object[] data) {

        LOGGER.debug("Helper uses the builder ...");

        checksumBuilder.reset();
        if (data == null) {
            checksumBuilder.addString(null);
        }
        else {

            // the number of steps to build a product (the checksum) in this example
            // are:

            int len = data.length;
            // prefix step
            LOGGER.debug("add general prefix ...");
            checksumBuilder.addString("[" + len + "]");

            for (int i = 0; i < len; i++) {
                Object element = data[i];

                // field prefix step
                LOGGER.debug("add field prefix ...");
                checksumBuilder.addString("/" + i + "=");

                LOGGER.debug("add field value ...");
                if (element == null || element instanceof String) {
                    checksumBuilder.addString((String) element);
                }
                else if (element instanceof Integer) {
                    checksumBuilder.addInteger((Integer) element);
                }
                else if (element instanceof Long) {
                    checksumBuilder.addLong((Long) element);
                }
                else if (element instanceof Double) {
                    checksumBuilder.addDouble((Double) element);
                }
                else if (element instanceof byte[]) {
                    checksumBuilder.addBytes((byte[]) element);
                }
            }
        }
        LOGGER.debug("Helper retrieves the result (checksum) from the builder ...");
        Checksum checksum = checksumBuilder.getChecksum();
        return checksum.getValue();
    }

}
