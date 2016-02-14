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
package bbct.android.common.activity.test;

import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import bbct.android.common.R;
import bbct.android.common.activity.FilterCards;
import bbct.android.common.activity.FragmentTags;
import bbct.android.common.activity.MainActivity;
import bbct.android.common.data.BaseballCard;
import bbct.android.common.test.BBCTTestUtil;
import bbct.android.common.test.BBCTTestUtil.EditTexts;
import bbct.android.common.test.BaseballCardCsvFileReader;
import bbct.android.common.test.DatabaseUtil;
import bbct.android.common.test.Predicate;
import java.io.InputStream;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import junit.framework.Test;
import junit.framework.TestSuite;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.withId;

/**
 * A parameterized test which can test filter correctness using any combination
 * of input in the {@link FilterCards} activity.
 */
public class FilterCardsPartialInputTest extends
        ActivityInstrumentationTestCase2<MainActivity> {

    private static final String CARD_DATA = "cards.csv";
    private static final String TEST_NAME = "testFilterCombination";
    private final Set<BBCTTestUtil.EditTexts> inputFieldsMask;

    private List<BaseballCard> allCards;
    private BaseballCard testCard;
    private Instrumentation inst = null;
    private DatabaseUtil dbUtil = null;

    public static Test suite() {
        TestSuite suite = new TestSuite();
        Set<BBCTTestUtil.EditTexts> editTexts = EnumSet.allOf(BBCTTestUtil.EditTexts.class);
        editTexts.remove(BBCTTestUtil.EditTexts.COUNT);
        editTexts.remove(BBCTTestUtil.EditTexts.PLAYER_POSITION);
        editTexts.remove(BBCTTestUtil.EditTexts.VALUE);

        for (BBCTTestUtil.EditTexts editText : editTexts) {
            Set<BBCTTestUtil.EditTexts> mask = new HashSet<>();
            mask.add(editText);

            if (!mask.isEmpty()) {
                suite.addTest(new FilterCardsPartialInputTest(mask));
            }
        }

        return suite;
    }

    public FilterCardsPartialInputTest(Set<BBCTTestUtil.EditTexts> inputFieldsFlags) {
        super(MainActivity.class);

        this.setName(TEST_NAME);
        this.inputFieldsMask = inputFieldsFlags;
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();

        this.inst = this.getInstrumentation();
        InputStream cardInputStream = this.inst.getContext().getAssets().open(CARD_DATA);
        BaseballCardCsvFileReader cardInput = new BaseballCardCsvFileReader(
                cardInputStream, true);
        this.allCards = cardInput.getAllBaseballCards();
        this.testCard = this.allCards.get(1);
        cardInput.close();

        this.dbUtil = new DatabaseUtil(this.inst.getTargetContext());
        this.dbUtil.populateTable(this.allCards);

        this.inst.setInTouchMode(true);
        getActivity();
    }

    @Override
    public void tearDown() throws Exception {
        this.dbUtil.deleteDatabase();

        super.tearDown();
    }

    public void testFilterCombination() {
        BBCTTestUtil.testMenuItem(R.id.filter_menu, FragmentTags.FILTER_CARDS);

        final Set<BBCTTestUtil.EditTexts> mask = inputFieldsMask;
        final BaseballCard test = testCard;
        Predicate<BaseballCard> filterPred = new Predicate<BaseballCard>() {
            @Override
            public boolean doTest(BaseballCard card) {
                boolean condition = true;

                if (mask.contains(EditTexts.BRAND)) {
                    condition = card.getBrand().equals(test.getBrand());
                }

                if (mask.contains(EditTexts.YEAR)) {
                    condition = condition && card.getYear() == test.getYear();
                }

                if (mask.contains(EditTexts.NUMBER)) {
                    condition = condition
                            && card.getNumber() == test.getNumber();
                }

                if (mask.contains(EditTexts.PLAYER_NAME)) {
                    condition = condition
                            && card.getPlayerName()
                                    .equals(test.getPlayerName());
                }

                if (mask.contains(EditTexts.TEAM)) {
                    condition = condition
                            && card.getTeam().equals(test.getTeam());
                }

                return condition;
            }
        };

        BBCTTestUtil.sendKeysToFilterCards(testCard, inputFieldsMask);
        onView(withId(R.id.save_menu)).perform(click());
        List<BaseballCard> expectedCards = BBCTTestUtil.filterList(allCards, filterPred);
        BBCTTestUtil.assertListViewContainsItems(expectedCards);
    }

}
