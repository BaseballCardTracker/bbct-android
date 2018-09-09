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
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;

import java.util.List;

import bbct.android.common.R;
import bbct.android.common.activity.BaseballCardList;
import bbct.android.common.activity.util.BaseballCardMultiChoiceModeListener;
import bbct.android.common.database.BaseballCard;
import bbct.android.common.view.BaseballCardView;
import butterknife.ButterKnife;

public class BaseballCardAdapter extends ArrayAdapter<BaseballCard> {

    private final FragmentActivity mActivity;
    private BaseballCardList mListFragment;
    private BaseballCardMultiChoiceModeListener mCallback;

    @SuppressWarnings("deprecation")
    public BaseballCardAdapter(Context context, int layout, List<BaseballCard> cards) {
        super(context, layout, cards);

        this.mActivity = (FragmentActivity) context;
    }

    public void setListFragment(BaseballCardList listFragment) {
        mListFragment = listFragment;
    }

    public void setActionModeCallback(BaseballCardMultiChoiceModeListener callback) {
        mCallback = callback;
    }

    @NonNull
    @Override
    public View getView(final int position, View convertView, @NonNull ViewGroup parent) {
        BaseballCardView row = (BaseballCardView) convertView;

        if (row == null) {
            row = new BaseballCardView(mActivity);
        }

        CheckBox ctv = ButterKnife.findById(row, R.id.checkmark);

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

        BaseballCard card = getItem(position);
        row.setBaseballCard(card);

        return row;
    }

    @Override
    public long getItemId(int position) {
        BaseballCard card = getItem(position);
        assert card != null;
        return card._id;
    }
}
