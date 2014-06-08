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
import android.app.Instrumentation;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.test.InstrumentationTestCase;
import android.test.ViewAsserts;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import bbct.android.common.R;
import bbct.android.common.activity.BaseballCardDetails;
import bbct.android.common.activity.FilterCards;
import bbct.android.common.data.BaseballCard;
import bbct.android.common.provider.BaseballCardSQLHelper;
import com.robotium.solo.Solo;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import junit.framework.Assert;

/**
 * Utility methods used for JUnit tests on classes in Android version of BBCT.
 */
final public class BBCTTestUtil {

    /**
     * Assert that the given ListView contains same data as the given list of
     * {@link BaseballCard}s.
     *
     * @param inst
     *            The instrumentation for the running test case. Used to
     *            synchronize this assertion with the instrumented activity
     *            class being tested.
     * @param expectedItems
     *            A List of the expected {@link BaseballCard} data.
     * @param listView
     *            The List view to check for {@link BaseballCard} data.
     */
    public static void assertListViewContainsItems(Instrumentation inst,
            List<BaseballCard> expectedItems, ListView listView) {
        inst.waitForIdleSync();

        // Subtract 1 from the number of views owned by the ListView to account
        // for the header View
        Assert.assertEquals(expectedItems.size(), listView.getCount() - 1);

        for (BaseballCard card : expectedItems) {

            boolean listContainsCard = false;
            for (int i = 0; i < listView.getChildCount(); i++) {
                // Add 1 to skip headers
                View row = listView.getChildAt(i + 1);
                TextView brandTextView = (TextView) row
                        .findViewById(R.id.brand_text_view);
                TextView yearTextView = (TextView) row
                        .findViewById(R.id.year_text_view);
                TextView numberTextView = (TextView) row
                        .findViewById(R.id.number_text_view);
                TextView playerNameTextView = (TextView) row
                        .findViewById(R.id.player_name_text_view);

                String brand = brandTextView.getText().toString(), playerName = playerNameTextView
                        .getText().toString();
                int year = Integer.parseInt(yearTextView.getText().toString()), number = Integer
                        .parseInt(numberTextView.getText().toString());

                if (card.getBrand().equals(brand) && card.getYear() == year
                        && card.getNumber() == number
                        && card.getPlayerName().equals(playerName)) {
                    listContainsCard = true;
                    break;
                }
            }

            Assert.assertTrue(listContainsCard);
        }
    }

