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
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import bbct.android.common.R;
import bbct.android.common.activity.FilterCards;
import com.robotium.solo.Solo;
import junit.framework.Assert;

/**
 * Tests for {@link FilterCards} activity class.
 */
public class FilterCardsTest extends ActivityInstrumentationTestCase2<FilterCards> {

    /**
     * Create instrumented test cases for {@link FilterCards}.
     */
    public FilterCardsTest() {
        super(FilterCards.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        this.activity = this.getActivity();
        this.solo = new Solo(this.getInstrumentation(), this.activity);
    }

    @Override
    protected void tearDown() throws Exception {
        this.solo.finishOpenedActivities();

        super.tearDown();
    }

    /**
     * Check preconditions which must hold to guarantee the validity of all
     * other tests. Assert that the Activity to test is not <code>null</code>
     * and its {@link EditText}s and "Ok" {@link Button} are disabled.
     */
    public void testPreConditions() {
        Assert.assertNotNull(this.activity);
        Assert.assertNotNull(this.solo);

        TableLayout tl = (TableLayout) this.activity.findViewById(R.id.tableLayout);
        for (int i = 0; i < tl.getChildCount(); i++) {
            TableRow row = (TableRow) tl.getChildAt(i);
            CheckBox cb = (CheckBox) row.getChildAt(CHECKBOX_INDEX);
            EditText input = (EditText) row.getChildAt(INPUT_INDEX);

            Assert.assertFalse(cb.isChecked());
            Assert.assertFalse(input.isEnabled());
        }

        View menuView = this.activity.findViewById(R.id.save_menu);
        Assert.assertNull(menuView);
    }

    /**
     * Test that the title of the {@link Activity} is correct.
     */
    public void testTitle() {
        String title = this.activity.getTitle().toString();
        String filterCardsTitle = this.activity
                .getString(R.string.filter_cards_title);

        Assert.assertTrue(title.contains(filterCardsTitle));
    }

    /**
     * Test individual {@link CheckBox} by clicking on it.
     * Upon click the corresponding {@link EditText} should
     * be enabled and should have focus.
     * @param checkId - the id of {@link CheckBox} to click
     * @param inputId - the id of {@link EditText} to test
     */
    private void testCheckBox(int checkId, int inputId) {
        CheckBox cb = (CheckBox) this.activity.findViewById(checkId);
        EditText input = (EditText) this.activity.findViewById(inputId);
        this.solo.clickOnView(cb);
        this.getInstrumentation().waitForIdleSync();
        Assert.assertTrue(input.isEnabled());
        Assert.assertTrue(input.hasFocus());

        View menuView = this.solo.getView(R.id.save_menu);
        Assert.assertNotNull(menuView);
    }

    /**
     * Test that "Brand" {@link EditText} is enabled and
     * has focus upon clicking on the corresponding {@link CheckBox}.
     */
    public void testBrandCheckBox() {
        this.testCheckBox(R.id.brand_check, R.id.brand_input);
    }

    /**
     * Test that "Year" {@link EditText} is enabled and
     * has focus upon clicking on the corresponding {@link CheckBox}.
     */
    public void testYearCheckBox() {
        this.testCheckBox(R.id.year_check, R.id.year_input);
    }

    /**
     * Test that "Number" {@link EditText} is enabled and
     * has focus upon clicking on the corresponding {@link CheckBox}.
     */
    public void testNumberCheckBox() {
        this.testCheckBox(R.id.number_check, R.id.number_input);
    }

    /**
     * Test that "Player Name" {@link EditText} is enabled and
     * has focus upon clicking on the corresponding {@link CheckBox}.
     */
    public void testPlayerNameCheckBox() {
        this.testCheckBox(R.id.player_name_check, R.id.player_name_input);
    }

    /**
     * Test that "Team" {@link EditText} is enabled and
     * has focus upon clicking on the corresponding {@link CheckBox}.
     */
    public void testTeamCheckBox() {
        this.testCheckBox(R.id.team_check, R.id.team_input);
    }

    /**
     * Test that the "Ok" {@link Button} and "Number"
     * {@link EditText} elements keep their state upon
     * changing activity orientation.
     */
    public void testSaveInstanceState() {
        this.testNumberCheckBox();
        this.solo.setActivityOrientation(Solo.LANDSCAPE);

        EditText numberInput = (EditText) this.activity.findViewById(R.id.number_input);
        Assert.assertTrue(numberInput.isEnabled());

//        Button okButton = (Button) this.activity.findViewById(R.id.ok_button);
//        Assert.assertTrue(okButton.isEnabled());
    }

    public void testNavigateUp() {
        ActionBar actionBar = ((ActionBarActivity) this.activity)
                .getSupportActionBar();
        Assert.assertTrue((actionBar.getDisplayOptions() & ActionBar.DISPLAY_HOME_AS_UP) > 0);
    }

    private Solo solo = null;
    private Activity activity = null;

    private static final int CHECKBOX_INDEX = 0;
    private static final int INPUT_INDEX = 2;
}
