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
import android.content.Context;
import android.graphics.Rect;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.test.ViewAsserts;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import bbct.android.common.R;
import bbct.android.common.activity.BaseballCardDetails;
import bbct.android.common.data.BaseballCard;
import bbct.android.common.test.BBCTTestUtil;
import bbct.android.common.test.BaseballCardCsvFileReader;
import java.io.InputStream;
import junit.framework.Assert;

/**
 * Tests for {@link BaseballCardDetails}.
 *
 * TODO: Add tests for the layout of {@link BaseballCardDetails}
 */
public class BaseballCardDetailsTest extends
        ActivityInstrumentationTestCase2<BaseballCardDetails> {

    private static final int SLEEP_TIME_TO_REFRESH = 200;
    private static final int KEYPAD_HEIGHT = 100;

    /**
     * Create instrumented test cases for {@link BaseballCardDetails}.
     */
    public BaseballCardDetailsTest() {
        super(BaseballCardDetails.class);
    }

    /**
     * Set up test fixture. This consists of an instance of the
     * {@link BaseballCardDetails} activity and all of its {@link EditText} and
     * {@link Button} views and a list of {@link BaseballCard} data.
     *
     * @throws Exception
     *             If an error occurs while chaining to the super class.
     */
    @Override
    public void setUp() throws Exception {
        super.setUp();

        this.inst = this.getInstrumentation();

        InputStream in = this.inst.getContext().getAssets()
                .open(BBCTTestUtil.CARD_DATA);
        BaseballCardCsvFileReader cardInput = new BaseballCardCsvFileReader(in,
                true);
        this.card = cardInput.getNextBaseballCard();
        cardInput.close();

        // Must call getActivity() before creating a DatabaseUtil object to
        // ensure that the database is created
        this.activity = this.getActivity();
        this.brandText = (EditText) this.activity.findViewById(R.id.brand_text);
        this.yearText = (EditText) this.activity.findViewById(R.id.year_text);
        this.numberText = (EditText) this.activity
                .findViewById(R.id.number_text);
        this.valueText = (EditText) this.activity.findViewById(R.id.value_text);
        this.countText = (EditText) this.activity.findViewById(R.id.count_text);
        this.playerNameText = (EditText) this.activity
                .findViewById(R.id.player_name_text);
        this.playerTeamText = (EditText) this.activity
                .findViewById(R.id.team_text);
        this.playerPositionSpinner = (Spinner) this.activity
                .findViewById(R.id.player_position_text);
        this.saveButton = (Button) this.activity.findViewById(R.id.save_button);
        this.doneButton = (Button) this.activity.findViewById(R.id.done_button);
        this.scrollView = (ScrollView) this.activity
                .findViewById(R.id.scroll_card_details);
    }

    /**
     * Check preconditions which must hold to guarantee the validity of all
     * other tests. Assert that the Activity to test is not <code>null</code>,
     * that none of its {@link EditText} views or {@link Button}s are
     * <code>null</code>.
     */
    public void testPreConditions() {
        Assert.assertNotNull(this.activity);
        Assert.assertNotNull(this.brandText);
        Assert.assertNotNull(this.yearText);
        Assert.assertNotNull(this.numberText);
        Assert.assertNotNull(this.valueText);
        Assert.assertNotNull(this.countText);
        Assert.assertNotNull(this.playerNameText);
        Assert.assertNotNull(this.playerTeamText);
        Assert.assertNotNull(this.playerPositionSpinner);
        Assert.assertNotNull(this.saveButton);
        Assert.assertNotNull(this.doneButton);
        Assert.assertNotNull(this.scrollView);
    }

    /**
     * Test that all text in the {@link EditText} views of a
     * {@link BaseballCardDetails} activity is preserved when the activity is
     * destroyed and the text is restored when the activity is restarted.
     *
     * @throws InterruptedException
     *             If BBCTTestUtil#sendKeysToCardDetails() is interrupted.
     */
    public void testStateDestroy() throws InterruptedException {
        BBCTTestUtil.sendKeysToCardDetails(this, this.activity, this.card);
        this.activity.finish();
        Assert.assertTrue(this.activity.isFinishing());
        this.activity = this.getActivity();
        BBCTTestUtil.assertAllEditTextContents(this.activity, this.card);
    }

    /**
     * Test that all text in the {@link EditText} views of a
     * {@link BaseballCardDetails} activity is preserved when the activity is
     * paused and the text is restored when the activity is restarted.
     *
     * @throws InterruptedException
     *             If {@link BBCTTestUtil#sendKeysToCardDetails()} is
     *             interrupted.
     */
    public void testStatePause() throws InterruptedException {
        BBCTTestUtil.sendKeysToCardDetails(this, this.activity, this.card);
        this.inst.callActivityOnRestart(this.activity);
        BBCTTestUtil.assertAllEditTextContents(this.activity, this.card);
    }

    /**
     * Test that the {@link BaseballCardDetails} activity finishes when the user
     * clicks the "Done" button.
     */
    @UiThreadTest
    public void testDoneButtonOnClick() {
        Assert.assertTrue(this.doneButton.performClick());
        Assert.assertTrue(this.activity.isFinishing());
    }

    /**
     * Test that 1)the focus moves to the next control {@link EditText} in the
     * {@link BaseballCardDetails} activity when the next button is clicked in
     * the soft keyboard. 2)keyboard is removed when the done button is clicked
     * in the soft keyboard
     */
    public void testNextButtonOnClick() {
        BBCTTestUtil.sendKeysToCurrFieldCardDetails(this.inst, this.brandText,
                this.card.getBrand());
        this.inst.sendKeyDownUpSync(KeyEvent.KEYCODE_ENTER);
        Assert.assertTrue(this.yearText.hasFocus());

        BBCTTestUtil.sendKeysToCurrFieldCardDetails(this.inst, this.yearText,
                Integer.toString(this.card.getYear()));
        this.inst.sendKeyDownUpSync(KeyEvent.KEYCODE_ENTER);
        Assert.assertTrue(this.numberText.hasFocus());

        BBCTTestUtil.sendKeysToCurrFieldCardDetails(this.inst, this.numberText,
                Integer.toString(this.card.getNumber()));
        this.inst.sendKeyDownUpSync(KeyEvent.KEYCODE_ENTER);
        Assert.assertTrue(this.valueText.hasFocus());

        BBCTTestUtil.sendKeysToCurrFieldCardDetails(this.inst, this.valueText,
                Integer.toString(this.card.getValue()));
        this.inst.sendKeyDownUpSync(KeyEvent.KEYCODE_ENTER);
        Assert.assertTrue(this.countText.hasFocus());

        BBCTTestUtil.sendKeysToCurrFieldCardDetails(this.inst, this.countText,
                Integer.toString(this.card.getCount()));
        this.inst.sendKeyDownUpSync(KeyEvent.KEYCODE_ENTER);
        Assert.assertTrue(this.playerNameText.hasFocus());

        BBCTTestUtil.sendKeysToCurrFieldCardDetails(this.inst,
                this.playerNameText, this.card.getPlayerName());
        this.inst.sendKeyDownUpSync(KeyEvent.KEYCODE_ENTER);
        Assert.assertTrue(this.playerTeamText.hasFocus());

        View rootView = ((ViewGroup) this.activity
                .findViewById(android.R.id.content)).getChildAt(0);
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

        BBCTTestUtil.sendKeysToCurrFieldCardDetails(this.inst,
                this.playerTeamText, this.card.getTeam());
        this.inst.sendKeyDownUpSync(KeyEvent.KEYCODE_ENTER);
        // Wait for the keyboard to disappear and view to be refreshed
        try {
            Thread.sleep(SLEEP_TIME_TO_REFRESH);
        } catch (InterruptedException e) {
            Log.e("Click Done button in soft Keyboard", e.getMessage());
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
        // check if the 'save' button is already visible. If yes, then
        // the screen cannot be scrolled. Assert true and return.
        if (BBCTTestUtil.isViewOnScreen(parentView, this.saveButton)) {
            assertTrue(true);
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
                Log.e("getResult", e.getMessage());
            }
            ViewAsserts.assertOnScreen(parentView, this.saveButton);
        }
    }

    public void testNavigateUp() {
        ActionBar actionBar = ((ActionBarActivity) this.activity).getSupportActionBar();
        Assert.assertTrue((actionBar.getDisplayOptions() & ActionBar.DISPLAY_HOME_AS_UP) > 0);
    }

    private Activity activity = null;
    private EditText brandText = null;
    private EditText yearText = null;
    private EditText numberText = null;
    private EditText valueText = null;
    private EditText countText = null;
    private EditText playerNameText = null;
    private EditText playerTeamText = null;
    private Spinner playerPositionSpinner = null;
    private Button saveButton = null;
    private Button doneButton = null;
    private ScrollView scrollView = null;
    private Instrumentation inst = null;
    private BaseballCard card = null;
}
