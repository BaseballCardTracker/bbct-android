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
import android.os.Build;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AbsListView;
import bbct.android.common.R;
import bbct.android.common.activity.BaseballCardList;

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class BaseballCardActionModeCallback implements AbsListView.MultiChoiceModeListener {

    private final BaseballCardList mListFragment;

    private ActionMode mMode;

    private boolean mStarted;

    public BaseballCardActionModeCallback(BaseballCardList listFragment) {
        mListFragment = listFragment;
    }

    /**
     * Called when an item is checked or unchecked during selection mode.
     *
     * @param mode     The {@link ActionMode} providing the selection mode
     * @param position Adapter position of the item that was checked or unchecked
     * @param id       Adapter ID of the item that was checked or unchecked
     * @param checked  <code>true</code> if the item is now checked, <code>false</code>
     */
    @Override
    public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {

    }

    /**
     * Called when action mode is first created. The menu supplied will be used to
     * generate action buttons for the action mode.
     *
     * @param mode ActionMode being created
     * @param menu Menu used to populate action buttons
     * @return true if the action mode should be created, false if entering this
     * mode should be aborted.
     */
    @Override
    public boolean onCreateActionMode(ActionMode mode, Menu menu) {
        mListFragment.getActivity().getMenuInflater().inflate(R.menu.context, menu);
        mMode = mode;
        mStarted = true;

        return true;
    }

    /**
     * Called to refresh an action mode's action menu whenever it is invalidated.
     *
     * @param mode ActionMode being prepared
     * @param menu Menu used to populate action buttons
     * @return true if the menu or action mode was updated, false otherwise.
     */
    @Override
    public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
        return false;
    }

    /**
     * Called to report a user click on an action button.
     *
     * @param mode The current ActionMode
     * @param item The item that was clicked
     * @return true if this callback handled the event, false if the standard MenuItem
     * invocation should continue.
     */
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

    /**
     * Called when an action mode is about to be exited and destroyed.
     *
     * @param mode The current ActionMode being destroyed
     */
    @Override
    public void onDestroyActionMode(ActionMode mode) {
        mStarted = false;
    }

    public boolean isStarted() {
        return mStarted;
    }

}
