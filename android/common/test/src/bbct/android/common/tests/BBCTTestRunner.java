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
 *
 * @author codeguru <codeguru@users.sourceforge.net>
 */
public class BBCTTestRunner extends InstrumentationTestRunner {

    @Override
    public TestSuite getAllTests() {
        TestSuite suite = new TestSuite();
        suite.addTest(BaseballCardDetailsPartialInputTest.suite());

        return suite;
    }

    private static final String TAG = BBCTTestRunner.class.getName();

}
