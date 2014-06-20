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

import android.app.Activity;
import android.app.Instrumentation;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.ListView;
import bbct.android.common.R;
import bbct.android.common.activity.FilterCards;
import bbct.android.common.activity.MainActivity;
import bbct.android.common.data.BaseballCard;
import bbct.android.common.test.BBCTTestUtil;
import bbct.android.common.test.BBCTTestUtil.EditTexts;
import bbct.android.common.test.BaseballCardCsvFileReader;
import bbct.android.common.test.DatabaseUtil;
import bbct.android.common.test.Predicate;
import com.robotium.solo.Solo;
import java.io.InputStream;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * A parameterized test which can test filter correctness using any combination
 * of input in the {@link FilterCards} activity.
 */
public class FilterCardsPartialInputTest extends
        ActivityInstrumentationTestCase2<MainActivity> {

    public static Test suite() {
        TestSuite suite = new TestSuite();
        Set<BBCTTestUtil.EditTexts> editTexts = EnumSet
                .allOf(BBCTTestUtil.EditTexts.class);
        editTexts.remove(BBCTTestUtil.EditTexts.COUNT);
        editTexts.remove(BBCTTestUtil.EditTexts.PLAYER_POSITION);
        editTexts.remove(BBCTTestUtil.EditTexts.VALUE);
        Set<Set<BBCTTestUtil.EditTexts>> masks = BBCTTestUtil
                .powerSet(editTexts);

        for (Set<BBCTTestUtil.EditTexts> mask : masks) {
            if (!mask.isEmpty()) {
                suite.addTest(new FilterCardsPartialInputTest(mask));
            }
        }

        return suite;
    }

    public FilterCardsPartialInputTest(
            Set<BBCTTestUtil.EditTexts> inputFieldsFlags) {
        super(MainActivity.class);

        this.setName(TEST_NAME);
        this.inputFieldsMask = inputFieldsFlags;
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();

        this.inst = this.getInstrumentation();
        InputStream cardInputStream = this.inst.getContext().getAssets()
                .open(BBCTTestUtil.CARD_DATA);
        BaseballCardCsvFileReader cardInput = new BaseballCardCsvFileReader(
                cardInputStream, true);
        this.allCards = cardInput.getAllBaseballCards();
        cardInput.close();

        this.dbUtil = new DatabaseUtil(this.inst.getTargetContext());
        this.dbUtil.populateTable(this.allCards);

        this.activity = this.getActivity();
        this.solo = new Solo(this.inst, this.activity);
        this.testCard = this.allCards.get(1);
        this.listView = (ListView) this.activity
                .findViewById(android.R.id.list);
    }

    @Override
    public void tearDown() throws Exception {
        this.solo.finishOpenedActivities();
        this.dbUtil.deleteDatabase();

        super.tearDown();
    }

    public void testFilterCombination() {
        BBCTTestUtil.testMenuItem(this.solo, R.id.filter_menu, FilterCards.class);

        final Set<BBCTTestUtil.EditTexts> mask = this.inputFieldsMask;
        final BaseballCard test = this.testCard;
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

        BBCTTestUtil.sendKeysToFilterCards(this.solo, this.testCard,
                this.inputFieldsMask);
        this.solo.clickOnActionBarItem(R.id.save_menu);

        this.inst.waitForIdleSync();
        List<BaseballCard> expectedCards = BBCTTestUtil.filterList(this.allCards, filterPred);
        BBCTTestUtil.assertListViewContainsItems(expectedCards, this.listView);
    }

    private List<BaseballCard> allCards;
    private BaseballCard testCard;
    private Solo solo = null;
    private Instrumentation inst = null;
    private Activity activity = null;
    private DatabaseUtil dbUtil = null;
    private ListView listView = null;
    private final Set<BBCTTestUtil.EditTexts> inputFieldsMask;
    private static final String TEST_NAME = "testFilterCombination";
}
