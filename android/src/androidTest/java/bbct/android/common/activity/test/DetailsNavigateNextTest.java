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

import android.support.annotation.IdRes;
import android.support.test.runner.AndroidJUnit4;
import android.view.KeyEvent;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import bbct.android.common.R;
import bbct.android.common.activity.BaseballCardDetails;
import bbct.android.common.test.rule.SupportFragmentTestRule;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.pressKey;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasFocus;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static bbct.android.common.test.ViewActions.requestFocus;

@RunWith(AndroidJUnit4.class)
public class DetailsNavigateNextTest {
    @Rule
    public SupportFragmentTestRule fragmentTestRule
            = new SupportFragmentTestRule(new BaseballCardDetails());

    @Test
    public void brandNext() {
        testNext(R.id.brand_text, R.id.year_text);
    }

    @Test
    public void yearNext() {
        testNext(R.id.year_text, R.id.number_text);
    }

    @Test
    public void numberNext() {
        testNext(R.id.number_text, R.id.value_text);
    }

    @Test
    public void valueNext() {
        testNext(R.id.value_text, R.id.count_text);
    }

    @Test
    public void countNext() {
        testNext(R.id.count_text, R.id.player_name_text);
    }

    @Test
    public void playerNameNext() {
        testNext(R.id.player_name_text, R.id.team_text);
    }

    private void testNext(@IdRes int startingTextView, @IdRes int nextTextView) {
        onView(withId(startingTextView))
                .check(matches(isDisplayed()))
                .perform(requestFocus())
                .check(matches(hasFocus()))
                .perform(pressKey(KeyEvent.KEYCODE_ENTER));
        onView(withId(nextTextView))
                .check(matches(hasFocus()));
    }
}
