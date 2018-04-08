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

import org.openjdk.jmh.annotations.*;

import static java.util.concurrent.TimeUnit.*;
import static org.openjdk.jmh.annotations.Mode.Throughput;

/**
 * Test benchmark.
 *
 * @author <a href="http://github.com/nicoulaj">nicoulaj</a>
 */
public class TestBenchmark {

    @Benchmark
    @BenchmarkMode(Throughput)
    @Fork(2)
    @Warmup(iterations = 1, time = 1, timeUnit = SECONDS)
    @Measurement(iterations = 3, time = 10, timeUnit = MILLISECONDS)
    @OutputTimeUnit(NANOSECONDS)
    public int benchmark() {
        return 0;
    }
}
