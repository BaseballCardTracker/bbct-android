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
import android.view.KeyEvent;
import android.view.View;
import android.widget.ListView;
import bbct.common.data.BaseballCard;
import java.io.InputStream;
import java.util.List;
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

        this.inst = this.getInstrumentation();

        this.activity = this.getActivity();
        this.list = (ListView) this.activity.findViewById(android.R.id.list);

        InputStream dataInput = this.inst.getContext().getAssets().open(DATA_ASSET);
        this.dbUtil = new DatabaseUtil();
    }

    @Override
    public void tearDown() throws Exception {
        this.activity.finish();
        this.dbUtil.deleteDatabase();

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
    
    public void testStartActivityWithEmptyDatabase() {
        Assert.fail("Check that instructions are raised as a Toast.");
    }

    public void testStateDestroy() {
        Assert.fail("Implement me!");
    }

    public void testStatePause() {
        Assert.fail("Implement me!");
    }

    public void testMenuLayoutNoFilter() {
        this.sendKeys(KeyEvent.KEYCODE_MENU);
        Assert.fail("Now how do I check the contents of the menu?");
    }

    public void testMenuLayoutWithFilter() {
        Assert.fail("Implement me!");
    }
    
    public void testAddCardsMenuItem() {
        this.testMenuItem(R.id.add_menu, BaseballCardDetails.class);
    }
    
    public void testFilterCardsMenuItem() {
        this.testMenuItem(R.id.filter_menu, FilterOptions.class);
    }

    public void testAboutMenuItem() {
        this.testMenuItem(R.id.about_menu, About.class);
    }

    /**
     * Test of {@link BaseballCardList#onListItemClick}.
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
    
    public void testAddCardToEmptyDatabase() {
        Assert.fail("Implement me!");
    }
    
    public void testAddCardToPopulatedDatabase() {
        Assert.fail("Implement me!");
    }
    
    public void testAddMultipleCardsAtOnce() {
        Assert.fail("Implement me!");
    }
    
    public void testAddCardMatchingCurrentFilter() {
        Assert.fail("Implement me!");
    }

    public void testAddCardMatchingNotCurrentFilter() {
        Assert.fail("Implement me!");
    }
    
    public void testAddCardAfterClearFilter() {
        Assert.fail("Implement me!");
    }
    
    public void testYearFilter() {
        Assert.fail("Implement me!");
    }

    public void testNumberFilter() {
        Assert.fail("Implement me!");
    }

    public void testYearAndNumberFilter() {
        Assert.fail("Implement me!");
    }

    public void testPlayerNameFilter() {
        Assert.fail("Implement me!");
    }

    private boolean assertListViewContainsItems(List<BaseballCard> expectedItems) {
        return false;
    }
    
    private void testMenuItem(int id, Class childActivityClass) {
        Instrumentation.ActivityMonitor monitor = new Instrumentation.ActivityMonitor(childActivityClass.getName(), null, false);
        this.inst.addMonitor(monitor);
        
        Assert.assertTrue(this.inst.invokeMenuActionSync(this.activity, id, MENU_FLAGS));
        
        Activity childActivity = this.inst.waitForMonitorWithTimeout(monitor, TIME_OUT);
        
        Assert.assertNotNull(childActivity);
        Assert.assertEquals(childActivityClass, childActivity.getClass());
        
        childActivity.finish();
        Assert.assertTrue(childActivity.isFinishing());
    }
    private Instrumentation inst = null;
    private Activity activity = null;
    private ListView list = null;
    private DatabaseUtil dbUtil = null;
    private static final String DATA_ASSET = "cards.csv";
    private static final int MENU_FLAGS = 0;
    private static final int TIME_OUT = 5 * 1000; // 5 seconds
}
