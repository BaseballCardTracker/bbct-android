/*
 * This file is part of BBCT for Android.
 *
 * Copyright 2016 codeguru <codeguru@users.sourceforge.net>
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
package bbct.android.common.screenshots;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeTextIntoFocusedView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import static org.hamcrest.Matchers.instanceOf;
import static bbct.android.common.test.matcher.Matchers.atPosition;

import android.app.Instrumentation;
import android.content.Context;
import android.util.Log;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.UiObjectNotFoundException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import bbct.android.common.R;
import bbct.android.common.database.BaseballCard;
import bbct.android.common.test.rule.DataTestRule;
import bbct.android.common.view.BaseballCardView;
import bbct.android.lite.activity.LiteActivity;
import tools.fastlane.screengrab.Screengrab;
import tools.fastlane.screengrab.UiAutomatorScreenshotStrategy;

public class Screenshots {
    @Rule
    public ActivityScenarioRule<LiteActivity> activityRule = new ActivityScenarioRule<>(
        LiteActivity.class
    );
    @Rule
    public DataTestRule dataTestRule = new DataTestRule();

    private static final String TAG = Screenshots.class.getName();
    private static final long TIMEOUT = 5000;
    private static final String YEAR_STRING = "1993";
    public static final int YEAR_INSTANCE = 1;

    protected Context context;
    protected Instrumentation inst;

    private BaseballCard card;

    @Before
    public void setUp() throws UiObjectNotFoundException {
        inst = InstrumentationRegistry.getInstrumentation();
        context = inst.getTargetContext();
        ActivityScenario<LiteActivity> scenario = activityRule.getScenario();

        card = dataTestRule.getCard(0);
        Screengrab.setDefaultScreenshotStrategy(new UiAutomatorScreenshotStrategy());
    }

    @Test
    public void takeScreenshotNewCard() {
        onView(withId(R.id.add_button)).perform(click());
        Screengrab.screenshot("01-NewCard");
    }

    @Test
    public void takeScreenshotCardList() {
        onView(withId(R.id.toolbar)).check(matches(isDisplayed()));
        Screengrab.screenshot("02-CardList");
    }

    @Test
    public void takeScreenshotCardDetail() {
        int cardIndex = 0;
        onView(atPosition(cardIndex, instanceOf(BaseballCardView.class)))
            .perform(click());
        Screengrab.screenshot("03-CardDetail");
    }

    @Test
    public void takeScreenshotFilterCards() {
        onView(withId(R.id.filter_menu)).perform(click());
        onView(withId(R.id.year_check)).perform(click());
        onView(withId(R.id.year_input))
            .perform(scrollTo(), clearText(), typeTextIntoFocusedView(YEAR_STRING));
        Screengrab.screenshot("04-FilterCards");
    }

    @Test
    public void takeScreenshotFilteredList() {
        onView(withId(R.id.filter_menu)).perform(click());
        onView(withId(R.id.year_check)).perform(click());
        onView(withId(R.id.year_input))
            .perform(scrollTo(), clearText(), typeTextIntoFocusedView(YEAR_STRING));
        onView(withId(R.id.confirm_button)).perform(click());
        Screengrab.screenshot("05-FilteredList");
    }
}
