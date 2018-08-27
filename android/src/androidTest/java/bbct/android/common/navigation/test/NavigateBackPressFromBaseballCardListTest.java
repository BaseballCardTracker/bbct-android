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

import android.content.Context;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


import bbct.android.common.R;
import bbct.android.common.activity.MainActivity;
import bbct.android.common.test.BBCTTestUtil;
import bbct.android.lite.provider.LiteActivity;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class NavigateBackPressFromBaseballCardListTest<T extends MainActivity> {
    @Rule
    public ActivityTestRule<T> activityActivityTestRule = null;
    private Context context = null;

    public NavigateBackPressFromBaseballCardListTest(Class<T> activityClass) {
        this.activityActivityTestRule = new ActivityTestRule<>(activityClass);
    }

    @Before
    public void setUp() throws Exception {
        context = getInstrumentation().getTargetContext();
    }

    @Test
    public void testBackPress() {
        String expectedTitle = context.getString(R.string.app_name);
        BBCTTestUtil.hideKeyboardAndPressBack();
        onView(withText(expectedTitle))
                .check(matches(isDisplayed()));
    }
}