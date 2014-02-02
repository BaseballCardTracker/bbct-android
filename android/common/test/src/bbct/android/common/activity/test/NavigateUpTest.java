package bbct.android.common.activity.test;

import android.test.ActivityInstrumentationTestCase2;
import bbct.android.common.activity.About;
import bbct.android.common.activity.BaseballCardDetails;
import bbct.android.common.activity.BaseballCardList;
import com.robotium.solo.Solo;
import junit.framework.Assert;

public class NavigateUpTest extends ActivityInstrumentationTestCase2<BaseballCardList> {

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
        this.solo.assertCurrentActivity("Expecting BaseballCardList", BaseballCardList.class);
        this.solo.clickOnActionBarItem(bbct.android.common.R.id.about_menu);
        Assert.assertTrue(this.solo.waitForActivity(About.class, TIMEOUT));
        this.solo.clickOnActionBarHomeButton();
        Assert.assertTrue(this.solo.waitForActivity(BaseballCardList.class, TIMEOUT));
    }

    public void testNavigateUpFromDetails() {
        this.solo.assertCurrentActivity("Expecting BaseballCardList", BaseballCardList.class);
        this.solo.clickOnActionBarItem(bbct.android.common.R.id.add_menu);
        Assert.assertTrue(this.solo.waitForActivity(BaseballCardDetails.class, TIMEOUT));
        this.solo.clickOnActionBarHomeButton();
        Assert.assertTrue(this.solo.waitForActivity(BaseballCardList.class, TIMEOUT));
    }

    private static final int TIMEOUT = 500;

    private Solo solo = null;

}
