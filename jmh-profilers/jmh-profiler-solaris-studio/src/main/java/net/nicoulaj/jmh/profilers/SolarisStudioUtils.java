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

/**
 * <a href="http://www.oracle.com/technetwork/server-storage/solarisstudio/overview/index.html">Solaris Studio</a> utilities.
 *
 * @author <a href="http://github.com/nicoulaj">nicoulaj</a>
 */
public class SolarisStudioUtils {

    /**
     * Check whether <code>collect</code> command is available.
     *
     * @return <code>true</code> if found in <code>PATH</code>.
     */
    public static boolean isCollectAvailable() {
        try {
            final Process p = new ProcessBuilder("collect", "-V").start();
            final String output = StringUtils.toString(p.getErrorStream());
            return output.contains("Oracle Solaris Studio Performance Analyzer");
        } catch (IOException e) {
            return false;
        }
    }
}
