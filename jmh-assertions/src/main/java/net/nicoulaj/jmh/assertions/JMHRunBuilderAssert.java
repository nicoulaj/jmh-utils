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

import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.profile.Profiler;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.TimeValue;
import org.openjdk.jmh.runner.options.VerboseMode;
import org.openjdk.jmh.runner.options.WarmupMode;

import java.util.concurrent.TimeUnit;

/**
 * Assertions builder.
 *
 * @author <a href="http://github.com/nicoulaj">nicoulaj</a>
 * @see net.nicoulaj.jmh.assertions.JMHAssertions
 */
public interface JMHRunBuilderAssert extends JMHRunResultsAssert {

    JMHRunBuilderAssert parent(Options other);

    JMHRunBuilderAssert include(String regexp);

    JMHRunBuilderAssert exclude(String regexp);

    JMHRunBuilderAssert resultFormat(ResultFormatType type);

    JMHRunBuilderAssert output(String filename);

    JMHRunBuilderAssert result(String filename);

    JMHRunBuilderAssert shouldDoGC(boolean value);

    JMHRunBuilderAssert addProfiler(Class<? extends Profiler> profiler);

    JMHRunBuilderAssert verbosity(VerboseMode mode);

    JMHRunBuilderAssert shouldFailOnError(boolean value);

    JMHRunBuilderAssert threads(int count);

    JMHRunBuilderAssert threadGroups(int... groups);

    JMHRunBuilderAssert syncIterations(boolean value);

    JMHRunBuilderAssert warmupIterations(int value);

    JMHRunBuilderAssert warmupBatchSize(int value);

    JMHRunBuilderAssert warmupTime(TimeValue value);

    JMHRunBuilderAssert warmupMode(WarmupMode mode);

    JMHRunBuilderAssert includeWarmup(String regexp);

    JMHRunBuilderAssert measurementIterations(int count);

    JMHRunBuilderAssert measurementBatchSize(int value);

    JMHRunBuilderAssert measurementTime(TimeValue value);

    JMHRunBuilderAssert mode(Mode mode);

    JMHRunBuilderAssert timeUnit(TimeUnit tu);

    JMHRunBuilderAssert operationsPerInvocation(int value);

    JMHRunBuilderAssert forks(int value);

    JMHRunBuilderAssert warmupForks(int value);

    JMHRunBuilderAssert jvm(String path);

    JMHRunBuilderAssert jvmArgs(String... value);

    JMHRunBuilderAssert jvmArgsAppend(String... value);

    JMHRunBuilderAssert jvmArgsPrepend(String... value);

    JMHRunBuilderAssert detectJvmArgs();

    JMHRunBuilderAssert param(String name, String... values);

    JMHRunResultsAssert runsWithoutError();
}
