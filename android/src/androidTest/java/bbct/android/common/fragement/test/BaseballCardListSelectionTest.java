package bbct.android.common.fragement.test;

import android.app.Instrumentation;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.UiDevice;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import bbct.android.common.R;
import bbct.android.common.activity.BaseballCardList;
import bbct.android.common.database.BaseballCard;
import bbct.android.common.test.BBCTTestUtil;
import bbct.android.common.test.DatabaseUtil;
import bbct.android.common.test.rule.DataTestRule;
import bbct.android.common.test.rule.SupportFragmentTestRule;

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
    public SupportFragmentTestRule fragmentTestRule =
        new SupportFragmentTestRule(new BaseballCardList());

    private UiDevice device;
    private Instrumentation inst;
    private DatabaseUtil dbUtil;
    private List<BaseballCard> allCards;

    @Before
    public void setUp() {
        inst = InstrumentationRegistry.getInstrumentation();
        device = UiDevice.getInstance(inst);
        allCards = dataTestRule.getAllCards();
        dbUtil = new DatabaseUtil(inst.getTargetContext());
    }

    @After
    public void tearDown() throws Throwable{
        device.setOrientationNatural();
    }

    @Test
    public void testPreConditions() {
        BBCTTestUtil.assertDatabaseCreated(inst.getTargetContext());
        Assert.assertTrue(dbUtil.containsAllBaseballCards(this.allCards));
        BBCTTestUtil.assertListViewContainsItems(this.allCards);
    }

    @Test
    public void testSelectAll() {
        onView(withRecyclerView(R.id.card_list).atPositionOnView(0, R.id.checkmark))
            .perform(click());
        onView(withId(R.id.select_all_menu))
            .perform(click());
        this.assertAllCheckboxesChecked();
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
        onView(withId(R.id.select_all_menu))
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
        onView(withId(R.id.select_all_menu))
            .check(matches(not(isDisplayed())));
        assertNoCheckboxesChecked();
    }

    @Test
    public void testOnItemLongClickStartActionMode() {
        int index = 4;
        onView(withRecyclerView(R.id.card_list).atPosition(index))
            .perform(longClick())
            .check(matches(isChecked()));
        onView(withId(R.id.delete_menu))
            .check(matches(isDisplayed()));
        onView(withId(R.id.select_all_menu))
            .check(matches(isDisplayed()));
    }

    private void assertAllCheckboxesChecked() {
        for (int i = 0; i < allCards.size(); i++) {
            onView(
                withRecyclerView(R.id.card_list)
                    .atPositionOnView(i, R.id.checkmark)
            ).check(matches(isChecked()));
        }
    }

    private void assertNoCheckboxesChecked() {
        for (int i = 0; i < allCards.size(); i++) {
            onView(withRecyclerView(R.id.card_list).atPositionOnView(i, R.id.checkmark))
                .check(matches(isNotChecked()));
        }
    }

}
