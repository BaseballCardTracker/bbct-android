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

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import bbct.android.common.activity.MainActivity;
import bbct.android.common.test.BBCTTestUtil;
import bbct.android.common.test.BaseballCardCsvFileReader;
import bbct.android.common.test.DatabaseUtil;
import bbct.data.BaseballCard;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static org.hamcrest.Matchers.containsString;

abstract public class BaseballCardListWithoutDataTest<T extends MainActivity> {
    @Rule
    public ActivityTestRule<T> activityTestRule;

    private static final String DATA_ASSET = "three_cards.csv";

    private Instrumentation inst = null;
    private Activity activity = null;
    private BaseballCardCsvFileReader cardInput = null;
    private DatabaseUtil dbUtil = null;

    public BaseballCardListWithoutDataTest(Class<T> activityClass) {
        activityTestRule = new ActivityTestRule<>(activityClass);
    }

    @Before
    public void setUp() throws Exception {
        this.inst = InstrumentationRegistry.getInstrumentation();
        this.activity = activityTestRule.getActivity();

        InputStream cardInputStream = this.inst.getContext().getAssets()
                .open(DATA_ASSET);
        this.cardInput = new BaseballCardCsvFileReader(cardInputStream, true);
        this.dbUtil = new DatabaseUtil(this.inst.getTargetContext());
    }

    @After
    public void tearDown() throws Exception {
        this.dbUtil.clearDatabase();
        this.cardInput.close();
    }

    @Test
    public void testPreConditions() {
        Assert.assertNotNull(this.activity);

        BBCTTestUtil.assertDatabaseCreated(this.inst.getTargetContext());
        Assert.assertTrue(this.dbUtil.isEmpty());
    }

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
