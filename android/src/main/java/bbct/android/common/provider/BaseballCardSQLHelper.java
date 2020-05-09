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
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

// TODO: Write JUnit tests.
public class BaseballCardSQLHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "bbct.db";

    /**
     * Current schema version.
     */
    public static final int SCHEMA_VERSION = 4;
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
     * Schema version which correctly added the team field.N
     */
    public static final int TEAM_SCHEMA = 3;

    /**
     * Schema version which adds Autographed and Condition fields.
     */
    public static final int AUTO_AND_CONDITION_SCHEMA = 4;

    public static final int ROOM_SCHEMA = 5;

    public static final int ALPHA_NUMERIC_SCHEMA = 6;

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
                + BaseballCardContract.BRAND_COL_NAME + " TEXT, "
                + BaseballCardContract.YEAR_COL_NAME + " INTEGER, "
                + BaseballCardContract.NUMBER_COL_NAME + " TEXT, "
                + BaseballCardContract.VALUE_COL_NAME + " INTEGER, "
                + BaseballCardContract.COUNT_COL_NAME + " INTEGER, "
                + BaseballCardContract.PLAYER_NAME_COL_NAME + " TEXT, "
                + BaseballCardContract.TEAM_COL_NAME + " TEXT, "
                + BaseballCardContract.PLAYER_POSITION_COL_NAME + " TEXT,"
                + BaseballCardContract.AUTOGRAPHED_COL_NAME + " INTEGER,"
                + BaseballCardContract.CONDITION_COL_NAME + " TEXT,"
                + "UNIQUE (" + BaseballCardContract.BRAND_COL_NAME + ", "
                + BaseballCardContract.YEAR_COL_NAME + ", "
                + BaseballCardContract.NUMBER_COL_NAME + "))";

        db.execSQL(sqlCreate);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < TEAM_SCHEMA) {
            String sqlUpgrade = "ALTER TABLE "
                    + BaseballCardContract.TABLE_NAME + " ADD COLUMN "
                    + BaseballCardContract.TEAM_COL_NAME + " VARCHAR(50)";
            db.execSQL(sqlUpgrade);
        }

        if (oldVersion < AUTO_AND_CONDITION_SCHEMA) {
            String addAutographed = "ALTER TABLE "
                    + BaseballCardContract.TABLE_NAME + " ADD COLUMN "
                    + BaseballCardContract.AUTOGRAPHED_COL_NAME + " INTEGER;";
            String addCondition = "ALTER TABLE "
                    + BaseballCardContract.TABLE_NAME + " ADD COLUMN "
                    + BaseballCardContract.CONDITION_COL_NAME + " TEXT";
            db.execSQL(addAutographed);
            db.execSQL(addCondition);
        }
    }

    private static final String TAG = BaseballCardSQLHelper.class.getName();
}
