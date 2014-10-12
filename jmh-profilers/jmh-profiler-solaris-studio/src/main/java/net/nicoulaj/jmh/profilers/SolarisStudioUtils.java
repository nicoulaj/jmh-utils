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
