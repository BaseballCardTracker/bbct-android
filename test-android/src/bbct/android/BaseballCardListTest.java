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

import android.content.Intent;
import android.os.Bundle;
import android.test.ActivityInstrumentationTestCase2;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import junit.framework.Assert;

/**
 *
 * @author codeguru <codeguru@users.sourceforge.net>
 */
public class BaseballCardListTest extends ActivityInstrumentationTestCase2<BaseballCardList> {

    public BaseballCardListTest() {
        super(BaseballCardList.class);
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
     * Test of onCreate method, of class BaseballCardList.
     */
    public void testOnCreate() {
        System.out.println("onCreate");
        Bundle savedInstanceState = null;
        BaseballCardList instance = new BaseballCardList();
        instance.onCreate(savedInstanceState);
        // TODO review the generated test code and remove the default call to Assert.fail.
        Assert.fail("The test case is a prototype.");
    }

    /**
     * Test of onDestroy method, of class BaseballCardList.
     */
    public void testOnDestroy() {
        System.out.println("onDestroy");
        BaseballCardList instance = new BaseballCardList();
        instance.onDestroy();
        // TODO review the generated test code and remove the default call to Assert.fail.
        Assert.fail("The test case is a prototype.");
    }

    /**
     * Test of onCreateOptionsMenu method, of class BaseballCardList.
     */
    public void testOnCreateOptionsMenu() {
        System.out.println("onCreateOptionsMenu");
        Menu menu = null;
        BaseballCardList instance = new BaseballCardList();
        boolean expResult = false;
        boolean result = instance.onCreateOptionsMenu(menu);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to Assert.fail.
        Assert.fail("The test case is a prototype.");
    }

    /**
     * Test of onPrepareOptionsMenu method, of class BaseballCardList.
     */
    public void testOnPrepareOptionsMenu() {
        System.out.println("onPrepareOptionsMenu");
        Menu menu = null;
        BaseballCardList instance = new BaseballCardList();
        boolean expResult = false;
        boolean result = instance.onPrepareOptionsMenu(menu);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to Assert.fail.
        Assert.fail("The test case is a prototype.");
    }

    /**
     * Test of onOptionsItemSelected method, of class BaseballCardList.
     */
    public void testOnOptionsItemSelected() {
        System.out.println("onOptionsItemSelected");
        MenuItem item = null;
        BaseballCardList instance = new BaseballCardList();
        boolean expResult = false;
        boolean result = instance.onOptionsItemSelected(item);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to Assert.fail.
        Assert.fail("The test case is a prototype.");
    }

    /**
     * Test of onSaveInstanceState method, of class BaseballCardList.
     */
    public void testOnSaveInstanceState() {
        System.out.println("onSaveInstanceState");
        Bundle outState = null;
        BaseballCardList instance = new BaseballCardList();
        instance.onSaveInstanceState(outState);
        // TODO review the generated test code and remove the default call to Assert.fail.
        Assert.fail("The test case is a prototype.");
    }

    /**
     * Test of onListItemClick method, of class BaseballCardList.
     */
    public void testOnListItemClick() {
        System.out.println("onListItemClick");
        ListView l = null;
        View v = null;
        int position = 0;
        long id = 0L;
        BaseballCardList instance = new BaseballCardList();
        instance.onListItemClick(l, v, position, id);
        // TODO review the generated test code and remove the default call to Assert.fail.
        Assert.fail("The test case is a prototype.");
    }

    /**
     * Test of onActivityResult method, of class BaseballCardList.
     */
    public void testOnActivityResult() {
        System.out.println("onActivityResult");
        int requestCode = 0;
        int resultCode = 0;
        Intent data = null;
        BaseballCardList instance = new BaseballCardList();
        instance.onActivityResult(requestCode, resultCode, data);
        // TODO review the generated test code and remove the default call to Assert.fail.
        Assert.fail("The test case is a prototype.");
    }
}
