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
import android.view.KeyEvent;

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
        for (int i = 0; i < input.length(); ++i) {
            String key = input.substring(i, i + 1);
            char keyChar = key.charAt(0);
            
            if (Character.isLetter(keyChar)) {
                if (Character.isUpperCase(keyChar)) {
                    test.sendKeys(KeyEvent.KEYCODE_SHIFT_LEFT);
                    test.sendKeys(key);
                } else {
                    test.sendKeys(key.toUpperCase());
                }
            } else if (Character.isDigit(keyChar)) {
                test.sendKeys(key);
            } else {
                switch (keyChar) {
                    case ' ':
                        test.sendKeys(KeyEvent.KEYCODE_SPACE);
                        break;
                        
                    // TODO Add cases for other printable characters
                }
            }
        }
    }

    private AndroidTestUtil() {
    }
}
