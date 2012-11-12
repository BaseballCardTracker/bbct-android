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
import android.database.sqlite.SQLiteDatabase;
import bbct.common.data.BaseballCard;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 *
 * @author codeguru <codeguru@users.sourceforge.net>
 */
public class DatabaseUtil {

    public DatabaseUtil(InputStream csvInput, boolean hasColHeaders) throws IOException {
        this.in = new BufferedReader(new InputStreamReader(csvInput));
        this.db = SQLiteDatabase.openDatabase(DB_LOC, null, SQLiteDatabase.OPEN_READWRITE);

        if (hasColHeaders) {
            this.in.readLine(); // Skip headers
        }
    }
    
    public SQLiteDatabase getDatabase() {
        return this.db;
    }
    
    public void deleteDatabase() {
        this.db.close();
        SQLiteDatabase.deleteDatabase(new File(DB_LOC));
    }

    public long insertBaseballCard(BaseballCard card) throws IOException {
        ContentValues cv = new ContentValues(7);
        cv.put(BaseballCardSQLHelper.BRAND_COL_NAME, card.getBrand());
        cv.put(BaseballCardSQLHelper.YEAR_COL_NAME, card.getYear());
        cv.put(BaseballCardSQLHelper.NUMBER_COL_NAME, card.getNumber());
        cv.put(BaseballCardSQLHelper.VALUE_COL_NAME, card.getValue());
        cv.put(BaseballCardSQLHelper.COUNT_COL_NAME, card.getCount());
        cv.put(BaseballCardSQLHelper.PLAYER_NAME_COL_NAME, card.getPlayerName());
        cv.put(BaseballCardSQLHelper.PLAYER_POSITION_COL_NAME, card.getPlayerPosition());

        return this.db.insert(TABLE_NAME, null, cv);
    }

    public BaseballCard getNextBaseballCardFromFile() throws IOException {
        String line = this.in.readLine();
        String[] data = line.split(",");
        String brand = data[0];
        int year = Integer.parseInt(data[1]);
        int number = Integer.parseInt(data[2]);
        int value = 10000;
        int count = 1;
        String playerName = data[3];
        String playerPosition = data[4];

        return new BaseballCard(brand, year, number, value, count, playerName, playerPosition);
    }

    public void populateTable() throws IOException {
        while (this.in.ready()) {
            BaseballCard card = this.getNextBaseballCardFromFile();
            this.insertBaseballCard(card);
        }
    }
    private BufferedReader in = null;
    private SQLiteDatabase db = null;
    private static final String DB_PATH = "/data/data/bbct.android/databases/";
    private static final String DB_NAME = BaseballCardSQLHelper.DATABASE_NAME;
    private static final String DB_LOC = DB_PATH + DB_NAME;
    private static final String TABLE_NAME = BaseballCardSQLHelper.TABLE_NAME;
}
