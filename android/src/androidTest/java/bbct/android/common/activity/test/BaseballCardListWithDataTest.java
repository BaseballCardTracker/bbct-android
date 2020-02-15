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
import android.os.RemoteException;

import androidx.test.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.uiautomator.UiDevice;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import bbct.android.common.R;
import bbct.android.common.activity.FragmentTags;
import bbct.android.common.activity.MainActivity;
import bbct.android.common.database.BaseballCard;
import bbct.android.common.test.BBCTTestUtil;
import bbct.android.common.test.DatabaseUtil;
import bbct.android.common.test.rule.DataTestRule;
import bbct.android.common.view.BaseballCardView;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static bbct.android.common.test.matcher.BaseballCardMatchers.withYear;
import static bbct.android.common.test.matcher.Matchers.atPosition;
import static bbct.android.common.test.matcher.Matchers.first;
import static bbct.android.common.test.matcher.RecyclerViewMatcher.contains;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;

abstract public class BaseballCardListWithDataTest <T extends MainActivity> {
    @Rule
    public DataTestRule dataTestRule = new DataTestRule();
    @Rule
    public ActivityTestRule<T> activityTestRule;

    private static final String TAG = BaseballCardListWithDataTest.class.getName();

    private UiDevice device;
    private List<BaseballCard> expectedCards;
    private Instrumentation inst;
    private Activity activity;
    private List<BaseballCard> allCards;
    private BaseballCard newCard = null;
    private DatabaseUtil dbUtil;

    public BaseballCardListWithDataTest(Class<T> activityClass) {
        activityTestRule = new ActivityTestRule<>(activityClass);
    }

    @Before
    public void setUp() {
        inst = InstrumentationRegistry.getInstrumentation();
        device = UiDevice.getInstance(inst);
        activity = activityTestRule.getActivity();
        allCards = dataTestRule.getAllCards();
        newCard = new BaseballCard(true, "Mint", "Code Guru Apps", 1993,
                1, 50000, 1, "Code Guru", "Code Guru Devs", "Catcher");

        dbUtil = new DatabaseUtil(inst.getTargetContext());
    }

    @After
    public void tearDown() throws RemoteException {
        device.setOrientationNatural();
    }

    @Test
    public void testPreConditions() {
        Assert.assertNotNull(this.activity);
        BBCTTestUtil.assertDatabaseCreated(this.inst.getTargetContext());
        Assert.assertTrue(this.dbUtil.containsAllBaseballCards(this.allCards));
        BBCTTestUtil.assertListViewContainsItems(this.allCards);
    }

    @Test
    public void testAddCardsMenuItem() {
        BBCTTestUtil.testMenuItem(R.id.add_button, FragmentTags.EDIT_CARD);
    }

    @Test
    public void testFilterCardsMenuItem() {
        BBCTTestUtil.testMenuItem(R.id.filter_menu, FragmentTags.FILTER_CARDS);
    }

    @Test
    public void testStateDestroyWithoutFilter() throws RemoteException {
        device.setOrientationLeft();
        BBCTTestUtil.assertListViewContainsItems(allCards);
    }

    @Test
    public void testStateDestroyWithFilter() throws RemoteException {
        this.testYearFilter();
        device.setOrientationLeft();
        BBCTTestUtil.assertListViewContainsItems(expectedCards);
    }

    @Test
    public void testStateDestroyClearFilter() throws RemoteException {
        final int year = 1993;
        onView(withId(R.id.filter_menu)).perform(click());
        BBCTTestUtil.sendKeysToCurrFieldFilterCards(
            R.id.year_check,
            R.id.year_input,
            year + ""
        );
        onView(withId(R.id.confirm_button)).perform(click());
        onView(withId(R.id.clear_filter_menu)).perform(click());
        device.setOrientationLeft();
        onView(withId(R.id.card_list))
            .check(matches(contains(allCards)));
    }

    @Test
    public void testOnListItemClick() {
        int cardIndex = 3;
        BaseballCard expectedCard = allCards.get(cardIndex);
        onView(atPosition(cardIndex, instanceOf(BaseballCardView.class)))
            .perform(click());
        BBCTTestUtil.assertAllEditTextContents(expectedCard);
    }

    @Test
    public void testAddDuplicateCard() {
        BaseballCard card = dataTestRule.getCard(0);
        onView(withId(R.id.add_button)).perform(click());
        BBCTTestUtil.addCard(card);
        onView(withText(R.string.duplicate_card_title))
                .check(matches(isDisplayed()));
        onView(withText(R.string.duplicate_card_error))
                .check(matches(isDisplayed()));
        onView(withText(android.R.string.ok))
                .check(matches(isDisplayed()))
                .perform(click());
        onView(withText(R.string.duplicate_card_title))
                .check(doesNotExist());
        onView(withText(R.string.duplicate_card_error))
                .check(doesNotExist());
    }

    @Test
    public void testAddCardToPopulatedDatabase() {
        onView(withId(R.id.add_button)).perform(click());
        BBCTTestUtil.addCard(newCard);
        // BBCTTestUtil.waitForToast(BBCTTestUtil.ADD_MESSAGE);
        onView(withContentDescription(containsString("Navigate up"))).perform(click());
        expectedCards = new ArrayList<>(allCards);
        expectedCards.add(newCard);
        onView(withId(R.id.card_list))
            .check(matches(contains(expectedCards)));
    }

