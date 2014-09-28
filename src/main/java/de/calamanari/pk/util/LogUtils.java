//@formatter:off
/*
 * Log Utilities
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

import java.util.logging.ConsoleHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Some utility methods related to java logging.
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public final class LogUtils {

    /**
     * Utility class
     */
    private LogUtils() {
        // no instances
    }

    /**
     * Adjust the console handler's log level, especially useful in some IDEs.
     * 
     * @param level required level
     */
    public static void setConsoleHandlerLogLevel(Level level) {
        Logger rootLogger = java.util.logging.Logger.getLogger("");

        // find console handler, if any
        for (Handler handler : rootLogger.getHandlers()) {
            if (handler instanceof ConsoleHandler) {
                handler.setLevel(level);
                return;
            }
        }

        // ok, we need a new console handler
        ConsoleHandler consoleHandler = new ConsoleHandler();
        rootLogger.addHandler(consoleHandler);
        consoleHandler.setLevel(level);
    }

    /**
     * Sets the log level related to multiple loggers.
     * 
     * @param level new level to set
     * @param loggers one or more loggers
     */
    public static void setLogLevel(Level level, Logger... loggers) {
        for (Logger logger : loggers) {
            logger.setLevel(level);
        }
    }

    /**
     * Sets the log level related to multiple classes assuming the class name as the related logger's name.
     * 
     * @param level new level to set
     * @param classes one or more classes
     */
    public static void setLogLevel(Level level, Class<?>... classes) {
        for (Class<?> cls : classes) {
            Logger.getLogger(cls.getName()).setLevel(level);
        }
    }

}
