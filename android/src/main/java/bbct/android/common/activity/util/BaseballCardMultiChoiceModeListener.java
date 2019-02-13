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
package bbct.android.common.activity.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.ListView;

import java.util.Objects;

import bbct.android.common.R;
import bbct.android.common.activity.BaseballCardList;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class BaseballCardMultiChoiceModeListener implements AbsListView.MultiChoiceModeListener {

    private final BaseballCardList mListFragment;

    private ActionMode mMode;

    private boolean mStarted;

    public BaseballCardMultiChoiceModeListener(BaseballCardList listFragment) {
        mListFragment = listFragment;
    }

    @Override
    public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
        if (position != 0) {
            ListView listView = mListFragment.getListView();

            boolean selectAllChecked = listView.isItemChecked(0);
            // Subtract 1 for header view, if it is checked
            int checkedCount = listView.getCheckedItemCount() - (selectAllChecked ? 1 : 0);
            int itemCount = listView.getAdapter().getCount() - 1;

            if (checkedCount == itemCount) {
                listView.setItemChecked(0, true);
            } else {
                listView.setItemChecked(0, false);
            }
        }
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        Activity activity = Objects.requireNonNull(mListFragment.getActivity());
        activity.getMenuInflater().inflate(R.menu.context, menu);
        mMode = mode;
        mStarted = true;

        return true;
    }

    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    @Override
    public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete_menu:
                mListFragment.deleteSelectedCards();
                mMode.finish();
                return true;
        }

        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        mStarted = false;
        mMode = null;
    }

    public boolean isStarted() {
        return mStarted;
    }

    public void finish() {
        mMode.finish();
    }
}
