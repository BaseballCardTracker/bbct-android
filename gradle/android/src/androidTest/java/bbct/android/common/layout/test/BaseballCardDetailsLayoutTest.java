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
package bbct.android.common.layout.test;

import android.test.ActivityInstrumentationTestCase2;
import android.widget.CheckBox;
import android.widget.EditText;
import bbct.android.common.R;
import bbct.android.common.activity.BaseballCardDetails;
import bbct.android.common.activity.FragmentTestActivity;
import butterknife.ButterKnife;
import butterknife.InjectView;

import static org.fest.assertions.api.ANDROID.assertThat;

public class BaseballCardDetailsLayoutTest extends ActivityInstrumentationTestCase2<FragmentTestActivity> {

    private BaseballCardDetails mFragment;
    @InjectView(R.id.autograph) CheckBox mAutographCheckBox;
    @InjectView(R.id.brand_text) EditText mBrandEditText;

    public BaseballCardDetailsLayoutTest() {
        super(FragmentTestActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        FragmentTestActivity activity = getActivity();
        mFragment = new BaseballCardDetails();
        activity.replaceFragment(mFragment);
        this.getInstrumentation().waitForIdleSync();
        ButterKnife.inject(this, activity);
    }

    public void testFragmentVisible() {
        assertThat(mFragment).isAdded().isVisible();
    }

    public void testAutographedCheckBox() {
        assertThat(mAutographCheckBox).isVisible().isNotChecked();
    }

    public void testBrandEditText() {
        assertThat(mBrandEditText).isVisible().hasFocus();
    }

}
