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
package bbct.android.common.provider.test;

import android.app.Instrumentation;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.InstrumentationTestCase;
import android.util.Log;
import bbct.android.common.data.BaseballCard;
import bbct.android.common.provider.BaseballCardContract;
import bbct.android.common.provider.BaseballCardSQLHelper;
import bbct.android.common.test.BBCTTestUtil;
import bbct.android.common.test.BaseballCardCsvFileReader;
import bbct.android.common.test.DatabaseUtil;
import java.io.InputStream;
import java.util.List;
import junit.framework.Assert;

/**
 * Tests for {@link BaseballCardSQLHelper}.
 *
 * @author codeguru <codeguru@users.sourceforge.net>
 */
public class BaseballCardSQLHelperTest extends InstrumentationTestCase {

    /**
     *
     * @throws Exception If an error occurs while chaining to the super class.
     */
    @Override
    public void setUp() throws Exception {
        Log.d(TAG, this.getName());

        super.setUp();

        this.inst = this.getInstrumentation();
        this.sqlHelper = new BaseballCardSQLHelper(this.inst.getTargetContext());
        this.db = this.sqlHelper.getWritableDatabase();
        this.dbUtil = new DatabaseUtil(this.inst.getTargetContext());

        InputStream input = this.inst.getContext().getAssets().open(BBCTTestUtil.CARD_DATA);
        BaseballCardCsvFileReader reader = new BaseballCardCsvFileReader(input, true);
        this.allCards = reader.getAllBaseballCards();
        this.card = this.allCards.get(3); // Ken Griffey, Jr.
        reader.close();
    }

    /**
     *
     * @throws Exception If an error occurs while chaining to the super class.
     */
    @Override
    public void tearDown() throws Exception {
        this.dbUtil.deleteDatabase();

        super.tearDown();
    }

    /**
     * Test for {@link BaseballCardSQLHelper#onCreate}.
     */
    public void testOnCreate() {
        Assert.assertNotNull(this.db);
        BBCTTestUtil.assertDatabaseCreated(this.inst.getTargetContext());
        Assert.assertTrue(this.dbUtil.isEmpty());
    }

    /**
     * Test for {@link BaseballCardSQLHelper#onUpgrade}.
     */
    public void testOnUpgrade() {
        int oldVersion = 0;
        int newVersion = 1;
        this.sqlHelper.onUpgrade(this.dbUtil.getDatabase(), oldVersion, newVersion);
        Assert.fail("Check that the database is not modified.");
    }

    /**
     * Test for {@link BaseballCardSQLHelper#insertBaseballCard}.
     */
    public void testInsertBaseballCard() {
        this.sqlHelper.insertBaseballCard(this.card);
        Assert.assertTrue(this.dbUtil.containsBaseballCard(this.card));
    }

    /**
     * Test for {@link BaseballCardSQLHelper#updateBaseballCard}.
     */
    public void testUpdateBaseballCard() {
        String brand = this.card.getBrand();
        int year = this.card.getYear();
        int number = this.card.getNumber();
        int newValue = this.card.getValue() + 150;
        int newCount = this.card.getCount() + 1;
        String name = this.card.getPlayerName();
        String team = this.card.getTeam();
        String position = this.card.getPlayerPosition();
        BaseballCard newCard = new BaseballCard(brand, year, number, newValue, newCount, name, team, position);
        this.sqlHelper.updateBaseballCard(this.card, newCard);

        Assert.assertFalse(this.dbUtil.containsBaseballCard(this.card));
        Assert.assertTrue(this.dbUtil.containsBaseballCard(newCard));
    }

    /**
     * Test for {@link BaseballCardSQLHelper#removeBaseballCard}.
     */
    public void testRemoveBaseballCard() {
        this.sqlHelper.insertBaseballCard(this.card);
        Assert.assertTrue(this.dbUtil.containsBaseballCard(this.card));

        this.sqlHelper.removeBaseballCard(this.card);
        Assert.assertFalse(this.dbUtil.containsBaseballCard(this.card));
    }

    /**
     * Test for {@link BaseballCardSQLHelper#getCursor}.
     */
    public void testGetCursor() {
        Cursor expResult = null;
        Cursor result = this.sqlHelper.getCursor();
        Assert.assertNotNull(result);
        Assert.assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to Assert.fail.
        Assert.fail("The test case is a prototype.");
    }

    /**
     * Test for {@link BaseballCardSQLHelper#clearFilter}.
     */
    public void testClearFilter() {
        this.testFilterCursorByYear();
        this.sqlHelper.clearFilter();
        // TODO review the generated test code and remove the default call to Assert.fail.
        Assert.fail("The test case is a prototype.");
    }

    /**
     * Test for {@link BaseballCardSQLHelper#filterCursorByYear}.
     */
    public void testFilterCursorByYear() {
        int year = 1993;
        this.sqlHelper.filterCursorByYear(year);

        Cursor cursor = this.sqlHelper.getCursor();
        while (cursor.moveToNext()) {
            Assert.assertEquals(year, cursor.getInt(cursor.getColumnIndex(BaseballCardContract.YEAR_COL_NAME)));
        }
    }

    /**
     * Test for {@link BaseballCardSQLHelper#filterCursorByNumber}.
     */
    public void testFilterCursorByNumber() {
        int number = 201;
        this.sqlHelper.filterCursorByNumber(number);

        Cursor cursor = this.sqlHelper.getCursor();
        while (cursor.moveToNext()) {
            Assert.assertEquals(number, cursor.getInt(cursor.getColumnIndex(BaseballCardContract.NUMBER_COL_NAME)));
        }
    }

    /**
     * Test for {@link BaseballCardSQLHelper#filterCursorByYearAndNumber}.
     */
    public void testFilterCursorByYearAndNumber() {
        int year = 1985;
        int number = 201;
        this.sqlHelper.filterCursorByYearAndNumber(year, number);

        Cursor cursor = this.sqlHelper.getCursor();
        while (cursor.moveToNext()) {
            Assert.assertEquals(year, cursor.getInt(cursor.getColumnIndex(BaseballCardContract.YEAR_COL_NAME)));
            Assert.assertEquals(number, cursor.getInt(cursor.getColumnIndex(BaseballCardContract.NUMBER_COL_NAME)));
        }
    }

    /**
     * Test for {@link BaseballCardSQLHelper#filterCursorByPlayerName}.
     */
    public void testFilterCursorByPlayerName() {
        String playerName = "Tom Browning";
        this.sqlHelper.filterCursorByPlayerName(playerName);

        Cursor cursor = this.sqlHelper.getCursor();
        while (cursor.moveToNext()) {
            Assert.assertEquals(playerName, cursor.getInt(cursor.getColumnIndex(BaseballCardContract.PLAYER_NAME_COL_NAME)));
        }
    }

    /**
     * Test for {@link BaseballCardSQLHelper#getBaseballCardFromCursor}.
     */
    public void testGetBaseballCardFromCursor() {
        System.out.println("getBaseballCardFromCursor");
        BaseballCard expResult = null;
        BaseballCard result = this.sqlHelper.getBaseballCardFromCursor();
        Assert.assertNotNull(result);
        Assert.assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to Assert.fail.
        Assert.fail("The test case is a prototype.");
    }

    private Instrumentation inst = null;
    private BaseballCardSQLHelper sqlHelper = null;
    private SQLiteDatabase db = null;
    private List<BaseballCard> allCards = null;
    private BaseballCard card = null;
    private DatabaseUtil dbUtil = null;
    private static final String TAG = BaseballCardSQLHelperTest.class.getName();
}
