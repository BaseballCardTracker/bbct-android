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

import android.app.Instrumentation;
import android.content.Context;
import android.graphics.Rect;
import android.os.RemoteException;
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.support.test.uiautomator.UiDevice;
import android.test.ViewAsserts;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import bbct.android.common.R;
import bbct.android.common.activity.BaseballCardDetails;
import bbct.android.common.activity.FragmentTestActivity;
import bbct.android.common.data.BaseballCard;
import bbct.android.common.test.BBCTTestUtil;
import bbct.android.common.test.BaseballCardCsvFileReader;
import butterknife.ButterKnife;
import butterknife.InjectView;
import java.io.InputStream;
import junit.framework.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasFocus;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * Tests for {@link BaseballCardDetails}.
 */
@RunWith(AndroidJUnit4.class)
public class BaseballCardDetailsTest {

    private static final int SLEEP_TIME_TO_REFRESH = 200;
    private static final int KEYPAD_HEIGHT = 100;
    private static final String CARD_DATA = "cards.csv";
    private static final String TAG = BaseballCardDetailsTest.class.getName();

    @Rule
    public ActivityTestRule<FragmentTestActivity> activityTestRule
            = new ActivityTestRule<>(FragmentTestActivity.class);

    private UiDevice device;
    private FragmentTestActivity activity;
    private Instrumentation inst;
    private BaseballCard card;

    @InjectView(R.id.player_position_text) Spinner playerPositionSpinner;
    @InjectView(R.id.scroll_card_details) ScrollView scrollView;

    /**
     * Set up test fixture. This consists of an instance of the
     * {@link BaseballCardDetails} activity and all of its {@link EditText} and
     * {@link Button} views and a list of {@link BaseballCard} data.
     *
     * @throws Exception
     *             If an error occurs while chaining to the super class.
     */
    @Before
    public void setUp() throws Exception {
        this.inst = InstrumentationRegistry.getInstrumentation();
        this.device = UiDevice.getInstance(this.inst);

        InputStream in = this.inst.getContext().getAssets().open(CARD_DATA);
        BaseballCardCsvFileReader cardInput = new BaseballCardCsvFileReader(in,
                true);
        this.card = cardInput.getNextBaseballCard();
        cardInput.close();

        this.inst.setInTouchMode(true);
        this.activity = activityTestRule.getActivity();
        this.activity.replaceFragment(new BaseballCardDetails());
        ButterKnife.inject(this, this.activity);
    }

    /**
     * Check preconditions which must hold to guarantee the validity of all
     * other tests. Assert that the Activity to test is not <code>null</code>,
     * that none of its {@link EditText} views or {@link Button}s are
     * <code>null</code>.
     */
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

    /**
     * Test that all text in the {@link EditText} views of a
     * {@link BaseballCardDetails} activity is preserved when the activity is
     * destroyed and the text is restored when the activity is restarted.
     */
    @Test
    public void testStateDestroy() throws RemoteException {
        BBCTTestUtil.sendKeysToCardDetails(this.card);
        UiDevice device = UiDevice.getInstance(inst);
        device.setOrientationLeft();
        BBCTTestUtil.assertAllEditTextContents(this.card);
        device.setOrientationNatural();
    }

    /**
     * Test that 1)the focus moves to the next control {@link EditText} in the
     * {@link BaseballCardDetails} activity when the next button is clicked in
     * the soft keyboard. 2)keyboard is removed when the done button is clicked
     * in the soft keyboard
     */
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

        ViewGroup content = ButterKnife.findById(this.activity, android.R.id.content);
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

    /**
     * Test that view of {@link BaseballCardDetails} activity can be scrolled
     * when the save button is not visible on screen.
     */
    @Test
    public void testCardDetailsScroll() {
        View parentView = this.activity.getWindow().getDecorView();
        // hide the soft keypad
        InputMethodManager imm = (InputMethodManager) this.activity
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(parentView.getWindowToken(), 0);
        // Wait till the keypad disappears
        try {
            Thread.sleep(SLEEP_TIME_TO_REFRESH);
        } catch (InterruptedException e) {
            Log.e("getResult", e.getMessage());
        }
        // check if the 'Player Position' spinner is already visible. If yes, then
        // the screen cannot be scrolled. Assert true and return.
        if (BBCTTestUtil.isViewOnScreen(parentView, this.playerPositionSpinner)) {
            Assert.assertTrue(true);
        } else {
            // scroll to the bottom and check if save button is on the screen
            this.scrollView.post(new Runnable() {
                @Override
                public void run() {
                    BaseballCardDetailsTest.this.scrollView
                            .fullScroll(View.FOCUS_DOWN);
                }
            });
            // Wait for the scroll
            try {
                Thread.sleep(SLEEP_TIME_TO_REFRESH);
            } catch (InterruptedException e) {
                // left blank
            }
            ViewAsserts.assertOnScreen(parentView, this.playerPositionSpinner);
        }
    }

}
