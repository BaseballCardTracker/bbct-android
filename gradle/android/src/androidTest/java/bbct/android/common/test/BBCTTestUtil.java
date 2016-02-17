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
import android.test.ViewAsserts;
import android.util.Log;
import android.view.View;
import bbct.android.common.R;
import bbct.android.common.data.BaseballCard;
import bbct.android.common.provider.BaseballCardSQLHelper;
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
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

/**
 * Utility methods used for JUnit tests on classes in Android version of BBCT.
 */
final public class BBCTTestUtil {
    private static final String TAG = BBCTTestUtil.class.getName();
    public static final String ADD_MESSAGE = "Card added successfully";
    public static final String DELETE_MESSAGE = "Cards deleted successfully";

    public static void assertListViewContainsItems(List<BaseballCard> expectedItems) {
        for (BaseballCard card : expectedItems) {
            onData(allOf(instanceOf(BaseballCard.class), is(card)))
                    .check(matches(isDisplayed()));
        }
    }

    public static void testMenuItem(int menuId, String fragmentTag) {
        onView(withId(menuId)).perform(click());
    }

    public static void addCard(BaseballCard card) throws Throwable {
        BBCTTestUtil.sendKeysToCardDetails(card);
        onView(withId(R.id.save_menu))
                .perform(click());
    }

    public static void waitForToast(Activity activity, String message) {
        onView(withText(message))
                .inRoot(withDecorView(not(activity.getWindow().getDecorView())))
                .check(matches(isDisplayed()));
    }

    public static void sendKeysToCardDetails(BaseballCard card) {
        BBCTTestUtil.sendKeysToCardDetails(card, EnumSet.allOf(EditTexts.class));
    }

    public static void sendKeysToCardDetails(BaseballCard card, Set<EditTexts> fieldFlags) {
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
            onData(allOf(instanceOf(String.class), is(card.getCondition())))
                    .perform(click());
            onView(withId(R.id.condition))
                    .check(matches(withSpinnerText(card.getCondition())));
        }

        onView(withId(R.id.brand_text)).check(matches(hasFocus()));
        if (fieldFlags.contains(EditTexts.BRAND)) {
            onView(withId(R.id.brand_text))
                    .perform(scrollTo(), typeTextIntoFocusedView(card.getBrand()))
                    .check(matches(withText(card.getBrand())));
        }
        device.pressEnter();

        onView(withId(R.id.year_text)).check(matches(hasFocus()));
        if (fieldFlags.contains(EditTexts.YEAR)) {
            String yearStr = Integer.toString(card.getYear());
            onView(withId(R.id.year_text))
                    .perform(scrollTo(), typeTextIntoFocusedView(yearStr))
                    .check(matches(withText(yearStr)));
        }
        device.pressEnter();

        onView(withId(R.id.number_text)).check(matches(hasFocus()));
        if (fieldFlags.contains(EditTexts.NUMBER)) {
            String numberStr = Integer.toString(card.getNumber());
            onView(withId(R.id.number_text))
                    .perform(scrollTo(), typeTextIntoFocusedView(numberStr))
                    .check(matches(withText(numberStr)));
        }
        device.pressEnter();

        onView(withId(R.id.value_text)).check(matches(hasFocus()));
        if (fieldFlags.contains(EditTexts.VALUE)) {
            String valueStr = String.format("%.2f", card.getValue() / 100.0);
            onView(withId(R.id.value_text))
                    .perform(scrollTo(), typeTextIntoFocusedView(valueStr))
                    .check(matches(withText(valueStr)));
        }
        device.pressEnter();

        onView(withId(R.id.count_text)).check(matches(hasFocus()));
        if (fieldFlags.contains(EditTexts.COUNT)) {
            String countStr = Integer.toString(card.getCount());
            onView(withId(R.id.count_text))
                    .perform(scrollTo(), typeTextIntoFocusedView(countStr))
                    .check(matches(withText(countStr)));
        }
        device.pressEnter();

        onView(withId(R.id.player_name_text)).check(matches(hasFocus()));
        if (fieldFlags.contains(EditTexts.PLAYER_NAME)) {
            onView(withId(R.id.player_name_text))
                    .perform(scrollTo(), typeTextIntoFocusedView(card.getPlayerName()))
                    .check(matches(withText(card.getPlayerName())));
        }
        device.pressEnter();

        onView(withId(R.id.team_text)).check(matches(hasFocus()));
        if (fieldFlags.contains(EditTexts.TEAM)) {
            onView(withId(R.id.team_text))
                    .perform(scrollTo(), typeTextIntoFocusedView(card.getTeam()))
                    .check(matches(withText(card.getTeam())));
        }
        device.pressEnter();

        if (fieldFlags.contains(EditTexts.PLAYER_POSITION)) {
            onView(withId(R.id.player_position_text))
                    .perform(scrollTo(), click());
            onData(allOf(instanceOf(String.class), is(card.getPlayerPosition())))
                    .perform(click());
            onView(withId(R.id.player_position_text))
                    .check(matches(withSpinnerText(card.getPlayerPosition())));
        }
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
                .check(matches(withSpinnerText(expectedCard.getPlayerPosition())));
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

    public static void sendKeysToFilterCards(BaseballCard testCard, Set<FilterOption> fieldFlags) {
        if (fieldFlags.contains(FilterOption.BRAND)) {
            sendKeysToCurrFieldFilterCards(R.id.brand_check, R.id.brand_input,
                    testCard.getBrand());
        }

        if (fieldFlags.contains(FilterOption.YEAR)) {
            sendKeysToCurrFieldFilterCards(R.id.year_check, R.id.year_input,
                    Integer.toString(testCard.getYear()));
        }

        if (fieldFlags.contains(FilterOption.NUMBER)) {
            sendKeysToCurrFieldFilterCards(R.id.number_check, R.id.number_input,
                    Integer.toString(testCard.getNumber()));
        }

        if (fieldFlags.contains(FilterOption.PLAYER_NAME)) {
            sendKeysToCurrFieldFilterCards(R.id.player_name_check, R.id.player_name_input,
                    testCard.getPlayerName());
        }

        if (fieldFlags.contains(FilterOption.TEAM)) {
            sendKeysToCurrFieldFilterCards(R.id.team_check, R.id.team_input,
                    testCard.getTeam());
        }
    }

    public static void sendKeysToCurrFieldFilterCards(int checkId, int editTextId, String input) {
        onView(withId(checkId)).perform(click());
        onView(withId(editTextId)).check(matches(isDisplayed())).perform(typeText(input));
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

    public enum EditTexts {
        AUTOGRAPHED, CONDITION, BRAND, YEAR, NUMBER, VALUE, COUNT, PLAYER_NAME, TEAM,
        PLAYER_POSITION
    }

    public enum FilterOption {
        BRAND, YEAR, NUMBER, PLAYER_NAME, TEAM
    }
}
