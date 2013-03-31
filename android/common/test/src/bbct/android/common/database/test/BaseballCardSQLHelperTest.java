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
package bbct.android.common.database.test;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import android.util.Log;
import bbct.android.common.data.BaseballCard;
import bbct.android.common.database.BaseballCardSQLHelper;
import bbct.android.common.test.BBCTTestUtil;
import bbct.android.common.test.DatabaseUtil;
import junit.framework.Assert;

/**
 * Tests for {@link BaseballCardSQLHelper}.
 *
 * @author codeguru <codeguru@users.sourceforge.net>
 */
public class BaseballCardSQLHelperTest extends AndroidTestCase {

    /**
     *
     * @throws Exception If an error occurs while chaining to the super class.
     */
    @Override
    public void setUp() throws Exception {
        Log.d(TAG, this.getName());

        super.setUp();

        this.sqlHelper = new BaseballCardSQLHelper(this.getContext());
        this.db = this.sqlHelper.getWritableDatabase();
        this.dbUtil = new DatabaseUtil(this.getContext().getPackageName());
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
        BBCTTestUtil.assertDatabaseCreated(this.getContext().getPackageName());
        Assert.assertTrue(this.dbUtil.isEmpty());
    }

    /**
     * Test for {@link BaseballCardSQLHelper#onUpgrade}.
     */
    public void testOnUpgrade() {
        int oldVersion = 0;
        int newVersion = 0;
        this.sqlHelper.onUpgrade(this.dbUtil.getDatabase(), oldVersion, newVersion);
        Assert.fail("Check that the database is not modified.");
    }

    /**
     * Test for {@link BaseballCardSQLHelper#insertBaseballCard}.
     */
    public void testInsertBaseballCard() {
        BaseballCard card = null;
        this.sqlHelper.insertBaseballCard(card);
        Assert.assertTrue(this.dbUtil.containsBaseballCard(card));
    }

    /**
     * Test for {@link BaseballCardSQLHelper#updateBaseballCard}.
     */
    public void testUpdateBaseballCard() {
        BaseballCard card = null;
        this.sqlHelper.updateBaseballCard(card);

        Assert.assertTrue(this.dbUtil.containsBaseballCard(card));
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
        System.out.println("clearFilter");
        this.sqlHelper.clearFilter();
        // TODO review the generated test code and remove the default call to Assert.fail.
        Assert.fail("The test case is a prototype.");
    }

    /**
     * Test for {@link BaseballCardSQLHelper#filterCursorByYear}.
     */
    public void testFilterCursorByYear() {
        System.out.println("filterCursorByYear");
        int year = 0;
        this.sqlHelper.filterCursorByYear(year);
        // TODO review the generated test code and remove the default call to Assert.fail.
        Assert.fail("The test case is a prototype.");
    }

    /**
     * Test for {@link BaseballCardSQLHelper#filterCursorByNumber}.
     */
    public void testFilterCursorByNumber() {
        System.out.println("filterCursorByNumber");
        int number = 0;
        this.sqlHelper.filterCursorByNumber(number);
        // TODO review the generated test code and remove the default call to Assert.fail.
        Assert.fail("The test case is a prototype.");
    }

    /**
     * Test for {@link BaseballCardSQLHelper#filterCursorByYearAndNumber}.
     */
    public void testFilterCursorByYearAndNumber() {
        System.out.println("filterCursorByYearAndNumber");
        int year = 0;
        int number = 0;
        this.sqlHelper.filterCursorByYearAndNumber(year, number);
        // TODO review the generated test code and remove the default call to Assert.fail.
        Assert.fail("The test case is a prototype.");
    }

    /**
     * Test for {@link BaseballCardSQLHelper#filterCursorByPlayerName}.
     */
    public void testFilterCursorByPlayerName() {
        System.out.println("filterCursorByPlayerName");
        String playerName = "";
        this.sqlHelper.filterCursorByPlayerName(playerName);
        // TODO review the generated test code and remove the default call to Assert.fail.
        Assert.fail("The test case is a prototype.");
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
    private BaseballCardSQLHelper sqlHelper = null;
    private SQLiteDatabase db = null;
    private DatabaseUtil dbUtil = null;
    private static final String TAG = BaseballCardSQLHelperTest.class.getName();
}
