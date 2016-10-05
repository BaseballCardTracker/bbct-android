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
import android.support.test.InstrumentationRegistry;
import android.support.test.rule.ActivityTestRule;
import android.widget.ListView;

import bbct.android.common.R;
import bbct.android.common.activity.BaseballCardList;
import bbct.android.common.activity.MainActivity;
import bbct.data.BaseballCard;
import bbct.android.common.test.BBCTTestUtil;
import bbct.android.common.test.BaseballCardCsvFileReader;
import bbct.android.common.test.DatabaseUtil;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.containsString;

/**
 * Tests for the {@link MainActivity} activity when the database does not
 * contain data.
 */
abstract public class BaseballCardListWithoutDataTest<T extends MainActivity> {
    @Rule
    public ActivityTestRule<T> activityTestRule;

    private static final String DATA_ASSET = "three_cards.csv";

    private Instrumentation inst = null;
    private Activity activity = null;
    private BaseballCardCsvFileReader cardInput = null;
    private DatabaseUtil dbUtil = null;

    /**
     * Create instrumented test cases for {@link MainActivity}.
     */
    public BaseballCardListWithoutDataTest(Class<T> activityClass) {
        activityTestRule = new ActivityTestRule<>(activityClass);
    }

    /**
     * Set up test fixture. This consists of an instance of the
     * {@link BaseballCardList} activity, its {@link ListView}, and a
     * {@link BaseballCardCsvFileReader} for sample baseball card data..
     *
     * @throws Exception If an error occurs while chaining to the super class.
     */
    @Before
    public void setUp() throws Exception {
        this.inst = InstrumentationRegistry.getInstrumentation();
        this.activity = activityTestRule.getActivity();

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
    @After
    public void tearDown() throws Exception {
        this.dbUtil.clearDatabase();
        this.cardInput.close();
    }

    /**
     * Check preconditions which must hold to guarantee the validity of all
     * other tests. Assert that the {@link Activity} to test and its
     * {@link ListView} are not <code>null</code>, that the {@link ListView} is
     * empty, and that the database was created and is empty.
     */
    @Test
    public void testPreConditions() {
        Assert.assertNotNull(this.activity);

        BBCTTestUtil.assertDatabaseCreated(this.inst.getTargetContext());
        Assert.assertTrue(this.dbUtil.isEmpty());
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
    @Test
    public void testAddCardToEmptyDatabase() throws Throwable {
        BaseballCard card = this.cardInput.getNextBaseballCard();

        BBCTTestUtil.addCard(card);
        // BBCTTestUtil.waitForToast(activity, BBCTTestUtil.ADD_MESSAGE);
        onView(withContentDescription(containsString("Navigate up"))).perform(click());

        Assert.assertTrue(this.dbUtil.containsBaseballCard(card));

        List<BaseballCard> cards = new ArrayList<>();
        cards.add(card);
        BBCTTestUtil.assertListViewContainsItems(cards);
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
    @Test
    public void testAddMultipleCards() throws Throwable {
        List<BaseballCard> cards = this.cardInput.getAllBaseballCards();

        for (BaseballCard card : cards) {
            BBCTTestUtil.addCard(card);
            // BBCTTestUtil.waitForToast(this.solo, BBCTTestUtil.ADD_MESSAGE);
        }

        onView(withContentDescription(containsString("Navigate up"))).perform(click());
        Assert.assertTrue(dbUtil.containsAllBaseballCards(cards));
        BBCTTestUtil.assertListViewContainsItems(cards);
    }
}
