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
package bbct.android.common.database;

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
 *
 * @author codeguru <codeguru@users.sourceforge.net>
 */
public class SingleColumnCursorAdapter extends CursorAdapter {

    public SingleColumnCursorAdapter(Activity activity, String colName) {
        super(activity, null, true);

        try {
            this.activity = activity;
            this.colName = colName;
            this.sqlHelper = SQLHelperFactory.getSQLHelper(activity);
        } catch (SQLHelperCreationException ex) {
            // TODO Show a dialog and exit app
            Toast.makeText(activity, R.string.database_error, Toast.LENGTH_LONG).show();
            Log.e(TAG, ex.getMessage(), ex);
        }
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(context);
        return inflater.inflate(android.R.layout.simple_dropdown_item_1line, parent, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ((TextView) view).setText(this.convertToString(cursor));
    }

    @Override
    public CharSequence convertToString(Cursor cursor) {
        return cursor.getString(cursor.getColumnIndexOrThrow(this.colName));
    }

    @Override
    public Cursor runQueryOnBackgroundThread(CharSequence constraint) {
        Cursor cursor = this.sqlHelper.getDistinctValues(this.colName, constraint == null ? null : constraint.toString());
        this.activity.startManagingCursor(cursor);

        return cursor;
    }
    private String colName = null;
    private BaseballCardSQLHelper sqlHelper = null;
    private Activity activity = null;
    private String TAG = SingleColumnCursorAdapter.class.getName();
}
