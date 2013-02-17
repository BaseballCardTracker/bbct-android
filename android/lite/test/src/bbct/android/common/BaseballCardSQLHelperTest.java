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
package bbct.android.common;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;
import bbct.common.data.BaseballCard;
import junit.framework.Assert;

/**
 *
 * @author codeguru <codeguru@users.sourceforge.net>
 */
public class BaseballCardSQLHelperTest extends AndroidTestCase {

    public BaseballCardSQLHelperTest() {
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
    }

    /**
     * Test of onCreate method, of class BaseballCardSQLHelper.
     */
    public void testOnCreate() {
        System.out.println("onCreate");
        SQLiteDatabase sqld = null;
        BaseballCardSQLHelper instance = null;
        instance.onCreate(sqld);
        // TODO review the generated test code and remove the default call to Assert.fail.
        Assert.fail("The test case is a prototype.");
    }

    /**
     * Test of onUpgrade method, of class BaseballCardSQLHelper.
     */
    public void testOnUpgrade() {
        System.out.println("onUpgrade");
        SQLiteDatabase sqld = null;
        int oldVersion = 0;
        int newVersion = 0;
        BaseballCardSQLHelper instance = null;
        instance.onUpgrade(sqld, oldVersion, newVersion);
        // TODO review the generated test code and remove the default call to Assert.fail.
        Assert.fail("The test case is a prototype.");
    }

    /**
     * Test of insertBaseballCard method, of class BaseballCardSQLHelper.
     */
    public void testInsertBaseballCard() {
        System.out.println("insertBaseballCard");
        BaseballCard card = null;
        BaseballCardSQLHelper instance = null;
        instance.insertBaseballCard(card);
        // TODO review the generated test code and remove the default call to Assert.fail.
        Assert.fail("The test case is a prototype.");
    }

    /**
     * Test of updateBaseballCard method, of class BaseballCardSQLHelper.
     */
    public void testUpdateBaseballCard() {
        System.out.println("updateBaseballCard");
        BaseballCard card = null;
        BaseballCardSQLHelper instance = null;
        instance.updateBaseballCard(card);
        // TODO review the generated test code and remove the default call to Assert.fail.
        Assert.fail("The test case is a prototype.");
    }

    /**
     * Test of getCursor method, of class BaseballCardSQLHelper.
     */
    public void testGetCursor() {
        System.out.println("getCursor");
        BaseballCardSQLHelper instance = null;
        Cursor expResult = null;
        Cursor result = instance.getCursor();
        Assert.assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to Assert.fail.
        Assert.fail("The test case is a prototype.");
    }

    /**
     * Test of clearFilter method, of class BaseballCardSQLHelper.
     */
    public void testClearFilter() {
        System.out.println("clearFilter");
        BaseballCardSQLHelper instance = null;
        instance.clearFilter();
        // TODO review the generated test code and remove the default call to Assert.fail.
        Assert.fail("The test case is a prototype.");
    }

    /**
     * Test of filterCursorByYear method, of class BaseballCardSQLHelper.
     */
    public void testFilterCursorByYear() {
        System.out.println("filterCursorByYear");
        int year = 0;
        BaseballCardSQLHelper instance = null;
        instance.filterCursorByYear(year);
        // TODO review the generated test code and remove the default call to Assert.fail.
        Assert.fail("The test case is a prototype.");
    }

    /**
     * Test of filterCursorByNumber method, of class BaseballCardSQLHelper.
     */
    public void testFilterCursorByNumber() {
        System.out.println("filterCursorByNumber");
        int number = 0;
        BaseballCardSQLHelper instance = null;
        instance.filterCursorByNumber(number);
        // TODO review the generated test code and remove the default call to Assert.fail.
        Assert.fail("The test case is a prototype.");
    }

    /**
     * Test of filterCursorByYearAndNumber method, of class
     * BaseballCardSQLHelper.
     */
    public void testFilterCursorByYearAndNumber() {
        System.out.println("filterCursorByYearAndNumber");
        int year = 0;
        int number = 0;
        BaseballCardSQLHelper instance = null;
        instance.filterCursorByYearAndNumber(year, number);
        // TODO review the generated test code and remove the default call to Assert.fail.
        Assert.fail("The test case is a prototype.");
    }

    /**
     * Test of filterCursorByPlayerName method, of class BaseballCardSQLHelper.
     */
    public void testFilterCursorByPlayerName() {
        System.out.println("filterCursorByPlayerName");
        String playerName = "";
        BaseballCardSQLHelper instance = null;
        instance.filterCursorByPlayerName(playerName);
        // TODO review the generated test code and remove the default call to Assert.fail.
        Assert.fail("The test case is a prototype.");
    }

    /**
     * Test of getBaseballCardFromCursor method, of class BaseballCardSQLHelper.
     */
    public void testGetBaseballCardFromCursor() {
        System.out.println("getBaseballCardFromCursor");
        BaseballCardSQLHelper instance = null;
        BaseballCard expResult = null;
        BaseballCard result = instance.getBaseballCardFromCursor();
        Assert.assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to Assert.fail.
        Assert.fail("The test case is a prototype.");
    }
}
