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
package bbct.android.premium.provider;

import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import bbct.android.common.data.BaseballCard;
import bbct.android.common.provider.BaseballCardContract;
import bbct.android.common.R;
import java.util.List;

/**
 * Overrides
 * {@link bbct.android.common.provider.BaseballCardSQLHelper#onConfigure(SQLiteDatabase)}
 * in order to import data from the Lite edition.
 */
public class PremiumSQLHelper extends
        bbct.android.common.provider.BaseballCardSQLHelper {

    /**
     * Create a {@link PremiumSQLHelper} with the given {@link Context}.
     *
     * @param context
     *            The Android {@link Context} for this {@link PremiumSQLHelper}.
     */
    public PremiumSQLHelper(Context context) {
        super(context);

        Log.d(TAG, "ctor");

        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        super.onCreate(db);

        Log.d(TAG, "onCreate()");

        if (this.isLiteInstalled()) {
            ContentResolver resolver = this.context.getContentResolver();
            Cursor results = resolver.query(BaseballCardContract.LITE_URI,
                    BaseballCardContract.PROJECTION, null, null, null);

            if (results != null) {
                List<BaseballCard> cards = BaseballCardContract
                        .getAllBaseballCardsFromCursor(results);
                this.insertAllBaseballCards(db, cards);
            } else {
                String errorMessage = this.context
                        .getString(R.string.import_error);
                throw new SQLException(errorMessage);
            }
        }

    }

    private boolean isLiteInstalled() {
        try {
            PackageInfo liteInfo = this.context
                    .getPackageManager()
                    .getPackageInfo(LITE_PACKAGE, PackageManager.GET_ACTIVITIES);
            if (liteInfo.versionCode < MIN_LITE_VERSION) {
                String errorMessage = this.context
                        .getString(R.string.lite_update_message);
                throw new SQLException(errorMessage);
            }

            return true;
        } catch (NameNotFoundException ex) {
            Log.i(TAG, LITE_PACKAGE + " package not found", ex);
            return false;
        }
    }

    /**
     * Insert data for multiple baseball cards into a SQLite database.
     *
     * @param cards
     *            The list of cards to insert into the database.
     */
    private void insertAllBaseballCards(SQLiteDatabase db,
                                       List<BaseballCard> cards) {
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

    private static final String TAG = PremiumSQLHelper.class.getName();
    private static final String LITE_PACKAGE = "bbct.android";
    private static final int MIN_LITE_VERSION = 3;
    private Context context = null;
}
