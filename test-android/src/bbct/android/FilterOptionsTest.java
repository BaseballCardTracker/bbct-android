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
import junit.framework.Assert;

/**
 *
 * @author codeguru <codeguru@users.sourceforge.net>
 */
public class FilterOptionsTest extends ActivityInstrumentationTestCase2<FilterOptions> {

    public FilterOptionsTest() {
        super(FilterOptions.class);
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
     * Test of onCreate method, of class FilterOptions.
     */
    public void testOnCreate() {
        System.out.println("onCreate");
        Bundle savedInstanceState = null;
        FilterOptions instance = new FilterOptions();
        instance.onCreate(savedInstanceState);
        // TODO review the generated test code and remove the default call to Assert.fail.
        Assert.fail("The test case is a prototype.");
    }

    /**
     * Test of onActivityResult method, of class FilterOptions.
     */
    public void testOnActivityResult() {
        System.out.println("onActivityResult");
        int requestCode = 0;
        int resultCode = 0;
        Intent data = null;
        FilterOptions instance = new FilterOptions();
        instance.onActivityResult(requestCode, resultCode, data);
        // TODO review the generated test code and remove the default call to Assert.fail.
        Assert.fail("The test case is a prototype.");
    }
}
