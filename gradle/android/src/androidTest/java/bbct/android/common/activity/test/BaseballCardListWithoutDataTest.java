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
import bbct.android.common.R;
import bbct.android.common.activity.About;
import bbct.android.common.activity.BaseballCardList;
import bbct.android.common.activity.FragmentTags;
import bbct.android.common.activity.MainActivity;
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
 * Tests for the {@link MainActivity} activity when the database does not
 * contain data.
 */
public class BaseballCardListWithoutDataTest<T extends MainActivity> extends
        ActivityInstrumentationTestCase2<T> {

    /**
     * Create instrumented test cases for {@link MainActivity}.
     */
    public BaseballCardListWithoutDataTest(Class<T> activityClass) {
        super(activityClass);
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

        this.solo = new Solo(this.inst, this.activity);

        InputStream cardInputStream = this.inst.getContext().getAssets()
                .open(DATA_ASSET);
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
     * other tests. Assert that the {@link Activity} to test and its
     * {@link ListView} are not <code>null</code>, that the {@link ListView} is
     * empty, and that the database was created and is empty.
     */
    public void testPreConditions() {
        Assert.assertNotNull(this.activity);
        this.solo.waitForFragmentByTag(FragmentTags.EDIT_CARD);

        BBCTTestUtil.assertDatabaseCreated(this.inst.getTargetContext());
        Assert.assertTrue(this.dbUtil.isEmpty());
    }

    /**
     * Test that the "About" menu item displays the {@link About} fragment.
     */
    public void testAboutMenuItem() {
        BBCTTestUtil.testMenuItem(this.solo, R.id.about_menu, FragmentTags.ABOUT);
    }

    /**
     * Test that the first baseball card data is added to the database and
     * updated in the {@link ListView}.
     *
     * @throws IOException If an error occurs while reading baseball card data from the
     *                     asset file.
     * @throws Throwable   If an error occurs while the portion of the test on the UI
     *                     thread runs.
     */
    public void testAddCardToEmptyDatabase() throws Throwable {
        BaseballCard card = this.cardInput.getNextBaseballCard();

        BBCTTestUtil.addCard(this.solo, card);
        BBCTTestUtil.waitForToast(this.solo, BBCTTestUtil.ADD_MESSAGE);
        this.solo.clickOnActionBarHomeButton();

        Assert.assertTrue(this.dbUtil.containsBaseballCard(card));

        List<BaseballCard> cards = new ArrayList<BaseballCard>();
        cards.add(card);

        this.inst.waitForIdleSync();
        ListView listView = (ListView) this.activity.findViewById(android.R.id.list);
        Assert.assertNotNull("ListView not found", listView);
        BBCTTestUtil.assertListViewContainsItems(cards, listView);
    }

    /**
     * Test that data for multiple baseball cards is added to the database and
     * updated in the {@link ListView}.
     *
     * @throws IOException If an error occurs while reading baseball card data from the
     *                     asset file.
     * @throws Throwable   If an error occurs while the portion of the test on the UI
     *                     thread runs.
     */
    public void testAddMultipleCards() throws Throwable {
        BBCTTestUtil.testMenuItem(this.solo, R.id.add_menu, FragmentTags.EDIT_CARD);
        List<BaseballCard> cards = this.cardInput.getAllBaseballCards();

        for (BaseballCard card : cards) {
            BBCTTestUtil.addCard(this.solo, card);
            BBCTTestUtil.waitForToast(this.solo, BBCTTestUtil.ADD_MESSAGE);
        }

        this.solo.clickOnActionBarHomeButton();
        this.inst.waitForIdleSync();
        ListView listView = (ListView) this.activity.findViewById(android.R.id.list);
        BBCTTestUtil.assertListViewContainsItems(cards, listView);
    }

    private Solo solo = null;
    private Instrumentation inst = null;
    private Activity activity = null;
    private BaseballCardCsvFileReader cardInput = null;
    private DatabaseUtil dbUtil = null;
    private static final String DATA_ASSET = "cards.csv";
}
