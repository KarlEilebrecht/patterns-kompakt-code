/*
 * Plain file data manager - CONCRETE FACTORY
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
package de.calamanari.pk.abstractfactory;

import java.io.File;
import java.util.logging.Logger;

import de.calamanari.pk.util.MiscUtils;

/**
 * Plain file data manager uses a text file for persistence, it demonstrates a CONCRETE FACTORY.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class PlainFileDataManager extends AbstractDataManager {

    /**
     * logger
     */
    public static final Logger LOGGER = Logger.getLogger(PlainFileDataManager.class.getName());

    /**
     * Creates new Plain File Data Manager
     */
    public PlainFileDataManager() {
        LOGGER.fine(this.getClass().getSimpleName() + " created.");
        this.setName("Plain File Data Manager");
    }

    @Override
    public PlainFileDataWriter createDataWriter(String itemName) {
        LOGGER.fine(this.getClass().getSimpleName() + ".createDataWriter() called.");
        if (itemName == null || (itemName.trim().length() == 0)) {
            // to be honest, the rule is not strict enough :-)
            throw new IllegalArgumentException("Argument itemName must not be null or empty.");
        }
        File destinationFile = new File(MiscUtils.getHomeDirectory(), itemName + ".txt");
        PlainFileDataWriter res = new PlainFileDataWriter(destinationFile);
        return res;
    }

    @Override
    public PlainFileDataReader createDataReader(String itemName) {
        if (itemName == null || (itemName.trim().length() == 0)) {
            // to be honest, the rule is not strict enough :-)
            throw new IllegalArgumentException("Argument itemName must not be null or empty.");
        }
        File sourceFile = new File(MiscUtils.getHomeDirectory(), itemName + ".txt");
        PlainFileDataReader res = new PlainFileDataReader(sourceFile);
        return res;
    }

}
