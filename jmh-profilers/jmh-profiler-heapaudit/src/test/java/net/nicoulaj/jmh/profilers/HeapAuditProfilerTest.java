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
import java.nio.file.Paths;

import static java.nio.file.Files.createTempFile;
import static net.nicoulaj.jmh.assertions.JMHAssertions.assertJMH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.contentOf;

/**
 * Test for {@link HeapAuditProfiler}.
 *
 * @author <a href="http://github.com/nicoulaj">nicoulaj</a>
 */
public class HeapAuditProfilerTest {

    @Test
    public void test001() throws Exception {

        if (true) throw new SkipException("Not implemented");

        final Path output = createTempFile(Paths.get("target"), "jmh-output-", ".log");

        assertJMH()
                .output(output.toString())
                .addProfiler(HeapAuditProfiler.class)
                .runsWithoutError();

        assertThat(contentOf(output.toFile()))
                .contains("# Preparing profilers: heapaudit")
                .contains("# Processing profiler results: heapaudit");
    }
}
