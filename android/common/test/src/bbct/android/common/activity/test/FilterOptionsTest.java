/*
 * This file is part of BBCT for Android.
 *
 * Copyright 2012 codeguru <codeguru@users.sourceforge.net>
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
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.util.Log;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import bbct.android.common.R;
import bbct.android.common.activity.FilterOptions;
import bbct.android.common.activity.filter.FilterActivity;
import bbct.android.common.activity.filter.NumberFilter;
import bbct.android.common.activity.filter.PlayerNameFilter;
import bbct.android.common.activity.filter.YearAndNumberFilter;
import bbct.android.common.activity.filter.YearFilter;
import junit.framework.Assert;

/**
 * Tests for {@link FilterOptions} activity class.
 *
 * TODO Add tests for configuration changes
 *
 * @author codeguru <codeguru@users.sourceforge.net>
 */
public class FilterOptionsTest extends ActivityInstrumentationTestCase2<FilterOptions> {

    /**
     * Create instrumented test cases for {@link FilterOptions}.
     */
    public FilterOptionsTest() {
        super(FilterOptions.class);
    }

    /**
     * Set up test fixture. This consists of an instance of the
     * {@link FilterOptions} activity, its {@link RadioGroup}, and the "OK" and
     * "Cancel" {@link Button}s.
     *
     * @throws Exception If an error occurs while chaining to the super class.
     */
    @Override
    public void setUp() throws Exception {
        super.setUp();

        this.inst = this.getInstrumentation();
        this.activity = this.getActivity();
        this.filterOptionsRadioGroup = (RadioGroup) this.activity.findViewById(R.id.filter_options_radio_group);
        this.okButton = (Button) this.activity.findViewById(R.id.ok_button);
        this.cancelButton = (Button) this.activity.findViewById(R.id.cancel_button);
    }

    /**
     * Check preconditions which must hold to guarantee the validity of all
     * other tests. Assert that the Activity to test and its {@link RadioGroup}
     * and {@link Button}s are not
     * <code>null</code>, that the {@link RadioGroup} has 4
     * {@link RadioButton}s, and that none of the {@link RadioButton}s are
     * checked.
     */
    public void testPreConditions() {
        Assert.assertNotNull(this.activity);
        Assert.assertNotNull(this.filterOptionsRadioGroup);
        Assert.assertNotNull(this.okButton);
        Assert.assertNotNull(this.cancelButton);

        Assert.assertEquals(RADIO_BUTTON_COUNT, this.filterOptionsRadioGroup.getChildCount());
        Assert.assertEquals(NO_RADIO_BUTTON_CHECKED, this.filterOptionsRadioGroup.getCheckedRadioButtonId());
    }

    /**
     * Test that the title of the {@link Activity} is correct.
     */
    public void testTitle() {
        String title = this.activity.getTitle().toString();
        String filterOptionsTitle = this.activity.getString(R.string.filter_options_title);

        Assert.assertTrue(title.contains(filterOptionsTitle));
    }

    /**
     * Check that an error message is displayed when the user clicks the "OK"
     * button without selecting a radio button.
     */
    @UiThreadTest
    public void testOkButtonOnClickWithNoRadioButtonChecked() {
        Assert.assertTrue(this.okButton.performClick());
        Assert.fail("How do I check that the error Toast is raised?");
        Assert.assertFalse(this.activity.isFinishing());
    }

    private void testOkButtonOnClick(Class<? extends FilterActivity> filterActivityClass, final int radioButtonId) throws Throwable {
        Instrumentation.ActivityMonitor filterActivityMonitor = new Instrumentation.ActivityMonitor(filterActivityClass.getName(), null, false);
        this.inst.addMonitor(filterActivityMonitor);

        final RadioButton filterRadioButton = (RadioButton) this.activity.findViewById(radioButtonId);

        this.runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "Running on UI Thread");
                Assert.assertFalse(filterRadioButton.performClick());
                Assert.assertTrue(FilterOptionsTest.this.okButton.performClick());
            }
        });

        Activity filterActivity = this.inst.waitForMonitorWithTimeout(filterActivityMonitor, TIME_OUT);
        Assert.assertNotNull(filterActivity);
        Assert.assertEquals(filterActivityClass, filterActivity.getClass());
        filterActivity.finish();
        Assert.assertTrue(filterActivity.isFinishing());
    }

    /**
     * Test that {@link YearFilter} starts when the user selects the "Year"
     * radio button and clicks "OK".
     *
     * @throws Throwable If an error occurs while the portion of the test on the
     * UI thread runs.
     */
    public void testOkButtonOnClickWithYearRadioButtonChecked() throws Throwable {
        Log.d(TAG, "testOkButtonOnClickWithYearRadioButtonChecked()");
        this.testOkButtonOnClick(YearFilter.class, R.id.year_filter_radio_button);
    }

    /**
     * Test that {@link NumberFilter} starts when the user selects the "Number"
     * radio button and clicks "OK".
     *
     * @throws Throwable If an error occurs while the portion of the test on the
     * UI thread runs.
     */
    public void testOkButtonOnClickWithNumberRadioButtonChecked() throws Throwable {
        this.testOkButtonOnClick(NumberFilter.class, R.id.number_filter_radio_button);
    }

    /**
     * Test that {@link YearAndNumberFilter} starts when the user selects the
     * "Year and Number" radio button and clicks "OK".
     *
     * @throws Throwable If an error occurs while the portion of the test on the
     * UI thread runs.
     */
    public void testOkButtonOnClickWithYearAndNumberRadioButtonChecked() throws Throwable {
        this.testOkButtonOnClick(YearAndNumberFilter.class, R.id.year_and_number_filter_radio_button);
    }

    /**
     * Test that {@link PlayerNameFilter} starts when the user selects the
     * "Player Name" radio button and clicks "OK".
     *
     * @throws Throwable If an error occurs while the portion of the test on the
     * UI thread runs.
     */
    public void testOkButtonOnClickWithPlayerRadioButtonChecked() throws Throwable {
        this.testOkButtonOnClick(PlayerNameFilter.class, R.id.player_name_filter_radio_button);
    }

    /**
     * Test that the {@link Activity} finishes when the user clicks "Cancel".
     */
    @UiThreadTest
    public void testCancelButtonOnClick() {
        Assert.assertTrue(this.cancelButton.performClick());
        Assert.assertTrue(this.activity.isFinishing());
    }
    private Instrumentation inst = null;
    private Activity activity = null;
    private RadioGroup filterOptionsRadioGroup = null;
    private Button okButton = null;
    private Button cancelButton = null;
    private static final int RADIO_BUTTON_COUNT = 4;
    private static final int NO_RADIO_BUTTON_CHECKED = -1;
    private static final int TIME_OUT = 5 * 1000; // 5 seconds
    private static final String TAG = "FilterOptionsTest";
}
