/*
 * Secure file data reader - CONCRETE PRODUCT
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
package de.calamanari.pk.abstractfactory;

import java.io.File;
import java.util.logging.Logger;

import de.calamanari.pk.util.MiscUtils;

/**
 * Secure file data reader, a CONCRETE PRODUCT of CONCRETE FACTORY
 * @author <a href="mailto:Karl.Eilebrecht(a/t)web.de">Karl Eilebrecht</a>
 */
public class SecureFileDataReader extends AbstractDataReader {

    /**
     * logger
     */
    public static final Logger LOGGER = Logger.getLogger(SecureFileDataReader.class.getName());

    /**
     * source file
     */
    private File sourceFile = null;

    /**
     * Creates new reader
     * @param file source NOT NULL
     */
    public SecureFileDataReader(File file) {
        LOGGER.fine(this.getClass().getSimpleName() + " created for file " + file);
        if (file == null) {
            throw new IllegalArgumentException("Argument file must not be null!");
        }
        this.setSourceInfo("Secure File '" + file + "'");
        this.sourceFile = file;
    }

    @Override
    public String readString() {
        LOGGER.fine(this.getClass().getSimpleName() + ".readString() called.");
        String scrambledItem = MiscUtils.readFileToString(sourceFile);
        String item = MiscUtils.unscramble(scrambledItem);
        LOGGER.fine("return='" + item + "'.");
        return item;
    }

}