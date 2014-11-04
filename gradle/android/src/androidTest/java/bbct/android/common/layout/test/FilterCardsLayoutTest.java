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
package bbct.android.common.layout.test;

import android.support.v4.app.Fragment;
import android.test.ActivityInstrumentationTestCase2;
import bbct.android.common.R;
import bbct.android.common.activity.FilterCards;
import bbct.android.common.activity.FragmentTestActivity;
import bbct.android.common.view.FilterOptionView;
import butterknife.ButterKnife;

import static org.fest.assertions.api.ANDROID.assertThat;

public class FilterCardsLayoutTest extends ActivityInstrumentationTestCase2<FragmentTestActivity> {

    private FragmentTestActivity mActivity;
    private Fragment mFragment;

    public FilterCardsLayoutTest() {
        super(FragmentTestActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        mActivity = getActivity();
        mFragment = new FilterCards();
        mActivity.replaceFragment(mFragment);
        this.getInstrumentation().waitForIdleSync();
    }

    public void testFragmentVisible() {
        assertThat(mFragment).isAdded().isVisible();
    }

    public void testBrandFilterOption() {
        testFilterOption(R.id.brand);
    }

    public void testYearFilterOption() {
        testFilterOption(R.id.year);
    }

    public void testNumberFilterOption() {
        testFilterOption(R.id.number);
    }

    public void testPlayerNameFilterOption() {
        testFilterOption(R.id.player_name);
    }

    public void testTeamFilterOption() {
        testFilterOption(R.id.team);
    }

    private void testFilterOption(int id) {
        FilterOptionView filterOption = ButterKnife.findById(mActivity, id);
        assertThat(filterOption).isVisible();
        // TODO: Write custom assertions for FilterOptionsView, including isNotChecked() and isChecked()
        // TODO: Also editTextIsEnabled() and editTextIsNotEnabled()
    }

}
