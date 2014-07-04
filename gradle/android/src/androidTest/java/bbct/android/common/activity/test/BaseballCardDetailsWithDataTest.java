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
package bbct.android.common.activity.test;

import android.widget.AutoCompleteTextView;
import bbct.android.common.R;
import bbct.android.common.activity.BaseballCardDetails;
import bbct.android.common.activity.FragmentTestActivity;
import bbct.android.common.data.BaseballCard;
import com.robotium.solo.Solo;
import junit.framework.Assert;

public class BaseballCardDetailsWithDataTest extends WithDataTest<FragmentTestActivity> {

    private Solo mSolo;
    private BaseballCard mCard;

    public BaseballCardDetailsWithDataTest() {
        super(FragmentTestActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        FragmentTestActivity activity = this.getActivity();
        activity.replaceFragment(new BaseballCardDetails());

        this.inst.waitForIdleSync();
        mSolo = new Solo(getInstrumentation(), activity);
        mSolo.getCurrentActivity();
        mCard = allCards.get(0);
    }

    @Override
    protected void tearDown() throws Exception {
        mSolo.finishOpenedActivities();

        super.tearDown();
    }

    public void testBrandAutoComplete() throws Throwable {
        AutoCompleteTextView brandText = (AutoCompleteTextView) mSolo.getCurrentActivity()
                .findViewById(R.id.brand_text);
        this.testAutoComplete(brandText, mCard.getBrand());
    }

    public void testPlayerNameAutoComplete() throws Throwable {
        AutoCompleteTextView playerNameText = (AutoCompleteTextView) mSolo.getCurrentActivity()
                .findViewById(R.id.player_name_text);
        this.testAutoComplete(playerNameText, mCard.getPlayerName());
    }

    public void testTeamAutoComplete() throws Throwable {
        AutoCompleteTextView teamText = (AutoCompleteTextView) mSolo.getCurrentActivity()
                .findViewById(R.id.team_text);
        this.testAutoComplete(teamText, mCard.getTeam());
    }

    private void testAutoComplete(AutoCompleteTextView textView, String text)
            throws Throwable {
        mSolo.typeText(textView, text.substring(0, 2));
        mSolo.waitForText(text);
        Assert.assertTrue(textView.isPopupShowing());
    }

}
