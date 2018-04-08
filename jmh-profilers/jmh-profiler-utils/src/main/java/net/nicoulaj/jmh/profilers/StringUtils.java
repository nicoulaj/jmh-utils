/*
 * JMH utils - http://nicoulaj.github.com/jmh-utils
 * Copyright © 2014-2018 JMH utils contributors
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as
 * published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public
 * License along with this program.  If not, see
 * <http://www.gnu.org/licenses/gpl-3.0.html>.
 */
package net.nicoulaj.jmh.profilers;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Iterator;

import static java.util.Arrays.asList;

/**
 * Static {@link String} helpers.
 *
 * @author <a href="http://github.com/nicoulaj">nicoulaj</a>
 */
public final class StringUtils {

    private static final int BUFFER_SIZE = 4 * 1024;

    private StringUtils() {
        // Static class
    }

    /**
     * Join strings with given separator.
     *
     * @param separator separator to use
     * @param strings   strings to join
     * @return joined strings
     */
    public static String join(String separator, String... strings) {
        return join(separator, asList(strings));
    }

    /**
     * Join strings with given separator.
     *
     * @param separator separator to use
     * @param strings   strings to join
     * @return joined strings
     */
    public static String join(String separator, Iterable<String> strings) {
        final StringBuilder buffer = new StringBuilder();
        for (Iterator<String> iter = strings.iterator(); iter.hasNext(); ) {
            buffer.append(iter.next());
            if (iter.hasNext()) buffer.append(separator);
        }
        return buffer.toString();
    }

    /**
     * Read an {@link InputStream}  into a {@link String}.
     *
     * @param inputStream input stream
     * @return output string
     * @throws IOException if failed to read from stream
     */
    public static String toString(InputStream inputStream) throws IOException {
        return toString(inputStream, Charset.defaultCharset());
    }

    /**
     * Read an {@link InputStream}  into a {@link String}.
     *
     * @param inputStream input stream
     * @param charset     charset to use
     * @return output string
     * @throws IOException if failed to read from stream
     */
    public static String toString(InputStream inputStream, Charset charset) throws IOException {
        final StringBuilder builder = new StringBuilder();
        final InputStreamReader reader = new InputStreamReader(inputStream, charset);
        final char[] buffer = new char[BUFFER_SIZE];
        int length;
        while ((length = reader.read(buffer)) != -1)
            builder.append(buffer, 0, length);
        return builder.toString();
    }
}
