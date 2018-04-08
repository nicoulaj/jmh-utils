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
import org.openjdk.jmh.runner.options.*;

import java.util.concurrent.TimeUnit;

import static net.nicoulaj.jmh.assertions.JMHAssertions.assertJMH;

/**
 * Default implementation of {@link net.nicoulaj.jmh.assertions.JMHRunBuilderAssert}.
 *
 * @author <a href="http://github.com/nicoulaj">nicoulaj</a>
 * @see net.nicoulaj.jmh.assertions.JMHAssertions
 */
final class RunBuilderAssert implements JMHRunBuilderAssert {

    private final ChainedOptionsBuilder options;

    RunBuilderAssert() {
        options = new OptionsBuilder();
    }

    private JMHRunResultsAssert build() {
        return assertJMH(options.build());
    }

    @Override
    public RunBuilderAssert warmupTime(final TimeValue value) {
        options.warmupTime(value);
        return this;
    }

    @Override
    public RunBuilderAssert jvmArgsPrepend(final String... value) {
        options.jvmArgsPrepend(value);
        return this;
    }

    @Override
    public RunBuilderAssert output(final String filename) {
        options.output(filename);
        return this;
    }

    @Override
    public RunBuilderAssert jvm(final String path) {
        options.jvm(path);
        return this;
    }

    @Override
    public RunBuilderAssert syncIterations(final boolean value) {
        options.syncIterations(value);
        return this;
    }

    @Override
    public RunBuilderAssert measurementTime(final TimeValue value) {
        options.measurementTime(value);
        return this;
    }

    @Override
    public RunBuilderAssert include(final String regexp) {
        options.include(regexp);
        return this;
    }

    @Override
    public RunBuilderAssert warmupBatchSize(final int value) {
        options.warmupBatchSize(value);
        return this;
    }

    @Override
    public RunBuilderAssert param(final String name, final String... values) {
        options.param(name, values);
        return this;
    }

    @Override
    public RunBuilderAssert measurementIterations(final int count) {
        options.measurementIterations(count);
        return this;
    }

    @Override
    public RunBuilderAssert measurementBatchSize(final int value) {
        options.measurementBatchSize(value);
        return this;
    }

    @Override
    public RunBuilderAssert warmupMode(final WarmupMode mode) {
        options.warmupMode(mode);
        return this;
    }

    @Override
    public RunBuilderAssert verbosity(final VerboseMode mode) {
        options.verbosity(mode);
        return this;
    }

    @Override
    public RunBuilderAssert warmupIterations(final int value) {
        options.warmupIterations(value);
        return this;
    }

    @Override
    public RunBuilderAssert detectJvmArgs() {
        options.detectJvmArgs();
        return this;
    }

    @Override
    public RunBuilderAssert warmupForks(final int value) {
        options.warmupForks(value);
        return this;
    }

    @Override
    public RunBuilderAssert jvmArgsAppend(final String... value) {
        options.jvmArgsAppend(value);
        return this;
    }

    @Override
    public RunBuilderAssert result(final String filename) {
        options.result(filename);
        return this;
    }

    @Override
    public RunBuilderAssert exclude(final String regexp) {
        options.exclude(regexp);
        return this;
    }

    @Override
    public JMHRunBuilderAssert resultFormat(final ResultFormatType type) {
        options.resultFormat(type);
        return this;
    }

    @Override
    public RunBuilderAssert timeUnit(final TimeUnit tu) {
        options.timeUnit(tu);
        return this;
    }

    @Override
    public JMHRunBuilderAssert operationsPerInvocation(final int value) {
        options.operationsPerInvocation(value);
        return this;
    }

    @Override
    public RunBuilderAssert shouldDoGC(final boolean value) {
        options.shouldDoGC(value);
        return this;
    }

    @Override
    public JMHRunBuilderAssert addProfiler(final Class<? extends Profiler> profiler) {
        options.addProfiler(profiler);
        return this;
    }

    @Override
    public RunBuilderAssert forks(final int value) {
        options.forks(value);
        return this;
    }

    @Override
    public RunBuilderAssert threadGroups(final int... groups) {
        options.threadGroups(groups);
        return this;
    }

    @Override
    public RunBuilderAssert shouldFailOnError(final boolean value) {
        options.shouldFailOnError(value);
        return this;
    }

    @Override
    public RunBuilderAssert includeWarmup(final String regexp) {
        options.includeWarmup(regexp);
        return this;
    }

    @Override
    public RunBuilderAssert mode(final Mode mode) {
        options.mode(mode);
        return this;
    }

    @Override
    public RunBuilderAssert jvmArgs(final String... value) {
        options.jvmArgs(value);
        return this;
    }

    @Override
    public RunBuilderAssert parent(final Options other) {
        options.parent(other);
        return this;
    }

    @Override
    public RunBuilderAssert threads(final int count) {
        options.threads(count);
        return this;
    }

    @Override
    public JMHRunResultsAssert runsWithoutError() {
        return build();
    }

    @Override
    public JMHRunResultAssert hasSingleResult() {
        return runsWithoutError().hasSingleResult();
    }

    @Override
    public JMHRunResultsAssert hasResults() {
        return runsWithoutError().hasResults();
    }

    @Override
    public JMHRunResultsAssert hasScoreOver(final double value) {
        return runsWithoutError().hasScoreOver(value);
    }

    @Override
    public JMHRunResultsAssert hasScoreOver(final double value, final double tolerance) {
        return runsWithoutError().hasScoreOver(value, tolerance);
    }
}
