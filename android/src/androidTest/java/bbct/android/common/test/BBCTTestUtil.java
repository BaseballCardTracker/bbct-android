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
import android.util.Log;

import androidx.test.espresso.ViewInteraction;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.UiDevice;

import org.hamcrest.Matcher;
import org.junit.Assert;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import bbct.android.common.R;
import bbct.android.common.database.BaseballCard;
import bbct.android.common.provider.BaseballCardSQLHelper;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.action.ViewActions.typeTextIntoFocusedView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.hasFocus;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isNotChecked;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

final public class BBCTTestUtil {
    private static final String TAG = BBCTTestUtil.class.getName();
    public static final String ADD_MESSAGE = "Card added successfully";
    public static final String DELETE_MESSAGE = "Cards deleted successfully";

    private BBCTTestUtil() {
    }

    public static void assertListViewContainsItems(List<BaseballCard> expectedItems) {
        for (BaseballCard card : expectedItems) {
            onData(allOf(instanceOf(BaseballCard.class), is(card)))
                    .check(matches(isDisplayed()));
        }
    }

    public static void testMenuItem(int menuId, String fragmentTag) {
        onView(withId(menuId)).perform(click());
    }

    public static void addCard(BaseballCard card) {
        BBCTTestUtil.sendKeysToCardDetails(card);
        onView(withId(R.id.save_button))
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
            if (card.autographed) {
                onView(withId(R.id.autograph))
                        .perform(scrollTo(), click())
                        .check(matches(isChecked()));
            }
        }

        if (fieldFlags.contains(EditTexts.CONDITION)) {
            onView(withId(R.id.condition))
                    .perform(scrollTo(), click());
            onData(allOf(instanceOf(String.class), is(card.condition)))
                    .perform(click());
            onView(withId(R.id.condition))
                    .check(matches(withSpinnerText(card.condition)));
        }

        onView(withId(R.id.brand_text)).check(matches(hasFocus()));
        if (fieldFlags.contains(EditTexts.BRAND)) {
            onView(withId(R.id.brand_text))
                    .perform(scrollTo(), clearText(), typeTextIntoFocusedView(card.brand))
                    .check(matches(withText(card.brand)));
        }
        device.pressEnter();

        onView(withId(R.id.year_text)).check(matches(hasFocus()));
        if (fieldFlags.contains(EditTexts.YEAR)) {
            String yearStr = Integer.toString(card.year);
            onView(withId(R.id.year_text))
                    .perform(scrollTo(), typeTextIntoFocusedView(yearStr))
                    .check(matches(withText(yearStr)));
        }
        device.pressEnter();

        onView(withId(R.id.number_text)).check(matches(hasFocus()));
        if (fieldFlags.contains(EditTexts.NUMBER)) {
            String numberStr = card.number;
            onView(withId(R.id.number_text))
                    .perform(scrollTo(), typeTextIntoFocusedView(numberStr))
                    .check(matches(withText(numberStr.replaceAll("[^a-zA-Z0-9]+", ""))));
        }
        device.pressEnter();

        onView(withId(R.id.value_text)).check(matches(hasFocus()));
        if (fieldFlags.contains(EditTexts.VALUE)) {
            String valueStr = String.format("%.2f", card.value / 100.0);
            onView(withId(R.id.value_text))
                    .perform(scrollTo(), typeTextIntoFocusedView(valueStr))
                    .check(matches(withText(valueStr)));
        }
        device.pressEnter();

        onView(withId(R.id.count_text)).check(matches(hasFocus()));
        if (fieldFlags.contains(EditTexts.COUNT)) {
            String countStr = Integer.toString(card.quantity);
            onView(withId(R.id.count_text))
                    .perform(scrollTo(), typeTextIntoFocusedView(countStr))
                    .check(matches(withText(countStr)));
        }
        device.pressEnter();

