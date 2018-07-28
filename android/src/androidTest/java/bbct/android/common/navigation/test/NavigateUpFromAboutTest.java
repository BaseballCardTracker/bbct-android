/*
 * This file is part of BBCT for Android.
 *
 * Copyright 2018 codeguru <codeguru@users.sourceforge.net>
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
package bbct.android.common.navigation.test;

import android.content.Context;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import bbct.android.common.R;
import bbct.android.common.test.DatabaseUtil;
import bbct.android.lite.provider.LiteActivity;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;


@RunWith(AndroidJUnit4.class)
public class NavigateUpFromAboutTest {
    @Rule
    public ActivityTestRule<LiteActivity> activityActivityTestRule = new ActivityTestRule<LiteActivity>(LiteActivity.class);
    private Context context = null;
    private DatabaseUtil dbUtil = null;

    @Before
    public void setUp() throws Exception {
        activityActivityTestRule.getActivity()
                .getSupportFragmentManager().beginTransaction();
        context = getInstrumentation().getTargetContext();
        this.dbUtil = new DatabaseUtil(context);
    }

    @Test
    public void testNavigateToBaseballCardDetailsThenAboutFragment() {
        String expectedTitle = context.getString(R.string.app_name);

        // pop to BaseballCardList first if db is empty
        if (this.dbUtil.isEmpty()) {
            onView(allOf(withContentDescription(R.string.abc_action_bar_up_description), isDisplayed())).perform(click());
        }
        // navigate to BaseballCardDetails
        onView(allOf(withContentDescription(R.string.add_menu), isDisplayed())).perform(click());

        // navigate to About
        openActionBarOverflowOrOptionsMenu(context);
        String aboutTitle = context.getString(R.string.about_title);
        onView(allOf(withText(aboutTitle), isDisplayed())).perform(click());

        // Navigate Up
        onView(allOf(withContentDescription(R.string.abc_action_bar_up_description), isDisplayed())).perform(click());
        onView(allOf(withText(expectedTitle), isDisplayed()));
    }

    @Test
    public void testNavigateToFilterThenAboutFragment() {
        String expectedTitle = context.getString(R.string.app_name);

        // pop to BaseballCardList first if db is empty
        if (this.dbUtil.isEmpty()) {
            onView(allOf(withContentDescription(R.string.abc_action_bar_up_description), isDisplayed())).perform(click());
        }

        // navigate to FilterCards
        onView(allOf(withContentDescription(R.string.filter_cards_title), isDisplayed())).perform(click());

        // navigate to About
        openActionBarOverflowOrOptionsMenu(context);
        String aboutTitle = context.getString(R.string.about_title);
        onView(allOf(withText(aboutTitle), isDisplayed())).perform(click());

        // Navigate Up
        onView(allOf(withContentDescription(R.string.abc_action_bar_up_description), isDisplayed())).perform(click());
        onView(allOf(withText(expectedTitle), isDisplayed()));
    }

    @Test
    public void testNavigateToAboutFragment() {
        openActionBarOverflowOrOptionsMenu(context);

        String aboutTitle = context.getString(R.string.about_title);
        onView(allOf(withText(aboutTitle), isDisplayed())).perform(click());

        String expectedTitle = context.getString(R.string.app_name, aboutTitle);
        onView(withText(expectedTitle)).check(matches(isDisplayed()));
    }

    @Test
    public void testNavigateUp() {
        String initialTitle = context.getString(R.string.app_name);
        openActionBarOverflowOrOptionsMenu(context);

        String aboutTitle = context.getString(R.string.about_title);

        onView(allOf(withText(aboutTitle), isDisplayed())).perform(click());
        String expectedTitle = context.getString(R.string.bbct_title, aboutTitle);
        onView(withText(expectedTitle)).check(matches(isDisplayed()));

        onView(allOf(withContentDescription(R.string.abc_action_bar_up_description), isDisplayed())).perform(click());
        onView(withText(initialTitle)).check(matches(isDisplayed()));
    }
}
