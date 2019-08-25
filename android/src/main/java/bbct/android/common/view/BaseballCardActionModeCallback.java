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
package bbct.android.common.view;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Objects;

import bbct.android.common.R;
import bbct.android.common.activity.BaseballCardList;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class BaseballCardActionModeCallback implements ActionMode.Callback {

    private final BaseballCardList listFragment;
    private ActionMode actionMode;
    private boolean isStarted;

    public BaseballCardActionModeCallback(BaseballCardList listFragment) {
        this.listFragment = listFragment;
    }

    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        Activity activity = Objects.requireNonNull(listFragment.getActivity());
        activity.getMenuInflater().inflate(R.menu.context, menu);
        actionMode = mode;
        isStarted = true;
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
                listFragment.deleteSelectedCards();
                actionMode.finish();
                return true;
            case R.id.select_all_menu:
                listFragment.setAllSelected(true);
                return true;
        }

        return false;
    }

    @Override
    public void onDestroyActionMode(ActionMode mode) {
        isStarted = false;
        actionMode = null;
    }

    public boolean isStarted() {
        return isStarted;
    }

    public void finish() {
        actionMode.finish();
    }
}
