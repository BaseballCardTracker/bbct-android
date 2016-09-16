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

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;
import bbct.android.common.R;
import java.util.Arrays;

/**
 * {@link ContentProvider} for baseball card data.
 */
public abstract class BaseballCardProvider extends ContentProvider {

    protected static final int ALL_CARDS = 1;
    protected static final int CARD_ID = 2;
    protected static final int DISTINCT = 3;

    private Context context;
    private BaseballCardSQLHelper sqlHelper = null;
    private static final String TAG = BaseballCardProvider.class.getName();

    public static final UriMatcher uriMatcher = new UriMatcher(
            UriMatcher.NO_MATCH);

    static {
        uriMatcher.addURI(BaseballCardContract.AUTHORITY,
                BaseballCardContract.TABLE_NAME, ALL_CARDS);
        uriMatcher.addURI(BaseballCardContract.AUTHORITY,
                BaseballCardContract.TABLE_NAME + "/#", CARD_ID);
        uriMatcher.addURI(BaseballCardContract.AUTHORITY,
                BaseballCardContract.TABLE_NAME + "/distinct", DISTINCT);
    }

    @Override
    public boolean onCreate() {
        Log.d(TAG, "onCreate()");

        context = this.getContext();
        this.sqlHelper = this.getSQLHelper(context);

        return true;
    }

    protected BaseballCardSQLHelper getSQLHelper(Context context) {
        return new BaseballCardSQLHelper(context);
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {
        Log.d(TAG, "query()");
        Log.d(TAG, "  uri=" + uri);
        Log.d(TAG, "  projection=" + Arrays.toString(projection));
        Log.d(TAG, "  selection=" + selection);
        Log.d(TAG, "  selectionArgs=" + Arrays.toString(selectionArgs));
        Log.d(TAG, "  sortOrder=" + sortOrder);

        Cursor cursor;
        SQLiteDatabase db = this.sqlHelper.getReadableDatabase();

        switch (uriMatcher.match(uri)) {
            case DISTINCT:
                if (!projection[0].equals(BaseballCardContract.ID_COL_NAME)) {
                    throw new SQLException("First column in the projection must be '_id'");
                }

                // Assume projection[1] == the "distinct" column
                cursor = db.query(true, BaseballCardContract.TABLE_NAME,
                        projection, selection, selectionArgs, projection[1],
                        null, sortOrder, null);
                break;

            case ALL_CARDS:
                cursor = db.query(BaseballCardContract.TABLE_NAME, projection,
                        selection, selectionArgs, null, null, sortOrder, null);
                break;

            case CARD_ID:
                String where = this.getWhereWithId(selection);
                long id = ContentUris.parseId(uri);
                String[] whereArgs = this.getWhereArgsWithId(selectionArgs, id);
                cursor = db.query(BaseballCardContract.TABLE_NAME, projection,
                        where, whereArgs, null, null, sortOrder);
                break;

            default:
                String errorFormat = context.getString(R.string.invalid_uri_error);
                String error = String.format(errorFormat, uri.toString());
                throw new IllegalArgumentException(error);
        }

        cursor.setNotificationUri(context.getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        switch (uriMatcher.match(uri)) {
            case ALL_CARDS:
            case DISTINCT:
                return BaseballCardContract.BASEBALL_CARD_LIST_MIME_TYPE;

            case CARD_ID:
                return BaseballCardContract.BASEBALL_CARD_ITEM_MIME_TYPE;

            default:
                String errorFormat = context.getString(
                        R.string.invalid_uri_error);
                String error = String.format(errorFormat, uri.toString());
                throw new IllegalArgumentException(error);
        }
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        if (uriMatcher.match(uri) != ALL_CARDS) {
            String errorFormat = context.getString(
                    R.string.invalid_uri_error);
            String error = String.format(errorFormat, uri.toString());
            throw new IllegalArgumentException(error);
        }

        long row = this.sqlHelper.getWritableDatabase().insert(
                BaseballCardContract.TABLE_NAME, null, values);

        if (row > 0) {
            Uri newUri = ContentUris.withAppendedId(uri, row);
            context.getContentResolver().notifyChange(newUri, null);
            return newUri;
        }

        throw new SQLException("Failed to insert row into " + uri);
    }

    @Override
    public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
        int affected;
        SQLiteDatabase db = this.sqlHelper.getWritableDatabase();

        switch (uriMatcher.match(uri)) {
            case ALL_CARDS:
                affected = db.delete(BaseballCardContract.TABLE_NAME,
                        selection, selectionArgs);
                break;

            case CARD_ID:
                String where = this.getWhereWithId(selection);
                long id = ContentUris.parseId(uri);
                String[] whereArgs = this.getWhereArgsWithId(selectionArgs, id);
                affected = db.delete(BaseballCardContract.TABLE_NAME, where,
                        whereArgs);
                break;

            default:
                String errorFormat = context.getString(
                        R.string.invalid_uri_error);
                String error = String.format(errorFormat, uri.toString());
                throw new IllegalArgumentException(error);
        }

        context.getContentResolver().notifyChange(uri, null);
        return affected;
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        SQLiteDatabase db = this.sqlHelper.getWritableDatabase();

        int affected;

        switch (uriMatcher.match(uri)) {
            case ALL_CARDS:
                affected = db.update(BaseballCardContract.TABLE_NAME, values,
                        selection, selectionArgs);
                break;

            case CARD_ID:
                String where = this.getWhereWithId(selection);
                long id = ContentUris.parseId(uri);
                String[] whereArgs = this.getWhereArgsWithId(selectionArgs, id);
                affected = db.update(BaseballCardContract.TABLE_NAME, values,
                        where, whereArgs);
                break;

            default:
                String errorFormat = context.getString(
                        R.string.invalid_uri_error);
                String error = String.format(errorFormat, uri.toString());
                throw new IllegalArgumentException(error);
        }

        context.getContentResolver().notifyChange(uri, null);
        return affected;
    }

    private String getWhereWithId(String selection) {
        String idSelection = BaseballCardContract.ID_COL_NAME + " = ?";
        return TextUtils.isEmpty(selection) ? idSelection : idSelection
                + " AND (" + selection + ")";

    }

    private String[] getWhereArgsWithId(String[] selectionArgs, long id) {
        int argCount = selectionArgs == null ? 1 : selectionArgs.length + 1;
        String[] whereArgs = new String[argCount];
        whereArgs[0] = Long.toString(id);

        if (selectionArgs != null) {
            System.arraycopy(selectionArgs, 0, whereArgs, 1, selectionArgs.length);
        }

        return whereArgs;
    }
}
