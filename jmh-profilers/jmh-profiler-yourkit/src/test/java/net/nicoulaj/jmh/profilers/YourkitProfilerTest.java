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

import org.testng.SkipException;
import org.testng.annotations.Test;

import java.nio.file.Path;

import static java.nio.file.Files.createDirectories;
import static java.nio.file.Files.createTempFile;
import static java.nio.file.Paths.get;
import static net.nicoulaj.jmh.assertions.JMHAssertions.assertJMH;
import static net.nicoulaj.jmh.profilers.YourkitUtils.detectYourkitAgentLib;
import static net.nicoulaj.jmh.profilers.YourkitUtils.detectYourkitHome;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.contentOf;

/**
 * Test for {@link YourkitProfiler}.
 *
 * @author <a href="http://github.com/nicoulaj">nicoulaj</a>
 */
public class YourkitProfilerTest {

    @Test
    public void test001() throws Exception {

        if (detectYourkitHome() == null || detectYourkitAgentLib() == null)
            throw new SkipException("Profiler not available in this environment, cannot test");

        createDirectories(get("target"));
        final Path output = createTempFile(get("target"), "jmh-output-", ".log");

        System.setProperty("jmh.yourkit.dir", "target");
        System.setProperty("jmh.yourkit.logdir", "target");

        assertJMH()
                .output(output.toString())
                .addProfiler(YourkitProfiler.class)
                .runsWithoutError();

        assertThat(contentOf(output.toFile()))
                .containsIgnoringCase("# Preparing profilers: yourkit")
                .containsIgnoringCase("# Processing profiler results: yourkit")
                .contains("Yourkit snapshot at ");
    }
}
