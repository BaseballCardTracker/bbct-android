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
package bbct.android.common.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;
import bbct.android.common.R;
import bbct.android.common.exception.SQLHelperCreationException;

/**
 *
 * @author codeguru <codeguru@users.sourceforge.net>
 */
public class BaseballCardProvider extends ContentProvider {

    @Override
    public boolean onCreate() {
        Log.d(TAG, "onCreate()");

        try {
            this.sqlHelper = SQLHelperFactory.getSQLHelper(this.getContext());

            return true;
        } catch (SQLHelperCreationException ex) {
            // TODO Show a dialog and exit app
            Toast.makeText(this.getContext(), R.string.database_error, Toast.LENGTH_LONG).show();
            Log.e(TAG, ex.getMessage(), ex);
            return false;
        }
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        Log.d(TAG, "query()");

        if (uri.equals(BaseballCardContract.CONTENT_URI)) {
            return this.sqlHelper.getReadableDatabase().query(BaseballCardContract.TABLE_NAME, projection, selection, selectionArgs, sortOrder, null, null);
        } else {
            String errorFormat = this.getContext().getString(R.string.invalid_uri_error);
            String error = String.format(errorFormat, uri.toString());
            throw new IllegalArgumentException(error);
        }
    }

    @Override
    public String getType(Uri uri) {
        if (uri.equals(BaseballCardContract.CONTENT_URI)) {
            return BaseballCardContract.BASEBALL_CARD_LIST_MIME_TYPE;
        }

        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        if (uri.equals(BaseballCardContract.CONTENT_URI)) {
            long row = this.sqlHelper.getWritableDatabase().insert(BaseballCardContract.TABLE_NAME, null, values);

            if (row != -1) {
                return uri.buildUpon().appendPath(Long.toString(row)).build();
            } else {
                return null;
            }
        }

        return null;
    }

    @Override
    public int delete(Uri uri, String string, String[] strings) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    @Override
    public int update(Uri uri, ContentValues cv, String string, String[] strings) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
    private BaseballCardSQLHelper sqlHelper = null;
    private static final String TAG = BaseballCardProvider.class.getName();
}
