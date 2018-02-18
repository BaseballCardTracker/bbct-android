/*
 * This file is part of BBCT for Android.
 *
 * Copyright 2014 codeguru <codeguru@users.sourceforge.net>
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
package bbct.android.common.layout.test;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import bbct.android.common.R;
import bbct.android.common.activity.FilterCards;
import bbct.android.common.test.rule.SupportFragmentTestRule;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.isNotChecked;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class FilterCardsLayoutTest {
    @Rule
    public SupportFragmentTestRule fragmentTestRule
            = new SupportFragmentTestRule(new FilterCards());

    @Test
    public void testBrandCheckBox() {
        testFilterOption(R.id.brand);
    }

    public void testYearFilterOption() {
        testFilterOption(R.id.year);
    }

    public void testNumberFilterOption() {
        testFilterOption(R.id.number);
    }

    public void testPlayerNameFilterOption() {
        testFilterOption(R.id.player_name);
    }

    public void testTeamFilterOption() {
        testFilterOption(R.id.team);
    }

    private void testFilterOption(int id) {
        onView(withId(id))
                .check(matches(allOf(isDisplayed(), isNotChecked())));
        onView(allOf(withParent(withId(id)), instanceOf(CheckBox.class)))
                .check(matches(allOf(isDisplayed(), isNotChecked())));
        onView(allOf(withParent(withId(id)), instanceOf(EditText.class)))
                .check(matches(allOf(isDisplayed(), not(isEnabled()))));
    }
}
