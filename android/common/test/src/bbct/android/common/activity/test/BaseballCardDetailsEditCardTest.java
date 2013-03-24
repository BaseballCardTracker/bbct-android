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
import android.content.Context;
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.view.KeyEvent;
import android.widget.EditText;
import bbct.android.common.R;
import bbct.android.common.activity.BaseballCardDetails;
import bbct.android.common.data.BaseballCard;
import bbct.android.common.test.BBCTTestUtil;
import bbct.android.common.test.BaseballCardCsvFileReader;
import bbct.android.common.test.DatabaseUtil;
import java.io.InputStream;
import junit.framework.Assert;

/**
 * Tests editing card value and count in a {@link BaseballCardDetails} activity.
 *
 * @author codeguru <codeguru@users.sourceforge.net>
 */
public class BaseballCardDetailsEditCardTest extends ActivityInstrumentationTestCase2<BaseballCardDetails> {

    /**
     * Create instrumented test cases for {@link BaseballCardDetails}.
     */
    public BaseballCardDetailsEditCardTest() {
        super(BaseballCardDetails.class);
    }

    /**
     * Set up test fixture. This consists of an instance of the
     * {@link BaseballCardDetails} activity and two {@link BaseballCard} objects
     * with the original data when first launching the activity and the new data
     * when editing.
     *
     * @throws Exception If an error occurs while chaining to the super class or
     * while reading data from the baseball card data asset file.
     */
    @Override
    public void setUp() throws Exception {
        super.setUp();

        this.inst = this.getInstrumentation();
        InputStream in = this.inst.getContext().getAssets().open(BBCTTestUtil.CARD_DATA);
        BaseballCardCsvFileReader cardInput = new BaseballCardCsvFileReader(in, true);
        this.card = cardInput.getNextBaseballCard(); // Ken Griffey Jr.
        cardInput.close();

        String brand = this.card.getBrand();
        int year = this.card.getYear();
        int number = this.card.getNumber();
        int newValue = this.card.getValue() + 50;
        int newCount = this.card.getCount() + 1;
        String name = this.card.getPlayerName();
        String position = this.card.getPlayerPosition();
        this.newCard = new BaseballCard(brand, year, number, newValue, newCount, name, position);

        // Must call getActivity() before creating a DatabaseUtil object to ensure that the database is created
        Context target = inst.getTargetContext();
        Intent intent = new Intent(target, BaseballCardDetails.class);
        intent.putExtra(target.getString(R.string.baseball_card_extra), this.card);
        this.setActivityIntent(intent);
        this.activity = this.getActivity();

        this.valueText = (EditText) this.activity.findViewById(R.id.value_text);

        this.dbUtil = new DatabaseUtil(this.activity.getPackageName());
    }

    /**
     * Tear down the test fixture by calling
     * {@link BaseballCardDetails#finish()} and deleting the app's database.
     *
     * @throws Exception If an error occurs while chaining to the super class.
     */
    @Override
    public void tearDown() throws Exception {
        this.activity.finish();
        this.dbUtil.deleteDatabase();

        super.tearDown();
    }

    /**
     * Test that the value and count field can be edited in the
     * {@link BaseballCardDetails} activity.
     */
    public void testEditCard() {
        BBCTTestUtil.assertAllEditTextContents(this.activity, this.card);
        Assert.assertEquals(this.valueText, this.activity.getCurrentFocus());
        String valueStr = String.format("%.2f", this.newCard.getValue() / 100.0);
        this.inst.sendStringSync(valueStr);
        inst.sendKeyDownUpSync(KeyEvent.KEYCODE_DPAD_DOWN);
        this.inst.sendStringSync(Integer.toString(this.newCard.getCount()));
        BBCTTestUtil.assertAllEditTextContents(this.activity, this.newCard);
    }
    private Instrumentation inst = null;
    private Activity activity = null;
    private EditText valueText = null;
    private BaseballCard card = null;
    private BaseballCard newCard = null;
    private DatabaseUtil dbUtil = null;
}
