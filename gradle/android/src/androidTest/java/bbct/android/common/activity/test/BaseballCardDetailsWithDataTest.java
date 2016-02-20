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
package bbct.android.common.activity.test;

import android.app.Activity;
import android.database.Cursor;
import android.os.RemoteException;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import bbct.android.common.R;
import bbct.android.common.activity.BaseballCardDetails;
import bbct.android.common.activity.FragmentTestActivity;
import bbct.android.common.data.BaseballCard;
import bbct.android.common.test.DataRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.CursorMatchers.withRowString;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.not;

@RunWith(AndroidJUnit4.class)
public class BaseballCardDetailsWithDataTest {
    @Rule
    public DataRule dataRule = new DataRule();
    @Rule
    public ActivityTestRule<FragmentTestActivity> activityTestRule
            = new ActivityTestRule<>(FragmentTestActivity.class);

    private BaseballCard mCard;

    @Before
    public void setUp() throws Exception {
        FragmentTestActivity activity = activityTestRule.getActivity();
        activity.replaceFragment(new BaseballCardDetails());
        mCard = dataRule.getCard(0);
    }

    @Test
    public void testBrandAutoComplete() throws Throwable {
        this.testAutoComplete(R.id.brand, mCard.getBrand());
    }

    @Test
    public void testPlayerNameAutoComplete() throws Throwable {
        this.testAutoComplete(R.id.player_name, mCard.getPlayerName());
    }

    @Test
    public void testTeamAutoComplete() throws Throwable {
        this.testAutoComplete(R.id.team, mCard.getTeam());
    }

    private void testAutoComplete(int textViewId, String text)
            throws Throwable {
        Activity activity = activityTestRule.getActivity();
        onView(withId(textViewId)).perform(typeText(text.substring(0, 2)));
        onData(allOf(instanceOf(Cursor.class), withRowString(1, text)))
                .inRoot(withDecorView(not(activity.getWindow().getDecorView())))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testBrandAutoCompleteDestroy() throws RemoteException {
        testAutoCompleteDestroy(R.id.brand, mCard.getBrand());
    }

    @Test
    public void testPlayerNameAutoCompleteDestroy() throws RemoteException {
        testAutoCompleteDestroy(R.id.player_name, mCard.getPlayerName());
    }

    @Test
    public void testTeamAutoCompleteDestroy() throws RemoteException {
        testAutoCompleteDestroy(R.id.team, mCard.getTeam());
    }

    private void testAutoCompleteDestroy(int id, String text) throws RemoteException {
        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        onView(withId(id)).perform(typeText(text));
        device.setOrientationLeft();
        onView(withId(id)).check(matches(withText(text)));
        device.setOrientationNatural();
    }
}
