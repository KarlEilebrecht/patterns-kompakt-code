/*
 * Abstract data reader - demonstrates ABSTRACT PRODUCT
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

import java.util.logging.Logger;

/**
 * Abstract data reader is one base class of the ABSTRACT PRODUCTs each concrete ABSTRACT FACTORY can create.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public abstract class AbstractDataReader {

    /**
     * logger
     */
    public static final Logger LOGGER = Logger.getLogger(AbstractDataReader.class.getName());

    /**
     * describes the source
     */
    private String sourceInfo = null;

    /**
     * Returns a textual description about the source.
     * @return source info, may be null if unavailable
     */
    public String getSourceInfo() {
        return sourceInfo;
    }

    /**
     * Sets the source info.
     * @param sourceInfo textual description, may be null
     */
    protected void setSourceInfo(String sourceInfo) {
        this.sourceInfo = sourceInfo;
    }

    /**
     * Reads the string from the source.
     * @return string from source
     */
    public abstract String readString();

}
