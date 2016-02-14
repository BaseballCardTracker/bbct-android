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
import android.support.test.espresso.Espresso;
import android.support.test.rule.ActivityTestRule;
import android.support.test.uiautomator.UiDevice;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.Checkable;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import bbct.android.common.R;
import bbct.android.common.activity.BaseballCardDetails;
import bbct.android.common.activity.BaseballCardList;
import bbct.android.common.activity.FilterCards;
import bbct.android.common.activity.FragmentTags;
import bbct.android.common.activity.MainActivity;
import bbct.android.common.data.BaseballCard;
import bbct.android.common.test.BBCTTestUtil;
import bbct.android.common.test.DataRule;
import bbct.android.common.test.DatabaseUtil;
import bbct.android.common.test.Predicate;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.robotium.solo.Solo;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import junit.framework.Assert;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.longClick;
import static android.support.test.espresso.assertion.ViewAssertions.doesNotExist;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isNotChecked;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

/**
 * Tests for the {@link MainActivity} activity when the database contains
 * data.
 */
abstract public class BaseballCardListWithDataTest <T extends MainActivity> {
    @Rule
    public DataRule dataRule = new DataRule();
    @Rule
    public ActivityTestRule<T> activityTestRule;

    private static final int SELECT_ALL = 0;
    private static final String TAG = BaseballCardListWithDataTest.class.getName();

    private List<BaseballCard> expectedCards;
    private Instrumentation inst;
    private Solo solo = null;
    private Activity activity = null;
    private List<BaseballCard> allCards;
    private BaseballCard newCard = null;
    private DatabaseUtil dbUtil;

    @InjectView(android.R.id.list) ListView listView;
    @InjectView(R.id.select_all) CheckBox selectAll;

    /**
     * Create instrumented test cases for {@link MainActivity}.
     */
    public BaseballCardListWithDataTest(Class<T> activityClass) {
        activityTestRule = new ActivityTestRule<>(activityClass);
    }

    /**
     * Set up test fixture. This consists of an instance of the
     * {@link BaseballCardList} activity, its {@link ListView}, and a populated
     * database.
     *
     * @throws Exception If an error occurs while chaining to the super class.
     */
    @Before
    public void setUp() throws Exception {
        inst = InstrumentationRegistry.getInstrumentation();
        this.activity = activityTestRule.getActivity();
        ButterKnife.inject(this, this.activity);
        allCards = dataRule.getAllCards();
        this.newCard = new BaseballCard(true, "Mint", "Code Guru Apps", 1993,
                1, 50000, 1, "Code Guru", "Code Guru Devs", "Catcher");

        this.dbUtil = new DatabaseUtil(this.inst.getTargetContext());
        this.solo = new Solo(this.inst, this.activity);
    }

    /**
     * Check preconditions which must hold to guarantee the validity of all
     * other tests. Assert that the {@link Activity} to test and its
     * {@link ListView} are not <code>null</code>, that the {@link ListView}
     * contains the expected data, and that the database was created with the
     * correct table and populated with the correct data.
     */
    @Test
    public void testPreConditions() {
        Assert.assertNotNull(this.activity);
        BBCTTestUtil.assertDatabaseCreated(this.inst.getTargetContext());
        Assert.assertTrue(this.dbUtil.containsAllBaseballCards(this.allCards));
        BBCTTestUtil.assertListViewContainsItems(this.allCards);
    }

    /**
     * Test that the "Add Cards" menu item launches a
     * {@link BaseballCardDetails} activity.
     */
    @Test
    public void testAddCardsMenuItem() {
        BBCTTestUtil.testMenuItem(R.id.add_menu, FragmentTags.EDIT_CARD);
    }

    /**
     * Test that the "Filter Cards" menu item launches a {@link FilterCards}
     * activity.
     */
    @Test
    public void testFilterCardsMenuItem() {
        BBCTTestUtil.testMenuItem(R.id.filter_menu, FragmentTags.FILTER_CARDS);
    }

