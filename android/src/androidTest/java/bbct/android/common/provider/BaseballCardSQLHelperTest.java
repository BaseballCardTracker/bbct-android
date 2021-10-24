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
package bbct.android.common.provider;

import android.app.Instrumentation;
import android.database.sqlite.SQLiteDatabase;
import android.test.InstrumentationTestCase;
import android.util.Log;

import org.junit.Assert;

import java.io.InputStream;

import bbct.android.common.test.BBCTTestUtil;
import bbct.android.common.test.BaseballCardCsvFileReader;
import bbct.android.common.test.DatabaseUtil;

public class BaseballCardSQLHelperTest extends InstrumentationTestCase {

    @Override
    public void setUp() throws Exception {
        Log.d(TAG, this.getName());

        super.setUp();

        this.inst = this.getInstrumentation();
        this.sqlHelper = new BaseballCardSQLHelper(this.inst.getTargetContext());
        this.db = this.sqlHelper.getWritableDatabase();
        this.dbUtil = new DatabaseUtil(this.inst.getTargetContext());

        InputStream input = this.inst.getContext().getAssets().open(CARD_DATA);
        BaseballCardCsvFileReader reader = new BaseballCardCsvFileReader(input,
                true);
        reader.close();
    }

    @Override
    public void tearDown() throws Exception {
        this.dbUtil.clearDatabase();

        super.tearDown();
    }

    public void testOnCreate() {
        Assert.assertNotNull(this.db);
        BBCTTestUtil.assertDatabaseCreated(this.inst.getTargetContext());
        Assert.assertTrue(this.dbUtil.isEmpty());
    }

    public void testOnUpgrade() {
        int oldVersion = 0;
        int newVersion = 1;
        this.sqlHelper.onUpgrade(this.dbUtil.getDatabase(), oldVersion,
                newVersion);
        Assert.fail("Check that the database is not modified.");
    }

    private Instrumentation inst = null;
    private BaseballCardSQLHelper sqlHelper = null;
    private SQLiteDatabase db = null;
    private DatabaseUtil dbUtil = null;

    private static final String CARD_DATA = "cards.csv";
    private static final String TAG = BaseballCardSQLHelperTest.class.getName();
}
