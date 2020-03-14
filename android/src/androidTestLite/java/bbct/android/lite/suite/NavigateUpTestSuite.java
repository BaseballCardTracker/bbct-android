package bbct.android.lite.suite;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import bbct.android.lite.activity.LiteActivityNavigateUpFromAboutTest;
import bbct.android.lite.activity.LiteActivityNavigateUpFromBaseballCardDetailsTest;
import bbct.android.lite.activity.LiteActivityNavigateUpFromFilterCardsTest;

@RunWith(Suite.class)
@Suite.SuiteClasses({
    LiteActivityNavigateUpFromAboutTest.class,
    LiteActivityNavigateUpFromBaseballCardDetailsTest.class,
    LiteActivityNavigateUpFromFilterCardsTest.class
})
public class NavigateUpTestSuite {
}
