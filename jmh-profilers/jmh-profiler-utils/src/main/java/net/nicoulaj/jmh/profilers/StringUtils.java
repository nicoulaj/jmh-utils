/*
 * ====================================================================
 * JMH utils :: profilers :: utils
 * ====================================================================
 * Copyright (C) 2014 Julien Nicoulaud <julien.nicoulaud@gmail.com>
 * ====================================================================
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
 * ====================================================================
 */
package net.nicoulaj.jmh.profilers;

import java.util.Iterator;

import static java.util.Arrays.asList;

/**
 * Static {@link String} helpers.
 *
 * @author <a href="http://github.com/nicoulaj">nicoulaj</a>
 */
public final class StringUtils {

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
}
