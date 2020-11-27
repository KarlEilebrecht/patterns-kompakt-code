//@formatter:off
/*
 * Log Utilities
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
package de.calamanari.pk.util;

import org.slf4j.Logger;
import org.slf4j.event.Level;

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

    public static boolean isLoggingEnabled(Logger logger, Level level) {
        boolean res = false;
        if (level != null) {
            //@formatter:off
            switch (level) {
                case TRACE: res = logger.isTraceEnabled(); break;
                case DEBUG: res = logger.isDebugEnabled(); break;
                case INFO: res = logger.isInfoEnabled(); break;
                case WARN: res = logger.isWarnEnabled(); break;
                case ERROR:  res = logger.isErrorEnabled(); break;
            }
            //@formatter:on
        }
        return res;
    }

    public static void log(Logger logger, Level level, String message) {
        if (level != null) {
            //@formatter:off
            switch (level) {
                case TRACE: logger.trace(message); break;
                case DEBUG: logger.debug(message); break;
                case INFO: logger.info(message); break;
                case WARN: logger.warn(message); break;
                case ERROR:  logger.error(message); break;
            }
            //@formatter:on
        }
    }

    public static void log(Logger logger, Level level, String message, Throwable t) {
        if (level != null) {
            //@formatter:off
            switch (level) {
                case TRACE: logger.trace(message, t); break;
                case DEBUG: logger.debug(message, t); break;
                case INFO: logger.info(message, t); break;
                case WARN: logger.warn(message, t); break;
                case ERROR:  logger.error(message, t); break;
            }
            //@formatter:on
        }
    }

    /**
     * Formats the given String in quotes, should the given string be longer then limit, it will be truncated
     * @param s (null will not be quoted)
     * @param limit (0 or negative turns off the limit)
     * @return formatted string in quotes, note: the maximum length of the returned String is <code>limit+6</code>
     */
    public static String limitAndQuoteStringForMessage(String s, int limit) {
        if (s == null) {
            return "null";
        }
        else if (limit > 0 && s.length() > limit) {
            return "'" + s.substring(0, limit) + " ...'";
        }
        else {
            return "'" + s + "'";
        }
    }

}
