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

import java.util.ArrayList;
import java.util.Collection;

/**
 * Default implementation of {@link net.nicoulaj.jmh.assertions.JMHRunResultsAssert}.
 *
 * @author <a href="http://github.com/nicoulaj">nicoulaj</a>
 * @see net.nicoulaj.jmh.assertions.JMHAssertions
 */
final class RunResultsAssert implements JMHRunResultsAssert {

    private final Collection<RunResult> runResults;

    private final Collection<RunResultAssert> runResultAsserts;

    RunResultsAssert(Collection<RunResult> runResults) {
        this.runResults = runResults;
        this.runResultAsserts = new ArrayList<>();
        for (RunResult runResult : runResults)
            runResultAsserts.add(new RunResultAssert(runResult));
    }

    @Override
    public JMHRunResultAssert hasSingleResult() {
        if (runResults.size() != 1)
            throw new AssertionError("Expected single result, got " + runResults.size());
        return runResultAsserts.iterator().next();
    }

    @Override
    public JMHRunResultsAssert hasResults() {
        if (runResults.size() <= 0)
            throw new AssertionError("No benchmark record");
        return this;
    }

    @Override
    public JMHRunResultsAssert hasScoreOver(double value) {
        return hasScoreOver(value, 0);
    }

    @Override
    public JMHRunResultsAssert hasScoreOver(final double value, final double tolerance) {
        hasResults();
        for (RunResultAssert runResultAssert : runResultAsserts)
            runResultAssert.hasScoreOver(value, tolerance);
        return this;
    }
}
