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
import java.util.List;

import static java.lang.Boolean.TRUE;
import static java.lang.Integer.getInteger;
import static java.lang.System.getProperty;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static net.nicoulaj.jmh.profilers.StringUtils.join;
import static net.nicoulaj.jmh.profilers.SystemUtils.getBoolean;
import static org.openjdk.jmh.results.AggregationPolicy.SUM;
import static org.openjdk.jmh.results.ResultRole.SECONDARY;

/**
 * <a href="http://www.oracle.com/technetwork/java/javaseproducts/mission-control/java-mission-control-1998576.html">Java Flight Recorder</a> support for <a href="http://openjdk.java.net/projects/code-tools/jmh">JMH</a>.
 *
 * @author <a href="http://github.com/nicoulaj">nicoulaj</a>
 */
public final class FlightRecorderProfiler implements ExternalProfiler {

    /**
     * Specifies whether the recording is a continuous background recording or it runs for a limited time.
     * By default, this parameter is set to false (recording runs for a limited time).
     * To make the recording run continuously, set the parameter to true.
     */
    private static final Boolean DEFAULT_RECORDING = getBoolean("jmh.jfr.defaultrecording", TRUE);

    /**
     * Specifies whether JFR should write a continuous recording to disk.
     * By default, this parameter is set to false (continuous recording to disk is disabled).
     * To enable it, set the parameter to true, and also set defaultrecording=true.
     */
    private static final Boolean DISK = getBoolean("jmh.jfr.disk", null);

    /**
     * Specifies whether a dump file of JFR data should be generated when the JVM terminates in a controlled manner.
     * By default, this parameter is set to false (dump file on exit is not generated).
     * To enable it, set the parameter to true, and also set defaultrecording=true.
     */
    private static final Boolean DUMP_ON_EXIT = getBoolean("jmh.jfr.dumponexit", TRUE);

    /**
     * Specifies the path and name of the dump file with JFR data that is created when the JVM exits in a controlled manner if you set the dumponexit=true parameter.
     * Setting the path makes sense only if you also set defaultrecording=true.
     */
    private static final String DUMP_ON_EXIT_PATH = getProperty("jmh.jfr.dumponexitpath", ".");

    /**
     * Specifies the total amount of primary memory (in bytes) used for data retention.
     * Append k or K, to specify the size in KB, m or M to specify the size in MB, g or G to specify the size in GB.
     * By default, the size is set to 462848 bytes.
     */
    private static final String GLOBAL_BUFFER_SIZE = getProperty("jmh.jfr.globalbuffersize", null);

    /**
     * Specify the amount of data written to the log file by JFR.
     * Allowed values : quiet, error, warning, info, debug, trace
     * By default, it is set to info.
     */
    private static final String LOG_LEVEL = getProperty("jmh.jfr.loglevel", null);

    /**
     * Specifies the maximum age (in minutes) of disk data for default recording.
     * By default, the maximum age is set to 15 minutes.
     * This parameter is valid only if you set the disk=true parameter.
     */
    private static final Integer MAX_AGE = getInteger("jmh.jfr.maxage", null);

    /**
     * Specifies the maximum size (in bytes) of the data chunks in a recording.
     * Append k or K, to specify the size in KB, m or M to specify the size in MB, g or G to specify the size in GB.
     * By default, the maximum size of data chunks is set to 12 MB.
     */
    private static final String MAX_CHUNK_SIZE = getProperty("jmh.jfr.maxchunksize", null);

    /**
     * Specifies the maximum size (in bytes) of disk data for default recording.
     * Append k or K, to specify the size in KB, m or M to specify the size in MB, g or G to specify the size in GB.
     * By default, the maximum size of disk data is not limited.
     */
    private static final String MAX_SIZE = getProperty("jmh.jfr.maxsize", null);

    /**
     * Specifies the repository (a directory) for temporary disk storage.
     * By default, the system's temporary directory is used.
     */
    private static final String REPOSITORY = getProperty("jmh.jfr.repository", null);