    /**
     * Test that a menu item correctly launches a child activity.
     *
     * @param menuId
     *            The id of the menu resource.
     * @param childActivityClass
     *            The Class of the child activity which should be launched.
     * @return A reference to the child activity which is launched, if the test
     *         succeeds.
     */
    public static Activity testMenuItem(Solo solo, int menuId,
                                        Class<? extends Activity> childActivityClass) {
        solo.clickOnActionBarItem(menuId);

        if (childActivityClass != null) {
            Assert.assertTrue(solo
                    .waitForActivity(childActivityClass, TIME_OUT));
        }

        return solo.getCurrentActivity();
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

    public static void waitForToast(Solo solo, String message) {
        Assert.assertTrue(solo.waitForDialogToOpen(TIME_OUT));
        Assert.assertTrue(solo.searchText(message));
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

        if (fieldFlags.contains(EditTexts.AUTOGRAPHED)) {
            if (card.isAutographed()) {
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
            solo.typeText(teamText, card.getTeam());
        }

        Thread.sleep(50);
        if (teamText.isPopupShowing()) {
            solo.goBack();
        }

        if (fieldFlags.contains(EditTexts.PLAYER_POSITION)) {
            Spinner playerPositionSpinner = (Spinner) solo
                    .getView(R.id.player_position_text);
            @SuppressWarnings("unchecked")
            ArrayAdapter<CharSequence> playerPositionAdapter = (ArrayAdapter<CharSequence>) playerPositionSpinner
                    .getAdapter();
            int newIndex = playerPositionAdapter.getPosition(card
                    .getPlayerPosition());
            int currIndex = playerPositionSpinner.getSelectedItemPosition();
            solo.pressSpinnerItem(1, newIndex - currIndex);
        }
    }

    /**
     * Fills in the current AutoCompleteTextView/EditText view, of the given
     * {@link BaseballCardDetails} activity.
     *
     * @param inst
     *            The {@link Instrumentation} object performing the test.
     * @param editTextView
     *            The {@link BaseballCardDetails} EditText object of the view to
     *            be filled
     * @param cardDetail
     *            The {@link BaseballCard} string object holding the data to add
     *            to the database.
     */
    public static void sendKeysToCurrFieldCardDetails(Instrumentation inst,
            EditText editTextView, String cardDetail) {
        Log.d(TAG, "sendKeysToCurrFieldCardDetails()");

        inst.sendStringSync(cardDetail);
        if (editTextView instanceof AutoCompleteTextView) {
            if (((AutoCompleteTextView) editTextView).isPopupShowing()) {
                inst.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
            }
        }
    }

    /**
     * Assert that the {@link EditText} views in the given
     * {@link BaseballCardDetails} activity contain the same data as the given
     * {@link BaseballCard}.
     *
     * @param cardDetails
     *            The {@link BaseballCardDetails} containing the
     *            {@link EditText} views to check.
     * @param expectedCard
     *            The {@link BaseballCard} containing the expected data.
     */
    public static void assertAllEditTextContents(Activity cardDetails,
            BaseballCard expectedCard) {
        CheckBox autographedCheckBox = (CheckBox) cardDetails
                .findViewById(R.id.autograph);
        Spinner conditionSpinner = (Spinner) cardDetails
                .findViewById(R.id.condition);
        EditText brandText = (EditText) cardDetails
                .findViewById(R.id.brand_text);
        EditText yearText = (EditText) cardDetails.findViewById(R.id.year_text);
        EditText numberText = (EditText) cardDetails
                .findViewById(R.id.number_text);
        EditText valueText = (EditText) cardDetails
                .findViewById(R.id.value_text);
        EditText countText = (EditText) cardDetails
                .findViewById(R.id.count_text);
        EditText playerNameText = (EditText) cardDetails
                .findViewById(R.id.player_name_text);
        Spinner playerPositionSpinner = (Spinner) cardDetails
                .findViewById(R.id.player_position_text);

        Assert.assertEquals(expectedCard.isAutographed(),
                autographedCheckBox.isChecked());
        Assert.assertEquals(expectedCard.getCondition(),
                conditionSpinner.getSelectedItem());
        Assert.assertEquals(expectedCard.getBrand(), brandText.getText()
                .toString());
        Assert.assertEquals(expectedCard.getYear(),
                Integer.parseInt(yearText.getText().toString()));
        Assert.assertEquals(expectedCard.getNumber(),
                Integer.parseInt(numberText.getText().toString()));
        Assert.assertEquals(
                expectedCard.getValue(),
                (int) (Double.parseDouble(valueText.getText().toString()) * 100));
        Assert.assertEquals(expectedCard.getCount(),
                Integer.parseInt(countText.getText().toString()));
        Assert.assertEquals(expectedCard.getPlayerName(), playerNameText
                .getText().toString());
        Assert.assertEquals(expectedCard.getPlayerPosition(),
                playerPositionSpinner.getSelectedItem());
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
     * Delete a card from the database by using {@link CheckedTextView} to mark
     * a card to delete and then clicking on "Delete" {@link Button}.
     */
    public static void removeCard(InstrumentationTestCase test,
            Activity cardList, BaseballCard card) throws Throwable {
        BBCTTestUtil.markCard(test, cardList, card);
        BBCTTestUtil.clickDeleteCardMenuItem(test, cardList);
    }

    public static void markCard(InstrumentationTestCase test,
            Activity cardList, BaseballCard card) throws Throwable {
        final ListView lv = (ListView) cardList.findViewById(android.R.id.list);

        String playerName = card.getPlayerName();
        String brand = card.getBrand();
        int year = card.getYear();
        int number = card.getNumber();

        for (int i = 1; i < lv.getChildCount(); i++) {
            View v = lv.getChildAt(i);
            final CheckedTextView ctv = (CheckedTextView) v
                    .findViewById(R.id.checkmark);

            boolean isEqualPName = playerName.equals(((TextView) v
                    .findViewById(R.id.player_name_text_view)).getText()
                    .toString());
            boolean isEqualBrand = brand.equals(((TextView) v
                    .findViewById(R.id.brand_text_view)).getText().toString());
            boolean isEqualYear = (year == Integer.parseInt(((TextView) v
                    .findViewById(R.id.year_text_view)).getText().toString()));
            boolean isEqualNumber = (number == Integer.parseInt(((TextView) v
                    .findViewById(R.id.number_text_view)).getText().toString()));

            if (isEqualPName && isEqualBrand && isEqualYear && isEqualNumber) {

                test.runTestOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Assert.assertTrue(ctv.performClick());
                    }
                });

                break;
            }
        }

    }

