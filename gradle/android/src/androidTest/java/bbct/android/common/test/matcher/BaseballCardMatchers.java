package bbct.android.common.test.matcher;

import bbct.android.common.data.BaseballCard;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

public class BaseballCardMatchers {
    public static Matcher<BaseballCard> withYear(final int year) {
        return new BaseMatcher<BaseballCard>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Card year " + year);
            }

            @Override
            public boolean matches(Object obj) {
                BaseballCard card = (BaseballCard)obj;
                return card.getYear() == year;
            }
        };
    }
}
