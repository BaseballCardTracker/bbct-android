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
import android.widget.ListView;
import android.widget.TextView;
import bbct.android.common.R;
import bbct.android.common.activity.About;
import bbct.android.common.activity.BaseballCardDetails;
import bbct.android.common.activity.BaseballCardList;
import bbct.android.common.activity.FilterOptions;
import bbct.android.common.data.BaseballCard;
import bbct.android.common.test.BBCTTestUtil;
import bbct.android.common.test.BaseballCardCsvFileReader;
import bbct.android.common.test.DatabaseUtil;
import com.robotium.solo.Solo;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import junit.framework.Assert;

/**
 * Tests for the {@link BaseballCardList} activity when the database does not
 * contain data.
 */
public class BaseballCardListWithoutDataTest extends ActivityInstrumentationTestCase2<BaseballCardList> {

    /**
     * Create instrumented test cases for {@link BaseballCardList}.
     */
    public BaseballCardListWithoutDataTest() {
        super(BaseballCardList.class);
    }

    /**
     * Set up test fixture. This consists of an instance of the
     * {@link BaseballCardList} activity, its {@link ListView}, and a
     * {@link BaseballCardCsvFileReader} for sample baseball card data..
     *
     * @throws Exception If an error occurs while chaining to the super class.
     */
    @Override
    public void setUp() throws Exception {
        super.setUp();

        this.inst = this.getInstrumentation();

        this.activity = this.getActivity();
        this.listView = (ListView) this.activity.findViewById(android.R.id.list);

        this.solo = new Solo(this.inst, this.activity);

        InputStream cardInputStream = this.inst.getContext().getAssets().open(DATA_ASSET);
        this.cardInput = new BaseballCardCsvFileReader(cardInputStream, true);
        this.dbUtil = new DatabaseUtil(this.inst.getTargetContext());
    }

    /**
     * Tear down the test fixture by calling {@link Activity#finish()}, deleting
     * the database, and closing the {@link BaseballCardCsvFileReader}.
     *
     * @throws Exception If an error occurs while chaining to the super class.
     */
    @Override
    public void tearDown() throws Exception {
        this.dbUtil.clearDatabase();
        this.cardInput.close();
        this.solo.finishOpenedActivities();

        super.tearDown();
    }

    /**
     * Check preconditions which must hold to guarantee the validity of all
     * other tests. Assert that the {@link  Activity} to test and its
     * {@link ListView} are not
     * <code>null</code>, that the {@link ListView} is empty, and that the
     * database was created and is empty.
     */
    public void testPreConditions() {
        Assert.assertNotNull(this.activity);
        Assert.assertNotNull(this.listView);

        TextView emptyList = (TextView) this.activity.findViewById(android.R.id.empty);
        Assert.assertEquals(this.activity.getString(R.string.start), emptyList.getText());

        // Subtract 1 from the number of views owned by the ListView to account for the header View
        Assert.assertEquals(0, this.listView.getCount() - 1);

        BBCTTestUtil.assertDatabaseCreated(this.inst.getTargetContext());
        Assert.assertTrue(this.dbUtil.isEmpty());
    }

    /**
     * Test that the title of the {@link Activity} is correct.
     */
    public void testTitle() {
        String expectedTitle = this.activity.getString(R.string.app_name);

        Assert.assertEquals(expectedTitle, this.activity.getTitle());
    }

    /**
     * Test that the "Add Cards" menu item launches a
     * {@link BaseballCardDetails} activity.
     */
    public void testAddCardsMenuItem() {
        Activity cardDetails = BBCTTestUtil.testMenuItem(this.inst, this.activity, R.id.add_menu, BaseballCardDetails.class);

        cardDetails.finish();
        Assert.assertTrue(cardDetails.isFinishing());
    }

    /**
     * Test that the "Filter Cards" menu item launches a {@link FilterOptions}
     * activity.
     */
    public void testFilterCardsMenuItem() {
        Activity filterOptions = BBCTTestUtil.testMenuItem(this.inst, this.activity, R.id.filter_menu, FilterOptions.class);

        filterOptions.finish();
        Assert.assertTrue(filterOptions.isFinishing());
    }

    /**
     * Test that the "Delete Cards" menu item is disabled.
     * It should be disabled because there is no data currently
     * displayed in the list and therefore no rows are marked.
     */
    public void testDeleteCardsMenuItem() {
        Assert.assertFalse(this.inst.invokeMenuActionSync(this.activity, R.id.delete_menu, 0));
    }

    /**
     * Test that the "About" menu item launches a {@link About} activity.
     */
    public void testAboutMenuItem() {
        Activity about = BBCTTestUtil.testMenuItem(this.inst, this.activity, R.id.about_menu, About.class);

        about.finish();
        Assert.assertTrue(about.isFinishing());
    }

    /**
     * Test that the first baseball card data is added to the database and
     * updated in the {@link ListView}.
     *
     * @throws IOException If an error occurs while reading baseball card data
     * from the asset file.
     * @throws Throwable If an error occurs while the portion of the test on the
     * UI thread runs.
     */
    public void testAddCardToEmptyDatabase() throws IOException, Throwable {
        Activity cardDetails = BBCTTestUtil.testMenuItem(this.inst, this.activity, R.id.add_menu, BaseballCardDetails.class);
        BaseballCard card = this.cardInput.getNextBaseballCard();

        BBCTTestUtil.addCard(this.solo, card);
        BBCTTestUtil.waitForToast(this.solo, BBCTTestUtil.ADD_MESSAGE);
        this.solo.clickOnButton("Done");

        Assert.assertTrue(this.dbUtil.containsBaseballCard(card));

        List<BaseballCard> cards = new ArrayList<BaseballCard>();
        cards.add(card);
        BBCTTestUtil.assertListViewContainsItems(this.inst, cards, this.listView);
    }

    /**
     * Test that data for multiple baseball cards is added to the database and
     * updated in the {@link ListView}.
     *
     * @throws IOException If an error occurs while reading baseball card data
     * from the asset file.
     * @throws Throwable If an error occurs while the portion of the test on the
     * UI thread runs.
     */
    public void testAddMultipleCards() throws IOException, Throwable {
        BBCTTestUtil.testMenuItem(this.inst, this.activity, R.id.add_menu, BaseballCardDetails.class);
        List<BaseballCard> cards = this.cardInput.getAllBaseballCards();

        for (BaseballCard card : cards) {
            BBCTTestUtil.addCard(this.solo, card);
            BBCTTestUtil.waitForToast(this.solo, BBCTTestUtil.ADD_MESSAGE);
        }

        this.solo.clickOnButton("Done");
        BBCTTestUtil.assertListViewContainsItems(this.inst, cards, this.listView);
    }

    private Solo solo = null;
    private Instrumentation inst = null;
    private Activity activity = null;
    private BaseballCardCsvFileReader cardInput = null;
    private DatabaseUtil dbUtil = null;
    private ListView listView = null;
    private static final String DATA_ASSET = "cards.csv";
}
