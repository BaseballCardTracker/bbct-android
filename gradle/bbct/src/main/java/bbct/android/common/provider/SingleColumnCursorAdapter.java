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

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;
import java.util.Arrays;

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

        this.activity = activity;
        this.colName = colName;
        this.uri = BaseballCardContract.getUri(this.activity.getPackageName())
                .buildUpon().appendPath("distinct").build();
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
     *            Existing view, returned earlier by {@link #newView}.
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
     * @param constraint
     *            The constraint for the query.
     *
     * @return A cursor with the requested items. May be empty.
     */
    @SuppressWarnings("deprecation")
    @Override
    public Cursor runQueryOnBackgroundThread(CharSequence constraint) {
        Log.d(TAG, "runQueryOnBackgroundThread()");
        Log.d(TAG, "  constraint=" + constraint);

        String[] projection = new String[] { BaseballCardContract.ID_COL_NAME,
                this.colName };
        String selection = constraint == null ? null : String.format(
                BaseballCardContract.STRING_SELECTION_FORMAT, this.colName);
        String[] args = constraint == null ? null : new String[] { constraint
                .toString().trim() + '%' };

        Log.d(TAG, "  projection=" + Arrays.toString(projection));
        Log.d(TAG, "  selection=" + selection);
        Log.d(TAG, "  args=" + Arrays.toString(args));

        Cursor cursor = this.activity.getContentResolver().query(this.uri,
                projection, selection, args, null);

        Log.d(TAG, "  cursor=" + cursor);
        Log.d(TAG, "    # of rows=" + cursor.getCount());

        this.activity.startManagingCursor(cursor);

        return cursor;
    }

    private String colName = null;
    private Activity activity = null;
    private final Uri uri;
    private static final String TAG = SingleColumnCursorAdapter.class.getName();
}
