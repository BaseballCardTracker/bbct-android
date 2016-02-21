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
package bbct.android.common.activity.test;

import android.app.Activity;
import android.os.RemoteException;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import bbct.android.common.R;
import bbct.android.common.activity.FilterCards;
import bbct.android.common.activity.FragmentTestActivity;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasFocus;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;

/**
 * Tests for {@link FilterCards} activity class.
 */
@RunWith(AndroidJUnit4.class)
public class FilterCardsTest {
    @Rule
    public ActivityTestRule<FragmentTestActivity> activityTestRule =
            new ActivityTestRule<>(FragmentTestActivity.class);

    private FragmentTestActivity activity = null;
    private UiDevice device;

    @Before
    public void setUp() throws Exception {
        activity = activityTestRule.getActivity();
        activity.replaceFragment(new FilterCards());
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
    }

    @After
    public void tearDown() throws RemoteException {
        device.setOrientationNatural();
    }

    /**
     * Check preconditions which must hold to guarantee the validity of all
     * other tests. Assert that the Activity to test is not <code>null</code>
     * and its {@link EditText}s and "Ok" {@link Button} are disabled.
     */
    @Test
    public void testPreConditions() {
        Assert.assertNotNull(this.activity);
        onView(withId(R.id.save_menu)).check(doesNotExist());
    }

    /**
     * Test that the title of the {@link Activity} is correct.
     */
    @Test
    public void testTitle() {
        String filterCardsTitle = this.activity.getString(R.string.filter_cards_title);
        onView(withText(containsString(filterCardsTitle))).check(matches(isDisplayed()));
    }

    private void testCheckBox(int filterId) {
        onView(allOf(withParent(withId(filterId)), instanceOf(CheckBox.class)))
                .perform(click());
        onView(allOf(withParent(withId(filterId)), instanceOf(EditText.class)))
                .check(matches(allOf(isEnabled(), hasFocus())));
        onView(withId(R.id.save_menu)).check(matches(isDisplayed()));
    }

    /**
     * Test that "Brand" {@link EditText} is enabled and
     * has focus upon clicking on the corresponding {@link CheckBox}.
     */
    @Test
    public void testBrandCheckBox() {
        this.testCheckBox(R.id.brand);
    }

    /**
     * Test that "Year" {@link EditText} is enabled and
     * has focus upon clicking on the corresponding {@link CheckBox}.
     */
    @Test
    public void testYearCheckBox() {
        this.testCheckBox(R.id.year);
    }

    /**
     * Test that "Number" {@link EditText} is enabled and
     * has focus upon clicking on the corresponding {@link CheckBox}.
     */
    @Test
    public void testNumberCheckBox() {
        this.testCheckBox(R.id.number);
    }

    /**
     * Test that "Player Name" {@link EditText} is enabled and
     * has focus upon clicking on the corresponding {@link CheckBox}.
     */
    @Test
    public void testPlayerNameCheckBox() {
        this.testCheckBox(R.id.player_name);
    }

    /**
     * Test that "Team" {@link EditText} is enabled and
     * has focus upon clicking on the corresponding {@link CheckBox}.
     */
    @Test
    public void testTeamCheckBox() {
        this.testCheckBox(R.id.team);
    }

    @Test
    public void testSaveInstanceStateBrand() throws RemoteException {
        this.testSaveInstanceState(R.id.brand);
    }

    @Test
    public void testSaveInstanceStateYear() throws RemoteException {
        this.testSaveInstanceState(R.id.year);
    }

    @Test
    public void testSaveInstanceStateNumber() throws RemoteException {
        this.testSaveInstanceState(R.id.number);
    }

    @Test
    public void testSaveInstanceStateTeam() throws RemoteException {
        this.testSaveInstanceState(R.id.team);
    }

    private void testSaveInstanceState(int filterId) throws RemoteException {
        this.testCheckBox(filterId);
        device.setOrientationLeft();
        onView(allOf(withParent(withId(filterId)), instanceOf(CheckBox.class)))
                .check(matches(isChecked()));
        onView(allOf(withParent(withId(filterId)), instanceOf(EditText.class)))
                .check(matches(isEnabled()));
    }
}
