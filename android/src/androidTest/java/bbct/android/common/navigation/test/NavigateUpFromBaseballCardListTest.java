package bbct.android.common.navigation.test;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import bbct.android.common.R;
import bbct.android.lite.provider.LiteActivity;
import bbct.data.BaseballCard;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onView;
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
public class NavigateUpFromBaseballCardListTest {

    @Rule
    public ActivityTestRule<LiteActivity> activityActivityTestRule = new ActivityTestRule<LiteActivity>(LiteActivity.class);

    private List<BaseballCard> allCards;

    @Before
    public void setUp() throws Exception {
        activityActivityTestRule.getActivity()
                .getSupportFragmentManager().beginTransaction();
    }

    @Test
    public void testNoDataNavigateUp() {
        String expectedTitle = getInstrumentation().getTargetContext().getString(R.string.app_name);
        onView(allOf(withContentDescription(R.string.abc_action_bar_up_description), isDisplayed())).perform(click());
        onView(withText(expectedTitle)).check(matches(isDisplayed()));
    }

}
