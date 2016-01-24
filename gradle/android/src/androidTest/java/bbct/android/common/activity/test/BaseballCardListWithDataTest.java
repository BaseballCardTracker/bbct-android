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
import android.test.TouchUtils;
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
import bbct.android.common.test.BaseballCardCsvFileReader;
import bbct.android.common.test.Predicate;
import butterknife.ButterKnife;
import butterknife.InjectView;
import com.robotium.solo.Solo;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import junit.framework.Assert;

/**
 * Tests for the {@link MainActivity} activity when the database contains
 * data.
 */
abstract public class BaseballCardListWithDataTest <T extends MainActivity> extends
        WithDataTest<T> {

    private static final int SELECT_ALL = 0;
    private static final String TAG = BaseballCardListWithDataTest.class.getName();

    private List<BaseballCard> expectedCards;
    private Solo solo = null;
    private Activity activity = null;
    private BaseballCard newCard = null;

    @InjectView(android.R.id.list) ListView listView;
    @InjectView(R.id.select_all) CheckBox selectAll;

    /**
     * Create instrumented test cases for {@link MainActivity}.
     */
    public BaseballCardListWithDataTest(Class<T> activityClass) {
        super(activityClass);
    }

    /**
     * Set up test fixture. This consists of an instance of the
     * {@link BaseballCardList} activity, its {@link ListView}, and a populated
     * database.
     *
     * @throws Exception If an error occurs while chaining to the super class.
     */
    @Override
    public void setUp() throws Exception {
        super.setUp();

        // Start Activity
        this.activity = this.getActivity();
        ButterKnife.inject(this, this.activity);
        this.newCard = new BaseballCard(true, "Mint", "Code Guru Apps", 1993,
                1, 50000, 1, "Code Guru", "Code Guru Devs", "Catcher");

        this.solo = new Solo(this.inst, this.activity);
    }

    /**
     * Tear down the test fixture by calling {@link Activity#finish()} and
     * deleting the database.
     *
     * @throws Exception If an error occurs while chaining to the super class.
     */
    @Override
    public void tearDown() throws Exception {
        this.solo.finishOpenedActivities();

        super.tearDown();
    }

    /**
     * Check preconditions which must hold to guarantee the validity of all
     * other tests. Assert that the {@link Activity} to test and its
     * {@link ListView} are not <code>null</code>, that the {@link ListView}
     * contains the expected data, and that the database was created with the
     * correct table and populated with the correct data.
     */
    public void testPreConditions() {
        Assert.assertNotNull(this.activity);

        BBCTTestUtil.assertDatabaseCreated(this.inst.getTargetContext());
        Assert.assertTrue(this.dbUtil.containsAllBaseballCards(this.allCards));

        this.solo.waitForFragmentByTag(FragmentTags.CARD_LIST);
        Assert.assertNotNull(listView);
        BBCTTestUtil.assertListViewContainsItems(this.allCards, listView);
    }

    /**
     * Test that the "Add Cards" menu item launches a
     * {@link BaseballCardDetails} activity.
     */
    public void testAddCardsMenuItem() {
        BBCTTestUtil.testMenuItem(this.solo, R.id.add_menu, FragmentTags.EDIT_CARD);
    }

    /**
     * Test that the "Filter Cards" menu item launches a {@link FilterCards}
     * activity.
     */
    public void testFilterCardsMenuItem() {
        BBCTTestUtil.testMenuItem(this.solo, R.id.filter_menu, FragmentTags.FILTER_CARDS);
    }

    /**
     * Test that the "Delete Cards" menu item is disabled. It should be disabled
     * because there is no data currently displayed in the list and therefore no
     * rows are marked.
     */
    public void testDeleteCardsMenuItem() {
        Assert.assertFalse(this.inst.invokeMenuActionSync(this.activity, R.id.delete_menu, 0));
    }

    /**
     * Test the header view of the {@link ListView}.
     */
    public void testHeader() {
        Assert.assertEquals(1, listView.getHeaderViewsCount());
        Assert.assertTrue(this.solo.searchText("Brand"));
        Assert.assertTrue(this.solo.searchText("Year"));
        Assert.assertTrue(this.solo.searchText("#"));
        Assert.assertTrue(this.solo.searchText("Player Name"));
    }

    /**
     * Test that a {@link BaseballCardList} activity without an active filter
     * will be correctly restored after it is destroyed.
     */
    public void testStateDestroyWithoutFilter() {
        this.activity.finish();
        Assert.assertTrue(this.activity.isFinishing());
        this.activity = this.getActivity();

        this.inst.waitForIdleSync();
        ButterKnife.inject(this, this.solo.getCurrentActivity());
        BBCTTestUtil.assertListViewContainsItems(this.allCards, listView);
    }

    /**
     * Test that a {@link BaseballCardList} activity with an active filter will
     * be correctly restored after it is destroyed.
     *
     * @throws Throwable If an error occurs while the portion of the test on the UI
     *                   thread runs.
     */
    public void testStateDestroyWithFilter() throws Throwable {
        this.testYearFilter();
        this.activity.finish();
        Assert.assertTrue(this.activity.isFinishing());
        this.activity = this.getActivity();

        this.inst.waitForIdleSync();
        ButterKnife.inject(this, this.activity);
        BBCTTestUtil.assertListViewContainsItems(this.expectedCards, listView);
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
    public void testStateDestroyClearFilter() throws Throwable {
        this.testClearFilter();
        this.activity.finish();
        Assert.assertTrue(this.activity.isFinishing());
        this.activity = this.getActivity();

        this.inst.waitForIdleSync();
        ButterKnife.inject(this, this.activity);
        BBCTTestUtil.assertListViewContainsItems(this.allCards, listView);
    }

    /**
     * Test that a {@link BaseballCardList} activity without an active filter
     * will be correctly restored after it is paused.
     */
    public void testStatePauseWithoutFilter() {
        this.inst.callActivityOnRestart(this.activity);
        this.inst.waitForIdleSync();
        ButterKnife.inject(this, this.activity);
        BBCTTestUtil.assertListViewContainsItems(this.allCards, listView);
    }

    /**
     * Test that a {@link BaseballCardList} activity with an active filter will
     * be correctly restored after it is paused.
     *
     * @throws Throwable If an error occurs while the portion of the test on the UI
     *                   thread runs.
     */
    public void testStatePauseWithFilter() throws Throwable {
        this.testYearFilter();
        this.inst.callActivityOnRestart(this.activity);
        this.inst.waitForIdleSync();
        ButterKnife.inject(this, this.activity);
        BBCTTestUtil.assertListViewContainsItems(this.expectedCards, listView);
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
    public void testStatePauseClearFilter() throws Throwable {
        this.testClearFilter();
        this.inst.callActivityOnRestart(this.activity);
        this.inst.waitForIdleSync();
        ButterKnife.inject(this, this.activity);
        BBCTTestUtil.assertListViewContainsItems(this.allCards, listView);
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
    public void testOnListItemClick() throws Throwable {
        Log.d(TAG, "testOnListItemClick()");

        int cardIndex = 3;

        Log.d(TAG, "cardIndex=" + cardIndex);

        // Add 1 for the header view.
        this.solo.clickInList(cardIndex + 1);
        this.solo.waitForFragmentByTag(FragmentTags.EDIT_CARD);

        // solo.clickInList() is 1-based
        BaseballCard expectedCard = this.allCards.get(cardIndex - 1);
        BBCTTestUtil.assertAllEditTextContents(this.activity, expectedCard);
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
    public void testAddDuplicateCard() throws Throwable {
        InputStream cardInputStream = this.inst.getContext().getAssets().open(CARD_DATA);
        BaseballCardCsvFileReader cardInput = new BaseballCardCsvFileReader(
                cardInputStream, true);
        BaseballCard card = cardInput.getNextBaseballCard();

        BBCTTestUtil.testMenuItem(this.solo, R.id.add_menu, FragmentTags.EDIT_CARD);
        BBCTTestUtil.addCard(this.solo, card);

        Assert.assertTrue(this.solo.waitForDialogToOpen());
        this.solo.clickOnButton("OK");
        Assert.assertTrue(this.solo.waitForDialogToClose());
    }

    /**
     * Test that baseball card data is correctly added to the database when it
     * already contains data for other cards.
     *
     * @throws Throwable If an error occurs while the portion of the test on the UI
     *                   thread runs.
     */
    public void testAddCardToPopulatedDatabase() throws Throwable {
        BBCTTestUtil.testMenuItem(this.solo, R.id.add_menu, FragmentTags.EDIT_CARD);
        BBCTTestUtil.addCard(this.solo, this.newCard);
        BBCTTestUtil.waitForToast(this.solo, BBCTTestUtil.ADD_MESSAGE);
        this.solo.goBack();

        this.allCards.add(this.newCard);
        this.inst.waitForIdleSync();
        ButterKnife.inject(this, this.activity);
        BBCTTestUtil.assertListViewContainsItems(this.allCards, listView);
    }

    /**
     * Test that the {@link ListView} is updated when the user adds a new card
     * which matches the current filter.
     *
     * @throws Throwable If an error occurs while the portion of the test on the UI
     *                   thread runs.
     */
    public void testAddCardMatchingCurrentFilter() throws Throwable {
        this.testYearFilter();

        BBCTTestUtil.testMenuItem(this.solo, R.id.add_menu, FragmentTags.EDIT_CARD);
        BBCTTestUtil.addCard(this.solo, this.newCard);
        BBCTTestUtil.waitForToast(this.solo, BBCTTestUtil.ADD_MESSAGE);
        this.solo.goBack();

        this.expectedCards.add(this.newCard);
        this.inst.waitForIdleSync();
        ButterKnife.inject(this, this.activity);
        BBCTTestUtil.assertListViewContainsItems(this.expectedCards, listView);
    }

    /**
     * Test that the {@link ListView} is not updated when the user adds a new
     * card which does not match the current filter.
     *
     * @throws Throwable If an error occurs while the portion of the test on the UI
     *                   thread runs.
     */
    public void testAddCardNotMatchingCurrentFilter() throws Throwable {
        this.testYearFilter();

        this.newCard = new BaseballCard(false, "Excellent", "Codeguru Apps",
                1976, 1, 50000, 1, "Codeguru", "Codeguru Devs", "Catcher");
        BBCTTestUtil.testMenuItem(this.solo, R.id.add_menu, FragmentTags.EDIT_CARD);
        BBCTTestUtil.addCard(this.solo, this.newCard);
        BBCTTestUtil.waitForToast(this.solo, BBCTTestUtil.ADD_MESSAGE);
        this.solo.goBack();
        this.inst.waitForIdleSync();
        ButterKnife.inject(this, this.activity);
        BBCTTestUtil.assertListViewContainsItems(this.expectedCards, listView);
    }

    /**
     * Test that the {@link ListView} is updated when the user adds a new card
     * after an active filter was cleared.
     *
     * @throws Throwable If an error occurs while the portion of the test on the UI
     *                   thread runs.
     */
    public void testAddCardAfterClearFilter() throws Throwable {
        this.testClearFilter();
        BBCTTestUtil.testMenuItem(this.solo, R.id.add_menu, FragmentTags.EDIT_CARD);
        BBCTTestUtil.addCard(this.solo, this.newCard);
        BBCTTestUtil.waitForToast(this.solo, BBCTTestUtil.ADD_MESSAGE);
        this.solo.goBack();

        this.allCards.add(this.newCard);
        this.inst.waitForIdleSync();
        ButterKnife.inject(this, this.activity);
        BBCTTestUtil.assertListViewContainsItems(this.allCards, listView);
    }

    /**
     * Test that upon clicking on header {@link View}, all items in
     * {@link ListView} are selected.
     */
    public void testMarkAll() {
        this.markAll();
        this.assertAllCheckboxesChecked();
    }

    private void assertAllCheckboxesChecked() {
        this.inst.waitForIdleSync();
        int numMarked = 0;

        Checkable selectAll = (Checkable) listView.getChildAt(0);
        if (selectAll.isChecked()) {
            numMarked++;
        }

        for (int i = 1; i < listView.getChildCount(); i++) {
            Checkable ctv = (Checkable) listView.getChildAt(i);

            if (ctv.isChecked()) {
                numMarked++;
            }
        }

        Assert.assertEquals(listView.getChildCount(), numMarked);
    }

    private void markAll() {
        this.solo.clickOnCheckBox(SELECT_ALL);
        Assert.assertTrue(this.solo.waitForView(R.id.delete_menu));

        this.inst.waitForIdleSync();
        Assert.assertTrue(selectAll.isChecked());

        for (int i = 1; i < listView.getCount(); i++) {
            Assert.assertTrue("Item #" + i + " is not checked", listView.isItemChecked(i));
        }
    }

    public void testDeleteAll() throws Throwable {
        this.markAll();
        deleteCards();
        Assert.assertNotNull(listView);
        Assert.assertEquals(1, listView.getAdapter().getCount());
        this.solo.waitForView(android.R.id.empty);
    }

    public void testUnmarkAll() throws Throwable {
        this.markAll();
        this.solo.clickOnCheckBox(SELECT_ALL);

        this.inst.waitForIdleSync();
        for (int i = 0; i < listView.getCount(); i++) {
            Assert.assertFalse("Item #" + i + " is checked", listView.isItemChecked(i));
        }
    }

    /**
     * Test that the {@link ListView} displays updated card list, when user
     * deletes cards with applied filter.
     */
    public void testDeleteCardUsingFilter() throws Throwable {
        this.testYearFilter();

        int cardIndex = 0;
        final int year = 1993;
        Predicate<BaseballCard> yearPred = new Predicate<BaseballCard>() {
            @Override
            public boolean doTest(BaseballCard card) {
                return card.getYear() == year;
            }
        };
        this.expectedCards = BBCTTestUtil.filterList(this.allCards, yearPred);
        this.expectedCards.remove(cardIndex);

        Assert.assertTrue(this.solo.waitForView(R.id.select_all));
        // Add 1 for header view
        this.solo.clickOnCheckBox(cardIndex + 1);
        Assert.assertTrue(this.solo.waitForView(R.id.delete_menu));

        deleteCards();

        ButterKnife.inject(this, this.activity);
        BBCTTestUtil.assertListViewContainsItems(this.expectedCards, listView);
    }

    private void deleteCards() {
        Assert.assertTrue(this.solo.waitForView(R.id.delete_menu));
        View deleteMenu = ButterKnife.findById(this.activity, R.id.delete_menu);
        Assert.assertNotNull(deleteMenu);
        TouchUtils.clickView(this, deleteMenu);
        BBCTTestUtil.waitForToast(this.solo, BBCTTestUtil.DELETE_MESSAGE);
        View addMenu = ButterKnife.findById(this.activity, R.id.add_menu);
        Assert.assertNotNull(addMenu);
        Assert.assertTrue(addMenu.isShown());
    }

    /**
     * Test that the {@link ListView} displays updated card list, when user
     * deletes cards without any applied filter.
     */
    public void testDeleteCardNoFilter() throws Throwable {
        int cardIndex = 0;

        this.expectedCards = new ArrayList<BaseballCard>(this.allCards);
        this.expectedCards.remove(cardIndex);

        // Add 1 for header view
        this.solo.clickOnCheckBox(cardIndex + 1);
        Assert.assertTrue(this.solo.waitForView(R.id.delete_menu));

        deleteCards();
        BBCTTestUtil.assertListViewContainsItems(this.expectedCards, listView);
    }

    /**
     * Test that the state of {@link CheckedTextView} is maintained when the
     * {@link BaseballCardList} activity changes orientation.
     */
    public void testSelectionAfterSaveInstanceState() throws Throwable {
        Log.d(TAG, "testSelectionAfterSaveInstanceState()");

        int index = 1;
        this.solo.clickOnCheckBox(index);

        Log.d(TAG, "change orientation");
        this.solo.setActivityOrientation(Solo.LANDSCAPE);

        Log.d(TAG, "assertions");
        Assert.assertTrue(this.solo.waitForView(R.id.delete_menu));
        Assert.assertTrue(this.solo.waitForView(android.R.id.list));
        ButterKnife.inject(this, this.solo.getCurrentActivity());
        Checkable row = (Checkable) listView.getChildAt(index);
        Assert.assertTrue(row.isChecked());

        this.activity.finish();
        Log.d(TAG, "finished");
    }

    /**
     * Test that the {@link ListView} displays the correct cards when filtered
     * by the card year.
     */
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
    public void testClearFilter() {
        this.testYearFilter();
        BBCTTestUtil.testMenuItem(this.solo, R.id.clear_filter_menu, FragmentTags.CARD_LIST);
        this.inst.waitForIdleSync();
        ButterKnife.inject(this, this.activity);
        BBCTTestUtil.assertListViewContainsItems(this.allCards, listView);
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
        BBCTTestUtil.testMenuItem(this.solo, R.id.filter_menu, FragmentTags.FILTER_CARDS);

        BBCTTestUtil.sendKeysToCurrFieldFilterCards(this.solo, checkId, editId, input);
        this.solo.clickOnActionBarItem(R.id.save_menu);
        this.inst.waitForIdleSync();

        this.expectedCards = BBCTTestUtil.filterList(this.allCards, filterPred);
        this.inst.waitForIdleSync();
        ButterKnife.inject(this, this.activity);
        BBCTTestUtil.assertListViewContainsItems(this.expectedCards, listView);

        Assert.assertTrue(this.solo.waitForView(R.id.clear_filter_menu));
    }

    public void testOnClickCheckboxStartActionMode() {
        int index = 4;
        this.solo.clickOnCheckBox(index);
        Assert.assertTrue(this.solo.waitForView(R.id.delete_menu));
    }

    public void testOnClickCheckboxStopActionMode() {
        this.testOnClickCheckboxStartActionMode();

        int index = 4;
        this.solo.clickOnCheckBox(index);
        Assert.assertTrue(this.solo.waitForView(R.id.add_menu));
        View addMenu = ButterKnife.findById(this.activity, R.id.add_menu);
        Assert.assertNotNull(addMenu);
        Assert.assertTrue(addMenu.isShown());
    }

    public void testOnClickCheckboxAll() {
        for(int i = 1; i <= 7; ++i) {
            this.solo.clickOnCheckBox(i);
        }

        this.inst.waitForIdleSync();
        Assert.assertTrue(selectAll.isChecked());
    }

    public void testOnCheckAllAndOnClickCheckbox() {
        this.solo.clickOnCheckBox(SELECT_ALL);
        this.solo.clickOnCheckBox(1);

        this.inst.waitForIdleSync();
        Assert.assertFalse(selectAll.isChecked());
    }

    public void testOnClickCheckboxAndOnCheckAll() {
        this.solo.clickOnCheckBox(1);
        this.solo.clickOnCheckBox(SELECT_ALL);
        this.assertAllCheckboxesChecked();
    }

    public void testOnItemLongClickStartActionMode() {
        int index = 4;
        ArrayList<TextView> views = this.solo.clickLongInList(index);
        this.inst.waitForIdleSync();
        Checkable check = (Checkable) views.get(0);
        Assert.assertTrue(check.isChecked());
        Assert.assertTrue(this.solo.waitForView(R.id.delete_menu));
    }

    public void testOnClickDoneButton() {
        this.testMarkAll();
        this.solo.clickOnImage(0);

        Assert.assertTrue(this.solo.waitForView(R.id.add_menu));
        View addMenu = ButterKnife.findById(this.activity, R.id.add_menu);
        Assert.assertTrue(addMenu.isShown());
        Assert.assertFalse(selectAll.isChecked());
    }

}
