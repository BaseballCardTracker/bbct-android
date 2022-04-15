package bbct.android.common.navigation;

import static com.google.common.truth.Truth.assertThat;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.testing.TestNavHostController;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;

import org.junit.Assert;
import org.junit.Test;

import java.util.Objects;

import bbct.android.common.R;
import bbct.android.common.fragment.BaseballCardList;
import bbct.android.lite.activity.LiteActivity;

public class NavigationTest {
    @Test
    public void appStartsOnListView() {
        TestNavHostController navController = new TestNavHostController(ApplicationProvider.getApplicationContext());
        ActivityScenario<LiteActivity> activityScenario = ActivityScenario.launch(LiteActivity.class);
        activityScenario.onActivity(activity -> {
            navController.setGraph(R.navigation.nav_graph);
            Navigation.setViewNavController(activity.requireViewById(R.id.nav_host_fragment), navController);
        });
        assertThat(Objects.requireNonNull(navController.getCurrentDestination()).getId()).isEqualTo(R.id.card_list);
    }

    @Test
    public void clickOnAddButtonNavigatesToDetails() {
        TestNavHostController navController = new TestNavHostController(ApplicationProvider.getApplicationContext());
        FragmentScenario<BaseballCardList> listScenario = FragmentScenario.launchInContainer(
            BaseballCardList.class,
            null,
            R.style.AppTheme
        );
        listScenario.onFragment(fragment -> {
            navController.setGraph(R.navigation.nav_graph);
            Navigation.setViewNavController(fragment.requireView(), navController);
        });
        Espresso.onView(ViewMatchers.withId(R.id.add_button)).perform(ViewActions.click());
        assertThat(Objects.requireNonNull(navController.getCurrentDestination()).getId()).isEqualTo(R.id.card_details);
    }

    @Test
    public void clickOnListItemNavigatesToDetails() {
        Assert.fail("NOT IMPLEMENTED");
    }

    @Test
    public void clickOnSearchMenuNavigatesToFilter() {
        TestNavHostController navController = new TestNavHostController(ApplicationProvider.getApplicationContext());
        ActivityScenario<LiteActivity> listScenario = ActivityScenario.launch(LiteActivity.class);
        listScenario.onActivity(activity -> {
            navController.setGraph(R.navigation.nav_graph);
            Navigation.setViewNavController(activity.requireViewById(R.id.nav_host_fragment), navController);
        });
        Espresso.onView(ViewMatchers.withId(R.id.filter_menu)).perform(ViewActions.click());
        assertThat(Objects.requireNonNull(navController.getCurrentDestination()).getId()).isEqualTo(R.id.filter_cards);
    }

    @Test
    public void clickOnApplyNavigatesBackToList() {
        Assert.fail("NOT IMPLEMENTED");
    }

    @Test
    public void clickOnAboutMenuNavigatesToAbout() {
        Assert.fail("NOT IMPLEMENTED");
    }
}
