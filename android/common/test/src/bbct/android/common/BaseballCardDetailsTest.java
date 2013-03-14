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
import android.content.Intent;
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import bbct.common.data.BaseballCard;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
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

        InputStream in = this.inst.getContext().getAssets().open(BBCTTestUtil.CARD_DATA);
        BaseballCardCsvFileReader cardInput = new BaseballCardCsvFileReader(in, true);
        this.allCards = cardInput.getAllBaseballCards();
        this.card = this.allCards.get(3); // Ken Griffey Jr.
        cardInput.close();

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

        this.dbUtil = new DatabaseUtil(this.activity.getPackageName());
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

        // TODO assert that database and ListView contain expected data
    }

    public void testStateDestroy() {
        BBCTTestUtil.sendKeysToCardDetails(this, this.activity, this.card);
        this.activity.finish();
        Assert.assertTrue(this.activity.isFinishing());
        this.activity = this.getActivity();
        BBCTTestUtil.assertAllEditTextContents(this.activity, this.card);
    }

    public void testStatePause() {
        BBCTTestUtil.sendKeysToCardDetails(this, this.activity, this.card);
        this.inst.callActivityOnRestart(this.activity);
        BBCTTestUtil.assertAllEditTextContents(this.activity, this.card);
    }

    public void testAddCard() {
        BBCTTestUtil.addCard(this, this.activity, this.card);
        this.inst.waitForIdleSync();
        Assert.assertTrue("Missing card: " + this.card, this.dbUtil.containsBaseballCard(card));
    }

    public void testAddMultipleCards() throws IOException {
        for (BaseballCard nextCard : this.allCards) {
            BBCTTestUtil.addCard(this, this.activity, nextCard);
        }

        this.inst.waitForIdleSync();
        for (BaseballCard nextCard : this.allCards) {
            Assert.assertTrue("Missing card: " + nextCard, this.dbUtil.containsBaseballCard(nextCard));
        }
    }

    public void testEditCard() {
        Assert.fail("Create a new test class for this test because the fixture is different");
        Intent intent = new Intent(this.inst.getTargetContext(), BaseballCardDetails.class);
        intent.putExtra(this.activity.getString(R.string.baseball_card_extra), card);
        this.activity = this.inst.startActivitySync(intent);
        this.assertAllEditTextContents(this.card);

        this.inst.runOnMainSync(new Runnable() {
            @Override
            public void run() {
                BaseballCardDetailsTest.this.countText.requestFocus();
            }
        });

        Assert.fail("Edit count and value");
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
    private List<BaseballCard> allCards = null;
    private BaseballCard card = null;
    private DatabaseUtil dbUtil = null;
}