    /**
     * Test the header view of the {@link ListView}.
     */
    @Test
    public void testHeader() {
        onView(withText(R.string.brand)).check(matches(isDisplayed()));
        onView(withText(R.string.year)).check(matches(isDisplayed()));
        onView(withText(R.string.number_col)).check(matches(isDisplayed()));
        onView(withText(R.string.player_name)).check(matches(isDisplayed()));
    }

    /**
     * Test that a {@link BaseballCardList} activity without an active filter
     * will be correctly restored after it is destroyed.
     */
    @Test
    public void testStateDestroyWithoutFilter() {
        this.activity.finish();
        Assert.assertTrue(this.activity.isFinishing());
        this.activity = activityTestRule.getActivity();
        BBCTTestUtil.assertListViewContainsItems(allCards);
    }

    /**
     * Test that a {@link BaseballCardList} activity with an active filter will
     * be correctly restored after it is destroyed.
     *
     * @throws Throwable If an error occurs while the portion of the test on the UI
     *                   thread runs.
     */
    @Test
    public void testStateDestroyWithFilter() throws Throwable {
        this.testYearFilter();
        this.activity.finish();
        Assert.assertTrue(this.activity.isFinishing());
        this.activity = activityTestRule.getActivity();
        BBCTTestUtil.assertListViewContainsItems(expectedCards);
    }

    /**
     * Test that a {@link BaseballCardList} activity without an active filter
     * will be correctly restored after it is destroyed. This tests differs from
     * {@link #testStateDestroyWithoutFilter()} because a filter is applied and
     * then cleared before the activity is destroyed.
     *
     * @throws Throwable If an error occurs while the portion of the test on the UI
     *                   thread runs.
     */
    @Test
    public void testStateDestroyClearFilter() throws Throwable {
        this.testClearFilter();
        this.activity.finish();
        Assert.assertTrue(this.activity.isFinishing());
        this.activity = activityTestRule.getActivity();
        BBCTTestUtil.assertListViewContainsItems(allCards);
    }

    /**
     * Test that a {@link BaseballCardList} activity without an active filter
     * will be correctly restored after it is paused.
     */
    @Test
    public void testStatePauseWithoutFilter() {
        this.inst.callActivityOnRestart(this.activity);
        BBCTTestUtil.assertListViewContainsItems(allCards);
    }

    /**
     * Test that a {@link BaseballCardList} activity with an active filter will
     * be correctly restored after it is paused.
     *
     * @throws Throwable If an error occurs while the portion of the test on the UI
     *                   thread runs.
     */
    @Test
    public void testStatePauseWithFilter() throws Throwable {
        this.testYearFilter();
        this.inst.callActivityOnRestart(this.activity);
        BBCTTestUtil.assertListViewContainsItems(expectedCards);
    }

    /**
     * Test that a {@link BaseballCardList} activity without an active filter
     * will be correctly restored after it is destroyed. This tests differs from
     * {@link #testStatePauseWithoutFilter()} because a filter is applied and
     * then cleared before the activity is paused.
     *
     * @throws Throwable If an error occurs while the portion of the test on the UI
     *                   thread runs.
     */
    @Test
    public void testStatePauseClearFilter() throws Throwable {
        this.testClearFilter();
        this.inst.callActivityOnRestart(this.activity);
        BBCTTestUtil.assertListViewContainsItems(this.allCards);
    }

    /**
     * Test that, when the user clicks on an item in the {@link ListView} of the
     * {@link BaseballCardList} activity, a {@link BaseballCardDetails} activity
     * is launched with its {@link EditText} views populated with the correct
     * data.
     *
     * @throws Throwable If an error occurs while the portion of the test on the UI
     *                   thread runs.
     */
    @Test
    public void testOnListItemClick() throws Throwable {
        Log.d(TAG, "testOnListItemClick()");
        int cardIndex = 3;
        BaseballCard expectedCard = allCards.get(cardIndex);
        onData(allOf(instanceOf(BaseballCard.class), is(expectedCard))).perform(click());
        BBCTTestUtil.assertAllEditTextContents(expectedCard);
    }

