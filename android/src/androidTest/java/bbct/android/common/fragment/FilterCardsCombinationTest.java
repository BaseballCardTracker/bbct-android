/*
 * This file is part of BBCT for Android.
 *
 * Copyright 2012-14 codeguru <codeguru@users.sourceforge.net>
 *
 * BBCT for Android is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * BBCT for Android is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package bbct.android.common.fragment;

import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.util.Log;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;

import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import bbct.android.common.R;
import bbct.android.common.activity.MainActivity;
import bbct.android.common.database.BaseballCard;
import bbct.android.common.test.BBCTTestUtil;
import bbct.android.common.test.BaseballCardCsvFileReader;
import bbct.android.common.test.DatabaseUtil;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static bbct.android.common.test.matcher.RecyclerViewMatcher.contains;

abstract public class FilterCardsCombinationTest<T extends MainActivity> extends
        ActivityInstrumentationTestCase2<T> {
    private static final String TAG = FilterCardsCombinationTest.class.getName();
    private static final String CARD_DATA = "cards.csv";
    private static final String TEST_NAME = "testFilter";
    private final Set<BBCTTestUtil.FilterOption> inputFieldsMask;

    private List<BaseballCard> allCards;
    private BaseballCard testCard;
    private DatabaseUtil dbUtil = null;

    public static <S extends FilterCardsCombinationTest> Test suite(Class<S> testClass)
            throws ReflectiveOperationException {
        TestSuite suite = new TestSuite();
        Set<BBCTTestUtil.FilterOption> options = EnumSet.allOf(BBCTTestUtil.FilterOption.class);

        for (BBCTTestUtil.FilterOption option : options) {
            Set<BBCTTestUtil.FilterOption> mask = new HashSet<>();
            mask.add(option);

            if (!mask.isEmpty()) {
                Constructor<S> ctor = testClass.getConstructor(Set.class);
                suite.addTest(ctor.newInstance(mask));
            }
        }

        return suite;
    }

    public FilterCardsCombinationTest(Class<T> activityClass, Set<BBCTTestUtil.FilterOption> inputFieldsFlags) {
        super(activityClass);

        this.setName(TEST_NAME);
        this.inputFieldsMask = inputFieldsFlags;
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();

        Instrumentation inst = this.getInstrumentation();
        InputStream cardInputStream = inst.getContext().getAssets().open(CARD_DATA);
        BaseballCardCsvFileReader cardInput = new BaseballCardCsvFileReader(
                cardInputStream, true);
        this.allCards = cardInput.getAllBaseballCards();
        this.testCard = this.allCards.get(1);
        cardInput.close();

        this.dbUtil = new DatabaseUtil(inst.getTargetContext());
        this.dbUtil.populateTable(this.allCards);

        inst.setInTouchMode(true);
        getActivity();
    }

    @Override
    public void tearDown() throws Exception {
        this.dbUtil.deleteDatabase();

        super.tearDown();
    }

    public void testFilter() {
        Log.d(TAG, "inputFieldsMask=" + inputFieldsMask);

        BBCTTestUtil.testMenuItem(R.id.filter_menu, FragmentTags.FILTER_CARDS);

        final Set<BBCTTestUtil.FilterOption> mask = inputFieldsMask;
        final BaseballCard test = testCard;
        Matcher<BaseballCard> cardMatcher = new BaseMatcher<BaseballCard>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("matching card");
            }

            @Override
            public boolean matches(Object obj) {
                BaseballCard card = (BaseballCard)obj;
                boolean condition = true;

                if (mask.contains(BBCTTestUtil.FilterOption.BRAND)) {
                    condition = card.brand.equals(test.brand);
                }

                if (mask.contains(BBCTTestUtil.FilterOption.YEAR)) {
                    condition = condition && card.year.equals(test.year);
                }

                if (mask.contains(BBCTTestUtil.FilterOption.NUMBER)) {
                    condition = condition && card.number.equals(test.number);
                }

                if (mask.contains(BBCTTestUtil.FilterOption.PLAYER_NAME)) {
                    condition = condition && card.playerName.equals(test.playerName);
                }

                if (mask.contains(BBCTTestUtil.FilterOption.TEAM)) {
                    condition = condition && card.team.equals(test.team);
                }

                return condition;
            }
        };

        BBCTTestUtil.sendKeysToFilterCards(testCard, inputFieldsMask);
        onView(withId(R.id.confirm_button)).perform(click());
        List<BaseballCard> expectedCards = BBCTTestUtil.filterList(allCards, cardMatcher);
        onView(withId(R.id.card_list))
            .check(matches(contains(expectedCards)));
    }
}
