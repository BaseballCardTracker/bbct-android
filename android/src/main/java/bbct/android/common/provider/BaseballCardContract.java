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

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

import bbct.android.common.database.BaseballCard;

public final class BaseballCardContract {

    public static final String TABLE_NAME = "baseball_cards";

    public static final String AUTHORITY = "bbct.android.common.provider";

    public static final String LITE_AUTHORITY = "bbct.android.lite.provider";

    public static final String PREMIUM_AUTHORITY = "bbct.android.premium.provider";

    public static final Uri CONTENT_URI = new Uri.Builder().scheme("content")
            .authority(AUTHORITY).path(TABLE_NAME).build();

    public static final Uri LITE_URI = new Uri.Builder().scheme("content")
            .authority(LITE_AUTHORITY).path(TABLE_NAME).build();

    public static final Uri PREMIUM_URI = new Uri.Builder().scheme("content")
            .authority(PREMIUM_AUTHORITY).path(TABLE_NAME).build();

    public static final String BASEBALL_CARD_LIST_MIME_TYPE = ContentResolver.CURSOR_DIR_BASE_TYPE
            + "/baseball_card";

    public static final String BASEBALL_CARD_ITEM_MIME_TYPE = ContentResolver.CURSOR_ITEM_BASE_TYPE
            + "/baseball_card";

    public static final String ID_COL_NAME = "_id";

    public static final String AUTOGRAPHED_COL_NAME = "autographed";

    public static final String CONDITION_COL_NAME = "condition";

    public static final String BRAND_COL_NAME = "brand";

    public static final String YEAR_COL_NAME = "year";

    public static final String NUMBER_COL_NAME = "number";

    public static final String VALUE_COL_NAME = "value";

    public static final String COUNT_COL_NAME = "card_count";

    public static final String PLAYER_NAME_COL_NAME = "player_name";

    public static final String TEAM_COL_NAME = "team";

    public static final String PLAYER_POSITION_COL_NAME = "player_position";

    public static final String[] PROJECTION = { ID_COL_NAME,
            AUTOGRAPHED_COL_NAME, CONDITION_COL_NAME, BRAND_COL_NAME,
            YEAR_COL_NAME, NUMBER_COL_NAME, VALUE_COL_NAME, COUNT_COL_NAME,
            PLAYER_NAME_COL_NAME, TEAM_COL_NAME, PLAYER_POSITION_COL_NAME };

    public static final String INT_SELECTION_FORMAT = "%s = ?";

    public static final String YEAR_SELECTION = String.format(
            INT_SELECTION_FORMAT, YEAR_COL_NAME);

    public static final String NUMBER_SELECTION = String.format(
            INT_SELECTION_FORMAT, NUMBER_COL_NAME);

    public static final String STRING_SELECTION_FORMAT = "%s LIKE ?";

    public static final Object BRAND_SELECTION = String.format(
            STRING_SELECTION_FORMAT, BRAND_COL_NAME);

    public static final String PLAYER_NAME_SELECTION = String.format(
            STRING_SELECTION_FORMAT, PLAYER_NAME_COL_NAME);

    public static final String TEAM_SELECTION = String.format(
            STRING_SELECTION_FORMAT, TEAM_COL_NAME);

    public static ContentValues getContentValues(BaseballCard card) {
        ContentValues cv = new ContentValues(7);
        cv.put(BaseballCardContract.AUTOGRAPHED_COL_NAME, card.autographed);
        cv.put(BaseballCardContract.CONDITION_COL_NAME, card.condition);
        cv.put(BaseballCardContract.BRAND_COL_NAME, card.brand);
        cv.put(BaseballCardContract.YEAR_COL_NAME, card.year);
        cv.put(BaseballCardContract.NUMBER_COL_NAME, card.number);
        cv.put(BaseballCardContract.VALUE_COL_NAME, card.value);
        cv.put(BaseballCardContract.COUNT_COL_NAME, card.quantity);
        cv.put(BaseballCardContract.PLAYER_NAME_COL_NAME, card.playerName);
        cv.put(BaseballCardContract.TEAM_COL_NAME, card.team);
        cv.put(BaseballCardContract.PLAYER_POSITION_COL_NAME,
                card.position);
        return cv;
    }

    public static Uri getUri(String packageName) {
        if (packageName.equals("bbct.android")) {
            return LITE_URI;
        } else if (packageName.equals("bbct.android.premium")) {
            return PREMIUM_URI;
        }

        return CONTENT_URI;
    }

    public static BaseballCard getBaseballCardFromCursor(Cursor cursor) {
        boolean autographed = cursor.getInt(cursor
                .getColumnIndex(BaseballCardContract.AUTOGRAPHED_COL_NAME)) != 0;
        String condition = cursor.getString(cursor
                .getColumnIndex(BaseballCardContract.CONDITION_COL_NAME));
        String brand = cursor.getString(cursor
                .getColumnIndex(BaseballCardContract.BRAND_COL_NAME));
        int year = cursor.getInt(cursor
                .getColumnIndex(BaseballCardContract.YEAR_COL_NAME));
        int number = cursor.getInt(cursor
                .getColumnIndex(BaseballCardContract.NUMBER_COL_NAME));
        int value = cursor.getInt(cursor
                .getColumnIndex(BaseballCardContract.VALUE_COL_NAME));
        int quantity = cursor.getInt(cursor
                .getColumnIndex(BaseballCardContract.COUNT_COL_NAME));
        String name = cursor.getString(cursor
                .getColumnIndex(BaseballCardContract.PLAYER_NAME_COL_NAME));
        String team = cursor.getString(cursor
                .getColumnIndex(BaseballCardContract.TEAM_COL_NAME));
        String position = cursor.getString(cursor
                .getColumnIndex(BaseballCardContract.PLAYER_POSITION_COL_NAME));

        return new BaseballCard(autographed, condition, brand, year, number,
                value, quantity, name, team, position);
    }

    public static List<BaseballCard> getAllBaseballCardsFromCursor(Cursor cursor) {
        List<BaseballCard> cards = new ArrayList<>();

        while (cursor.moveToNext()) {
            BaseballCard card = BaseballCardContract.getBaseballCardFromCursor(cursor);
            cards.add(card);
        }

        return cards;
    }

}