    /**
     * Test that an error message is displayed when the user attempts to add
     * baseball card data which duplicates data already in the database.
     *
     * @throws IOException If an error occurs while reading baseball card data from an
     *                     asset file.
     * @throws Throwable   If an error occurs while the portion of the test on the UI
     *                     thread runs.
     */
    @Test
    public void testAddDuplicateCard() throws Throwable {
        BaseballCard card = dataRule.getCard(0);
        BBCTTestUtil.testMenuItem(R.id.add_menu, FragmentTags.EDIT_CARD);
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

    /**
     * Test that baseball card data is correctly added to the database when it
     * already contains data for other cards.
     *
     * @throws Throwable If an error occurs while the portion of the test on the UI
     *                   thread runs.
     */
    @Test
    public void testAddCardToPopulatedDatabase() throws Throwable {
        BBCTTestUtil.testMenuItem(R.id.add_menu, FragmentTags.EDIT_CARD);
        BBCTTestUtil.addCard(newCard);
        // BBCTTestUtil.waitForToast(BBCTTestUtil.ADD_MESSAGE);
        onView(withContentDescription(containsString("Navigate up"))).perform(click());
        allCards.add(newCard);
        BBCTTestUtil.assertListViewContainsItems(allCards);
    }

    /**
     * Test that the {@link ListView} is updated when the user adds a new card
     * which matches the current filter.
     *
     * @throws Throwable If an error occurs while the portion of the test on the UI
     *                   thread runs.
     */
    @Test
    public void testAddCardMatchingCurrentFilter() throws Throwable {
        testYearFilter();
        BBCTTestUtil.testMenuItem(R.id.add_menu, FragmentTags.EDIT_CARD);
        BBCTTestUtil.addCard(newCard);
        // BBCTTestUtil.waitForToast(activity, BBCTTestUtil.ADD_MESSAGE);
        onView(withContentDescription(containsString("Navigate up"))).perform(click());
        expectedCards.add(newCard);
        BBCTTestUtil.assertListViewContainsItems(expectedCards);
    }

    /**
     * Test that the {@link ListView} is not updated when the user adds a new
     * card which does not match the current filter.
     *
     * @throws Throwable If an error occurs while the portion of the test on the UI
     *                   thread runs.
     */
    @Test
    public void testAddCardNotMatchingCurrentFilter() throws Throwable {
        testYearFilter();
        newCard = new BaseballCard(false, "Excellent", "Codeguru Apps",
                1976, 1, 50000, 1, "Codeguru", "Codeguru Devs", "Catcher");
        BBCTTestUtil.testMenuItem(R.id.add_menu, FragmentTags.EDIT_CARD);
        BBCTTestUtil.addCard(newCard);
        // BBCTTestUtil.waitForToast(activity, BBCTTestUtil.ADD_MESSAGE);
        onView(withContentDescription(containsString("Navigate up"))).perform(click());
        BBCTTestUtil.assertListViewContainsItems(expectedCards);
    }

    /**
     * Test that the {@link ListView} is updated when the user adds a new card
     * after an active filter was cleared.
     *
     * @throws Throwable If an error occurs while the portion of the test on the UI
     *                   thread runs.
     */
    @Test
    public void testAddCardAfterClearFilter() throws Throwable {
        testClearFilter();
        BBCTTestUtil.testMenuItem(R.id.add_menu, FragmentTags.EDIT_CARD);
        BBCTTestUtil.addCard(newCard);
        // BBCTTestUtil.waitForToast(activity, BBCTTestUtil.ADD_MESSAGE);
        onView(withContentDescription(containsString("Navigate up"))).perform(click());
        allCards.add(newCard);
        BBCTTestUtil.assertListViewContainsItems(allCards);
    }

    /**
     * Test that upon clicking on header {@link View}, all items in
     * {@link ListView} are selected.
     */
    @Test
    public void testMarkAll() {
        this.markAll();
        this.assertAllCheckboxesChecked();
    }

    private void assertAllCheckboxesChecked() {
        onView(withId(R.id.select_all))
                .check(matches(isChecked()));

        for (int i = 0; i < allCards.size(); i++) {
            onData(instanceOf(BaseballCard.class))
                    .atPosition(i)
                    .check(matches(isChecked()));
        }
    }

    private void markAll() {
        onView(withId(R.id.select_all))
                .perform(click())
                .check(matches(isChecked()));
        onView(withId(R.id.delete_menu))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testDeleteAll() throws Throwable {
        this.markAll();
        deleteCards();
        onView(withId(android.R.id.empty)).check(matches(isDisplayed()));
    }

    @Test
    public void testUnmarkAll() throws Throwable {
        this.markAll();
        onView(withId(R.id.select_all)).perform(click());
        assertNoCheckboxesChecked();
    }

    private void assertNoCheckboxesChecked() {
        onView(withId(R.id.select_all))
                .check(matches(isNotChecked()));

        for (int i = 0; i < allCards.size(); i++) {
            onData(instanceOf(BaseballCard.class))
                    .atPosition(i)
                    .check(matches(isNotChecked()));
        }
    }

    /**
     * Test that the {@link ListView} displays updated card list, when user
     * deletes cards with applied filter.
     */
    @Test
    public void testDeleteCardUsingFilter() throws Throwable {
        testYearFilter();

        int cardIndex = 0;
        final int year = 1993;
        Predicate<BaseballCard> yearPred = new Predicate<BaseballCard>() {
            @Override
            public boolean doTest(BaseballCard card) {
                return card.getYear() == year;
            }
        };
        expectedCards = BBCTTestUtil.filterList(allCards, yearPred);
        expectedCards.remove(cardIndex);

        onData(instanceOf(BaseballCard.class))
                .atPosition(cardIndex)
                .onChildView(withId(R.id.checkmark))
                .perform(click());
        onView(withId(R.id.delete_menu))
                .check(matches(isDisplayed()));
        deleteCards();
        BBCTTestUtil.assertListViewContainsItems(expectedCards);
    }

    private void deleteCards() {
        onView(withId(R.id.delete_menu)).perform(click());
        // BBCTTestUtil.waitForToast(activity, BBCTTestUtil.DELETE_MESSAGE);
        onView(withId(R.id.add_menu)).check(matches(isDisplayed()));
    }

    /**
     * Test that the {@link ListView} displays updated card list, when user
     * deletes cards without any applied filter.
     */
    @Test
    public void testDeleteCardNoFilter() throws Throwable {
        int cardIndex = 0;

        expectedCards = new ArrayList<>(allCards);
        expectedCards.remove(cardIndex);

        onData(instanceOf(BaseballCard.class))
                .atPosition(cardIndex)
                .onChildView(withId(R.id.checkmark))
                .perform(click());
        onView(withId(R.id.delete_menu))
                .check(matches(isDisplayed()));
        deleteCards();
        BBCTTestUtil.assertListViewContainsItems(expectedCards);
    }

    /**
     * Test that the state of {@link CheckedTextView} is maintained when the
     * {@link BaseballCardList} activity changes orientation.
     */
    @Test
    public void testSelectionAfterSaveInstanceState() throws Throwable {
        Log.d(TAG, "testSelectionAfterSaveInstanceState()");

        int index = 1;
        onData(instanceOf(BaseballCard.class))
                .atPosition(index)
                .onChildView(withId(R.id.checkmark))
                .perform(click());
        onView(withId(R.id.delete_menu))
                .check(matches(isDisplayed()));

        Log.d(TAG, "change orientation");
        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());
        device.setOrientationLeft();

        Log.d(TAG, "assertions");
        onData(instanceOf(BaseballCard.class))
                .atPosition(index)
                .onChildView(withId(R.id.checkmark))
                .check(matches(isChecked()));

        device.setOrientationNatural();
    }

    /**
     * Test that the {@link ListView} displays the correct cards when filtered
     * by the card year.
     */
    @Test
    public void testYearFilter() {
        final int year = 1993;

        Predicate<BaseballCard> yearPred = new Predicate<BaseballCard>() {
            @Override
            public boolean doTest(BaseballCard card) {
                return card.getYear() == year;
            }
        };

        this.testSingleFilter(R.id.year_check, R.id.year_input, year + "", yearPred);
    }

    /**
     * Test that all cards are displayed after a filter is cleared.
     */
    @Test
    public void testClearFilter() {
        this.testYearFilter();
        onView(withId(R.id.clear_filter_menu))
                .perform(click())
                .check(doesNotExist());
        onView(withId(R.id.add_menu))
                .check(matches(isDisplayed()));
        BBCTTestUtil.assertListViewContainsItems(allCards);
    }

    /**
     * Test a filter using a single parameter.
     *
     * @param checkId    - the id of the {@link CheckBox} to activate.
     * @param editId     - the id of the {@link EditText} to input text into.
     * @param input      - the input to use for filtering.
     * @param filterPred - @see {@link Predicate}.
     */
    private void testSingleFilter(int checkId, int editId, String input,
                                  Predicate<BaseballCard> filterPred) {
        BBCTTestUtil.testMenuItem(R.id.filter_menu, FragmentTags.FILTER_CARDS);

        BBCTTestUtil.sendKeysToCurrFieldFilterCards(checkId, editId, input);
        onView(withId(R.id.save_menu)).perform(click());
        expectedCards = BBCTTestUtil.filterList(allCards, filterPred);
        BBCTTestUtil.assertListViewContainsItems(this.expectedCards);
        onView(withId(R.id.clear_filter_menu)).check(matches(isDisplayed()));
    }

    @Test
    public void testOnClickCheckboxStartActionMode() {
        int index = 4;
        this.solo.clickOnCheckBox(index);
        Assert.assertTrue(this.solo.waitForView(R.id.delete_menu));
    }

    @Test
    public void testOnClickCheckboxStopActionMode() {
        this.testOnClickCheckboxStartActionMode();

        int index = 4;
        this.solo.clickOnCheckBox(index);
        Assert.assertTrue(this.solo.waitForView(R.id.add_menu));
        View addMenu = ButterKnife.findById(this.activity, R.id.add_menu);
        Assert.assertNotNull(addMenu);
        Assert.assertTrue(addMenu.isShown());
    }

    @Test
    public void testOnClickCheckboxAll() {
        for(int i = 1; i <= 7; ++i) {
            this.solo.clickOnCheckBox(i);
        }

        this.inst.waitForIdleSync();
        Assert.assertTrue(selectAll.isChecked());
    }

    @Test
    public void testOnCheckAllAndOnClickCheckbox() {
        this.solo.clickOnCheckBox(SELECT_ALL);
        this.solo.clickOnCheckBox(1);

        this.inst.waitForIdleSync();
        Assert.assertFalse(selectAll.isChecked());
    }

    @Test
    public void testOnClickCheckboxAndOnCheckAll() {
        this.solo.clickOnCheckBox(1);
        this.solo.clickOnCheckBox(SELECT_ALL);
        this.assertAllCheckboxesChecked();
    }

    @Test
    public void testOnItemLongClickStartActionMode() {
        int index = 4;
        onData(instanceOf(BaseballCard.class))
                .atPosition(index)
                .perform(longClick());
        onData(instanceOf(BaseballCard.class))
                .atPosition(index)
                .onChildView(withId(R.id.checkmark))
                .check(matches(isChecked()));
        onView(withId(R.id.delete_menu))
                .check(matches(isDisplayed()));
    }

    @Test
    public void testOnClickDoneButton() {
        this.testMarkAll();
        onView(withId(R.id.action_mode_close_button)).perform(click());
        onView(withId(R.id.add_menu)).check(matches(isDisplayed()));
        onView(withId(R.id.select_all)).check(matches(isNotChecked()));
    }

}
