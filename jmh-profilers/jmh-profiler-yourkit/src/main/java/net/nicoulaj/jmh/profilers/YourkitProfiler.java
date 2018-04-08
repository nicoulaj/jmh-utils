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

import static java.lang.Integer.getInteger;
import static java.lang.System.getProperty;
import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static net.nicoulaj.jmh.profilers.StringUtils.join;
import static net.nicoulaj.jmh.profilers.SystemUtils.getBoolean;
import static net.nicoulaj.jmh.profilers.YourkitUtils.detectYourkitAgentLib;
import static net.nicoulaj.jmh.profilers.YourkitUtils.detectYourkitHome;
import static org.openjdk.jmh.results.AggregationPolicy.SUM;
import static org.openjdk.jmh.results.ResultRole.SECONDARY;

/**
 * <a href="http://www.yourkit.com">Yourkit</a> support for <a href="http://openjdk.java.net/projects/code-tools/jmh">JMH</a>.
 *
 * @author <a href="http://github.com/nicoulaj">nicoulaj</a>
 */
public final class YourkitProfiler implements ExternalProfiler {

    /**
     * Yourkit installation home directory.
     */
    private static final String YOURKIT_HOME = getProperty("jmh.yourkit.home", detectYourkitHome());

    /**
     * Yourkit agent library (path relative to {@link #YOURKIT_HOME}).
     */
    private static final String YOURKIT_AGENT_LIB = getProperty("jmh.yourkit.agentlib", detectYourkitAgentLib());

    /**
     * Specify the port that the profiler agent listens on for communication with the Profiler.
     *
     * @see <a href="http://www.yourkit.com/docs/java/help/startup_options.jsp">Yourkit startup options documentation</a>
     */
    private static final Integer PORT = getInteger("jmh.yourkit.port", null);

    /**
     * Same as {@link #PORT} but this binds agent socket to a particular IP only.
     * <p/>
     * Syntax: {@code <ip>:<port>}
     *
     * @see <a href="http://www.yourkit.com/docs/java/help/startup_options.jsp">Yourkit startup options documentation</a>
     */
    private static final String LISTEN = getProperty("jmh.yourkit.listen", null);

    /**
     * Allow only local connections to the profiled application.
     *
     * @see <a href="http://www.yourkit.com/docs/java/help/startup_options.jsp">Yourkit startup options documentation</a>
     */
    private static final Boolean ONLY_LOCAL = getBoolean("jmh.yourkit.only_local", null);

    /**
     * Postpone start of telemetry collection (milliseconds).
     *
     * @see <a href="http://www.yourkit.com/docs/java/help/startup_options.jsp">Yourkit startup options documentation</a>
     */
    private static final Integer DELAY = getInteger("jmh.yourkit.delay", null);

    /**
     * Telemetry retention (hours).
     *
     * @see <a href="http://www.yourkit.com/docs/java/help/startup_options.jsp">Yourkit startup options documentation</a>
     */
    private static final Integer TELEMETRY_LIMIT = getInteger("jmh.yourkit.telemetry_limit", null);

    /**
     * Telemetry period (milliseconds).
     *
     * @see <a href="http://www.yourkit.com/docs/java/help/startup_options.jsp">Yourkit startup options documentation</a>
     */
    private static final Integer TELEMETRY_PERIOD = getInteger("jmh.yourkit.telemetry_period", null);

    /**
     * Probes: limit the number of rows to be stored by the profiler agent per table.
     *
     * @see <a href="http://www.yourkit.com/docs/java/help/startup_options.jsp">Yourkit startup options documentation</a>
     */
    private static final Integer PROVE_TABLE_LENGTH_LIMIT = getInteger("jmh.yourkit.probe_table_length_limit", null);

    /**
     * Specify the number of recently finished threads for which CPU sampling, tracing and monitor profiling results are kept.
     *
     * @see <a href="http://www.yourkit.com/docs/java/help/startup_options.jsp">Yourkit startup options documentation</a>
     */
    private static final Integer DEAD_THREAD_LIMIT = getInteger("jmh.yourkit.dead_thread_limit", null);

