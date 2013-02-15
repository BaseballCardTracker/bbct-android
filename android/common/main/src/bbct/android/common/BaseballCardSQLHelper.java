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

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import bbct.common.data.BaseballCard;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO: Write JUnit tests.
 *
 * @author codeguru <codeguru@users.sourceforge.net>
 */
public class BaseballCardSQLHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "bbct.db";
    public static final int SCHEMA_VERSION = 1;

    public BaseballCardSQLHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sqlCreate = "CREATE TABLE IF NOT EXISTS " + BaseballCardContract.TABLE_NAME + "("
                + BaseballCardContract.ID_COL_NAME + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + BaseballCardContract.BRAND_COL_NAME + " VARCHAR(10), "
                + BaseballCardContract.YEAR_COL_NAME + " INTEGER, "
                + BaseballCardContract.NUMBER_COL_NAME + " INTEGER, "
                + BaseballCardContract.VALUE_COL_NAME + " INTEGER, "
                + BaseballCardContract.COUNT_COL_NAME + " INTEGER, "
                + BaseballCardContract.PLAYER_NAME_COL_NAME + " VARCHAR(50), "
                + BaseballCardContract.PLAYER_POSITION_COL_NAME + " VARCHAR(20),"
                + "UNIQUE (" + BaseballCardContract.BRAND_COL_NAME + ", " + BaseballCardContract.YEAR_COL_NAME + ", " + BaseballCardContract.NUMBER_COL_NAME + "))";

        db.execSQL(sqlCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // no-op
    }

    public void insertBaseballCard(BaseballCard card) {
        this.getWritableDatabase().insert(BaseballCardContract.TABLE_NAME, null, this.getContentValues(card));
    }

    public void insertAllBaseballCards(List<BaseballCard> cards) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.beginTransactionNonExclusive();
        try {
            for (BaseballCard card : cards) {
                db.insert(BaseballCardContract.TABLE_NAME, null, this.getContentValues(card));
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public void updateBaseballCard(BaseballCard card) {
        String[] args = {card.getBrand(), Integer.toString(card.getYear()), Integer.toString(card.getNumber())};
        String where = BaseballCardContract.BRAND_COL_NAME + "=? AND " + BaseballCardContract.YEAR_COL_NAME + "=? AND " + BaseballCardContract.NUMBER_COL_NAME + "=?";

        this.getWritableDatabase().update(BaseballCardContract.TABLE_NAME, this.getContentValues(card), where, args);
    }

    public Cursor getCursor() {
        if (this.currCursor == null) {
            this.clearFilter();
        }

        return this.currCursor;
    }

    public void clearFilter() {
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
        String position = cursor.getString(cursor.getColumnIndex(BaseballCardContract.PLAYER_POSITION_COL_NAME));

        return new BaseballCard(brand, year, number, value, count, name, position);
    }

    public List<BaseballCard> getAllBaseballCardsFromCursor(Cursor cursor) {
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

        return this.getWritableDatabase().query(true, BaseballCardContract.TABLE_NAME, cols, filter, args, null, null, null, null);
    }

    private ContentValues getContentValues(BaseballCard card) {
        ContentValues cv = new ContentValues(7);
        cv.put(BaseballCardContract.BRAND_COL_NAME, card.getBrand());
        cv.put(BaseballCardContract.YEAR_COL_NAME, card.getYear());
        cv.put(BaseballCardContract.NUMBER_COL_NAME, card.getNumber());
        cv.put(BaseballCardContract.VALUE_COL_NAME, card.getValue());
        cv.put(BaseballCardContract.COUNT_COL_NAME, card.getCount());
        cv.put(BaseballCardContract.PLAYER_NAME_COL_NAME, card.getPlayerName());
        cv.put(BaseballCardContract.PLAYER_POSITION_COL_NAME, card.getPlayerPosition());

        return cv;
    }
    private Cursor currCursor = null;
}
