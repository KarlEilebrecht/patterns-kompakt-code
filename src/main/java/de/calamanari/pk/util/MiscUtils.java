//@formatter:off
/*
 * Miscellaneous Utilities
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.UnsupportedEncodingException;
import java.lang.management.ManagementFactory;
import java.lang.reflect.Array;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.event.Level;

/**
 * Miscellaneous utilities
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public final class MiscUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(MiscUtils.class);
    /**
     * JVM start time in nanoseconds (estimated, for delta calculation)
     */
    private static final long STARTUP_NANO_TIME;
    static {
        long deltaNanosSinceJvmStart = (System.currentTimeMillis() - ManagementFactory.getRuntimeMXBean().getStartTime()) * 1_000_000;
        STARTUP_NANO_TIME = System.nanoTime() - deltaNanosSinceJvmStart;
    }

    private static final String UTF_8 = "UTF-8";

    /**
     * one million
     */
    public static final int MILLION = 1_000_000;

    /**
     * one billion
     */
    public static final int BILLION = 1_000_000_000;

    /**
     * Utility class
     */
    private MiscUtils() {
        // no instances
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
     * Returns the user's home directory
     * 
     * @return current user's home directory
     */
    public static File getHomeDirectory() {
        return new File(System.getProperty("user.home"));
    }

    /**
     * Writes the given string to the specified file using UTF-8 character set.
     * 
     * @param str string to be written
     * @param destinationFile target file
     * @return file size
     */
    public static long writeStringToFile(String str, File destinationFile) {
        return writeStringToFile(str, destinationFile, UTF_8);
    }

    /**
     * Writes the given string to the specified file.
     * 
     * @param str string to be written
     * @param destinationFile target file
     * @param charsetName character set, null means "UTF-8"
     * @return file size
     */
    public static long writeStringToFile(String str, File destinationFile, String charsetName) {
        if (charsetName == null) {
            charsetName = UTF_8;
        }
        try {
            Files.write(destinationFile.toPath(), str.getBytes(charsetName));
        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        return destinationFile.length();
    }

    /**
     * Reads the given file and returns its content as a string using UTF-8 character set.
     * 
     * @param sourceFile file to be read
     * @return String with file content
     */
    public static String readFileToString(File sourceFile) {
        return readFileToString(sourceFile, UTF_8);
    }

    /**
     * Reads the given file and returns its content as a string
     * 
     * @param sourceFile file to be read
     * @param charsetName character set, null means "UTF-8"
     * @return String with file content, line separator = '\n'
     */
    public static String readFileToString(File sourceFile, String charsetName) {
        if (charsetName == null) {
            charsetName = UTF_8;
        }
        StringBuilder res = null;
        try {
            List<String> lines = Files.readAllLines(sourceFile.toPath(), Charset.forName(charsetName));
            int count = 0;
            int len = lines.size();
            if (len > 0) {
                res = new StringBuilder(100 + len * 20);
                for (String line : lines) {
                    if (count > 0) {
                        res.append("\n");
                    }
                    res.append(line);
                    count++;
                }
            }
        }
        catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        return (res == null ? "" : res.toString());
    }

    /**
     * This method encodes a given string - using top secret algorithm! :-)
     * 
     * @param source text
     * @return scrambled text
     */
    public static String scramble(String source) {
        char[] characters = source.toCharArray();
        int len = characters.length;
        int startOffset = len % 17;
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char ch = characters[i];
            if (ch >= 65 && ch <= 90) {
                int code = ch;
                code = code - 65;
                code = code + startOffset + i;
                code = code % 26;
                code = code + 97;
                ch = (char) code;
            }
            else if (ch >= 97 && ch <= 122) {
                int code = ch;
                code = code - 97;
                code = code + startOffset + i;
                code = code % 26;
                code = code + 65;
                ch = (char) code;
            }
            sb.append(ch);
        }
        return sb.toString();
    }

    /**
     * This method decodes a given string - using top secret algorithm! :-)
     * 
     * @param scrambled text
     * @return unscrambled text
     */
    public static String unscramble(String scrambled) {
        char[] characters = scrambled.toCharArray();
        int len = characters.length;
        int startOffset = len % 17;
        StringBuilder sb = new StringBuilder(len);
        for (int i = 0; i < len; i++) {
            char ch = characters[i];
            if (ch >= 65 && ch <= 90) {
                int code = ch;
                code = code - 65;
                int dif = (startOffset + i) - code;
                if (dif > 0) {
                    dif = dif + ((26 - (dif % 26)) % 26);
                    code = code + dif;
                }
                code = code - (startOffset + i);
                code = code + 97;
                ch = (char) code;
            }
            else if (ch >= 97 && ch <= 122) {
                int code = ch;
                code = code - 97;
                int dif = (startOffset + i) - code;
                if (dif > 0) {
                    dif = dif + ((26 - (dif % 26)) % 26);
                    code = code + dif;
                }
                code = code - (startOffset + i);
                code = code + 65;
                ch = (char) code;
            }
            sb.append(ch);
        }
        return sb.toString();
    }

    /**
     * Returns the file object for the denoted resource
     * 
     * @param resourceName name of resource
     * @return file object
     */
    public static File findFile(String resourceName) {
        URL url = ClassLoader.getSystemResource(resourceName);
        if (url == null) {
            throw new RuntimeException("Unable to find resource: " + resourceName);
        }
        return new File(url.getFile());
    }

    /**
     * Returns the bytes of the given string using UTF-8.
     * 
     * @param text string to be converted into bytes
     * @return bytes after encoding the given text using UTF-8
     */
    public static byte[] getUtf8Bytes(String text) {
        byte[] bytes = null;
        try {
            bytes = text.getBytes(UTF_8);
        }
        catch (UnsupportedEncodingException ex) {
            // should not happen
            throw new RuntimeException(ex);
        }
        return bytes;
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
     * Returns the time in seconds formatted
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
     * Simulates a pass-by-value situation (serialize/deserialize the given object)
     * 
     * @param input must be serializable, not null
     * @param <T> type to be serialized and deserialized
     * @return object passed by value
     * @throws IOException if serialization/deserialization failed
     */
    public static <T extends Object> T passByValue(T input) throws IOException {
        @SuppressWarnings("resource")
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(bos)) {
            oos.writeObject(input);
        }
        bos.close();

        try (ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray()); ObjectInputStream ois = new ObjectInputStream(bis)) {
            @SuppressWarnings("unchecked")
            T res = (T) input.getClass().cast(ois.readObject());
            return res;
        }
        catch (ClassNotFoundException | ClassCastException ex) {
            throw new IOException("Unexpected type change after serialization/deserialization." + ex);
        }
    }

    /**
     * This method "boxes" an array of primitives to the corresponding array of wrapper instances.
     * 
     * @param primitiveArray (i.e. long[])
     * @param <T> wrapper type of array for boxing
     * @return wrapper array (i.e. Long[])
     */
    public static <T> T[] boxArray(Object primitiveArray) {
        Class<?> primitiveType = primitiveArray.getClass().getComponentType();
        if (primitiveType == null || !primitiveType.isPrimitive()) {
            throw new IllegalArgumentException("The given argument is not an array of primitive values.");
        }
        int len = Array.getLength(primitiveArray);
        Object[] wrapperArray = null;
        if (byte.class.equals(primitiveType)) {
            wrapperArray = boxByteArray(primitiveArray, len);
        }
        else if (short.class.equals(primitiveType)) {
            wrapperArray = boxShortArray(primitiveArray, len);
        }
        else if (int.class.equals(primitiveType)) {
            wrapperArray = boxIntArray(primitiveArray, len);
        }
        else if (long.class.equals(primitiveType)) {
            wrapperArray = boxLongArray(primitiveArray, len);
        }
        else if (float.class.equals(primitiveType)) {
            wrapperArray = boxFloatArray(primitiveArray, len);
        }
        else if (double.class.equals(primitiveType)) {
            wrapperArray = boxDoubleArray(primitiveArray, len);
        }
        else if (boolean.class.equals(primitiveType)) {
            wrapperArray = boxBooleanArray(primitiveArray, len);
        }
        else if (char.class.equals(primitiveType)) {
            wrapperArray = boxCharArray(primitiveArray, len);
        }
        else {
            // should not happen :-)
            throw new IllegalArgumentException("Unsupported type " + primitiveType + ".");
        }

        @SuppressWarnings({ "unchecked" })
        T[] res = (T[]) wrapperArray;
        return res;
    }

    private static Object[] boxCharArray(Object primitiveArray, int len) {
        Object[] wrapperArray;
        char[] charArray = (char[]) primitiveArray;
        wrapperArray = new Character[len];
        for (int i = 0; i < len; i++) {
            wrapperArray[i] = Character.valueOf(charArray[i]);
        }
        return wrapperArray;
    }

    private static Object[] boxBooleanArray(Object primitiveArray, int len) {
        Object[] wrapperArray;
        boolean[] booleanArray = (boolean[]) primitiveArray;
        wrapperArray = new Boolean[len];
        for (int i = 0; i < len; i++) {
            wrapperArray[i] = Boolean.valueOf(booleanArray[i]);
        }
        return wrapperArray;
    }

    private static Object[] boxDoubleArray(Object primitiveArray, int len) {
        Object[] wrapperArray;
        double[] doubleArray = (double[]) primitiveArray;
        wrapperArray = new Double[len];
        for (int i = 0; i < len; i++) {
            wrapperArray[i] = Double.valueOf(doubleArray[i]);
        }
        return wrapperArray;
    }

    private static Object[] boxFloatArray(Object primitiveArray, int len) {
        Object[] wrapperArray;
        float[] floatArray = (float[]) primitiveArray;
        wrapperArray = new Float[len];
        for (int i = 0; i < len; i++) {
            wrapperArray[i] = Float.valueOf(floatArray[i]);
        }
        return wrapperArray;
    }

    private static Object[] boxLongArray(Object primitiveArray, int len) {
        Object[] wrapperArray;
        long[] longArray = (long[]) primitiveArray;
        wrapperArray = new Long[len];
        for (int i = 0; i < len; i++) {
            wrapperArray[i] = Long.valueOf(longArray[i]);
        }
        return wrapperArray;
    }

    private static Object[] boxIntArray(Object primitiveArray, int len) {
        Object[] wrapperArray;
        int[] intArray = (int[]) primitiveArray;
        wrapperArray = new Integer[len];
        for (int i = 0; i < len; i++) {
            wrapperArray[i] = Integer.valueOf(intArray[i]);
        }
        return wrapperArray;
    }

    private static Object[] boxShortArray(Object primitiveArray, int len) {
        Object[] wrapperArray;
        short[] shortArray = (short[]) primitiveArray;
        wrapperArray = new Short[len];
        for (int i = 0; i < len; i++) {
            wrapperArray[i] = Short.valueOf(shortArray[i]);
        }
        return wrapperArray;
    }

    private static Object[] boxByteArray(Object primitiveArray, int len) {
        Object[] wrapperArray;
        byte[] byteArray = (byte[]) primitiveArray;
        wrapperArray = new Byte[len];
        for (int i = 0; i < len; i++) {
            wrapperArray[i] = Byte.valueOf(byteArray[i]);
        }
        return wrapperArray;
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
                    if (logLevel != null && LogUtils.isLogginEnabled(LOGGER, logLevel)) {
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
            if (logLevel != null && LogUtils.isLogginEnabled(LOGGER, logLevel)) {
                LOGGER.warn("Interrupted while sleeping.", ex);
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
     * The latter will be wrapped in a {@linkplain RuntimeException}.
     * 
     * @param millis time in milliseconds to sleep
     */
    public static void sleepThrowRuntimeException(long millis) {
        try {
            Thread.sleep(millis);
        }
        catch (InterruptedException ex) {
            Thread.currentThread().interrupt();
            throw new RuntimeException(ex);
        }
    }

    /**
     * Determines whether the current VM runs on a Microsoft Windows System.
     * 
     * @return true if OS is Windows otherwise false
     */
    public static boolean isWindowsOS() {
        boolean res = false;
        String osName = System.getProperty("os.name");
        if (osName != null && osName.startsWith("Windows")) {
            res = true;
        }
        return res;
    }
}
