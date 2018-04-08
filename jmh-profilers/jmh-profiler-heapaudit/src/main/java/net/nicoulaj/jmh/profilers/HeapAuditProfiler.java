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
import org.openjdk.jmh.results.Result;

import java.io.File;
import java.util.Collection;
import java.util.List;

/**
 * <a href="https://github.com/foursquare/heapaudit">HeapAudit</a> support for <a href="http://openjdk.java.net/projects/code-tools/jmh">JMH</a>.
 *
 * @author <a href="http://github.com/nicoulaj">nicoulaj</a>
 */
public class HeapAuditProfiler implements ExternalProfiler {

    @Override
    public String label() {
        return "heapaudit";
    }

    @Override
    public String getDescription() {
        return "Heap Audit";
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
    public boolean checkSupport(List<String> msgs) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Collection<String> addJVMInvokeOptions(final BenchmarkParams params) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Collection<String> addJVMOptions(final BenchmarkParams params) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public void beforeTrial(final BenchmarkParams benchmarkParams) {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public Collection<? extends Result> afterTrial(final BenchmarkParams benchmarkParams, final File stdOut, final File stdErr) {
        throw new UnsupportedOperationException("Not implemented");
    }
}
