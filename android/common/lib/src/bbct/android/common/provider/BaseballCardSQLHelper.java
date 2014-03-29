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

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import bbct.android.common.data.BaseballCard;
import java.util.ArrayList;
import java.util.List;

/**
 * Provides helper methods to access the underlying SQLite database.
 *
 * TODO: Write JUnit tests.
 */
public class BaseballCardSQLHelper extends SQLiteOpenHelper {

    /**
     * Name of the SQLite database.
     */
    public static final String DATABASE_NAME = "bbct.db";
    /**
     * Current schema version.
     */
    public static final int SCHEMA_VERSION = 3;
    /**
     * Original schema version.
     */
    public static final int ORIGINAL_SCHEMA = 1;
    /**
     * Schema version when attempting to add the team field. This version was
     * buggy.
     */
    public static final int BAD_TEAM_SCHEMA = 2;
    /**
     * Schema version which correctly added the team field.
     */
    public static final int TEAM_SCHEMA = 3;

    /**
     * Create a new {@link BaseballCardSQLHelper} with the given Android
     * {@link Context}.
     *
     * @param context
     *            The Android {@link Context} for this {@link SQLiteOpenHelper}.
     */
    public BaseballCardSQLHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA_VERSION);

        Log.d(TAG, "ctor");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "onCreate()");

        String sqlCreate = "CREATE TABLE IF NOT EXISTS "
                + BaseballCardContract.TABLE_NAME + "("
                + BaseballCardContract.ID_COL_NAME
                + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + BaseballCardContract.BRAND_COL_NAME + " VARCHAR(10), "
                + BaseballCardContract.YEAR_COL_NAME + " INTEGER, "
                + BaseballCardContract.NUMBER_COL_NAME + " INTEGER, "
                + BaseballCardContract.VALUE_COL_NAME + " INTEGER, "
                + BaseballCardContract.COUNT_COL_NAME + " INTEGER, "
                + BaseballCardContract.PLAYER_NAME_COL_NAME + " VARCHAR(50), "
                + BaseballCardContract.TEAM_COL_NAME + " VARCHAR(50), "
                + BaseballCardContract.PLAYER_POSITION_COL_NAME
                + " VARCHAR(20)," + "UNIQUE ("
                + BaseballCardContract.BRAND_COL_NAME + ", "
                + BaseballCardContract.YEAR_COL_NAME + ", "
                + BaseballCardContract.NUMBER_COL_NAME + "))";

        db.execSQL(sqlCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if ((oldVersion == ORIGINAL_SCHEMA || oldVersion == BAD_TEAM_SCHEMA)
                && newVersion == TEAM_SCHEMA) {
            String sqlUpgrade = "ALTER TABLE "
                    + BaseballCardContract.TABLE_NAME + " ADD COLUMN "
                    + BaseballCardContract.TEAM_COL_NAME + " VARCHAR(50)";
            db.execSQL(sqlUpgrade);
        }
    }

    /**
     * Insert data for multiple baseball cards into a SQLite database.
     *
     * @param cards
     *            The list of cards to insert into the database.
     */
    public void insertAllBaseballCards(List<BaseballCard> cards) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.beginTransaction();
        try {
            for (BaseballCard card : cards) {
                db.insert(BaseballCardContract.TABLE_NAME, null,
                        BaseballCardContract.getContentValues(card));
            }
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    /**
     * Populate a {@link BaseballCard} from the data in the current row of the
     * current {@link Cursor}.
     *
     * @return A {@link BaseballCard} from the data in the current row of the
     *         current {@link Cursor}.
     */
    public BaseballCard getBaseballCardFromCursor() {
        return this.getBaseballCardFromCursor(this.currCursor);
    }

    /**
     * Populate a {@link BaseballCard} from the data in the current row of the
     * given {@link Cursor}.
     *
     * @param cursor
     *            The {@link Cursor} to obtain data from.
     * @return A {@link BaseballCard} from the data in the current row of the
     *         given {@link Cursor}.
     */
    public BaseballCard getBaseballCardFromCursor(Cursor cursor) {
        String brand = cursor.getString(cursor
                .getColumnIndex(BaseballCardContract.BRAND_COL_NAME));
        int year = cursor.getInt(cursor
                .getColumnIndex(BaseballCardContract.YEAR_COL_NAME));
        int number = cursor.getInt(cursor
                .getColumnIndex(BaseballCardContract.NUMBER_COL_NAME));
        int value = cursor.getInt(cursor
                .getColumnIndex(BaseballCardContract.VALUE_COL_NAME));
        int count = cursor.getInt(cursor
                .getColumnIndex(BaseballCardContract.COUNT_COL_NAME));
        String name = cursor.getString(cursor
                .getColumnIndex(BaseballCardContract.PLAYER_NAME_COL_NAME));
        String team = cursor.getString(cursor
                .getColumnIndex(BaseballCardContract.TEAM_COL_NAME));
        String position = cursor.getString(cursor
                .getColumnIndex(BaseballCardContract.PLAYER_POSITION_COL_NAME));

        return new BaseballCard(brand, year, number, value, count, name, team,
                position);
    }

    /**
     * Populate a {@link List} of {@link BaseballCard}s from the data in the
     * given {@link Cursor}.
     *
     * @param cursor
     *            The {@link Cursor} to obtain data from.
     * @return A {@link List} of {@link BaseballCard}s from the data in the
     *         given {@link Cursor}.
     */
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

    private static final String TAG = BaseballCardSQLHelper.class.getName();
    private final Cursor currCursor = null;
}
