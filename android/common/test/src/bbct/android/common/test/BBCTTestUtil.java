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
import android.app.ListActivity;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.test.InstrumentationTestCase;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckedTextView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import bbct.android.common.R;
import bbct.android.common.activity.BaseballCardDetails;
import bbct.android.common.data.BaseballCard;
import bbct.android.common.provider.BaseballCardSQLHelper;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import junit.framework.Assert;

/**
 * Utility methods used for JUnit tests on classes in Android version of BBCT.
 *
 * @author codeguru <codeguru@users.sourceforge.net>
 */
final public class BBCTTestUtil {

    /**
     * Assert that the given ListView contains same data as the given list of
     * {@link BaseballCard}s.
     *
     * @param inst The instrumentation for the running test case. Used to
     * synchronize this assertion with the instrumented activity class being
     * tested.
     * @param expectedItems A List of the expected {@link BaseballCard} data.
     * @param listView The List view to check for {@link BaseballCard} data.
     */
    public static void assertListViewContainsItems(Instrumentation inst, List<BaseballCard> expectedItems, ListView listView) {
        inst.waitForIdleSync();

        // Subtract 1 from the number of views owned by the ListView to account for the header View
        Assert.assertEquals(expectedItems.size(), listView.getCount() - 1);

        for (int i = 0; i < expectedItems.size(); ++i) {
            // Add 1 to skip headers
            View row = listView.getChildAt(i + 1);
            TextView brandTextView = (TextView) row.findViewById(R.id.brand_text_view);
            TextView yearTextView = (TextView) row.findViewById(R.id.year_text_view);
            TextView numberTextView = (TextView) row.findViewById(R.id.number_text_view);
            TextView playerNameTextView = (TextView) row.findViewById(R.id.player_name_text_view);

            StringBuilder rowText = new StringBuilder("Row ").append(i).append(":")
                    .append(brandTextView.getText()).append(',')
                    .append(yearTextView.getText()).append(',')
                    .append(numberTextView.getText()).append(',')
                    .append(playerNameTextView.getText());
            Log.d(TAG, rowText.toString());

            BaseballCard expectedCard = expectedItems.get(i);
            Log.d(TAG, "Baseball Card #" + i + ":" + expectedCard);

            Assert.assertEquals(expectedCard.getBrand(), brandTextView.getText().toString());
            Assert.assertEquals(expectedCard.getYear(), Integer.parseInt(yearTextView.getText().toString()));
            Assert.assertEquals(expectedCard.getNumber(), Integer.parseInt(numberTextView.getText().toString()));
            Assert.assertEquals(expectedCard.getPlayerName(), playerNameTextView.getText().toString());
        }
    }

    /**
     * Test that a menu item correctly launches a child activity.
     *
     * @param inst The instrumentation used for this test.
     * @param activity The activity associated with the menu being tested.
     * @param menuId The id of the menu resource.
     * @param childActivityClass The Class of the child activity which should be
     * launched.
     * @return A reference to the child activity which is launched, if the test
     * succeeds.
     */
    public static Activity testMenuItem(Instrumentation inst, Activity activity, int menuId, Class<? extends Activity> childActivityClass) {
        Instrumentation.ActivityMonitor monitor = new Instrumentation.ActivityMonitor(childActivityClass.getName(), null, false);
        inst.addMonitor(monitor);

        Assert.assertTrue(inst.invokeMenuActionSync(activity, menuId, MENU_FLAGS));

        Activity childActivity = inst.waitForMonitorWithTimeout(monitor, TIME_OUT);

        Assert.assertNotNull(childActivity);
        Assert.assertEquals(childActivityClass, childActivity.getClass());

        return childActivity;
    }

    /**
     * Add a card to the database using the {@link EditText} views from the
     * given {@link BaseballCardDetails} activity and check that the save button
     * can be clicked.
     *
     * @param test The {@link InstrumentationTestCase} object performing the
     * test.
     * @param cardDetails The {@link BaseballCardDetails} activity being tested.
     * @param card The {@link BaseballCard} object holding the data to add to
     * the database.
     *
     * @throws Throwable If an error occurs while the portion of the test on the
     * UI thread runs.
     */
    public static void addCard(InstrumentationTestCase test, Activity cardDetails, BaseballCard card) throws Throwable {
        BBCTTestUtil.sendKeysToCardDetails(test, cardDetails, card);
        BBCTTestUtil.clickCardDetailsSave(test, cardDetails);

        // TODO Check that Toast appears with correct message.
    }