    @Test
    public void testAddCardMatchingCurrentFilter() {
        final int year = 1993;
        onView(withId(R.id.filter_menu)).perform(click());
        BBCTTestUtil.sendKeysToCurrFieldFilterCards(
            R.id.year_check,
            R.id.year_input,
            year + ""
        );
        onView(withId(R.id.confirm_button)).perform(click());
        onView(withId(R.id.add_button)).perform(click());
        BBCTTestUtil.addCard(newCard);
        // BBCTTestUtil.waitForToast(activity, BBCTTestUtil.ADD_MESSAGE);
        onView(withContentDescription(containsString("Navigate up")))
            .perform(click());
        expectedCards = BBCTTestUtil.filterList(allCards, withYear(year));
        expectedCards.add(newCard);
        onView(withId(R.id.card_list))
            .check(matches(contains(expectedCards)));
    }

    @Test
    public void testAddCardNotMatchingCurrentFilter() {
        final int year = 1993;
        newCard = new BaseballCard(
            false,
            "Excellent",
            "Codeguru Apps",
            1976,
            1,
            50000,
            1,
            "Codeguru",
            "Codeguru Devs",
            "Catcher"
        );
        onView(withId(R.id.filter_menu)).perform(click());
        BBCTTestUtil.sendKeysToCurrFieldFilterCards(
            R.id.year_check,
            R.id.year_input,
            year + ""
        );
        onView(withId(R.id.confirm_button)).perform(click());
        onView(withId(R.id.add_button)).perform(click());
        BBCTTestUtil.addCard(newCard);
        // BBCTTestUtil.waitForToast(activity, BBCTTestUtil.ADD_MESSAGE);
        onView(withContentDescription(containsString("Navigate up"))).perform(click());
        expectedCards = BBCTTestUtil.filterList(allCards, withYear(year));
        onView(withId(R.id.card_list))
            .check(matches(contains(expectedCards)));
    }

    @Test
    public void testAddCardAfterClearFilter() {
        final int year = 1993;
        onView(withId(R.id.filter_menu)).perform(click());
        BBCTTestUtil.sendKeysToCurrFieldFilterCards(
            R.id.year_check,
            R.id.year_input,
            "" + year
        );
        onView(withId(R.id.confirm_button)).perform(click());
        onView(withId(R.id.clear_filter_menu)).perform(click());
        onView(withId(R.id.add_button)).perform(click());
        BBCTTestUtil.addCard(newCard);
        // BBCTTestUtil.waitForToast(activity, BBCTTestUtil.ADD_MESSAGE);
        onView(withContentDescription(containsString("Navigate up"))).perform(click());
        expectedCards = new ArrayList<>(allCards);
        expectedCards.add(newCard);
        onView(withId(R.id.card_list))
            .check(matches(contains(expectedCards)));
    }

    @Test
    public void testDeleteAll() {
        onView(first(withId(R.id.checkmark))).perform(click());
        onView(withId(R.id.select_all_menu)).perform(click());
        onView(withId(R.id.delete_menu)).perform(click());
        // BBCTTestUtil.waitForToast(activity, BBCTTestUtil.DELETE_MESSAGE);
        expectedCards = new ArrayList<>();
        onView(withId(R.id.card_list)).check(matches(contains(expectedCards)));
    }

    @Test
    public void testDeleteCardUsingFilter() {
        int cardIndex = 0;
        final int year = 1993;
        expectedCards = BBCTTestUtil.filterList(allCards, withYear(year));
        expectedCards.remove(cardIndex);

        onView(withId(R.id.filter_menu)).perform(click());
        BBCTTestUtil.sendKeysToCurrFieldFilterCards(
            R.id.year_check,
            R.id.year_input,
            year + ""
        );
        onView(withId(R.id.confirm_button)).perform(click());
        onView(first(withId(R.id.checkmark)))
            .perform(click());
        onView(withId(R.id.delete_menu))
            .perform(click());
        // BBCTTestUtil.waitForToast(activity, BBCTTestUtil.DELETE_MESSAGE);
        onView(withId(R.id.card_list))
            .check(matches(contains(expectedCards)));
    }

    @Test
    public void testDeleteCardNoFilter() {
        int cardIndex = 0;
        expectedCards = new ArrayList<>(allCards);
        expectedCards.remove(cardIndex);
        onView(first(withId(R.id.checkmark)))
            .perform(click());
        onView(withId(R.id.delete_menu))
            .perform(click());
        // BBCTTestUtil.waitForToast(activity, BBCTTestUtil.DELETE_MESSAGE);
        onView(withId(R.id.card_list))
            .check(matches(contains(expectedCards)));
    }

    @Test
    public void testYearFilter() {
        final int year = 1993;
        onView(withId(R.id.filter_menu)).perform(click());
        BBCTTestUtil.sendKeysToCurrFieldFilterCards(
            R.id.year_check,
            R.id.year_input,
            year + ""
        );
        onView(withId(R.id.confirm_button)).perform(click());
        expectedCards = BBCTTestUtil.filterList(allCards, withYear(year));
        onView(withId(R.id.card_list))
            .check(matches(contains(expectedCards)));
        onView(withId(R.id.clear_filter_menu))
            .check(matches(isDisplayed()));
    }

    @Test
    public void testClearFilter() {
        final int year = 1993;
        onView(withId(R.id.filter_menu)).perform(click());
        BBCTTestUtil.sendKeysToCurrFieldFilterCards(
            R.id.year_check,
            R.id.year_input,
            year + ""
        );
        onView(withId(R.id.confirm_button)).perform(click());
        onView(withId(R.id.clear_filter_menu)).perform(click());
        onView(withId(R.id.card_list))
            .check(matches(contains(allCards)));
    }
}
