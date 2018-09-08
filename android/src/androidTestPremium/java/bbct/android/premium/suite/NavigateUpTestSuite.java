package bbct.android.premium.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import bbct.android.premium.activity.test.MainActivityNavigateUpFromAboutTest;
import bbct.android.premium.activity.test.MainActivityNavigateUpFromBaseballCardDetailsTest;
import bbct.android.premium.activity.test.MainActivityNavigateUpFromBaseballCardListTest;
import bbct.android.premium.activity.test.MainActivityNavigateUpFromFilterCardsTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    MainActivityNavigateUpFromAboutTest.class,
    MainActivityNavigateUpFromBaseballCardDetailsTest.class,
    MainActivityNavigateUpFromBaseballCardListTest.class,
    MainActivityNavigateUpFromFilterCardsTest.class
})
public class NavigateUpTestSuite {
}
