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
package bbct.android.common.provider;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import bbct.android.common.data.BaseballCard;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO: Write JUnit tests.
 *
 * @author codeguru <codeguru@users.sourceforge.net>
 */
public class BaseballCardSQLHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "bbct.db";
    public static final int SCHEMA_VERSION = 3;
    public static final int ORIGINAL_SCHEMA = 1;
    public static final int BAD_TEAM_SCHEMA = 2;
    public static final int TEAM_SCHEMA = 3;

    public BaseballCardSQLHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA_VERSION);

        Log.d(TAG, "ctor");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate()");

        String sqlCreate = "CREATE TABLE IF NOT EXISTS " + BaseballCardContract.TABLE_NAME + "("
                + BaseballCardContract.ID_COL_NAME + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + BaseballCardContract.BRAND_COL_NAME + " VARCHAR(10), "
                + BaseballCardContract.YEAR_COL_NAME + " INTEGER, "
                + BaseballCardContract.NUMBER_COL_NAME + " INTEGER, "
                + BaseballCardContract.VALUE_COL_NAME + " INTEGER, "
                + BaseballCardContract.COUNT_COL_NAME + " INTEGER, "
                + BaseballCardContract.PLAYER_NAME_COL_NAME + " VARCHAR(50), "
                + BaseballCardContract.TEAM_COL_NAME + " VARCHAR(50), "
                + BaseballCardContract.PLAYER_POSITION_COL_NAME + " VARCHAR(20),"
                + "UNIQUE (" + BaseballCardContract.BRAND_COL_NAME + ", " + BaseballCardContract.YEAR_COL_NAME + ", " + BaseballCardContract.NUMBER_COL_NAME + "))";

        db.execSQL(sqlCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if ((oldVersion == ORIGINAL_SCHEMA || oldVersion == BAD_TEAM_SCHEMA)
                && newVersion == TEAM_SCHEMA) {
            String sqlUpgrade = "ALTER TABLE " + BaseballCardContract.TABLE_NAME
                    + " ADD COLUMN " + BaseballCardContract.TEAM_COL_NAME + " VARCHAR(50)";
            db.execSQL(sqlUpgrade);
        }
    }

    /**
     * Insert baseball card data into a SQLite Database.
     *
     * @param card The baseball card data to insert into the database.
     * @return The row ID of the newly inserted row, or -1 if an error occurred.
     */
    public long insertBaseballCard(BaseballCard card) {
        return this.getWritableDatabase().insert(BaseballCardContract.TABLE_NAME, null, BaseballCardContract.getContentValues(card));
    }

    public void insertAllBaseballCards(SQLiteDatabase db, List<BaseballCard> cards) {
        db.beginTransactionNonExclusive();
        try {
            for (BaseballCard card : cards) {
                db.insert(BaseballCardContract.TABLE_NAME, null, BaseballCardContract.getContentValues(card));
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public int updateBaseballCard(BaseballCard oldCard, BaseballCard newCard) {
        String[] args = {oldCard.getBrand(), Integer.toString(oldCard.getYear()), Integer.toString(oldCard.getNumber())};
        String where = BaseballCardContract.BRAND_COL_NAME + "=? AND " + BaseballCardContract.YEAR_COL_NAME + "=? AND " + BaseballCardContract.NUMBER_COL_NAME + "=?";

        return this.getWritableDatabase().update(BaseballCardContract.TABLE_NAME, BaseballCardContract.getContentValues(newCard), where, args);
    }

    public Cursor getCursor() {
        Log.d(TAG, "getCursor()");

        if (this.currCursor == null) {
            this.clearFilter();
        }

        return this.currCursor;
    }

    public void clearFilter() {
        Log.d(TAG, "clearFilter()");

        this.currCursor = this.getWritableDatabase().query(BaseballCardContract.TABLE_NAME, null, null, null, null, null, null);
    }

    public void filterCursorByYear(int year) {
        String filter = BaseballCardContract.YEAR_COL_NAME + " = ?";
        String[] args = {Integer.toString(year)};
        this.currCursor = this.getWritableDatabase().query(BaseballCardContract.TABLE_NAME, null, filter, args, null, null, null);
    }

    public void filterCursorByNumber(int number) {
        String filter = BaseballCardContract.NUMBER_COL_NAME + " = ?";
        String[] args = {Integer.toString(number)};
        this.currCursor = this.getWritableDatabase().query(BaseballCardContract.TABLE_NAME, null, filter, args, null, null, null);
    }

    public void filterCursorByYearAndNumber(int year, int number) {
        String filter = BaseballCardContract.YEAR_COL_NAME + " = ?  AND " + BaseballCardContract.NUMBER_COL_NAME + " = ?";
        String[] args = {Integer.toString(year), Integer.toString(number)};
        this.currCursor = this.getWritableDatabase().query(BaseballCardContract.TABLE_NAME, null, filter, args, null, null, null);
    }

    public void filterCursorByPlayerName(String playerName) {
        // TODO: Document wild cards in user manual?
        String filter = BaseballCardContract.PLAYER_NAME_COL_NAME + " LIKE ?";
        String[] args = {playerName};
        this.currCursor = this.getWritableDatabase().query(BaseballCardContract.TABLE_NAME, null, filter, args, null, null, null);
    }

    public void filterCursorByTeam(String team) {
        // TODO: Document wild cards in user manual?
        String filter = BaseballCardContract.TEAM_COL_NAME + " LIKE ?";
        String[] args = {team};
        this.currCursor = this.getWritableDatabase().query(BaseballCardContract.TABLE_NAME, null, filter, args, null, null, null);
    }

    public BaseballCard getBaseballCardFromCursor() {
        return this.getBaseballCardFromCursor(this.currCursor);
    }

    public BaseballCard getBaseballCardFromCursor(Cursor cursor) {
        String brand = cursor.getString(cursor.getColumnIndex(BaseballCardContract.BRAND_COL_NAME));
        int year = cursor.getInt(cursor.getColumnIndex(BaseballCardContract.YEAR_COL_NAME));
        int number = cursor.getInt(cursor.getColumnIndex(BaseballCardContract.NUMBER_COL_NAME));
        int value = cursor.getInt(cursor.getColumnIndex(BaseballCardContract.VALUE_COL_NAME));
        int count = cursor.getInt(cursor.getColumnIndex(BaseballCardContract.COUNT_COL_NAME));
        String name = cursor.getString(cursor.getColumnIndex(BaseballCardContract.PLAYER_NAME_COL_NAME));
        String team = cursor.getString(cursor.getColumnIndex(BaseballCardContract.TEAM_COL_NAME));
        String position = cursor.getString(cursor.getColumnIndex(BaseballCardContract.PLAYER_POSITION_COL_NAME));

        return new BaseballCard(brand, year, number, value, count, name, team, position);
    }

    public List<BaseballCard> getAllBaseballCardsFromCursor(Cursor cursor) {
        Log.d(TAG, "getAllBaseballCardsFromCursor()");
        Log.d(TAG, "cursor=" + cursor);

        List<BaseballCard> cards = new ArrayList<BaseballCard>();

        while (cursor.moveToNext()) {
            BaseballCard card = this.getBaseballCardFromCursor(cursor);
            cards.add(card);
        }

        return cards;
    }

    public Cursor getDistinctValues(String colName, String constraint) {
        String[] cols = {BaseballCardContract.ID_COL_NAME, colName};
        String filter = (constraint == null) ? null : colName + " LIKE ?";
        String[] args = {constraint.trim() + '%'};

        return this.getWritableDatabase().query(BaseballCardContract.TABLE_NAME, cols, filter, args, colName, null, null, null);
    }
    private static final String TAG = BaseballCardSQLHelper.class.getName();
    private Cursor currCursor = null;
}
