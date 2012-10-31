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

import android.os.Bundle;
import android.test.ActivityInstrumentationTestCase2;
import bbct.common.data.BaseballCard;
import junit.framework.Assert;

/**
 *
 * @author codeguru <codeguru@users.sourceforge.net>
 */
public class BaseballCardDetailsTest extends ActivityInstrumentationTestCase2<BaseballCardDetails> {

    public BaseballCardDetailsTest() {
        super(BaseballCardDetails.class);
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
     * Test of onCreate method, of class BaseballCardDetails.
     */
    public void testOnCreate() {
        System.out.println("onCreate");
        Bundle savedInstanceState = null;
        BaseballCardDetails instance = new BaseballCardDetails();
        instance.onCreate(savedInstanceState);
        // TODO review the generated test code and remove the default call to Assert.fail.
        Assert.fail("The test case is a prototype.");
    }

    /**
     * Test of onDestroy method, of class BaseballCardDetails.
     */
    public void testOnDestroy() {
        System.out.println("onDestroy");
        BaseballCardDetails instance = new BaseballCardDetails();
        instance.onDestroy();
        // TODO review the generated test code and remove the default call to Assert.fail.
        Assert.fail("The test case is a prototype.");
    }

    /**
     * Test of getBaseballCard method, of class BaseballCardDetails.
     */
    public void testGetBaseballCard() throws Exception {
        System.out.println("getBaseballCard");
        BaseballCardDetails instance = new BaseballCardDetails();
        BaseballCard expResult = null;
        BaseballCard result = instance.getBaseballCard();
        Assert.assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to Assert.fail.
        Assert.fail("The test case is a prototype.");
    }
}
