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

import static java.lang.System.getProperty;

/**
 * Static {@link java.lang.System} helpers.
 *
 * @author <a href="http://github.com/nicoulaj">nicoulaj</a>
 */
public final class SystemUtils {

    private SystemUtils() {
        // Static class
    }

    /**
     * Returns the boolean value of the system property with the
     * specified name. The first argument is treated as the name of a
     * system property. System properties are accessible through the
     * {@link System#getProperty(String)} method.
     * The string value of this property is then interpreted as an
     * boolean value, as per the {@link Boolean#parseBoolean(String)} method,
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
     * @see System#getProperty(String)
     * @see System#getProperty(String, String)
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
     * A Java Virtual Machine.
     *
     * @author <a href="http://github.com/nicoulaj">nicoulaj</a>
     * @see OperatingSystem
     * @see Architecture
     * @see Bitness
     */
    public static enum JVM {

        /** HotSpot. */
        HOTSPOT,

        /** Oracle JRockit. */
        JROCKIT,

        /** Azul Zing. */
        ZING,

        /** IBM J9. */
        J9,

        /** Graal VM. */
        GRAAL,

        /** Unknown. */
        UNKNOWN;

        public static JVM getJVM() {
            // FIXME This is totally broken, only HotSpot is detected
            final String osname = getProperty("java.vm.name", "unknown").toLowerCase();
            if (osname.matches(".*(openjdk|hotspot).*")) return HOTSPOT;
            return UNKNOWN;
        }
    }

    /**
     * A platform / operating system.
     *
     * @author <a href="http://github.com/nicoulaj">nicoulaj</a>
     * @see Architecture
     * @see Bitness
     * @see JVM
     */
    public static enum OperatingSystem {

        /** Windows. */
        WINDOWS,

        /** Linux. */
        LINUX,

        /** Solaris. */
        SOLARIS,

        /** Mac. */
        MAC,

        /** HP-UX. */
        HP_UX,

        /** AIX. */
        AIX,

        /** FreeBSD. */
        FREEBSD,

        /** Unknown. */
        UNKNOWN;

        public static OperatingSystem getOS() {
            // FIXME This is totally broken, at least HP-UX, AIX and FreeBSD are missing
            final String osname = getProperty("os.name", "unknown").toLowerCase();
            if (osname.startsWith("windows")) return WINDOWS;
            if (osname.startsWith("linux")) return LINUX;
            if (osname.startsWith("sunos")) return SOLARIS;
            if (osname.startsWith("mac") || osname.startsWith("darwin")) return MAC;
            return UNKNOWN;
        }
    }

    /**
     * A system architecture.
     *
     * @author <a href="http://github.com/nicoulaj">nicoulaj</a>
     * @see OperatingSystem
     * @see Bitness
     * @see JVM
     */
    public static enum Architecture {

        X86,
        AMD64,
        ARM,
        PPC,
        PPC64,
        SPARC,
        IA64,
        UNKNOWN;

        public static Architecture getArch() {
            // FIXME "Works on my machine"
            switch (getProperty("os.arch", "unknown").toLowerCase()) {
            case "x86":
            case "i386":
            case "i486":
            case "i586":
            case "i686":
                return X86;
            case "x86_64":
            case "amd64":
                return AMD64;
            case "sparc":
                return SPARC;
            case "ppc":
                return PPC;
            }
            return UNKNOWN;
        }
    }

    /**
     * 32/64 bits.
     *
     * @author <a href="http://github.com/nicoulaj">nicoulaj</a>
     * @see Architecture
     * @see OperatingSystem
     * @see JVM
     */
    public static enum Bitness {

        /** 32 bits. */
        BITS_32,

        /** 64 bits. */
        BITS_64,

        /** Unknown. */
        UNKNOWN;

        public static Bitness getBitness() {
            // FIXME Probably only works on Hotspot
            switch (getProperty("sun.arch.data.model", "unknown")) {
            case "32":
                return BITS_32;
            case "64":
                return BITS_64;
            }
            return UNKNOWN;
        }
    }
}
