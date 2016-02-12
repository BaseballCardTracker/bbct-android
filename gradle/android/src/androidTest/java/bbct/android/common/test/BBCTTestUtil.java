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
package bbct.android.common.test;

import android.app.Activity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.support.test.InstrumentationRegistry;
import android.support.test.espresso.ViewInteraction;
import android.support.test.uiautomator.UiDevice;
import android.support.v4.app.Fragment;
import android.test.ViewAsserts;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.Spinner;
import bbct.android.common.R;
import bbct.android.common.activity.BaseballCardDetails;
import bbct.android.common.activity.FilterCards;
import bbct.android.common.data.BaseballCard;
import bbct.android.common.provider.BaseballCardSQLHelper;
import butterknife.ButterKnife;
import com.robotium.solo.Solo;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import junit.framework.Assert;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.action.ViewActions.typeTextIntoFocusedView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.hasFocus;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isNotChecked;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

/**
 * Utility methods used for JUnit tests on classes in Android version of BBCT.
 */
final public class BBCTTestUtil {

    /**
     * Assert that the given ListView contains same data as the given list of
     * {@link BaseballCard}s.
     *
     * @param expectedItems
     *            A List of the expected {@link BaseballCard} data.
     * @param listView
     *            The List view to check for {@link BaseballCard} data.
     */
    public static void assertListViewContainsItems(List<BaseballCard> expectedItems,
                                                   ListView listView) {
        // Add 1 to the number of expected cards to account for the header View
        Assert.assertEquals(expectedItems.size() + 1, listView.getAdapter().getCount());

        Adapter adapter = listView.getAdapter();
        for (int i = 0; i < expectedItems.size(); ++i) {
            Assert.assertEquals(expectedItems.get(i), adapter.getItem(i + 1));
        }
    }

    public static void assertListViewContainsItems(List<BaseballCard> expectedItems) {
        for (BaseballCard card : expectedItems) {
            onData(allOf(is(instanceOf(BaseballCard.class)), is(card)))
                    .check(matches(isDisplayed()));
        }
    }

    /**
     * Test that a menu item correctly launches a child activity.
     *
     * @param solo
     *            The {@link Solo} instance used for this test.
     * @param menuId
     *            The id of the menu resource.
     * @param fragmentTag
     *            The tag used when adding the {@link Fragment} to the main activity.
     */
    public static void testMenuItem(Solo solo, int menuId, String fragmentTag) {
        solo.clickOnActionBarItem(menuId);
        Assert.assertTrue(solo.waitForFragmentByTag(fragmentTag));
    }

    /**
     * Add a card to the database using the {@link EditText} views from the
     * given {@link BaseballCardDetails} activity and check that the save button
     * can be clicked.
     *
     * @param solo
     *            The {@link Solo} object performing the
     *            test.
     * @param card
     *            The {@link BaseballCard} object holding the data to add to the
     *            database.
     *
     * @throws Throwable
     *             If an error occurs while the portion of the test on the UI
     *             thread runs.
     */
    public static void addCard(Solo solo, BaseballCard card) throws Throwable {
        BBCTTestUtil.sendKeysToCardDetails(solo, card);
        solo.clickOnActionBarItem(R.id.save_menu);
    }

    public static void addCard(BaseballCard card) throws Throwable {
        BBCTTestUtil.sendKeysToCardDetails(card);
        onView(withId(R.id.save_menu))
                .perform(click());
    }

    public static void waitForToast(Solo solo, String message) {
        Assert.assertTrue(solo.waitForDialogToOpen(TIME_OUT));
        Assert.assertTrue(solo.searchText(message));
    }

    public static void waitForToast(Activity activity, String message) {
        onView(withText(message))
                .inRoot(withDecorView(not(activity.getWindow().getDecorView())))
                .check(matches(isDisplayed()));
    }

    /**
     * Fills in all {@link EditText} views of the given
     * {@link BaseballCardDetails} activity.
     *
     * @param solo
     *            The {@link Solo} object performing the
     *            test.
     * @param card
     *            The {@link BaseballCard} object holding the data to add to the
     *            database.
     * @throws InterruptedException
     *             If {@link Thread#sleep(long)} is interrupted.
     * @see #sendKeysToCardDetails(Solo, BaseballCard, Set)
     */
    public static void sendKeysToCardDetails(Solo solo, BaseballCard card)
            throws InterruptedException {
        BBCTTestUtil.sendKeysToCardDetails(solo, card,
                EnumSet.allOf(EditTexts.class));
    }

