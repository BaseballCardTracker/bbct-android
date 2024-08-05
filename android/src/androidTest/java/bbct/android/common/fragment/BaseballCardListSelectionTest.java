package bbct.android.common.fragment;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.longClick;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.isNotChecked;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.instanceOf;
import static bbct.android.common.test.matcher.Matchers.atPosition;
import static bbct.android.common.test.matcher.Matchers.first;

import android.app.Instrumentation;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.UiDevice;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.List;

import bbct.android.common.R;
import bbct.android.common.activity.MainActivity;
import bbct.android.common.database.BaseballCard;
import bbct.android.common.test.BBCTTestUtil;
import bbct.android.common.test.DatabaseUtil;
import bbct.android.common.test.rule.DataTestRule;
import bbct.android.common.view.BaseballCardView;

abstract public class BaseballCardListSelectionTest<T extends MainActivity> {
    @Rule
    public DataTestRule dataTestRule = new DataTestRule();
    @Rule
    public ActivityScenarioRule<T> activityScenarioRule;

    private UiDevice device;
    private Instrumentation inst;
    private DatabaseUtil dbUtil;
    private List<BaseballCard> allCards;

    public BaseballCardListSelectionTest(Class<T> activityClass) {
        activityScenarioRule = new ActivityScenarioRule<>(activityClass);
    }

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
        BBCTTestUtil.assertListContainsItems(this.allCards);
    }

    @Test
    public void testSelectAll() {
        onView(first(withId(R.id.checkmark)))
            .perform(click());
        onView(withId(R.id.select_all_menu))
            .perform(click());
        this.assertAllCheckboxesChecked();
    }

    @Test
    public void testSelectionAfterSaveInstanceState() throws Throwable {
        int index = 1;
        onView(atPosition(index, withId(R.id.checkmark)))
            .perform(click());
        onView(withId(R.id.delete_menu))
            .check(matches(isDisplayed()));
        device.setOrientationLeft();
        onView(atPosition(index, withId(R.id.checkmark)))
            .check(matches(isChecked()));
    }

    @Test
    public void testOnClickCheckboxStartActionMode() {
        int index = 4;
        onView(atPosition(index, withId(R.id.checkmark)))
            .perform(click());
        onView(withId(R.id.delete_menu))
            .check(matches(isDisplayed()));
        onView(withId(R.id.select_all_menu))
            .check(matches(isDisplayed()));
    }

    @Test
    public void testOnClickCheckboxStopActionMode() {
        int index = 4;
        onView(atPosition(index, withId(R.id.checkmark)))
            .perform(click());
        onView(withId(R.id.delete_menu))
            .check(matches(isDisplayed()));
        onView(atPosition(index, withId(R.id.checkmark)))
            .perform(click());
        onView(withId(R.id.delete_menu))
            .check(doesNotExist());
        onView(withId(R.id.select_all_menu))
            .check(doesNotExist());
        assertNoCheckboxesChecked();
    }

    @Test
    public void testOnItemLongClickStartActionMode() {
        int index = 4;
        onView(atPosition(index, instanceOf(BaseballCardView.class)))
            .perform(longClick());
        onView(withId(R.id.delete_menu))
            .check(matches(isDisplayed()));
        onView(withId(R.id.select_all_menu))
            .check(matches(isDisplayed()));
    }

    private void assertAllCheckboxesChecked() {
        for (int i = 0; i < allCards.size(); i++) {
            onView(atPosition(i, instanceOf(BaseballCardView.class)))
                .check(matches(isChecked()));
        }
    }

    private void assertNoCheckboxesChecked() {
        for (int i = 0; i < allCards.size(); i++) {
            onView(atPosition(i, instanceOf(BaseballCardView.class)))
                .check(matches(isNotChecked()));
        }
    }

}
