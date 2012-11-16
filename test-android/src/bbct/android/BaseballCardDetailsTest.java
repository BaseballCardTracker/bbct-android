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
package bbct.android;

import android.app.Activity;
import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import bbct.common.data.BaseballCard;
import java.io.InputStream;
import junit.framework.Assert;

/**
 *
 * @author codeguru <codeguru@users.sourceforge.net>
 */
public class BaseballCardDetailsTest extends ActivityInstrumentationTestCase2<BaseballCardDetails> {

    public BaseballCardDetailsTest() {
        super(BaseballCardDetails.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();

        this.inst = this.getInstrumentation();

        InputStream in = this.inst.getContext().getAssets().open(CARD_DATA);
        this.cardInput = new BaseballCardCsvFileReader(in, true);
        this.card = this.cardInput.getNextBaseballCard();

        // Must call getActivity() before creating a DatabaseUtil object to ensure that the database is created
        this.activity = this.getActivity();
        this.brandText = (EditText) this.activity.findViewById(R.id.brand_text);
        this.yearText = (EditText) this.activity.findViewById(R.id.year_text);
        this.numberText = (EditText) this.activity.findViewById(R.id.number_text);
        this.valueText = (EditText) this.activity.findViewById(R.id.value_text);
        this.countText = (EditText) this.activity.findViewById(R.id.count_text);
        this.playerNameText = (EditText) this.activity.findViewById(R.id.player_name_text);
        this.playerPositionSpinner = (Spinner) this.activity.findViewById(R.id.player_position_text);
        this.saveButton = (Button) this.activity.findViewById(R.id.save_button);
        this.doneButton = (Button) this.activity.findViewById(R.id.done_button);

        this.dbUtil = new DatabaseUtil();
    }

    @Override
    public void tearDown() throws Exception {
        this.activity.finish();
        this.dbUtil.deleteDatabase();

        super.tearDown();
    }

    public void testPreConditions() {
        Assert.assertNotNull(this.activity);
        Assert.assertNotNull(this.brandText);
        Assert.assertNotNull(this.yearText);
        Assert.assertNotNull(this.numberText);
        Assert.assertNotNull(this.valueText);
        Assert.assertNotNull(this.countText);
        Assert.assertNotNull(this.playerNameText);
        Assert.assertNotNull(this.playerPositionSpinner);
        Assert.assertNotNull(this.saveButton);
        Assert.assertNotNull(this.doneButton);
        Assert.assertNotNull(this.dbUtil.getDatabase());
    }

    public void testStateDestroy() {
        BBCTTestUtil.sendKeysToCardDetails(this, this.activity, this.card);
        this.activity.finish();
        Assert.assertTrue(this.activity.isFinishing());
        this.activity = this.getActivity();

        Assert.assertEquals(this.card.getBrand(), this.brandText.getText().toString());
        Assert.assertEquals(this.card.getYear(), Integer.parseInt(this.yearText.getText().toString()));
        Assert.assertEquals(this.card.getNumber(), Integer.parseInt(this.numberText.getText().toString()));
        Assert.assertEquals(this.card.getValue(), (int) (Double.parseDouble(this.valueText.getText().toString()) * 100));
        Assert.assertEquals(this.card.getCount(), Integer.parseInt(this.countText.getText().toString()));
        Assert.assertEquals(this.card.getPlayerName(), this.playerNameText.getText().toString());
        Assert.assertEquals(this.card.getPlayerPosition(), this.playerPositionSpinner.getSelectedItem());
    }

    public void testStatePause() {
        BBCTTestUtil.sendKeysToCardDetails(this, this.activity, this.card);
        this.inst.callActivityOnRestart(this.activity);

        Assert.assertEquals(this.card.getBrand(), this.brandText.getText().toString());
        Assert.assertEquals(this.card.getYear(), Integer.parseInt(this.yearText.getText().toString()));
        Assert.assertEquals(this.card.getNumber(), Integer.parseInt(this.numberText.getText().toString()));
        Assert.assertEquals(this.card.getValue(), (int) (Double.parseDouble(this.valueText.getText().toString()) * 100));
        Assert.assertEquals(this.card.getCount(), Integer.parseInt(this.countText.getText().toString()));
        Assert.assertEquals(this.card.getPlayerName(), this.playerNameText.getText().toString());
        Assert.assertEquals(this.card.getPlayerPosition(), this.playerPositionSpinner.getSelectedItem());
    }

    public void testAddCard() {
        BBCTTestUtil.addCard(this, this.activity, this.card);
        this.inst.waitForIdleSync();
        Assert.assertTrue(this.dbUtil.containsBaseballCard(card));
    }

    public void testEditCard() {
        Assert.fail("Implement me!");
    }

    public void testNoBrand() {
        Assert.fail("Implement me!");
    }

    public void testNoYear() {
        Assert.fail("Implement me!");
    }

    public void testNoNumber() {
        Assert.fail("Implement me!");
    }

    public void testNoValue() {
        Assert.fail("Implement me!");
    }

    public void testNoCount() {
        Assert.fail("Implement me!");
    }

    public void testNoPlayerName() {
        Assert.fail("Implement me!");
    }

    @UiThreadTest
    public void testDoneButtonOnClick() {
        Assert.assertTrue(this.doneButton.performClick());
        Assert.assertTrue(this.activity.isFinishing());
    }
    private Activity activity = null;
    private EditText brandText = null;
    private EditText yearText = null;
    private EditText numberText = null;
    private EditText valueText = null;
    private EditText countText = null;
    private EditText playerNameText = null;
    private Spinner playerPositionSpinner = null;
    private Button saveButton = null;
    private Button doneButton = null;
    private Instrumentation inst = null;
    private BaseballCardCsvFileReader cardInput = null;
    private BaseballCard card = null;
    private DatabaseUtil dbUtil = null;
    private static final String CARD_DATA = "cards.csv";
}
