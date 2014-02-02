package bbct.android.common.activity.test;

import android.test.ActivityInstrumentationTestCase2;
import bbct.android.common.activity.About;
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

    public void testNavigateUpFromAbout() {
        this.solo.clickOnActionBarItem(bbct.android.common.R.id.about_menu);
        Assert.assertTrue(this.solo.waitForActivity(About.class));
        this.solo.clickOnActionBarHomeButton();
        Assert.assertTrue(this.solo.waitForActivity(BaseballCardList.class));
    }

    private Solo solo = null;

}
