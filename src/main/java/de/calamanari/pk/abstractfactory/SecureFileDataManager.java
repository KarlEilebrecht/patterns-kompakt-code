//@formatter:off
/*
 * Secure file data manager - CONCRETE FACTORY
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
 * Secure file data manager uses a text file for persistence, it demonstrates a CONCRETE FACTORY.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public class SecureFileDataManager extends AbstractDataManager {

    /**
     * logger
     */
    public static final Logger LOGGER = Logger.getLogger(SecureFileDataManager.class.getName());

    /**
     * Creates new Secure File Data Manager
     */
    public SecureFileDataManager() {
        this.setName("Secure File Data Manager");
    }

    @Override
    public SecureFileDataWriter createDataWriter(String itemName) {
        LOGGER.fine(this.getClass().getSimpleName() + ".createDataWriter() called.");
        if (itemName == null || (itemName.trim().length() == 0)) {
            // to be honest, the rule is not strict enough :-)
            throw new IllegalArgumentException("Argument itemName must not be null or empty.");
        }
        File destinationFile = new File(MiscUtils.getHomeDirectory(), itemName + ".sec");
        SecureFileDataWriter res = new SecureFileDataWriter(destinationFile);
        return res;
    }

    @Override
    public SecureFileDataReader createDataReader(String itemName) {
        LOGGER.fine(this.getClass().getSimpleName() + ".createDataReader() called.");
        if (itemName == null || (itemName.trim().length() == 0)) {
            // to be honest, the rule is not strict enough :-)
            throw new IllegalArgumentException("Argument itemName must not be null or empty.");
        }
        File sourceFile = new File(MiscUtils.getHomeDirectory(), itemName + ".sec");
        SecureFileDataReader res = new SecureFileDataReader(sourceFile);
        return res;
    }

}
