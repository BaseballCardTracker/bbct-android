package bbct.android.common.activity.test;

import android.app.Activity;
import android.test.ActivityInstrumentationTestCase2;
import bbct.android.common.activity.About;
import bbct.android.common.activity.BaseballCardDetails;
import bbct.android.common.activity.BaseballCardList;
import bbct.android.common.activity.FilterCards;
import bbct.android.common.activity.MainActivity;
import com.robotium.solo.Solo;
import junit.framework.Assert;

/**
 * Tests that the Home icon navigates up to the correct activity.
 */
public class NavigateUpTest extends
        ActivityInstrumentationTestCase2<MainActivity> {

    public NavigateUpTest() {
        super(MainActivity.class);
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

    /**
     * Navigating up from {@link About} should restore {@link BaseballCardList}.
     */
    public void testNavigateUpFromAbout() {
        this.testNavigateUp(bbct.android.common.R.id.about_menu, About.class);
    }

    /**
     * Navigating up from {@link BaseballCardDetails} should restore
     * {@link BaseballCardList}.
     */
    public void testNavigateUpFromDetails() {
        this.testNavigateUp(bbct.android.common.R.id.add_menu,
                MainActivity.class);
    }

    /**
     * Navigating up from {@link FilterCards} should restore
     * {@link BaseballCardList}.
     */
    public void testNavigateUpFromFilterCards() {
        this.testNavigateUp(bbct.android.common.R.id.filter_menu,
                FilterCards.class);
    }

    private void testNavigateUp(int menuId,
            Class<? extends Activity> expectedActivity) {
        this.solo.clickOnActionBarItem(menuId);
        Assert.assertTrue(this.solo.waitForActivity(expectedActivity, TIMEOUT));
        this.solo.clickOnActionBarHomeButton();
        Assert.assertTrue(this.solo.waitForActivity(MainActivity.class,
                TIMEOUT));
    }

    private static final int TIMEOUT = 500;
    private Solo solo = null;

}
