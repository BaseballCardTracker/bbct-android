/*
 * This file is part of BBCT for Android.
 *
 * Copyright 2012 codeguru <codeguru@users.sourceforge.net>
 *
 * BBCT for Android is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * BBCT for Android is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package bbct.android.common.tests;

import android.test.InstrumentationTestRunner;
import bbct.android.common.BaseballCardDetailsPartialInputTest;
import junit.framework.TestSuite;

/**
 * This is a custom test runner which runs the parameterized tests created by
 * {@link bbct.android.common.BaseballCardDetailsPartialInputTest#suite()}.
 * These tests are not run by the standard
 * <code>InstrumentationTestRunner</code>.
 *
 * @author codeguru <codeguru@users.sourceforge.net>
 */
public class BBCTTestRunner extends InstrumentationTestRunner {

    /**
     * Create a {@link TestSuite} of parameterized tests. Currently, this is
     * only the tests from
     * {@link bbct.android.common.BaseballCardDetailsPartialInputTest#suite()}.
     *
     * @return A {@link TestSuite} of parameterized tests.
     */
    @Override
    public TestSuite getAllTests() {
        TestSuite suite = new TestSuite();
        suite.addTest(BaseballCardDetailsPartialInputTest.suite());

        return suite;
    }
    private static final String TAG = BBCTTestRunner.class.getName();
}
