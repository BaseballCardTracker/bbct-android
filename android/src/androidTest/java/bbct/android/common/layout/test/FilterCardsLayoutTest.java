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

import androidx.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import bbct.android.common.R;
import bbct.android.common.activity.FilterCards;
import bbct.android.common.test.rule.SupportFragmentTestRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.isNotChecked;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class FilterCardsLayoutTest {
    @Rule
    public SupportFragmentTestRule fragmentTestRule
            = new SupportFragmentTestRule(new FilterCards());

    @Test
    public void testBrandCheckBox() {
        testCheckBox(R.id.brand_check);
    }

    @Test
    public void testYearCheckBox() {
        testCheckBox(R.id.year_check);
    }

    @Test
    public void testNumberCheckBox() {
        testCheckBox(R.id.number_check);
    }

    @Test
    public void testPlayerNameCheckBox() {
        testCheckBox(R.id.player_name_check);
    }

    @Test
    public void testTeamCheckBox() {
        testCheckBox(R.id.team_check);
    }

    private void testCheckBox(int id) {
        onView(withId(id)).check(matches(allOf(isDisplayed(), isNotChecked())));
    }

    @Test
    public void testBrandEditText() {
        testEditText(R.id.brand_input);
    }

    @Test
    public void testYearEditText() {
        testEditText(R.id.year_input);
    }

    @Test
    public void testNumberEditText() {
        testEditText(R.id.number_input);
    }

    @Test
    public void testPlayerNameEditText() {
        testEditText(R.id.player_name_input);
    }

    @Test
    public void testTeamEditText() {
        testEditText(R.id.team_input);
    }

    private void testEditText(int id) {
        onView(withId(id)).check(matches(allOf(isDisplayed(), not(isEnabled()), withText(""))));
    }
}
