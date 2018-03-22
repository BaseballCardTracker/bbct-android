package bbct.android.common.navigation.test;

import android.support.test.rule.ActivityTestRule;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import bbct.android.common.R;
import bbct.android.lite.provider.LiteActivity;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

/**
 * Created by Joselito Narte Jr. on 16/03/2018.
 */

public class NavigateUpFromFilterCardsTest {
    @Rule
    public ActivityTestRule<LiteActivity> activityActivityTestRule = new ActivityTestRule<LiteActivity>(LiteActivity.class);

    @Before
    public void setUp() throws Exception {
        activityActivityTestRule.getActivity()
                .getSupportFragmentManager().beginTransaction();
    }

    @Test
    public void testNavigateUp() {

        String expectedTitle = getInstrumentation().getTargetContext().getString(R.string.app_name);
        onView(allOf(withContentDescription(R.string.abc_action_bar_up_description), isDisplayed())).perform(click());
        onView(allOf(withContentDescription(R.string.filter_cards_title), isDisplayed())).perform(click());
        onView(allOf(withContentDescription(R.string.abc_action_bar_up_description), isDisplayed())).perform(click());
        onView(allOf(withText(expectedTitle), isDisplayed()));

    }
}