    /**
     * Specifies whether thread sampling is enabled.
     * Thread sampling occurs only if the sampling event is enabled along with this parameter.
     * By default, this parameter is enabled.
     */
    private static final Boolean SAMPLE_THREADS = getBoolean("jmh.jfr.samplethreads", null);

    /**
     * Specifies the path and name of the event settings file (of type JFC).
     * By default, the default.jfc file is used, which is located in JAVA_HOME/jre/lib/jfr. *
     */
    private static final String SETTINGS = getProperty("jmh.jfr.settings", null);

    /**
     * Stack depth for stack traces by JFR.
     * By default, the depth is set to 64 method calls.
     * The maximum is 2048, minimum is 1.
     */
    private static final String STACK_DEPTH = getProperty("jmh.jfr.stackdepth", null);

    /**
     * Specifies the per-thread local buffer size (in bytes).
     * Append k or K, to specify the size in KB, m or M to specify the size in MB, g or G to specify the size in GB.
     * Higher values for this parameter allow more data gathering without contention to flush it to the global storage.
     * It can increase application footprint in a thread-rich environment.
     * By default, the local buffer size is set to 5 KB.
     */
    private static final String THREAD_BUFFER_SIZE = getProperty("jmh.jfr.threadbuffersize", null);

    @Override
    public String getDescription() {
        return "Java Flight Recorder";
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
        return emptyList();
    }

    @Override
    public Collection<String> addJVMOptions(final BenchmarkParams params) {

        final List<String> opts = new ArrayList<>();
        if (DEFAULT_RECORDING != null) opts.add("defaultrecording=" + String.valueOf(DEFAULT_RECORDING));
        if (DISK != null) opts.add("disk=" + String.valueOf(DISK));
        if (DUMP_ON_EXIT != null) opts.add("dumponexit=" + String.valueOf(DUMP_ON_EXIT));
        if (DUMP_ON_EXIT_PATH != null) opts.add("dumponexitpath=" + DUMP_ON_EXIT_PATH);
        if (GLOBAL_BUFFER_SIZE != null) opts.add("globalbuffersize=" + GLOBAL_BUFFER_SIZE);
        if (LOG_LEVEL != null) opts.add("loglevel=" + LOG_LEVEL);
        if (MAX_AGE != null) opts.add("maxage=" + MAX_AGE);
        if (MAX_CHUNK_SIZE != null) opts.add("maxchunksize=" + MAX_CHUNK_SIZE);
        if (MAX_SIZE != null) opts.add("maxsize=" + MAX_SIZE);
        if (REPOSITORY != null) opts.add("repository=" + REPOSITORY);
        if (SAMPLE_THREADS != null) opts.add("samplethreads=" + String.valueOf(SAMPLE_THREADS));
        if (SETTINGS != null) opts.add("settings=" + SETTINGS);
        if (STACK_DEPTH != null) opts.add("stackdepth=" + STACK_DEPTH);
        if (THREAD_BUFFER_SIZE != null) opts.add("threadbuffersize=" + THREAD_BUFFER_SIZE);

        return asList("-XX:+UnlockCommercialFeatures",
                      "-XX:+FlightRecorder",
                      "-XX:FlightRecorderOptions=" + join(",", opts));
    }

    @Override
    public void beforeTrial(final BenchmarkParams benchmarkParams) {
        // Nothing to do
    }

    @Override
    public Collection<? extends Result> afterTrial(BenchmarkResult benchmarkResult, long l, final File stdOut, final File stdErr) {
        return asList(new JFRResult());
    }

    private static final class JFRResult extends Result<JFRResult> implements Aggregator<JFRResult> {

        JFRResult() {
            super(SECONDARY, "@jfr", new EmptyStatistics(), "none", SUM);
        }

        @Override
        protected Aggregator<JFRResult> getThreadAggregator() {
            return this;
        }

        @Override
        protected Aggregator<JFRResult> getIterationAggregator() {
            return this;
        }

        @Override
        protected String simpleExtendedInfo() {
            return "Java Flight Recorder recording at " + Paths.get(DUMP_ON_EXIT_PATH).toAbsolutePath();
        }

        @Override
        public JFRResult aggregate(final Collection<JFRResult> results) {
            return new JFRResult();
        }
    }
}
