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
    public static final String NAME_COL_NAME = "player_name";
    /**
     * The column name for the player's position.
     */
    public static final String POSITION_COL_NAME = "player_position";

    public BaseballCardSQLHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqld) {
        String sqlCreate = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
                + BRAND_COL_NAME + " VARCHAR(10), "
                + YEAR_COL_NAME + " INTEGER, "
                + NUMBER_COL_NAME + " INTEGER, "
                + VALUE_COL_NAME + " INTEGER, "
                + COUNT_COL_NAME + " INTEGER, "
                + NAME_COL_NAME + " VARCHAR(50), "
                + POSITION_COL_NAME + " VARCHAR(20),"
                + "PRIMARY KEY (" + BRAND_COL_NAME + ", " + YEAR_COL_NAME + ", " + NUMBER_COL_NAME + "))";

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
        String where = BRAND_COL_NAME + "=?, " + YEAR_COL_NAME + "=?, " + NUMBER_COL_NAME + "=?";

        this.getWritableDatabase().update(TABLE_NAME, this.getContentValues(card), where, args);
    }

    public Cursor getCursor() {
        return this.getWritableDatabase().query(TABLE_NAME, null, null, null, null, null, null);
    }

    public Cursor getCursorFilteredByYear(int year) {
        String filter = YEAR_COL_NAME + "=" + year;
        return this.getWritableDatabase().query(TABLE_NAME, null, filter, null, null, null, null);
    }

    public Cursor getCursorFilterdByNumber(int number) {
        String filter = NUMBER_COL_NAME + "=" + number;
        return this.getWritableDatabase().query(TABLE_NAME, null, filter, null, null, null, null);
    }

    public Cursor getCursorFilteredByYearAndNumber(int year, int number) {
        String filter = YEAR_COL_NAME + "=" + year + "AND" + NUMBER_COL_NAME + "=" + number;
        return this.getWritableDatabase().query(TABLE_NAME, null, filter, null, null, null, null);
    }

    public Cursor getCursorFilterdByPlayerName(String playerName) {
        String filter = NAME_COL_NAME + "=" + playerName;
        return this.getWritableDatabase().query(TABLE_NAME, null, filter, null, null, null, null);
    }

    private ContentValues getContentValues(BaseballCard card) {
        ContentValues cv = new ContentValues(7);
        cv.put(BRAND_COL_NAME, card.getBrand());
        cv.put(YEAR_COL_NAME, card.getYear());
        cv.put(NUMBER_COL_NAME, card.getNumber());
        cv.put(VALUE_COL_NAME, card.getValue());
        cv.put(COUNT_COL_NAME, card.getCount());
        cv.put(NAME_COL_NAME, card.getPlayerName());
        cv.put(POSITION_COL_NAME, card.getPlayerPosition());

        return cv;
    }
}
