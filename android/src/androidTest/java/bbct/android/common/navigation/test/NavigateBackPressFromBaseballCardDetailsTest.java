/*
 * This file is part of BBCT for Android.
 *
 * Copyright 2018 codeguru <codeguru@users.sourceforge.net>
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
package bbct.android.common.navigation.test;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import bbct.android.common.R;
import bbct.android.common.activity.MainActivity;
import bbct.android.common.test.BBCTTestUtil;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class NavigateBackPressFromBaseballCardDetailsTest<T extends MainActivity> {
    @Rule
    public ActivityTestRule<T> activityActivityTestRule = null;

    public NavigateBackPressFromBaseballCardDetailsTest(Class<T> activityClass) {
        this.activityActivityTestRule = new ActivityTestRule<>(activityClass);
    }

    @Test
    public void testDefaultNavigateUpWithNoData() {
        String cardDetailsTitle = getInstrumentation().getTargetContext().getString(R.string.card_details_title);
        String expectedTitle = getInstrumentation().getTargetContext().getString(R.string.bbct_title, cardDetailsTitle);
        onView(withText(expectedTitle))
                .check(matches(isDisplayed()));
        BBCTTestUtil.hideKeyboardAndPressBack();
        onView(withText(R.string.app_name))
                .check(matches(isDisplayed()));
    }
}