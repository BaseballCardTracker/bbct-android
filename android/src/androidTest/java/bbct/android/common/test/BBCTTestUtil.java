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
import android.util.Log;
import android.widget.CheckBox;
import android.widget.EditText;

import junit.framework.Assert;

import org.hamcrest.Matcher;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import bbct.android.common.R;
import bbct.android.common.provider.BaseballCardSQLHelper;
import bbct.data.BaseballCard;

import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.action.ViewActions.typeTextIntoFocusedView;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.hasFocus;
import static android.support.test.espresso.matcher.ViewMatchers.isChecked;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.isEnabled;
import static android.support.test.espresso.matcher.ViewMatchers.isNotChecked;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withSpinnerText;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
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

        onView(withId(R.id.brand)).check(matches(hasFocus()));
        if (fieldFlags.contains(EditTexts.BRAND)) {
            onView(withId(R.id.brand))
                    .perform(scrollTo(), clearText(), typeTextIntoFocusedView(card.getBrand()))
                    .check(matches(withText(card.getBrand())));
        }
        device.pressEnter();

        onView(withId(R.id.year)).check(matches(hasFocus()));
        if (fieldFlags.contains(EditTexts.YEAR)) {
            String yearStr = Integer.toString(card.getYear());
            onView(withId(R.id.year))
                    .perform(scrollTo(), typeTextIntoFocusedView(yearStr))
                    .check(matches(withText(yearStr)));
        }
        device.pressEnter();

        onView(withId(R.id.number)).check(matches(hasFocus()));
        if (fieldFlags.contains(EditTexts.NUMBER)) {
            String numberStr = Integer.toString(card.getNumber());
            onView(withId(R.id.number))
                    .perform(scrollTo(), typeTextIntoFocusedView(numberStr))
                    .check(matches(withText(numberStr)));
        }
        device.pressEnter();

        onView(withId(R.id.value)).check(matches(hasFocus()));
        if (fieldFlags.contains(EditTexts.VALUE)) {
            String valueStr = String.format("%.2f", card.getValue() / 100.0);
            onView(withId(R.id.value))
                    .perform(scrollTo(), typeTextIntoFocusedView(valueStr))
                    .check(matches(withText(valueStr)));
        }
        device.pressEnter();

        onView(withId(R.id.quantity)).check(matches(hasFocus()));
        if (fieldFlags.contains(EditTexts.QUANTITY)) {
            String countStr = Integer.toString(card.getCount());
            onView(withId(R.id.quantity))
                    .perform(scrollTo(), typeTextIntoFocusedView(countStr))
                    .check(matches(withText(countStr)));
        }
        device.pressEnter();

        onView(withId(R.id.player_name)).check(matches(hasFocus()));
        if (fieldFlags.contains(EditTexts.PLAYER_NAME)) {
            onView(withId(R.id.player_name))
                    .perform(scrollTo(), typeTextIntoFocusedView(card.getPlayerName()))
                    .check(matches(withText(card.getPlayerName())));
        }
        device.pressEnter();

        onView(withId(R.id.team)).check(matches(hasFocus()));
        if (fieldFlags.contains(EditTexts.TEAM)) {
            onView(withId(R.id.team))
                    .perform(scrollTo(), typeTextIntoFocusedView(card.getTeam()))
                    .check(matches(withText(card.getTeam())));
        }
        device.pressEnter();

        if (fieldFlags.contains(EditTexts.PLAYER_POSITION)) {
            onView(withId(R.id.player_position))
                    .perform(scrollTo(), click());
            onData(allOf(instanceOf(String.class), is(card.getPlayerPosition())))
                    .perform(click());
            onView(withId(R.id.player_position))
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
        onView(withId(R.id.brand))
                .check(matches(withText(expectedCard.getBrand())));
        String yearStr = Integer.toString(expectedCard.getYear());
        onView(withId(R.id.year))
                .check(matches(withText(yearStr)));
        String numberStr = Integer.toString(expectedCard.getNumber());
        onView(withId(R.id.number))
                .check(matches(withText(numberStr)));
        String valueStr = String.format("%.2f", expectedCard.getValue() / 100.0);
        onView(withId(R.id.value))
                .check(matches(withText(valueStr)));
        String countStr = Integer.toString(expectedCard.getCount());
        onView(withId(R.id.quantity))
                .check(matches(withText(countStr)));
        onView(withId(R.id.player_name))
                .check(matches(withText(expectedCard.getPlayerName())));
        onView(withId(R.id.player_position))
                .check(matches(withSpinnerText(expectedCard.getPlayerPosition())));
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
            sendKeysToCurrFieldFilterCards(R.id.brand, testCard.getBrand());
        }

        if (fieldFlags.contains(FilterOption.YEAR)) {
            sendKeysToCurrFieldFilterCards(R.id.year,
                    Integer.toString(testCard.getYear()));
        }

        if (fieldFlags.contains(FilterOption.NUMBER)) {
            sendKeysToCurrFieldFilterCards(R.id.number,
                    Integer.toString(testCard.getNumber()));
        }

        if (fieldFlags.contains(FilterOption.PLAYER_NAME)) {
            sendKeysToCurrFieldFilterCards(R.id.player_name, testCard.getPlayerName());
        }

        if (fieldFlags.contains(FilterOption.TEAM)) {
            sendKeysToCurrFieldFilterCards(R.id.team, testCard.getTeam());
        }
    }

    public static void sendKeysToCurrFieldFilterCards(int filterId, String input) {
        onView(allOf(withParent(withId(filterId)), instanceOf(CheckBox.class)))
                .perform(click());
        onView(allOf(withParent(withId(filterId)), instanceOf(EditText.class)))
                .check(matches(allOf(isEnabled(), isDisplayed())))
                .perform(typeText(input));
    }

    public static List<BaseballCard> filterList(List<BaseballCard> list,
            Matcher<BaseballCard> cardMatcher) {
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
        AUTOGRAPHED, CONDITION, BRAND, YEAR, NUMBER, VALUE, QUANTITY, PLAYER_NAME, TEAM,
        PLAYER_POSITION
    }

    public enum FilterOption {
        BRAND, YEAR, NUMBER, PLAYER_NAME, TEAM
    }
}
