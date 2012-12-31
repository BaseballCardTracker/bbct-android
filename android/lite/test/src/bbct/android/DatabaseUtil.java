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
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import bbct.common.data.BaseballCard;
import java.io.File;
import java.util.List;

/**
 *
 * @author codeguru <codeguru@users.sourceforge.net>
 */
public class DatabaseUtil {

    public DatabaseUtil() {
        this.db = SQLiteDatabase.openDatabase(DB_LOC, null, SQLiteDatabase.OPEN_READWRITE);
    }

    public SQLiteDatabase getDatabase() {
        return this.db;
    }

    public void deleteDatabase() {
        this.db.close();
        SQLiteDatabase.deleteDatabase(new File(DB_LOC));
    }

    public long insertBaseballCard(BaseballCard card) {
        ContentValues cv = new ContentValues(7);
        cv.put(BaseballCardContract.BRAND_COL_NAME, card.getBrand());
        cv.put(BaseballCardContract.YEAR_COL_NAME, card.getYear());
        cv.put(BaseballCardContract.NUMBER_COL_NAME, card.getNumber());
        cv.put(BaseballCardContract.VALUE_COL_NAME, card.getValue());
        cv.put(BaseballCardContract.COUNT_COL_NAME, card.getCount());
        cv.put(BaseballCardContract.PLAYER_NAME_COL_NAME, card.getPlayerName());
        cv.put(BaseballCardContract.PLAYER_POSITION_COL_NAME, card.getPlayerPosition());

        return this.db.insert(TABLE_NAME, null, cv);
    }

    public void populateTable(List<BaseballCard> cards) {
        for (BaseballCard card : cards) {
            this.insertBaseballCard(card);
        }
    }

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
        
        return cursor.getCount() == 1;
    }
    
    public boolean containsAllBaseballCards(List<BaseballCard> cards) {
        for (BaseballCard card : cards) {
            if (!this.containsBaseballCard(card)) {
                return false;
            }
        }
        
        return true;
    }
    private SQLiteDatabase db = null;
    private static final String DB_PATH = "/data/data/bbct.android/databases/";
    private static final String DB_NAME = BaseballCardSQLHelper.DATABASE_NAME;
    private static final String DB_LOC = DB_PATH + DB_NAME;
    private static final String TABLE_NAME = BaseballCardContract.TABLE_NAME;
}
