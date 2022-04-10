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
package bbct.android.common.functional.survey;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;

import androidx.test.espresso.intent.Intents;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import bbct.android.common.R;
import bbct.android.common.SharedPreferenceKeys;
import bbct.android.common.activity.MainActivity;
import bbct.android.common.test.rule.Survey1SharedPreferencesTestRule;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasData;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.startsWith;

public class Survey1DialogTest {
    @Rule
    public Survey1SharedPreferencesTestRule prefsRule = new Survey1SharedPreferencesTestRule();

    private SharedPreferences prefs;
    private Context context;

    @Before
    public void setUp() {
        context = InstrumentationRegistry.getInstrumentation().getTargetContext();
        startApp();
        Intents.init();
        prefs = prefsRule.getPrefs();
    }

    @After
    public void tearDown() {
        Intents.release();
    }

    @Test
    public void testPreconditions() {
        Assert.assertFalse(prefs.contains(SharedPreferenceKeys.SURVEY_TAKEN_PREF));
        Assert.assertFalse(prefs.contains(SharedPreferenceKeys.SURVEY1_DATE));
        Assert.assertTrue(prefs.contains(SharedPreferenceKeys.INSTALL_DATE));
    }

    @Test
    public void testTakeSurveyNow() {
        onView(withText(R.string.survey1))
                .check(matches(isDisplayed()));
        onView(withText(R.string.now))
                .check(matches(isDisplayed()))
                .perform(click());
        Uri surveyUri = Uri.parse(MainActivity.SURVEY1_URI);
        intended(allOf(hasAction(Intent.ACTION_VIEW), hasData(surveyUri)));
        startApp();
        Assert.assertTrue(prefs.contains(SharedPreferenceKeys.SURVEY1_DATE));
        onView(withText(startsWith("BBCT")))
                .check(matches(isDisplayed()));
        onView(withText(R.string.survey1))
                .check(doesNotExist());
    }

    @Test
    public void testTakeSurveyLater() {
        onView(withText(R.string.survey1))
                .check(matches(isDisplayed()));
        onView(withText(R.string.later))
                .check(matches(isDisplayed()))
                .perform(click());
        startApp();
        Assert.assertFalse(prefs.contains(SharedPreferenceKeys.SURVEY_TAKEN_PREF));
        onView(withText(R.string.survey1))
                .check(matches(isDisplayed()));
    }

    private void startApp() {
        String packageName = context.getPackageName();
        Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
        assert intent != null;
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        context.startActivity(intent);
    }
}