    /**
     * Capture a snapshot on profiled application exit.
     * <p/>
     * Syntax: {@code snapshot|memory}
     *
     * @see <a href="http://www.yourkit.com/docs/java/help/startup_options.jsp">Yourkit startup options documentation</a>
     */
    private static final String ON_EXIT = getProperty("jmh.yourkit.on_exit", "snapshot");

    /**
     * Specify custom snapshot directory.
     *
     * @see <a href="http://www.yourkit.com/docs/java/help/startup_options.jsp">Yourkit startup options documentation</a>
     */
    private static final String DIR = getProperty("jmh.yourkit.dir", ".");

    /**
     * Specify custom logs directory.
     *
     * @see <a href="http://www.yourkit.com/docs/java/help/startup_options.jsp">Yourkit startup options documentation</a>
     */
    private static final String LOG_DIR = getProperty("jmh.yourkit.logdir", DIR);

    /**
     * Specify a custom location of the CPU sampling settings configuration file.
     *
     * @see <a href="http://www.yourkit.com/docs/java/help/startup_options.jsp">Yourkit startup options documentation</a>
     * @see <a href="http://www.yourkit.com/docs/java/help/sampling_settings.jsp">Sampling settings documentation</a>
     */
    private static final String SAMPLING_SETTINGS_PATH = getProperty("jmh.yourkit.sampling_settings_path", null);

    /**
     * Specify a custom location of the CPU tracing settings configuration file.
     *
     * @see <a href="http://www.yourkit.com/docs/java/help/startup_options.jsp">Yourkit startup options documentation</a>
     * @see <a href="http://www.yourkit.com/docs/java/help/tracing_settings.jsp">Tracing settings documentation</a>
     */
    private static final String TRACING_SETTINGS_PATH = getProperty("jmh.yourkit.tracing_settings_path", null);

    /**
     * Application will launch with CPU sampling turned on.
     *
     * @see <a href="http://www.yourkit.com/docs/java/help/startup_options.jsp">Yourkit startup options documentation</a>
     */
    private static final Boolean SAMPLING = getBoolean("jmh.yourkit.sampling", null);

    /**
     * Application will launch with CPU tracing turned on.
     *
     * @see <a href="http://www.yourkit.com/docs/java/help/startup_options.jsp">Yourkit startup options documentation</a>
     */
    private static final Boolean TRACING = getBoolean("jmh.yourkit.tracing", null);

    /**
     * Application will launch with object allocation recording started and record each N-th allocation.
     *
     * @see <a href="http://www.yourkit.com/docs/java/help/startup_options.jsp">Yourkit startup options documentation</a>
     */
    private static final Integer ALLOC_EACH = getInteger("jmh.yourkit.alloceach", null);

    /**
     * Application will launch with object allocation recording started and record allocation of objects with size bigger or equal B bytes.
     *
     * @see <a href="http://www.yourkit.com/docs/java/help/startup_options.jsp">Yourkit startup options documentation</a>
     */
    private static final Long ALLOC_SIZE_LIMIT = Long.getLong("jmh.yourkit.allocsizelimit", null);

    /**
     * Do not perform per-class garbage object allocation recording.
     *
     * @see <a href="http://www.yourkit.com/docs/java/help/startup_options.jsp">Yourkit startup options documentation</a>
     */
    private static final Boolean NO_PER_CLASS_GC = getBoolean("jmh.yourkit.noperclassgc", null);

    /**
     * Use sampled object allocation recording.
     *
     * @see <a href="http://www.yourkit.com/docs/java/help/startup_options.jsp">Yourkit startup options documentation</a>
     */
    private static final Boolean ALLOC_SAMPLED = getBoolean("jmh.yourkit.allocsampled", null);

    /**
     * Launch Java application with started monitor profiling.
     *
     * @see <a href="http://www.yourkit.com/docs/java/help/startup_options.jsp">Yourkit startup options documentation</a>
     */
    private static final Boolean MONITORS = getBoolean("jmh.yourkit.monitors", null);

    /**
     * Automatically capture a memory snapshot when used heap memory reaches the threshold.
     *
     * @see <a href="http://www.yourkit.com/docs/java/help/startup_options.jsp">Yourkit startup options documentation</a>
     */
    private static final Integer USED_MEM = getInteger("jmh.yourkit.usedmem", null);

