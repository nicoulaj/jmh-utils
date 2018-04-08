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

import org.openjdk.jmh.infra.BenchmarkParams;
import org.openjdk.jmh.profile.ExternalProfiler;
import org.openjdk.jmh.results.Aggregator;
import org.openjdk.jmh.results.BenchmarkResult;
import org.openjdk.jmh.results.Result;

import java.io.File;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static java.lang.Integer.getInteger;
import static java.lang.System.getProperty;
import static java.util.Arrays.asList;
import static org.openjdk.jmh.results.AggregationPolicy.SUM;
import static org.openjdk.jmh.results.ResultRole.SECONDARY;

/**
 * <a href="http://www.oracle.com/technetwork/server-storage/solarisstudio/overview/index.html">Solaris Studio</a> support for <a href="http://openjdk.java.net/projects/code-tools/jmh">JMH</a>.
 *
 * @author <a href="http://github.com/nicoulaj">nicoulaj</a>
 */
public class SolarisStudioProfiler implements ExternalProfiler {

    /**
     * Specify clock profiling interval.
     *
     * @see <a href="http://docs.oracle.com/cd/E19205-01/820-4180/man1/collect.1.html"><code>man collect</code></a> or <code>collect -h</code>.
     */
    private static final String CLOCK_PROFILING_INTERVAL = getProperty("jmh.solaris-studio.clock-profiling-interval", null);

    /**
     * Specify HW counter profiling.
     *
     * @see <a href="http://docs.oracle.com/cd/E19205-01/820-4180/man1/collect.1.html"><code>man collect</code></a> or <code>collect -h</code>.
     */
    private static final String HW_COUNTER_PROFILING = getProperty("jmh.solaris-studio.hw-counter-profiling", null);

    /**
     * Specify synchronization wait tracing.
     *
     * @see <a href="http://docs.oracle.com/cd/E19205-01/820-4180/man1/collect.1.html"><code>man collect</code></a> or <code>collect -h</code>.
     */
    private static final String SYNCHRONIZATION_WAIT_TRACING = getProperty("jmh.solaris-studio.synchronization-wait-tracing", null);

    /**
     * Specify thread analyzer experiment.
     *
     * @see <a href="http://docs.oracle.com/cd/E19205-01/820-4180/man1/collect.1.html"><code>man collect</code></a> or <code>collect -h</code>.
     */
    private static final String THREAD_ANALYZER = getProperty("jmh.solaris-studio.thread-analyzer", null);

    /**
     * Specify heap tracing.
     *
     * @see <a href="http://docs.oracle.com/cd/E19205-01/820-4180/man1/collect.1.html"><code>man collect</code></a> or <code>collect -h</code>.
     */
    private static final String HEAP_TRACING = getProperty("jmh.solaris-studio.heap-tracing", null);

    /**
     * Specify I/O tracing.
     *
     * @see <a href="http://docs.oracle.com/cd/E19205-01/820-4180/man1/collect.1.html"><code>man collect</code></a> or <code>collect -h</code>.
     */
    private static final String IO_TRACING = getProperty("jmh.solaris-studio.io-tracing", null);

    /**
     * Specify time over which to record data.
     *
     * @see <a href="http://docs.oracle.com/cd/E19205-01/820-4180/man1/collect.1.html"><code>man collect</code></a> or <code>collect -h</code>.
     */
    private static final String DURATION = getProperty("jmh.solaris-studio.duration", null);

    /**
     * Specify following descendant processes.
     *
     * @see <a href="http://docs.oracle.com/cd/E19205-01/820-4180/man1/collect.1.html"><code>man collect</code></a> or <code>collect -h</code>.
     */
    private static final String FOLLOW_DESCENDANT = getProperty("jmh.solaris-studio.follow-descendant", null);

    /**
     * Specify archiving of load-objects.
     *
     * @see <a href="http://docs.oracle.com/cd/E19205-01/820-4180/man1/collect.1.html"><code>man collect</code></a> or <code>collect -h</code>.
     */
    private static final String ARCHIVING = getProperty("jmh.solaris-studio.archiving", null);

    /**
     * Specify periodic sampling interval (s).
     *
     * @see <a href="http://docs.oracle.com/cd/E19205-01/820-4180/man1/collect.1.html"><code>man collect</code></a> or <code>collect -h</code>.
     */
    private static final Integer SAMPLING_INTERVAL = getInteger("jmh.solaris-studio.sampling-interval", null);

    /**
     * Specify experiment size limit (MB).
     *
     * @see <a href="http://docs.oracle.com/cd/E19205-01/820-4180/man1/collect.1.html"><code>man collect</code></a> or <code>collect -h</code>.
     */
    private static final Integer SIZE_LIMIT = getInteger("jmh.solaris-studio.size-limit", null);

    /**
     * Specify signal for samples.
     *
     * @see <a href="http://docs.oracle.com/cd/E19205-01/820-4180/man1/collect.1.html"><code>man collect</code></a> or <code>collect -h</code>.
     */
    private static final String SIGNAL = getProperty("jmh.solaris-studio.signal", null);

    /**
     * Specify experiment name.
     *
     * @see <a href="http://docs.oracle.com/cd/E19205-01/820-4180/man1/collect.1.html"><code>man collect</code></a> or <code>collect -h</code>.
     */
    private static final String NAME = getProperty("jmh.solaris-studio.name", null);

