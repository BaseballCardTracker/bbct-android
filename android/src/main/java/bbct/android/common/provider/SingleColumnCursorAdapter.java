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
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;
import android.widget.CursorAdapter;
import android.widget.SimpleCursorAdapter;

import java.util.Arrays;

/**
 * Provides a {@link CursorAdapter} for a single column from a database.
 */
public class SingleColumnCursorAdapter extends SimpleCursorAdapter {
    private String colName = null;
    private Activity activity = null;
    private final Uri uri;
    private static final String TAG = SingleColumnCursorAdapter.class.getName();

    @SuppressWarnings("deprecation")
    public SingleColumnCursorAdapter(Activity activity, String colName) {
        super(activity, android.R.layout.simple_dropdown_item_1line, null,
                new String[]{colName}, new int[]{android.R.id.text1});

        this.activity = activity;
        this.colName = colName;
        this.uri = BaseballCardContract.getUri(this.activity.getPackageName())
                .buildUpon().appendPath("distinct").build();
    }

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
        if (cursor != null) {
            Log.d(TAG, "    # of rows=" + cursor.getCount());
        }

        return cursor;
    }
}
