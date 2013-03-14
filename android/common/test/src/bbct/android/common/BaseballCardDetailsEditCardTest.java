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
package bbct.android.common;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.EditText;
import bbct.common.data.BaseballCard;
import java.io.InputStream;
import junit.framework.Assert;

/**
 *
 * @author codeguru <codeguru@users.sourceforge.net>
 */
public class BaseballCardDetailsEditCardTest extends ActivityInstrumentationTestCase2<BaseballCardDetails> {

    public BaseballCardDetailsEditCardTest() {
        super(BaseballCardDetails.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();

        Instrumentation inst = this.getInstrumentation();
        InputStream in = inst.getContext().getAssets().open(BBCTTestUtil.CARD_DATA);
        BaseballCardCsvFileReader cardInput = new BaseballCardCsvFileReader(in, true);
        this.card = cardInput.getNextBaseballCard(); // Ken Griffey Jr.
        cardInput.close();

        // Must call getActivity() before creating a DatabaseUtil object to ensure that the database is created
        Context target = inst.getTargetContext();
        Intent intent = new Intent(target, BaseballCardDetails.class);
        intent.putExtra(target.getString(R.string.baseball_card_extra), this.card);
        this.setActivityIntent(intent);
        this.activity = this.getActivity();

        this.valueText = (EditText) this.activity.findViewById(R.id.value_text);
        this.countText = (EditText) this.activity.findViewById(R.id.count_text);

        this.dbUtil = new DatabaseUtil(this.activity.getPackageName());
    }

    @Override
    public void tearDown() throws Exception {
        this.activity.finish();
        this.dbUtil.deleteDatabase();

        super.tearDown();
    }

    public void testEditCard() {
        BBCTTestUtil.assertAllEditTextContents(this.activity, this.card);
        Assert.assertEquals(this.valueText, this.activity.getCurrentFocus());
        Assert.fail("Edit count and value");
    }
    private Activity activity = null;
    private EditText valueText = null;
    private EditText countText = null;
    private BaseballCard card = null;
    private DatabaseUtil dbUtil = null;
}
