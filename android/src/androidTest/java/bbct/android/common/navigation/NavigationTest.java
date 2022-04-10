package bbct.android.common.navigation;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.navigation.Navigation;
import androidx.navigation.testing.TestNavHostController;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;

import org.junit.Assert;
import org.junit.Test;

import bbct.android.common.R;
import bbct.android.common.fragment.BaseballCardList;

public class NavigationTest {
    @Test
    public void appStartsOnListView() {
        Assert.fail("NOT IMPLEMENTED");
    }

    @Test
    public void clickOnAddButtonNavigatesToDetails() {
        TestNavHostController navController = new TestNavHostController(ApplicationProvider.getApplicationContext());
        FragmentScenario<BaseballCardList> listScenario = FragmentScenario.launchInContainer(BaseballCardList.class);
        listScenario.onFragment(fragment ->
            Navigation.setViewNavController(fragment.requireView(), navController)
        );
        Espresso.onView(ViewMatchers.withId(R.id.add_button)).perform(ViewActions.click());
        Assert.assertEquals(navController.getCurrentDestination().getId(), R.id.card_details);
    }

    @Test
    public void clickOnListItemNavigatesToDetails() {
        Assert.fail("NOT IMPLEMENTED");
    }

    @Test
    public void clickOnSearchMenuNavigatesToFilter() {
        Assert.fail("NOT IMPLEMENTED");
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
