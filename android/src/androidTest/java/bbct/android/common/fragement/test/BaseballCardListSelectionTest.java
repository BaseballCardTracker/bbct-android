package bbct.android.common.fragement.test;

import android.app.Activity;
import android.app.Instrumentation;

import androidx.test.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.uiautomator.UiDevice;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import bbct.android.common.R;
import bbct.android.common.test.BBCTTestUtil;
import bbct.android.common.test.DatabaseUtil;
import bbct.android.common.test.rule.DataTestRule;
import bbct.android.lite.provider.LiteActivity;
import bbct.data.BaseballCard;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.longClick;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isNotChecked;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static bbct.android.common.test.matcher.RecyclerViewMatcher.withRecyclerView;
import static org.hamcrest.Matchers.not;

public class BaseballCardListSelectionTest {
    @Rule
    public DataTestRule dataTestRule = new DataTestRule();
    @Rule
    public ActivityTestRule<LiteActivity> activityTestRule = new ActivityTestRule<>(LiteActivity.class);

    private UiDevice device;
    private Activity activity;
    private Instrumentation inst;
    private DatabaseUtil dbUtil;
    private List<BaseballCard> allCards;

    @Before
    public void setUp() {
        inst = InstrumentationRegistry.getInstrumentation();
        device = UiDevice.getInstance(inst);
        activity = activityTestRule.getActivity();
        allCards = dataTestRule.getAllCards();
        dbUtil = new DatabaseUtil(inst.getTargetContext());
    }

    @Test
    public void testPreConditions() {
        Assert.assertNotNull(this.activity);
        BBCTTestUtil.assertDatabaseCreated(inst.getTargetContext());
        Assert.assertTrue(dbUtil.containsAllBaseballCards(this.allCards));
        BBCTTestUtil.assertListViewContainsItems(this.allCards);
    }

    @Test
    public void testMarkAll() {
        this.markAll();
        this.assertAllCheckboxesChecked();
    }

    @Test
    public void testUnmarkAll() {
        this.markAll();
        onView(withId(R.id.select_all)).perform(click());
        assertNoCheckboxesChecked();
    }

    @Test
    public void testSelectionAfterSaveInstanceState() throws Throwable {
        int index = 1;
        onView(withRecyclerView(R.id.card_list).atPositionOnView(index, R.id.checkmark))
            .perform(click());
        onView(withId(R.id.delete_menu))
            .check(matches(isDisplayed()));
        device.setOrientationLeft();
        onView(withRecyclerView(R.id.card_list).atPositionOnView(index, R.id.checkmark))
            .check(matches(isChecked()));
    }

    @Test
    public void testOnClickCheckboxStartActionMode() {
        int index = 4;
        onView(withRecyclerView(R.id.card_list).atPositionOnView(index, R.id.checkmark))
            .perform(click())
            .check(matches(isChecked()));
        onView(withId(R.id.delete_menu))
            .check(matches(isDisplayed()));
        onView(withId(R.id.delete_menu))
            .check(matches(isDisplayed()));
    }

    @Test
    public void testOnClickCheckboxStopActionMode() {
        int index = 4;
        onView(withRecyclerView(R.id.card_list).atPositionOnView(index, R.id.checkmark))
            .perform(click());
        onView(withId(R.id.delete_menu))
            .check(matches(isDisplayed()));
        onView(withRecyclerView(R.id.card_list).atPositionOnView(index, R.id.checkmark))
            .perform(click())
            .check(matches(isNotChecked()));
        onView(withId(R.id.delete_menu))
            .check(matches(not(isDisplayed())));
    }

    @Test
    public void testOnClickCheckboxAll() {
        for(int i = 1; i < allCards.size() + 1; ++i) {
            onView(withRecyclerView(R.id.card_list).atPositionOnView(i, R.id.checkmark))
                .perform(click());
        }

        onView(withId(R.id.select_all))
            .check(matches(isChecked()));
    }

    @Test
    public void testOnCheckAllAndOnClickCheckbox() {
        onView(withId(R.id.select_all))
            .perform(click());
        onView(withRecyclerView(R.id.card_list).atPositionOnView(1, R.id.checkmark))
            .perform(click());
        onView(withId(R.id.select_all))
            .check(matches(isNotChecked()));
    }

    @Test
    public void testOnClickCheckboxAndOnCheckAll() {
        onView(withRecyclerView(R.id.card_list).atPositionOnView(1, R.id.checkmark))
            .perform(click());
        onView(withId(R.id.select_all))
            .perform(click());
        this.assertAllCheckboxesChecked();
    }

    @Test
    public void testOnItemLongClickStartActionMode() {
        int index = 4;
        onView(withRecyclerView(R.id.card_list).atPosition(index))
            .perform(longClick())
            .check(matches(isChecked()));
        onView(withId(R.id.delete_menu))
            .check(matches(isDisplayed()));
    }

    private void markAll() {
        onView(withId(R.id.select_all))
            .perform(click())
            .check(matches(isChecked()));
        onView(withId(R.id.delete_menu))
            .check(matches(isDisplayed()));
    }

    private void assertAllCheckboxesChecked() {
        onView(withId(R.id.select_all))
            .check(matches(isChecked()));

        // skip header view
        for (int i = 1; i < allCards.size() + 1; i++) {
            onView(
                withRecyclerView(R.id.card_list)
                    .atPositionOnView(i, R.id.checkmark)
            ).check(matches(isChecked()));
        }
    }

    private void assertNoCheckboxesChecked() {
        onView(withId(R.id.select_all))
            .check(matches(isNotChecked()));

        for (int i = 1; i < allCards.size() + 1; i++) {
            onView(withRecyclerView(R.id.card_list).atPositionOnView(i, R.id.checkmark))
                .check(matches(isNotChecked()));
        }
    }

}
