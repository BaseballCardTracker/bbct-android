package bbct.android.lite.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import bbct.android.lite.activity.test.LiteActivityNavigateUpFromAboutTest;
import bbct.android.lite.activity.test.LiteActivityNavigateUpFromBaseballCardDetailsTest;
import bbct.android.lite.activity.test.LiteActivityNavigateUpFromBaseballCardListTest;
import bbct.android.lite.activity.test.LiteActivityNavigateUpFromFilterCardsTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    LiteActivityNavigateUpFromAboutTest.class,
    LiteActivityNavigateUpFromBaseballCardDetailsTest.class,
    LiteActivityNavigateUpFromBaseballCardListTest.class,
    LiteActivityNavigateUpFromFilterCardsTest.class
})
public class NavigateUpTestSuite {
}
