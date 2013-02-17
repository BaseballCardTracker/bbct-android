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
package bbct.android.premium;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;
import bbct.android.common.BaseballCardContract;
import bbct.common.data.BaseballCard;
import java.util.List;

/**
 *
 * @author codeguru <codeguru@users.sourceforge.net>
 */
public class BaseballCardSQLHelper extends bbct.android.common.BaseballCardSQLHelper {

    public BaseballCardSQLHelper(Context context) {
        super(context);

        Log.d(TAG, "ctor");

        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        super.onCreate(db);

        Log.d(TAG, "onCreate()");

        Toast.makeText(context, R.string.import_message, Toast.LENGTH_LONG).show();

        ContentResolver resolver = this.context.getContentResolver();
        Cursor results = resolver.query(BaseballCardContract.CONTENT_URI, BaseballCardContract.PROJECTION, null, null, null);
        List<BaseballCard> cards = this.getAllBaseballCardsFromCursor(results);
        this.insertAllBaseballCards(db, cards);
    }
    private static final String TAG = BaseballCardSQLHelper.class.getName();
    private Context context = null;
}