        onView(withId(R.id.player_name_text)).check(matches(hasFocus()));
        if (fieldFlags.contains(EditTexts.PLAYER_NAME)) {
            onView(withId(R.id.player_name_text))
                    .perform(scrollTo(), typeTextIntoFocusedView(card.playerName))
                    .check(matches(withText(card.playerName)));
        }
        device.pressEnter();

        onView(withId(R.id.team_text)).check(matches(hasFocus()));
        if (fieldFlags.contains(EditTexts.TEAM)) {
            onView(withId(R.id.team_text))
                    .perform(scrollTo(), typeTextIntoFocusedView(card.team))
                    .check(matches(withText(card.team)));
        }
        device.pressEnter();

        if (fieldFlags.contains(EditTexts.PLAYER_POSITION)) {
            onView(withId(R.id.player_position_text))
                    .perform(scrollTo(), click());
            onData(allOf(instanceOf(String.class), is(card.position)))
                    .perform(click());
            onView(withId(R.id.player_position_text))
                    .check(matches(withSpinnerText(card.position)));
        }
    }

    public static void assertAllEditTextContents(BaseballCard expectedCard) {
        ViewInteraction autographView = onView(withId(R.id.autograph));

        if (expectedCard.autographed) {
            autographView.check(matches(isChecked()));
        } else {
            autographView.check(matches(isNotChecked()));
        }

        onView(withId(R.id.condition))
                .check(matches(withSpinnerText(expectedCard.condition)));
        onView(withId(R.id.brand_text))
                .check(matches(withText(expectedCard.brand)));
        String yearStr = Integer.toString(expectedCard.year);
        onView(withId(R.id.year_text))
                .check(matches(withText(yearStr)));
        String numberStr = expectedCard.number;
        onView(withId(R.id.number_text))
                .check(matches(withText(numberStr)));
        String valueStr = String.format("%.2f", expectedCard.value / 100.0);
        onView(withId(R.id.value_text))
                .check(matches(withText(valueStr)));
        String countStr = Integer.toString(expectedCard.quantity);
        onView(withId(R.id.count_text))
                .check(matches(withText(countStr)));
        onView(withId(R.id.player_name_text))
                .check(matches(withText(expectedCard.playerName)));
        onView(withId(R.id.player_position_text))
                .check(matches(withSpinnerText(expectedCard.position)));
    }

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
                    testCard.brand);
        }

        if (fieldFlags.contains(FilterOption.YEAR)) {
            sendKeysToCurrFieldFilterCards(R.id.year_check, R.id.year_input,
                    Integer.toString(testCard.year));
        }

        if (fieldFlags.contains(FilterOption.NUMBER)) {
            sendKeysToCurrFieldFilterCards(R.id.number_check, R.id.number_input,
                    testCard.number);
        }

        if (fieldFlags.contains(FilterOption.PLAYER_NAME)) {
            sendKeysToCurrFieldFilterCards(R.id.player_name_check, R.id.player_name_input,
                    testCard.playerName);
        }

        if (fieldFlags.contains(FilterOption.TEAM)) {
            sendKeysToCurrFieldFilterCards(R.id.team_check, R.id.team_input,
                    testCard.team);
        }
    }

    public static void sendKeysToCurrFieldFilterCards(int checkId, int editTextId, String input) {
        onView(withId(checkId)).perform(click());
        onView(withId(editTextId)).check(matches(isDisplayed())).perform(typeText(input));
    }

    public static List<BaseballCard> filterList(
        List<BaseballCard> list,
        Matcher<BaseballCard> cardMatcher
    ) {
        List<BaseballCard> filteredList = new ArrayList<>();

        for (BaseballCard obj : list) {
            if (cardMatcher.matches(obj)) {
                filteredList.add(obj);
            }
        }

        return filteredList;
    }

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

    public enum EditTexts {
        AUTOGRAPHED, CONDITION, BRAND, YEAR, NUMBER, VALUE, COUNT, PLAYER_NAME, TEAM,
        PLAYER_POSITION
    }

    public enum FilterOption {
        BRAND, YEAR, NUMBER, PLAYER_NAME, TEAM
    }
}
