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
import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import bbct.android.common.R;
import bbct.android.common.activity.BaseballCardDetails;
import bbct.android.common.data.BaseballCard;
import bbct.android.common.test.BBCTTestUtil;
import bbct.android.common.test.BaseballCardCsvFileReader;
import bbct.android.common.test.DatabaseUtil;
import com.robotium.solo.Solo;
import java.io.InputStream;
import java.util.List;
import junit.framework.Assert;

/**
 * Tests for {@link BaseballCardDetails}.
 */
public class BaseballCardDetailsAddCardsTest extends
        ActivityInstrumentationTestCase2<BaseballCardDetails> {

    /**
     * Create instrumented test cases for {@link BaseballCardDetails}.
     */
    public BaseballCardDetailsAddCardsTest() {
        super(BaseballCardDetails.class);
    }

    /**
     * Set up test fixture. This consists of an instance of the
     * {@link BaseballCardDetails} activity and all of its {@link EditText} and
     * {@link Button} views and a list of {@link BaseballCard} data.
     *
     * @throws Exception
     *             If an error occurs while chaining to the super class.
     */
    @Override
    public void setUp() throws Exception {
        super.setUp();

        this.inst = this.getInstrumentation();

        InputStream in = this.inst.getContext().getAssets()
                .open(BBCTTestUtil.CARD_DATA);
        BaseballCardCsvFileReader cardInput = new BaseballCardCsvFileReader(in,
                true);
        this.allCards = cardInput.getAllBaseballCards();
        this.card = this.allCards.get(3); // Ken Griffey Jr.
        cardInput.close();

        this.activity = this.getActivity();

        this.solo = new Solo(this.inst, this.activity);
    }

    /**
     * Tear down the test fixture by calling {@link Activity#finish()} and
     * deleting the app's database.
     *
     * @throws Exception
     *             If an error occurs while chaining to the super class.
     */
    @Override
    public void tearDown() throws Exception {
        DatabaseUtil dbUtil = new DatabaseUtil(this.inst.getTargetContext());
        dbUtil.clearDatabase();

        super.tearDown();
    }

    /**
     * Test that baseball card data is correctly added to the database when it
     * is entered into the {@link BaseballCardDetails} activity.
     *
     * @throws Throwable
     *             If an error occurs while the portion of the test on the UI
     *             thread runs.
     */
    public void testAddCard() throws Throwable {
        BBCTTestUtil.addCard(this.solo, this.card);
        BBCTTestUtil.waitForToast(this.solo, BBCTTestUtil.ADD_MESSAGE);
        DatabaseUtil dbUtil = new DatabaseUtil(this.inst.getTargetContext());
        Assert.assertTrue("Missing card: " + this.card,
                dbUtil.containsBaseballCard(this.card));
    }

    /**
     * Test that baseball card data for multiple cards is correctly added to the
     * database when it is entered into the {@link BaseballCardDetails}
     * activity. This test enters all data using a single invocation of the
     * {@link BaseballCardDetails} activity.
     *
     * @throws Throwable
     *             If an error occurs while the portion of the test on the UI
     *             thread runs.
     */
    public void testAddMultipleCards() throws Throwable {
        for (BaseballCard nextCard : this.allCards) {
            BBCTTestUtil.addCard(this.solo, nextCard);
            BBCTTestUtil.waitForToast(this.solo, BBCTTestUtil.ADD_MESSAGE);
        }

        DatabaseUtil dbUtil = new DatabaseUtil(this.inst.getTargetContext());
        for (BaseballCard nextCard : this.allCards) {
            Assert.assertTrue("Missing card: " + nextCard,
                    dbUtil.containsBaseballCard(nextCard));
        }
    }

    public void testBrandAutoComplete() throws Throwable {
        AutoCompleteTextView brandText = (AutoCompleteTextView) this.activity.findViewById(R.id.brand_text);
        this.testAutoComplete(brandText, this.card.getBrand());
    }

    private void testAutoComplete(AutoCompleteTextView textView, String text)
            throws Throwable {
        BBCTTestUtil.addCard(this.solo, this.card);
        this.solo.typeText(textView, text.substring(0, 2));
        Assert.assertTrue(textView.isPopupShowing());
    }

    private Solo solo = null;
    private Activity activity = null;
    private Instrumentation inst = null;
    private List<BaseballCard> allCards = null;
    private BaseballCard card = null;
}
