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

import static java.lang.System.getProperty;
import static java.util.Arrays.asList;

/**
 * Static helpers for implementing {@link org.openjdk.jmh.profile.Profiler}.
 *
 * @author <a href="http://github.com/nicoulaj">nicoulaj</a>
 */
public final class ProfilerUtil {

    private ProfilerUtil() {
        // Static class
    }

    /**
     * Check whether the current JVM is HotSpot.
     *
     * @return {@code true} if HotSpot system property matches
     */
    public static boolean isHotSpot() {
        return getProperty("java.vm.name", "unknown").matches(".*(OpenJDK|HotSpot).*");
    }

    /**
     * Returns the boolean value of the system property with the
     * specified name. The first argument is treated as the name of a
     * system property. System properties are accessible through the
     * {@link java.lang.System#getProperty(java.lang.String)} method.
     * The string value of this property is then interpreted as an
     * boolean value, as per the {@link java.lang.Boolean#parseBoolean(String)} method,
     * and an {@code Boolean} object representing this value is
     * returned.
     * <p/>
     * <p>The second argument is the default value. The default value is
     * returned if there is no property of the specified name, if the
     * property does not have the correct numeric format, or if the
     * specified name is empty or {@code null}.
     *
     * @param nm  property name.
     * @param val default value.
     * @return the {@code Integer} value of the property.
     * @throws SecurityException for the same reasons as
     *                           {@link System#getProperty(String) System.getProperty}
     * @see System#getProperty(java.lang.String)
     * @see System#getProperty(java.lang.String, java.lang.String)
     */
    public static Boolean getBoolean(String nm, Boolean val) {
        String v = null;
        try {
            v = System.getProperty(nm);
        } catch (IllegalArgumentException | NullPointerException e) {
            // Default
        }
        if (v != null) return Boolean.parseBoolean(v);
        return val;
    }

    /**
     * Join strings with given separator
     *
     * @param separator separator to use
     * @param strings   strings to join
     * @return joined strings
     */
    public static String join(String separator, String... strings) {
        return join(separator, asList(strings));
    }

    /**
     * Join strings with given separator
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
