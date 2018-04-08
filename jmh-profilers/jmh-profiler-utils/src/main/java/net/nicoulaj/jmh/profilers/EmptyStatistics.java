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

import org.openjdk.jmh.util.AbstractStatistics;

import java.util.Iterator;
import java.util.Map;

/**
 * Dummy {@link org.openjdk.jmh.util.Statistics}.
 *
 * @author <a href="http://github.com/nicoulaj">nicoulaj</a>
 */
public class EmptyStatistics extends AbstractStatistics {

    @Override
    public double getMax() {
        return 0;
    }

    @Override
    public double getMin() {
        return 0;
    }

    @Override
    public long getN() {
        return 0;
    }

    @Override
    public double getSum() {
        return 0;
    }

    @Override
    public double getVariance() {
        return 0;
    }

    @Override
    public double getPercentile(final double rank) {
        return 0;
    }

    @Override
    public int[] getHistogram(double[] doubles) {
        return new int[0];
    }

    @Override
    public Iterator<Map.Entry<Double, Long>> getRawData() {
        return new Iterator<Map.Entry<Double, Long>>() {
            @Override
            public boolean hasNext() {
                return false;
            }

            @Override
            public Map.Entry<Double, Long> next() {
                throw new UnsupportedOperationException();
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
    }
}
