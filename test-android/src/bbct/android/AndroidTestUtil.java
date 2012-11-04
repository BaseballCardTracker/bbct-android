/*
 * This file is part of bbct.
 *
 * Copyright 2012 codeguru <codeguru@users.sourceforge.net>
 *
 * bbct is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * bbct is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package bbct.android;

import android.test.InstrumentationTestCase;

/**
 * Utility class with convenience methods for testing Android apps.
 *
 * @author codeguru <codeguru@users.sourceforge.net>
 */
public class AndroidTestUtil {

    /**
     * Convenience method that sends the digits of an
     * <code>int</code> variable to an
     * {@link android.test.InstrumentationTestCase} as key presses.
     *
     * @param test The {@link android.test.InstrumentationTestCase} to send the
     * key presses to
     * @param input The integer to send
     */
    public static void sendKeysFromInt(InstrumentationTestCase test, int input) {
        AndroidTestUtil.sendKeysFromString(test, Integer.toString(input));
    }

    /**
     * Convenience method that sends the characters of a
     * {@link java.lang.String} to an
     * {@link android.test.InstrumentationTestCase} as key presses.
     *
     * @param test The {@link android.test.InstrumentationTestCase} to send the
     * key presses to
     * @param input The {@link java.lang.String} to send
     */
    public static void sendKeysFromString(InstrumentationTestCase test, String input) {
        input = input.toUpperCase();
        for (int i = 0; i < input.length(); ++i) {
            test.sendKeys(input.substring(i, i + 1));
        }
    }

    private AndroidTestUtil() {
    }
}
