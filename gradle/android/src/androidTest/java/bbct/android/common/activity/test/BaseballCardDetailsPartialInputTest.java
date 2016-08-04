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
import java.io.IOException;
import java.io.InputStream;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Set;
import junit.framework.Test;
import junit.framework.TestSuite;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.hasFocus;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static bbct.android.common.test.matcher.Matchers.hasErrorText;

/**
 * A parameterized test which can test any combination of input in the
 * {@link BaseballCardDetails} activity.
 */
public class BaseballCardDetailsPartialInputTest extends
        ActivityInstrumentationTestCase2<FragmentTestActivity> {
    private static final String CARD_DATA = "cards.csv";
    private static final String TEST_NAME = "testPartialInput";
    private static final String TAG = BaseballCardDetailsPartialInputTest.class.getName();

    private BaseballCard card = null;
    private final Set<BBCTTestUtil.EditTexts> inputFieldsMask;

    /**
     * Creates a {@link TestSuite} containing every possible combination of
     * blank {@link EditText} views in the {@link BaseballCardDetails} activity.
     *
     * @return A {@link TestSuite} containing every possible combination of
     * blank {@link EditText} views in the {@link BaseballCardDetails} activity.
     */
    public static Test suite() {
        TestSuite suite = new TestSuite();
        Set<BBCTTestUtil.EditTexts> editTexts =
                EnumSet.range(BBCTTestUtil.EditTexts.BRAND, BBCTTestUtil.EditTexts.TEAM);

        for (BBCTTestUtil.EditTexts editText : editTexts) {
            Set<BBCTTestUtil.EditTexts> mask = new HashSet<>();
            mask.add(editText);
            Log.d(TAG, "mask: " + mask);
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
        Instrumentation inst = this.getInstrumentation();
        InputStream in = inst.getContext().getAssets().open(CARD_DATA);
        BaseballCardCsvFileReader cardInput = new BaseballCardCsvFileReader(in, true);
        this.card = cardInput.getNextBaseballCard();
        cardInput.close();

        inst.setInTouchMode(true);
        FragmentTestActivity activity = this.getActivity();
        activity.replaceFragment(new BaseballCardDetails());
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

        BBCTTestUtil.sendKeysToCardDetails(this.card, this.inputFieldsMask);
        onView(withId(R.id.save_menu)).perform(click());

        int focusId = -1;

        if (!this.inputFieldsMask.contains(BBCTTestUtil.EditTexts.TEAM)) {
            onView(withId(R.id.team_text))
                    .check(matches(hasErrorText(R.string.team_input_error)));
            focusId = R.id.team_text;
        }
        if (!this.inputFieldsMask.contains(BBCTTestUtil.EditTexts.PLAYER_NAME)) {
            onView(withId(R.id.player_name_text))
                    .check(matches(hasErrorText(R.string.player_name_input_error)));
            focusId = R.id.player_name_text;
        }
        if (!this.inputFieldsMask.contains(BBCTTestUtil.EditTexts.COUNT)) {
            onView(withId(R.id.count_text))
                    .check(matches(hasErrorText(R.string.count_input_error)));
            focusId = R.id.count_text;
        }
        if (!this.inputFieldsMask.contains(BBCTTestUtil.EditTexts.VALUE)) {
            onView(withId(R.id.value_text))
                    .check(matches(hasErrorText(R.string.value_input_error)));
            focusId = R.id.value_text;
        }
        if (!this.inputFieldsMask.contains(BBCTTestUtil.EditTexts.NUMBER)) {
            onView(withId(R.id.number_text))
                    .check(matches(hasErrorText(R.string.number_input_error)));
            focusId = R.id.number_text;
        }
        if (!this.inputFieldsMask.contains(BBCTTestUtil.EditTexts.YEAR)) {
            onView(withId(R.id.year_text))
                    .check(matches(hasErrorText(R.string.year_input_error)));
            focusId = R.id.year_text;
        }
        if (!this.inputFieldsMask.contains(BBCTTestUtil.EditTexts.BRAND)) {
            onView(withId(R.id.brand_text))
                    .check(matches(hasErrorText(R.string.brand_input_error)));
            focusId = R.id.brand_text;
        }

        if (focusId != -1) {
            onView(withId(focusId)).check(matches(hasFocus()));
        }
    }
}
