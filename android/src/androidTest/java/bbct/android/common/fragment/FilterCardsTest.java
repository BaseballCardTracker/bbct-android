/*
 * This file is part of BBCT for Android.
 *
 * Copyright 2012-14 codeguru <codeguru@users.sourceforge.net>
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
package bbct.android.common.fragment;

import android.app.Activity;
import android.os.RemoteException;

import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;
import androidx.test.uiautomator.UiDevice;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import bbct.android.common.R;
import bbct.android.common.activity.FilterCards;
import bbct.android.common.test.rule.SupportFragmentTestRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasFocus;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isEnabled;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class FilterCardsTest {
    @Rule
    public SupportFragmentTestRule fragmentTestRule
            = new SupportFragmentTestRule(new FilterCards());

    private Activity activity = null;
    private UiDevice device;

    @Before
    public void setUp() {
        activity = fragmentTestRule.getActivity();
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
    }

    @After
    public void tearDown() throws RemoteException {
        device.setOrientationNatural();
    }

    @Test
    public void testPreConditions() {
        Assert.assertNotNull(this.activity);
        onView(withId(R.id.confirm_button)).check(matches(not(isEnabled())));
    }

    @Test
    public void testTitle() {
        String filterCardsTitle = this.activity.getString(R.string.filter_cards_title);
        onView(withText(containsString(filterCardsTitle))).check(matches(isDisplayed()));
    }

    private void testCheckBox(int checkId, int inputId) {
        onView(withId(checkId)).perform(click());
        onView(withId(inputId)).check(matches(allOf(isEnabled(), hasFocus())));
        onView(withId(R.id.confirm_button)).check(matches(isDisplayed()));
    }

    @Test
    public void testBrandCheckBox() {
        this.testCheckBox(R.id.brand_check, R.id.brand_input);
    }

    @Test
    public void testYearCheckBox() {
        this.testCheckBox(R.id.year_check, R.id.year_input);
    }

    @Test
    public void testNumberCheckBox() {
        this.testCheckBox(R.id.number_check, R.id.number_input);
    }

    @Test
    public void testPlayerNameCheckBox() {
        this.testCheckBox(R.id.player_name_check, R.id.player_name_input);
    }

    @Test
    public void testTeamCheckBox() {
        this.testCheckBox(R.id.team_check, R.id.team_input);
    }

    @Test
    public void testSaveInstanceState() throws RemoteException {
        this.testNumberCheckBox();
        device.setOrientationLeft();
        onView(withId(R.id.number_input)).check(matches(isEnabled()));
    }
}
