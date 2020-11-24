//@formatter:off
/*
 * FileUtils
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

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

/**
 * Utilities methods for dealing with files
 * 
 * @author <a href="mailto:Karl.Eilebrecht(a/t)calamanari.de">Karl Eilebrecht</a>
 */
public final class FileUtils {

    /**
     * Utility class
     */
    private FileUtils() {
        // no instances
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
        return writeStringToFile(str, destinationFile, StandardCharsets.UTF_8);
    }

    /**
     * Writes the given string to the specified file.
     * 
     * @param str string to be written
     * @param destinationFile target file
     * @param charset character set, null means "UTF-8"
     * @return file size
     */
    public static long writeStringToFile(String str, File destinationFile, Charset charset) {
        if (charset == null) {
            charset = StandardCharsets.UTF_8;
        }
        try {
            Files.write(destinationFile.toPath(), str.getBytes(charset));
        }
        catch (IOException | RuntimeException ex) {
            throw new FileAccessException(String.format("Unable to write String %s to file %s using charset %s.",
                    LogUtils.limitAndQuoteStringForMessage(str, 100), String.valueOf(destinationFile), String.valueOf(charset)), ex);
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
        return readFileToString(sourceFile, StandardCharsets.UTF_8);
    }

    /**
     * Reads the given file and returns its content as a string
     * 
     * @param sourceFile file to be read
     * @param charset character set, null means "UTF-8"
     * @return String with file content, line separator = '\n'
     */
    public static String readFileToString(File sourceFile, Charset charset) {
        if (charset == null) {
            charset = StandardCharsets.UTF_8;
        }
        StringBuilder res = null;
        try {
            List<String> lines = Files.readAllLines(sourceFile.toPath(), charset);
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
        catch (IOException | RuntimeException ex) {
            throw new FileAccessException(
                    String.format("Unable to read source file %s into a String using charset %s.", String.valueOf(sourceFile), String.valueOf(charset)), ex);
        }
        return (res == null ? "" : res.toString());
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
            throw new FileAccessException("Unable to find resource: " + resourceName);
        }
        return new File(url.getFile());
    }

}
