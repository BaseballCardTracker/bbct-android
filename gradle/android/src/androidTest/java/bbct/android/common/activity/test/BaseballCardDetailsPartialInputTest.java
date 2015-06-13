/*
 * This file is part of BBCT for Android.
 *
 * Copyright 2012-14 codeguru <codeguru@users.sourceforge.net==
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
 * along with this program.  If not, see <http://www.gnu.org/licenses/==.
 */
package bbct.android.common.activity.test;

import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import bbct.android.common.R;
import bbct.android.common.activity.BaseballCardDetails;
import bbct.android.common.activity.FragmentTestActivity;
import bbct.android.common.data.BaseballCard;
import bbct.android.common.test.BBCTTestUtil;
import bbct.android.common.test.BaseballCardCsvFileReader;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.robotium.solo.Solo;
import java.io.IOException;
import java.io.InputStream;
import java.util.EnumSet;
import java.util.Set;
import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * A parameterized test which can test any combination of input in the
 * {@link BaseballCardDetails} activity.
 */
public class BaseballCardDetailsPartialInputTest extends
        ActivityInstrumentationTestCase2<FragmentTestActivity> {

    private static final String CARD_DATA = "cards.csv";
    private static final String TEST_NAME = "testPartialInput";
    private static final String TAG = BaseballCardDetailsPartialInputTest.class.getName();

    private Solo solo = null;
    private FragmentTestActivity activity = null;
    private Instrumentation inst = null;
    private BaseballCard card = null;
    private final Set<BBCTTestUtil.EditTexts> inputFieldsMask;

    @InjectView(R.id.brand_text) EditText brandEditText = null;
    @InjectView(R.id.year_text) EditText yearEditText = null;
    @InjectView(R.id.number_text) EditText numberEditText = null;
    @InjectView(R.id.count_text) EditText countEditText = null;
    @InjectView(R.id.value_text) EditText valueEditText = null;
    @InjectView(R.id.player_name_text) EditText playerNameEditText = null;
    @InjectView(R.id.team_text) EditText teamEditText = null;

    /**
     * Creates a {@link TestSuite} containing every possible combination of
     * blank {@link EditText} views in the {@link BaseballCardDetails} activity.
     *
     * @return A {@link TestSuite} containing every possible combination of
     * blank {@link EditText} views in the {@link BaseballCardDetails} activity.
     */
    public static Test suite() {
        TestSuite suite = new TestSuite();
        Set<BBCTTestUtil.EditTexts> editTexts = EnumSet.allOf(BBCTTestUtil.EditTexts.class);
        editTexts.remove(BBCTTestUtil.EditTexts.PLAYER_POSITION);
        Set<Set<BBCTTestUtil.EditTexts>> masks = BBCTTestUtil.powerSet(editTexts);

        for (Set<BBCTTestUtil.EditTexts> mask : masks) {
            suite.addTest(new BaseballCardDetailsPartialInputTest(mask));
        }

        return suite;
    }

    /**
     * Creates a test which will input data to the {@link EditText} views
     * indicated by the given flags.<code>|</code>).
     *
     * @param inputFieldsFlags The {@link EditText} views to receive input.
     *
     */
    public BaseballCardDetailsPartialInputTest(Set<BBCTTestUtil.EditTexts> inputFieldsFlags) {
        super(FragmentTestActivity.class);

        this.setName(TEST_NAME);
        this.inputFieldsMask = inputFieldsFlags;
    }

    /**
     * Set up the test fixture for this test. Reads an input CSV file for
     * baseball card data to use as input. Finds the {@link EditText} and
     * {@link Button} views which will be used during testing.
     *
     * @throws IOException If an error occurs while reading the CSV file.
     */
    @Override
    public void setUp() throws IOException {
        this.inst = this.getInstrumentation();

        InputStream in = this.inst.getContext().getAssets().open(CARD_DATA);
        BaseballCardCsvFileReader cardInput = new BaseballCardCsvFileReader(in, true);
        this.card = cardInput.getNextBaseballCard();
        cardInput.close();

        this.inst.setInTouchMode(true);
        this.activity = this.getActivity();
        this.activity.replaceFragment(new BaseballCardDetails());
        this.inst.waitForIdleSync();
        ButterKnife.inject(this, this.activity);

        this.solo = new Solo(this.inst, this.activity);
    }

    /**
     * Validates that {@link BaseballCardDetails} correctly handles missing
     * input by setting the error messages of any blank {@link EditText} views.
     *
     * @throws Throwable If an error occurs while the portion of the test on the
     * UI thread runs.
     */
    public void testPartialInput() throws Throwable {
        Log.d(TAG, "testPartialInput()");
        Log.d(TAG, "inputFieldsMask=" + this.inputFieldsMask);

        BBCTTestUtil.sendKeysToCardDetails(this.solo, this.card, this.inputFieldsMask);
        this.solo.clickOnActionBarItem(R.id.save_menu);

        EditText focusEditText = null;

        if (!this.inputFieldsMask.contains(BBCTTestUtil.EditTexts.TEAM)) {
            Assert.assertEquals(this.activity.getString(R.string.team_input_error), this.teamEditText.getError());
            focusEditText = this.teamEditText;
        }
        if (!this.inputFieldsMask.contains(BBCTTestUtil.EditTexts.PLAYER_NAME)) {
            Assert.assertEquals(this.activity.getString(R.string.player_name_input_error), this.playerNameEditText.getError());
            focusEditText = this.playerNameEditText;
        }
        if (!this.inputFieldsMask.contains(BBCTTestUtil.EditTexts.COUNT)) {
            Assert.assertEquals(this.activity.getString(R.string.count_input_error), this.countEditText.getError());
            focusEditText = this.countEditText;
        }
        if (!this.inputFieldsMask.contains(BBCTTestUtil.EditTexts.VALUE)) {
            Assert.assertEquals(this.activity.getString(R.string.value_input_error), this.valueEditText.getError());
            focusEditText = this.valueEditText;
        }
        if (!this.inputFieldsMask.contains(BBCTTestUtil.EditTexts.NUMBER)) {
            Assert.assertEquals(this.activity.getString(R.string.number_input_error), this.numberEditText.getError());
            focusEditText = this.numberEditText;
        }
        if (!this.inputFieldsMask.contains(BBCTTestUtil.EditTexts.YEAR)) {
            Assert.assertEquals(this.activity.getString(R.string.year_input_error), this.yearEditText.getError());
            focusEditText = this.yearEditText;
        }
        if (!this.inputFieldsMask.contains(BBCTTestUtil.EditTexts.BRAND)) {
            Assert.assertEquals(this.activity.getString(R.string.brand_input_error), this.brandEditText.getError());
            focusEditText = this.brandEditText;
        }

        this.inst.waitForIdleSync();

        if (focusEditText != null) {
            Assert.assertTrue(focusEditText.hasFocus());
        }
    }

}
