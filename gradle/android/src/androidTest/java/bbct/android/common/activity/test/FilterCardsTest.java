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
import android.test.ActivityInstrumentationTestCase2;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import bbct.android.common.R;
import bbct.android.common.activity.FilterCards;
import bbct.android.common.activity.FragmentTestActivity;
import butterknife.ButterKnife;
import com.robotium.solo.Solo;
import junit.framework.Assert;

/**
 * Tests for {@link FilterCards} activity class.
 */
public class FilterCardsTest extends ActivityInstrumentationTestCase2<FragmentTestActivity> {

    /**
     * Create instrumented test cases for {@link FilterCards}.
     */
    public FilterCardsTest() {
        super(FragmentTestActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        this.getInstrumentation().setInTouchMode(true);
        this.activity = this.getActivity();
        this.activity.replaceFragment(new FilterCards());
        this.getInstrumentation().waitForIdleSync();
        this.solo = new Solo(this.getInstrumentation(), this.activity);
    }

    /**
     * Check preconditions which must hold to guarantee the validity of all
     * other tests. Assert that the Activity to test is not <code>null</code>
     * and its {@link EditText}s and "Ok" {@link Button} are disabled.
     */
    public void testPreConditions() {
        Assert.assertNotNull(this.activity);
        Assert.assertNotNull(this.solo);
        View menuView = ButterKnife.findById(this.activity, R.id.save_menu);
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
        CheckBox cb = ButterKnife.findById(this.activity, checkId);
        EditText input = ButterKnife.findById(this.activity, inputId);
        this.solo.clickOnView(cb);
        this.getInstrumentation().waitForIdleSync();
        Assert.assertTrue(input.isEnabled());
        Assert.assertTrue(input.hasFocus());

        Assert.assertTrue(this.solo.waitForView(R.id.save_menu));
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

        this.solo.waitForView(R.id.number_input);
        EditText numberInput = ButterKnife.findById(this.solo.getCurrentActivity(), R.id.number_input);
        Assert.assertTrue(numberInput.isEnabled());
    }

    private Solo solo = null;
    private FragmentTestActivity activity = null;

}
