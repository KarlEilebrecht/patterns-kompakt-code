/*
 * Checksum Helper - the DIRECTOR in this BUILDER example.
 * Code-Beispiel zum Buch Patterns Kompakt, Verlag Springer Vieweg
 * Copyright 2013 Karl Eilebrecht
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
package de.calamanari.pk.builder;

import java.util.logging.Logger;

/**
 * Checksum Helper - the DIRECTOR in this BUILDER example.<br>
 * Instances of this class create a checksum out of a given object array using a BUILDER.<br>
 * These helper instances are not safe to be used concurrently by multiple threads.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)web.de">Karl Eilebrecht</a>
 */
public class ChecksumHelper {

    /**
     * logger
     */
    protected static final Logger LOGGER = Logger.getLogger(ChecksumHelper.class.getName());

    /**
     * Reference to the internally used builder.
     */
    private final ChecksumBuilder checksumBuilder;

    /**
     * Creates new helper instance using the given builder (kind of CONSTRUCTOR INJECTION).
     * @param checksumBuilder instance to be used internally
     */
    public ChecksumHelper(ChecksumBuilder checksumBuilder) {
        this.checksumBuilder = checksumBuilder;
    }

    /**
     * This method of the director uses the builder INTERNALLY to compute a checksum over the given array elements.
     * @param data array elements
     * @return checksum
     */
    public long computeChecksum(Object[] data) {

        LOGGER.fine("Helper uses the builder ...");

        checksumBuilder.reset();
        if (data == null) {
            checksumBuilder.addString(null);
        }
        else {

            // the number of steps to build a product (the checksum) in this example
            // are:

            int len = data.length;
            // prefix step
            LOGGER.fine("add general prefix ...");
            checksumBuilder.addString("[" + len + "]");

            for (int i = 0; i < len; i++) {
                Object element = data[i];

                // field prefix step
                LOGGER.fine("add field prefix ...");
                checksumBuilder.addString("/" + i + "=");

                LOGGER.fine("add field value ...");
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
        LOGGER.fine("Helper retrieves the result (checksum) from the builder ...");
        Checksum checksum = checksumBuilder.getChecksum();
        return checksum.getValue();
    }

}