    /**
     * Automatically capture a HPROF snapshot when used heap memory reaches the threshold.
     *
     * @see <a href="http://www.yourkit.com/docs/java/help/startup_options.jsp">Yourkit startup options documentation</a>
     */
    private static final Integer USED_MEM_HPROF = getInteger("jmh.yourkit.usedmemhprof", null);

    /**
     * Periodically capture performance snapshots.
     *
     * @see <a href="http://www.yourkit.com/docs/java/help/startup_options.jsp">Yourkit startup options documentation</a>
     */
    private static final Integer PERIODIC_PERF = getInteger("jmh.yourkit.periodicperf", null);

    /**
     * Periodically capture memory snapshots in the profiler's format (*.snapshot).
     *
     * @see <a href="http://www.yourkit.com/docs/java/help/startup_options.jsp">Yourkit startup options documentation</a>
     */
    private static final Integer PERIODIC_MEM = getInteger("jmh.yourkit.periodicmem", null);

    /**
     * Periodically capture HPROF snapshots.
     *
     * @see <a href="http://www.yourkit.com/docs/java/help/startup_options.jsp">Yourkit startup options documentation</a>
     */
    private static final Integer PERIODIC_HPROF = getInteger("jmh.yourkit.periodichprof", null);

    /**
     * Do not collect thread stack and status information shown in Thread view as well as in other telemetry views.
     *
     * @see <a href="http://www.yourkit.com/docs/java/help/startup_options.jsp">Yourkit startup options documentation</a>
     */
    private static final Boolean DISABLE_STACK_TELEMETRY = getBoolean("jmh.yourkit.disablestacktelemetry", null);

    /**
     * Do not collect exception telemetry.
     *
     * @see <a href="http://www.yourkit.com/docs/java/help/startup_options.jsp">Yourkit startup options documentation</a>
     */
    private static final Boolean DISABLE_EXCEPTION_TELEMETRY = getBoolean("jmh.yourkit.disableexceptiontelemetry", null);

    /**
     * Disable on OutOfMemoryError snapshots.
     *
     * @see <a href="http://www.yourkit.com/docs/java/help/startup_options.jsp">Yourkit startup options documentation</a>
     */
    private static final Boolean DISABLE_OOM_DUMPER = getBoolean("jmh.yourkit.disableoomedumper", null);

    /**
     * Specify which probes should be registered on startup.
     *
     * @see <a href="http://www.yourkit.com/docs/java/help/startup_options.jsp">Yourkit startup options documentation</a>
     * @see <a href="http://www.yourkit.com/docs/java/help/register_probes.jsp">Probe registration documentation</a>
     */
    private static final String PROBE_ON = getProperty("jmh.yourkit.probe_on", null);

    /**
     * Specify which probes should be registered on startup.
     *
     * @see <a href="http://www.yourkit.com/docs/java/help/startup_options.jsp">Yourkit startup options documentation</a>
     * @see <a href="http://www.yourkit.com/docs/java/help/register_probes.jsp">Probe registration documentation</a>
     */
    private static final String PROBE_OFF = getProperty("jmh.yourkit.probe_off", null);

    /**
     * Specify which probes should be registered on startup.
     *
     * @see <a href="http://www.yourkit.com/docs/java/help/startup_options.jsp">Yourkit startup options documentation</a>
     * @see <a href="http://www.yourkit.com/docs/java/help/register_probes.jsp">Probe registration documentation</a>
     */
    private static final String PROBE_AUTO = getProperty("jmh.yourkit.probe_auto", null);

    /**
     * Specify which probes should be registered on startup.
     *
     * @see <a href="http://www.yourkit.com/docs/java/help/startup_options.jsp">Yourkit startup options documentation</a>
     * @see <a href="http://www.yourkit.com/docs/java/help/register_probes.jsp">Probe registration documentation</a>
     */
    private static final String PROBE_DISABLE = getProperty("jmh.yourkit.probe_disable", null);

