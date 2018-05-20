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
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.InputStream;
import java.util.List;

import bbct.android.common.activity.BaseballCardDetails;
import bbct.android.common.test.BBCTTestUtil;
import bbct.android.common.test.BaseballCardCsvFileReader;
import bbct.android.common.test.DatabaseUtil;
import bbct.android.common.test.rule.SupportFragmentTestRule;
import bbct.data.BaseballCard;

@RunWith(AndroidJUnit4.class)
public class BaseballCardDetailsAddCardsTest {
    @Rule
    public SupportFragmentTestRule fragmentTestRule =
            new SupportFragmentTestRule(new BaseballCardDetails());

    private static final String CARD_DATA = "three_cards.csv";

    private List<BaseballCard> allCards = null;
    private BaseballCard card = null;
    private DatabaseUtil dbUtil;

    @Before
    public void setUp() throws Exception {
        Instrumentation inst = InstrumentationRegistry.getInstrumentation();
        dbUtil = new DatabaseUtil(inst.getTargetContext());

        InputStream in = inst.getContext().getAssets().open(CARD_DATA);
        BaseballCardCsvFileReader cardInput = new BaseballCardCsvFileReader(in,
                true);
        this.allCards = cardInput.getAllBaseballCards();
        this.card = this.allCards.get(0);
        cardInput.close();
    }

    @After
    public void tearDown() throws Exception {
        dbUtil.clearDatabase();
    }

    @Test
    public void testAddCard() throws Throwable {
        BBCTTestUtil.addCard(card);
        // BBCTTestUtil.waitForToast(fragmentTestRule.getActivity(), BBCTTestUtil.ADD_MESSAGE);
        Assert.assertTrue("Missing card: " + card, dbUtil.containsBaseballCard(card));
    }

    @Test
    public void testAddMultipleCards() throws Throwable {
        for (BaseballCard nextCard : this.allCards) {
            BBCTTestUtil.addCard(nextCard);
            // BBCTTestUtil.waitForToast(this.solo, BBCTTestUtil.ADD_MESSAGE);
        }

        for (BaseballCard nextCard : this.allCards) {
            Assert.assertTrue("Missing card: " + nextCard, dbUtil.containsBaseballCard(nextCard));
        }
    }
}
