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
package net.nicoulaj.jmh.assertions;

import org.openjdk.jmh.results.RunResult;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;

import java.util.Collection;

/**
 * Assertions for <a href="http://openjdk.java.net/projects/code-tools/jmh">JMH</a>
 *
 * @author <a href="http://github.com/nicoulaj">nicoulaj</a>
 */
public final class JMHAssertions {

    private JMHAssertions() {
        // Static class
    }

    public static JMHRunBuilderAssert assertJMH() {
        return new RunBuilderAssert();
    }

    public static JMHRunResultsAssert assertJMH(Options options) {
        return assertJMH(run(options));
    }

    public static JMHRunResultsAssert assertJMH(Collection<RunResult> results) {
        return new RunResultsAssert(results);
    }

    public static JMHRunResultAssert assertJMH(RunResult result) {
        return new RunResultAssert(result);
    }

    private static Collection<RunResult> run(Options options) {
        try {
            return new Runner(options).run();
        } catch (RunnerException e) {
            throw new AssertionError(e);
        }
    }
}
