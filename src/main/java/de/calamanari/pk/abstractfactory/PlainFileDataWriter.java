//@formatter:off
/*
 * Plain file data writer - CONCRETE PRODUCT
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
package de.calamanari.pk.abstractfactory;

import java.io.File;
import java.util.logging.Logger;

import de.calamanari.pk.util.MiscUtils;

/**
 * Plain file data writer, a CONCRETE PRODUCT of CONCRETE FACTORY
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class PlainFileDataWriter extends AbstractDataWriter {

    /**
     * logger
     */
    public static final Logger LOGGER = Logger.getLogger(PlainFileDataWriter.class.getName());

    /**
     * destination file
     */
    private File destinationFile = null;

    /**
     * Creates new writer
     * 
     * @param file destination NOT NULL
     */
    public PlainFileDataWriter(File file) {
        LOGGER.fine(this.getClass().getSimpleName() + " created.");
        if (file == null) {
            throw new IllegalArgumentException("Argument file must not be null!");
        }
        this.setDestinationInfo("Plain File '" + file + "'");
        this.destinationFile = file;
    }

    @Override
    public long writeString(String item) {
        LOGGER.fine(this.getClass().getSimpleName() + ".writeString('" + item + "') called.");
        LOGGER.fine("output='" + item + "'.");
        return MiscUtils.writeStringToFile(item, destinationFile);
    }

}
