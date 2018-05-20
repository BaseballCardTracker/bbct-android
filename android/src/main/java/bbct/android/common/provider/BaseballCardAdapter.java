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

import android.database.Cursor;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

import bbct.android.common.R;
import bbct.android.common.activity.BaseballCardList;
import bbct.android.common.activity.util.BaseballCardMultiChoiceModeListener;
import bbct.android.common.view.BaseballCardView;
import bbct.data.BaseballCard;
import butterknife.ButterKnife;

public class BaseballCardAdapter extends SimpleCursorAdapter {

    private static final String TAG = BaseballCardAdapter.class.getName();

    private final FragmentActivity mActivity;
    private BaseballCardList mListFragment;
    private BaseballCardMultiChoiceModeListener mCallback;

    @SuppressWarnings("deprecation")
    public BaseballCardAdapter(FragmentActivity activity, int layout, Cursor c,
            String[] from, int[] to) {
        super(activity, layout, c, from, to);

        this.mActivity = activity;
    }

    public void setListFragment(BaseballCardList listFragment) {
        mListFragment = listFragment;
    }

    public void setActionModeCallback(BaseballCardMultiChoiceModeListener callback) {
        mCallback = callback;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        Log.d(TAG, "getView");
        View row = convertView;

        if (row == null) {
            row = LayoutInflater.from(mActivity).inflate(R.layout.row, parent, false);
        }

        CheckBox ctv = ButterKnife.findById(row, R.id.checkmark);
        super.getView(position, row, parent);

        // set listener
        ctv.setOnCheckedChangeListener(new OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked && !mCallback.isStarted()) {
                    mActivity.startActionMode(mCallback);
                }

                ListView listView = mListFragment.getListView();
                // Add 1 to compensate for the header view
                listView.setItemChecked(position + 1, isChecked);
            }
        });

        return row;
    }

    @Override
    public BaseballCard getItem(int index) {
        Log.d(TAG, "getItem()");
        Log.d(TAG, "  index=" + index);
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

}
