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
package bbct.android.data

import android.content.ContentResolver
import android.content.ContentValues
import android.database.Cursor
import android.net.Uri

object BaseballCardContract {
    const val TABLE_NAME: String = "baseball_cards"

    const val AUTHORITY: String = "bbct.android.common.provider"

    const val LITE_AUTHORITY: String = "bbct.android.lite.provider"

    const val PREMIUM_AUTHORITY: String = "bbct.android.premium.provider"

    val CONTENT_URI: Uri = Uri
        .Builder()
        .scheme("content")
        .authority(AUTHORITY)
        .path(TABLE_NAME)
        .build()

    val LITE_URI: Uri = Uri
        .Builder()
        .scheme("content")
        .authority(LITE_AUTHORITY)
        .path(TABLE_NAME)
        .build()

    val PREMIUM_URI: Uri = Uri
        .Builder()
        .scheme("content")
        .authority(PREMIUM_AUTHORITY)
        .path(TABLE_NAME)
        .build()

    const val BASEBALL_CARD_LIST_MIME_TYPE: String = (ContentResolver.CURSOR_DIR_BASE_TYPE
        + "/baseball_card")

    const val BASEBALL_CARD_ITEM_MIME_TYPE: String = (ContentResolver.CURSOR_ITEM_BASE_TYPE
        + "/baseball_card")

    const val ID_COL_NAME: String = "_id"

    const val AUTOGRAPHED_COL_NAME: String = "autographed"

    const val CONDITION_COL_NAME: String = "condition"

    const val BRAND_COL_NAME: String = "brand"

    const val YEAR_COL_NAME: String = "year"

    const val NUMBER_COL_NAME: String = "number"

    const val VALUE_COL_NAME: String = "value"

    const val COUNT_COL_NAME: String = "card_count"

    const val PLAYER_NAME_COL_NAME: String = "player_name"

    const val TEAM_COL_NAME: String = "team"

    const val PLAYER_POSITION_COL_NAME: String = "player_position"

    val PROJECTION: Array<String> = arrayOf(
        ID_COL_NAME,
        AUTOGRAPHED_COL_NAME,
        CONDITION_COL_NAME,
        BRAND_COL_NAME,
        YEAR_COL_NAME,
        NUMBER_COL_NAME,
        VALUE_COL_NAME,
        COUNT_COL_NAME,
        PLAYER_NAME_COL_NAME,
        TEAM_COL_NAME,
        PLAYER_POSITION_COL_NAME
    )

    const val INT_SELECTION_FORMAT: String = "%s = ?"

    val YEAR_SELECTION: String = String.format(
        INT_SELECTION_FORMAT,
        YEAR_COL_NAME
    )

    val NUMBER_SELECTION: String = String.format(
        INT_SELECTION_FORMAT,
        NUMBER_COL_NAME
    )

    const val STRING_SELECTION_FORMAT: String = "%s LIKE ?"

    val BRAND_SELECTION: Any = String.format(
        STRING_SELECTION_FORMAT,
        BRAND_COL_NAME
    )

    val PLAYER_NAME_SELECTION: String = String.format(
        STRING_SELECTION_FORMAT,
        PLAYER_NAME_COL_NAME
    )

    val TEAM_SELECTION: String = String.format(
        STRING_SELECTION_FORMAT,
        TEAM_COL_NAME
    )

    fun getContentValues(card: BaseballCard): ContentValues {
        val cv = ContentValues(7)
        cv.put(
            AUTOGRAPHED_COL_NAME,
            card.autographed
        )
        cv.put(
            CONDITION_COL_NAME,
            card.condition
        )
        cv.put(
            BRAND_COL_NAME,
            card.brand
        )
        cv.put(
            YEAR_COL_NAME,
            card.year
        )
        cv.put(
            NUMBER_COL_NAME,
            card.number
        )
        cv.put(
            VALUE_COL_NAME,
            card.value
        )
        cv.put(
            COUNT_COL_NAME,
            card.quantity
        )
        cv.put(
            PLAYER_NAME_COL_NAME,
            card.playerName
        )
        cv.put(
            TEAM_COL_NAME,
            card.team
        )
        cv.put(
            PLAYER_POSITION_COL_NAME,
            card.position
        )
        return cv
    }

    fun getUri(packageName: String): Uri {
        if (packageName == "bbct.android") {
            return LITE_URI
        } else if (packageName == "bbct.android.premium") {
            return PREMIUM_URI
        }

        return CONTENT_URI
    }

    fun getBaseballCardFromCursor(cursor: Cursor): BaseballCard {
        val id = cursor.getLong(
            cursor
                .getColumnIndex(ID_COL_NAME)
        )
        val autographed = cursor.getInt(
            cursor
                .getColumnIndex(AUTOGRAPHED_COL_NAME)
        ) != 0
        val condition = cursor.getString(
            cursor
                .getColumnIndex(CONDITION_COL_NAME)
        )
        val brand = cursor.getString(
            cursor
                .getColumnIndex(BRAND_COL_NAME)
        )
        val year = cursor.getInt(
            cursor
                .getColumnIndex(YEAR_COL_NAME)
        )
        val number = cursor.getString(
            cursor
                .getColumnIndex(NUMBER_COL_NAME)
        )
        val value = cursor.getInt(
            cursor
                .getColumnIndex(VALUE_COL_NAME)
        )
        val quantity = cursor.getInt(
            cursor
                .getColumnIndex(COUNT_COL_NAME)
        )
        val name = cursor.getString(
            cursor
                .getColumnIndex(PLAYER_NAME_COL_NAME)
        )
        val team = cursor.getString(
            cursor
                .getColumnIndex(TEAM_COL_NAME)
        )
        val position = cursor.getString(
            cursor
                .getColumnIndex(PLAYER_POSITION_COL_NAME)
        )

        return BaseballCard(
            id,
            autographed,
            condition,
            brand,
            year,
            number,
            value,
            quantity,
            name,
            team,
            position
        )
    }

    fun getAllBaseballCardsFromCursor(cursor: Cursor): List<BaseballCard> {
        val cards: MutableList<BaseballCard> = ArrayList()

        while (cursor.moveToNext()) {
            val card = getBaseballCardFromCursor(cursor)
            cards.add(card)
        }

        return cards
    }
}