    /**
     * Specify experiment directory.
     *
     * @see <a href="http://docs.oracle.com/cd/E19205-01/820-4180/man1/collect.1.html"><code>man collect</code></a> or <code>collect -h</code>.
     */
    private static final String DIRECTORY = getProperty("jmh.solaris-studio.directory", ".");

    /**
     * Specify experiment group.
     *
     * @see <a href="http://docs.oracle.com/cd/E19205-01/820-4180/man1/collect.1.html"><code>man collect</code></a> or <code>collect -h</code>.
     */
    private static final String GROUP = getProperty("jmh.solaris-studio.group", null);

    /**
     * Redirect all of <code>collect</code>'s output to file.
     *
     * @see <a href="http://docs.oracle.com/cd/E19205-01/820-4180/man1/collect.1.html"><code>man collect</code></a> or <code>collect -h</code>.
     */
    private static final String OUTPUT = getProperty("jmh.solaris-studio.output", null);

    /**
     * Print expanded log of processing.
     *
     * @see <a href="http://docs.oracle.com/cd/E19205-01/820-4180/man1/collect.1.html"><code>man collect</code></a> or <code>collect -h</code>.
     */
    private static final Boolean VERBOSE = SystemUtils.getBoolean("jmh.solaris-studio.verbose", null);

    /**
     * Specify comment label.
     *
     * @see <a href="http://docs.oracle.com/cd/E19205-01/820-4180/man1/collect.1.html"><code>man collect</code></a> or <code>collect -h</code>.
     */
    private static final String LABEL = getProperty("jmh.solaris-studio.label", null);

    @Override
    public String getDescription() {
        return "Solaris Studio";
    }

    @Override
    public boolean allowPrintOut() {
        return true;
    }

    @Override
    public boolean allowPrintErr() {
        return true;
    }

    @Override
    public Collection<String> addJVMInvokeOptions(final BenchmarkParams params) {

        final List<String> opts = new ArrayList<>();

        opts.add("collect");

        if (CLOCK_PROFILING_INTERVAL != null) {
            opts.add("-p");
            opts.add(String.valueOf(CLOCK_PROFILING_INTERVAL));
        }
        if (HW_COUNTER_PROFILING != null) {
            opts.add("-h");
            opts.add(String.valueOf(HW_COUNTER_PROFILING));
        }
        if (SYNCHRONIZATION_WAIT_TRACING != null) {
            opts.add("-s");
            opts.add(String.valueOf(SYNCHRONIZATION_WAIT_TRACING));
        }
        if (THREAD_ANALYZER != null) {
            opts.add("-r");
            opts.add(String.valueOf(THREAD_ANALYZER));
        }
        if (HEAP_TRACING != null) {
            opts.add("-H");
            opts.add(String.valueOf(HEAP_TRACING));
        }
        if (IO_TRACING != null) {
            opts.add("-i");
            opts.add(String.valueOf(IO_TRACING));
        }
        if (DURATION != null) {
            opts.add("-t");
            opts.add(String.valueOf(DURATION));
        }
        if (FOLLOW_DESCENDANT != null) {
            opts.add("-F");
            opts.add(String.valueOf(FOLLOW_DESCENDANT));
        }
        if (ARCHIVING != null) {
            opts.add("-A");
            opts.add(String.valueOf(ARCHIVING));
        }
        if (SAMPLING_INTERVAL != null) {
            opts.add("-S");
            opts.add(String.valueOf(SAMPLING_INTERVAL));
        }
        if (SIZE_LIMIT != null) {
            opts.add("-L");
            opts.add(String.valueOf(SIZE_LIMIT));
        }
        if (SIGNAL != null) {
            opts.add("-l");
            opts.add(String.valueOf(SIGNAL));
        }
        if (NAME != null) {
            opts.add("-o");
            opts.add(String.valueOf(NAME));
        }
        if (DIRECTORY != null) {
            opts.add("-d");
            opts.add(String.valueOf(DIRECTORY));
        }
        if (GROUP != null) {
            opts.add("-g");
            opts.add(String.valueOf(GROUP));
        }
        if (OUTPUT != null) {
            opts.add("-O");
            opts.add(String.valueOf(OUTPUT));
        }
        if (VERBOSE != null && VERBOSE) { opts.add("-v");}
        if (LABEL != null) {
            opts.add("-C");
            opts.add(String.valueOf(LABEL));
        }

        return  opts;
    }

    @Override
    public Collection<String> addJVMOptions(final BenchmarkParams params) {
        return Collections.emptyList();
    }

    @Override
    public void beforeTrial(final BenchmarkParams benchmarkParams) {
        // Nothing to do
    }

    @Override
    public Collection<? extends Result> afterTrial(final BenchmarkResult benchmarkResult, long l, final File stdOut, final File stdErr) {
        return asList(new SolarisStudioResult());
    }

    private static final class SolarisStudioResult extends Result<SolarisStudioResult> implements Aggregator<SolarisStudioResult> {

        SolarisStudioResult() {
            super(SECONDARY, "@solaris-studio", new EmptyStatistics(), "none", SUM);
        }

        @Override
        protected Aggregator<SolarisStudioResult> getThreadAggregator() {
            return this;
        }

        @Override
        protected Aggregator<SolarisStudioResult> getIterationAggregator() {
            return this;
        }

        @Override
        protected String simpleExtendedInfo() {
            return "Solaris Studio experiment at " + Paths.get(DIRECTORY).toAbsolutePath();
        }

        @Override
        public SolarisStudioResult aggregate(final Collection<SolarisStudioResult> results) {
            return new SolarisStudioResult();
        }
    }
}
