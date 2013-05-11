/*
 * Checksum Builder - interface for checksum BUILDERs.
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

/**
 * Checksum Builder - interface for checksum BUILDERs.<br>
 * The PRODUCTs of these checksum builders are Checksum instances.<br>
 * If not otherwise stated, concrete instances are NOT safe to be used concurrently by multiple threads.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)web.de">Karl Eilebrecht</a>
 */
public interface ChecksumBuilder {

    /**
     * Builder-method to add a string
     * @param text some string or null
     */
    public void addString(String text);

    /**
     * Builder-method to add a long value
     * @param value some numeric value or null
     */
    public void addLong(Long value);

    /**
     * Builder-method to add an integer value
     * @param value some numeric value or null
     */
    public void addInteger(Integer value);

    /**
     * Builder-method to add an double value
     * @param value some numeric value or null
     */
    public void addDouble(Double value);

    /**
     * Builder-method to add a byte array
     * @param bytes some bytes or null
     */
    public void addBytes(byte[] bytes);

    /**
     * Concrete BUILDERs return checksum for the given text.<br>
     * This is the result of the BUILDERs life.
     * @return checksum value
     */
    public Checksum getChecksum();

    /**
     * re-initializes the builder, the instance shall afterwards behave like a new one.
     */
    public void reset();

}
