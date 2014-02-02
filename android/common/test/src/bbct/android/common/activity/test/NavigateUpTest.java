package bbct.android.common.activity.test;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import bbct.android.common.activity.About;
import bbct.android.common.activity.BaseballCardDetails;
import bbct.android.common.activity.BaseballCardList;
import bbct.android.common.activity.FilterOptions;
import bbct.android.common.activity.filter.FilterActivity;
import bbct.android.common.activity.filter.NumberFilter;
import bbct.android.common.activity.filter.PlayerNameFilter;
import bbct.android.common.activity.filter.TeamFilter;
import bbct.android.common.activity.filter.YearAndNumberFilter;
import bbct.android.common.activity.filter.YearFilter;
import com.robotium.solo.Solo;
import junit.framework.Assert;

public class NavigateUpTest extends
        ActivityInstrumentationTestCase2<BaseballCardList> {

    public NavigateUpTest() {
        super(BaseballCardList.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();

        this.solo = new Solo(this.getInstrumentation(), this.getActivity());
    }

    @Override
    protected void tearDown() throws Exception {
        this.solo.finishOpenedActivities();

        super.tearDown();
    }

    public void testNavigateUpFromAbout() {
        this.testNavigateUp(bbct.android.common.R.id.about_menu, About.class);
    }

    public void testNavigateUpFromDetails() {
        this.testNavigateUp(bbct.android.common.R.id.add_menu,
                BaseballCardDetails.class);
    }

    public void testNavigateUpFromFilterOptions() {
        this.testNavigateUp(bbct.android.common.R.id.filter_menu,
                FilterOptions.class);
    }

    public void testNavigateUpFromYearFilter() {
        this.testNavigateUpFromFilter(YEAR_RADIO_BUTTON_INDEX, YearFilter.class);
    }

    public void testNavigateUpFromNumberFilter() {
        this.testNavigateUpFromFilter(NUMBER_RADIO_BUTTON_INDEX,
                NumberFilter.class);
    }

    public void testNavigateUpFromYearAndNumberFilter() {
        this.testNavigateUpFromFilter(YEAR_AND_NUMBER_RADIO_BUTTON_INDEX,
                YearAndNumberFilter.class);
    }

    public void testNavigateUpFromPlayerNameFilter() {
        this.testNavigateUpFromFilter(PLAYER_NAME_RADIO_BUTTON_INDEX,
                PlayerNameFilter.class);
    }

    public void testNavigateUpFromTeamFilter() {
        this.testNavigateUpFromFilter(TEAM_RADIO_BUTTON_INDEX, TeamFilter.class);
    }

    private void testNavigateUp(int menuId,
            Class<? extends Activity> expectedActivity) {
        this.solo.clickOnActionBarItem(menuId);
        Assert.assertTrue(this.solo.waitForActivity(expectedActivity, TIMEOUT));
        this.solo.clickOnActionBarHomeButton();
        Assert.assertTrue(this.solo.waitForActivity(BaseballCardList.class,
                TIMEOUT));
    }

    private void testNavigateUpFromFilter(int filterIndex,
            Class<? extends FilterActivity> filterActivity) {
        this.solo.clickOnActionBarItem(bbct.android.common.R.id.filter_menu);
        Assert.assertTrue(this.solo.waitForActivity(FilterOptions.class,
                TIMEOUT));
        this.solo.clickOnRadioButton(filterIndex);
        Assert.assertTrue(this.solo.waitForActivity(filterActivity, TIMEOUT));
        this.solo.clickOnActionBarHomeButton();
        Assert.assertTrue(this.solo.waitForActivity(FilterOptions.class,
                TIMEOUT));
    }

    private static final int TIMEOUT = 500;
    private static final int YEAR_RADIO_BUTTON_INDEX = 0;
    private static final int NUMBER_RADIO_BUTTON_INDEX = 1;
    private static final int YEAR_AND_NUMBER_RADIO_BUTTON_INDEX = 2;
    private static final int PLAYER_NAME_RADIO_BUTTON_INDEX = 3;
    private static final int TEAM_RADIO_BUTTON_INDEX = 4;

    private Solo solo = null;

}