    /**
     * Specify where to find probe class(es) which are registered by class name.
     *
     * @see <a href="http://www.yourkit.com/docs/java/help/startup_options.jsp">Yourkit startup options documentation</a>
     * @see <a href="http://www.yourkit.com/docs/java/help/register_probes.jsp">Probe registration documentation</a>
     */
    private static final String PROBE_CLASSPATH = getProperty("jmh.yourkit.probeclasspath", null);

    /**
     * Specify where to find probe class(es) which are registered by class name.
     *
     * @see <a href="http://www.yourkit.com/docs/java/help/startup_options.jsp">Yourkit startup options documentation</a>
     * @see <a href="http://www.yourkit.com/docs/java/help/register_probes.jsp">Probe registration documentation</a>
     */
    private static final String PROBE_BOOT_CLASSPATH = getProperty("jmh.yourkit.probebootclasspath", null);

    /**
     * Specify the file with description of the triggers to be applied from startup.
     *
     * @see <a href="http://www.yourkit.com/docs/java/help/startup_options.jsp">Yourkit startup options documentation</a>
     * @see <a href="http://www.yourkit.com/docs/java/help/triggers.jsp">Triggers documentation</a>
     */
    private static final String TRIGGERS = getProperty("jmh.yourkit.triggers", null);

    /**
     * Do not instrument bytecode with instructions needed for object allocation recording.
     *
     * @see <a href="http://www.yourkit.com/docs/java/help/startup_options.jsp">Yourkit startup options documentation</a>
     */
    private static final Boolean DISABLE_ALLOC = getBoolean("jmh.yourkit.disablealloc", null);

    /**
     * Do not instrument bytecode with instructions needed for CPU tracing. Only CPU sampling will be available.
     *
     * @see <a href="http://www.yourkit.com/docs/java/help/startup_options.jsp">Yourkit startup options documentation</a>
     */
    private static final Boolean DISABLE_TRACING = getBoolean("jmh.yourkit.disabletracing", null);

    /**
     * Disable several capabilities at once: disablealloc, disabletracing, disableexceptiontelemetry, disablestacktelemetry, probe_disable=*.
     *
     * @see <a href="http://www.yourkit.com/docs/java/help/startup_options.jsp">Yourkit startup options documentation</a>
     */
    private static final Boolean DISABLE_ALL = getBoolean("jmh.yourkit.disableall", null);

