package bbct.android.premium.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import bbct.android.premium.activity.MainActivityNavigateUpFromAboutTest;
import bbct.android.premium.activity.MainActivityNavigateUpFromBaseballCardDetailsTest;
import bbct.android.premium.activity.MainActivityNavigateUpFromFilterCardsTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    MainActivityNavigateUpFromAboutTest.class,
    MainActivityNavigateUpFromBaseballCardDetailsTest.class,
    MainActivityNavigateUpFromFilterCardsTest.class
})
public class NavigateUpTestSuite {
}
