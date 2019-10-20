/*
 * This file is part of BBCT for Android.
 *
 * Copyright 2014-16 codeguru <codeguru@users.sourceforge.net>
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

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.net.Uri;
import android.test.ProviderTestCase2;

import junit.framework.Assert;

import java.util.ArrayList;
import java.util.List;

import bbct.android.common.provider.BaseballCardContract;
import bbct.android.common.provider.BaseballCardProvider;
import bbct.android.common.provider.BaseballCardSQLHelper;
import bbct.android.common.test.DatabaseUtil;
import bbct.data.BaseballCard;

public abstract class BaseballCardProviderTest<T extends BaseballCardProvider> extends ProviderTestCase2<T> {
    private static final String CREATE_TABLE = "CREATE TABLE baseball_cards"
            + "  (_id INTEGER PRIMARY KEY AUTOINCREMENT,"
            + "    autographed INTEGER, condition       TEXT,"
            + "    brand       TEXT,    year            INTEGER,"
            + "    number      INTEGER, value           INTEGER,"
            + "    card_count  INTEGER, player_name     TEXT,"
            + "    team        TEXT,    player_position TEXT,"
            + "  UNIQUE (brand, year, number));\n";
    private static final String INSERT_DATA = "BEGIN TRANSACTION;\n"
            + "INSERT INTO \"baseball_cards\" VALUES(1,0,'Excellent','Topps',1991,278,10000,1,'Alex Fernandez','White Sox','Pitcher');\n"
            + "INSERT INTO \"baseball_cards\" VALUES(2,0,'Excellent','Topps',1974,175,10000,1,'Bob Stanley','Red Sox','Pitcher');\n"
            + "INSERT INTO \"baseball_cards\" VALUES(3,0,'Excellent','Topps',1985,201,10000,1,'Vince Coleman','Cardinals','Left Field');\n"
            + "INSERT INTO \"baseball_cards\" VALUES(4,0,'Excellent','TMG',1993,5,10000,1,'Ken Griffey Jr.','All-Star','Center Field');\n"
            + "INSERT INTO \"baseball_cards\" VALUES(5,0,'Excellent','Upper Deck',1993,18,10000,1,'Dave Hollins','Phillies','Third Base');\n"
            + "INSERT INTO \"baseball_cards\" VALUES(6,0,'Excellent','Upper Deck',1990,189,10000,1,'Tom Browning','Reds','Pitcher');\n"
            + "INSERT INTO \"baseball_cards\" VALUES(7,0,'Excellent','Topps',1982,121,10000,1,'Ed Lynch','Mets','Pitcher');\n"
            + "COMMIT;\n";
    private static final List<BaseballCard> CARDS = new ArrayList<>();
    static {
        CARDS.add(new BaseballCard(false, "Excellent", "Topps", 1991, "278",
                10000, 1, "Alex Fernandez", "White Sox", "Pitcher"));
        CARDS.add(new BaseballCard(false, "Excellent", "Topps", 1974, "175",
                10000, 1, "Bob Stanley", "Red Sox", "Pitcher"));
        CARDS.add(new BaseballCard(false, "Excellent", "Topps", 1985, "201",
                10000, 1, "Vince Coleman", "Cardinals", "Left Field"));
        CARDS.add(new BaseballCard(false, "Excellent", "TMG", 1993, "5", 10000,
                1, "Ken Griffey Jr.", "All-Star", "Center Field"));
        CARDS.add(new BaseballCard(false, "Excellent", "Upper Deck", 1993, "18",
                10000, 1, "Dave Hollins", "Phillies", "Third Base"));
        CARDS.add(new BaseballCard(false, "Excellent", "Upper Deck", 1990, "189",
                10000, 1, "Tom Browning", "Reds", "Pitcher"));
        CARDS.add(new BaseballCard(false, "Excellent", "Topps", 1982, "121",
                10000, 1, "Ed Lynch", "Mets", "Pitcher"));
    }

    private ContentResolver resolver = null;

    public BaseballCardProviderTest(Class<T> providerClass) {
        super(providerClass, BaseballCardContract.AUTHORITY);
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
        DatabaseUtils.createDbFromSqlStatements(this.getMockContext(),
                BaseballCardSQLHelper.DATABASE_NAME,
                BaseballCardSQLHelper.SCHEMA_VERSION, CREATE_TABLE
                        + INSERT_DATA);
        this.resolver = this.getMockContentResolver();
    }

    @Override
    public void tearDown() throws Exception {
        this.getMockContext().deleteDatabase(
                BaseballCardSQLHelper.DATABASE_NAME);

        super.tearDown();
    }

    public void testPreConditions() {
        DatabaseUtil dbUtil = new DatabaseUtil(this.getMockContext());
        Assert.assertTrue(dbUtil
                .containsAllBaseballCards(BaseballCardProviderTest.CARDS));
    }

    public void testQueryAll() {
        Cursor cursor = this.resolver.query(BaseballCardContract.CONTENT_URI,
                BaseballCardContract.PROJECTION, null, null, null);
        Assert.assertNotNull(cursor);

        List<BaseballCard> actual = BaseballCardContract
                .getAllBaseballCardsFromCursor(cursor);
        Assert.assertTrue(BaseballCardProviderTest.CARDS.containsAll(actual));
        Assert.assertTrue(actual.containsAll(BaseballCardProviderTest.CARDS));
    }

    public void testQueryId() {
        Uri uri = ContentUris.withAppendedId(BaseballCardContract.CONTENT_URI,
                1);
        Cursor cursor = this.resolver.query(uri,
                BaseballCardContract.PROJECTION, null, null, null);
        Assert.assertNotNull(cursor);
        Assert.assertEquals(1, cursor.getCount());
    }

    public void testInsert() {
        BaseballCard newCard = new BaseballCard(true, "Mint", "Code Guru Apps",
                2013, "1", 25, 1, "Code Guru", "Code Guru Devs", "Pitcher");
        Uri result = this.resolver.insert(BaseballCardContract.CONTENT_URI,
                BaseballCardContract.getContentValues(newCard));
        Assert.assertNotNull(result);
    }

    public void testUpdateValue() {
        ContentValues values = new ContentValues();
        int newValue = 50000;
        values.put(BaseballCardContract.VALUE_COL_NAME, newValue);
        int affected = this.resolver.update(BaseballCardContract.CONTENT_URI,
                values, null, null);
        Assert.assertEquals(CARDS.size(), affected);

        Cursor cursor = this.resolver.query(BaseballCardContract.CONTENT_URI,
                BaseballCardContract.PROJECTION, null, null, null);

        if (cursor != null) {
            while (!cursor.moveToNext()) {
                int value = cursor.getInt(cursor
                        .getColumnIndex(BaseballCardContract.VALUE_COL_NAME));
                Assert.assertEquals(value, newValue);
            }
            cursor.close();
        }
    }

    public void testDeleteAll() {
        int affected = this.resolver.delete(BaseballCardContract.CONTENT_URI,
                null, null);
        Assert.assertEquals(CARDS.size(), affected);
    }

    public void testGetTypeItem() {
        String expected = BaseballCardContract.BASEBALL_CARD_ITEM_MIME_TYPE;
        String actual = this.resolver.getType(Uri.withAppendedPath(
                BaseballCardContract.CONTENT_URI, "/1"));
        Assert.assertEquals(expected, actual);
    }

    public void testGetTypeList() {
        String expected = BaseballCardContract.BASEBALL_CARD_LIST_MIME_TYPE;
        String actual = this.resolver.getType(BaseballCardContract.CONTENT_URI);
        Assert.assertEquals(expected, actual);
    }

}
