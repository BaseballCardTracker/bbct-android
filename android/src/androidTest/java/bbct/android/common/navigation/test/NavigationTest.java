package bbct.android.common.navigation.test;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import bbct.android.common.R;
import bbct.android.common.activity.BaseballCardList;

public class NavigationTest {
    @Test
    public void appStartsOnListView() {
        Assert.fail("NOT IMPLEMENTED");
    }

    @Test
    public void clickOnAddButtonNavigatesToDetails() {
        NavController navController = Mockito.mock(NavController.class);
        FragmentScenario<BaseballCardList> listScenario = FragmentScenario.launchInContainer(BaseballCardList.class);
        listScenario.onFragment(fragment ->
            Navigation.setViewNavController(fragment.requireView(), navController)
        );
        Espresso.onView(ViewMatchers.withId(R.id.add_button)).perform(ViewActions.click());
        Mockito.verify(navController).navigate(R.id.action_details);
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
