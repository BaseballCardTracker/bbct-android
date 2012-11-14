/*
 * This file is part of BBCT for Android.
 *
 * Copyright 2012 codeguru <codeguru@users.sourceforge.net>
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
package bbct.android;

import android.app.Activity;
import android.app.Instrumentation;
import android.database.sqlite.SQLiteDatabase;
import android.test.ActivityInstrumentationTestCase2;
import android.widget.ListView;
import bbct.common.data.BaseballCard;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import junit.framework.Assert;

/**
 *
 * @author codeguru <codeguru@users.sourceforge.net>
 */
public class BaseballCardListWithoutDataTest extends ActivityInstrumentationTestCase2<BaseballCardList> {

    public BaseballCardListWithoutDataTest() {
        super(BaseballCardList.class);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();

        this.inst = this.getInstrumentation();

        this.activity = this.getActivity();
        this.list = (ListView) this.activity.findViewById(android.R.id.list);

        InputStream cardInputStream = this.inst.getContext().getAssets().open(DATA_ASSET);
        this.cardInput = new BaseballCardCsvFileReader(cardInputStream, true);
        this.dbUtil = new DatabaseUtil();
    }

    @Override
    public void tearDown() throws Exception {
        this.activity.finish();
        this.dbUtil.deleteDatabase();
        this.cardInput.close();
        
        super.tearDown();
    }

    public void testPreConditions() {
        Assert.assertNotNull(this.activity);
        Assert.assertNotNull(this.list);

        // Check that database was created with the correct version and table
        SQLiteDatabase db = this.dbUtil.getDatabase();
        Assert.assertNotNull(db);
        Assert.assertEquals(BaseballCardSQLHelper.SCHEMA_VERSION, db.getVersion());
        Assert.assertEquals(BaseballCardSQLHelper.TABLE_NAME, SQLiteDatabase.findEditTable(BaseballCardSQLHelper.TABLE_NAME));
    }

    public void testTitle() {
        String expectedTitle = this.activity.getString(R.string.app_name);

        Assert.assertEquals(expectedTitle, this.activity.getTitle());
    }

    public void testAddCardsMenuItem() {
        Activity cardDetails = BBCTTestUtil.testMenuItem(this.inst, this.activity, R.id.add_menu, BaseballCardDetails.class);

        cardDetails.finish();
        Assert.assertTrue(cardDetails.isFinishing());
    }

    public void testFilterCardsMenuItem() {
        Activity filterOptions = BBCTTestUtil.testMenuItem(this.inst, this.activity, R.id.filter_menu, FilterOptions.class);

        filterOptions.finish();
        Assert.assertTrue(filterOptions.isFinishing());
    }

    public void testAboutMenuItem() {
        Activity about = BBCTTestUtil.testMenuItem(this.inst, this.activity, R.id.about_menu, About.class);

        about.finish();
        Assert.assertTrue(about.isFinishing());
    }

    public void testStartActivityWithEmptyDatabase() {
        Assert.fail("Check that instructions are raised as a Toast.");
    }

    public void testAddCardToEmptyDatabase() throws IOException {
        Activity cardDetails = BBCTTestUtil.testMenuItem(this.inst, this.activity, R.id.add_menu, BaseballCardDetails.class);
        BaseballCard card = this.cardInput.getNextBaseballCard();

        BBCTTestUtil.addCard(this, cardDetails, card);
        BBCTTestUtil.clickCardDetailsDone(this.inst, cardDetails);

        List<BaseballCard> cards = new ArrayList<BaseballCard>();
        cards.add(card);
        BBCTTestUtil.assertListViewContainsItems(cards, this.list);
    }

    public void testAddMultipleCards() throws IOException {
        Activity cardDetails = BBCTTestUtil.testMenuItem(this.inst, this.activity, R.id.add_menu, BaseballCardDetails.class);
        List<BaseballCard> cards = this.cardInput.getAllBaseballCards();

        for (BaseballCard card : cards) {
            BBCTTestUtil.addCard(this, cardDetails, card);
        }

        BBCTTestUtil.clickCardDetailsDone(this.inst, cardDetails);
        BBCTTestUtil.assertListViewContainsItems(cards, this.list);
    }
    private Instrumentation inst = null;
    private Activity activity = null;
    private BaseballCardCsvFileReader cardInput = null;
    private DatabaseUtil dbUtil = null;
    private ListView list = null;
    private static final String DATA_ASSET = "cards.csv";
}
