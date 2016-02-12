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

import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;
import android.widget.EditText;
import bbct.android.common.activity.BaseballCardDetails;
import bbct.android.common.activity.FragmentTestActivity;
import bbct.android.common.data.BaseballCard;
import bbct.android.common.test.BBCTTestUtil;
import bbct.android.common.test.BaseballCardCsvFileReader;
import bbct.android.common.test.DatabaseUtil;
import com.robotium.solo.Solo;
import java.io.InputStream;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * Tests editing card value and count in a {@link BaseballCardDetails} activity.
 */
public class BaseballCardDetailsEditCardTest extends
        ActivityInstrumentationTestCase2<FragmentTestActivity> {

    /**
     * Creates a {@link TestSuite} containing tests for every possible
     * combination of {@link EditText} views in the {@link BaseballCardDetails}
     * activity.
     *
     * @return A {@link TestSuite} containing tests for every possible
     *         combination of {@link EditText} views in the
     *         {@link BaseballCardDetails} activity.
     */
    public static Test suite() {
        Log.d(TAG, "suite()");

        TestSuite suite = new TestSuite();
        Set<BBCTTestUtil.EditTexts> editTexts = EnumSet.allOf(BBCTTestUtil.EditTexts.class);

        for (BBCTTestUtil.EditTexts editText : editTexts) {
            Set<BBCTTestUtil.EditTexts> mask = new HashSet<>();
            mask.add(editText);
            Log.d(TAG, "mask: " + mask);
            suite.addTest(new BaseballCardDetailsEditCardTest(mask));
        }

        return suite;
    }

    /**
     * Creates a test which will edit data in the {@link EditText} views
     * indicated by the given mask.
     *
     * @param inputMask
     *            The {@link EditText} views to edit.
     */
    public BaseballCardDetailsEditCardTest(Set<BBCTTestUtil.EditTexts> inputMask) {
        super(FragmentTestActivity.class);

        this.setName(TEST_NAME);
        this.inputMask = inputMask;
    }

    /**
     * Set up test fixture. This consists of an instance of the
     * {@link BaseballCardDetails} activity and two {@link BaseballCard} objects
     * with the original data when first launching the activity and the new data
     * when editing.
     *
     * @throws Exception
     *             If an error occurs while chaining to the super class or while
     *             reading data from the baseball card data asset file.
     */
    @Override
    public void setUp() throws Exception {
        Log.d(TAG, "setUp()");
        Log.d(TAG, "inputMask=" + this.inputMask);

        super.setUp();

        Instrumentation inst = this.getInstrumentation();
        InputStream in = inst.getContext().getAssets()
                .open(CARD_DATA);
        BaseballCardCsvFileReader cardInput = new BaseballCardCsvFileReader(in,
                true);
        List<BaseballCard> allCards = cardInput.getAllBaseballCards();
        this.oldCard = allCards.get(0); // Alex Fernandez
        this.newCard = allCards.get(4); // Dave Hollins
        cardInput.close();

        this.newCard.setValue(this.newCard.getValue() + 50);
        this.newCard.setCount(this.newCard.getCount() + 1);

        this.dbUtil = new DatabaseUtil(inst.getTargetContext());
        long cardId = this.dbUtil.insertBaseballCard(this.oldCard);

        Log.d(TAG, "cardId=" + cardId);

        if (cardId == -1) {
            Log.e(TAG, this.oldCard.toString());
        }

        inst.setInTouchMode(true);
        FragmentTestActivity activity = this.getActivity();
        BaseballCardDetails fragment = BaseballCardDetails.getInstance(cardId, this.oldCard);
        activity.replaceFragment(fragment);
        this.solo = new Solo(inst, activity);
    }

    /**
     * Tear down the test fixture by deleting the app's database.
     *
     * @throws Exception
     *             If an error occurs while chaining to the super class.
     */
    @Override
    public void tearDown() throws Exception {
        this.dbUtil.clearDatabase();

        super.tearDown();

        Log.d(TAG, "tearDown()");
    }

    /**
     * Test that the value and count field can be edited in the
     * {@link BaseballCardDetails} activity.
     *
     * @throws Throwable
     *             If an error occurs while the portion of the test on the UI
     *             thread runs.
     */
    public void testEditCard() throws Throwable {
        Log.d(TAG, "testEditCard()");

        Assert.assertTrue(this.dbUtil.containsBaseballCard(this.oldCard));

        BBCTTestUtil.assertAllEditTextContents(this.oldCard);
        BBCTTestUtil.sendKeysToCardDetails(this.solo, this.newCard,
                this.inputMask);

        BaseballCard expected = this.getExpectedCard();

        Log.d("DEBUG", "Checking cards for inputMask " + this.inputMask);
        BBCTTestUtil.assertAllEditTextContents(expected);
        Log.d("DEBUG", "Success!");

        this.solo.clickOnButton("Save");
        Assert.assertTrue(this.dbUtil.containsBaseballCard(expected));
    }

    private BaseballCard getExpectedCard() {
        boolean autographed = this.inputMask
                .contains(BBCTTestUtil.EditTexts.AUTOGRAPHED) ? this.newCard
                .isAutographed() : this.oldCard.isAutographed();
        String condition = this.inputMask
                .contains(BBCTTestUtil.EditTexts.CONDITION) ? this.newCard
                .getCondition() : this.oldCard.getCondition();
        String brand = this.inputMask.contains(BBCTTestUtil.EditTexts.BRAND) ? this.newCard
                .getBrand() : this.oldCard.getBrand();
        int year = this.inputMask.contains(BBCTTestUtil.EditTexts.YEAR) ? this.newCard
                .getYear() : this.oldCard.getYear();
        int number = this.inputMask.contains(BBCTTestUtil.EditTexts.NUMBER) ? this.newCard
                .getNumber() : this.oldCard.getNumber();
        int value = this.inputMask.contains(BBCTTestUtil.EditTexts.VALUE) ? this.newCard
                .getValue() : this.oldCard.getValue();
        int count = this.inputMask.contains(BBCTTestUtil.EditTexts.COUNT) ? this.newCard
                .getCount() : this.oldCard.getCount();
        String name = this.inputMask
                .contains(BBCTTestUtil.EditTexts.PLAYER_NAME) ? this.newCard
                .getPlayerName() : this.oldCard.getPlayerName();
        String team = this.inputMask.contains(BBCTTestUtil.EditTexts.TEAM) ? this.newCard
                .getTeam() : this.oldCard.getTeam();
        String position = this.inputMask
                .contains(BBCTTestUtil.EditTexts.PLAYER_POSITION) ? this.newCard
                .getPlayerPosition() : this.oldCard.getPlayerPosition();
        return new BaseballCard(autographed, condition, brand, year, number,
                value, count, name, team, position);
    }

    private final Set<BBCTTestUtil.EditTexts> inputMask;
    private Solo solo = null;
    private BaseballCard oldCard = null;
    private BaseballCard newCard = null;
    private DatabaseUtil dbUtil = null;

    private static final String TEST_NAME = "testEditCard";
    private static final String CARD_DATA = "cards.csv";
    private static final String TAG = BaseballCardDetailsEditCardTest.class
            .getName();
}
