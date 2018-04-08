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

import org.testng.annotations.Test;

import static net.nicoulaj.jmh.assertions.JMHAssertions.assertJMH;

/**
 * Test for {@link JMHAssertions}.
 *
 * @author <a href="http://github.com/nicoulaj">nicoulaj</a>
 */
public class JMHAssertionsTest {

    @Test
    public void test001() {
        assertJMH().runsWithoutError();
    }

    @Test
    public void test002() {
        assertJMH().hasResults();
    }

    @Test
    public void test003() {
        assertJMH().hasScoreOver(0.1);
    }
}
