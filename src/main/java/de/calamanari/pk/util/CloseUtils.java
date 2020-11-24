//@formatter:off
/*
 * CloseUtils
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
package de.calamanari.pk.util;

import java.io.Closeable;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

/**
 * Methods to simplify closing (chains of) resources and to make code less verbose.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class CloseUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(CloseUtils.class);

    private CloseUtils() {
        // util, no instances
    }

    /**
     * Closes the given resource(s) and catches any exception
     * 
     * @param logLevel the level for logging the exceptions, null suppresses logging
     * @param closeables resource(s) to be closed, null elements will be silently ignored
     */
    public static void closeResourceCatch(Level logLevel, Closeable... closeables) {
        for (Closeable closeable : closeables) {
            if (closeable != null) {
                try {
                    closeable.close();
                }
                catch (IOException | RuntimeException ex) {
                    if (logLevel != null && LogUtils.isLoggingEnabled(LOGGER, logLevel)) {
                        LogUtils.log(LOGGER, logLevel, "Error closing resource.", ex);
                    }
                }
            }
        }
    }

    /**
     * Closes the given resource(s) and catches any exception
     * 
     * @param closeables resource(s) to be closed, null elements will be silently ignored
     */
    public static void closeResourceCatch(Closeable... closeables) {
        closeResourceCatch(null, closeables);
    }

}
