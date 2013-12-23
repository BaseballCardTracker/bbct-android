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

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;
import android.widget.Toast;
import bbct.android.common.R;
import bbct.android.common.exception.SQLHelperCreationException;

/**
 * Provides a {@link CursorAdapter} for a single column from a database.
 */
public class SingleColumnCursorAdapter extends CursorAdapter {

    /**
     * Create a {@link SingleColumnCursorAdapter} for the column with the given
     * name.
     *
     * @param activity
     *            The {@link Activity} responsible for managing the
     *            {@link Cursor} underlying this [@link Adapter}.
     * @param colName
     *            The name of the column to query.
     */
    public SingleColumnCursorAdapter(Activity activity, String colName) {
        super(activity, null, true);

        try {
            this.activity = activity;
            this.colName = colName;
            this.sqlHelper = SQLHelperFactory.getSQLHelper(activity);
        } catch (SQLHelperCreationException ex) {
            // TODO Show a dialog and exit app
            Toast.makeText(activity, R.string.database_error, Toast.LENGTH_LONG)
                    .show();
            Log.e(TAG, ex.getMessage(), ex);
        }
    }

    /**
     * Create a {@link TextView} in which to display an item.
     *
     * @param context
     *            Interface to application's global information.
     * @param cursor
     *            The cursor from which to get the data. The cursor is already
     *            moved to the correct position.
     * @param parent
     *            The parent to which the new view is to be attached.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(android.R.layout.simple_dropdown_item_1line,
                parent, false);
    }

    /**
     * Reuse an existing {@link View} to display an item.
     *
     * @param view
     *            Existing view, returned earlier by {@link newView}.
     * @param context
     *            Interface to application's global information.
     * @param cursor
     *            The cursor from which to get the data. The cursor is already
     *            moved to the correct position.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ((TextView) view).setText(this.convertToString(cursor));
    }

    /**
     * Converts the cursor into a {@link CharSequence} by getting the
     * {@link String} from the single column in the cursor.
     *
     * @param cursor
     *            The cursor to convert to a {@link CharSequence}.
     *
     * @return A string representation of the given cursor.
     */
    @Override
    public CharSequence convertToString(Cursor cursor) {
        return cursor.getString(cursor.getColumnIndexOrThrow(this.colName));
    }

    /**
     * Run a query for the given constraint and return a {@link Cursor} with the
     * results.
     *
     * @param constraint The constraint for the query.
     *
     * @return A cursor with the requested items. May be empty.
     */
    @SuppressWarnings("deprecation")
    @Override
    public Cursor runQueryOnBackgroundThread(CharSequence constraint) {
        Cursor cursor = this.sqlHelper.getDistinctValues(this.colName,
                constraint == null ? null : constraint.toString());
        this.activity.startManagingCursor(cursor);

        return cursor;
    }

    private String colName = null;
    private BaseballCardSQLHelper sqlHelper = null;
    private Activity activity = null;
    private static final String TAG = SingleColumnCursorAdapter.class.getName();
}
