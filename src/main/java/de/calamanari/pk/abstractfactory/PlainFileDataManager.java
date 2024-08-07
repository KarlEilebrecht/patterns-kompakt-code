//@formatter:off
/*
 * Plain file data manager - CONCRETE FACTORY
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

/**
 * Plain file data manager uses a text file for persistence, it demonstrates a CONCRETE FACTORY.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class PlainFileDataManager extends AbstractDataManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(PlainFileDataManager.class);

    /**
     * Creates new Plain File Data Manager
     */
    public PlainFileDataManager() {
        LOGGER.debug("{} created.", this.getClass().getSimpleName());
        this.setName("Plain File Data Manager");
    }

    @Override
    public PlainFileDataWriter createDataWriter(String itemName) {
        LOGGER.debug("{}.createDataWriter() called.", this.getClass().getSimpleName());
        if (itemName == null || (itemName.trim().length() == 0)) {
            // to be honest, the rule is not strict enough :-)
            throw new IllegalArgumentException("Argument itemName must not be null or empty.");
        }
        File destinationFile = new File(FileUtils.getHomeDirectory(), itemName + ".txt");
        return new PlainFileDataWriter(destinationFile);
    }

    @Override
    public PlainFileDataReader createDataReader(String itemName) {
        if (itemName == null || (itemName.trim().length() == 0)) {
            // to be honest, the rule is not strict enough :-)
            throw new IllegalArgumentException("Argument itemName must not be null or empty.");
        }
        File sourceFile = new File(FileUtils.getHomeDirectory(), itemName + ".txt");
        return new PlainFileDataReader(sourceFile);
    }

}
