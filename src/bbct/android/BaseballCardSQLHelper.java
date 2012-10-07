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

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import bbct.common.data.BaseballCard;

/**
 * TODO: Write JUnit tests.
 *
 * @author codeguru <codeguru@users.sourceforge.net>
 */
public class BaseballCardSQLHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "bbct.db";
    private static final int SCHEMA_VERSION = 1;
    /**
     * The table name to use in the underlying database.
     */
    public static final String TABLE_NAME = "baseball_cards";
    /**
     * The column name for the primary key.
     */
    public static final String ID_COL_NAME="_id";
    /**
     * The column name for the card brand.
     */
    public static final String BRAND_COL_NAME = "brand";
    /**
     * The column name for the card year.
     */
    public static final String YEAR_COL_NAME = "year";
    /**
     * The column name for the card number.
     */
    public static final String NUMBER_COL_NAME = "number";
    /**
     * The column name for the card value.
     */
    public static final String VALUE_COL_NAME = "value";
    /**
     * The column name for the card count.
     */
    public static final String COUNT_COL_NAME = "card_count";
    /**
     * The column name for the player's name.
     */
    public static final String PLAYER_NAME_COL_NAME = "player_name";
    /**
     * The column name for the player's position.
     */
    public static final String PLAYER_POSITION_COL_NAME = "player_position";
    
    public static final String[] PROJECTION = {
        BRAND_COL_NAME, YEAR_COL_NAME, NUMBER_COL_NAME, VALUE_COL_NAME, COUNT_COL_NAME, PLAYER_NAME_COL_NAME, PLAYER_POSITION_COL_NAME
    };

    public BaseballCardSQLHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqld) {
        String sqlCreate = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                + ID_COL_NAME + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + BRAND_COL_NAME + " VARCHAR(10), "
                + YEAR_COL_NAME + " INTEGER, "
                + NUMBER_COL_NAME + " INTEGER, "
                + VALUE_COL_NAME + " INTEGER, "
                + COUNT_COL_NAME + " INTEGER, "
                + PLAYER_NAME_COL_NAME + " VARCHAR(50), "
                + PLAYER_POSITION_COL_NAME + " VARCHAR(20),"
                + "UNIQUE (" + BRAND_COL_NAME + ", " + YEAR_COL_NAME + ", " + NUMBER_COL_NAME + "))";

        sqld.execSQL(sqlCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqld, int oldVersion, int newVersion) {
        // no-op
    }

    public void insertBaseballCard(BaseballCard card) {
        this.getWritableDatabase().insert(TABLE_NAME, null, this.getContentValues(card));
    }

    public void updateBaseballCard(BaseballCard card) {
        String[] args = {card.getBrand(), Integer.toString(card.getYear()), Integer.toString(card.getNumber())};
        String where = BRAND_COL_NAME + "=? AND " + YEAR_COL_NAME + "=? AND " + NUMBER_COL_NAME + "=?";

        this.getWritableDatabase().update(TABLE_NAME, this.getContentValues(card), where, args);
    }

    public Cursor getCursor() {
        return this.cursor;
    }

    public void unfilterCursor() {
        this.cursor = this.getWritableDatabase().query(TABLE_NAME, null, null, null, null, null, null);
    }

    public void filterCursorByYear(int year) {
        String filter = YEAR_COL_NAME + " = ?";
        String[] args = {Integer.toString(year)};
        this.cursor = this.getWritableDatabase().query(TABLE_NAME, null, filter, args, null, null, null);
    }

    public void filterCursorByNumber(int number) {
        String filter = NUMBER_COL_NAME + " = ?";
        String[] args = {Integer.toString(number)};
        this.cursor = this.getWritableDatabase().query(TABLE_NAME, null, filter, args, null, null, null);
    }

    public void filterCursorByYearAndNumber(int year, int number) {
        String filter = YEAR_COL_NAME + " = ?  AND " + NUMBER_COL_NAME + " = ?";
        String[] args = {Integer.toString(year), Integer.toString(number)};
        this.cursor = this.getWritableDatabase().query(TABLE_NAME, null, filter, args, null, null, null);
    }

    public void filterCursorByPlayerName(String playerName) {
        String filter = PLAYER_NAME_COL_NAME + " = ?";
        String[] args = {playerName};
        this.cursor = this.getWritableDatabase().query(TABLE_NAME, null, filter, args, null, null, null);
    }
    
    public BaseballCard getBaseballCardFromCursor() {
        String brand = this.cursor.getString(this.cursor.getColumnIndex(BRAND_COL_NAME));
        int year = this.cursor.getInt(this.cursor.getColumnIndex(YEAR_COL_NAME));
        int number = this.cursor.getInt(this.cursor.getColumnIndex(NUMBER_COL_NAME));
        int value = this.cursor.getInt(this.cursor.getColumnIndex(VALUE_COL_NAME));
        int count = this.cursor.getInt(this.cursor.getColumnIndex(COUNT_COL_NAME));
        String name = this.cursor.getString(this.cursor.getColumnIndex(PLAYER_NAME_COL_NAME));
        String position = this.cursor.getString(this.cursor.getColumnIndex(PLAYER_POSITION_COL_NAME));
        
        return new BaseballCard(brand, year, number, value, count, name, position);
    }

    private ContentValues getContentValues(BaseballCard card) {
        ContentValues cv = new ContentValues(7);
        cv.put(BRAND_COL_NAME, card.getBrand());
        cv.put(YEAR_COL_NAME, card.getYear());
        cv.put(NUMBER_COL_NAME, card.getNumber());
        cv.put(VALUE_COL_NAME, card.getValue());
        cv.put(COUNT_COL_NAME, card.getCount());
        cv.put(PLAYER_NAME_COL_NAME, card.getPlayerName());
        cv.put(PLAYER_POSITION_COL_NAME, card.getPlayerPosition());

        return cv;
    }
    private Cursor cursor = null;
}
