/*
 * This file is part of BBCT for Android.
 *
 * Copyright 2017 codeguru <codeguru@users.sourceforge.net>
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
package bbct.android.common.activity.test;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.view.KeyEvent;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import bbct.android.common.R;
import bbct.android.common.activity.BaseballCardDetails;
import bbct.android.common.activity.FragmentTestActivity;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.pressKey;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasFocus;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class DetailsNavigateNextTest {
    @Rule
    public ActivityTestRule<FragmentTestActivity> activityTestRule
            = new ActivityTestRule<>(FragmentTestActivity.class);

    @Test
    public void brandNext() throws Exception {
        FragmentTestActivity activity = activityTestRule.getActivity();
        activity.replaceFragment(new BaseballCardDetails());
        onView(withId(R.id.brand_text))
                .check(matches(hasFocus()))
                .perform(pressKey(KeyEvent.KEYCODE_ENTER));
        onView(withId(R.id.year_text))
                .check(matches(hasFocus()));
    }
}
