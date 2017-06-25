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
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import bbct.android.common.R;
import bbct.android.common.activity.BaseballCardDetails;
import bbct.android.common.test.rule.DataTestRule;
import bbct.android.common.test.rule.SupportFragmentTestRule;
import bbct.data.BaseballCard;

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
    public DataTestRule dataTestRule = new DataTestRule();
    @Rule
    public SupportFragmentTestRule fragmentTestRule
            = new SupportFragmentTestRule(new BaseballCardDetails());

    private BaseballCard mCard;
    private UiDevice device;

    @Before
    public void setUp() throws Exception {
        mCard = dataTestRule.getCard(0);
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
    }

    @After
    public void tearDown() throws RemoteException {
        device.setOrientationNatural();
    }

    @Test
    public void testBrandAutoComplete() throws Throwable {
        this.testAutoComplete(R.id.brand_text, mCard.getBrand());
    }

    @Test
    public void testPlayerNameAutoComplete() throws Throwable {
        this.testAutoComplete(R.id.player_name_text, mCard.getPlayerName());
    }

    @Test
    public void testTeamAutoComplete() throws Throwable {
        this.testAutoComplete(R.id.team_text, mCard.getTeam());
    }

    private void testAutoComplete(int textViewId, String text)
            throws Throwable {
        Activity activity = fragmentTestRule.getActivity();
        onView(withId(textViewId)).perform(typeText(text.substring(0, 2)));
        onData(allOf(instanceOf(Cursor.class), withRowString(1, text)))
                .inRoot(withDecorView(not(activity.getWindow().getDecorView())))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testBrandAutoCompleteDestroy() throws RemoteException {
        testAutoCompleteDestroy(R.id.brand_text, mCard.getBrand());
    }

    @Test
    public void testPlayerNameAutoCompleteDestroy() throws RemoteException {
        testAutoCompleteDestroy(R.id.player_name_text, mCard.getPlayerName());
    }

    @Test
    public void testTeamAutoCompleteDestroy() throws RemoteException {
        testAutoCompleteDestroy(R.id.team_text, mCard.getTeam());
    }

    private void testAutoCompleteDestroy(int id, String text) throws RemoteException {
        onView(withId(id)).perform(typeText(text));
        device.setOrientationLeft();
        onView(withId(id)).check(matches(withText(text)));
    }
}
