package bbct.android.common.navigation.test;

import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

import bbct.android.common.R;
import bbct.android.common.test.rule.DataTestRule;
import bbct.android.lite.provider.LiteActivity;
import bbct.data.BaseballCard;

import static android.support.test.InstrumentationRegistry.getInstrumentation;
import static android.support.test.espresso.Espresso.onData;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;

/**
 * Created by Joselito Narte Jr. on 16/03/2018.
 */

@RunWith(AndroidJUnit4.class)
public class NavigateUpFromBaseballCardDetailsTest {
    @Rule
    public DataTestRule dataTestRule = new DataTestRule();

    @Rule
    public ActivityTestRule<LiteActivity> activityActivityTestRule = new ActivityTestRule<LiteActivity>(LiteActivity.class);

    @Before
    public void setUp() throws Exception {
        activityActivityTestRule.getActivity()
                .getSupportFragmentManager().beginTransaction();
    }

    @Test
    public void testDefaultNavigateUpWithNoData() {
        String cardDetailsTitle = getInstrumentation().getTargetContext().getString(R.string.card_details_title);
        String expectedTitle = getInstrumentation().getTargetContext().getString(R.string.bbct_title, cardDetailsTitle);

        onView(withText(expectedTitle)).check(matches(isDisplayed()));
        onView(allOf(withContentDescription(R.string.abc_action_bar_up_description), isDisplayed())).perform(click());
        onView(withText(R.string.app_name)).check(matches(isDisplayed()));
    }

    @Test
    public void testWithDataSelectedThenNavigateUp() {

        String initialTitle = (String) activityActivityTestRule.getActivity().getTitle();

        List<BaseballCard> allCards = dataTestRule.getAllCards();
        Assert.assertTrue(allCards.size() > 0);

        int cardIndex = 0;
        BaseballCard expectedCard = allCards.get(cardIndex);
        onData(allOf(instanceOf(BaseballCard.class), is(expectedCard))).perform(click());

        String cardDetailsTitle = getInstrumentation().getTargetContext().getString(R.string.card_details_title);
        String expectedTitle = getInstrumentation().getTargetContext().getString(R.string.bbct_title, cardDetailsTitle);

        onView(withText(expectedTitle)).check(matches(isDisplayed()));

        onView(allOf(withContentDescription(R.string.abc_action_bar_up_description), isDisplayed())).perform(click());
        onView(withText(initialTitle)).check(matches(isDisplayed()));
    }


}
