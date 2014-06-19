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

import android.content.Context;
import android.database.Cursor;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.SimpleCursorAdapter;
import bbct.android.common.R;
import bbct.android.common.data.BaseballCard;

/**
 * This class adds click listeners to {@link CheckedTextView} in
 * {@link SimpleCursorAdapter}. It enables {@link CheckedTextView} to toggle
 * its' state.
 */
public class BaseballCardAdapter extends SimpleCursorAdapter {

    @SuppressWarnings("deprecation")
    public BaseballCardAdapter(Context context, int layout, Cursor c,
            String[] from, int[] to) {
        super(context, layout, c, from, to);
        this.context = context;
    }

    /**
     * Restores selection of {@link CheckedTextView} if there was a previous
     * selection made on the same element. Also adds {@link OnClickListener} to
     * {@link CheckedTextView}.
     *
     * @see {@link SimpleCursorAdapter#getView(int, View, ViewGroup)}
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = super.getView(position, convertView, parent);

        CheckedTextView ctv = (CheckedTextView) v.findViewById(R.id.checkmark);
        final ActionBarActivity curActivity = (ActionBarActivity) this.context;

        // restore selection
        if (this.selection != null) {
            ctv.setChecked(this.selection[position]);
        }

        // set listener
        ctv.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                CheckedTextView cview = (CheckedTextView) v
                        .findViewById(R.id.checkmark);
                BaseballCardAdapter.this.selection[position] = cview
                        .isChecked();

                curActivity.supportInvalidateOptionsMenu();
            }
        });

        return v;
    }

    /**
     * Marks/unmarks all items in the {@link BaseballCardAdapter}.
     *
     * @param check
     *            - a boolean indicating whether all items will be checked
     */
    public void toggleAll(boolean check) {
        for (int i = 0; i < this.selection.length; i++) {
            this.selection[i] = check;
        }
        this.notifyDataSetChanged();
    }

    /**
     * Returns the saved selection object.
     *
     * @return an array of marked items
     */
    public boolean[] getSelection() {
        return this.selection;
    }

    @Override
    public BaseballCard getItem(int index) {
        Cursor cursor = (Cursor) super.getItem(index);
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
        int count = cursor.getInt(cursor
                .getColumnIndex(BaseballCardContract.COUNT_COL_NAME));
        String name = cursor.getString(cursor
                .getColumnIndex(BaseballCardContract.PLAYER_NAME_COL_NAME));
        String team = cursor.getString(cursor
                .getColumnIndex(BaseballCardContract.TEAM_COL_NAME));
        String position = cursor.getString(cursor
                .getColumnIndex(BaseballCardContract.PLAYER_POSITION_COL_NAME));

        return new BaseballCard(autographed, condition, brand, year, number,
                value, count, name, team, position);
    }

    private boolean[] selection;
    private final Context context;
}
