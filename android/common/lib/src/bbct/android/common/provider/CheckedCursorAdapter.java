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
import android.os.Build;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckedTextView;
import android.widget.SimpleCursorAdapter;
import bbct.android.common.R;

/**
 * This class adds click listeners to {@link CheckedTextView} in
 * {@link SimpleCursorAdapter}. It enables {@link CheckedTextView}
 * to toggle its' state.
 */
public class CheckedCursorAdapter extends SimpleCursorAdapter {

    public CheckedCursorAdapter(Context context, int layout, Cursor c, String[] from, int[] to) {
        super(context, layout, c, from, to);
    }

    /**
     * Binds data from {@link Cursor} to the appropriate {@link View}.
     * Also adds {@link OnClickListener} to {@link CheckedTextView}.
     * 
     * @see {@link SimpleCursorAdapter#bindView}
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        super.bindView(view, context, cursor);

        // set listeners for CheckedTextView
        final CheckedTextView ctv = (CheckedTextView) view.findViewById(R.id.checkmark);
        final Activity curActivity = (Activity) context;
        ctv.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                CheckedTextView cview = (CheckedTextView) v.findViewById(R.id.checkmark);
                cview.toggle();

                // update menu in the correct Activity
                curActivity.runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                            curActivity.invalidateOptionsMenu();
                        }
                    }

                });

            }

        });

    }

}
