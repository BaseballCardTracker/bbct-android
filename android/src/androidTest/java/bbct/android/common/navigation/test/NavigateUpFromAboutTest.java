package bbct.android.common.navigation.test;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import bbct.android.common.R;
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

/**
 * Created by Joselito Narte Jr. on 16/03/2018.
 */

@RunWith(AndroidJUnit4.class)
public class NavigateUpFromAboutTest {
    @Rule
    public ActivityTestRule<LiteActivity> activityActivityTestRule = new ActivityTestRule<LiteActivity>(LiteActivity.class);

    @Before
    public void setUp() throws Exception {
        activityActivityTestRule.getActivity()
                .getSupportFragmentManager().beginTransaction();
    }

    @Test
    public void testNavigateToAboutFragment() {
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

        String aboutTitle = getInstrumentation().getTargetContext().getString(R.string.about_title);
        onView(allOf(withText(aboutTitle), isDisplayed())).perform(click());

        String expectedTitle = getInstrumentation().getTargetContext().getString(R.string.bbct_title, aboutTitle);
        onView(withText(expectedTitle)).check(matches(isDisplayed()));
    }

    @Test
    public void testNavigateWithHomeIcon() {
        String initialTitle = (String) activityActivityTestRule.getActivity().getTitle();
        openActionBarOverflowOrOptionsMenu(getInstrumentation().getTargetContext());

        String aboutTitle = getInstrumentation().getTargetContext().getString(R.string.about_title);

        onView(allOf(withText(aboutTitle), isDisplayed())).perform(click());
        String expectedTitle = getInstrumentation().getTargetContext().getString(R.string.bbct_title, aboutTitle);
        onView(withText(expectedTitle)).check(matches(isDisplayed()));

        onView(allOf(withContentDescription(R.string.abc_action_bar_up_description), isDisplayed())).perform(click());
        onView(withText(initialTitle)).check(matches(isDisplayed()));
    }
}
