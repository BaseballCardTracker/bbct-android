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
package bbct.android.common.fragment;

import android.os.RemoteException;

import androidx.test.InstrumentationRegistry;
import androidx.test.runner.AndroidJUnit4;
import androidx.test.uiautomator.UiDevice;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import bbct.android.common.R;
import bbct.android.common.activity.BaseballCardDetails;
import bbct.android.common.database.BaseballCard;
import bbct.android.common.test.rule.DataTestRule;
import bbct.android.common.test.rule.SupportFragmentTestRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

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
    public void setUp() {
        mCard = dataTestRule.getCard(0);
        device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
    }

    @After
    public void tearDown() throws RemoteException {
        device.setOrientationNatural();
    }

    @Test
    public void testBrandAutoCompletePopup() {
        this.testAutoCompletePopup(R.id.brand_text, mCard.brand);
    }

    @Test
    public void testPlayerNameAutoCompletePopup() {
        this.testAutoCompletePopup(R.id.player_name_text, mCard.playerName);
    }

    @Test
    public void testTeamAutoCompletePopup() {
        this.testAutoCompletePopup(R.id.team_text, mCard.team);
    }

    private void testAutoCompletePopup(int textViewId, String text) {
        onView(withId(textViewId)).perform(typeText(text.substring(0, 2)));
        onView(withText(text))
            .inRoot(isPlatformPopup())
            .perform(click());
    }

    @Test
    public void testBrandAutoCompleteDestroy() throws RemoteException {
        testAutoCompleteDestroy(R.id.brand_text, mCard.brand);
    }

    @Test
    public void testPlayerNameAutoCompleteDestroy() throws RemoteException {
        testAutoCompleteDestroy(R.id.player_name_text, mCard.playerName);
    }

    @Test
    public void testTeamAutoCompleteDestroy() throws RemoteException {
        testAutoCompleteDestroy(R.id.team_text, mCard.team);
    }

    private void testAutoCompleteDestroy(int id, String text) throws RemoteException {
        onView(withId(id)).perform(typeText(text));
        device.setOrientationLeft();
        onView(withId(id)).check(matches(withText(text)));
    }

    @Test
    public void testBrandAutoCompleteSelect() {
        testAutoCompleteSelect(R.id.brand_text, mCard.brand);
    }

    @Test
    public void testPlayerNameAutoCompleteSelect() {
        testAutoCompleteSelect(R.id.player_name_text, mCard.playerName);
    }

    @Test
    public void testTeamAutoCompleteSelect() {
        testAutoCompleteSelect(R.id.team_text, mCard.team);
    }

    private void testAutoCompleteSelect(int textViewId, String text) {
        onView(withId(textViewId)).perform(typeText(text.substring(0, 2)));
        onView(withText(text))
            .inRoot(isPlatformPopup())
            .perform(click());
        onView(withId(textViewId)).check(matches(withText(text)));
    }
}
