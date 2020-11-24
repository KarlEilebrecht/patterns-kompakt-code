//@formatter:off
/*
 * TimeUtils
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

import java.lang.management.ManagementFactory;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

/**
 * Utility class to support some time computation.
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 *
 */
public class TimeUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(TimeUtils.class);

    /**
     * JVM start time in nanoseconds (estimated, for delta calculation)
     */
    public static final long STARTUP_NANO_TIME;
    static {
        long deltaNanosSinceJvmStart = (System.currentTimeMillis() - ManagementFactory.getRuntimeMXBean().getStartTime()) * 1_000_000;
        STARTUP_NANO_TIME = System.nanoTime() - deltaNanosSinceJvmStart;
    }

    private TimeUtils() {
        // utility class
    }

    /**
     * Nanoseconds since system startup
     * <ul>
     * <li>The first call initializes the measurement. This is the only calculation involving <i>wall clock time</i> ( <code>System.currentTimeMillis()</code>).
     * <br>
     * Thus it is a good idea to call this method once right after JVM start to avoid any problems with clock adjustments (i.g. daylight saving).</li>
     * <li>The precision may vary on different platforms.</li>
     * </ul>
     * 
     * @return positive value denoting time in nanoseconds
     */
    public static long getSystemUptimeNanos() {
        long nowNanos = System.nanoTime();
        long startNanos = STARTUP_NANO_TIME;
        long deltaNanos = 0;
        if (startNanos > 0 && nowNanos < 0) {
            startNanos = startNanos - Long.MAX_VALUE;
            nowNanos = nowNanos - Long.MAX_VALUE;
        }
        if (startNanos <= 0) {
            deltaNanos = startNanos - nowNanos;
            if (deltaNanos < 0) {
                deltaNanos = deltaNanos * (-1);
            }
        }
        else {
            deltaNanos = nowNanos - startNanos;
        }
        return deltaNanos;
    }

    /**
     * Sets the time parts of the given calendar to 0
     * 
     * @param cal some date
     */
    public static void setMidnight(Calendar cal) {
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
    }

    /**
     * Returns the difference between the two dates in days (at midnight).<br>
     * Be aware of the fact that this straight-forward implementation doesn't care much about time zones (use default)!
     * 
     * @param date1 first date
     * @param date2 second date
     * @return difference in days
     */
    public static int dayDiff(Date date1, Date date2) {
        Calendar c1 = Calendar.getInstance();
        Calendar c2 = Calendar.getInstance();

        int factor = 1;
        if (date1.compareTo(date2) > 0) {
            factor = -1;
        }

        c1.setTime(factor == 1 ? date1 : date2);
        c2.setTime(factor == 1 ? date2 : date1);

        setMidnight(c1);
        setMidnight(c2);

        int days = 0;
        while (c1.before(c2)) {
            c1.add(Calendar.DAY_OF_MONTH, 1);
            days++;
        }
        return days * factor;

    }

    /**
     * Returns the time in seconds US-formatted with up to 6 decimals
     * 
     * @param nanos time in nanoseconds
     * @return formatted time in seconds
     */
    public static String formatNanosAsSeconds(long nanos) {
        NumberFormat nf = NumberFormat.getInstance(Locale.US);
        nf.setMaximumFractionDigits(6);
        nf.setMinimumFractionDigits(0);
        return nf.format(((double) nanos) / 1000000000);
    }

    /**
     * Calls {@linkplain Thread#sleep(long)} and optionally logs any {@linkplain InterruptedException}
     * 
     * @param logLevel if null or level is disabled, ignores the exception, otherwise logs with the given level
     * @param millis time in milliseconds to sleep
     */
    public static void sleep(Level logLevel, long millis) {
        try {
            Thread.sleep(millis);
        }
        catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            if (logLevel != null && LogUtils.isLoggingEnabled(LOGGER, logLevel)) {
                LOGGER.warn(String.format("Interrupted while sleeping, configured millis was: %d", millis), ex);
            }
        }
    }

    /**
     * Sleeps for the given time or until an {@linkplain InterruptedException} occurs.<br>
     * The latter will be suppressed.
     * 
     * @param millis time in milliseconds to sleep
     */
    public static void sleepIgnoreException(long millis) {
        sleep(null, millis);
    }

    /**
     * Sleeps for the given time or until an {@linkplain InterruptedException} occurs.<br>
     * The latter will be wrapped in an {@link UnexpectedInterruptedException}.
     * 
     * @param millis time in milliseconds to sleep
     */
    public static void sleepThrowRuntimeException(long millis) {
        try {
            Thread.sleep(millis);
        }
        catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new UnexpectedInterruptedException(String.format("Interrupted while sleeping, configured millis was: %d", millis), ex);
        }
    }

}
