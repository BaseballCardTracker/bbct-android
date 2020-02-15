/*
 * This file is part of BBCT for Android.
 *
 * Copyright 2015 codeguru <codeguru@users.sourceforge.net>
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
package bbct.android.common.functional.test;

import android.os.RemoteException;

import org.junit.Test;

import bbct.android.common.R;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeTextIntoFocusedView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

public class BaseballCardDetailsSleepTest extends UiAutomatorTest {
    private static final String TAG = BaseballCardDetailsSleepTest.class.getName();

    @Test
    public void testSleep() throws RemoteException {
        String brand = "Topps";
        onView(withId(R.id.add_button))
            .perform(click());
        onView(withId(R.id.brand_text))
            .perform(clearText(), typeTextIntoFocusedView(brand));

        device.waitForIdle();
        device.sleep();

        device.wakeUp();
        onView(allOf(withId(R.id.brand), withText(brand)))
            .check(matches(isDisplayed()));
    }
}
