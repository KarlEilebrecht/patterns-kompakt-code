//@formatter:off
/*
 * Secure file data writer - CONCRETE PRODUCT
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
package de.calamanari.pk.abstractfactory;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.calamanari.pk.util.FileUtils;
import de.calamanari.pk.util.SimpleScrambleCodec;

/**
 * Secure file data writer, a CONCRETE PRODUCT of CONCRETE FACTORY
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class SecureFileDataWriter extends AbstractDataWriter {

    private static final Logger LOGGER = LoggerFactory.getLogger(SecureFileDataWriter.class);

    /**
     * destination file
     */
    private File destinationFile = null;

    /**
     * Creates new writer
     * 
     * @param file destination NOT NULL
     */
    public SecureFileDataWriter(File file) {
        LOGGER.debug("{} created for file {}", this.getClass().getSimpleName(), file);
        if (file == null) {
            throw new IllegalArgumentException("Argument file must not be null!");
        }
        this.setDestinationInfo("Secure File '" + file + "'");
        this.destinationFile = file;
    }

    @Override
    public long writeString(String item) {
        LOGGER.debug("{}.writeString('{}') called.", this.getClass().getSimpleName(), item);
        String scrambledItem = SimpleScrambleCodec.encode(item);
        LOGGER.debug("output='{}'.", scrambledItem);
        return FileUtils.writeStringToFile(scrambledItem, destinationFile);
    }

}
