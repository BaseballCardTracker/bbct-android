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
import android.app.Instrumentation;
import android.graphics.Rect;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.test.InstrumentationRegistry;
import androidx.test.espresso.Espresso;
import androidx.test.runner.AndroidJUnit4;
import androidx.test.uiautomator.UiDevice;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.InputStream;

import bbct.android.common.R;
import bbct.android.common.activity.BaseballCardDetails;
import bbct.android.common.database.BaseballCard;
import bbct.android.common.test.BBCTTestUtil;
import bbct.android.common.test.BaseballCardCsvFileReader;
import bbct.android.common.test.rule.SupportFragmentTestRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.hasFocus;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

@RunWith(AndroidJUnit4.class)
public class BaseballCardDetailsTest {

    private static final int SLEEP_TIME_TO_REFRESH = 200;
    private static final int KEYPAD_HEIGHT = 100;
    private static final String CARD_DATA = "cards.csv";
    private static final String TAG = BaseballCardDetailsTest.class.getName();

    @Rule
    public SupportFragmentTestRule fragmentTestRule
            = new SupportFragmentTestRule(new BaseballCardDetails());

    private UiDevice device;
    private Activity activity;
    private BaseballCard card;

    @Before
    public void setUp() throws Exception {
        Instrumentation inst = InstrumentationRegistry.getInstrumentation();
        this.device = UiDevice.getInstance(inst);

        InputStream in = inst.getContext().getAssets().open(CARD_DATA);
        BaseballCardCsvFileReader cardInput = new BaseballCardCsvFileReader(in,
                true);
        this.card = cardInput.getNextBaseballCard();
        cardInput.close();

        this.activity = fragmentTestRule.getActivity();
    }

    @After
    public void tearDown() throws RemoteException {
        device.setOrientationNatural();
    }

    @Test
    public void testPreConditions() {
        Assert.assertNotNull(this.activity);
        onView(withId(R.id.brand_text)).check(matches(isDisplayed()));
        onView(withId(R.id.year_text)).check(matches(isDisplayed()));
        onView(withId(R.id.number_text)).check(matches(isDisplayed()));
        onView(withId(R.id.value_text)).check(matches(isDisplayed()));
        onView(withId(R.id.count_text)).check(matches(isDisplayed()));
        onView(withId(R.id.player_name_text)).check(matches(isDisplayed()));
        onView(withId(R.id.team_text)).check(matches(isDisplayed()));
        onView(withId(R.id.player_position_text)).check(matches(isDisplayed()));
        onView(withId(R.id.scroll_card_details)).check(matches(isDisplayed()));
    }

    @Test
    public void testStateDestroy() throws RemoteException {
        BBCTTestUtil.sendKeysToCardDetails(this.card);
        device.setOrientationLeft();
        BBCTTestUtil.assertAllEditTextContents(this.card);
    }

    @Test
    public void testNextButtonOnClick() {
        onView(withId(R.id.brand_text)).check(matches(hasFocus()));
        device.pressEnter();
        onView(withId(R.id.year_text)).perform(click()).check(matches(hasFocus()));
        device.pressEnter();
        onView(withId(R.id.number_text)).check(matches(hasFocus()));
        device.pressEnter();
        onView(withId(R.id.value_text)).check(matches(hasFocus()));
        device.pressEnter();
        onView(withId(R.id.count_text)).check(matches(hasFocus()));
        device.pressEnter();
        onView(withId(R.id.player_name_text)).check(matches(hasFocus()));
        device.pressEnter();
        onView(withId(R.id.team_text)).check(matches(hasFocus()));

        ViewGroup content = activity.findViewById(android.R.id.content);
        View rootView = content.getChildAt(0);
        Rect r = new Rect();
        // r will be populated with the coordinates of the view area still
        // visible.
        rootView.getWindowVisibleDisplayFrame(r);

        int heightdiffBefore = rootView.getRootView().getHeight()
                - (r.bottom - r.top);
        boolean condnBefore = false;
        if (heightdiffBefore > KEYPAD_HEIGHT) {
            condnBefore = true;
        }

        device.pressEnter();
        // Wait for the keyboard to disappear and view to be refreshed
        try {
            Thread.sleep(SLEEP_TIME_TO_REFRESH);
        } catch (InterruptedException e) {
            Log.e(TAG, "Click Done button in soft Keyboard");
        }
        rootView.getWindowVisibleDisplayFrame(r);
        int heightdiffAfter = rootView.getRootView().getHeight()
                - (r.bottom - r.top);
        boolean condnAfter = false;
        if (heightdiffAfter < KEYPAD_HEIGHT) {
            condnAfter = true;
        }
        Assert.assertTrue(condnBefore && condnAfter);
    }

    @Test
    public void testCardDetailsScroll() {
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.player_position_text))
                .perform(scrollTo())
                .check(matches(isDisplayed()));
    }

}
