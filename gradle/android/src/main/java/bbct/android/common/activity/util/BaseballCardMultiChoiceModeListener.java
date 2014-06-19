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

import android.content.ContentUris;
import android.content.Context;
import android.net.Uri;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.AbsListView;
import android.widget.HeaderViewListAdapter;
import android.widget.ListView;
import android.widget.Toast;
import bbct.android.common.R;
import bbct.android.common.provider.BaseballCardAdapter;
import bbct.android.common.provider.BaseballCardContract;

public class BaseballCardMultiChoiceModeListener implements AbsListView.MultiChoiceModeListener {

    private Context context;
    private ListView list;
    private BaseballCardAdapter adapter;
    private Uri uri;

    public BaseballCardMultiChoiceModeListener(Context context, ListView list) {
        this.context = context;
        this.list = list;
        this.adapter = (BaseballCardAdapter) ((HeaderViewListAdapter) list.getAdapter())
                .getWrappedAdapter();
        this.uri = BaseballCardContract.getUri(this.context.getPackageName());
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
        MenuInflater inflater = mode.getMenuInflater();
        inflater.inflate(R.menu.context, menu);
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
                long[] ids = this.list.getCheckedItemIds();
                for (long id : ids) {
                    Uri deleteUri = ContentUris.withAppendedId(this.uri, id);
                    this.context.getContentResolver().delete(deleteUri, null, null);
                }

                Toast.makeText(this.context, R.string.card_deleted_message, Toast.LENGTH_LONG)
                        .show();
                return true;

            default:
                return false;
        }
    }

    /**
     * Called when an action mode is about to be exited and destroyed.
     *
     * @param mode The current ActionMode being destroyed
     */
    @Override
    public void onDestroyActionMode(ActionMode mode) {

    }

}
