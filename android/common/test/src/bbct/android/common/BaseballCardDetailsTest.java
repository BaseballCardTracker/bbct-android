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
import android.test.ActivityInstrumentationTestCase2;
import android.test.UiThreadTest;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import bbct.common.data.BaseballCard;
import java.io.InputStream;
import java.util.List;
import junit.framework.Assert;

/**
 * Tests for {@link BaseballCardDetails}.
 *
 * TODO: Add tests for the layout of {@link BaseballCardDetails}
 *
 * @author codeguru <codeguru@users.sourceforge.net>
 */
public class BaseballCardDetailsTest extends ActivityInstrumentationTestCase2<BaseballCardDetails> {

    /**
     * Create instrumented test cases for {@link BaseballCardDetails}.
     */
    public BaseballCardDetailsTest() {
        super(BaseballCardDetails.class);
    }

    /**
     * Set up test fixture. This consists of an instance of the
     * {@link BaseballCardDetails} activity and all of its {@link EditText} and
     * {@link Button} views and a list of {@link BaseballCard} data.
     *
     * @throws Exception If an error occurs while chaining to the super class.
     */
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
    /**
     * Tear down the test fixture by calling
     * {@link BaseballCardDetails#finish()} and deleting the app's database.
     *
     * @throws Exception If an error occurs while chaining to the super class.
     */
    public void tearDown() throws Exception {
        this.activity.finish();
        this.dbUtil.deleteDatabase();

        super.tearDown();
    }

    /**
     * Assert that the Activity to test is not
     * <code>null</code>, that none of its expected {@link EditText} or
     * {@link Button} views are
     * <code>null</code>, and that the SQLite database was created.
     */
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

        BBCTTestUtil.assertDatabaseCreated(this.activity.getPackageName());
        Assert.assertTrue(this.dbUtil.isEmpty());
    }

    /**
     * Test that all text in the {@link EditText} views of a
     * {@link BaseballCardDetails} activity is preserved when the activity is
     * destroyed and the text is restored when the activity is restarted.
     */
    public void testStateDestroy() {
        BBCTTestUtil.sendKeysToCardDetails(this, this.activity, this.card);
        this.activity.finish();
        Assert.assertTrue(this.activity.isFinishing());
        this.activity = this.getActivity();
        BBCTTestUtil.assertAllEditTextContents(this.activity, this.card);
    }

    /**
     * Test that all text in the {@link EditText} views of a
     * {@link BaseballCardDetails} activity is preserved when the activity is
     * paused and the text is restored when the activity is restarted.
     */
    public void testStatePause() {
        BBCTTestUtil.sendKeysToCardDetails(this, this.activity, this.card);
        this.inst.callActivityOnRestart(this.activity);
        BBCTTestUtil.assertAllEditTextContents(this.activity, this.card);
    }

    /**
     * Test that baseball card data is correctly added to the database when it
     * is entered into the {@link BaseballCardDetails} activity.
     */
    public void testAddCard() {
        BBCTTestUtil.addCard(this, this.activity, this.card);
        this.inst.waitForIdleSync();
        Assert.assertTrue("Missing card: " + this.card, this.dbUtil.containsBaseballCard(card));
    }

    /**
     * Test that baseball card data for multiple cards is correctly added to the
     * database when it is entered into the {@link BaseballCardDetails}
     * activity. This test enteres all data using a single invocation of the
     * {@link BaseballCardDetails} activity.
     */
    public void testAddMultipleCards() {
        for (BaseballCard nextCard : this.allCards) {
            BBCTTestUtil.addCard(this, this.activity, nextCard);
        }

        this.inst.waitForIdleSync();
        for (BaseballCard nextCard : this.allCards) {
            Assert.assertTrue("Missing card: " + nextCard, this.dbUtil.containsBaseballCard(nextCard));
        }
    }

    /**
     * Test that the {@link BaseballCardDetails} activity finishes when the user
     * clicks the "Done" button.
     */
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
