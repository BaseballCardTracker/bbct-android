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

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import bbct.android.common.R;

/**
 * This class adds click listeners to {@link CheckedTextView} in
 * {@link SimpleCursorAdapter}. It enables {@link CheckedTextView}
 * to toggle its' state.
 */
public class BaseballCardAdapter extends SimpleCursorAdapter {

    @SuppressWarnings("deprecation")
    public BaseballCardAdapter(Context context, int layout, Cursor c, String[] from, int[] to) {
        super(context, layout, c, from, to);
        this.context = context;
    }

    /**
     * Restores selection of {@link CheckedTextView} if there
     * was a previous selection made on the same element. Also
     * adds {@link OnClickListener} to {@link CheckedTextView}.
     *
     * @see {@link SimpleCursorAdapater#getView}
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View v = super.getView(position, convertView, parent);

        CheckedTextView ctv = (CheckedTextView) v.findViewById(R.id.checkmark);
        final Activity curActivity = (Activity) this.context;

        // restore selection
        if (this.selection != null) {
            ctv.setChecked(this.selection[position]);
        }

        // set listener
        ctv.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                CheckedTextView cview = (CheckedTextView) v.findViewById(R.id.checkmark);
                cview.toggle();
                BaseballCardAdapter.this.selection[position] = cview.isChecked();

                // update menu in the correct Activity
                curActivity.runOnUiThread(new Runnable() {

                    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
                    @Override
                    public void run() {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                            curActivity.invalidateOptionsMenu();
                        }
                    }

                });

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

        this.updateDataSet();
    }

    /**
     * Returns the saved selection object.
     * @return an array of marked items
     */
    public boolean[] getSelection() {
        return this.selection;
    }

    /**
     * Sets the saved selection object.
     * @param sel - an array of marked items
     */
    public void setSelection(boolean[] sel) {
        this.selection = sel;
        this.updateDataSet();
    }

    /**
     * Notifies {@link BaseballCardAdapter} of changed data
     * and also updates {@link ListView} in the appropriate
     * {@link ListActivity}
     */
    private void updateDataSet() {
        this.notifyDataSetChanged();
        final ListActivity curActivity = (ListActivity) this.context;
        curActivity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                curActivity.getListView().setAdapter(BaseballCardAdapter.this);
            }
        });
    }

    private boolean[] selection;
    private final Context context;
}

