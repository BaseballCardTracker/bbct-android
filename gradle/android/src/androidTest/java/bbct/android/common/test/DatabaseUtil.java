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
package bbct.android.common.test;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import bbct.android.common.data.BaseballCard;
import bbct.android.common.provider.BaseballCardContract;
import bbct.android.common.provider.BaseballCardSQLHelper;
import java.util.List;

/**
 * Utility class for accessing a SQLite database during tests.
 */
public class DatabaseUtil {

    /**
     * Create a {@link DatabaseUtil} object for the given Android package.
     *
     * @param packageName Name of the Android package being tested.
     */
    public DatabaseUtil(Context context) {
        String sqlCreate = "CREATE TABLE IF NOT EXISTS " + BaseballCardContract.TABLE_NAME + "("
                + BaseballCardContract.ID_COL_NAME + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + BaseballCardContract.BRAND_COL_NAME + " TEXT, "
                + BaseballCardContract.YEAR_COL_NAME + " INTEGER, "
                + BaseballCardContract.NUMBER_COL_NAME + " INTEGER, "
                + BaseballCardContract.VALUE_COL_NAME + " INTEGER, "
                + BaseballCardContract.COUNT_COL_NAME + " INTEGER, "
                + BaseballCardContract.PLAYER_NAME_COL_NAME + " TEXT, "
                + BaseballCardContract.TEAM_COL_NAME + " TEXT, "
                + BaseballCardContract.PLAYER_POSITION_COL_NAME + " TEXT,"
                + BaseballCardContract.AUTOGRAPHED_COL_NAME + " INTEGER,"
                + BaseballCardContract.CONDITION_COL_NAME + " TEXT,"
                + "UNIQUE (" + BaseballCardContract.BRAND_COL_NAME + ", " + BaseballCardContract.YEAR_COL_NAME + ", " + BaseballCardContract.NUMBER_COL_NAME + "))";

        this.context = context;
        this.db = this.context.openOrCreateDatabase(DB_NAME, SQLiteDatabase.OPEN_READWRITE, null);
        this.db.execSQL(sqlCreate);
    }

    /**
     * Get the database being tested.
     *
     * @return The database being tested.
     */
    public SQLiteDatabase getDatabase() {
        return this.db;
    }

    /**
     * Delete the database being tested. Should return the app to its inital
     * state after installation.
     */
    public void deleteDatabase() {
        this.db.close();
        this.context.deleteDatabase(DB_NAME);
    }

    /**
     * Delete all rows in the database table.
     */
    public void clearDatabase() {
        this.db.delete(BaseballCardContract.TABLE_NAME, null, null);
        this.db.close();
    }

    /**
     * Insert baseball card data into the database.
     *
     * @param card The baseball card data to insert.
     * @return The row ID of the newly inserted row, or -1 if an error occurred.
     */
    public long insertBaseballCard(BaseballCard card) {
        ContentValues cv = BaseballCardContract.getContentValues(card);

        return this.db.insert(TABLE_NAME, null, cv);
    }

    /**
     * Insert all the baseball card data from the given List.
     *
     * @param cards The list of baseball card data to insert into the database.
     */
    public void populateTable(List<BaseballCard> cards) {
        for (BaseballCard card : cards) {
            this.insertBaseballCard(card);
        }
    }

    /**
     * Check if the database contains the given baseball card.
     *
     * @param card The baseball card data to find.
     * @return <code>true</code> if the baseball card data is
     * found. <code>false</code> otherwise.
     */
    public boolean containsBaseballCard(BaseballCard card) {
        String[] columns = {BaseballCardContract.ID_COL_NAME};
        String selection = BaseballCardContract.BRAND_COL_NAME + " = ?"
                + "   AND " + BaseballCardContract.YEAR_COL_NAME + " = ?"
                + "   AND " + BaseballCardContract.NUMBER_COL_NAME + " = ?"
                + "   AND " + BaseballCardContract.VALUE_COL_NAME + " = ?"
                + "   AND " + BaseballCardContract.COUNT_COL_NAME + " = ?"
                + "   AND " + BaseballCardContract.PLAYER_NAME_COL_NAME + " = ?"
                + "   AND " + BaseballCardContract.PLAYER_POSITION_COL_NAME + " = ?";
        String[] selectionArgs = {card.getBrand(), Integer.toString(card.getYear()), Integer.toString(card.getNumber()),
            Integer.toString(card.getValue()), Integer.toString(card.getCount()), card.getPlayerName(), card.getPlayerPosition()};
        Cursor cursor = this.db.query(TABLE_NAME, columns, selection, selectionArgs, null, null, null);
        boolean result = cursor.getCount() == 1;
        cursor.close();
        return result;
    }

    /**
     * Check if the database contains all of the cards in the given list.
     *
     * @param cards The list of baseball cards to find.
     * @return <code>true</code> if all the baseball card data is
     * found. <code>false</code> otherwise.
     */
    public boolean containsAllBaseballCards(List<BaseballCard> cards) {
        for (BaseballCard card : cards) {
            if (!this.containsBaseballCard(card)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Check that the database is empty.
     *
     * @return <code>true</code> if the database is empty. <code>false</code>
     * otherwise.
     */
    public boolean isEmpty() {
        String[] columns = {BaseballCardContract.ID_COL_NAME};
        Cursor cursor = this.db.query(TABLE_NAME, columns, null, null, null, null, null);
        boolean result = cursor.getCount() == 0;
        cursor.close();
        return result;
    }
    private SQLiteDatabase db = null;
    private Context context = null;
    private static final String DB_NAME = BaseballCardSQLHelper.DATABASE_NAME;
    private static final String TABLE_NAME = BaseballCardContract.TABLE_NAME;
    private static final String TAG = DatabaseUtil.class.getName();
}