    /**
     * Click the "Save" button on the given {@link BaseballCardDetails}
     * activity. This is all wrapped into a helper method because the button
     * click must be done on the UI thread while the assertion must not.
     *
     * @param test The {@link InstrumentationTestCase} object performing the
     * test.
     * @param cardDetails The {@link BaseballCardDetails} activity being tested.
     *
     * @throws Throwable If an error occurs while the portion of the test on the
     * UI thread runs.
     */
    public static void clickCardDetailsSave(InstrumentationTestCase test, Activity cardDetails) throws Throwable {
        final Button saveButton = (Button) cardDetails.findViewById(R.id.save_button);

        test.runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                Assert.assertTrue(saveButton.performClick());
            }
        });
    }

    /**
     * Click the "Done" button on the given {@link BaseballCardDetails} activity
     * and assert that the activity is finishing. This is all wrapped into a
     * helper method because the button click must be done on the UI thread
     * while the assertion must not.
     *
     * @param test The {@link InstrumentationTestCase} object performing the
     * test.
     * @param cardDetails The {@link BaseballCardDetails} activity being tested.
     *
     * @throws Throwable If an error occurs while the portion of the test on the
     * UI thread runs.
     */
    public static void clickCardDetailsDone(InstrumentationTestCase test, Activity cardDetails) throws Throwable {
        final Button doneButton = (Button) cardDetails.findViewById(R.id.done_button);

        test.runTestOnUiThread(new Runnable() {
            @Override
            public void run() {
                Assert.assertTrue(doneButton.performClick());
            }
        });

        Assert.assertTrue(cardDetails.isFinishing());
    }

    /**
     * Fills in all {@link EditText} views of the given
     * {@link BaseballCardDetails} activity.
     *
     * @param test The {@link InstrumentationTestCase} object performing the
     * test.
     * @param cardDetails The {@link BaseballCardDetails} activity being tested.
     * @param card The {@link BaseballCard} object holding the data to add to
     * the database.
     * @see #sendKeysToCardDetails(InstrumentationTestCase, Activity, BaseballCard, Set)
     */
    public static void sendKeysToCardDetails(InstrumentationTestCase test, Activity cardDetails, BaseballCard card) {
        BBCTTestUtil.sendKeysToCardDetails(test, cardDetails, card, EnumSet.allOf(EditTexts.class));
    }

    /**
     * Fills in all EditText views, except the ones indicated, of the given
     * {@link BaseballCardDetails} activity.
     *
     * @param test The {@link InstrumentationTestCase} object performing the
     * test.
     * @param cardDetails The {@link BaseballCardDetails} activity being tested.
     * @param card The {@link BaseballCard} object holding the data to add to
     * the database.
     * @param fieldFlags The {@link EditText} views to fill in.
     */
    public static void sendKeysToCardDetails(InstrumentationTestCase test, Activity cardDetails, BaseballCard card, Set<EditTexts> fieldFlags) {
        Log.d(TAG, "sendKeysToCardDetails()");

        Instrumentation inst = test.getInstrumentation();

        if (fieldFlags.contains(EditTexts.BRAND)) {
            AutoCompleteTextView brandText = (AutoCompleteTextView) cardDetails.findViewById(R.id.brand_text);
            sendKeysToCurrFieldCardDetails(inst, brandText, card.getBrand());
        }
        inst.sendKeyDownUpSync(KeyEvent.KEYCODE_DPAD_DOWN);

        if (fieldFlags.contains(EditTexts.YEAR)) {
            sendKeysToCurrFieldCardDetails(inst, null, Integer.toString(card.getYear()));
        }
        inst.sendKeyDownUpSync(KeyEvent.KEYCODE_DPAD_DOWN);

        if (fieldFlags.contains(EditTexts.NUMBER)) {
            sendKeysToCurrFieldCardDetails(inst, null, Integer.toString(card.getNumber()));
        }
        inst.sendKeyDownUpSync(KeyEvent.KEYCODE_DPAD_DOWN);

        if (fieldFlags.contains(EditTexts.VALUE)) {
            String valueStr = String.format("%.2f", card.getValue() / 100.0);
            sendKeysToCurrFieldCardDetails(inst, null, valueStr);
        }
        inst.sendKeyDownUpSync(KeyEvent.KEYCODE_DPAD_DOWN);

        if (fieldFlags.contains(EditTexts.COUNT)) {
            sendKeysToCurrFieldCardDetails(inst, null, Integer.toString(card.getCount()));
        }
        inst.sendKeyDownUpSync(KeyEvent.KEYCODE_DPAD_DOWN);

        if (fieldFlags.contains(EditTexts.PLAYER_NAME)) {
            AutoCompleteTextView playerNameText = (AutoCompleteTextView) cardDetails.findViewById(R.id.player_name_text);
            sendKeysToCurrFieldCardDetails(inst, playerNameText, card.getPlayerName());
        }
        inst.sendKeyDownUpSync(KeyEvent.KEYCODE_DPAD_DOWN);

        if (fieldFlags.contains(EditTexts.TEAM)) {
            AutoCompleteTextView teamText = (AutoCompleteTextView) cardDetails.findViewById(R.id.team_text);
            sendKeysToCurrFieldCardDetails(inst, teamText, card.getTeam());
        }
        inst.sendKeyDownUpSync(KeyEvent.KEYCODE_DPAD_DOWN);

        if (fieldFlags.contains(EditTexts.PLAYER_POSITION)) {
            Spinner playerPositionSpinner = (Spinner) cardDetails.findViewById(R.id.player_position_text);
            @SuppressWarnings("unchecked")
            ArrayAdapter<CharSequence> playerPositionAdapter = (ArrayAdapter<CharSequence>) playerPositionSpinner.getAdapter();
            int newPos = playerPositionAdapter.getPosition(card.getPlayerPosition());
            int oldPos = playerPositionSpinner.getSelectedItemPosition();
            int move = newPos - oldPos;

            Log.d(TAG, "newPos=" + newPos + ", oldPos=" + oldPos + ", move=" + move);

            inst.sendKeyDownUpSync(KeyEvent.KEYCODE_DPAD_CENTER);
            if (move > 0) {
                test.sendRepeatedKeys(move, KeyEvent.KEYCODE_DPAD_DOWN);
            } else {
                test.sendRepeatedKeys(-move, KeyEvent.KEYCODE_DPAD_UP);
            }
            inst.sendKeyDownUpSync(KeyEvent.KEYCODE_DPAD_CENTER);
        }
    }

    /**
     * Fills in the current AutoCompleteTextView/EditText view,  of the given
     * {@link BaseballCardDetails} activity.
     *
     * @param inst The {@link Instrumentation} object performing the
     * test.
     * @param editTextView The {@link BaseballCardDetails} EditText object
     *                         of the view to be filled
     * @param cardDetail The {@link BaseballCard} string object holding the data to add to
     * the database.
     */
    public static void sendKeysToCurrFieldCardDetails(Instrumentation inst, EditText editTextView, String cardDetail) {
        Log.d(TAG, "sendKeysToCurrFieldCardDetails()");

        inst.sendStringSync(cardDetail);
        if (editTextView instanceof AutoCompleteTextView) {
            if(((AutoCompleteTextView)editTextView).isPopupShowing()) {
                inst.sendKeyDownUpSync(KeyEvent.KEYCODE_BACK);
            }
        }
    }

    /**
     * Assert that the {@link EditText} views in the given
     * {@link BaseballCardDetails} activity contain the same data as the given
     * {@link BaseballCard}.
     *
     * @param cardDetails The {@link BaseballCardDetails} containing the
     * {@link EditText} views to check.
     * @param expectedCard The {@link BaseballCard} containing the expected
     * data.
     */
    public static void assertAllEditTextContents(Activity cardDetails, BaseballCard expectedCard) {
        EditText brandText = (EditText) cardDetails.findViewById(R.id.brand_text);
        EditText yearText = (EditText) cardDetails.findViewById(R.id.year_text);
        EditText numberText = (EditText) cardDetails.findViewById(R.id.number_text);
        EditText valueText = (EditText) cardDetails.findViewById(R.id.value_text);
        EditText countText = (EditText) cardDetails.findViewById(R.id.count_text);
        EditText playerNameText = (EditText) cardDetails.findViewById(R.id.player_name_text);
        Spinner playerPositionSpinner = (Spinner) cardDetails.findViewById(R.id.player_position_text);

        Assert.assertEquals(expectedCard.getBrand(), brandText.getText().toString());
        Assert.assertEquals(expectedCard.getYear(), Integer.parseInt(yearText.getText().toString()));
        Assert.assertEquals(expectedCard.getNumber(), Integer.parseInt(numberText.getText().toString()));
        Assert.assertEquals(expectedCard.getValue(), (int) (Double.parseDouble(valueText.getText().toString()) * 100));
        Assert.assertEquals(expectedCard.getCount(), Integer.parseInt(countText.getText().toString()));
        Assert.assertEquals(expectedCard.getPlayerName(), playerNameText.getText().toString());
        Assert.assertEquals(expectedCard.getPlayerPosition(), playerPositionSpinner.getSelectedItem());
    }

    /**
     * Assert that database was created with the correct version and table and
     * that it is empty.
     *
     * @param targetPackage The target context.
     */
    public static void assertDatabaseCreated(Context targetContext) {
        DatabaseUtil dbUtil = new DatabaseUtil(targetContext);
        SQLiteDatabase db = dbUtil.getDatabase();
        Assert.assertNotNull(db);
        Assert.assertEquals(BaseballCardSQLHelper.SCHEMA_VERSION, db.getVersion());

        // TODO How do I check that a table exists in the database?
        // TODO How do I check that a table has the correct columns?
    }

    /**
     * Delete a card from the database by using {@link CheckedTextView} to mark
     * a card to delete and then clicking on "Delete" {@link Button}.
     */
    public static void removeCard(InstrumentationTestCase test, Activity cardList, BaseballCard card) throws Throwable {
        BBCTTestUtil.markCard(test, cardList, card);
        BBCTTestUtil.clickDeleteCardMenuItem(test, cardList);
    }

    public static void markCard(InstrumentationTestCase test, Activity cardList, BaseballCard card) throws Throwable {
        final ListView lv = ((ListActivity) cardList).getListView();

        String playerName = card.getPlayerName();
        String brand = card.getBrand();
        int year = card.getYear();
        int number = card.getNumber();

        for (int i = 1; i < lv.getChildCount(); i++) {
            View v = lv.getChildAt(i);
            final CheckedTextView ctv = (CheckedTextView) v.findViewById(R.id.checkmark);

            boolean isEqualPName = playerName.equals(((TextView)v.findViewById(R.id.player_name_text_view)).getText().toString());
            boolean isEqualBrand = brand.equals(((TextView)v.findViewById(R.id.brand_text_view)).getText().toString());
            boolean isEqualYear = ( year == Integer.parseInt(((TextView)v.findViewById(R.id.year_text_view)).getText().toString()) );
            boolean isEqualNumber = ( number == Integer.parseInt(((TextView)v.findViewById(R.id.number_text_view)).getText().toString()) );

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

    public static void clickDeleteCardMenuItem(InstrumentationTestCase test, Activity cardList) throws Throwable {
        Assert.assertTrue(test.getInstrumentation().invokeMenuActionSync(cardList, R.id.delete_menu, 0));
    }

    /**
     * Checks if the given child view is visible in the given parent view.
     * Logic followed is same as {@link ViewAsserts.assertOnScreen()}.
     *
     * @param parentView The {@link View} object containing the child view
     * test.
     * @param childView The {@link View} object to be checked.
     */
    public static boolean isViewOnScreen(View parentView, View childView) {
        int[] xyChild = new int[2];
        childView.getLocationOnScreen(xyChild);
        int[] xyParent = new int[2];
        parentView.getLocationOnScreen(xyParent);
        int childViewYFromRoot = xyChild[1] - xyParent[1];
        int rootViewHeight = childView.getRootView().getHeight();
        //If the button is visible on screen, then
        //view should have positive y coordinate on screen and
        //view should have y location on screen less than drawing
        //height of root view
        if (childViewYFromRoot >= 0 && childViewYFromRoot <= rootViewHeight) {
            return true;
        }
        return false;
    }

    /**
     * Generate the power set of a given {@link Set}.
     *
     * @param input A set of elements
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
     * Enumeration for {@link android.widget.EditText} views which will be used in
     * {@link #sendKeysToCardDetails(InstrumentationTestCase, Activity, BaseballCard, Set)}.
     */
    public enum EditTexts {
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
    private static final int MENU_FLAGS = 0;
    private static final int TIME_OUT = 5 * 1000; // 5 seconds
    private static final String TAG = BBCTTestUtil.class.getName();
}