    public static void sendKeysToCardDetails(BaseballCard card)
            throws InterruptedException {
        BBCTTestUtil.sendKeysToCardDetails(card, EnumSet.allOf(EditTexts.class));
    }

    /**
     * Fills in all EditText views, except the ones indicated, of the given
     * {@link BaseballCardDetails} activity.
     *
     * @param solo
     *            The {@link Solo} object performing the
     *            test.
     * @param card
     *            The {@link BaseballCard} object holding the data to add to the
     *            database.
     * @param fieldFlags
     *            The {@link EditText} views to fill in.
     * @throws InterruptedException
     *             If {@link Thread#sleep(long)} is interrupted.
     */
    public static void sendKeysToCardDetails(Solo solo, BaseballCard card,
            Set<EditTexts> fieldFlags) throws InterruptedException {
        Log.d(TAG, "sendKeysToCardDetails()");

        solo.waitForView(R.id.scroll_card_details);
        final ScrollView scrollView = ButterKnife.findById(solo.getCurrentActivity(),
                R.id.scroll_card_details);
        Assert.assertNotNull("Scroll view not found", scrollView);
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.fullScroll(ScrollView.FOCUS_UP);
            }
        });

        solo.waitForView(R.id.autograph);
        if (fieldFlags.contains(EditTexts.AUTOGRAPHED)) {
            if (card.isAutographed()) {
                solo.waitForView(R.id.autograph);
                solo.clickOnCheckBox(0);
            }
        }

        if (fieldFlags.contains(EditTexts.CONDITION)) {
            Spinner conditionSpinner = (Spinner) solo.getView(R.id.condition);
            @SuppressWarnings("unchecked")
            ArrayAdapter<CharSequence> conditionAdapter = (ArrayAdapter<CharSequence>) conditionSpinner
                    .getAdapter();
            int newIndex = conditionAdapter.getPosition(card.getCondition());
            int currIndex = conditionSpinner.getSelectedItemPosition();
            solo.pressSpinnerItem(0, newIndex - currIndex);
        }

        AutoCompleteTextView brandText = (AutoCompleteTextView) solo
                .getView(R.id.brand_text);
        if (fieldFlags.contains(EditTexts.BRAND)) {
            solo.typeText(brandText, card.getBrand());
        }

        Thread.sleep(50);
        if (brandText.isPopupShowing()) {
            solo.goBack();
        }

        if (fieldFlags.contains(EditTexts.YEAR)) {
            EditText yearText = (EditText) solo.getView(R.id.year_text);
            solo.typeText(yearText, Integer.toString(card.getYear()));
        }

        if (fieldFlags.contains(EditTexts.NUMBER)) {
            EditText numberText = (EditText) solo.getView(R.id.number_text);
            solo.typeText(numberText, Integer.toString(card.getNumber()));
        }

        if (fieldFlags.contains(EditTexts.VALUE)) {
            String valueStr = String.format("%.2f", card.getValue() / 100.0);
            EditText valueText = (EditText) solo.getView(R.id.value_text);
            solo.typeText(valueText, valueStr);
        }

        if (fieldFlags.contains(EditTexts.COUNT)) {
            EditText countText = (EditText) solo.getView(R.id.count_text);
            solo.typeText(countText, Integer.toString(card.getCount()));
        }

        AutoCompleteTextView playerNameText = (AutoCompleteTextView) solo
                .getView(R.id.player_name_text);
        if (fieldFlags.contains(EditTexts.PLAYER_NAME)) {
            solo.typeText(playerNameText, card.getPlayerName());
        }

        Thread.sleep(50);
        if (playerNameText.isPopupShowing()) {
            solo.goBack();
        }

        AutoCompleteTextView teamText = (AutoCompleteTextView) solo
                .getView(R.id.team_text);
        if (fieldFlags.contains(EditTexts.TEAM)) {
            if (!solo.searchText(solo.getString(R.string.team_label))) {
                scrollDown(scrollView);
            }
            solo.typeText(teamText, card.getTeam());
        }

        Thread.sleep(50);
        if (teamText.isPopupShowing()) {
            solo.goBack();
        }

        if (fieldFlags.contains(EditTexts.PLAYER_POSITION)) {
            Spinner playerPositionSpinner = (Spinner) solo.getView(R.id.player_position_text);
            @SuppressWarnings("unchecked")
            ArrayAdapter<CharSequence> playerPositionAdapter = (ArrayAdapter<CharSequence>) playerPositionSpinner
                    .getAdapter();
            int newIndex = playerPositionAdapter.getPosition(card
                    .getPlayerPosition());
            int currIndex = playerPositionSpinner.getSelectedItemPosition();

            scrollDown(scrollView);
            solo.waitForView(R.id.player_position_text);

            boolean isConditionVisible = solo.searchText(solo.getString(R.string.condition_label),
                    false);
            boolean isPositionVisible = solo.searchText(
                    solo.getString(R.string.player_position_label), true);

            int index = -1;
            if (!isConditionVisible && isPositionVisible) {
                index = 0;
            }
            if (isPositionVisible && isConditionVisible) {
                index = 1;
            }

            Assert.assertFalse("Invalid index", index == -1);
            solo.pressSpinnerItem(index, newIndex - currIndex);
        }
    }

    public static void sendKeysToCardDetails(BaseballCard card, Set<EditTexts> fieldFlags)
            throws InterruptedException {
        Log.d(TAG, "sendKeysToCardDetails()");

        UiDevice device = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

        if (fieldFlags.contains(EditTexts.AUTOGRAPHED)) {
            if (card.isAutographed()) {
                onView(withId(R.id.autograph))
                        .perform(scrollTo(), click())
                        .check(matches(isChecked()));
            }
        }

        if (fieldFlags.contains(EditTexts.CONDITION)) {
            onView(withId(R.id.condition))
                    .perform(scrollTo(), click());
            onData(allOf(is(instanceOf(String.class)), is(card.getCondition())))
                    .perform(click());
            onView(withId(R.id.condition))
                    .check(matches(withSpinnerText(card.getCondition())));
        }

        if (fieldFlags.contains(EditTexts.BRAND)) {
            onView(withId(R.id.brand_text))
                    .perform(scrollTo(), typeText(card.getBrand()))
                    .check(matches(withText(card.getBrand())));
        }
        device.pressEnter();

        onView(withId(R.id.year_text)).check(matches(hasFocus()));
        if (fieldFlags.contains(EditTexts.YEAR)) {
            String yearStr = Integer.toString(card.getYear());
            onView(withId(R.id.year_text))
                    .perform(scrollTo(), typeText(yearStr))
                    .check(matches(withText(yearStr)));
        }
        device.pressEnter();

        onView(withId(R.id.number_text)).check(matches(hasFocus()));
        if (fieldFlags.contains(EditTexts.NUMBER)) {
            String numberStr = Integer.toString(card.getNumber());
            onView(withId(R.id.number_text))
                    .perform(scrollTo(), typeText(numberStr))
                    .check(matches(withText(numberStr)));
        }
        device.pressEnter();

        onView(withId(R.id.value_text)).check(matches(hasFocus()));
        if (fieldFlags.contains(EditTexts.VALUE)) {
            String valueStr = String.format("%.2f", card.getValue() / 100.0);
            onView(withId(R.id.value_text))
                    .perform(scrollTo(), typeText(valueStr))
                    .check(matches(withText(valueStr)));
        }
        device.pressEnter();

        onView(withId(R.id.count_text)).check(matches(hasFocus()));
        if (fieldFlags.contains(EditTexts.COUNT)) {
            String countStr = Integer.toString(card.getCount());
            onView(withId(R.id.count_text))
                    .perform(scrollTo(), typeText(countStr))
                    .check(matches(withText(countStr)));
        }
        device.pressEnter();

        onView(withId(R.id.player_name_text)).check(matches(hasFocus()));
        if (fieldFlags.contains(EditTexts.PLAYER_NAME)) {
            onView(withId(R.id.player_name_text))
                    .perform(scrollTo(), typeText(card.getPlayerName()))
                    .check(matches(withText(card.getPlayerName())));
        }
        device.pressEnter();

        onView(withId(R.id.team_text)).check(matches(hasFocus()));
        if (fieldFlags.contains(EditTexts.TEAM)) {
            onView(withId(R.id.team_text))
                    .perform(scrollTo(), typeText(card.getTeam()))
                    .check(matches(withText(card.getTeam())));
        }
        device.pressEnter();

        if (fieldFlags.contains(EditTexts.PLAYER_POSITION)) {
            onView(withId(R.id.player_position_text))
                    .perform(scrollTo(), click());
            onData(allOf(is(instanceOf(String.class)), is(card.getPlayerPosition())))
                    .perform(click());
            onView(withId(R.id.player_position_text))
                    .check(matches(withSpinnerText(card.getPlayerPosition())));
        }
    }

    public static void scrollDown(final ScrollView scrollView) {
        scrollView.post(new Runnable() {
            @Override
            public void run() {
                scrollView.arrowScroll(ScrollView.FOCUS_DOWN);
            }
        });
    }

    public static void assertAllEditTextContents(BaseballCard expectedCard) {
        ViewInteraction autographView = onView(withId(R.id.autograph));

        if (expectedCard.isAutographed()) {
            autographView.check(matches(isChecked()));
        } else {
            autographView.check(matches(isNotChecked()));
        }

        onView(withId(R.id.condition))
                .check(matches(withSpinnerText(expectedCard.getCondition())));
        onView(withId(R.id.brand_text))
                .check(matches(withText(expectedCard.getBrand())));
        String yearStr = Integer.toString(expectedCard.getYear());
        onView(withId(R.id.year_text))
                .check(matches(withText(yearStr)));
        String numberStr = Integer.toString(expectedCard.getNumber());
        onView(withId(R.id.number_text))
                .check(matches(withText(numberStr)));
        String valueStr = String.format("%.2f", expectedCard.getValue() / 100.0);
        onView(withId(R.id.value_text))
                .check(matches(withText(valueStr)));
        String countStr = Integer.toString(expectedCard.getCount());
        onView(withId(R.id.count_text))
                .check(matches(withText(countStr)));
        onView(withId(R.id.player_name_text))
                .check(matches(withText(expectedCard.getPlayerName())));
        onView(withId(R.id.player_position_text))
                .check(matches(withText(expectedCard.getPlayerPosition())));
    }
    /**
     * Assert that database was created with the correct version and table and
     * that it is empty.
     *
     * @param targetContext
     *            The target context.
     */
    public static void assertDatabaseCreated(Context targetContext) {
        DatabaseUtil dbUtil = new DatabaseUtil(targetContext);
        SQLiteDatabase db = dbUtil.getDatabase();
        Assert.assertNotNull(db);
        Assert.assertEquals(BaseballCardSQLHelper.SCHEMA_VERSION,
                db.getVersion());

        // TODO How do I check that a table exists in the database?
        // TODO How do I check that a table has the correct columns?
    }

    /**
     * Fills in all {@link EditText} views, except the ones indicated, of the
     * {@link FilterCards} activity.
     *
     * @param solo
     *            - the {@link Solo} object to perform clicks on views.
     * @param testCard
     *            - the {@link BaseballCard} which is used to fill in the data.
     * @param fieldFlags
     *            - the {@link EditText} views to fill in.
     */
    public static void sendKeysToFilterCards(Solo solo, BaseballCard testCard,
                                             Set<EditTexts> fieldFlags) {
        if (fieldFlags.contains(EditTexts.BRAND)) {
            sendKeysToCurrFieldFilterCards(solo, R.id.brand_check, R.id.brand_input,
                    testCard.getBrand());
        }

        if (fieldFlags.contains(EditTexts.YEAR)) {
            sendKeysToCurrFieldFilterCards(solo, R.id.year_check, R.id.year_input,
                    testCard.getYear() + "");
        }

        if (fieldFlags.contains(EditTexts.NUMBER)) {
            sendKeysToCurrFieldFilterCards(solo, R.id.number_check, R.id.number_input,
                    testCard.getNumber() + "");
        }

        if (fieldFlags.contains(EditTexts.PLAYER_NAME)) {
            sendKeysToCurrFieldFilterCards(solo, R.id.player_name_check, R.id.player_name_input,
                    testCard.getPlayerName());
        }

        if (fieldFlags.contains(EditTexts.TEAM)) {
            sendKeysToCurrFieldFilterCards(solo, R.id.team_check, R.id.team_input,
                    testCard.getTeam());
        }
    }

    /**
     * Fills in the current {@link EditText} view, of the given
     * {@link FilterCards} activity.
     *
     * @param solo
     *            - the {@link Solo} object to perform clicks on view.
     * @param checkId
     *            - the id of the {@link CheckBox} to click.
     * @param editTextId
     *            - the id of the {@link EditText} to send input to.
     * @param input
     *            - the input string.
     */
    public static void sendKeysToCurrFieldFilterCards(Solo solo, int checkId, int editTextId,
                                                      String input) {
        Activity filterCards = solo.getCurrentActivity();
        Assert.assertTrue(solo.waitForView(checkId));
        CheckBox cb = ButterKnife.findById(filterCards, checkId);
        solo.clickOnView(cb);
        EditText editText = ButterKnife.findById(filterCards, editTextId);
        solo.typeText(editText, input);
    }

    public static List<BaseballCard> filterList(List<BaseballCard> list,
            Predicate<BaseballCard> pred) {
        List<BaseballCard> filteredList = new ArrayList<>();

        for (BaseballCard obj : list) {
            if (pred.doTest(obj)) {
                filteredList.add(obj);
            }
        }

        return filteredList;
    }

    /**
     * Checks if the given child view is visible in the given parent view. Logic
     * followed is same as {@link ViewAsserts#assertOnScreen(View, View)}.
     *
     * @param parentView
     *            The {@link View} object containing the child view test.
     * @param childView
     *            The {@link View} object to be checked.
     */
    public static boolean isViewOnScreen(View parentView, View childView) {
        int[] xyChild = new int[2];
        childView.getLocationOnScreen(xyChild);
        int[] xyParent = new int[2];
        parentView.getLocationOnScreen(xyParent);
        int childViewYFromRoot = xyChild[1] - xyParent[1];
        int rootViewHeight = childView.getRootView().getHeight();
        // If the button is visible on screen, then
        // view should have positive y coordinate on screen and
        // view should have y location on screen less than drawing
        // height of root view
        return childViewYFromRoot >= 0 && childViewYFromRoot <= rootViewHeight;
    }

    /**
     * Generate the power set of a given {@link Set}.
     *
     * @param input
     *            A set of elements
     * @return The power set of the given {@link Set}
     */
    public static <T> Set<Set<T>> powerSet(Set<T> input) {
        Log.d(TAG, "powerSet()");
        Log.d(TAG, "input=" + input);

        Set<T> copy = new HashSet<>(input);
        if (copy.isEmpty()) {
            Set<Set<T>> power = new HashSet<>();
            power.add(new HashSet<T>());
            return power;
        }

        Iterator<T> itr = copy.iterator();
        T elem = itr.next();
        itr.remove();

        Log.d(TAG, "elem=" + elem);

        Set<Set<T>> power = powerSet(copy);

        Log.d(TAG, "power=" + power);

        Set<Set<T>> powerCopy = new HashSet<>();

        for (Set<T> set : power) {
            Set<T> setCopy = new HashSet<>(set);
            setCopy.add(elem);
            powerCopy.add(setCopy);
        }

        power.addAll(powerCopy);

        Log.d(TAG, "power=" + power);

        return power;
    }

    private BBCTTestUtil() {
    }

    /**
     * Enumeration for {@link android.widget.EditText} views which will be used
     * in {@link #sendKeysToCardDetails(Solo, BaseballCard, Set)}.
     */
    public enum EditTexts {
        /**
         * Input the autographed state.
         */
        AUTOGRAPHED,
        /**
         * Input the card condition.
         */
        CONDITION,
        /**
         * Input the card brand field.
         */
        BRAND,
        /**
         * Input the card year field.
         */
        YEAR,
        /**
         * Input the card number field.
         */
        NUMBER,
        /**
         * Input the card value field.
         */
        VALUE,
        /**
         * Input the card count field.
         */
        COUNT,
        /**
         * Input the player name field.
         */
        PLAYER_NAME,
        /**
         * Input the team field.
         */
        TEAM,
        /**
         * Input the player position field.
         */
        PLAYER_POSITION
    }

    public static final String ADD_MESSAGE = "Card added successfully";
    public static final String DELETE_MESSAGE = "Cards deleted successfully";
    private static final int TIME_OUT = 5 * 1000; // 5 seconds
    private static final String TAG = BBCTTestUtil.class.getName();
}
