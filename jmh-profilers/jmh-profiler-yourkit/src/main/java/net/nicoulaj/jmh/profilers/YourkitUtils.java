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
import static net.nicoulaj.jmh.profilers.SystemUtils.Architecture.getArch;
import static net.nicoulaj.jmh.profilers.SystemUtils.Bitness.getBitness;
import static net.nicoulaj.jmh.profilers.SystemUtils.OperatingSystem.getOS;

/**
 * <a href="http://www.yourkit.com">Yourkit</a> utilities.
 *
 * @author <a href="http://github.com/nicoulaj">nicoulaj</a>
 */
public final class YourkitUtils {

    private YourkitUtils() {
        // Static class
    }

    /**
     * Detect Yourkit installation directory.
     *
     * @return {@code null} if detection failed
     */
    public static String detectYourkitHome() {

        // Try yourkit.home sysprop
        String yourkitHome = getProperty("yourkit.home");

        // Try YOURKIT env var
        if (yourkitHome == null)
            yourkitHome = System.getenv("YOURKIT_HOME");

        // TODO Try from "yourkit" command in PATH

        return yourkitHome;
    }

    /**
     * Detect Yourkit agent library to use for current OS/arch/bitness.
     *
     * @return {@code null} if detection failed
     */
    public static String detectYourkitAgentLib() {
        // After http://www.yourkit.com/docs/java/help/agent.jsp
        switch (getOS()) {
        case WINDOWS:
            switch (getBitness()) {
            case BITS_32:
                return "bin\\win32\\yjpagent.dll";
            case BITS_64:
                return "bin\\win64\\yjpagent.dll";
            }
        case LINUX:
            switch (getArch()) {
            case X86:
            case AMD64:
                switch (getBitness()) {
                case BITS_32:
                    return "bin/linux-x86-32/libyjpagent.so";
                case BITS_64:
                    return "bin/linux-x86-64/libyjpagent.so";
                }
            case PPC:
                return "bin/linux-ppc-32/libyjpagent.so";
            case PPC64:
                return "bin/linux-ppc-64/libyjpagent.so";
            }
        case SOLARIS:
            switch (getArch()) {
            case X86:
            case AMD64:
                switch (getBitness()) {
                case BITS_32:
                    return "bin/linux-x86-32/libyjpagent.so";
                case BITS_64:
                    return "bin/linux-x86-64/libyjpagent.so";
                }
            case SPARC:
                switch (getBitness()) {
                case BITS_32:
                    return "bin/solaris-x86-32/libyjpagent.so";
                case BITS_64:
                    return "bin/solaris-x86-64/libyjpagent.so";
                }
            }
        case MAC:
            return "bin/mac/libyjpagent.jnilib";
        case HP_UX:
            switch (getBitness()) {
            case BITS_32:
                return "bin/hpux-ia64-32/libyjpagent.so";
            case BITS_64:
                return "bin/hpux-ia64-64/libyjpagent.so";
            }
        case AIX:
            switch (getBitness()) {
            case BITS_32:
                return "bin/aix-ppc-32/libyjpagent.so";
            case BITS_64:
                return "bin/aix-ppc-64/libyjpagent.so";
            }
        case FREEBSD:
            switch (getBitness()) {
            case BITS_32:
                return "bin/freebsd-x86-32/libyjpagent.so";
            case BITS_64:
                return "bin/freebsd-x86-64/libyjpagent.so";
            }
        }
        return null;
    }
}