    public static void clickDeleteCardMenuItem(InstrumentationTestCase test,
            Activity cardList) throws Throwable {
        Assert.assertTrue(test.getInstrumentation().invokeMenuActionSync(
                cardList, R.id.delete_menu, 0));
    }

    /**
     * Fills in all {@link EditText} views, except the ones indicated, of the
     * {@link FilterCards} activity.
     *
     * @param test
     *            - the {@link InstrumentationTestCase} object perform in the
     *            test.
     * @param solo
     *            - the {@link Solo} object to perform clicks on views.
     * @param testCard
     *            - the {@link BaseballCard} which is used to fill in the data.
     * @param fieldFlags
     *            - the {@link EditText} views to fill in.
     */
    public static void sendKeysToFilterCards(InstrumentationTestCase test,
            Solo solo, BaseballCard testCard, Set<EditTexts> fieldFlags) {
        Instrumentation inst = test.getInstrumentation();

        if (fieldFlags.contains(EditTexts.BRAND)) {
            sendKeysToCurrFieldFilterCards(inst, solo, R.id.brand_check,
                    testCard.getBrand());
        }

        if (fieldFlags.contains(EditTexts.YEAR)) {
            sendKeysToCurrFieldFilterCards(inst, solo, R.id.year_check,
                    testCard.getYear() + "");
        }

        if (fieldFlags.contains(EditTexts.NUMBER)) {
            sendKeysToCurrFieldFilterCards(inst, solo, R.id.number_check,
                    testCard.getNumber() + "");
        }

        if (fieldFlags.contains(EditTexts.PLAYER_NAME)) {
            sendKeysToCurrFieldFilterCards(inst, solo, R.id.player_name_check,
                    testCard.getPlayerName());
        }

        if (fieldFlags.contains(EditTexts.TEAM)) {
            sendKeysToCurrFieldFilterCards(inst, solo, R.id.team_check,
                    testCard.getTeam());
        }
    }

    /**
     * Fills in the current {@link EditText} view, of the given
     * {@link FilterCards} activity.
     *
     * @param inst
     *            - the {@link Instrumentation} object perform in the test.
     * @param solo
     *            - the {@link Solo} object to perform clicks on view.
     * @param checkId
     *            - the id of {@link CheckBox} to click.
     * @param input
     *            - the input string.
     */
    public static void sendKeysToCurrFieldFilterCards(Instrumentation inst,
            Solo solo, int checkId, String input) {
        Activity filterCards = solo.getCurrentActivity();
        CheckBox cb = (CheckBox) filterCards.findViewById(checkId);
        solo.clickOnView(cb);
        inst.sendStringSync(input);
    }

    public static List<BaseballCard> filterList(List<BaseballCard> list,
            Predicate<BaseballCard> pred) {
        List<BaseballCard> filteredList = new ArrayList<BaseballCard>();

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

        Set<T> copy = new HashSet<T>(input);
        if (copy.isEmpty()) {
            Set<Set<T>> power = new HashSet<Set<T>>();
            power.add(new HashSet<T>());
            return power;
        }

        Iterator<T> itr = copy.iterator();
        T elem = itr.next();
        itr.remove();

        Log.d(TAG, "elem=" + elem);

        Set<Set<T>> power = powerSet(copy);

        Log.d(TAG, "power=" + power);

        Set<Set<T>> powerCopy = new HashSet<Set<T>>();

        for (Set<T> set : power) {
            Set<T> setCopy = new HashSet<T>(set);
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

    /**
     * Asset file which contains card data as CSV values.
     */
    public static final String CARD_DATA = "cards.csv";
    public static String ADD_MESSAGE = "Card added successfully";
    public static String DELETE_MESSAGE = "Cards deleted successfully";
    private static final int TIME_OUT = 5 * 1000; // 5 seconds
    private static final String TAG = BBCTTestUtil.class.getName();
}