    @Override
    public String getDescription() {
        return "Yourkit";
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
        if (PORT != null) opts.add("port=" + String.valueOf(PORT));
        if (LISTEN != null) opts.add("listen=" + String.valueOf(LISTEN));
        if (ONLY_LOCAL != null) opts.add("only_local=" + String.valueOf(ONLY_LOCAL));
        if (DELAY != null) opts.add("delay=" + String.valueOf(DELAY));
        if (TELEMETRY_LIMIT != null) opts.add("telemetry_limit=" + String.valueOf(TELEMETRY_LIMIT));
        if (TELEMETRY_PERIOD != null) opts.add("telemetry_period=" + String.valueOf(TELEMETRY_PERIOD));
        if (PROVE_TABLE_LENGTH_LIMIT != null) opts.add("probe_table_length_limit=" + String.valueOf(PROVE_TABLE_LENGTH_LIMIT));
        if (DEAD_THREAD_LIMIT != null) opts.add("dead_thread_limit=" + String.valueOf(DEAD_THREAD_LIMIT));
        if (ON_EXIT != null) opts.add("onexit=" + String.valueOf(ON_EXIT));
        if (DIR != null) opts.add("dir=" + String.valueOf(DIR));
        if (LOG_DIR != null) opts.add("logdir=" + String.valueOf(LOG_DIR));
        if (SAMPLING_SETTINGS_PATH != null) opts.add("sampling_settings_path=" + String.valueOf(SAMPLING_SETTINGS_PATH));
        if (TRACING_SETTINGS_PATH != null) opts.add("tracing_settings_path=" + String.valueOf(TRACING_SETTINGS_PATH));
        if (SAMPLING != null) opts.add("sampling=" + String.valueOf(SAMPLING));
        if (TRACING != null) opts.add("tracing=" + String.valueOf(TRACING));
        if (ALLOC_EACH != null) opts.add("alloceach=" + String.valueOf(ALLOC_EACH));
        if (ALLOC_SIZE_LIMIT != null) opts.add("allocsizelimit=" + String.valueOf(ALLOC_SIZE_LIMIT));
        if (NO_PER_CLASS_GC != null) opts.add("noperclassgc=" + String.valueOf(NO_PER_CLASS_GC));
        if (ALLOC_SAMPLED != null) opts.add("allocsampled=" + String.valueOf(ALLOC_SAMPLED));
        if (MONITORS != null) opts.add("monitors=" + String.valueOf(MONITORS));
        if (USED_MEM != null) opts.add("usedmem=" + String.valueOf(USED_MEM));
        if (USED_MEM_HPROF != null) opts.add("usedmemhprof=" + String.valueOf(USED_MEM_HPROF));
        if (PERIODIC_PERF != null) opts.add("periodicperf=" + String.valueOf(PERIODIC_PERF));
        if (PERIODIC_MEM != null) opts.add("periodicmem=" + String.valueOf(PERIODIC_MEM));
        if (PERIODIC_HPROF != null) opts.add("periodichprof=" + String.valueOf(PERIODIC_HPROF));
        if (DISABLE_STACK_TELEMETRY != null) opts.add("disablestacktelemetry=" + String.valueOf(DISABLE_STACK_TELEMETRY));
        if (DISABLE_EXCEPTION_TELEMETRY != null) opts.add("disableexceptiontelemetry=" + String.valueOf(DISABLE_EXCEPTION_TELEMETRY));
        if (DISABLE_OOM_DUMPER != null) opts.add("disableoomedumper=" + String.valueOf(DISABLE_OOM_DUMPER));
        if (PROBE_ON != null) opts.add("probe_on=" + String.valueOf(PROBE_ON));
        if (PROBE_OFF != null) opts.add("probe_off=" + String.valueOf(PROBE_OFF));
        if (PROBE_AUTO != null) opts.add("probe_auto=" + String.valueOf(PROBE_AUTO));
        if (PROBE_DISABLE != null) opts.add("probe_disable=" + String.valueOf(PROBE_DISABLE));
        if (PROBE_CLASSPATH != null) opts.add("probeclasspath=" + String.valueOf(PROBE_CLASSPATH));
        if (PROBE_BOOT_CLASSPATH != null) opts.add("probebootclasspath=" + String.valueOf(PROBE_BOOT_CLASSPATH));
        if (TRIGGERS != null) opts.add("triggers=" + String.valueOf(TRIGGERS));
        if (DISABLE_ALLOC != null) opts.add("disablealloc=" + String.valueOf(DISABLE_ALLOC));
        if (DISABLE_TRACING != null) opts.add("disabletracing=" + String.valueOf(DISABLE_TRACING));
        if (DISABLE_ALL != null) opts.add("disableall=" + String.valueOf(DISABLE_ALL));

        return asList("-agentpath:" + YOURKIT_HOME + File.separator + YOURKIT_AGENT_LIB + "=" + join(",", opts));
    }

    @Override
    public void beforeTrial(final BenchmarkParams benchmarkParams) {
        // Nothing to do
    }

    @Override
    public Collection<? extends Result> afterTrial(final BenchmarkResult benchmarkParams, long l, final File stdOut, final File stdErr) {
        return asList(new YourkitResult());
    }

    private static final class YourkitResult extends Result<YourkitResult> implements Aggregator<YourkitResult> {

        YourkitResult() {
            super(SECONDARY, "@yourkit", new EmptyStatistics(), "none", SUM);
        }

        @Override
        protected Aggregator<YourkitResult> getThreadAggregator() {
            return this;
        }

        @Override
        protected Aggregator<YourkitResult> getIterationAggregator() {
            return this;
        }

        @Override
        public YourkitResult aggregate(Collection<YourkitResult> collection) {
            return new YourkitResult();
        }

        @Override
        protected String simpleExtendedInfo() {
            return "Yourkit snapshot at " + Paths.get(DIR).toAbsolutePath();
        }
    }
}
